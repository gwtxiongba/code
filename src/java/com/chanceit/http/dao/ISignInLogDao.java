package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.http.pojo.SignInLog;


public interface ISignInLogDao {
	public void save(SignInLog log);
	
	public SignInLog getById(int signInlogId);
	
	public List getByDriverId(String hql,Object... objs);
	
	public SignInLog getSignLong(String hql, Object... objs);
	
	public List<SignInLog> getSignLongList(String hql,Object... objs);
}