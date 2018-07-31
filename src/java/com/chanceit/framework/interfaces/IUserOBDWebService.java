/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jul 10, 2013
 * Id: IUserOBDWebService.java,v 1.0 Jul 10, 2013 2:17:03 PM Administrator
 */
package com.chanceit.framework.interfaces;

/**
 * @ClassName IUserOBDWebService
 * @author Administrator
 * @date Jul 10, 2013 2:17:03 PM
 * @Description 导航盒子webservice
 */
public interface IUserOBDWebService {
	
	/**
	 * @author Administrator
	 * @date Jul 11, 2013
	 * @param identifier  盒子ID
	 * @param mileage  行驶里程
	 * @param recTime  记录时间
	 * @param maintainMileage 保养里程
	 * @param maintainTime  保养时间
	 * @return
	 * @Description 修正导航盒子的里程
	 */
	public String correctMileage(String identifier ,int mileage ,String recTime ,String maintainMileage , String maintainTime);
	
	/**
	 * @author Administrator
	 * @date Jul 11, 2013
	 * @param shopId
	 * @param identifier
	 * @return
	 * @Description 保存4S店与用户关系表
	 */
	public String addShopUser(String shopId ,String identifier ,String tel,String displacement);
	
	/**
	 * @author Administrator
	 * @date Jul 17, 2013
	 * @param identifiers
	 * @param synTime
	 * @return
	 * @Description 下载4S店里程信息
	 */
	public String downLoadMileage(String identifiers ,String synTime);
	
	/**
	 * @author Administrator
	 * @date Oct 23, 2013
	 * @param shopId
	 * @param pageNo
	 * @param synTime
	 * @return
	 * @Description 下载用户里程信息
	 */
	public String downLoadMileagePage(String shopId ,int pageNo, String synTime);
	
	/**
	 * @author Administrator
	 * @date Jul 26, 2013
	 * @param identifier
	 * @return
	 * @Description 获得用户位置信息
	 */
	public String checkPoint(String identifier);
	
	/**
	 * @author Administrator
	 * @date Jul 31, 2013
	 * @param shopId
	 * @return
	 * @Description 获得4S店在店用户资料
	 */
	public String checkInShopUser(String shopId);
	
	/**
	 * @author Administrator
	 * @date Sep 12, 2013
	 * @param identifier
	 * @param mileage 
	 * @return
	 * @Description 上传里程
	 */
	public String updateMileage(String identifier, String mileage);
	
	/**
	 * @author Administrator
	 * @date Sep 13, 2013
	 * @param identifier
	 * @param faultID 错误ID
	 * @return
	 * @Description 更新错误码
	 */
	public String updateFault(String identifier, String faultID);
	
	/**
	 * @author dj
	 * @date Sep 17, 2013
	 * @param identifier
	 * @param paras
	 * @param status
	 * @return
	 * @Description 上报预警提醒
	 */
	public String sendWarnInfo(String identifier,String paras,int status);
	
	/**
	 * @author dj
	 * @date Nov 25, 2013
	 * @param identifier
	 * @param x
	 * @param y
	 * @return
	 * @Description 上报碰撞信息
	 */
	public String sendHitInfo(String identifier, String x, String y);
	
	/**
	 * @author Administrator
	 * @date Nov 22, 2013
	 * @param groupId
	 * @param recorderTime
	 * @return
	 * @Description 下载断电记录
	 */
	public String downloadDispower(String groupId,String recorderTime);

}
