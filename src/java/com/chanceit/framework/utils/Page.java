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
 * @Description 执行分页方法的对象
 */
public class Page {
	
	// 公共变量
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	//分页参数
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
	//返回结果
	//protected List<T> result = Collections.emptyList();
	//返回结果，以键值对的形式存储
	@SuppressWarnings("unchecked")
	@Expose
	protected List result = Collections.emptyList();
	//返回的结果集以对象的方式封装
	@SuppressWarnings("unchecked")
	protected List resultObj = Collections.emptyList();
	
	@Expose
	protected long totalCount = 0;

	// 构造函数
	//默认的时候是10条

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

	//查询参数函数

	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	/**
	 * 获得每页的记录数量,默认为1.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量,低于1时自动调整为1.
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;

		if (pageSize < 1) {
			this.pageSize = 1;
		}
	}

	/**
	* 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
	*/
	public int getFirst() {
		return ((pageNo - 1) * pageSize) + 1;
	}

	/**
	 * 获得排序字段,无默认值.多个排序字段时用','分隔.
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置排序字段,多个排序字段时用','分隔.
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 是否已设置排序字段,无默认值.
	 */
	public boolean isOrderBySetted() {
		return StringUtils.isNotBlank(orderBy);
	}

	/**
	 * 获得排序方向.
	 * 
	 * @param order 可选值为desc或asc,多个排序字段时用','分隔.
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 设置排序方式向.
	 * 
	 * @param order 可选值为desc或asc,多个排序字段时用','分隔.
	 */
	public void setOrder(final String order) {
		//检查order字符串的合法值
		String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr))
				throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
		}
		this.order = StringUtils.lowerCase(order);
	}

	/**
	 * 查询对象时是否自动另外执行count查询获取总记录数,默认为false.
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * 查询对象时是否自动另外执行count查询获取总记录数.
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	// 查询结果函数

	/**
	 * 取得总记录数,默认值为-1.
	 */
	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 根据pageSize与totalCount计算总页数,默认值为0.
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
	 * 是否还有下一页.
	 */
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	/**
	 * 取得下页的页号,序号从1开始.
	 * 当前页为尾页时仍返回尾页序号.
	 */
	public int getNextPage() {
		if (isHasNext())
			return pageNo + 1;
		else
			return pageNo;
	}

	/**
	 * 是否还有上一页.
	 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 取得上页的页号,序号从1开始.
	 * 当前页为首页时返回首页序号.
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
	 * 取得页内的记录列表.
	 */
	@SuppressWarnings("unchecked")
	public List getResult() {
		return result;
	}

	/**
	 * 填充页面的记录列表.
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
