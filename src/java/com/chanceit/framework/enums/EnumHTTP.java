package com.chanceit.framework.enums;

public class EnumHTTP {
	public static final String ACCOUNT_CHECK = "account_check"; //�˺������
	public static final String ACCOUNT_SAVE = "account_save";     //����˺�
	
	public static final String ACCOUNT_VIEW = "account_view";   //��ʾ�˺���Ϣ
	public static final String ACCOUNT_VIEWOWN = "account_viewown";   //��ʾ�����˺���Ϣ
	public static final String ACCOUNT_LOGIN = "login"; //�˺ŵ�¼
	public static final String ACCOUNT_DEL = "account_del";     //ɾ���˺�
	public static final String ACCOUNT_RESET = "account_reset"; //�˻�����
	public static final String ACCOUNT_SET = "account_set";     //�����޸�
	public static final String ACCOUNT_LOGOUT = "logout";  //�û��ǳ�
	
	public static final String ACCOUNT_CHECK_NAME="check_accountName";//�ж��˺�Ψһ �첽
	public static final String ACCOUNT_CHECK_EMAIL="check_email";//�ж�email�Ƿ���֤ �첽
	
	public static final String ACCOUNT_UPDATE_EMAIL="update_email";//�����˺�����
	
	public static final String ACCOUNTNAME_CHECK="accountName_check";//�ж��˺��Ƿ����
	public static final String ACCOUNT_SENDPWD_EMAIL="forgetPwd_sendMail";//�������뷢����֤��
	public static final String ACCOUNT_CHECK_RANDOMCODE="check_verificationCode";//��֤���䷢�͵���֤��
	public static final String ACCOUNT_RESET_PASSWORD="repwd";//������������
	
	public static final String ACCOUNT_ACTIVATE_EMAIL="send_activete_mail";//�����˺ż����ʼ�
	public static final String ACCOUNT_ACTIVATE_HREF="href_account_activate";//�����˺�
	
	public static final String USER_CHECK = "user_check"; //�û����
	public static final String CAR_VIEW = "car_view";   //��ʾ����
	public static final String CHECK_UID  = "check_uid";//��֤uid
	
	public static final String SHOP_USER_INFO = "1000";             //ͨ���û�ID���4S��������Ϣ
	
	public static final String SHOP_TEL = "1001";				    //ͨ��4S��ID���4S���Ԯ�绰��Ϣ
	
	public static final String NEWS_LIST = "2000";                  //��ȡ�����б���Ϣ
	
	public static final String NEWS_TITLE_LIST = "2001";		    //�����ҳͼƬ����
	
	public static final String SHOW_STATUS = "3000";                //��ȡOBD״̬
	public static final String SHOW_OBD = "3001";                   //���OBD��Ϣ
	public static final String SAVE_USER_EXPAND = "3002";           //�������������Ϣ
	public static final String SAVE_USER_REMIND = "3003";           //�������������Ϣ
	public static final String GET_USER_EXPAND = "3004";            //��ȡ����������Ϣ
	public static final String GET_USER_REMIND = "3005";            //��ȡ����������Ϣ
	
	public static final String PRODUCT_LIST = "4000";               	 //��ȡ��Ʒ��Ϣ
	public static final String PRODUCT_SECONDCAR_LIST = "4001";     	 //��ȡ���ֳ���Ʒ��Ϣ
	public static final String PRODUCT_SHOP_LIST = "4002";               //��ȡ��Ʒ��Ϣ
	public static final String PRODUCT_SHOP_SECONDCAR_LIST = "4003";     //��ȡ���ֳ���Ʒ��Ϣ
	
	public static final String SAVE_MILEAGE = "5000";              	//�������ӽӿ�/������������Ϣ
	
	public static final String CAR_PAGE = "6000";               	//�����б�
	public static final String SERIES_PAGE = "6001";				//��ϵ�б�
	public static final String BRAND_LIST = "6002";			    	//Ʒ���б�
	
	//-----------------------------------------
	public static final String GOLBAL = "golbal";     //
	public static final String SET_PWD = "setpwd";//�޸�����
	public static final String SAVECOMPANY = "saveCompany";//��������Ա������ �༭��ҵ��Ϣ
	public static final String SETCOMPANY = "setCompany";//�༭�Լ���ҵ��Ϣ
	public static final String LISTCOMPANY = "listCompany";//��ҵ��Ϣ�б�
	public static final String DELCOMPANY = "delCompany";//ɾ����ҵ��Ϣ
	
