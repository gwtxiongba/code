/**
 * Copyright (c) 2011-2012 RoadRover Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Nov 19, 2011
 * Id: Page.java,v 1.0 Nov 19, 2011 4:05:43 PM Administrator
 */
package com.chanceit.framework.utils;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.chanceit.framework.enums.EnumCommon;
import com.google.gson.annotations.Expose;

/**
 * @ClassName Page
 * @author yehao
 * @date Nov 19, 2011 4:05:43 PM
 * @Description ִ�з�ҳ�����Ķ���
 */
public class Page {
	
	// ��������
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	//��ҳ����
	@Expose
	protected int pageNo = 1;
	@Expose
	protected int pageSize = 1;
	protected String orderBy = null;
	protected String order = null;
	protected boolean autoCount = true;
	@SuppressWarnings("unused")
	@Expose
	public int totalPages = 0;
	//���ؽ��
	//protected List<T> result = Collections.emptyList();
	//���ؽ�����Լ�ֵ�Ե���ʽ�洢
	@SuppressWarnings("unchecked")
	@Expose
	protected List result = Collections.emptyList();
	//���صĽ�����Զ���ķ�ʽ��װ
	@SuppressWarnings("unchecked")
	protected List resultObj = Collections.emptyList();
	
	@Expose
	protected long totalCount = 0;

	// ���캯��
	//Ĭ�ϵ�ʱ����10��

	public Page() {
		setPageSize(EnumCommon.PAGE_SIZE);
	}

	public Page(final int pageSize) {
		setPageSize(pageSize);
	}

	public Page(final int pageSize, final boolean autoCount) {
		setPageSize(pageSize);
		this.autoCount = autoCount;
	}

	//��ѯ��������

	/**
	 * ��õ�ǰҳ��ҳ��,��Ŵ�1��ʼ,Ĭ��Ϊ1.
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * ���õ�ǰҳ��ҳ��,��Ŵ�1��ʼ,����1ʱ�Զ�����Ϊ1.
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	/**
	 * ���ÿҳ�ļ�¼����,Ĭ��Ϊ1.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * ����ÿҳ�ļ�¼����,����1ʱ�Զ�����Ϊ1.
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;

		if (pageSize < 1) {
			this.pageSize = 1;
		}
	}

	/**
	* ����pageNo��pageSize���㵱ǰҳ��һ����¼���ܽ�����е�λ��,��Ŵ�1��ʼ.
	*/
	public int getFirst() {
		return ((pageNo - 1) * pageSize) + 1;
	}

	/**
	 * ��������ֶ�,��Ĭ��ֵ.��������ֶ�ʱ��','�ָ�.
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * ���������ֶ�,��������ֶ�ʱ��','�ָ�.
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * �Ƿ������������ֶ�,��Ĭ��ֵ.
	 */
	public boolean isOrderBySetted() {
		return StringUtils.isNotBlank(orderBy);
	}

	/**
	 * ���������.
	 * 
	 * @param order ��ѡֵΪdesc��asc,��������ֶ�ʱ��','�ָ�.
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * ��������ʽ��.
	 * 
	 * @param order ��ѡֵΪdesc��asc,��������ֶ�ʱ��','�ָ�.
	 */
	public void setOrder(final String order) {
		//���order�ַ����ĺϷ�ֵ
		String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr))
				throw new IllegalArgumentException("������" + orderStr + "���ǺϷ�ֵ");
		}
		this.order = StringUtils.lowerCase(order);
	}

	/**
	 * ��ѯ����ʱ�Ƿ��Զ�����ִ��count��ѯ��ȡ�ܼ�¼��,Ĭ��Ϊfalse.
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * ��ѯ����ʱ�Ƿ��Զ�����ִ��count��ѯ��ȡ�ܼ�¼��.
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	// ��ѯ�������

	/**
	 * ȡ���ܼ�¼��,Ĭ��ֵΪ-1.
	 */
	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * ����pageSize��totalCount������ҳ��,Ĭ��ֵΪ0.
	 */
	public long getTotalPages() {
		if (totalCount <= 0)
			return 1;

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	/**
	 * �Ƿ�����һҳ.
	 */
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	/**
	 * ȡ����ҳ��ҳ��,��Ŵ�1��ʼ.
	 * ��ǰҳΪβҳʱ�Է���βҳ���.
	 */
	public int getNextPage() {
		if (isHasNext())
			return pageNo + 1;
		else
			return pageNo;
	}

	/**
	 * �Ƿ�����һҳ.
	 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * ȡ����ҳ��ҳ��,��Ŵ�1��ʼ.
	 * ��ǰҳΪ��ҳʱ������ҳ���.
	 */
	public int getPrePage() {
		if (isHasPre())
			return pageNo - 1;
		else
			return pageNo;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * ȡ��ҳ�ڵļ�¼�б�.
	 */
	@SuppressWarnings("unchecked")
	public List getResult() {
		return result;
	}

	/**
	 * ���ҳ��ļ�¼�б�.
	 */
	@SuppressWarnings("unchecked")
	public void setResult(List result) {
		this.result = result;
	}

	/**
	 * @return the resultObj
	 */
	@SuppressWarnings("unchecked")
	public List getResultObj() {
		return resultObj;
	}

	/**
	 * @param resultObj the resultObj to set
	 */
	@SuppressWarnings("unchecked")
	public void setResultObj(List resultObj) {
		this.resultObj = resultObj;
	}
	

	
}
