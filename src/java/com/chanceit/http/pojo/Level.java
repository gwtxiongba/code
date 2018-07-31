package com.chanceit.http.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
 * Level entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "level")
public class Level implements java.io.Serializable {

	// Fields
	@Expose
	private Integer levelId;
	@Expose
	private String levelName;
	@Expose
	private Integer limitNum;

	// Constructors

	/** default constructor */
	public Level() {
	}

	/** minimal constructor */
	public Level(Integer levelId) {
		this.levelId = levelId;
	}

	/** full constructor */
	public Level(Integer levelId, String levelName, Integer limitNum) {
		this.levelId = levelId;
		this.levelName = levelName;
		this.limitNum = limitNum;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "level_id", unique = true, nullable = false)
	public Integer getLevelId() {
		return this.levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	@Column(name = "level_name", length = 100)
	public String getLevelName() {
		return this.levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	@Column(name = "limit_num")
	public Integer getLimitNum() {
		return this.limitNum;
	}

	public void setLimitNum(Integer limitNum) {
		this.limitNum = limitNum;
	}

}