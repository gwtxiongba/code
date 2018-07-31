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
 * @Description 新闻的webservice服务
 */
public interface INewInfoWebService {
	
	/**
	 * @author Administrator
	 * @date Jul 22, 2013
	 * @param newsId     	新闻ID
	 * @param companyId     机构ID
	 * @param newsTitle     新闻标题
	 * @param newsContext   新闻正文
	 * @param newsType      新闻类型
	 * @param listOrder     排序号
	 * @return
	 * @Description 保存/更新新闻标题
	 */
	public String saveNews(Integer newsId ,String companyId,String newsTitle,String newsContext,String newsType,Integer listOrder ,String image);

	public String deleteNews(String newsIds);
	
}
