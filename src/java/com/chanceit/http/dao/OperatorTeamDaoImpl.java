package com.chanceit.http.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.OperatorTeam;
import com.chanceit.http.pojo.Team;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("opTeamDao")
@Repository
public class OperatorTeamDaoImpl extends HibernateService implements IOperatorTeamDao {
	
	@Override
	public void save(OperatorTeam opTeam) {
		getSession().saveOrUpdate(opTeam);
	}
	
	
	@Override
	public boolean delete(List ids,int accountId) {
		String hql = "delete from Team u where u.teamId in (:ids) and u.account.accountId=:aid";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids).setParameter("aid", accountId);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public boolean fakeDelete(List ids,int accountId) {
		String hql = "update Team u set delTime=:sj,ifDel=1 where u.teamId in (:ids) and u.account.accountId=:aid";
		Query query = super.getSession().createQuery(hql).setParameter("sj", new Date()).setParameterList("ids",ids).setParameter("aid", accountId);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	@Override
	public void delete(Team team) {
		getSession().delete(team);
	}

	@Override
	public void update(Team team) {
		getSession().clear();
		getSession().update(team);
	}

	@Override
	public Team get(int teamId) {
		return (Team) getSession().get(Team.class, teamId);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List getList(String hql,Object[] objs) {
		return super.find(hql,objs);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List getMapList(String hql,Object... objs) {
		Query  query = super.getQuery(hql, objs);
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Page getPageList(Page page , String hql ,Object... values) {
		return super.findListPage(page, hql,values);
	}
	
	
	@Override
	public List getListSql(String sql ,Object... values) {
		return super.findSQL(sql, values);
	}


	@Override
	public void delete(int operatorId) {
		String hql = "delete from OperatorTeam u where u.account.accountId=:oid";
		Query query = super.getSession().createQuery(hql).setParameter("oid",operatorId );
		query.executeUpdate();
	}

	@Override
	public void deleteByTeam(int teamId) {
		String hql = "delete from OperatorTeam u where u.team.teamId=:oid";
		Query query = super.getSession().createQuery(hql).setParameter("oid",teamId );
		query.executeUpdate();
	}

	@Override
	public boolean bindTeam(List ids,int operatorId){
			String hql = "update Team t set t.operator.accountId=:oid where t.teamId in (:tids)";
			Query query = super.getSession().createQuery(hql).setParameter("oid", operatorId).setParameterList("tids", ids);
			int rs = query.executeUpdate();
			if(rs == 0){
				return false;
			}
			return true;
	}
	
	@Override
	public boolean unBindTeam(List ids,int operatorId){
		if(ids.size()>0){
			String hql = "update Team t set t.operator.accountId=NULL where t.teamId not in (:tids) and t.operator.accountId=:oid";
			Query query = super.getSession().createQuery(hql).setParameterList("tids",ids).setParameter("oid", operatorId); 
			int rs = query.executeUpdate();
			if(rs == 0){
				return false;
			}
			return true;
		}else{
			String hql = "update Team t set t.operator.accountId=NULL where t.operator.accountId=:oid";
			Query query = super.getSession().createQuery(hql).setParameter("oid", operatorId);
			int rs = query.executeUpdate();
			if(rs == 0){
				return false;
			}
		}
		return false;	
	}


	@Override
	public Account getAccount(int operatorId) {
		return (Account) getSession().get(Account.class, operatorId);
	}

	/**
	 * 根据ids获取关系的车队IDs
	 */
	@Override
	public String getTeamIdsByOperatorId(String operatorIds) {
		String teamIds = "";
		String ophql = " from OperatorTeam o where o.account.accountId in (" + operatorIds +")";
		List oplist = find(ophql.toString(), new Object[]{});
		for(Object obj: oplist){
			OperatorTeam opTeam = (OperatorTeam)obj;
			if(opTeam.getTeam() != null){
				teamIds += opTeam.getTeam().getTeamId() + ",";
			}
		}
		if(!"".equals(teamIds)){
			teamIds = teamIds.substring(0, teamIds.length()-1);
		}
		return teamIds;
	}
	
}
