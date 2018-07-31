package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.http.pojo.GhPoints;

public interface IGhPointsDao {
  public void add(GhPoints gp);
  public void del(GhPoints gp);
  public List<GhPoints> getAll(int accountId,String teams);
  public List getLineRuleList(String sql);
  public List getMaxId();
  public void updateDel(int id);
  public List<GhPoints> getCircles(int accountId,String type,String teamIds);
}
