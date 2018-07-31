/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.JPushUtils;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.framework.utils.StringUtil;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Driver;
import com.chanceit.http.pojo.Member;
import com.chanceit.http.pojo.Order;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IDriverService;
import com.chanceit.http.service.IMemberService;
import com.chanceit.http.service.IOperatorTeamService;
import com.chanceit.http.service.IOrderService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;

/**
 * @ClassName OrderView
 * @author Administrator
 * @date 
 * @Description 订单管理相关接口
 */
@Component("orderView")
public class OrderView extends BaseAction {
	
	@Autowired
	@Qualifier("orderService")
	private IOrderService orderService;
	
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
	@Autowired
	@Qualifier("jpushUtils")
	private JPushUtils jpushUtils;
	
	@Autowired
	@Qualifier("memberService")
	private IMemberService memberService;
	@Autowired
	@Qualifier("driverService")
	private IDriverService driverService;
	
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	public String list(Page page){
		String status=getParameter("status");
		String ss_name=getParameter("ss_name");//搜索：乘车人姓名
		String ss_tel=getParameter("ss_tel"); //搜索：联系电话
		Account account = getSessionAccount();
		String teamIds = "";
		String deptId = "";
		int flag = 0;
		int st = Integer.parseInt(status);
		if( account.getLevel().getLevelId()==3){
			teamIds = account.getTeam().getTeamId()+"";
			 flag = 1;
			 if(st == 0){
					status = "0,1,2,3";
				}else if(st == 1){
					status = "7,-1,-2,-3,-4,-5";
				}else{
					status = "4,5,6";
				}
			//list=orderService.getOrdersByTeamId(teamIds, null);
		}else if(account.getLevel().getLevelId()<=2){
			teamIds = account.getTeam().getTeamId()+"";
			 flag = 1;
			 if(st == 0){
					status = "0,3";
				}else if(st == 1){
					status = "7,-1,-2,-3,-4,-5";
				}else{
					status = "1,2,4,5,6";
				}
			//list=orderService.getOrdersByTeamId(account.getTeam().getTeamId()+"", null);
		}else if(account.getLevel().getLevelId()==5){
			teamIds = teamService.getTeamIdStr(account);
			 flag = 1;
			 if(st == 0){
					status = "0,1,2,3";
				}else if(st == 1){
					status = "7,-1,-2,-3,-4,-5";
				}else{
					status = "4,5,6";
				}
			
		}else{
			//list=orderService.getOrdersByTeamId("", null);
			 flag = 0;
			 teamIds = account.getTeam().getTeamId()+"";
			 deptId = account.getDept().getDeptId()+"";
			 if(st == 0){
					status = "0";
				}else if(st == 1){
					status = "7,-1,-2,-3,-4,-5";
				}else{
					status = "1,2,3,4,5,6";
				}
		}
		//String teamIds=teamService.getTeamIdStr(account);
		
		
		
//		if(st == 0){
//			status = "0";
//		}else if(st == 1){
//			status = "7,-1,-2,-3,-4,-5";
//		}else{
//			status = "1,2,3,4,5,6";
//		}
		Object[] keywords = new Object[]{teamIds,status,deptId,ss_name,ss_tel};
		page = orderService.getPageList(page, keywords,flag);
		//System.out.println("aaa:"+ResultManager.getBodyResult(page));
		return ResultManager.getBodyResult(page);
	}
	
