package com.chanceit.http.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.http.pojo.ConfigInfo;
import com.chanceit.http.pojo.PcodeCensus;
import com.chanceit.http.pojo.PcodeDiary;
import com.chanceit.http.pojo.PcodeInfo;

/**
 * @author zhangxin
 * @description 
 */
@Component("pcodeCensusDao")
@Repository
public class PcodeCensusDaoImpl extends HibernateService implements IpcodeCensusDao {

	/**
	 * 保存统计信息
	 */
	@Override
	public void save(PcodeCensus pcodeSensus) {
		getSession().save(pcodeSensus);
	}
	
	/**
	 * 保存统计日志
	 */
	@Override
	public void saveDiary(PcodeDiary pcodeDiary) {
		getSession().save(pcodeDiary);
	}

	/**
	 * 获取数据库里最大日期
	 */
	@Override
	public Date getMaxDate() {
		Date date = (Date)getSession().createQuery("select max(p.codeDate) from  PcodeCensus p" ).uniqueResult();
		return date;
	}

	/**
	 * 获取月份分组信息
	 */
	@Override
	public List getPcodeDates() {
		String sql = "SELECT p.code_date FROM pcode_census p GROUP BY DATE_FORMAT(p.code_date, 'y%-%m') ORDER BY p.code_date desc";
		return getSession().createSQLQuery(sql).list();
	}

	/**
	 * 得到统计列表
	 */
	@Override
	public List getCensusList(String date) {
		String sql = "SELECT p.pcode as pcode, sum(p.code_count) as count FROM pcode_census p GROUP BY p.pcode";
		return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	/**
	 * 通过pcode获取相关故障码说明
	 */
	@Override
	public PcodeInfo getPcodeInfo(String pcode) {
		String sql = "from PcodeInfo p where p.pcode = ?";
		List list = find(sql, new Object[]{pcode});
		if(list.size() != 1){
			return null;
		}else{
			return (PcodeInfo)list.get(0);
		}
	}

	/**
	 * 通过识别码和日期获取检测日志信息
	 * @throws Exception 
	 */
	@Override
	public List getPcodeDiary(String identifiers, String date) throws Exception {
		//String hql = "from PcodeDiary p where p.car.vehicleId in (" + identifiers+ ") and codeDate ='2014-09-03'";
		String hql = "from PcodeDiary p where p.car.identifier in (" + identifiers+ ") and p.codeDate ='" + date + "'";
		Query query = super.getSession().createQuery(hql);
		return query.list();
	
	}
	
	/**
	 * 通过识别码和日期获取检测日志信息
	 * @throws Exception 
	 */
	@Override
	public List getBeforeRecordIds(String identifiers, String date){
		//String hql = "from PcodeDiary p where p.car.vehicleId in (" + identifiers+ ") and codeDate ='2014-09-03'";
		String hql = "select p.diaryId from PcodeDiary p where p.car.identifier in (" + identifiers+ ") and p.codeDate ='" + date + "'";
		Query query = super.getSession().createQuery(hql);
		return query.list();
	
	}

	/**
	 * 通过识别码和日期删除检测日志信息
	 */
	@Override
	public void deleteBeforeRecord(List ids) {
		String hql = "delete from PcodeDiary p where p.diaryId in (:ids)";
		getSession().createQuery(hql).setParameterList("ids",ids).executeUpdate();
	}

	/**
	 * 获取配置信息
	 */
	@Override
	public ConfigInfo getConfigInfo(Integer accountId) {
		String hql = "from ConfigInfo c where c.account.accountId = :accountId";
		List list = getSession().createQuery(hql).setParameter("accountId",accountId).list();
		if(list.size() != 1){
			return null;
		}else{
			return (ConfigInfo)list.get(0);
		}
	}

	/**
	 * 插入配置信息
	 */
	@Override
	public void saveConfigInfo(ConfigInfo configInfo) {
		getSession().save(configInfo);
	}
	
}
