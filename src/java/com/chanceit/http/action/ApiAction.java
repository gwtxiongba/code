/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 22, 2013
 * Id: ApiAction.java,v 1.0 Jun 22, 2013 9:50:49 AM Administrator
 */
package com.chanceit.http.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.enums.EnumHTTP;
import com.chanceit.framework.exceptions.CommonException;
import com.chanceit.framework.exceptions.RegularException;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.UserService;

/**
 * @ClassName ApiAction
 * @author Administrator
 * @date Jun 22, 2013 9:50:49 AM
 * @Description 客户端方法接口类
 */
@Results( {
	@Result(name = "login", location = "login.html"),
	@Result(name = "index", location = "index.jsp")
})
public class ApiAction extends BaseAction{

	/**
	 * @Fields serialVersionUID 序列号
	 */
	private static final long serialVersionUID = -8350898203340761840L;
	
	private static final String MOBILE_REGEXT = "^.*_mobile$";
	
	private String cmd="login";
	
	@Autowired
	@Qualifier("weizhangView")
	private WeizhangView weizhangView;
	@Autowired
	@Qualifier("weibaoView")
	private WeibaoView weibaoView;
	@Autowired
	@Qualifier("reportNewView")
	private ReportNewView reportNewView;
	@Autowired
	@Qualifier("weixiuView")
	private WeixiuView weixiuView;
	@Autowired
	@Qualifier("brandView")
	private BrandView brandView;
	@Autowired
	@Qualifier("carstyleView")
	private CarstyleView carstyleView;
	@Autowired
	@Qualifier("carusesView")
	private CarusesView carusesView;
	@Autowired
	@Qualifier("baoxiaoView")
	private BaoxiaoView baoxiaoView;
	@Autowired
	@Qualifier("askleaveView")
	private AskleaveView askleaveView;
	@Autowired
	@Qualifier("govAppView")
	private GovAppView govAppView;
	@Autowired
	@Qualifier("memberView")
	private MemberView memberView;
	@Autowired
	@Qualifier("orderView")
	private OrderView orderView;
	@Autowired
	@Qualifier("reportView")
	private ReportView reportView;
	@Autowired
	@Qualifier("accountView")
	private AccountView accountView;
	
	@Autowired
	@Qualifier("driverView")
	private DriverView driverView;
	
	@Autowired
	@Qualifier("userView")
	private UserView userView;
	
	@Autowired
	@Qualifier("teamView")
	private TeamView teamView;
	
	@Autowired
	@Qualifier("prestoreMsgView")
	private PrestoreMsgView prestoreMsgView;
	
	@Autowired
	@Qualifier("messageCenterView")
	private MessageCenterView msgCenterView;
	
	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;
	
	@Autowired
	@Qualifier("boxWarnView")
	private BoxWarnView boxWarnView;
	
	@Autowired
	@Qualifier("warnView")
	private WarnView warnView;
	
	@Autowired
	@Qualifier("faultCheckView")
	private FaultCheckView faultCheckView;
	
	@Autowired
	@Qualifier("opTeamView")
	private OperatorTeamView opTeamView;
	
	@Autowired
	@Qualifier("carDriverView")
	private CarDriverView carDriverView;
	
	@Autowired
	@Qualifier("pcodeCensusView")
	private PcodeCensusView pcodeCensusView;
	
	@Autowired
	@Qualifier("parkingView")
	private ParkingView parkingView;
	
	@Autowired
	@Qualifier("baseInfoView")
	private BaseInfoView baseInfoView;
	
	@Autowired
	@Qualifier("appView")
	private AppView appView;
	
	@Autowired
	@Qualifier("formsView")
	private FormsView formsView;
	
	@Autowired
	@Qualifier("excelFormsView")
	private ExcelFormsView excelFormsView;
	
	@Autowired
	@Qualifier("guanhuiView")
	private GuanHuiView guanhuiView;
	
