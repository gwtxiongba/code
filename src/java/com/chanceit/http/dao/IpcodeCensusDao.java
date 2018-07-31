package com.chanceit.http.dao;

import java.util.Date;
import java.util.List;

import com.chanceit.http.pojo.ConfigInfo;
import com.chanceit.http.pojo.PcodeCensus;
import com.chanceit.http.pojo.PcodeDiary;
import com.chanceit.http.pojo.PcodeInfo;



public interface IpcodeCensusDao {
	public void save(PcodeCensus pcodeCensus);
	public void saveDiary(PcodeDiary pcodeDiary);
	public Date getMaxDate();
	public List getPcodeDates();
	public List getCensusList(String date);
	public PcodeInfo getPcodeInfo(String pcode);
	public List getPcodeDiary(String identifiers, String date) throws Exception;
	public List getBeforeRecordIds(String identifiers, String date);
	public void deleteBeforeRecord(List ids);
	public ConfigInfo getConfigInfo(Integer accountId);
	public void saveConfigInfo(ConfigInfo configInfo);
}