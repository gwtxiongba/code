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
@Table(name = "base_info")
public class BaseInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4080516237516454509L;
	@Expose
	private Integer baseInfoId;
	@Expose
	private String brand;
	@Expose
	private Double weight;
	@Expose
	private Short totalNumber;
	@Expose
	private Date prcTime;
	@Expose
	private Date limTime;
	@Expose
	private User user;
	
	public BaseInfo(){}
	
	
	public BaseInfo(Integer baseInfoId, String brand, double weight,
			short totalNumber, Date prcTime, Date limTime, User user) {
		super();
		this.baseInfoId = baseInfoId;
		this.brand = brand;
		this.weight = weight;
		this.totalNumber = totalNumber;
		this.prcTime = prcTime;
		this.limTime = limTime;
		this.user = user;
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "base_id", unique = true, nullable = false)
	public Integer getBaseInfoId() {
		return baseInfoId;
	}
	public void setBaseInfoId(Integer baseInfoId) {
		this.baseInfoId = baseInfoId;
	}
	
	@Column(name = "brand", length = 20)
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@Column(name = "veh_weight")
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	@Column(name = "veh_number")
	public short getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(short totalNumber) {
		this.totalNumber = totalNumber;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "production_time", length = 0)
	public Date getPrcTime() {
		return prcTime;
	}
	public void setPrcTime(Date prcTime) {
		this.prcTime = prcTime;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "limited_time", length = 0)
	public Date getLimTime() {
		return limTime;
	}

	public void setLimTime(Date limTime) {
		this.limTime = limTime;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
