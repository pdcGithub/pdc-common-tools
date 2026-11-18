/******************************************************************************************************

This file "PageInfo.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import net.mickarea.tools.utils.StrUtil;

/**
 * 这是在进行分页查询时，对应的分页信息对象类。pageNum 和 pageSize 有默认值
 * @author Michael Pang (Dongcan Pang)
 * @since 2023年5月21日
 */
public class PageInfo {

	/**
	 * 当前页的页码（最小从1开始）
	 */
	private int pageNum = 1;
	
	/**
	 * 一个数据页的分页大小（默认每页20条）
	 */
	private int pageSize = 20;
	
	/**
	 * 一共有多少个分页
	 */
	private int totalPage = 0;
	
	/**
	 * 一共有多少条数据
	 */
	private int totalCount = 0;
	
	/**
	 * 无参 构造函数。用于生成一个分页信息对象。这个构造函数，依赖 setter 和 getter 给私有属性进行初始化。
	 * pageNum 和 pageSize 本身是有初始值的。
	 */
	public PageInfo() {
		// 这里通过其它构造函数，进行初始化
		// total 信息，默认是 0 就行了。
		this(1, 20, 0, 0);
	}
	
	/**
	 * 有参 构造函数。用于生成一个分页信息对象。pageNum 和 pageSize 本身是有初始值的，如果这里设置合理，则覆盖。
	 * @param pageNum 前页的页码
	 * @param pageSize 数据页的分页大小
	 */
	public PageInfo(int pageNum, int pageSize) {
		// 这里通过其它构造函数，进行初始化
		// total 信息，默认是 0 就行了。
		this(pageNum, pageSize, 0, 0);
	}
	
	/**
	 * 有参 构造函数。用于生成一个分页信息对象。pageNum 和 pageSize 本身是有初始值的，如果这里设置合理，则覆盖。
	 * @param pageNum 前页的页码
	 * @param pageSize 数据页的分页大小
	 * @param totalPage 数据分页后的总页数
	 * @param totalCount 数据分页后的总记录数
	 */
	public PageInfo(int pageNum, int pageSize, int totalPage, int totalCount) {
		// 为了方便统一管理，这里由 setter 方法处理
		this.setPageNum(pageNum);
		this.setPageSize(pageSize);
		this.setTotalPage(totalPage);
		this.setTotalCount(totalCount);
	}

	// ========================================== getter and setter
	
	/**
	 * 获取 当前页的页码
	 * @return 当前页的页码
	 */
	public int getPageNum() {
		return this.pageNum;
	}
	
	/**
	 * 设置 当前页的页码（作为一个分页信息，页码最小是1，最大是 int 类型的最大值。不符合要求的值，不会设置）
	 * @param pageNum 当前页的页码。
	 * @return 本对象的引用
	 */
	public PageInfo setPageNum(int pageNum) {
		// 如果符合要求，则设置
		if(1<=pageNum && pageNum<=Integer.MAX_VALUE) this.pageNum = pageNum;
		// 返回本对象的引用
		return this;
	}
	
	/**
	 * 获取 数据页的分页大小
	 * @return 数据页的分页大小
	 */
	public int getPageSize() {
		return this.pageSize;
	}
	
	/**
	 * 设置 数据页的分页大小（作为分页大小，最小是1，最大是 int 类型的最大值。不符合要求的值，不会设置）
	 * @param pageSize 数据页的分页大小
	 * @return 本对象的引用
	 */
	public PageInfo setPageSize(int pageSize) {
		// 如果符合要求，则设置
		if(1<=pageSize && pageSize<=Integer.MAX_VALUE) this.pageSize = pageSize;
		// 返回本对象的引用
		return this;
	}
	
	/**
	 * 获取 数据分页后的总页数
	 * @return 数据分页后的总页数
	 */
	public int getTotalPage() {
		return this.totalPage;
	}
	
	/**
	 * 设置 数据分页后的总页数（作为分页总数，最小是0，最大是 int 类型的最大值。不符合要求的值，不会设置）
	 * @param totalPage 数据分页后的总页数
	 * @return 本对象的引用
	 */
	public PageInfo setTotalPage(int totalPage) {
		// 如果符合要求，则设置
		if(0<=totalPage && totalPage<=Integer.MAX_VALUE) this.totalPage = totalPage;
		// 返回本对象的引用
		return this;
	}
	
	/**
	 * 获取 数据分页后的总记录数
	 * @return 数据分页后的总记录数
	 */
	public int getTotalCount() {
		return this.totalCount;
	}
	
	/**
	 * 设置 数据分页后的总记录数（作为分页总记录数，最小是0，最大是 int 类型的最大值。不符合要求的值，不会设置）
	 * @param totalCount 数据分页后的总记录数
	 * @return 
	 */
	public PageInfo setTotalCount(int totalCount) {
		// 如果符合要求，则设置
		if(0<=totalCount && totalCount<=Integer.MAX_VALUE) this.totalCount = totalCount;
		// 返回本对象的引用
		return this;
	}
	
	/**
	 * 这里输出的是 本对象的私有属性信息
	 */
	@Override
	public String toString() {
		return StrUtil.getJavaBeanFieldsInfo(this);
	}
	
}
