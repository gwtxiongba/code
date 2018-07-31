package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Parking;

public interface IParkingService {
	
	public Parking get(int parkingId);
	public List getByDriver(int driverId);
	public void save(Parking parking);
	public void delete(Parking parking);
	public boolean delete(String ids);
	public List getList();
	public Page getParkingList(Page page,Object[] values );
	public Page getUserList(Page page, Object[] values);
	public boolean updateUser(int driverId);
	public List getUser(int driverId);
	public List getUser(String plate);
	public List getWarn(int vehicleId);
	public boolean delDriver(String driverId);
	
}
