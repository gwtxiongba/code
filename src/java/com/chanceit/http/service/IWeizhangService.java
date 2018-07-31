package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Weizhang;

public interface IWeizhangService {
	public boolean update(Weizhang weizhang);

	public Weizhang get(int weizhangId);
	public Page getPageList(Page page,Object[] values );
	public String save(Weizhang weizhang);
	public boolean saveWeizhang(Weizhang weizhang);
	public boolean deleteWeizhang(int id) ;
	
	public List getList(Object[] values );
}