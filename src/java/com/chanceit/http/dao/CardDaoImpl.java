package com.chanceit.http.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Card;

/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("cardDao")
@Repository
public class CardDaoImpl extends HibernateService implements ICardDao {
	
	@Override
	public void save(Card card) {
		getSession().saveOrUpdate(card);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Page getPageList(Page page , String hql ,Object... values) {
		return super.findListPage(page, hql,values);
	}

	@Override
	public Card get(int cardId) {
		return (Card) getSession().get(Card.class, cardId);
	}
	
	@Override
	public boolean deleteAll(List ids) {
		String hql = "delete from Card c where c.cardId in (:ids)";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List getList(String hql,Object[] objs) {
		return super.find(hql,objs);
	}

}
