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
 * @Description ��Ʒ��Ϣwebservice����ӿ�
 */
public interface IProductWebService {
	
	/**
	 * @author Administrator
	 * @date Jul 29, 2013
	 * @param productId  ��ƷID
	 * @param shopId     ����ID
	 * @param productName   ��Ʒ����
	 * @param productImage  ��ƷͼƬ·��
	 * @param price         ��Ʒ�۸�
	 * @param description   ��Ʒ����
	 * @param thumbnailImage  ����ͼ
	 * @return
	 * @Description �����̳�ͼƬ����������ƷID����ʽ:{productId:1000}
	 */
	public String saveProduct(int productId,String shopId,String productName,String productImage,
			String price,String description,String thumbnailImage);
	
	/**
	 * @author Administrator
	 * @date Jul 29, 2013
	 * @param productIds ��ƷID�б��Զ��ŷָ�
	 * @return
	 * @Description ɾ����Ʒ,�ɹ�����{}
	 */
	public String deleteProduct(String productIds);
	
	/**
	 * @author Administrator
	 * @date Sep 13, 2013
	 * @param carId  ��ID
	 * @param shopId  4S��ID
	 * @param identifier �û�ʶ����
	 * @param tel ��ϵ�绰
	 * @param buyTime ����ʱ��
	 * @param mileage ��ʻ���
	 * @param brand Ʒ��
	 * @param carType ����
	 * @param carStyle ����
	 * @param device ������Դ
	 * @param color ��ɫ
	 * @param ifaccident  �Ƿ����¹�
	 * @param plate ����
	 * @param pic1 ͼƬ1
	 * @param pic2 ͼƬ2
	 * @param pic3 ͼƬ3
	 * @param pic4 ͼƬ4
	 * @param licenceTime ����ʱ��
	 * @param price �۸�
	 * @return
	 * @Description TODO(������һ�仰�����������������)
	 */
	public String saveSecondCar(String carId,String shopId,String identifier,String tel,String buyTime,Integer mileage,String brand,
			String carType,String carStyle,String device,String color,String ifaccident,String plate,String pic1,
			String pic2,String pic3,String pic4,String licenceTime,String price,Short state);

}
