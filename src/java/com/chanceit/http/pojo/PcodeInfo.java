package com.chanceit.http.pojo;
// default package

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
 * Message entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "pcode_info")
public class PcodeInfo implements java.io.Serializable {

	@Expose
	private Integer id;
	@Expose
	private String pcode;
	@Expose
	private String codeTitle;
	@Expose
	private String description;
	@Expose
	private String brand;
	@Expose
	private Integer type;
	@Expose
	private String belowsys;

	/** default constructor */
	public PcodeInfo() {
	}

	/** full constructor */
	public PcodeInfo(Integer id, String pcode, String codeTitle, String description,
			 String brand, Integer type, String belowsys) {
		this.id = id;
		this.pcode = pcode;
		this.codeTitle = codeTitle;
		this.description = description;
		this.brand = brand;
		this.type = type;
		this.belowsys = belowsys;
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
	
	@Column(name = "pcode")
	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	@Column(name = "code_title")
	public String getCodeTitle() {
		return codeTitle;
	}

	public void setCodeTitle(String codeTitle) {
		this.codeTitle = codeTitle;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "brand")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Column(name = "type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "belowsys")
	public String getBelowsys() {
		return belowsys;
	}

	public void setBelowsys(String belowsys) {
		this.belowsys = belowsys;
	}

}