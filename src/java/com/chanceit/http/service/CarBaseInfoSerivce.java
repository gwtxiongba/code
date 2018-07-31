package com.chanceit.http.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.ICarBaseInfoDao;
import com.chanceit.http.dao.IUserDao;
import com.chanceit.http.pojo.BaseInfo;



@Transactional
@Component("carBaseInfoService")
public class CarBaseInfoSerivce implements ICarBaseInfoService{
	
	@Autowired
	@Qualifier("carBaseInfoDao")
	private ICarBaseInfoDao carBaseInfoDao;
	
	@Autowired
	@Qualifier("userDao")
	private IUserDao userDao;

	@Override
	public List getPlateList(Object[] keywords) {
		String sql = "select user_id as userId , plate,identifier,brand from user where team_id in ("+keywords[0]+") and (online is null or online = 0) and if_del=0 ";
		 if(keywords[1]!=null && !keywords[1].equals("")){
			sql += " and car_type="+keywords[1]; 
		 }
      if(keywords[2]!=null && !keywords[2].equals("")){
    	  sql += " and brand like '%"+keywords[2]+"%'"; 
		 }
      if("1".equals(keywords[3])){
    	  sql += " and level = 1000"; 
		 }else{
			 sql += " and level is null"; 
		 }
		return carBaseInfoDao.getListSql(sql, null);
	}
	@Override
	public List getPlateListAll(Object[] keywords) {
		String sql = "select user_id as userId , plate,identifier,brand from user where team_id in ("+keywords[0]+") and if_del=0";
		 if(keywords[1]!=null && !keywords[1].equals("")){
			sql += " and car_type="+keywords[1]; 
		 }
      if(keywords[2]!=null && !keywords[2].equals("")){
    	  sql += " and brand like '%"+keywords[2]+"%'"; 
		 }
		return carBaseInfoDao.getListSql(sql, null);
	}
	@Override
	public void save(BaseInfo baseInfo) {
		carBaseInfoDao.save(baseInfo);
	}

	@Override
	public List getByUser(int userId) {
		String hql = "from BaseInfo b where b.user.vehicleId = " + userId;
		return carBaseInfoDao.getList(hql, null);
	}

	@Override
	public boolean updateUser(int userId, int baseId) {
		return carBaseInfoDao.updateUser(userId, baseId);
	}

	@Override
	public BaseInfo get(int baseInfoId) {
		return carBaseInfoDao.get(baseInfoId);
	}

	@Override
	public Page getBaseInfo(Page page, Object[] values) {
		StringBuffer hql = new StringBuffer("from BaseInfo b where 1=1");
		if(values[0]!=null && !values[0].equals("")){
			hql.append(" and b.user.account.accountId=").append(values[0]);
		}
		if(values[1]!=null && !values[1].equals("")){
			hql.append(" and b.user.team.teamId in(").append(values[1]).append(")");
		}
		return carBaseInfoDao.getPageList(page, hql.toString());
	}

	@Override
	public void delBaseInfo(BaseInfo baseInfo) {
		carBaseInfoDao.delete(baseInfo);
	}
	
	
}
