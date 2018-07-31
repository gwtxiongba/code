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
@Table(name = "card_info")
public class Card implements java.io.Serializable {

	@Expose
	private Integer cardId;
	@Expose
	private String cardNo;
	@Expose
	private String cardKey;
	@Expose
	private String cardInfo;

	/** default constructor */
	public Card() {
	}

	/** full constructor */
	public Card(Integer cardId, String cardNo, String cardKey, String cardInfo) {
		this.cardId = cardId;
		this.cardNo = cardNo;
		this.cardKey = cardKey;
		this.cardInfo = cardInfo;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "card_id")
	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}
	
	@Column(name = "card_no")
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	@Column(name = "card_key")
	public String getCardKey() {
		return cardKey;
	}

	public void setCardKey(String cardKey) {
		this.cardKey = cardKey;
	}

	@Column(name = "card_info")
	public String getCardInfo() {
		return cardInfo;
	}

	public void setCardInfo(String cardInfo) {
		this.cardInfo = cardInfo;
	}

}