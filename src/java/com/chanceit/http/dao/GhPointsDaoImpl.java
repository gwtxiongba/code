package com.chanceit.http.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.http.pojo.GhPoints;
@Component("ghpointDao")
@Repository
@SuppressWarnings("unchecked")
public class GhPointsDaoImpl extends HibernateService implements IGhPointsDao{

	@Override
	public void add(GhPoints gp) {
		// TODO Auto-generated method stub
		getSession().saveOrUpdate(gp);
	}
    
	@Override
	public List getMaxId(){
		String sql="select max(id) from ghpoints";
		List list = getSqlList(sql);
		return list;
	}
	
    @Override
	public void del(GhPoints gp) {
		// TODO Auto-generated method stub
		getSession().delete(gp);
	}
   
	@Override
	public List<GhPoints> getAll(int accountId,String teams){
		String hql=" from GhPoints where team_id in("+teams+") and ifdel=0 and ws is  null";
		//String hql=" from GhPoints where accountId = "+accountId;
//		if(role){
//			hql=" from GhPoints where op_id = "+accountId+" and ifdel=0 and ws is  null";
//		}
		List<GhPoints> list = find(hql, null);
		return list;
	}
	
	@Override
	public List getLineRuleList(String sql){
		List list = getSqlList(sql); 
		return list;
	}
	
	public boolean delete(List ids) {
		String hql = "delete from GhPoints u where u.id in (:ids)";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void updateDel(int id){
		String sql = "update ghpoints set ifdel=1 where id = "+id;
		excuteSQL(sql);
	}
	
	@Override
	public List<GhPoints> getCircles(int accountId,String type,String teamIds){
		String hql=" from GhPoints where team_id in("+teamIds+") and ifdel=0 and ws = '"+type+"'";
		//String hql=" from GhPoints where accountId = "+accountId;
//		if(role){
//			hql=" from GhPoints where op_id = "+accountId+" and ifdel=0 and ws = '"+type+"'";
//		}
		List<GhPoints> list = find(hql, null);
		return list;
	}
}
