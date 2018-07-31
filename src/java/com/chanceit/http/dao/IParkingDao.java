package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Parking;

public interface IParkingDao {
	
	
	public void save(Parking parking);
	
	public void delete(Parking parking);
	
	public void update(Parking parking);
	
	public boolean delete(List ids);
	
	public Parking get(int parkingId);
	
	public List getList(String hql,Object[] objs);
	
	public Page getPageList(Page page, String hql, Object... values);
	
	public boolean updateUser(int driverId);
}
