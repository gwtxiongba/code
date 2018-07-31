package com.chanceit.http.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Team;

public interface ITeamService {
	public boolean update(Team team);

	public Team get(int teamId);
	public Page getPageList(Page page,Object[] values );
	public boolean ifExist(String teamName) throws Exception;
	public Team getByName(String teamName) throws Exception;
	public String save(Team team);
	public boolean saveTeam(Team team);
	public Team getTeam(int teamId);
	public boolean deleteTeam(String ids,int accountId) ;
	public JSONObject getJson_x(Account account, int accountId,String uids) throws Exception;
	
	public List getListSql(String sql, Object... values);
	
	/**
	 * @param teamId
	 * @return
	 * @throws Exception
	 * @des teamId是否有未解绑的车辆
	 */
	public boolean ifBind(String teamId) throws Exception;
	/**
	 * @des 没有车辆时，拼装一个空的json
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	public JSONObject getJson(Account account, int accountId,int flag) throws Exception;
	
	public String getIdentifiersByTeamIds(String teamIds) throws Exception;
	
	public String getTeamIdsByAccountId(Integer accountId);
	public List getList(Object[] values);
	 public List getTeamsList(Account account,Integer accountId);
	 public JSONArray getMapForApp(Integer accountId) throws Exception;
	 public List getTeamListForApp(int accountId);
	 public List getNoTeamCountApp(int accountId);
	 public String getTeamIdStr(Account account);
	 public boolean ifParent(int pid) throws Exception;
	 public JSONObject getJsonDept(Account account, int accountId,int flag) throws Exception;
}