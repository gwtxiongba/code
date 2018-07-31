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
 * @Description ��������webservice
 */
public interface IUserOBDWebService {
	
	/**
	 * @author Administrator
	 * @date Jul 11, 2013
	 * @param identifier  ����ID
	 * @param mileage  ��ʻ���
	 * @param recTime  ��¼ʱ��
	 * @param maintainMileage �������
	 * @param maintainTime  ����ʱ��
	 * @return
	 * @Description �����������ӵ����
	 */
	public String correctMileage(String identifier ,int mileage ,String recTime ,String maintainMileage , String maintainTime);
	
	/**
	 * @author Administrator
	 * @date Jul 11, 2013
	 * @param shopId
	 * @param identifier
	 * @return
	 * @Description ����4S�����û���ϵ��
	 */
	public String addShopUser(String shopId ,String identifier ,String tel,String displacement);
	
	/**
	 * @author Administrator
	 * @date Jul 17, 2013
	 * @param identifiers
	 * @param synTime
	 * @return
	 * @Description ����4S�������Ϣ
	 */
	public String downLoadMileage(String identifiers ,String synTime);
	
	/**
	 * @author Administrator
	 * @date Oct 23, 2013
	 * @param shopId
	 * @param pageNo
	 * @param synTime
	 * @return
	 * @Description �����û������Ϣ
	 */
	public String downLoadMileagePage(String shopId ,int pageNo, String synTime);
	
	/**
	 * @author Administrator
	 * @date Jul 26, 2013
	 * @param identifier
	 * @return
	 * @Description ����û�λ����Ϣ
	 */
	public String checkPoint(String identifier);
	
	/**
	 * @author Administrator
	 * @date Jul 31, 2013
	 * @param shopId
	 * @return
	 * @Description ���4S���ڵ��û�����
	 */
	public String checkInShopUser(String shopId);
	
	/**
	 * @author Administrator
	 * @date Sep 12, 2013
	 * @param identifier
	 * @param mileage 
	 * @return
	 * @Description �ϴ����
	 */
	public String updateMileage(String identifier, String mileage);
	
	/**
	 * @author Administrator
	 * @date Sep 13, 2013
	 * @param identifier
	 * @param faultID ����ID
	 * @return
	 * @Description ���´�����
	 */
	public String updateFault(String identifier, String faultID);
	
	/**
	 * @author dj
	 * @date Sep 17, 2013
	 * @param identifier
	 * @param paras
	 * @param status
	 * @return
	 * @Description �ϱ�Ԥ������
	 */
	public String sendWarnInfo(String identifier,String paras,int status);
	
	/**
	 * @author dj
	 * @date Nov 25, 2013
	 * @param identifier
	 * @param x
	 * @param y
	 * @return
	 * @Description �ϱ���ײ��Ϣ
	 */
	public String sendHitInfo(String identifier, String x, String y);
	
	/**
	 * @author Administrator
	 * @date Nov 22, 2013
	 * @param groupId
	 * @param recorderTime
	 * @return
	 * @Description ���ضϵ��¼
	 */
	public String downloadDispower(String groupId,String recorderTime);

}
