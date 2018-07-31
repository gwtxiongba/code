package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.ILevelDao;
import com.chanceit.http.pojo.Level;

@Transactional
@Component("levelService")
public class LevelService implements ILevelService {
	@Autowired
	@Qualifier("levelDao")
	private ILevelDao levelDao;
	
	@Override
	public boolean save(Level level) {
		try{
			levelDao.save(level);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean delete(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return levelDao.delete(list);
	}

	@Override
	public boolean update(Level level) {
		levelDao.update(level);
		return true;
	}
	

	@Override
	public Level get(int levelId) {
		return levelDao.get(levelId);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Level> getList(String companyId) {
//		HttpSession session = ServletActionContext.getRequest().getSession();
//		Levels level = (Levels)session.getAttribute("level");
		
		String hql = "from Level l where 1=1";
		return levelDao.getList(hql,null);
	}
	
	@Override
	public Page getPageList(Page page,Object[] values ){
		StringBuffer hql = new StringBuffer("" +
				"select l.company.companyId as companyId ,l.company.companyName as companyName, " +
				" l.levelId as levelId , l.level as level,levelType as levelType, levelInfo as levelInfo ," +
				" createTime as createTime, levelIp as levelIp"+
				" from Level l where 1=1 ");
		if(values[0]!=null && !values[0].equals("")){
			hql.append("and level= '").append(values[0]).append("'");
		}
		if(values[1]!=null && !values[1].equals("")){
			hql.append(" and levelType=").append(values[1]);
		}

		if(values[3]!=null && !values[3].equals("")){
			String start = values[3].toString() + " 00:00:00";
			String end = values[3].toString() + " 23:59:59";
			hql.append(" and createTime between '").append(start).append("' and '").append(end).append("'");
		}
		hql.append(" order by createTime desc");
		return levelDao.getPageList(page, hql.toString());
	}

	@Override
	public boolean save(short levelType, String levelInfo) {
		// TODO Auto-generated method stub
		return false;
	}


}
