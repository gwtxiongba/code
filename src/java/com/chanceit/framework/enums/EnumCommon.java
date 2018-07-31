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
 * @Description 通用枚举类
 */
public class EnumCommon {
	
	/**
	 * 分页类型大小
	 */
	public static final int PAGE_SIZE = 20;
	
	/**
	 * 默认的上传文件文件夹
	 */
	public static final String UPLOAD_PATH = "upload";
	
	public static final String UPLOAD_WRITE_PATH = "uploadwrite";
	
	public static final String DEFAULT_SHOP_ID = "1000";
	
	public static final String SPARK_ADDRESS = "@chanceit";
	
	/**
	 * 项目地址
	 */
	public static final String PROJECT_PATH = "http://192.168.200.201:83";
	
	/**
	 * 执行成功(webservice)
	 */
	public static final int WEBSERVICE_SUCCESS = 0;
	
	/**
	 * 未找到对应UID(webservice)
	 */
	public static final int WEBSERVICE_NOUID = 1;
	
	/**
	 * 时间转换失败(webservice)
	 */
	public static final int WEBSERVICE_DATEFORMAT = 2;
	
	/**
	 * 数据库异常(webservice)
	 */
	public static final int WEBSERVICE_SQL = 3;
	
	/**
	 * 参数格式不对(webservice)
	 */
	public static final int WEBSERVICE_FIELD = 4;
	
	/**
	 * 其他错误(webservice)
	 */
	public static final int WEBSERVICE_OTHER = 5;
	
	/**
	 * 空结果集(webservice)
	 */
	public static final int WEBSERVICE_NORESULT = 6;
	
	//未登录
	public static final int EXCEPTION_NO_SESSION = 200;
	//参数不能为空
	public static final int EXCEPTION_PARA = 205;
	//重置密码失败
	public static final int EXCEPTION_RESET = 206;
	//账号密码不一致
	public static final int EXCEPTION_LOGIN = 201;
	//无权访问
	public static final int EXCEPTION_RIGHT = 202;
	//验证码不一致
	public static final int EXCEPTION_VCODE = 203;
	
	//系统异常
	public static final int EXCEPTION_SYS = 101;
	public static final String[] paraDescription = new String[]{"当前车速","发送机转速","电瓶电压","冷却液温度","发送机负载","节气门位置","大气压","进气管压力","空气流量","空气温度","进气管温度","OBD协议","OBD标准","燃油量"};
	public static final String[] paraUnit = new String[]{"公里/小时","转/分钟","伏特","摄氏度","%","%","千帕","千帕","克/秒","摄氏度","摄氏度","","","%"};
	
	
	//写卡成功
	public static final int WRITECARD_SUCCESS = 301;
	//写卡失败
	public static final int WRITECARD_FAILE = 302;
	//刷卡成功
	public static final int READCARD_SUCCESS = 303;
	//刷卡失败
	public static final int READCARD_FAILE = 304;
}
