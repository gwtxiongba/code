/**
 * Copyright (c) 2011-2012 RoadRover Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Apr 25, 2013
 * Id: DateConverter.java,v 1.0 Apr 25, 2013 2:01:53 PM Administrator
 */
package com.chanceit.framework.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 * @ClassName DateConverter
 * @author Administrator
 * @date Apr 25, 2013 2:01:53 PM
 * @Description struts2自动转日期的类
 */
public class DateConverter extends StrutsTypeConverter {
	
	private static final DateFormat[] ACCEPT_DATE_FORMATS = {
		  new SimpleDateFormat("yyyy-MM-dd"),
		  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
		  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
		 };
	
	/** 
	 * @author Administrator
	 * @date Apr 25, 2013
	 * @param context
	 * @param values
	 * @param toClass
	 * @return
	 * @Description 重写String转date的方法
	 * @see org.apache.struts2.util.StrutsTypeConverter#convertFromString(java.util.Map, java.lang.String[], java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		
		if (ArrayUtils.isEmpty(values) ||StringUtils.isEmpty(values[0])) {     
            return null;     
        }  
		if(toClass == Date.class){
			String dateString = null;
			dateString = values[0];
			for (DateFormat format : ACCEPT_DATE_FORMATS) {
			   try {
			    return format.parse(dateString);
			   } catch (Exception e) {
			    continue;
			   }
			}
		}
		return null;
	}

	/** 
	 * @author Administrator
	 * @date Apr 25, 2013
	 * @param context
	 * @param o
	 * @return
	 * @Description 重写date转String的方法
	 * @see org.apache.struts2.util.StrutsTypeConverter#convertToString(java.util.Map, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String convertToString(Map context, Object o) {
		 if (o instanceof Date) {    
			for (DateFormat format : ACCEPT_DATE_FORMATS) {
			   try {
			    return format.format(o);
			   } catch (Exception e) {
			    continue;
			   }
			}
	     }  
	     return o.toString(); 
	}
	
	

}
