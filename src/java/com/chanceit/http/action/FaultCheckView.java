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
	
	
	//获取OBD故障码
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
	
	//开启/关闭OBD通讯
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
			String path = openOrCloseOBDUrl+"uid:"+ identifier +"/op:1";	//请求打开OBD通讯
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
			String path = openOrCloseOBDUrl+"uid:"+ identifier +"/op:0";	//请求关闭OBD通讯
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
		return ResultManager.getFaildResult(EnumCommon.WEBSERVICE_OTHER, "其他错误");
	}
	
	//获取OBD实时数据
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
		String path1 = getOBDInfoUrl+"uid:"+ identifier;		//获取OBD实时数据请求
		url = new URL(path1);
		con = url.openConnection();
		
		logger.info("-------start------");
		
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
		sb=new StringBuffer();
		while(reader1.read() != -1){
			sb.append(reader1.readLine());
		}
		String OBDInfo = new String("{"+sb);
//		String OBDInfo = "{'isok':true,'result':0,'body':[{'title':'燃油量','value':66,'unit':'%','range':'0-100','error':false},{'title':'发动机负荷','value':84,'unit':'%','range':'0-100','error':false}," +
//				"{'title':'OBD 协议','value':'ISO 14230-4 KWP2000(fast init,10.4 kBit/s)','unit':'','range':'0-100','error':false},{'title':'OBD 标准','value':'EOBD and OBD-II','unit':'%','range':'0-100','error':false}," +
//				"{'title':'冷却液温度','value':33,'unit':'','range':'0-100','error':false},{'title':'发动机转速','value':1433,'unit':'%','range':'0-100','error':false}," +
//				"{'title':'进气温度','value':58,'unit':'','range':'0-100','error':false},{'title':'空气流量','value':10,'unit':'%','range':'0-100','error':false},{'title':'节气门绝对位置','value':10,'unit':'%','range':'0-100','error':false}," +
//				"{'title':'车辆车速','value':200,'unit':'','range':'0-100','error':false},{'title':'电瓶电压','value':11.6,'unit':'%','range':'0-100','error':false}]}";
		
		logger.info("-------end------");
		
		JSONObject OBDInfoJson = new JSONObject(OBDInfo);
		Boolean isok = (Boolean) OBDInfoJson.get("isok");
		
		logger.info("isOk==>" + isok);
		if(isok){
			//0.表示有数据上来了；1.等待车辆上传数据...；2.车辆已经离线，已经停止获取obd数据；3.车辆已经离线，已经停止获取obd数据
			Integer r = (Integer) OBDInfoJson.get("result");
			JSONObject dataJson = new JSONObject();
			if(r == 0){
				dataJson.put("result", 0);
				JSONArray arr = OBDInfoJson.getJSONArray("body");
				for(int i=0;i<arr.length();i++){
					JSONObject json = (JSONObject)arr.get(i);
					String title = json.get("title").toString().trim();
					String titleStr = "";
					if("燃油量".equals(title)){
						titleStr = "oil";
					}else if("发动机负荷".equals(title)){
						titleStr = "engine";
					}else if("冷却液温度".equals(title)){
						titleStr = "temperature";
					}else if("发动机转速".equals(title)){
						titleStr = "eSpeed";
					}else if("车辆车速".equals(title)){
						titleStr = "cSpeed";
					}else if("电瓶电压".equals(title)){
						titleStr = "voltage";
					}else if("OBD 协议".equals(title)){
						titleStr = "agreement";
					}else if("OBD 标准".equals(title)){
						titleStr = "standard";
					}else if("进气温度".equals(title)){
						titleStr = "in_temperature";
					}else if("空气流量".equals(title)){
						titleStr = "air";
					}else if("节气门绝对位置".equals(title)){
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


















