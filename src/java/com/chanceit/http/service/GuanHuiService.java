package com.chanceit.http.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IGhActionDao;
import com.chanceit.http.dao.IGhLineWarnDao;
import com.chanceit.http.dao.IGhPointsDao;
import com.chanceit.http.pojo.GhAction;
import com.chanceit.http.pojo.GhLineWarn;
import com.chanceit.http.pojo.GhPoints;

@Transactional
@Component("guanhuiService")
@SuppressWarnings("unchecked")
public class GuanHuiService implements IGuanHuiService {

	@Autowired
	@Qualifier("ghactionDao")
	private IGhActionDao ghactionDao;
	@Autowired
	@Qualifier("ghpointDao")
	private IGhPointsDao ghpointDao;

	@Autowired
	@Qualifier("ghlineDao")
	private IGhLineWarnDao ghlineDao;

	@Override
	public String postData() {
		// TODO Auto-generated method stub
//		MessageClient mc = new MessageClient();
//		// String uid = "153206344";
//		// String uid = "253039860";
//		String url = postUrl;
//		String param = "uids=1,1,1&pts=30.587861,114.307109;30.582017,114.320907;30.575301,114.316991;30.574834,114.318248;30.574803,114.319398;30.574213,114.320296;30.579529,114.325219&r=100";
//		String rec = mc.crawlPost(url, param, "utf-8");
		return "";
	}

	@Override
	public void save(GhAction action) {
		// TODO Auto-generated method stub
		ghactionDao.add(action);
	}

	@Override
	public void save(GhPoints gp) {
		// TODO Auto-generated method stub
		ghpointDao.add(gp);
	}
	
	@Override
	public void  updateDel(int id){
		ghpointDao.updateDel(id);
	}

	@Override
	public void saveline(GhLineWarn glw) {
		// TODO Auto-generated method stub
		ghlineDao.add(glw);
	}
    @Override
	public List<GhPoints> getLineList(int accountId,String  teamids){
    //	ghpointDao.getAll();
    	return ghpointDao.getAll(accountId,teamids);
    }
    
    @Override
	public List<GhPoints> getCircleList(int accountId,String type,String teamIds){
        //	ghpointDao.getAll();
        	return ghpointDao.getCircles(accountId, type,teamIds);
        }
    
    @Override
	public Page getWarnList(Page page,int accountId,String teamids){
        //	ghpointDao.getAll();
    	//String sql="select a.*,b.pts as path from ghpoints as b,(select l.*,u.plate from linewarn as l,user as u  where u.identifier=l.uid and l.account_id="+accountId+") as a where a.lineid=b.id ";    
    	//String sql="select a.* from (select l.*,u.plate from linewarn as l,user as u  where u.identifier=l.uid and l.account_id="+accountId+" ) as a,ghaction as b where a.uid=b.uid order by a.time desc";    
    	String sql="select l.*,u.plate from linewarn as l,user as u  where u.identifier=l.uid and u.if_del=0 and u.team_id in("+teamids+") order by time desc";    
//    	if(role){
//    		sql="select l.*,u.plate from linewarn as l,user as u  where u.identifier=l.uid and u.if_del=0 and l.op_id="+accountId+" order by time desc";    
//    	}
    	return ghlineDao.getPageList(page, sql, null);
    }
    
    @Override
	public List getWarnListToApp(int accountId,int time){
        //	ghpointDao.getAll();
    	//String sql="select a.*,b.pts as path from ghpoints as b,(select l.*,u.plate from linewarn as l,user as u  where u.identifier=l.uid and l.account_id="+accountId+") as a where a.lineid=b.id ";    
    	//String sql="select a.* from (select l.*,u.plate from linewarn as l,user as u  where u.identifier=l.uid and l.account_id="+accountId+" ) as a,ghaction as b where a.uid=b.uid order by a.time desc";    
    	String sql="select l.*,u.plate from linewarn as l,user as u  where u.identifier=l.uid and u.if_del=0 and l.account_id="+accountId+" and l.time>"+time+" order by time desc";    
    	return ghlineDao.getWarnList(sql);
    }
    
