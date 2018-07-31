package com.chanceit.http.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Member;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("memberDao")
@Repository
public class MemberDaoImpl extends HibernateService implements IMemberDao {
	
	@Override
	public String save(Member member) {
		Serializable skey = getSession().save(member);
		return skey.toString();
	}
	@Override
	public void saveMember(Member member) {
		getSession().saveOrUpdate(member);
	}
	@Override
	public boolean deleteMember(List ids) {
		String hql = "delete from Member u where u.id in (:ids)";
		Query query = super.getSession().createQuery(hql).setParameterList("ids", ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public Member get(int id) {
		return (Member) getSession().get(Member.class, id);
	}
	
	@Override
	public boolean fakeDelete(List ids,int accountId) {
		String hql = "update Member u set delTime=:sj,ifDel=1 where u.memberId in (:ids) and u.account.accountId=:aid";
		Query query = super.getSession().createQuery(hql).setParameter("sj", new Date()).setParameterList("ids",ids).setParameter("aid", accountId);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	@Override
	public void delete(Member member) {
		getSession().delete(member);
	}

	@Override
	public void update(Member member) {
		getSession().clear();
		getSession().update(member);
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
	public Page getPageList(Page page , String sql ,Object... values) {
		return super.findSQLMapPage(page, sql, values);
	}
	
	
	@Override
	public List getListSql(String sql ,Object... values) {
		return super.findSQL(sql, values);
	}
	@Override
	public boolean resetMember(List ids) {
		String hql="update Member set pwd='123456' where id in (:ids)";
		Query query=super.getSession().createQuery(hql).setParameterList("ids", ids);
		int rs=query.executeUpdate();
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
}
