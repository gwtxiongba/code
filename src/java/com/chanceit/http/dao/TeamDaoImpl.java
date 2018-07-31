package com.chanceit.http.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Team;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("teamDao")
@Repository
public class TeamDaoImpl extends HibernateService implements ITeamDao {
	
	@Override
	public String save(Team team) {
		Serializable skey = super.getSession().save(team);
		return skey.toString();
	}
	@Override
	public void saveTeam(Team team) {
		getSession().saveOrUpdate(team);
	}
	@Override
	public boolean delete(List ids,int accountId) {
		String hql = "delete from Team u where u.teamId in (:ids)";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids);  
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
	public List getSqlObj(String sql ,Class<?> ct) {
		return super.getSqlList(sql, ct);
	}

	

}
