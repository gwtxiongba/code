package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.framework.utils.TreeUtil;
import com.chanceit.http.dao.IMenuDao;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Menu;
import com.chanceit.http.pojo.TeamTree;
import com.google.gson.Gson;

@Transactional
@Component("menuService")
public class MenuService implements IMenuService {
	@Override
	public JSONObject getJson_x(Account account, int accountId, String uids)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}






	@Autowired
	@Qualifier("menuDao")
	private IMenuDao menuDao;
	
	
	
	
	
	
	
	@Override
	public String save(Menu team) {
		try{
			return menuDao.save(team);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean saveMenu(Menu m) {
		try{
			menuDao.saveTeam(m);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	public boolean delete(String ids,int accountId) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return true;
	}
	
	@Override
	public boolean update(Menu m) {
		menuDao.update(m);
		return true;
	}
	

	@Override
	public Menu get(int menuId) {
		return menuDao.get(menuId);
	}
	


	@Override
	public Page getPageList(Page page,Object[] values ){
		StringBuffer hql = new StringBuffer("" +
//				"select  teamId,teamName,teamPwd,companyName,teamTel,address,email,visitIp" +
//				" createTime as createTime, teamIp as teamIp"+
				" from Team l where 1=1 ");
		if(values[0]!=null && !values[0].equals("")){
			hql.append(" and (l.pid=").append(values[0]).append(" or l.teamId=").append(values[0]).append(")") ;
		}
		if(values[1]!=null && !values[1].equals("")){
			hql.append(" and l.plate like '%").append(values[1]).append("%'");
		}
		hql.append(" order by l.createTime desc");
		return menuDao.getPageList(page, hql.toString());
	}
	
	
	
	@Override
	public boolean ifExist(String teamName) throws Exception{
//		String sql = "select * from team where team_name=?";
//		List list = teamDao.getListSql(sql, teamName);
//		if(list.size()==0){
//			return false;
//		}else{
//			return true;
//		}
		return true;
	}
	@Override
	public boolean deleteMenu(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return menuDao.delete(list);
	}

	@Override
	public Menu getByName(String teamName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	/***********************获取车辆树状结构******************************/
	@Override
	public JSONObject getJson(Account account, int accountId,int flag) throws Exception{
		
		String uids = "153110799";
		StringBuffer hql = new StringBuffer();
		String sql = "";
		JSONObject rsJson;
			 sql = " select * from menu   where FIND_IN_SET(menu_id, getChildLst(1)) ";
		 //List list = teamDao.getList(hql.toString(),new Object[]{});
		List list = menuDao.getListSql(sql,null);
		//List<Team> list = teamDao.getSqlObj(sql,Team.class);
		List<TeamTree> listtr = new ArrayList<TeamTree>();
		HashSet<Integer> set = new HashSet<Integer>();
		for(int i = 0;i< list.size();i++){
			Map m = (Map) list.get(i);
			Integer teamId = (Integer) m.get("menu_id");
			String teamname = (String) m.get("menu_name");
			Integer pid = (Integer) m.get("parent_id");
			TeamTree ttr = new TeamTree();
			ttr.setPid(pid);
			ttr.setTeamId(teamId);
			ttr.setTeamName(teamname);
			ttr.setLeaf(false);
			ttr.setIconCls("com_ic");
					listtr.add(ttr);
			
			
		}
		
		TreeUtil tu = new TreeUtil(listtr);
      
		TeamTree tt =tu.generateTreeNode(1);
		Gson g = new Gson();
		 rsJson = new JSONObject(g.toJson(tt));
		
		
		return rsJson;
	}
	
	
	
	
	
	
	private List<TeamTree> removeDuplicate(List<TeamTree> list) {
		HashSet<TeamTree> set = new HashSet<TeamTree>();
		          List<TeamTree> newList = new ArrayList<TeamTree>();
		          for (Iterator<TeamTree> iter = list.iterator(); iter.hasNext();) {
		        	  TeamTree element = (TeamTree) iter.next();
		             if (set.add(element))
		                newList.add(element);
		         }
		         return newList;
		     }
	
	
	
	
	

	

	

	

  
  
  
  
}
