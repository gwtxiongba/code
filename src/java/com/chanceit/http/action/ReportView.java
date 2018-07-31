package com.chanceit.http.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.contrib.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.exceptions.CommonException;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IDriveReportService;
import com.chanceit.http.service.IUserService;
@Component("reportView")
public class ReportView extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier("driveReportService")
	private IDriveReportService driveReportService;
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	//按日统计详细列表所需数据
	public String listCycleMileageOil(){
		String vehicleIds = this.getParameterNoCheck("vehicleId");
		//String flag = this.getParameterNoCheck("flag");
		String startTime = this.getParameterNoCheck("date0");
		String endTime = this.getParameterNoCheck("date1");
		String oilper = this.getParameterNoCheck("fuel");
		String plate = this.getParameterNoCheck("plate");
		if(StringUtils.isNotBlank(plate)){
			String accountId;
			Account account = getSessionAccount();
			if(account.getRole().getRoleId() != 2){
				accountId = getCompanyId();
			}else{
				accountId = account.getAccountId().toString();
			}
			boolean ifExistPlate = userService.ifExistPlate(plate, Integer.parseInt(accountId), null);
			if(!ifExistPlate){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"车牌对应的车辆不存在");
			}
		}
		
		List list = null;
		JSONObject obj = new JSONObject();
//		if(StringUtils.isNotBlank(flag)){
//			Date currentDate = new Date();
//			if("-1".equals(flag) || "0".equals(flag)){
//				startTime = DateUtil.getAfterDate(currentDate, Integer.valueOf(flag), "yyyy-MM-dd");
//				endTime = DateUtil.getAfterDate(currentDate,Integer.valueOf(flag),"yyyy-MM-dd");
//			}else{
//				startTime = DateUtil.getAfterDate(currentDate, Integer.valueOf(flag)+1, "yyyy-MM-dd");
//				endTime = DateUtil.getAfterDate(currentDate,0,"yyyy-MM-dd");
//			}
//		}
		list = driveReportService.getCycleReportMileageOil(vehicleIds,startTime,endTime,oilper,plate);
		JSONArray arry = JSONArray.fromObject(list.toArray());
		return arry.toString();
	}
	
	//按日统计 图表显示列表所需数据
	public String listCycleReport() throws Exception{
		String vehicleIds = this.getParameterNoCheck("vehicleIds");
		/*
		 * flag  -7代表近七天 -3代表近3天 -1代表昨天 0代表今天 
		 */
		String startTime = this.getParameterNoCheck("date0");
		String endTime = this.getParameterNoCheck("date1");
		String oilper = this.getParameterNoCheck("fuel");
		String plate = this.getParameterNoCheck("plate");
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		if(StringUtils.isNotBlank(plate)){
			
			boolean ifExistPlate = userService.ifExistPlate(plate, Integer.parseInt(accountId), null);
			if(!ifExistPlate){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"车牌对应的车辆不存在");
			}
		}else{
			Object[]values = {accountId,null,null};
			List userList = userService.getList(values);
			for(int i=0;i<userList.size();i++){
				User user = (User)userList.get(i);
				if(StringUtils.isBlank(vehicleIds)){
					vehicleIds = user.getVehicleId().toString();
				}else{
					vehicleIds +=","+user.getVehicleId().toString();
				}
			}
		}
		
		List list = null;
		JSONObject obj = new JSONObject();
//		if(StringUtils.isNotBlank(flag)){
//			Date currentDate = new Date();
//			if("-1".equals(flag) || "0".equals(flag)){
//				startTime = DateUtil.getAfterDate(currentDate, Integer.valueOf(flag), "yyyy-MM-dd");
//				endTime = DateUtil.getAfterDate(currentDate,Integer.valueOf(flag),"yyyy-MM-dd");
//			}else{
//				startTime = DateUtil.getAfterDate(currentDate, Integer.valueOf(flag)+1, "yyyy-MM-dd");
//				endTime = DateUtil.getAfterDate(currentDate,0,"yyyy-MM-dd");
//			}
//			
//		}
		list = driveReportService.getCycleReport(vehicleIds,startTime,endTime,oilper,plate);
		JSONArray arry = JSONArray.fromObject(list);
