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
 * @Description 时间的一个调整方法
 */
public class DateUtil {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat dateFormatY = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @return 当前时间字符串
	 * @Description 获得当前静态的时间字符串,以默认的yyyy-MM-dd hh:mm:ss 格式
	 */
	public static String getLocalDateString(){
		return dateFormat.format(new Date());
	}
	
	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @param date 时间
	 * @return
	 * @Description 格式化一个时间,以默认的yyyy-MM-dd hh:mm:ss 格式
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
	 * @param date 时间对象
	 * @param fomat 时间格式
	 * @return 时间字符串
	 * @Description 将时间是格式化成指定的字符串格式
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
	 * @param source 时间字符串
	 * @param fomat 时间格式
	 * @return 格式化好的时间
	 * @throws ParseException 
	 * @Description 按照指定的时间字符串格式化一个时间
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
	 * @param source 日期格式
	 * @return 日期
	 * @throws ParseException 
	 * @Description 将时间字符串格式化成一个时间，以默认的yyyy-MM-dd hh:mm:ss 格式
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
	 * @param format 希望返回的日期格式
	 * @return the date  someone days before the input date
	 * @description 返回输入日期n天前的日期
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
	 * @description 返回输入日期n天前的日期
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
	 * @param format   希望返回的日期格式
	 * @return the date someone days after the input date
	 * @description 返回输入日期n天后的日期
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
	 * @description 返回输入日期n天后的日期
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
	 * @Description 返回输入日期的几个小时之后的时间
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
	 * @Description 返回指定月的最后一天
	 */
	public static String getLastDay(String month) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] datetimes = month.split("-");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.YEAR, Integer.parseInt(datetimes[0]));
		lastDate.set(Calendar.MONTH, Integer.parseInt(datetimes[1]));
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		//lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		str = sdf.format(lastDate.getTime());
		return str;
	}
	

	
	
	/** 
	 * @author gwt
	 * @date Sep 10, 2013
	 * @return
	 * @Description 获取指定月的第一天
	 */
	public static String getFirstDayOfMonth(String month) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] datetimes = month.split("-");
		Calendar firstDate = Calendar.getInstance();
		firstDate.set(Calendar.YEAR,Integer.parseInt(datetimes[0]));
		firstDate.set(Calendar.MONTH,Integer.parseInt(datetimes[1])-1);
		firstDate.set(Calendar.DATE, 1);// 设为当前月的1号
		str = sdf.format(firstDate.getTime());
		return str;
	}
	
	
	
	/** 
	 * @author gwt
	 * @date Sep 10, 2013
	 * @param d
	 * @return
	 * @Description 获取指定月的天数
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
	 *  @Description 获取指定年的月
	 */
	public static Integer getMonth(Date d){
	    Calendar lastDate = Calendar.getInstance();
		   lastDate.setTime(d);
		   lastDate.add(Calendar.MONTH, 1);
		   int month = lastDate.get(Calendar.MONTH);
		return month;
	}
	public static String getDateString(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
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
	 * 获取当天的起始时间
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
