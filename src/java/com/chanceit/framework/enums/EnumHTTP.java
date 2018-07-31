package com.chanceit.framework.enums;

public class EnumHTTP {
	public static final String ACCOUNT_CHECK = "account_check"; //账号名检测
	public static final String ACCOUNT_SAVE = "account_save";     //添加账号
	
	public static final String ACCOUNT_VIEW = "account_view";   //显示账号信息
	public static final String ACCOUNT_VIEWOWN = "account_viewown";   //显示自身账号信息
	public static final String ACCOUNT_LOGIN = "login"; //账号登录
	public static final String ACCOUNT_DEL = "account_del";     //删除账号
	public static final String ACCOUNT_RESET = "account_reset"; //账户重置
	public static final String ACCOUNT_SET = "account_set";     //密码修改
	public static final String ACCOUNT_LOGOUT = "logout";  //用户登出
	
	public static final String ACCOUNT_CHECK_NAME="check_accountName";//判断账号唯一 异步
	public static final String ACCOUNT_CHECK_EMAIL="check_email";//判断email是否验证 异步
	
	public static final String ACCOUNT_UPDATE_EMAIL="update_email";//更新账号邮箱
	
	public static final String ACCOUNTNAME_CHECK="accountName_check";//判断账号是否存在
	public static final String ACCOUNT_SENDPWD_EMAIL="forgetPwd_sendMail";//忘记密码发送验证码
	public static final String ACCOUNT_CHECK_RANDOMCODE="check_verificationCode";//验证邮箱发送的验证码
	public static final String ACCOUNT_RESET_PASSWORD="repwd";//邮箱重置密码
	
	public static final String ACCOUNT_ACTIVATE_EMAIL="send_activete_mail";//发送账号激活邮件
	public static final String ACCOUNT_ACTIVATE_HREF="href_account_activate";//激活账号
	
	public static final String USER_CHECK = "user_check"; //用户检测
	public static final String CAR_VIEW = "car_view";   //显示车辆
	public static final String CHECK_UID  = "check_uid";//验证uid
	
	public static final String SHOP_USER_INFO = "1000";             //通过用户ID获得4S店资料信息
	
	public static final String SHOP_TEL = "1001";				    //通过4S店ID获得4S店救援电话信息
	
	public static final String NEWS_LIST = "2000";                  //获取新闻列表信息
	
	public static final String NEWS_TITLE_LIST = "2001";		    //获得首页图片新闻
	
	public static final String SHOW_STATUS = "3000";                //获取OBD状态
	public static final String SHOW_OBD = "3001";                   //获得OBD信息
	public static final String SAVE_USER_EXPAND = "3002";           //保存个人设置信息
	public static final String SAVE_USER_REMIND = "3003";           //保存个人提醒信息
	public static final String GET_USER_EXPAND = "3004";            //获取个人设置信息
	public static final String GET_USER_REMIND = "3005";            //获取个人提醒信息
	
	public static final String PRODUCT_LIST = "4000";               	 //获取商品信息
	public static final String PRODUCT_SECONDCAR_LIST = "4001";     	 //获取二手车商品信息
	public static final String PRODUCT_SHOP_LIST = "4002";               //获取商品信息
	public static final String PRODUCT_SHOP_SECONDCAR_LIST = "4003";     //获取二手车商品信息
	
	public static final String SAVE_MILEAGE = "5000";              	//导航盒子接口/保存盒子里程信息
	
	public static final String CAR_PAGE = "6000";               	//车型列表
	public static final String SERIES_PAGE = "6001";				//车系列表
	public static final String BRAND_LIST = "6002";			    	//品牌列表
	
	//-----------------------------------------
	public static final String GOLBAL = "golbal";     //
	public static final String SET_PWD = "setpwd";//修改密码
	public static final String SAVECOMPANY = "saveCompany";//超级管理员：新增 编辑企业信息
	public static final String SETCOMPANY = "setCompany";//编辑自己企业信息
	public static final String LISTCOMPANY = "listCompany";//企业信息列表
	public static final String DELCOMPANY = "delCompany";//删除企业信息
	
