package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.PasswordLog;


public interface IAccountDao {

	public String save(Account account);
	public void saveOrUpdate(Account account);
	public boolean delete(List ids);

	public void delete(Account account);

	public void update(Account account);

	public Account get(int accountId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public List getMapList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);
	
	@SuppressWarnings("unchecked")
	public Page getPageList2(Page page, String hql, Object... values);
	
	public List getListSql(String sql, Object... values);
	
	/**
	 * �洢�û��˺����빩�ڲ�ʹ��
	 * add by zhangxin 2014-10-11
	 * @param passwordLog
	 */
	public void saveOrUpdatePswd(PasswordLog passwordLog);
	
	/**
	 * �޸��û�����
	 * @param value
	 */
	public void updatePswd(Object[] value);
	
	/**
	 * ��ȡ������Ϣ
	 * add by zhangxin 2014-10-13
	 * @param value
	 * @return
	 */
	public List<PasswordLog> getPswdLog(int accountId);
	
	public boolean resetAccount(List ids);

}