package com.chanceit.http.pojo;


/**
 * add by zhangxin 2014-08-25
 * 前台显示用
 */
public class OpTeamVO implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = -568735069139383214L;
	
	private int operatorId;
	
	private String operatorName;
	
	private String teams;

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getTeams() {
		return teams;
	}

	public void setTeams(String teams) {
		this.teams = teams;
	}
}