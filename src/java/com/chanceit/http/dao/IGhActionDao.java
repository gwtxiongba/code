package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.http.pojo.GhAction;

public interface IGhActionDao {
  public void add(GhAction action);
  public void del(GhAction action);
  public void del(int pid);  
   public List<GhAction> getById (int id);
  public List<GhAction> getByUidAndPid(int pid);
  public void save(String sql);
}
