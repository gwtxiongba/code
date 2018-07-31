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
 * @Description 4S�����webservice
 */
public interface IShopInfoWebService {
	
	
	/**
	 * @author Administrator
	 * @date Jul 11, 2013
	 * @param shopId            4S��ID
	 * @param shopName          4S������
	 * @param shopNickName    	4S���ǳ�
	 * @param shopDesc    		4S����
	 * @param address    		4S���ַ
	 * @param shopCoordinate    4S��λ��
	 * @param rescueTel         ��Ԯ�绰
	 * @param qualityTel        ��Ʒ����绰
	 * @param workTel           ԤԼ�绰
	 * @param adviceTel         ��ѯ�绰
	 * @param ccmplaintTel      Ͷ�ߵ绰
	 * @param saleTel      		���ֳ��绰
	 * @return ���
	 * @Description ����/����4S����Ϣ
	 */
	public String saveShopInfo(String shopId ,String shopName,String shopNickName,String shopDesc,
			String address,String shopCoordinate,String tel,
			String rescueTel,String qualityTel,String workTel,String adviceTel,String ccmplaintTel , String saleTel);

}
