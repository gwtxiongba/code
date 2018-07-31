package com.chanceit.http.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.CarDriver;

/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("carDriverDao")
@Repository
public class CarDriverDaoImpl extends HibernateService implements ICarDriverDao {
	
	@Override
	public void save(CarDriver carDriver) {
		getSession().saveOrUpdate(carDriver);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Page getPageList(Page page , String hql ,Object... values) {
		return super.findListPage(page, hql,values);
	}

	@Override
	public CarDriver get(int carDriverId) {
		return (CarDriver) getSession().get(CarDriver.class, carDriverId);
	}
	
	@Override
	public boolean deleteAll(List ids) {
		String hql = "delete from CarDriver c where c.carDriverId in (:ids)";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List getList(String hql,Object[] objs) {
		return super.find(hql,objs);
	}

	@Override
	public List getByDriverId(String sql,Object[] values){
		return super.findSQL(sql,values);
	}
}
