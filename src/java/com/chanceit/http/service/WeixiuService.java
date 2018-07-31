package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.ILevelDao;
import com.chanceit.http.dao.IWeixiuDao;
import com.chanceit.http.pojo.Weixiu;
import com.chanceit.http.pojo.Member;

@Transactional
@Component("weixiuService")
public class WeixiuService implements IWeixiuService {
	@Autowired
	@Qualifier("weixiuDao")
	private IWeixiuDao weixiuDao;
	
	
	@Override
	public Weixiu getWeixiu(int weixiuId) {
		return weixiuDao.get(weixiuId);
	}
	
	@Override
	public String save(Weixiu weixiu) {
		try{
			return weixiuDao.save(weixiu);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean saveWeixiu(Weixiu weixiu) {
		try{
			weixiuDao.saveWeixiu(weixiu);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	public boolean delete(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return weixiuDao.delete(list);
	}
	
	@Override
	public boolean update(Weixiu weixiu) {
		weixiuDao.update(weixiu);
		return true;
	}
	

	@Override
	public Weixiu get(int weixiuId) {
		return weixiuDao.get(weixiuId);
	}


	@Override
	public Page getPageList(Page page,Object[] values ){
		String sql ="select w.*,u.plate as plate from weixiu w left join user u on w.car_id=u.user_id where 1=1 ";
//		if(values[0]!=null && !values[0].equals("")){
//			sql.append(" and l.account.accountId in(").append(values[0]).append(")");
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			sql.append(" and l.weixiuName like '%").append(values[1]).append("%'");
//		}
		if(values[0]!=null && !values[0].equals("")){
			sql+=" and w.status in ("+values[0]+")";
		}
		if(values[1]!=null && !values[1].equals("")){
			sql+=" and u.team_id = "+values[1];
		}
		if(values[2]!=null && !values[2].equals("")){
			sql+=" and w.name like '%"+values[2]+"%'";
		}
		if(values[3]!=null && !values[3].equals("")){
			sql+=" and u.plate like '%"+values[3]+"%'";
		}
		sql+=" order by w.id desc";
		return weixiuDao.getPageList(page, sql);
	}

	
	@Override
	public boolean deleteWeixiu(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return weixiuDao.delete(list);
	}

	
	@Override
	public List getList(int type,int uid,String status){
		String sql ="select a.*,d.driver_name as name,d.driver_tel as tel,d.user_name as userName from weixiu a left join driver d on a.user_id=d.driver_id where 1=1 ";
//		if(values[0]!=null && !values[0].equals("")){
//			sql.append(" and l.account.accountId in(").append(values[0]).append(")");
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			sql.append(" and l.weixiuName like '%").append(values[1]).append("%'");
//		}
		if(type == 1){
			sql+=" and d.driver_id = "+uid;
			
		}else{
			sql+=" and d.team_id = "+uid;
		}
		if(status.length()>0){
		sql+=" and a.status in ("+status+")";
		}
		sql+=" order by a.id desc";
		return weixiuDao.getListSql(sql, null);
	}

	
	@Override
	public List getWeixiuBId(int weixiuId){
		String sql = "select * from weixiu d left join user u on u.weixiu_id = d.weixiu_id where d.weixiu_id ="+weixiuId;
		return weixiuDao.getListSql(sql,null);
	}
}
