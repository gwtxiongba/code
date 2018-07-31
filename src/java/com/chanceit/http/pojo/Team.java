package com.chanceit.http.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.List;

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
 * Level entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "team")
public class Team implements java.io.Serializable {

	// Fields
	@Expose
	private Integer teamId;
	@Expose
	private String teamName;
	private Date createTime;
	//private Account account;
	private Integer accountId;
	@Expose
	private Integer order;
	@Expose
	private Integer amount;
	private Integer operator;
	private Integer pid;
	
	@Column(name = "pid")
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	// Constructors
	@Column(name = "team_order")
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	/** default constructor */
	public Team() {
	}

	/** minimal constructor */
	public Team(Integer teamId) {
		this.teamId = teamId;
	}


	// Property accessors


	@Column(name = "team_name", length = 50)
	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}



	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 0)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "car_amount")
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "team_id", unique = true, nullable = false)
	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	@Column(name = "account_id")
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	@Column(name = "operator_id")
	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}


	

}