package com.chanceit.http.action;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.utils.MessageProducer;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.GhAction;
import com.chanceit.http.pojo.GhLineWarn;
import com.chanceit.http.pojo.GhPoints;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IGuanHuiService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;
import com.google.gson.Gson;
/**
 * @author Administrator
 *   百度路径的操作逻辑接口
 *   add by gwt 2015-01-05
 */
@Component("guanhuiView")
public class GuanHuiView extends BaseAction{
	@Autowired
	@Qualifier("guanhuiService")
	private IGuanHuiService guanhuiService;
	
	@Autowired
	@Qualifier("messageProducer")
	private MessageProducer messageProducer;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
	/**
	 * @return
	 * http发送
	 */
	public String guanghuiTeamcars() throws Exception{
	//String res =	guanhuiService.postData();
	String identifiers = null;
	String pid = getParameter("lineId");
	List<GhAction> list = guanhuiService.getByUidAndPid(Integer.parseInt(pid));
	for(int i=0;i<list.size();i++){
		if(i==0){
			identifiers=list.get(i).getUid();	
		}else{
			identifiers+=","+list.get(i).getUid();	
		}
	}
	Account account = null;
	account = getSessionAccount();
	if (account == null) {
		return ResultManager.getFaildResult(102, "您未登录系统或长时间未操作");
	}
	String accountId;
	if (account.getRole().getRoleId() != 2) {
		accountId = getCompanyId();
	} else {
		accountId = account.getAccountId().toString();
	}
	JSONObject json = teamService.getJson_x(account, Integer.parseInt(accountId),identifiers);
	return json.toString();
	} 
	
