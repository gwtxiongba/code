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
import com.chanceit.http.pojo.Askleave;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("askleaveDao")
@Repository
public class AskleaveDaoImpl extends HibernateService implements IAskleaveDao {
	
	@Override
	public String save(Askleave askleave) {
		Serializable skey = getSession().save(askleave);
		return skey.toString();
	}
	@Override
	public void saveAskleave(Askleave askleave) {
		getSession().saveOrUpdate(askleave);
	}
	@Override
	public boolean delete(List ids,int accountId) {
		String hql = "delete from Askleave u where u.askleaveId in (:ids) and u.account.accountId=:aid";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids).setParameter("aid", accountId);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void delete(Askleave askleave) {
		getSession().delete(askleave);
	}

	@Override
	public void update(Askleave askleave) {
		getSession().clear();
		getSession().update(askleave);
	}

	@Override
	public Askleave get(int askleaveId) {
		return (Askleave) getSession().get(Askleave.class, askleaveId);
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
	public Page getPageList(Page page , String sql ,Object... values) {
		return super.findSQLMapPage(page, sql, values);
	}
	
	
	@Override
	public List getListSql(String sql ,Object... values) {
		return super.findSQL(sql, values);
	}
	

	/**
	 * 修改司机
	 * @param loginName 
	 * @return
	 * @throws Exception
	 */
	@Override
	public void updateAskleave(String hql) {
		Query query = getSession().createQuery(hql);
		query.executeUpdate();  
	}
	
	
}
