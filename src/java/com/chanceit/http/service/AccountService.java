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
				
				if(account.getParentId()==0){ //公司账户
					account.setAccountId(Integer.parseInt(id));
					//shopInfoWebService.saveCompanyInfo(account);
				}
				//}//
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
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Account> getList(String companyId) {
//		HttpSession session = ServletActionContext.getRequest().getSession();
//		Accounts account = (Accounts)session.getAttribute("account");
		
		String hql = "from Account l where l.company.companyId = ?";
		Object [] values = new Object[]{Integer.parseInt("1")};  
		return accountDao.getList(hql,values);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Account> getListTe(int teamId) {
//		HttpSession session = ServletActionContext.getRequest().getSession();
//		Accounts account = (Accounts)session.getAttribute("account");
		
		String hql = "from Account l where l.team.teamId = ?";
		Object [] values = new Object[]{teamId};  
		return accountDao.getList(hql,values);
	}
	/**
	 * 查询操作员列表
	 */
	@Override
	public Page getPageList(Page page,Object[] values ){
//		StringBuffer hql = new StringBuffer("select accountId as accountId,accountName as account,accountRealName as accountRealName,companyName as realname,accountTel as phone,address as address,email as email,createTime as createTime " +
//			" ,visitTime as visitTime, logintimes as logintimes,team.teamName as teamName,team.teamId as teamId,dept.deptId as deptId,dept.deptName as name,level.levelId as levelId, level.levelName as levelName"+	
//			" from Account l where 1=1 and accountName != 'admin'");
//		if(StringUtils.isNotBlank(values[0].toString())){
//			hql.append(" and l.parentId=").append(values[0]);
//		}
		String sql = "select l.account_id as accountId,l.account_name as account,l.account_real_name as accountRealName,l.company_name as realname,l.account_tel as phone,l.address as address,l.email as email,l.create_time as createTime " +
			" ,l.visit_time as visitTime, l.logintimes as logintimes,team.team_name as teamName,team.team_id as teamId,dept.id as deptId,dept.name as deptName,level.level_id as levelId, level.level_name as levelName"+	
			" from account l left join team as team on team.team_id = l.team_id left join dept as dept on dept.id = l.dept_id left join level as level on l.level_id = level.level_id where 1=1 and l.account_name != 'admin'";
		
		if(StringUtils.isNotBlank(values[1].toString())){
			sql += " and team.team_id in("+values[1]+")";
		}
		if(values[2]!=null && !values[2].equals("")){
			sql += " and l.company_Name like '%"+values[2]+"%'";//根据 姓名 来搜索
		}
		if (values[3] != null && !"".equals(values[3])) {
			if(values[3].equals("teamName")){
				sql +=" order by convert(team.team_name using gbk)  asc";
			}else if(values[3].equals("account")){
				sql +=" order by convert(l.account_name using gbk)  asc";
			}else if(values[3].equals("realname")){
				sql +=" order by convert(l.account_real_name using gbk)  asc";
			}else if(values[3].equals("levelName")){
				sql +=" order by convert(level.level_name using gbk)  asc";
			}else if(values[3].equals("deptName")){
				sql +=" order by convert(dept.name using gbk)  asc";
			}else if(values[3].equals("phone")){
				sql +=" order by l.account_tel  asc";
			}else if(values[3].equals("logintimes")){
				sql +=" order by l.logintimes  asc";
			}else if(values[3].equals("visitTime")){
				sql +=" order by l.visit_time  asc";
			}
		}
		//hql.append(" order by l.createTime desc");
		return accountDao.getPageList2(page, sql);
	}
	
	@Override
	public Page getPageListCompany(Page page){
		StringBuffer hql = new StringBuffer("select accountId as accountId,accountName as account,companyName as name,accountTel as phone,address as address,email as email,createTime as createTime " +
				"from Account l where parentId=0 ");
		hql.append(" order by l.createTime desc");
		return accountDao.getPageList2(page, hql.toString());
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
	
	@Override
	public boolean ifExistEmail(String email,int accountId){
		String sql = "select * from account where email=?";
		if(accountId!=0){
			sql = sql+" and account_id !=" + accountId;
		}
		List list = accountDao.getListSql(sql, email);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}
	@Override
	public Account getByRandomCode(String randomCode)throws Exception{
    	String hql = " from Account where randomCode=?";
		List list = accountDao.getList(hql, new Object[]{randomCode});
		if(list.size()>0){
			return (Account)list.get(0);
		}
		else{
			return null;
		}
    }

	@Override
	public Account getByEmail(String email) throws Exception {
		String hql = " from Account where email=?";
		List list = accountDao.getList(hql, new Object[]{email});
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
