package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Carstyle;

public interface ICarstyleService {
	public boolean update(Carstyle carstyle);

	public Carstyle get(int carstyleId);
	public Page getPageList(Page page,Object[] values );
	public String save(Carstyle carstyle);
	public boolean saveCarstyle(Carstyle carstyle);
	public boolean deleteCarstyle(int id) ;
	
	public List getList(Object[] values );
}