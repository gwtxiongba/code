package com.chanceit.http.service;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component("getPosition")
public class PositionWebService implements IPositionWebService {

	/**
	 * 批量获取位置点，uid参数格式：10753,10644,11085,11788
	 * 手机用
	 */
//	@Override
//	public String getPositions(String uids){
//		JSONObject rs = new JSONObject(); 
//		try {
//			JSONArray jsonAryPos = new JSONArray();
//			Map socketRsMap = socketMessageService.checkPoint(uids);//"10753,10644,11085,11788"
//			String[] uidAry = uids.split(",");
//			if(socketRsMap!=null && socketRsMap.size()==uidAry.length){
//					for(int i=0;i<uidAry.length;i++){
//						JSONObject results = (JSONObject)socketRsMap.get(uidAry[i]);
//						jsonAryPos.put(results);
//					}
//						rs.put("code", 0);
//						rs.put("rs", jsonAryPos.toString());
//						return  rs.toString();
//			}else{
//				rs.put("code", 1); //获取的位置点异常
//				rs.put("rs", "获取的位置点异常");
//				return rs.toString();
//			}
//		} catch (Exception e) {
//			return "{'code':2,'rs':'系统异常！'}";
//		}
//	}
	
}
