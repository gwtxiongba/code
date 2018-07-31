package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.ICardDao;
import com.chanceit.http.pojo.Card;

@Transactional
@Component("cardService")
public class CardService implements ICardService {
	@Autowired
	@Qualifier("cardDao")
	private ICardDao cardDao;
	
	/**
	 * ������֤�������Ϣ
	 */
	@Override
	public void save(Card card) {
		cardDao.save(card);
	}
	
	/**
	 * ��ȡ����˾�������б�
	 */
	@Override
	public Page getPageList(Page page, Object[] values){
		StringBuffer hql = new StringBuffer();
		hql.append(" from Card c where 1=1 ");
		if(values[0]!=null && !values[0].equals("")){
			hql.append(" and c.cardId=").append(values[0]);
		}
		hql.append(" order by c.cardId desc");
		return cardDao.getPageList(page, hql.toString());
	}

	/**
	 * ͨ��ID��ѯ����˾��������Ϣ
	 */
	@Override
	public Card get(int cardId) {
		return cardDao.get(cardId);
	}
	
	/**
	 * ɾ������˾������
	 */
	@Override
	public boolean deleteAll(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return cardDao.deleteAll(list);
	}

	/**
	 * ͨ����������ȡ������Ϣ
	 */
	@Override
	public Card getCardByCardNo(String cardNo) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from Card c where c.cardNo = ? ");
		hql.append(" order by c.cardId desc");
		List list = cardDao.getList(hql.toString(), new Object[]{cardNo});
		if(list.size() > 0){
			return (Card)list.get(0);
		}else{
			return null;
		}
	}

	

}
