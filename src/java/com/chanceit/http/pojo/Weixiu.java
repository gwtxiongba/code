package com.chanceit.http.pojo;
// default package

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
 * Driver entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "weixiu")
public class Weixiu implements java.io.Serializable {

	// Fields
	@Expose
	private Integer id;
	@Expose
	private Integer carId;//ά�޳���id
	@Expose
	private double fee;//ά�޷���
	@Expose
	private Date time;//ά��ʱ��
	@Expose
	private String reason;//ά��ԭ��
	@Expose
	private String name;//ά����
	@Expose
	private String tel;//�绰
	@Expose
	private Integer status;//����״̬
	@Expose
	private String reason2;//�ܾ�ԭ��
	
	

	/** default constructor */
	public Weixiu() {
	}

	/** minimal constructor */
	public Weixiu(Integer id) {
		this.id = id;
	}


	// Property accessors
		@Id
		@GeneratedValue(strategy = IDENTITY)
		@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "car_id", nullable = false)
	public Integer getCarId() {
		return carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "time")
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}


	@Column(name = "reason")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "status",nullable=false)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "fee")
	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "tel")
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "reason2")
	public String getReason2() {
		return reason2;
	}

	public void setReason2(String reason2) {
		this.reason2 = reason2;
	}
	
	
}