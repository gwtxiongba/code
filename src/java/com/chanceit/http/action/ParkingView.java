package com.chanceit.http.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.framework.utils.StringUtil;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Driver;
import com.chanceit.http.pojo.Parking;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IDriverService;
import com.chanceit.http.service.IParkingService;
import com.chanceit.http.service.IUserService;

@Component("parkingView")
public class ParkingView extends BaseAction{

	@Autowired
	@Qualifier("driverService")
	private IDriverService driverService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("parkingService")
	private IParkingService parkingService;
	
	private static final long serialVersionUID = -1761899972676417784L;
	
	public String saveParking() throws Exception{
		String p_id = getParameter("parkingId");
		String adminName = getParameter("adminName");
		String p_address = getParameter("address");
		String p_tel = getParameter("tel");
		String p_position = getParameter("position");
		String p_parkingName = getParameter("name");
		String driver_Id = getParameter("driverId");
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() == 3){
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_RIGHT,"您无权执行该操作");
		}
		
		String[] str = p_position.split(",");
		String x = str[0];
		String y = str[1];
		
		if(StringUtils.isBlank(p_parkingName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"请输入车库名称");
		if(StringUtils.isBlank(p_id)){	//add操作
			Driver driver = new Driver();
			driver.setDriverName(p_parkingName);
			driver.setCreateTime(new Date());
			driver.setLevel((short)1);
			String pinyinName = StringUtil.converterToFirstSpell(p_parkingName);
			//判断是否超过4位数,例爱新觉罗.XXXX,如果超过取前4位,不足4位补0
			if(pinyinName.length() > 4){
				pinyinName.substring(0, 4);
			}else if(pinyinName.length() < 4){
				int length = pinyinName.length();
				int minLength = 4 - length;
				for(int i = 0;i < minLength;i++){
					pinyinName += "0";
				}
			}
			String loginName = "";
			//查询是否有重名,有重名添加序号后缀,序号为尾数依次加1
			List driverList = driverService.getExistNameList(pinyinName);
			if(driverList.size() > 0){
				Driver tempDriver = (Driver)driverList.get(0);
				String tempName = tempDriver.getValName();
				int seriNo = Integer.parseInt(tempName.substring(tempName.length()-4, tempName.length())) + 1;
				loginName = pinyinName + seriNo;
			}else{
				loginName = pinyinName + "1000";
			}
			driver.setValName(loginName);
			driver.setValPassword("123456");
			driverService.save(driver);
			
			Parking parking = new Parking();
			parking.setParkingPosition(p_address);
			parking.setAdminName(adminName);
			parking.setDriver(driver);
			parking.setParkingTel(p_tel);
			parking.setX(x);
			parking.setY(y);
			parking.setCreateTime(new Date());
			parkingService.save(parking);
			return ResultManager.getSuccResult();
		}else{//edit
			Driver driver =	driverService.get(Integer.parseInt(driver_Id));
			if(driver==null){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"不存在此司机ID！");
			}
			Parking parking = parkingService.get(Integer.parseInt(p_id));
			if(parking == null){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"不存在此车库ID！");
			}
			parking.setAdminName(adminName);
			parking.setParkingPosition(p_address);
			parking.setDriver(driver);
			parking.setX(x);
			parking.setY(y);
			parking.setParkingTel(p_tel);
			parkingService.save(parking);
			return ResultManager.getSuccResult();
		}
			
	}
	
	public String deleteParking(){
		String p_id = getParameter("parkingId");
		String driverId = getParameter("driverId");
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() == 3){
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_RIGHT,"您无权执行该操作");
		}
		if(StringUtils.isBlank(p_id)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"请选择车库");
		if(StringUtils.isBlank(driverId)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"不存在driverId");
		
		if(parkingService.delete(p_id)){
			if(parkingService.delDriver(driverId)){
				parkingService.updateUser(Integer.parseInt(driverId));
					return ResultManager.getSuccResult();
			}
		}
		return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"ifMonitor参数异常");
	}
	
	public String parkingPageList(Page page) throws Exception{
		String plate = getParameter("plate");
		String driverId = "";
		if(StringUtils.isNotBlank(plate)){
			List list = parkingService.getUser(plate);
			if(list.size()>0){
				User user = (User) list.get(0);
				if(user.getDriver() == null){
					return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"车辆未入库");
				}
				driverId = user.getDriver().getDriverId().toString();
			}else{
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"车牌无效");
			}
		}
		Object[] keywords = new Object[]{driverId};
		page = parkingService.getParkingList(page,keywords);
		return ResultManager.getBodyResult(page);
	}
	
	public String userPageList(){
		String driver_Id = getParameter("driverId");
		if(StringUtils.isBlank(driver_Id)){
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"车库未绑定管理员");
		}
		Object[] keywords = new Object[]{driver_Id};
		page = parkingService.getUserList(page, keywords);
		return ResultManager.getBodyResult(page);
	}
}


















