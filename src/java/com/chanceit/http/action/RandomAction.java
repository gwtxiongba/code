/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jul 2, 2013
 * Id: RandomAction.java,v 1.0 Jul 2, 2013 4:24:42 PM Administrator
 */
package com.chanceit.http.action;

import java.io.ByteArrayInputStream;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.utils.RandomNumUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * @ClassName RandomAction
 * @author Administrator
 * @date Jul 2, 2013 4:24:42 PM
 * @Description TODO(������һ�仰��������������)
 */
public class RandomAction extends BaseAction{    
	
	/**
	 * @Fields serialVersionUID serialUID
	 */
	private static final long serialVersionUID = 2094461236388074397L;
	
	private ByteArrayInputStream inputStream;    
	
	@Override
	public String execute() throws Exception{    
		RandomNumUtil rdnu=RandomNumUtil.Instance();    
		this.setInputStream(rdnu.getImage());//ȡ�ô�������ַ�����ͼƬ    
		ActionContext.getContext().getSession().put("random", rdnu.getString());//ȡ������ַ�������HttpSession    
		return SUCCESS;    
	}    
	public void setInputStream(ByteArrayInputStream inputStream) {    
		this.inputStream = inputStream;    
	}    
	public ByteArrayInputStream getInputStream() {    
		return inputStream;    
	}   
}
