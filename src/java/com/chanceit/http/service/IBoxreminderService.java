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
	 * @Description 获得提醒列表并置为已提醒。
	 */
	@SuppressWarnings("unchecked")
	public Page getDetaiPagelList(Page page, Object[] values) throws Exception;

	/**
	 * @author dj
	 * @date Nov 28, 2013
	 * @param page
	 * @param values
	 * @return
	 * @Description 碰撞列表，未处理只显示3天内的
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
	 * @Description 手机端使用
	 */
	public Page getTodayList(Page page,String companyId) throws Exception;
	
	/**
	 * @author dj
	 * @date Nov 28, 2013
	 * @param hitId
	 * @param remark
	 * @return
	 * @throws Exception
	 * @Description 处理备注碰撞信息
	 */
	
	public boolean remark(String hitId,String remark)  throws Exception;
	/**
	 * @des 获取报警车辆总数
	 * @param values
	 * @return
	 */
	public String getAlarmCount(Object[] values);

	/**
	 * add by zhangxin 2014-07-14
	 * @des 获取报警指令列表
	 * @param values
	 * @return
	 */
	public List getCmdList(Account account, Object[] values);
	/**
	 * @des 获取车辆当天的提醒列表
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