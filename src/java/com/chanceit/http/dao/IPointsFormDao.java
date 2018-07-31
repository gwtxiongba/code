package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.http.pojo.PointsForm;

public interface IPointsFormDao {
  public List<PointsForm> find();
  public List findList(String sql);
  public List findSumList(String sql);
}