	public static final String SAVEOPERATOR = "saveOperator";//添加操作员信息 编辑操作员信息 整合
	public static final String LISTOPERATOR = "listOperator";   //操作员列表
	public static final String DELOPERATOR = "delOperator";   //删除操作员列表
	
	public static final String LISTTEAM = "listTeam";   //车队列表
	public static final String SAVETEAM = "saveTeam";   //添加车队
	public static final String DELTEAM = "delTeam";   //删除车队
	public static final String TEAMVEHICLES = "teamVehicles";     //当前车队分组车辆，与 未分组车辆 列表
	
	public static final String LISTDRIVER = "listDriver";   //司机列表
	public static final String SAVEDRIVER = "saveDriver";   //保存编辑司机
	public static final String DELDRIVER = "delDriver";     //删除司机
	public static final String UNBINDDRIVERS = "unbindDrivers";     //未绑的司机
	public static final String LISTDRIVERLOG = "listDriverLog";		//司机考勤列表
	public static final String DRIVERDATETRACE = "driverDateTrace";	//获得司机考勤历史轨迹
	public static final String CARANDDRIVER = "carAndDriver";		//司机正在开的车
	public static final String LISTABNORMALOG = "listAbnormaLog";	//获取非法使用汽车的司机列表
	public static final String GETABNORMALLOGCOUNT ="getAbnormalLogCount"; //获取非法使用车辆报警个数
	public static final String DRIVERDETAIL = "driverDetail";
	public static final String TELPHONEODER = "telphoneOder";		//获取断电/供电指令电话
	
	public static final String LISTVEHICLE = "listVehicle";   //车辆用户列表
	public static final String SAVEVEHICLE = "saveVehicle";     //添加编辑
	public static final String DELVEHICLE = "delVehicle";     //删除车辆
	public static final String LISTMONITORVEHICLE = "listMonitor";   //监控的车辆用户列表
	public static final String SAVEMONITORVEHICLES = "saveMonitor";   //批量保存监控的车辆列表
	public static final String VEHICLECOUNT = "vehicleCount";   //车辆数量和在线数量
	public static final String VEHICLEONLINES = "vehicleOnlines";//在线列表
	
	public static final String SENDMSG  = "sendMsg";//发送消息
	public static final String SENDPOINT = "sendPoint";//发送位置点
	public static final String MAPDATA  = "mapData";//百度地图，显示单个车辆信息
	public static final String CAR_ACTION = "car_action";     // 老得socket批量获取位置点
	
	public static final String GETALARMCOUNT = "getAlarmCount";//获取报警车辆总数
	public static final String LISTALARM = "listAlarm";//报警车辆列表
	public static final String HISTORYALARM = "historyAlarm";//车辆报警历史
	public static final String GETABNORMALCOUNT = "getAbnormalCount";//获取异常参数车辆总数
	public static final String LISTABNORMAL = "listAbnormal";//异常参数车辆列表
	public static final String HISTORYABNORMAL = "historyAbnormal";//异常参数提醒历史
	public static final String DATEEVENT = "dateEvent";//提醒事件
	
	//add by zhangxin 2014-6-27
	public static final String SAVEPRESTOREMSG = "savePreStoreMsg";     //保存预存信息
	public static final String LISTPRESTOREMSG = "listPrestoreMsg";     //获取预存信息列表
	public static final String DELPRESTOREMSG = "delPreStoreMsg";       //删除预存信息
	
	//add by zhangxin 2014-6-30
	public static final String SENDMSGCENTER  = "sendMsgCenter";//发送消息
	public static final String LISTMSGSESSION  = "listMsgSession";//消息列表
	public static final String GETMSGSESSION  = "getMsgSession";//消息列表
	
	//add by zhangxin 2014-7-14
	public static final String LISTCMDINFO  = "listCmdInfo";//发送消息
	
	//add by zhangxin 2014-7-15
	public static final String DATETRACE  = "dateTrace";//获取历史轨迹点
	
	//add by zhangxin 2014-7-18
	public static final String MONTHDRIVELOGS  = "monthDriveLogs";//行车日记(月)
	
	//add by zhangxin 2014-7-29
	public static final String YEARDRIVELOGS  = "yearDriveLogs";//行车日记(年)
	
