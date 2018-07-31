package com.chanceit.http.service;

import java.util.List;

public interface IDriveReportService {

	public void report();
	
	
//	public List getCycleReport(String identifiers,String flag);
	
	public List getCycleReport(String identifiers,String startTime,String endTime,String oilper,String plate);
	
	public List getCycleReportMileageOil(String vehicleIds,String startTime,String endTime,String oilper,String plate);
	
	public List getCycleReportMileageOilByMonth(String identifiers,String year,String month,String oilper,String plate);
}
