/******************************************************************************************************

This file "OrderParamTester1.java" is part of project "pdc-common-database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import net.mickarea.tools.beans.tables.Student;
import net.mickarea.tools.beans.views.VStudent;
import net.mickarea.tools.beans.virtualentities.VOStudent;
import net.mickarea.tools.utils.PatternUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * 这是 net.mickarea.tools.models.query.OrderParam 类的单元测试
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年11月16日
 */
public class OrderParamTester1 {

	/**
	 * 这里比对的是 排序操作符的合规性 asc 、 desc
	 */
	@Test
	void testOptList() {
		
		assertFalse(OrderParam.OPT_LIST.contains(null));
		assertFalse(OrderParam.OPT_LIST.contains(""));
		assertFalse(OrderParam.OPT_LIST.contains(" "));
		assertFalse(OrderParam.OPT_LIST.contains("      "));
		assertFalse(OrderParam.OPT_LIST.contains(" asc "));
		
		assertTrue(OrderParam.OPT_LIST.contains("asc"));
		assertTrue(OrderParam.OPT_LIST.contains("desc"));
		assertTrue(OrderParam.OPT_LIST.contains(OrderParam.OPT_ASC));
		assertTrue(OrderParam.OPT_LIST.contains(OrderParam.OPT_DESC));
		
	}
	
	/**
	 * 这里测试的是 无参构造函数 OrderParam()
	 */
	@Test
	void testConstructor1() {
		
		OrderParam o1 = new OrderParam();
		
		// 初始状态比对
		assertEquals(null, o1.getParamName());
		assertEquals(null, o1.getAlias());
		assertEquals(OrderParam.OPT_ASC, o1.getOrderOpt());
		assertEquals(null, o1.getClassName());
		
		// paramName 测试
		o1.setParamName(null);
		assertEquals(null, o1.getParamName());
		o1.setParamName("");
		assertEquals(null, o1.getParamName());
		o1.setParamName(" ");
		assertEquals(null, o1.getParamName());
		o1.setParamName("    ");
		assertEquals(null, o1.getParamName());
		o1.setParamName(" null "); // 对于 sql 语句，null 是一个关键字。不能随意填写。
		assertEquals(null, o1.getParamName());
		o1.setParamName("  a  a  a ");
		assertEquals("aaa", o1.getParamName());
		o1.setParamName("stuNo");
		assertEquals("stuNo", o1.getParamName());
		
		// alias 测试
		o1.setAlias(null);
		assertEquals(null, o1.getAlias());
		o1.setAlias("");
		assertEquals(null, o1.getAlias());
		o1.setAlias(" ");
		assertEquals(null, o1.getAlias());
		o1.setAlias("    ");
		assertEquals(null, o1.getAlias());
		o1.setAlias(" null "); // 对于 sql 语句，null 是一个关键字。不能随意填写。
		assertEquals(null, o1.getAlias());
		o1.setAlias(" 2 k ");
		assertEquals("2k", o1.getAlias());
		o1.setAlias("bbb");
		assertEquals("bbb", o1.getAlias());
		
		// orderOpt 测试。默认它是 ASC
		o1.setOrderOpt(null);
		assertEquals(OrderParam.OPT_ASC, o1.getOrderOpt());
		o1.setOrderOpt("");
		assertEquals(OrderParam.OPT_ASC, o1.getOrderOpt());
		o1.setOrderOpt(" ");
		assertEquals(OrderParam.OPT_ASC, o1.getOrderOpt());
		o1.setOrderOpt("   ");
		assertEquals(OrderParam.OPT_ASC, o1.getOrderOpt());
		o1.setOrderOpt(" null ");
		assertEquals(OrderParam.OPT_ASC, o1.getOrderOpt());
		o1.setOrderOpt("aaaa");
		assertEquals(OrderParam.OPT_ASC, o1.getOrderOpt());
		o1.setOrderOpt(" b  b b ");
		assertEquals(OrderParam.OPT_ASC, o1.getOrderOpt());
		o1.setOrderOpt(OrderParam.OPT_DESC); // 需要合规，才会设置进去
		assertEquals(OrderParam.OPT_DESC, o1.getOrderOpt());
		o1.setOrderOpt(OrderParam.OPT_ASC);
		assertEquals(OrderParam.OPT_ASC, o1.getOrderOpt());
		
		// className 测试
		o1.setClassName(null);
		assertEquals(null, o1.getClassName());
		o1.setClassName(this.getClass());
		assertEquals(null, o1.getClassName());
		o1.setClassName(String.class);
		assertEquals(null, o1.getClassName());
		o1.setClassName(Student.class);
		assertEquals(Student.class, o1.getClassName());
		o1.setClassName(VStudent.class);
		assertEquals(VStudent.class, o1.getClassName());
		o1.setClassName(VOStudent.class);
		assertEquals(VOStudent.class, o1.getClassName());
	}
	
