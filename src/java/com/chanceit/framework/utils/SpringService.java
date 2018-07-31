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
 * @Description spring�ӿڹ�����
 */
@Component("springService")
public class SpringService implements ApplicationContextAware{
	
	private static ApplicationContext applicationContext;

	/** 
	 * @author yehao
	 * @date Nov 10, 2011
	 * @param arg0
	 * @throws BeansException
	 * @Description ����spring��IOC����
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
	 * @return IOC����
	 * @Description ���IOC����
	 */
	public static ApplicationContext getApplicationContext(){
		checkApplicationContext();
		return applicationContext;
	}
	
	/**
	 * @author yehao
	 * @date Nov 10, 2011
	 * @Description ��������Ƿ�Ϊ��
	 */
	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException("applicaitonContextδע��,�뽫��������spring����");
		}
	}
	
	/**
	 * @author yehao
	 * @date Nov 10, 2011
	 * @Description ���������
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
	 * @Description ���bean����
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name){
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}

}
