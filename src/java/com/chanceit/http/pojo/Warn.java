/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jul 26, 2013
 * Id: UserStatus.java,v 1.0 Jul 26, 2013 2:06:51 PM Administrator
 */
package com.chanceit.http.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

/**
 * @ClassName UserStatus
 * @author Administrator
 * @date Jul 26, 2013 2:06:51 PM
 * @Description 用户的状态信息
 */
@Entity
@Table(name = "warn")
public class Warn {
	private User user;
	private Integer warnId;
	private Integer companyId;
	private Date createTime;
	private int status;
	private String identifier;                	//盒子ID
	private Integer nowSpeed;                  	//当前车速
	private Integer engineSpeed;               	//发送机转速
	private Integer batteryVoltage;            	//电瓶电压
	private Integer coolantTemp;			   	//冷却液温度
	private Integer engineLoad;				   	//发送机负载
	private Integer throttle;				   	//节气门位置
	private Integer airPressure;               	//大气压
	private Integer intakePressure;			   	//进气管压力
	private Integer airFlow;				   	//空气流量 
	private Integer airTemp;					//空气温度
	private Integer intakeTemp;					//进气管温度
	private String obdProtocol;					//OBD协议
	private String obdStandand;					//OBD标准
	private Integer fuelConsumption;			//燃油量

	private String para;//参数字符串
	private String binaryStatus;//位
	

	@Expose
	private Driver driver;
	@Expose
	private Date time;
	@Expose
	private String reader;
	@Expose
	private Short ifRead;
	@Expose
	private String reason;
	/**
	 * 
	 */
	public Warn() {
		super();
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "warn_id", unique = true, nullable = false)
	public Integer getWarnId() {
		return warnId;
	}

	public void setWarnId(Integer warnId) {
		this.warnId = warnId;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vehicle_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name="status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column(name="company_id")
	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@Column(name="identifier")
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Column(name="now_speed")
	public Integer getNowSpeed() {
		return nowSpeed;
	}

	public void setNowSpeed(Integer nowSpeed) {
		if(nowSpeed == null) nowSpeed = 0;
		this.nowSpeed = nowSpeed;
	}

	@Column(name="engine_speed")
	public Integer getEngineSpeed() {
		return engineSpeed;
	}

	public void setEngineSpeed(Integer engineSpeed) {
		if(engineSpeed == null) engineSpeed = 0;
		this.engineSpeed = engineSpeed;
	}

	@Column(name="battery_voltage")
	public Integer getBatteryVoltage() {
		return batteryVoltage;
	}

	public void setBatteryVoltage(Integer batteryVoltage) {
		if(batteryVoltage == null) batteryVoltage = 0;
		this.batteryVoltage = batteryVoltage;
	}

	@Column(name="coolant_temp")
	public Integer getCoolantTemp() {
		return coolantTemp;
	}

	public void setCoolantTemp(Integer coolantTemp) {
		if(coolantTemp == null) coolantTemp = 0;
		this.coolantTemp = coolantTemp;
	}

	@Column(name="engine_load")
	public Integer getEngineLoad() {
		return engineLoad;
	}

	public void setEngineLoad(Integer engineLoad) {
		if(engineLoad == null) engineLoad = 0;
		this.engineLoad = engineLoad;
	}

	@Column(name="throttle")
	public Integer getThrottle() {
		return throttle;
	}

	public void setThrottle(Integer throttle) {
		if(throttle == null) throttle = 0;
		this.throttle = throttle;
	}

	@Column(name="air_pressure")
	public Integer getAirPressure() {
		return airPressure;
	}

	public void setAirPressure(Integer airPressure) {
		if(airPressure == null) airPressure = 0;
		this.airPressure = airPressure;
	}

	@Column(name="intake_pressure")
	public Integer getIntakePressure() {
		return intakePressure;
	}

	public void setIntakePressure(Integer intakePressure) {
		if(intakePressure == null) intakePressure = 0;
		this.intakePressure = intakePressure;
	}

	@Column(name="air_flow")
	public Integer getAirFlow() {
		return airFlow;
	}

	public void setAirFlow(Integer airFlow) {
		if(airFlow == null) airFlow = 0;
		this.airFlow = airFlow;
	}

	@Column(name="air_temp")
	public Integer getAirTemp() {
		return airTemp;
	}

	public void setAirTemp(Integer airTemp) {
		if(airTemp == null) airTemp = 0;
		this.airTemp = airTemp;
	}

	@Column(name="intake_temp")
	public Integer getIntakeTemp() {
		return intakeTemp;
	}

	public void setIntakeTemp(Integer intakeTemp) {
		if(intakeTemp == null) intakeTemp = 0;
		this.intakeTemp = intakeTemp;
	}

	@Column(name="obd_protocol")
	public String getObdProtocol() {
		return obdProtocol;
	}

	public void setObdProtocol(String obdProtocol) {
		if(obdProtocol == null) obdProtocol = "";
		this.obdProtocol = obdProtocol;
	}

	@Column(name="obd_standard")
	public String getObdStandand() {
		return obdStandand;
	}

	public void setObdStandand(String obdStandand) {
		if(obdStandand == null) obdStandand = "";
		this.obdStandand = obdStandand;
	}

	@Column(name="fuel_consumption")
	public Integer getFuelConsumption() {
		return fuelConsumption;
	}

	public void setFuelConsumption(Integer fuelConsumption) {
		if(fuelConsumption == null) fuelConsumption = 0;
		this.fuelConsumption = fuelConsumption;
	}
	
	@Column(name="para")
	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	@Column(name = "binary_satus")
	public String getBinaryStatus() {
		return binaryStatus;
	}

	public void setBinaryStatus(String binaryStatus) {
		this.binaryStatus = binaryStatus;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "driver_id")
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	@Column(name = "if_read")
	public Short getIfRead() {
		return this.ifRead;
	}

	public void setIfRead(Short ifRead) {
		this.ifRead = ifRead;
	}

	@Column(name = "reason", length = 65535)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Column(name = "reader", length = 20)
	public String getReader() {
		return this.reader;
	}
	
	public void setReader(String reader) {
		this.reader = reader;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time", length = 0)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
