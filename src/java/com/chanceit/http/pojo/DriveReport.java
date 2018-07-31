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
@Table(name="driver_report")
public class DriveReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String identifier;
	private String plate;
	private String driver;
	private Date startTime; 
	private Date endTime;
	private float time_length;
	private float mileage;
	private float maxSpeed;
	private String beginAddress;
	private String overAddress;
	private String groupName;
	@Column(name="group_name")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public DriveReport() {
	}

	/** minimal constructor */
	public DriveReport(Integer id) {
		this.id = id;
	}
	public DriveReport(Integer id,String plate,String driver,Date startTime,Date endTime,float time_length,float mileage,
			float maxSpeed,String beginAddress,String overAddress){
		this.id = id;
		this.plate = plate;
		this.driver = driver;
		this.startTime = startTime;
		this.endTime = endTime;
		this.time_length = time_length;
		this.mileage = mileage;
		this.maxSpeed = maxSpeed;
		this.beginAddress = beginAddress;
		this.overAddress = overAddress;
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

	@Column(name="plate",length=50)
	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	@Column(name="driver",length=50)
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time",length=0)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_time",length=0)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name="time_length")
	public float getTime_length() {
		return time_length;
	}

	public void setTime_length(float time_length) {
		this.time_length = time_length;
	}

	@Column(name="mileage")
	public float getMileage() {
		return mileage;
	}

	public void setMileage(float mileage) {
		this.mileage = mileage;
	}

	@Column(name="max_speed")
	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Column(name="begin_address",length=100)
	public String getBeginAddress() {
		return beginAddress;
	}

	public void setBeginAddress(String beginAddress) {
		this.beginAddress = beginAddress;
	}

	@Column(name="over_address",length=100)
	public String getOverAddress() {
		return overAddress;
	}

	public void setOverAddress(String overAddress) {
		this.overAddress = overAddress;
	}
	@Column(name="identifier",length=50)
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
