package com.chanceit.http.dao;


import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.http.pojo.PointsForm;

@Component("pointsFormDao")
@Repository
public class PointsFormDaoIml extends HibernateService implements IPointsFormDao{

	@Override
	public List<PointsForm> find() {
		// TODO Auto-generated method stub
		List<PointsForm> list = find();
		return list;
	}
   
	@Override
	public List findList(String sql){
		 List list = getSqlList(sql, PointsForm.class);
		return list;
	}

	@Override
	public List findSumList(String sql) {
		// TODO Auto-generated method stub
	 List list = getSqlList(sql);
		return list;
	}
	

}
