package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.ILevelDao;
import com.chanceit.http.dao.IMemberDao;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Member;

@Transactional
@Component("memberService")
public class MemberService implements IMemberService {
	@Autowired
	@Qualifier("memberDao")
	private IMemberDao memberDao;
	
	@Autowired
	@Qualifier("levelDao")
	private ILevelDao levelDao;
	
	
	@Override
	public Member getMember(int memberId) {
		return memberDao.get(memberId);
	}
	
	@Override
	public String save(Member member) {
		try{
			return memberDao.save(member);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean saveMember(Member member) {
		try{
			memberDao.saveMember(member);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean update(Member member) {
		memberDao.update(member);
		return true;
	}
	

	@Override
	public Member get(int memberId) {
		return memberDao.get(memberId);
	}


	@Override
	public Page getPageList(Page page,Object[] values,int levelId ){
//		StringBuffer hql = new StringBuffer("" +
//				" from Member l where 1=1 ");
//		StringBuffer hql=new StringBuffer("select l.*,t.team_name as teamName,d.name as deptName  from member l,team t,dept d where 1=1 and l.team_id=t.team_id and l.dept_id=d.id ");
		StringBuffer hql=new StringBuffer("select l.*,t.team_name as teamName,d.name as deptName  from member l left join team t on l.team_id=t.team_id left join dept d  on l.dept_id=d.id  where 1=1 ");
//		if(values[0]!=null && !values[0].equals("")){
//			hql.append(" and l.account.accountId in(").append(values[0]).append(")");
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			hql.append(" and l.memberName like '%").append(values[1]).append("%'");
//		}
//		if(values[0]!=null && !values[0].equals("")){
//			hql.append(" and l.teamId in (").append(values[0]).append(")");
//		}
		if(levelId == 4){
			if(values[0]!=null && !values[0].equals("")){
				hql.append(" and l.dept_id in (").append(values[0]).append(")");
			}
		}else{
			if(values[0]!=null && !values[0].equals("")){
				hql.append(" and l.team_id in (").append(values[0]).append(")");
			}
		}
		if(values[1]!=null && !values[1].equals("")){
			hql.append(" and l.status = ").append(values[1]);
		}
		if(values[2]!=null && !values[2].equals("")){
			hql.append(" and l.name like '%").append(values[2]).append("%'");//¸ù¾Ý ³Ë³µÈËÐÕÃû À´ËÑË÷
		}
		if(values[3]!=null && !values[3].equals("")){
			hql.append(" and l.tel like '%").append(values[3]).append("%'");//¸ù¾Ý ÁªÏµµç»° À´ËÑË÷
		}
		hql.append(" order by l.id desc");
		return memberDao.getPageList(page, hql.toString());
	}

	
	@Override
	public boolean ifExist(String memberName) throws Exception{
		String sql = "select * from member where user_name=?";
		List list = memberDao.getListSql(sql, memberName);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean deleteMember(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return memberDao.deleteMember(list);
	}

	@Override
	public Member getByUserName(String memberName) throws Exception {
		String hql = " from Member where userName=? and status = 1";
		List list = memberDao.getList(hql, new Object[]{memberName});
		if(list.size()>0){
			return (Member)list.get(0);
		}
		else{
			return null;
		}
	}

	@Override
	public boolean resetMember(String ids) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return memberDao.resetMember(list);
	}

	@Override
	public boolean ifBind(String memberId) throws Exception{
		String sql = "select * from User where member_id in (?) and if_del = 0";
		List list = memberDao.getListSql(sql, memberId);
		if(list.size()==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public List getUnbindList(Object[] keywords) {
		String sql = "select member_id as memberId,member_name as memberName from member d where account_id=? and d.member_id not in" +
				" (select member_id from user where account_id=? and member_id is NOT NULL and if_del = 0)";
		return memberDao.getListSql(sql, keywords[0],keywords[0]);
	}
	
	



	
	@Override
	public List getList(int levelId,Object[] values ){
		StringBuffer hql = new StringBuffer("" +
				" from Member l where 1=1 ");
		if(levelId<=3){
		if(values[0]!=null && !values[0].equals("")){
			hql.append(" and l.teamId=").append(values[0]);
		}
		}else{
			if(values[0]!=null && !values[0].equals("")){
				hql.append(" and l.deptId=").append(values[0]);
			}	
		}
		
		if(values[1]!=null && !values[1].equals("")){
			hql.append(" and l.status=").append(values[1]);
		}	
		hql.append(" order by l.createTime desc");
		return memberDao.getList(hql.toString(),null);
	}

	public List getListSql(int levelId,Object[] values ){
		//StringBuffer hql = new StringBuffer("" +
				//" from Member l where 1=1 ");
		String sql = "select m.* ,t.team_name as teamName,d.name as deptName from member m left join team t on m.team_id = t.team_id left join dept d on d.id = m.dept_id where 1=1";
		if(levelId==4){
		
		if(values[0]!=null && !values[0].equals("")){
			sql += " and m.dept_id="+values[0];
			//hql.append(" and l.deptId=").append(values[0]);
		}	
		}else{
			if(values[0]!=null && !values[0].equals("")){
				sql  += " and m.team_id="+values[0];
				//hql.append(" and m.t=").append(values[0]);
			}
		}
		
		if(values[1]!=null && !values[1].equals("")){
			//hql.append(" and l.status=").append(values[1]);
			sql += " and m.status="+values[1];
		}	
		sql += " order by m.create_time desc";
		//hql.append(" order by l.createTime desc");
		return memberDao.getListSql(sql, null);
	}
	
	
	@Override
	public List getMemberBId(int memberId){
		String sql = "select * from member d left join user u on u.member_id = d.member_id where d.member_id ="+memberId;
		return memberDao.getListSql(sql,null);
	}
	
	@Override
	public List getListForReport(Object[] values) {
		String sql ="SELECT m.id as id,m.name as name ,SUM(ifnull(o.miles,0))/1000 as mile ,ROUND(SUM(IFNULL(o.cost,0)+IFNULL(o.glf,0)),2) as fee,SUM(TIME_TO_SEC(o.over_time)-TIME_TO_SEC(o.start_time))/3600 as time  from orders o LEFT JOIN member m on o.car_user_id=m.id  where 1=1  and o.status=7 and m.status=1 and if_del=0 ";
//		if(values[0]!=null && !values[0].equals("")){
//			sql+=" and u.plate like '%"+values[0]+"%'";
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			sql+=" and u.identifier like '%"+values[1]+"%'";
//		}
		if(values[0]!= null && !"".equals(values[0])){
			sql+=" and m.team_id in ("+values[0]+")";
		}
		if(values[1]!= null && !"".equals(values[1])){
			sql+=" and o.over_time >= '"+values[1]+"'";
		}
		if(values[2]!= null && !"".equals(values[2])){
			sql+=" and o.over_time <= '"+values[2]+"'";
		}
		sql+="group by m.id,m.name order by m.id desc";
		return memberDao.getListSql(sql);
	}

	@Override
	public List getListMonthForReport(Object[] values) {
		String sql ="SELECT DATE_FORMAT(o.over_time,'%Y%m') as mon,m.name as obj ,m.id as obj_id,SUM(ifnull(o.miles,0))/1000 as mile ,ROUND(SUM(IFNULL(o.cost,0)+IFNULL(o.glf,0)),2) as fee,SUM(TIME_TO_SEC(o.over_time)-TIME_TO_SEC(o.start_time))/3600 as time  from orders o LEFT JOIN member m on o.car_user_id=m.id  where 1=1  and o.status=7 and m.status=1 and if_del=0 ";
		if(values[0]!= null && !"".equals(values[0])){
			sql+=" and m.id ="+values[0];
		}
		if(values[1]!= null && !"".equals(values[1])){
			sql+=" and o.over_time >= '"+values[1]+"'";
		}
		if(values[2]!= null && !"".equals(values[2])){
			sql+=" and o.over_time <= '"+values[2]+"'";
		}
		sql+=" group by DATE_FORMAT(o.over_time,'%Y%m'),m.name,m.id order by DATE_FORMAT(o.over_time,'%Y%m') desc";
		return memberDao.getListSql(sql);
	}
	
	@Override
	public List getListMonthDetailForReport(Object[] values) {
		String sql ="SELECT m.name as obj ,o.start_time,o.over_time,ifnull(o.miles,0)/1000 as mile ,ROUND(IFNULL(o.cost,0)+IFNULL(o.glf,0),2) as fee,(TIME_TO_SEC(o.over_time)-TIME_TO_SEC(o.start_time))/3600 as time  from orders o LEFT JOIN member m on o.car_user_id=m.id  where 1=1  and o.status=7 and m.status=1 and if_del=0 ";
		if(values[0]!= null && !"".equals(values[0])){
			sql+=" and m.id ="+values[0];
		}
		if(values[1]!= null && !"".equals(values[1])){
			sql+=" and DATE_FORMAT(o.over_time,'%Y%m') ="+values[1];
		}
		if(values[2]!= null && !"".equals(values[2])){
			sql+=" and o.over_time >= '"+values[2]+"'";
		}
		if(values[3]!= null && !"".equals(values[3])){
			sql+=" and o.over_time <= '"+values[3]+"'";
		}
		sql+=" order by o.over_time desc";
		return memberDao.getListSql(sql);
	}
}
