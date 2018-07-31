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
import com.chanceit.http.pojo.Weixiu;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("weixiuDao")
@Repository
public class WeixiuDaoImpl extends HibernateService implements IWeixiuDao {
	
	@Override
	public String save(Weixiu weixiu) {
		Serializable skey = getSession().save(weixiu);
		return skey.toString();
	}
	@Override
	public void saveWeixiu(Weixiu weixiu) {
		getSession().saveOrUpdate(weixiu);
	}
	@Override
	public boolean delete(List ids) {
		String hql = "delete from Weixiu u where u.weixiuId in (:ids) ";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void delete(Weixiu weixiu) {
		getSession().delete(weixiu);
	}

	@Override
	public void update(Weixiu weixiu) {
		getSession().clear();
		getSession().update(weixiu);
	}

	@Override
	public Weixiu get(int weixiuId) {
		return (Weixiu) getSession().get(Weixiu.class, weixiuId);
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
	public void updateWeixiu(String hql) {
		Query query = getSession().createQuery(hql);
		query.executeUpdate();  
	}
	
	
}