//		obj.put("isOk", true);
//		obj.put("result", arry);
		String s = arry.toString();
		return s;
	}
	////按月统计图标所需数据
	public String listCycleMileageOilByMonth() throws Exception{
		String vehicleIds = this.getParameterNoCheck("vehicleId");
		String yM = this.getParameterNoCheck("date");
		String arr[] = yM.split("-");
		String year = arr[0];
		String month = arr[1];
		String oilper = this.getParameterNoCheck("fuel");
		String plate = this.getParameterNoCheck("plate");
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		if(StringUtils.isNotBlank(plate)){
			boolean ifExistPlate = userService.ifExistPlate(plate, Integer.parseInt(accountId), null);
			if(!ifExistPlate){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"车牌对应的车辆不存在");
			}
		}else{
			Object[]values = {accountId,null,null};
			List userList = userService.getList(values);
			for(int i=0;i<userList.size();i++){
				User user = (User)userList.get(i);
				if(StringUtils.isBlank(vehicleIds)){
					vehicleIds = user.getVehicleId().toString();
				}else{
					vehicleIds +=","+user.getVehicleId().toString();
				}
			}
		}
		
		JSONObject obj = new JSONObject();
		List list = driveReportService.getCycleReportMileageOilByMonth(vehicleIds, year, month,oilper,plate);
//		for(int i=0;i<list.size();i++){
//			Map map = (Map)list.get(i);
//			Float oil_count = Float.valueOf(map.get("oil_count").toString());
//			Float mile = Float.valueOf(map.get("mileage").toString());
//			map.put("per_oil_count",String.format("%.2f", (oil_count/mile)));
//			list.add(i, map);
//		}
		JSONArray arry = JSONArray.fromObject(list);
