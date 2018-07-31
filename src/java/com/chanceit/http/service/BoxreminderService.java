package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.Collection;
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
import com.chanceit.http.dao.IBoxreminderDao;
import com.chanceit.http.dao.IOperatorTeamDao;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Boxreminder;
		
@Transactional
@Component("boxreminderService")
public class BoxreminderService implements IBoxreminderService {
	@Autowired
	@Qualifier("boxreminderDao")
	private IBoxreminderDao boxreminderDao;
	
	@Autowired
	@Qualifier("opTeamDao")
	private IOperatorTeamDao opTeamDao;
	
	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * select l.user_id as vehicleId,l.identifier as identifier,l.plate as plate,l.type as type,l.reason as reason,l.if_read as ifRead,l.time as time,l.account_id as accountId 
from (select b.warn_id,b.time ,b.type,b.reader,b.reason,b.if_read,u.user_id, u.identifier, u.plate,u.account_id  FROM  boxreminder b
								left join `user` u on u.user_id=b.vehicle_id    order by b.create_time desc) l where 1=1 
and l.account_id=6 
 group by l.identifier order by l.time desc
 以车辆id 聚合
	 */
	@Override
	public Page getPageList(Page page, Object[] values) {
		StringBuffer sql = new StringBuffer(
				"select l.user_id as vehicleId,l.identifier as identifier,l.plate as plate,l.type as type,l.reason as reason,l.x as x,l.y as y, "
						+ "l.if_read as ifRead,l.time as time from (select b.x,b.y, b.warn_id,b.time ,b.type,b.reader,b.reason,b.if_read,u.user_id, u.identifier, u.team_id, u.plate ,u.account_id "
						+ "FROM  boxreminder b left join `user` u on u.user_id=b.vehicle_id    order by b.time desc) l where 1=1 ");
		
		if(values[0] != null && !values[0].equals("")) {
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
		if(values[2] != null){
			sql.append(" and l.type in (").append(values[2]).append(")");	
		}
		sql.append(" group by l.identifier order by l.time desc");
		page = boxreminderDao.getPageList2(page, sql.toString());
		List list = boxreminderDao.getListSql(sql.toString());
		page.setTotalCount(list.size());
		page.setTotalPages((int) page.getTotalPages());
		return page;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public Page getDetaiPagelList(Page page, Object[] values) throws Exception{
//		StringBuffer hql =  new StringBuffer("select warnId as warnId, b.user.driver as driver,b.reason as reason,b.ifRead as ifRead, b.reader as reader,b.time as time,b.x as x,b.y as y from Boxreminder b");
		StringBuffer hql =  new StringBuffer(" from Boxreminder b");
		List list;
//		if("mobile".equals(from)){ //备用
//			hql.append(" where type<2 and state=0 and l.ifWarnPhone=0 and l.user.company.companyId=?  order by l.hitTime desc " );
//		}else{
			hql.append(" where  b.accountId=? and b.user.vehicleId=?  " );
//		}
		if (values[2] != null && !values[2].equals("")) {
//			String today = DateUtil.parseDate(values[2].toString(), "yyyy-MM-dd") ;
			String start = values[2] +" 00:00:00";
			String end   = values[2] +" 23:59:59";
			hql.append(" and b.time between '").append(start).append("' and '").append(end).append("'");
		}
		if(values[4] != null){
			hql.append(" and type in (").append(values[4]).append(")");	
		}
			
		hql.append("  order by b.time desc");
		//list = boxreminderDao.getList(hql.toString(),Integer.parseInt(values[0].toString()), Integer.parseInt(values[1].toString()));
		page = boxreminderDao.getListPage(page,hql.toString(),Integer.parseInt(values[0].toString()), Integer.parseInt(values[1].toString()));
		StringBuffer warnIdSb = new StringBuffer();
		list = page.getResult();
		if(list.size()>0){
			for(Iterator ite = list.iterator();ite.hasNext();){
				Boxreminder bm = (Boxreminder)ite.next();
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
		Account account = (Account)values[1];
		StringBuffer sql = new StringBuffer();
		
		sql.append("select * from (SELECT * from ( select * from boxreminder where account_id = ?");
		if(account.getRole().getRoleId() == 3){
			//先查询操作员拥有车队
			StringBuffer sql1 = new StringBuffer();
			sql1.append(" select u.user_id as vehicleId");
			sql1.append(" from user u ");
			sql1.append(" where u.if_del = 0 ");
			sql1.append(" and u.account_id = ? ");
			sql1.append(" and u.team_id in ");
			sql1.append(" (select o.team_id ");
			sql1.append(" from operator_team o ");
			sql1.append(" where o.operator_id = ?);");
			List<Map> vehicleIdsList = boxreminderDao.getListSql(sql1.toString(), new Object[]{values[0], account.getAccountId()});
			String vehicleIds = "";
			for(Map map: vehicleIdsList){
				vehicleIds += map.get("vehicleId") + ",";
			}
			if(!"".equals(vehicleIds)){
				sql.append(" and vehicle_id in (").append(vehicleIds.substring(0, vehicleIds.length() -1)).append(")");
			}
		}
		sql.append(" order by time desc   ) l  group by l.vehicle_id  ) b where b.if_read=0 ");
		
		List list = boxreminderDao.getListSql(sql.toString(),new Object[]{values[0]});
		
		if(list.size()>0){
			return "{\"total\":"+list.size()+"}";
		}else{
			return "{\"total\":0}";
		}
	}
	
	
	///////
	
	@Override
	public Collection getDateEvent(Object[] keywords) {
		StringBuffer hql =  new StringBuffer("select b.type as eventType,b.time as time,b.x as x,b.y as y from Boxreminder b");
		hql.append(" where  b.accountId=? and b.user.vehicleId=?  " );
		if (keywords[2] != null && !keywords[2].equals("")) {
			String start = keywords[2] +" 00:00:00";
			String end   = keywords[2] +" 23:59:59";
			hql.append(" and b.time between '").append(start).append("' and '").append(end).append("'");
		}	
		hql.append("  order by b.time desc");
		List list = boxreminderDao.getMapList(hql.toString(),Integer.parseInt(keywords[0].toString()), Integer.parseInt(keywords[1].toString()));
		return list;
	}


	@Override
	public Page getTodayList(Page page,String companyId) throws Exception{
		StringBuffer hql = new StringBuffer("" +
				"select workCode as workCode ,ifReply as ifReplay, l.company.companyId as companyId ,l.company.companyName as companyName, " +
				" l.user.userId as userId,l.hitId as hitId , handler as handler, state as state,title as title ," +
				" name as userName,carName as carName ,plate as plate,"+
				" applyTime as applyTime, handleTime as handleTime ,ifRead as ifRead,typeName as typeName,"+
				" hitTime as hitTime, actionType as actionType " +
				" from Boxreminder l where l.parentId=0 ");


		hql.append(" and (company.companyId=").append(companyId);
		hql.append(" or company.parentId=").append(companyId).append(")");
		
		String today = DateUtil.parseDate(new Date(), "yyyy-MM-dd") ;
		String start = today +" 00:00:00";
		String end   = today +" 23:59:59";
		hql.append(" and hitTime between '").append(start).append("' and '").append(end).append("'");
		
		hql.append(" and state=1");

		hql.append(" order by recordTime desc");
//		logService.save(EnumCommon.browse_log, "浏览了预约列表！");
		return boxreminderDao.getPageList(page, hql.toString());
	}
	@Override
	public boolean save(Boxreminder hit) {
		try{
			boxreminderDao.save(hit);
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
		return boxreminderDao.delete(list);
	}

	@Override
	public boolean update(Boxreminder work) {
		boxreminderDao.update(work);
		return true;
	}
	

	@Override
	public Boxreminder get(int hitId) {
		return boxreminderDao.get(hitId);
	}
	
	@Override
	public boolean reply(Object[] objAry) throws Exception{
		
	return false;
	}
	
	@Override
	public List getBoxreminder(String hitId) throws Exception {
		String hql = "from Hit  where hitId=?";// + hitId;
		List list = boxreminderDao.getList(hql, Integer.parseInt(hitId));
//		finishHit("100000002");
		return list;
	}

	

	@Override
	public boolean remark(String hitId,String remark)  throws Exception{
		String hql = "update Hit set state=1,remark='"+remark+"' where hitId=" + hitId;
		if(!boxreminderDao.update(hql)){
			logger.error("将id=" + hitId +  "的碰撞置为已处理失败！");
			return false;
		}else{
			return true;
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
			hql = "update Boxreminder set ifWarnPhone=1,ifRead=1,reader= '" + reader +"' where warnId in (" + warnId+")";
		}else{
			hql = "update Boxreminder set ifWarnWeb=1,ifRead=1,reader= '" + reader +"' where warnId  in (" + warnId+")";
		}
		if(!boxreminderDao.update(hql)){
			logger.error("将id=" + warnId +  "的提醒置为已提醒失败！");
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * add by zhangxin 2014-07-14
	 * 获取指令列表
	 */
	@Override
	public List getCmdList(Account account, Object[] values) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" u.user_id as vehicleId,");
		sql.append(" u.identifier as identifier,");
		sql.append(" u.plate as plate,");
		sql.append(" b.type as type,");
		sql.append(" b.reason as reason,");
		sql.append(" b.if_read as ifRead,");
		sql.append(" b.time as time,");
		sql.append(" b.create_time as createTime,");
		sql.append(" b.x as x,");
		sql.append(" b.y as y");
		sql.append(" FROM `user` u ");
		sql.append(" JOIN boxreminder b ");
		sql.append(" on u.user_id = b.vehicle_id");
		sql.append(" and u.account_id = b.account_id");
		sql.append(" where u.if_del = 0");
		//去掉点火的报警信息 
		sql.append(" and b.type <> 4");
		if (values[0] != null && !values[0].equals("")) {
			sql.append(" and u.account_id = ").append(values[0]);
		}
		//sql.append(" and b.create_time > '2014-07-28 11:33:00'");
		if (values[1] != null && !values[1].equals("")) {
			sql.append(" and b.create_time > '").append(values[1]).append("'");
		}
		//如果是操作员,则获取操作员所拥有权限的车辆ID
		if(account.getRole().getRoleId() == 3){
			//先查询操作员拥有车队
			String teamIds = opTeamDao.getTeamIdsByOperatorId(account.getAccountId() + "");
			if(!"".equals(teamIds)){
				sql.append(" and u.team_id in (").append(teamIds).append(")");
			}
		}
		sql.append(" order by b.time desc");
		List list = boxreminderDao.getCmdList(sql.toString());
		return list;
	}

	@Override
	public List getWarningForApp(Object[] values){
		StringBuffer sql = new StringBuffer(
				"select l.user_id as vehicleId,l.identifier as identifier,l.plate as plate,l.type as type,l.reason as reason,l.x as x,l.y as y, "
						+ "l.if_read as ifRead,l.time as time,l.warn_id as id from (select b.x,b.y, b.warn_id,b.time ,b.type,b.reader,b.reason,b.if_read,u.user_id, u.identifier, u.team_id, u.plate ,u.account_id "
						+ "FROM  boxreminder b left join `user` u on u.user_id=b.vehicle_id    order by b.time desc) l where 1=1 ");
		
		if(values[0] != null && !values[0].equals("")) {
			sql.append(" and l.user_id = ").append(values[0]);
		}
		if(values[1] != null){
			sql.append(" and l.time >  '").append(values[1]).append("'");
		}
		if(values[2] != null){
			sql.append(" and l.type in (").append(values[2]).append(")");	
		}
		sql.append("  order by l.time desc");
		List list = boxreminderDao.getListSql(sql.toString());
		return list;
	}
	
	@Override
	public List getCountGroupWarn(Object[] values){
		StringBuffer sql = new StringBuffer("select u.plate,u.identifier,u.user_id vehicleId,b.reason,b.time,sum(b.if_read=0) count from boxreminder b,user u where u.user_id=b.vehicle_id ");
		if(values[0] != null && !values[0].equals("")) {
			sql.append(" and b.account_id = ").append(values[0]);
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
		List list = boxreminderDao.getListSql(sql.toString());
		return list;
	}
	@Override
	public List getCountGroupWarn2App(Object[] values){
		StringBuffer sql = new StringBuffer("select u.plate,u.identifier,u.user_id vehicleId,b.reason,b.time,sum(b.if_read=0) count from boxreminder b,user u where u.user_id=b.vehicle_id ");
		if(values[0] != null && !values[0].equals("")) {
			sql.append(" and b.account_id = ").append(values[0]);
		}
		if(values[1] != null){
					sql.append(" and b.time >  '").append(values[1]).append("'");
		}
		if(values[2] != null){
			sql.append(" and b.type in (").append(values[2]).append(")");	
		}
		sql.append(" group by u.user_id order by b.time desc");
		List list = boxreminderDao.getListSql(sql.toString());
		return list;
	}
	
	@Override
	public List getCountGroupWarn2App2(Object[] values){
		StringBuffer sql = new StringBuffer("select u.plate,u.identifier,u.user_id vehicleId,b.reason,b.time,sum(b.if_read=0) count from boxreminder b,user u where u.user_id=b.vehicle_id ");
		if(values[0] != null && !values[0].equals("")) {
			sql.append(" and u.team_id in (").append(values[0]).append(")");
		}
		if(values[1] != null){
					sql.append(" and b.time >  '").append(values[1]).append("'");
		}
		if(values[2] != null){
			sql.append(" and b.type in (").append(values[2]).append(")");	
		}
		sql.append(" group by u.user_id order by b.time desc");
		List list = boxreminderDao.getListSql(sql.toString());
		return list;
	}
	
	@Override
	public boolean setWarnedForApp(String warnId,String reader)  throws Exception{
		String	hql = "update Boxreminder set ifWarnPhone=1,ifRead=1,reader= '" + reader +"' where warnId in (" + warnId+")";
		if(!boxreminderDao.update(hql)){
			logger.error("将id=" + warnId +  "的提醒置为已提醒失败！");
			return false;
		}else{
			return true;
		}
	}
}
