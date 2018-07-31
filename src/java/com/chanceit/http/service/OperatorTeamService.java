package com.chanceit.http.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IOperatorTeamDao;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.OpTeamVO;
import com.chanceit.http.pojo.OperatorTeam;
import com.chanceit.http.pojo.Team;

@Transactional
@Component("opTeamService")
public class OperatorTeamService implements IOperatorTeamService {
	@Autowired
	@Qualifier("opTeamDao")
	private IOperatorTeamDao opTeamDao;
	

	/**
	 * add by zhangxin 2014-08-25
	 * 添加操作员与车队绑定关系
	 */
	@Override
	public void saveOpTeam(OperatorTeam opTeam) {
		opTeamDao.save(opTeam);
	}
	
	
	/**
	 * add by zhangxin 2014-08-25
	 * 查询操作员是否存在绑定关系的车队
	 */
	@Override
	public String getGroupList(String accountId, int operatorId) throws Exception{
		/*String hql = "select t.teamId as teamId, t.teamName as teamName from Team t where t.account.accountId = ? order by t.teamId desc";
		List list= opTeamDao.getMapList(hql,new Object[]{Integer.parseInt(accountId)});
		Map teamMap = new HashMap();
		for(int i = 0;i < list.size();i++){
			Map team = (Map)list.get(i);
			teamMap.put(team.get("teamId"), team);
		}*/
		JSONArray array = new JSONArray();
		//获取已绑定操作员的车队信息
		String hql = "from OperatorTeam o where o.account.accountId = " + operatorId;
		List list= opTeamDao.getList(hql,new Object[]{});
		String bindTeamIds = "";
		for(int i=0;i < list.size();i++){
			JSONObject json = new JSONObject();
			OperatorTeam opTeam = (OperatorTeam)list.get(i);
			if(opTeam.getTeam() != null){
				json.put("teamId", opTeam.getTeam().getTeamId());
				json.put("teamName", opTeam.getTeam().getTeamName());
				json.put("operatorId", operatorId);
				bindTeamIds += bindTeamIds + opTeam.getTeam().getTeamId() + ",";
				array.add(json);
			}
		}
		if(!"".equals(bindTeamIds)){
			bindTeamIds = bindTeamIds.substring(0, bindTeamIds.length() - 1);
		}
		
		//获取未绑定操作员的车队信息
		String teamHql = "";
		if(!"".equals(bindTeamIds)){
			teamHql = "from Team t where t.account.accountId =" + accountId +" and t.teamId not in (" + bindTeamIds + ")";
		}else{
			teamHql = "from Team t where t.account.accountId =" + accountId;
		}
		List teamList= opTeamDao.getList(teamHql,new Object[]{});
		for(int i=0;i < teamList.size();i++){
			JSONObject json = new JSONObject();
			Team team = (Team)teamList.get(i);
			json.put("teamId", team.getTeamId());
			json.put("teamName", team.getTeamName());
			json.put("operatorId", 0);
			array.add(json);
		}
		return array.toString();
	}

	/**
	 * add by shaozheng 2014-08-26
	 * 绑定车队的operatorId
	 */
	@Override
	public boolean bindTeam(String ids, int operatorId) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for(String id:idsAry){
			list.add(Integer.parseInt(id));
		}
		return opTeamDao.bindTeam(list,operatorId);
	}

	/**
	 * add by shaozheng 2014-08-26
	 * 取消绑定车队的operatorId
	 */
	@Override
	public boolean unBindTeam(String ids, int operatorId) {
		List list = new ArrayList();
		if(StringUtils.isNotBlank(ids)){
			String[] idsAry = ids.split(",");
			for(String id:idsAry){
				list.add(Integer.parseInt(id));
			}
			return opTeamDao.unBindTeam(list, operatorId);	
		}
		return opTeamDao.unBindTeam(list, operatorId);
	}

	/**
	 * add by zhangxin 2014-08-25
	 * 查询操作员绑定车队列表
	 */
	@Override
	public String getPageList(Page page, Object[] values) {
		StringBuffer hql = new StringBuffer(" from OperatorTeam o where 1=1 ");
		if(values[0]!=null && !values[0].equals("")){
			hql.append(" and o.account.parentId=").append(values[0]);
		}
		hql.append(" order by o.createTime desc");
		List objlist = opTeamDao.getList(hql.toString(), new Object[]{});
		
		int operatorId;
		Map<Integer, OpTeamVO> opMap = new HashMap<Integer, OpTeamVO>();
		//将车队信息用逗号进行拼接
		for(int i = 0;i < objlist.size();i++){
			OperatorTeam opTeam = (OperatorTeam)objlist.get(i);
			operatorId = opTeam.getAccount().getAccountId();
			//看是否已有操作员,没有操作员就添加新的key,并将车队信息放入分组
			if(opMap.get(operatorId) == null){
				OpTeamVO vo = new OpTeamVO();
				vo.setOperatorId(operatorId);
				vo.setOperatorName(opTeam.getAccount().getAccountName());
				if(opTeam.getTeam() != null){
					vo.setTeams(opTeam.getTeam().getTeamName());
				}
				opMap.put(operatorId, vo);
			}else{
				OpTeamVO vo = opMap.get(operatorId);
				String teams = vo.getTeams();
				vo.setTeams(teams + "," + opTeam.getTeam().getTeamName());
				opMap.put(operatorId, vo);
			}
		}
		
		Set<Map.Entry<Integer, OpTeamVO>> set = opMap.entrySet();
		
		List<OpTeamVO> voList = new ArrayList<OpTeamVO>();
        for (Iterator<Map.Entry<Integer, OpTeamVO>> it = set.iterator(); it.hasNext();) {
            Map.Entry<Integer, OpTeamVO> entry = it.next();
            voList.add(entry.getValue());
            System.out.println(entry.getKey() + "--->" + entry.getValue());
        }
        
        net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(voList);
		return array.toString();
	}
	
	@Override
	public void delete(int operatorId) {
		opTeamDao.delete(operatorId);
	}

	@Override
	public void deleteByTeam(int teamId) {
		opTeamDao.deleteByTeam(teamId);
	}
	
	@Override
	public Team get(int teamId) {
		return opTeamDao.get(teamId);
	}


	@Override
	public void save(OperatorTeam opTeam) {
		opTeamDao.save(opTeam);
	}


	@Override
	public Account getAccount(int operatorId) {
		return opTeamDao.getAccount(operatorId);
	}

	/**
	 * 根据ids查询关系列表
	 */
	@Override
	public List getOpTeamByIds(String ids){
		String hql = " from OperatorTeam o where o.account.accountId in (" + ids + ")";
		return opTeamDao.getList(hql.toString(), new Object[]{});
	}
	
	/**
	 * 根据ids获取关系的车队IDs
	 */
	@Override
	public String getTeamIdsByOperatorId(String ids){
		return opTeamDao.getTeamIdsByOperatorId(ids);
	}
}