	/**
	 * @return
	 * 增加绑定数据
	 */
	public String save(){
		//insert ghaction(uid,pid) values("111",21),("222",4),("455",47)
		String userIds = getParameter("vehicleIds");
		String pid = getParameter("lineId");
		//List list = guanhuiService.getByUidAndPid(lineName,Integer.parseInt(pid));
		if(StringUtils.isBlank(userIds) || StringUtils.isBlank(pid)){
			return ResultManager.getFaildResult("参数错误");	
		}
		List<User> usersList = userService.getUidById(userIds);
		String sql = "insert ghaction(uid,pid) values";
		for(int i = 0;i<usersList.size();i++){
			String identifier = usersList.get(i).getIdentifier();
		    if(i==0){
		    	sql+= "('"+identifier+"',"+pid+")";
		    }else{
		    	sql+=",('"+identifier+"',"+pid+")";
		    }
		    JSONObject json = new JSONObject();
		    try {
				json.put("uid", identifier);
				json.put("pid", pid);
				json.put("type", "line");
				sendMsg("line",json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(usersList.size() > 0){
			guanhuiService.batchSave(sql);	
		}else{
			return ResultManager.getFaildResult("参数错误");	
		}
//		if(list==null || list.size() == 0){
//		GhAction ac = new GhAction();
//		ac.setPid(12);
//		ac.setUid("123");
//		guanhuiService.save(ac);
//		}else{
//			return ResultManager.getFaildResult("该车已经绑定相同的路劲");	
//		}
		return ResultManager.getSuccResult();
	}
	/**
	 * @return
	 * 增加规划路劲数据（2）
	 */
	public String savex(){
		try{
		String lineName = getParameter("lineName");
		String distance = getParameter("distance");
		String path = getParameter("path");
		String radius = getParameter("radius");
		String lineId = getParameter("lineId");
		Account account = null;
		account = getSessionAccount();
		if (account == null) {
			return ResultManager.getFaildResult(102, "您未登录系统或长时间未操作");
		}
		String accountId;
		if (account.getRole().getRoleId() != 2) {
			accountId = getCompanyId();
		} else {
			accountId = account.getAccountId().toString();
		}
		
		float r = 0;
		int id = 0;
		if(!StringUtils.isEmpty(radius)){
			r = Float.parseFloat(radius);
		}
		if(StringUtils.isBlank(lineName) || StringUtils.isBlank(path)){
			return ResultManager.getFaildResult("参数错误保存失败");
		}
		GhPoints gp = new GhPoints();
		gp.setLineName(lineName);
		gp.setPath(path);
		gp.setRadius(r);
		gp.setDistance(distance);
		gp.setCreateTime(new Date());
		gp.setAccountId(Integer.parseInt(accountId));
		gp.setIfDel(0);
		gp.setTeamId(account.getTeam().getTeamId());
		if(account.getRole().getRoleId() == 3){
			gp.setOpId(account.getAccountId());	
		}
		if(!StringUtils.isEmpty(lineId)){
			id = Integer.parseInt(lineId);
			gp.setLineId(id);
		}
		JSONObject json = new JSONObject();
		
		guanhuiService.save(gp);
		if(id == 0){
		 int line = guanhuiService.getMaxId();
		 if(line != 0){
			 json.put("id", line);
			 json.put("pts", path);
			 json.put("r", r);
			 json.put("type", "line");
			 sendMsg("line",json.toString()); 
		 }
		}else{
			 json.put("id", id);
			 json.put("pts", path);
			 json.put("r", r);
			 sendMsg("line",json.toString()); 
		}
		return ResultManager.getSuccResult();
		}catch(ConstraintViolationException e){System.out.println(e.getMessage());
			return ResultManager.getFaildResult("已经存在相同的路径名");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return ResultManager.getFaildResult("数据解析错误");
		}
	}
	/**
	 * @return
	 * 增加数据（3）
	 */
	public String savel(){
		GhLineWarn gp = new GhLineWarn();
		gp.setUid("aaa");
		gp.setLineid(1);
//		gp.setX(12);
//		gp.setY(12);
		guanhuiService.saveline(gp);
		//messageProducer.sendDataToCrQueue("gwtgwtwgt");
		return "ss";
	}
	
	/******
	 * 删除数据
	 * */
	public String del(){
		String lineId = getParameter("lineId");
		GhPoints gp = new GhPoints();
		int id = 0;
		if(!StringUtils.isEmpty(lineId)){
			id = Integer.parseInt(lineId);
			gp.setLineId(id);
		}else{
			return ResultManager.getFaildResult("参数错误，删除失败");	
		}
		//guanhuiService.updateDel(id);
		JSONObject json = new JSONObject();
		guanhuiService.delete(gp);
		guanhuiService.delete(id);
		try {
			json.put("id", id);
			json.put("delflag", 1);
			json.put("type", "line");
			sendMsg("line",json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultManager.getSuccResult();
	}
	
	public String getPlate(){
		return null;
	}
	
	/**
	 * @return
	 * 删除
	 */
	public String delRule(){
		String ruleId = getParameter("ruleId");
		GhAction ag = new GhAction();
		int id = 0;
		if(!StringUtils.isEmpty(ruleId)){
			id = Integer.parseInt(ruleId);
			ag.setId(id);
		}else{
			return ResultManager.getFaildResult("参数错误，删除失败");	
		}
		List<GhAction> ga = guanhuiService.getById(id);
		JSONObject json = new JSONObject();
		try {
		if(ga != null && ga.size()>0){
			json.put("uid", ga.get(0).getUid());
			json.put("pid", ga.get(0).getPid());
			json.put("delflag", 1);
			json.put("type", "line");
			sendMsg("line",json.toString());
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("错误" + e.getMessage());
			e.printStackTrace();
		}
		guanhuiService.delete(ag);
		return ResultManager.getSuccResult();
	}
	
	/**
	 * @return
	 * 增加数据
	 */
	public String sendMsg(String type,String str){
		String str0 = "{uid:111,pid:111}"; //绑定用户到某一条路劲  
		String str1 = "{uid:111,pid:111,delflag:1}"; // 取消绑定
		String str2 = "{id:111,pts:111,r:111}"; //划线后发送
		String str3 = "{id:111,delflag:1}";//删掉路劲发送
		messageProducer.sendDataToCrQueue(type,str);
		return "ss";
	}
	
	/**
	 * @return
	 * 获取规划路劲列表
	 */
	public String getLineList(){
		String accountId;
		Account account = null;
		account = getSessionAccount();
		boolean role = false;
		if (account.getRole().getRoleId() != 2) {
			if(account.getRole().getRoleId() == 3){
				role = true;
				accountId = account.getAccountId().toString();
			}else{
			accountId = getCompanyId();
			}
			//accountId = getCompanyId();
		} else {
			accountId = account.getAccountId().toString();
		}
		String teamIds = teamService.getTeamIdStr(account);
		List<GhPoints> list = guanhuiService.getLineList(Integer.parseInt(accountId),teamIds);
		Gson go = new Gson();
		String res = ResultManager.getBodyResult(list);
		return res;
	}
	
	/**
	 * @return
	 * 获取用户绑定路劲的列表
	 */
	public String getLineRuleList(){
		List list = guanhuiService.getLineRuleList();
		System.out.println(list.size());
		JSONArray array = new JSONArray();
		for(Iterator it = list.iterator();it.hasNext();){
			Object[] obj = (Object[]) it.next();
			JSONObject json = new JSONObject();
			try {
			json.put("identifier", obj[0]);
			json.put("lineId", obj[1]);
			json.put("ruleId", obj[2]);
			json.put("plate", obj[4]);
			array.put(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				logger.error("获取用户绑定路径列表错误" + e.getMessage());
				ResultManager.getFaildResult("数据解析错误");
			}
		}
		//String res = ResultManager.getBodyResult(list);
		return array.toString();
	}
	public String getLineWarn(Page page){
		String accountId;
		Account account = getSessionAccount();
		boolean role = false;
		if(account.getRole().getRoleId() != 2){
			if(account.getRole().getRoleId() == 3){
				role = true;
				accountId = account.getAccountId().toString();
			}else{
			accountId = getCompanyId();
			}
		}else{
			accountId = account.getAccountId().toString();
		}
		String teamIds = teamService.getTeamIdStr(account);
		page = guanhuiService.getWarnList(page, Integer.parseInt(accountId),teamIds);
		String s = ResultManager.getBodyResult(page);
		s=s.replaceAll("\\\\", "");
		return s;
	}
	
	public String updateIsread(){
		String id = getParameter("limitId");
		if(StringUtils.isBlank(id)){
			ResultManager.getFaildResult(3,"参数错误");
		}
		guanhuiService.update(Integer.parseInt(id));
		return ResultManager.getSuccResult();
	}
	
	public String getCount(){
		Account account = null;
		account = getSessionAccount();
		String accountId;
		boolean role = false;
		if(account.getRole().getRoleId() != 2){
			if(account.getRole().getRoleId() == 3){
				role = true;
				accountId = account.getAccountId().toString();
			}else{
			accountId = getCompanyId();
			}
		}else{
			accountId = account.getAccountId().toString();
		}
		String teamIds = teamService.getTeamIdStr(account);
		List list = guanhuiService.getCount(Integer.parseInt(accountId),teamIds);
		 JSONObject json = new JSONObject();
		if(list != null){
			 int num = list.size();
			 try {
				json.put("total", num);
				return json.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 
		return "{\"total\":0}";
	}
	public String getLimiteLine(){
		List list = guanhuiService.getLimitLine();
		JSONArray array = new JSONArray();
		for(Iterator it = list.iterator();it.hasNext();){
			Object[] obj = (Object[]) it.next();
			JSONObject json = new JSONObject();
			try {
			json.put("identifier", obj[0]);
			json.put("path", obj[1]);
			json.put("createTime", obj[2]);
			json.put("deltime", obj[3]);
			json.put("ifdel", obj[4]);
			array.put(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				logger.error("获取用户绑定路径列表错误" + e.getMessage());
				ResultManager.getFaildResult("数据解析错误");
			}
		}
		return array.toString();
	}
	
	
	/***             *区域报警*               **********/
	
	public String saveCircle() {
		// TODO Auto-generated method stub
		try{
			String lineName = getParameter("areaName");
			String area = getParameter("area");
			String areaId = getParameter("areaId");
			Account account = null;
			account = getSessionAccount();
			if (account == null) {
				return ResultManager.getFaildResult(102, "您未登录系统或长时间未操作");
			}
			String accountId;
			if (account.getRole().getRoleId() != 2) {
				accountId = getCompanyId();
			} else {
				accountId = account.getAccountId().toString();
			}
			
			if(StringUtils.isBlank(lineName) || StringUtils.isBlank(area)){
				return ResultManager.getFaildResult("参数错误保存失败");
			}
			float r = 0;
			int id = 0;
			String[] areas = area.split(",");
			if(!StringUtils.isEmpty(areas[2])){
				r = Float.parseFloat(areas[2]);
			}
			GhPoints gp = new GhPoints();
			gp.setLineName(lineName);
			gp.setPath(area);
			gp.setRadius(r);
			gp.setCreateTime(new Date());
			gp.setAccountId(Integer.parseInt(accountId));
			gp.setIfDel(0);
			gp.setWs("circle");
			gp.setTeamId(account.getTeam().getTeamId());
			if(account.getRole().getRoleId() == 3){
				gp.setOpId(account.getAccountId());	
			}
			if(!StringUtils.isEmpty(areaId)){
				id = Integer.parseInt(areaId);
				gp.setLineId(id);
			}
			JSONObject json = new JSONObject();
			
			guanhuiService.save(gp);
			if(id == 0){
			 int line = guanhuiService.getMaxId();
			 if(line != 0){
				 json.put("id", line);
				 json.put("pts", areas[0]+","+areas[1]);
				 json.put("r", r);
				 json.put("type", "circle");
				 sendMsg("line",json.toString()); 
			 }
			}else{
				 json.put("id", id);
				 json.put("pts",  areas[0]+","+areas[1]);
				 json.put("r", r);
				 json.put("type", "circle");
				 sendMsg("line",json.toString()); 
			}
			return ResultManager.getSuccResult();
			}catch(ConstraintViolationException e){System.out.println(e.getMessage());
				return ResultManager.getFaildResult("已经存在相同的路径名");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return ResultManager.getFaildResult("数据解析错误");
			}
	}

	public String getCircles() {
		// TODO Auto-generated method stub
		
		try {
			User obj =  userService.getByName("153206344");
			Team t = teamService.get(obj.getTeam().getTeamId());
			if(t.getOperator() == null){
				System.out.println("a");
			}else{
				System.out.println("b");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//User car = userService.getByName(uid);
//		Point point = converter.wgs2bd(xx,yy);
//		int account_id = 0;
//		if(team.getOperator()!=null){
//			account_id = team.getOperator().getAccountId();
//		}else{
//			account_id = team.getAccount().getAccountId();
//		}
		String accountId;
		Account account = null;
		account = getSessionAccount();
		boolean role = false;
		if (account.getRole().getRoleId() != 2) {
			if(account.getRole().getRoleId() == 3){
				role = true;
				accountId = account.getAccountId().toString();
			}else{
			accountId = getCompanyId();
			}
		} else {
			
			accountId = account.getAccountId().toString();
		}
		String teamIds = teamService.getTeamIdStr(account);
		
		List<GhPoints> list = guanhuiService.getCircleList(Integer.parseInt(accountId),"circle",teamIds);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			GhPoints gh = list.get(i);
			
			JSONObject json = new JSONObject();
			try {
			json.put("areaId", gh.getLineId());
			json.put("areaName", gh.getLineName());
			json.put("createTime", gh.getCreateTime());
			String path = gh.getPath();
			json.put("area", path+","+gh.getRadius());
			array.put(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		Gson go = new Gson();
//		String res = ResultManager.getBodyResult(list);
		return array.toString();
	}

	public String delCircle() {
		// TODO Auto-generated method stub
		String lineId = getParameter("areaId");
		GhPoints gp = new GhPoints();
		int id = 0;
		if(!StringUtils.isEmpty(lineId)){
			id = Integer.parseInt(lineId);
			gp.setLineId(id);
		}else{
			return ResultManager.getFaildResult("参数错误，删除失败");	
		}
		//guanhuiService.updateDel(id);
		JSONObject json = new JSONObject();
		guanhuiService.delete(gp);
		guanhuiService.delete(id);
		try {
			json.put("id", id);
			json.put("delflag", 1);
			json.put("type", "circle");
			sendMsg("line",json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultManager.getSuccResult();
	}

	public String saveRule0() {
		// TODO Auto-generated method stub
		String userIds = getParameter("vehicleIds");
		String pid = getParameter("areaId");
		//List list = guanhuiService.getByUidAndPid(lineName,Integer.parseInt(pid));
		if(StringUtils.isBlank(userIds) || StringUtils.isBlank(pid)){
			return ResultManager.getFaildResult("参数错误");	
		}
		List<User> usersList = userService.getUidById(userIds);
		String sql = "insert ghaction(uid,pid) values";
		for(int i = 0;i<usersList.size();i++){
			String identifier = usersList.get(i).getIdentifier();
		    if(i==0){
		    	sql+= "('"+identifier+"',"+pid+")";
		    }else{
		    	sql+=",('"+identifier+"',"+pid+")";
		    }
		    JSONObject json = new JSONObject();
		    try {
				json.put("uid", identifier);
				json.put("pid", pid);
				json.put("type", "circle");
				sendMsg("line",json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(usersList.size() > 0){
			guanhuiService.batchSave(sql);	
		}else{
			return ResultManager.getFaildResult("参数错误");	
		}
//		if(list==null || list.size() == 0){
//		GhAction ac = new GhAction();
//		ac.setPid(12);
//		ac.setUid("123");
//		guanhuiService.save(ac);
//		}else{
//			return ResultManager.getFaildResult("该车已经绑定相同的路劲");	
//		}
		return ResultManager.getSuccResult();
	}

	public String getCircleRule() {
		// TODO Auto-generated method stub
		List list = guanhuiService.getCircleRuleList("circle");
		JSONArray array = new JSONArray();
		for(Iterator it = list.iterator();it.hasNext();){
			Object[] obj = (Object[]) it.next();
			JSONObject json = new JSONObject();
			try {
			json.put("identifier", obj[0]);
			json.put("areaId", obj[1]);
			json.put("ruleId", obj[2]);
			json.put("plate", obj[4]);
			array.put(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				logger.error("获取用户绑定路径列表错误" + e.getMessage());
				ResultManager.getFaildResult("数据解析错误");
			}
		}
		//String res = ResultManager.getBodyResult(list);
		return array.toString();
	}

	public String delRule0() {
		// TODO Auto-generated method stub
		String ruleId = getParameter("ruleId");
		GhAction ag = new GhAction();
		int id = 0;
		if(!StringUtils.isEmpty(ruleId)){
			id = Integer.parseInt(ruleId);
			ag.setId(id);
		}else{
			return ResultManager.getFaildResult("参数错误，删除失败");	
		}
		List<GhAction> ga = guanhuiService.getById(id);
		JSONObject json = new JSONObject();
		try {
		if(ga != null && ga.size()>0){
			json.put("uid", ga.get(0).getUid());
			json.put("pid", ga.get(0).getPid());
			json.put("delflag", 1);
			json.put("type", "circle");
			sendMsg("line",json.toString());
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("错误" + e.getMessage());
			e.printStackTrace();
		}
		guanhuiService.delete(ag);
		return ResultManager.getSuccResult();
	}
	public String getPoylgon() {
		// TODO Auto-generated method stub
		String accountId;
		Account account = null;
		account = getSessionAccount();
		boolean role = false;
		if (account.getRole().getRoleId() != 2) {
			if(account.getRole().getRoleId() == 3){
				role = true;
				accountId = account.getAccountId().toString();
			}else{
				
			accountId = getCompanyId();
			}
		} else {
			accountId = account.getAccountId().toString();
		}
		String teamIds = teamService.getTeamIdStr(account);
		List<GhPoints> list = guanhuiService.getCircleList(Integer.parseInt(accountId),"polygon",teamIds);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			GhPoints gh = list.get(i);
			
			JSONObject json = new JSONObject();
			try {
			json.put("areaId", gh.getLineId());
			json.put("areaName", gh.getLineName());
			json.put("createTime", gh.getCreateTime());
			String path = gh.getPath();
			json.put("area", path);
			array.put(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		Gson go = new Gson();
//		String res = ResultManager.getBodyResult(list);
		return array.toString();
	}

	public String getPolygonRules() {
		// TODO Auto-generated method stub
		List list = guanhuiService.getCircleRuleList("polygon");
		JSONArray array = new JSONArray();
		for(Iterator it = list.iterator();it.hasNext();){
			Object[] obj = (Object[]) it.next();
			JSONObject json = new JSONObject();
			try {
			json.put("identifier", obj[0]);
			json.put("areaId", obj[1]);
			json.put("ruleId", obj[2]);
			json.put("plate", obj[4]);
			array.put(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				logger.error("获取用户绑定路径列表错误" + e.getMessage());
				ResultManager.getFaildResult("数据解析错误");
			}
		}
		//String res = ResultManager.getBodyResult(list);
		return array.toString();
	}

	public String savePolygon() {
		// TODO Auto-generated method stub
		try{
			String lineName = getParameter("areaName");
			String area = getParameter("area");
			String areaId = getParameter("areaId");
			Account account = null;
			account = getSessionAccount();
			if (account == null) {
				return ResultManager.getFaildResult(102, "您未登录系统或长时间未操作");
			}
			String accountId;
			if (account.getRole().getRoleId() != 2) {
				accountId = getCompanyId();
			} else {
				accountId = account.getAccountId().toString();
			}
			
			if(StringUtils.isBlank(lineName) || StringUtils.isBlank(area)){
				return ResultManager.getFaildResult("参数错误保存失败");
			}
			float r = 0;
			int id = 0;
			GhPoints gp = new GhPoints();
			gp.setLineName(lineName);
			gp.setPath(area);
			gp.setRadius(r);
			gp.setCreateTime(new Date());
			gp.setAccountId(Integer.parseInt(accountId));
			gp.setIfDel(0);
			gp.setWs("polygon");
			gp.setTeamId(account.getTeam().getTeamId());
			if(account.getRole().getRoleId() == 3){
				gp.setOpId(account.getAccountId());	
			}
			if(!StringUtils.isEmpty(areaId)){
				id = Integer.parseInt(areaId);
				gp.setLineId(id);
			}
			JSONObject json = new JSONObject();
			
			guanhuiService.save(gp);
			if(id == 0){
			 int line = guanhuiService.getMaxId();
			 if(line != 0){
				 json.put("id", line);
				 json.put("pts", area);
				 json.put("r", r);
				 json.put("type", "polygon");
				 sendMsg("line",json.toString()); 
			 }
			}else{
				 json.put("id", id);
				 json.put("pts",  area);
				 json.put("r", r);
				 json.put("type", "polygon");
				 sendMsg("line",json.toString()); 
			}
			return ResultManager.getSuccResult();
			}catch(ConstraintViolationException e){System.out.println(e.getMessage());
				return ResultManager.getFaildResult("已经存在相同的路径名");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return ResultManager.getFaildResult("数据解析错误");
			}
	}

	public String delPolygon() {
		// TODO Auto-generated method stub
		String lineId = getParameter("areaId");
		GhPoints gp = new GhPoints();
		int id = 0;
		if(!StringUtils.isEmpty(lineId)){
			id = Integer.parseInt(lineId);
			gp.setLineId(id);
		}else{
			return ResultManager.getFaildResult("参数错误，删除失败");	
		}
		//guanhuiService.updateDel(id);
		JSONObject json = new JSONObject();
		guanhuiService.delete(gp);
		guanhuiService.delete(id);
		try {
			json.put("id", id);
			json.put("delflag", 1);
			json.put("type", "polygon");
			sendMsg("line",json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultManager.getSuccResult();
	}

	public String saveRules1() {
		// TODO Auto-generated method stub
		String userIds = getParameter("vehicleIds");
		String pid = getParameter("areaId");
		//List list = guanhuiService.getByUidAndPid(lineName,Integer.parseInt(pid));
		if(StringUtils.isBlank(userIds) || StringUtils.isBlank(pid)){
			return ResultManager.getFaildResult("参数错误");	
		}
		List<User> usersList = userService.getUidById(userIds);
		String sql = "insert ghaction(uid,pid) values";
		for(int i = 0;i<usersList.size();i++){
			String identifier = usersList.get(i).getIdentifier();
		    if(i==0){
		    	sql+= "('"+identifier+"',"+pid+")";
		    }else{
		    	sql+=",('"+identifier+"',"+pid+")";
		    }
		    JSONObject json = new JSONObject();
		    try {
				json.put("uid", identifier);
				json.put("pid", pid);
				json.put("type", "polygon");
				sendMsg("line",json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(usersList.size() > 0){
			guanhuiService.batchSave(sql);	
		}else{
			return ResultManager.getFaildResult("参数错误");	
		}
//		if(list==null || list.size() == 0){
//		GhAction ac = new GhAction();
//		ac.setPid(12);
//		ac.setUid("123");
//		guanhuiService.save(ac);
//		}else{
//			return ResultManager.getFaildResult("该车已经绑定相同的路劲");	
//		}
		return ResultManager.getSuccResult();
	}
	
	public String delRule1() {
		// TODO Auto-generated method stub
		String ruleId = getParameter("ruleId");
		GhAction ag = new GhAction();
		int id = 0;
		if(!StringUtils.isEmpty(ruleId)){
			id = Integer.parseInt(ruleId);
			ag.setId(id);
		}else{
			return ResultManager.getFaildResult("参数错误，删除失败");	
		}
		List<GhAction> ga = guanhuiService.getById(id);
		JSONObject json = new JSONObject();
		try {
		if(ga != null && ga.size()>0){
			json.put("uid", ga.get(0).getUid());
			json.put("pid", ga.get(0).getPid());
			json.put("delflag", 1);
			json.put("type", "polygon");
			sendMsg("line",json.toString());
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("错误" + e.getMessage());
			e.printStackTrace();
		}
		guanhuiService.delete(ag);
		return ResultManager.getSuccResult();
	}
	
	public String test(){
		JSONObject json = new JSONObject();
		 try {
			 json.put("id", 3);
			 json.put("pts", "116.397945,39.934412");
			 json.put("r", 10);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //sendMsg("line",json.toString()); 
		 JSONObject json1 = new JSONObject();
		    try {
				json1.put("uid", "209828412");
				json1.put("pid", 3);
				//sendMsg("line",json1.toString()); 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "asd";
	}

	
}
