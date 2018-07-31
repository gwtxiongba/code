package com.chanceit.http.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
 * Dept entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "car_team", catalog = "carmonitor")
public class CarTeam implements java.io.Serializable {

	// Fields
	@Expose
	private Integer carTeamId;
	@Expose
	private String carTeamName;
	@Expose
	private Integer teamId;
    private Date createTime;
	// Constructors

	/** default constructor */
	public CarTeam() {
	}

	/** full constructor */
	public CarTeam(String carTeamName, Integer teamId) {
		this.carTeamName = carTeamName;
		this.teamId = teamId;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getCarTeamId() {
		return this.carTeamId;
	}

	public void setCarTeamId(Integer carTeamId) {
		this.carTeamId = carTeamId;
	}

	@Column(name = "name", nullable = false)
	public String getCarTeamName() {
		return this.carTeamName;
	}

	public void setCarTeamName(String carTeamName) {
		this.carTeamName = carTeamName;
	}

	@Column(name = "team_id", nullable = false)
	public Integer getTeamId() {
		return this.teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}