	public static final String SAVEOPERATOR = "saveOperator";//��Ӳ���Ա��Ϣ �༭����Ա��Ϣ ����
	public static final String LISTOPERATOR = "listOperator";   //����Ա�б�
	public static final String DELOPERATOR = "delOperator";   //ɾ������Ա�б�
	
	public static final String LISTTEAM = "listTeam";   //�����б�
	public static final String SAVETEAM = "saveTeam";   //��ӳ���
	public static final String DELTEAM = "delTeam";   //ɾ������
	public static final String TEAMVEHICLES = "teamVehicles";     //��ǰ���ӷ��鳵������ δ���鳵�� �б�
	
	public static final String LISTDRIVER = "listDriver";   //˾���б�
	public static final String SAVEDRIVER = "saveDriver";   //����༭˾��
	public static final String DELDRIVER = "delDriver";     //ɾ��˾��
	public static final String UNBINDDRIVERS = "unbindDrivers";     //δ���˾��
	public static final String LISTDRIVERLOG = "listDriverLog";		//˾�������б�
	public static final String DRIVERDATETRACE = "driverDateTrace";	//���˾��������ʷ�켣
	public static final String CARANDDRIVER = "carAndDriver";		//˾�����ڿ��ĳ�
	public static final String LISTABNORMALOG = "listAbnormaLog";	//��ȡ�Ƿ�ʹ��������˾���б�
	public static final String GETABNORMALLOGCOUNT ="getAbnormalLogCount"; //��ȡ�Ƿ�ʹ�ó�����������
	public static final String DRIVERDETAIL = "driverDetail";
	public static final String TELPHONEODER = "telphoneOder";		//��ȡ�ϵ�/����ָ��绰
	
	public static final String LISTVEHICLE = "listVehicle";   //�����û��б�
	public static final String SAVEVEHICLE = "saveVehicle";     //��ӱ༭
	public static final String DELVEHICLE = "delVehicle";     //ɾ������
	public static final String LISTMONITORVEHICLE = "listMonitor";   //��صĳ����û��б�
	public static final String SAVEMONITORVEHICLES = "saveMonitor";   //���������صĳ����б�
	public static final String VEHICLECOUNT = "vehicleCount";   //������������������
	public static final String VEHICLEONLINES = "vehicleOnlines";//�����б�
	
	public static final String SENDMSG  = "sendMsg";//������Ϣ
	public static final String SENDPOINT = "sendPoint";//����λ�õ�
	public static final String MAPDATA  = "mapData";//�ٶȵ�ͼ����ʾ����������Ϣ
	public static final String CAR_ACTION = "car_action";     // �ϵ�socket������ȡλ�õ�
	
	public static final String GETALARMCOUNT = "getAlarmCount";//��ȡ������������
	public static final String LISTALARM = "listAlarm";//���������б�
	public static final String HISTORYALARM = "historyAlarm";//����������ʷ
	public static final String GETABNORMALCOUNT = "getAbnormalCount";//��ȡ�쳣������������
	public static final String LISTABNORMAL = "listAbnormal";//�쳣���������б�
	public static final String HISTORYABNORMAL = "historyAbnormal";//�쳣����������ʷ
	public static final String DATEEVENT = "dateEvent";//�����¼�
	
	//add by zhangxin 2014-6-27
	public static final String SAVEPRESTOREMSG = "savePreStoreMsg";     //����Ԥ����Ϣ
	public static final String LISTPRESTOREMSG = "listPrestoreMsg";     //��ȡԤ����Ϣ�б�
	public static final String DELPRESTOREMSG = "delPreStoreMsg";       //ɾ��Ԥ����Ϣ
	
	//add by zhangxin 2014-6-30
	public static final String SENDMSGCENTER  = "sendMsgCenter";//������Ϣ
	public static final String LISTMSGSESSION  = "listMsgSession";//��Ϣ�б�
	public static final String GETMSGSESSION  = "getMsgSession";//��Ϣ�б�
	
