/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jul 30, 2013
 * Id: RegularException.java,v 1.0 Jul 30, 2013 2:13:13 PM Administrator
 */
package com.chanceit.framework.exceptions;

/**
 * @ClassName RegularException
 * @author Administrator
 * @date Jul 30, 2013 2:13:13 PM
 * @Description 正则表达式的校验异常
 */
public class RegularException extends RuntimeException {

	/**
	 * @Fields serialVersionUID serialUID
	 */
	private static final long serialVersionUID = -4594403363793252754L;
	
	public RegularException(){
		super();
	}
	
	public RegularException(String info){
		super(info);
	}
	
	public RegularException(Throwable throwable){
		super(throwable);
	}
	
	public RegularException(String info,Throwable throwable){
		super(info, throwable);
	}

}
