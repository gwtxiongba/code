package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.PasswordLog;

public interface IAccountService {

	/**
	 * @author dj
	 * @date Jun 21, 2013
	 * @param accountType
	 * @param accountInfo
	 * @return
	 * @Description save account
	 */
	public boolean save(short accountType, String accountInfo);

	public boolean save(Account account);

	public boolean delete(String ids);

	public boolean update(Account account);

	public Account get(int accountId);
	public List<Account> getListTe(int teamId) ;
	@SuppressWarnings("unchecked")
	public List<Account> getList(String companyId);
	public Page getPageList(Page page,Object[] values );
	public boolean ifExist(String accountName) throws Exception;
	public Account getByName(String accountName) throws Exception;
	public Account getByRandomCode(String randomCode)throws Exception;
	public Account getByEmail(String email)throws Exception;
	public boolean ifExistEmail(String email,int accountId);
	public Page getPageListCompany(Page page);
	
	/**
	 * 存储用户账号密码供内部使用
	 * add by zhangxin 2014-10-11
	 * @param passwordLog
	 */
	public void saveOrUpdatePswd(PasswordLog passwordLog);
	
	/**
	 * 修改密码
	 * add by zhangxin 2014-10-11
	 */
	public void updatePswd(Object[] value);
	
	public boolean resetAccount(String ids);

}