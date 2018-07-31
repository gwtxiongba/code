package com.chanceit.http.service;

import org.json.JSONException;

import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.ConfigInfo;


public interface IpcodeCensusService {
	public void start();
	public String getPcodeDates() throws JSONException;
	public String getCensusList(String date) throws JSONException;
	public String getDetectionInfo(String identifiers, Account sessionAccount) throws Exception;
	public String getDetectionDiary(String identifiers, String date) throws Exception;
	public ConfigInfo getConfigInfo(Integer accountId);
}