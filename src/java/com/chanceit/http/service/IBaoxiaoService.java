package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Baoxiao;

public interface IBaoxiaoService {
	public boolean update(Baoxiao baoxiao);

	public Baoxiao get(int baoxiaoId);
	public Page getPageList(Page page,Object[] values );
	public Baoxiao getByName(String baoxiaoName) throws Exception;
	public String save(Baoxiao baoxiao);
	public boolean saveBaoxiao(Baoxiao baoxiao);
	public Baoxiao getBaoxiao(int baoxiaoId);
	public boolean deleteBaoxiao(String ids,int accountId) ;

	
	public List getList(int type,int uid,String status);
	public List getBaoxiaoBId(int baoxiaoId);
}