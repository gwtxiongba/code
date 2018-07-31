package com.chanceit.http.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.http.pojo.SignInLog;

/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("signInLogDao")
@Repository
public class SignInLogDaoImpl extends HibernateService implements ISignInLogDao {
	
	@Override
	public void save(SignInLog log) {
		getSession().saveOrUpdate(log);
	}

	@Override
	public SignInLog getById(int signInlogId) {
		return (SignInLog) getSession().get(SignInLog.class, signInlogId);
	}

	@Override
	public List getByDriverId(String hql, Object... objs) {
		return super.find(hql, objs);
	}

	@Override
	public SignInLog getSignLong(String hql, Object... objs) {
		List list = find(hql, objs);
		if(list.size() > 0){
			return (SignInLog) list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List<SignInLog> getSignLongList(String hql,Object... objs){
		return find(hql,objs);
	}
}
