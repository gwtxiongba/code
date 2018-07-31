/**
 * Copyright (c) 2011-2012 RoadRover Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Dec 16, 2013
 * Id: TestMessage.java,v 1.0 Dec 16, 2013 11:38:29 AM Administrator
 */
package com.chanceit.tests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.chanceit.http.pojo.GhAction;

/**
 * @ClassName TestMessage
 * @author Administrator
 * @date Dec 16, 2013 11:38:29 AM
 * @Description TODO(������һ�仰��������������)
 */
public class TestMessage {
	
	public static SessionFactory sessionFactory ;
	
	@Before
	public void before(){
		String[] a = {"spring-base.xml","spring-expand.xml"};
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(a);
		sessionFactory = (SessionFactory) context.getBean("sessionFactory");
	}
	
	@Test
	public void checkPoint(){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		GhAction ac= new GhAction();
		ac.setId(1);
		ac.setPid(1);
		ac.setUid("1542");
		session.save(ac);
		session.getTransaction().commit();
		//System.out.println(socketMessageService.checkPoint("10753"));
	}

}
