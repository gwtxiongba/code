/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.exceptions.CommonException;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Brand;
import com.chanceit.http.service.IBrandService;
import com.chanceit.http.service.IDriverService;
import com.chanceit.http.service.IMemberService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;
import com.chanceit.http.service.TeamService;

@Component("reportNewView")
public class ReportNewView extends BaseAction {
	private String charSet="UTF-8";// 字符集
	private int connectionTimeOut = 30000;// 连接超时时间
	private int readTimeOut = 40000;// 读取超时时间
	
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("driverService")
	private IDriverService driverService;
	
	@Autowired
	@Qualifier("memberService")
	private IMemberService memberService;
	
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	
//	public String listPage(Page page){
//		page = brandService.getPageList(page, null);
//		return ResultManager.getBodyResult(page);
//	}
	
	public String listCarReport(){
		//String brandName = getParameter("brandName");
		//String teams=teamService.getTeamIdStr(getSessionAccount());
		String begin_time=getParameter("begin_time")==null?null:(getParameter("begin_time")+" 00:00:00");
		String end_time=getParameter("end_time")==null?null:(getParameter("end_time")+" 23:59:59");
		int teams=getSessionAccount().getTeam().getTeamId();
		Object[] keywords = new Object[]{teams,begin_time,end_time};
		List list=new ArrayList();
		list = userService.getListForReport(keywords);
		return ResultManager.getBodyResult(list);
	}
	
	public String listMonthReport(){
		int type=Integer.parseInt(getParameter("type"));
		String id=getParameter("id");
		String begin_time=(getParameter("begin_time")==null||"null".equals(getParameter("begin_time")))?null:(getParameter("begin_time")+" 00:00:00");
		String end_time=(getParameter("end_time")==null||"null".equals(getParameter("end_time")))?null:(getParameter("end_time")+" 23:59:59");
		
		Object[] keywords=new Object[]{id,begin_time,end_time};
		List list=new ArrayList();
		if(type==1){//车辆 按月统计
			list=userService.getListMonthForReport(keywords);
		}
		if(type==2){//司机 按月统计
			list=driverService.getListMonthForReport(keywords);
		}
		if(type==3){//乘客 按月统计
			list=memberService.getListMonthForReport(keywords);
		}
//		
		return ResultManager.getBodyResult(list);
	}
	
	public String listDriverReport(){
		//String brandName = getParameter("brandName");
		//String teams=teamService.getTeamIdStr(getSessionAccount());
		String begin_time=getParameter("begin_time")==null?null:(getParameter("begin_time")+" 00:00:00");
		String end_time=getParameter("end_time")==null?null:(getParameter("end_time")+" 23:59:59");
		int teams=getSessionAccount().getTeam().getTeamId();
		Object[] keywords = new Object[]{teams,begin_time,end_time};
		List list=new ArrayList();
		list = driverService.getListForReport(keywords);
		return ResultManager.getBodyResult(list);
	}
	
	public String listMemberReport(){
		//String brandName = getParameter("brandName");
		//String teams=teamService.getTeamIdStr(getSessionAccount());
		String begin_time=getParameter("begin_time")==null?null:(getParameter("begin_time")+" 00:00:00");
		String end_time=getParameter("end_time")==null?null:(getParameter("end_time")+" 23:59:59");
		int teams=getSessionAccount().getTeam().getTeamId();
		Object[] keywords = new Object[]{teams,begin_time,end_time};
		List list=new ArrayList();
		list = memberService.getListForReport(keywords);
		return ResultManager.getBodyResult(list);
	}
	
	public String listMonthReportDetail() throws Exception{
		List<Map<String, String>> excelMap1 = new ArrayList<Map<String,String>>();
		String type = getParameterNoCheck("type");//根据值判断是哪个统计(车辆/司机/乘客)
		String id = getParameterNoCheck("id");//统计对象的id
		String date=getParameterNoCheck("date");//统计月份       格式：201611
		String begin_time=(getParameter("begin_time")==null||"".equals(getParameter("begin_time")))?null:(getParameter("begin_time")+" 00:00:00");
		String end_time=(getParameter("end_time")==null||"".equals(getParameter("end_time")))?null:(getParameter("end_time")+" 23:59:59");
		
		//int teamId=getSessionAccount().getTeam().getTeamId();
		Object[] keywords ;
		keywords = new Object[]{id,date,begin_time,end_time};
		
		//List list = userService.getListForExcel(keywords);
		List list=null;
		if("monthStats1_id".equals(type)){
			list=userService.getListMonthDetailForReport(keywords);
		}else if("monthStats2_id".equals(type)){
			list=driverService.getListMonthDetailForReport(keywords);
		}else if("monthStats3_id".equals(type)){
			list=memberService.getListMonthDetailForReport(keywords);
		}
		
		for (int i = 0; i < list.size(); i++) {
			Map map=(Map) list.get(i);
//			String ii = (String) map.get("identifier");
//			String result=ResultManager.getBodyResult(list.get(i));
//			JSONObject jo=new JSONObject(result);
//			map.put("identifier",jo.getString("identifier"));
//			map.put("plate",jo.getString("plate"));
//			map.put("team",jo.getString("teamName")==null?"":jo.getString("teamName"));
//			map.put("type",jo.getString("carStyle")==null?"":jo.getString("carStyle"));
//			map.put("brand",jo.getString("brand")==null?"":jo.getString("brand"));
//			map.put("regTime",jo.getString("reg_time")==null?"":jo.getString("reg_time"));
//			map.put("buyTime",jo.getString("buy_time")==null?"":jo.getString("buy_time"));
//			map.put("tel",jo.getString("tel")==null?"":jo.getString("tel"));
//			map.put("iccid",jo.getString("iccid")==null?"":jo.getString("iccid"));
//			if(map.get("online") != null){
//				int o = (Integer) map.get("online");
//				map.put
//			}
//			if(jo.getString("online")=="null"){
//				map.put("online","空闲");
//			}
//			else if("0".equals(jo.getString("online"))){
//				map.put("online","空闲");
//			}
//			else if("1".equals(jo.getString("online"))){
//				map.put("online","维修中");
//			}
//			else if("2".equals(jo.getString("online"))){
//				map.put("online","已派出");
//			} 
			excelMap1.add(map);
		}
		export(excelMap1);
		return null;
	}
	
