/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Sep 10, 2013
 * Id: ICompetitionWebService.java,v 1.0 Sep 10, 2013 3:01:32 PM Administrator
 */
package com.chanceit.framework.interfaces;

/**
 * @ClassName ICompetitionWebService
 * @author Administrator
 * @date Sep 10, 2013 3:01:32 PM
 * @Description 竞争4S店webservice
 */
public interface ICompetitionWebService {
	
	/**
	 * @author Administrator
	 * @date Sep 10, 2013
	 * @param competeId   竞争店ID（可选：如果有则表示是更新1）
	 * @param shopId 机构ID
	 * @param competeName 竞争店名称
	 * @param competeAddress 竞争店地址
	 * @param competePointX 竞争店坐标X
	 * @param competePointY 竞争店坐标Y
	 * @return
	 * @Description 保存/更新竞争4S店信息
	 */
	public String save(Integer competeId ,String shopId ,String competeName , String competeAddress,String competePointX,String competePointY );
	
	/**
	 * @author Administrator
	 * @date Sep 10, 2013
	 * @param competeIds 竞争店ID列表
	 * @return
	 * @Description 删除竞争店信息
	 */
	public String delete(String competeIds);
	
	/**
	 * @author Administrator
	 * @date Sep 11, 2013
	 * @param shopId 集团机构ID
	 * @param recorderTime 下载时间
	 * @return
	 * @Description 以集团为单位下载指定时间到最新时间的竞争店进入记录
	 */
	public String downloadCompetitionRecorder(String shopId ,String recorderTime);
	
	/**
	 * @author Administrator
	 * @date Sep 17, 2013
	 * @param identifier
	 * @param competeId
	 * @param recorderDate
	 * @param desc
	 * @return
	 * @Description 添加竞争信息记录
	 */
	public String addStatistics(String identifier, int competeId,
			String recorderDate, String desc);
	
	
	/**
	 * @author Administrator
	 * @date Sep 17, 2013
	 * @param identifier 用户ID
	 * @return
	 * @Description 下载竞争店列表
	 */
	public String downloadShopList(String identifier);
	

}
