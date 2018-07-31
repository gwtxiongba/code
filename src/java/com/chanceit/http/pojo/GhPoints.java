package com.chanceit.http.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "ghpoints")
public class GhPoints implements java.io.Serializable {
	@Expose
	private Integer lineId;
	@Expose
	private String path;
	private String ws;
	@Expose
	private float radius;
	@Expose
	private String lineName;
	@Expose
	private String distance;
	@Expose
	private Date createTime;
    
	private Integer accountId;
	
	private Date delTime;
	private Integer ifDel;
	private Integer opId;
	private Integer teamId;
	@Column(name = "team_id")
	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deltime", length = 0)
	public Date getDelTime() {
		return delTime;
	}

	public void setDelTime(Date delTime) {
		this.delTime = delTime;
	}
	
	@Column(name = "ifdel")
	public Integer getIfDel() {
		return ifDel;
	}

	public void setIfDel(Integer ifDel) {
		this.ifDel = ifDel;
	}

	@Column(name = "account_id")
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getLineId() {
		return lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}


	@Column(name = "ws")
	public String getWs() {
		return ws;
	}

	public void setWs(String ws) {
		this.ws = ws;
	}

	
	
	@Column(name = "pts")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	@Column(name = "name")
	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	
	@Column(name = "distance", length = 20)
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time", length = 0)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "r")
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	@Column(name = "op_id")
	public Integer getOpId() {
		return opId;
	}

	public void setOpId(Integer opId) {
		this.opId = opId;
	}
}
