package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.pojo.User;


public interface IUserDao {

	public void save(User user);

	public boolean delete(List ids,int accountId);

	public void delete(User user);

	public void update(User user);

	public User get(int userId);

	@SuppressWarnings("unchecked")
	public List getList(String hql,Object... objs) ;

	@SuppressWarnings("unchecked")
	public List getMapList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);
	public Page getPageList_x(Page page , String sql ,Object... values);
	public List getListSql(String sql, Object... values);
	public boolean fakeDelete(List ids,int accountId);

	public void saveTeam(Team team);
	public boolean bindTeam(List ids,int accountId,int teamId);
	public boolean unBindTeam(List ids,int accountId,int teamId);
	/**
	 * @des �����༭�����Ƿ���
	 * @param ifMonitor
	 * @param ids
	 * @param accountId
	 * @return
	 */
	public boolean editIfMonitor(int ifMonitor,List ids,int accountId);

	/**
	 * @des idsΪ�յ�ʱ��������
	 * @param accountId
	 * @param teamId
	 * @return
	 */
	public boolean unBindTeamAll(int accountId, int teamId);
	
	public User getUserById(int vehicleId);
	
	/**
	 * ��ȡ�����豸ʶ����
	 * @return
	 */
	public List getAllIdentifier();
	
	/**
	 * ��ȡ����Ȩ�޵ĳ����б�
	 * @param sql
	 * @return
	 */
	public List getListForOp(String sql);
	
	public Long countSQL(String hql ,Object... objs);
	
	public Object findObject(String hql ,Object... objs );
	
	public void excuteUpdateSql(String sql);
	
	public boolean deleteByIdentifier(String ids,int accountId);
}