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
import com.chanceit.http.pojo.Weizhang;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("weizhangDao")
@Repository
public class WeizhangDaoImpl extends HibernateService implements IWeizhangDao {
	
	@Override
	public String save(Weizhang weizhang) {
		Serializable skey = getSession().save(weizhang);
		return skey.toString();
	}
	@Override
	public void saveWeizhang(Weizhang weizhang) {
		getSession().saveOrUpdate(weizhang);
	}
	@Override
	public boolean delete(int id) {
		String hql = "delete from Weizhang u where u.id = :id";
		Query query = super.getSession().createQuery(hql).setParameter("id",id);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void delete(Weizhang weizhang) {
		getSession().delete(weizhang);
	}

	@Override
	public void update(Weizhang weizhang) {
		getSession().clear();
		getSession().update(weizhang);
	}

	@Override
	public Weizhang get(int weizhangId) {
		return (Weizhang) getSession().get(Weizhang.class, weizhangId);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List getList(String hql,Object[] objs) {
		return super.find(hql,objs);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public Page getPageList(Page page , String hql ,Object... values) {
		return super.findSQLMapPage(page, hql,values);
	}
	
	
	@Override
	public List getListSql(String sql ,Object... values) {
		return super.findSQL(sql, values);
	}
	
}
