package com.chanceit.http.pojo;
// default package

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "message")
public class Message implements java.io.Serializable {

	// Fields
	@Expose
	private Integer messageId;
	private String messageTitle;
	private String messageImage;
	@Expose
	private String messageContent;
	@Expose
	private Account company;
	@Expose
	private Date createTime;
	@Expose
	private Short messageType;
	private String thumbnailImage;
	@Expose
	private String creator;
	@Expose
	private String recipient;
	
	private String recipientDescription;
	private Integer sendNum;
	@Expose
	private String messageSummary;
	@Expose
	private String type;
	private Integer serverId;
	
	//add by zhangxin 2014-6-30
	@Expose
	private User car;
	@Expose
	private Integer ifread;
	@Expose
	private Integer sendFlag;
	//end

	// Constructors
	@Column(name = "send_num")
	public Integer getSendNum() {
		return sendNum;
	}

	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}
	
	@Column(name = "server_id")
	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	/** default constructor */
	public Message() {
	}

	/** full constructor */
	public Message(String messageTitle, String messageImage,
			String messageContent, Account company, Date createTime,
			Short messageType, String thumbnailImage, String creator,
			Team team, Integer ifread, Integer sendFlag) {
		this.messageTitle = messageTitle;
		this.messageImage = messageImage;
		this.messageContent = messageContent;
		this.company = company;
		this.createTime = createTime;
		this.messageType = messageType;
		this.thumbnailImage = thumbnailImage;
		this.creator = creator;
		this.ifread = ifread;
		this.sendFlag = sendFlag;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "message_id", unique = true, nullable = false)
	public Integer getMessageId() {
		return this.messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	@Column(name = "message_title", length = 100)
	public String getMessageTitle() {
		return this.messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	@Column(name = "message_image", length = 200)
	public String getMessageImage() {
		return this.messageImage;
	}

	public void setMessageImage(String messageImage) {
		this.messageImage = messageImage;
	}

	@Column(name = "message_content", length = 65535)
	public String getMessageContent() {
		return this.messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	public Account getCompany() {
		return this.company;
	}

	public void setCompany(Account company) {
		this.company = company;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 0)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "message_type")
	public Short getMessageType() {
		return this.messageType;
	}

	public void setMessageType(Short messageType) {
		this.messageType = messageType;
	}

	@Column(name = "thumbnail_image", length = 50)
	public String getThumbnailImage() {
		return this.thumbnailImage;
	}

	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}

	@Column(name = "creator", length = 20)
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	@Column(name = "recipient", length = 65535)
	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	@Column(name = "recipient_description", length = 200)
	public String getRecipientDescription() {
		return recipientDescription;
	}

	public void setRecipientDescription(String recipientDescription) {
		this.recipientDescription = recipientDescription;
	}

	@Column(name = "message_summary", length = 200)
	public String getMessageSummary() {
		return messageSummary;
	}

	public void setMessageSummary(String messageSummary) {
		this.messageSummary = messageSummary;
	}
	
	@Column(name = "type", length = 200)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "car_id")
	public User getCar() {
		return car;
	}

	public void setCar(User car) {
		this.car = car;
	}

	@Column(name = "ifread")
	public void setIfread(Integer ifread) {
		this.ifread = ifread;
	}
	
	public void setSendFlag(Integer sendFlag) {
		this.sendFlag = sendFlag;
	}

	@Column(name = "send_flag")
	public Integer getSendFlag() {
		return sendFlag;
	}

	public Integer getIfread() {
		return ifread;
	}
	

}