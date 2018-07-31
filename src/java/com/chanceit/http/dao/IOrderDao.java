package com.chanceit.http.dao;

import java.util.List;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Order;


public interface IOrderDao {

	public String save(Order order);

	public boolean delete(List ids,int accountId);

	public void delete(Order order);

	public void update(Order order);

	public Order get(int orderId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public List getMapList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);

	public List getListSql(String sql, Object... values);
	public boolean fakeDelete(List ids,int accountId);

	public void saveOrder(Order order);
	
	public List getOrdersByTeamId(String sql, Object... values);
}