	@Autowired
	@Qualifier("deptView")
	private DeptView deptView;
	/** 
	 * @author Administrator
	 * @date Jun 22, 2013
	 * @return
	 * @throws Exception
	 * @Description TODO(这里用一句话描述这个方法的作用)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {
		//System.out.println(getRequest().getRequestURI()+"...cmd:"+cmd);
		
		
		Account account = getSessionAccount();
		String ifapp = getParameter("ifapp");
		if(checkCMD(cmd) && ifApp(ifapp) && !EnumHTTP.CAR_ACTION.equals(cmd) &&
				!EnumHTTP.WRITECARD.equals(cmd) && !EnumHTTP.READCARD.equals(cmd) &&
				!EnumHTTP.OPERATECARSTATE.equals(cmd)){//检查需要验证登录状态的CMD登录状态
			if(account == null){
				//Account account1 = accountService.getByName("sys");getSession().setAttribute("account", account1);
				writeJson2Page(ResultManager.getFaildResult(EnumCommon.EXCEPTION_NO_SESSION,"您未登录系统或长时间未操作"));
				return null;
			}
			if(!checkRight(cmd) ){//检查权限是否缺失
				writeJson2Page(ResultManager.getFaildResult(EnumCommon.EXCEPTION_RIGHT,"您无权执行该操作"));
				return null;
			}
		}
		
		try {
			if(EnumHTTP.GOLBAL.equals(cmd)){
				writeJson2Page(userView.golbal());
			}else if(EnumHTTP.SET_PWD.equals(cmd)){
				writeJson2Page(accountView.set());
			}else if(EnumHTTP.ACCOUNT_RESET_PASSWORD.equals(cmd)){
				writeJson2Page(accountView.resetPassword());
			} else 
			if(EnumHTTP.ACCOUNT_LOGIN.equals(cmd)){
				writeJson2Page(accountView.login());
//				return "index";
//				super.getRequest().getRequestDispatcher("index.jsp").forward(super.getRequest(), super.getResponse());
//				super.getResponse().sendRedirect("index.jsp");
			} else 
			if(EnumHTTP.ACCOUNT_LOGOUT.equals(cmd)){
				writeJson2Page(accountView.logout());
			}else 
			if(EnumHTTP.SAVECOMPANY.equals(cmd)){
				writeJson2Page(accountView.save());
			} else 
			if(EnumHTTP.SETCOMPANY.equals(cmd)){
				writeJson2Page(accountView.edit());
			} else 	
			if(EnumHTTP.LISTCOMPANY.equals(cmd)){
				writeJson2Page(accountView.listCompany(page));
			} else 
			if(EnumHTTP.DELCOMPANY.equals(cmd)){	//
				writeJson2Page(accountView.delete());
			} else 	
			if(EnumHTTP.SAVEOPERATOR.equals(cmd)){	//编辑操作员
				writeJson2Page(accountView.saveOperator());
			} else 	
			if(EnumHTTP.LISTOPERATOR.equals(cmd)){	//操作员列表
				writeJson2Page(accountView.list(page));
			} else 
			if(EnumHTTP.DELOPERATOR.equals(cmd)){	//删操作员
				writeJson2Page(accountView.delete());
			} else 	
			if(EnumHTTP.LISTDRIVER.equals(cmd)){	//司机列表
				writeJson2Page(driverView.list(page));
			} else 	
			if(EnumHTTP.SAVEDRIVER.equals(cmd)){	//保存司机
				writeJson2Page(driverView.saveDriver());
			} else
			if(EnumHTTP.DELDRIVER.equals(cmd)){	//删除司机
				writeJson2Page(driverView.delete());
			} 
			else 
			if(EnumHTTP.UNBINDDRIVERS.equals(cmd)){	//未绑的司机
				writeJson2Page(driverView.unbindDrivers());
			}else
			if(EnumHTTP.LISTDRIVERLOG.equals(cmd)){
				writeJson2Page(driverView.getDriverLogPage(page));		//司机考勤列表
			}else 
			if(EnumHTTP.DRIVERDATETRACE.equals(cmd)){
				writeJson2Page(driverView.getPoint());
			}else
			if(EnumHTTP.LISTABNORMALOG.equals(cmd)){
				writeJson2Page(driverView.getAbnormaDriverLogPage(page));	//非法用车司机列表
			}else 
			if(EnumHTTP.GETABNORMALLOGCOUNT.equals(cmd)){
				writeJson2Page(driverView.getAbnormalDriverLogCount());
			}else
			if(EnumHTTP.CARANDDRIVER.equals(cmd)){
				writeJson2Page(driverView.getCarList());
			}else
			if(EnumHTTP.TELPHONEODER.equals(cmd)){
				writeJson2Page(driverView.phoneOrder());
			} else 	
			if(EnumHTTP.SAVETEAM.equals(cmd)){			//车队车辆接口开始
				writeJson2Page(teamView.saveTeam());
			} else 	
			if(EnumHTTP.LISTTEAM.equals(cmd)){
				writeJson2Page(teamView.list(page));
			} else 
			if(EnumHTTP.DELTEAM.equals(cmd)){
				writeJson2Page(teamView.delete());
			}else
			if(EnumHTTP.DRIVERDETAIL.equals(cmd)){
				writeJson2Page(userView.driverDetail());
			}else 	
			if(EnumHTTP.TEAMVEHICLES.equals(cmd)){
				writeJson2Page(userView.getList());
			} else 
			if(EnumHTTP.LISTVEHICLE.equals(cmd)){//车辆接口开始
				writeJson2Page(userView.pageList(page));
			} else 
			if(EnumHTTP.LISTMONITORVEHICLE.equals(cmd)){//监控车辆列表
				writeJson2Page(userView.list());
			} else 
			if(EnumHTTP.SAVEMONITORVEHICLES.equals(cmd)){//批量设置监控车辆
				writeJson2Page(userView.saveMonitor());
			} else 	
			if(EnumHTTP.SAVEVEHICLE.equals(cmd)){
				writeJson2Page(userView.save());
			} else 
			if(EnumHTTP.DELVEHICLE.equals(cmd)){
				writeJson2Page(userView.delete());
			} 
		  /*else if(EnumHTTP.SENDMSG.equals(cmd)){
				writeJson2Page(userView.sendMessage());
			}*/
			else if(EnumHTTP.SENDPOINT.equals(cmd)){
//				writeJson2Page(userView.sendPoint());
			}else 	
			if(EnumHTTP.VEHICLECOUNT.equals(cmd)){
				writeJson2Page(userView.vehiclesOnlineCount());//在线数量
			}else if(EnumHTTP.MAPDATA.equals(cmd)){
				writeJson2Page(userView.mapdata());
			}else if(EnumHTTP.VEHICLEONLINES.equals(cmd)){
				writeJson2Page(userView.getOnlineList());
			}else if(EnumHTTP.MONITDATA.equals(cmd)){
				writeJson2Page(userView.getLocationByUids());
			}
			//add by zhouhui 2014-10-22 断油断电车辆显示
			else if(EnumHTTP.RELAYCOUNT.equals(cmd)){
				writeJson2Page(userView.vehiclesRelayCount());//继电器设备数量
			}else if(EnumHTTP.RELAYOFFS.equals(cmd)){
				writeJson2Page(userView.getRelayList(page));
				
			}else if(EnumHTTP.LISTALARM.equals(cmd)){ //提醒列表
				writeJson2Page(boxWarnView.getPagelist(page));
			}else if(EnumHTTP.HISTORYALARM.equals(cmd)){ //提醒详细列表
				writeJson2Page(boxWarnView.getlist(page));
			}else if(EnumHTTP.GETALARMCOUNT.equals(cmd)){ //获取报警车辆总数
				writeJson2Page(boxWarnView.getAlarmCount());
			}else if(EnumHTTP.LISTABNORMAL.equals(cmd)){ // 异常参数列表
				writeJson2Page(warnView.getPagelist(page));
			}else if(EnumHTTP.HISTORYABNORMAL.equals(cmd)){ //异常参数详细列表
				writeJson2Page(warnView.getlist(page));
			}else if(EnumHTTP.GETABNORMALCOUNT.equals(cmd)){ //获取异常参数车辆总数
				writeJson2Page(warnView.getAlarmCount());
			}else if(EnumHTTP.DATEEVENT.equals(cmd)){ //获取某天提醒事件列表
				writeJson2Page(boxWarnView.getDateEvent());
			}
			//add by zhangxin 2014-6-27
			//预存消息相关逻辑
			//保存预存消息
			else if(EnumHTTP.SAVEPRESTOREMSG.equals(cmd)){
				writeJson2Page(prestoreMsgView.save());
			} 
			//获取预存消息列表
			else if(EnumHTTP.LISTPRESTOREMSG.equals(cmd)){
				writeJson2Page(prestoreMsgView.list(page));
			} 
			//删除预存消息列表
			else if(EnumHTTP.DELPRESTOREMSG.equals(cmd)){
				writeJson2Page(prestoreMsgView.delete());
			} 
			//end
			