    @Override
	public List getWarnListToApp2(int accountId,int time,String teamIds){
        //	ghpointDao.getAll();
    	//String sql="select a.*,b.pts as path from ghpoints as b,(select l.*,u.plate from linewarn as l,user as u  where u.identifier=l.uid and l.account_id="+accountId+") as a where a.lineid=b.id ";    
    	//String sql="select a.* from (select l.*,u.plate from linewarn as l,user as u  where u.identifier=l.uid and l.account_id="+accountId+" ) as a,ghaction as b where a.uid=b.uid order by a.time desc";    
    	String sql="select l.*,u.plate from linewarn as l,user as u  where u.identifier=l.uid and u.if_del=0 and u.team_id in("+teamIds+") and l.time>"+time+" order by time desc";    
    	return ghlineDao.getWarnList(sql);
    }
    
	@Override
	public List getLineRuleList(){
    	String sql = "select a.uid as identifier,a.pid as lineId,a.id as ruleId,b.r as radius,c.plate as plate from ghpoints as b,ghaction as a,user as c where  c.if_del=0 and c.identifier=a.uid and a.pid=b.id and b.ws is  null";
    	List list = ghpointDao.getLineRuleList(sql);
    	return list;
    }
	@Override
	public List getCircleRuleList(String type){
    	String sql = "select a.uid as identifier,a.pid as lineId,a.id as ruleId,b.r as radius,c.plate as plate from ghpoints as b,ghaction as a,user as c where c.identifier=a.uid and c.if_del = 0 and a.pid=b.id and b.ws = '"+type+"'";
    	List list = ghpointDao.getLineRuleList(sql);
    	return list;
    }
	
	
	@Override
	public void delete(GhPoints gp){
		ghpointDao.del(gp);
		
	}
	@Override
	public void delete(GhAction gA){
		ghactionDao.del(gA);
		
	}
	@Override
	public void delete(int pid){
		ghactionDao.del(pid);
		
	}
	@Override
	public int getMaxId(){
	List list = ghpointDao.getMaxId();
	int id = 0;
	 if(list != null && list.size() > 0){
		  id= Integer.parseInt(list.get(0).toString());
	 }
	 return id;
	} 
	@Override
	public List<GhAction> getById(int id){
		//ghactionDao.getById(id);
		return ghactionDao.getById(id);
	}
	@Override
	public List<GhAction> getByUidAndPid(int pid){
	//	String sql = "select * from ghaction where and pid="+pid;
		return ghactionDao.getByUidAndPid(pid);
	}
	@Override
	public void batchSave(String sql){
		ghactionDao.save(sql);
	}
	@Override
	public void update(int id){
		String sql = "update linewarn set isread=1 where id="+id;
		ghlineDao.update(sql);
	}
	@Override
	public List getCount(int account_id,String teamids){
		//String sql = "select *  from linewarn where isread=0 and account_id="+account_id;
		String sql = "select l.*,u.plate from linewarn as l,user as u  where l.isread=0 and u.identifier=l.uid and u.if_del=0 and u.team_id in("+teamids+")";
		
		return ghlineDao.getCount(sql);
	}
	
	@Override
	public List getLimitLine(){
		//String sql = "select a.uid as uid,b.pts as path,b.time as time from ghaction as a,ghpoints as b where a.pid=b.id";
		String sql = "select a.uid as uid,b.pts as path,b.time as time,b.deltime as deltime,b.ifdel as ifdel from ghaction as a,ghpoints as b where a.pid=b.id";
		//String sql = "select a.uid as identifier,b.pts as path from ghaction as a,ghpoints as b where a.pid=b.id and a.uid='"+123+"'";
		return ghlineDao.getLimiList(sql);
	}
	@Override
	public List getLimitLine(String uid,String type){
		//String sql = "select a.uid as uid,b.pts as path,b.time as time from ghaction as a,ghpoints as b where a.pid=b.id";
		
		String sql = "";//String sql = "select a.uid as uid,b.pts as path,b.time as time,b.deltime as deltime,b.ifdel as ifdel from ghaction as a,ghpoints as b where a.pid=b.id";
	//	if("line".equals(type)){
			//sql = "select a.uid as identifier,b.pts as path,b.ws as ws from ghaction as a,ghpoints as b where a.pid=b.id and a.uid='"+uid+"' and b.ws  is null ";
		//}else{
			sql = "select a.uid as identifier,b.pts as path,b.ws as ws from ghaction as a,ghpoints as b where a.pid=b.id and a.uid='"+uid+"'";
		//}
		return ghlineDao.getLimiList(sql);
	}
}
