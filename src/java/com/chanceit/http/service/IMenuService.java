package com.chanceit.http.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Menu;

public interface IMenuService {
	public boolean update(Menu team);

	public Menu get(int menuId);
	public Page getPageList(Page page,Object[] values );
	public boolean ifExist(String menuName) throws Exception;
	public Menu getByName(String menuName) throws Exception;
	public String save(Menu team);
	public boolean saveMenu(Menu team);
	public boolean deleteMenu(String ids) ;
	public JSONObject getJson_x(Account account, int accountId,String uids) throws Exception;
	
	public JSONObject getJson(Account account, int accountId,int flag) throws Exception;
	
}