			//add by zhangxin 2014-6-30
			//消息中心相关逻辑
			//获取消息中心列表
			else if(EnumHTTP.LISTMSGSESSION.equals(cmd)){
				writeJson2Page(msgCenterView.list(page));
			} 
			//发送消息
//			else if(EnumHTTP.SENDMSGCENTER.equals(cmd)){
//				writeJson2Page(msgCenterView.save());
//			} 
			//发送消息
			else if(EnumHTTP.GETMSGSESSION.equals(cmd)){
				writeJson2Page(msgCenterView.getlist(page));
			} 
			//end
			
			//add by zhangxin 2014-7-14
			//指令列表
			else if(EnumHTTP.LISTCMDINFO.equals(cmd)){
				writeJson2Page(boxWarnView.cmdlist());
			} 
			//end
			
			//add by zhangxin 2014-7-16
			//获取位置点信息
			else if(EnumHTTP.DATETRACE.equals(cmd)){
				writeJson2Page(userView.getPoint());
			} 
			//end
			
			//add by zhangxin 2014-7-18
			//获取行车日记
			else if(EnumHTTP.MONTHDRIVELOGS.equals(cmd)){
				writeJson2Page(userView.getDiaryPoint());
			} 
			//获取行车日记
			else if(EnumHTTP.YEARDRIVELOGS.equals(cmd)){
				writeJson2Page(userView.getYearDiaryPoint());
			} 
			//end
			
