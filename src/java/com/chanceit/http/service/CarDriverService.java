package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.ICarDriverDao;
import com.chanceit.http.pojo.CarDriver;

@Transactional
@Component("carDriverService")
public class CarDriverService implements ICarDriverService {
	@Autowired
	@Qualifier("carDriverDao")
	private ICarDriverDao carDriverDao;
	
	/**
	 * 保存车辆司机规则
	 */
	@Override
	public void save(CarDriver carDriver) {
		carDriverDao.save(carDriver);
	}
	
	/**
	 * 获取车辆司机规则列表
	 */
	@Override
	public Page getPageList(Page page, Object[] values){
		StringBuffer hql = new StringBuffer("" +
				" from CarDriver c where 1=1 ");
		if(values[0]!=null && !values[0].equals("")){
			hql.append(" and c.car.vehicleId=").append(values[0]);
		}
		if(values[1]!=null && !values[1].equals("")){
			hql.append(" and c.car.account.accountId=").append(values[1]);
		}
		if(values[2]!= null && !"".equals(values[2])){
			hql.append(" and c.car.team.teamId in (").append(values[2]).append(")");
		}
		hql.append(" order by c.car.vehicleId desc");
		return carDriverDao.getPageList(page, hql.toString());
	}

	/**
	 * 通过ID查询车辆司机规则信息
	 */
	@Override
	public CarDriver get(int carDriverId) {
		return carDriverDao.get(carDriverId);
	}
	
	@Override
	public List getByDriverId(int driverId){
		String sql = " select u.plate,u.identifier,c.start_time,c.end_time from car_driver c,user u where c.user_id=u.user_id and c.driver_id="+driverId; 
		return carDriverDao.getByDriverId(sql, null);
	}
	
	/**
	 * 删除车辆司机规则
	 */
	@Override
	public boolean deleteAll(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return carDriverDao.deleteAll(list);
	}

	/**
	 * 通过司机和车辆ID获取车辆规则信息
	 */
	@Override
	public CarDriver getInfoByBothId(int driverId, int carId) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from CarDriver c where c.driver.driverId = ? and c.car.vehicleId = ? ");
		List list = carDriverDao.getList(hql.toString(), new Object[]{driverId, carId});
		if(list.size() > 0){
			return (CarDriver)list.get(0);
		}else{
			return null;
		}
	}

}
