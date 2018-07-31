/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.framework.utils.StringUtil;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Driver;
import com.chanceit.http.pojo.Member;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IOperatorTeamService;
import com.chanceit.http.service.IMemberService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;
import com.chanceit.http.service.TeamService;

/**
 * @ClassName MemberView
 * @author Administrator
 * @date 
 * @Description ����������ؽӿ�
 */
@Component("memberView")
public class MemberView extends BaseAction {
	
	@Autowired
	@Qualifier("memberService")
	private IMemberService memberService;
	
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	//�û��б�
	public String list(Page page){
		String status = getParameter("status");
		String ss_name=getParameter("ss_name");//�������˳�������
		String ss_tel=getParameter("ss_tel"); //��������ϵ�绰
//		String driverName = getParameter("driverName");
//		String accountId;
//		Account account = getSessionAccount();
//			accountId = account.getAccountId().toString();
//		List<Account> list = accountService.getListTe(account.getTeam().getTeamId());
//		String accountIDs ="";
//		for (int i = 0; i < list.size(); i++) {
//			if(i == 0){
//				accountIDs = list.get(i).getAccountId()+"";
//			}else{
//				accountIDs += ","+list.get(i).getAccountId();
//			}
//		}
		
		Account account = getSessionAccount();
		//String teamIds=teamService.getTeamIdStr(account);
		int teamIds=account.getTeam().getTeamId();
		if(account.getLevel().getLevelId() == 4){
			teamIds = account.getDept().getDeptId();
		}
		
		Object[] keywords = new Object[]{teamIds,status,ss_name,ss_tel};
		page = memberService.getPageList(page, keywords,account.getLevel().getLevelId());
		//System.out.println("aaa:"+ResultManager.getBodyResult(page));
		return ResultManager.getBodyResult(page);
	}
	
//	//�����û�
//	public String addMember() throws Exception{
//		String user_name = getParameter("user_name");
//		String pwd = getParameter("pwd"); 
//		String name = getParameter("name");
//		String tel = getParameter("tel");
//		String team_id = getParameter("team_id");
//		String dept_id = getParameter("dept_id");
////		if(StringUtils.isBlank(driverName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"������˾������");
////		String accountId;
////		Account account = getSessionAccount();
////		accountId = account.getAccountId().toString();
//			Member member=new Member();
//			member.setUserName(user_name);
//			member.setPwd(pwd);
//			member.setName(name);
//			member.setTel(tel);
//			member.setTeamId(Integer.parseInt(team_id));
//			member.setDeptId(Integer.parseInt(dept_id));
//			member.setCreateTime(new Date());
//			
//			member.setLeavel(0);
//			member.setIfDel(0);
//			
//			member.setStatus(0);
//			
//			memberService.save(member);
//			return ResultManager.getSuccResult();
//			 
//	}
	
	//����member��idɾ���û�
	public String delMember() throws Exception{
		String ids=getParameter("id");
		memberService.deleteMember(ids);
		return ResultManager.getSuccResult();
		
	}
	
//	//�޸�member�û�
//	public String updateMember() throws Exception{
//		String id=getParameter("id");
//		String user_name = getParameter("user_name");
//		String pwd = getParameter("pwd"); 
//		String name = getParameter("name");
//		String tel = getParameter("tel");
//		String team_id = getParameter("team_id");
//		String dept_id = getParameter("dept_id"); 
//		
//		Member member=memberService.get(Integer.parseInt(id));//����id��ѯmember����
//		
//		member.setUserName(user_name);
//		member.setPwd(pwd);
//		member.setName(name);
//		member.setTel(tel);
//		member.setTeamId(Integer.parseInt(team_id));
//		member.setDeptId(Integer.parseInt(dept_id));
//		
//		memberService.saveMember(member);//SaveOrUpdate��������member
//		return ResultManager.getSuccResult();
//	}
	
	
	public String saveMember() throws Exception{
		String id=getParameter("id");
		String user_name = getParameter("user_name");
		//String pwd = getParameter("pwd"); 
		String name = getParameter("name");
		String tel = getParameter("tel");
		String team_id = getParameter("team_id");
		String dept_id = getParameter("deptName");
		String warning=getParameter("warning");
		
		if(StringUtils.isBlank(user_name)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�������û���");
		//Account account = getSessionAccount();
		String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
//			accountId = account.getAccountId().toString();
//		}
		if(StringUtils.isBlank(id)){//add
			//��ѯ�Ƿ�������,����������ʾ����
			boolean exist=memberService.ifExist(user_name);
			if(exist){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�û����Ѵ��ڣ�������");
			}
			
			Member member = new Member();
			member.setUserName(user_name);
			member.setPwd("123456");
			member.setName(name);
			member.setTel(tel);
			member.setTeamId(Integer.parseInt(team_id));
			if(!StringUtils.isBlank(dept_id)){
				member.setDeptId(Integer.parseInt(dept_id));
			}
			if(!StringUtils.isBlank(warning)){
				member.setWarning(Integer.parseInt(warning));
			}
			
			member.setLeavel(1000);
			member.setIfDel(0);
			member.setStatus(1);
			member.setCreateTime(new Date());
			
			memberService.saveMember(member);
			return ResultManager.getSuccResult();
			 
		}else{//edit
			Member member=memberService.get(Integer.parseInt(id));//����id��ѯmember����
			if(member==null){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����ڴ�id��");
			}
			member.setName(name);
			member.setTel(tel);
			if(!StringUtils.isBlank(dept_id)){
				member.setDeptId(Integer.parseInt(dept_id));
			}
			if(!StringUtils.isBlank(warning)){
				member.setWarning(Integer.parseInt(warning));
			}
			memberService.saveMember(member);
			return ResultManager.getSuccResult();
		}
	}
	
	public String editMember() throws Exception {
		String uid = getParameter("id");
		String status = getParameter("status");
			Member m = memberService.get(Integer.parseInt(uid));
			if("-1".equals(status)){
				m.setStatus(-1);
			}else if("1".equals(status)){
				m.setStatus(1);
			}
			memberService.update(m);

			return ResultManager.getSuccResult();

	}
	
	public String resetMember() throws Exception{
		String ids=getParameter("id");
		memberService.resetMember(ids);
		return ResultManager.getSuccResult();
	}
	
}
