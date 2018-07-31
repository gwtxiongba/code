package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.ILevelDao;
import com.chanceit.http.dao.IOrderDao;
import com.chanceit.http.pojo.Order;

@Transactional
@Component("orderService")
public class OrderService implements IOrderService {
	@Autowired
	@Qualifier("orderDao")
	private IOrderDao orderDao;
	
	@Autowired
	@Qualifier("levelDao")
	private ILevelDao levelDao;
	
	
	@Override
	public Order getOrder(int orderId) {
		return orderDao.get(orderId);
	}
	
	@Override
	public String save(Order order) {
		try{
			return orderDao.save(order);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean saveOrder(Order order) {
		try{
			orderDao.saveOrder(order);
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
		return orderDao.delete(list,accountId);
	}
	
	@Override
	public boolean update(Order order) {
		orderDao.update(order);
		return true;
	}
	

	@Override
	public Order get(int orderId) {
		return orderDao.get(orderId);
	}


	@Override
	public Page getPageList(Page page,Object[] values,int flag ){
	  String sql="select o.id as id,o.order_num as orderNum,o.order_user_name as orderUserName,o.car_user_id as carUserId" +
	  		",o.car_user_name as carUserName,o.car_user_tel as carUserTel,o.status as status,o.begin_time as beginTime,o.end_time as endTime,o.begin_addr as beginAddr," +
	  		"o.end_addr as endAddr,o.start_time as startTime,o.over_time as overTime,o.miles as miles,o.glf as glf,IFNULL(o.cost,0) as cost,o.takes as takes,o.remark as remark,IFNULL(o.reason,'') as reason" +
	  		",o.begin_lat as beginLat,o.begin_lng as beginLng, o.end_lat as endLat,o.end_lng as endLng,o.pnumber as pnumber,d.driver_name as driverName,d.driver_tel as driverTel,u.plate as plate,u.brand as brand " +
	  		",c.name as caruse,cs.name as carStyle from orders o left join member m on o.car_user_id = m.id " +
	  		" left join user u on o.car_id=u.user_id " +
	  		" left join caruses c on c.id = o.cause "+
	  		" left join carstyle cs on cs.id = o.carstyle_id"+
	  		" left join driver d on o.driver_id = d.driver_id where m.team_id in( "+values[0]+")";
		
	   if(values[2]!=null && !values[2].equals("")){
		//  sql += " where m.team_id in( "+values[0]+")";
		  sql += " and m.dept_id in( "+values[2]+")";
	    }else{
	    }
		if(values[1]!=null && !values[1].equals("")){
			sql += " and o.status in("+values[1]+")";
		}
		sql +=" order by o.id desc";
		return orderDao.getPageList(page, sql);
	}

	
	@Override
	public boolean ifExist(String orderName) throws Exception{
		String sql = "select * from order where order_name=?";
		List list = orderDao.getListSql(sql, orderName);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean deleteOrder(String ids, int accountId) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return orderDao.delete(list,accountId);
	}

	@Override
	public Order getByName(String orderName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	public List getCount(Object[] values){
		String sql = "select o.* from orders o left join member m on o.car_user_id = m.id  where 1=1";
		      if(values[0]!=null && !values[0].equals("")){
				  sql += " and m.team_id in( "+values[0]+")";
			    }
		      if(values[2]!=null && !values[2].equals("")){
			    	  sql += " and m.dept_id in( "+values[2]+")";
			    }
				if(values[1]!=null && !values[1].equals("")){
					sql += " and o.status in("+values[1]+")";
				}
				
				return orderDao.getListSql(sql,null);
	}
	


	@Override
	public Page getOrderLogPage(Page page, Object[] values) {
		StringBuffer hql = new StringBuffer("from SignInLog s where 1=1"); 
		if(values[0] != null && !"".equals(values[0])){
			hql.append(" and s.order.orderId=").append(values[0]);
		}
		if(values[1]!= null && !"".equals(values[1])){
			hql.append(" and s.car.account.accountId=").append(values[1]);
		}
		if(values[1]!= null && !"".equals(values[1])){
			hql.append(" and s.order.account.accountId=").append(values[1]);
		}
		if(values[2]!= null && !"".equals(values[2])){
			hql.append(" and s.car.team.teamId in (").append(values[2]).append(")");
		}
		hql.append(" order by s.signInTime desc ");
		return orderDao.getPageList(page, hql.toString());
	}
	
	@Override
	public List getCar(Object[] values){
		String  sql = "select * from user where user_id in (select user_id from sign_in_log where order_id=? and unbind_time is null)";
		return orderDao.getListSql(sql, values[0]);
	}
	

	
	@Override
	public List getList(int type,int uid,String status){
		String sql="select o.id as id,o.order_num as orderNum,o.order_user_name as orderUserName,o.car_user_id as carUserId" +
  		",o.car_user_name as carUserName,o.car_user_tel as carUserTel,o.status as status,o.begin_time as beginTime,o.end_time as endTime,o.begin_addr as beginAddr," +
  		"o.end_addr as endAddr,o.start_time as startTime,o.over_time as overTime,IFNULL(o.miles,0) as miles, IFNULL(o.glf,0) as glf, IFNULL(o.cost,0) as cost,o.takes as takes,o.remark as remark,o.reason as reason" +
  		",o.begin_lat as beginLat,o.begin_lng as beginLng, o.end_lat as endLat,o.end_lng as endLng,o.pnumber as pnumber,d.driver_name as driverName,d.driver_tel as driverTel,u.identifier as carId, u.plate as plate,u.brand as brand " +
  		",c.name as caruse,cs.name as carStyle,o.create_time as createTime,o.driver_id as driverid " +
  		",IFNULL(o.score,1) as score,IFNULL(o.feedback,'') as feedback from orders o left join member m on o.car_user_id = m.id " +
  		"left join user u on o.car_id=u.user_id " +
  		" left join driver d on o.driver_id = d.driver_id" +
  		" left join caruses c on c.id = o.cause " +
  		" left join carstyle cs on cs.id = o.carstyle_id where 1=1";
	
   if(type == 0){
	     sql += " and o.car_user_id ="+uid;
    }else if(type == 1){
    	 sql += " and o.driver_id ="+uid;
    }else if(type == 2){
    	 sql += " and m.team_id in ("+uid+")";
    }else{
    	 sql += " and m.dept_id in ("+uid+")";
    }
   
     sql += " and o.status in("+status+")";
//	if(values[1]!=null && !values[1].equals("")){
//		sql += " and o.status in("+values[1]+")";
//	}
	sql +=" order by o.id desc";
		return orderDao.getListSql(sql,null);
	}

	
	
	@Override
	public List getOrderBId(int orderId){
	
		String sql = "select o.car_user_name as fareName, o.car_user_tel as fareTel ,o.begin_addr as pos1,o.end_addr as pos2,u.plate as plate,d.driver_name as driverName,d.driver_tel as driverTel,t.team_name as  unit from orders o ,user u,driver d,team t where u.user_id = o.car_id and d.driver_id = o.driver_id and u.team_id = t.team_id and o.id ="+orderId;
		return orderDao.getListSql(sql,null);
	}

	@Override
	public List getOrdersByTeamId(String teamIds, Object... values) {
		// TODO Auto-generated method stub
		String sql="select * from orders where car_user_id in (select id from member where team_id in( "+teamIds+"))";
		return orderDao.getOrdersByTeamId(sql, values);
	}
	
	
}
