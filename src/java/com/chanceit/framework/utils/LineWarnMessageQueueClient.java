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
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.chanceit.http.pojo.GhLineWarn;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IBoxreminderService;
import com.chanceit.http.service.IGuanHuiService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;

public class LineWarnMessageQueueClient implements MessageListener{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String charSet="UTF-8";// �ַ���
	private int connectionTimeOut = 30000;// ���ӳ�ʱʱ��
	private int readTimeOut = 40000;// ��ȡ��ʱʱ��
	
	@Autowired
	@Qualifier("boxreminderService")
	private IBoxreminderService boxreminderService;
//	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
//	
	@Autowired
	@Qualifier("converter")
	private Converter converter;
	@Autowired
	@Qualifier("guanhuiService")
	private IGuanHuiService guanhuiService;
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	private String appid;
	private String nodeJsUrl;
	private String muid = "";
	private int time = 0;
	@Override
	public void onMessage(Message message) {
		try {
			String data = new String(message.getBody(), "utf-8");
			logger.info("***************************�յ�·����Ϣ��***********************");
//			String routingKey = message.getMessageProperties().getReceivedRoutingKey();
//			String identifier = routingKey.replaceAll("box.linewarn.", "");
			//{"uid":"153101142","lineid":"1","lng":"114.12121","lat":"30.456142","speed":0,"angle":0,"time":123456}
		   System.out.println(data);
			JSONObject jsonObj = new JSONObject(data);
			String uid = jsonObj.getString("uid");
			String type = jsonObj.getString("type");
			double x = jsonObj.getDouble("lng");
			double y = jsonObj.getDouble("lat");
			double xx = 0;
			double yy = 0;
			//String sql = "select a.uid as identifier,b.pts as path from ghaction as a,ghpoints as b where a.pid=b.id and a.uid='"+uid+"'";
//			if(StringUtils.isBlank(uid) || StringUtils.isBlank(lineid)){
//				return;
//			}
//			if(!StringUtils.isBlank(x)){
//				xx = Double.parseDouble(Long.parseLong(x)/(3600*24)+"");
//			}
//			if(!StringUtils.isBlank(y)){
//				yy = Double.parseDouble(Long.parseLong(y)/(3600*24)+"");
//			}
			List list = guanhuiService.getLimitLine(uid,type);
			if(list == null || list.size() == 0){
				return;
			}
			JSONArray array = new JSONArray();
			JSONArray array1 = new JSONArray();
			for(Iterator it = list.iterator();it.hasNext();){
				Object[] obj = (Object[]) it.next();
				if(obj[2] == null){
					array.put(obj[1].toString());
				}else{
					array1.put(obj[1].toString());
				}
			}
//			Team team = (Team) userService.getAccountIdByIden(Integer.parseInt(uid));
			User car = userService.getByName(uid);
////			Point point = converter.wgs2bd(xx,yy);
			int account_id = 0;
			if(car.getTeam() == null){
				return;
			}else{
			Team team = teamService.get(car.getTeam().getTeamId());
			
			if(team.getOperator()!=null){
				account_id = team.getAccountId();
			}else{
				account_id = team.getAccountId();
			}
			}
			GhLineWarn glw = new GhLineWarn();
			glw.setUid(uid);
			glw.setType(type);
			glw.setX(String.valueOf(x));
			glw.setY(String.valueOf(y));
			glw.setIsread(0);
			glw.setAnagel(jsonObj.getInt("angle"));
			glw.setSpeed(jsonObj.getInt("speed"));
			glw.setTime(jsonObj.getInt("time"));
			glw.setAccountId(car.getAccount().getAccountId());
			glw.setPath(array.toString());
			glw.setArea(array1.toString());
			glw.setOpId(account_id);
			if(!muid.equals(uid) || jsonObj.getInt("time")>time+60){
			guanhuiService.saveline(glw);
			muid = uid;
			time = jsonObj.getInt("time");
			}
//			//ͨ��ʶ�����ȡ������Ϣ
//			
			if(car != null){
//				Boxreminder boxreminder = new Boxreminder();
//				boxreminder.setUser(car);
//				Point point = converter.wgs2bd(yy, xx);
//				boxreminder.setX(String.valueOf(point.getLongitude()));//new java.text.DecimalFormat(��#.00��).format(3.1415926)
//				boxreminder.setY(String.valueOf(point.getLatitude()));
//				boxreminder.setTime(new Date());
//				boxreminder.setIfRead((short)0);
//				boxreminder.setIfWarnPhone((short)0);
//				boxreminder.setIfWarnWeb((short)0);
//				boxreminder.setCreateTime(new Date());
//				boxreminder.setType((short)16);
//				//1 ��ײ 2 �෭ 3 �ϵ� 4 ��� 5 �ε�� 
//				//6 �� 7 �͵�ѹ 8 ƣ�ͼ�ʻ 9 ���豸��� 10 β�䱻�� 
//				//11 ���ű��� 12 ���汻�� 13 �������� 14 �������ƶ� 15 ��ĸ�豸����
//				boxreminder.setReason(getDes((short)16,0));
//				boxreminder.setAccountId(car.getAccount().getAccountId());//
//				boxreminder.setDriver(car.getDriver());
//				boxreminderService.save(boxreminder);
				//noticeNodeJsServer(6,car.getTeam().getTeamId(), "-�յ�������Ϣ-");
			}else{
				logger.error("���յ���Ϣʶ�����ڴ�ϵͳ��δ�ҵ���");
			}
			
		} catch (JSONException e) {
			logger.error("���յ���Ϣ��ʽ����" + e.toString());
		} catch (Exception ee) {
			logger.error("��������ʧ�ܣ�ԭ��" + ee.getMessage());
		}
	}

	//ƴ�ӵ���node��������url���ҽ��з�����Ϣ��ǰ̨
	private void noticeNodeJsServer(int type, int companyId, String data) throws UnsupportedEncodingException {
		String newData = URLEncoder.encode(data,"utf-8"); 
		String url = this.nodeJsUrl+"appid="+this.appid+"&type="+type+"&companyId="+companyId+"&data=line";
		logger.info("url==>" + url);
		System.out.println("////"+url);
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
	
	public String getDes(short code,int flag){
		if(flag==1){
			switch(code){
				case 1:
					return "hit";
				case 2:
					return "sideTurn";
				case 3:
					return "pull";
				case 4:
					return "fire";
				case 5:
					return "takeBattery";
				case 6:
					return "shake";	
				case 7:
					return "lowvoltage";	
				case 8:
					return "tiredDrive";	
				default:
					return "";
			}
		}else{
			switch(code){
				case 1:
					return "��ײ";
				case 2:
					return "�෭";
				case 3:
					return "�ϵ�";
				case 4:
					return "���";
				case 5:
					return "�ε��";
				case 6:
					return "��";	
				case 7:
					return "�͵�ѹ";
				case 8:
					return "ƣ�ͼ�ʻ";
				case 9:
					return "���豸���";
				case 10:
					return "β�䱻��";
				case 11:
					return "���ű���";
				case 12:
					return "���汻��";
				case 13:
					return "��������";
				case 14:
					return "�������ƶ�";
				case 15:
					return "��ĸ�豸����";
				case 16:
					return "Χ������";
				default:
					return "";
			}
		}
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
