package com.chanceit.http.pojo;

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

@Entity
@Table(name = "parking")
public class Parking implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1662713580358050641L;
	@Expose
	private Integer parkingId;
	@Expose
	private String adminName;
	@Expose
	private String parkingPosition;
	@Expose
	private String parkingTel;
	@Expose
	private Date createTime;
	@Expose
	private String x;
	@Expose
	private String y;
	@Expose
	private Driver driver;
	
	public Parking(){
		
	}


	public Parking(Integer parkingId, String adminName, String parkingPosition,
			String parkingTel, Date createTime, String x, String y,
			Driver driver) {
		super();
		this.parkingId = parkingId;
		this.adminName = adminName;
		this.parkingPosition = parkingPosition;
		this.parkingTel = parkingTel;
		this.createTime = createTime;
		this.x = x;
		this.y = y;
		this.driver = driver;
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "parking_id", unique = true, nullable = false)
	public Integer getParkingId() {
		return parkingId;
	}

	public void setParkingId(Integer parkingId) {
		this.parkingId = parkingId;
	}

	
	@Column(name = "parking_position" , length = 30)
	public String getParkingPosition() {
		return parkingPosition;
	}


	public void setParkingPosition(String parkingPosition) {
		this.parkingPosition = parkingPosition;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 0)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "driver_id")
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	
	@Column(name = "admin_name" , length = 20)
	public String getAdminName() {
		return adminName;
	}


	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}


	@Column(name = "parking_tel" , length = 20)
	public String getParkingTel() {
		return parkingTel;
	}

	public void setParkingTel(String parkingTel) {
		this.parkingTel = parkingTel;
	}

	@Column(name = "x" , length = 20)
	public String getX() {
		return x;
	}


	public void setX(String x) {
		this.x = x;
	}

	@Column(name = "y" , length = 20)
	public String getY() {
		return y;
	}


	public void setY(String y) {
		this.y = y;
	}
	
}
