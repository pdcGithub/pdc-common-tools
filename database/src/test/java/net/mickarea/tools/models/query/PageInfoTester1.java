/******************************************************************************************************

This file "PageInfoTester1.java" is part of project "pdc-common-database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import net.mickarea.tools.utils.PatternUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * 这是 net.mickarea.tools.models.query.PageInfo 类的单元测试
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年11月18日
 */
public class PageInfoTester1 {

	/**
	 * 这里测试的是 PageInfo() 构造函数
	 * @throws Exception 一些可能的异常
	 */
	@Test
	void testConstructor1() throws Exception {
		
		PageInfo p1 = new PageInfo();
		
		// 判断初始状态
		assertEquals(1, p1.getPageNum());
		assertEquals(20, p1.getPageSize());
		assertEquals(0, p1.getTotalPage());
		assertEquals(0, p1.getTotalCount());
		
		// 对这私有属性设置值，然后再比对 pageNum 。页码初始为 1 ，需要 大于等于 1 才会覆盖设置。
		p1.setPageNum(-1);
		assertEquals(1, p1.getPageNum());
		p1.setPageNum(0);
		assertEquals(1, p1.getPageNum());
		p1.setPageNum(2);
		assertEquals(2, p1.getPageNum());
		p1.setPageNum(Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, p1.getPageNum());
		p1.setPageNum(Integer.MAX_VALUE + 1);
		assertEquals(Integer.MAX_VALUE, p1.getPageNum()); // max + 1 ，然后变 负数。这样不会设置进去。
		
		// 对这私有属性设置值，然后再比对 pageSize 。pageSize 默认是 20。设置时，需要大于等于 1
		p1.setPageSize(-1);
		assertEquals(20, p1.getPageSize());
		p1.setPageSize(0);
		assertEquals(20, p1.getPageSize());
		p1.setPageSize(1);
		assertEquals(1, p1.getPageSize());
		p1.setPageSize(Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, p1.getPageSize());
		p1.setPageSize(Integer.MAX_VALUE +1);
		assertEquals(Integer.MAX_VALUE, p1.getPageSize()); // max + 1 ，然后变 负数。这样不会设置进去。
		
		// 对这私有属性设置值，然后再比对 totalPage 。total page 默认是 0，设置时，需要大于等于 0
		p1.setTotalPage(-1);
		assertEquals(0, p1.getTotalPage());
		p1.setTotalPage(0);
		assertEquals(0, p1.getTotalPage());
		p1.setTotalPage(1);
		assertEquals(1, p1.getTotalPage());
		p1.setTotalPage(Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, p1.getTotalPage());
		p1.setTotalPage(Integer.MAX_VALUE + 1);
		assertEquals(Integer.MAX_VALUE, p1.getTotalPage()); // max + 1 ，然后变 负数。这样不会设置进去。
		
		// 对这私有属性设置值，然后再比对 totalCount 。默认是 0，设置时，需要大于等于 0
		p1.setTotalCount(-1);
		assertEquals(0, p1.getTotalCount());
		p1.setTotalCount(0);
		assertEquals(0, p1.getTotalCount());
		p1.setTotalCount(1);
		assertEquals(1, p1.getTotalCount());
		p1.setTotalCount(Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, p1.getTotalCount());
		p1.setTotalCount(Integer.MAX_VALUE + 1);
		assertEquals(Integer.MAX_VALUE, p1.getTotalCount()); // max + 1 ，然后变 负数。这样不会设置进去。
		
	}
	