	//add by zhangxin 2014-7-14
	public static final String LISTCMDINFO  = "listCmdInfo";//������Ϣ
	
	//add by zhangxin 2014-7-15
	public static final String DATETRACE  = "dateTrace";//��ȡ��ʷ�켣��
	
	//add by zhangxin 2014-7-18
	public static final String MONTHDRIVELOGS  = "monthDriveLogs";//�г��ռ�(��)
	
	//add by zhangxin 2014-7-29
	public static final String YEARDRIVELOGS  = "yearDriveLogs";//�г��ռ�(��)
	
	//add by zhangxin 2014-8-12
	public static final String LISTCARDRIVER  = "listCarDriver";//����˾�������б�
	public static final String SAVECARDRIVER  = "saveCarDriver";//���泵��˾������
	public static final String DELCARDRIVER  = "delCarDriver";//ɾ������˾������
	
	//add by zhangxin 2014-8-13
	//public static final String VALWRITECARD  = "valWriteCard";  //��֤д��
	public static final String WRITECARD  = "writeCard";     //д��
	public static final String READCARD  = "readCard";       //����
	
	public static final String LISTADMIN = "listAdmin";   //����Ա�б�
	
	public static final String SENDOBDDATA	= "sendObdData"; //��ȡOBDʵʱ����
	
	public static final String OBDCODE	= "obdCode"; //��ȡOBD������
	
	public static final String OBDSWITCH = "obdSwitch"; //����/�ر�OBDͨѶ
	
	//add by zhangxin 2014-08-22
	public static final String DOSCAN = "doScan";  //����б�
	public static final String DETECTIONDIARY = "detectionDiaryList";  //�����־�б�
	
	//add by zhangxin 2014-08-25
	public static final String LISTOPTEAM = "listOpTeam";  //����Ա���ӹ�ϵ�б�
	public static final String LISTTEAMSANDOPERATOR = "listTeamsAndOperator";  //���Ӱ󶨹�ϵ�б�
	
	//add by shaozheng 2014-08-26
	public static final String UPDATEOPTEAM = "updateOpTeam";	//���Ӱ󶨹�ϵ�޸�
	
	//add by shaozheng 2014-08-29
	public static final String SAVEPARKING = "saveParking";			//����/�޸ĳ�����Ϣ
	public static final String DELPARKING = "delParking"; 			//ɾ��������Ϣ
	public static final String LISTPARKING = "listParking"; 		//��ȡ������Ϣ
	public static final String LISTPARKINGCAR = "listParkingCar";	//��ȡ���⳵����Ϣ
	
	//add by shaozheng 2014-09-05
	public static final String LISTPLATE = "listPlate";			//��ȡ����������Ϣ�ĳ���(��ǰteam����״̬�ĳ�)
	public static final String LISTPLATEALL = "listPlateAll";	//��ȡ����������Ϣ�ĳ���(��ǰteam����״̬�ĳ�)
	public static final String SAVEBASEINFO = "saveBaseInfo";	//����/�޸ĳ���������Ϣ
	public static final String LISTBASEINFO = "listBaseInfo";	//��ȡ����������Ϣ�б�
	public static final String DELBASEINFO = "delBaseInfo";		//ɾ������������Ϣ
	//public static final String LISTBRAND = "listBrand";			//Ʒ���б�
	
	/**********************************************************************************
	/*********************app��ؽӿ� ����session��֤*******************************************/
	
