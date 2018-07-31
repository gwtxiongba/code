package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.ILevelDao;
import com.chanceit.http.dao.IDriverDao;
import com.chanceit.http.pojo.Driver;
import com.chanceit.http.pojo.Member;

@Transactional
@Component("driverService")
public class DriverService implements IDriverService {
	@Autowired
	@Qualifier("driverDao")
	private IDriverDao driverDao;
	
	@Autowired
	@Qualifier("levelDao")
	private ILevelDao levelDao;
	
	
	@Override
	public Driver getDriver(int driverId) {
		return driverDao.get(driverId);
	}
	
	@Override
	public String save(Driver driver) {
		try{
			return driverDao.save(driver);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean saveDriver(Driver driver) {
		try{
			driverDao.saveDriver(driver);
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
		return driverDao.delete(list);
	}
	
	@Override
	public boolean update(Driver driver) {
		driverDao.update(driver);
		return true;
	}
	

	@Override
	public Driver get(int driverId) {
		return driverDao.get(driverId);
	}


	@Override
	public Page getPageList(Page page,Object[] values ){
		StringBuffer hql = new StringBuffer("" +
				" from Driver l where 1=1 ");
		if(values[0]!=null && !values[0].equals("")){
			hql.append(" and l.teamId in(").append(values[0]).append(")");
		}
		if(values[1]!=null && !values[1].equals("")){
			hql.append(" and l.driverName like '%").append(values[1]).append("%'");
		}
		if(values[2]!=null && !values[2].equals("")){
			hql.append(" and l.status =").append(values[2]);
		}
		if(values[3]!=null && !values[3].equals("")){
			hql.append(" and l.driverTel like '%").append(values[3]).append("%'");
		}
		hql.append(" order by l.createTime desc");
		return driverDao.getPageList(page, hql.toString());
	}

	
	@Override
	public boolean ifExist(String username) throws Exception{
		String sql = "select * from driver where user_name=?";
		List list = driverDao.getListSql(sql, username);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean deleteDriver(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return driverDao.delete(list);
	}

	@Override
	public Driver getByName(String driverName) throws Exception {
		String hql = " from Driver where driverName=?";
		List list = driverDao.getList(hql, new Object[]{driverName});
		if(list.size()>0){
			return (Driver)list.get(0);
		}
		else{
			return null;
		}
	}
	@Override
	public Driver getByUserName(String UserName) throws Exception {
		String hql = " from Driver where userName=?";
		List list = driverDao.getList(hql, new Object[]{UserName});
		if(list.size()>0){
			return (Driver)list.get(0);
		}
		else{
			return null;
		}
	}
	@Override
	public boolean ifBind(String driverId) throws Exception{
		String sql = "select * from User where driver_id in (?) and if_del = 0";
		List list = driverDao.getListSql(sql, driverId);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public List getUnbindList(Object[] keywords) {
		String sql = "select driver_id as driverId,driver_name as driverName from driver d where team_id="+keywords[0];
		return driverDao.getListSql(sql, null);
	}
	
	/**
	 * 获取司机重名列表
	 * add by zhangxin 2014-08-13
	 * @param loginName 
	 * @return
	 * @throws Exception
	 */
	@Override
	public List getExistNameList(String loginName) throws Exception{
		StringBuffer hql = new StringBuffer();
		hql.append(" from Driver l where  l.userName = '").append(loginName).append("'");
		
		return driverDao.getExistNameList(hql.toString());
	}
	
	/**
	 * 通过卡号来获取司机对象
	 * @param loginName 
	 * @return
	 * @throws Exception
	 */
	@Override
	public Driver getDriverByCardNo(String cardNo) throws Exception{
		StringBuffer hql = new StringBuffer();
		hql.append(" from Driver c where c.card.cardNo = ?");
		hql.append(" order by c.driverId desc");
		List driverList = driverDao.getList(hql.toString(), new Object[]{cardNo});
		if(driverList.size() > 0){
			return (Driver)driverList.get(0);
		}else{
			return null;
		}
		 
	}
	
	/**
	 * 通过登录名来获取司机对象
	 * @param loginName 
	 * @return
	 * @throws Exception
	 */
	@Override
	public Driver getDriverByValName(String valName) throws Exception{
		StringBuffer hql = new StringBuffer();
		hql.append(" from Driver l where  l.valName = ?");
		hql.append(" order by l.valName desc");
		List driverList = driverDao.getList(hql.toString(), new Object[]{valName});
		if(driverList.size() > 0){
			return (Driver)driverList.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 通过登录名和卡号来获取司机对象
	 * @param loginName 
	 * @return
	 * @throws Exception
	 */
	@Override
	public Driver getDriverByCondition(Object[] condition) throws Exception{
		StringBuffer hql = new StringBuffer();
		hql.append(" from Driver d where 1=1");
		if(condition[0] != null){
			hql.append(" and d.valName=?");
		}
		if(condition[1] != null){
			hql.append(" and d.cardNo=?");
		}
		hql.append(" order by d.valName desc");
		List driverList = driverDao.getList(hql.toString(), condition);
		if(driverList.size() > 0){
			return (Driver)driverList.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 通过登录名和卡号来修改司机对象
	 * @param loginName 
	 * @return
	 * @throws Exception
	 */
	@Override
	public void updateDriverByCondition(Object[] condition) throws Exception{
		StringBuffer hql = new StringBuffer();
		hql.append(" update Driver d set d.cardNo ='").append(condition[0]).append("'");
		hql.append(" where 1=1 ");
		if(condition[0] != null){
			hql.append(" and (d.cardNo is null or d.cardNo !='").append(condition[0]).append("')");
		}
		if(condition[1] != null){
			hql.append(" and d.valName ='").append(condition[1]).append("'");
		}
		driverDao.updateDriver(hql.toString());
	}

	@Override
	public Page getDriverLogPage(Page page, Object[] values) {
		StringBuffer hql = new StringBuffer("from SignInLog s where 1=1"); 
		if(values[0] != null && !"".equals(values[0])){
			hql.append(" and s.driver.driverId=").append(values[0]);
		}
		if(values[1]!= null && !"".equals(values[1])){
			hql.append(" and s.car.account.accountId=").append(values[1]);
		}
		if(values[1]!= null && !"".equals(values[1])){
			hql.append(" and s.driver.account.accountId=").append(values[1]);
		}
		if(values[2]!= null && !"".equals(values[2])){
			hql.append(" and s.car.team.teamId in (").append(values[2]).append(")");
		}
		hql.append(" order by s.signInTime desc ");
		return driverDao.getPageList(page, hql.toString());
	}
	
	@Override
	public List getCar(Object[] values){
		String  sql = "select * from user where user_id in (select user_id from sign_in_log where driver_id=? and unbind_time is null)";
		return driverDao.getListSql(sql, values[0]);
	}
	
	@Override
	public Page getAbnormaDriverLogPage(Page page ,Object[] values) {
		StringBuffer hql = new StringBuffer("from SignInLog s where s.ifLegal=0");
		if(values[0]!=null && !values[0].equals("")){
			hql.append(" and s.signInTime >= '").append(values[0]).append("'");
		}
		if(values[1]!=null && !values[1].equals("")){
			hql.append(" and s.signInTime <= '").append(values[1]).append("'");
		}
		if(values[2]!=null && !values[2].equals("")){
			hql.append(" and s.car.account.accountId=").append(values[2]);
		}
		if(values[3]!=null && !values[2].equals("")){
			hql.append(" and s.car.team.teamId in (").append(values[3]).append(")");
		}
		return driverDao.getPageList(page, hql.toString());
	}

	@Override
	public String getAbnormalDriverLogCount(Object[] values) {
		StringBuffer hql = new StringBuffer("from SignInLog s where s.ifLegal=0");
		if(values[0]!=null && !values[0].equals("")){
			hql.append(" and s.signInTime >= '").append(values[0]).append("'");
		}
		if(values[1]!=null && !values[1].equals("")){
			hql.append(" and s.signInTime <= '").append(values[1]).append("'");
		}
		if(values[2]!=null && !values[2].equals("")){
			hql.append(" and s.car.account.accountId=").append(values[2]);
		}
		if(values[3]!=null && !values[2].equals("")){
			hql.append(" and s.car.team.teamId in (").append(values[3]).append(")");
		}
		List list = driverDao.getList(hql.toString(),null);
		if(list.size()>0){
			return "{\"total\":"+list.size()+"}";
		}else{
			return "{\"total\":0}";
		}
	}
	@Override
	public List getAbnormaDriverLogList (Object[] values) {
		StringBuffer hql = new StringBuffer("from SignInLog s where s.ifLegal=0");
		if(values[0]!=null && !values[0].equals("")){
			hql.append(" and s.signInTime >= '").append(values[0]).append("'");
		}
		if(values[1]!=null && !values[1].equals("")){
			hql.append(" and s.signInTime <= '").append(values[1]).append("'");
		}
		if(values[2]!=null && !values[2].equals("")){
			hql.append(" and s.car.account.accountId=").append(values[2]);
		}
		if(values[3]!=null && !values[2].equals("")){
			hql.append(" and s.car.team.teamId in (").append(values[3]).append(")");
		}
		return driverDao.getList(hql.toString(),null);
	}
	
	@Override
	public List getList(Object[] values ){
		StringBuffer hql = new StringBuffer("" +
				" from Driver l where 1=1 ");
		if(values[0]!=null && !values[0].equals("")){
			hql.append(" and l.teamId in (").append(values[0]).append(")");
		}
		if(values[1]!=null && !values[1].equals("")){
			hql.append(" and l.driverName like '%").append(values[1]).append("%'");
		}
		if(values[2]!=null && !values[2].equals("")){
			hql.append(" and l.status =").append(values[2]);
		}
		if(values[3]!=null && !values[3].equals("")){
			hql.append(" and l.driverTel like '%").append(values[3]).append("%'");
		}
		hql.append(" order by l.createTime desc");
		return driverDao.getList(hql.toString(),null);
	}

	
	@Override
	public boolean unBindCar(int driverId){
		String sql = "update user set driver_id = null where driver_id = "+driverId;
		return driverDao.unBindDriver(sql);
	}
	
	@Override
	public List getDriverBId(int driverId){
		String sql = "select * from driver d left join user u on u.driver_id = d.driver_id where d.driver_id ="+driverId;
		return driverDao.getListSql(sql,null);
	}

	@Override
	public boolean resetDriver(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return driverDao.resetDriver(list);
	}
	
	@Override
	public List getListForReport(Object[] values) {
		String sql ="SELECT d.driver_id as id,d.driver_name as name ,SUM(ifnull(o.miles,0))/1000 as mile ,ROUND(SUM(IFNULL(o.cost,0)+IFNULL(o.glf,0)),2) as fee,SUM(TIME_TO_SEC(o.over_time)-TIME_TO_SEC(o.start_time))/3600 as time  from orders o LEFT JOIN driver d on o.driver_id=d.driver_id  where 1=1  and o.status=7 and d.status=1 ";
//		if(values[0]!=null && !values[0].equals("")){
//			sql+=" and u.plate like '%"+values[0]+"%'";
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			sql+=" and u.identifier like '%"+values[1]+"%'";
//		}
		if(values[0]!= null && !"".equals(values[0])){
			sql+=" and d.team_id in ("+values[0]+")";
		}
		if(values[1]!= null && !"".equals(values[1])){
			sql+=" and o.over_time >= '"+values[1]+"'";
		}
		if(values[2]!= null && !"".equals(values[2])){
			sql+=" and o.over_time <= '"+values[2]+"'";
		}
		sql+="group by d.driver_id,d.driver_name order by d.driver_id desc";
		return driverDao.getListSql(sql);
	}

	@Override
	public List getListMonthForReport(Object[] values) {
		String sql ="SELECT DATE_FORMAT(o.over_time,'%Y%m') as mon,d.driver_name as obj ,d.driver_id as obj_id,SUM(ifnull(o.miles,0))/1000 as mile ,ROUND(SUM(IFNULL(o.cost,0)+IFNULL(o.glf,0)),2) as fee,SUM(TIME_TO_SEC(o.over_time)-TIME_TO_SEC(o.start_time))/3600 as time  from orders o LEFT JOIN driver d on o.driver_id=d.driver_id  where 1=1  and o.status=7 and d.status=1 ";
		if(values[0]!= null && !"".equals(values[0])){
			sql+=" and d.driver_id ="+values[0];
		}
		if(values[1]!= null && !"".equals(values[1])){
			sql+=" and o.over_time >= '"+values[1]+"'";
		}
		if(values[2]!= null && !"".equals(values[2])){
			sql+=" and o.over_time <= '"+values[2]+"'";
		}
		sql+=" group by DATE_FORMAT(o.over_time,'%Y%m'),d.driver_name,d.driver_id order by DATE_FORMAT(o.over_time,'%Y%m') desc";
		return driverDao.getListSql(sql);
	}
	
	@Override
	public List getListMonthDetailForReport(Object[] values) {
		String sql ="SELECT d.driver_name as obj ,o.start_time,o.over_time,ifnull(o.miles,0)/1000 as mile ,ROUND(IFNULL(o.cost,0)+IFNULL(o.glf,0),2) as fee,(TIME_TO_SEC(o.over_time)-TIME_TO_SEC(o.start_time))/3600 as time  from orders o LEFT JOIN driver d on o.driver_id=d.driver_id  where 1=1  and o.status=7 and d.status=1 ";
		if(values[0]!= null && !"".equals(values[0])){
			sql+=" and d.driver_id ="+values[0];
		}
		if(values[1]!= null && !"".equals(values[1])){
			sql+=" and DATE_FORMAT(o.over_time,'%Y%m') ="+values[1];
		}
		if(values[2]!= null && !"".equals(values[2])){
			sql+=" and o.over_time >= '"+values[2]+"'";
		}
		if(values[3]!= null && !"".equals(values[3])){
			sql+=" and o.over_time <= '"+values[3]+"'";
		}
		sql+=" order by o.over_time desc";
		return driverDao.getListSql(sql);
	}
}
