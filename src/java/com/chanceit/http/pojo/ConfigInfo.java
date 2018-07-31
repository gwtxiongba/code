package com.chanceit.http.pojo;

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
 * 全局配置表,方面全部参数的拓展,可添加字段
 */
@Entity
@Table(name = "config_info")
public class ConfigInfo implements java.io.Serializable {

	// Fields
	@Expose
	private Integer configId;
	@Expose
	private Account account;
	@Expose
	private Date scanDate;
	@Expose
	private String configInfo;

	// Constructors

	/** default constructor */
	public ConfigInfo() {
	}

	/** full constructor */
	public ConfigInfo(Integer configId, Account account, Date scanDate, String configInfo) {
		this.configId = configId;
		this.account = account;
		this.scanDate = scanDate;
		this.configInfo = configInfo;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "config_id", unique = true, nullable = false)
	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "scan_date", length = 0)
	public Date getScanDate() {
		return scanDate;
	}

	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}

	@Column(name = "config_info")
	public String getConfigInfo() {
		return configInfo;
	}

	public void setConfigInfo(String configInfo) {
		this.configInfo = configInfo;
	}

}