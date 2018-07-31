package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Weixiu;

public interface IWeixiuService {
	public boolean update(Weixiu weixiu);

	public Weixiu get(int weixiuId);
	public Page getPageList(Page page,Object[] values );
	public String save(Weixiu weixiu);
	public boolean saveWeixiu(Weixiu weixiu);
	public Weixiu getWeixiu(int weixiuId);
	public boolean deleteWeixiu(String ids) ;

	
	public List getList(int type,int uid,String status);
	public List getWeixiuBId(int weixiuId);
}