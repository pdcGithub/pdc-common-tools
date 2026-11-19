/******************************************************************************************************

This file "ProcParamTester1.java" is part of project "pdc-common-database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Types;
import java.time.LocalDate;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import net.mickarea.tools.utils.PatternUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * 这是 net.mickarea.tools.models.query.ProcParam 的单元测试
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年11月18日
 */
public class ProcParamTester1 {

	/**
	 * 这里测试的是 存储过程参数对象 的传送类型 常量
	 * @throws Exception 一些可能的异常。
	 */
	@Test
	void testTypeList() throws Exception {
		
		// 异常值
		assertFalse(ProcParam.TYPE_LIST.contains(null));
		assertFalse(ProcParam.TYPE_LIST.contains(""));
		assertFalse(ProcParam.TYPE_LIST.contains(" "));
		assertFalse(ProcParam.TYPE_LIST.contains("    "));
		assertFalse(ProcParam.TYPE_LIST.contains("aaa"));
		assertFalse(ProcParam.TYPE_LIST.contains("null"));
		
		// 正常值
		assertTrue(ProcParam.TYPE_LIST.contains("in"));
		assertTrue(ProcParam.TYPE_LIST.contains("out"));
		assertTrue(ProcParam.TYPE_LIST.contains("inout"));
		assertTrue(ProcParam.TYPE_LIST.contains(ProcParam.TYPE_IN));
		assertTrue(ProcParam.TYPE_LIST.contains(ProcParam.TYPE_OUT));
		assertTrue(ProcParam.TYPE_LIST.contains(ProcParam.TYPE_INOUT));
		
	}
	
	/**
	 * 测试构造函数 ProcParam()
	 * @throws Exception 一些可能的异常。
	 */
	@Test
	void testConstructor1() throws Exception {
		
		ProcParam p1 = new ProcParam();
		
		// 检测初始状态
		assertEquals(ProcParam.TYPE_IN, p1.getParamType());
		assertEquals(null, p1.getParamName());
		assertEquals(null, p1.getParamValue());
		assertEquals(Types.VARCHAR, p1.getOutParamSqlType());
		
		// 检测 paramType 
		p1.setParamType(null);
		assertEquals(ProcParam.TYPE_IN, p1.getParamType());
		p1.setParamType("");
		assertEquals(ProcParam.TYPE_IN, p1.getParamType());
		p1.setParamType(" ");
		assertEquals(ProcParam.TYPE_IN, p1.getParamType());
		p1.setParamType("     ");
		assertEquals(ProcParam.TYPE_IN, p1.getParamType());
		p1.setParamType("null");
		assertEquals(ProcParam.TYPE_IN, p1.getParamType());
		p1.setParamType("aaaa");
		assertEquals(ProcParam.TYPE_IN, p1.getParamType());
		p1.setParamType(ProcParam.TYPE_IN);
		assertEquals(ProcParam.TYPE_IN, p1.getParamType());
		p1.setParamType(ProcParam.TYPE_OUT);
		assertEquals(ProcParam.TYPE_OUT, p1.getParamType());
		p1.setParamType(ProcParam.TYPE_INOUT);
		assertEquals(ProcParam.TYPE_INOUT, p1.getParamType());
		
		// 检测 paramName
		p1.setParamName(null);
		assertEquals(null, p1.getParamName());
		p1.setParamName("");
		assertEquals(null, p1.getParamName());
		p1.setParamName(" ");
		assertEquals(null, p1.getParamName());
		p1.setParamName("    ");
		assertEquals(null, p1.getParamName());
		p1.setParamName(" n u l l ");
		assertEquals(null, p1.getParamName()); // 作为参数名，还是不建议 "null"
		p1.setParamName(" kdk  kdkdk");
		assertEquals("kdkkdkdk", p1.getParamName());
		p1.setParamName(" k _  1 ");
		assertEquals("k_1", p1.getParamName());
		
		// 检测 paramValue (因为 value 是 Object 类型，它可以是任意值)
		p1.setParamValue(null);
		assertEquals(null, p1.getParamValue());
		p1.setParamValue("");
		assertEquals("", p1.getParamValue());
		p1.setParamValue(" ");
		assertEquals(" ", p1.getParamValue());
		p1.setParamValue("    ");
		assertEquals("    ", p1.getParamValue());
		p1.setParamValue(" n ul l ");
		assertEquals(" n ul l ", p1.getParamValue());
		p1.setParamValue(1);
		assertEquals(1, p1.getParamValue());
		p1.setParamValue(3.14);
		assertEquals(3.14, p1.getParamValue());
		p1.setParamValue(LocalDate.parse("2012-12-21"));
		assertEquals(LocalDate.parse("2012-12-21"), p1.getParamValue());
		
		// 检测 outParamSqlType（对于 sql 类型，它是一堆 int 数值。并且在 java.sql.Types 中声明了）
		p1.setOutParamSqlType(-999999999);
		assertEquals(Types.VARCHAR, p1.getOutParamSqlType()); // 默认是有值的
		p1.setOutParamSqlType(999999999);
		assertEquals(Types.VARCHAR, p1.getOutParamSqlType());
		p1.setOutParamSqlType(Types.BIT);
		assertEquals(Types.BIT, p1.getOutParamSqlType());
		p1.setOutParamSqlType(Types.BLOB);
		assertEquals(Types.BLOB, p1.getOutParamSqlType());
		p1.setOutParamSqlType(Types.DATE);
		assertEquals(Types.DATE, p1.getOutParamSqlType());
		
	}
	
