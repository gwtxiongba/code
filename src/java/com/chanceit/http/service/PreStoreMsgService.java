package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IpreStoreMsgDao;
import com.chanceit.http.pojo.PreStoreMessage;

@Transactional
@Component("preStoreMsgService")
public class PreStoreMsgService implements IpreStoreMsgService {
	@Autowired
	@Qualifier("preStoreMsgDao")
	private IpreStoreMsgDao preStoreMsgDao;

	@Override
	public Page getMsgList(Page page, Object[] values) {
		return preStoreMsgDao.getMsgList(page, values[0]);
	}
	
	@Override
	public boolean save(PreStoreMessage preStoreMessage) {
		try{
			preStoreMsgDao.save(preStoreMessage);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public PreStoreMessage getPsMsg(int psMsgId) {
		
		return null;
	}

	@Override
	public boolean deleteMsg(String psMsgIds) {
		String[] idsAry = psMsgIds.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return preStoreMsgDao.delete(list);
	}


}
