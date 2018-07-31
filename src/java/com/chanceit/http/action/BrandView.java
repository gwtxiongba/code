/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Brand;
import com.chanceit.http.service.IBrandService;

@Component("brandView")
public class BrandView extends BaseAction {
	private String charSet="UTF-8";// 字符集
	private int connectionTimeOut = 30000;// 连接超时时间
	private int readTimeOut = 40000;// 读取超时时间
	@Autowired
	@Qualifier("brandService")
	private IBrandService brandService;
	
	
	
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	
	public String list(){
		//String brandName = getParameter("carSytleId");
		
//		Object[] keywords = new Object[]{status};
		List list=new ArrayList();
		list = brandService.getList(null);
		return ResultManager.getBodyResult(list);
	}
	
	public String listPage(Page page){
		page = brandService.getPageList(page, null);
		return ResultManager.getBodyResult(page);
	}

	
//	public String addBrand() throws Exception{
//		String name=getParameter("name");
//		
//		Brand brand=new Brand();
//		brand.setName(name);
//		
//		brandService.save(brand);
//		
//		return ResultManager.getSuccResult();
//	}
	
	//增加或修改
	public String saveBrand() throws Exception{
		String id=getParameter("id");
		String name=getParameter("name");
		
		if(StringUtils.isBlank(id)){//增加
			Brand brand=new Brand();
			brand.setName(name);
			brandService.save(brand);
			return ResultManager.getSuccResult();
			
		}else{//修改
			
			Brand brand=brandService.get(Integer.parseInt(id));
			if(brand==null){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"不存在此id！");
			}
			brand.setName(name);
			brandService.saveBrand(brand);
			return ResultManager.getSuccResult();
		}
		
		
	}
	
	
	public String delete() throws Exception{
		String id = getParameter("id");
		if(StringUtils.isBlank(id)){
			return ResultManager.getFaildResult("请选择需要删除的纪录");
		}
		brandService.deleteBrand(Integer.parseInt(id));
		return ResultManager.getSuccResult();
	}

}
