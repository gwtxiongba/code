package com.chanceit.http.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IUserService;

@Component("faultCheckView")
public class FaultCheckView extends BaseAction{
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6865036114135308043L;
	private String faultCheckUrl;
	private String openOrCloseOBDUrl;
    private String getOBDInfoUrl;
	public String getOpenOrCloseOBDUrl() {
		return openOrCloseOBDUrl;
	}

	public void setOpenOrCloseOBDUrl(String openOrCloseOBDUrl) {
		this.openOrCloseOBDUrl = openOrCloseOBDUrl;
	}

	public String getFaultCheckUrl() {
		return faultCheckUrl;
	}

	public void setFaultCheckUrl(String faultCheckUrl) {
		this.faultCheckUrl = faultCheckUrl;
	}
	
	public static void main(String[] args) throws Exception {
		FaultCheckView f = new FaultCheckView();
		System.out.println(f.getObdData());
	}
	
	
	//��ȡOBD������
	public String getObdCode() throws Exception{
		String vehicleId = getParameter("vehicleId");
		User user = userService.get(Integer.parseInt(vehicleId));
		String in0 = user.getIdentifier();
		
		List<String> pcodes = new ArrayList<String>();
 		Service service = new Service();
 		Call call = (Call) service.createCall();
 		call.setTargetEndpointAddress(new URL(faultCheckUrl));
 		call.setOperationName("getPcodeDesc");
 		String faultInfo = (String) call.invoke(new Object[]{in0, null});
 		
 		JSONObject jsonFaultInfo = new JSONObject(faultInfo);
 		JSONArray jsonResult = jsonFaultInfo.getJSONArray("result");
 		for(int i=0;i<jsonResult.length();i++){
 			JSONObject object2 = jsonResult.getJSONObject(i);
 			String pcode = object2.getString("pcode");
 			pcodes.add(pcode);
 		}
 		return ResultManager.getBodyResult(pcodes);
	}
	
	//����/�ر�OBDͨѶ
	public String getObdState() throws Exception{
		URL url;
		URLConnection con;
		StringBuffer sb;
		String vehicleId = getParameter("vehicleId");
		String state = getParameter("state");
		User user = userService.get(Integer.parseInt(vehicleId));
		String identifier = user.getIdentifier();
//		String identifier = "194290502";
//		String state = "true";
		if("true".equals(state)){
			String path = openOrCloseOBDUrl+"uid:"+ identifier +"/op:1";	//�����OBDͨѶ
			logger.info("-------path------"+path);
			url = new URL(path);
			con = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			sb=new StringBuffer();
			while(reader.read() != -1){
				sb.append(reader.readLine());
			}
			String openOrClose = new String("{"+sb);
			return openOrClose;
		}else if("false".equals(state)){
			String path = openOrCloseOBDUrl+"uid:"+ identifier +"/op:0";	//����ر�OBDͨѶ
			url = new URL(path);
			con = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			sb=new StringBuffer();
			while(reader.read() != -1){
				sb.append(reader.readLine());
			}
			String openOrClose = new String("{"+sb);
			return openOrClose;
		}
		return ResultManager.getFaildResult(EnumCommon.WEBSERVICE_OTHER, "��������");
	}
	
	//��ȡOBDʵʱ����
	public String getObdData() throws Exception{
		Logger logger = Logger.getLogger(FaultCheckView.class);
		
		URL url;
		URLConnection con;
		StringBuffer sb;
		String vehicleId = getParameter("vehicleId");
		User user = userService.get(Integer.parseInt(vehicleId));
		String identifier = user.getIdentifier();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//		String identifier = "194290502";
		String path1 = getOBDInfoUrl+"uid:"+ identifier;		//��ȡOBDʵʱ��������
		url = new URL(path1);
		con = url.openConnection();
		
		logger.info("-------start------");
		
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
		sb=new StringBuffer();
		while(reader1.read() != -1){
			sb.append(reader1.readLine());
		}
		String OBDInfo = new String("{"+sb);
//		String OBDInfo = "{'isok':true,'result':0,'body':[{'title':'ȼ����','value':66,'unit':'%','range':'0-100','error':false},{'title':'����������','value':84,'unit':'%','range':'0-100','error':false}," +
//				"{'title':'OBD Э��','value':'ISO 14230-4 KWP2000(fast init,10.4 kBit/s)','unit':'','range':'0-100','error':false},{'title':'OBD ��׼','value':'EOBD and OBD-II','unit':'%','range':'0-100','error':false}," +
//				"{'title':'��ȴҺ�¶�','value':33,'unit':'','range':'0-100','error':false},{'title':'������ת��','value':1433,'unit':'%','range':'0-100','error':false}," +
//				"{'title':'�����¶�','value':58,'unit':'','range':'0-100','error':false},{'title':'��������','value':10,'unit':'%','range':'0-100','error':false},{'title':'�����ž���λ��','value':10,'unit':'%','range':'0-100','error':false}," +
//				"{'title':'��������','value':200,'unit':'','range':'0-100','error':false},{'title':'��ƿ��ѹ','value':11.6,'unit':'%','range':'0-100','error':false}]}";
		
		logger.info("-------end------");
		
		JSONObject OBDInfoJson = new JSONObject(OBDInfo);
		Boolean isok = (Boolean) OBDInfoJson.get("isok");
		
		logger.info("isOk==>" + isok);
		if(isok){
			//0.��ʾ�����������ˣ�1.�ȴ������ϴ�����...��2.�����Ѿ����ߣ��Ѿ�ֹͣ��ȡobd���ݣ�3.�����Ѿ����ߣ��Ѿ�ֹͣ��ȡobd����
			Integer r = (Integer) OBDInfoJson.get("result");
			JSONObject dataJson = new JSONObject();
			if(r == 0){
				dataJson.put("result", 0);
				JSONArray arr = OBDInfoJson.getJSONArray("body");
				for(int i=0;i<arr.length();i++){
					JSONObject json = (JSONObject)arr.get(i);
					String title = json.get("title").toString().trim();
					String titleStr = "";
					if("ȼ����".equals(title)){
						titleStr = "oil";
					}else if("����������".equals(title)){
						titleStr = "engine";
					}else if("��ȴҺ�¶�".equals(title)){
						titleStr = "temperature";
					}else if("������ת��".equals(title)){
						titleStr = "eSpeed";
					}else if("��������".equals(title)){
						titleStr = "cSpeed";
					}else if("��ƿ��ѹ".equals(title)){
						titleStr = "voltage";
					}else if("OBD Э��".equals(title)){
						titleStr = "agreement";
					}else if("OBD ��׼".equals(title)){
						titleStr = "standard";
					}else if("�����¶�".equals(title)){
						titleStr = "in_temperature";
					}else if("��������".equals(title)){
						titleStr = "air";
					}else if("�����ž���λ��".equals(title)){
						titleStr = "position";
					}
					
					
					if(title != "" && !"".equals(title)){
						dataJson.put(titleStr, json.get("value"));
					}
				}
				String time = sdf.format(new Date());
				dataJson.put("time",time);
				return  dataJson.toString();
			}else{
				logger.info("OBDInfo==>" + OBDInfo);
				return OBDInfo;
			}
		}
		return OBDInfo;
	}

	public String getGetOBDInfoUrl() {
		return getOBDInfoUrl;
	}

	public void setGetOBDInfoUrl(String getOBDInfoUrl) {
		this.getOBDInfoUrl = getOBDInfoUrl;
	}
}


















