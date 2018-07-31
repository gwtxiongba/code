/**
 * Copyright (c) 2011-2012 RoadRover Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Nov 19, 2011
 * Id: EnumCommon.java,v 1.0 Nov 19, 2011 4:08:37 PM Administrator
 */
package com.chanceit.framework.enums;

/**
 * @ClassName EnumCommon
 * @author Administrator
 * @date Nov 19, 2011 4:08:37 PM
 * @Description ͨ��ö����
 */
public class EnumCommon {
	
	/**
	 * ��ҳ���ʹ�С
	 */
	public static final int PAGE_SIZE = 20;
	
	/**
	 * Ĭ�ϵ��ϴ��ļ��ļ���
	 */
	public static final String UPLOAD_PATH = "upload";
	
	public static final String UPLOAD_WRITE_PATH = "uploadwrite";
	
	public static final String DEFAULT_SHOP_ID = "1000";
	
	public static final String SPARK_ADDRESS = "@chanceit";
	
	/**
	 * ��Ŀ��ַ
	 */
	public static final String PROJECT_PATH = "http://192.168.200.201:83";
	
	/**
	 * ִ�гɹ�(webservice)
	 */
	public static final int WEBSERVICE_SUCCESS = 0;
	
	/**
	 * δ�ҵ���ӦUID(webservice)
	 */
	public static final int WEBSERVICE_NOUID = 1;
	
	/**
	 * ʱ��ת��ʧ��(webservice)
	 */
	public static final int WEBSERVICE_DATEFORMAT = 2;
	
	/**
	 * ���ݿ��쳣(webservice)
	 */
	public static final int WEBSERVICE_SQL = 3;
	
	/**
	 * ������ʽ����(webservice)
	 */
	public static final int WEBSERVICE_FIELD = 4;
	
	/**
	 * ��������(webservice)
	 */
	public static final int WEBSERVICE_OTHER = 5;
	
	/**
	 * �ս����(webservice)
	 */
	public static final int WEBSERVICE_NORESULT = 6;
	
	//δ��¼
	public static final int EXCEPTION_NO_SESSION = 200;
	//��������Ϊ��
	public static final int EXCEPTION_PARA = 205;
	//��������ʧ��
	public static final int EXCEPTION_RESET = 206;
	//�˺����벻һ��
	public static final int EXCEPTION_LOGIN = 201;
	//��Ȩ����
	public static final int EXCEPTION_RIGHT = 202;
	//��֤�벻һ��
	public static final int EXCEPTION_VCODE = 203;
	
	//ϵͳ�쳣
	public static final int EXCEPTION_SYS = 101;
	public static final String[] paraDescription = new String[]{"��ǰ����","���ͻ�ת��","��ƿ��ѹ","��ȴҺ�¶�","���ͻ�����","������λ��","����ѹ","������ѹ��","��������","�����¶�","�������¶�","OBDЭ��","OBD��׼","ȼ����"};
	public static final String[] paraUnit = new String[]{"����/Сʱ","ת/����","����","���϶�","%","%","ǧ��","ǧ��","��/��","���϶�","���϶�","","","%"};
	
	
	//д���ɹ�
	public static final int WRITECARD_SUCCESS = 301;
	//д��ʧ��
	public static final int WRITECARD_FAILE = 302;
	//ˢ���ɹ�
	public static final int READCARD_SUCCESS = 303;
	//ˢ��ʧ��
	public static final int READCARD_FAILE = 304;
}