	/*****
	 * 导出明细 excel 
	 * add   2016.12.12
	 * *****/
	public void export(List list) throws Exception{
		HSSFWorkbook workBook = new HSSFWorkbook();
		String title = String.valueOf(System.currentTimeMillis());
		HSSFSheet sheet = workBook.createSheet(title);
		HttpServletResponse response = getResponse();
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition", "attachment;filename="+title+".xls");
		sheet.setDefaultColumnWidth(16);
		String obj="";//统计对象
		String start_time="";
		String over_time="";
		String mile="";//总里程
		String time="";//总时长
		String fee="";//总费用
		
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
		//System.out.println(list);
		for(int i = 0;i<list.size();i++){
			Map map = (Map)list.get(i);
//			if(map.containsKey("uid")){
//				uid = map.get("uid").toString();
//			}
//			if(map.containsKey("plate")){
//				plate = map.get("plate").toString();
//			}
			if(map.containsKey("obj")){
				obj = map.get("obj").toString();
			}
			if(map.containsKey("start_time")){
				start_time = map.get("start_time").toString();
			}
			if(map.containsKey("over_time")){
				over_time = map.get("over_time").toString();
			}
			if(map.containsKey("mile")){
				mile = map.get("mile").toString();
				}
			if(map.containsKey("time")){
				time = map.get("time").toString();
			}
			if(map.containsKey("fee")){
				fee = map.get("fee").toString();
			}
//			 if(team == null || team == ""){
//				 team = "未分组车辆";
//			 }
			if(i == 0){
				HSSFRow row = sheet.createRow(0);
			HSSFCell cell10 = row.createCell(0);
			cell10.setCellValue("统计对象");
			cell10.setCellStyle(cellHeadStyle);
			HSSFCell cell11 = row.createCell(1);
			cell11.setCellValue("开始时间");
			cell11.setCellStyle(cellHeadStyle);
			HSSFCell cell12 = row.createCell(2);
			cell12.setCellValue("结束时间");
			cell12.setCellStyle(cellHeadStyle);
			HSSFCell cell13 = row.createCell(3);
			cell13.setCellValue("总里程（公里）");
			cell13.setCellStyle(cellHeadStyle);
			HSSFCell cell14=row.createCell(4);
			cell14.setCellValue("总时长（小时）");
			cell14.setCellStyle(cellHeadStyle);
			HSSFCell cell15=row.createCell(5);
			cell15.setCellValue("总费用（元）");
			cell15.setCellStyle(cellHeadStyle);
			}
			
			HSSFRow contentRow = sheet.createRow(i+1);
				HSSFCell cell_a0 = contentRow.createCell(0);
				cell_a0.setCellValue(obj);
				cell_a0.setCellStyle(contentCellStyle);
				HSSFCell cell_a1 = contentRow.createCell(1);
				cell_a1.setCellValue(start_time);
				cell_a1.setCellStyle(contentCellStyle);
				HSSFCell cell_a2 = contentRow.createCell(2);
				cell_a2.setCellValue(over_time);
				cell_a2.setCellStyle(contentCellStyle);
				HSSFCell cell_a3 = contentRow.createCell(3);
				cell_a3.setCellValue(mile);
				cell_a3.setCellStyle(contentCellStyle);
				HSSFCell cell_a4 = contentRow.createCell(4);
				cell_a4.setCellValue(time);
				cell_a4.setCellStyle(contentCellStyle);
				HSSFCell cell_a5 = contentRow.createCell(5);
				cell_a5.setCellValue(fee);
				cell_a5.setCellStyle(contentCellStyle);
			
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
	
}