			//add by zhangxin 2014-08-12
			//车辆司机规则列表
			else if(EnumHTTP.LISTCARDRIVER.equals(cmd)){
				writeJson2Page(carDriverView.list(page));
			} 
			//保存车辆司机规则
			else if(EnumHTTP.SAVECARDRIVER.equals(cmd)){
				writeJson2Page(carDriverView.saveCarDriver());
			} 
			//删除车辆司机规则
			else if(EnumHTTP.DELCARDRIVER.equals(cmd)){
				writeJson2Page(carDriverView.delete());
			} 
			//写卡
			else if(EnumHTTP.WRITECARD.equals(cmd)){
				writeJson2Page(carDriverView.writeCard());
			} 
			//读卡
			/*else if(EnumHTTP.READCARD.equals(cmd)){
				carDriverView.valSignIn("194515836","e5286eb2","hsj01000",new Date());
				writeJson2Page("");
			} */
			//车辆检测
			else if(EnumHTTP.DOSCAN.equals(cmd)){
				String result = pcodeCensusView.getDetectionInfo();
				if("0".equals(result)){
					writeJson2Page(ResultManager.getFaildResult("没有车辆"));
				}else if("1".equals(result)){
					writeJson2Page(ResultManager.getFaildResult("没有车队"));
				}else{
					writeJson2Page(result);
				}
				
			} 
			//车辆检测报告
			else if(EnumHTTP.DETECTIONDIARY.equals(cmd)){
				String result = pcodeCensusView.getDetectionDiary();
				if("1".equals(result)){//没有team
					writeJson2Page(ResultManager.getFaildResult("没有车队"));
				}else if("0".equals(result)){
					//没有车辆
					writeJson2Page(ResultManager.getFaildResult("没有车辆"));
				}else{
					writeJson2Page(result);
				}
			} 
			//车队绑定关系列表
			else if(EnumHTTP.LISTTEAMSANDOPERATOR.equals(cmd)){
				writeJson2Page(opTeamView.getGroupList());
			//操作员车队绑定关系列表
			}else if(EnumHTTP.LISTOPTEAM.equals(cmd)){
				writeJson2Page(opTeamView.list());
			//车队绑定关系修改
			}else if(EnumHTTP.UPDATEOPTEAM.equals(cmd)){
				writeJson2Page(opTeamView.upDateOpTeam());
			//断油断电
			}else if(EnumHTTP.OPERATECAR.equals(cmd)){
				writeJson2Page(userView.powerOffAndFuelCut());
			//断油断电消息接收
			}else if(EnumHTTP.OPERATECARSTATE.equals(cmd)){
				writeJson2Page(userView.getCarInfoState());
			} 
			//end
			else if(EnumHTTP.LISTADMIN.equals(cmd)){
				writeJson2Page(accountView.adminList(page));
			} 
			else if(EnumHTTP.ACCOUNTNAME_CHECK.equals(cmd)){
				writeJson2Page(accountView.checkAccountName());
			}else if(EnumHTTP.ACCOUNT_SENDPWD_EMAIL.equals(cmd)){
				writeJson2Page(accountView.forgetPasswordMail());
			} else if(EnumHTTP.ACCOUNT_CHECK_RANDOMCODE.equals(cmd)){
				writeJson2Page(accountView.checkVerificationCode());
			}else if(EnumHTTP.ACCOUNT_ACTIVATE_EMAIL.equals(cmd)){
				writeJson2Page(accountView.sendActivateMail());
			}else if(EnumHTTP.ACCOUNT_ACTIVATE_HREF.equals(cmd)){
				writeJson2Page(accountView.hrefAccountActivate());
			}else if(EnumHTTP.ACCOUNT_CHECK_NAME.equals(cmd)){
				writeJson2Page(accountView.checkAccName());
			}else if(EnumHTTP.ACCOUNT_CHECK_EMAIL.equals(cmd)){
				writeJson2Page(accountView.checkEmail());
			}else if(EnumHTTP.ACCOUNT_UPDATE_EMAIL.equals(cmd)){
				writeJson2Page(accountView.updateEmail());
			}
			else 
			if(EnumHTTP.CHECK_UID.equals(cmd)){
//				writeJson2Page(userView.checkUid());
			}else 	
			if(EnumHTTP.CAR_ACTION.equals(cmd)){
//				writeJson2Page(userView.getListbak());
			}else 	
			if(EnumHTTP.CAR_VIEW.equals(cmd)){
				writeJson2Page(userView.view());
			}
			else if(EnumHTTP.OBDSWITCH.equals(cmd)){			//开启/关闭OBD通讯
				writeJson2Page(faultCheckView.getObdState());
			}else if(EnumHTTP.OBDCODE.equals(cmd)){
				writeJson2Page(faultCheckView.getObdCode());	//获取OBD故障码
			}else if(EnumHTTP.SENDOBDDATA.equals(cmd)){
				writeJson2Page(faultCheckView.getObdData());	//获取OBD实时数据	 
			}
			//add by shaozheng 2014-08-29
			else if(EnumHTTP.SAVEPARKING.equals(cmd)){			//保存/修改车库信息
				writeJson2Page(parkingView.saveParking());		
			}else if(EnumHTTP.DELPARKING.equals(cmd)){			//删除车库信息
				writeJson2Page(parkingView.deleteParking());
			}else if(EnumHTTP.LISTPARKING.equals(cmd)){			//获取车库信息
				writeJson2Page(parkingView.parkingPageList(page));
			}else if(EnumHTTP.LISTPARKINGCAR.equals(cmd)){		//获取车库车辆信息
				writeJson2Page(parkingView.userPageList());	
			}else if(EnumHTTP.LISTPLATE.equals(cmd)){		//获取车辆基本信息的车牌(当前team空闲状态的车)
				writeJson2Page(baseInfoView.plateList());
			}else if(EnumHTTP.LISTPLATEALL.equals(cmd)){	//获取车辆基本信息的车牌(当前team所有状态的车)
				writeJson2Page(baseInfoView.plateListAll());
			}else if(EnumHTTP.SAVEBASEINFO.equals(cmd)){
				writeJson2Page(baseInfoView.saveBaseInfo());
			}else if(EnumHTTP.LISTBASEINFO.equals(cmd)){
				writeJson2Page(baseInfoView.baseInfoPageList(page));
			}else if(EnumHTTP.DELBASEINFO.equals(cmd)){
				writeJson2Page(baseInfoView.delBaseInfo());
			}
//			else if(EnumHTTP.LISTBRAND.equals(cmd)){
//				writeJson2Page(baseInfoView.brandList());
//			}
			
/************************************安卓APP客户端相关接口********************************************************/
			//add by zhangxin 2014-09-21 安卓客户端相关接口
			//登录
			else if(EnumHTTP.APPLOGIN.equals(cmd)){
				writeJson2Page(appView.loginForApp());
			}
			//车队列表
			else if(EnumHTTP.APPLISTTEAM.equals(cmd)){
				writeJson2Page(appView.listForApp());
			}
			//监控列表
			else if(EnumHTTP.APPLISTMONITOR.equals(cmd)){
				writeJson2Page(appView.monitorListForApp());
			}
			else if(EnumHTTP.APPITEMTEAM.equals(cmd)){
				writeJson2Page(appView.getListByTeamId());
			}else if(EnumHTTP.APPITEMCAR.equals(cmd)){
				writeJson2Page(appView.viewForApp());
			}
			else if(EnumHTTP.APPGROUPLIST.equals(cmd)){
				writeJson2Page(appView.getGroupList());
			}
			else if(EnumHTTP.APPWARNINGLIST.equals(cmd)){
				writeJson2Page(appView.getWaringList());
			}
			else if(EnumHTTP.APPWARNINGROUPGLIST.equals(cmd)){
				writeJson2Page(appView.getWaringGroupList());
			}
			else if(EnumHTTP.APPITEMTEAMNO.equals(cmd)){
				writeJson2Page(appView.getListNo());
			}
			else if(EnumHTTP.APPADDTEAM.equals(cmd)){
				writeJson2Page(appView.addTeam());
			}else if(EnumHTTP.APPUNBANDINDRIVER.equals(cmd)){
				writeJson2Page(appView.unbindDrivers());
			}else if(EnumHTTP.APPADDCAR.equals(cmd)){
				writeJson2Page(appView.addCars());
			}else if(EnumHTTP.APPEXCEPTION.equals(cmd)){
				writeJson2Page(appView.getExceptionList());
			}else if(EnumHTTP.APPEXCEPTIONLIST.equals(cmd)){
				writeJson2Page(appView.getExceptionItemList());
			}else if(EnumHTTP.APPDRIVERLIST.equals(cmd)){
				writeJson2Page(appView.diverList());
			}else if(EnumHTTP.APPADDDRIVER.equals(cmd)){
				writeJson2Page(appView.saveDriver());
			}else if(EnumHTTP.APPDRIVERDITEL.equals(cmd)){
				writeJson2Page(appView.getCarDriver());
			}else if(EnumHTTP.APPDELDRIVER.equals(cmd)){
				writeJson2Page(appView.deleteDriver());
			}else if(EnumHTTP.APPSETWARN.equals(cmd)){
				writeJson2Page(appView.setWarned());
			}else if(EnumHTTP.APPONLINE.equals(cmd)){
				writeJson2Page(appView.getOnlineList());
			}else if(EnumHTTP.APPDRIVER.equals(cmd)){
				writeJson2Page(appView.getDriverById());
			}else if(EnumHTTP.APPDELUSER.equals(cmd)){
				writeJson2Page(appView.delCarById());
			}else if(EnumHTTP.APPWEILAN.equals(cmd)){
				writeJson2Page(appView.getLineWarn());
			}else if(EnumHTTP.GETTEAMSFORCAR.equals(cmd)){
				writeJson2Page(appView.getTeams());
			}
			
