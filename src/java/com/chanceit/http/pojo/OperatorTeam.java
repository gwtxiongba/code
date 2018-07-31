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
@Table(name = "operator_team")
public class OperatorTeam implements java.io.Serializable {

	@Expose
	private Integer opTeamId;
	@Expose
	private Account account;
	@Expose
	private Team team;
	@Expose
	private Date createTime;

	/** default constructor */
	public OperatorTeam() {
	}

	/** full constructor */
	public OperatorTeam(Integer opTeamId, Account account, Team team, Date createTime) {
		this.opTeamId = opTeamId;
		this.account = account;
		this.team = team;
		this.createTime = createTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "op_team_id", unique = true, nullable = false)
	public Integer getOpTeamId() {
		return opTeamId;
	}

	public void setOpTeamId(Integer opTeamId) {
		this.opTeamId = opTeamId;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "operator_id")
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="team_id")
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 0)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}