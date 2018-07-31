package com.chanceit.framework.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.http.action.CarDriverView;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.User;
import com.chanceit.http.pojo.Warn;
import com.chanceit.http.service.IMessageCenterService;
import com.chanceit.http.service.IUserService;
import com.chanceit.http.service.IWarnService;

public class MessageQueueClient implements MessageListener{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String charSet="UTF-8";// �ַ���
	private int connectionTimeOut = 30000;// ���ӳ�ʱʱ��
	private int readTimeOut = 40000;// ��ȡ��ʱʱ��
	
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("messageCenterService")
	private IMessageCenterService msgCenterService;
	
	@Autowired
	@Qualifier("warnService")
	private IWarnService warnService;
	
	@Autowired
	@Qualifier("converter")
	private Converter converter;
	
	@Autowired
	@Qualifier("carDriverView")
	private CarDriverView carDriverView;
	
	private String appid;
	private String nodeJsUrl;
	
	@Override
	public void onMessage(Message message) {
		try {
			String data = new String(message.getBody(), "utf-8");
			logger.info("=======================������Ϣ��==========================");
			JSONObject jsonObj = new JSONObject(data);
			String type = jsonObj.get("msgType").toString();
			if("1".equals(type)){//��Ϣ
				String identifier = jsonObj.get("identifier").toString();
				String msgContent = jsonObj.get("msgBody").toString();
				String time = jsonObj.get("time").toString();
				User user =  userService.getByName(identifier);
				if(user==null){
					logger.info("ʶ�����û�"+identifier+"�����ڴ�ϵͳ��");
				}else{
					com.chanceit.http.pojo.Message msg = new com.chanceit.http.pojo.Message();
					msg.setCar(user);
					msg.setMessageContent(msgContent);
					msg.setCompany(user.getAccount());
					msg.setCreateTime(new Date());//��չ
					//����/������Ϣ��ʶ��  0��ʾ���յ���  1��ʾ���͵���  Ĭ��Ϊ1
					msg.setSendFlag(0);
					//����δ����ʶ�� 0δ��  1�Ѷ�
					msg.setIfread(0);
					
					if(msgCenterService.save(msg)){
						logger.info("������յ���Ϣ��");
					}else{
						logger.error("������յ���Ϣʧ�ܣ�");
					}
				}
			}else if("5".equals(type)){//Ԥ����Ϣ
				logger.info("-�յ�Ԥ��-");
				String companyId = jsonObj.get("companyId").toString();
				String identifier = jsonObj.get("identifier").toString();
				String paras = jsonObj.get("paras").toString();
				int status = Integer.parseInt(jsonObj.get("status").toString());
				Date time = DateUtil.parseDate(jsonObj.get("time").toString(), "yyyy-MM-dd HH:mm:ss");
				User user = userService.getByName(identifier);
				if(user!=null){
					Warn warn = new Warn();
					warn.setUser(user);//
					warn.setCompanyId(Integer.parseInt(companyId.replace("jkxt", "")));//
					warn.setIdentifier(identifier);
					warn.setStatus(status);
					String[] paraAry = paras.split(",");
					
					warn.setNowSpeed(Integer.parseInt(paraAry[0]));
					warn.setEngineSpeed(Integer.parseInt(paraAry[1]));
					warn.setBatteryVoltage(Integer.parseInt(paraAry[2]));
					warn.setCoolantTemp(Integer.parseInt(paraAry[3]));
					warn.setEngineLoad(Integer.parseInt(paraAry[4]));
					warn.setThrottle(Integer.parseInt(paraAry[5]));
					warn.setAirPressure(Integer.parseInt(paraAry[6]));
					warn.setIntakePressure(Integer.parseInt(paraAry[7]));
					warn.setAirFlow(Integer.parseInt(paraAry[8]));
					warn.setAirTemp(Integer.parseInt(paraAry[9]));
					warn.setIntakeTemp(Integer.parseInt(paraAry[10]));
					warn.setObdProtocol(paraAry[11]);
					warn.setObdStandand(paraAry[12]);
					warn.setFuelConsumption(Integer.parseInt(paraAry[13]));
					warn.setCreateTime(new Date());
					warn.setPara(paras);
					warn.setBinaryStatus(toFullBinaryString(status));
					
					warn.setTime(time);
					warn.setIfRead((short)0);
					warn.setReason(getReason(paras, status));
					warn.setDriver(user.getDriver());
					warnService.save(warn);
					
				}else{
					logger.info("���յ�Ԥ����Ϣʶ�����������û�δ֪��");
				}
			}else if("99".equals(type)){//ˢ����Ϣ
				logger.info("-�յ�ˢ����Ϣ-");
				String identifier = jsonObj.get("identifier").toString();
				String companyId =jsonObj.get("companyId").toString().replace("jkxt", ""); 
				String valName = jsonObj.get("valName").toString();
				String cardNo = jsonObj.get("cardNo").toString();
				String bk = jsonObj.get("bk").toString();
				Date swipeTime = DateUtil.parseDate(jsonObj.get("time").toString(), "yyyy-MM-dd HH:mm:ss");
				carDriverView.valSignIn(identifier, cardNo, valName,swipeTime);
				//typeΪ3Ϊˢ������
				noticeNodeJsServer(3,Integer.parseInt(companyId), "-�յ�ˢ����Ϣ-");
			}else if("19".equals(type)){//���Ͷϵ�
				logger.info("-�͵����-");
				String routingKey = message.getMessageProperties().getReceivedRoutingKey();
				String identifier = routingKey.replaceAll("box.status.", "");
				JSONObject json_msg = new JSONObject(jsonObj.get("msgBody").toString());
				int opcode = json_msg.getInt("opCode2");
				int tempInt = json_msg.getInt("tmpInt"); 
				//ͨ��ʶ�����ȡ������Ϣ
				User car = userService.getByName(identifier);
				int op = (opcode&(int)Math.pow(2, 8))/(int)Math.pow(2, 8);
				if(car != null){
				    //����node��������ˢ��
				    Account account = car.getAccount();
				    int companyId = account.getAccountId();
				    logger.debug("companyId===>" + companyId);
				
					logger.info("-------start------");
				    logger.info("result======>" + json_msg);
				    logger.info("op======>" + opcode);
				    logger.info("------end-------");
					
					if(((opcode >> 30) & 0x3 )== 2){ 
						switch(tempInt){ 
							case 1: 
								noticeNodeJsServer(5, companyId, "����������ʻ,ִ��ָ��ʧ��!" );
								logger.info("------����������ʻ,ִ��ָ��ʧ��------");
								break; 
							case 2: 
								noticeNodeJsServer(5, companyId, "Ӳ������,ִ��ָ��ʧ��!");
								logger.info("------Ӳ������,ִ��ָ��ʧ��------");
								break; 
							case 3: 
								noticeNodeJsServer(5, companyId, "ǰһ��ָ���ȡ��!");
								logger.info("------ǰһ��ָ���ȡ��------");
								break; 
							case 4: 
								noticeNodeJsServer(5, companyId, "�������ִ��ָ�ʱ!");
								logger.info("------�������ִ��ָ�ʱ------");
								break; 
							default: 
								noticeNodeJsServer(5, companyId, "δ֪����,ִ������ʧ��!");
								logger.info("------δ֪����,ִ������ʧ��------");
								break; 
						} 
					} else if (((opcode >> 30) & 0x3 ) == 1) { 
						noticeNodeJsServer(4, companyId, "ִ������ɹ�!");
						userService.setOff(car.getVehicleId(),op);
						logger.info("------ִ������ɹ�------");
					} 
				}
				
			}else{
				logger.error("���յ���Ϣ����δ֪��");
			}
		} catch (JSONException e) {
			logger.error("���յ���Ϣ��ʽ����" + e.toString());
		} catch (Exception ee) {
			logger.error("��������ʧ�ܣ�ԭ��" + ee.getMessage());
		}
		
		//op = 1 Ϊ�Ѿ����Ͷϵ�  op = 0Ϊδ���Ͷϵ�
		/*int op = (opcode&(int)Math.pow(2, 8))/(int)Math.pow(2, 8);
		
		System.out.println("op==>" + op);
		logger.info("-------start------");
	    logger.info("result======>" + result);
	    logger.info("op======>" + op);
	    logger.info("------end-------");
	    
	    if(car != null){
		    //����node��������ˢ��
		    Account account = car.getAccount();
		    int companyId;
		    if(account.getRole().getRoleId() == 3){
		    	companyId = Integer.parseInt(getCompanyId());
		    }else{
		    	companyId = account.getAccountId();
		    }
		    logger.debug("companyId===>" + companyId);
		    
		    if((opcode&(int)Math.pow(2, 30))/(int)Math.pow(2, 30) == 1){ 
		    	mc.noticeNodeJsServer1(4, companyId);
		    	 //op = 1 Ϊ�Ѿ����Ͷϵ�  op = 0Ϊδ���Ͷϵ�
				userService.setOff(car.getVehicleId(),op);
				logger.info("------ִ������ɹ�------");
			}else if((opcode & ((int) Math.pow(2, 31) + 1)) / ((int) Math.pow(2, 31) + 1) == 1) { 
				mc.noticeNodeJsServer1(5, companyId);
				logger.info("------ִ������ʧ��------");
			}else{ 
				mc.noticeNodeJsServer1(6, companyId);
				logger.info("------ִ�����ʱ-----");
			}
		    
	    }
	    */
	}

