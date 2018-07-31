package com.chanceit.http.service;

import java.util.Date;
import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Weibao;

public interface IWeibaoService {
	public boolean update(Weibao weibao);

	public Weibao get(int weibaoId);
	public Page getPageList(Page page,Object[] values );
	public String save(Weibao weibao);
	public boolean saveWeibao(Weibao weibao);
	public boolean deleteWeibao(int id) ;
	public List getListEnd(Object[] values);
	public List getList(Object[] values );
}