	/**
	 * 测试构造函数 ProcParam(String pType, Object pValue)
	 * @throws Exception 一些可能的异常。
	 */
	@Test
	void testConstructor2() throws Exception {
		
		// 前面已经测试了 setter 和 getter 方法。这里只需要测试 构造函数的初始化 情况
		
		// 异常 参数 初始化( value 可以是任意值，所以不测 异常情况)
		ProcParam pErr1 = new ProcParam(null, "value");
		ProcParam pErr2 = new ProcParam("", "value");
		ProcParam pErr3 = new ProcParam(" ", "value");
		ProcParam pErr4 = new ProcParam("null", "value");
		ProcParam pErr5 = new ProcParam("aaa", "value");
		
		// 结果比对 1
		assertEquals(ProcParam.TYPE_IN, pErr1.getParamType());
		assertEquals(null, pErr1.getParamName());
		assertEquals("value", pErr1.getParamValue());
		assertEquals(Types.VARCHAR, pErr1.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_IN, pErr2.getParamType());
		assertEquals(null, pErr2.getParamName());
		assertEquals("value", pErr2.getParamValue());
		assertEquals(Types.VARCHAR, pErr2.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_IN, pErr3.getParamType());
		assertEquals(null, pErr3.getParamName());
		assertEquals("value", pErr3.getParamValue());
		assertEquals(Types.VARCHAR, pErr3.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_IN, pErr4.getParamType());
		assertEquals(null, pErr4.getParamName());
		assertEquals("value", pErr4.getParamValue());
		assertEquals(Types.VARCHAR, pErr4.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_IN, pErr5.getParamType());
		assertEquals(null, pErr5.getParamName());
		assertEquals("value", pErr5.getParamValue());
		assertEquals(Types.VARCHAR, pErr5.getOutParamSqlType());
		
		// 正常 参数 初始化
		ProcParam pOk1 = new ProcParam(ProcParam.TYPE_IN, "value2");
		ProcParam pOk2 = new ProcParam(ProcParam.TYPE_OUT, "value2");
		ProcParam pOk3 = new ProcParam(ProcParam.TYPE_INOUT, "value2");
		
		// 结果比对 2
		assertEquals(ProcParam.TYPE_IN, pOk1.getParamType());
		assertEquals(null, pOk1.getParamName());
		assertEquals("value2", pOk1.getParamValue());
		assertEquals(Types.VARCHAR, pOk1.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_OUT, pOk2.getParamType());
		assertEquals(null, pOk2.getParamName());
		assertEquals("value2", pOk2.getParamValue());
		assertEquals(Types.VARCHAR, pOk2.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_INOUT, pOk3.getParamType());
		assertEquals(null, pOk3.getParamName());
		assertEquals("value2", pOk3.getParamValue());
		assertEquals(Types.VARCHAR, pOk3.getOutParamSqlType());
	}
	
