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
 * @Description �ͻ��˷����ӿ���
 */
@Results( {	
	@Result(name = "index", location = "index.jsp"),
	@Result(name = "login", location = "login.html")

})
public class IndexAction extends BaseAction{

	/**
	 * @Fields serialVersionUID ���к�
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
	 * @Description TODO(������һ�仰�����������������)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {
		Account account = getSessionAccount();
		if(checkCMD(cmd)){//�����Ҫ��֤��¼״̬��CMD��¼״̬
			if(account == null){
//				writeJson2Page(ResultManager.getFaildResult(ResultManager.EXCEPTION_NO_SESSION,"�û�δ��¼"));
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
			writeJson2Page(ResultManager.getFaildResult("����ת���쳣"));
		} catch (CommonException e) {
			writeJson2Page(ResultManager.getFaildResult(e.getMessage()));
		} catch (RegularException e) {
			writeJson2Page(ResultManager.getFaildRegularResult(e.getMessage()));
		} catch (Exception e) {
			logger.warn("ϵͳ����δ֪�쳣����Ϣ��ʾ�����"+e.getMessage());
			writeJson2Page(ResultManager.getFaildResult(ResultManager.EXCEPTION_500,"ϵͳ�쳣"));
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @author Administrator
	 * @date Jul 2, 2013
	 * @param cmd
	 * @return
	 * @Description ��֤CMD�Ƿ���Ҫ��֤��¼��¼
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
	 * @Description ����Ϣд��ǰ�˵ķ����������ֻ��ͻ��������������ݵ�ʱ����Ҫ����
	 * @see com.chanceit.framework.action.BaseAction#writeJson2Page(java.lang.String)
	 */
	@Override
	public void writeJson2Page(String json){
		if(checkCMDMobile(cmd)){ //����������ֻ��ͻ�����������Ϣ���У�����������
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
//	 * @Description ����˺��Ƿ���Ȩ��
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
