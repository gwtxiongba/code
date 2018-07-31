package com.chanceit.http.dao;

import java.util.List;
import java.util.Map;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Message;
import com.chanceit.http.pojo.User;

public interface IMessageDao {

	public String save(Message message);

	public boolean delete(List ids);

	public void delete(Message message);

	public void update(Message message);

	public Message get(int messageId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);
	
	/**
	 * 
	 * @author Administrator
	 * @date Sep 24, 2013
	 * @param year
	 * @param month
	 * @param day
	 * @param companyId
	 * @return
	 * @Description 根据年月日获取消息数量
	 */
	public long getCountByDate(String year,String month,String day,String companyId);
	/**
	 * 
	 * @author Administrator
	 * @date Sep 24, 2013
	 * @param year
	 * @param month
	 * @param companyId
	 * @return
	 * @Description 根据年月获取年，月，日的消息数量统计
	 */
	@SuppressWarnings("unchecked")
	public Map getDetailCount(String year,String month,String companyId);
	
	public long getUserCountMessage(User user ,String business ,long countDay);
	
	/**
	 * 获取消息中心列表
	 * @param page
	 * @param values
	 * @return
	 */
	public Page getMsgList(Page page, String hql);
	
	/**
	 * 获取车辆消息列表
	 * @param page
	 * @param values
	 * @return
	 */
	public Page getMsgInforList(Page page,String hql);
}