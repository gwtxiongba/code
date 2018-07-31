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

import com.chanceit.http.pojo.Boxreminder;
import com.chanceit.http.pojo.Point;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IBoxreminderService;
import com.chanceit.http.service.IUserService;

public class WarnMessageQueueClient implements MessageListener{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String charSet="UTF-8";// �ַ���
	private int connectionTimeOut = 30000;// ���ӳ�ʱʱ��
	private int readTimeOut = 40000;// ��ȡ��ʱʱ��
	
	@Autowired
	@Qualifier("boxreminderService")
	private IBoxreminderService boxreminderService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("converter")
	private Converter converter;
	
	private String appid;
	private String nodeJsUrl;
	
	@Override
	public void onMessage(Message message) {
		try {
			String data = new String(message.getBody(), "utf-8");
			logger.info("=======================�յ�������Ϣ��===+"+data+"=======================");
		   // System.out.println(data);
			String routingKey = message.getMessageProperties().getReceivedRoutingKey();
			String identifier = routingKey.replaceAll("box.warning.", "");
			
			JSONObject jsonObj = new JSONObject(data);
			int type = jsonObj.getInt("type");
			
			//ȥ���������
			if(type == 4 || type == 3 || type == 9 || type == 10 || type == 11|| type == 12|| type == 13|| type == 14|| type == 15 ||type == 17 || type==18){
				return;
			}
			
			int x = jsonObj.getInt("x");
			int y = jsonObj.getInt("y");
			
			/*int type = 2;
			String x = "104.66267502021695";
			String y = "28.781693798440397";
			String identifier = "209768190";*/
			
			//ͨ��ʶ�����ȡ������Ϣ
			User car = userService.getByName(identifier);

//
//			2015-01-24 16:07:19,734 [SimpleAsyncTaskExecutor-1] INFO  [com.chanceit.framework.utils.WarnMessageQueueClient] - =======================�յ�������Ϣ��===+{
//				"x":	9882580,
//				"y":	2644424,
//				"type":	2,
//				"reserve":	0
//			}=======================
//			114.01196936441224//30.003784439182446  114.3937  30.610
			//114.01196358108986//30.003782894670202
			if(car != null){
				Boxreminder boxreminder = new Boxreminder();
				boxreminder.setUser(car);
				Point point = converter.wgs2bd(y/(3600.0*24.0), x/(3600.0*24.0));
				logger.info("======================"+String.valueOf(point.getLongitude())+"//"+String.valueOf(point.getLatitude()));
				boxreminder.setX(String.valueOf(point.getLongitude()));//new java.text.DecimalFormat(��#.00��).format(3.1415926)
				boxreminder.setY(String.valueOf(point.getLatitude()));
				boxreminder.setTime(new Date());
				boxreminder.setIfRead((short)0);
				boxreminder.setIfWarnPhone((short)0);
				boxreminder.setIfWarnWeb((short)0);
				boxreminder.setCreateTime(new Date());
				boxreminder.setType((short)type);
				//1 ��ײ 2 �෭ 3 �ϵ� 4 ��� 5 �ε�� 
				//6 �� 7 �͵�ѹ 8 ƣ�ͼ�ʻ 9 ���豸��� 10 β�䱻�� 
				//11 ���ű��� 12 ���汻�� 13 �������� 14 �������ƶ� 15 ��ĸ�豸����,16 ���ٱ���
				boxreminder.setReason(getDes((short)type,0));
				boxreminder.setAccountId(car.getAccount().getAccountId());//
				boxreminder.setDriver(car.getDriver());
				boxreminderService.save(boxreminder);
				noticeNodeJsServer(2,car.getTeam().getTeamId(), "-�յ�������Ϣ-");
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
		String url = this.nodeJsUrl+"appid="+this.appid+"&type="+type+"&companyId="+companyId+"&data=" + newData;
//		logger.info("url==>" + url);
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
				case 9:
					return "overSpeed";
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
					return "���ٱ���";
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