			//add by zhangyejia 2014-12-30
//			else if(EnumHTTP.CYCLEDAYSUMMARY.equals(cmd)){//周期报表中的日报表
//				writeJson2Page(reportView.listCycleReport());
//			}else if(EnumHTTP.CYCLEDAYSUMMARYDETAIL.equals(cmd)){//周期报表中的日报表详细列表
//				writeJson2Page(reportView.listCycleMileageOil());
//			}
	/*******************************************************************************/
		  else if(EnumHTTP.CYCLEMONTHSUMMARY.equals(cmd)){//周期报表中的月报表
				writeJson2Page(reportView.listCycleMileageOilByMonth());
			}else if(EnumHTTP.EXPORTCYCLEDAYSUMMARY.equals(cmd)){//周期报表中的日报表导出excel
				reportView.exportCycle();
			}else if(EnumHTTP.EXPORTCYCLEMONTHSUMMARY.equals(cmd)){//周期报表中的月报表导出excel
				reportView.exportCycleMonth();
			}
			//add by gwt 2015-01-04
			/******************报表**************************/
			else if(EnumHTTP.FORMDRIVING.equals(cmd)){
				writeJson2Page(formsView.getDrivingForm());   
			}else if(EnumHTTP.FORMPATH.equals(cmd)){
				writeJson2Page(formsView.getDrivingList());
			}
			else if(EnumHTTP.FORMLOCATION.equals(cmd)){
				writeJson2Page(formsView.getLocation());
			}else if(EnumHTTP.FORMOVERPATH.equals(cmd)){
				writeJson2Page(formsView.getOverSpeed());
			}else if(EnumHTTP.FORMOBDLIST.equals(cmd)){
				writeJson2Page(formsView.getObdMileList());
			}
			else if(EnumHTTP.FORMSTOPLIST.equals(cmd)){
				writeJson2Page(formsView.getStopList());
			}
			else if(EnumHTTP.FORMSTOP.equals(cmd)){
				writeJson2Page(formsView.getStopForm());
			}
			else if(EnumHTTP.FORMFIRE.equals(cmd)){
				writeJson2Page(formsView.getFireForm());
			}
			else if(EnumHTTP.FORMFIRELIST.equals(cmd)){
				writeJson2Page(formsView.getFireList());
			}else if(EnumHTTP.FORMTEAMS.equals(cmd)){
				writeJson2Page(formsView.getTeamCar());   //得到车队车辆
			}else if(EnumHTTP.FORMDUANDIAN.equals(cmd)){
				writeJson2Page(formsView.getBreakDian());   //
			}else if(EnumHTTP.FORMDUANDIANONE.equals(cmd)){
				writeJson2Page(formsView.getBreakDianOne());   //
			}else if(EnumHTTP.FORMDMONTH.equals(cmd)){
				writeJson2Page(formsView.getOnlineByMonth());   //
			}else if(EnumHTTP.FORMDDAY.equals(cmd)){
				writeJson2Page(formsView.getOnlineByDay());   //
			}else if(EnumHTTP.FORMDDATEFULE.equals(cmd)){
				writeJson2Page(formsView.getFuleByDay());   //
			}else if(EnumHTTP.FORMDSTATSDAY.equals(cmd)){
				writeJson2Page(formsView.dayForm());   //
			}else if(EnumHTTP.FORMDSTATSMF.equals(cmd)){
				writeJson2Page(formsView.dayFormList());   //
			}
			
			/****************导出报表接口**********************/
			else if(EnumHTTP.FORMDRIVINGEXCEL.equals(cmd)){
				excelFormsView.getDrivingForm();   
			}else if(EnumHTTP.FORMPATHEXCEL.equals(cmd)){
				excelFormsView.getDrivingList();
			}
			else if(EnumHTTP.FORMLOCATIONEXCEL.equals(cmd)){
				  excelFormsView.getLocation();
			}else if(EnumHTTP.FORMOVERPATH.equals(cmd)){
				excelFormsView.getOverSpeed();
			}else if(EnumHTTP.FORMOBDLISTEXCEL.equals(cmd)){
				excelFormsView.getObdMileList();
			}
			else if(EnumHTTP.FORMSTOPLISTEXCEL.equals(cmd)){
				excelFormsView.getStopList();
			}
			else if(EnumHTTP.FORMSTOPEXCEL.equals(cmd)){
				excelFormsView.getStopForm();
			}
			else if(EnumHTTP.FORMFIREEXCEL.equals(cmd)){
				excelFormsView.getFireForm();
			}
			else if(EnumHTTP.FORMFIRELISTEXCEL.equals(cmd)){
				excelFormsView.getFireList();
			
			}else if(EnumHTTP.FORMDUANDIANEXCEL.equals(cmd)){
				excelFormsView.getBreakDian();   //
			}else if(EnumHTTP.FORMDUANDIANONEEXCEL.equals(cmd)){
				excelFormsView.getBreakDianOne();   //
			}else if(EnumHTTP.FORMDMONTHEXCEL.equals(cmd)){
				excelFormsView.getOnlineByMonth();   //
			}else if(EnumHTTP.FORMDDAYEXCEL.equals(cmd)){
			//	writeJson2Page(excelFormsView.getOnlineByDay());   //
			}else if(EnumHTTP.FORMDDATEFULEEXCEL.equals(cmd)){
				excelFormsView.getFuleByDay();   //
			}else if(EnumHTTP.FORMDSTATSDAYEXCEL.equals(cmd)){
				excelFormsView.dayForm();   //
			}else if(EnumHTTP.FORMDSTATSMFEXCEL.equals(cmd)){
				excelFormsView.dayFormList();   //
			}
			
