package com.chanceit.http.dao;

import java.util.List;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Askleave;


public interface IAskleaveDao {

	public String save(Askleave askleave);

	public boolean delete(List ids,int accountId);

	public void delete(Askleave askleave);

	public void update(Askleave askleave);

	public Askleave get(int askleaveId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public List getMapList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);

	public List getListSql(String sql, Object... values);

	public void saveAskleave(Askleave askleave);
	public void updateAskleave(String hql);
}