package com.chanceit.http.pojo;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;
import com.google.gson.annotations.Expose;

/**
 * Message entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "prestore_msg")
public class PreStoreMessage implements java.io.Serializable {

	// Fields
	@Expose
	private Integer preMsgId;
	@Expose
	private String preMsgContent;
	@Expose
	private Integer accountId;

	/** default constructor */
	public PreStoreMessage() {
	}

	/** full constructor */
	public PreStoreMessage(Integer preMsgId, String preMsgContent, 
			Integer accountId) {
		this.preMsgId = preMsgId;
		this.preMsgContent = preMsgContent;
		this.accountId = accountId;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "pre_msg_id")
	public Integer getPreMsgId() {
		return preMsgId;
	}

	public void setPreMsgId(Integer preMsgId) {
		this.preMsgId = preMsgId;
	}

	@Column(name = "pre_msg_content")
	public String getPreMsgContent() {
		return preMsgContent;
	}

	public void setPreMsgContent(String preMsgContent) {
		this.preMsgContent = preMsgContent;
	}

	@Column(name = "account_id")
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

}