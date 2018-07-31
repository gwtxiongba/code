package com.chanceit.http.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
 * Level entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "password_log")
public class PasswordLog implements java.io.Serializable {

	// Fields
	@Expose
	private Integer pwdId;
	@Expose
	private Integer accountId;
	@Expose
	private String password;

	// Constructors

	/** default constructor */
	public PasswordLog() {
	}

	/** full constructor */
	public PasswordLog(Integer pwdId, Integer accountId, String password) {
		this.pwdId = pwdId;
		this.accountId = accountId;
		this.password = password;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "pswd_id", unique = true, nullable = false)
	public Integer getPwdId() {
		return pwdId;
	}

	public void setPwdId(Integer pwdId) {
		this.pwdId = pwdId;
	}

	@Column(name = "account_id")
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}