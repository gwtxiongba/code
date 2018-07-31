package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.GhAction;
import com.chanceit.http.pojo.GhLineWarn;
import com.chanceit.http.pojo.GhPoints;

public interface IGuanHuiService {
  public String postData();
  public void save(GhAction action);
  public void save(GhPoints gp);
  public void saveline(GhLineWarn glw);
  public List<GhPoints> getLineList(int accountId,String  teamids);
  public List getLineRuleList();
  public void delete(GhPoints gp);
  public void delete(GhAction gA);
  public void delete(int pid);
  public int getMaxId();
  public List<GhAction> getById(int id);
  public List<GhAction> getByUidAndPid(int pid);
  public void batchSave(String sql);
  public Page getWarnList(Page page,int accountId,String teamids);
  public void update(int id);
  public List getCount(int account_id,String teamids);
  public List getLimitLine();
  public void  updateDel(int id);
  public List getLimitLine(String uid,String type);
  public List<GhPoints> getCircleList(int accountId,String type,String teamIds);
  public List getCircleRuleList(String type);
  public List getWarnListToApp(int accountId,int time);
  public List getWarnListToApp2(int accountId,int time,String teamIds);
}