	//ƴ�ӵ���node��������url���ҽ��з�����Ϣ��ǰ̨
	private void noticeNodeJsServer(int type, int companyId, String data) throws UnsupportedEncodingException {
		String newData = URLEncoder.encode(data,"utf-8"); 
		String url = this.nodeJsUrl+"appid="+this.appid+"&type="+type+"&companyId="+companyId+"&data=" + newData;
		logger.info("url==>" + url);
		crawlPost(url,"","utf-8");
	}

	public String crawlPost(String url,String para, String charset) {
		String content = "";
		BufferedReader reader= null;
		try{
			URLConnection connection = new URL(url).openConnection();
			connection.setConnectTimeout(connectionTimeOut);//
			connection.setReadTimeout(readTimeOut);//
			connection.setUseCaches(false);
				connection.setDoOutput(true);
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(),charset);
				out.write(para);
				out.flush();
				out.close();
			String sCurrentLine = "";
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
					charset == null ? this.charSet : charset));// ��ȡ��ҳԴ���룬���û��ָ���ַ�������ʹ��Ĭ�ϵ�UTF-8
			while ((sCurrentLine = reader.readLine()) != null) {
				content += sCurrentLine;
			}
			System.out.println(content);
		} catch (IOException e) {
			logger.error("����"+url + "ʧ�ܣ�ԭ��:"+e.getMessage());
			close(reader);
		}finally {
			close(reader);
		}
		return content;
	}
	
	public static void close(Closeable c) {
		if(c!=null)
			try {
				c.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				c = null;
			}
	}
	
	
	 /**
	  * @author dj
	  * @date Sep 18, 2013
	  * @param x
	  * @return
	  * @Description ��ӡ������ֵ�����������Ƹ�ʽ
	  */
   private static String toFullBinaryString(int x) {
      int[] buffer = new int[Integer.SIZE];
      for (int i = (Integer.SIZE - 1); i >= 0; i--) {
          buffer[i] = x >> i & 1;
      }
      String s = "";
      for (int j = (Integer.SIZE - 1); j >= 0; j--) {
          s = s + buffer[j];
      }
      return s;
   }
  /**
   * @des �����쳣�����ĺ���
   * @param paras
   * @param status
   * @return
   */
  private String getReason(String paras, int status){
		String binary  = toFullBinaryString(status);
		String[] paraAry = paras.split(",");
		paraAry[2] = String.valueOf(Integer.parseInt(paraAry[2])/1000) ;
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<28;i++){
			String temStr = binary.substring(i, i+2);
			logger.info(temStr);
			if(!"00".equals(temStr)){
				if("10".equals(temStr)){
					sb.append(EnumCommon.paraDescription[i/2]).append(" ").append(paraAry[i/2]).append(EnumCommon.paraUnit[i/2]).append(" ��||");
				}else if("01".equals(temStr)){
					sb.append(EnumCommon.paraDescription[i/2]).append(" ").append(paraAry[i/2]).append(EnumCommon.paraUnit[i/2]).append(" ��||");
				}
			}
			i++;
		}
		if(!"".equals(sb.toString())){
			sb.delete(sb.length()-2,sb.length());
		}
		return sb.toString();
  }

	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getNodeJsUrl() {
		return nodeJsUrl;
	}

	public void setNodeJsUrl(String nodeJsUrl) {
		this.nodeJsUrl = nodeJsUrl;
	}
	
}
