package com.chanceit.http.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
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

public class TeamTree implements java.io.Serializable {

	// Fields
	private Integer id;
	private String text;
	private Integer accountId;
	private Integer amount;
	private Integer parent_id;
	boolean leaf = false;
    private Integer uid;
    private String ifOff;
    private String cutpwd;
    private String relay;
    private String iconCls;
	private List<TeamTree> children = new ArrayList<TeamTree>();;
	public Integer getPid() {
		return parent_id;
	}

	public void setPid(Integer parent_id) {
		this.parent_id = parent_id;
	}

	// Constructors

	/** default constructor */
	public TeamTree() {
	}

	/** minimal constructor */
	public TeamTree(Integer id) {
		this.id = id;
	}


	// Property accessors


	public String getTeamName() {
		return this.text;
	}

	public void setTeamName(String text) {
		this.text = text;
	}


	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	

	public Integer getTeamId() {
		return id;
	}

	public void setTeamId(Integer id) {
		this.id = id;
	}
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public List<TeamTree> getTreeList() {
		return children;
	}

	public void setTreeList(List<TeamTree> children) {
		this.children = children;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getIfOff() {
		return ifOff;
	}

	public void setIfOff(String ifOff) {
		this.ifOff = ifOff;
	}

	public String getCutpwd() {
		return cutpwd;
	}

	public void setCutpwd(String cutpwd) {
		this.cutpwd = cutpwd;
	}

	public String getRelay() {
		return relay;
	}

	public void setRelay(String relay) {
		this.relay = relay;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

//	public boolean isChecked() {
//		return checked;
//	}
//
//	public void setChecked(boolean checked) {
//		this.checked = checked;
//	}

	

}