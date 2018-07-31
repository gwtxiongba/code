package com.chanceit.http.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

/**
 * Member entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "member", catalog = "carmonitor")
public class Member implements java.io.Serializable {

	// Fields
	@Expose
	private Integer id;
	@Expose
	private String userName;
	private String pwd;
	@Expose
	private String name;
	@Expose
	private String tel;
	@Expose
	private Integer teamId;
	@Expose
	private Integer leavel;
	@Expose
	private Integer ifDel;
	@Expose
	private Integer status;
	@Expose
	private Date createTime;
	@Expose
	private Integer deptId;
	@Expose
	private Integer warning;

	// Constructors

	/** default constructor */
	public Member() {
	}

	/** minimal constructor */
	public Member(String userName, String pwd, Integer teamId, Integer leavel,
			Integer ifDel) {
		this.userName = userName;
		this.pwd = pwd;
		this.teamId = teamId;
		this.leavel = leavel;
		this.ifDel = ifDel;
	}

	/** full constructor */
	public Member(String userName, String pwd, String name, String tel,
			Integer teamId, Integer leavel, Integer ifDel, Integer status,
			Date createTime,Integer deptId) {
		this.userName = userName;
		this.pwd = pwd;
		this.name = name;
		this.tel = tel;
		this.teamId = teamId;
		this.leavel = leavel;
		this.ifDel = ifDel;
		this.status = status;
		this.createTime = createTime;
		this.deptId=deptId;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "user_name", nullable = false)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "pwd", nullable = false)
	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "tel")
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "team_id", nullable = false)
	public Integer getTeamId() {
		return this.teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	@Column(name = "leavel")
	public Integer getLeavel() {
		return this.leavel;
	}

	public void setLeavel(Integer leavel) {
		this.leavel = leavel;
	}

	@Column(name = "if_del")
	public Integer getIfDel() {
		return this.ifDel;
	}

	public void setIfDel(Integer ifDel) {
		this.ifDel = ifDel;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 0)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "dept_id")
	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	
	@Column(name = "warning")
	public Integer getWarning() {
		return warning;
	}

	public void setWarning(Integer warning) {
		this.warning = warning;
	}

}