	/**
	 * 测试构造函数 ProcParam(String pType, String pName, Object pValue)
	 * @throws Exception 一些可能的异常。
	 */
	@Test
	void testConstructor3() throws Exception {
		
		// 前面已经测试了 setter 和 getter 方法。这里只需要测试 构造函数的初始化 情况
		
		// 异常 参数 初始化( value 可以是任意值，所以不测 异常情况)
		ProcParam pErr1 = new ProcParam(null, "name1", "value1");
		ProcParam pErr2 = new ProcParam("", "name1", "value1");
		ProcParam pErr3 = new ProcParam(" ", "name1", "value1");
		ProcParam pErr4 = new ProcParam("    ", "name1", "value1");
		ProcParam pErr5 = new ProcParam("aaaa", "name1", "value1");
		ProcParam pErr6 = new ProcParam(ProcParam.TYPE_IN, null, "value1");
		ProcParam pErr7 = new ProcParam(ProcParam.TYPE_IN, "", "value1");
		ProcParam pErr8 = new ProcParam(ProcParam.TYPE_IN, " ", "value1");
		ProcParam pErr9 = new ProcParam(ProcParam.TYPE_IN, "null", "value1");
		
		// 结果比对 1
		assertEquals(ProcParam.TYPE_IN, pErr1.getParamType());
		assertEquals("name1", pErr1.getParamName());
		assertEquals("value1", pErr1.getParamValue());
		assertEquals(Types.VARCHAR, pErr1.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_IN, pErr2.getParamType());
		assertEquals("name1", pErr2.getParamName());
		assertEquals("value1", pErr2.getParamValue());
		assertEquals(Types.VARCHAR, pErr2.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_IN, pErr3.getParamType());
		assertEquals("name1", pErr3.getParamName());
		assertEquals("value1", pErr3.getParamValue());
		assertEquals(Types.VARCHAR, pErr3.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_IN, pErr4.getParamType());
		assertEquals("name1", pErr4.getParamName());
		assertEquals("value1", pErr4.getParamValue());
		assertEquals(Types.VARCHAR, pErr4.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_IN, pErr5.getParamType());
		assertEquals("name1", pErr5.getParamName());
		assertEquals("value1", pErr5.getParamValue());
		assertEquals(Types.VARCHAR, pErr5.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_IN, pErr6.getParamType());
		assertEquals(null, pErr6.getParamName());
		assertEquals("value1", pErr6.getParamValue());
		assertEquals(Types.VARCHAR, pErr6.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_IN, pErr7.getParamType());
		assertEquals(null, pErr7.getParamName());
		assertEquals("value1", pErr7.getParamValue());
		assertEquals(Types.VARCHAR, pErr7.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_IN, pErr8.getParamType());
		assertEquals(null, pErr8.getParamName());
		assertEquals("value1", pErr8.getParamValue());
		assertEquals(Types.VARCHAR, pErr8.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_IN, pErr9.getParamType());
		assertEquals(null, pErr9.getParamName());
		assertEquals("value1", pErr9.getParamValue());
		assertEquals(Types.VARCHAR, pErr9.getOutParamSqlType());
		
		// 正常 参数 初始化
		ProcParam pOk1 = new ProcParam(ProcParam.TYPE_IN, " n a m e 3 ", "value3");
		ProcParam pOk2 = new ProcParam(ProcParam.TYPE_OUT, " n a m e 3 ", "value3");
		ProcParam pOk3 = new ProcParam(ProcParam.TYPE_INOUT, " n a m e 3 ", "value3");
		
		// 结果比对 2
		assertEquals(ProcParam.TYPE_IN, pOk1.getParamType());
		assertEquals("name3", pOk1.getParamName());
		assertEquals("value3", pOk1.getParamValue());
		assertEquals(Types.VARCHAR, pOk1.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_OUT, pOk2.getParamType());
		assertEquals("name3", pOk2.getParamName());
		assertEquals("value3", pOk2.getParamValue());
		assertEquals(Types.VARCHAR, pOk2.getOutParamSqlType());
		
		assertEquals(ProcParam.TYPE_INOUT, pOk3.getParamType());
		assertEquals("name3", pOk3.getParamName());
		assertEquals("value3", pOk3.getParamValue());
		assertEquals(Types.VARCHAR, pOk3.getOutParamSqlType());
	}
	
	/**
	 * 测试 toString() 函数
	 * @throws Exception 一些可能的异常。
	 */
	@Test
	void testToString() throws Exception {
		
		// 把 5个 构造函数都测试一遍
		ProcParam pOk1 = new ProcParam();
		ProcParam pOk2 = new ProcParam(ProcParam.TYPE_OUT, "valu2");
		ProcParam pOk3 = new ProcParam(ProcParam.TYPE_INOUT, "name3", "value3");
		
		// 这里的输出，主要用于打印输出。观察信息是否有异常
		Stdout.pl(pOk1);
		Stdout.pl(pOk2);
		Stdout.pl(pOk3);
		
		// 定义一个正则，用于判断格式是否正确。(匹配时，忽略大小写)
		String regexp = "procparam\\{\\(private\\).+(,[\\s]*\\(private\\).+)*\\}";
		
		// 判断
		assertTrue(PatternUtil.matches(regexp, pOk1.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, pOk2.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, pOk3.toString(), Pattern.CASE_INSENSITIVE));
		
	}
	
}
