package com.chanceit.http.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Message;
import com.chanceit.http.pojo.User;

@Component("messageDao")
@Repository
public class MessageDaoImpl extends HibernateService implements IMessageDao {
	
	@Override
	public String save(Message message) {
		return getSession().save(message).toString();
	}
	
	@Override
	public boolean delete(List ids) {
		String hql = "delete from Message where messageId in (:ids)";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void delete(Message message) {
		getSession().delete(message);
	}

	@Override
	public void update(Message message) {
		getSession().clear();
		getSession().update(message);
	}

	@Override
	public Message get(int messageId) {
		return (Message) getSession().get(Message.class, messageId);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List getList(String hql,Object[] objs) {
		return super.find(hql,objs);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Page getPageList(Page page , String hql ,Object... values) {
		return super.findMapPage(page, hql,values);
	}
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
	@Override
	public long getCountByDate(String year,String month,String day,String companyId) {
		String hql="from Message m where m.company.companyId='"+companyId+"'";
		if(StringUtils.isNotEmpty(year)){
			hql=hql+" and year(m.createTime)='"+year+"'";
		}
		if(StringUtils.isNotEmpty(month)){
			hql=hql+" and month(m.createTime)='"+month+"'";
		}
		if(StringUtils.isNotEmpty(day)){
			hql=hql+" and day(m.createTime)='"+day+"'";
		}
		return countHQLResult(hql,null);
	}
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
	@Override
	public Map getDetailCount(String companyId,String year, String month) {
		String sql="";
		Map<String,List> map=new HashMap<String,List>();
		String type="";
		//返回查询每年统计数量的sql
		if(StringUtils.isEmpty(year)&&StringUtils.isEmpty(month)){
			sql="SELECT YEAR(create_time) d,COUNT(*) c FROM message where company_id='"+companyId+"' GROUP BY YEAR(create_time)";
			type="year";
		}
		//返回查询当年每月的数量sql
		if(StringUtils.isNotEmpty(year)){
			sql="SELECT MONTH(create_time) d,COUNT(*) c FROM message WHERE company_id='"+companyId+"' and  YEAR(create_time)='"+year+"' GROUP BY MONTH(create_time) ";
			type="month";
		}
		//返回查询当年月每天的数量sql
		if(StringUtils.isNotEmpty(year)&&StringUtils.isNotEmpty(month)){
			sql="SELECT DAY(create_time) d ,COUNT(*) c FROM message WHERE company_id='"+companyId+"' and YEAR(create_time)='"+year+"' AND MONTH(create_time)='"+month+"' GROUP BY DAY(create_time)";
			type="day";
		}
		if("".equals(sql)){
			return null;
		}else{
			 map.put(type, getSQLQuery(sql,null).list());
		}
		return map;
	}

	/** 
	 * @author Administrator
	 * @date Nov 25, 2013
	 * @param user
	 * @param business
	 * @param countDay
	 * @return
	 * @Description TODO(这里用一句话描述这个方法的作用)
	 * @see com.chanceit.common.dao.IMessageDao#getUserCountMessage(com.chanceit.common.pojos.User, java.lang.String, long)
	 */
	@Override
	public long getUserCountMessage(User user, String business, long countDay) {
		String date = DateUtil.getBeforeDate(new Date(), (int)countDay, "yyyy-MM-dd hh:mm:ss");
		String hql = "select count(*) from Message m where m.type = ? and m.recipient = ? and m.createTime > '"+date +"'";
		return findUnique(hql, business ,user.getIdentifier());
	}

	@Override
	public Page getMsgList(Page page, String sql) {
		return findSQLPage(page, sql);
	}

	@Override
	public Page getMsgInforList(Page page,String sql) {
		return findSQLPage(page,sql);
	}
}
