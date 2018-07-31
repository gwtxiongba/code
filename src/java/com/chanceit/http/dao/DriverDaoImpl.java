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
import com.chanceit.http.pojo.Driver;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("driverDao")
@Repository
public class DriverDaoImpl extends HibernateService implements IDriverDao {
	
	@Override
	public String save(Driver driver) {
		Serializable skey = getSession().save(driver);
		return skey.toString();
	}
	@Override
	public void saveDriver(Driver driver) {
		getSession().saveOrUpdate(driver);
	}
	@Override
	public boolean delete(List ids) {
		String hql = "delete from Driver u where u.driverId in (:ids) ";
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
		String hql = "update Driver u set delTime=:sj,ifDel=1 where u.driverId in (:ids) and u.account.accountId=:aid";
		Query query = super.getSession().createQuery(hql).setParameter("sj", new Date()).setParameterList("ids",ids).setParameter("aid", accountId);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	@Override
	public void delete(Driver driver) {
		getSession().delete(driver);
	}

	@Override
	public void update(Driver driver) {
		getSession().clear();
		getSession().update(driver);
	}

	@Override
	public Driver get(int driverId) {
		return (Driver) getSession().get(Driver.class, driverId);
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
	
	/**
	 * 获取司机拼音重名列表
	 * @param loginName 
	 * @return
	 * @throws Exception
	 */
	@Override
	public List getExistNameList(String hql) {
		return super.find(hql);
	}

	/**
	 * 修改司机
	 * @param loginName 
	 * @return
	 * @throws Exception
	 */
	@Override
	public void updateDriver(String hql) {
		Query query = getSession().createQuery(hql);
		query.executeUpdate();  
	}
	
	/**
	 * 解除绑定
	 */
	@Override
	public boolean  unBindDriver(String sql) {
		try {
			Query query = getSession().createSQLQuery(sql);
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		  
	}
	@Override
	public boolean resetDriver(List ids) {
		String hql="update Driver set pwd='123456' where driverId in (:ids)";
		Query query=super.getSession().createQuery(hql).setParameterList("ids", ids);
		int rs=query.executeUpdate();
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
}
