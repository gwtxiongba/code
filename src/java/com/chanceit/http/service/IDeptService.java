package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Dept;

public interface IDeptService {
  public void save(Dept dept);
  public Page getList(Page page,int teamId);
  public Dept getDept(int id);
  public void edit(Dept dept);
  public void delete(Dept dept);
  public List getListByTeamId(int teamId);
}
