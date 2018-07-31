package com.chanceit.http.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Driver entity.
 * 
 * @author gwt
 */
@Entity
@Table(name = "linewarn")
public class GhLineWarn implements java.io.Serializable {
	private Integer id;
	private String uid;
	private Integer lineid;
	private String x;
	private String y;
	private String type;
	private Integer isread;
	private Integer accountId;
	private Integer speed;
	private Integer anagel;
	private Integer time;
	private String path;
	private String area;
	private Integer opId;
	@Column(name = "op_id")
	public Integer getOpId() {
		return opId;
	}

	public void setOpId(Integer opId) {
		this.opId = opId;
	}

	@Column(name = "area")
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Column(name = "account_id")
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "isread")
	public Integer getIsread() {
		return isread;
	}

	public void setIsread(Integer isread) {
		this.isread = isread;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "uid", length = 20)
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	@Column(name = "lineid")
	public Integer getLineid() {
		return lineid;
	}

	public void setLineid(Integer lineid) {
		this.lineid = lineid;
	}
	@Column(name = "x")
	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}
	@Column(name = "y")
	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}
	@Column(name = "speed")
	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	@Column(name = "anagel")
	public Integer getAnagel() {
		return anagel;
	}

	public void setAnagel(Integer anagel) {
		this.anagel = anagel;
	}
	
	@Column(name = "time")
	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}
	@Column(name = "path")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
