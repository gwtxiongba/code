package com.chanceit.http.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.exceptions.CommonException;
import com.chanceit.framework.utils.JedisService;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.SpringService;
import com.chanceit.http.dao.ILevelDao;
import com.chanceit.http.dao.IOperatorTeamDao;
import com.chanceit.http.dao.IUserDao;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Level;
import com.chanceit.http.pojo.User;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

@Transactional
@Component("userService")
public class UserService implements IUserService {
	private String charSet="UTF-8";// 字符集
	private int connectionTimeOut = 30000;// 连接超时时间
	private int readTimeOut = 40000;// 读取超时时间
	private String httpURL = "http://127.0.0.1:8080/api.action?";
	private String appid="1";
	@Autowired
	@Qualifier("userDao")
	private IUserDao userDao;

	@Autowired
	@Qualifier("levelDao")
	private ILevelDao levelDao;




	@Autowired
	@Qualifier("jedisService")
	private JedisService jedisService;

	@Autowired
	@Qualifier("opTeamDao")
	private IOperatorTeamDao opTeamDao;
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	@Override
	public boolean save(User user) throws Exception {
		// try{
//		if (user.getVehicleId() == null && user.getIdentifier().length() == 9) { // new
//			String shopId = userOBDWebService.saveShopUser(user);
//			if (!user.getAccount().getAccountId().toString().equals(shopId)) {
//				throw new CommonException("识别码已被绑定过");
//			}
//		}
		userDao.save(user);
		synRedis(user.getAccount().getAccountId());
		return true;
		// }catch(Exception e){
		// e.printStackTrace();
		// return false;
		// }
	}

