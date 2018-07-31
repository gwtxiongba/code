package com.chanceit.http.action;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Dept;
import com.chanceit.http.service.IDeptService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;
@Component("deptView")
public class DeptView extends BaseAction {
	@Autowired
	@Qualifier("deptService")
	private IDeptService deptService;
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
   public  String getDept(){
	   String id = getParameter("deptId");
	  Dept dept =  deptService.getDept(Integer.parseInt(id));
	  return "";
  }
   public String getDeptList(Page page){
	   String teamId = getParameter("teamId");
	   if(StringUtils.isEmpty(teamId)){
		   teamId = "0";
	   }
		   page = deptService.getList(page,Integer.parseInt(teamId));
	   return ResultManager.getBodyResult(page);
   }
   public String save(){
	   String deptId = getParameter("deptId");
	   String name = getParameter("deptName");
	   String teamId = getParameter("teamId");
	   if(StringUtils.isBlank(name)){
		   return ResultManager.getFaildResult(1, "名称不能为空");
	   }
//	   if(StringUtils.isEmpty(teamId)){
//		   return ResultManager.getFaildResult(1, "请选择公司");
//	   }
	   
	   if(StringUtils.isEmpty(deptId)){
		   Dept dept = new Dept();
		   dept.setDeptName(name);
		   dept.setTeamId(Integer.parseInt(teamId)); 
		   deptService.save(dept);
	   }else{
		   Dept dept =   deptService.getDept(Integer.parseInt(deptId));
		   dept.setDeptName(name);
		   deptService.save(dept);
	   }
	   
	 
	   return ResultManager.getSuccResult();
   }
   public String getDeptTeams() throws Exception{
	   Account account = null;
		account = getSessionAccount();
		if(account==null){
			return ResultManager.getFaildResult(102, "您未登录系统或长时间未操作");
		}
	   return teamService.getJsonDept(account, Integer.parseInt(account.getAccountId().toString()),0).toString();	
   }
   public String deleteDept(){
	   String deptId = getParameter("deptId");
	   if(StringUtils.isEmpty(deptId)){
		   return ResultManager.getFaildResult(1, "请选择部门"); 
	   }
	   Dept dept = deptService.getDept(Integer.parseInt(deptId));
	   if(dept == null){
		   return ResultManager.getFaildResult(1, "没有相关部门");  
	   }
	   deptService.delete(dept);
	   return ResultManager.getSuccResult();
   }
   
}
