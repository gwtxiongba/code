package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.BaseInfo;


public interface ICarBaseInfoService {
	public List getPlateList(Object[] keywords);
	
	public void save(BaseInfo baseInfo);
	
	public List getByUser(int userId);
	public boolean updateUser(int userId,int baseId);
	public BaseInfo get(int baseInfoId);
	public Page getBaseInfo(Page page,Object[] values );
	public void delBaseInfo(BaseInfo baseInfo);
	public List getPlateListAll(Object[] keywords);
}
