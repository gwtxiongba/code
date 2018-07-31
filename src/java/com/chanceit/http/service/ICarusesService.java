package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Caruses;

public interface ICarusesService {
	public boolean update(Caruses caruses);

	public Caruses get(int carusesId);
	public Page getPageList(Page page,Object[] values );
	public String save(Caruses caruses);
	public boolean saveCaruses(Caruses caruses);
	public boolean deleteCaruses(int id) ;
	
	public List getList(Object[] values );
}