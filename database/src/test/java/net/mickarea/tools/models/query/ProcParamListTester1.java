/******************************************************************************************************

This file "ProcParamListTester1.java" is part of project "pdc-common-database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Types;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import net.mickarea.tools.utils.PatternUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * 这里是 net.mickarea.tools.models.query.ProcParamList 类的单元测试
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年11月19日
 */
public class ProcParamListTester1 {

	/**
	 * 这里测试的是 构造函数
	 * @throws Exception 一些可能的异常
	 */
	@Test
	void testConstructor1() throws Exception {
		
		// 一个空参构造函数
		ProcParamList pList = new ProcParamList();
		
		// 检测初始状态 ( 调用 以确认这个容器有初始化 )
		assertEquals(0, pList.getParamLength());
		
	}
	
	/**
	 * 这里测试的是 addParam 方法
	 * @throws Exception 一些可能的异常
	 */
	@Test
	void testAddParam() throws Exception {
		
		// 
		ProcParamList pList = new ProcParamList();
		
		// 添加并比对
		pList.addParam(null);
		assertEquals(0, pList.getParamLength()); // null 不会加进去
		
		pList.addParam(new ProcParam());
		assertEquals(1, pList.getParamLength()); // 对于默认的 ProcParam 流向是 IN ，outSqlType 是 varchar
		
		pList.addParam(new ProcParam(ProcParam.TYPE_OUT, "value2"));
		assertEquals(2, pList.getParamLength());
		
		pList.addParam(new ProcParam(ProcParam.TYPE_INOUT, "name3", "value3"));
		assertEquals(3, pList.getParamLength());
		
	}
	
	/**
	 * 这里测试的是 getParam 方法
	 * @throws Exception 一些可能的异常
	 */
	@Test
	void testGetParam() throws Exception {
		
		// 首先 定义一个 list容器。然后添加几个内容进去
		ProcParamList pList = new ProcParamList();
		pList.addParam(new ProcParam());
		pList.addParam(new ProcParam(ProcParam.TYPE_OUT, "value2"));
		pList.addParam(new ProcParam(ProcParam.TYPE_INOUT, "name3", "value3"));
		pList.addParam(new ProcParam(ProcParam.TYPE_IN, "name3", "value4")); // 这里故意写 name3 ，看看会不会错误提取。正常是按顺序第一个。
		
		// 这里把对象提取出来，再比对 
		
		assertEquals(null, pList.getParam(-100)); // 序号不对，则返回 null
		assertEquals(null, pList.getParam(0));
		assertEquals(null, pList.getParam(100));
		// 
		assertNotEquals(null, pList.getParam(1));
		assertEquals(ProcParam.TYPE_IN, pList.getParam(1).getParamType());
		assertEquals(null, pList.getParam(1).getParamName());
		assertEquals(null, pList.getParam(1).getParamValue());
		assertEquals(Types.VARCHAR, pList.getParam(1).getOutParamSqlType());
		// 
		assertNotEquals(null, pList.getParam(2));
		assertEquals(ProcParam.TYPE_OUT, pList.getParam(2).getParamType());
		assertEquals(null, pList.getParam(2).getParamName());
		assertEquals("value2", pList.getParam(2).getParamValue());
		assertEquals(Types.VARCHAR, pList.getParam(2).getOutParamSqlType());
		// 
		assertNotEquals(null, pList.getParam(3));
		assertEquals(ProcParam.TYPE_INOUT, pList.getParam(3).getParamType());
		assertEquals("name3", pList.getParam(3).getParamName());
		assertEquals("value3", pList.getParam(3).getParamValue());
		assertEquals(Types.VARCHAR, pList.getParam(3).getOutParamSqlType());
		// 
		assertNotEquals(null, pList.getParam(4));
		assertEquals(ProcParam.TYPE_IN, pList.getParam(4).getParamType());
		assertEquals("name3", pList.getParam(4).getParamName());
		assertEquals("value4", pList.getParam(4).getParamValue());
		assertEquals(Types.VARCHAR, pList.getParam(4).getOutParamSqlType());
		
		// 这里把对象提取出来，再比对 。这里，通过参数名称来提取
		assertEquals(null, pList.getParam((String)null));
		assertEquals(null, pList.getParam(""));
		assertEquals(null, pList.getParam(" "));
		assertEquals(null, pList.getParam("     "));
		assertEquals(null, pList.getParam("null"));
		
		ProcParam p3 = pList.getParam(" n a m e 3 ");
		
		// 看看 提取的是否为 第3个。因为，第4个也是 name3 ，但是值不同，顺序不同。
		assertNotEquals(null, p3);
		assertEquals(ProcParam.TYPE_INOUT, p3.getParamType());
		assertEquals("name3", p3.getParamName());
		assertEquals("value3", p3.getParamValue());
		assertEquals(Types.VARCHAR, p3.getOutParamSqlType());
		
	}
	
	/**
	 * 这里测试的是 remove 方法
	 * @throws Exception 一些可能的异常
	 */
	@Test
	void testRemove() throws Exception {
		
		// 首先 定义一个 list容器。然后添加几个内容进去
		ProcParamList pList = new ProcParamList();
		pList.addParam(new ProcParam());
		pList.addParam(new ProcParam(ProcParam.TYPE_OUT, "value2"));
		pList.addParam(new ProcParam(ProcParam.TYPE_INOUT, "name3", "value3"));
		pList.addParam(new ProcParam(ProcParam.TYPE_IN, "name3", "value4"));
		
		// 初始长度
		assertEquals(4, pList.getParamLength());
		
		pList.removeAll();
		
		// 删除后的长度
		assertEquals(0, pList.getParamLength());
		
	}
	
	/**
	 * 这里测试的是 toString 方法
	 * @throws Exception 一些可能的异常
	 */
	@Test
	void testToString() throws Exception {
		
		ProcParamList pList = new ProcParamList();
		pList.addParam(new ProcParam());
		pList.addParam(new ProcParam(ProcParam.TYPE_OUT, "value2"));
		pList.addParam(new ProcParam(ProcParam.TYPE_INOUT, "name3", "value3"));
		pList.addParam(new ProcParam(ProcParam.TYPE_IN, "name3", "value4"));
		
		// 这里的输出，主要用于打印输出。观察信息是否有异常
		Stdout.pl(pList);
		
		// 定义一个正则，用于判断格式是否正确。(匹配时，忽略大小写)
		String regexp = "procparamlist\\{\\(private\\).+(,[\\s]*\\(private\\).+)*\\}";
		
		// 判断
		assertTrue(PatternUtil.matches(regexp, pList.toString(), Pattern.CASE_INSENSITIVE));
		
	}
	
}
