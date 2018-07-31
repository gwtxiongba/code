/**
 * Copyright (c) 2011-2012 RoadRover Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Nov 1, 2011
 * Id: EnumCmd.java,v 1.0 Nov 1, 2011 5:52:38 PM Administrator
 */
package com.chanceit.framework.enums;

/**
 * @ClassName EnumCmd
 * @author yehao
 * @date Nov 1, 2011 5:52:38 PM
 * @Description ����Ϣ���ʱ�������ö����
 */
public class EnumCmd {
	
	public static final short SEQID=2;  //��ͷ
	public static final int HEAD=0x00000002;  
	public static final int TAIL=0x00000003; 
	
	public	static final int ENUM_CMD_GETDETAILBYTEL = 21;
	public	static final int ENUM_CMD_CHECKID = 25;
	public	static final int ENUM_CMD_ADDTOGROUP_CHECKPSW = 32;
	public	static final int ENUM_CMD_MODIFYPSW = 34;
	public	static final int ENUM_CMD_POPMSG = 35;
	public	static final int ENUM_CMD_LOGIN = 36;
	public	static final int ENUM_CMD_CHECKUSER = 37;
	public	static final int ENUM_CMD_REGISTSIMPLE = 38;
	public	static final int ENUM_CMD_REGISTDETAIL = 39;
	public	static final int ENUM_CMD_GETDETAILINFO = 40;
	public	static final int ENUM_CMD_MODIFYINFO = 41;
	public	static final int ENUM_CMD_ADDFRIEND = 42;
	public	static final int ENUM_CMD_DELFRIEND = 43;
	public	static final int ENUM_CMD_ADDTOGROUP = 44;
	public	static final int ENUM_CMD_CACELGROUP = 45;
	public	static final int ENUM_CMD_DELGROUP = 46;
	public	static final int ENUM_CMD_GETFRIENDLIST = 47;
	public	static final int ENUM_CMD_GETGROUPLIST = 48;
	public	static final int ENUM_CMD_GETGROUPINFO = 49;
	public	static final int ENUM_CMD_GETUSRINFOLIST = 50;
	public	static final int ENUM_CMD_GETONLINESTATUS = 51;
	public	static final int ENUM_CMD_SENDMSG = 52;
	public	static final int ENUM_CMD_SENDGROUPMSG = 53;
	public	static final int ENUM_CMD_SENDMASSMSG = 58;
	public	static final int ENUM_CMD_MONITORINFO = 88;
	public	static final int ENUM_CMD_MONITORPATH = 89;
	public	static final int ENUM_CMD_SHOPIN_SHOPUSER = 90;
	public  static final int ENUM_CMD_GETCOORS = 92;//��ȡ���û���,�����ٶȣ���λ�ǣ�����״̬
	public  static final int ENUM_CMD_GETDOUBLECOORS = 93; //��ȡ���û��㣬���ٶȣ���λ�ǣ�����״̬��//0������ 1���� 

	
}
