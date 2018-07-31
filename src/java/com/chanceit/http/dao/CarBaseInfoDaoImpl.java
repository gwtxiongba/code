package com.chanceit.http.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.BaseInfo;


@Component("carBaseInfoDao")
@Repository
public class CarBaseInfoDaoImpl extends HibernateService implements ICarBaseInfoDao{

	@Override
	public boolean delete(List ids, int baseInfo) {
		return false;
	}

	@Override
	public void delete(BaseInfo baseInfo) {
		getSession().delete(baseInfo);
	}

	@Override
	public BaseInfo get(int baseInfoId) {
		return (BaseInfo) getSession().get(BaseInfo.class, baseInfoId);
	}

	@Override
	public void save(BaseInfo baseInfo) {
		getSession().saveOrUpdate(baseInfo);
	}

	@Override
	public void update(BaseInfo baseInfo) {
		getSession().clear();
		getSession().update(baseInfo);
	}

	@Override
	public Page getPageList(Page page, String hql, Object... values) {
		return super.findListPage(page, hql,values);
	}

	@Override
	public List getListSql(String sql, Object... values) {
		return super.findSQL(sql, values);
	}

	@Override
	public boolean updateUser(int userId,int baseId) {
		String hql = "update User u set u.baseInfo.baseInfoId=:base where u.vehicleId =:user";
		Query query = super.getSession().createQuery(hql).setParameter("base", baseId).setParameter("user", userId);
		int rs = query.executeUpdate();
		if(rs == 0){
			return false;
		}
		return true;
	}

	@Override
	public List getList(String hql, Object[] objs) {
		return super.find(hql,objs);
	}
	
}
