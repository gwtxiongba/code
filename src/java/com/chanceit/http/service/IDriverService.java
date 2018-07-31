package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Driver;

public interface IDriverService {
	public boolean update(Driver Driver);

	public Driver get(int driverId);
	public Page getPageList(Page page,Object[] values );
	public boolean ifExist(String driverName) throws Exception;
	public Driver getByName(String driverName) throws Exception;
	public String save(Driver driver);
	public boolean saveDriver(Driver driver);
	public Driver getDriver(int driverId);
	public boolean deleteDriver(String ids) ;
	/**
	 * @param driverId
	 * @return
	 * @throws Exception
	 * @des driverId是否有未解绑的车辆
	 */
	public boolean ifBind(String driverId) throws Exception;

	public List getUnbindList(Object[] keywords);
	public Driver getByUserName(String UserName) throws Exception ;
	public List getExistNameList(String loginName) throws Exception;
	public Driver getDriverByValName(String valName) throws Exception;
	public Driver getDriverByCardNo(String cardNo) throws Exception;
	public Driver getDriverByCondition(Object[] condition) throws Exception;
	public void updateDriverByCondition(Object[] condition) throws Exception;
	
	public Page getDriverLogPage(Page page,Object[] values);
	public List getCar(Object[] values);
	public Page getAbnormaDriverLogPage(Page page ,Object[] values);
	public String getAbnormalDriverLogCount(Object[] values);
	public List getAbnormaDriverLogList (Object[] values) ;
	public List getList(Object[] values );
	public boolean unBindCar(int driverId);
	public List getDriverBId(int driverId);
	public boolean resetDriver(String ids);
	
	public List getListForReport(Object[] values);
	public List getListMonthForReport(Object[] values);
	public List getListMonthDetailForReport(Object[] values);
}