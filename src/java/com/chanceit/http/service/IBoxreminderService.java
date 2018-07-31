package com.chanceit.http.service;

import java.util.Collection;
import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Boxreminder;

public interface IBoxreminderService {

	public boolean save(Boxreminder hits);

	public boolean delete(String ids);

	public boolean update(Boxreminder hit);

	public Boxreminder get(int hitId);

	/**
	 * @author dj
	 * @date Nov 28, 2013
	 * @param companyId
	 * @return
	 * @throws Exception
	 * @Description ��������б���Ϊ�����ѡ�
	 */
	@SuppressWarnings("unchecked")
	public Page getDetaiPagelList(Page page, Object[] values) throws Exception;

	/**
	 * @author dj
	 * @date Nov 28, 2013
	 * @param page
	 * @param values
	 * @return
	 * @Description ��ײ�б�δ����ֻ��ʾ3���ڵ�
	 */
	public Page getPageList(Page page, Object[] values);
	
	public boolean reply(Object[] objAry) throws Exception;

	public List getBoxreminder(String hitId)  throws Exception;

	/**
	 * 
	 * @author dj
	 * @date Sep 5, 2013
	 * @param page
	 * @param companyId
	 * @return
	 * @throws Exception
	 * @Description �ֻ���ʹ��
	 */
	public Page getTodayList(Page page,String companyId) throws Exception;
	
	/**
	 * @author dj
	 * @date Nov 28, 2013
	 * @param hitId
	 * @param remark
	 * @return
	 * @throws Exception
	 * @Description ����ע��ײ��Ϣ
	 */
	
	public boolean remark(String hitId,String remark)  throws Exception;
	/**
	 * @des ��ȡ������������
	 * @param values
	 * @return
	 */
	public String getAlarmCount(Object[] values);

	/**
	 * add by zhangxin 2014-07-14
	 * @des ��ȡ����ָ���б�
	 * @param values
	 * @return
	 */
	public List getCmdList(Account account, Object[] values);
	/**
	 * @des ��ȡ��������������б�
	 * @param keywords
	 * @return
	 */
	public Collection getDateEvent(Object[] keywords);
	public List getWarningForApp(Object[] values);
	public List getCountGroupWarn(Object[] values);
	public List getCountGroupWarn2App(Object[] values);
	public List getCountGroupWarn2App2(Object[] values);
	
	public boolean setWarnedForApp(String warnId,String reader) throws Exception;
}