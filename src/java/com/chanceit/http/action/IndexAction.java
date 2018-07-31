/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 22, 2013
 * Id: ApiAction.java,v 1.0 Jun 22, 2013 9:50:49 AM Administrator
 */
package com.chanceit.http.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumHTTP;
import com.chanceit.framework.exceptions.CommonException;
import com.chanceit.framework.exceptions.RegularException;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;

/**
 * @ClassName ApiAction
 * @author Administrator
 * @date Jun 22, 2013 9:50:49 AM
 * @Description 客户端方法接口类
 */
@Results( {	
	@Result(name = "index", location = "index.jsp"),
	@Result(name = "login", location = "login.html")

})
public class IndexAction extends BaseAction{

	/**
	 * @Fields serialVersionUID 序列号
	 */
	private static final long serialVersionUID = -8350898203340761840L;
	
	private static final String MOBILE_REGEXT = "^.*_mobile$";
	
	private String cmd;
	
	
	@Autowired
	@Qualifier("accountView")
	private AccountView accountView;
	
	
	@Autowired
	@Qualifier("userView")
	private UserView userView;
	/** 
	 * @author Administrator
	 * @date Jun 22, 2013
	 * @return
	 * @throws Exception
	 * @Description TODO(这里用一句话描述这个方法的作用)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {
		Account account = getSessionAccount();
		if(checkCMD(cmd)){//检查需要验证登录状态的CMD登录状态
			if(account == null){
//				writeJson2Page(ResultManager.getFaildResult(ResultManager.EXCEPTION_NO_SESSION,"用户未登录"));
//				return "login";
//				super.getRequest().getRequestDispatcher("login.html").forward(super.getRequest(), super.getResponse());
				super.getResponse().sendRedirect("login.html");
				return null;
			}
		}
		
		try {
			userView.list();
			return "index";
		} catch (NumberFormatException e) {
			writeJson2Page(ResultManager.getFaildResult("数字转换异常"));
		} catch (CommonException e) {
			writeJson2Page(ResultManager.getFaildResult(e.getMessage()));
		} catch (RegularException e) {
			writeJson2Page(ResultManager.getFaildRegularResult(e.getMessage()));
		} catch (Exception e) {
			logger.warn("系统发生未知异常，信息提示结果："+e.getMessage());
			writeJson2Page(ResultManager.getFaildResult(ResultManager.EXCEPTION_500,"系统异常"));
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @author Administrator
	 * @date Jul 2, 2013
	 * @param cmd
	 * @return
	 * @Description 验证CMD是否需要验证登录登录
	 */
	private boolean checkCMD(String cmd){
		if(EnumHTTP.ACCOUNT_LOGIN.equalsIgnoreCase(cmd) || EnumHTTP.ACCOUNT_SAVE.equalsIgnoreCase(cmd)
				|| EnumHTTP.ACCOUNT_CHECK.equalsIgnoreCase(cmd)){
			return false;
		}
		if(EnumHTTP.ACCOUNT_LOGOUT.equalsIgnoreCase(cmd)){
			return false;
		}
		return true;
	}
	
	/**
	 * @author Administrator
	 * @date Nov 4, 2013
	 * @param json
	 * @Description 将信息写进前端的方法，增加手机客户端请求敏感数据的时候需要加密
	 * @see com.chanceit.framework.action.BaseAction#writeJson2Page(java.lang.String)
	 */
	@Override
	public void writeJson2Page(String json){
		if(checkCMDMobile(cmd)){ //如果发现是手机客户端请求，且信息敏感，则会加密数据
			super.writeJson2Page(json, null);
		} else {
			super.writeJson2Page(json);
		}
	}
	
//	/**
//	 * @author Administrator
//	 * @date Jul 3, 2013
//	 * @param cmd
//	 * @return
//	 * @Description 检查账号是否有权限
//	 */
//	private boolean checkRighuserg cmd){
//		Account user = getSessionAccount();
//		Set<Rights> rights = account.getRole().getRights();
//		for (Rights rights2 : rights) {
//			if(rights2.getRightCode().equalsIgnoreCase(cmd)){
//				return true;
//			}
//		}
//		return false;
//	}
	
	private boolean checkCMDMobile(String cmd){
		Pattern p = Pattern.compile(MOBILE_REGEXT);
		Matcher m = p.matcher(cmd);
		return m.find();
	}

	/**
	 * @return the cmd
	 */
	public String getCmd() {
		return cmd;
	}

	/**
	 * @param cmd the cmd to set
	 */
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
}