	public String addOrder() throws Exception{
		String beginTime = getParameter("dateTime0")+":00";
		String endTime = getParameter("dateTime1")+":00"; 
		String car_user_id = getParameter("car_user_id");
		String car_user_name = getParameter("car_user_name");
		String car_user_tel = getParameter("tel");
		String number = getParameter("number"); //乘车人数
		String takes = getParameter("takes");//货物
		String use_reason = getParameter("use_reason");//用车事由
		String remark = getParameter("remark"); //备注
		String caruses = getParameter("caruses"); //
		String adress0 = getParameter("adress0");//起始地址
		String adress1 = getParameter("adress1");//结束地址
		String carStyleId = getParameter("carStyleId");//结束地址
		String pos0 = getParameter("pos0");//起始坐标
		String pos1 = getParameter("pos1");//结束坐标
		String pos0_x=pos0.split(",")[0];
		String pos0_y=pos0.split(",")[1];
		String pos1_x=pos1.split(",")[0];
		String pos1_y=pos1.split(",")[1];
		
		
		String brand = getParameter("brand");
		String carId = getParameter("carId");
		String driverId = getParameter("driverId");
//		if(StringUtils.isBlank(driverName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"请输入司机名称");
		String accountId;
		Account account = getSessionAccount();
		int order_user_id=account.getAccountId();
		String order_user_name=account.getAccountName();
		String order_user_tel=account.getAccountTel();
		
			Order order=new Order();
			if(account.getLevel().getLevelId() ==1){
			order.setStatus(1);
			}else if(account.getLevel().getLevelId() ==2){
				order.setStatus(2);	
			}else if(account.getLevel().getLevelId() ==3 || account.getLevel().getLevelId() ==5){
				order.setStatus(4);	
			}else if(account.getLevel().getLevelId() ==4){
				order.setStatus(3);	
			}
			order.setOrderNum(String.valueOf(new Date().getTime()));
			order.setOrderUserId(order_user_id);
			order.setOrderUserName(order_user_name);
			order.setOrderUserTel(order_user_tel);
			order.setCarUserId(Integer.parseInt(car_user_id));
			order.setCarUserName(car_user_name);
			order.setCarUserTel(car_user_tel);
			order.setBeginTime(DateUtil.getDate(beginTime));
			order.setEndTime(DateUtil.getDate(endTime));
			order.setRemark(remark);
			order.setBeginAddr(adress0);
			order.setEndAddr(adress1);
			order.setBeginLat(Double.parseDouble(pos0_x));
			order.setBeginLng(Double.parseDouble(pos0_y));
			order.setEndLat(Double.parseDouble(pos1_x));
			order.setEndLng(Double.parseDouble(pos1_y));
			if(!StringUtils.isBlank(carId)){
			order.setCarId(Integer.parseInt(carId));
			User u = userService.get(Integer.parseInt(carId));
			u.setOnline((short)2);
			userService.update(u);
			}
			if(!StringUtils.isBlank(driverId)){
			order.setDriverId(Integer.parseInt(driverId));
			}
			if(!StringUtils.isBlank(carStyleId)){
			order.setCarStyleId(Integer.parseInt(carStyleId));
			}
			
			if(!StringUtils.isBlank(caruses)){
				order.setCause(Integer.parseInt(caruses));
				}
			order.setTakes(takes);
			order.setPnumber(Integer.parseInt(number));
			order.setCreateTime(new Date());
//			driver.setCreateTime(new Date());
//			driver.setAccount(accountService.get(Integer.parseInt(accountId)));//***注意 操作员可添加
//			DriverId = driverService.save(driver);
			orderService.save(order);
			return ResultManager.getSuccResult();
			 
	}
	
	public String editOrder(){
		String orderId = getParameter("orderId");
		String reason = getParameter("reason");
		String carId = getParameter("carId");
		String driverId = getParameter("driverId");
		String status = getParameter("status");
		Account account = getSessionAccount();
		Order order = orderService.get(Integer.parseInt(orderId));
		if(Integer.parseInt(status) == -1){
		 	order.setReason(reason);
		 	if(account.getLevel().getLevelId()==3 || account.getLevel().getLevelId() ==5){
		 		order.setStatus(-4);
		 	}else if(account.getLevel().getLevelId() == 1){
		 		order.setStatus(-1);
		 	}else if(account.getLevel().getLevelId() == 2){
		 		order.setStatus(-2);
		 	}else{
		 		order.setStatus(-3);
		 	}
		 	
		}else{
			order.setReason(reason);
		 	if(account.getLevel().getLevelId()==3 || account.getLevel().getLevelId() ==5){
		 		
               if(!StringUtils.isEmpty(carId) && !StringUtils.isEmpty(driverId)){
            	   order.setStatus(4);
            	   order.setCarId(Integer.parseInt(carId));
            	   order.setDriverId(Integer.parseInt(driverId));
				}else{
					return ResultManager.getFaildResult(1, "请为乘客配置车辆");	
				}
		 	}else if(account.getLevel().getLevelId() == 1){
		 		order.setStatus(1);
		 	}else if(account.getLevel().getLevelId() == 2){
		 		order.setStatus(2);
		 	}else{
		 		order.setStatus(3);
		 	}
		}
		Member u = memberService.get(order.getCarUserId());
		
		orderService.update(order);
		jpushUtils.sendMsgToMem(u.getUserName(), order.getStatus(), order.getReason());
		if(order.getStatus() == 4){
			Driver d = driverService.get(order.getDriverId());
			jpushUtils.sendMsgToDriver(d.getUserName(), order.getStatus(), order.getReason());
		}
		try {
			jpushUtils.noticeNodeJsServer(11,account.getTeam().getTeamId(), "new order");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultManager.getSuccResult();
	}
	
	public String getOrderCount(){
		Account account = getSessionAccount();
		String teamIds = account.getTeam().getTeamId()+"";
		String status ="0";
		String deptId = "";
		if( account.getLevel().getLevelId()==3 || account.getLevel().getLevelId() ==5){
					status = "0,1,2,3";
			//list=orderService.getOrdersByTeamId(teamIds, null);
		}else if(account.getLevel().getLevelId()<=2){
					status = "0,3";
			//list=orderService.getOrdersByTeamId(account.getTeam().getTeamId()+"", null);
		}else{
			//list=orderService.getOrdersByTeamId("", null);
			 deptId = account.getDept().getDeptId()+"";
					status = "0";
		}
		Object[] keywords = new Object[]{teamIds,status,deptId};
		List list = orderService.getCount(keywords);
		JSONObject json = new JSONObject();
		json.put("total", list.size());
		return json.toString();
	}
	public String getPrintData(){
		String orderId = getParameter("orderId");
		System.out.println(orderId);
		Account account = getSessionAccount();
		List order = orderService.getOrderBId(Integer.parseInt(orderId));
		return ResultManager.getBodyResult(order.get(0));
	}
}
