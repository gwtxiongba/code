package com.chanceit.http.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Level;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("levelDao")
@Repository
public class LevelDaoImpl extends HibernateService implements ILevelDao {
	
	@Override
	public void save(Level level) {
		getSession().save(level);
	}
	
	@Override
	public boolean delete(List ids) {
		String hql = "delete from Level where levelId in (:ids)";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void delete(Level level) {
		getSession().delete(level);
	}

	@Override
	public void update(Level level) {
		getSession().clear();
		getSession().update(level);
	}

	@Override
	public Level get(int levelId) {
		return (Level) getSession().get(Level.class, levelId);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List getList(String hql,Object[] objs) {
		return super.find(hql,objs);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List getMapList(String hql,Object... objs) {
		Query  query = super.getQuery(hql, objs);
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Page getPageList(Page page , String hql ,Object... values) {
		return super.findListPage(page, hql,values);
	}
	
	
	@Override
	public List getListSql(String sql ,Object... values) {
		return super.findSQL(sql, values);
	}
	

}