	/**
	 * 这里测试的是 有参构造函数 OrderParam(String paramName)
	 */
	@Test
	void testConstructor2() {
		
		// 构造函数初始化
		OrderParam o1 = new OrderParam((String)null);
		OrderParam o2 = new OrderParam("");
		OrderParam o3 = new OrderParam(" ");
		OrderParam o4 = new OrderParam("     ");
		OrderParam o5 = new OrderParam(" null "); // 对于 sql 语句，null 是一个关键字。不能随意填写。
		OrderParam o6 = new OrderParam(" a a  a ");
		OrderParam o7 = new OrderParam(" stuNo ");
		
		// 结果比对 （getter 和 setter 方法 已经在 无参构造函数测试了。所以这里直接比对构造函数的结果就行了。）
		assertEquals(null, o1.getParamName());
		assertEquals(null, o1.getAlias());
		assertEquals(OrderParam.OPT_ASC, o1.getOrderOpt());
		assertEquals(null, o1.getClassName());
		
		assertEquals(null, o2.getParamName());
		assertEquals(null, o2.getAlias());
		assertEquals(OrderParam.OPT_ASC, o2.getOrderOpt());
		assertEquals(null, o2.getClassName());
		
		assertEquals(null, o3.getParamName());
		assertEquals(null, o3.getAlias());
		assertEquals(OrderParam.OPT_ASC, o3.getOrderOpt());
		assertEquals(null, o3.getClassName());
		
		assertEquals(null, o4.getParamName());
		assertEquals(null, o4.getAlias());
		assertEquals(OrderParam.OPT_ASC, o4.getOrderOpt());
		assertEquals(null, o4.getClassName());
		
		assertEquals(null, o5.getParamName());
		assertEquals(null, o5.getAlias());
		assertEquals(OrderParam.OPT_ASC, o5.getOrderOpt());
		assertEquals(null, o5.getClassName());
		
		assertEquals("aaa", o6.getParamName());
		assertEquals(null, o6.getAlias());
		assertEquals(OrderParam.OPT_ASC, o6.getOrderOpt());
		assertEquals(null, o6.getClassName());
		
		assertEquals("stuNo", o7.getParamName());
		assertEquals(null, o7.getAlias());
		assertEquals(OrderParam.OPT_ASC, o7.getOrderOpt());
		assertEquals(null, o7.getClassName());
		
	}
	
