package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IWeibaoDao;
import com.chanceit.http.pojo.Weibao;

@Transactional
@Component("weibaoService")
public class WeibaoService implements IWeibaoService {
	@Autowired
	@Qualifier("weibaoDao")
	private IWeibaoDao weibaoDao;
	
	
	
	@Override
	public String save(Weibao weibao) {
		try{
			return weibaoDao.save(weibao);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean saveWeibao(Weibao weibao) {
		try{
			weibaoDao.saveWeibao(weibao);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public boolean update(Weibao weibao) {
		weibaoDao.update(weibao);
		return true;
	}
	
	@Override
	public boolean deleteWeibao(int id) {
		return weibaoDao.delete(id);
	}

	@Override
	public Weibao get(int weibaoId) {
		return weibaoDao.get(weibaoId);
	}


	@Override
	public Page getPageList(Page page,Object[] values ){
		String sql="select w.*,u.plate from weibao w left join user u on w.car_id=u.user_id where 1=1 ";
		if(values[0]!=null && !values[0].equals("")){
				sql += " and u.team_id in("+values[0]+")";
		}		
		if(values[1]!=null && !values[1].equals("")){
			sql += " and u.plate like '%"+values[1]+"%'";
		}
		if("1".equals(values[2])){
			Date d = DateUtil.getAfterDate(new Date(), 30);
			
			sql += " and (w.year_time < '"+DateUtil.getDateY(d)+"' or w.keep_time < '"+DateUtil.getDateY(d)+"' or w.safe_time<'"+DateUtil.getDateY(d) +"')";
		}
		sql +=" order by w.id desc";
		
		return weibaoDao.getPageList(page, sql);
	}

	
	@Override
	public List getList(Object[] values ){
		StringBuffer hql = new StringBuffer("" +
				" from Weibao l where 1=1 ");
//		if(values[0]!=null && !values[0].equals("")){
//			hql.append(" and l.account.accountId=").append(values[0]);
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			hql.append(" and l.weibaoName like '%").append(values[1]).append("%'");
//		}
		hql.append(" order by l.id desc");
		return weibaoDao.getList(hql.toString(),null);
	}
	
	public List getListEnd(Object[] values ){
		String sql="select w.*,u.plate from weibao w left join user u on w.car_id=u.user_id where 1=1 ";
		if(values[0]!=null && !values[0].equals("")){
				sql += " and u.team_id in("+values[0]+")";
		}		
		if(values[1]!=null && !values[1].equals("")){
			sql += " and u.plate like '%"+values[1]+"%'";
		}
		if("1".equals(values[2])){
			Date d = DateUtil.getAfterDate(new Date(), 30);
			
			sql += " and (w.year_time < '"+DateUtil.getDateY(d)+"' or w.keep_time < '"+DateUtil.getDateY(d)+"' or w.safe_time<'"+DateUtil.getDateY(d) +"')";
		}
		sql +=" order by w.id desc";
		
		return weibaoDao.getListSql(sql,null);
	}
	
}
