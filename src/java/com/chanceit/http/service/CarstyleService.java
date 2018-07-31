package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.ICarstyleDao;
import com.chanceit.http.pojo.Carstyle;

@Transactional
@Component("carstyleService")
public class CarstyleService implements ICarstyleService {
	@Autowired
	@Qualifier("carstyleDao")
	private ICarstyleDao carstyleDao;
	
	
	
	@Override
	public String save(Carstyle carstyle) {
		try{
			return carstyleDao.save(carstyle);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean saveCarstyle(Carstyle carstyle) {
		try{
			carstyleDao.saveCarstyle(carstyle);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public boolean update(Carstyle carstyle) {
		carstyleDao.update(carstyle);
		return true;
	}
	
	@Override
	public boolean deleteCarstyle(int id) {
		return carstyleDao.delete(id);
	}

	@Override
	public Carstyle get(int carstyleId) {
		return carstyleDao.get(carstyleId);
	}


	@Override
	public Page getPageList(Page page,Object[] values ){
		StringBuffer hql = new StringBuffer("" +
				" from Carstyle l where 1=1 ");
//		if(values[0]!=null && !values[0].equals("")){
//			hql.append(" and l.account.accountId in(").append(values[0]).append(")");
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			hql.append(" and l.carstyleName like '%").append(values[1]).append("%'");
//		}
//		if(values[0]!=null && !values[0].equals("")){
//			hql.append(" and l.teamId =").append(values[0]);
//		}
		hql.append(" order by l.id desc");
		return carstyleDao.getPageList(page, hql.toString());
	}

	
	@Override
	public List getList(Object[] values ){
		StringBuffer hql = new StringBuffer("" +
				" from Carstyle l where 1=1 ");
		
		
		return carstyleDao.getList(hql.toString(),null);
	}

}