			else if(EnumHTTP.MEMBEREXCEL.equals(cmd)){	//乘客报表 导出
				writeJson2Page(excelFormsView.memberExcel());
			}
			else if(EnumHTTP.DRIVEREXCEL.equals(cmd)){	//乘客报表 导出
				writeJson2Page(excelFormsView.driverExcel());
			}
			else if(EnumHTTP.CAREXCEL.equals(cmd)){	//车辆报表 导出
				writeJson2Page(excelFormsView.carExcel());
			}
			
			
			//add by gwt 2015-01-04
			/****************广汇********************/
			else if(EnumHTTP.GUANHUIPOST.equals(cmd)){
				writeJson2Page(guanhuiView.guanghuiTeamcars());
			}
			else if(EnumHTTP.GUANHUISAVERULE.equals(cmd)){
				writeJson2Page(guanhuiView.save());
			}
			else if(EnumHTTP.GUANHUISAVELINE.equals(cmd)){
				writeJson2Page(guanhuiView.savex());
			}else if(EnumHTTP.GUANHUILINES.equals(cmd)){
				writeJson2Page(guanhuiView.getLineList());
			}else if(EnumHTTP.GUANHUILINESRULE.equals(cmd)){
				writeJson2Page(guanhuiView.getLineRuleList());
			}else if(EnumHTTP.GUANHUIDELLINE.equals(cmd)){
				writeJson2Page(guanhuiView.del());
			}else if(EnumHTTP.GUANHUIDERULE.equals(cmd)){
				writeJson2Page(guanhuiView.delRule());
			}else if(EnumHTTP.GUANHUILINEWARN.equals(cmd)){
				writeJson2Page(guanhuiView.getLineWarn(page));
			}else if(EnumHTTP.GUANHUILINEUPDTE.equals(cmd)){
				writeJson2Page(guanhuiView.updateIsread());
			}else if(EnumHTTP.GUANHUILINECOUNT.equals(cmd)){
				writeJson2Page(guanhuiView.getCount());
			}else if(EnumHTTP.GUANHUILINELIMIT.equals(cmd)){
				writeJson2Page(guanhuiView.getLimiteLine());
			}else if(EnumHTTP.GETTEAMS.equals(cmd)){
				writeJson2Page(userView.getTeams());
			}else if(EnumHTTP.GUANHUISAVECIRCLE.equals(cmd)){
				writeJson2Page(guanhuiView.saveCircle());
			}
			else if(EnumHTTP.GUANHUIGETCIRCLES.equals(cmd)){
				writeJson2Page(guanhuiView.getCircles());
			}
			else if(EnumHTTP.GUANHUIDELCIRCLES.equals(cmd)){
				writeJson2Page(guanhuiView.delCircle());
			}else if(EnumHTTP.GUANHUISAVERULES0.equals(cmd)){
				writeJson2Page(guanhuiView.saveRule0());
			}else if(EnumHTTP.GUANHUIGETCIRCLERULES.equals(cmd)){
				writeJson2Page(guanhuiView.getCircleRule());
			}else if(EnumHTTP.GUANHUIDELRULES0.equals(cmd)){
				writeJson2Page(guanhuiView.delRule0());
			}else if(EnumHTTP.GUANHUIGETPL.equals(cmd)){
				writeJson2Page(guanhuiView.getPoylgon());
			}else if(EnumHTTP.GUANHUIGETPLR.equals(cmd)){
				writeJson2Page(guanhuiView.getPolygonRules());
			}else if(EnumHTTP.GUANHUISAVEPL.equals(cmd)){
				writeJson2Page(guanhuiView.savePolygon());
			}else if(EnumHTTP.GUANHUIDELPL.equals(cmd)){
				writeJson2Page(guanhuiView.delPolygon());
			}else if(EnumHTTP.GUANHUISAVERULES1.equals(cmd)){
				writeJson2Page(guanhuiView.saveRules1());
			}
			else if(EnumHTTP.GUANHUIDELRULES1.equals(cmd)){
				writeJson2Page(guanhuiView.delRule1());
			}else if(EnumHTTP.GUANHUITEST.equals(cmd)){
				writeJson2Page(guanhuiView.test());
			}
			
			//超速报警的速度设置
			else if(EnumHTTP.OVERSPEED.equals(cmd)){
				writeJson2Page(userView.overSpeed());
			}
			//位置点上传时间间隔
			else if(EnumHTTP.POSTIME.equals(cmd)){
				writeJson2Page(userView.posTime());
			}else if(EnumHTTP.LISTBRANDBYSTYLE.equals(cmd)){
				writeJson2Page(userView.listBrandBySytle());
			}
			
			
			/*******************政务平台融合接口*************/
			/**************************************************/
			else if(EnumHTTP.LISTORDER.equals(cmd)){	//订单列表
				writeJson2Page(orderView.list(page));
			}
			else if(EnumHTTP.ADDORDER.equals(cmd)){	//新增预约订单
				writeJson2Page(orderView.addOrder());
			}
			else if(EnumHTTP.LISTMEMBER.equals(cmd)){	//用车人列表
				writeJson2Page(memberView.list(page));
			}
			else if(EnumHTTP.SAVEMEMBER.equals(cmd)){	// 新增/修改用车人
				writeJson2Page(memberView.saveMember());
			}
			else if(EnumHTTP.DELMEMBER.equals(cmd)){	// 删除用车人
				writeJson2Page(memberView.delMember());
			}
			else if(EnumHTTP.GETDEPTTEAMS.equals(cmd)){	//乘客列表
				writeJson2Page(deptView.getDeptTeams());
			}else if(EnumHTTP.GETDEPTLIST.equals(cmd)){	//部门列表
				writeJson2Page(deptView.getDeptList(page));
			}else if(EnumHTTP.SAVEDEPT.equals(cmd)){	//保存部门
				writeJson2Page(deptView.save());
			}else if(EnumHTTP.DELETEDEPT.equals(cmd)){	//删除部门
				writeJson2Page(deptView.deleteDept());
			}else if(EnumHTTP.GETLEAVELS.equals(cmd)){	//角色
				writeJson2Page(accountView.getLeavels());
			}else if(EnumHTTP.EDITORDER.equals(cmd)){	//角色
				writeJson2Page(orderView.editOrder());
			}else if(EnumHTTP.LISTASKLEAVE.equals(cmd)){	//请假列表
				writeJson2Page(askleaveView.list(page));
			}
			else if(EnumHTTP.AUDITASKLEAVE.equals(cmd)){	//审批请假
				writeJson2Page(askleaveView.auditAskleave());
			}
			else if(EnumHTTP.LISTBAOXIAO.equals(cmd)){	//报销列表
				writeJson2Page(baoxiaoView.list(page));
			}
			else if(EnumHTTP.AUDITBAOXIAO.equals(cmd)){	//审批报销
				writeJson2Page(baoxiaoView.auditBaoxiao());
			}
			else if(EnumHTTP.ADDBAOXIAO.equals(cmd)){	//新增报销
				writeJson2Page(baoxiaoView.addBaoxiao());
			}
			
			else if(EnumHTTP.LISTCARUSES.equals(cmd)){	//用车事由 列表
				writeJson2Page(carusesView.list(page));
			}
			else if(EnumHTTP.SAVECARUSES.equals(cmd)){	//用车事由 增加或修改
				writeJson2Page(carusesView.saveCaruses());
			}
			else if(EnumHTTP.DELETECARUSES.equals(cmd)){	//用车事由 删除
				writeJson2Page(carusesView.delete());
			}
			
