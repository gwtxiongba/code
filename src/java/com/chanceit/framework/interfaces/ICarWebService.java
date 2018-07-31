/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Nov 15, 2013
 * Id: ICarWebService.java,v 1.0 Nov 15, 2013 5:08:36 PM Administrator
 */
package com.chanceit.framework.interfaces;

/**
 * @ClassName ICarWebService
 * @author Administrator
 * @date Nov 15, 2013 5:08:36 PM
 * @Description TODO(������һ�仰��������������)
 */
public interface ICarWebService {
	
	/**
	 * @author Administrator
	 * @date Nov 15, 2013
	 * @param carId
	 * @param shopId
	 * @param carName
	 * @param seriesId
	 * @param seriesYear
	 * @param displacement
	 * @return
	 * @Description ���泵��
	 */
	public String saveCar(String carId,String shopId ,String carName ,String seriesId ,String seriesYear , String displacement );
	
	/**
	 * @author Administrator
	 * @date Nov 15, 2013
	 * @param carIds
	 * @return
	 * @Description ɾ������
	 */
	public String deleteCar(String carIds);
	
	/**
	 * @author Administrator
	 * @date Nov 15, 2013
	 * @param seriesId
	 * @param shopId
	 * @param brand
	 * @param brandImage
	 * @param seriesName
	 * @return
	 * @Description ���泵ϵ
	 */
	public String saveSeries(String seriesId,String shopId , String brand ,String brandImage ,String seriesName,String depot);
	
	
	/**
	 * @author Administrator
	 * @date Nov 15, 2013
	 * @param seriesId
	 * @return
	 * @Description ɾ����ϵ
	 */
	public String deletSeries(String seriesId);
	
}