	/**
	 * 这里测试的是 有参构造函数 OrderParam(String paramName, String orderOpt)
	 */
	@Test
	void testConstructor3() {
		
		// 构造函数初始化（要测试 2个 参数）
		OrderParam o1 = new OrderParam((String)null, OrderParam.OPT_ASC);
		OrderParam o2 = new OrderParam("", OrderParam.OPT_ASC);
		OrderParam o3 = new OrderParam(" ", OrderParam.OPT_ASC);
		OrderParam o4 = new OrderParam("    ", OrderParam.OPT_ASC);
		OrderParam o5 = new OrderParam(" null ", OrderParam.OPT_ASC);
		OrderParam o6 = new OrderParam(" aaa ", null);
		OrderParam o7 = new OrderParam(" aaa ", "");
		OrderParam o8 = new OrderParam(" aaa ", " ");
		OrderParam o9 = new OrderParam(" aaa ", "    ");
		OrderParam o10 = new OrderParam(" aaa ", " bbb ");
		OrderParam o11 = new OrderParam(" aaa ", OrderParam.OPT_DESC);
		OrderParam o12 = new OrderParam(" aaa ", OrderParam.OPT_ASC);
		
		// 结果比对 （getter 和 setter 方法 已经在 无参构造函数测试了。所以这里直接比对构造函数的结果就行了。）
		assertEquals(null, o1.getParamName());
		assertEquals(null, o1.getAlias());
		assertEquals(OrderParam.OPT_ASC, o1.getOrderOpt());
		assertEquals(null, o1.getClassName());
		
		assertEquals(null, o2.getParamName());
		assertEquals(null, o2.getAlias());
		assertEquals(OrderParam.OPT_ASC, o2.getOrderOpt());
		assertEquals(null, o2.getClassName());
		
		assertEquals(null, o3.getParamName());
		assertEquals(null, o3.getAlias());
		assertEquals(OrderParam.OPT_ASC, o3.getOrderOpt());
		assertEquals(null, o3.getClassName());
		
		assertEquals(null, o4.getParamName());
		assertEquals(null, o4.getAlias());
		assertEquals(OrderParam.OPT_ASC, o4.getOrderOpt());
		assertEquals(null, o4.getClassName());
		
		assertEquals(null, o5.getParamName());
		assertEquals(null, o5.getAlias());
		assertEquals(OrderParam.OPT_ASC, o5.getOrderOpt());
		assertEquals(null, o5.getClassName());
		
		assertEquals("aaa", o6.getParamName());
		assertEquals(null, o6.getAlias());
		assertEquals(OrderParam.OPT_ASC, o6.getOrderOpt());
		assertEquals(null, o6.getClassName());
		
		assertEquals("aaa", o7.getParamName());
		assertEquals(null, o7.getAlias());
		assertEquals(OrderParam.OPT_ASC, o7.getOrderOpt());
		assertEquals(null, o7.getClassName());
		
		assertEquals("aaa", o8.getParamName());
		assertEquals(null, o8.getAlias());
		assertEquals(OrderParam.OPT_ASC, o8.getOrderOpt());
		assertEquals(null, o8.getClassName());
		
		assertEquals("aaa", o9.getParamName());
		assertEquals(null, o9.getAlias());
		assertEquals(OrderParam.OPT_ASC, o9.getOrderOpt());
		assertEquals(null, o9.getClassName());
		
		assertEquals("aaa", o10.getParamName());
		assertEquals(null, o10.getAlias());
		assertEquals(OrderParam.OPT_ASC, o10.getOrderOpt());
		assertEquals(null, o10.getClassName());
		
		assertEquals("aaa", o11.getParamName());
		assertEquals(null, o11.getAlias());
		assertEquals(OrderParam.OPT_DESC, o11.getOrderOpt());
		assertEquals(null, o11.getClassName());
		
		assertEquals("aaa", o12.getParamName());
		assertEquals(null, o12.getAlias());
		assertEquals(OrderParam.OPT_ASC, o12.getOrderOpt());
		assertEquals(null, o12.getClassName());
		
	}
	
