package com.chanceit.http.dao;

import java.util.List;
import java.util.Map;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Weizhang;
import com.chanceit.http.pojo.User;

public interface IWeizhangDao {

	public String save(Weizhang weizhang);
	
	public void saveWeizhang(Weizhang weizhang) ;

	public boolean delete(int id);

	public void delete(Weizhang weizhang);

	public void update(Weizhang weizhang);

	public Weizhang get(int weizhangId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);
	
	public List getListSql(String sql ,Object... values) ;
	
}