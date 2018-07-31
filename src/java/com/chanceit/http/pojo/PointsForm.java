package com.chanceit.http.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "status_log")
public class PointsForm implements java.io.Serializable {
	private Integer id;
	@Expose
	private Integer uid;
	@Expose
	private Integer time1;
	@Expose
	private Integer time2;
	@Expose
	private String x;
	@Expose
	private String y;
	@Expose
	private Integer maxSpeed;
	@Expose
	private Integer minSpeed;
	@Expose
	private Integer miles;
	private Integer flag;

	@Column(name = "flag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "uid")
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	@Column(name = "time1")
	public Integer getTime1() {
		return time1;
	}

	public void setTime1(Integer time1) {
		this.time1 = time1;
	}

	@Column(name = "time2")
	public Integer getTime2() {
		return time2;
	}

	public void setTime2(Integer time2) {
		this.time2 = time2;
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

	@Column(name = "max_speed")
	public Integer getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(Integer maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Column(name = "min_speed")
	public Integer getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(Integer minSpeed) {
		this.minSpeed = minSpeed;
	}

	@Column(name = "miles")
	public Integer getMiles() {
		return miles;
	}

	public void setMiles(Integer miles) {
		this.miles = miles;
	}
}
