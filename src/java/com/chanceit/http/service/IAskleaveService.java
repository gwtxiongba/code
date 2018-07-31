package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Askleave;

public interface IAskleaveService {
	public boolean update(Askleave askleave);

	public Askleave get(int askleaveId);
	public Page getPageList(Page page,Object[] values );
	public Askleave getByName(String askleaveName) throws Exception;
	public String save(Askleave askleave);
	public boolean saveAskleave(Askleave askleave);
	public Askleave getAskleave(int askleaveId);
	public boolean deleteAskleave(String ids,int accountId) ;

	
	public List getList(int type,int uid,String status);
	public List getAskleaveBId(int askleaveId);
}