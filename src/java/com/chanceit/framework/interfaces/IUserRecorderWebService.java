/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Sep 10, 2013
 * Id: IUserRecorderWebService.java,v 1.0 Sep 10, 2013 9:42:47 AM Administrator
 */
package com.chanceit.framework.interfaces;

/**
 * @ClassName IUserRecorderWebService
 * @author Administrator
 * @date Sep 10, 2013 9:42:47 AM
 * @Description TODO(这里用一句话描述这个类的作用)
 */
public interface IUserRecorderWebService {

	/**
	 * @author Administrator
	 * @date Sep 10, 2013
	 * @param identifier
	 * @param recdate
	 * @param mileDay
	 * @param mileNight
	 * @param driveTimeDay
	 * @param driveTimeNight
	 * @param highestSpeed
	 * @param speedL20
	 * @param speed20T40
	 * @param speed40T60
	 * @param speed60T80
	 * @param speed80T100
	 * @param speed100T120
	 * @param speedU120Day
	 * @param speedU120Night
	 * @return
	 * @Description TODO(保存行车日记的数据)
	 */
	public String addDailyRecorder(String identifier, String recdate,
			Integer mile, Integer driveTime,Integer acceleration,Integer brakes,
			Integer highestSpeed,Integer obdFuel,Integer obdMile,  
			Integer speedL20, Integer speed20T50, Integer speed50T80,
			Integer speed80T120, Integer speedU120,Integer mileDay,Integer driveTimeDay);

	/**
	 * @author gwt
	 * @date Sep 10, 2013
	 * @param identifier
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @Description 查询行车记录
	 */
	public String getRecorder(String identifier, String fromDate, String toDate);

	/**
	 * @author gwt
	 * @date Sep 11, 2013
	 * @param identifier
	 * @param month
	 * @return
	 * @Description 根据月份查记录
	 */
	public String getDataByMonth(String identifier, String month);
	
	public String getData(String identifier, String fromDate, String toDate);
}
