package com.chanceit.http.dao;

import java.util.List;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Baoxiao;


public interface IBaoxiaoDao {

	public String save(Baoxiao baoxiao);

	public boolean delete(List ids,int accountId);

	public void delete(Baoxiao baoxiao);

	public void update(Baoxiao baoxiao);

	public Baoxiao get(int baoxiaoId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public List getMapList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);

	public List getListSql(String sql, Object... values);

	public void saveBaoxiao(Baoxiao baoxiao);
	public void updateBaoxiao(String hql);
}