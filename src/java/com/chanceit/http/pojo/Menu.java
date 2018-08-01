package com.chanceit.http.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

/**
 * Level entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "menu")
public class Menu implements java.io.Serializable {

	// Fields
	@Expose
	private Integer menuId;
	@Expose
	private String menuName;
	private String url;
	private Date createTime;
	@Expose
	private String madeAdmin;
	private Integer parentId;
	
	
	@Column(name = "menu_name")
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	@Column(name = "made_admin")
	public String getMadeAdmin() {
		return madeAdmin;
	}

	public void setMadeAdmin(String madeAdmin) {
		this.madeAdmin = madeAdmin;
	}
	@Column(name = "parent_id")
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 0)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "menu_id", unique = true, nullable = false)
	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	@Column(name = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	

}