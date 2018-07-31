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
 * @Description �ͻ��˷����ӿ���
 */
@Results( {
	@Result(name = "login", location = "login.html"),
	@Result(name = "index", location = "index.jsp")
})
public class ApiAction extends BaseAction{

	/**
	 * @Fields serialVersionUID ���к�
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
	 * @Description TODO(������һ�仰�����������������)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {
		//System.out.println(getRequest().getRequestURI()+"...cmd:"+cmd);
		
		
		Account account = getSessionAccount();
		String ifapp = getParameter("ifapp");
		if(checkCMD(cmd) && ifApp(ifapp) && !EnumHTTP.CAR_ACTION.equals(cmd) &&
				!EnumHTTP.WRITECARD.equals(cmd) && !EnumHTTP.READCARD.equals(cmd) &&
				!EnumHTTP.OPERATECARSTATE.equals(cmd)){//�����Ҫ��֤��¼״̬��CMD��¼״̬
			if(account == null){
				//Account account1 = accountService.getByName("sys");getSession().setAttribute("account", account1);
				writeJson2Page(ResultManager.getFaildResult(EnumCommon.EXCEPTION_NO_SESSION,"��δ��¼ϵͳ��ʱ��δ����"));
				return null;
			}
			if(!checkRight(cmd) ){//���Ȩ���Ƿ�ȱʧ
				writeJson2Page(ResultManager.getFaildResult(EnumCommon.EXCEPTION_RIGHT,"����Ȩִ�иò���"));
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
			if(EnumHTTP.SAVEOPERATOR.equals(cmd)){	//�༭����Ա
				writeJson2Page(accountView.saveOperator());
			} else 	
			if(EnumHTTP.LISTOPERATOR.equals(cmd)){	//����Ա�б�
				writeJson2Page(accountView.list(page));
			} else 
			if(EnumHTTP.DELOPERATOR.equals(cmd)){	//ɾ����Ա
				writeJson2Page(accountView.delete());
			} else 	
			if(EnumHTTP.LISTDRIVER.equals(cmd)){	//˾���б�
				writeJson2Page(driverView.list(page));
			} else 	
			if(EnumHTTP.SAVEDRIVER.equals(cmd)){	//����˾��
				writeJson2Page(driverView.saveDriver());
			} else
			if(EnumHTTP.DELDRIVER.equals(cmd)){	//ɾ��˾��
				writeJson2Page(driverView.delete());
			} 
			else 
			if(EnumHTTP.UNBINDDRIVERS.equals(cmd)){	//δ���˾��
				writeJson2Page(driverView.unbindDrivers());
			}else
			if(EnumHTTP.LISTDRIVERLOG.equals(cmd)){
				writeJson2Page(driverView.getDriverLogPage(page));		//˾�������б�
			}else 
			if(EnumHTTP.DRIVERDATETRACE.equals(cmd)){
				writeJson2Page(driverView.getPoint());
			}else
			if(EnumHTTP.LISTABNORMALOG.equals(cmd)){
				writeJson2Page(driverView.getAbnormaDriverLogPage(page));	//�Ƿ��ó�˾���б�
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
			if(EnumHTTP.SAVETEAM.equals(cmd)){			//���ӳ����ӿڿ�ʼ
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
			if(EnumHTTP.LISTVEHICLE.equals(cmd)){//�����ӿڿ�ʼ
				writeJson2Page(userView.pageList(page));
			} else 
			if(EnumHTTP.LISTMONITORVEHICLE.equals(cmd)){//��س����б�
				writeJson2Page(userView.list());
			} else 
			if(EnumHTTP.SAVEMONITORVEHICLES.equals(cmd)){//�������ü�س���
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
				writeJson2Page(userView.vehiclesOnlineCount());//��������
			}else if(EnumHTTP.MAPDATA.equals(cmd)){
				writeJson2Page(userView.mapdata());
			}else if(EnumHTTP.VEHICLEONLINES.equals(cmd)){
				writeJson2Page(userView.getOnlineList());
			}else if(EnumHTTP.MONITDATA.equals(cmd)){
				writeJson2Page(userView.getLocationByUids());
			}
			//add by zhouhui 2014-10-22 ���Ͷϵ糵����ʾ
			else if(EnumHTTP.RELAYCOUNT.equals(cmd)){
				writeJson2Page(userView.vehiclesRelayCount());//�̵����豸����
			}else if(EnumHTTP.RELAYOFFS.equals(cmd)){
				writeJson2Page(userView.getRelayList(page));
				
			}else if(EnumHTTP.LISTALARM.equals(cmd)){ //�����б�
				writeJson2Page(boxWarnView.getPagelist(page));
			}else if(EnumHTTP.HISTORYALARM.equals(cmd)){ //������ϸ�б�
				writeJson2Page(boxWarnView.getlist(page));
			}else if(EnumHTTP.GETALARMCOUNT.equals(cmd)){ //��ȡ������������
				writeJson2Page(boxWarnView.getAlarmCount());
			}else if(EnumHTTP.LISTABNORMAL.equals(cmd)){ // �쳣�����б�
				writeJson2Page(warnView.getPagelist(page));
			}else if(EnumHTTP.HISTORYABNORMAL.equals(cmd)){ //�쳣������ϸ�б�
				writeJson2Page(warnView.getlist(page));
			}else if(EnumHTTP.GETABNORMALCOUNT.equals(cmd)){ //��ȡ�쳣������������
				writeJson2Page(warnView.getAlarmCount());
			}else if(EnumHTTP.DATEEVENT.equals(cmd)){ //��ȡĳ�������¼��б�
				writeJson2Page(boxWarnView.getDateEvent());
			}
			//add by zhangxin 2014-6-27
			//Ԥ����Ϣ����߼�
			//����Ԥ����Ϣ
			else if(EnumHTTP.SAVEPRESTOREMSG.equals(cmd)){
				writeJson2Page(prestoreMsgView.save());
			} 
			//��ȡԤ����Ϣ�б�
			else if(EnumHTTP.LISTPRESTOREMSG.equals(cmd)){
				writeJson2Page(prestoreMsgView.list(page));
			} 
			//ɾ��Ԥ����Ϣ�б�
			else if(EnumHTTP.DELPRESTOREMSG.equals(cmd)){
				writeJson2Page(prestoreMsgView.delete());
			} 
			//end
			
			//add by zhangxin 2014-6-30
			//��Ϣ��������߼�
			//��ȡ��Ϣ�����б�
			else if(EnumHTTP.LISTMSGSESSION.equals(cmd)){
				writeJson2Page(msgCenterView.list(page));
			} 
			//������Ϣ
//			else if(EnumHTTP.SENDMSGCENTER.equals(cmd)){
//				writeJson2Page(msgCenterView.save());
//			} 
			//������Ϣ
			else if(EnumHTTP.GETMSGSESSION.equals(cmd)){
				writeJson2Page(msgCenterView.getlist(page));
			} 
			//end
			
			//add by zhangxin 2014-7-14
			//ָ���б�
			else if(EnumHTTP.LISTCMDINFO.equals(cmd)){
				writeJson2Page(boxWarnView.cmdlist());
			} 
			//end
			
			//add by zhangxin 2014-7-16
			//��ȡλ�õ���Ϣ
			else if(EnumHTTP.DATETRACE.equals(cmd)){
				writeJson2Page(userView.getPoint());
			} 
			//end
			
			//add by zhangxin 2014-7-18
			//��ȡ�г��ռ�
			else if(EnumHTTP.MONTHDRIVELOGS.equals(cmd)){
				writeJson2Page(userView.getDiaryPoint());
			} 
			//��ȡ�г��ռ�
			else if(EnumHTTP.YEARDRIVELOGS.equals(cmd)){
				writeJson2Page(userView.getYearDiaryPoint());
			} 
			//end
			
			//add by zhangxin 2014-08-12
			//����˾�������б�
			else if(EnumHTTP.LISTCARDRIVER.equals(cmd)){
				writeJson2Page(carDriverView.list(page));
			} 
			//���泵��˾������
			else if(EnumHTTP.SAVECARDRIVER.equals(cmd)){
				writeJson2Page(carDriverView.saveCarDriver());
			} 
			//ɾ������˾������
			else if(EnumHTTP.DELCARDRIVER.equals(cmd)){
				writeJson2Page(carDriverView.delete());
			} 
			//д��
			else if(EnumHTTP.WRITECARD.equals(cmd)){
				writeJson2Page(carDriverView.writeCard());
			} 
			//����
			/*else if(EnumHTTP.READCARD.equals(cmd)){
				carDriverView.valSignIn("194515836","e5286eb2","hsj01000",new Date());
				writeJson2Page("");
			} */
			//�������
			else if(EnumHTTP.DOSCAN.equals(cmd)){
				String result = pcodeCensusView.getDetectionInfo();
				if("0".equals(result)){
					writeJson2Page(ResultManager.getFaildResult("û�г���"));
				}else if("1".equals(result)){
					writeJson2Page(ResultManager.getFaildResult("û�г���"));
				}else{
					writeJson2Page(result);
				}
				
			} 
			//������ⱨ��
			else if(EnumHTTP.DETECTIONDIARY.equals(cmd)){
				String result = pcodeCensusView.getDetectionDiary();
				if("1".equals(result)){//û��team
					writeJson2Page(ResultManager.getFaildResult("û�г���"));
				}else if("0".equals(result)){
					//û�г���
					writeJson2Page(ResultManager.getFaildResult("û�г���"));
				}else{
					writeJson2Page(result);
				}
			} 
			//���Ӱ󶨹�ϵ�б�
			else if(EnumHTTP.LISTTEAMSANDOPERATOR.equals(cmd)){
				writeJson2Page(opTeamView.getGroupList());
			//����Ա���Ӱ󶨹�ϵ�б�
			}else if(EnumHTTP.LISTOPTEAM.equals(cmd)){
				writeJson2Page(opTeamView.list());
			//���Ӱ󶨹�ϵ�޸�
			}else if(EnumHTTP.UPDATEOPTEAM.equals(cmd)){
				writeJson2Page(opTeamView.upDateOpTeam());
			//���Ͷϵ�
			}else if(EnumHTTP.OPERATECAR.equals(cmd)){
				writeJson2Page(userView.powerOffAndFuelCut());
			//���Ͷϵ���Ϣ����
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
			else if(EnumHTTP.OBDSWITCH.equals(cmd)){			//����/�ر�OBDͨѶ
				writeJson2Page(faultCheckView.getObdState());
			}else if(EnumHTTP.OBDCODE.equals(cmd)){
				writeJson2Page(faultCheckView.getObdCode());	//��ȡOBD������
			}else if(EnumHTTP.SENDOBDDATA.equals(cmd)){
				writeJson2Page(faultCheckView.getObdData());	//��ȡOBDʵʱ����	 
			}
			//add by shaozheng 2014-08-29
			else if(EnumHTTP.SAVEPARKING.equals(cmd)){			//����/�޸ĳ�����Ϣ
				writeJson2Page(parkingView.saveParking());		
			}else if(EnumHTTP.DELPARKING.equals(cmd)){			//ɾ��������Ϣ
				writeJson2Page(parkingView.deleteParking());
			}else if(EnumHTTP.LISTPARKING.equals(cmd)){			//��ȡ������Ϣ
				writeJson2Page(parkingView.parkingPageList(page));
			}else if(EnumHTTP.LISTPARKINGCAR.equals(cmd)){		//��ȡ���⳵����Ϣ
				writeJson2Page(parkingView.userPageList());	
			}else if(EnumHTTP.LISTPLATE.equals(cmd)){		//��ȡ����������Ϣ�ĳ���(��ǰteam����״̬�ĳ�)
				writeJson2Page(baseInfoView.plateList());
			}else if(EnumHTTP.LISTPLATEALL.equals(cmd)){	//��ȡ����������Ϣ�ĳ���(��ǰteam����״̬�ĳ�)
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
			
/************************************��׿APP�ͻ�����ؽӿ�********************************************************/
			//add by zhangxin 2014-09-21 ��׿�ͻ�����ؽӿ�
			//��¼
			else if(EnumHTTP.APPLOGIN.equals(cmd)){
				writeJson2Page(appView.loginForApp());
			}
			//�����б�
			else if(EnumHTTP.APPLISTTEAM.equals(cmd)){
				writeJson2Page(appView.listForApp());
			}
			//����б�
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
//			else if(EnumHTTP.CYCLEDAYSUMMARY.equals(cmd)){//���ڱ����е��ձ���
//				writeJson2Page(reportView.listCycleReport());
//			}else if(EnumHTTP.CYCLEDAYSUMMARYDETAIL.equals(cmd)){//���ڱ����е��ձ�����ϸ�б�
//				writeJson2Page(reportView.listCycleMileageOil());
//			}
	/*******************************************************************************/
		  else if(EnumHTTP.CYCLEMONTHSUMMARY.equals(cmd)){//���ڱ����е��±���
				writeJson2Page(reportView.listCycleMileageOilByMonth());
			}else if(EnumHTTP.EXPORTCYCLEDAYSUMMARY.equals(cmd)){//���ڱ����е��ձ�����excel
				reportView.exportCycle();
			}else if(EnumHTTP.EXPORTCYCLEMONTHSUMMARY.equals(cmd)){//���ڱ����е��±�����excel
				reportView.exportCycleMonth();
			}
			//add by gwt 2015-01-04
			/******************����**************************/
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
				writeJson2Page(formsView.getTeamCar());   //�õ����ӳ���
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
			
			/****************��������ӿ�**********************/
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
			
			else if(EnumHTTP.MEMBEREXCEL.equals(cmd)){	//�˿ͱ��� ����
				writeJson2Page(excelFormsView.memberExcel());
			}
			else if(EnumHTTP.DRIVEREXCEL.equals(cmd)){	//�˿ͱ��� ����
				writeJson2Page(excelFormsView.driverExcel());
			}
			else if(EnumHTTP.CAREXCEL.equals(cmd)){	//�������� ����
				writeJson2Page(excelFormsView.carExcel());
			}
			
			
			//add by gwt 2015-01-04
			/****************���********************/
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
			
			//���ٱ������ٶ�����
			else if(EnumHTTP.OVERSPEED.equals(cmd)){
				writeJson2Page(userView.overSpeed());
			}
			//λ�õ��ϴ�ʱ����
			else if(EnumHTTP.POSTIME.equals(cmd)){
				writeJson2Page(userView.posTime());
			}else if(EnumHTTP.LISTBRANDBYSTYLE.equals(cmd)){
				writeJson2Page(userView.listBrandBySytle());
			}
			
			
			/*******************����ƽ̨�ںϽӿ�*************/
			/**************************************************/
			else if(EnumHTTP.LISTORDER.equals(cmd)){	//�����б�
				writeJson2Page(orderView.list(page));
			}
			else if(EnumHTTP.ADDORDER.equals(cmd)){	//����ԤԼ����
				writeJson2Page(orderView.addOrder());
			}
			else if(EnumHTTP.LISTMEMBER.equals(cmd)){	//�ó����б�
				writeJson2Page(memberView.list(page));
			}
			else if(EnumHTTP.SAVEMEMBER.equals(cmd)){	// ����/�޸��ó���
				writeJson2Page(memberView.saveMember());
			}
			else if(EnumHTTP.DELMEMBER.equals(cmd)){	// ɾ���ó���
				writeJson2Page(memberView.delMember());
			}
			else if(EnumHTTP.GETDEPTTEAMS.equals(cmd)){	//�˿��б�
				writeJson2Page(deptView.getDeptTeams());
			}else if(EnumHTTP.GETDEPTLIST.equals(cmd)){	//�����б�
				writeJson2Page(deptView.getDeptList(page));
			}else if(EnumHTTP.SAVEDEPT.equals(cmd)){	//���沿��
				writeJson2Page(deptView.save());
			}else if(EnumHTTP.DELETEDEPT.equals(cmd)){	//ɾ������
				writeJson2Page(deptView.deleteDept());
			}else if(EnumHTTP.GETLEAVELS.equals(cmd)){	//��ɫ
				writeJson2Page(accountView.getLeavels());
			}else if(EnumHTTP.EDITORDER.equals(cmd)){	//��ɫ
				writeJson2Page(orderView.editOrder());
			}else if(EnumHTTP.LISTASKLEAVE.equals(cmd)){	//����б�
				writeJson2Page(askleaveView.list(page));
			}
			else if(EnumHTTP.AUDITASKLEAVE.equals(cmd)){	//�������
				writeJson2Page(askleaveView.auditAskleave());
			}
			else if(EnumHTTP.LISTBAOXIAO.equals(cmd)){	//�����б�
				writeJson2Page(baoxiaoView.list(page));
			}
			else if(EnumHTTP.AUDITBAOXIAO.equals(cmd)){	//��������
				writeJson2Page(baoxiaoView.auditBaoxiao());
			}
			else if(EnumHTTP.ADDBAOXIAO.equals(cmd)){	//��������
				writeJson2Page(baoxiaoView.addBaoxiao());
			}
			
			else if(EnumHTTP.LISTCARUSES.equals(cmd)){	//�ó����� �б�
				writeJson2Page(carusesView.list(page));
			}
			else if(EnumHTTP.SAVECARUSES.equals(cmd)){	//�ó����� ���ӻ��޸�
				writeJson2Page(carusesView.saveCaruses());
			}
			else if(EnumHTTP.DELETECARUSES.equals(cmd)){	//�ó����� ɾ��
				writeJson2Page(carusesView.delete());
			}
			
			else if(EnumHTTP.LISTCARSTYLE.equals(cmd)){	//���� �б�
				writeJson2Page(carstyleView.list(page));
			}
			else if(EnumHTTP.SAVECARSTYLE.equals(cmd)){	//���� ���ӻ��޸�
				writeJson2Page(carstyleView.saveCarstyle());
			}
			else if(EnumHTTP.DELETECARSTYLE.equals(cmd)){	//����  ɾ��
				writeJson2Page(carstyleView.delete());
			}
			
			else if(EnumHTTP.LISTBRAND.equals(cmd)){	//Ʒ�� �б�
				writeJson2Page(brandView.list());
			}
			else if(EnumHTTP.LISTBRANDPAGE.equals(cmd)){	//Ʒ�� ��ҳ�б�
				writeJson2Page(brandView.listPage(page));
			}
			else if(EnumHTTP.SAVEBRAND.equals(cmd)){	//Ʒ�� ���ӻ��޸�
				writeJson2Page(brandView.saveBrand());
			}
			else if(EnumHTTP.DELETEBRAND.equals(cmd)){	//Ʒ�� ɾ��
				writeJson2Page(brandView.delete());
			}else if(EnumHTTP.GETORDERCOUNT.equals(cmd)){	//
				writeJson2Page(orderView.getOrderCount());
			}else if(EnumHTTP.GETCARSTYLE.equals(cmd)){	//
				writeJson2Page(carstyleView.getCarStyle());
			}	else if(EnumHTTP.LISTWEIXIU.equals(cmd)){	//ά���б�
				writeJson2Page(weixiuView.list(page));
			}
			else if(EnumHTTP.AUDITWEIXIU.equals(cmd)){	//����ά��
				writeJson2Page(weixiuView.auditWeixiu());
			}
			else if(EnumHTTP.RESETMEMBER.equals(cmd)){	//�˿���������
				writeJson2Page(memberView.resetMember());
			}
			else if(EnumHTTP.RESETDRIVER.equals(cmd)){	//˾����������
				writeJson2Page(driverView.resetDriver());
			}
			else if(EnumHTTP.RESETACCOUNT.equals(cmd)){	//����Ա�˻���������
				writeJson2Page(accountView.resetAccount());
			}
			
			else if(EnumHTTP.LISTWEIBAO.equals(cmd)){	//ά���б�
				writeJson2Page(weibaoView.list(page));
			}
			else if(EnumHTTP.SAVEWEIBAO.equals(cmd)){	//ά���༭������
				writeJson2Page(weibaoView.saveWeibao());
			}
			else if(EnumHTTP.DELWEIBAO.equals(cmd)){	//ά��ɾ��
				writeJson2Page(weibaoView.delete());
			}
			
			else if(EnumHTTP.LISTWEIZHANG.equals(cmd)){	//ά���б�
				writeJson2Page(weizhangView.list(page));
			}
			else if(EnumHTTP.SAVEWEIZHANG.equals(cmd)){	//ά�±༭������
				writeJson2Page(weizhangView.saveWeizhang());
			}
			else if(EnumHTTP.DELWEIZHANG.equals(cmd)){	//ά��ɾ��
				writeJson2Page(weizhangView.delete());
			}
			
			//ͳ�Ʋ���
			else if(EnumHTTP.REPORTCAR.equals(cmd)){	//����ͳ��
				writeJson2Page(reportNewView.listCarReport());
			}
			else if(EnumHTTP.REPORTMONTH.equals(cmd)){	//���·�ͳ��
				writeJson2Page(reportNewView.listMonthReport());
			}
			else if(EnumHTTP.REPORTDRIVER.equals(cmd)){	//˾��ͳ��
				writeJson2Page(reportNewView.listDriverReport());
			}
			else if(EnumHTTP.REPORTMEMBER.equals(cmd)){	//�˿�ͳ��
				writeJson2Page(reportNewView.listMemberReport());
			}
			else if(EnumHTTP.REPORTMONTHDETAIL.equals(cmd)){	//�·���ϸ����
				writeJson2Page(reportNewView.listMonthReportDetail());
			}else if(EnumHTTP.GETWEIBAOEND.equals(cmd)){	//�·���ϸ����
				writeJson2Page(weibaoView.getCount());
			}
			
			
			else if(EnumHTTP.UPDATETEAM.equals(cmd)){	//�϶����ͽṹ �޸ĳ���team
				writeJson2Page(userView.updateTeam());
			}
			
			
			
			
			/*******************�����ó�APP�ӿ�*************/
			/**************************************************/
			else if(EnumHTTP.LOGINFORGOV.equals(cmd)){	//��¼
				writeJson2Page(govAppView.LoginForGov());
			}
			else if(EnumHTTP.MEMBERREGISTER.equals(cmd)){	//�˿�ע��
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
				writeJson2Page(ResultManager.getFaildResult(ResultManager.EXCEPTION_404,"�ӿڷ����Ҳ���"));
			}
		} catch (NumberFormatException e) {
			writeJson2Page(ResultManager.getFaildResult("����ת���쳣"));
		} catch (CommonException e) {
			writeJson2Page(ResultManager.getFaildResult(e.getMessage()));
		} catch (RegularException e) {
			writeJson2Page(ResultManager.getFaildRegularResult(e.getMessage()));
		}catch (ObjectNotFoundException oe){
			writeJson2Page(ResultManager.getFaildResult(ResultManager.EXCEPTION_500,"û�ж�Ӧ�Ĺ�˾������ϵϵͳ����Ա"));
		}
		
		catch (Exception e) {
			logger.warn("ϵͳ����δ֪�쳣����Ϣ��ʾ�����"+e.getMessage()+e.getStackTrace()+"cmd="+cmd);
			writeJson2Page(ResultManager.getFaildResult(ResultManager.EXCEPTION_500,"ϵͳ�쳣"));
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
		//����Ա�򳬼�����ԱȨ�޲��ܲ���
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
	 * @Description ��֤CMD�Ƿ���Ҫ��֤��¼��¼
	 */
	private boolean checkCMD(String cmd){
		if(EnumHTTP.ACCOUNT_LOGIN.equalsIgnoreCase(cmd) || EnumHTTP.ACCOUNT_SAVE.equalsIgnoreCase(cmd)
				|| EnumHTTP.ACCOUNT_CHECK.equalsIgnoreCase(cmd)){
			return false;
		}
		if(EnumHTTP.ACCOUNT_CHECK_NAME.equals(cmd)||EnumHTTP.ACCOUNT_CHECK_EMAIL.equals(cmd)||EnumHTTP.ACCOUNT_UPDATE_EMAIL.equals(cmd)){
			return false;
		}
		//������������˺ż������
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
	 * @Description ����Ϣд��ǰ�˵ķ����������ֻ��ͻ��������������ݵ�ʱ����Ҫ����
	 * @see com.chanceit.framework.action.BaseAction#writeJson2Page(java.lang.String)
	 */
	@Override
	public void writeJson2Page(String json){
		if(checkCMDMobile(cmd)){ //����������ֻ��ͻ�����������Ϣ���У�����������
			super.writeJson2Page(json, null);
		} else {
			super.writeJson2Page(json);
		}
	}
	
	//�ж��Ƿ�Ϊ�ֻ��ͻ��˷���
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
