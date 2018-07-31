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
 * @Description ����4S��webservice
 */
public interface ICompetitionWebService {
	
	/**
	 * @author Administrator
	 * @date Sep 10, 2013
	 * @param competeId   ������ID����ѡ����������ʾ�Ǹ���1��
	 * @param shopId ����ID
	 * @param competeName ����������
	 * @param competeAddress �������ַ
	 * @param competePointX ����������X
	 * @param competePointY ����������Y
	 * @return
	 * @Description ����/���¾���4S����Ϣ
	 */
	public String save(Integer competeId ,String shopId ,String competeName , String competeAddress,String competePointX,String competePointY );
	
	/**
	 * @author Administrator
	 * @date Sep 10, 2013
	 * @param competeIds ������ID�б�
	 * @return
	 * @Description ɾ����������Ϣ
	 */
	public String delete(String competeIds);
	
	/**
	 * @author Administrator
	 * @date Sep 11, 2013
	 * @param shopId ���Ż���ID
	 * @param recorderTime ����ʱ��
	 * @return
	 * @Description �Լ���Ϊ��λ����ָ��ʱ�䵽����ʱ��ľ���������¼
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
	 * @Description ��Ӿ�����Ϣ��¼
	 */
	public String addStatistics(String identifier, int competeId,
			String recorderDate, String desc);
	
	
	/**
	 * @author Administrator
	 * @date Sep 17, 2013
	 * @param identifier �û�ID
	 * @return
	 * @Description ���ؾ������б�
	 */
	public String downloadShopList(String identifier);
	

}
