package com.chanceit.framework.utils;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chanceit.framework.enums.EnumCommon;
import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @ClassName ResultManager
 * @author Administrator
 * @date May 8, 2013 6:06:41 PM
 * @Description ���ؽ��json������
 */
public class ResultManager {
	
	/**
	 * ȱ��Ȩ��
	 */
	public static final int EXCEPTION_LESS_RIGHT = 1;  
	
	/**
	 * IO�쳣
	 */
	public static final int EXCEPTION_IO_EXCEPTION = 100;
	
	/**
	 * δ��¼
	 */
	public static final int EXCEPTION_NO_SESSION = 0;
	
	/**
	 * �ӿ�δ�ҵ�
	 */
	public static final int EXCEPTION_404 = 404;
	
	/**
	 * 500
	 */
	public static final int EXCEPTION_500 = 500;
	
	/**
	 * �ճ�����
	 */
	public static final int EXCEPTION_COMMON = 2;
	
	/**
	 * ����У���쳣
	 */
	public static final int EXCEPTION_REGULAR = 3;
	
	
	
	/**
	 * ��־ϵͳ
	 */
	protected static final Logger logger = LoggerFactory.getLogger(ResultManager.class);
	
	/**
	 * @author Administrator
	 * @date Jun 24, 2013
	 * @param code
	 * @return
	 * @Description �����쳣��ֻд�쳣����
	 */
	public static String getFaildResult(int code){
		JSONObject result = new JSONObject();
		try {
			result.put("code", code);
		} catch (JSONException e) {
			logger.error("�����ܳ��ֵĴ������ô�����Ϣjson�����쳣"+e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * @author Administrator
	 * @date Jun 24, 2013
	 * @param reason
	 * @return
	 * @Description ��ͨ������Ϣ��ʾ
	 */
	public static String getFaildResult(String reason){
		JSONObject result = new JSONObject();
		try {
			result.put("code",EnumCommon.EXCEPTION_PARA);
			result.put("data", reason);
		} catch (JSONException e) {
			logger.error("�����ܳ��ֵĴ������ô�����Ϣjson�����쳣"+e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * @author Administrator
	 * @date Jul 30, 2013
	 * @param code
	 * @return
	 * @Description �������У�������֤�쳣
	 */
	public static String getFaildRegularResult(String code){
		JSONObject result = new JSONObject();
		try {
			result.put("code", EXCEPTION_REGULAR);
			result.put("data", "�����쳣;�������ID:"+code);
			result.put("name", code);
		} catch (JSONException e) {
			logger.error("�����ܳ��ֵĴ�����������У�������Ϣjson�����쳣"+e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * @author yehao
	 * @date Feb 19, 2013
	 * @param result
	 * @param code
	 * @return
	 * @throws JSONException
	 * @Description ���ô������Ϣ��ʾ
	 */
	public static String getFaildResult(int code,String reason){
		JSONObject result = new JSONObject();
		try {
			result.put("code", code);
			result.put("data", reason);
		} catch (JSONException e) {
			logger.error("�����ܳ��ֵĴ������ô�����Ϣjson�����쳣"+e.getMessage());
		}
		return result.toString();
	}
	
	
	/**
	 * add by zhangxin 2014-09-21
	 * @Description ��׿���ݷ��ظ�ʽ
	 */
	public static String getResultForApp(boolean isOk,Object data){
		JSONObject result = new JSONObject();
		try {
			result.put("isok", isOk);
			result.put("result", data);
		} catch (JSONException e) {
			logger.error("�����ܳ��ֵĴ������ô�����Ϣjson�����쳣"+e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * @author yehao
	 * @date Feb 19, 2013
	 * @param result
	 * @return
	 * @throws JSONException
	 * @Description ���óɹ�����ʾ��Ϣ
	 */
	public static String getSuccResult() {
		JSONObject result = new JSONObject();
		return result.toString();
	}
	
	/**
	 * @author Administrator
	 * @date May 9, 2013
	 * @param flag
	 * @param bodyJson
	 * @return
	 * @Description ���ô���ͨ��Ϣ�Ľ����
	 */
	@Deprecated
	public static String getBodyResult(boolean flag , Object obj){
		JSONObject result = new JSONObject();
		try {
			if(flag){
				result.put("body", obj);
			} else {
				result.put("code", EXCEPTION_COMMON);
			}
		} catch (JSONException e) {
			logger.error("�����ܳ��ֵĴ������ô�����Ϣjson�����쳣"+e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * @author Administrator
	 * @date Jun 24, 2013
	 * @param collection
	 * @return
	 * @Description ���ؼ������л���Ϣ
	 */
	public static String getBodyResult(Collection collection){
		return parseJsonSetDate(collection);
	}
	
	/**
	 * @author Administrator
	 * @date Jun 24, 2013
	 * @param obj
	 * @return
	 * @Description ������ͨ���л���Ϣ
	 */
	public static String getBodyResult(Object obj){
		return parseJsonSetDate(obj);
	}
	
	/**
	 * @author Administrator
	 * @date May 9, 2013
	 * @param flag
	 * @param jsonBody
	 * @return
	 * @Description ����Json������Ϣ�Ľ����,���ת��ʧ�����Զ�������ͨ��ʽ
	 */
	public static String getBodyResultJsonObject(boolean flag , String jsonBody){
		JSONObject result = new JSONObject();
		try {
			if(flag){
//				result.put("code", true);
//				JSONObject bodyJson = new JSONObject(jsonBody);
//				result.put("body", bodyJson);
				return jsonBody;
			} else {
				result.put("code", -1);
				JSONObject bodyJson = new JSONObject(jsonBody);
				result.put("reason", bodyJson);
			}
		} catch (JSONException e) {
			return getBodyResult(flag, jsonBody);
		}
		return result.toString();
	}
	
	/**
	 * @author Administrator
	 * @date May 9, 2013
	 * @param flag
	 * @param jsonBody
	 * @return
	 * @Description ���ת��ʧ�ܣ��������ͨ��ʽ����
	 */
	public static String getBodyResultJsonArray(boolean flag , String jsonBody){
		JSONObject result = new JSONObject();
		try {
			if(flag){
//				result.put("code", true);
//				JSONArray bodyJson = new JSONArray(jsonBody);
//				result.put("body", bodyJson);
				return jsonBody;
			} else {
				result.put("code", -1);
				JSONArray bodyJson = new JSONArray(jsonBody);
				result.put("reason", bodyJson);
			}
		} catch (JSONException e) {
			return getBodyResult(flag, jsonBody);
		}
		return result.toString();
	}
	
	/**
	 * @author yehao
	 * @date Dec 21, 2012
	 * @param obj
	 * @return
	 * @Description ��һ������ת����json��Ĭ��ʱ��yyyy-MM-dd
	 */
	public static String parseJson(Object obj){
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();//��ת��û��@Exposeע����ֶ�
		builder.setDateFormat("yyyy-MM-dd");
		builder.serializeNulls();
		Gson gson = builder.create();
		return gson.toJson(obj);
	}
	
	/**
	 * @author yehao
	 * @date Dec 21, 2012
	 * @param obj
	 * @return
	 * @Description ��һ������ת����json,�ṩʱ��ķ�ʽ
	 */
	public static String parseJson(Object obj ,String dateFormat){
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();//��ת��û��@Exposeע����ֶ�
		builder.setDateFormat(dateFormat);
		builder.serializeNulls();
		Gson gson = builder.create();
		return gson.toJson(obj);
	}
	
	/**
	 * @author yehao
	 * @date Dec 29, 2012
	 * @param obj
	 * @return
	 * @Description �ṩδ��@Expose��ע��Ķ���json����Ĭ��ʱ��yyyy-MM-dd
	 */
	public static String parseJsonNoExpose(Object obj){
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat("yyyy-MM-dd");
		builder.serializeNulls();
		Gson gson = builder.create();
		return gson.toJson(obj);
	}
	
	/**
	 * @author yehao
	 * @date Dec 29, 2012
	 * @param obj
	 * @param dateFormat
	 * @return
	 * @Description �ṩδ��@Expose��ע��Ķ���json�����ṩʱ���Զ���
	 */
	public static String parseJsonNoExpose(Object obj , String dateFormat){
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat(dateFormat);
		builder.serializeNulls();
		Gson gson = builder.create();
		return gson.toJson(obj);
	}
	
	/**
	 * @author yehao
	 * @date Jan 8, 2013
	 * @param obj
	 * @param strategies
	 * @return
	 * @Description ���ӿ����õ����л�������
	 */
	public static String parseJsonExclusionStrategies(Object obj , String dateFormat , ExclusionStrategy... strategies){
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat(dateFormat);
		builder.setExclusionStrategies(strategies);
		builder.serializeNulls();
		Gson gson = builder.create();
		return gson.toJson(obj);
	}
	
	/**
	 * @author yehao
	 * @date Apr 25, 2013
	 * @param obj
	 * @return
	 * @Description ���л��ַ�����ʱ���ʱ���ʽ
	 */
	public static String parseJsonSetDate(Object obj){
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();//��ת��û��@Exposeע����ֶ�
		builder.setDateFormat("yyyy-MM-dd");
		builder.serializeNulls();
		builder.registerTypeAdapter(Timestamp.class, new JsonSerializer<Timestamp>(){//Timestampʱ���ʽ��Ҫ��ʱ����
			@Override
			public JsonElement serialize(Timestamp date, Type type,
					JsonSerializationContext arg2) {
					return new JsonPrimitive(DateUtil.getDate(date));
			}});
		Gson gson = builder.create();
		return gson.toJson(obj);
	}
	
	public static String getJsonByBoolean(boolean bool) throws Exception{
		JSONObject result = new JSONObject();
		if(bool){
			result.put("code", 0);
			return result.toString();
		}else{
			result.put("code", -1);
			result.put("reason", "δ֪����");
			return result.toString();
		}
	}

}
