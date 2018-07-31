package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IOperatorTeamDao;
import com.chanceit.http.dao.IWarnDao;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Warn;
		
@Transactional
@Component("warnService")
public class WarnService implements IWarnService {
	@Autowired
	@Qualifier("warnDao")
	private IWarnDao warnDao;
	
	@Autowired
	@Qualifier("opTeamDao")
	private IOperatorTeamDao opTeamDao;
	
	@Override
	public Page getPageList(Page page, Object[] values) {
		StringBuffer sql = new StringBuffer(
				"select l.user_id as vehicleId,l.identifier as identifier,l.plate as plate,l.reason as reason,"
						+ "l.if_read as ifRead,l.time as time from (select b.warn_id,b.time ,b.reader,b.reason,b.if_read,u.user_id, u.identifier, u.team_id, u.plate ,u.account_id "
						+ "FROM  Warn b left join `user` u on u.user_id=b.vehicle_id    order by b.time desc) l where 1=1 ");
		if (values[0] != null && !values[0].equals("")) {
			sql.append(" and l.account_id = ").append(values[0]);
		}
		if(values[1] != null){
			Account account = (Account)values[1];
			if(account.getRole().getRoleId() == 3){
				String teamIds = opTeamDao.getTeamIdsByOperatorId(account.getAccountId() + "");
				if(!"".equals(teamIds)){
					sql.append(" and l.team_id in (").append(teamIds).append(")");
				}
			}
		}

		sql.append(" group by l.identifier order by l.time desc");
		page = warnDao.getPageList2(page, sql.toString());
		List list = warnDao.getListSql(sql.toString());
		page.setTotalCount(list.size());
		page.setTotalPages((int) page.getTotalPages());
		return page;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Page getDetaiPagelList(Page page, Object[] values) throws Exception{
//		StringBuffer hql =  new StringBuffer("select warnId as warnId, b.user.driver as driver,b.reason as reason,b.ifRead as ifRead, b.reader as reader,b.time as time,b.x as x,b.y as y from Boxreminder b");
		StringBuffer hql =  new StringBuffer(" from Warn b");
		List list;
//		if("mobile".equals(from)){ //备用
//			hql.append(" where type<2 and state=0 and l.ifWarnPhone=0 and l.user.company.companyId=?  order by l.hitTime desc " );
//		}else{
			hql.append(" where  b.companyId=? and b.user.vehicleId=?  " );
//		}
		if (values[2] != null && !values[2].equals("")) {
//			String today = DateUtil.parseDate(values[2].toString(), "yyyy-MM-dd") ;
			String start = values[2] +" 00:00:00";
			String end   = values[2] +" 23:59:59";
			hql.append(" and b.time between '").append(start).append("' and '").append(end).append("'");
		}	
		hql.append("  order by b.time desc");
		//list = boxreminderDao.getList(hql.toString(),Integer.parseInt(values[0].toString()), Integer.parseInt(values[1].toString()));
		page = warnDao.getListPage(page,hql.toString(),Integer.parseInt(values[0].toString()), Integer.parseInt(values[1].toString()));
		StringBuffer warnIdSb = new StringBuffer();
		list = page.getResult();
		if(list.size()>0){
			for(Iterator ite = list.iterator();ite.hasNext();){
				Warn bm = (Warn)ite.next();
				if(bm.getIfRead()==0){
					warnIdSb.append(bm.getWarnId()).append(",");
				}
			}
			if(!"".equals(warnIdSb.toString())){
				warnIdSb.delete(warnIdSb.length()-1,warnIdSb.length());
				this.setWarned(warnIdSb.toString(), null,values[3].toString());
			}
		}
		return page;
	}
	
	
	@Override
	public String getAlarmCount(Object[] values){
		StringBuffer hql = new StringBuffer("select count(*) as amount from Warn l where ifRead=0");
		if (values[0] != null && !values[0].equals("")) {
			hql.append(" and l.companyId = ").append(values[0]);
		}
		//如果是操作员,查询操作员所拥有权限的车辆
		if(values[1] != null){
			Account account = (Account)values[1];
			if(account.getRole().getRoleId() == 3){
				StringBuffer sql = new StringBuffer();
				sql.append(" select u.identifier ");
				sql.append(" from user u ");
				sql.append(" where u.if_del = 0 ");
				sql.append(" and u.account_id = ? ");
				sql.append(" and u.team_id in ");
				sql.append(" (select o.team_id ");
				sql.append(" from operator_team o ");
				sql.append(" where o.operator_id = ?);");
				List<Map> identifierList = warnDao.getListSql(sql.toString(), new Object[]{values[0], account.getAccountId()});
				String identifiers = "";
				for(Map map: identifierList){
					identifiers += map.get("identifier") + ",";
				}
				if(!"".equals(identifiers)){
					hql.append(" and l.identifier in (").append(identifiers.substring(0, identifiers.length()-1)).append(")");
				}
			}
		}
		
		hql.append(" group by l.user.vehicleId ");
		List list = warnDao.getMapList(hql.toString());
		if(list.size()>0){
//			Map map = (HashMap)list.get(0);
//			int i = Integer.parseInt(map.get("amount").toString());
			return "{\"total\":"+list.size()+"}";
		}else{
			return "{\"total\":0}";
		}
	}
	
	
	/**
	 * @author dj
	 * @date Nov 29, 2013
	 * @param hitId
	 * @param mobile
	 * @return
	 * @throws Exception
	 * @Description 将信息置为已提醒 
	 */
	public boolean setWarned(String warnId,String from,String reader)  throws Exception{
		String hql;
		if("mobile".equals(from)){
			hql = "update Warn set ifRead=1,reader= '" + reader +"' where warnId in (" + warnId+")";
		}else{
			hql = "update Warn set ifRead=1,reader= '" + reader +"' where warnId  in (" + warnId+")";
		}
		if(!warnDao.update(hql)){
			logger.error("将id=" + warnId +  "的参数异常提醒置为已提醒失败！");
			return false;
		}else{
			return true;
		}
	}
	
	Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public boolean save(Warn warn) {
		try{
			warnDao.save(warn);
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
		return warnDao.delete(list);
	}

	@Override
	public boolean update(Warn work) {
		warnDao.update(work);
		return true;
	}

	@Override
	public Warn get(int warnId) {
		return warnDao.get(warnId);
	}
	
	@Override
	public List getWarn(String warnId) throws Exception {
		String hql = "from Warn  where warnId=?";// + warnId;
		List list = warnDao.getList(hql, Integer.parseInt(warnId));
//		finishWarn("100000002");
		return list;
	}
	

	/**
	 * @author dj
	 * @date Dec 2, 2013
	 * @param page
	 * @param companyId
	 * @return
	 * @throws Exception
	 * @Description backup
	 * @see com.chanceit.common.service.IWarnService#getTodayList(com.chanceit.framework.utils.Page, java.lang.String)
	 */
	@Override
	public Page getTodayList(Page page,String companyId) throws Exception{
		StringBuffer hql = new StringBuffer("" +
				"select workCode as workCode ,ifReply as ifReplay, l.company.companyId as companyId ,l.company.companyName as companyName, " +
				" l.user.userId as userId,l.warnId as warnId , handler as handler, state as state,title as title ," +
				" name as userName,carName as carName ,plate as plate,"+
				" applyTime as applyTime, handleTime as handleTime ,ifRead as ifRead,typeName as typeName,"+
				" warnTime as warnTime, actionType as actionType " +
				" from Warn l where l.parentId=0 ");


		hql.append(" and (company.companyId=").append(companyId);
		hql.append(" or company.parentId=").append(companyId).append(")");
		
		String today = DateUtil.parseDate(new Date(), "yyyy-MM-dd") ;
		String start = today +" 00:00:00";
		String end   = today +" 23:59:59";
		hql.append(" and warnTime between '").append(start).append("' and '").append(end).append("'");
		
		hql.append(" and state=1");

		hql.append(" order by recordTime desc");
//		logService.save(EnumCommon.browse_log, "浏览了预约列表！");
		return warnDao.getPageList(page, hql.toString());
	}
	
	@Override
	public List getCountGroupWarn(Object[] values){
		StringBuffer sql = new StringBuffer("select u.plate,u.identifier,u.user_id vehicleId,b.reason,b.time,sum(b.if_read=0) count from warn b,user u where u.user_id=b.vehicle_id ");
		if(values[0] != null && !values[0].equals("")) {
			sql.append(" and u.account_id = ").append(values[0]);
		}
		if(values[1] != null){
			Account account = (Account)values[1];
			if(account.getRole().getRoleId() == 3){
				String teamIds = opTeamDao.getTeamIdsByOperatorId(account.getAccountId() + "");
				if(!"".equals(teamIds)){
					sql.append(" and b.team_id in (").append(teamIds).append(")");
				}
			}
		}
		if(values[2] != null){
			sql.append(" and b.type in (").append(values[2]).append(")");	
		}
		sql.append(" group by u.user_id order by time desc");
		List list = warnDao.getListSql(sql.toString());
		return list;
	}
	
	@Override
	public List getListWarn(Object[] values) throws Exception{
//		StringBuffer hql =  new StringBuffer("select warnId as warnId, b.user.driver as driver,b.reason as reason,b.ifRead as ifRead, b.reader as reader,b.time as time,b.x as x,b.y as y from Boxreminder b");
		StringBuffer hql =  new StringBuffer(" from Warn b");
		List list;
//		if("mobile".equals(from)){ //备用
//			hql.append(" where type<2 and state=0 and l.ifWarnPhone=0 and l.user.company.companyId=?  order by l.hitTime desc " );
//		}else{
			hql.append(" where  b.companyId=? and b.user.vehicleId=?  " );
//		}
		if (values[2] != null && !values[2].equals("")) {
//			String today = DateUtil.parseDate(values[2].toString(), "yyyy-MM-dd") ;
			String start = values[2] +" 00:00:00";
			String end   = values[2] +" 23:59:59";
			hql.append(" and b.time between '").append(start).append("' and '").append(end).append("'");
		}	
		hql.append("  order by b.time desc");
		//list = boxreminderDao.getList(hql.toString(),Integer.parseInt(values[0].toString()), Integer.parseInt(values[1].toString()));
		list = warnDao.getList(hql.toString(),Integer.parseInt(values[0].toString()), Integer.parseInt(values[1].toString()));
		return list;
	}
	
	@Override
	public List getCountGroupWarn2App(Object[] values){
		StringBuffer sql = new StringBuffer("select u.plate,u.identifier,u.user_id vehicleId,b.reason,b.time,sum(b.if_read=0) count from warn b,user u where u.user_id=b.vehicle_id ");
		if(values[0] != null && !values[0].equals("")) {
			sql.append(" and u.account_id = ").append(values[0]);
		}
		if(values[1] != null){
			sql.append(" and b.time >  '").append(values[1]).append("'");
		}
		if(values[2] != null){
			sql.append(" and b.type in (").append(values[2]).append(")");	
		}
		sql.append(" group by u.user_id order by b.time desc");
		List list = warnDao.getListSql(sql.toString());
		return list;
	}
}
