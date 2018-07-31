package com.chanceit.http.dao;

import java.util.Date;
import java.util.List;

import com.chanceit.http.pojo.CycleReport;
import com.chanceit.http.pojo.DriveReport;
import com.chanceit.http.pojo.Report;

public interface IDriveReportDao {

	public void saveDriveReport(DriveReport report);
	public void saveReport(Report report);
	public void saveCycleReport(CycleReport report);
	
	public long getCycleReportCount(Integer vehicleId,String date);
	
	public long getDriveReportCount(String identifier,String beginTime,String endTime);
	
	public List getCycleReport(String hql);
	
	public List getCycleReportMileageOil(String sql);
	public Date getMaxDate();
}
