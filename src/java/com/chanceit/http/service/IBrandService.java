package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Brand;

public interface IBrandService {
	public boolean update(Brand brand);

	public Brand get(int brandId);
	public Page getPageList(Page page,Object[] values );
	public String save(Brand brand);
	public boolean saveBrand(Brand brand);
	public boolean deleteBrand(int id) ;
	
	public List getList(Object[] values );
}