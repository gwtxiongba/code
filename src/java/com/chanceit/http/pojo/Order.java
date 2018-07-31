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

/**
 * Orders entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "orders", catalog = "carmonitor")
public class Order implements java.io.Serializable {

	// Fields
	@Expose
	private Integer id;
	private String orderNum;
	@Expose
	private Integer orderUserId;
	@Expose
	private String orderUserName;
	@Expose
	private String orderUserTel;
	@Expose
	private Integer carUserId;
	@Expose
	private String carUserName;
	@Expose
	private String carUserTel;
	@Expose
	private Integer status;  
	 /**  
	    0:待审批
	    1：管理员同意；
	    2：公司领导同意；
	    3：部门领导同意；
	    4：已派车；
	    5：已发车；
	    6：乘客已上车；
	    7：已完成
	    -1：管理员拒绝；
	    -2：公司领导拒绝；
	    -3：部门领导拒绝；
	    -4：车队长拒绝；
	    -5：已取消
	*/  
	@Expose
	private Date beginTime;
	@Expose
	private Date endTime;
	@Expose
	private String beginAddr;
	@Expose
	private String endAddr;
	@Expose
	private Double beginLat;
	@Expose
	private Double endLat;
	@Expose
	private Double beginLng;
	@Expose
	private Double endLng;
	@Expose
	private Date startTime;
	@Expose
	private Date overTime;
	@Expose
	private Double miles;
	@Expose
	private Double glf;
	@Expose
	private Double cost;
	@Expose
	private Integer carId;
	@Expose
	private Integer driverId;
	@Expose
	private String reason;
	@Expose
	private String remark;
	@Expose
	private Integer cause;
	@Expose
	private Integer carStyleId;
	@Expose
	private Integer pnumber;
	@Expose
	private String takes;
	@Expose
	private Date createTime;
	@Expose
	private Integer score;
	@Expose
	private String feedback;
	private Integer brandId;
	@Column(name = "takes")
	public String getTakes() {
		return takes;
	}

	public void setTakes(String takes) {
		this.takes = takes;
	}

	// Constructors
	@Column(name = "cause")
	public Integer getCause() {
		return cause;
	}

	public void setCause(Integer cause) {
		this.cause = cause;
	}
	@Column(name = "carstyle_id")
	public Integer getCarStyleId() {
		return carStyleId;
	}

	public void setCarStyleId(Integer carStyleId) {
		this.carStyleId = carStyleId;
	}
	@Column(name = "pnumber")
	public Integer getPnumber() {
		return pnumber;
	}

	public void setPnumber(Integer pnumber) {
		this.pnumber = pnumber;
	}

	/** default constructor */
	public Order() {
	}

	/** minimal constructor */
	public Order(Integer id, String orderNum, Integer status, Date beginTime,
			Date endTime) {
		this.id = id;
		this.orderNum = orderNum;
		this.status = status;
		this.beginTime = beginTime;
		this.endTime = endTime;
	}

	/** full constructor */
	public Order(Integer id, String orderNum, Integer orderUserId,
			String orderUserName, String orderUserTel, Integer carUserId,
			String carUserName, String carUserTel, Integer status,
			Date beginTime, Date endTime, String beginAddr, String endAddr,
			Double beginLat, Double endLat, Double beginLng, Double endLng,
			Date startTime, Date overTime, Double miles, Double glf,
			Double cost, Integer carId, Integer driverId, String reason,
			String remark) {
		this.id = id;
		this.orderNum = orderNum;
		this.orderUserId = orderUserId;
		this.orderUserName = orderUserName;
		this.orderUserTel = orderUserTel;
		this.carUserId = carUserId;
		this.carUserName = carUserName;
		this.carUserTel = carUserTel;
		this.status = status;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.beginAddr = beginAddr;
		this.endAddr = endAddr;
		this.beginLat = beginLat;
		this.endLat = endLat;
		this.beginLng = beginLng;
		this.endLng = endLng;
		this.startTime = startTime;
		this.overTime = overTime;
		this.miles = miles;
		this.glf = glf;
		this.cost = cost;
		this.carId = carId;
		this.driverId = driverId;
		this.reason = reason;
		this.remark = remark;
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

	@Column(name = "order_num")
	public String getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	@Column(name = "order_user_id")
	public Integer getOrderUserId() {
		return this.orderUserId;
	}

	public void setOrderUserId(Integer orderUserId) {
		this.orderUserId = orderUserId;
	}

	@Column(name = "order_user_name")
	public String getOrderUserName() {
		return this.orderUserName;
	}

	public void setOrderUserName(String orderUserName) {
		this.orderUserName = orderUserName;
	}

	@Column(name = "order_user_tel")
	public String getOrderUserTel() {
		return this.orderUserTel;
	}

	public void setOrderUserTel(String orderUserTel) {
		this.orderUserTel = orderUserTel;
	}

	@Column(name = "car_user_id")
	public Integer getCarUserId() {
		return this.carUserId;
	}

	public void setCarUserId(Integer carUserId) {
		this.carUserId = carUserId;
	}

	@Column(name = "car_user_name")
	public String getCarUserName() {
		return this.carUserName;
	}

	public void setCarUserName(String carUserName) {
		this.carUserName = carUserName;
	}

	@Column(name = "car_user_tel")
	public String getCarUserTel() {
		return this.carUserTel;
	}

	public void setCarUserTel(String carUserTel) {
		this.carUserTel = carUserTel;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "begin_time")
	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time")
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "begin_addr")
	public String getBeginAddr() {
		return this.beginAddr;
	}

	public void setBeginAddr(String beginAddr) {
		this.beginAddr = beginAddr;
	}

	@Column(name = "end_addr")
	public String getEndAddr() {
		return this.endAddr;
	}

	public void setEndAddr(String endAddr) {
		this.endAddr = endAddr;
	}

	@Column(name = "begin_lat")
	public Double getBeginLat() {
		return this.beginLat;
	}

	public void setBeginLat(Double beginLat) {
		this.beginLat = beginLat;
	}

	@Column(name = "end_lat")
	public Double getEndLat() {
		return this.endLat;
	}

	public void setEndLat(Double endLat) {
		this.endLat = endLat;
	}

	@Column(name = "begin_lng")
	public Double getBeginLng() {
		return this.beginLng;
	}

	public void setBeginLng(Double beginLng) {
		this.beginLng = beginLng;
	}

	@Column(name = "end_lng")
	public Double getEndLng() {
		return this.endLng;
	}

	public void setEndLng(Double endLng) {
		this.endLng = endLng;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time")
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "over_time")
	public Date getOverTime() {
		return this.overTime;
	}

	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}

	@Column(name = "miles")
	public Double getMiles() {
		return this.miles;
	}

	public void setMiles(Double miles) {
		this.miles = miles;
	}

	@Column(name = "glf")
	public Double getGlf() {
		return this.glf;
	}

	public void setGlf(Double glf) {
		this.glf = glf;
	}

	@Column(name = "cost")
	public Double getCost() {
		return this.cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Column(name = "car_id")
	public Integer getCarId() {
		return this.carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	@Column(name = "driver_id")
	public Integer getDriverId() {
		return this.driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	@Column(name = "reason")
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "score")
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	@Column(name = "feedback")
	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	@Column(name = "brand_id")
	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

}