	@Override
	public boolean delete(String ids, int accountId) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for (String id : idsAry) {
			list.add(Integer.parseInt(id));
		}
		if (userDao.delete(list, accountId)) {
			synRedis(accountId);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean fakeDelete(String ids, int accountId) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		User u = null;
		for (String id : idsAry) {
			 u = userDao.get(Integer.parseInt(id));
			list.add(Integer.parseInt(id));
		}
		if (userDao.fakeDelete(list, accountId)) {
			//synRedis(accountId);
			// 通知node.js
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean update(User user) {
		userDao.update(user);
		return true;
	}

	@Override
	public User get(int userId) {
		return userDao.get(userId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List getList(String accountId, int teamId) throws Exception {
		String hql = "select vehicleId as vehicleId,plate as plate,l.team.teamId as teamId,identifier as identifier from User l where l.ifDel=0 and l.level is null and (l.team.teamId is NUll or l.team.teamId=? ) order by vehicleId asc,l.team.teamId desc";
		Object[] values = new Object[] {  teamId };
		List list = userDao.getMapList(hql, values);
		return list;
	}

	@Override
	public List getListById(String accountId, int teamId) throws Exception {
		String hql = "select identifier as identifier, vehicleId as vehicleId,plate as plate,x as x,y as y,ifOff as ifOff from User l where l.ifDel=0 and l.account.accountId = ? and  l.team.teamId=? and l.level is null  order by vehicleId asc,l.team.teamId desc";
		Object[] values = new Object[] { Integer.parseInt(accountId), teamId };
		List list = userDao.getMapList(hql, values);
		return list;
	}

	@Override
	public List getList(Object[] values) throws Exception {
		StringBuffer hql = new StringBuffer(
				"from User l where 1=1 and l.level is null and l.ifDel =0 ");
		if (values[0] != null && !values[0].equals("")) {
			hql.append(" and l.account.accountId=").append(values[0]);
		}
		if (values[1] != null && !values[1].equals("")) {
			hql.append(" and l.plate like '%").append(values[1]).append("%'");
		} else {
			hql.append(" and ifMonitor=1 ");
		}
		if (values[2] != null && !values[2].equals("")) {
			hql.append(" and l.team.teamId in (").append(values[2]).append(")");
		}
		hql.append(" order by l.createTime desc");

		return userDao.getList(hql.toString());
	}

	@Override
	public List getList1(String accountId) throws Exception {
		StringBuffer hql = new StringBuffer(
				"from User l where 1=1 and l.ifDel =0 and l.level is null");
		hql.append(" and l.account.accountId=").append(accountId);
		List list = userDao.getList(hql.toString());
		return userDao.getList(hql.toString());
	}
	
	@Override
	public List getList2(int teamId,String teamIds) throws Exception {
		StringBuffer hql = new StringBuffer(
				"from User l where 1=1 and l.ifDel =0 and l.level is null");
		if(teamId == 36){
			hql.append(" and (l.team.teamId in (").append(teamIds).append(") or l.team.teamId is null)");
		}else{
		hql.append(" and l.team.teamId in (").append(teamIds).append(")");
		}
		return userDao.getList(hql.toString());
	}

	/**
	 * 操作员权限的查询
	 * 
	 * @param values
	 * @return
	 * @throws Exception
	 */
	@Override
	public List getListForOperator(Object[] values) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" * ");
		sql.append(" FROM ");
		sql.append(" user u ");
		sql.append(" WHERE u.account_id = ").append(values[0]).append(" ");
		sql.append(" and");
		sql.append(" (u.team_id is null ");
		sql.append(" or ");
		sql.append(" u.team_id in ");
		sql
				.append(
						" (select o.team_id from operator_team o where o.operator_id = ")
				.append(values[1]).append(")) ");
		sql.append(" and u.if_del = 0 ");
		if (values[2] != null && !values[2].equals("")) {
			sql.append(" and u.plate like '%").append(values[2]).append("%'");
		} else {
			sql.append(" and u.if_monitor = 1 ");
		}
		sql.append(" order by u.create_time desc;");
		return userDao.getListForOp(sql.toString());
	}

//	@Override
//	@SuppressWarnings("unchecked")
//	/**
//	 * 备用，只拉取了 绑定的车队，没车的显示不出来
//	 */
//	public JSONObject getJson(String accountId) throws Exception {
//		JSONObject rsJson = new JSONObject();
//		JSONArray jsonAryVehicle = new JSONArray();
//		String currentTeamName;
//
//		String hql = "from User l where l.account.accountId = ? and l.ifDel = 0 order by vehicleId asc,l.team.teamId asc";
//		Object[] values = new Object[] { Integer.parseInt(accountId) };
//
//		List<User> list = userDao.getList(hql, values);
//		long _t = System.currentTimeMillis();
//		System.out.println(_t);
//		if (list.size() > 0) {
//			String[] uids = new String[list.size()];
//			for (int i = 0; i < list.size(); i++) {
//				User user = list.get(i);
//				uids[i] = user.getIdentifier() + "_";
//			}
//			Map<String, JSONArray> teamMap = new HashMap<String, JSONArray>();
//			for (int i = 0; i < list.size(); i++) {
//				User user = list.get(i);
//				// 设置单个车辆信息
//				JSONObject results = new JSONObject();
//
//				results.put("vehicleId", user.getVehicleId());
//				results.put("identifier", user.getIdentifier());
//				results.put("plate", user.getPlate());
//
//				if (user.getTeam() != null) {
//					currentTeamName = user.getTeam().getTeamName();
//					results.put("order", user.getTeam().getOrder());
//				} else {
//					currentTeamName = "未分组车辆";
//					results.put("order", 10000);
//				}
//
//				// 看是否已有分组,没有分组就添加新的分组,并将车辆信息放入分组
//				if (teamMap.get(currentTeamName) == null) {
//					JSONArray dataArray = new JSONArray();
//					dataArray.put(results);
//					teamMap.put(currentTeamName, dataArray);
//				} else {
//					JSONArray dataArray = teamMap.get(currentTeamName);
//					dataArray.put(results);
//				}
//			}
//
//			Set<Map.Entry<String, JSONArray>> set = teamMap.entrySet();
//			for (Iterator<Map.Entry<String, JSONArray>> it = set.iterator(); it
//					.hasNext();) {
//				Map.Entry<String, JSONArray> entry = it
//						.next();
//				JSONObject jo = new JSONObject();
//				jo.put("teamName", entry.getKey());
//				jo.put("vehicles", entry.getValue());
//				jsonAryVehicle.put(jo);
//
//				System.out.println(entry.getKey() + "--->" + entry.getValue());
//			}
//
//			rsJson.put("teams", jsonAryVehicle);
//			rsJson.put("total", list.size());
//			rsJson.put("onlines", 0);// jedisUtils.getOnlinesAmount(uids));
//			return rsJson;
//		}
//		return null;
//	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject getOnlineAmount(Account account, String accountId)
			throws Exception {
		JSONObject rsJson = new JSONObject();

		StringBuffer hql = new StringBuffer();
		hql.append(" from User l where l.ifDel = 0");
		//hql.append(" and l.account.accountId = ").append(accountId);
		// 操作员自己的权限
		//if (account.getRole().getRoleId() == 3) {
			String teamIds = teamService.getTeamIdStr(account);
					
			if (!"".equals(teamIds)) {
				hql.append(" and l.team.teamId in (").append(teamIds).append(
						")");
			}
		//}
		hql.append("order by vehicleId asc");

		List<User> list = userDao.getList(hql.toString(), new Object[] {});
		if (list.size() > 0) {
			String uids = "";
			for (int i = 0; i < list.size(); i++) {
				User user = list.get(i);
				uids += user.getIdentifier() + ",";
			}
			System.out.println("///"+uids);
			rsJson.put("total", list.size());
			rsJson.put("onlines", jedisService.getOnlinesAmount(uids));
			return rsJson;
		}
		rsJson.put("total", 0);
		rsJson.put("onlines", 0);
		return rsJson;
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject getRelayAmount(Object[] values) throws Exception {
		JSONObject rsJson = new JSONObject();
		StringBuffer hql = new StringBuffer();
		hql
				.append(" from User l where l.ifDel = 0 and l.ifRelay = 1 and l.ifOff=0");
		hql.append(" and l.account.accountId = ").append(values[2]);
		// 操作员自己的权限
		hql.append("order by ifRelay desc");

		List<User> list = userDao.getList(hql.toString(), new Object[] {});
		if (list.size() > 0) {
			// String uids="";
			// for(int i=0;i<list.size();i++){
			// User user = list.get(i);
			// uids += user.getIdentifier()+",";
			// }
			Account account = (Account) values[0];
			values[0] = account.getAccountId().toString();
			rsJson.put("total", getRelayTotal(values).size());
			rsJson.put("ons", list.size());
			return rsJson;
		}
		rsJson.put("total", 0);
		rsJson.put("ons", 0);
		return rsJson;
	}

	@SuppressWarnings("unchecked")
	public List getRelayTotal(Object[] values) throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append(" from User l where l.ifDel = 0 and l.ifRelay = 1");
		hql.append(" and l.account.accountId = ").append(values[2]);
		// 操作员自己的权限
		hql.append("order by ifRelay desc");

		return userDao.getList(hql.toString());
	}

	@SuppressWarnings("unchecked")
	public String getJsonFromDb(String accountId, String time, String pt0,
			String pt1) throws Exception {
		JSONObject rsJson = new JSONObject();
		JSONArray jsonAryPos = new JSONArray();
		JSONArray jsonAryRemove = new JSONArray();
		JSONArray jsonAryEdit = new JSONArray();

		Date lastTime;
		if (!StringUtils.isEmpty(time)) {
			lastTime = new Date(Long.parseLong(time));
		} else {
			lastTime = null;
		}

		String hql = "from User l where l.account.accountId = ? and (l.ifDel is NULL or l.ifDel<>1 or l.delTime>=?) order by vehicleId asc";
		Object[] values = new Object[] { Integer.parseInt(accountId), lastTime };

		String uids = null;
		List<User> list = userDao.getList(hql, values);
		long _t = System.currentTimeMillis();
		System.out.println(_t);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				User user = list.get(i);
				JSONObject results = new JSONObject();

				results.put("vehicleId", user.getVehicleId());
				results.put("identifier", user.getIdentifier());
				results.put("plate", user.getPlate());
				results.put("userName", user.getUserName() == null ? "" : user
						.getUserName());
				results.put("tel", user.getTel() == null ? "" : user.getTel());
				results.put("carModel", user.getCarModel() == null ? "" : user
						.getCarModel());

				JSONObject xyJson = new JSONObject();
				xyJson.put("x", Double.parseDouble(user.getX()));
				xyJson.put("y", Double.parseDouble(user.getY()));
				results.remove("point");
				results.put("point", xyJson);

				results.put("speed", 0);
				results.put("angle", 0);
				results.put("online", 0);
				results.put("time", "");

				if (user.getIfDel() == null || user.getIfDel() != 1) { // 过滤掉删掉得
					results.remove("normal");
					jsonAryPos.put(results);
				}
			}

			rsJson.put("pos", jsonAryPos);
			rsJson.put("remove", jsonAryRemove);
			rsJson.put("edit", jsonAryEdit);
			rsJson.put("_t", _t);
		}
		if (StringUtils.isEmpty(time)) {//
			return jsonAryPos.toString();
		} else {
			return rsJson.toString();
		}
	}

