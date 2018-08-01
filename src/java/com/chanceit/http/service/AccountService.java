package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IAccountDao;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.PasswordLog;

@Transactional
@Component("accountService")
public class AccountService implements IAccountService {
	@Autowired
	@Qualifier("accountDao")
	private IAccountDao accountDao;
	
	
	@Override
	public boolean save(Account account) {
		try{
			
			if(account.getAccountId()!=null && account.getAccountId()>0){//edit
				accountDao.saveOrUpdate(account);
			}else{
				//if(!"admin".equals(account.getAccountName())){
				String id = accountDao.save(account);
				
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean delete(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return accountDao.delete(list);
	}

	@Override
	public boolean update(Account account) {
		accountDao.update(account);
		return true;
	}
	

	@Override
	public Account get(int accountId) {
		return accountDao.get(accountId);
	}
	
	
	
	/**
	 * 查询操作员列表
	 */
	@Override
	public Page getPageList(Page page,Object[] values ){
		StringBuffer hql = new StringBuffer("from Account where 1=1");
//		if(StringUtils.isNotBlank(values[0].toString())){
//			hql.append(" and l.parentId=").append(values[0]);
//		}
		
		hql.append(" order by createTime desc");
		return accountDao.getPageList(page, hql.toString(),values);
	}
	
	
	
	@Override
	public boolean save(short accountType, String accountInfo) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean ifExist(String accountName) throws Exception{
		String sql = "select * from account where account_name=?";
		List list = accountDao.getListSql(sql, accountName);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public Account getByName(String accountName) throws Exception{
		String hql = " from Account where accountName=?";
		List list = accountDao.getList(hql, new Object[]{accountName});
		if(list.size()>0){
			return (Account)list.get(0);
		}
		else{
			return null;
		}
	}	
	
	
	

	

	/**
	 * 存储用户账号密码供内部使用
	 * add by zhangxin 2014-10-11
	 * @param passwordLog
	 */
	@Override
	public void saveOrUpdatePswd(PasswordLog passwordLog){
		accountDao.saveOrUpdatePswd(passwordLog);
	}
	
	/**
	 * 修改存储密码
	 * add by zhangxin 2014-10-11
	 */
	@Override
	public void updatePswd(Object[] value){
		List<PasswordLog> list = accountDao.getPswdLog((Integer)value[1]);
		//如果已经存储了密码就直接修改,没有则新增
		if(list.size() > 0){
			accountDao.updatePswd(value);
		}else{
			PasswordLog log = new PasswordLog();
			log.setAccountId((Integer)value[1]);
			log.setPassword((String)value[0]);
			accountDao.saveOrUpdatePswd(log);
		}
	}

	@Override
	public boolean resetAccount(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return accountDao.resetAccount(list);
	}
}
