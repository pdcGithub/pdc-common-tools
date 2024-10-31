/******************************************************************************************************

This file "PageInfo.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import net.mickarea.tools.utils.Stdout;

/**
 * &gt;&gt;&nbsp;分页查询数据时的分页对象类。pageNum 和 pageSize 有默认值
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月21日
 */
public class PageInfo {

	/**
	 * &gt;&gt;&nbsp;当前页的页码（最小从1开始）
	 */
	private int pageNum = 1;
	/**
	 * &gt;&gt;&nbsp;一个数据页的分页大小（默认每页20条）
	 */
	private int pageSize = 20;
	/**
	 * &gt;&gt;&nbsp;一共有多少个分页
	 */
	private int totalPage = 0;
	/**
	 * &gt;&gt;&nbsp;一共有多少条数据
	 */
	private int totalCount = 0;
	
	/**
	 * &gt;&gt;&nbsp;构造函数
	 */
	public PageInfo() {
	}
	public PageInfo(int pageNum, int pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}
	public PageInfo(int pageNum, int pageSize, int totalPage, int totalCount) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.totalCount = totalCount;
	}

	// getter and setter
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	@Override
	public String toString() {
		return Stdout.fplToAnyWhere("分页信息如下：共有记录 %s 条，当前页码 %s，分页大小为 %s , 共有 %s 页数据。 ", 
				this.totalCount,
				this.pageNum,
				this.pageSize,
				this.totalPage);
	}
	
	/**
	 * &gt;&gt;&nbsp;测试函数
	 */
	public static void main(String[] args) {
		Stdout.pl(new PageInfo());
		Stdout.pl(new PageInfo(2, 15));
		Stdout.pl(new PageInfo(1, 30, 55, 1650));
	}
	
}
