package com.chanceit.http.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Parking;

@Component("parkingDao")
@Repository
public class ParkingDaoImp extends HibernateService implements IParkingDao {
	
	@Override
	public void delete(Parking parking) {
		getSession().delete(parking);
	}

	@Override
	public void save(Parking parking) {
		getSession().saveOrUpdate(parking);
	}

	@Override
	public void update(Parking parking) {
		getSession().clear();
		getSession().update(parking);
	}

	@Override
	public Parking get(int parkingId) {
		return (Parking) getSession().get(Parking.class, parkingId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List getList(String hql,Object[] objs) {
		return super.find(hql,objs);
	}

	@Override
	public boolean delete(List ids) {
		String hql = "delete from Parking where parkingId in (:ids)";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public Page getPageList(Page page, String hql, Object... values) {
		return super.findListPage(page, hql,values);
	}

	@Override
	public boolean updateUser(int driverId) {
		String hql = "update User u set u.driver.driverId = NULL where u.driver.driverId=:id";
		Query query = super.getSession().createQuery(hql).setParameter("id", driverId);
		int rs = query.executeUpdate();
		if(rs == 0){
			return false;
		}
		return true;
	}

}
