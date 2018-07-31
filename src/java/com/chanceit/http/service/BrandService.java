package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IBrandDao;
import com.chanceit.http.pojo.Brand;

@Transactional
@Component("brandService")
public class BrandService implements IBrandService {
	@Autowired
	@Qualifier("brandDao")
	private IBrandDao brandDao;
	
	
	
	@Override
	public String save(Brand brand) {
		try{
			return brandDao.save(brand);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean saveBrand(Brand brand) {
		try{
			brandDao.saveBrand(brand);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public boolean update(Brand brand) {
		brandDao.update(brand);
		return true;
	}
	
	@Override
	public boolean deleteBrand(int id) {
		return brandDao.delete(id);
	}

	@Override
	public Brand get(int brandId) {
		return brandDao.get(brandId);
	}


	@Override
	public Page getPageList(Page page,Object[] values ){
		StringBuffer hql = new StringBuffer("" +
				" from Brand l where 1=1 ");
//		if(values[0]!=null && !values[0].equals("")){
//			hql.append(" and l.account.accountId in(").append(values[0]).append(")");
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			hql.append(" and l.brandName like '%").append(values[1]).append("%'");
//		}
//		if(values[0]!=null && !values[0].equals("")){
//			hql.append(" and l.teamId =").append(values[0]);
//		}
		hql.append(" order by l.id desc");
		return brandDao.getPageList(page, hql.toString());
	}

	
	@Override
	public List getList(Object[] values ){
		StringBuffer hql = new StringBuffer("" +
				" from Brand l where 1=1 ");
//		if(values[0]!=null && !values[0].equals("")){
//			hql.append(" and l.account.accountId=").append(values[0]);
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			hql.append(" and l.brandName like '%").append(values[1]).append("%'");
//		}
		hql.append(" order by l.id desc");
		return brandDao.getList(hql.toString(),null);
	}

}
