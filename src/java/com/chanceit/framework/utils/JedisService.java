package com.chanceit.framework.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.xfire.client.Client;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

@Component("jedisService")
public class JedisService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	// "http://192.168.0.130:8080/shopHttp/services/positionService?wsdl";
	private String serverUrl = "http://127.0.0.1:8080/services/positionService?wsdl";
	//private String serverUrl = "http://192.168.0.153:8080/shopHttp/services/positionService?wsdl";
	private String urlRead = "120.76.124.198";
	private int port = 6379;
	@Autowired
	@Qualifier("converter")
	private Converter converter;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		JedisService ju = new JedisService();
		//1405311185
		//1405311088515
		System.out.println(ju.getUidInfo("153020384"));
		StringBuffer sb = new StringBuffer();
		sb.append("dfdf").append(",");
		System.out.println(sb.length());
		sb = sb.delete(sb.length()-1,sb.length());
		System.out.println(sb.toString());
	}

	
	public String getUidInfo(String uid) throws Exception{
		try {
			Resource resource;
			Client client = ClientFactory.getClient(serverUrl);
			Object[] objs = new Object[1];
			objs[0] = uid;
			String result = client.invoke("getUidInfo", objs)[0].toString();
			return result;
		} catch (Exception e) {
			logger.info("获取uid:"+uid+"的信息出错！原因："+e.toString());
			return null;
		}
	}
	
	public boolean synchronizeUid(String companyId,String uids){
//		try {
//			String key = messageClient.getAppid()+"_"+companyId; //"jk_"+
//			Resource resource;
//			Client client = ClientFactory.getClient(serverUrl);
//			Object[] objs = new Object[2];
//			objs[0] = key;
//			objs[1] = uids;
//			String result = client.invoke("synchronizeUid", objs)[0].toString();
//			return Boolean.parseBoolean( result);
//		} catch (Exception e) {
//			logger.info("同步公司"+companyId+"的uid到redis服务器出错！原因："+e.toString());
//			return false;
//		}
		return true;
	}

	public int getOnlinesAmount(String uids){
		try {
			Resource resource;
			Client client = ClientFactory.getClient(serverUrl);
			Object[] objs = new Object[1];
			objs[0] = uids;
			String result = client.invoke("getOnlinesAmount", objs)[0].toString();
			return Integer.parseInt(result);
		} catch (Exception e) {
			logger.info("获取在线车辆数量出错！原因："+e.toString());
			return 0;
		}
	}
	
	/**
	 * @des 获取在线or不在线的列表
	 * @param uids
	 * @param online 0离线 1在线
	 * @return
	 * @throws Exception
	 */
	public List getList(List list,int online) throws Exception{
		List newList = new ArrayList();
		String uids = "";
		String result ="";
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				Map map = (HashMap)list.get(i);
				uids += map.get("identifier").toString() +",";
			}
			try {
				Client client = ClientFactory.getClient(serverUrl);
				Object[] objs = new Object[1];
				objs[0] = uids;
				result = client.invoke("getOnlineList", objs)[0].toString();
				if(StringUtils.isBlank(result)){
					logger.info("获取在线车辆列表出错！原因：服务返回null");
					return newList;
				}
			} catch (Exception e) {
				logger.info("获取在线车辆列表出错！原因："+e.toString());
				return newList;
			}
			String[] uidAry = result.split(",");
			if(list.size()!= uidAry.length){
				logger.info("获取在线车辆列表出错！原因：列表数量不匹配！");
			}else{
				for(int i=0;i<list.size();i++){
					if(Integer.parseInt(uidAry[i]) == online){
						newList.add(list.get(i));
					}
				}
			}
		}
		return newList;
	}

	/** 
	 * add by gwt 2015.4.8;
	 * @param tableId
	 * @return  从redis获取批量位置点
	 */
	public String getLocationByUids(String tableId){
		try {
			Resource resource;
		  //String serverUrl = "http://localhost:8080/shopHttp/services/positionService?wsdl";
			Client client = ClientFactory.getClient(serverUrl);
			Object[] objs = new Object[1];
			objs[0] = tableId;
			String result = client.invoke("getLocationByUids", objs)[0].toString();
			return result;
		} catch (Exception e) {
			logger.info("获取批量位置点出错！原因："+e.toString());
			return null;
		}
	}

	public String getServerUrl() {
		return serverUrl;
	}


	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	public Jedis getJedis(String url) {
		// if(jedis == null){
		Jedis jedis = new Jedis(url, port);// 连接redis
		// }
		// jedis.
		return jedis;
	}
	
	
	public String getLocationByUidsMe(String uids) {
		Jedis jedis = getJedis(urlRead);
		// String uids = jedis.get(tableId);
		String[] uidArry = uids.split(",");
		String onLine = "";
		try {
			onLine = getList_x(jedis, uidArry);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] onLineArr = onLine.split(",");
		List<String> list2 = jedis.mget(uidArry);
		List res_list = new ArrayList();
		String allUids = "";
		boolean flag = true;
		for (int i = 0; i < list2.size(); i++) {
			String json_tem = list2.get(i);
			if (json_tem != null && json_tem != "") {
				try {
					JSONObject json = new JSONObject(json_tem);
					json.put("uid", uidArry[i]);
					json.put("online", onLineArr[i]);
					res_list.add(json);
					if (flag) {
						allUids = uidArry[i];
						flag = false;
					} else {
						allUids += "," + uidArry[i];
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		JSONObject json = new JSONObject();
		String res = null;
		try {
			json.put("res", res_list);
			json.put("uids", allUids);
			res = json.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		// Gson gs = new Gson();
		// String res = gs.toJson(res_list);
		try{
		if (jedis.isConnected()) {
			jedis.quit();
			jedis.disconnect();
		}
		}catch(Exception e){
			
		}
		return res;
	}

	public String getList_x(Jedis jedis, String[] uidAry) throws Exception {
		StringBuffer sb = new StringBuffer();
		List<String> list2 = jedis.mget(uidAry);
		for (int i = 0; i < list2.size(); i++) {
			Object obj = list2.get(i);
			if (obj != null) {
				// System.out.println(obj.toString());
				JSONObject json = new JSONObject(obj.toString());
				long time = Long.parseLong(String.valueOf(json.get("time")));
				long interval = DateUtil.sub(time);
				if (interval > 180) {// 大于3mins 为离线
					json.put("online", 0);
				} else {
					json.put("online", 1);
				}
				int online = (Integer) json.get("online");
				sb.append(online).append(",");
			} else { // 查询不到的情况下默认为离线
				sb.append(0).append(",");
			}
		}
		sb = sb.delete(sb.length() - 1, sb.length());
		return sb.toString();
	}
	public String getUrlRead() {
		return urlRead;
	}


	public void setUrlRead(String urlRead) {
		this.urlRead = urlRead;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}
	
}