//		obj.put("isOk", true);
//		obj.put("result", arry);
		return arry.toString();
	}
	
	public void exportCycle() throws Exception{
		String summary = this.getParameterNoCheck("summary");
		String plate = this.getParameterNoCheck("plate");
		String vehicleIds = this.getParameterNoCheck("vehicleId");
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		if(StringUtils.isNotBlank(plate)){
			
			boolean ifExistPlate = userService.ifExistPlate(plate, Integer.parseInt(accountId), null);
			if(!ifExistPlate){
				throw new CommonException("车牌对应的车辆不存在");
			}
		}else{
			Object[]values = {accountId,null,null};
			List userList = userService.getList(values);
			for(int i=0;i<userList.size();i++){
				User user = (User)userList.get(i);
				if(StringUtils.isBlank(vehicleIds)){
					vehicleIds = user.getVehicleId().toString();
				}else{
					vehicleIds +=","+user.getVehicleId().toString();
				}
			}
		}
		
		if(StringUtils.isNotBlank(summary)){
			exportAllCycleDay(plate,vehicleIds);
		}else{
			exportCycleDay(plate,vehicleIds);
		}
	}
	//导出
	public void exportCycleDay(String platePram,String vehicleIds) throws Exception{
		
//		String flag = this.getParameterNoCheck("flag");
		String startTime = this.getParameterNoCheck("date0");
		String endTime = this.getParameterNoCheck("date1");
		String oilper = this.getParameterNoCheck("fuel");
		HttpServletResponse response = this.getResponse();
		response.setContentType("octets/stream");
		List list = null;
		
//		if(StringUtils.isNotBlank(flag)){
//			Date currentDate = new Date();
//			if("-1".equals(flag) || "0".equals(flag)){
//				startTime = DateUtil.getAfterDate(currentDate, Integer.valueOf(flag), "yyyy-MM-dd");
//				endTime = DateUtil.getAfterDate(currentDate,Integer.valueOf(flag),"yyyy-MM-dd");
//			}else{
//				startTime = DateUtil.getAfterDate(currentDate, Integer.valueOf(flag)+1, "yyyy-MM-dd");
//				endTime = DateUtil.getAfterDate(currentDate,0,"yyyy-MM-dd");
//			}
//		}
		list = driveReportService.getCycleReportMileageOil(vehicleIds,startTime,endTime,oilper,platePram);
		HSSFWorkbook workBook = new HSSFWorkbook();
		String title = String.valueOf(System.currentTimeMillis());
		HSSFSheet sheet = workBook.createSheet(title);
		  response.addHeader("Content-Disposition", "attachment;filename="+title+".xls");
		sheet.setDefaultColumnWidth(15);
		String identifer = "";
		String id = "";
		String plate = "";
		String date = "";
		String team = "";
		String mileage = "";
		String oil = "";
		String accTimes = "";
		String brakes = "";
		String speedTime = "";
		int index = 0;
		HSSFCellStyle cellHeadStyle = workBook.createCellStyle();
		HSSFCellStyle contentCellStyle = workBook.createCellStyle();
		contentCellStyle.setBorderBottom((short)1);
		contentCellStyle.setBorderTop((short)1);
		contentCellStyle.setBorderLeft((short)1);
		contentCellStyle.setBorderRight((short)1);
		cellHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellHeadStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		cellHeadStyle.setBorderBottom((short)1);
		cellHeadStyle.setBorderTop((short)1);
		cellHeadStyle.setBorderLeft((short)1);
		cellHeadStyle.setBorderRight((short)1);
		HSSFFont font = workBook.createFont();
		font.setBoldweight((short)4);
		cellHeadStyle.setFont(font);
		for(int i = 0;i<list.size();i++){
			Map map = (Map)list.get(i);
			id = (String)map.get("identifier");
			plate = (String)map.get("plate");
			date = (String)map.get("date");
			team = (String)map.get("team");
			mileage = map.get("mileage").toString();
			oil = map.get("fuel").toString();
			accTimes = map.get("acctimes").toString();
			brakes = map.get("sleepytimes").toString();
			speedTime = map.get("overspeedhours").toString();
			if(!id.equals(identifer)){
				if(i != 0){
					sheet.createRow(index);
					index++;
				}
				identifer = id;
				
				
				HSSFRow row_head = sheet.createRow(index);
				HSSFCell titleCell = row_head.createCell(0);
				titleCell.setCellValue("车牌："+plate+"  设备ID："+id);
				titleCell.setCellStyle(contentCellStyle);
				CellRangeAddress region = new CellRangeAddress(index,index,(short)0,(short)7);
				sheet.addMergedRegion(region);
				RegionUtil.setBorderBottom(1, region, sheet, workBook);
				RegionUtil.setBorderTop(1, region, sheet, workBook);
				RegionUtil.setBorderLeft(1, region, sheet, workBook);
				RegionUtil.setBorderRight(1, region, sheet, workBook);
				index ++;
				
				HSSFRow row = sheet.createRow(index);
				//row.setRowStyle(cellHeadStyle);
				HSSFCell headDateCell = row.createCell(0);
				headDateCell.setCellValue("日期");
				headDateCell.setCellStyle(cellHeadStyle);
				HSSFCell headPlateCell = row.createCell(1);
				headPlateCell.setCellValue("车牌号码");
				headPlateCell.setCellStyle(cellHeadStyle);
				HSSFCell headGroupCell = row.createCell(2);
				headGroupCell.setCellValue("所属分组");
				headGroupCell.setCellStyle(cellHeadStyle);
				HSSFCell headMileageCell = row.createCell(3);
				headMileageCell.setCellValue("行驶里程(公里)");
				headMileageCell.setCellStyle(cellHeadStyle);
				HSSFCell headOilCell = row.createCell(4);
				headOilCell.setCellValue("油耗(升)");
				headOilCell.setCellStyle(cellHeadStyle);
				HSSFCell headSpeedCell = row.createCell(5);
				headSpeedCell.setCellValue("超速时间(小时)");
				headSpeedCell.setCellStyle(cellHeadStyle);
				HSSFCell headAccCell = row.createCell(6);
				headAccCell.setCellValue("急加/减速次数");
				headAccCell.setCellStyle(cellHeadStyle);
				HSSFCell headSleepCell = row.createCell(7);
				headSleepCell.setCellValue("疲劳驾驶次数");
				headSleepCell.setCellStyle(cellHeadStyle);
				index++;
			}
			HSSFRow contentRow = sheet.createRow(index);
			index++;
			HSSFCell dateCell = contentRow.createCell(0);
			dateCell.setCellValue(date);
			dateCell.setCellStyle(contentCellStyle);
			HSSFCell plateCell = contentRow.createCell(1);
			plateCell.setCellValue(plate);
			plateCell.setCellStyle(contentCellStyle);
			HSSFCell teamCell = contentRow.createCell(2);
			teamCell.setCellValue(team);
			teamCell.setCellStyle(contentCellStyle);
			HSSFCell mileageCell = contentRow.createCell(3);
			mileageCell.setCellValue(mileage);
			mileageCell.setCellStyle(contentCellStyle);
			HSSFCell oilCell = contentRow.createCell(4);
			oilCell.setCellValue(oil);
			oilCell.setCellStyle(contentCellStyle);
			HSSFCell speedTimeCell = contentRow.createCell(5);
			speedTimeCell.setCellValue(speedTime);
			speedTimeCell.setCellStyle(contentCellStyle);
			HSSFCell accTimesCell = contentRow.createCell(6);
			accTimesCell.setCellValue(accTimes);
			accTimesCell.setCellStyle(contentCellStyle);
			HSSFCell brakesCell = contentRow.createCell(7);
			brakesCell.setCellValue(brakes);
			brakesCell.setCellStyle(contentCellStyle);
		}
		
		OutputStream out;
		try {
			out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new CommonException("导出Excel出错");
		}
		/*
		 * 明天做按月统计
		 */
	}
	//统计导出
	public void exportAllCycleDay(String platePram,String vehicleIds)  throws Exception{
//		String flag = this.getParameterNoCheck("flag");
		String startTime = this.getParameterNoCheck("date0");
		String endTime = this.getParameterNoCheck("date1");
		String oilper = this.getParameterNoCheck("fuel");
		HttpServletResponse response = this.getResponse();
		response.setContentType("octets/stream");
		List list = null;
//		if(StringUtils.isNotBlank(flag)){
//			Date currentDate = new Date();
//			if("-1".equals(flag) || "0".equals(flag)){
//				startTime = DateUtil.getAfterDate(currentDate, Integer.valueOf(flag), "yyyy-MM-dd");
//				endTime = DateUtil.getAfterDate(currentDate,Integer.valueOf(flag),"yyyy-MM-dd");
//			}else{
//				startTime = DateUtil.getAfterDate(currentDate, Integer.valueOf(flag)+1, "yyyy-MM-dd");
//				endTime = DateUtil.getAfterDate(currentDate,0,"yyyy-MM-dd");
//			}
//		}
		list = driveReportService.getCycleReport(vehicleIds,startTime,endTime,oilper,platePram);
		HSSFWorkbook workBook = new HSSFWorkbook();
		String title = String.valueOf(System.currentTimeMillis());
		HSSFSheet sheet = workBook.createSheet(title);
		  response.addHeader("Content-Disposition", "attachment;filename="+title+".xls");
		sheet.setDefaultColumnWidth(15);
		String identifer = "";
		String id = "";
		String plate = "";
		String team = "";
		String mileage = "";
		String oil = "";
		String accTimes = "";
		String brakes = "";
		String speedTime = "";
		HSSFCellStyle cellHeadStyle = workBook.createCellStyle();
		HSSFCellStyle contentCellStyle = workBook.createCellStyle();
		contentCellStyle.setBorderBottom((short)1);
		contentCellStyle.setBorderTop((short)1);
		contentCellStyle.setBorderLeft((short)1);
		contentCellStyle.setBorderRight((short)1);
		cellHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellHeadStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		cellHeadStyle.setBorderBottom((short)1);
		cellHeadStyle.setBorderTop((short)1);
		cellHeadStyle.setBorderLeft((short)1);
		cellHeadStyle.setBorderRight((short)1);
		HSSFFont font = workBook.createFont();
		font.setBoldweight((short)2);
		cellHeadStyle.setFont(font);
		for(int i = 0;i<list.size();i++){
			Map map = (Map)list.get(i);
			id = (String)map.get("identifier");
			plate = (String)map.get("plate");
			team = (String)map.get("team");
			mileage = map.get("mileage").toString();
			oil = map.get("fuel").toString();
			accTimes = map.get("acctimes").toString();
			brakes = map.get("sleepytimes").toString();
			speedTime = map.get("overspeedhours").toString();
			if(i == 0){
				HSSFRow row = sheet.createRow(0);
				HSSFCell headPlateCell = row.createCell(0);
				headPlateCell.setCellValue("车牌号码");
				headPlateCell.setCellStyle(cellHeadStyle);
				HSSFCell headGroupCell = row.createCell(1);
				headGroupCell.setCellValue("所属分组");
				headGroupCell.setCellStyle(cellHeadStyle);
				HSSFCell headMileageCell = row.createCell(2);
				headMileageCell.setCellValue("行驶里程(公里)");
				headMileageCell.setCellStyle(cellHeadStyle);
				HSSFCell headOilCell = row.createCell(3);
				headOilCell.setCellValue("油耗(升)");
				headOilCell.setCellStyle(cellHeadStyle);
				HSSFCell headSpeedCell = row.createCell(4);
				headSpeedCell.setCellValue("超速时间(小时)");
				headSpeedCell.setCellStyle(cellHeadStyle);
				HSSFCell headAccCell = row.createCell(5);
				headAccCell.setCellValue("急加/减速次数");
				headAccCell.setCellStyle(cellHeadStyle);
				HSSFCell headSleepCell = row.createCell(6);
				headSleepCell.setCellValue("疲劳驾驶次数");
				headSleepCell.setCellStyle(cellHeadStyle);
			}
			HSSFRow contentRow = sheet.createRow(i+1);
			HSSFCell plateCell = contentRow.createCell(0);
			plateCell.setCellValue(plate);
			plateCell.setCellStyle(contentCellStyle);
			HSSFCell teamCell = contentRow.createCell(1);
			teamCell.setCellValue(team);
			teamCell.setCellStyle(contentCellStyle);
			HSSFCell mileageCell = contentRow.createCell(2);
			mileageCell.setCellValue(mileage);
			mileageCell.setCellStyle(contentCellStyle);
			HSSFCell oilCell = contentRow.createCell(3);
			oilCell.setCellValue(oil);
			oilCell.setCellStyle(contentCellStyle);
			HSSFCell speedTimeCell = contentRow.createCell(4);
			speedTimeCell.setCellValue(speedTime);
			speedTimeCell.setCellStyle(contentCellStyle);
			HSSFCell accTimesCell = contentRow.createCell(5);
			accTimesCell.setCellValue(accTimes);
			accTimesCell.setCellStyle(contentCellStyle);
			HSSFCell brakesCell = contentRow.createCell(6);
			brakesCell.setCellValue(brakes);
			brakesCell.setCellStyle(contentCellStyle);
		}
		
		OutputStream out;
		try {
			out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new CommonException("导出Excel出错");
		}
		
	}
	
	public void exportCycleMonth() throws Exception{
		String vehicleIds = this.getParameterNoCheck("vehicleId");
//		String year = this.getParameterNoCheck("year");
//		String month = this.getParameterNoCheck("month");
		String yM = this.getParameterNoCheck("date");
		String arr[] = yM.split("-");
		String year = arr[0];
		String month = arr[1];
		String oilper = this.getParameterNoCheck("fuel");
		String platePram = this.getParameterNoCheck("plate");
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		if(StringUtils.isNotBlank(platePram)){
			boolean ifExistPlate = userService.ifExistPlate(platePram, Integer.parseInt(accountId), null);
			if(!ifExistPlate){
				throw new CommonException("车牌对应的车辆不存在");
			}
		}else{
			Object[]values = {accountId,null,null};
			List userList = userService.getList(values);
			for(int i=0;i<userList.size();i++){
				User user = (User)userList.get(i);
				if(StringUtils.isBlank(vehicleIds)){
					vehicleIds = user.getVehicleId().toString();
				}else{
					vehicleIds +=","+user.getVehicleId().toString();
				}
			}
		}
		List list = driveReportService.getCycleReportMileageOilByMonth(vehicleIds, year, month,oilper,platePram);
		HSSFWorkbook workBook = new HSSFWorkbook();
		String title = String.valueOf(System.currentTimeMillis());
		HSSFSheet sheet = workBook.createSheet(title);
		HttpServletResponse response = this.getResponse();
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition", "attachment;filename="+title+".xls");
		sheet.setDefaultColumnWidth(15);
		String identifer = "";
		String id = "";
		String plate = "";
		String date = "";
		String team = "";
		String mileage = "";
		String oil = "";
		String accTimes = "";
		String brakes = "";
		String speedTime = "";
		HSSFCellStyle cellHeadStyle = workBook.createCellStyle();
		HSSFCellStyle contentCellStyle = workBook.createCellStyle();
		contentCellStyle.setBorderBottom((short)1);
		contentCellStyle.setBorderTop((short)1);
		contentCellStyle.setBorderLeft((short)1);
		contentCellStyle.setBorderRight((short)1);
		cellHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellHeadStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		cellHeadStyle.setBorderBottom((short)1);
		cellHeadStyle.setBorderTop((short)1);
		cellHeadStyle.setBorderLeft((short)1);
		cellHeadStyle.setBorderRight((short)1);
		HSSFFont font = workBook.createFont();
		font.setBoldweight((short)2);
		cellHeadStyle.setFont(font);
		for(int i = 0;i<list.size();i++){
			Map map = (Map)list.get(i);
			id = (String)map.get("identifier");
			plate = (String)map.get("plate");
			date = (String)map.get("create_date");
			team = (String)map.get("team");
			mileage = map.get("mileage").toString();
			oil = map.get("fuel").toString();
			accTimes = map.get("acctimes").toString();
			brakes = map.get("sleepytimes").toString();
			speedTime = map.get("overspeedhours").toString();
			if(i == 0){
				HSSFRow row = sheet.createRow(0);
//				row.setRowStyle(cellHeadStyle);
				HSSFCell headPlateCell = row.createCell(0);
				headPlateCell.setCellValue("车牌号码");
				headPlateCell.setCellStyle(cellHeadStyle);
				HSSFCell headGroupCell = row.createCell(1);
				headGroupCell.setCellValue("所属分组");
				headGroupCell.setCellStyle(cellHeadStyle);
				HSSFCell headMileageCell = row.createCell(2);
				headMileageCell.setCellValue("行驶里程(公里)");
				headMileageCell.setCellStyle(cellHeadStyle);
				HSSFCell headOilCell = row.createCell(3);
				headOilCell.setCellValue("油耗(升)");
				headOilCell.setCellStyle(cellHeadStyle);
				HSSFCell headSpeedCell = row.createCell(4);
				headSpeedCell.setCellValue("超速时间(小时)");
				headSpeedCell.setCellStyle(cellHeadStyle);
				HSSFCell headAccCell = row.createCell(5);
				headAccCell.setCellValue("急加/减速次数");
				headAccCell.setCellStyle(cellHeadStyle);
				HSSFCell headSleepCell = row.createCell(6);
				headSleepCell.setCellValue("疲劳驾驶次数");
				headSleepCell.setCellStyle(cellHeadStyle);
			}
			HSSFRow contentRow = sheet.createRow(i+1);
			HSSFCell plateCell = contentRow.createCell(0);
			plateCell.setCellValue(plate);
			plateCell.setCellStyle(contentCellStyle);
			HSSFCell teamCell = contentRow.createCell(1);
			teamCell.setCellValue(team);
			teamCell.setCellStyle(contentCellStyle);
			HSSFCell mileageCell = contentRow.createCell(2);
			mileageCell.setCellValue(mileage);
			mileageCell.setCellStyle(contentCellStyle);
			HSSFCell oilCell = contentRow.createCell(3);
			oilCell.setCellValue(oil);
			oilCell.setCellStyle(contentCellStyle);
			HSSFCell speedTimeCell = contentRow.createCell(4);
			speedTimeCell.setCellValue(speedTime);
			speedTimeCell.setCellStyle(contentCellStyle);
			HSSFCell accTimesCell = contentRow.createCell(5);
			accTimesCell.setCellValue(accTimes);
			accTimesCell.setCellStyle(contentCellStyle);
			HSSFCell brakesCell = contentRow.createCell(6);
			brakesCell.setCellValue(brakes);
			brakesCell.setCellStyle(contentCellStyle);
		}
		
		OutputStream out;
		try {
			out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new CommonException("导出Excel出错");
		}
	}
	
	@Override
	public String getCompanyId(){
		String accountId;
		Account account = getSessionAccount();
		if(account==null){
			return "";
		}
		if(account.getRole().getRoleId()==1){
			accountId = account.getAccountId().toString();
		}else{
			accountId = account.getParentId().toString();
		}
		return accountId;
	}
}
