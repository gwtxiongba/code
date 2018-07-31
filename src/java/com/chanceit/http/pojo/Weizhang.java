package com.chanceit.http.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Baoxiao entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "weizhang", catalog = "carmonitor")
public class Weizhang implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer carId;
	private Date foulTime;
	private String foulAddress;
	private String foulReason;
	private Integer foulMark;
	private double foulPrice;
	private Integer driverId;

	// Constructors

	/** default constructor */
	public Weizhang() {
	}

	/** minimal constructor */
	public Weizhang(Integer id) {
		this.id = id;
	}

	
	
	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}



	public void setId(Integer id) {
		this.id = id;
	}

	
	@Column(name = "car_id", nullable = false)
	public Integer getCarId() {
		return this.carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	
	@Temporal(TemporalType.DATE)
	@Column(name = "foul_time")
	public Date getFourTime() {
		return foulTime;
	}

	public void setFourTime(Date foulTime) {
		this.foulTime = foulTime;
	}

	@Column(name = "foul_address")
	public String getFourAddress() {
		return foulAddress;
	}

	public void setFourAddress(String foulAddress) {
		this.foulAddress = foulAddress;
	}

	@Column(name = "foul_reason")
	public String getFourReason() {
		return foulReason;
	}

	public void setFourReason(String foulReason) {
		this.foulReason = foulReason;
	}

	@Column(name = "foul_mark")
	public Integer getFourMark() {
		return foulMark;
	}

	public void setFourMark(Integer foulMark) {
		this.foulMark = foulMark;
	}

	@Column(name = "foul_price")
	public double getFourPrice() {
		return foulPrice;
	}

	public void setFourPrice(double foulPrice) {
		this.foulPrice = foulPrice;
	}

	@Column(name = "driver_id", nullable = false)
	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}


}