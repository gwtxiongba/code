package com.chanceit.http.dao;

import java.util.List;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Member;


public interface IMemberDao {

	public String save(Member member);

	public boolean deleteMember(List ids);

	public void delete(Member member);

	public void update(Member member);

	public Member get(int memberId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public List getMapList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);

	public List getListSql(String sql, Object... values);
	public boolean fakeDelete(List ids,int accountId);

	public void saveMember(Member member);
	
	public boolean resetMember(List ids);
}