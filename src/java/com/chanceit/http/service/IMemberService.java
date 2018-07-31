package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Member;

public interface IMemberService {
	public boolean update(Member Member);

	public Member get(int memberId);
	public Page getPageList(Page page,Object[] values,int levelId );
	public boolean ifExist(String memberName) throws Exception;
	public Member getByUserName(String memberName) throws Exception;
	public String save(Member member);
	public boolean saveMember(Member member);
	public Member getMember(int memberId);
	public boolean deleteMember(String ids) ;
	public List getListSql(int levelId,Object[] values );
	public boolean resetMember(String ids);
	/**
	 * @param memberId
	 * @return
	 * @throws Exception
	 * @des memberId是否有未解绑的车辆
	 */
	public boolean ifBind(String memberId) throws Exception;

	public List getUnbindList(Object[] keywords);

	public List getList(int levelId,Object[] values );
	public List getMemberBId(int memberId);
	
	public List getListForReport(Object[] values);
	public List getListMonthForReport(Object[] values);
	public List getListMonthDetailForReport(Object[] values);
}