			else if(EnumHTTP.LISTCARSTYLE.equals(cmd)){	//车型 列表
				writeJson2Page(carstyleView.list(page));
			}
			else if(EnumHTTP.SAVECARSTYLE.equals(cmd)){	//车型 增加或修改
				writeJson2Page(carstyleView.saveCarstyle());
			}
			else if(EnumHTTP.DELETECARSTYLE.equals(cmd)){	//车型  删除
				writeJson2Page(carstyleView.delete());
			}
			
			else if(EnumHTTP.LISTBRAND.equals(cmd)){	//品牌 列表
				writeJson2Page(brandView.list());
			}
			else if(EnumHTTP.LISTBRANDPAGE.equals(cmd)){	//品牌 分页列表
				writeJson2Page(brandView.listPage(page));
			}
			else if(EnumHTTP.SAVEBRAND.equals(cmd)){	//品牌 增加或修改
				writeJson2Page(brandView.saveBrand());
			}
			else if(EnumHTTP.DELETEBRAND.equals(cmd)){	//品牌 删除
				writeJson2Page(brandView.delete());
			}else if(EnumHTTP.GETORDERCOUNT.equals(cmd)){	//
				writeJson2Page(orderView.getOrderCount());
			}else if(EnumHTTP.GETCARSTYLE.equals(cmd)){	//
				writeJson2Page(carstyleView.getCarStyle());
			}	else if(EnumHTTP.LISTWEIXIU.equals(cmd)){	//维修列表
				writeJson2Page(weixiuView.list(page));
			}
			else if(EnumHTTP.AUDITWEIXIU.equals(cmd)){	//审批维修
				writeJson2Page(weixiuView.auditWeixiu());
			}
			else if(EnumHTTP.RESETMEMBER.equals(cmd)){	//乘客重置密码
				writeJson2Page(memberView.resetMember());
			}
			else if(EnumHTTP.RESETDRIVER.equals(cmd)){	//司机重置密码
				writeJson2Page(driverView.resetDriver());
			}
			else if(EnumHTTP.RESETACCOUNT.equals(cmd)){	//管理员账户重置密码
				writeJson2Page(accountView.resetAccount());
			}
			
			else if(EnumHTTP.LISTWEIBAO.equals(cmd)){	//维保列表
				writeJson2Page(weibaoView.list(page));
			}
			else if(EnumHTTP.SAVEWEIBAO.equals(cmd)){	//维保编辑和新增
				writeJson2Page(weibaoView.saveWeibao());
			}
			else if(EnumHTTP.DELWEIBAO.equals(cmd)){	//维保删除
				writeJson2Page(weibaoView.delete());
			}
			
			else if(EnumHTTP.LISTWEIZHANG.equals(cmd)){	//维章列表
				writeJson2Page(weizhangView.list(page));
			}
			else if(EnumHTTP.SAVEWEIZHANG.equals(cmd)){	//维章编辑和新增
				writeJson2Page(weizhangView.saveWeizhang());
			}
			else if(EnumHTTP.DELWEIZHANG.equals(cmd)){	//维章删除
				writeJson2Page(weizhangView.delete());
			}
			
			//统计部分
			else if(EnumHTTP.REPORTCAR.equals(cmd)){	//车辆统计
				writeJson2Page(reportNewView.listCarReport());
			}
			else if(EnumHTTP.REPORTMONTH.equals(cmd)){	//按月份统计
				writeJson2Page(reportNewView.listMonthReport());
			}
			else if(EnumHTTP.REPORTDRIVER.equals(cmd)){	//司机统计
				writeJson2Page(reportNewView.listDriverReport());
			}
			else if(EnumHTTP.REPORTMEMBER.equals(cmd)){	//乘客统计
				writeJson2Page(reportNewView.listMemberReport());
			}
			else if(EnumHTTP.REPORTMONTHDETAIL.equals(cmd)){	//月份明细下载
				writeJson2Page(reportNewView.listMonthReportDetail());
			}else if(EnumHTTP.GETWEIBAOEND.equals(cmd)){	//月份明细下载
				writeJson2Page(weibaoView.getCount());
			}
			
			
			else if(EnumHTTP.UPDATETEAM.equals(cmd)){	//拖动树型结构 修改车辆team
				writeJson2Page(userView.updateTeam());
			}
			
			
			
			
			/*******************政务用车APP接口*************/
			/**************************************************/
			else if(EnumHTTP.LOGINFORGOV.equals(cmd)){	//登录
				writeJson2Page(govAppView.LoginForGov());
			}
			else if(EnumHTTP.MEMBERREGISTER.equals(cmd)){	//乘客注册
				writeJson2Page(govAppView.memberRegister());
			}
			else if(EnumHTTP.DRIVERREGISTER.equals(cmd)){
				//writeJson2Page(govAppView.driverRegister());
			}else if(EnumHTTP.APPGETTEAMS.equals(cmd)){	
				writeJson2Page(govAppView.getTeams());
			}else if(EnumHTTP.APPGETDEPT.equals(cmd)){	
				writeJson2Page(govAppView.getDept());
			}else if(EnumHTTP.APPGETLEAVE.equals(cmd)){
				writeJson2Page(govAppView.getLeaveList());
			}else if(EnumHTTP.APPEDITLEAVE.equals(cmd)){	
				writeJson2Page(govAppView.auditAskleave());
			}else if(EnumHTTP.APPGETORDERS.equals(cmd)){	
				writeJson2Page(govAppView.getOrders());
			}else if(EnumHTTP.APPGETCARSTYLE.equals(cmd)){	
				writeJson2Page(govAppView.getCarStyle());
			}else if(EnumHTTP.APPGETCAURSE.equals(cmd)){	
				writeJson2Page(govAppView.getCaurse());
			}else if(EnumHTTP.APPGETBRAND.equals(cmd)){	
				writeJson2Page(govAppView.getBrand());
			}else if(EnumHTTP.APPADDASKLEAVE.equals(cmd)){	
				writeJson2Page(govAppView.addLeave());
			}else if(EnumHTTP.APPADDBAOXIAO.equals(cmd)){	
				writeJson2Page(govAppView.addBaoxiao());
			}else if(EnumHTTP.APPGETBAOXIAO.equals(cmd)){	
				writeJson2Page(govAppView.getBaoxiao());
			}else if(EnumHTTP.APPEDITBAOXIAO.equals(cmd)){	
				writeJson2Page(govAppView.auditBaoxiao());
			}else if(EnumHTTP.APPGETCAR.equals(cmd)){	
				writeJson2Page(govAppView.getPlate());
			}else if(EnumHTTP.APPGETDRIVER.equals(cmd)){	
				writeJson2Page(govAppView.getDriver());
			}else if(EnumHTTP.APPADDORDER.equals(cmd)){	
				writeJson2Page(govAppView.addOrder());
			}else if(EnumHTTP.APPGETMEMBER.equals(cmd)){	
				writeJson2Page(govAppView.getMember());
			}else if(EnumHTTP.APPEDITORDER.equals(cmd)){	
				writeJson2Page(govAppView.editOrder());
			}else if(EnumHTTP.APPEDITORDERGLF.equals(cmd)){	
				writeJson2Page(govAppView.editGlfOrder());
			}else if(EnumHTTP.APPEDITINFO.equals(cmd)){	
				writeJson2Page(govAppView.eiditUserInfo());
			}else if(EnumHTTP.APPEDITPWD.equals(cmd)){	
				writeJson2Page(govAppView.eiditUserPwd());
			}else if(EnumHTTP.APPEDITMEMBER.equals(cmd)){	
				writeJson2Page(govAppView.editMemberDriver());
			}else if(EnumHTTP.AUDITMEMBER.equals(cmd)){	
				writeJson2Page(memberView.editMember());
			}else if(EnumHTTP.AUDITDRIVER.equals(cmd)){	
				writeJson2Page(driverView.editDriver());
			}else if(EnumHTTP.GETPRINTDATA.equals(cmd)){	
				writeJson2Page(orderView.getPrintData());
			}else if(EnumHTTP.EDITFEEDORDER.equals(cmd)){	
				writeJson2Page(govAppView.editFeedOrder());
			}
			
			
			
