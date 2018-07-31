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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

/**
 * Boxreminder entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "boxreminder")
public class Boxreminder implements java.io.Serializable {

	// Fields

	private Integer warnId;
	private User user;
	private Integer accountId;
	@Expose
	private Date time;
	private Date createTime;
	@Expose
	private String reader;
	private String coordinate;
	@Expose
	private Short ifRead;
	@Expose
	private String reason;
	private Short ifWarnPhone;
	private Short ifWarnWeb;
	private Short type;
	private String bkValue;
	@Expose
	private String x;
	@Expose
	private String y;
	@Expose
	private Driver driver;

	// Constructors

	/** default constructor */
	public Boxreminder() {
	}

	/** minimal constructor */
	public Boxreminder(Integer warnId) {
		this.warnId = warnId;
	}

	/** full constructor */
	public Boxreminder(Integer warnId, Integer vehicleId, Integer accountId,
			Date time, Date createTime, String reader, String coordinate,
			Short ifRead, String reason, Short ifWarnPhone, Short ifWarnWeb,
			Short type, String bkValue, String x, String y) {
		this.warnId = warnId;
		this.accountId = accountId;
		this.time = time;
		this.createTime = createTime;
		this.reader = reader;
		this.coordinate = coordinate;
		this.ifRead = ifRead;
		this.reason = reason;
		this.ifWarnPhone = ifWarnPhone;
		this.ifWarnWeb = ifWarnWeb;
		this.type = type;
		this.bkValue = bkValue;
		this.x = x;
		this.y = y;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "warn_id", unique = true, nullable = false)
	public Integer getWarnId() {
		return this.warnId;
	}

	public void setWarnId(Integer warnId) {
		this.warnId = warnId;
	}

	@Column(name = "account_id")
	public Integer getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 0)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@Column(name = "coordinate", length = 100)
	public String getCoordinate() {
		return this.coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
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

	@Column(name = "if_warn_phone")
	public Short getIfWarnPhone() {
		return this.ifWarnPhone;
	}

	public void setIfWarnPhone(Short ifWarnPhone) {
		this.ifWarnPhone = ifWarnPhone;
	}

	@Column(name = "if_warn_web")
	public Short getIfWarnWeb() {
		return this.ifWarnWeb;
	}

	public void setIfWarnWeb(Short ifWarnWeb) {
		this.ifWarnWeb = ifWarnWeb;
	}

	@Column(name = "type")
	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@Column(name = "bk_value", length = 100)
	public String getBkValue() {
		return this.bkValue;
	}

	public void setBkValue(String bkValue) {
		this.bkValue = bkValue;
	}

	@Column(name = "x")
	public String getX() {
		return this.x;
	}

	public void setX(String x) {
		this.x = x;
	}

	@Column(name = "y")
	public String getY() {
		return this.y;
	}

	public void setY(String y) {
		this.y = y;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vehicle_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "driver_id")
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
}