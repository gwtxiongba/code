package com.chanceit.http.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name="cycle_report")
public class CycleReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String identifier;//设备码
	private String plate;//车牌
	private String group;//所属分组
	private String driver;//车主
	private Float mileage;//总里程数
	private Float oilCount;//总耗油量
	private Float perOilCount;//百公里油耗
	private Integer beyondSpeedCount;//超速次数
	private Integer beyondParkCount;//超时停车次数
	private Float maxSpeed;//最大时速
	private Float beyondSpeedTime;
	private Date createDate;
	private Float countTime;//当日行驶时间
	private Integer brakes;//急减速次数
	private Integer accTimes;//急加速次数
	private Integer fatigueTimes;//疲劳驾驶次数
	private Integer vehicleId;//车辆ID
	public CycleReport(){
		
	}
	
	public CycleReport(Integer id){
		this.id = id;
	}
	
	
	public CycleReport(Integer id, String identifier, String plate,
			String group, String driver, Float mileage, Float oilCount,
			Float perOilCount, Integer beyondSpeedCount, Integer beyondParkCount,
			Float maxSpeed,Float beyondSpeedTime
			,Date createDate,Float countTime,
			Integer brakes,Integer accTimes,Integer fatigueTimes,
			Integer vehicleId) {
		this.id = id;
		this.identifier = identifier;
		this.plate = plate;
		this.group = group;
		this.driver = driver;
		this.mileage = mileage;
		this.oilCount = oilCount;
		this.perOilCount = perOilCount;
		this.beyondSpeedCount = beyondSpeedCount;
		this.beyondParkCount = beyondParkCount;
		this.maxSpeed = maxSpeed;
		this.beyondSpeedTime = beyondSpeedTime;
		this.createDate = createDate;
		this.countTime = countTime;
		this.brakes = brakes;
		this.accTimes = accTimes;
		this.fatigueTimes = fatigueTimes;
		this.vehicleId = vehicleId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="identifier",length=50)
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	@Column(name="plate",length=50)
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	
	@Column(name="group_name",length=50)
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	@Column(name="driver",length=50)
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	@Column(name="mileage")
	public Float getMileage() {
		return mileage;
	}
	public void setMileage(Float mileage) {
		this.mileage = mileage;
	}
	
	@Column(name="oil_count")
	public Float getOilCount() {
		return oilCount;
	}
	public void setOilCount(Float oilCount) {
		this.oilCount = oilCount;
	}
	
	@Column(name="per_oil_count")
	public Float getPerOilCount() {
		return perOilCount;
	}
	public void setPerOilCount(Float perOilCount) {
		this.perOilCount = perOilCount;
	}
	
	@Column(name="beyond_speed_count")
	public Integer getBeyondSpeedCount() {
		return beyondSpeedCount;
	}
	public void setBeyondSpeedCount(Integer beyondSpeedCount) {
		this.beyondSpeedCount = beyondSpeedCount;
	}
	
	@Column(name="beyond_park_count")
	public Integer getBeyondParkCount() {
		return beyondParkCount;
	}
	public void setBeyondParkCount(Integer beyondParkCount) {
		this.beyondParkCount = beyondParkCount;
	}

	@Column(name="beyond_speed_time")
	public Float getBeyondSpeedTime() {
		return beyondSpeedTime;
	}

	public void setBeyondSpeedTime(Float beyondSpeedTime) {
		this.beyondSpeedTime = beyondSpeedTime;
	}

	@Column(name="max_speed")
	public Float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(Float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name="count_time")
	public Float getCountTime() {
		return countTime;
	}

	public void setCountTime(Float countTime) {
		this.countTime = countTime;
	}

	@Column(name="brakes")
	public Integer getBrakes() {
		return brakes;
	}

	
	public void setBrakes(Integer brakes) {
		this.brakes = brakes;
	}

	@Column(name="acc_times")
	public Integer getAccTimes() {
		return accTimes;
	}

	public void setAccTimes(Integer accTimes) {
		this.accTimes = accTimes;
	}

	@Column(name="fatigue_times")
	public Integer getFatigueTimes() {
		return fatigueTimes;
	}

	public void setFatigueTimes(Integer fatigueTimes) {
		this.fatigueTimes = fatigueTimes;
	}

	@Column(name="vehicle_id")
	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}
	
}
