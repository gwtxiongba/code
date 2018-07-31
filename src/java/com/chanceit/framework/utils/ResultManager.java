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
 * @Description 返回结果json处理类
 */
public class ResultManager {
	
	/**
	 * 缺少权限
	 */
	public static final int EXCEPTION_LESS_RIGHT = 1;  
	
	/**
	 * IO异常
	 */
	public static final int EXCEPTION_IO_EXCEPTION = 100;
	
	/**
	 * 未登录
	 */
	public static final int EXCEPTION_NO_SESSION = 0;
	
	/**
	 * 接口未找到
	 */
	public static final int EXCEPTION_404 = 404;
	
	/**
	 * 500
	 */
	public static final int EXCEPTION_500 = 500;
	
	/**
	 * 日常出错
	 */
	public static final int EXCEPTION_COMMON = 2;
	
	/**
	 * 正则校验异常
	 */
	public static final int EXCEPTION_REGULAR = 3;
	
	
	
	/**
	 * 日志系统
	 */
	protected static final Logger logger = LoggerFactory.getLogger(ResultManager.class);
	
	/**
	 * @author Administrator
	 * @date Jun 24, 2013
	 * @param code
	 * @return
	 * @Description 错误异常，只写异常代号
	 */
	public static String getFaildResult(int code){
		JSONObject result = new JSONObject();
		try {
			result.put("code", code);
		} catch (JSONException e) {
			logger.error("不可能出现的错误：设置错误信息json引用异常"+e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * @author Administrator
	 * @date Jun 24, 2013
	 * @param reason
	 * @return
	 * @Description 普通错误信息提示
	 */
	public static String getFaildResult(String reason){
		JSONObject result = new JSONObject();
		try {
			result.put("code",EnumCommon.EXCEPTION_PARA);
			result.put("data", reason);
		} catch (JSONException e) {
			logger.error("不可能出现的错误：设置错误信息json引用异常"+e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * @author Administrator
	 * @date Jul 30, 2013
	 * @param code
	 * @return
	 * @Description 获得正则校验错误验证异常
	 */
	public static String getFaildRegularResult(String code){
		JSONObject result = new JSONObject();
		try {
			result.put("code", EXCEPTION_REGULAR);
			result.put("data", "参数异常;错误参数ID:"+code);
			result.put("name", code);
		} catch (JSONException e) {
			logger.error("不可能出现的错误：设置正则校验错误信息json引用异常"+e.getMessage());
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
	 * @Description 设置错误的信息提示
	 */
	public static String getFaildResult(int code,String reason){
		JSONObject result = new JSONObject();
		try {
			result.put("code", code);
			result.put("data", reason);
		} catch (JSONException e) {
			logger.error("不可能出现的错误：设置错误信息json引用异常"+e.getMessage());
		}
		return result.toString();
	}
	
	
	/**
	 * add by zhangxin 2014-09-21
	 * @Description 安卓数据返回格式
	 */
	public static String getResultForApp(boolean isOk,Object data){
		JSONObject result = new JSONObject();
		try {
			result.put("isok", isOk);
			result.put("result", data);
		} catch (JSONException e) {
			logger.error("不可能出现的错误：设置错误信息json引用异常"+e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * @author yehao
	 * @date Feb 19, 2013
	 * @param result
	 * @return
	 * @throws JSONException
	 * @Description 设置成功的提示信息
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
	 * @Description 设置带普通消息的结果集
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
			logger.error("不可能出现的错误：设置错误信息json引用异常"+e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * @author Administrator
	 * @date Jun 24, 2013
	 * @param collection
	 * @return
	 * @Description 返回集合序列化信息
	 */
	public static String getBodyResult(Collection collection){
		return parseJsonSetDate(collection);
	}
	
	/**
	 * @author Administrator
	 * @date Jun 24, 2013
	 * @param obj
	 * @return
	 * @Description 返回普通序列化信息
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
	 * @Description 设置Json对象消息的结果集,如果转化失败则自动采用普通方式
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
	 * @Description 如果转化失败，则采用普通方式设置
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
	 * @Description 将一个对象转化成json，默认时间yyyy-MM-dd
	 */
	public static String parseJson(Object obj){
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();//不转换没有@Expose注解的字段
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
	 * @Description 将一个对象转化成json,提供时间的方式
	 */
	public static String parseJson(Object obj ,String dateFormat){
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();//不转换没有@Expose注解的字段
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
	 * @Description 提供未按@Expose的注解的对象json化，默认时间yyyy-MM-dd
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
	 * @Description 提供未按@Expose的注解的对象json化，提供时间自定义
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
	 * @Description 增加可配置的序列化方法的
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
	 * @Description 序列化字符串的时候带时间格式
	 */
	public static String parseJsonSetDate(Object obj){
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();//不转换没有@Expose注解的字段
		builder.setDateFormat("yyyy-MM-dd");
		builder.serializeNulls();
		builder.registerTypeAdapter(Timestamp.class, new JsonSerializer<Timestamp>(){//Timestamp时间格式需要带时分秒
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
			result.put("reason", "未知错误！");
			return result.toString();
		}
	}

}
