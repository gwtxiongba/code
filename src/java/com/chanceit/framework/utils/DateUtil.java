/**
 * Copyright (c) 2011-2012 RoadRover Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Nov 17, 2011
 * Id: DateUtil.java,v 1.0 Nov 17, 2011 10:48:35 AM Administrator
 */
package com.chanceit.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName DateUtil
 * @author yehao
 * @date Nov 17, 2011 10:48:35 AM
 * @Description ʱ���һ����������
 */
public class DateUtil {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat dateFormatY = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @return ��ǰʱ���ַ���
	 * @Description ��õ�ǰ��̬��ʱ���ַ���,��Ĭ�ϵ�yyyy-MM-dd hh:mm:ss ��ʽ
	 */
	public static String getLocalDateString(){
		return dateFormat.format(new Date());
	}
	
	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @param date ʱ��
	 * @return
	 * @Description ��ʽ��һ��ʱ��,��Ĭ�ϵ�yyyy-MM-dd hh:mm:ss ��ʽ
	 */
	public static String getDate(Date date){
		return dateFormat.format(date);
	}
	public static String getDateY(Date date){
		return dateFormatY.format(date);
	}
	/**
	 * @author yehao
	 * @date Dec 9, 2011
	 * @param date ʱ�����
	 * @param fomat ʱ���ʽ
	 * @return ʱ���ַ���
	 * @Description ��ʱ���Ǹ�ʽ����ָ�����ַ�����ʽ
	 */
	public static String parseDate(Date date , String fomat){
		if(date == null || fomat == null) return null;
		String source = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(fomat);
		source = dateFormat.format(date);
		return source;
	}
	
	/**
	 * @author yehao
	 * @date Dec 9, 2011
	 * @param source ʱ���ַ���
	 * @param fomat ʱ���ʽ
	 * @return ��ʽ���õ�ʱ��
	 * @throws ParseException 
	 * @Description ����ָ����ʱ���ַ�����ʽ��һ��ʱ��
	 */
	public static Date parseDate(String source , String fomat) throws ParseException{
		if(StringUtils.isEmpty(source) || fomat == null) return null;
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(fomat);
		date = dateFormat.parse(source);
		return date;
	}
	
	public static long sub(long time){
		long current = System.currentTimeMillis()/1000;
//		try {
//		System.out.println(current);
//			Thread.sleep(1000);
//			System.out.println(System.currentTimeMillis()/1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		return current-time;
	}
	
	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @param source ���ڸ�ʽ
	 * @return ����
	 * @throws ParseException 
	 * @Description ��ʱ���ַ�����ʽ����һ��ʱ�䣬��Ĭ�ϵ�yyyy-MM-dd hh:mm:ss ��ʽ
	 */
	public static Date getDate(String source) throws ParseException{
		Date date = null;
		date = dateFormat.parse(source);
		return date;
	}
	
