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
@Table(name = "brand", catalog = "carmonitor")
public class Brand implements java.io.Serializable {

	// Fields
	@Expose
	private Integer id;
	@Expose
	private String name;
	private String icon;

	// Constructors

	/** default constructor */
	public Brand() {
	}

	/** minimal constructor */
	public Brand(Integer id, String name) {
		this.id = id;
		this.name = name;
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

	
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Column(name = "icon")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}