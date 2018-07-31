/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.exceptions.CommonException;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Message;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IMessageCenterService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * @ClassName ProstoreMsg
 * @author zhangxin
 * @date 2014-6-27
 * @Description Ԥ����Ϣ��ؽӿ�
 */
@Component("messageCenterView")
public class MessageCenterView extends BaseAction {
	
	private static final long serialVersionUID = 1657519070056147590L;

	@Autowired
	@Qualifier("messageCenterService")
	private IMessageCenterService msgCenterService;
	
	/**
	 * ������Ϣ
	 * @throws Exception
	 */
//	public String save() throws Exception {
//		String msgContent = getParameter("msgContent");
//		String vehicleIds = getParameter("vehicleIds");
//		String identifiers = getParameter("identifiers");
//		String image = getParameter("image");
//		if(StringUtils.isEmpty(identifiers)) throw new CommonException("ʶ���������д");
//		if(StringUtils.isEmpty(msgContent)) throw new CommonException("��Ϣ���ݱ�����д");
//		Account account = super.getSessionAccount();
//		
//		List<Message> msgList = new ArrayList<Message>();
//		//��ȡ���г���,��������ID,��ÿ����������Ϣ����
//		String[] idsAry = vehicleIds.split(",");
//		for(String id:idsAry){
//			User userCar = new User();
//			userCar.setVehicleId(Integer.parseInt(id));
//			
//			Message msg = new Message();
//			msg.setCar(userCar);
//			msg.setMessageContent(msgContent);
//			msg.setCompany(account);
//			msg.setCreateTime(new Date());
//			//����/������Ϣ��ʶ��  0��ʾ���յ���  1��ʾ���͵���  Ĭ��Ϊ1
//			msg.setSendFlag(1);
//			//����δ����ʶ�� 0δ��  1�Ѷ�
//			msg.setIfread(0);
//			
//			msgList.add(msg);
//		}
//		
//		if(messageClient.sendSingleMessage(identifiers, msgContent, true, image)){
//			boolean success = msgCenterService.save(msgList);
//			
//			if(!success){
//				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS, "ϵͳ�쳣��");
//			}else{
//				return ResultManager.getSuccResult();
//			}
//		}else{
//			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS, "������Ϣʧ�ܣ�");
//		}
		
		
//	}
	
	/**
	 * ͨ����¼��Ϣ  ��ѯ����Ϣ�����б�
	 * @return ��Ϣ�����б�
	 */
	public String list(Page page) {
		String ifread = getParameter("ifread");
		//String startDate = getParameter("startDate");
		//String endDate = getParameter("endDate");
		Account account = super.getSessionAccount();
		int accountId = account.getAccountId();
		Object[] keywords = new Object[]{accountId, ifread};
		page = msgCenterService.getMsgList(page, keywords);
		return ResultManager.getBodyResult(page);
	}
	
	/**
	 * ÿ��������Ϣ�б�
	 * @return ÿ��������Ϣ�б�
	 */
	public String getlist(Page page) {
		String carId = getParameter("carId");
		String startDate = getParameter("startDate");
		String endDate = getParameter("endDate");
		Account account = super.getSessionAccount();
		int accountId = account.getAccountId();
		Object[] keywords = new Object[]{accountId, carId, startDate, endDate};
		page  = msgCenterService.getMsgInforList(page,keywords);
		return ResultManager.getBodyResult(page);
	}
	
