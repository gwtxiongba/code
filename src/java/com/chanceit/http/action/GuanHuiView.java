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
 *   �ٶ�·���Ĳ����߼��ӿ�
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
	 * http����
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
		return ResultManager.getFaildResult(102, "��δ��¼ϵͳ��ʱ��δ����");
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
	 * ���Ӱ�����
	 */
	public String save(){
		//insert ghaction(uid,pid) values("111",21),("222",4),("455",47)
		String userIds = getParameter("vehicleIds");
		String pid = getParameter("lineId");
		//List list = guanhuiService.getByUidAndPid(lineName,Integer.parseInt(pid));
		if(StringUtils.isBlank(userIds) || StringUtils.isBlank(pid)){
			return ResultManager.getFaildResult("��������");	
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
			return ResultManager.getFaildResult("��������");	
		}
//		if(list==null || list.size() == 0){
//		GhAction ac = new GhAction();
//		ac.setPid(12);
//		ac.setUid("123");
//		guanhuiService.save(ac);
//		}else{
//			return ResultManager.getFaildResult("�ó��Ѿ�����ͬ��·��");	
//		}
		return ResultManager.getSuccResult();
	}
	/**
	 * @return
	 * ���ӹ滮·�����ݣ�2��
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
			return ResultManager.getFaildResult(102, "��δ��¼ϵͳ��ʱ��δ����");
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
			return ResultManager.getFaildResult("�������󱣴�ʧ��");
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
			return ResultManager.getFaildResult("�Ѿ�������ͬ��·����");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return ResultManager.getFaildResult("���ݽ�������");
		}
	}
	/**
	 * @return
	 * �������ݣ�3��
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
	 * ɾ������
	 * */
	public String del(){
		String lineId = getParameter("lineId");
		GhPoints gp = new GhPoints();
		int id = 0;
		if(!StringUtils.isEmpty(lineId)){
			id = Integer.parseInt(lineId);
			gp.setLineId(id);
		}else{
			return ResultManager.getFaildResult("��������ɾ��ʧ��");	
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
	 * ɾ��
	 */
	public String delRule(){
		String ruleId = getParameter("ruleId");
		GhAction ag = new GhAction();
		int id = 0;
		if(!StringUtils.isEmpty(ruleId)){
			id = Integer.parseInt(ruleId);
			ag.setId(id);
		}else{
			return ResultManager.getFaildResult("��������ɾ��ʧ��");	
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
			logger.error("����" + e.getMessage());
			e.printStackTrace();
		}
		guanhuiService.delete(ag);
		return ResultManager.getSuccResult();
	}
	
	/**
	 * @return
	 * ��������
	 */
	public String sendMsg(String type,String str){
		String str0 = "{uid:111,pid:111}"; //���û���ĳһ��·��  
		String str1 = "{uid:111,pid:111,delflag:1}"; // ȡ����
		String str2 = "{id:111,pts:111,r:111}"; //���ߺ���
		String str3 = "{id:111,delflag:1}";//ɾ��·������
		messageProducer.sendDataToCrQueue(type,str);
		return "ss";
	}
	
	/**
	 * @return
	 * ��ȡ�滮·���б�
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
	 * ��ȡ�û���·�����б�
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
				logger.error("��ȡ�û���·���б����" + e.getMessage());
				ResultManager.getFaildResult("���ݽ�������");
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
			ResultManager.getFaildResult(3,"��������");
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
				logger.error("��ȡ�û���·���б����" + e.getMessage());
				ResultManager.getFaildResult("���ݽ�������");
			}
		}
		return array.toString();
	}
	
	
	/***             *���򱨾�*               **********/
	
	public String saveCircle() {
		// TODO Auto-generated method stub
		try{
			String lineName = getParameter("areaName");
			String area = getParameter("area");
			String areaId = getParameter("areaId");
			Account account = null;
			account = getSessionAccount();
			if (account == null) {
				return ResultManager.getFaildResult(102, "��δ��¼ϵͳ��ʱ��δ����");
			}
			String accountId;
			if (account.getRole().getRoleId() != 2) {
				accountId = getCompanyId();
			} else {
				accountId = account.getAccountId().toString();
			}
			
			if(StringUtils.isBlank(lineName) || StringUtils.isBlank(area)){
				return ResultManager.getFaildResult("�������󱣴�ʧ��");
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
				return ResultManager.getFaildResult("�Ѿ�������ͬ��·����");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return ResultManager.getFaildResult("���ݽ�������");
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
			return ResultManager.getFaildResult("��������ɾ��ʧ��");	
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
			return ResultManager.getFaildResult("��������");	
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
			return ResultManager.getFaildResult("��������");	
		}
//		if(list==null || list.size() == 0){
//		GhAction ac = new GhAction();
//		ac.setPid(12);
//		ac.setUid("123");
//		guanhuiService.save(ac);
//		}else{
//			return ResultManager.getFaildResult("�ó��Ѿ�����ͬ��·��");	
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
				logger.error("��ȡ�û���·���б����" + e.getMessage());
				ResultManager.getFaildResult("���ݽ�������");
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
			return ResultManager.getFaildResult("��������ɾ��ʧ��");	
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
			logger.error("����" + e.getMessage());
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
				logger.error("��ȡ�û���·���б����" + e.getMessage());
				ResultManager.getFaildResult("���ݽ�������");
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
				return ResultManager.getFaildResult(102, "��δ��¼ϵͳ��ʱ��δ����");
			}
			String accountId;
			if (account.getRole().getRoleId() != 2) {
				accountId = getCompanyId();
			} else {
				accountId = account.getAccountId().toString();
			}
			
			if(StringUtils.isBlank(lineName) || StringUtils.isBlank(area)){
				return ResultManager.getFaildResult("�������󱣴�ʧ��");
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
				return ResultManager.getFaildResult("�Ѿ�������ͬ��·����");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return ResultManager.getFaildResult("���ݽ�������");
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
			return ResultManager.getFaildResult("��������ɾ��ʧ��");	
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
			return ResultManager.getFaildResult("��������");	
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
			return ResultManager.getFaildResult("��������");	
		}
//		if(list==null || list.size() == 0){
//		GhAction ac = new GhAction();
//		ac.setPid(12);
//		ac.setUid("123");
//		guanhuiService.save(ac);
//		}else{
//			return ResultManager.getFaildResult("�ó��Ѿ�����ͬ��·��");	
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
			return ResultManager.getFaildResult("��������ɾ��ʧ��");	
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
			logger.error("����" + e.getMessage());
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
