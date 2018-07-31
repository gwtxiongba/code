package com.chanceit.http.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "dept", catalog = "carmonitor")
public class Dept implements java.io.Serializable {

	// Fields
	@Expose
	private Integer deptId;
	@Expose
	private String deptName;
	@Expose
	private Integer teamId;

	// Constructors

	/** default constructor */
	public Dept() {
	}

	/** full constructor */
	public Dept(String deptName, Integer teamId) {
		this.deptName = deptName;
		this.teamId = teamId;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Integer id) {
		this.deptId = id;
	}

	@Column(name = "name", nullable = false)
	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "team_id", nullable = false)
	public Integer getTeamId() {
		return this.teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

}