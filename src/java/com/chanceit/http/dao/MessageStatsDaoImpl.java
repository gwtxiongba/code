package com.chanceit.http.dao;

import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.MessageStats;

@Component("messageStatsDao")
@Repository
public class MessageStatsDaoImpl extends HibernateService implements IMessageStatsDao {
	
	@Override
	public String save(MessageStats MessageStats) {
		return getSession().save(MessageStats).toString();
	}
	
	@Override
	public boolean delete(List ids) {
		String hql = "delete from MessageStats where id in (:ids)";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void delete(MessageStats MessageStats) {
		getSession().delete(MessageStats);
	}

	@Override
	public void update(MessageStats MessageStats) {
		getSession().clear();
		getSession().update(MessageStats);
	}

	@Override
	public MessageStats get(String id) {
		return (MessageStats) getSession().get(MessageStats.class, id);
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


}
