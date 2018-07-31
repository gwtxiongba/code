package com.chanceit.http.pojo;
// default package

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
 * Message entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "pcode_census")
public class PcodeCensus implements java.io.Serializable {

	@Expose
	private Integer pcodeId;
	@Expose
	private Integer identifier;
	@Expose
	private String pcode;
	@Expose
	private int codeCount;
	@Expose
	private Date codeDate;

	/** default constructor */
	public PcodeCensus() {
	}

	/** full constructor */
	public PcodeCensus(Integer pcodeId, Integer identifier, String pcode, 
			int codeCount, Date codeDate) {
		this.pcodeId = pcodeId;
		this.identifier = identifier;
		this.pcode = pcode;
		this.codeCount = codeCount;
		this.codeDate = codeDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "card_id")
	public Integer getPcodeId() {
		return pcodeId;
	}

	public void setPcodeId(Integer pcodeId) {
		this.pcodeId = pcodeId;
	}

	@Column(name = "identifier")
	public Integer getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	@Column(name = "pcode")
	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	@Column(name = "code_count")
	public int getCodeCount() {
		return codeCount;
	}

	public void setCodeCount(int codeCount) {
		this.codeCount = codeCount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "code_date", length = 0)
	public Date getCodeDate() {
		return codeDate;
	}

	public void setCodeDate(Date codeDate) {
		this.codeDate = codeDate;
	}
	
	@Override
	public String toString(){
		return "pcode-->" + pcode + "codeDate-->" + codeDate + "codeCount-->" + codeCount + "identifier-->" + identifier;
	}

}