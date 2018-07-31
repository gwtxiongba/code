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
import com.chanceit.http.dao.ILevelDao;
import com.chanceit.http.dao.ITeamDao;
import com.chanceit.http.dao.IUserDao;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.CarTeamTree;
import com.chanceit.http.pojo.OperatorTeam;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.pojo.TeamTree;
import com.chanceit.http.pojo.User;
import com.google.gson.Gson;

@Transactional
@Component("teamService")
public class TeamService implements ITeamService {
	@Autowired
	@Qualifier("teamDao")
	private ITeamDao teamDao;
	
	@Autowired
	@Qualifier("userDao")
	private IUserDao userDao;
	
	@Autowired
	@Qualifier("levelDao")
	private ILevelDao levelDao;
	
	
	@Override
	public Team getTeam(int teamId) {
		return teamDao.get(teamId);
	}
	
	@Override
	public String save(Team team) {
		try{
			return teamDao.save(team);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean saveTeam(Team team) {
		try{
			teamDao.saveTeam(team);
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
		return teamDao.delete(list,accountId);
	}
	
	@Override
	public boolean update(Team team) {
		teamDao.update(team);
		return true;
	}
	

	@Override
	public Team get(int teamId) {
		return teamDao.get(teamId);
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
		return teamDao.getPageList(page, hql.toString());
	}
	
	
	@Override
	public List getList(Object[] values){
		StringBuffer hql = new StringBuffer("" +
//				"select  teamId,teamName,teamPwd,companyName,teamTel,address,email,visitIp" +
//				" createTime as createTime, teamIp as teamIp"+
				" from Team l where 1=1 ");
		if(values[0]!=null && !values[0].equals("")){
			hql.append(" and l.account.accountId=").append(values[0]);
		}
		if(values[1]!=null && !values[1].equals("")){
			hql.append(" and l.plate like '%").append(values[1]).append("%'");
		}
		hql.append(" order by l.createTime desc");
		return teamDao.getList(hql.toString(), new Object[]{});
	}
	
	@Override
	public boolean ifExist(String teamName) throws Exception{
		String sql = "select * from team where team_name=?";
		List list = teamDao.getListSql(sql, teamName);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}
	@Override
	public boolean ifParent(int pid) throws Exception{
		String sql = "select * from team where pid=?";
		List list = teamDao.getListSql(sql, pid);
		System.out.println(sql);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}
	@Override
	public boolean deleteTeam(String ids, int accountId) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return teamDao.delete(list,accountId);
	}

	@Override
	public Team getByName(String teamName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean ifBind(String teamId) throws Exception{
		String sql = "select * from User where if_del = 0 and team_id in (?)";
		List list = teamDao.getListSql(sql, teamId);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}
	
	public String getTeamIdStr(Account account){
		String sql = " select * from team   where FIND_IN_SET(team_id, getChildLst("+account.getTeam().getTeamId()+")) ";
		//List list = teamDao.getList(hql.toString(),new Object[]{});
		//List list = teamDao.getListSql(sql,null);
		String uidss = "";
		List<Team> list = teamDao.getSqlObj(sql,Team.class);
		for(int i = 0; i<list.size(); i++){
			if(i==0){
				uidss = list.get(i).getTeamId()+"";
			}else{
				uidss += ","+list.get(i).getTeamId();
			}
		}
		return uidss;
	}
	/***********************获取车辆树状结构******************************/
	@Override
	public JSONObject getJson(Account account, int accountId,int flag) throws Exception{
		
		String uids = "153110799";
		StringBuffer hql = new StringBuffer();
		String sql = "";
		JSONObject rsJson;
		if(flag == 0){
		 sql = " select t.*, u.user_id,u.plate,u.identifier,u.team_id as tid,u.if_off as if_off,u.cut_pwd as cutpwd,u.if_relay as if_relay from team t left join user u on  t.team_id = u.team_id and  u.if_del=0 where FIND_IN_SET(t.team_id, getChildLst("+account.getTeam().getTeamId()+")) ";
		}else{
			 sql = " select * from team   where FIND_IN_SET(team_id, getChildLst("+account.getTeam().getTeamId()+")) ";
		}
		 //List list = teamDao.getList(hql.toString(),new Object[]{});
		List list = teamDao.getListSql(sql,null);
		//List<Team> list = teamDao.getSqlObj(sql,Team.class);
		List<TeamTree> listtr = new ArrayList<TeamTree>();
		HashSet<Integer> set = new HashSet<Integer>();
		if(flag == 0){
		for(int i = 0;i< list.size();i++){
			Map m = (Map) list.get(i);
			Integer uid = (Integer) m.get("user_id");
			Integer teamId = (Integer) m.get("team_id");
			Integer account_Id = (Integer) m.get("account_id");
			String teamname = (String) m.get("team_name");
			Integer pid = (Integer) m.get("pid");
			Integer tid = (Integer) m.get("tid");
			Integer amount = (Integer) m.get("amount");
			String identifer=(String) m.get("identifier");
			String plate=(String) m.get("plate");
			Byte ifOff=(Byte) m.get("if_off");
			Byte ifrelay=(Byte) m.get("if_relay");
			String cutpwd=(String) m.get("cutpwd");
			TeamTree ttr = new TeamTree();
			ttr.setAccountId(account_Id);
			ttr.setAmount(amount);
			ttr.setPid(pid);
			ttr.setTeamId(teamId);
			ttr.setTeamName(teamname);
			ttr.setLeaf(false);
			ttr.setIconCls("com_ic");
			if(!set.contains(teamId)){
				set.add(teamId);
				
				listtr.add(ttr);
			}
			if(uid != null){
				TeamTree ttr1 = new TeamTree();
				ttr1.setAccountId(account_Id);
				ttr1.setAmount(0);
				ttr1.setPid(tid);
				ttr1.setTeamId(Integer.parseInt(identifer));
				ttr1.setTeamName(plate);
				ttr1.setLeaf(true);
				ttr1.setUid(uid);
				ttr1.setIconCls("car_ic");
				uids+=","+identifer;
				if(ifOff == null){
					ttr1.setIfOff("");
				}else{
					ttr1.setIfOff(String.valueOf(ifOff));
				}
				if(cutpwd == null){
					ttr1.setCutpwd("");
				}else{
					ttr1.setCutpwd(cutpwd);
				}
				if(ifrelay == null){
					ttr1.setRelay("");
				}else{
					ttr1.setRelay(String.valueOf(ifrelay));
				}
				if(!set.contains(uid)){
					set.add(uid);
					listtr.add(0,ttr1);
				}
			}
			
		}
		if(account.getTeam().getTeamId() == 36){
		String carsql = "select u.user_id,u.plate,u.identifier,u.team_id as tid,u.if_off as if_off,u.cut_pwd as cutpwd,u.if_relay as if_relay from user u where u.team_id is null and  u.if_del=0";
		List listU = teamDao.getListSql(carsql,null);
		TeamTree ttr = new TeamTree();
		ttr.setAccountId(account.getAccountId());
		ttr.setAmount(0);
		ttr.setPid(account.getTeam().getTeamId());
		ttr.setTeamId(10000);
		ttr.setTeamName("未分组车辆");
		ttr.setLeaf(false);
		ttr.setIconCls("com_ic");
		if(!set.contains(10000)){
			set.add(10000);
			
			listtr.add(ttr);
		}
		for(int i =0;i<listU.size();i++){
			Map m = (Map) listU.get(i);
			Integer uid = (Integer) m.get("user_id");
			Integer teamId = (Integer) m.get("team_id");
			Integer account_Id = (Integer) m.get("account_id");
			Integer tid = (Integer) m.get("tid");
			String identifer=(String) m.get("identifier");
			String plate=(String) m.get("plate");
			Byte ifOff=(Byte) m.get("if_off");
			Byte ifrelay=(Byte) m.get("if_relay");
			String cutpwd=(String) m.get("cutpwd");
			TeamTree ttr1 = new TeamTree();
			ttr1.setAccountId(account_Id);
			ttr1.setAmount(0);
			ttr1.setPid(10000);
			ttr1.setTeamId(Integer.parseInt(identifer));
			ttr1.setTeamName(plate);
			ttr1.setLeaf(true);
			ttr1.setUid(uid);
			ttr1.setIconCls("car_ic");
			uids+=","+identifer;
			if(ifOff == null){
				ttr1.setIfOff("");
			}else{
				ttr1.setIfOff(String.valueOf(ifOff));
			}
			if(cutpwd == null){
				ttr1.setCutpwd("");
			}else{
				ttr1.setCutpwd(cutpwd);
			}
			if(ifrelay == null){
				ttr1.setRelay("");
			}else{
				ttr1.setRelay(String.valueOf(ifrelay));
			}
			if(!set.contains(uid)){
				set.add(uid);
				listtr.add(0,ttr1);
			}
		}
		}
		TreeUtil tu = new TreeUtil(listtr,null);
		tu.getOnlineCount(account,uids);
        tu.doCount(account.getTeam().getTeamId());
      
		//System.out.println("//////"+c+"//"+tu.result);
		TeamTree tt =tu.generateTreeNode(account.getTeam().getTeamId());
		tu.clear();
		Gson g = new Gson();
		 rsJson = new JSONObject(g.toJson(tt));
		}else{
			for(int i = 0;i< list.size();i++){
				Map m = (Map) list.get(i);
				Integer teamId = (Integer) m.get("team_id");
				Integer account_Id = (Integer) m.get("account_id");
				String teamname = (String) m.get("team_name");
				Integer pid = (Integer) m.get("pid");
				Integer amount = (Integer) m.get("amount");
				TeamTree ttr = new TeamTree();
				ttr.setAccountId(account_Id);
				ttr.setAmount(amount);
				ttr.setPid(pid);
				ttr.setTeamId(teamId);
				ttr.setTeamName(teamname);
				ttr.setLeaf(false);
				
				if(!set.contains(teamId)){
					set.add(teamId);
					
					listtr.add(ttr);
				}
			}
			TreeUtil tu = new TreeUtil(listtr,null);
			//System.out.println("//////"+c+"//"+tu.result);
			TeamTree tt =tu.generateTreeNodeForapp(account.getTeam().getTeamId());
			Gson g = new Gson();
			 rsJson = new JSONObject(g.toJson(tt));
		}
		
		return rsJson;
	}
	
	
	
	
	/***********************获取部门树状结构******************************/
	@Override
	public JSONObject getJsonDept(Account account, int accountId,int flag) throws Exception{
		
		
		StringBuffer hql = new StringBuffer();
		String sql = "";
		if(flag == 0){
		// sql = " select t.*, u.user_id,u.plate,u.identifier,u.team_id as tid,u.if_off as if_off,u.cut_pwd as cutpwd,u.if_relay as if_relay from team t left join user u on  t.team_id = u.team_id and  u.if_del=0 where FIND_IN_SET(t.team_id, getChildLst("+account.getTeam().getTeamId()+")) ";
		 // sql = "select a.team_id as team_id,a.team_name as team_name ,a.pid as pid,a.tid as tid,a.deptId as deptId,a.dept_name as dept_name, b.memid as memberId ,b.mname as memberName from (select t.*, dd.id as deptId,dd.name as dept_name,dd.team_id as tid  from team t left join dept dd on  t.team_id = dd.team_id  where FIND_IN_SET(t.team_id, getChildLst("+account.getTeam().getTeamId()+"))) a left join  ( select d.id as did ,m.id as memId,m.name as mname from dept d left join member m on d.id = m.dept_id) b on a.deptId=b.did";
		sql = "select t.* ,d.team_id as d_teamId,d.id as deptId,d.name as dept_name,m.id as memberId,m.name as memberName ,m.team_id as m_tid ,m.dept_id as m_deptId from team t left join dept d on d.team_id = t.team_id left join member m on m.team_id = t.team_id where  FIND_IN_SET(t.team_id, getChildLst("+account.getTeam().getTeamId()+")) ";
		if(account.getLevel().getLevelId() == 4){
			sql += " and d.id = "+account.getDept().getDeptId();
			sql += " and m.dept_id = "+account.getDept().getDeptId();
		}
		}else{
			 sql = " select * from team   where FIND_IN_SET(team_id, getChildLst("+account.getTeam().getTeamId()+")) ";
		}
		
		
		 //List list = teamDao.getList(hql.toString(),new Object[]{});
		List list = teamDao.getListSql(sql,null);
		//List<Team> list = teamDao.getSqlObj(sql,Team.class);
		List<TeamTree> listtr = new ArrayList<TeamTree>();
		HashSet<Integer> set = new HashSet<Integer>();
		if(flag == 0){
		for(int i = 0;i< list.size();i++){
			//{memberId=1, pid=187, memberName=李三, tid=212, team_id=212, team_name=呼市分公司}
			Map m = (Map) list.get(i);
			Integer deptId = (Integer) m.get("deptId");
			Integer memberId = (Integer) m.get("memberId");
			String deptName = (String) m.get("dept_name");
			String memberName = (String) m.get("memberName");
			Integer teamId = (Integer) m.get("team_id");
			Integer account_Id = (Integer) m.get("account_id");
			String teamname = (String) m.get("team_name");
			Integer pid = (Integer) m.get("pid");
			Integer m_tid = (Integer) m.get("m_tid");
			Integer m_deptId = (Integer) m.get("m_deptId");
			Integer d_teamId = (Integer) m.get("d_teamId");
			
			//Integer amount = (Integer) m.get("amount");
//			String identifer=(String) m.get("identifier");
//			String plate=(String) m.get("plate");
//			Byte ifOff=(Byte) m.get("if_off");
//			Byte ifrelay=(Byte) m.get("if_relay");
//			String cutpwd=(String) m.get("cutpwd");
			TeamTree ttr = new TeamTree();
			ttr.setAccountId(account_Id);
			//ttr.setAmount(amount);
			ttr.setPid(pid);
			ttr.setTeamId(teamId);
			ttr.setTeamName(teamname);
			ttr.setLeaf(false);
			ttr.setUid(teamId);
			ttr.setIconCls("com_ic");
			if(!set.contains(teamId)){
				set.add(teamId);
				
				listtr.add(ttr);
			}
			if(deptId != null){
				TeamTree ttr1 = new TeamTree();
				ttr1.setAccountId(account_Id);
				ttr1.setAmount(0);
				ttr1.setPid(d_teamId);
				ttr1.setTeamId(deptId+10000);
				ttr1.setTeamName(deptName);
				ttr1.setLeaf(false);
				ttr1.setUid(deptId);
				ttr1.setIconCls("dept_ic");
//				if(ifOff == null){
//					ttr1.setIfOff("");
//				}else{
//					ttr1.setIfOff(String.valueOf(ifOff));
//				}
//				if(cutpwd == null){
//					ttr1.setCutpwd("");
//				}else{
//					ttr1.setCutpwd(cutpwd);
//				}
//				if(ifrelay == null){
//					ttr1.setRelay("");
//				}else{
//					ttr1.setRelay(String.valueOf(ifrelay));
//				}
				if(!set.contains(deptId+10000)){
					set.add(deptId+10000);
					listtr.add(ttr1);
				}
			}
				if(memberId != null){
					System.out.println(m_deptId+"/////"+m_tid+"////"+memberId);
					TeamTree ttr2 = new TeamTree();
					ttr2.setAccountId(account_Id);
					ttr2.setAmount(0);
					if(m_deptId != null && m_deptId != 0){
					ttr2.setPid(m_deptId+10000);
					}else{
						ttr2.setPid(m_tid);
					}
					ttr2.setTeamId(memberId+20000);
					ttr2.setTeamName(memberName);
					ttr2.setLeaf(true);
					ttr2.setUid(memberId);
					ttr2.setIconCls("person_ic");
					if(!set.contains(memberId+20000)){
						set.add(memberId+20000);
						listtr.add(ttr2);
					}
				}
			
		}
		}else{
			for(int i = 0;i< list.size();i++){
				Map m = (Map) list.get(i);
				Integer teamId = (Integer) m.get("team_id");
				Integer account_Id = (Integer) m.get("account_id");
				String teamname = (String) m.get("team_name");
				Integer pid = (Integer) m.get("pid");
				Integer amount = (Integer) m.get("amount");
				TeamTree ttr = new TeamTree();
				ttr.setAccountId(account_Id);
				ttr.setAmount(amount);
				ttr.setPid(pid);
				ttr.setTeamId(teamId);
				ttr.setTeamName(teamname);
				ttr.setLeaf(false);
				if(!set.contains(teamId)){
					set.add(teamId);
					
					listtr.add(ttr);
				}
			}
		}
		TreeUtil tu = new TreeUtil(listtr,null);
		TeamTree tt =tu.generateTreeNodeForapp(account.getTeam().getTeamId());
		Gson g = new Gson();
		JSONObject rsJson = new JSONObject(g.toJson(tt));
		System.out.println("////////////"+g.toJson(tt));
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
	
	@SuppressWarnings("unchecked")
	public Map getMap(Integer accountId) throws Exception{
		String currentTeamName;
		Map<String, Object> teamMap = new HashMap<String, Object>();
		String hql = "";
			 hql = "from User l where l.account.accountId = ? and l.ifDel =0  order by vehicleId asc,l.team.teamId asc";
		Object [] values = new Object[]{accountId};  
		
		List<User> list= userDao.getList(hql,values);
		long _t = System.currentTimeMillis();
		System.out.println(_t);
		if(list.size()>0){
			String[] uids=new String[list.size()];
			for(int i=0;i<list.size();i++){
				User user = list.get(i);
				uids[i]= user.getIdentifier()+"_";
			}
			
			for(int i = 0;i < list.size();i++){
				User user = list.get(i);
				//设置单个车辆信息
				JSONObject results = new JSONObject();
				results.put("vehicleId", user.getVehicleId());
				results.put("identifier", user.getIdentifier());
				results.put("plate", user.getPlate());
				if(user.getIfRelay() == null){
					results.put("ifRelay", "");
				}else{
					results.put("ifRelay", user.getIfRelay());
				}
				if(user.getCutpwd() == null){
					results.put("cutpwd", "");
				}else{
					results.put("cutpwd",user.getCutpwd());
				}
				if(user.getIfOff() == null){
					results.put("ifOff", "");
				}else{
					results.put("ifOff",user.getIfOff());
				}
				//order字段用来在页面树进行排序
				if(user.getTeam()!=null){
					currentTeamName = user.getTeam().getTeamName();
					results.put("order", user.getTeam().getOrder());
				}else{
					currentTeamName="未分组车辆";
					results.put("order", 10000);
				}
				
				//看是否已有分组,没有分组就添加新的分组,并将车辆信息放入分组
				if(teamMap.get(currentTeamName) == null){
					JSONArray dataArray = new JSONArray();
					dataArray.put(results);
					teamMap.put(currentTeamName, dataArray);
				}else{
					JSONArray dataArray = (JSONArray) teamMap.get(currentTeamName);
					dataArray.put(results);
				}
			}
			
	        teamMap.put("total", list.size());
	        teamMap.put("onlines", 0);//jedisUtils.getOnlinesAmount(uids));
			return teamMap;
		}
		return null;
	}
	/******
	 * add by gwt 2015-01-10
	 * */
	@Override
	public JSONObject getJson_x(Account account, int accountId,String identifiers) throws Exception{
		
		String sql = "";
		 sql = " select t.*, u.user_id,u.plate,u.identifier,u.team_id as tid,u.if_off as if_off,u.cut_pwd as cutpwd,u.if_relay as if_relay from team t left join user u on  t.team_id = u.team_id and  u.if_del=0  where FIND_IN_SET(t.team_id, getChildLst("+account.getTeam().getTeamId()+")) ";
		List list = teamDao.getListSql(sql,null);
		//List<Team> list = teamDao.getSqlObj(sql,Team.class);
		List<CarTeamTree> listtr = new ArrayList<CarTeamTree>();
		HashSet<Integer> set = new HashSet<Integer>();
		for(int i = 0;i< list.size();i++){
			Map m = (Map) list.get(i);
			Integer uid = (Integer) m.get("user_id");
			Integer teamId = (Integer) m.get("team_id");
			Integer account_Id = (Integer) m.get("account_id");
			String teamname = (String) m.get("team_name");
			Integer pid = (Integer) m.get("pid");
			Integer tid = (Integer) m.get("tid");
			Integer amount = (Integer) m.get("amount");
			String identifer=(String) m.get("identifier");
			String plate=(String) m.get("plate");
			Byte ifOff=(Byte) m.get("if_off");
			Byte ifrelay=(Byte) m.get("if_relay");
			String cutpwd=(String) m.get("cutpwd");
			CarTeamTree ttr = new CarTeamTree();
			ttr.setAccountId(account_Id);
			ttr.setAmount(amount);
			ttr.setPid(pid);
			ttr.setTeamId(teamId);
			ttr.setTeamName(teamname);
			ttr.setLeaf(false);
			if(!set.contains(teamId)){
				set.add(teamId);
				
				listtr.add(ttr);
			}
			if(uid != null){
				CarTeamTree ttr1 = new CarTeamTree();
				ttr1.setAccountId(account_Id);
				ttr1.setAmount(0);
				ttr1.setPid(tid);
				ttr1.setTeamId(Integer.parseInt(identifer));
				ttr1.setTeamName(plate);
				ttr1.setLeaf(true);
				ttr1.setUid(uid);
				if(ifOff == null){
					ttr1.setIfOff("");
				}else{
					ttr1.setIfOff(String.valueOf(ifOff));
				}
				if(cutpwd == null){
					ttr1.setCutpwd("");
				}else{
					ttr1.setCutpwd(cutpwd);
				}
				if(ifrelay == null){
					ttr1.setRelay("");
				}else{
					ttr1.setRelay(String.valueOf(ifrelay));
				}
				if(!set.contains(uid)){
					set.add(uid);
					listtr.add(ttr1);
				}
			}
			
		}
		TreeUtil tu = new TreeUtil(null,listtr);
		CarTeamTree tt =tu.generateTreeNodecar(account.getTeam().getTeamId());
		Gson g = new Gson();
		String ss = g.toJson(tt);
		JSONObject rsJson = new JSONObject(g.toJson(tt));
		//rsJson = ;
		return rsJson;
	}
	
	/***
	 * add by gwt 2015-01-10
	 * */
	@SuppressWarnings("unchecked")
	public Map getMap_x(Integer accountId,String identifiers) throws Exception{
		String currentTeamName;
		Map<String, Object> teamMap = new HashMap<String, Object>();
		String hql = "";
		if(identifiers == null){
		 hql = "from User l where l.account.accountId = ? and l.ifDel =0  order by vehicleId asc,l.team.teamId asc";
		}else{
		 hql = "from User l where l.identifier not in ("+identifiers+") and l.account.accountId = ? and l.ifDel =0  order by vehicleId asc,l.team.teamId asc";
		}
		Object [] values = new Object[]{accountId};  
		
		List<User> list= userDao.getList(hql,values);
		long _t = System.currentTimeMillis();
		System.out.println(_t);
		if(list.size()>0){
			String[] uids=new String[list.size()];
			for(int i=0;i<list.size();i++){
				User user = list.get(i);
				uids[i]= user.getIdentifier()+"_";
			}
			
			for(int i = 0;i < list.size();i++){
				User user = list.get(i);
				//设置单个车辆信息
				JSONObject results = new JSONObject();
				results.put("id", user.getVehicleId());
				results.put("leaf", true);
				results.put("text", user.getPlate());
				results.put("checked", false);
//				if(user.getIfRelay() == null){
//					results.put("ifRelay", "");
//				}else{
//					results.put("ifRelay", user.getIfRelay());
//				}
//				if(user.getCutpwd() == null){
//					results.put("cutpwd", "");
//				}else{
//					results.put("cutpwd",user.getCutpwd());
//				}
//				if(user.getIfOff() == null){
//					results.put("ifOff", "");
//				}else{
//					results.put("ifOff",user.getIfOff());
//				}
//				//order字段用来在页面树进行排序
				if(user.getTeam()!=null){
					currentTeamName = user.getTeam().getTeamName();
					results.put("order", user.getTeam().getOrder());
				}else{
					currentTeamName="未分组车辆";
					results.put("order", 10000);
				}
//				
				//看是否已有分组,没有分组就添加新的分组,并将车辆信息放入分组
				if(teamMap.get(currentTeamName) == null){
					JSONArray dataArray = new JSONArray();
					dataArray.put(results);
					teamMap.put(currentTeamName, dataArray);
				}else{
					JSONArray dataArray = (JSONArray) teamMap.get(currentTeamName);
					dataArray.put(results);
				}
			}
			
	        teamMap.put("total", list.size());
	        teamMap.put("onlines", 0);//jedisUtils.getOnlinesAmount(uids));
			return teamMap;
		}
		return null;
	}
	

	

	/**
	 * add by zhangxin 2014-08-22
	 * 通过车队ID获取车队下所有车辆的设备码
	 */
	@Override
	public String getIdentifiersByTeamIds(String teamIds) throws Exception {
		String hql = "select u.identifier from User u where u.team.teamId in (" + teamIds + ")";
		List list = teamDao.getList(hql, null);
		if(list != null){
			return list.toString().replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "");
		}else{
			return "";
		}
	}

	/**
	 * 通过当前登录的管理员获取其下所有车队信息
	 */
	@Override
	public String getTeamIdsByAccountId(Integer accountId) {
		String hql = "select t.teamId from Team t where t.account.accountId = ?";
		List list = teamDao.getList(hql, new Object[]{accountId});
		if(list != null){
			return list.toString().replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "");
		}else{
			return "";
		}
	}

  @Override
@SuppressWarnings("unchecked")
public List getTeamsList(Account account,Integer accountId){
	    List list;
		//如果是操作员只查询操作员所属的车队信息
//		if(account.getRole().getRoleId() == 3){
//			//先查询操作员拥有车队
//			String sql = " select a.team_id as teamId,a.team_name as teamName from operator_team as b,team as a where a.team_id = b.team_id and b.operator_id="+account.getAccountId();
//			 list = teamDao.getListSql(sql.toString(),new Object[]{});
//		}else{
		   String sql1 = "select l.team_id as teamId,l.team_name as teamName from  team l where 1=1 and l.account_id="+accountId;
		      list = teamDao.getListSql(sql1.toString(),new Object[]{});
//		}
	 return list; 
  }	
  
  @Override
public JSONArray getMapForApp(Integer accountId) throws Exception{
	  String currentTeamName =  "";
		Map<String, Object> teamMap = new HashMap<String, Object>();
		
		String hql1 =" from Team t where t.account.accountId="+ accountId;
		List<Team> listTeam= teamDao.getList(hql1,null);
		String hql = "from User l where l.account.accountId = ? and l.team.teamId is not null and l.ifDel =0  order by vehicleId asc,l.team.teamId asc";
		Object [] values = new Object[]{accountId};  
		
		List<User> list= userDao.getList(hql,values);
		for(int i=0;i<list.size();i++){
          	User u = list.get(i);
          	if(u.getTeam() != null){
          		currentTeamName = u.getTeam().getTeamName();
          	}else{
          		
          	}
          	if(teamMap.get(currentTeamName) == null){
    			JSONArray dataArray = new JSONArray();
    			dataArray.put(ResultManager.getBodyResult(u));
    			teamMap.put(currentTeamName, dataArray);
    		}else{
    			JSONArray dataArray = (JSONArray) teamMap.get(currentTeamName);
    			dataArray.put(ResultManager.getBodyResult(u));
    		}
		}
		JSONArray array = new JSONArray();
		for(int j = 0; j<listTeam.size();j++){
			JSONObject json = new JSONObject();
			if(teamMap.get(listTeam.get(j).getTeamName()) != null){
				json.put("teamId", listTeam.get(j).getTeamId());
				json.put("teamName", listTeam.get(j).getTeamName());
				json.put("users", teamMap.get(listTeam.get(j).getTeamName()));
			}else{
				json.put("teamId", listTeam.get(j).getTeamId());
				json.put("teamName", listTeam.get(j).getTeamName());
				json.put("users", "null");
			}
			array.put(json);
		}
			return array;
	}
    
  @Override
public List getTeamListForApp(int accountId){
	  String sql = "select t.team_id as teamId,t.team_name as teamName,t.team_order as teamOrder , count(u.user_id) as amount from team t left join user u on t.team_id = u.team_id where t.account_id=u.account_id and  t.account_id = "+accountId+" and u.if_del=0 group by t.team_id order by t.create_time desc";
	  String sql1 = "select  count(user_id) as count from user  where account_id = "+accountId+" and if_del=0 and team_id is null";
	  List list1 = teamDao.getListSql(sql, null);
	  List list= teamDao.getListSql(sql1, null);
	 Map maps  = new HashedMap(); 
	 maps.put("teamName", "未分组车辆");
	  maps.put("teamOrder", null);
	  maps.put("teamId", null);
	  if(list != null && list.size()>0){
		  Map map = (Map) list.get(0);
		  //count = map.get("count");
		  maps.put("amount", map.get("count"));
	  }
	  
	  list1.add(maps);
	  return list1;
  }
  @Override
public List getNoTeamCountApp(int accountId){
	  String sql = "select  count(user_id) as count from user  where account_id = 6 and if_del=0 and team_id is null";
     return teamDao.getListSql(sql, null);
  }

@Override
public List getListSql(String sql, Object... values) {
	// TODO Auto-generated method stub
	return teamDao.getListSql(sql, values);
}
  
  
  
}
