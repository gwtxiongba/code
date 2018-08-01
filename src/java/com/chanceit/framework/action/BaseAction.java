/**
 * Copyright (c) 2011-2012 RoadRover Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Oct 31, 2011
 * Id: BaseAction.java,v 1.0 Oct 31, 2011 2:45:57 PM yehao
 */
package com.chanceit.framework.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.chanceit.common.inits.RegularConfig;
import com.chanceit.framework.enums.EnumCommon;
//import com.chanceit.framework.exception.RegularException;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @ClassName BaseAction
 * @author yehao
 * @date Oct 31, 2011 2:45:57 PM
 * @Description action的基类，对actionsupport 根据项目要求进行了轻度封装
 */
public abstract class BaseAction extends ActionSupport implements Preparable{
	

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 12064156654156L;

	/**
	 * 日志系统
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	protected Page page = new Page(EnumCommon.PAGE_SIZE);
	
	/**
	 * @author yehao
	 * @date Oct 31, 2011
	 * @return
	 * @Description 获得request
	 */
	public HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	
	/**
	 * @author yehao
	 * @date Oct 31, 2011
	 * @return
	 * @Description 获得response
	 */
	public HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}
	
	/**
	 * @author yehao
	 * @date Oct 31, 2011
	 * @return
	 * @Description 获得session
	 */
	public HttpSession getSession(){
		return getRequest().getSession();
	}
	
	/**
	 * @author yehao
	 * @date Nov 29, 2011
	 * @param path
	 * @return 服务器相对路径
	 * @Description 获得某个文件夹中的服务器相对路径
	 */
	public static String getBasePath(String path){
		return ServletActionContext.getServletContext().getRealPath(path);
	}
	
	/**
	 * @author Administrator
	 * @date Jul 30, 2013
	 * @param code
	 * @return
	 * @Description 获取验证参数
	 */
	public String getParameter(String code){
		String value = getRequest().getParameter(code);
		if(StringUtils.isNotEmpty(value)){
			value = value.trim();
//			if(!RegularConfig.checkNameRegular(code, value)) throw new RegularException(code);
		}
		return value;
	}
	
	/**
	 * @author Administrator
	 * @date Aug 13, 2013
	 * @param code
	 * @return
	 * @Description 获取非正则的接口
	 */
	public String getParameterNoCheck(String code){
		String value = getRequest().getParameter(code);
		if(StringUtils.isNotEmpty(value)){
			value = value.trim();
		}
		return value;
	}
	
	/**
	 * @author yehao
	 * @date Jan 4, 2013
	 * @param json
	 * @Description 编写json字符串去页面
	 */
	public void writeJson2Page(String json){
		try {
			getResponse().setContentType("text/html;charset=utf-8");
			getResponse().getWriter().write(json);
			getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * @author Administrator
	 * @date Nov 8, 2011
	 * @throws Exception
	 * @Description 初始化参数
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	@Override
	public void prepare() throws Exception {
		String pageNo = getRequest().getParameter("page");
		String pageSize = getRequest().getParameter("pageSize");
		try {
			if(pageNo != null && !pageNo.equals("")){
				page.setPageNo(Integer.parseInt(pageNo));
			}
			if(pageSize != null && !pageSize.equals("")){
				page.setPageSize(Integer.parseInt(pageSize));
			} else {
				checkPageSize(EnumCommon.PAGE_SIZE);
			}
		} catch (NumberFormatException e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * @author yehao
	 * @date Jan 4, 2013
	 * @Description 设置page的大小
	 */
	protected void checkPageSize(int pageSize){
		page.setPageSize(pageSize);
	}
	
	/**
	 * @author Administrator
	 * @date Jul 2, 2013
	 * @return
	 * @Description 获得当前登录对象
	 */
	public Account getSessionAccount(){
		return (Account) getSession().getAttribute("account");
	}
	
	/**
	 * @author Administrator
	 * @date Nov 4, 2013
	 * @param json
	 * @param key
	 * @Description 以DES加密的方式将字符串写到页面
	 */
	public void writeJson2Page(String json,String key){
		try {
			getResponse().setContentType("text/html;charset=utf-8");
			getResponse().addHeader("ContentEncoding", "DES");
//			Des des = new Des();
//			json = des.enCrypto(json);
			getResponse().getWriter().write(json);
			getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			logger.warn("DES加密失败，错误信息："+e.getMessage());
		}
	}
}