	//add by zhangxin 2014-8-12
	public static final String LISTCARDRIVER  = "listCarDriver";//车辆司机规则列表
	public static final String SAVECARDRIVER  = "saveCarDriver";//保存车辆司机规则
	public static final String DELCARDRIVER  = "delCarDriver";//删除车辆司机规则
	
	//add by zhangxin 2014-8-13
	//public static final String VALWRITECARD  = "valWriteCard";  //验证写卡
	public static final String WRITECARD  = "writeCard";     //写卡
	public static final String READCARD  = "readCard";       //读卡
	
	public static final String LISTADMIN = "listAdmin";   //管理员列表
	
	public static final String SENDOBDDATA	= "sendObdData"; //获取OBD实时数据
	
	public static final String OBDCODE	= "obdCode"; //获取OBD故障码
	
	public static final String OBDSWITCH = "obdSwitch"; //开启/关闭OBD通讯
	
	//add by zhangxin 2014-08-22
	public static final String DOSCAN = "doScan";  //检测列表
	public static final String DETECTIONDIARY = "detectionDiaryList";  //检测日志列表
	
	//add by zhangxin 2014-08-25
	public static final String LISTOPTEAM = "listOpTeam";  //管理员车队关系列表
	public static final String LISTTEAMSANDOPERATOR = "listTeamsAndOperator";  //车队绑定关系列表
	
	//add by shaozheng 2014-08-26
	public static final String UPDATEOPTEAM = "updateOpTeam";	//车队绑定关系修改
	
	//add by shaozheng 2014-08-29
	public static final String SAVEPARKING = "saveParking";			//保存/修改车库信息
	public static final String DELPARKING = "delParking"; 			//删除车库信息
	public static final String LISTPARKING = "listParking"; 		//获取车库信息
	public static final String LISTPARKINGCAR = "listParkingCar";	//获取车库车辆信息
	
	//add by shaozheng 2014-09-05
	public static final String LISTPLATE = "listPlate";			//获取车辆基本信息的车牌(当前team空闲状态的车)
	public static final String LISTPLATEALL = "listPlateAll";	//获取车辆基本信息的车牌(当前team所有状态的车)
	public static final String SAVEBASEINFO = "saveBaseInfo";	//保存/修改车辆基本信息
	public static final String LISTBASEINFO = "listBaseInfo";	//获取车辆基本信息列表
	public static final String DELBASEINFO = "delBaseInfo";		//删除车辆基本信息
	//public static final String LISTBRAND = "listBrand";			//品牌列表
	
	/**********************************************************************************
	/*********************app相关接口 无需session验证*******************************************/
	
	//add by zhangxin 2014-09-19
	public static final String APPLOGIN = "10000";       //登录接口
	public static final String APPLISTTEAM = "10001";    //车队列表
	public static final String APPLISTMONITOR = "10002";    //监控列表
	//add by gwt 2014-12-18
	public static final String APPITEMTEAM = "10003";    //某一个车队的车两列表
	public static final String APPITEMCAR = "10004";    //某一辆车的详情
	public static final String APPITEMTEAMNO = "10005"; //未分组车辆list
	public static final String APPGROUPLIST = "10006"; //
	public static final String APPWARNINGLIST = "10007"; //报警信息
	public static final String APPWARNINGROUPGLIST = "10008"; //单个报警信息
	public static final String APPADDTEAM = "10009";
	public static final String APPUNBANDINDRIVER = "100010";  //未绑定司机
	public static final String APPADDCAR = "100011";  //添加车辆
	public static final String APPEXCEPTION = "100012";  //异常参数
	public static final String APPEXCEPTIONLIST = "100013";  //异常参数明细
	public static final String APPDRIVERLIST = "100014";  //司机列表
	public static final String APPADDDRIVER = "100015";  //添加司机
	public static final String APPDRIVERDITEL = "100016";  //司机信息
	public static final String APPDELDRIVER = "100017";  //删除司机
	public static final String APPSETWARN = "100018";  //报警信息已读
	public static final String APPONLINE = "100019";  //在线车辆
	public static final String APPDRIVER = "100020";  //单个司机
	public static final String APPDELUSER = "100021";  //删除车辆
	public static final String APPWEILAN = "100022";  //围栏报警
	public static final String GETTEAMSFORCAR="100023";//app获取公司列表
	
