package com.chanceit.http.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * MessageStats entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "message_stats")
public class MessageStats implements java.io.Serializable {

	// Fields

	private String id;
	private Date createTime;
	private String identifier;

	// Constructors

	/** default constructor */
	public MessageStats() {
	}

	/** minimal constructor */
	public MessageStats(String id, String identifier) {
		this.id = id;
		this.identifier = identifier;
	}

	/** full constructor */
	public MessageStats(String id, Date createTime, String identifier) {
		this.id = id;
		this.createTime = createTime;
		this.identifier = identifier;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 0)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "identifier", nullable = false)
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}





}