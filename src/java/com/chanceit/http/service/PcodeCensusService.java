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
	 * ��������,ÿ������12����ִ��
	 * �����ӷ������ϵ�����Ĺ�������־����ͳ�Ƶ��뱾�ر�
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
	 		//��ȡ�����豸ʶ����
	 		List identifierlist = userDao.getAllIdentifier();
	 		String in0 = identifierlist.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
	 		//��ȡ���ݿ��������
	 		//String in2 = "2014-08-20";
	 		Date maxDate = pcodeCensusDao.getMaxDate();
	 		String in1 = DateUtil.parseDate(maxDate, "yyyy-MM-dd");
	 		if(in1 == null){
	 			in1 = DateUtil.parseDate(new Date(), "yyyy-MM-dd");
	 		}
	 		
	 		System.out.println(in1);
	 		//Զ�̵���service��ȡΪͳ����Ĺ������б�
			String rec = (String) call.invoke(new Object[]{in0,in1});
			
			System.out.println(rec);
			GsonUtil gu = new GsonUtil();
			Gson gson = gu.getGson();
			List<PcodeCensus> ps = gson.fromJson(rec, new TypeToken<List<PcodeCensus>>(){}.getType());
			for(int i = 0; i < ps.size() ; i++){
				PcodeCensus p = ps.get(i);
				//��������ͳ����Ϣд���
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
	 * Զ�̵��÷����ȡ���������Ϣ
	 */
	@Override
	public String getDetectionInfo(String identifiers, Account sessionAccount) throws Exception {
		JSONArray dataArray = new JSONArray();
		//String in0 = "194290502, 253187864";  
		//String in0 = "253187864, 153198340";
		Service service = new Service();
 		Call call = (Call) service.createCall();
 		call.setTargetEndpointAddress(new URL(pcodeUrl));
 		//Զ�̵��ú��ӷ�������ȡdiary�����Ϣ
 		call.setOperationName("getDetectionInfoList");
 		String rec = (String) call.invoke(new Object[]{identifiers});
	
 		String hql = "from User u where u.identifier in (" + identifiers + ") and u.ifDel = 0 ";
 		List<User> useList = userDao.getList(hql);
 		Map carMap = new HashMap();
 		Map carJsonMap = new HashMap();
 		//��ȡ����������װ����MAP���� ����������
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
 		//���ù����������Ϣ
 		JSONArray arr = JSONArray.fromObject(rec);
 		//���һ���ڼ����,��ǰһ�μ����־��Ϣȫ��ɾ����
 		List ids = pcodeCensusDao.getBeforeRecordIds(identifiers, DateUtil.parseDate(new Date(), "yyyy-MM-dd"));
 		if(ids.size() > 0){
 			pcodeCensusDao.deleteBeforeRecord(ids);
 		}
 		
		for(int i = 0; i < arr.size();i++){
			JSONObject json = (JSONObject) arr.get(i);
			String identifier = json.getString("identifier");
			User tempCar = (User) carMap.get(identifier);
			JSONObject tempJson = (JSONObject) carJsonMap.get(identifier);
			
			//���������
			String codes = json.getString("pcode");
			if(codes != null && !"".equals(codes.trim())){
				String newPcode = codes.replaceAll("\\*", "").replaceAll(" ", "");
				String[] tempCodes = newPcode.split(",");
				//ȥ��һ�������е��ظ��Ĺ�����
				List<String> codeList = new ArrayList<String>();  
		        for (int j=0; j<tempCodes.length; j++) {  
		        	//System.out.println(codeList.contains(codes[i]));
		            if(!codeList.contains(tempCodes[j])) {  
		            	codeList.add(tempCodes[j]);  
		            }  
		        }  
		        
		        
		        
		        //���ݹ����������ĸ����������Ϣ��װ��[0,0,0,0]����Ϣ
		        //P:�����ܳ�ϵͳ   B:������ϵͳ   C:����ϵͳ   U:����ͨѶϵͳ
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
					//��pcode��Ϣд����־��
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
				//�����ϴ������  ���ϳ��ִ���
				tempJson.put("codeCounts", new int[]{P,B,C,U});
			}
		}
		
		//����map,�����й��ϼ����Ϣ��װ��jsonArray
		Set<Map.Entry<String, Integer>> set = carJsonMap.entrySet();
        for (Iterator<Map.Entry<String, Integer>> it = set.iterator(); it.hasNext();) {
            Map.Entry<String, Integer> entry = it.next();
            dataArray.add(entry.getValue());
        }
        
        //��ȫ��������Ϣ���в���scanDate
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
	 * Զ�̵��÷����ȡ���������Ϣ
	 * ���ظ�ʽ
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
			//ʶ����
			String identifier = diary.getCar().getIdentifier();
			//����
			String plate = diary.getCar().getPlate();
			//������
			String pcode = diary.getPcode();
			//���������
			String title = diary.getCodeTitle();
			//������˵��
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
				
				 //P:�����ܳ�ϵͳ   B:������ϵͳ   C:����ϵͳ   U:����ͨѶϵͳ
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
				//����������Ӧ�Ĺ�����������Ӧ����������������ݲ���������+1
				//��֮���½�һ����������Ϣ
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
		
		
		//����map,�����й��ϼ����־��Ϣ��װ��jsonArray
		Set<Map.Entry<String, JSONObject>> set = dataMap.entrySet();
        for (Iterator<Map.Entry<String, JSONObject>> it = set.iterator(); it.hasNext();) {
            Map.Entry<String, JSONObject> entry = it.next();
            dataArray.add(entry.getValue());
        }
            
		return dataArray.toString();
	}
	
	
	/**
	 * ��ȡʱ�����Ϣ
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
	 * ���ݹ���������ȡͳ���б�
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
	 * ����accountId��ȡ������Ϣ
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
		builder.excludeFieldsWithoutExposeAnnotation();//��ת��û��@Exposeע����ֶ�
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
