package com.chanceit.framework.utils;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @ClassName StringUtil
 * @author chenjie
 * @date Dec 5, 2011 10:58:30 AM
 * @Description ����String�Ĺ�����
 */
public class StringUtil {

	/**
	 * @author chenjie
	 * @date Dec 5, 2011
	 * @param str
	 * @return
	 * @Description ����ַ������˵ĵ�/˫����
	 */
	public static String clearQuote(String str) {
		str = str.trim();
		if ((str.startsWith("\"") && str.endsWith("\""))
				|| (str.startsWith("'") && str.endsWith("'"))) {
			return str.substring(1, str.length() - 1);
		} else {
			return str;
		}
	}

	/**
	 * @author chenjie
	 * @date Dec 5, 2011
	 * @param regexStr
	 *            ������ʽ
	 * @param content
	 *            ��ƥ���ַ���
	 * @param group
	 *            ƥ�������ڵķ���
	 * @return ƥ��Ľ��
	 * @Description ����ƥ��һ�����
	 */
	public static String regexMatchOne(String regexStr, String content,
			int group) {
		Pattern p = Pattern.compile(regexStr);
		Matcher m = p.matcher(content);
		String str = "";
		if (m.find()) {
			str = m.group(group).trim();
		}
		return str;
	}

	/**
	 * @author chenjie
	 * @date Dec 5, 2011
	 * @param regexStr
	 *            ������ʽ
	 * @param content
	 *            ��ƥ����ַ���
	 * @return ƥ��Ķ�����
	 * @Description ����ƥ�������
	 */
	public static List<String> regexMatchMore(String regexStr, String content) {
		return regexMatchMore(regexStr, content, 0);
	}

	/**
	 * @author chenjie
	 * @date Dec 5, 2011
	 * @param regexStr
	 *            ������ʽ
	 * @param content
	 *            ��ƥ����ַ���
	 * @param group
	 *            �ڲ�������
	 * @return ƥ��Ķ�����
	 * @Description ����ƥ�������
	 */
	public static List<String> regexMatchMore(String regexStr, String content,
			int group) {
		Pattern p = Pattern.compile(regexStr);
		Matcher m = p.matcher(content);
		List<String> list = new ArrayList<String>();
		while (m.find()) {
			String str = m.group(group).trim();
			list.add(str);
		}
		return list;
	}

	/**
	 * @author chenjie
	 * @date Dec 6, 2011
	 * @param map
	 * @return
	 * @Description ��map����ת�����ַ���
	 */
	public static String toString(Map<?, ?> map) {
		StringBuffer buf = new StringBuffer();
		Set<?> keys = map.keySet();
		for (Object key : keys) {
			Object value = map.get(key);
			if (value instanceof Map)
				buf.append(key).append(":").append(toString((Map<?, ?>) value))
						.append("\n");
			else {
				buf.append(key).append("=").append(value).append(" ");
			}
		}
		return buf.toString();
	}

	/**
	 * ������SQLע���ַ���
	 */
	public static String[] SQL_INJECTION_STRING = { "select", "union",
			"update", "delete", "insert", "into", "'", "creat", "and", "where",
			"1=1", "1=2", "/*", "//", "\\", "///", "/", "\\\\", "drop",
			"lock table", "grant", "ascii", "count", "chr", "mid", "master",
			"truncate" };

	/**
	 * @author chenjie
	 * @date Dec 14, 2011
	 * @param context
	 *            ԭʼ�ַ�������
	 * @return ���˵�������SQLע���ַ������ַ���
	 * @Description ��ȡ���˵�������SQLע���ַ������ַ���
	 */
	@Deprecated
	public static String getSQLFilterContext(String context) {
		if (context == null)
			return "";
		for (int i = 0; i < SQL_INJECTION_STRING.length; i++)
			context = context.replaceAll("(?i)" + SQL_INJECTION_STRING[i], "");
		return context;
	}

	/**
	 * @author chenjie
	 * @date Dec 16, 2011
	 * @param resultCode ʧ�ܷ����룬��com.roadrover.framework.enums.EnumCommon�ĳ�����ȡֵ
	 * @param msg ʧ����Ϣ
	 * @return ����WebServiceʧ�ܵ�XML
	 * @Description ��������WebServiceʧ�ܵ�XML��createNotOKWebServiceXML(String resultCode, String msg)����������
	 */
	public static String createNotOKWebServiceXML(int resultCode, String msg) {
		return createNotOKWebServiceXML(String.valueOf(resultCode), msg);
	}
	/**
	 * @author chenjie
	 * @date Dec 16, 2011
	 * @param resultCode ʧ�ܷ����룬��com.roadrover.framework.enums.EnumCommon�ĳ�����ȡֵ
	 * @param msg ʧ����Ϣ
	 * @return ����WebServiceʧ�ܵ�XML
	 * @Description ��������WebServiceʧ�ܵ�XML
	 */
	public static String createNotOKWebServiceXML(String resultCode, String msg) {
		StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		xml.append("<response>\n<result>").append(resultCode).append("</result>\n");
		xml.append("<resultMsg>").append(msg).append("</resultMsg>\n</response>");
		return xml.toString();
	}
	
	/**
	 * @author yehao
	 * @date Dec 21, 2012
	 * @return
	 * @Description ����һ��δ��½��״̬json�ַ���
	 */
	public static String createNoSessionJson(){
		JSONObject result = new JSONObject();
		try {
			result.put("code", "faild");
			result.put("reason", "δ��¼");
		} catch (JSONException e) {
			e.printStackTrace();
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
		builder.registerTypeAdapter(Timestamp.class, new JsonSerializer<Timestamp>(){//Timestampʱ���ʽ��Ҫ��ʱ����
			@Override
			public JsonElement serialize(Timestamp date, Type type,
					JsonSerializationContext arg2) {
					return new JsonPrimitive(DateUtil.getDate(date));
			}});
		Gson gson = builder.create();
		return gson.toJson(obj);
	}
	
	
	/**
	  * ����ת��λ����ƴ������ĸ��Ӣ���ַ�����
	  * add by zhangxin 2014-08-13
	  * @param chines ����
	  * @return ƴ��
	  */
    public static String converterToFirstSpell(String chines){    	 
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
            	pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }
 
    /**
      * ����ת��λ����ƴ����Ӣ���ַ�����
      * add by zhangxin 2014-08-13
      * @param chines ����
      * @return ƴ��
      */
    public static String converterToSpell(String chines){    	 
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
            	pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }
    
}







