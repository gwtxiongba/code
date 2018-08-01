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

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.enums.EnumHTTP;
import com.chanceit.framework.exceptions.CommonException;
import com.chanceit.framework.exceptions.RegularException;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.service.IAccountService;

/**
 * @ClassName ApiAction
 * @author Administrator
 * @date Jun 22, 2013 9:50:49 AM
 * @Description �ͻ��˷����ӿ���
 */
@Results( {
	@Result(name = "login", location = "login.html"),
	@Result(name = "index", location = "index.jsp")
})
public class ApiAction extends BaseAction{

	/**
	 * @Fields serialVersionUID ���к�
	 */
	private static final long serialVersionUID = -8350898203340761840L;
	
	private static final String MOBILE_REGEXT = "^.*_mobile$";
	
	private String cmd="login";
	
	
	@Autowired
	@Qualifier("accountView")
	private AccountView accountView;
	
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
		//System.out.println(getRequest().getRequestURI()+"...cmd:"+cmd);
		
		
		Account account = getSessionAccount();
		String ifapp = getParameter("ifapp");
		if(checkCMD(cmd) && ifApp(ifapp)){//�����Ҫ��֤��¼״̬��CMD��¼״̬
			if(account == null){
				//Account account1 = accountService.getByName("sys");getSession().setAttribute("account", account1);
				writeJson2Page(ResultManager.getFaildResult(EnumCommon.EXCEPTION_NO_SESSION,"��δ��¼ϵͳ��ʱ��δ����"));
				return null;
			}
		}
		
		try {
			if(EnumHTTP.GOLBAL.equals(cmd)){
				writeJson2Page(accountView.gloabal());
			}else if(EnumHTTP.SET_PWD.equals(cmd)){
				writeJson2Page(accountView.set());
			}else if(EnumHTTP.ACCOUNT_RESET_PASSWORD.equals(cmd)){
				writeJson2Page(accountView.resetPassword());
			} else 
			if(EnumHTTP.ACCOUNT_LOGIN.equals(cmd)){
				writeJson2Page(accountView.login());
			} else 
			if(EnumHTTP.ACCOUNT_LOGOUT.equals(cmd)){
				writeJson2Page(accountView.logout());
			}else 
			if(EnumHTTP.SAVECOMPANY.equals(cmd)){
				writeJson2Page(accountView.save());
			} else 
			if(EnumHTTP.SETCOMPANY.equals(cmd)){
				writeJson2Page(accountView.edit());
			} else 	
			if(EnumHTTP.DELCOMPANY.equals(cmd)){	//
				writeJson2Page(accountView.delete());
			} else 	
			if(EnumHTTP.LISTOPERATOR.equals(cmd)){	//����Ա�б�
				writeJson2Page(accountView.list(page));
			} else 
			if(EnumHTTP.DELOPERATOR.equals(cmd)){	//ɾ����Ա
				writeJson2Page(accountView.delete());
			} 
			else if(EnumHTTP.ACCOUNTNAME_CHECK.equals(cmd)){
				writeJson2Page(accountView.checkAccountName());
		
			}else if(EnumHTTP.ACCOUNT_CHECK_NAME.equals(cmd)){
				writeJson2Page(accountView.checkAccName());
			
			}
			else 
			if(EnumHTTP.CHECK_UID.equals(cmd)){
//				writeJson2Page(userView.checkUid());
			}else 	
			if(EnumHTTP.SAVEMENU.equals(cmd)){
				writeJson2Page(accountView.saveMenu());
			}
			
//			else if(EnumHTTP.RESETACCOUNT.equals(cmd)){	//����Ա�˻���������
//				writeJson2Page(accountView.resetAccount());
//			}
			
			else {
				writeJson2Page(ResultManager.getFaildResult(ResultManager.EXCEPTION_404,"�ӿڷ����Ҳ���"));
			}
		} catch (NumberFormatException e) {
			writeJson2Page(ResultManager.getFaildResult("����ת���쳣"));
		} catch (CommonException e) {
			writeJson2Page(ResultManager.getFaildResult(e.getMessage()));
		} catch (RegularException e) {
			writeJson2Page(ResultManager.getFaildRegularResult(e.getMessage()));
		}catch (ObjectNotFoundException oe){
			writeJson2Page(ResultManager.getFaildResult(ResultManager.EXCEPTION_500,"û�ж�Ӧ�Ĺ�˾������ϵϵͳ����Ա"));
		}
		
		catch (Exception e) {
			logger.warn("ϵͳ����δ֪�쳣����Ϣ��ʾ�����"+e.getMessage()+e.getStackTrace()+"cmd="+cmd);
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
		if(EnumHTTP.ACCOUNT_CHECK_NAME.equals(cmd)||EnumHTTP.ACCOUNT_CHECK_EMAIL.equals(cmd)||EnumHTTP.ACCOUNT_UPDATE_EMAIL.equals(cmd)){
			return false;
		}
		//������������˺ż������
		if(EnumHTTP.ACCOUNTNAME_CHECK .equals(cmd)||EnumHTTP.ACCOUNT_SENDPWD_EMAIL.equals(cmd)||
				EnumHTTP.ACCOUNT_CHECK_RANDOMCODE.equals(cmd)||EnumHTTP.ACCOUNT_RESET_PASSWORD.equals(cmd)||EnumHTTP.ACCOUNT_ACTIVATE_EMAIL.equals(cmd)||EnumHTTP.ACCOUNT_ACTIVATE_HREF.equals(cmd)){
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
	
	//�ж��Ƿ�Ϊ�ֻ��ͻ��˷���
	public boolean ifApp(String ifapp){
		if(ifapp != null && !"".equals(ifapp) && "1".equals(ifapp)){
			return false;
		}
		return true;
	}
	
	
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
