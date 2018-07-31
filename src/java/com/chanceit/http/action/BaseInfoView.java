package com.chanceit.http.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.framework.utils.StringUtil;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.BaseInfo;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.ICarBaseInfoService;
import com.chanceit.http.service.IOperatorTeamService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;
import com.chanceit.http.service.TeamService;

@Component("baseInfoView")
public class BaseInfoView extends BaseAction{
	
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("opTeamService")
	private IOperatorTeamService opTeamService;
	
	@Autowired
	@Qualifier("carBaseInfoService")
	private ICarBaseInfoService carBaseInfoService;
	/**
	 * 
	 * @return
	 */
	private String brandUrl;
	
	
	
	public String getBrandUrl() {
		return brandUrl;
	}

	public void setBrandUrl(String brandUrl) {
		this.brandUrl = brandUrl;
	}

	public String plateList(){
		String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
//			accountId = account.getAccountId().toString();
//		}
		String styleId = getParameter("styleId");
		String brand = getParameter("brand");
		String type = getParameter("type");
		String teamIds=account.getTeam().getTeamId()+"";
		if(account.getLevel().getLevelId() == 5){
			teamIds = teamService.getTeamIdStr(account);
		}
		Object[] keywords = new Object[]{teamIds,styleId,brand,type};
		List list = carBaseInfoService.getPlateList(keywords);
		return ResultManager.getBodyResult(list);
	}
	
	public String plateListAll(){
		String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
//			accountId = account.getAccountId().toString();
//		}
		String styleId = getParameter("styleId");
		String brand = getParameter("brand");
		String teamIds=account.getTeam().getTeamId()+"";
		Object[] keywords = new Object[]{teamIds,styleId,brand};
		List list = carBaseInfoService.getPlateListAll(keywords);
		return ResultManager.getBodyResult(list);
	}
	
	public String brandList() throws Exception{
		URL url = new URL(brandUrl);
		URLConnection con = url.openConnection();
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
		StringBuffer sb=new StringBuffer();
		
		while(reader1.read() != -1){
			sb.append(reader1.readLine());
		}
		String rec = new String("["+sb);
		JSONArray array = new JSONArray(rec);
		JSONArray result = new JSONArray();
		for(int i=0;i<array.length();i++){
			JSONObject object = array.getJSONObject(i);
			String brand = (String) object.get("brand");
			String mark = StringUtil.converterToFirstSpell(brand);
			JSONObject temp = new JSONObject();
			temp.put("brand", brand);
			temp.put("mark", mark);
			result.put(temp);
		}
		return result.toString();
	}
	
	
	public String saveBaseInfo() throws Exception{
		String baseId = getParameter("baseId");
		String weight = getParameter("weight");
		String total = getParameter("totalNumber");
		String prcTime = getParameter("prcTime");
		String limTime = getParameter("limTime");
		String userId = getParameter("userId");
		String brand = getParameter("brand");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isBlank(userId)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"请选择正确的车牌号");
		if(StringUtils.isBlank(prcTime)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"请输入生产时间");
		if(StringUtils.isBlank(limTime)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"请输入年审时间");
		User user = userService.get(Integer.parseInt(userId));
		if(StringUtils.isBlank(baseId)){ //add
			if(user.getBaseId() != null){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"车辆基本信息已经存在");
			}
			BaseInfo baseInfo = new BaseInfo();
			baseInfo.setWeight(Double.parseDouble(weight));
			baseInfo.setTotalNumber(Short.parseShort(total));
			baseInfo.setPrcTime(format.parse(prcTime));
			baseInfo.setLimTime(format.parse(limTime));
			baseInfo.setBrand(brand);
			baseInfo.setUser(user);
			carBaseInfoService.save(baseInfo);
			
			user.setBaseId(baseInfo.getBaseInfoId());
			userService.save(user);
			return ResultManager.getSuccResult();
		}else{
			BaseInfo baseInfo = carBaseInfoService.get(Integer.parseInt(baseId));
			if(baseInfo==null ) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"不存在此id");
			baseInfo.setWeight(Double.parseDouble(weight));
			baseInfo.setTotalNumber(Short.parseShort(total));
			baseInfo.setPrcTime(format.parse(prcTime));
			baseInfo.setLimTime(format.parse(limTime));
			baseInfo.setBrand(brand);
			baseInfo.setUser(user);
			carBaseInfoService.save(baseInfo);
			return ResultManager.getSuccResult();
		}
	}
	
	public String baseInfoPageList(Page page){
		String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
			accountId = account.getAccountId().toString();
//		}
		//操作员权限只获取自身有权限的车队ID
		Object[] keywords ;
		if(account.getRole().getRoleId() == 3){
			String teamIds = opTeamService.getTeamIdsByOperatorId(account.getAccountId() + "");
			keywords = new Object[]{accountId,teamIds};
		}else{
			keywords = new Object[]{accountId,null};
		}
		page = carBaseInfoService.getBaseInfo(page, keywords);
		return ResultManager.getBodyResult(page);
	}
	
	public String delBaseInfo() throws Exception{
		String baseId = getParameter("baseId");
		String userId = getParameter("userId");
		User user = userService.get(Integer.parseInt(userId));
		BaseInfo baseInfo = carBaseInfoService.get(Integer.parseInt(baseId));
		if(baseInfo != null && user != null){
			carBaseInfoService.delBaseInfo(baseInfo);
			user.setBaseId(null);
			userService.save(user);
			return ResultManager.getSuccResult();
		}
		return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"参数异常");
	}
}












