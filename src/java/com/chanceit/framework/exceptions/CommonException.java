/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 22, 2013
 * Id: CommonException.java,v 1.0 Jun 22, 2013 10:01:00 AM Administrator
 */
package com.chanceit.framework.exceptions;

/**
 * @ClassName CommonException
 * @author Administrator
 * @date Jun 22, 2013 10:01:00 AM
 * @Description 通用异常协议
 */
public class CommonException extends RuntimeException{

	/**
	 * @Fields serialVersionUID 序列号
	 */
	private static final long serialVersionUID = -1732336945187933831L;
	
	public CommonException(){
		super();
	}
	
	public CommonException(String info){
		super(info);
	}
	
	public CommonException(Throwable throwable){
		super(throwable);
	}
	
	public CommonException(String info,Throwable throwable){
		super(info, throwable);
	}

}
