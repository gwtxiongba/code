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
@Table(name = "askleave")
public class Askleave implements java.io.Serializable {

	// Fields
	@Expose
	private Integer id;
	@Expose
	private Integer userId;
	@Expose
	private Date startTime;
	@Expose
	private Date endTime;
	@Expose
	private String reason;//请假原因
	@Expose
	private Integer status;
	@Expose
	private Date createTime;
	@Expose
	private String reason2;//拒绝原因
	
	

	/** default constructor */
	public Askleave() {
	}

	/** minimal constructor */
	public Askleave(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public Askleave(Integer id, Integer userId, Date startTime, Date endTime,
			String reason, Integer status, Date createTime, String reason2) {
		super();
		this.id = id;
		this.userId = userId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.reason = reason;
		this.status = status;
		this.createTime = createTime;
		this.reason2 = reason2;
	}

	// Property accessors
		@Id
		@GeneratedValue(strategy = IDENTITY)
		@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "user_id", nullable = false)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "reason")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "status",nullable=false)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="reason2")
	public String getReason2() {
		return reason2;
	}

	public void setReason2(String reason2) {
		this.reason2 = reason2;
	}

	
	
}