	/*
	public static void main(String[] args) throws MalformedURLException {
		/*
		MessageClient mc = new MessageClient();
		String url = "http://www.chanceit.cn:8080/PersonalMonitoring/services/HistoryWebService?wsdl";
		String uid = "153206344";
		String param = "uid=153206344";
		String result = mc.crawlPost(url, param, "utf-8");
		System.out.println(result);*/
		
	
		/*
		URL url = new URL ("http://www.chanceit.cn:8080/PersonalMonitoring/services/HistoryWebService?wsdl");
        //�ĳ���ĵ�ַ
        SOAPMappingRegistry smr = new SOAPMappingRegistry ();
        StringDeserializer sd = new StringDeserializer ();
        //smr.mapTypes (Constants.NS_URI_SOAP_ENC, new QName ("", "Result", null, null, sd);
        // ��������·���Ͳ���
        SOAPHTTPConnection st = new SOAPHTTPConnection();
        // ��������
        Call call = new Call ();
        call.setSOAPTransport(st);
        call.setSOAPMappingRegistry (smr);
        //call.setTargetObjectURI ("http://tempuri.org/message/");
        //call.setEncodingStyleURI ("http://schemas.xmlsoap.org/soap/encoding/");
        call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);
        call.setTargetObjectURI("urn:xmethods-caSynrochnized");
        call.setMethodName("historyPathWebService");
        
        Vector params = new Vector();
        params.addElement(new Parameter("uid", String.class, "153206344", null));
        call.setParams(params);
        Response resp = null;
        try {
          resp = call.invoke (url, "");
        }
        catch (SOAPException e) {
        	System.err.println("Caught SOAPException (" + e.getFaultCode () + ": " + e.getMessage ());
        return;
        }
        // ��鷵��ֵ
        if (resp != null && !resp.generatedFault()) {
	        Parameter ret = resp.getReturnValue();
	        Object value = ret.getValue();
	        System.out.println ("Answer--> " + value);
        } else {
            Fault fault = resp.getFault ();
            System.err.println ("Generated fault: ");
            System.out.println (" Fault Code = " + fault.getFaultCode());
            System.out.println (" Fault String = " + fault.getFaultString());
        }
        
	}*/
	
	public static void main(String[] args) throws Exception {
		/*String strDate = "2014-06-01";
		String beginDateStr = strDate.replaceAll("-", "");
		String beginDate = beginDateStr + "0000";
		
		String endDateStr = addDate(strDate, 1);
		String endDate = endDateStr.replaceAll("-", "") + "0000";
		
		
		MessageClient mc = new MessageClient();
		String url = "http://www.chanceit.cn:82/GetBaiduPoints/getDayPointsInfo?";
		String uid = "153206344";
		String param = "uid=" + 1111 + 
					   "&getBeginDate=" + beginDate + 
					   "&getEndDate=" + endDate;*/
		//String param = a1();
		
 		
		
		//System.out.println(param);
 	}
	
	
	//�г��ռ�
//	public static String a1() throws Exception{
//		String endpoint = "http://127.0.0.1:8080/services/UserRecorderService"; 
//		String in0 = "153206344";
// 		String in1 = "2014-07-08";
// 		String endTime = "2014-04-02";
// 		Service service = new Service();
// 		Call call = (Call) service.createCall();
// 		call.setTargetEndpointAddress(new URL(endpoint));
// 		call.setOperationName("getDataByMonth_and");
// 		String rec = (String) call.invoke(new Object[]{in0, in1});
// 		return rec;
// 		
//	}

	//�г��ռ�
//	public String getaaa() throws Exception{
//		String endpoint = "http://127.0.0.1:8080/services/UserRecorderService"; 
//		//String endpoint = "http://192.168.1.14:8090/services/UserRecorderService";
//		String in0 = "153206344";
//		String date = "2014-07-08";
// 		//String in1 = "2014-07-08";
// 		String in1 = date.substring(0,7) + "-01";
// 		
// 		Service service = new Service();
// 		Call call = (Call) service.createCall();
// 		call.setTargetEndpointAddress(new URL(endpoint));
// 		call.setOperationName("getDataByMonth_and");
// 		String rec = (String) call.invoke(new Object[]{in0, in1});
// 		return rec;
// 		
// 		/*System.out.println(rec);
// 		JSONObject obj = new JSONObject(rec);
// 		
// 		JSONArray array = (JSONArray) obj.get("2014-07-01");
// 		String a = ResultManager.getBodyResult(array);
// 		return array.toString();*/
//	}
	
	
//	//��������
//	public static String m1() throws Exception{
//		String endpoint = "http://www.chanceit.cn:8080/PersonalMonitoring/services/HistoryWebService";
// 		String uid = "153206344";
// 		Service service = new Service();
// 		Call call = (Call) service.createCall();
// 		call.setTargetEndpointAddress(new URL(endpoint));
// 		call.setOperationName("trackWebservice");
// 		return (String) call.invoke(new Object[]{uid});
//	}
	