			else {
				writeJson2Page(ResultManager.getFaildResult(ResultManager.EXCEPTION_404,"接口方法找不到"));
			}
		} catch (NumberFormatException e) {
			writeJson2Page(ResultManager.getFaildResult("数字转换异常"));
		} catch (CommonException e) {
			writeJson2Page(ResultManager.getFaildResult(e.getMessage()));
		} catch (RegularException e) {
			writeJson2Page(ResultManager.getFaildRegularResult(e.getMessage()));
		}catch (ObjectNotFoundException oe){
			writeJson2Page(ResultManager.getFaildResult(ResultManager.EXCEPTION_500,"没有对应的公司，请联系系统管理员"));
		}
		
		catch (Exception e) {
			logger.warn("系统发生未知异常，信息提示结果："+e.getMessage()+e.getStackTrace()+"cmd="+cmd);
			writeJson2Page(ResultManager.getFaildResult(ResultManager.EXCEPTION_500,"系统异常"));
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean checkRight(String cmd) {
		if(StringUtils.isBlank(cmd)){
			return true;
		}
		Account account = getSessionAccount();
		//update by zhangxin 2014-8-18
		//管理员或超级管理员权限才能操作
		//if(account.getParentId()!= 0){
		if(account.getRole().getRoleId() == 3){
//			if(cmd.equalsIgnoreCase("listOperator")||cmd.equalsIgnoreCase("saveOperator")||cmd.equalsIgnoreCase("delOperator")
//				||cmd.equalsIgnoreCase("listTeam")||cmd.equalsIgnoreCase("saveTeam")||cmd.equalsIgnoreCase("delTeam")
//				||cmd.equalsIgnoreCase("saveDriver")||cmd.equalsIgnoreCase("delDriver")){
//				return false;
//			}else{
				return true;
//			}
		}else{
			return true;
		}
	}

	/**
	 * @author Administrator
	 * @date Jul 2, 2013
	 * @param cmd
	 * @return
	 * @Description 验证CMD是否需要验证登录登录
	 */
	private boolean checkCMD(String cmd){
		if(EnumHTTP.ACCOUNT_LOGIN.equalsIgnoreCase(cmd) || EnumHTTP.ACCOUNT_SAVE.equalsIgnoreCase(cmd)
				|| EnumHTTP.ACCOUNT_CHECK.equalsIgnoreCase(cmd)){
			return false;
		}
		if(EnumHTTP.ACCOUNT_CHECK_NAME.equals(cmd)||EnumHTTP.ACCOUNT_CHECK_EMAIL.equals(cmd)||EnumHTTP.ACCOUNT_UPDATE_EMAIL.equals(cmd)){
			return false;
		}
		//忘记密码操作账号激活操作
		if(EnumHTTP.ACCOUNTNAME_CHECK .equals(cmd)||EnumHTTP.ACCOUNT_SENDPWD_EMAIL.equals(cmd)||
				EnumHTTP.ACCOUNT_CHECK_RANDOMCODE.equals(cmd)||EnumHTTP.ACCOUNT_RESET_PASSWORD.equals(cmd)||EnumHTTP.ACCOUNT_ACTIVATE_EMAIL.equals(cmd)||EnumHTTP.ACCOUNT_ACTIVATE_HREF.equals(cmd)){
			return false;
		}
		if(EnumHTTP.ACCOUNT_LOGOUT.equalsIgnoreCase(cmd)){
			return false;
		}
		return true;
	}
	
	/**
	 * @author Administrator
	 * @date Nov 4, 2013
	 * @param json
	 * @Description 将信息写进前端的方法，增加手机客户端请求敏感数据的时候需要加密
	 * @see com.chanceit.framework.action.BaseAction#writeJson2Page(java.lang.String)
	 */
	@Override
	public void writeJson2Page(String json){
		if(checkCMDMobile(cmd)){ //如果发现是手机客户端请求，且信息敏感，则会加密数据
			super.writeJson2Page(json, null);
		} else {
			super.writeJson2Page(json);
		}
	}
	
	//判断是否为手机客户端访问
	public boolean ifApp(String ifapp){
		if(ifapp != null && !"".equals(ifapp) && "1".equals(ifapp)){
			return false;
		}
		return true;
	}
	
	
	private boolean checkCMDMobile(String cmd){
		Pattern p = Pattern.compile(MOBILE_REGEXT);
		Matcher m = p.matcher(cmd);
		return m.find();
	}

	/**
	 * @return the cmd
	 */
	public String getCmd() {
		return cmd;
	}

	/**
	 * @param cmd the cmd to set
	 */
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
}
