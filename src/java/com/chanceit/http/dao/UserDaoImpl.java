package com.chanceit.http.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.pojo.User;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("userDao")
@Repository
public class UserDaoImpl extends HibernateService implements IUserDao {
	
	@Override
	public void save(User user) {
		getSession().saveOrUpdate(user);
	}
	@Override
	public void saveTeam(Team team) {
		getSession().saveOrUpdate(team);
	}
	@Override
	public boolean delete(List ids,int accountId) {
		String hql = "delete from User u where u.vehicleId in (:ids)";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids).setParameter("aid", accountId);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public boolean deleteByIdentifier(String ids,int accountId) {
		String hql = "delete from User u where u.identifier = '"+ids+"'";
		Query query = super.getSession().createQuery(hql);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	@Override
	public boolean fakeDelete(List ids,int accountId) {
		String hql = "update User u set delTime=:sj,ifDel=1 ,team=null where u.vehicleId in (:ids) ";
		Query query = super.getSession().createQuery(hql).setParameter("sj", new Date()).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	@Override
	public void delete(User user) {
		getSession().delete(user);
	}

	@Override
	public void update(User user) {
		getSession().clear();
		getSession().update(user);
	}

	@Override
	public User get(int userId) {
		return (User) getSession().get(User.class, userId);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List getList(String hql,Object... objs) {
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
	@SuppressWarnings("unchecked")
	public Page getPageList_x(Page page , String sql ,Object... values) {
		return super.findSQLMapPage(page, sql, values);
	}
	
	@Override
	public List getListSql(String sql ,Object... values) {
		return super.findSQL(sql, values);
	}
	
	@Override
	public boolean bindTeam(List ids,int accountId,int teamId){
		String hql = "update User u set u.team.teamId=:tid where u.vehicleId in (:ids) and u.account.accountId=:aid";
		Query query = super.getSession().createQuery(hql).setParameter("tid", teamId).setParameterList("ids",ids).setParameter("aid", accountId);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public boolean unBindTeam(List ids,int accountId,int teamId){
		String hql = "update User u set u.team.teamId=NULL where u.vehicleId not in (:ids) and u.team.teamId=:tid and u.account.accountId=:aid";
		Query query = super.getSession().createQuery(hql).setParameter("tid", teamId).setParameterList("ids",ids).setParameter("aid", accountId);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean unBindTeamAll(int accountId, int teamId) {
		String hql = "update User u set u.team.teamId=NULL where u.team.teamId=:tid and u.account.accountId=:aid";
		Query query = super.getSession().createQuery(hql).setParameter("tid", teamId).setParameter("aid", accountId);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public boolean editIfMonitor(int ifMonitor,List ids,int accountId){
		String hql = "update User u set u.ifMonitor=:i where u.vehicleId in (:ids) ";
		Query query = super.getSession().createQuery(hql).setParameter("i", (short)ifMonitor).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public User getUserById(int vehicleId){
		return (User) getSession().get(User.class, vehicleId);
	}
	/**
	 * 获取所有设备识别码
	 * @return
	 */
	@Override
	public List getAllIdentifier(){
		String hql = "select u.identifier from User u";
		return super.find(hql);
	}
	
	/**
	 * 获取操作权限的车辆列表
	 * @param sql
	 * @return
	 */
	@Override
	public List getListForOp(String sql){
		return getSession().createSQLQuery(sql).addEntity(User.class).list();
	}
	@Override
	public Long countSQL(String hql, Object... objs) {
		return super.countSQLResult(hql, objs);
	}
	@Override
	public Object findObject(String hql, Object... objs) {
		return super.findSQLUnique(hql, objs);
	}
	
	@Override
	public void excuteUpdateSql(String sql) {
		super.getSession().createSQLQuery(sql).executeUpdate();
	}
}
