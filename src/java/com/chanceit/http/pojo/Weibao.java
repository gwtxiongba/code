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
@Table(name = "weibao", catalog = "carmonitor")
public class Weibao implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer carId;
	private Date yearTime;
	private Date keepTime;
	private Date safeTime;

	// Constructors

	/** default constructor */
	public Weibao() {
	}

	/** minimal constructor */
	public Weibao(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public Weibao(Integer id, Integer carId, Date yearTime, Date keepTime,
			Date safeTime) {
		super();
		this.id = id;
		this.carId = carId;
		this.yearTime = yearTime;
		this.keepTime = keepTime;
		this.safeTime = safeTime;
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

	
	@Column(name = "car_id")
	public Integer getCarId() {
		return this.carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	
	@Temporal(TemporalType.DATE)
	@Column(name = "year_time")
	public Date getYearTime() {
		return yearTime;
	}

	public void setYearTime(Date yearTime) {
		this.yearTime = yearTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "keep_time")
	public Date getKeepTime() {
		return keepTime;
	}

	public void setKeepTime(Date keepTime) {
		this.keepTime = keepTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "safe_time")
	public Date getSafeTime() {
		return safeTime;
	}

	public void setSafeTime(Date safeTime) {
		this.safeTime = safeTime;
	}


}