	//add by zhangxin 2014-09-19
	public static final String APPLOGIN = "10000";       //��¼�ӿ�
	public static final String APPLISTTEAM = "10001";    //�����б�
	public static final String APPLISTMONITOR = "10002";    //����б�
	//add by gwt 2014-12-18
	public static final String APPITEMTEAM = "10003";    //ĳһ�����ӵĳ����б�
	public static final String APPITEMCAR = "10004";    //ĳһ����������
	public static final String APPITEMTEAMNO = "10005"; //δ���鳵��list
	public static final String APPGROUPLIST = "10006"; //
	public static final String APPWARNINGLIST = "10007"; //������Ϣ
	public static final String APPWARNINGROUPGLIST = "10008"; //����������Ϣ
	public static final String APPADDTEAM = "10009";
	public static final String APPUNBANDINDRIVER = "100010";  //δ��˾��
	public static final String APPADDCAR = "100011";  //��ӳ���
	public static final String APPEXCEPTION = "100012";  //�쳣����
	public static final String APPEXCEPTIONLIST = "100013";  //�쳣������ϸ
	public static final String APPDRIVERLIST = "100014";  //˾���б�
	public static final String APPADDDRIVER = "100015";  //���˾��
	public static final String APPDRIVERDITEL = "100016";  //˾����Ϣ
	public static final String APPDELDRIVER = "100017";  //ɾ��˾��
	public static final String APPSETWARN = "100018";  //������Ϣ�Ѷ�
	public static final String APPONLINE = "100019";  //���߳���
	public static final String APPDRIVER = "100020";  //����˾��
	public static final String APPDELUSER = "100021";  //ɾ������
	public static final String APPWEILAN = "100022";  //Χ������
	public static final String GETTEAMSFORCAR="100023";//app��ȡ��˾�б�
	
	/************************************************************************/
	//add by zhangxin 2014-10-14
	public static final String OPERATECAR = "operateCar";  //���Ͷϵ�
	public static final String OPERATECARSTATE = "operateCarState";  //���Ͷϵ�
	
	//add by zhouhui 2014-10-22
	public static final String RELAYCOUNT = "relayCount";   //�м̵������������ͶϿ�����
	public static final String RELAYOFFS = "relayOffs";//�����б�	
	
//	public static final String CYCLEDAYSUMMARY = "statsDaysMF";//���ڱ����е��ձ���
//	public static final String CYCLEDAYSUMMARYDETAIL = "statsDateMF";//���ڱ����е��ձ�����ϸ�б�
	public static final String CYCLEMONTHSUMMARY = "statsMonthMF";//���ڱ����е��±���
	public static final String EXPORTCYCLEDAYSUMMARY = "exportCycleDaySummary";//���ڱ����е��ձ�����excel
	public static final String EXPORTCYCLEMONTHSUMMARY = "exportCycleMonthSummary";//���ڱ����е��±�����excel

    /*****************************************************************************************/
	/**************************�������  add by gwt 2015-01-04**********************************************************/
    public static final String FORMDRIVING = "formdDriving";  //����� ���ͺ� ��״ͼ����
    public static final String FORMLOCATION = "formdLocation";  //λ�õ�����
    public static final String FORMPATH = "formdPath";   //  ��ʻ�����б�����  ��ʻ��¼
    public static final String FORMOVERPATH = "formdOverPath";  //���ٱ����б�����  
    public static final String FORMOBDLIST = "formObdList";   //��̱����б�����
    public static final String FORMSTOP = "formStop";   //ֹͣ������״ͼ����
    public static final String FORMSTOPLIST = "formStopList";  //ֹͣ�����б�����
    public static final String FORMFIRE = "formFire";   //��𱨱���״ͼ����
    public static final String FORMFIRELIST = "formFireList";  //��𱨱��б�����
    public static final String FORMOVER = "formOverSpeed";    //���ٱ���
    public static final String FORMTEAMS = "formTeamsCar";//��ȡ����
    public static final String FORMDUANDIAN = "totalAlarms";//����ͳ��ͼ
    public static final String FORMDUANDIANONE = "detailAlarms";//��������
    public static final String FORMDMONTH = "monthOnlines"; //������
    public static final String FORMDDAY = "dateOnlines";//������
    public static final String FORMDDATEFULE = "dateFuel";//�����ͺ�
    public static final String FORMDSTATSDAY = "statsTotalMF";//��ͳ��
    public static final String FORMDSTATSMF= "statsDaysMF";//��ͳ��
    
    /*****************����excel��ӿ�*******************/
    
