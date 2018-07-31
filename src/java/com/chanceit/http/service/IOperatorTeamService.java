package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.OperatorTeam;
import com.chanceit.http.pojo.Team;


public interface IOperatorTeamService {
	
	public void saveOpTeam(OperatorTeam opTeam);
	public String getPageList(Page page,Object[] values);
	public String getGroupList(String accountId, int operatorId) throws Exception;
	
	public boolean bindTeam(String ids,int operatorId);
	public boolean unBindTeam(String ids,int operatorId);
	
	public void delete(int operatorId);
	public void deleteByTeam(int teamId);
	
	public Account getAccount(int operatorId);
	public Team get(int teamId);
	public void save(OperatorTeam opTeam);
	public List getOpTeamByIds(String ids);
	public String getTeamIdsByOperatorId(String ids);

}