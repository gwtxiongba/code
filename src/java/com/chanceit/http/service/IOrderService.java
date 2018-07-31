package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Order;

public interface IOrderService {
	public boolean update(Order Order);

	public Order get(int orderId);
	public Page getPageList(Page page,Object[] values,int flag );
	public boolean ifExist(String orderName) throws Exception;
	public Order getByName(String orderName) throws Exception;
	public String save(Order order);
	public boolean saveOrder(Order order);
	public Order getOrder(int orderId);
	public boolean deleteOrder(String ids,int accountId) ;
	public List getCount(Object[] values);
	/**
	 * @param orderId
	 * @return
	 * @throws Exception
	 * @des orderId是否有未解绑的车辆
	 */


	
	public Page getOrderLogPage(Page page,Object[] values);
	public List getCar(Object[] values);
	public List getList(int type,int uid,String status);
	public List getOrderBId(int orderId);
	public List getOrdersByTeamId(String teamIds, Object... values);
}