    public static final String FORMDRIVINGEXCEL = "formdDriving_excel";  //����� ���ͺ� ��״ͼ����
    public static final String FORMLOCATIONEXCEL = "formdLocation_excel";  //λ�õ�����
    public static final String FORMPATHEXCEL = "formdPath_excel";   //  ��ʻ�����б�����  ��ʻ��¼
    public static final String FORMOVERPATHEXCEL = "formdOverPath_excel";  //���ٱ����б�����  
    public static final String FORMOBDLISTEXCEL = "formObdList_excel";   //��̱����б�����
    public static final String FORMSTOPEXCEL = "formStop_excel";   //ֹͣ������״ͼ����
    public static final String FORMSTOPLISTEXCEL = "formStopList_excel";  //ֹͣ�����б�����
    public static final String FORMFIREEXCEL = "formFire_excel";   //��𱨱���״ͼ����
    public static final String FORMFIRELISTEXCEL = "formFireList_excel";  //��𱨱��б�����
    public static final String FORMDUANDIANEXCEL = "totalAlarms_excel";//����ͳ��ͼ
    public static final String FORMDUANDIANONEEXCEL = "detailAlarms_excel";//��������
    public static final String FORMDMONTHEXCEL = "monthOnlines_excel"; //������
    public static final String FORMDDAYEXCEL = "dateOnlines_excel";//������
    public static final String FORMDDATEFULEEXCEL = "dateFuel_excel";//�����ͺ�
    public static final String FORMDSTATSDAYEXCEL = "statsTotalMF_excel";//��ͳ��
    public static final String FORMDSTATSMFEXCEL= "statsDaysMF_excel";//���б�
    
    public static final String MEMBEREXCEL="member_excel";//�˿ͱ���
    public static final String DRIVEREXCEL="driver_excel";//˾������
    public static final String CAREXCEL="car_excel";//��������
    
    
    /********************�����Ŀ�½ӿ�**************************************************/
    public static final String GUANHUIPOST = "guanhuiTeamsCar";
    public static final String GUANHUISAVE = "ghsave";
    public static final String GUANHUISAVEX = "ghsavex";
    public static final String GUANHUILINES = "getLines";  //��ȡ�滮·���б�
    public static final String GUANHUILINESRULE = "getLineRules";  //��ȡ��·�����б�
    public static final String GUANHUISAVERULE = "saveRules";  //�����·��
    public static final String GUANHUISAVELINE = "saveLine";   //����滮·��
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
    /***********�����ӿ�****************/
    public static final String GETTEAMS = "getTeams"; 
    public static final String MONITDATA = "monitData";
    
    public static final String OVERSPEED = "overSpeed";//��������
    public static final String POSTIME= "posTime";//λ�õ��ϴ�ʱ����
    
    
    /*****************�����ó�APP�ӿ�******************************/
    public static final String LOGINFORGOV = "loginForGov";//APP��¼
    public static final String MEMBERREGISTER = "memberRegister";//�˿�ע��
    public static final String DRIVERREGISTER = "driverRegister";//˾��ע��
    public static final String APPGETTEAMS = "getCompanyForapp";//��ȡ��˾
    public static final String APPGETDEPT = "getDeptForapp";//��ȡ����
    public static final String APPGETLEAVE = "getAcLeaveForapp";//�������
    public static final String APPEDITLEAVE = "editLeaveForapp";//����޸�
    public static final String APPGETORDERS = "getOrdersForapp";//��ȡ����
    public static final String APPGETCARSTYLE = "getCarStyleForapp";//��������
    public static final String APPGETCAURSE = "getCaurseForapp";//�ó�����
    public static final String APPGETBRAND = "getBrandForapp";//Ʒ��
    public static final String APPADDASKLEAVE = "addAskleaveForapp";//������
    public static final String APPADDBAOXIAO = "addBaoxiaoForapp";//��ӱ���
    public static final String APPGETBAOXIAO = "getBaoxiaoForapp";//��ȡ����
    public static final String APPEDITBAOXIAO = "editBaoxiaoForapp";//�޸ı���
    public static final String APPGETCAR = "getCarForapp";//��ȡ����
    public static final String APPGETDRIVER = "getDriverForapp";//��ȡ˾��
    public static final String APPADDORDER = "addOrderForapp";//��ȡ˾��
    public static final String APPGETMEMBER = "getMemberForapp";//��ȡ�˿�
    public static final String APPEDITORDER = "editOrderForapp";//�޸Ķ���
    public static final String APPEDITORDERGLF = "editOrderGlfForapp";//�޸Ĺ�·��
    public static final String APPEDITINFO = "editInfoForapp";//�޸���Ϣ
    public static final String APPEDITPWD = "editpwdForapp";//�޸���Ϣ
    public static final String APPEDITMEMBER = "editMemberForapp";//�޸���Ϣ
    public static final String APPLISTBANC = "listBancheForapp";//�޸���Ϣ
    