	/**
	 * 这里测试的是 PageInfo(int pageNum, int pageSize) 构造函数
	 * @throws Exception 一些可能的异常
	 */
	@Test
	void testConstructor2() throws Exception {
		
		// 首先，每一个参数使用异常的结果，测试一次
		
		PageInfo p1 = new PageInfo(-1, 2);
		PageInfo p2 = new PageInfo(0, 2);
		PageInfo p3 = new PageInfo(Integer.MAX_VALUE+1, 2);
		// 
		PageInfo p4 = new PageInfo(2, -1); // 注意：如果设置不成功，pageSize 默认是20的。
		PageInfo p5 = new PageInfo(2, 0);
		PageInfo p6 = new PageInfo(2, Integer.MAX_VALUE+1);
		
		assertEquals(1, p1.getPageNum());
		assertEquals(2, p1.getPageSize());
		assertEquals(0, p1.getTotalPage());
		assertEquals(0, p1.getTotalCount());
		
		assertEquals(1, p2.getPageNum());
		assertEquals(2, p2.getPageSize());
		assertEquals(0, p2.getTotalPage());
		assertEquals(0, p2.getTotalCount());
		
		assertEquals(1, p3.getPageNum());
		assertEquals(2, p3.getPageSize());
		assertEquals(0, p3.getTotalPage());
		assertEquals(0, p3.getTotalCount());
		
		assertEquals(2, p4.getPageNum()); // 注意：如果设置不成功，pageSize 默认是20的。
		assertEquals(20, p4.getPageSize());
		assertEquals(0, p4.getTotalPage());
		assertEquals(0, p4.getTotalCount());
		
		assertEquals(2, p5.getPageNum());
		assertEquals(20, p5.getPageSize());
		assertEquals(0, p5.getTotalPage());
		assertEquals(0, p5.getTotalCount());
		
		assertEquals(2, p6.getPageNum());
		assertEquals(20, p6.getPageSize());
		assertEquals(0, p6.getTotalPage());
		assertEquals(0, p6.getTotalCount());
		
		// 然后，测试正确的参数
		PageInfo pOk1 = new PageInfo(222, Integer.MAX_VALUE);
		PageInfo pOk2 = new PageInfo(Integer.MAX_VALUE, 333);
		
		assertEquals(222, pOk1.getPageNum());
		assertEquals(Integer.MAX_VALUE, pOk1.getPageSize());
		assertEquals(0, pOk1.getTotalPage());
		assertEquals(0, pOk1.getTotalCount());
		
		assertEquals(Integer.MAX_VALUE, pOk2.getPageNum());
		assertEquals(333, pOk2.getPageSize());
		assertEquals(0, pOk2.getTotalPage());
		assertEquals(0, pOk2.getTotalCount());
		
	}
	