	//����Ĺ켣����
//	public static String m2() throws Exception{
//		String endpoint = "http://www.chanceit.cn:8080/PersonalMonitoring/services/HistoryWebService";
// 		String uid = "153206344";
// 		String beginTime = "2013-01-02";
// 		String endTime = "2014-04-02";
// 		Service service = new Service();
// 		Call call = (Call) service.createCall();
// 		call.setTargetEndpointAddress(new URL(endpoint));
// 		call.setOperationName("historyPathWebService");
// 		return (String) call.invoke(new Object[]{uid, beginTime, endTime});
//	}
	
	//������ǰ�Ĺ켣����
//	public String getPoint() throws Exception{
//		String identifier = getParameter("identifier");
//		String strDate = getParameter("date"); 
//		
//		String beginDateStr = strDate.replaceAll("-", "");
//		String beginDate = beginDateStr + "0000";
//		
//		String endDateStr = addDate(strDate, 1);
//		String endDate = endDateStr.replaceAll("-", "") + "0000";
//		
//		
//		MessageClient mc = new MessageClient();
//		String url = "http://www.chanceit.cn:82/GetBaiduPoints/getDayPointsInfo?";
//		String uid = "153206344";
//		String param = "uid=" + identifier + 
//					   "&getBeginDate=" + beginDate + 
//					   "&getEndDate=" + endDate;
//		String rec = mc.crawlPost(url, param, "utf-8");
//		
//		//����json�ַ���
//		JSONObject obj = new JSONObject(rec);
//		boolean isOK = (Boolean)obj.get("isok");
//		
//		List gps = new ArrayList();
//		if(isOK){
//			JSONArray pointArray = (JSONArray)obj.get("points");
//			System.out.println(pointArray);
//			//gps = new Object[1][pointArray.length()];
//		    for(int i = 0;i < pointArray.length();i++){  
//		    	JSONObject jo = pointArray.getJSONObject(i);
//		    	
//		    	String x = "";
//		    	String y = "";
//		    	boolean base64 = (Boolean)obj.get("base64");
//		    	//�ж��Ƿ����,������ܾͽ��н��ܴ���
//		    	if(base64){
//		    		byte[] deX = Base64.decode(jo.getString("x")); 
//		    		byte[] deY = Base64.decode(jo.getString("y")); 
//		    		x = new String(deX);
//		    		y = new String(deY);
//		    	}else{
//		    		x = jo.getString("x");
//		    		y = jo.getString("y");
//		    	}
//		    	
//		    	String time = jo.getString("t");
//		    	
//		    	int speed = 0;
//		    	int azimuth = 0;
//		    	//�ж��������Ƿ��з�λ��
//		    	if(jo.get("speed") != null){
//		    		speed = jo.getInt("speed");
//		    	}
//		    	if(jo.get("azimuth") != null){
//		    		azimuth = jo.getInt("azimuth");
//		    	}
//		    	
//		    	Object[] pointObj = new Object[]{x,y,time,speed,azimuth};
//		    	gps.add(pointObj);
//		    
//		    }
//		}
//		return ResultManager.getBodyResult(gps);
//	}
	
	//�г��ռ�
//	public static String m4() throws Exception{
//		//String endpoint = "http://www.chanceit.cn:8081/services/UserRecorderService"; 
//		String endpoint = "http://192.168.1.14:8090/services/UserRecorderService";
//		String identifier = "153206344";
// 		String beginTime = "2013-01-02";
// 		String endTime = "2014-04-02";
// 		Service service = new Service();
// 		Call call = (Call) service.createCall();
// 		call.setTargetEndpointAddress(new URL(endpoint));
// 		call.setOperationName("getRecorder");
// 		return (String) call.invoke(new Object[]{identifier, beginTime, endTime});
//	}
	
	public static String addDate(String strDate, int days) throws ParseException{
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dft.parse(strDate);
		
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		// ���ڼ�һ��Ϊ��������
		ca.add(Calendar.DATE, days);
		
		Date eDate = ca.getTime();
		String addDate = dft.format(eDate);
		return addDate;
	}
}
