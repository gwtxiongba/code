package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IDriverDao;
import com.chanceit.http.dao.IParkingDao;
import com.chanceit.http.dao.IUserDao;
import com.chanceit.http.pojo.Driver;
import com.chanceit.http.pojo.Parking;

@Transactional
@Component("parkingService")
public class ParkingService implements IParkingService  {
	
	@Autowired
	@Qualifier("parkingDao")
	private IParkingDao parkingDao;
	
	@Autowired
	@Qualifier("userDao")
	private IUserDao userDao;
	
	@Autowired
	@Qualifier("driverDao")
	private IDriverDao driverDao;

	
	@Override
	public Parking get(int parkingId){
		return parkingDao.get(parkingId);
	}
	
	@Override
	public void save(Parking parking) {
		parkingDao.save(parking);
	}

	@Override
	public void delete(Parking parking) {
		parkingDao.delete(parking);
	}

	@Override
	public boolean delete(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return parkingDao.delete(list);
	}

	@Override
	public List getList() {
		String hql = "from Parking p ";
		return parkingDao.getList(hql, null);
	}

	@Override
	public Page getParkingList(Page page, Object[] values) {
		StringBuffer hql = new StringBuffer("from Parking p where 1=1 ");
		if(values[0]!=null && !"".equals(values[0])){
			hql.append("and p.driver.driverId=").append(values[0]);
		}
		return parkingDao.getPageList(page, hql.toString());
	}
	
	@Override
	public Page getUserList(Page page, Object[] values){
		String hql = "from User u where u.driver.driverId=" + values[0];
		return parkingDao.getPageList(page, hql);
	}

	@Override
	public boolean updateUser(int driverId) {
		return parkingDao.updateUser(driverId);
	}

	@Override
	public List getUser(int driverId) {
		String hql = "from User u where u.driver.driverId =" + driverId;
		return parkingDao.getList(hql,null);
	}

	@Override
	public List getUser(String plate) {
		String hql = "from User u where u.plate like '%" + plate +"%'";
		return parkingDao.getList(hql,null);
	}

	@Override
	public List getByDriver(int driverId) {
		String hql = "from Parking p where p.driver.driverId = " + driverId;
		return parkingDao.getList(hql,null);
	}

	@Override
	public List getWarn(int vehicleId) {
		String hql = "from Warn w where w.user.vehicleId = " + vehicleId;
		return parkingDao.getList(hql,null);
	}
	
	@Override
	public boolean delDriver(String driverId){
		Driver driver =  driverDao.get(Integer.parseInt(driverId));
		if(driver != null){
			driverDao.delete(driver);
			return true;
		}
		return false;
	}
}