	/**
	 * 这里测试的是 PageInfo(int pageNum, int pageSize, int totalPage, int totalCount) 构造函数
	 * @throws Exception 一些可能的异常
	 */
	@Test
	void testConstructor3() throws Exception {
		
		// 首先，每一个参数使用异常的结果，测试一次
		PageInfo pErr1 = new PageInfo(-1, 222, 333, 444);
		PageInfo pErr2 = new PageInfo(0, 222, 333, 444);
		PageInfo pErr3 = new PageInfo(Integer.MAX_VALUE+1, 222, 333, 444);
		//
		PageInfo pErr4 = new PageInfo(111, -1, 333, 444);
		PageInfo pErr5 = new PageInfo(111, 0, 333, 444);
		PageInfo pErr6 = new PageInfo(111, Integer.MAX_VALUE+1, 333, 444);
		//
		PageInfo pErr7 = new PageInfo(111, 222, -1, 444);
		PageInfo pErr8 = new PageInfo(111, 222, Integer.MAX_VALUE+1, 444);
		//
		PageInfo pErr9 = new PageInfo(111, 222, 333, -1);
		PageInfo pErr10 = new PageInfo(111, 222, 333, Integer.MAX_VALUE+1);
		 
		assertEquals(1, pErr1.getPageNum());
		assertEquals(222, pErr1.getPageSize());
		assertEquals(333, pErr1.getTotalPage());
		assertEquals(444, pErr1.getTotalCount());
		// 
		assertEquals(1, pErr2.getPageNum());
		assertEquals(222, pErr2.getPageSize());
		assertEquals(333, pErr2.getTotalPage());
		assertEquals(444, pErr2.getTotalCount());
		// 
		assertEquals(1, pErr3.getPageNum());
		assertEquals(222, pErr3.getPageSize());
		assertEquals(333, pErr3.getTotalPage());
		assertEquals(444, pErr3.getTotalCount());
		//
		assertEquals(111, pErr4.getPageNum());
		assertEquals(20, pErr4.getPageSize());
		assertEquals(333, pErr4.getTotalPage());
		assertEquals(444, pErr4.getTotalCount());
		// 
		assertEquals(111, pErr5.getPageNum());
		assertEquals(20, pErr5.getPageSize());
		assertEquals(333, pErr5.getTotalPage());
		assertEquals(444, pErr5.getTotalCount());
		// 
		assertEquals(111, pErr6.getPageNum());
		assertEquals(20, pErr6.getPageSize());
		assertEquals(333, pErr6.getTotalPage());
		assertEquals(444, pErr6.getTotalCount());
		// 
		assertEquals(111, pErr7.getPageNum());
		assertEquals(222, pErr7.getPageSize());
		assertEquals(0, pErr7.getTotalPage());
		assertEquals(444, pErr7.getTotalCount());
		// 
		assertEquals(111, pErr8.getPageNum());
		assertEquals(222, pErr8.getPageSize());
		assertEquals(0, pErr8.getTotalPage());
		assertEquals(444, pErr8.getTotalCount());
		//
		assertEquals(111, pErr9.getPageNum());
		assertEquals(222, pErr9.getPageSize());
		assertEquals(333, pErr9.getTotalPage());
		assertEquals(0, pErr9.getTotalCount());
		//
		assertEquals(111, pErr10.getPageNum());
		assertEquals(222, pErr10.getPageSize());
		assertEquals(333, pErr10.getTotalPage());
		assertEquals(0, pErr10.getTotalCount());
		
		// 然后，测试正确的参数
		PageInfo pOk1 = new PageInfo(111, 222, 333, 444);
		PageInfo pOk2 = new PageInfo(Integer.MAX_VALUE, Integer.MAX_VALUE-1, Integer.MAX_VALUE-2, Integer.MAX_VALUE-3);
		
		assertEquals(111, pOk1.getPageNum());
		assertEquals(222, pOk1.getPageSize());
		assertEquals(333, pOk1.getTotalPage());
		assertEquals(444, pOk1.getTotalCount());
		// 
		assertEquals(Integer.MAX_VALUE, pOk2.getPageNum());
		assertEquals(Integer.MAX_VALUE-1, pOk2.getPageSize());
		assertEquals(Integer.MAX_VALUE-2, pOk2.getTotalPage());
		assertEquals(Integer.MAX_VALUE-3, pOk2.getTotalCount());
	}
	
	/**
	 * 这里测试的是 toString() 方法
	 * @throws Exception 一些可能的异常
	 */
	@Test
	void testToString() throws Exception {
		
		// 把 5个 构造函数都测试一遍
		PageInfo pOk1 = new PageInfo();
		PageInfo pOk2 = new PageInfo(111, 222, 333, 444);
		PageInfo pOk3 = new PageInfo(Integer.MAX_VALUE, Integer.MAX_VALUE-1, Integer.MAX_VALUE-2, Integer.MAX_VALUE-3);
		
		// 这里的输出，主要用于打印输出。观察信息是否有异常
		Stdout.pl(pOk1);
		Stdout.pl(pOk2);
		Stdout.pl(pOk3);
		
		// 定义一个正则，用于判断格式是否正确。(匹配时，忽略大小写)
		String regexp = "pageinfo\\{\\(private\\).+(,[\\s]*\\(private\\).+)*\\}";
		
		// 判断
		assertTrue(PatternUtil.matches(regexp, pOk1.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, pOk2.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, pOk3.toString(), Pattern.CASE_INSENSITIVE));
		
	}
	
}