	/**
	 * @author dj
	 * @date Dec 10,2011
	 * @param date
	 * @param days
	 * @param format ϣ�����ص����ڸ�ʽ
	 * @return the date  someone days before the input date
	 * @description ������������n��ǰ������
	 */
	public static String getBeforeDate(Date date,int days,String format)   
	{   
		if(date == null) return null;
	    SimpleDateFormat df = new SimpleDateFormat(format);   
	    Calendar calendar = Calendar.getInstance();      
	    calendar.setTime(date);   
	    calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) - days);   
	    return df.format(calendar.getTime());   
	}
	
	/**
	 * @author dj
	 * @date Dec 10,2011
	 * @param date
	 * @param days
	 * @return the date  someone days before the input date
	 * @description ������������n��ǰ������
	 */
	public static Date getBeforeDate(Date date,int days)   
	{   
		if(date == null) return null;
	    Calendar calendar = Calendar.getInstance();      
	    calendar.setTime(date);   
	    calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) - days);   
	    return calendar.getTime();   
	}
	
	/**
	 * @author dj
	 * @date Dec 10,2011
	 * @param date
	 * @param days
	 * @param format   ϣ�����ص����ڸ�ʽ
	 * @return the date someone days after the input date
	 * @description ������������n��������
	 */
	public static String getAfterDate(Date date,int days,String format)   
	{   
		if(date == null) return null;
	    SimpleDateFormat df = new SimpleDateFormat(format);   
	    Calendar calendar = Calendar.getInstance();      
	    calendar.setTime(date);   
	    calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) + days);   
	    return df.format(calendar.getTime());   
	}
	
	/**
	 * @author dj
	 * @date Dec 10,2011
	 * @param date
	 * @param days
	 * @return the date someone days after the input date
	 * @description ������������n��������
	 */
	public static Date getAfterDate(Date date,int days)   
	{   
		if(date == null) return null;
	    Calendar calendar = Calendar.getInstance();      
	    calendar.setTime(date);   
	    calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) + days);   
	    return calendar.getTime();   
	}
	/**
	 * 
	 * @author Administrator
	 * @date Jan 15, 2014
	 * @param date
	 * @param hours
	 * @return
	 * @Description �����������ڵļ���Сʱ֮���ʱ��
	 */
	public static Date getAfterHoursDate(Date date,int hours)   
	{   
		if(date == null) return null;
	    Calendar calendar = Calendar.getInstance();      
	    calendar.setTime(date);   
	    calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY) + hours);   
	    return calendar.getTime();   
	}
	
	
	/** 
	 * @author gwt
	 * @date Sep 10, 2013
	 * @return
	 * @Description ����ָ���µ����һ��
	 */
	public static String getLastDay(String month) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] datetimes = month.split("-");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.YEAR, Integer.parseInt(datetimes[0]));
		lastDate.set(Calendar.MONTH, Integer.parseInt(datetimes[1]));
		lastDate.set(Calendar.DATE, 1);// ��Ϊ��ǰ�µ�1��
		//lastDate.add(Calendar.MONTH, 1);// ��һ���£���Ϊ���µ�1��
		lastDate.add(Calendar.DATE, -1);// ��ȥһ�죬��Ϊ�������һ��

		str = sdf.format(lastDate.getTime());
		return str;
	}
	

	
	
	/** 
	 * @author gwt
	 * @date Sep 10, 2013
	 * @return
	 * @Description ��ȡָ���µĵ�һ��
	 */
	public static String getFirstDayOfMonth(String month) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] datetimes = month.split("-");
		Calendar firstDate = Calendar.getInstance();
		firstDate.set(Calendar.YEAR,Integer.parseInt(datetimes[0]));
		firstDate.set(Calendar.MONTH,Integer.parseInt(datetimes[1])-1);
		firstDate.set(Calendar.DATE, 1);// ��Ϊ��ǰ�µ�1��
		str = sdf.format(firstDate.getTime());
		return str;
	}
	
	
	
	/** 
	 * @author gwt
	 * @date Sep 10, 2013
	 * @param d
	 * @return
	 * @Description ��ȡָ���µ�����
	 */
	public static int getMonthDays(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String date=sdf.format(d);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7)) -1);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return maxDay;
	}
	
	/**
	 * @param d
	 * @return
	 *  @Description ��ȡָ�������
	 */
	public static Integer getMonth(Date d){
	    Calendar lastDate = Calendar.getInstance();
		   lastDate.setTime(d);
		   lastDate.add(Calendar.MONTH, 1);
		   int month = lastDate.get(Calendar.MONTH);
		return month;
	}
	public static String getDateString(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��");
		return sdf.format(d);
	}
	
	public static String getDateStrings(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(d);
	}

	public static String getDateByMills(long mills){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mills);
		return parseDate(calendar.getTime(),"yyyy-MM-dd HH:mm:ss");
	}
	
	/*
	 * add begin by zhangyejia 2014-12-24
	 * ��ȡ�������ʼʱ��
	 */
	public static String getDateFirstString(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return dateFormat.format(calendar.getTime());
	}
	
	public static String getDateLastString(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return dateFormat.format(calendar.getTime());
	}
	 // add end by zhangyejia 2014-12-24
}