    /*****************����ƽ̨�ںϽӿ�******************************/
    public static final String LISTORDER = "listOrder";//�����б�
    public static final String ADDORDER = "addOrder";//����ԤԼ����
    public static final String LISTMEMBER="listMember";//�ó����б�
    public static final String SAVEMEMBER="saveMember";//�����ó���
	
	public static final String GETLEAVELS = "getLeavels";//��ɫ
    public static final String SAVEDEPT = "saveDept";
    public static final String GETDEPTLIST = "listDeptByTeamId";
    public static final String GETDEPT = "getDept";
    public static final String GETDEPTTEAMS = "getDeptTeam";
    public static final String DELETEDEPT = "delDept";
    public static final String DELMEMBER = "delMember";//ɾ���ó���
    public static final String EDITORDER = "editOrder";//
     public static final String LISTASKLEAVE="listAskleave";//����б�
    public static final String AUDITASKLEAVE="auditAskleave";//�������
    
    
    public static final String LISTBAOXIAO="listBaoxiao";//�����б�
    public static final String AUDITBAOXIAO="auditBaoxiao";//��������
    public static final String ADDBAOXIAO="addBaoxiao";//��������
    
    public static final String LISTCARUSES="listCaruses";//�ó����� �б�
    public static final String SAVECARUSES="saveCaruses";//���ӻ��޸�
    public static final String DELETECARUSES="delCaruses";//ɾ��
    
    public static final String LISTCARSTYLE="listCarstyle";//���� �б�
    public static final String SAVECARSTYLE="saveCarstyle";//���ӻ��޸�
    public static final String DELETECARSTYLE="delCarstyle";//ɾ��
    
    public static final String LISTBRAND="listBrand";//Ʒ�� �б�
    public static final String LISTBRANDPAGE="listBrandPage";//Ʒ�� ��ҳ�б�
    public static final String SAVEBRAND="saveBrand";//���ӻ��޸�
    public static final String DELETEBRAND="delBrand";//ɾ��
    
    public static final String GETORDERCOUNT="getOrderCount";//ɾ��
    public static final String GETCARSTYLE="listCarstyleSel";//ɾ��
    
    public static final String AUDITMEMBER="auditMember";//�����˿�
    public static final String AUDITDRIVER="auditDriver";//����˾��
    public static final String GETPRINTDATA="getPrintData";//����˾��
    public static final String EDITFEEDORDER="editFeedOrder";//ƽ��
	 public static final String LISTWEIXIU="listWeixiu";//ά���б�
    public static final String AUDITWEIXIU="auditWeixiu";//����ά��
    
    public static final String RESETMEMBER="resetMember";//�˿���������
    public static final String RESETDRIVER="resetDriver";//˾����������
    public static final String RESETACCOUNT="resetAccount";//����Ա�˻���������
    
    public static final String LISTWEIBAO="listWeibao";//ά���б�
    public static final String SAVEWEIBAO="saveWeibao";//ά���������޸�
    public static final String DELWEIBAO="delWeibao";//ά��ɾ��
    public static final String GETWEIBAOEND="getWeibaoEnd";//ά��ɾ��
    
    public static final String LISTWEIZHANG="listWeizhang";//ά���б�
    public static final String SAVEWEIZHANG="saveWeizhang";//ά���������޸�
    public static final String DELWEIZHANG="delWeizhang";//ά��ɾ��
    
    //ͳ�Ʋ���
    public static final String REPORTCAR="reportCar";//����ͳ��
    public static final String REPORTMONTH="reportMonth";//���·�ͳ��
    public static final String REPORTMONTHDETAIL="reportMonthDetail";//�·���ϸ����
    public static final String REPORTDRIVER="reportDriver";//˾��ͳ��
    public static final String REPORTMEMBER="reportMember";//�˿�ͳ��
    
    public static final String UPDATETEAM="updateTeam";//�϶����ͽṹ �޸ĳ���team
    public static final String LISTBRANDBYSTYLE="listBrandStyle";//
}









