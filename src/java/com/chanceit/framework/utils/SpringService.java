/**
 * Copyright (c) 2011-2012 RoadRover Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Nov 10, 2011
 * Id: SpringService.java,v 1.0 Nov 10, 2011 1:53:00 PM Administrator
 */
package com.chanceit.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName SpringService
 * @author Administrator
 * @date Nov 10, 2011 1:53:00 PM
 * @Description spring接口工具类
 */
@Component("springService")
public class SpringService implements ApplicationContextAware{
	
	private static ApplicationContext applicationContext;

	/** 
	 * @author yehao
	 * @date Nov 10, 2011
	 * @param arg0
	 * @throws BeansException
	 * @Description 设置spring的IOC容器
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringService.applicationContext = applicationContext;
	}
	
	/**
	 * @author yehao
	 * @date Nov 10, 2011
	 * @return IOC容器
	 * @Description 获得IOC容器
	 */
	public static ApplicationContext getApplicationContext(){
		checkApplicationContext();
		return applicationContext;
	}
	
	/**
	 * @author yehao
	 * @date Nov 10, 2011
	 * @Description 检查容易是否为空
	 */
	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException("applicaitonContext未注入,请将该类至于spring管理");
		}
	}
	
	/**
	 * @author yehao
	 * @date Nov 10, 2011
	 * @Description 将容器清空
	 */
	public static void cleanApplicationContext() {
		applicationContext = null;
	}
	
	/**
	 * @author yehao
	 * @date Nov 10, 2011
	 * @param <T>
	 * @param name
	 * @return
	 * @Description 获得bean对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name){
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}

}
