package com.chanceit.http.service;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Card;

public interface ICardService {
	public void save(Card card);
	public Page getPageList(Page page, Object[] values);
	public Card get(int card);
	public boolean deleteAll(String ids);
	public Card getCardByCardNo(String cardNo);
}