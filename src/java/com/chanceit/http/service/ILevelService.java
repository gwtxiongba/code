package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Level;

public interface ILevelService {

	/**
	 * @author dj
	 * @date Jun 21, 2013
	 * @param accountType
	 * @param accountInfo
	 * @return
	 * @Description save account
	 */
	public boolean save(short levelType, String levelInfo);

	public boolean save(Level level);

	public boolean delete(String ids);

	public boolean update(Level level);

	public Level get(int levelId);

	@SuppressWarnings("unchecked")
	public List<Level> getList(String companyId);
	public Page getPageList(Page page,Object[] values );

}