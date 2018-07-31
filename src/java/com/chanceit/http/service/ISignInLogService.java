package com.chanceit.http.service;

import java.util.Date;
import java.util.List;

import com.chanceit.http.pojo.SignInLog;

public interface ISignInLogService {
	public void save(SignInLog log);
	public SignInLog getById(int signInlogId);
	public List getSignInLog(Object[] values);
	public SignInLog getLastDriverByCarId(int carId);
	
	public List<SignInLog> getSignInLogList(int userid,Date startTime,Date endTime);
	public SignInLog getSignInLog(int userid,Date time);
}