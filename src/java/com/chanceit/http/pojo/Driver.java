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
@Table(name = "driver")
public class Driver implements java.io.Serializable {

	// Fields
	@Expose
	private Integer driverId;
	private Integer accountId;
	@Expose
	private String driverName;
	@Expose
	private String driverTel;
	private Date createTime;
	@Expose
	private String remark;
	@Expose
	private String license;
	
	@Expose
	private String valName;
	@Expose
	private String valPassword;
	@Expose
	private String cardNo;
	@Expose
	private Short ifDel;
	@Expose
	private Short level;
	
	@Expose
	private Integer teamId;
	@Expose
	private String zjcx;
	@Expose
	private Integer status;
	@Expose
	private String userName;
	@Expose
	private String pwd;
	@Expose
	private Date endTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time", length = 0)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "user_name")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name = "pwd")
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}


	/** default constructor */
	public Driver() {
	}

	/** minimal constructor */
	public Driver(Integer driverId) {
		this.driverId = driverId;
	}

	/** full constructor */
	public Driver(Integer driverId, String driverName, String driverTel,
			Date createTime, String remark, String license) {
		this.driverId = driverId;
		this.driverName = driverName;
		this.driverTel = driverTel;
		this.createTime = createTime;
		this.remark = remark;
		this.license = license;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "driver_id", unique = true, nullable = false)
	public Integer getDriverId() {
		return this.driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	@Column(name = "driver_name", length = 20)
	public String getDriverName() {
		return this.driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	@Column(name = "driver_tel", length = 20)
	public String getDriverTel() {
		return this.driverTel;
	}

	public void setDriverTel(String driverTel) {
		this.driverTel = driverTel;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 0)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "remark", length = 65535)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "license", length = 50)
	public String getLicense() {
		return this.license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	@Column(name = "val_name", length = 20)
	public String getValName() {
		return valName;
	}

	public void setValName(String valName) {
		this.valName = valName;
	}
	
	@Column(name = "val_password", length = 20)
	public String getValPassword() {
		return valPassword;
	}

	public void setValPassword(String valPassword) {
		this.valPassword = valPassword;
	}
	
	@Column(name = "card_no", length = 20)
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	@Column(name = "if_del")
	public Short getIfDel() {
		return ifDel;
	}

	public void setIfDel(Short ifDel) {
		this.ifDel = ifDel;
	}
	@Column(name = "level")
	public Short getLevel() {
		return level;
	}

	public void setLevel(Short level) {
		this.level = level;
	}

	@Column(name = "team_id")
	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	@Column(name = "zjcx", length = 45)
	public String getZjcx() {
		return zjcx;
	}

	public void setZjcx(String zjcx) {
		this.zjcx = zjcx;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "account_id")
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	
}