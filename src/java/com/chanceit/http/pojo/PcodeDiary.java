package com.chanceit.http.pojo;
// default package

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

/**
 * Message entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "pcode_diary")
public class PcodeDiary implements java.io.Serializable {

	@Expose
	private Integer diaryId;
	@Expose
	private User car;
	@Expose
	private String pcode;
	@Expose
	private String codeTitle;
	@Expose
	private String codeInfo;
	@Expose
	private Date codeDate;

	/** default constructor */
	public PcodeDiary() {
	}

	/** full constructor */
	public PcodeDiary(Integer diaryId, User car, String pcode, String codeTitle,
			String codeInfo, Date codeDate) {
		this.diaryId = diaryId;
		this.car = car;
		this.pcode = pcode;
		this.codeTitle = codeTitle;
		this.codeInfo = codeInfo;
		this.codeDate = codeDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "diary_id")
	public Integer getDiaryId() {
		return diaryId;
	}

	public void setDiaryId(Integer diaryId) {
		this.diaryId = diaryId;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vehicle_id")
	public User getCar() {
		return car;
	}

	public void setCar(User car) {
		this.car = car;
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

	@Column(name = "code_info")
	public String getCodeInfo() {
		return codeInfo;
	}

	public void setCodeInfo(String codeInfo) {
		this.codeInfo = codeInfo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "code_date", length = 0)
	public Date getCodeDate() {
		return codeDate;
	}

	public void setCodeDate(Date codeDate) {
		this.codeDate = codeDate;
	}

	
}