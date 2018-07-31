/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jul 19, 2013
 * Id: INewInfoWebService.java,v 1.0 Jul 19, 2013 1:46:01 PM Administrator
 */
package com.chanceit.framework.interfaces;

/**
 * @ClassName INewInfoWebService
 * @author Administrator
 * @date Jul 19, 2013 1:46:01 PM
 * @Description ���ŵ�webservice����
 */
public interface INewInfoWebService {
	
	/**
	 * @author Administrator
	 * @date Jul 22, 2013
	 * @param newsId     	����ID
	 * @param companyId     ����ID
	 * @param newsTitle     ���ű���
	 * @param newsContext   ��������
	 * @param newsType      ��������
	 * @param listOrder     �����
	 * @return
	 * @Description ����/�������ű���
	 */
	public String saveNews(Integer newsId ,String companyId,String newsTitle,String newsContext,String newsType,Integer listOrder ,String image);

	public String deleteNews(String newsIds);
	
}