	/**
	 * 这里测试的是 有参构造函数 OrderParam(String paramName, String orderOpt, String alias)
	 */
	@Test
	void testConstructor4() {

		// 构造函数初始化（要测试3个参数）
		OrderParam o1 = new OrderParam((String)null, OrderParam.OPT_DESC, "2k");
		OrderParam o2 = new OrderParam("", OrderParam.OPT_DESC, "2k");
		OrderParam o3 = new OrderParam(" ", OrderParam.OPT_DESC, "2k");
		OrderParam o4 = new OrderParam("    ", OrderParam.OPT_DESC, "2k");
		OrderParam o5 = new OrderParam(" null ", OrderParam.OPT_DESC, "2k");
		OrderParam o6 = new OrderParam("aaa", null, "2k");
		OrderParam o7 = new OrderParam("aaa", "", "2k");
		OrderParam o8 = new OrderParam("aaa", " ", "2k");
		OrderParam o9 = new OrderParam("aaa", " sss ", "2k");
		OrderParam o10 = new OrderParam("aaa", OrderParam.OPT_DESC, null);
		OrderParam o11 = new OrderParam("aaa", OrderParam.OPT_DESC, "");
		OrderParam o12 = new OrderParam("aaa", OrderParam.OPT_DESC, " ");
		OrderParam o13 = new OrderParam("aaa", OrderParam.OPT_DESC, "    ");
		OrderParam o14 = new OrderParam("aaa", OrderParam.OPT_DESC, " null ");
		OrderParam o15 = new OrderParam("aaa", OrderParam.OPT_DESC, "2k");
		
		// 结果比对 （getter 和 setter 方法 已经在 无参构造函数测试了。所以这里直接比对构造函数的结果就行了。）
		assertEquals(null, o1.getParamName());
		assertEquals("2k", o1.getAlias());
		assertEquals(OrderParam.OPT_DESC, o1.getOrderOpt());
		assertEquals(null, o1.getClassName());
		
		assertEquals(null, o2.getParamName());
		assertEquals("2k", o2.getAlias());
		assertEquals(OrderParam.OPT_DESC, o2.getOrderOpt());
		assertEquals(null, o2.getClassName());
		
		assertEquals(null, o3.getParamName());
		assertEquals("2k", o3.getAlias());
		assertEquals(OrderParam.OPT_DESC, o3.getOrderOpt());
		assertEquals(null, o3.getClassName());
		
		assertEquals(null, o4.getParamName());
		assertEquals("2k", o4.getAlias());
		assertEquals(OrderParam.OPT_DESC, o4.getOrderOpt());
		assertEquals(null, o4.getClassName());
		
		assertEquals(null, o5.getParamName());
		assertEquals("2k", o5.getAlias());
		assertEquals(OrderParam.OPT_DESC, o5.getOrderOpt());
		assertEquals(null, o5.getClassName());
		
		assertEquals("aaa", o6.getParamName());
		assertEquals("2k", o6.getAlias());
		assertEquals(OrderParam.OPT_ASC, o6.getOrderOpt());
		assertEquals(null, o6.getClassName());
		
		assertEquals("aaa", o7.getParamName());
		assertEquals("2k", o7.getAlias());
		assertEquals(OrderParam.OPT_ASC, o7.getOrderOpt());
		assertEquals(null, o7.getClassName());
		
		assertEquals("aaa", o8.getParamName());
		assertEquals("2k", o8.getAlias());
		assertEquals(OrderParam.OPT_ASC, o8.getOrderOpt());
		assertEquals(null, o8.getClassName());
		
		assertEquals("aaa", o9.getParamName());
		assertEquals("2k", o9.getAlias());
		assertEquals(OrderParam.OPT_ASC, o9.getOrderOpt());
		assertEquals(null, o9.getClassName());
		
		assertEquals("aaa", o10.getParamName());
		assertEquals(null, o10.getAlias());
		assertEquals(OrderParam.OPT_DESC, o10.getOrderOpt());
		assertEquals(null, o10.getClassName());
		
		assertEquals("aaa", o11.getParamName());
		assertEquals(null, o11.getAlias());
		assertEquals(OrderParam.OPT_DESC, o11.getOrderOpt());
		assertEquals(null, o11.getClassName());
		
		assertEquals("aaa", o12.getParamName());
		assertEquals(null, o12.getAlias());
		assertEquals(OrderParam.OPT_DESC, o12.getOrderOpt());
		assertEquals(null, o12.getClassName());
		
		assertEquals("aaa", o13.getParamName());
		assertEquals(null, o13.getAlias());
		assertEquals(OrderParam.OPT_DESC, o13.getOrderOpt());
		assertEquals(null, o13.getClassName());
		
		assertEquals("aaa", o14.getParamName());
		assertEquals(null, o14.getAlias());
		assertEquals(OrderParam.OPT_DESC, o14.getOrderOpt());
		assertEquals(null, o14.getClassName());
		
		assertEquals("aaa", o15.getParamName());
		assertEquals("2k", o15.getAlias());
		assertEquals(OrderParam.OPT_DESC, o15.getOrderOpt());
		assertEquals(null, o15.getClassName());
		
	}
	