	/************************************************************************/
	//add by zhangxin 2014-10-14
	public static final String OPERATECAR = "operateCar";  //断油断电
	public static final String OPERATECARSTATE = "operateCarState";  //断油断电
	
	//add by zhouhui 2014-10-22
	public static final String RELAYCOUNT = "relayCount";   //有继电器车辆数量和断开数量
	public static final String RELAYOFFS = "relayOffs";//车辆列表	
	
//	public static final String CYCLEDAYSUMMARY = "statsDaysMF";//周期报表中的日报表
//	public static final String CYCLEDAYSUMMARYDETAIL = "statsDateMF";//周期报表中的日报表详细列表
	public static final String CYCLEMONTHSUMMARY = "statsMonthMF";//周期报表中的月报表
	public static final String EXPORTCYCLEDAYSUMMARY = "exportCycleDaySummary";//周期报表中的日报表导出excel
	public static final String EXPORTCYCLEMONTHSUMMARY = "exportCycleMonthSummary";//周期报表中的月报表导出excel

    /*****************************************************************************************/
	/**************************报表相关  add by gwt 2015-01-04**********************************************************/
    public static final String FORMDRIVING = "formdDriving";  //总里程 总油耗 柱状图数据
    public static final String FORMLOCATION = "formdLocation";  //位置点数据
    public static final String FORMPATH = "formdPath";   //  行驶报表列表数据  行驶记录
    public static final String FORMOVERPATH = "formdOverPath";  //超速报表列表数据  
    public static final String FORMOBDLIST = "formObdList";   //里程报表列表数据
    public static final String FORMSTOP = "formStop";   //停止报表柱状图数据
    public static final String FORMSTOPLIST = "formStopList";  //停止报表列表数据
    public static final String FORMFIRE = "formFire";   //点火报表柱状图数据
    public static final String FORMFIRELIST = "formFireList";  //点火报表列表数据
    public static final String FORMOVER = "formOverSpeed";    //超速报表
    public static final String FORMTEAMS = "formTeamsCar";//获取车辆
    public static final String FORMDUANDIAN = "totalAlarms";//报警统计图
    public static final String FORMDUANDIANONE = "detailAlarms";//报警详情
    public static final String FORMDMONTH = "monthOnlines"; //月上线
    public static final String FORMDDAY = "dateOnlines";//日上线
    public static final String FORMDDATEFULE = "dateFuel";//日行油耗
    public static final String FORMDSTATSDAY = "statsTotalMF";//月统计
    public static final String FORMDSTATSMF= "statsDaysMF";//日统计
    
    /*****************导出excel表接口*******************/
    
    public static final String FORMDRIVINGEXCEL = "formdDriving_excel";  //总里程 总油耗 柱状图数据
    public static final String FORMLOCATIONEXCEL = "formdLocation_excel";  //位置点数据
    public static final String FORMPATHEXCEL = "formdPath_excel";   //  行驶报表列表数据  行驶记录
    public static final String FORMOVERPATHEXCEL = "formdOverPath_excel";  //超速报表列表数据  
    public static final String FORMOBDLISTEXCEL = "formObdList_excel";   //里程报表列表数据
    public static final String FORMSTOPEXCEL = "formStop_excel";   //停止报表柱状图数据
    public static final String FORMSTOPLISTEXCEL = "formStopList_excel";  //停止报表列表数据
    public static final String FORMFIREEXCEL = "formFire_excel";   //点火报表柱状图数据
    public static final String FORMFIRELISTEXCEL = "formFireList_excel";  //点火报表列表数据
    public static final String FORMDUANDIANEXCEL = "totalAlarms_excel";//报警统计图
    public static final String FORMDUANDIANONEEXCEL = "detailAlarms_excel";//报警详情
    public static final String FORMDMONTHEXCEL = "monthOnlines_excel"; //月上线
    public static final String FORMDDAYEXCEL = "dateOnlines_excel";//日上线
    public static final String FORMDDATEFULEEXCEL = "dateFuel_excel";//日行油耗
    public static final String FORMDSTATSDAYEXCEL = "statsTotalMF_excel";//月统计
    public static final String FORMDSTATSMFEXCEL= "statsDaysMF_excel";//月列表
    
