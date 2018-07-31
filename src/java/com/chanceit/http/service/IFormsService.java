package com.chanceit.http.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.chanceit.http.pojo.Account;

public interface IFormsService {
  public String getObdMile (String uids,String beginTime,String endTime,int mile,float fule) throws Exception;
  public String getObdMileList (int uid,String beginTime,String endTime) throws Exception;
  public String getTeamCars(Account account,int uid)throws Exception;
  public  Map<String,String> getLocation(String uids);
  public String getDrivingList(int uid,String beginTime,String endTime,String obdmile,double fule) throws Exception;
  public String getSpeedList(int uid,String beginTime,String endTime,int sp) throws Exception;
  public  List getStopForm(int uid,int beginTime,int endTime,int second);
  public  List getStopList(String uids,int beginTime,int endTime,int second);
  public  List getFireList(String uids,int beginTime,int endTime,int second);
  public  List getFireFormList(int uid,int beginTime,int endTime,int second);
  public List getOnlineByMonth(String uids,String beginTime,String endTime) throws Exception;
  public List getBodyByMonth(String uids,String beginTime,String endTime) throws Exception;
  public List getOnlineByDay(String uids,String beginTime,String endTime)throws Exception;
  public List getMileFromDB(String uids,String beginTime, String endTime,int mile)throws Exception;
  public String getPlate(int uid);
  public String getTeam(int uid);
  public List getLocationOne(int uid,String firstDate, String lastDate,int sec);
  public List getTeamAndPlate(String uid);
  public String getBreakDian(String uids,String beginTime,String endTime) throws Exception;
  public String getBreakDianOne(String uid,String beginTime,String endTime) throws Exception;
  public List getPengZhuangForm(String uids,int type,String beginTime,String endTime);
  public List getPengZhuangList(int uid,int type,String beginTime,String endTime);
  public List getOverSpeedForm(String uids,String beginTime,String endTime)throws Exception;
  public List getOverSpeedList(int uid,String beginTime,String endTime) throws Exception;
  public String testPost(String x, String y) throws IOException;
}
