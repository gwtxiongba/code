package com.chanceit.http.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.http.dao.ISignInLogDao;
import com.chanceit.http.pojo.SignInLog;

@Transactional
@Component("signInLogService")
public class SignInLogService implements ISignInLogService {
	@Autowired
	@Qualifier("signInLogDao")
	private ISignInLogDao signInLogDao;
	
	/**
	 * 保存打卡信息
	 */
	@Override
	public void save(SignInLog log) {
		signInLogDao.save(log);
	}

	@Override
	public SignInLog getById(int signInlogId) {
		return signInLogDao.getById(signInlogId);
	}

	@Override
	public List getSignInLog(Object[] values) {
		String hql = "from SignInLog s where s.driver.driverId=? and s.signInTime <=? and (s.unbindTime>=? or s.unbindTime is NULL)";
		return signInLogDao.getByDriverId(hql,values[0],values[2],values[1]);
	}

	/**
	 * 通过车的ID获取最近一条日志信息
	 */
	@Override
	public SignInLog getLastDriverByCarId(int carId) {
		String hql = "from SignInLog s where s.car.vehicleId=? order by s.signInTime desc";
		return signInLogDao.getSignLong(hql, new Object[]{carId});
	}
	
	@Override
	public List<SignInLog> getSignInLogList(int userid,Date startTime,Date endTime){
		String hql = "from SignInLog s where s.car.vehicleId=? and (((s.signInTime >=? and s.signInTime <=?) or ((s.unbindTime>=? and s.unbindTime<=?))) or (s.unbindTime is NULL and s.signInTime <=?))";
		return signInLogDao.getSignLongList(hql, userid,startTime,endTime,startTime,endTime,endTime);
	}
	
	@Override
	public SignInLog getSignInLog(int userid,Date time){
		String hql = "from SignInLog s where s.car.vehicleId=? and ((s.signInTime <=? and s.unbindTime >=?) or (s.unbindTime is NULL and s.signInTime <=?))";
		return signInLogDao.getSignLong(hql, new Object[]{userid,time,time,time});
	}
}
