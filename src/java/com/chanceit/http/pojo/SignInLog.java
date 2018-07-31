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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

/**
 * Message entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sign_in_log")
public class SignInLog implements java.io.Serializable {

	@Expose
	private Integer logId;
	@Expose
	private Driver driver;
	@Expose
	private User car;
	@Expose
	private Integer ifLegal;
	@Expose
	private Integer fromType;
	@Expose
	private Date signInTime;
	@Expose
	private String logInfo;
	@Expose
	private Date unbindTime;
	@Expose
	private String x;
	@Expose
	private String y;
	/** default constructor */
	public SignInLog() {
	}

	/** full constructor */
	public SignInLog(Integer logId, Integer ifLegal, Integer fromType,
			Date signInTime, String logInfo, Date unbindTime) {
		super();
		this.logId = logId;
		this.ifLegal = ifLegal;
		this.fromType = fromType;
		this.signInTime = signInTime;
		this.logInfo = logInfo;
		this.unbindTime = unbindTime;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "log_id")
	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="driver_id")
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public User getCar() {
		return car;
	}

	public void setCar(User car) {
		this.car = car;
	}

	@Column(name = "if_legal")
	public Integer getIfLegal() {
		return ifLegal;
	}

	public void setIfLegal(Integer ifLegal) {
		this.ifLegal = ifLegal;
	}
	
	@Column(name = "from_type")
	public Integer getFromType() {
		return fromType;
	}

	public void setFromType(Integer fromType) {
		this.fromType = fromType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sign_in_time", length = 0)
	public Date getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(Date signInTime) {
		this.signInTime = signInTime;
	}

	@Column(name = "log_info")
	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "unbind_time", length = 0)
	public Date getUnbindTime() {
		return unbindTime;
	}

	public void setUnbindTime(Date unbindTime) {
		this.unbindTime = unbindTime;
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