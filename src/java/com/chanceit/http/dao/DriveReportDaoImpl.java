package com.chanceit.http.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.http.pojo.CycleReport;
import com.chanceit.http.pojo.DriveReport;
import com.chanceit.http.pojo.Report;
@Component("driveReportDao")
@Repository
public class DriveReportDaoImpl extends HibernateService implements IDriveReportDao{

	@Override
	public void saveDriveReport(DriveReport report) {
		// TODO Auto-generated method stub
		getSession().save(report);
	}

	@Override
	public void saveCycleReport(CycleReport report) {
		// TODO Auto-generated method stub
		getSession().save(report);
	}
	
	@Override
	public long getCycleReportCount(Integer vehicleId,String date){
		String hql = "from CycleReport c where c.vehicleId="+vehicleId+" and c.createDate ='"+date+"'";
		return this.countHQLResult(hql, null);
	}

	@Override
	public long getDriveReportCount(String identifier,String beginTime,String endTime){
		String hql = "from DriveReport d where d.identifier='"+identifier+"' and d.startTime='"+beginTime+"' and d.endTime='"+endTime+"'";
		return this.countHQLResult(hql, null);
	}
	@Override
	public List getCycleReport(String hql){
		return this.findSQLList(hql);
	}
	
	@Override
	public List getCycleReportMileageOil(String sql){
		return this.findSQLList(sql);
	}

	@Override
	public void saveReport(Report report) {
		// TODO Auto-generated method stub
		this.getSession().save(report);
	}
	
	@Override
	public Date getMaxDate() {
		Date date = (Date)getSession().createQuery("select max(p.createDate) from  CycleReport p" ).uniqueResult();
		return date;
	}
}
