package com.chanceit.http.dao;

import java.util.List;
import java.util.Map;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Weibao;
import com.chanceit.http.pojo.User;

public interface IWeibaoDao {

	public String save(Weibao weibao);
	
	public void saveWeibao(Weibao weibao) ;

	public boolean delete(int id);

	public void delete(Weibao weibao);

	public void update(Weibao weibao);

	public Weibao get(int weibaoId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);
	
	public List getListSql(String sql ,Object... values) ;
	
}