	private boolean ifMove(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

//	@Override
//	public User getPointUser(String identifier, User user) throws JSONException {
//		String rs = socketMessageService.checkPoint(Integer
//				.parseInt(identifier));
//		JSONObject results = new JSONObject(rs);
//		JSONObject xyJson = (JSONObject) results.get("point");
//		if (!"0".equals(xyJson.get("x").toString())
//				&& !"0".equals(xyJson.get("y").toString())) {
//			user.setX(xyJson.get("x").toString());
//			user.setY(xyJson.get("y").toString());
//			return user;
//		} else {
//			return null;
//		}
//	}

	@Override
	public Page getPageList(Page page, Object[] values) throws Exception {
		StringBuffer hql = new StringBuffer("" +
		// "select
		// userId,userName,userPwd,companyName,userTel,address,email,visitIp" +
				// " createTime as createTime, userIp as userIp"+
				" from User l where 1=1 and l.ifDel=0 and l.level is null");
		if (values[0] != null && !values[0].equals("")) {
			hql.append(" and l.account.accountId=").append(values[0]);
		}
		if (values[1] != null && !values[1].equals("")) {
			hql.append(" and l.plate like '%").append(values[1]).append("%'");
		}
		if (values[2] != null && !values[2].equals("")) {
			hql.append(" and l.team.teamId=").append(values[2]);
		}
		if (values[3] != null && !values[3].equals("")) {
			hql.append(" and l.identifier like '%").append(values[3]).append(
					"%'");
		}
		if (values[4] != null && !"".equals(values[4])) {
			hql.append(" and l.team.teamId in (").append(values[4]).append(")");
		}
		hql.append(" order by l.createTime desc");
		return userDao.getPageList(page, hql.toString());
		// Page page2 = userDao.getPageList(page, hql.toString());
		// List list = page.getResult();
		// if(values[3]==null || values[3].equals("") || list.size()==0){
		// return page2;
		// }else{ //查询在线or离线列表
		// List newList = jedisUtils.getList(list,
		// Integer.valueOf(values[3].toString()));
		// page2.setResult(newList);
		// return page2;
		// }
	}

	@Override
	public List getOnlineList(Object[] values) throws Exception {

		StringBuffer hql = new StringBuffer(
				"select vehicleId as vehicleId, identifier as identifier,plate as plate from User l where 1=1 and l.ifDel=0 ");
		if (values[0] != null && !values[0].equals("")) {
			hql.append(" and l.account.accountId=").append(values[0]);
		}
		if (values[1] != null && !values[1].equals("")) {
			hql.append(" and l.plate like '%").append(values[1]).append("%'");
		}
		if (values[2] != null && !values[2].equals("")) {
			hql.append(" and l.team.teamId in (").append(values[2]).append(")");
		}
		// hql.append(" order by l.createTime desc");

		List list = userDao.getMapList(hql.toString());
		List newList = jedisService.getList(list, 1);
		return newList;
	}

	@Override
	public Page getRelayList(Page page, Object[] values) throws Exception {

		StringBuffer hql = new StringBuffer(""
				+ "from User l where 1=1 and l.ifDel=0 and l.ifRelay=1");
		if (values[0] != null && !values[0].equals("")) {
			hql.append(" and l.account.accountId=").append(values[0]);
		}
		if (values[1] != null && !values[1].equals("")) {
			hql.append(" and l.team.teamId in (").append(values[1]).append(")");
		}
		if (values[2] != null && !values[2].equals("")) {
			hql.append(" and l.plate like '%").append(values[2]).append("%'");
		}
		if (values[3] != null && !values[3].equals("")) {
			hql.append(" and l.identifier like '%").append(values[3]).append(
					"%'");
		}
		hql.append(" order by l.ifOff desc");

		// List newList = jedisService.getList(list,1);
		return userDao.getPageList(page, hql.toString());
	}

	@Override
	public boolean save(short userType, String userInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ifExist(String userName) throws Exception {
		String sql = "select * from user where user_name=?";
		List list = userDao.getListSql(sql, userName);
		if (list.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public List listBrand(Account account ,int  styleId) throws Exception {
		String sql = "select brand from user where team_id="+account.getTeam().getTeamId()+" and car_type = "+styleId;
		List list = userDao.getListSql(sql, null);
		return list;
	}

	/**
	 * @des socket发导航仪，不支持手机
	 * @param uids
	 * @param pointX
	 * @param pointY
	 * @param sendType
	 * @param type
	 * @param address
	 * @param desc
	 * @return
	 */
//	@Override
//	public boolean sendPoint(String uids, double pointX, double pointY,
//			String sendType, String type, String address, String desc) {
//		return socketMessageService.sendPoint(uids, pointX, pointY, sendType,
//				type, address, desc);
//	}
//
//	@Override
//	public boolean sendPoint(String uids, String content, String coordinate) {
//		MessageClient messageClient = SpringService.getBean("messageClient");
//		return messageClient.sendNaviMsg(uids, content, coordinate);
//	}

	@Override
	public User getByName(String identifier) throws Exception {
		String hql = " from User u where u.identifier=? and u.ifDel = 0";
		List list = userDao.getList(hql, new Object[] { identifier });
		if (list.size() > 0) {
			return (User) list.get(0);
		} else {
			return null;
		}
	}
	
	

//	@Override
//	public boolean sendMessage(String uids, String content) {
//		return socketMessageService.sendMessage(uids, content);
//	}

	@Override
	public boolean ifExist(String identifier, Integer accountId,
			String vehicleId) {
		String hql = " from User u where identifier=? and u.ifDel=0 "; // and
																		// u.account.accountId=?
		if (vehicleId != null) {
			hql += " and u.vehicleId <>" + vehicleId;
		}
		List list = userDao.getList(hql, new Object[] { identifier });// accountId
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean ifExistPlate(String plate, Integer companyId,
			String vehicleId) {
		String hql = " from User u where plate=? and u.ifDel=0 and u.account.accountId=?"; // and
																							// u.account.accountId=?
		if (vehicleId != null) {
			hql += " and u.vehicleId <>" + vehicleId;
		}
		List list = userDao.getList(hql, new Object[] { plate, companyId });//
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean acountCar(Integer accountId, Integer levelId) {
		String hql = " from User u where u.ifDel=0 and u.account.accountId=?";
		List list = userDao.getList(hql, new Object[] { accountId });
		Level level = levelDao.get(levelId);
		if (list.size() < level.getLimitNum()) { // max 200
			return false;
		} else {
			return true;
		}
	}

	public boolean ifIn(Double x, Double y) {

		return false;
	}

	@SuppressWarnings( { "unchecked" })
	public static List<Map<String, String>> JSONToList(String string) {
		try {

			JSONArray Data_jsonArray = new JSONArray(string);
			if (!Data_jsonArray.isNull(0)) {
				JSONObject jobj = Data_jsonArray.getJSONObject(0);
				@SuppressWarnings("rawtypes")
				ArrayList keys = new ArrayList();
				int keys_posi = 0;
				Iterator<String> IT = jobj.keys();
				while (IT.hasNext()) {
					keys.add(keys_posi, IT.next());
					// System.out.println(keys.get(keys_posi));
					keys_posi++;
				}

				List<Map<String, String>> list = new ArrayList<Map<String, String>>();

				for (int i = 0; i < Data_jsonArray.length(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					for (int j = 0; j < keys.size(); j++) {
						map.put(keys.get(j).toString(), Data_jsonArray
								.getJSONObject(i).getString(
										keys.get(j).toString()));
					}
					list.add(map);
				}
				return list;
			}
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean bindTeam(String ids, int accountId, int teamId)
			throws Exception {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for (String id : idsAry) {
			list.add(Integer.parseInt(id));
		}
		if (userDao.bindTeam(list, accountId, teamId)) {
			// synRedisTeamId(accountId,teamId);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean unBindTeam(String ids, int accountId, int teamId) {
		String[] idsAry = ids.split(",");
		List list = new ArrayList();
		for (String id : idsAry) {
			list.add(Integer.parseInt(id));
		}
		return userDao.unBindTeam(list, accountId, teamId);
	}

	@Override
	public boolean unBindTeamAll(int accountId, int teamId) {
		return userDao.unBindTeamAll(accountId, teamId);
	}

	@Override
	public boolean saveMonitor(Integer ifMonitor, String vehicleIds,
			int accountId) {
		String[] idsAry = vehicleIds.split(",");
		List list = new ArrayList();
		for (String id : idsAry) {
			list.add(Integer.parseInt(id));
		}
		return userDao.editIfMonitor(ifMonitor, list, accountId);
	}

	/**
	 * @des 同步uid到redis服务器
	 * @param companyId
	 */
	public void synRedis(int companyId) {
//		String hql = "from User l where l.account.accountId = ? and l.ifDel =0  order by vehicleId asc,l.team.teamId asc";
//		Object[] values = new Object[] { companyId };
//
//		String uids = null;
//		List<User> list = userDao.getList(hql, values);
//		if (list.size() > 0) {
//			for (int i = 0; i < list.size(); i++) {
//				User user = list.get(i);
//				if (i == 0) {
//					uids = user.getIdentifier();
//				} else {
//					uids = uids + "," + user.getIdentifier();
//				}
//			}
//			jedisService.synchronizeUid(String.valueOf(companyId), uids);
//		}
	}

	/**
	 * @des 同步uid到redis服务器
	 * @param companyId
	 */
	@Override
	public void synRedisTeamId(int companyId, int teamId) throws Exception {
//		String hql = "from User l where l.account.accountId = ? and l.team.teamId=? and l.ifDel =0  order by vehicleId asc";
//		Object[] values = new Object[] { companyId, teamId };
//
//		String uids = null;
//		List<User> list = userDao.getList(hql, values);
//		if (list.size() > 0) {
//			for (int i = 0; i < list.size(); i++) {
//				User user = list.get(i);
//				if (i == 0) {
//					uids = user.getIdentifier();
//				} else {
//					uids = uids + "," + user.getIdentifier();
//				}
//			}
//			jedisService.synchronizeUid(String.valueOf(companyId) + "_"
//					+ String.valueOf(teamId), uids);
//		}
	}

//	@Override
//	public String getJson(String accountId, String time, String pt0, String pt1)
//			throws Exception {
//		// HttpSession session = ServletActionContext.getRequest().getSession();
//		// Users user = (Users)session.getAttribute("user");
//		JSONObject rsJson = new JSONObject();
//		JSONArray jsonAryPos = new JSONArray();
//		JSONArray jsonAryRemove = new JSONArray();
//		JSONArray jsonAryEdit = new JSONArray();
//
//		Date lastTime;
//		if (!StringUtils.isEmpty(time)) {
//			lastTime = new Date(Long.parseLong(time));
//		} else {
//			lastTime = null;
//		}
//
//		String hql = "from User l where l.account.accountId = ? and l.ifDel =0  order by vehicleId asc";
//		Object[] values = new Object[] { Integer.parseInt(accountId) };
//		// String hql = "from User l where l.ifDel =0 order by vehicleId asc";
//		// Object [] values = new Object[]{};
//
//		String uids = null;
//		List<User> list = userDao.getList(hql, values);
//		long _t = System.currentTimeMillis();
//		System.out.println(_t);
//		if (list.size() > 0) {
//			for (int i = 0; i < list.size(); i++) {
//
//				User user = list.get(i);
//				if (user.getIfDel() != null && user.getIfDel() == 1) {
//					continue;
//				}
//				if (i == 0) {
//					uids = user.getIdentifier();
//				} else {
//					uids = uids + "," + user.getIdentifier();
//				}
//			}
//			if (uids != null) {
//				Map socketRsMap = socketMessageService.checkPoint(uids);// "10753,10644,11085,11788"
//				System.out.println(list.size());
//				String[] strAry = uids.split(",");
//				if (socketRsMap != null && socketRsMap.size() == strAry.length) {
//					if (StringUtils.isEmpty(time)) {// 第一次获取情况下
//						for (int i = 0; i < list.size(); i++) {
//							User user = list.get(i);
//							JSONObject results = (JSONObject) socketRsMap
//									.get(user.getIdentifier());
//							results.put("cid", user.getVehicleId());
//							results.put("identifier", user.getIdentifier());
//							results.put("plate", user.getPlate());
//							results.put("userName",
//									user.getUserName() == null ? "" : user
//											.getUserName());
//							results.put("tel", user.getTel() == null ? ""
//									: user.getTel());
//							results.put("carModel",
//									user.getCarModel() == null ? "" : user
//											.getCarModel());
//							if ("0".equals(results.get("normal").toString())) {// 第一次获取情况下，位置异常就获取数据库里的原始位置点
//								JSONObject xyJson = new JSONObject();
//								xyJson
//										.put("x", Double.parseDouble(user
//												.getX()));
//								xyJson
//										.put("y", Double.parseDouble(user
//												.getY()));
//								results.remove("point");
//								results.put("point", xyJson);
//							}
//							if (user.getIfDel() == null || user.getIfDel() != 1) { // 过滤掉删掉得
//								results.remove("normal");
//								jsonAryPos.put(results);
//							}
//						}
//						return jsonAryPos.toString();
//					} else {
//						for (int i = 0; i < list.size(); i++) {
//							User user = list.get(i);
//							if (user.getIfDel() != null && user.getIfDel() == 1) { // 同步未删除的
//								jsonAryRemove.put(user.getVehicleId());
//								continue;
//							}
//							JSONObject results = (JSONObject) socketRsMap
//									.get(user.getIdentifier());
//							results.put("cid", user.getVehicleId());
//							if (user.getUpTime() != null
//									&& user.getUpTime().compareTo(lastTime) >= 0
//									|| user.getCreateTime().compareTo(lastTime) >= 0) { // 同步未更新的
//								results.put("identifier", user.getIdentifier());
//								results.put("plate", user.getPlate());
//								results.put("userName",
//										user.getUserName() == null ? "" : user
//												.getUserName());
//								results.put("tel", user.getTel() == null ? ""
//										: user.getTel());
//								results.put("carModel",
//										user.getCarModel() == null ? "" : user
//												.getCarModel());
//								jsonAryEdit.put(results);
//							}
//							if (!"0".equals(results.get("normal").toString())) {// 非第一次获取情况下，位置异常的车忽略掉
//								jsonAryPos.put(results);
//							}
//							results.remove("normal");
//						}// end for
//					}
//
//					rsJson.put("pos", jsonAryPos);
//					rsJson.put("remove", jsonAryRemove);
//					rsJson.put("edit", jsonAryEdit);
//					rsJson.put("_t", _t);
//				} else {
//					return getJsonFromDb(accountId, time, pt0, pt1);
//				} // end if(socketRsMap!=null &&
//					// socketRsMap.size()==list.size())
//			} // end if(uids!=null)
//		}
//		if (StringUtils.isEmpty(time)) {//
//			return jsonAryPos.toString();
//		} else {
//			return rsJson.toString();
//		}
//	}

	@Override
	public Long getCount(Object[] values) throws Exception {
		String sql = "select count(*) from user u where u.team_id in("+values[0]+")  and u.if_del =0 and u.level is null";
		return userDao.countSQL(sql, null);
	}

	@Override
	public Object getUserId(Object[] values) {
		String hql = "select user_id from user where driver_id=? and u.if_del =0 and u.level is null";
		return userDao.findObject(hql, values[0]);
	}

	@Override
	public List getDriverDetailed(Object[] values) {
		String hql = "from User u where u.ifDel = 0 and u.driver.driverId="
				+ values[0];
		return userDao.getList(hql);
	}

	/**
	 * 判断断油断电密码是否正确 add by zhangxin 2014-10-14
	 */
	@Override
	public boolean checkOffPwd(String identifier, String pwd) {
		String hql = "from User u where u.ifDel = 0 and u.identifier = ? and u.cutpwd = ?";
		List list = userDao.getList(hql, new Object[] { identifier, pwd });
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否已经断油断电
	 */
	@Override
	public boolean checkIfOff(int identifier) {
		String hql = "from User u where u.ifDel = 0 and u.ifOff = 1 and and u.identifier = ? ";
		List list = userDao.getList(hql, new Object[] { identifier });
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置车断油断电的状态
	 */
	@Override
	public void setOff(int carId, int state) {
		String sql = "update user set if_off =" + state + " and user_id ="
				+ carId;
		userDao.excuteUpdateSql(sql);
	}

	@Override
	public List getList() {
		String hql = "from User u where u.ifDel = 0";
		List list = userDao.getList(hql, null);
		return list;
	}

	@Override
	public List<User> getUidByPlate(String plate) {
		// TODO Auto-generated method stub
		String hql = " from User u where u.plate='" + plate + "'";
		List<User> list = userDao.getList(hql, null);
		return list;
	}

	@Override
	public List<User> getUidById(String ids) {
		// TODO Auto-generated method stub
		
		String hql = " from User u where u.vehicleId in (" + ids + ")";
		List<User> list = userDao.getList(hql, null);
		return list;
	}

	@Override
	public String httpSave(String uiserid, String discc) {
		String param = "cmd=discc&userid=" + uiserid + "&discc=" + discc;
		String rec = crawlPost(httpURL, param, "utf-8");
		return rec;
	}

	@Override
	public Page getUsersPage(Page page, Object[] values) {
		// vehicleId":10561,"identifier":"12345678999","plate":"23363","type":8,"driver":null,"ifMonitor":null,"baseId":null,"ifRelay":0,"cutpwd":null,"ifOff":null,"discc":null
//		StringBuffer sql = new StringBuffer(
//				"" +
//				 "select
//				 userId,userName,userPwd,companyName,userTel,address,email,visitIp"
//				 +
//						 " createTime as createTime, userIp as userIp"+
		StringBuffer sql =  new StringBuffer("select u.user_id as vehicleId,u.identifier as identifier,u.plate as plate,u.if_monitor as ifMonitor,u.base_id as baseId ,u.if_relay as ifRelay,u.cut_pwd as cutpwd,u.if_off as ifOff,u.discc as discc,u.team_id as teamId,t.team_name as teamName  " +
				" ,u.car_type as typeId,u.brand as brand,u.start_price as startPrice,u.km_price as kmPrice,u.low_price as lowPrice,u.reg_time as regTime ,u.buy_time as buyTime,u.user_number as userNumber,u.tel as tel,u.iccid as iccid,u.online as online ,u.car_name as carModel,u.line_gps as lineGps, c.name as type  " +
				",d.driver_name as driverName,d.driver_tel as driverTel,u.driver_id as driverId from user as u " +
				" left join team as t on u.team_id=t.team_id " +
				" left join carstyle as c on c.id = u.car_type " +
				" left join driver as d on u.driver_id = d.driver_id where 1=1 and u.if_del=0 ");
//		if (values[0] != null && !values[0].equals("")) {
//			sql.append(" and u.account_id=").append(values[0]);
//		}
		if(values[6] != null && !"".equals(values[6])){
			if("0".equals(values[6])){
				sql.append(" and  u.level is null");	
			}else if("1".equals(values[6])){
				sql.append(" and  u.level = 1000");	
			}
		}
		if (values[1] != null && !values[1].equals("")) {
			
			sql.append(" and u.plate like '%").append(values[1]).append("%'");
		}
//		if (values[2] != null && !values[2].equals("")) {
//			sql.append(" and u.team_id in").append(values[2]).append(")");
//		}
		if (values[3] != null && !values[3].equals("")) {
			sql.append(" and u.identifier like '%").append(values[3]).append(
					"%'");
		}
		if (values[4] != null && !"".equals(values[4])) {
			if(values[0].equals(36)){
				sql.append(" and (u.team_id in (").append(values[4]).append(") or u.team_id is null)");	
			}else{
			sql.append(" and u.team_id in (").append(values[4]).append(")");
			}
		}
		
		if (values[5] != null && !"".equals(values[5])) {
			if(values[5].equals("teamName")){
				sql.append(" order by convert(t.team_name using gbk)  asc");
			}else if(values[5].equals("type")){
				sql.append(" order by convert(c.name using gbk)  asc");
			}else if(values[5].equals("brand")){
				sql.append(" order by convert(u.brand using gbk)  asc");	
			}else if(values[5].equals("regTime")){
				sql.append(" order by u.reg_time  desc");
			}else if((values[5].equals("buyTime"))){
				sql.append(" order by u.buy_time  desc");	
			}else if((values[5].equals("plate"))){
				sql.append(" order by convert(u.plate using gbk)  asc");	
			}else if((values[5].equals("online"))){
				sql.append(" order by u.online desc");	
			}else if((values[5].equals("driverTel"))){
				sql.append(" order by d.driver_tel desc");	
			}else if((values[5].equals("driverName"))){
				sql.append(" order by convert(d.driver_name using gbk)  asc");	
			}
		}
		else{
			sql.append(" order by u.user_id  desc");	
		}
		//sql.append(" order by convert(u.brand using gbk)  asc");
		return userDao.getPageList_x(page, sql.toString());
//		StringBuffer hql = new StringBuffer("" +
////				"select  userId,userName,userPwd,companyName,userTel,address,email,visitIp" +
////				" createTime as createTime, userIp as userIp"+
//				" from User l where 1=1 and l.ifDel=0 ");
////		if(values[0]!=null && !values[0].equals("")){
////			hql.append(" and l.account.accountId=").append(values[0]);
////		}
//		if(values[1]!=null && !values[1].equals("")){
//			hql.append(" and l.plate like '%").append(values[1]).append("%'");
//		}
////		if(values[2]!=null && !values[2].equals("")){
////			hql.append(" and l.team.teamId=").append(values[2]);
////		}
//		if(values[3]!=null && !values[3].equals("")){
//			hql.append(" and l.identifier like '%").append(values[3]).append("%'");
//		}
//		if(values[4]!= null && !"".equals(values[4])){
//			hql.append(" and l.team.teamId in (").append(values[4]).append(")");
//		}
//		hql.append(" order by l.createTime desc");
//		return userDao.getPageList(page, hql.toString());
	}
	
	@Override
	public List<User> getUserListToMap(String uids){
		String hql = " from User u where u.identifier in (" + uids + ")";
		List<User> listTem = userDao.getList(hql, null);
		return listTem;
	}
	
	public String getHttpURL() {
		return httpURL;
	}

	public void setHttpURL(String httpURL) {
		this.httpURL = httpURL;
	}
	

	public String crawlPost(String url,String para, String charset) {
		String content = "";
		BufferedReader reader= null;
		try{
			URLConnection connection = new URL(url).openConnection();
			connection.setConnectTimeout(connectionTimeOut);//
			connection.setReadTimeout(readTimeOut);//
			connection.setUseCaches(false);
				connection.setDoOutput(true);
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(),charset);
				out.write(para);
				out.flush();
				out.close();
			String sCurrentLine = "";
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
					charset == null ? this.charSet : charset));// 读取网页源代码，如果没有指定字符集，则使用默认的UTF-8
			while ((sCurrentLine = reader.readLine()) != null) {
				content += sCurrentLine;
			}
			System.out.println(content);
		} catch (IOException e) {
			if(reader!=null)
				try {
					reader.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				} finally {
					reader = null;
				}
		}finally{
			if(reader!=null)
				try {
					reader.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
		}
		return content;
	}

	@Override
	public List getListForExcel(Object[] values) {
		String sql ="select u.*,t.team_name as teamName,c.name as carStyle,d.driver_name as driverName,d.driver_tel as driverTel from user u left join Team t on u.team_id=t.team_id left join carstyle c on u.car_type=c.id left join driver as d on u.driver_id = d.driver_id  where 1=1 and u.if_del=0 ";
		if(values[0]!=null && !values[0].equals("")){
			sql+=" and u.plate like '%"+values[0]+"%'";
		}
		if(values[1]!=null && !values[1].equals("")){
			sql+=" and u.identifier like '%"+values[1]+"%'";
		}
		if(values[2]!= null && !"".equals(values[2])){
			sql+=" and u.team_id in ("+values[2]+")";
		}
		sql+=" order by u.create_time desc";
		return userDao.getListSql(sql);
	}

	@Override
	public List getListForReport(Object[] values) {
		String sql ="SELECT u.user_id as id,u.plate as plate ,SUM(ifnull(o.miles,0))/1000 as mile ,ROUND(SUM(IFNULL(o.cost,0)+IFNULL(o.glf,0)),2) as fee,SUM(UNIX_TIMESTAMP(o.over_time)-UNIX_TIMESTAMP(o.start_time))/3600 as time  from orders o LEFT JOIN user u on o.car_id=u.user_id  where 1=1 and u.if_del=0 and o.status=7 ";
//		if(values[0]!=null && !values[0].equals("")){
//			sql+=" and u.plate like '%"+values[0]+"%'";
//		}
//		if(values[1]!=null && !values[1].equals("")){
//			sql+=" and u.identifier like '%"+values[1]+"%'";
//		}
		if(values[0]!= null && !"".equals(values[0])){
			sql+=" and u.team_id in ("+values[0]+")";
		}
		if(values[1]!= null && !"".equals(values[1])){
			sql+=" and o.over_time >= '"+values[1]+"'";
		}
		if(values[2]!= null && !"".equals(values[2])){
			sql+=" and o.over_time <= '"+values[2]+"'";
		}
		sql+=" group by u.user_id,u.plate order by u.plate desc";
		return userDao.getListSql(sql);
	}

	@Override
	public List getListMonthForReport(Object[] values) {
		String sql ="SELECT DATE_FORMAT(o.over_time,'%Y%m') as mon,u.plate as obj ,u.user_id as obj_id,SUM(ifnull(o.miles,0))/1000 as mile ,ROUND(SUM(IFNULL(o.cost,0)+IFNULL(o.glf,0)),2) as fee,SUM(UNIX_TIMESTAMP(o.over_time)-UNIX_TIMESTAMP(o.start_time))/3600 as time  from orders o LEFT JOIN user u on o.car_id=u.user_id  where 1=1 and u.if_del=0 and o.status=7 ";
		if(values[0]!= null && !"".equals(values[0])){
			sql+=" and u.user_id ="+values[0];
		}
		if(values[1]!= null && !"".equals(values[1])){
			sql+=" and o.over_time >= '"+values[1]+"'";
		}
		if(values[2]!= null && !"".equals(values[2])){
			sql+=" and o.over_time <= '"+values[2]+"'";
		}
		sql+=" group by DATE_FORMAT(o.over_time,'%Y%m'),u.plate,u.user_id order by DATE_FORMAT(o.over_time,'%Y%m') desc";
		return userDao.getListSql(sql);
	}

	@Override
	public List getListMonthDetailForReport(Object[] values) {
		String sql ="SELECT u.plate as obj ,o.start_time,o.over_time,ifnull(o.miles,0)/1000 as mile ,ROUND(IFNULL(o.cost,0)+IFNULL(o.glf,0),2) as fee,(UNIX_TIMESTAMP(o.over_time)-UNIX_TIMESTAMP(o.start_time))/3600 as time  from orders o LEFT JOIN user u on o.car_id=u.user_id  where 1=1 and u.if_del=0 and o.status=7 ";
		if(values[0]!= null && !"".equals(values[0])){
			sql+=" and u.user_id ="+values[0];
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
		return userDao.getListSql(sql);
	}
}
