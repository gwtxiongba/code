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
@Table(name = "carstyle", catalog = "carmonitor")
public class Carstyle implements java.io.Serializable {

	// Fields
	@Expose
	private Integer id;
	@Expose
	private String name;

	// Constructors

	/** default constructor */
	public Carstyle() {
	}

	/** minimal constructor */
	public Carstyle(Integer id, String name) {
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

}