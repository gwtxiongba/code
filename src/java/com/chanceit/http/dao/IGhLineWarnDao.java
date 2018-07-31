package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.GhLineWarn;

public interface IGhLineWarnDao {
  public void add(GhLineWarn glw);
  public void del(GhLineWarn glw);
  public Page getPageList(Page page , String hql ,Object... values);
  public void update(String sql);
  public List getCount(String sql);
  public List getLimiList(String sql);
  public List getWarnList(String sql);
}
