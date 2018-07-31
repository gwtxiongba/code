/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Carstyle;
import com.chanceit.http.service.ICarstyleService;

@Component("carstyleView")
public class CarstyleView extends BaseAction {
	private String charSet="UTF-8";// �ַ���
	private int connectionTimeOut = 30000;// ���ӳ�ʱʱ��
	private int readTimeOut = 40000;// ��ȡ��ʱʱ��
	@Autowired
	@Qualifier("carstyleService")
	private ICarstyleService carstyleService;
	
	
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	
	public String list(Page page){
		//String carstyleName = getParameter("carstyleName");
		
//		Object[] keywords = new Object[]{status};
		page = carstyleService.getPageList(page, null);
		return ResultManager.getBodyResult(page);
	}

	public String getCarStyle() {
		List list = carstyleService.getList(null);
		return ResultManager.getBodyResult(list);
	}
//	public String addCarstyle() throws Exception{
//		String name=getParameter("name");
//		
//		Carstyle carstyle=new Carstyle();
//		carstyle.setName(name);
//		
//		carstyleService.save(carstyle);
//		
//		return ResultManager.getSuccResult();
//	}
	
	//���ӻ��޸�
	public String saveCarstyle() throws Exception{
		String id=getParameter("id");
		String name=getParameter("name");
		
		if(StringUtils.isBlank(id)){//����
			Carstyle carstyle=new Carstyle();
			carstyle.setName(name);
			carstyleService.save(carstyle);
			return ResultManager.getSuccResult();
			
		}else{//�޸�
			
			Carstyle carstyle=carstyleService.get(Integer.parseInt(id));
			if(carstyle==null){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����ڴ�id��");
			}
			carstyle.setName(name);
			carstyleService.saveCarstyle(carstyle);
			return ResultManager.getSuccResult();
		}
		
		
	}
	
	
	public String delete() throws Exception{
		String id = getParameter("id");
		if(StringUtils.isBlank(id)){
			return ResultManager.getFaildResult("��ѡ����Ҫɾ���ļ�¼");
		}
		carstyleService.deleteCarstyle(Integer.parseInt(id));
		return ResultManager.getSuccResult();
	}

}
