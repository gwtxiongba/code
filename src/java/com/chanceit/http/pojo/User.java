package com.chanceit.http.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

/**
 * User entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user")
public class User implements java.io.Serializable {

	// Fields

	@Expose
	private Integer vehicleId;
	private Account account;
	@Expose
	private String identifier;
	private String userName;
	@Expose
	private String plate;
	@Expose
	private String tel;
	@Expose
	private String carModel;
	private Date createTime;
	private Short online;
	private String x;
	private String y;
	private Date delTime;
	private Short ifDel;
	private Date upTime;
	@Expose
	private Team team;
	@Expose
	private Integer type=0;
	@Expose
	private Driver driver;
	@Expose
	private Short ifMonitor;
	@Expose
	private Integer baseId;
	@Expose
	private Short ifRelay;
	@Expose
	private String cutpwd;
	@Expose
	private Short ifOff;
	// Constructors
	@Expose
	private String discc;   //排量 油耗相关 传到服务器  add by gwt
	@Expose
    private Double startPrice;
	@Expose
    private Double kmPrice;
	@Expose
    private Double lowPrice;
    @Expose
    private Date regTime;
    @Expose
    private Date buyTime;
    @Expose
    private Integer userNumber;
    @Expose
    private String brand;
    @Expose
    private String iccid;
    @Expose
    private String lineGps;
    @Expose
    private Integer level;
    
    @Column(name = "line_gps")
    public String getLineGps() {
		return lineGps;
	}

	public void setLineGps(String lineGps) {
		this.lineGps = lineGps;
	}
	@Column(name = "level")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Column(name = "iccid")
    public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	@Column(name = "brand")
    public String getBrand() {
		return brand;
	}

	public void setBrand(String brand_id) {
		this.brand = brand_id;
	}

	@Column(name = "start_price")
	public Double getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(Double startPrice) {
		this.startPrice = startPrice;
	}
	@Column(name = "km_price")
	public Double getKmPrice() {
		return kmPrice;
	}

	public void setKmPrice(Double kmPrice) {
		this.kmPrice = kmPrice;
	}
	@Column(name = "low_price")
	public Double getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(Double lowPrice) {
		this.lowPrice = lowPrice;
	}
	
	@Column(name = "reg_time")
	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "buy_time")
	public Date getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}
	@Column(name = "user_number")
	public Integer getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(Integer userNumber) {
		this.userNumber = userNumber;
	}

	@Column(name = "if_monitor")
	public Short getIfMonitor() {
		return ifMonitor;
	}

	public void setIfMonitor(Short ifMonitor) {
		this.ifMonitor = ifMonitor;
	}

	@Column(name = "car_type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(Integer userId) {
		this.vehicleId = userId;
	}

	/** full constructor */
	
	public User(Integer vehicleId, String identifier, String userName,
			String plate, String tel, Date createTime, Short ifRelay,
			String cutpwd) {
		super();
		this.vehicleId = vehicleId;
		this.identifier = identifier;
		this.userName = userName;
		this.plate = plate;
		this.tel = tel;
		this.createTime = createTime;
		this.ifRelay = ifRelay;
		this.cutpwd = cutpwd;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_id", unique = true, nullable = false)
	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	@Column(name = "identifier", length = 100)
	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Column(name = "user_name", length = 50)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "plate", length = 20)
	public String getPlate() {
		return this.plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	@Column(name = "tel", length = 20)
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "car_name", length = 100)
	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 0)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@Column(name = "online")
	public Short getOnline() {
		return online;
	}

	public void setOnline(Short online) {
		this.online = online;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	@Column(name = "x", length = 100)
	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}
	
	@Column(name = "y", length = 100)
	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "del_time", length = 0)
	public Date getDelTime() {
		return delTime;
	}

	public void setDelTime(Date delTime) {
		this.delTime = delTime;
	}
	
	@Column(name = "if_del")
	public Short getIfDel() {
		return ifDel;
	}

	public void setIfDel(Short ifDel) {
		this.ifDel = ifDel;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "up_time", length = 0)
	public Date getUpTime() {
		return upTime;
	}

	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "driver_id")
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	@Column(name = "base_id")
	public Integer getBaseId() {
		return baseId;
	}

	public void setBaseId(Integer baseId) {
		this.baseId = baseId;
	}
	@Column(name = "if_relay")
	public Short getIfRelay() {
		return ifRelay;
	}

	public void setIfRelay(Short ifRelay) {
		this.ifRelay = ifRelay;
	}
	@Column(name = "cut_pwd", length = 20)
	public String getCutpwd() {
		return cutpwd;
	}
	@Column(name = "if_off")
	public void setCutpwd(String cutpwd) {
		this.cutpwd = cutpwd;
	}

	public Short getIfOff() {
		return ifOff;
	}

	public void setIfOff(Short ifOff) {
		this.ifOff = ifOff;
	}
	@Column(name = "discc")
	public String getDiscc() {
		return discc;
	}

	public void setDiscc(String discc) {
		this.discc = discc;
	}
	
}