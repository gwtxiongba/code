package com.chanceit.framework.utils;

import java.lang.reflect.Type;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author zhangxin 2014-08-20
 * �����ƽ̨����gson���ݺ�,���������Զ����date��ʽ������
 *
 */
public class GsonUtil implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp>{
	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * ���л�����
	 */
	@Override
	public JsonElement serialize(Timestamp time, Type arg1,
			JsonSerializationContext arg2) {
		String dfStr = format.format(new Date(time.getTime()));
		return new JsonPrimitive(dfStr);
	}
	
	/**
	 * �����л�����
	 */
	@Override
	public Timestamp deserialize(JsonElement json, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		 if (!(json instanceof JsonPrimitive)) {      
             throw new JsonParseException("The date should be a string value");      
         }      
		 try {      
             Date date = format.parse(json.getAsString());      
             return new Timestamp(date.getTime());      
         } catch (ParseException e) {      
             throw new JsonParseException(e);      
         }   
	}
	
	/**
	 * ��ȡGson����
	 * @param jsonStr
	 * @return
	 */
	public Gson getGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();//��ת��û��@Exposeע����ֶ�
		builder.registerTypeAdapter(Timestamp.class, new GsonUtil());
		builder.setDateFormat("yyyy-MM-dd");
		builder.serializeNulls();
		return builder.create();
	}
	
}
