/**
 * Copyright (c) 2011-2012 RoadRover Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Nov 17, 2011
 * Id: HibernateService.java,v 1.0 Nov 17, 2011 10:38:56 AM yehao
 */
package com.chanceit.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.formula.functions.T;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @ClassName HibernateService
 * @author yehao
 * @date Nov 17, 2011 10:38:56 AM
 * @Description hibernate��ѯ������һЩ�򵥵ķ�װ��
 */
@Component("hibernateService")
public class HibernateService {
	
protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("sessionFactory")
	protected SessionFactory sessionFactory;
	@Autowired
	@Qualifier("converter")
	private Converter converter;
	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @return ͨ�÷���
	 * @Description ���session����
	 */
	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @param hql
	 * @param values �����ɱ�Ĳ���,��˳���.
	 * @return
	 * @Description ����hql�Ͳ�����˳��󶨲�����Query����
	 */
	public Query getQuery(String hql , Object... values){
		Assert.hasText(hql, "hql����Ϊ��");
		Query query = getSession().createQuery(hql);
		if(values != null){
			for (int i = 0; i < values.length; i++) {
				if(values[i] instanceof Date){
					query.setDate(i, (Date)values[i]);			
				} 
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}
	
	public Query getSQLQuery(String hql , Object... values){
		Assert.hasText(hql, "hql����Ϊ��");
		Query query = getSession().createSQLQuery(hql);
		if(values != null){
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}
	
	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @param hql hql���
	 * @Description ִ��HQL���
	 */
	public void excuteHQL(String hql , Object... values){
		Assert.hasText(hql, "hql����Ϊ��");
		getQuery(hql,values).executeUpdate();
	}
	
	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @param sql
	 * @Description ִ��SQL���
	 */
	public void excuteSQL(String sql){
		Assert.hasText(sql, "sql����Ϊ��");
		getSession().createSQLQuery(sql).executeUpdate();
	}

	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @param hql
	 * @return
	 * @Description ��ѯ����
	 */
	@SuppressWarnings("unchecked")
	public List find(String hql,Object... values){
		Assert.hasText(hql, "hql����Ϊ��");
		Query query = getQuery(hql,values);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List findSQL(String sql,Object... values){
		Assert.hasText(sql, "hql����Ϊ��");
		return getSQLQuery(sql,values).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	
	/**
	 * @author yehao
	 * @date Nov 19, 2011
	 * @param page
	 * @param hql
	 * @return
	 * @Description ��ù���list�ķ�ҳ����
	 */
	public Page findListPage(Page page , String hql ,Object... values){
		Assert.hasText(hql, "hql����Ϊ��");
		long totalCount = countHQLResult(hql,values);//�������
		page.setTotalCount(totalCount);
		page.setTotalPages((int)page.getTotalPages());//������ҳ����С
		Query query = getQuery(hql,values);
		query = setPageParameterToQuery(query, page);
		page.setResult(query.list());
		return page;
	}
	
	/**
	 * @author Administrator
	 * @date Nov 24, 2011
	 * @param page
	 * @param hql
	 * @return
	 * @Description ��ù��ڼ�ֵ�Եķ�ҳ����
	 */
	@SuppressWarnings("unchecked")
	public Page findMapPage(Page page , String hql ,Object... values){
		Assert.hasText(hql, "hql����Ϊ��");
		long totalCount = countHQLResult(hql,values);//�������
		page.setTotalCount(totalCount);
		page.setTotalPages((int)page.getTotalPages());//������ҳ����С
		Query query = getQuery(hql,values);
		query = setPageParameterToQuery(query, page);
		page.setResult(query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
		return page;
	}
	
	/**
	 * @author Administrator
	 * @date Jun 27, 2013
	 * @param page
	 * @param sql
	 * @param values
	 * @return
	 * @Description ��ù��ڼ�ֵ�Եķ�ҳ����,SQL��䷽��
	 */
	public Page findSQLMapPage(Page page , String sql ,Object... values){
		Assert.hasText(sql, "hql����Ϊ��");
		long totalCount = countSQLResult(sql,values);//�������
		page.setTotalCount(totalCount);
		page.setTotalPages((int)page.getTotalPages());//������ҳ����С
		Query query = getSQLQuery(sql,values);
		query = setPageParameterToQuery(query, page);
		page.setResult(query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
		return page;
	}
	
	/**
	 * @author dj
	 * @date Nov 24, 2011
	 * @param page
	 * @param hql
	 * @return
	 * @Description ��ù��ڼ�ֵ�Եķ�ҳ����,�ܼ�¼��ҳ�������жϣ������������
	 */
	@SuppressWarnings("unchecked")
	public Page findSQLMapPage2(Page page , String sql ,Object... values){
		Assert.hasText(sql, "hql����Ϊ��");
		Query query = getSQLQuery(sql,values);
		query = setPageParameterToQuery(query, page);
		page.setResult(query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
		return page;
	}
	
	@SuppressWarnings("unchecked")
	public Page findSQLMapPage4(Page page , String sql ,Object... values){
		Assert.hasText(sql, "hql����Ϊ��");
		Query query = getSQLQuery(sql,values);
		query = setPageParameterToQuery(query, page);
		List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		page.setResult(list);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	public Page findSQLMapPage3(Page page , String sql ,Object... values){
		Assert.hasText(sql, "hql����Ϊ��");
		long totalCount = countSQLResult(sql,values);//�������
		page.setTotalCount(totalCount);
		page.setTotalPages((int)page.getTotalPages());//������ҳ����С
		Query query = getSQLQuery(sql,values);
		query = setPageParameterToQuery(query, page);
		List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		List res_list = new ArrayList();
		for(int i=0;i<list.size();i++){
			Map map_ = new HashedMap();
			Map map = (Map) list.get(i);
			map_.put("limitId", map.get("id"));
			map_.put("plate", map.get("plate"));
			map_.put("identifier", map.get("uid"));
			//map_.put("path", map.get("path"));
		   if(Integer.parseInt(map.get("isread").toString()) == 0){
			   map_.put("ifRead", false); 
		   }else{
			   map_.put("ifRead", true); 
		   }
			List listjson = new ArrayList();
			listjson.add(map.get("x"));
			listjson.add(map.get("y"));
			try {
				listjson.add(getTimes(Long.parseLong(map.get("time").toString())));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				listjson.add("");
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				listjson.add("");
			}
			listjson.add(map.get("speed"));
			listjson.add(map.get("anagel"));
			map_.put("gps", listjson);
			JSONArray aar = new JSONArray();
			String s = "";
			String ss = "";
			if( map.get("path") != null){
				 s = map.get("path").toString();
			if(s.contains("[") && s.contains("]")  && s.contains(",")){
			int beg = s.indexOf("[");
			int end = s.indexOf("]");
			s=s.substring(beg+2,end-1);
			aar.add(s);
			}
			}
			JSONArray aar1 = new JSONArray();
			if( map.get("area") != null){
				 ss = map.get("area").toString();
			if(ss.contains("[") && ss.contains("]")  && ss.contains(",")){
			int beg = ss.indexOf("[");
			int end = ss.indexOf("]");
			ss=ss.substring(beg+2,end-1);
			aar1.add(ss);
			}
			}
//			if("circle".equals(map.get("type"))){
//				map_.put("paths", new JSONArray());
//				map_.put("areas", aar);
//			}else if("polygon".equals(map.get("type"))){
//				map_.put("paths", new JSONArray());
//				map_.put("areas", aar);
//			}else{
				map_.put("paths", aar);
				map_.put("areas", aar1);
//			}
//			if( map.get("path") != null){
//				 s = map.get("path").toString();
//			if(s.contains("[") && s.contains("]")  && s.contains(",")){
//			int beg = s.indexOf("[");
//			int end = s.indexOf("]");
//			s=s.substring(beg+2,end-1);
//			aar.add(s);
//			}
//			}
//			map_.put("paths", aar);
			//System.out.println(map.get("path"));
			res_list.add(map_);
		}
		page.setResult(res_list);
		return page;
	}
	
	/**
	 * @author yehao
	 * @date Nov 19, 2011
	 * @param query
	 * @param page
	 * @return
	 * @Description ���ò�ѯ�ķ�Χ�Ͳ���
	 */
	private Query setPageParameterToQuery(Query query , Page page){
		
		if(page.getPageSize() <= 0){
			throw new IllegalArgumentException();
		}
		query.setFirstResult(page.getFirst()-1);
		query.setMaxResults(page.getPageSize());
		return query;
	}
	
	/**
	 * @author yehao
	 * @date Nov 19, 2011
	 * @param hql
	 * @return
	 * @Description ��ò�ѯ���
	 */
	protected long countHQLResult(String hql ,Object... values){
		Long count = 0L;
		String fromHql = hql;
		//select�Ӿ���order by�Ӿ��Ӱ��count��ѯ,���м򵥵��ų�.
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from ");
		fromHql = StringUtils.substringBefore(fromHql, "order by");
		fromHql = StringUtils.substringBefore(fromHql, "group by");
		String countHql = "select count(*) " + fromHql;

		try {
			count = findUnique(countHql,values);
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
		return count;
	}
	
	/**
	 * @author Administrator
	 * @date Oct 23, 2013
	 * @param hql
	 * @param values
	 * @return
	 * @Description TODO(������һ�仰�����������������)
	 */
	protected long countSQLResult(String hql ,Object... values){
		Long count = 0L;
		String fromHql = hql;
		//select�Ӿ���order by�Ӿ��Ӱ��count��ѯ,���м򵥵��ų�.
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");

		String countHql = "select count(*) " + fromHql;

		try {
			count = Long.parseLong(findSQLUnique(countHql,values).toString());
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
		return count;
	}
	
	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @param hql
	 * @return
	 * @Description �Լ�ֵ�Եķ�ʽ���ؽ��HQL�Ĳ�ѯ��ʽ
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findHQLMap(String hql ,Object... values){
		return getQuery(hql,values).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	/**
	 * @author Administrator
	 * @date Nov 17, 2011
	 * @param sql
	 * @return
	 * @Description �Լ�ֵ�Եķ�ʽ���ؽ��SQL�Ĳ�ѯ��ʽ
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findSQLList(String sql){
		return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @param <T>
	 * @param hql
	 * @param values
	 * @return
	 * @Description ��ѯΨһ����
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T findUnique(String hql ,Object... values ){
		return (T) getQuery(hql, values).uniqueResult();
	}
	
	/**
	 * @author yehao
	 * @date Nov 17, 2011
	 * @param <T>
	 * @param hql
	 * @param values
	 * @return
	 * @Description ��ѯΨһ����
	 */
	@SuppressWarnings("unchecked")
	public Object findSQLUnique(String hql ,Object... values ){
		return getSQLQuery(hql, values).uniqueResult();
	}
	
	/**
	 * @author zhangxin
	 * @date 2014-7-1
	 * @param page
	 * @param sql
	 * @return
	 * @Description ��ù��ڼ�ֵ�Եķ�ҳ����,SQL��䷽��
	 */
	public Page findSQLPage(Page page , String sql){
		Assert.hasText(sql, "hql����Ϊ��");
		List resultList = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		page.setTotalCount(resultList.size());
		page.setTotalPages((int)page.getTotalPages());//������ҳ����С
		page.setResult(resultList);
		Query query = getSQLQuery(sql, new Object[0]);
		query = setPageParameterToQuery(query, page);
		page.setResult(query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
		return page;
	}
	
	/**
	 * @param sql
	 * @return
	 * ��sql��ѯ ��ӵ�ʵ����󼯺�
	 * 
	 */
	public List getSqlList(String sql,Class<?> ct){
	List l = getSession().createSQLQuery(sql).addEntity(ct).list();
	  return l;
	}
	
	/**
	 * @param sql
	 * @return
	 * ��sql��ѯ ��ӵ�ʵ����󼯺�
	 * 
	 */
	public List getSqlList(String sql){
	List l = getSession().createSQLQuery(sql).list();
	  return l;
	}
	private String getTimes(Long strtime) throws ParseException{
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(strtime*1000);
	    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    Date date = new Date(strtime*1000);
	    format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	    return format.format(date);
	}
}