	/**
	 * 这里测试的是 有参构造函数 OrderParam(String paramName, String orderOpt, String alias, Class<?> className)
	 */
	@Test
	void testConstructor5() {

		// 构造函数初始化（这里要测试 4 个参数）
		OrderParam o1 = new OrderParam((String)null, OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o2 = new OrderParam("", OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o3 = new OrderParam(" ", OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o4 = new OrderParam("   ", OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o5 = new OrderParam(" null ", OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o6 = new OrderParam("aaa", null, "2k", Student.class);
		OrderParam o7 = new OrderParam("aaa", "", "2k", Student.class);
		OrderParam o8 = new OrderParam("aaa", " ", "2k", Student.class);
		OrderParam o9 = new OrderParam("aaa", "sss", "2k", Student.class);
		OrderParam o10 = new OrderParam("aaa", OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o11 = new OrderParam("aaa", OrderParam.OPT_ASC, "2k", Student.class);
		OrderParam o12 = new OrderParam("aaa", OrderParam.OPT_DESC, null, Student.class);
		OrderParam o13 = new OrderParam("aaa", OrderParam.OPT_DESC, "", Student.class);
		OrderParam o14 = new OrderParam("aaa", OrderParam.OPT_DESC, " ", Student.class);
		OrderParam o15 = new OrderParam("aaa", OrderParam.OPT_DESC, "    ", Student.class);
		OrderParam o16 = new OrderParam("aaa", OrderParam.OPT_DESC, " null ", Student.class);
		OrderParam o17 = new OrderParam("aaa", OrderParam.OPT_DESC, "2k", null);
		OrderParam o18 = new OrderParam("aaa", OrderParam.OPT_DESC, "2k", this.getClass());
		OrderParam o19 = new OrderParam("aaa", OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o20 = new OrderParam("aaa", OrderParam.OPT_DESC, "2k", VStudent.class);
		OrderParam o21 = new OrderParam("aaa", OrderParam.OPT_DESC, "2k", VOStudent.class);
		
		// 结果比对 （getter 和 setter 方法 已经在 无参构造函数测试了。所以这里直接比对构造函数的结果就行了。）
		assertEquals(null, o1.getParamName());
		assertEquals("2k", o1.getAlias());
		assertEquals(OrderParam.OPT_DESC, o1.getOrderOpt());
		assertEquals(Student.class, o1.getClassName());
		
		assertEquals(null, o2.getParamName());
		assertEquals("2k", o2.getAlias());
		assertEquals(OrderParam.OPT_DESC, o2.getOrderOpt());
		assertEquals(Student.class, o2.getClassName());
		
		assertEquals(null, o3.getParamName());
		assertEquals("2k", o3.getAlias());
		assertEquals(OrderParam.OPT_DESC, o3.getOrderOpt());
		assertEquals(Student.class, o3.getClassName());
		
		assertEquals(null, o4.getParamName());
		assertEquals("2k", o4.getAlias());
		assertEquals(OrderParam.OPT_DESC, o4.getOrderOpt());
		assertEquals(Student.class, o4.getClassName());
		
		assertEquals(null, o5.getParamName());
		assertEquals("2k", o5.getAlias());
		assertEquals(OrderParam.OPT_DESC, o5.getOrderOpt());
		assertEquals(Student.class, o5.getClassName());
		
		assertEquals("aaa", o6.getParamName());
		assertEquals("2k", o6.getAlias());
		assertEquals(OrderParam.OPT_ASC, o6.getOrderOpt());
		assertEquals(Student.class, o6.getClassName());
		
		assertEquals("aaa", o7.getParamName());
		assertEquals("2k", o7.getAlias());
		assertEquals(OrderParam.OPT_ASC, o7.getOrderOpt());
		assertEquals(Student.class, o7.getClassName());
		
		assertEquals("aaa", o8.getParamName());
		assertEquals("2k", o8.getAlias());
		assertEquals(OrderParam.OPT_ASC, o8.getOrderOpt());
		assertEquals(Student.class, o8.getClassName());
		
		assertEquals("aaa", o9.getParamName());
		assertEquals("2k", o9.getAlias());
		assertEquals(OrderParam.OPT_ASC, o9.getOrderOpt());
		assertEquals(Student.class, o9.getClassName());
		
		assertEquals("aaa", o10.getParamName());
		assertEquals("2k", o10.getAlias());
		assertEquals(OrderParam.OPT_DESC, o10.getOrderOpt());
		assertEquals(Student.class, o10.getClassName());
		
		assertEquals("aaa", o11.getParamName());
		assertEquals("2k", o11.getAlias());
		assertEquals(OrderParam.OPT_ASC, o11.getOrderOpt());
		assertEquals(Student.class, o11.getClassName());
		
		assertEquals("aaa", o12.getParamName());
		assertEquals(null, o12.getAlias());
		assertEquals(OrderParam.OPT_DESC, o12.getOrderOpt());
		assertEquals(Student.class, o12.getClassName());
		
		assertEquals("aaa", o13.getParamName());
		assertEquals(null, o13.getAlias());
		assertEquals(OrderParam.OPT_DESC, o13.getOrderOpt());
		assertEquals(Student.class, o13.getClassName());
		
		assertEquals("aaa", o14.getParamName());
		assertEquals(null, o14.getAlias());
		assertEquals(OrderParam.OPT_DESC, o14.getOrderOpt());
		assertEquals(Student.class, o14.getClassName());
		
		assertEquals("aaa", o15.getParamName());
		assertEquals(null, o15.getAlias());
		assertEquals(OrderParam.OPT_DESC, o15.getOrderOpt());
		assertEquals(Student.class, o15.getClassName());
		
		assertEquals("aaa", o16.getParamName());
		assertEquals(null, o16.getAlias());
		assertEquals(OrderParam.OPT_DESC, o16.getOrderOpt());
		assertEquals(Student.class, o16.getClassName());
		
		assertEquals("aaa", o17.getParamName());
		assertEquals("2k", o17.getAlias());
		assertEquals(OrderParam.OPT_DESC, o17.getOrderOpt());
		assertEquals(null, o17.getClassName());
		
		assertEquals("aaa", o18.getParamName());
		assertEquals("2k", o18.getAlias());
		assertEquals(OrderParam.OPT_DESC, o18.getOrderOpt());
		assertEquals(null, o18.getClassName());
		
		assertEquals("aaa", o19.getParamName());
		assertEquals("2k", o19.getAlias());
		assertEquals(OrderParam.OPT_DESC, o19.getOrderOpt());
		assertEquals(Student.class, o19.getClassName());
		
		assertEquals("aaa", o20.getParamName());
		assertEquals("2k", o20.getAlias());
		assertEquals(OrderParam.OPT_DESC, o20.getOrderOpt());
		assertEquals(VStudent.class, o20.getClassName());
		
		assertEquals("aaa", o21.getParamName());
		assertEquals("2k", o21.getAlias());
		assertEquals(OrderParam.OPT_DESC, o21.getOrderOpt());
		assertEquals(VOStudent.class, o21.getClassName());
		
	}
	
	/**
	 * 这里测试的是 toSqlString() 函数
	 */
	@Test
	void testToSqlString() {
		
		// =========== 因为拼接很简单，就是 别名 + 字段 + 排序操作符。所以，直接用 构造函数来处理
		OrderParam o1 = new OrderParam((String)null, OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o2 = new OrderParam("", OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o3 = new OrderParam(" ", OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o4 = new OrderParam("   ", OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o5 = new OrderParam(" null ", OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o6 = new OrderParam("stuNo", null, "2k", Student.class);
		OrderParam o7 = new OrderParam("stuNo", "", "2k", Student.class);
		OrderParam o8 = new OrderParam("stuNo", " ", "2k", Student.class);
		OrderParam o9 = new OrderParam("stuNo", "sss", "2k", Student.class);
		OrderParam o10 = new OrderParam("stuNo", OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o11 = new OrderParam("stuNo", OrderParam.OPT_ASC, "2k", Student.class);
		OrderParam o12 = new OrderParam("stuNo", OrderParam.OPT_DESC, null, Student.class);
		OrderParam o13 = new OrderParam("stuNo", OrderParam.OPT_DESC, "", Student.class);
		OrderParam o14 = new OrderParam("stuNo", OrderParam.OPT_DESC, " ", Student.class);
		OrderParam o15 = new OrderParam("stuNo", OrderParam.OPT_DESC, "    ", Student.class);
		OrderParam o16 = new OrderParam("stuNo", OrderParam.OPT_DESC, " null ", Student.class);
		OrderParam o17 = new OrderParam("stuNo", OrderParam.OPT_DESC, "2k", null);
		OrderParam o18 = new OrderParam("stuNo", OrderParam.OPT_DESC, "2k", this.getClass());
		OrderParam o19 = new OrderParam("stuNo", OrderParam.OPT_DESC, "2k", Student.class);
		OrderParam o20 = new OrderParam("stuNo", OrderParam.OPT_DESC, "2k", VStudent.class);
		OrderParam o21 = new OrderParam("stuNo", OrderParam.OPT_DESC, "2k", VOStudent.class);
		
		// 比对结果（如果字段名为空，则 别名不会拼接。只留下 排序操作符。因为排序操作符，有默认值）
		assertEquals(" desc", o1.toSqlString());
		assertEquals(" desc", o2.toSqlString());
		assertEquals(" desc", o3.toSqlString());
		assertEquals(" desc", o4.toSqlString());
		assertEquals(" desc", o5.toSqlString());
		assertEquals("2k.stu_no asc", o6.toSqlString());
		assertEquals("2k.stu_no asc", o7.toSqlString());
		assertEquals("2k.stu_no asc", o8.toSqlString());
		assertEquals("2k.stu_no asc", o9.toSqlString());
		assertEquals("2k.stu_no desc", o10.toSqlString());
		assertEquals("2k.stu_no asc", o11.toSqlString());
		assertEquals("stu_no desc", o12.toSqlString());
		assertEquals("stu_no desc", o13.toSqlString());
		assertEquals("stu_no desc", o14.toSqlString());
		assertEquals("stu_no desc", o15.toSqlString());
		assertEquals("stu_no desc", o16.toSqlString());
		assertEquals("2k.stuNo desc", o17.toSqlString());
		assertEquals("2k.stuNo desc", o18.toSqlString());
		assertEquals("2k.stu_no desc", o19.toSqlString());
		assertEquals("2k.stu_no desc", o20.toSqlString());
		assertEquals("2k.stu_no desc", o21.toSqlString());
		
	}
	
	/**
	 * 这里测试的是 toString() 函数
	 */
	@Test
	void testToString() {
		
		// 把 5个 构造函数都测试一遍
		OrderParam o1 = new OrderParam();
		OrderParam o2 = new OrderParam("stuNo");
		OrderParam o3 = new OrderParam("stuNo", OrderParam.OPT_DESC);
		OrderParam o4 = new OrderParam("stuNo", OrderParam.OPT_DESC, "2k");
		OrderParam o5 = new OrderParam("stuNo", OrderParam.OPT_DESC, "2k", Student.class);
		
		// 这里的输出，主要用于打印输出。观察信息是否有异常
		Stdout.pl(o1);
		Stdout.pl(o2);
		Stdout.pl(o3);
		Stdout.pl(o4);
		Stdout.pl(o5);
		
		// 定义一个正则，用于判断格式是否正确。(匹配时，忽略大小写)
		String regexp = "orderparam\\{\\(private\\).+(,[\\s]*\\(private\\).+)*\\}";
		
		// 判断
		assertTrue(PatternUtil.matches(regexp, o1.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, o2.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, o3.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, o4.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, o5.toString(), Pattern.CASE_INSENSITIVE));
		
	}
	
}
