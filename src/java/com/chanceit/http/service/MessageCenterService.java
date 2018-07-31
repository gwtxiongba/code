package com.chanceit.http.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IMessageDao;
import com.chanceit.http.pojo.Message;

@Transactional
@Component("messageCenterService")
public class MessageCenterService implements IMessageCenterService {
	@Autowired
	@Qualifier("messageDao")
	private IMessageDao messageDao;

	@Override
	public Page getMsgList(Page page, Object[] values) {
		StringBuffer hql = new StringBuffer();
		hql.append("select  ");
		hql.append("a.car_id as carId, ");
		hql.append("a.message_id as msgId, ");
		hql.append("u.plate as plate, ");
		hql.append("u.identifier as identifier, ");
		hql.append("a.message_content as content, ");
		hql.append("a.create_time as createTime, ");
		hql.append("a.ifread  as ifread");
		hql.append(" from ");
		hql.append("(SELECT car_id , message_id, message_content, create_time, ifread FROM message where 1=1 ");
		if(values[0] != null){
			hql.append(" and company_id = ").append(values[0]);
		}
		//ÅÐ¶ÏÐÅÏ¢Î´¶Á
		if(values[1] != null && (Integer)values[1] != 0){
			hql.append(" and ifread = ").append(values[1]);
		}else{
			hql.append(" and ifread = 0 ");
		}
		/*
		if(values[2] != null){
			hql.append(" and date_format(create_time, '%Y-%c-%d') >= str_to_date('").append(values[2]).append("', '%Y-%c-%d')");
		}
		if(values[3] != null){
			hql.append(" and date_format(create_time, '%Y-%c-%d') <= str_to_date('").append(values[3]).append("', '%Y-%c-%d')");
		}*/
		hql.append(" ORDER BY create_time DESC) a  ");
		hql.append(" JOIN user u  ");
		hql.append(" ON a.car_id = u.user_id ");
		hql.append(" GROUP BY a.create_time DESC");
		return messageDao.getMsgList(page, hql.toString());
	}

	@Override
	public boolean save(List<Message> msgList) {
		try{
			for(Message msg: msgList){
				messageDao.save(msg);
			}	
			return true;
		}catch(Exception e){
			return false;
		}
		
	}

	@Override
	public Page getMsgInforList(Page page,Object[] values) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT car_id as carId,");
		sql.append(" message_id as msgId,");
		sql.append(" message_content as content,");
		sql.append(" create_time as createTime,");
		sql.append(" ifread as ifread");
		sql.append(" FROM message where 1=1");
		if(values[0] != null){
			sql.append(" and company_id = ").append(values[0]);
		}
		//ÅÐ¶ÏÐÅÏ¢Î´¶Á
		if(values[1] != null){
			sql.append(" and car_id = ").append(values[1]);
		}
		if(values[2] != null && !"".equals(values[2])){
			sql.append(" and date_format(create_time, '%Y-%c-%d') >= str_to_date('").append(values[2]).append("', '%Y-%c-%d')");
		}
		if(values[3] != null && !"".equals(values[3])){
			sql.append(" and date_format(create_time, '%Y-%c-%d') <= str_to_date('").append(values[3]).append("', '%Y-%c-%d')");
		}
		sql.append(" ORDER BY create_time DESC ");
		
		return messageDao.getMsgInforList(page,sql.toString());
	}
	
	@Override
	public boolean save(Message msg) {
		try{
			messageDao.save(msg);
			return true;
		}catch(Exception e){
			return false;
		}
		
	}
	
}
