package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.ILevelDao;
import com.chanceit.http.dao.IAskleaveDao;
import com.chanceit.http.pojo.Askleave;
import com.chanceit.http.pojo.Member;

@Transactional
@Component("askleaveService")
public class AskleaveService implements IAskleaveService {
	@Autowired
	@Qualifier("askleaveDao")
	private IAskleaveDao askleaveDao;
	
	
	@Override
	public Askleave getAskleave(int askleaveId) {
		return askleaveDao.get(askleaveId);
	}
	
	@Override
	public String save(Askleave askleave) {
		try{
			return askleaveDao.save(askleave);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean saveAskleave(Askleave askleave) {
		try{
			askleaveDao.saveAskleave(askleave);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	public boolean delete(String ids,int accountId) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return askleaveDao.delete(list,accountId);
	}
	
	@Override
	public boolean update(Askleave askleave) {
		askleaveDao.update(askleave);
		return true;
	}
	

	@Override
	public Askleave get(int askleaveId) {
		return askleaveDao.get(askleaveId);
	}


	@Override
	public Page getPageList(Page page,Object[] values ){
		String sql ="select a.*,d.driver_name as name ,d.driver_tel as tel,d.user_name as userName from askleave a left join driver d on a.user_id=d.driver_id where 1=1 ";
//		if(values[0]!=null && !values[0].equals("")){
//			sql.append(" and l.account.accountId in(").append(values[0]).append(")");
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			sql.append(" and l.askleaveName like '%").append(values[1]).append("%'");
//		}
		if(values[0]!=null && !values[0].equals("")){
			sql+=" and a.status in ("+values[0]+")";
		}
		if(values[1]!=null && !values[1].equals("")){
			sql+=" and d.team_id = "+values[1];
		}
		if(values[2]!=null && !values[2].equals("")){
			sql+=" and d.driver_name like '%"+values[2]+"%'";
		}
		sql+=" order by a.id desc";
		return askleaveDao.getPageList(page, sql);
	}

	
	@Override
	public boolean deleteAskleave(String ids, int accountId) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return askleaveDao.delete(list,accountId);
	}

	@Override
	public Askleave getByName(String askleaveName) throws Exception {
		String hql = " from Askleave where askleaveName=?";
		List list = askleaveDao.getList(hql, new Object[]{askleaveName});
		if(list.size()>0){
			return (Askleave)list.get(0);
		}
		else{
			return null;
		}
	}

	
	


	
	@Override
	public List getList(int type,int uid,String status){
		String sql ="select a.*,d.driver_name as name,d.driver_tel as tel,d.user_name as userName from askleave a left join driver d on a.user_id=d.driver_id where 1=1 ";
//		if(values[0]!=null && !values[0].equals("")){
//			sql.append(" and l.account.accountId in(").append(values[0]).append(")");
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			sql.append(" and l.askleaveName like '%").append(values[1]).append("%'");
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
		return askleaveDao.getListSql(sql, null);
	}

	
	@Override
	public List getAskleaveBId(int askleaveId){
		String sql = "select * from askleave d left join user u on u.askleave_id = d.askleave_id where d.askleave_id ="+askleaveId;
		return askleaveDao.getListSql(sql,null);
	}
}
