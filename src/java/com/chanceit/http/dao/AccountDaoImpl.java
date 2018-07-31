package com.chanceit.http.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.Coder;
import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.PasswordLog;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("accountDao")
@Repository
public class AccountDaoImpl extends HibernateService implements IAccountDao {
	
	@Override
	public String save(Account account) {
		Serializable skey = super.getSession().save(account);
		return skey.toString();
		
	}
	
	@Override
	public void saveOrUpdate(Account account){
		getSession().saveOrUpdate(account);
	}
	
	@Override
	public boolean delete(List ids) {
		String hql = "delete from Account where accountId in (:ids)";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void delete(Account account) {
		getSession().delete(account);
	}

	@Override
	public void update(Account account) {
		getSession().clear();
		getSession().update(account);
	}

	@Override
	public Account get(int accountId) {
		return (Account) getSession().get(Account.class, accountId);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List getList(String hql,Object[] objs) {
		return super.find(hql,objs);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List getMapList(String hql,Object... objs) {
		Query  query = super.getQuery(hql, objs);
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Page getPageList(Page page , String hql ,Object... values) {
		return super.findListPage(page, hql,values);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Page getPageList2(Page page , String hql ,Object... values) {
		//return super.findListPage(page, hql,values);
		return super.findSQLMapPage(page, hql, values);
	}
	
	@Override
	public List getListSql(String sql ,Object... values) {
		return super.findSQL(sql, values);
	}
	
	/**
	 * 存储用户账号密码供内部使用
	 * add by zhangxin 2014-10-11
	 * @param passwordLog
	 */
	@Override
	public void saveOrUpdatePswd(PasswordLog passwordLog){
		getSession().saveOrUpdate(passwordLog);
	}
	
	/**
	 * 修改用户密码
	 * @param value
	 */
	@Override
	public void updatePswd(Object[] value){
		String hql = "update PasswordLog p set p.password=:pwd where p.accountId =:accountId";
		Query query = super.getSession().createQuery(hql).setParameter("pwd", value[0]).setParameter("accountId", value[1]);
		query.executeUpdate();
	}

	/**
	 * 获取密码信息
	 * add by zhangxin 2014-10-13
	 * @param value
	 * @return
	 */
	@Override
	public List<PasswordLog> getPswdLog(int accountId) {
		String hql = "from PasswordLog p where p.accountId = ?";
		return find(hql, new Object[]{accountId});
	}

	@Override
	public boolean resetAccount(List ids) {
		String hql="update Account set accountPwd= :pwd where accountId in (:ids)";
		Query query;
		int rs=0;
		try {
			query = super.getSession().createQuery(hql).setParameter("pwd", Coder.encryptMD5("123456")).setParameterList("ids", ids);
			rs=query.executeUpdate();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
}
