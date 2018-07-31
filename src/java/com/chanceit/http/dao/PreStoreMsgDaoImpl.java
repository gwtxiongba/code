package com.chanceit.http.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.PreStoreMessage;

/**
 * @author zhangxin
 * @description 
 */
@Component("preStoreMsgDao")
@Repository
public class PreStoreMsgDaoImpl extends HibernateService implements IpreStoreMsgDao {

	@Override
	public Page getMsgList(Page page, Object... values) {
		String sql = "select p.pre_msg_id as preMsgId, p.pre_msg_content as preMsgContent from prestore_msg p where p.account_id=?";
		return super.findSQLMapPage(page, sql, values);
	}

	@Override
	public boolean delete(List ids) {
		String hql = "delete from PreStoreMessage p where p.preMsgId in (:ids) ";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void save(PreStoreMessage preStoreMessage) {
		getSession().save(preStoreMessage);
	}

}
