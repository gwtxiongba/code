package com.chanceit.http.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.rpc.ServiceException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.GsonUtil;
import com.chanceit.http.dao.IUserDao;
import com.chanceit.http.dao.IpcodeCensusDao;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.ConfigInfo;
import com.chanceit.http.pojo.PcodeCensus;
import com.chanceit.http.pojo.PcodeDiary;
import com.chanceit.http.pojo.PcodeInfo;
import com.chanceit.http.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Transactional
@Component("pcodeCensusService")
public class PcodeCensusService implements IpcodeCensusService {
	@Autowired
	@Qualifier("pcodeCensusDao")
	private IpcodeCensusDao pcodeCensusDao;
	
	@Autowired
	@Qualifier("userDao")
	private IUserDao userDao;
	
	private String pcodeUrl;


	/**
	 * 调度任务,每天晚上12点钟执行
	 * 将盒子服务器上的昨天的故障码日志经过统计导入本地表
	 */
	@Override
	public void start(){
		 try {
			String identifiers = "153212024,194506270";
	 		Service service = new Service();
	 		Call call = (Call) service.createCall();
	 		call.setTargetEndpointAddress(new URL(pcodeUrl));
	 		call.setOperationName("getPcodeInfoList");
	 		//String in0 = identifiers;
	 		//获取所有设备识别码
	 		List identifierlist = userDao.getAllIdentifier();
	 		String in0 = identifierlist.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
	 		//获取数据库最大日期
	 		//String in2 = "2014-08-20";
	 		Date maxDate = pcodeCensusDao.getMaxDate();
	 		String in1 = DateUtil.parseDate(maxDate, "yyyy-MM-dd");
	 		if(in1 == null){
	 			in1 = DateUtil.parseDate(new Date(), "yyyy-MM-dd");
	 		}
	 		
	 		System.out.println(in1);
	 		//远程调用service获取为统计完的故障码列表
			String rec = (String) call.invoke(new Object[]{in0,in1});
			
			System.out.println(rec);
			GsonUtil gu = new GsonUtil();
			Gson gson = gu.getGson();
			List<PcodeCensus> ps = gson.fromJson(rec, new TypeToken<List<PcodeCensus>>(){}.getType());
			for(int i = 0; i < ps.size() ; i++){
				PcodeCensus p = ps.get(i);
				//将故障码统计信息写入表
				pcodeCensusDao.save(p);
			    System.out.println(p.toString());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 远程调用服务获取车辆检测信息
	 */
	@Override
	public String getDetectionInfo(String identifiers, Account sessionAccount) throws Exception {
		JSONArray dataArray = new JSONArray();
		//String in0 = "194290502, 253187864";  
		//String in0 = "253187864, 153198340";
		Service service = new Service();
 		Call call = (Call) service.createCall();
 		call.setTargetEndpointAddress(new URL(pcodeUrl));
 		//远程调用盒子服务器获取diary相关信息
 		call.setOperationName("getDetectionInfoList");
 		String rec = (String) call.invoke(new Object[]{identifiers});
	
 		String hql = "from User u where u.identifier in (" + identifiers + ") and u.ifDel = 0 ";
 		List<User> useList = userDao.getList(hql);
 		Map carMap = new HashMap();
 		Map carJsonMap = new HashMap();
 		//将取出来的数据装换成MAP对象 方便后面调用
 		for(User car: useList){
 			carMap.put(car.getIdentifier(), car);
 			JSONObject carJson = new JSONObject();
 			carJson.put("vehicleId", car.getVehicleId());
 			carJson.put("identifier", car.getIdentifier());
 			carJson.put("plate", car.getPlate());
 			carJson.put("codeCounts", new int[]{0,0,0,0});
 			carJsonMap.put(car.getIdentifier(), carJson);
 		}
 		
 		Map map = new HashMap();
 		//配置故障码次数信息
 		JSONArray arr = JSONArray.fromObject(rec);
 		//如果一天内检测多次,则将前一次检测日志信息全部删除掉
 		List ids = pcodeCensusDao.getBeforeRecordIds(identifiers, DateUtil.parseDate(new Date(), "yyyy-MM-dd"));
 		if(ids.size() > 0){
 			pcodeCensusDao.deleteBeforeRecord(ids);
 		}
 		
		for(int i = 0; i < arr.size();i++){
			JSONObject json = (JSONObject) arr.get(i);
			String identifier = json.getString("identifier");
			User tempCar = (User) carMap.get(identifier);
			JSONObject tempJson = (JSONObject) carJsonMap.get(identifier);
			
			//处理故障码
			String codes = json.getString("pcode");
			if(codes != null && !"".equals(codes.trim())){
				String newPcode = codes.replaceAll("\\*", "").replaceAll(" ", "");
				String[] tempCodes = newPcode.split(",");
				//去掉一条数据中的重复的故障码
				List<String> codeList = new ArrayList<String>();  
		        for (int j=0; j<tempCodes.length; j++) {  
		        	//System.out.println(codeList.contains(codes[i]));
		            if(!codeList.contains(tempCodes[j])) {  
		            	codeList.add(tempCodes[j]);  
		            }  
		        }  
		        
		        
		        
		        //根据故障码的首字母将故障码信息封装成[0,0,0,0]的信息
		        //P:动力总成系统   B:盘悬挂系统   C:车身系统   U:网络通讯系统
		        int P = 0;
		        int B = 0;
		        int C = 0;
		        int U = 0;
				for(String pcode: codeList){
					if("P".equals(pcode.substring(0, 1))){
						P++;
					}else if("B".equals(pcode.substring(0, 1))){
						B++;
					}else if("C".equals(pcode.substring(0, 1))){
					    C++;
				    }else{
				    	U++;
				    }
					
					PcodeInfo info = pcodeCensusDao.getPcodeInfo(pcode);
					//将pcode信息写入日志表
					PcodeDiary diary = new PcodeDiary();
					diary.setCar(tempCar);
					diary.setPcode(pcode);
					if(info != null){
						diary.setCodeTitle(info.getCodeTitle());
						diary.setCodeInfo(info.getDescription());
					}else{
						diary.setCodeTitle("");
						diary.setCodeInfo("");
					}
					diary.setCodeDate(new Date());
					pcodeCensusDao.saveDiary(diary);
					
				}
				//按故障大类分类  故障出现次数
				tempJson.put("codeCounts", new int[]{P,B,C,U});
			}
		}
		
		//遍历map,将所有故障检测信息封装成jsonArray
		Set<Map.Entry<String, Integer>> set = carJsonMap.entrySet();
        for (Iterator<Map.Entry<String, Integer>> it = set.iterator(); it.hasNext();) {
            Map.Entry<String, Integer> entry = it.next();
            dataArray.add(entry.getValue());
        }
        
        //在全局配置信息表中插入scanDate
        ConfigInfo config = pcodeCensusDao.getConfigInfo(sessionAccount.getAccountId());
        if(config != null){
        	config.setScanDate(new Date());
        	pcodeCensusDao.saveConfigInfo(config);
        }else{
        	config = new ConfigInfo();
        	config.setScanDate(new Date());
        	config.setAccount(sessionAccount);
        	pcodeCensusDao.saveConfigInfo(config);
        }
            
		return dataArray.toString();
	}
	
	/**
	 * 远程调用服务获取车辆检测信息
	 * 返回格式
	 * [{"identifier":"253225980","info":{"ccount":3,"c":[{"pcode":"C0003","title":"CCCCCC3","info":"CCCCCCCCC"},     
	 *                                                    {"pcode":"C0001","title":"CCCCCC1","info":"CCCCCCCCC"},   
	 *                                                    {"pcode":"C0002","title":"CCCCCC2","info":"CCCCCCCCC"}],
     *                                    "ucount":1,"u":[{"pcode":"U0001","title":"UUUUUU1","info":"UUUUUUUUU"}]}},
     *  {"identifier":"153198340","info":{"bcount":2,"b":[{"pcode":"B0001","title":"BBBBBB1","info":"BBBBBBBBB"},                                                  
     *                                                    {"pcode":"B0002","title":"BBBBBB2","info":"BBBBBBBBB"}],
     *                                    "pcount":1,"p":[{"pcode":"P0001","title":"PPPPPP1","info":"PPPPPPPPP"}]}},
     *  {"identifier":"194724564","info":{"pcount":1,"p":[{"pcode":"P0002","title":"PPPPPP2","info":"PPPPPPPPP"}]}}]
	 */
	@Override
	public String getDetectionDiary(String identifiers, String date) throws Exception {
		List<PcodeDiary> diaryList = pcodeCensusDao.getPcodeDiary(identifiers, date);
		//List<PcodeDiary> diaryList = pcodeCensusDao.getPcodeDiary("194290502", "2014-09-03");
		//List<PcodeDiary> diaryList = pcodeCensusDao.getPcodeDiary("9885,9886,9887", "2014-09-02");
		Map<String, JSONObject> dataMap = new HashMap<String, JSONObject>();
		JSONArray dataArray = new JSONArray();
		for(PcodeDiary diary: diaryList){
			//识别码
			String identifier = diary.getCar().getIdentifier();
			//车牌
			String plate = diary.getCar().getPlate();
			//故障码
			String pcode = diary.getPcode();
			//故障码标题
			String title = diary.getCodeTitle();
			//故障码说明
			String info = diary.getCodeInfo();
			JSONObject json = new JSONObject();
			if(dataMap.get(identifier) == null){
				json.put("identifier", identifier);
				json.put("plate", plate);
				JSONObject infoJson = new JSONObject();
				JSONArray detailArray = new JSONArray();
				JSONObject detailJson = new JSONObject();
				detailJson.put("pcode", pcode);
				detailJson.put("title", title);
				detailJson.put("info", info);
				detailArray.add(detailJson);
				
				 //P:动力总成系统   B:盘悬挂系统   C:车身系统   U:网络通讯系统
		        int P = 0;
		        int B = 0;
		        int C = 0;
		        int U = 0;
				if("P".equals(pcode.substring(0, 1))){
					P = 1;
					infoJson.put("pcount", P);
					infoJson.put("p", detailArray);
				}else if("B".equals(pcode.substring(0, 1))){
					B = 1;
					infoJson.put("bcount", B);
					infoJson.put("b", detailArray);
				}else if("C".equals(pcode.substring(0, 1))){
				    C = 1;
					infoJson.put("ccount", C);
					infoJson.put("c", detailArray);
			    }else{
			    	U = 1;
					infoJson.put("ucount", U);
					infoJson.put("u", detailArray);
			    }
				json.put("info", infoJson);
			}else{
				json = dataMap.get(identifier);
				JSONObject infoJson = json.getJSONObject("info");
				JSONArray detailArray = new JSONArray();
				JSONObject detailJson = new JSONObject();
				detailJson.put("pcode", pcode);
				detailJson.put("title", title);
				detailJson.put("info", info);
				//如果属于相对应的故障码就在其对应的数组下面添加数据并且数量加+1
				//反之就新建一条故障码信息
				if("P".equals(pcode.substring(0, 1))){
					if(infoJson.get("p") != null){
						int pcount = infoJson.getInt("pcount");
						pcount++;
						infoJson.put("pcount", pcount);
						detailArray = infoJson.getJSONArray("p");
						detailArray.add(detailJson);
					}else{
						infoJson.put("pcount", 1);
						detailArray.add(detailJson);
					}
					infoJson.put("p", detailArray);
				}else if("B".equals(pcode.substring(0, 1))){
					if(infoJson.get("b") != null){
						int bcount = infoJson.getInt("bcount");
						bcount++;
						infoJson.put("bcount", bcount);
						detailArray = infoJson.getJSONArray("b");
						detailArray.add(detailJson);
					}else{
						infoJson.put("bcount", 1);
						detailArray.add(detailJson);
					}
					infoJson.put("b", detailArray);
				}else if("C".equals(pcode.substring(0, 1))){
					if(infoJson.get("c") != null){
						int ccount = infoJson.getInt("ccount");
						ccount++;
						infoJson.put("ccount", ccount);
						detailArray = infoJson.getJSONArray("c");
						detailArray.add(detailJson);
					}else{
						infoJson.put("ccount", 1);
						detailArray.add(detailJson);
					}
					infoJson.put("c", detailArray);
			    }else{
			    	if(infoJson.get("u") != null){
						int ucount = infoJson.getInt("ucount");
						ucount++;
						infoJson.put("ucount", ucount);
						detailArray = infoJson.getJSONArray("u");
						detailArray.add(detailJson);
					}else{
						infoJson.put("ucount", 1);
						detailArray.add(detailJson);
					}
					infoJson.put("u", detailArray);
			    }
			}
			dataMap.put(identifier, json);
		}
		
		
		//遍历map,将所有故障检测日志信息封装成jsonArray
		Set<Map.Entry<String, JSONObject>> set = dataMap.entrySet();
        for (Iterator<Map.Entry<String, JSONObject>> it = set.iterator(); it.hasNext();) {
            Map.Entry<String, JSONObject> entry = it.next();
            dataArray.add(entry.getValue());
        }
            
		return dataArray.toString();
	}
	
	
	/**
	 * 获取时间戳信息
	 * @return
	 * @throws JSONException 
	 */
	@Override
	public String getPcodeDates() throws JSONException{
		List dateList = pcodeCensusDao.getPcodeDates();
		JSONArray arr = new JSONArray();
		for(int i = 0;i < dateList.size();i++){
			JSONObject json = new JSONObject();
			Date date = (Date) dateList.get(i);
			String jsonDate = DateUtil.parseDate(date, "yyyy-MM");
			json.put("date", jsonDate);
			arr.add(json);
		}
		return arr.toString();
	}
	
	/**
	 * 根据故障码分组获取统计列表
	 * @return
	 * @throws JSONException 
	 */
	@Override
	public String getCensusList(String date) throws JSONException{
		List censusList = pcodeCensusDao.getCensusList("");
		
		JSONArray arr = new JSONArray();
		for(int i = 0;i < censusList.size();i++){
			JSONObject json = new JSONObject();
			Map map = (Map) censusList.get(i);
			json.put("pcode", map.get("pcode"));
			json.put("count", map.get("count"));
			arr.add(json);
		}
		return arr.toString();
		
	}
	
	/**
	 * 根据accountId获取配置信息
	 */
	@Override
	public ConfigInfo getConfigInfo(Integer accountId) {
		return pcodeCensusDao.getConfigInfo(accountId);
	}
	

	public String getPcodeUrl() {
		return pcodeUrl;
	}

	public void setPcodeUrl(String pcodeUrl) {
		this.pcodeUrl = pcodeUrl;
	}

	
	public static void main(String[] args) {
		/*
		String rec = "[{'codeDate':'Tue Aug 19 00:00:00 CST 2014','pcode':'P0420','codeCount':54,'identifier':153212024}," +
				"{'codeDate':'Tue Aug 19 00:00:00 CST 2014','pcode':'P0430','codeCount':54,'identifier':153212024}," +
				"{'codeDate':'Tue Aug 19 00:00:00 CST 2014','pcode':'P0504','codeCount':11,'identifier':194506270}]";
		
		System.out.println(rec);
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();//不转换没有@Expose注解的字段
		builder.registerTypeAdapter(Timestamp.class, new GsonUtil());
		builder.setDateFormat("yyyy-MM-dd");
		builder.serializeNulls();
		Gson gson = builder.create();
		List<PcodeCensus> ps = gson.fromJson(rec, new TypeToken<List<PcodeCensus>>(){}.getType());
		for(int i = 0; i < ps.size() ; i++)
		{
			PcodeCensus p = ps.get(i);
		    System.out.println(p.toString());
		}*/
		
	}

}
