package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Warn;

public interface IWarnService {

	public boolean save(Warn warns);

	public boolean delete(String ids);

	public boolean update(Warn warn);

	public Warn get(int warnId);

	@SuppressWarnings("unchecked")
	public Page getDetaiPagelList(Page page, Object[] values) throws Exception;

	public Page getPageList(Page page, Object[] values);
	
	public List getWarn(String warnId)  throws Exception;

	/**
	 * 
	 * @author dj
	 * @date Sep 5, 2013
	 * @param page
	 * @param companyId
	 * @return
	 * @throws Exception
	 * @Description 手机端使用，获得当天要到店的客户预约列表
	 */
	public Page getTodayList(Page page,String companyId) throws Exception;
	public String getAlarmCount(Object[] values);
	public List getCountGroupWarn(Object[] values);
	public List getListWarn(Object[] values) throws Exception;
	public List getCountGroupWarn2App(Object[] values);
	

}