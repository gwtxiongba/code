package com.chanceit.http.pojo;

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
 * Account entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "account")
public class Account implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = -568735069139383214L;
	@Expose
	private Integer accountId;
	@Expose
	private String accountName;
	@Expose
	private String accountRealName;
	private Level level;
	private String accountPwd;
	@Expose
	private String companyName;
	@Expose
	private String accountTel;
	@Expose
	private String address;
	@Expose
	private String email;
	private String visitIp;
	@Expose
	private Date createTime;
	private String createIp;
	private Date visitTime;
	private Date sendTime;
	private String isActivate;
	private String randomCode;
	
	private String verificationCount;
	//new
	private Integer parentId;
	private String companyNick;
	private String coordinate;
	private Role role;
	private Integer logintimes;
	private Team team;
	private Dept dept;

	@Column(name = "logintimes")
	public Integer getLogintimes() {
		return logintimes;
	}

	public void setLogintimes(Integer logintimes) {
		this.logintimes = logintimes;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "parent_id")
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Column(name = "company_nick", length = 8)
	public String getCompanyNick() {
		return companyNick;
	}

	public void setCompanyNick(String companyNick) {
		this.companyNick = companyNick;
	}

	@Column(name = "coordinate", length = 20)
	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	// Constructors
	@Column(name = "verification_count", length = 5)
	public String getVerificationCount() {
		return verificationCount;
	}

	public void setVerificationCount(String verificationCount) {
		this.verificationCount = verificationCount;
	}

	/** default constructor */
	public Account() {
	}

	/** minimal constructor */
	public Account(Integer accountId) {
		this.accountId = accountId;
	}

	/** full constructor */
	public Account(Integer accountId, String accountName, Integer levelId, String accountRealName,
			String accountPwd, String accountNick, String accountTel,
			String address, Date createTime, String createIp, Date visitTime,Date sendTime,String randomCode,String isActivate,Team teamId,Dept dept) {
		this.accountId = accountId;
		this.accountName = accountName;
		this.accountRealName = accountRealName;
		this.accountPwd = accountPwd;
		this.companyName = accountNick;
		this.accountTel = accountTel;
		this.address = address;
		this.createTime = createTime;
		this.createIp = createIp;
		this.visitTime = visitTime;
		this.sendTime=sendTime;
		this.randomCode=randomCode;
		this.isActivate=isActivate;
		this.team = teamId;
		this.dept = dept;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "account_id", unique = true, nullable = false)
	public Integer getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	@Column(name = "account_real_name", length = 50)
	public String getAccountRealName() {
		return accountRealName;
	}

	public void setAccountRealName(String accountRealName) {
		this.accountRealName = accountRealName;
	}

	@Column(name = "account_name", length = 50)
	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "level_id")
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
	
	@Column(name = "account_pwd", length = 50)
	public String getAccountPwd() {
		return this.accountPwd;
	}

	public void setAccountPwd(String accountPwd) {
		this.accountPwd = accountPwd;
	}

	@Column(name = "account_tel", length = 20)
	public String getAccountTel() {
		return this.accountTel;
	}

	public void setAccountTel(String accountTel) {
		this.accountTel = accountTel;
	}

	@Column(name = "address", length = 200)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 0)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "create_ip", length = 20)
	public String getCreateIp() {
		return this.createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "visit_time", length = 0)
	public Date getVisitTime() {
		return this.visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	
	@Column(name = "company_name")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "visitIp")
	public String getVisitIp() {
		return visitIp;
	}

	public void setVisitIp(String visitIp) {
		this.visitIp = visitIp;
	}


	@Column(name = "is_activate")
	public String getIsActivate() {
		return isActivate;
	}

	public void setIsActivate(String isActivate) {
		this.isActivate = isActivate;
	}
	@Column(name = "random_code")
	public String getRandomCode() {
		return randomCode;
	}

	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "send_time", length = 0)
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dept_id")
	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}
	
}