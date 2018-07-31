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

import org.springframework.stereotype.Component;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.SMS;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
@Component("jpushUtils")
public class JPushUtils {
	
	private String charSet = "UTF-8";// 字符集
	private int connectionTimeOut = 30000;// 连接超时时间
	private int readTimeOut = 40000;// 读取超时时间
	private  String app_key_marster;
	private  String app_key;
	private  String pre;
	private String nodeJsUrl;
    private int appid = 1;

	public String getNodeJsUrl() {
		return nodeJsUrl;
	}




	public void setNodeJsUrl(String nodeJsUrl) {
		this.nodeJsUrl = nodeJsUrl;
	}




	public  String getPre() {
		return pre;
	}




	public  void setPre(String pre) {
		this.pre = pre;
	}




	public  void sendMsgToMem(String uids,int status,String content){
		 
		
		 JPushClient jpushClient = new JPushClient(app_key_marster, app_key);
	       // try {
		 String title = "";
		 String msg = "";
		 
			switch(status) {
			case -4:
				title = "预约被拒绝";
				msg = "有一个预约被车队长拒绝，请查看";
				break;
			case -3:
				title = "预约被拒绝";
				msg = "有一个预约被部门领导拒绝，请查看";
				break;
			case -2:
				title = "预约被拒绝";
				msg = "有一个预约被公司领导拒绝，请查看";
				break;
			case -1:
				title = "预约被拒绝";
				msg = "您有一个预约被管理员拒绝，请查看";
				break;
			case 0:
				title = "新建了一个预约";
				msg = "您有一个新预约，请查看";
				break;
			case 1:
				title = "预约通过";
				msg = "您的一条预约管理员已同意";
				break;
			case 2:
				title = "预约通过";
				msg = "您的一条预约公司领导已同意";
				break;
			case 3:
				title = "预约通过";
				msg = "您的一条预约部门领导已同意";
				break;
			case 4:
				title = "已派车";
				msg = "您的预约已经派车";
				break;
			case 5:
				title = "已发车";
				msg = "您的预约车辆已经出发";
				break;
			case 6:
				title = "乘客已上车";
				msg = "您预约的车辆已经到达，您已经上车";
				break;
			case 7:
				title = "已完成";
				msg = "您的预约已完成，谢谢";
				break;
				
			}
		 
    	 PushPayload payload = buildPushObject_ios_audienceMore_messageWithExtras(pre+uids, msg);
    	 
    	 try {
 	        PushResult result = jpushClient.sendPush(payload);
// 	        LOG.info("Got result - " + result);

 	    } catch (APIConnectionException e) {
 	        // Connection error, should retry later
// 	        LOG.error("Connection error, should retry later", e);

 	    } catch (APIRequestException e) {
 	        // Should review the error, and fix the request
// 	        LOG.error("Should review the error, and fix the request", e);
// 	        LOG.info("HTTP Status: " + e.getStatus());
// 	        LOG.info("Error Code: " + e.getErrorCode());
// 	        LOG.info("Error Message: " + e.getErrorMessage());
 	    }		
	}
	public  void sendMsgToDriver(String uids,int status,String content){
		 
		
		 JPushClient jpushClient = new JPushClient(app_key_marster, app_key);
	       // try {
		 String title = "";
		 String msg = "";
		 
			switch(status) {
			case -5:
				title = "预约被取消";
				msg = "有一个预约被取消";
				break;
			
			case 4:
				title = "已派车";
				msg = "有一个预约分派给您，请准时发车";
				break;
			
			}
		 
   	 PushPayload payload = buildPushObject_ios_audienceMore_messageWithExtras(pre+uids, msg);
   	 
   	 try {
	        PushResult result = jpushClient.sendPush(payload);
//	        LOG.info("Got result - " + result);

	    } catch (APIConnectionException e) {
	        // Connection error, should retry later
//	        LOG.error("Connection error, should retry later", e);

	    } catch (APIRequestException e) {
	        // Should review the error, and fix the request
//	        LOG.error("Should review the error, and fix the request", e);
//	        LOG.info("HTTP Status: " + e.getStatus());
//	        LOG.info("Error Code: " + e.getErrorCode());
//	        LOG.info("Error Message: " + e.getErrorMessage());
	    }		
	}
	

	
    public static PushPayload buildPushObject_all_all_alert(String ALERT) {
        return PushPayload.alertAll(ALERT);
    }
    
    public static PushPayload buildPushObject_android_tag_alertWithTitle(String ALERT,String TITLE) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag("tag1"))
                .setNotification(Notification.android(ALERT, TITLE, null))
                .build();
    }
    
    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(String ALERT,String MSG_CONTENT) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and("tag1", "tag_all"))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(ALERT)
                                .setBadge(5)
                                .setSound("happy")
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                 .setMessage(Message.content(MSG_CONTENT))
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(true)
                         .build())
                 .build();
    }
    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras(String alias,String MSG_CONTENT) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                       // .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
                        .addAudienceTarget(AudienceTarget.alias(alias))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(MSG_CONTENT)
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }
    
   
  
    public void noticeNodeJsServer(int type, int companyId, String data) throws UnsupportedEncodingException {
		String newData = URLEncoder.encode(data,"utf-8"); 
		String url = this.nodeJsUrl+"appid="+this.appid+"&type="+type+"&companyId="+companyId+"&data=order";
		crawlPost(url,"","utf-8");
	}

	public  String getApp_key_marster() {
		return app_key_marster;
	}



	public  void setApp_key_marster(String app_key_marster) {
		this.app_key_marster = app_key_marster;
	}



	public  String getApp_key() {
		return app_key;
	}



	public  void setApp_key(String app_key) {
		this.app_key = app_key;
	}
	public String crawlPost(String url, String para, String charset) {
		String content = "";
		BufferedReader reader = null;
		try {
			URLConnection connection = new URL(url).openConnection();
			connection.setConnectTimeout(connectionTimeOut);//
			connection.setReadTimeout(readTimeOut);//
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connection
					.getOutputStream(), charset);
			out.write(para);
			out.flush();
			out.close();
			String sCurrentLine = "";
			reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream(),
							charset == null ? this.charSet : charset));// 读取网页源代码，如果没有指定字符集，则使用默认的UTF-8
			while ((sCurrentLine = reader.readLine()) != null) {
				content += sCurrentLine;
			}
			 System.out.println(content);
		} catch (IOException e) {
			System.out.println("访问"+url + "失败！原因:"+e.getMessage());
			e.printStackTrace();
			close(reader);
		} finally {
			close(reader);
		}
		return content;
	}

	public static void close(Closeable c) {
		if (c != null)
			try {
				c.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				c = null;
			}
	}
}
