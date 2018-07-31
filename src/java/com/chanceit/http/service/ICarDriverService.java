package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.CarDriver;

public interface ICarDriverService {
	public void save(CarDriver carDriver);
	public Page getPageList(Page page, Object[] values);
	public CarDriver get(int carDriverId);
	public boolean deleteAll(String ids);
	public CarDriver getInfoByBothId(int driverId, int carId);
	public List getByDriverId(int driverId);
}