    public static final String MEMBEREXCEL="member_excel";//乘客报表
    public static final String DRIVEREXCEL="driver_excel";//司机报表
    public static final String CAREXCEL="car_excel";//车辆报表
    
    
    /********************广汇项目新接口**************************************************/
    public static final String GUANHUIPOST = "guanhuiTeamsCar";
    public static final String GUANHUISAVE = "ghsave";
    public static final String GUANHUISAVEX = "ghsavex";
    public static final String GUANHUILINES = "getLines";  //获取规划路径列表
    public static final String GUANHUILINESRULE = "getLineRules";  //获取绑定路劲的列表
    public static final String GUANHUISAVERULE = "saveRules";  //保存绑定路径
    public static final String GUANHUISAVELINE = "saveLine";   //保存规划路径
    public static final String GUANHUIDELLINE = "delLine"; 
    public static final String GUANHUIDERULE = "delRule"; 
    public static final String GUANHUILINEWARN = "limitAlarms"; 
    public static final String GUANHUILINEUPDTE = "readLimitAlarm"; 
    public static final String GUANHUILINECOUNT = "getLimitCount";  
    public static final String GUANHUILINELIMIT = "getLimitLines"; 
    public static final String GUANHUISAVECIRCLE = "saveCircle"; 
    public static final String GUANHUIGETCIRCLES = "getCircles"; 
    public static final String GUANHUIDELCIRCLES = "delCircle";
    public static final String GUANHUIGETCIRCLERULES = "getCircleRules";
    public static final String GUANHUISAVERULES0 = "saveRules0";
    public static final String GUANHUIDELRULES0 = "delRule0";
    public static final String GUANHUIDELRULES1 = "delRule1";
    public static final String GUANHUIGETPL = "getPolygons";
    public static final String GUANHUIGETPLR = "getPolygonRules";
    public static final String GUANHUISAVEPL = "savePolygon";
    public static final String GUANHUIDELPL = "delPolygon";
    public static final String GUANHUISAVERULES1 = "saveRules1";
    public static final String GUANHUITEST = "test";
    /***********新增接口****************/
    public static final String GETTEAMS = "getTeams"; 
    public static final String MONITDATA = "monitData";
    
    public static final String OVERSPEED = "overSpeed";//超速设置
    public static final String POSTIME= "posTime";//位置点上传时间间隔
    
    
    /*****************政务用车APP接口******************************/
    public static final String LOGINFORGOV = "loginForGov";//APP登录
    public static final String MEMBERREGISTER = "memberRegister";//乘客注册
    public static final String DRIVERREGISTER = "driverRegister";//司机注册
    public static final String APPGETTEAMS = "getCompanyForapp";//获取公司
    public static final String APPGETDEPT = "getDeptForapp";//获取部门
    public static final String APPGETLEAVE = "getAcLeaveForapp";//请假申请
    public static final String APPEDITLEAVE = "editLeaveForapp";//请假修改
    public static final String APPGETORDERS = "getOrdersForapp";//获取订单
    public static final String APPGETCARSTYLE = "getCarStyleForapp";//车辆类型
    public static final String APPGETCAURSE = "getCaurseForapp";//用车事由
    public static final String APPGETBRAND = "getBrandForapp";//品牌
    public static final String APPADDASKLEAVE = "addAskleaveForapp";//添加请假
    public static final String APPADDBAOXIAO = "addBaoxiaoForapp";//添加报销
    public static final String APPGETBAOXIAO = "getBaoxiaoForapp";//获取报销
    public static final String APPEDITBAOXIAO = "editBaoxiaoForapp";//修改报销
    public static final String APPGETCAR = "getCarForapp";//获取车辆
    public static final String APPGETDRIVER = "getDriverForapp";//获取司机
    public static final String APPADDORDER = "addOrderForapp";//获取司机
    public static final String APPGETMEMBER = "getMemberForapp";//获取乘客
    public static final String APPEDITORDER = "editOrderForapp";//修改订单
    public static final String APPEDITORDERGLF = "editOrderGlfForapp";//修改过路费
    public static final String APPEDITINFO = "editInfoForapp";//修改信息
    public static final String APPEDITPWD = "editpwdForapp";//修改信息
    public static final String APPEDITMEMBER = "editMemberForapp";//修改信息
    public static final String APPLISTBANC = "listBancheForapp";//修改信息
    
