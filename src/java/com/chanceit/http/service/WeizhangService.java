package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IWeizhangDao;
import com.chanceit.http.pojo.Weizhang;

@Transactional
@Component("weizhangService")
public class WeizhangService implements IWeizhangService {
	@Autowired
	@Qualifier("weizhangDao")
	private IWeizhangDao weizhangDao;
	
	
	
	@Override
	public String save(Weizhang weizhang) {
		try{
			return weizhangDao.save(weizhang);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean saveWeizhang(Weizhang weizhang) {
		try{
			weizhangDao.saveWeizhang(weizhang);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public boolean update(Weizhang weizhang) {
		weizhangDao.update(weizhang);
		return true;
	}
	
	@Override
	public boolean deleteWeizhang(int id) {
		return weizhangDao.delete(id);
	}

	@Override
	public Weizhang get(int weizhangId) {
		return weizhangDao.get(weizhangId);
	}


	@Override
	public Page getPageList(Page page,Object[] values ){
		String sql="select w.*,u.plate,d.driver_name as driverName from weizhang w left join user u on w.car_id=u.user_id left join driver d on w.driver_id=d.driver_id where 1=1 ";
		if(values[0]!=null && !values[0].equals("")){
				sql += " and u.team_id in("+values[0]+")";
		}		
		if(values[1]!=null && !values[1].equals("")){
			sql += " and u.plate like '%"+values[1]+"%'";
		}
		sql +=" order by w.id desc";
		return weizhangDao.getPageList(page, sql);
	}

	
	@Override
	public List getList(Object[] values ){
		StringBuffer hql = new StringBuffer("" +
				" from Weizhang l where 1=1 ");
//		if(values[0]!=null && !values[0].equals("")){
//			hql.append(" and l.account.accountId=").append(values[0]);
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			hql.append(" and l.weizhangName like '%").append(values[1]).append("%'");
//		}
		hql.append(" order by l.id desc");
		return weizhangDao.getList(hql.toString(),null);
	}

}
