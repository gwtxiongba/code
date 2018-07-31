package com.chanceit.http.dao;

import java.util.List;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Weixiu;


public interface IWeixiuDao {

	public String save(Weixiu weixiu);

	public boolean delete(List ids);

	public void delete(Weixiu weixiu);

	public void update(Weixiu weixiu);

	public Weixiu get(int weixiuId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public List getMapList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);

	public List getListSql(String sql, Object... values);

	public void saveWeixiu(Weixiu weixiu);
	public void updateWeixiu(String hql);
}