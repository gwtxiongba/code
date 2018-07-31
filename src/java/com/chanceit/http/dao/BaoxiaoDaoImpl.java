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
import com.chanceit.http.pojo.Baoxiao;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("baoxiaoDao")
@Repository
public class BaoxiaoDaoImpl extends HibernateService implements IBaoxiaoDao {
	
	@Override
	public String save(Baoxiao baoxiao) {
		Serializable skey = getSession().save(baoxiao);
		return skey.toString();
	}
	@Override
	public void saveBaoxiao(Baoxiao baoxiao) {
		getSession().saveOrUpdate(baoxiao);
	}
	@Override
	public boolean delete(List ids,int accountId) {
		String hql = "delete from Baoxiao u where u.baoxiaoId in (:ids) and u.account.accountId=:aid";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids).setParameter("aid", accountId);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void delete(Baoxiao baoxiao) {
		getSession().delete(baoxiao);
	}

	@Override
	public void update(Baoxiao baoxiao) {
//		getSession().clear();
//		getSession().update(baoxiao);
		String hql = "update Baoxiao set status = :status,info2=:info2 where id= :id";
		Query query = getSession().createQuery(hql).setParameter("status",baoxiao.getStatus()).setParameter("info2",baoxiao.getInfo2()).setParameter("id", baoxiao.getId());
		query.executeUpdate();
	}

	@Override
	public Baoxiao get(int baoxiaoId) {
		return (Baoxiao) getSession().get(Baoxiao.class, baoxiaoId);
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
	public void updateBaoxiao(String hql) {
		Query query = getSession().createQuery(hql);
		query.executeUpdate();  
	}
	
	
}
