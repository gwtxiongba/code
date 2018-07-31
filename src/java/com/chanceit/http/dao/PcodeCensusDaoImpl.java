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
	 * ����ͳ����Ϣ
	 */
	@Override
	public void save(PcodeCensus pcodeSensus) {
		getSession().save(pcodeSensus);
	}
	
	/**
	 * ����ͳ����־
	 */
	@Override
	public void saveDiary(PcodeDiary pcodeDiary) {
		getSession().save(pcodeDiary);
	}

	/**
	 * ��ȡ���ݿ����������
	 */
	@Override
	public Date getMaxDate() {
		Date date = (Date)getSession().createQuery("select max(p.codeDate) from  PcodeCensus p" ).uniqueResult();
		return date;
	}

	/**
	 * ��ȡ�·ݷ�����Ϣ
	 */
	@Override
	public List getPcodeDates() {
		String sql = "SELECT p.code_date FROM pcode_census p GROUP BY DATE_FORMAT(p.code_date, 'y%-%m') ORDER BY p.code_date desc";
		return getSession().createSQLQuery(sql).list();
	}

	/**
	 * �õ�ͳ���б�
	 */
	@Override
	public List getCensusList(String date) {
		String sql = "SELECT p.pcode as pcode, sum(p.code_count) as count FROM pcode_census p GROUP BY p.pcode";
		return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	/**
	 * ͨ��pcode��ȡ��ع�����˵��
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
	 * ͨ��ʶ��������ڻ�ȡ�����־��Ϣ
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
	 * ͨ��ʶ��������ڻ�ȡ�����־��Ϣ
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
	 * ͨ��ʶ���������ɾ�������־��Ϣ
	 */
	@Override
	public void deleteBeforeRecord(List ids) {
		String hql = "delete from PcodeDiary p where p.diaryId in (:ids)";
		getSession().createQuery(hql).setParameterList("ids",ids).executeUpdate();
	}

	/**
	 * ��ȡ������Ϣ
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
	 * ����������Ϣ
	 */
	@Override
	public void saveConfigInfo(ConfigInfo configInfo) {
		getSession().save(configInfo);
	}
	
}
