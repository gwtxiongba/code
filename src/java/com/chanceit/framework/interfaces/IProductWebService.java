/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jul 27, 2013
 * Id: IProductWebService.java,v 1.0 Jul 27, 2013 4:08:07 PM Administrator
 */
package com.chanceit.framework.interfaces;

/**
 * @ClassName IProductWebService
 * @author Administrator
 * @date Jul 27, 2013 4:08:07 PM
 * @Description 商品信息webservice服务接口
 */
public interface IProductWebService {
	
	/**
	 * @author Administrator
	 * @date Jul 29, 2013
	 * @param productId  商品ID
	 * @param shopId     机构ID
	 * @param productName   商品名称
	 * @param productImage  商品图片路径
	 * @param price         商品价格
	 * @param description   商品描述
	 * @param thumbnailImage  缩略图
	 * @return
	 * @Description 保存商城图片，并返回商品ID，格式:{productId:1000}
	 */
	public String saveProduct(int productId,String shopId,String productName,String productImage,
			String price,String description,String thumbnailImage);
	
	/**
	 * @author Administrator
	 * @date Jul 29, 2013
	 * @param productIds 商品ID列表，以逗号分隔
	 * @return
	 * @Description 删除商品,成功返回{}
	 */
	public String deleteProduct(String productIds);
	
	/**
	 * @author Administrator
	 * @date Sep 13, 2013
	 * @param carId  车ID
	 * @param shopId  4S店ID
	 * @param identifier 用户识别码
	 * @param tel 联系电话
	 * @param buyTime 购车时间
	 * @param mileage 行驶里程
	 * @param brand 品牌
	 * @param carType 车型
	 * @param carStyle 车款
	 * @param device 数据来源
	 * @param color 颜色
	 * @param ifaccident  是否有事故
	 * @param plate 车牌
	 * @param pic1 图片1
	 * @param pic2 图片2
	 * @param pic3 图片3
	 * @param pic4 图片4
	 * @param licenceTime 上牌时间
	 * @param price 价格
	 * @return
	 * @Description TODO(这里用一句话描述这个方法的作用)
	 */
	public String saveSecondCar(String carId,String shopId,String identifier,String tel,String buyTime,Integer mileage,String brand,
			String carType,String carStyle,String device,String color,String ifaccident,String plate,String pic1,
			String pic2,String pic3,String pic4,String licenceTime,String price,Short state);

}
