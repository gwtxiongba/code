package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.CarTeam;
import com.chanceit.http.pojo.Dept;

public interface ICarTeamService {
  public void save(CarTeam dept);
  public Page getList(Page page,int teamId);
  public CarTeam getDept(int id);
  public void edit(CarTeam dept);
  public void delete(CarTeam dept);
  public List getListByTeamId(int teamId);
}
