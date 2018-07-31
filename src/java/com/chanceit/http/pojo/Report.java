package com.chanceit.http.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="report")
public class Report implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String identifier;//设备码
	private String plate;//车牌
	private String driver;//司机
	private String group;//所属分组
	private Float speed;//速度
	private String x;
	private String y;
	private Date date;//时间
	private int azimuth;//方位角
	
	public Report(){
		
	}
	public Report(Integer id) {
		this.id = id;
	}
	public Report(Integer id, String identifier, String plate, String driver,
			String group, Float speed, String x, String y, Date date,
			int azimuth) {
		this.id = id;
		this.identifier = identifier;
		this.plate = plate;
		this.driver = driver;
		this.group = group;
		this.speed = speed;
		this.x = x;
		this.y = y;
		this.date = date;
		this.azimuth = azimuth;
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
	
	@Column(name="identifier",length=50)
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	@Column(name="plate",length=50)
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	
	@Column(name="driver",length=50)
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	@Column(name="group_name",length=50)
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	@Column(name="speed",length=20)
	public Float getSpeed() {
		return speed;
	}
	public void setSpeed(Float speed) {
		this.speed = speed;
	}
	@Column(name="x",length=50)
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	@Column(name="y",length=50)
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="pos_time",nullable=false)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Column(name="azimuth")
	public int getAzimuth() {
		return azimuth;
	}
	public void setAzimuth(int azimuth) {
		this.azimuth = azimuth;
	}
	
}
