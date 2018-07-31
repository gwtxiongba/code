/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Caruses;
import com.chanceit.http.service.ICarusesService;

@Component("carusesView")
public class CarusesView extends BaseAction {
	private String charSet="UTF-8";// �ַ���
	private int connectionTimeOut = 30000;// ���ӳ�ʱʱ��
	private int readTimeOut = 40000;// ��ȡ��ʱʱ��
	@Autowired
	@Qualifier("carusesService")
	private ICarusesService carusesService;
	
	
	
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	
	public String list(Page page){
		//String carusesName = getParameter("carusesName");
		
//		Object[] keywords = new Object[]{status};
		page = carusesService.getPageList(page, null);
		return ResultManager.getBodyResult(page);
	}

	
//	public String addCaruses() throws Exception{
//		String name=getParameter("name");
//		
//		Caruses caruses=new Caruses();
//		caruses.setName(name);
//		
//		carusesService.save(caruses);
//		
//		return ResultManager.getSuccResult();
//	}
	
	//���ӻ��޸�
	public String saveCaruses() throws Exception{
		String id=getParameter("id");
		String name=getParameter("name");
		
		if(StringUtils.isBlank(id)){//����
			Caruses caruses=new Caruses();
			caruses.setName(name);
			carusesService.save(caruses);
			return ResultManager.getSuccResult();
			
		}else{//�޸�
			
			Caruses caruses=carusesService.get(Integer.parseInt(id));
			if(caruses==null){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����ڴ�id��");
			}
			caruses.setName(name);
			carusesService.saveCaruses(caruses);
			return ResultManager.getSuccResult();
		}
		
		
	}
	
	
	public String delete() throws Exception{
		String id = getParameter("id");
		if(StringUtils.isBlank(id)){
			return ResultManager.getFaildResult("��ѡ����Ҫɾ���ļ�¼");
		}
		carusesService.deleteCaruses(Integer.parseInt(id));
		return ResultManager.getSuccResult();
	}

}
