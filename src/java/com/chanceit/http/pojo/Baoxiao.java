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

/**
 * Baoxiao entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "baoxiao", catalog = "carmonitor")
public class Baoxiao implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer userId;
	private Integer teamId;
	private Integer carId;
	private Date time;
	private Date createTime;
	private Double glf;
	private Double jyf;
	private Double xcf;
	private Double wxf;
	private Double byf;
	private Double bxf;
	private Double lqf;
	private Double tcf;
	private Double njf;
	private Double qtf;
	private Integer status;
	private String info1;
	private String info2;

	// Constructors

	/** default constructor */
	public Baoxiao() {
	}

	/** minimal constructor */
	public Baoxiao(Integer userId, Integer teamId) {
		this.userId = userId;
		this.teamId = teamId;
	}

	/** full constructor */
	public Baoxiao(Integer userId, Integer teamId, Integer carId, Date time,
			Date createTime, Double glf, Double jyf, Double xcf,
			Double wxf, Double byf, Double bxf, Double lqf, Double tcf,
			Double njf, Double qtf, Integer status, String info1, String info2) {
		this.userId = userId;
		this.teamId = teamId;
		this.carId = carId;
		this.time = time;
		this.createTime = createTime;
		this.glf = glf;
		this.jyf = jyf;
		this.xcf = xcf;
		this.wxf = wxf;
		this.byf = byf;
		this.bxf = bxf;
		this.lqf = lqf;
		this.tcf = tcf;
		this.njf = njf;
		this.qtf = qtf;
		this.status = status;
		this.info1 = info1;
		this.info2 = info2;
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

	@Column(name = "user_id", nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "team_id")
	public Integer getTeamId() {
		return this.teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	@Column(name = "car_id")
	public Integer getCarId() {
		return this.carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time")
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "glf")
	public Double getGlf() {
		return this.glf;
	}

	public void setGlf(Double glf) {
		this.glf = glf;
	}

	@Column(name = "jyf")
	public Double getJyf() {
		return this.jyf;
	}

	public void setJyf(Double jyf) {
		this.jyf = jyf;
	}

	@Column(name = "xcf")
	public Double getXcf() {
		return this.xcf;
	}

	public void setXcf(Double xcf) {
		this.xcf = xcf;
	}

	@Column(name = "wxf")
	public Double getWxf() {
		return this.wxf;
	}

	public void setWxf(Double wxf) {
		this.wxf = wxf;
	}

	@Column(name = "byf")
	public Double getByf() {
		return this.byf;
	}

	public void setByf(Double byf) {
		this.byf = byf;
	}

	@Column(name = "bxf")
	public Double getBxf() {
		return this.bxf;
	}

	public void setBxf(Double bxf) {
		this.bxf = bxf;
	}

	@Column(name = "lqf")
	public Double getLqf() {
		return this.lqf;
	}

	public void setLqf(Double lqf) {
		this.lqf = lqf;
	}

	@Column(name = "tcf")
	public Double getTcf() {
		return this.tcf;
	}

	public void setTcf(Double tcf) {
		this.tcf = tcf;
	}

	@Column(name = "njf")
	public Double getNjf() {
		return this.njf;
	}

	public void setNjf(Double njf) {
		this.njf = njf;
	}

	@Column(name = "qtf")
	public Double getQtf() {
		return this.qtf;
	}

	public void setQtf(Double qtf) {
		this.qtf = qtf;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "info1")
	public String getInfo1() {
		return this.info1;
	}

	public void setInfo1(String info1) {
		this.info1 = info1;
	}

	@Column(name = "info2")
	public String getInfo2() {
		return this.info2;
	}

	public void setInfo2(String info2) {
		this.info2 = info2;
	}

}