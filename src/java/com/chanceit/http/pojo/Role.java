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
@Table(name = "role")
public class Role implements java.io.Serializable {

	// Fields
	@Expose
	private Integer roleId;
	@Expose
	private String roleName;
	@Expose
	private String rights;

	// Constructors
	@Column(name = "rights")
	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	/** default constructor */
	public Role() {
	}

	/** minimal constructor */
	public Role(Integer roleId) {
		this.roleId = roleId;
	}

	/** full constructor */
	public Role(Integer roleId, String roleName, Integer limitNum) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "role_id", unique = true, nullable = false)
	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Column(name = "role_name", length = 100)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}




}