    /*****************政务平台融合接口******************************/
    public static final String LISTORDER = "listOrder";//订单列表
    public static final String ADDORDER = "addOrder";//新增预约订单
    public static final String LISTMEMBER="listMember";//用车人列表
    public static final String SAVEMEMBER="saveMember";//保存用车人
	
	public static final String GETLEAVELS = "getLeavels";//角色
    public static final String SAVEDEPT = "saveDept";
    public static final String GETDEPTLIST = "listDeptByTeamId";
    public static final String GETDEPT = "getDept";
    public static final String GETDEPTTEAMS = "getDeptTeam";
    public static final String DELETEDEPT = "delDept";
    public static final String DELMEMBER = "delMember";//删除用车人
    public static final String EDITORDER = "editOrder";//
     public static final String LISTASKLEAVE="listAskleave";//请假列表
    public static final String AUDITASKLEAVE="auditAskleave";//审批请假
    
    
    public static final String LISTBAOXIAO="listBaoxiao";//报销列表
    public static final String AUDITBAOXIAO="auditBaoxiao";//审批报销
    public static final String ADDBAOXIAO="addBaoxiao";//新增报销
    
    public static final String LISTCARUSES="listCaruses";//用车事由 列表
    public static final String SAVECARUSES="saveCaruses";//增加或修改
    public static final String DELETECARUSES="delCaruses";//删除
    
    public static final String LISTCARSTYLE="listCarstyle";//车型 列表
    public static final String SAVECARSTYLE="saveCarstyle";//增加或修改
    public static final String DELETECARSTYLE="delCarstyle";//删除
    
    public static final String LISTBRAND="listBrand";//品牌 列表
    public static final String LISTBRANDPAGE="listBrandPage";//品牌 分页列表
    public static final String SAVEBRAND="saveBrand";//增加或修改
    public static final String DELETEBRAND="delBrand";//删除
    
    public static final String GETORDERCOUNT="getOrderCount";//删除
    public static final String GETCARSTYLE="listCarstyleSel";//删除
    
    public static final String AUDITMEMBER="auditMember";//审批乘客
    public static final String AUDITDRIVER="auditDriver";//审批司机
    public static final String GETPRINTDATA="getPrintData";//审批司机
    public static final String EDITFEEDORDER="editFeedOrder";//平价
	 public static final String LISTWEIXIU="listWeixiu";//维修列表
    public static final String AUDITWEIXIU="auditWeixiu";//审批维修
    
    public static final String RESETMEMBER="resetMember";//乘客重置密码
    public static final String RESETDRIVER="resetDriver";//司机重置密码
    public static final String RESETACCOUNT="resetAccount";//管理员账户重置密码
    
    public static final String LISTWEIBAO="listWeibao";//维保列表
    public static final String SAVEWEIBAO="saveWeibao";//维保新增和修改
    public static final String DELWEIBAO="delWeibao";//维保删除
    public static final String GETWEIBAOEND="getWeibaoEnd";//维保删除
    
    public static final String LISTWEIZHANG="listWeizhang";//维章列表
    public static final String SAVEWEIZHANG="saveWeizhang";//维章新增和修改
    public static final String DELWEIZHANG="delWeizhang";//维章删除
    
    //统计部分
    public static final String REPORTCAR="reportCar";//车辆统计
    public static final String REPORTMONTH="reportMonth";//按月份统计
    public static final String REPORTMONTHDETAIL="reportMonthDetail";//月份明细下载
    public static final String REPORTDRIVER="reportDriver";//司机统计
    public static final String REPORTMEMBER="reportMember";//乘客统计
    
    public static final String UPDATETEAM="updateTeam";//拖动树型结构 修改车辆team
    public static final String LISTBRANDBYSTYLE="listBrandStyle";//
}









