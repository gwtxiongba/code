package com.chanceit.http.pojo;
// default package

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

/**
 * Driver entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "car_driver")
public class CarDriver implements java.io.Serializable {

	@Expose
	private Integer carDriverId;
	@Expose
	private User car;
	@Expose
	private Driver driver;
	@Expose
	private Date startTime;
	@Expose
	private Date endTime;

	/** default constructor */
	public CarDriver() {
	}

	/** full constructor */
	public CarDriver(Integer carDriverId, User car, Driver driver, Date startTime, Date endTime) {
		this.carDriverId = carDriverId;
		this.car = car;
		this.driver = driver;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "car_driver_id", unique = true, nullable = false)
	public Integer getCarDriverId() {
		return carDriverId;
	}

	public void setCarDriverId(Integer carDriverId) {
		this.carDriverId = carDriverId;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public User getCar() {
		return car;
	}

	public void setCar(User car) {
		this.car = car;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="driver_id")
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time", length = 0)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time", length = 0)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}