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
import com.chanceit.http.pojo.Order;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("orderDao")
@Repository
public class OrderDaoImpl extends HibernateService implements IOrderDao {
	
	@Override
	public String save(Order order) {
		getSession().flush();
		Serializable skey = getSession().save(order);
		return skey.toString();
	}
	@Override
	public void saveOrder(Order order) {
		getSession().saveOrUpdate(order);
	}
	@Override
	public boolean delete(List ids,int accountId) {
		String hql = "delete from Order u where u.orderId in (:ids) and u.account.accountId=:aid";
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
		String hql = "update Order u set delTime=:sj,ifDel=1 where u.orderId in (:ids) and u.account.accountId=:aid";
		Query query = super.getSession().createQuery(hql).setParameter("sj", new Date()).setParameterList("ids",ids).setParameter("aid", accountId);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	@Override
	public void delete(Order order) {
		getSession().delete(order);
	}

	@Override
	public void update(Order order) {
		getSession().clear();
		getSession().update(order);
	}

	@Override
	public Order get(int orderId) {
		return (Order) getSession().get(Order.class, orderId);
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
		return super.findSQLMapPage(page, hql,values);
	}
	
	
	@Override
	public List getListSql(String sql ,Object... values) {
		return super.findSQL(sql, values);
	}
	
	//根据teamId查找对应下的预约订单
	@Override
	public List getOrdersByTeamId(String sql, Object... values) {
		// TODO Auto-generated method stub
		return super.findSQL(sql, values);
	}
	
}
