/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jul 11, 2013
 * Id: IShopInfoWebService.java,v 1.0 Jul 11, 2013 3:51:57 PM Administrator
 */
package com.chanceit.framework.interfaces;

/**
 * @ClassName IShopInfoWebService
 * @author Administrator
 * @date Jul 11, 2013 3:51:57 PM
 * @Description 4S店服务webservice
 */
public interface IShopInfoWebService {
	
	
	/**
	 * @author Administrator
	 * @date Jul 11, 2013
	 * @param shopId            4S店ID
	 * @param shopName          4S店名称
	 * @param shopNickName    	4S店昵称
	 * @param shopDesc    		4S店简介
	 * @param address    		4S店地址
	 * @param shopCoordinate    4S店位置
	 * @param rescueTel         救援电话
	 * @param qualityTel        精品服务电话
	 * @param workTel           预约电话
	 * @param adviceTel         咨询电话
	 * @param ccmplaintTel      投诉电话
	 * @param saleTel      		二手车电话
	 * @return 结果
	 * @Description 保存/更新4S店信息
	 */
	public String saveShopInfo(String shopId ,String shopName,String shopNickName,String shopDesc,
			String address,String shopCoordinate,String tel,
			String rescueTel,String qualityTel,String workTel,String adviceTel,String ccmplaintTel , String saleTel);

}
