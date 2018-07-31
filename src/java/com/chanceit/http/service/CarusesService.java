package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.ICarusesDao;
import com.chanceit.http.pojo.Caruses;

@Transactional
@Component("carusesService")
public class CarusesService implements ICarusesService {
	@Autowired
	@Qualifier("carusesDao")
	private ICarusesDao carusesDao;
	
	
	
	@Override
	public String save(Caruses caruses) {
		try{
			return carusesDao.save(caruses);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean saveCaruses(Caruses caruses) {
		try{
			carusesDao.saveCaruses(caruses);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public boolean update(Caruses caruses) {
		carusesDao.update(caruses);
		return true;
	}
	
	@Override
	public boolean deleteCaruses(int id) {
		return carusesDao.delete(id);
	}

	@Override
	public Caruses get(int carusesId) {
		return carusesDao.get(carusesId);
	}


	@Override
	public Page getPageList(Page page,Object[] values ){
		StringBuffer hql = new StringBuffer("" +
				" from Caruses l where 1=1 ");
//		if(values[0]!=null && !values[0].equals("")){
//			hql.append(" and l.account.accountId in(").append(values[0]).append(")");
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			hql.append(" and l.carusesName like '%").append(values[1]).append("%'");
//		}
//		if(values[0]!=null && !values[0].equals("")){
//			hql.append(" and l.teamId =").append(values[0]);
//		}
		hql.append(" order by l.id desc");
		return carusesDao.getPageList(page, hql.toString());
	}

	
	@Override
	public List getList(Object[] values ){
		StringBuffer hql = new StringBuffer("" +
				" from Caruses l where 1=1 ");
		return carusesDao.getList(hql.toString(),null);
	}

}
