/******************************************************************************************************

This file "CondiParamTester1.java" is part of project "pdc-common-database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import net.mickarea.tools.beans.tables.Student;
import net.mickarea.tools.beans.views.VStudent;
import net.mickarea.tools.beans.virtualentities.VOStudent;
import net.mickarea.tools.utils.PatternUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 这是 net.mickarea.tools.models.query.CondiParam 类的单元测试。
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年11月2日
 */
public class CondiParamTester1 {

	/**
	 * 因为 OPT_LIST 改为 public 了，所以要测试
	 * @throws Exception 一些可能的异常
	 */
	@Test
	void testOptList() throws Exception {
		
		assertFalse(CondiParam.OPT_LIST.contains(null));
		assertFalse(CondiParam.OPT_LIST.contains(""));
		assertFalse(CondiParam.OPT_LIST.contains(" "));
		assertFalse(CondiParam.OPT_LIST.contains("      "));
		assertFalse(CondiParam.OPT_LIST.contains(" = "));
		
		assertTrue(CondiParam.OPT_LIST.contains("="));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_EQ));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_GT));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_GTE));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_LT));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_LTE));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_NOT_EQ));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_LIKE));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_LIKE_PRE));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_LIKE_SUF));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_IS_NULL));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_IS_NOT_NULL));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_REGEXP));
		assertTrue(CondiParam.OPT_LIST.contains(CondiParam.OPT_IN));
	}
	
	/**
	 * 这是无参的构造函数 CondiParam 
	 */
	@Test
	void testConstructor1() throws Exception {
		
		CondiParam c1 = new CondiParam();
		
		// 检查初始状态的值
		assertEquals(null, c1.getAlias());
		assertEquals(null, c1.getParamName());
		assertEquals(CondiParam.OPT_EQ, c1.getOptString()); // 操作符默认是有值的
		assertEquals(null, c1.getValue());
		assertEquals(null, c1.getClassName());
		assertEquals(null, c1.getSqlTemplateParamName());
		
		// 对这私有属性设置值，然后再比对 alias
		c1.setAlias("aaa");
		assertEquals("aaa", c1.getAlias());
		c1.setAlias("  b b b  ");
		assertEquals("bbb", c1.getAlias());
		c1.setAlias("");
		assertEquals("", c1.getAlias());
		c1.setAlias("    ");
		assertEquals("", c1.getAlias());
		c1.setAlias(null);
		assertEquals("", c1.getAlias());
		
		// 对私有属性设置值，然后再比对 paramName
		c1.setParamName("nnn");
		assertEquals("nnn", c1.getParamName());
		c1.setParamName("   m  m  m");
		assertEquals("mmm", c1.getParamName());
		c1.setParamName("");
		assertEquals("", c1.getParamName());
		c1.setParamName("    ");
		assertEquals("", c1.getParamName());
		c1.setParamName(null);
		assertEquals("", c1.getParamName());
		
		// 对私有属性设置值，然后再比对 optString 
		List<String> opts = Arrays.asList(
			CondiParam.OPT_EQ, CondiParam.OPT_GT, CondiParam.OPT_GTE, CondiParam.OPT_IN, CondiParam.OPT_IS_NOT_NULL,
			CondiParam.OPT_IS_NULL, CondiParam.OPT_LIKE, CondiParam.OPT_LIKE_PRE, CondiParam.OPT_LIKE_SUF, CondiParam.OPT_LT,
			CondiParam.OPT_LTE, CondiParam.OPT_NOT_EQ, CondiParam.OPT_REGEXP);
		// 遍历前先设置 null
		c1.setOptString(null);
		// 遍历测试
		for(String opt : opts) {
			c1.setOptString(opt);
			assertEquals(opt, c1.getOptString());
		}
		c1.setOptString("  =  ");
		assertEquals(CondiParam.OPT_EQ, c1.getOptString());
		c1.setOptString("");
		assertEquals("", c1.getOptString());
		c1.setOptString("   ");
		assertEquals("", c1.getOptString());
		c1.setOptString(null);
		assertEquals("", c1.getOptString());
		
		// 对私有属性设置值，然后再比对 value
		c1.setValue("abc");
		assertEquals("abc", c1.getValue());
		c1.setValue("  a  b  c  ");
		assertEquals("  a  b  c  ", c1.getValue());
		c1.setValue("");
		assertEquals("", c1.getValue());
		c1.setValue("    ");
		assertEquals("    ", c1.getValue());
		c1.setValue(11L);
		assertEquals(11L, c1.getValue());
		c1.setValue(LocalDate.now());
		assertEquals(LocalDate.now(), c1.getValue()); // 这里应该是同一天的。如果不同那说明你在23点和0点之间测试了。
		c1.setValue(null);
		assertEquals(null, c1.getValue());
		
		// 对私有属性设置值，然后再比对 className
		c1.setClassName(Student.class);
		assertEquals(Student.class, c1.getClassName());
		c1.setClassName(VStudent.class);
		assertEquals(VStudent.class, c1.getClassName());
		c1.setClassName(VOStudent.class);
		assertEquals(VOStudent.class, c1.getClassName());
		c1.setClassName(this.getClass());
		assertEquals(null, c1.getClassName());
		c1.setClassName(null);
		assertEquals(null, c1.getClassName());
		
		// 对私有属性设置值，然后再比对 sqlTemplateParamName
		c1.setSqlTemplateParamName("abcd");
		assertEquals("abcd", c1.getSqlTemplateParamName());
		c1.setSqlTemplateParamName("   a   b  c  d  ");
		assertEquals("abcd", c1.getSqlTemplateParamName());
		c1.setSqlTemplateParamName("");
		assertEquals("", c1.getSqlTemplateParamName());
		c1.setSqlTemplateParamName("       ");
		assertEquals("", c1.getSqlTemplateParamName());
		c1.setSqlTemplateParamName(null);
		assertEquals("", c1.getSqlTemplateParamName());
		
	}
	
	/**
	 * 这是有参的构造函数 CondiParam(String paramName, Object value)
	 */
	@Test
	void testConstructor2() throws Exception {
		
		// ========= 首先是异常的参数 
		
		// paramName 异常，value 正常
		CondiParam cErr1 = new CondiParam("", 1);
		CondiParam cErr2 = new CondiParam("    ", 1);
		CondiParam cErr3 = new CondiParam(null, 1);
		// paramName 正常，value 异常
		CondiParam cErr4 = new CondiParam("aaa", null);
		// 校验结果
		assertEquals("", cErr1.getParamName()); 
		assertEquals(1, cErr1.getValue());
		assertEquals(CondiParam.OPT_EQ, cErr1.getOptString());
		assertEquals(null, cErr1.getAlias());
		assertEquals(null, cErr1.getClassName());
		assertEquals("", cErr1.getSqlTemplateParamName());
		// 
		assertEquals("", cErr2.getParamName()); 
		assertEquals(1, cErr2.getValue());
		assertEquals(CondiParam.OPT_EQ, cErr2.getOptString());
		assertEquals(null, cErr2.getAlias());
		assertEquals(null, cErr2.getClassName());
		assertEquals("", cErr2.getSqlTemplateParamName());
		// 
		assertEquals("", cErr3.getParamName()); 
		assertEquals(1, cErr3.getValue());
		assertEquals(CondiParam.OPT_EQ, cErr3.getOptString());
		assertEquals(null, cErr3.getAlias());
		assertEquals(null, cErr3.getClassName());
		assertEquals("", cErr3.getSqlTemplateParamName());
		// 
		assertEquals("aaa", cErr4.getParamName()); 
		assertEquals(null, cErr4.getValue());
		assertEquals(CondiParam.OPT_EQ, cErr4.getOptString());
		assertEquals(null, cErr4.getAlias());
		assertEquals(null, cErr4.getClassName());
		assertEquals("aaa", cErr4.getSqlTemplateParamName());
		
		// ========= 然后是正常的参数（字段名，是允许有空白字符的，内部赋值会处理）
		CondiParam cOk1 = new CondiParam("  stu_no  ", "S00001");
		CondiParam cOk2 = new CondiParam("  birth_date  ", LocalDate.now());
		// 校验结果
		assertEquals(null, cOk1.getAlias());
		assertEquals("stu_no", cOk1.getParamName());
		assertEquals(CondiParam.OPT_EQ, cOk1.getOptString()); // 操作符默认是有值的
		assertEquals("S00001", cOk1.getValue());
		assertEquals(null, cOk1.getClassName());
		assertEquals("stu_no", cOk1.getSqlTemplateParamName()); // 如果 构造函数设置了 paraName，则默认的 tempName 是跟它一致
		// 
		assertEquals(null, cOk2.getAlias());
		assertEquals("birth_date", cOk2.getParamName());
		assertEquals(CondiParam.OPT_EQ, cOk2.getOptString()); // 操作符默认是有值的
		assertEquals(LocalDate.now(), cOk2.getValue());
		assertEquals(null, cOk2.getClassName());
		assertEquals("birth_date", cOk2.getSqlTemplateParamName()); // 如果 构造函数设置了 paraName，则默认的 tempName 是跟它一致
		
	}
	
	/**
	 * 这是有参的构造函数 CondiParam(String paramName, Object value, String optString)
	 */
	@Test
	void testConstructor3() throws Exception {
		
		// ========= 首先是异常的参数 
		
		// paramName 异常，value 正常，optString 正常
		CondiParam cErr1 = new CondiParam("",   1, CondiParam.OPT_GTE);
		CondiParam cErr2 = new CondiParam("  ", 1, CondiParam.OPT_GTE);
		CondiParam cErr3 = new CondiParam(null, 1, CondiParam.OPT_GTE);
		// paramName 正常，value 异常，optString 正常
		CondiParam cErr4 = new CondiParam("  aaa  ", null, CondiParam.OPT_GTE);
		// paramName 正常，value 正常，optString 异常
		CondiParam cErr5 = new CondiParam("  aaa  ", 1, "");
		CondiParam cErr6 = new CondiParam("  aaa  ", 1, "  ");
		CondiParam cErr7 = new CondiParam("  aaa  ", 1, null);
		// 校验结果
		assertEquals(null, cErr1.getAlias());
		assertEquals("", cErr1.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr1.getOptString());
		assertEquals(1, cErr1.getValue());
		assertEquals(null, cErr1.getClassName());
		assertEquals("", cErr1.getSqlTemplateParamName());
		//
		assertEquals(null, cErr2.getAlias());
		assertEquals("", cErr2.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr2.getOptString());
		assertEquals(1, cErr2.getValue());
		assertEquals(null, cErr2.getClassName());
		assertEquals("", cErr2.getSqlTemplateParamName());
		//
		assertEquals(null, cErr3.getAlias());
		assertEquals("", cErr3.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr3.getOptString());
		assertEquals(1, cErr3.getValue());
		assertEquals(null, cErr3.getClassName());
		assertEquals("", cErr3.getSqlTemplateParamName());
		// 
		assertEquals(null, cErr4.getAlias());
		assertEquals("aaa", cErr4.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr4.getOptString());
		assertEquals(null, cErr4.getValue());
		assertEquals(null, cErr4.getClassName());
		assertEquals("aaa", cErr4.getSqlTemplateParamName());
		// 
		assertEquals(null, cErr5.getAlias());
		assertEquals("aaa", cErr5.getParamName());
		assertEquals("", cErr5.getOptString());
		assertEquals(1, cErr5.getValue());
		assertEquals(null, cErr5.getClassName());
		assertEquals("aaa", cErr5.getSqlTemplateParamName());
		// 
		assertEquals(null, cErr6.getAlias());
		assertEquals("aaa", cErr6.getParamName());
		assertEquals("", cErr6.getOptString());
		assertEquals(1, cErr6.getValue());
		assertEquals(null, cErr6.getClassName());
		assertEquals("aaa", cErr6.getSqlTemplateParamName());
		// 
		assertEquals(null, cErr7.getAlias());
		assertEquals("aaa", cErr7.getParamName());
		assertEquals("", cErr7.getOptString());
		assertEquals(1, cErr7.getValue());
		assertEquals(null, cErr7.getClassName());
		assertEquals("aaa", cErr7.getSqlTemplateParamName());
		
		// ========= 然后是正常的参数（字段名，是允许有空白字符的，内部赋值会处理）
		
		CondiParam cOk1 = new CondiParam("  a  b  c  ",   1, CondiParam.OPT_GTE);
		CondiParam cOk2 = new CondiParam("  a  b  c  ",   LocalDate.now(), CondiParam.OPT_LIKE);
		// 校验结果
		assertEquals(null, cOk1.getAlias());
		assertEquals("abc", cOk1.getParamName());
		assertEquals(CondiParam.OPT_GTE, cOk1.getOptString()); // 操作符默认是有值的
		assertEquals(1, cOk1.getValue());
		assertEquals(null, cOk1.getClassName());
		assertEquals("abc", cOk1.getSqlTemplateParamName()); // 如果 构造函数设置了 paraName，则默认的 tempName 是跟它一致
		// 
		assertEquals(null, cOk2.getAlias());
		assertEquals("abc", cOk2.getParamName());
		assertEquals(CondiParam.OPT_LIKE, cOk2.getOptString()); // 操作符默认是有值的
		assertEquals(LocalDate.now(), cOk2.getValue());
		assertEquals(null, cOk2.getClassName());
		assertEquals("abc", cOk2.getSqlTemplateParamName()); // 如果 构造函数设置了 paraName，则默认的 tempName 是跟它一致
		
	}
	
	/**
	 * 这是有参的构造函数 CondiParam(String paramName, Object value, String optString, String alias)
	 */
	@Test
	void testConstructor4() throws Exception {

		// ========= 首先是异常的参数 
		
		// paramName 异常，value 正常，optString 正常，alias 正常
		CondiParam cErr1 = new CondiParam("",   1, CondiParam.OPT_GTE, "2k");
		CondiParam cErr2 = new CondiParam("  ",   1, CondiParam.OPT_GTE, "2k");
		CondiParam cErr3 = new CondiParam(null,   1, CondiParam.OPT_GTE, "2k");
		// paramName 正常，value 异常，optString 正常，alias 正常
		CondiParam cErr4 = new CondiParam(" a b c",   null, CondiParam.OPT_GTE, "2k");
		// paramName 正常，value 正常，optString 异常，alias 正常
		CondiParam cErr5 = new CondiParam(" a b c",   "fff", "", "2k");
		CondiParam cErr6 = new CondiParam(" a b c",   "fff", "  ", "2k");
		CondiParam cErr7 = new CondiParam(" a b c",   "fff", null, "2k");
		CondiParam cErr8 = new CondiParam(" a b c",   "fff", "kkkk", "2k");
		// paramName 正常，value 正常，optString 正常，alias 异常
		CondiParam cErr9 = new CondiParam(" a b c",   "fff", CondiParam.OPT_GTE, "");
		CondiParam cErr10 = new CondiParam(" a b c",   "fff", CondiParam.OPT_GTE, "  ");
		CondiParam cErr11 = new CondiParam(" a b c",   "fff", CondiParam.OPT_GTE, null);
		// 校验结果
		assertEquals("2k", cErr1.getAlias());
		assertEquals("", cErr1.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr1.getOptString());
		assertEquals(1, cErr1.getValue());
		assertEquals(null, cErr1.getClassName());
		assertEquals("", cErr1.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr2.getAlias());
		assertEquals("", cErr2.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr2.getOptString());
		assertEquals(1, cErr2.getValue());
		assertEquals(null, cErr2.getClassName());
		assertEquals("", cErr2.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr3.getAlias());
		assertEquals("", cErr3.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr3.getOptString());
		assertEquals(1, cErr3.getValue());
		assertEquals(null, cErr3.getClassName());
		assertEquals("", cErr3.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr4.getAlias());
		assertEquals("abc", cErr4.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr4.getOptString());
		assertEquals(null, cErr4.getValue());
		assertEquals(null, cErr4.getClassName());
		assertEquals("abc", cErr4.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr5.getAlias());
		assertEquals("abc", cErr5.getParamName());
		assertEquals("", cErr5.getOptString());
		assertEquals("fff", cErr5.getValue());
		assertEquals(null, cErr5.getClassName());
		assertEquals("abc", cErr5.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr6.getAlias());
		assertEquals("abc", cErr6.getParamName());
		assertEquals("", cErr6.getOptString());
		assertEquals("fff", cErr6.getValue());
		assertEquals(null, cErr6.getClassName());
		assertEquals("abc", cErr6.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr7.getAlias());
		assertEquals("abc", cErr7.getParamName());
		assertEquals("", cErr7.getOptString());
		assertEquals("fff", cErr7.getValue());
		assertEquals(null, cErr7.getClassName());
		assertEquals("abc", cErr7.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr8.getAlias());
		assertEquals("abc", cErr8.getParamName());
		assertEquals("", cErr8.getOptString());
		assertEquals("fff", cErr8.getValue());
		assertEquals(null, cErr8.getClassName());
		assertEquals("abc", cErr8.getSqlTemplateParamName());
		// 
		assertEquals("", cErr9.getAlias());
		assertEquals("abc", cErr9.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr9.getOptString());
		assertEquals("fff", cErr9.getValue());
		assertEquals(null, cErr9.getClassName());
		assertEquals("abc", cErr9.getSqlTemplateParamName());
		// 
		assertEquals("", cErr10.getAlias());
		assertEquals("abc", cErr10.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr10.getOptString());
		assertEquals("fff", cErr10.getValue());
		assertEquals(null, cErr10.getClassName());
		assertEquals("abc", cErr10.getSqlTemplateParamName());
		// 
		assertEquals("", cErr11.getAlias());
		assertEquals("abc", cErr11.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr11.getOptString());
		assertEquals("fff", cErr11.getValue());
		assertEquals(null, cErr11.getClassName());
		assertEquals("abc", cErr11.getSqlTemplateParamName());
		
		// ========= 然后是正常的参数（字段名，是允许有空白字符的，内部赋值会处理）
		
		CondiParam cOk1 = new CondiParam(" a b c",   "fff", CondiParam.OPT_GTE, " 2 k ");
		CondiParam cOk2 = new CondiParam(" a b c",   LocalDate.now(), CondiParam.OPT_GTE, " a ");
		// 校验结果
		assertEquals("2k", cOk1.getAlias());
		assertEquals("abc", cOk1.getParamName());
		assertEquals(CondiParam.OPT_GTE, cOk1.getOptString()); // 操作符默认是有值的
		assertEquals("fff", cOk1.getValue());
		assertEquals(null, cOk1.getClassName());
		assertEquals("abc", cOk1.getSqlTemplateParamName()); // 如果 构造函数设置了 paraName，则默认的 tempName 是跟它一致
		// 
		assertEquals("a", cOk2.getAlias());
		assertEquals("abc", cOk2.getParamName());
		assertEquals(CondiParam.OPT_GTE, cOk2.getOptString()); // 操作符默认是有值的
		assertEquals(LocalDate.now(), cOk2.getValue());
		assertEquals(null, cOk2.getClassName());
		assertEquals("abc", cOk2.getSqlTemplateParamName()); // 如果 构造函数设置了 paraName，则默认的 tempName 是跟它一致
		
	}
	
	/**
	 * 这是有参的构造函数 CondiParam(String paramName, Object value, String optString, String alias, Class<?> className)
	 */
	@Test
	void testConstructor5() throws Exception {

		// ========= 首先是异常的参数
		
		// paramName 异常，value 正常，optString 正常，alias 正常，className 正常
		CondiParam cErr1 = new CondiParam("",   1, CondiParam.OPT_GTE, "2k", Student.class);
		CondiParam cErr2 = new CondiParam("  ",   1, CondiParam.OPT_GTE, "2k", Student.class);
		CondiParam cErr3 = new CondiParam(null,   1, CondiParam.OPT_GTE, "2k", Student.class);
		// paramName 正常，value 异常，optString 正常，alias 正常，className 正常
		CondiParam cErr4 = new CondiParam(" a b c ", null, CondiParam.OPT_GTE, "2k", Student.class);
		// paramName 正常，value 正常，optString 异常，alias 正常，className 正常
		CondiParam cErr5 = new CondiParam(" a b c ", 1, "", "2k", Student.class);
		CondiParam cErr6 = new CondiParam(" a b c ", 1, "   ", "2k", Student.class);
		CondiParam cErr7 = new CondiParam(" a b c ", 1, "kkkkk", "2k", Student.class);
		// paramName 正常，value 正常，optString 正常，alias 异常，className 正常
		CondiParam cErr8 = new CondiParam(" a b c ", 1, CondiParam.OPT_EQ, "", Student.class);
		CondiParam cErr9 = new CondiParam(" a b c ", 1, CondiParam.OPT_EQ, "   ", Student.class);
		CondiParam cErr10 = new CondiParam(" a b c ", 1, CondiParam.OPT_EQ, null, Student.class);
		// paramName 正常，value 正常，optString 正常，alias 正常，className 异常
		CondiParam cErr11 = new CondiParam(" a b c ", 1, CondiParam.OPT_EQ, " 2 k ", null);
		CondiParam cErr12 = new CondiParam(" a b c ", 1, CondiParam.OPT_EQ, " 2 k ", this.getClass());
		// 校验结果 
		assertEquals("2k", cErr1.getAlias());
		assertEquals("", cErr1.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr1.getOptString());
		assertEquals(1, cErr1.getValue());
		assertEquals(Student.class, cErr1.getClassName());
		assertEquals("", cErr1.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr2.getAlias());
		assertEquals("", cErr2.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr2.getOptString());
		assertEquals(1, cErr2.getValue());
		assertEquals(Student.class, cErr2.getClassName());
		assertEquals("", cErr2.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr3.getAlias());
		assertEquals("", cErr3.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr3.getOptString());
		assertEquals(1, cErr3.getValue());
		assertEquals(Student.class, cErr3.getClassName());
		assertEquals("", cErr3.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr4.getAlias());
		assertEquals("abc", cErr4.getParamName());
		assertEquals(CondiParam.OPT_GTE, cErr4.getOptString());
		assertEquals(null, cErr4.getValue());
		assertEquals(Student.class, cErr4.getClassName());
		assertEquals("abc", cErr4.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr5.getAlias());
		assertEquals("abc", cErr5.getParamName());
		assertEquals("", cErr5.getOptString());
		assertEquals(1, cErr5.getValue());
		assertEquals(Student.class, cErr5.getClassName());
		assertEquals("abc", cErr5.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr6.getAlias());
		assertEquals("abc", cErr6.getParamName());
		assertEquals("", cErr6.getOptString());
		assertEquals(1, cErr6.getValue());
		assertEquals(Student.class, cErr6.getClassName());
		assertEquals("abc", cErr6.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr7.getAlias());
		assertEquals("abc", cErr7.getParamName());
		assertEquals("", cErr7.getOptString());
		assertEquals(1, cErr7.getValue());
		assertEquals(Student.class, cErr7.getClassName());
		assertEquals("abc", cErr7.getSqlTemplateParamName());
		// 
		assertEquals("", cErr8.getAlias());
		assertEquals("abc", cErr8.getParamName());
		assertEquals(CondiParam.OPT_EQ, cErr8.getOptString());
		assertEquals(1, cErr8.getValue());
		assertEquals(Student.class, cErr8.getClassName());
		assertEquals("abc", cErr8.getSqlTemplateParamName());
		// 
		assertEquals("", cErr9.getAlias());
		assertEquals("abc", cErr9.getParamName());
		assertEquals(CondiParam.OPT_EQ, cErr9.getOptString());
		assertEquals(1, cErr9.getValue());
		assertEquals(Student.class, cErr9.getClassName());
		assertEquals("abc", cErr9.getSqlTemplateParamName());
		// 
		assertEquals("", cErr10.getAlias());
		assertEquals("abc", cErr10.getParamName());
		assertEquals(CondiParam.OPT_EQ, cErr10.getOptString());
		assertEquals(1, cErr10.getValue());
		assertEquals(Student.class, cErr10.getClassName());
		assertEquals("abc", cErr10.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr11.getAlias());
		assertEquals("abc", cErr11.getParamName());
		assertEquals(CondiParam.OPT_EQ, cErr11.getOptString());
		assertEquals(1, cErr11.getValue());
		assertEquals(null, cErr11.getClassName());
		assertEquals("abc", cErr11.getSqlTemplateParamName());
		// 
		assertEquals("2k", cErr12.getAlias());
		assertEquals("abc", cErr12.getParamName());
		assertEquals(CondiParam.OPT_EQ, cErr12.getOptString());
		assertEquals(1, cErr12.getValue());
		assertEquals(null, cErr12.getClassName());
		assertEquals("abc", cErr12.getSqlTemplateParamName());
		
		// ========= 然后是正常的参数（字段名，是允许有空白字符的，内部赋值会处理）
		CondiParam cOk1 = new CondiParam(" s t u _ n o ", 1, CondiParam.OPT_GTE, " 2 k ", Student.class);
		CondiParam cOk2 = new CondiParam(" birth_date ", LocalDate.now(), CondiParam.OPT_LTE, " aaa ", VStudent.class);
		CondiParam cOk3 = new CondiParam(" birth_date ", LocalDate.now(), CondiParam.OPT_LTE, " aaa ", VOStudent.class);
		// 
		assertEquals("2k", cOk1.getAlias());
		assertEquals("stu_no", cOk1.getParamName());
		assertEquals(CondiParam.OPT_GTE, cOk1.getOptString());
		assertEquals(1, cOk1.getValue());
		assertEquals(Student.class, cOk1.getClassName());
		assertEquals("stu_no", cOk1.getSqlTemplateParamName());
		// 
		assertEquals("aaa", cOk2.getAlias());
		assertEquals("birth_date", cOk2.getParamName());
		assertEquals(CondiParam.OPT_LTE, cOk2.getOptString());
		assertEquals(LocalDate.now(), cOk2.getValue());
		assertEquals(VStudent.class, cOk2.getClassName());
		assertEquals("birth_date", cOk2.getSqlTemplateParamName());
		// 
		assertEquals("aaa", cOk3.getAlias());
		assertEquals("birth_date", cOk3.getParamName());
		assertEquals(CondiParam.OPT_LTE, cOk3.getOptString());
		assertEquals(LocalDate.now(), cOk3.getValue());
		assertEquals(VOStudent.class, cOk3.getClassName());
		assertEquals("birth_date", cOk3.getSqlTemplateParamName());
		
	}
	
	/**
	 * 这里测试 valueObjectToString 方法。非列表参数。要测试常用的数据库类型，以及常用的Java数据类型。
	 * <p>函数的作用是 把不同类型的value值，转换成一个能显示出来的字符串。</p>
	 * <p>测试涵盖（null+字符串+8个基本数据类型+8个基本引用类型+BigDecimal+日期类型+时间类型）</p>
	 */
	@Test
	void testValueObjectToString1_mysql8() throws Exception {
		
		// 定义一个 条件参数变量
		CondiParam c1 = new CondiParam();
		
		// mysql8 有2个防注入处理。DBTYPE_MYSQL(处理多个字符) 和 DBTYPE_MYSQL_ANSI(只处理单引号)。
		
		// ======================== DBTYPE_MYSQL
		
		// null 值
		assertEquals("null", c1.valueObjectToString((Object)null, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertTrue(c1.valueObjectToString("aaaa", null).contains("error"));
		assertTrue(c1.valueObjectToString("aaaa", "notdb").contains("error"));
		
		// 字符值（无注入的字符串，有注入的字符串）。注意：字符串值需要在前后增加 单引号。
		assertEquals("''", c1.valueObjectToString("", DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("'null'", c1.valueObjectToString("null", DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("' null '", c1.valueObjectToString(" null ", DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("'a'", c1.valueObjectToString("a", DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("' b '", c1.valueObjectToString(" b ", DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("' b kkk ff '", c1.valueObjectToString(" b kkk ff ", DBSQLInjectionUtil.DBTYPE_MYSQL));
		// 有注入的字符串
		assertEquals("'\\\\ \\\" \\% \\' \\_ \\= \\; abcdef ` ...'", 
			c1.valueObjectToString("\\ \" % ' _ = ; abcdef ` ...", DBSQLInjectionUtil.DBTYPE_MYSQL));
		
		// 8 种基本数据类型（byte、short、int、long、float、double、char、boolean）
		assertEquals("1", c1.valueObjectToString((byte)1, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2", c1.valueObjectToString((short)2, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("123456789", c1.valueObjectToString(123456789, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("1234567890123", c1.valueObjectToString(1234567890123L, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2.0", c1.valueObjectToString(2.0f, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("3.14159265358979", c1.valueObjectToString(3.14159265358979, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("'a'", c1.valueObjectToString('a', DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("1", c1.valueObjectToString(true, DBSQLInjectionUtil.DBTYPE_MYSQL)); // 在 mysql 中 BOOLEAN 底层是 TINYINT(1)，TRUE 就是 1
		assertEquals("0", c1.valueObjectToString(false, DBSQLInjectionUtil.DBTYPE_MYSQL));
		
		// 8 种基本数据类型的引用对象（Byte、Short、Integer、Long、Float、Double、Char、Boolean）
		assertEquals("1", c1.valueObjectToString(new Byte("1"), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2", c1.valueObjectToString(new Short("2"), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("123456789", c1.valueObjectToString(new Integer("123456789"), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("1234567890123", c1.valueObjectToString(new Long(1234567890123L), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2.0", c1.valueObjectToString(new Float(2.0f), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("3.14159265358979", c1.valueObjectToString(new Double(3.14159265358979), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("'a'", c1.valueObjectToString(new Character('a'), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("1", c1.valueObjectToString(new Boolean(true), DBSQLInjectionUtil.DBTYPE_MYSQL)); // 在 mysql 中 BOOLEAN 底层是 TINYINT(1)，TRUE 就是 1
		assertEquals("0", c1.valueObjectToString(new Boolean(false), DBSQLInjectionUtil.DBTYPE_MYSQL));
		
		// 大数字 BigDecimal 类型(小数部分保留 10 位，然后四舍五入)
		assertEquals("-1", c1.valueObjectToString(new BigDecimal("-1"), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("0", c1.valueObjectToString(new BigDecimal("0"), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("1", c1.valueObjectToString(new BigDecimal("1"), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("1234567890123456", c1.valueObjectToString(new BigDecimal("1234567890123456"), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("1.2345678901", c1.valueObjectToString(new BigDecimal("1.234567890123456"), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("-1.2345678901", c1.valueObjectToString(new BigDecimal("-1.234567890123456"), DBSQLInjectionUtil.DBTYPE_MYSQL));
		
		// 日期(java.sql.Date, java.util.Date, LocalDate)
		long oriDate = LocalDate.parse("2012-12-21").atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("'2012-12-21 00:00:00.0'", c1.valueObjectToString(new java.sql.Date(oriDate), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("'2012-12-21 00:00:00.0'", c1.valueObjectToString(new java.util.Date(oriDate), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("'2012-12-21'", c1.valueObjectToString(Instant.ofEpochMilli(oriDate)
																.atZone(ZoneOffset.ofHours(8))
																.toLocalDate(), DBSQLInjectionUtil.DBTYPE_MYSQL));
		
		// 时间(java.sql.Timestamp, LocalDateTime, java.util.Date)
		long oriTimestamp = LocalDateTime.parse("2012-12-21T05:20:17.123").atZone(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("'2012-12-21 05:20:17.123'", c1.valueObjectToString(new Timestamp(oriTimestamp), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("'2012-12-21T05:20:17.123'", c1.valueObjectToString(Instant.ofEpochMilli(oriTimestamp)
																.atZone(ZoneOffset.ofHours(8))
																.toLocalDateTime(), DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("'2012-12-21 05:20:17.123'", c1.valueObjectToString(new java.util.Date(oriTimestamp), DBSQLInjectionUtil.DBTYPE_MYSQL));
		
		// ======================== DBTYPE_MYSQL_ANSI （这里防注入只处理 单引号 '）
		
		// null 值
		assertEquals("null", c1.valueObjectToString(null, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		
		// 字符值（无注入的字符串，有注入的字符串）。注意：字符串值需要在前后增加 单引号。
		assertEquals("''", c1.valueObjectToString("", DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("'null'", c1.valueObjectToString("null", DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("' null '", c1.valueObjectToString(" null ", DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("'a'", c1.valueObjectToString("a", DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("' b '", c1.valueObjectToString(" b ", DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("' b kkk ff '", c1.valueObjectToString(" b kkk ff ", DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		// 有注入的字符串 （这里防注入只处理 单引号 '）
		assertEquals("'\\ \" % '' _ = ; abcdef ` ...'", 
			c1.valueObjectToString("\\ \" % ' _ = ; abcdef ` ...", DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		
		// 8 种基本数据类型（byte、short、int、long、float、double、char、boolean）
		assertEquals("1", c1.valueObjectToString((byte)1, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2", c1.valueObjectToString((short)2, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("123456789", c1.valueObjectToString(123456789, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("1234567890123", c1.valueObjectToString(1234567890123L, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2.0", c1.valueObjectToString(2.0f, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("3.14159265358979", c1.valueObjectToString(3.14159265358979, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("'a'", c1.valueObjectToString('a', DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("1", c1.valueObjectToString(true, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI)); // 在 mysql 中 BOOLEAN 底层是 TINYINT(1)，TRUE 就是 1
		assertEquals("0", c1.valueObjectToString(false, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		
		// 8 种基本数据类型的引用对象（Byte、Short、Integer、Long、Float、Double、Char、Boolean）
		assertEquals("1", c1.valueObjectToString(new Byte("1"), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2", c1.valueObjectToString(new Short("2"), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("123456789", c1.valueObjectToString(new Integer("123456789"), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("1234567890123", c1.valueObjectToString(new Long(1234567890123L), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2.0", c1.valueObjectToString(new Float(2.0f), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("3.14159265358979", c1.valueObjectToString(new Double(3.14159265358979), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("'a'", c1.valueObjectToString(new Character('a'), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("1", c1.valueObjectToString(new Boolean(true), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI)); // 在 mysql 中 BOOLEAN 底层是 TINYINT(1)，TRUE 就是 1
		assertEquals("0", c1.valueObjectToString(new Boolean(false), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		
		// 大数字 BigDecimal 类型(小数部分保留 10 位，然后四舍五入)
		assertEquals("-1", c1.valueObjectToString(new BigDecimal("-1"), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("0", c1.valueObjectToString(new BigDecimal("0"), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("1", c1.valueObjectToString(new BigDecimal("1"), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("1234567890123456", c1.valueObjectToString(new BigDecimal("1234567890123456"), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("1.2345678901", c1.valueObjectToString(new BigDecimal("1.234567890123456"), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("-1.2345678901", c1.valueObjectToString(new BigDecimal("-1.234567890123456"), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		
		// 日期(java.sql.Date, java.util.Date, LocalDate)
		long oriDate2 = LocalDate.parse("2012-12-21").atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("'2012-12-21 00:00:00.0'", c1.valueObjectToString(new java.sql.Date(oriDate2), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("'2012-12-21 00:00:00.0'", c1.valueObjectToString(new java.util.Date(oriDate2), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("'2012-12-21'", c1.valueObjectToString(Instant.ofEpochMilli(oriDate2)
																.atZone(ZoneOffset.ofHours(8))
																.toLocalDate(), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		
		// 时间(java.sql.Timestamp, LocalDateTime, java.util.Date)
		long oriTimestamp2 = LocalDateTime.parse("2012-12-21T05:20:17.123").atZone(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("'2012-12-21 05:20:17.123'", c1.valueObjectToString(new Timestamp(oriTimestamp2), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("'2012-12-21T05:20:17.123'", c1.valueObjectToString(Instant.ofEpochMilli(oriTimestamp2)
																.atZone(ZoneOffset.ofHours(8))
																.toLocalDateTime(), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("'2012-12-21 05:20:17.123'", c1.valueObjectToString(new java.util.Date(oriTimestamp2), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		
	}
	
	/**
	 * 这里测试 valueObjectToString 方法。非列表参数。要测试常用的数据库类型，以及常用的Java数据类型。
	 * <p>函数的作用是 把不同类型的value值，转换成一个能显示出来的字符串。</p>
	 * <p>测试涵盖（null+字符串+8个基本数据类型+8个基本引用类型+BigDecimal+日期类型+时间类型）</p>
	 */
	@Test
	void testValueObjectToString1_oracle11() throws Exception {
		
		// 定义一个 条件参数变量
		CondiParam c1 = new CondiParam();
		
		// null 值
		assertEquals("null", c1.valueObjectToString((Object)null, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertTrue(c1.valueObjectToString("aaaa", null).contains("error"));
		assertTrue(c1.valueObjectToString("aaaa", "notdb").contains("error"));
		
		// 字符值（无注入的字符串，有注入的字符串）。注意：字符串值需要在前后增加 单引号。
		assertEquals("''", c1.valueObjectToString("", DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("'null'", c1.valueObjectToString("null", DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("' null '", c1.valueObjectToString(" null ", DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("'a'", c1.valueObjectToString("a", DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("' b '", c1.valueObjectToString(" b ", DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("' b kkk ff '", c1.valueObjectToString(" b kkk ff ", DBSQLInjectionUtil.DBTYPE_ORACLE));
		// 有注入的字符串
		assertEquals("'\\ \" % '' _ = ; abcdef ` ...'", 
			c1.valueObjectToString("\\ \" % ' _ = ; abcdef ` ...", DBSQLInjectionUtil.DBTYPE_ORACLE));
		
		// 8 种基本数据类型（byte、short、int、long、float、double、char、boolean）
		assertEquals("1", c1.valueObjectToString((byte)1, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2", c1.valueObjectToString((short)2, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("123456789", c1.valueObjectToString(123456789, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("1234567890123", c1.valueObjectToString(1234567890123L, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2.0", c1.valueObjectToString(2.0f, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("3.14159265358979", c1.valueObjectToString(3.14159265358979, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("'a'", c1.valueObjectToString('a', DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("1", c1.valueObjectToString(true, DBSQLInjectionUtil.DBTYPE_ORACLE)); // 在 oracle 中 BOOLEAN 底层是 number(1)，TRUE 就是 1
		assertEquals("0", c1.valueObjectToString(false, DBSQLInjectionUtil.DBTYPE_ORACLE));
		
		// 8 种基本数据类型的引用对象（Byte、Short、Integer、Long、Float、Double、Character、Boolean）
		assertEquals("1", c1.valueObjectToString(new Byte("1"), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2", c1.valueObjectToString(new Short("2"), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("123456789", c1.valueObjectToString(new Integer("123456789"), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("1234567890123", c1.valueObjectToString(new Long(1234567890123L), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2.0", c1.valueObjectToString(new Float(2.0f), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("3.14159265358979", c1.valueObjectToString(new Double(3.14159265358979), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("'a'", c1.valueObjectToString(new Character('a'), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("1", c1.valueObjectToString(new Boolean(true), DBSQLInjectionUtil.DBTYPE_ORACLE)); // 在 oracle 中 BOOLEAN 底层是 number(1)，TRUE 就是 1
		assertEquals("0", c1.valueObjectToString(new Boolean(false), DBSQLInjectionUtil.DBTYPE_ORACLE));
		
		// 大数字 BigDecimal 类型(小数部分保留 10 位，然后四舍五入)
		assertEquals("-1", c1.valueObjectToString(new BigDecimal("-1"), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("0", c1.valueObjectToString(new BigDecimal("0"), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("1", c1.valueObjectToString(new BigDecimal("1"), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("1234567890123456", c1.valueObjectToString(new BigDecimal("1234567890123456"), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("1.2345678901", c1.valueObjectToString(new BigDecimal("1.234567890123456"), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("-1.2345678901", c1.valueObjectToString(new BigDecimal("-1.234567890123456"), DBSQLInjectionUtil.DBTYPE_ORACLE));
		
		// 日期(java.sql.Date, java.util.Date, LocalDate)
		long oriDate = LocalDate.parse("2012-12-21").atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("timestamp '2012-12-21 00:00:00.0'", c1.valueObjectToString(new java.sql.Date(oriDate), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("timestamp '2012-12-21 00:00:00.0'", c1.valueObjectToString(new java.util.Date(oriDate), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("date '2012-12-21'", c1.valueObjectToString(Instant.ofEpochMilli(oriDate)
																.atZone(ZoneOffset.ofHours(8))
																.toLocalDate(), DBSQLInjectionUtil.DBTYPE_ORACLE));
		
		// 时间(java.sql.Timestamp, LocalDateTime, java.util.Date)
		long oriTimestamp = LocalDateTime.parse("2012-12-21T05:20:17.123").atZone(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("timestamp '2012-12-21 05:20:17.123'", c1.valueObjectToString(new Timestamp(oriTimestamp), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("timestamp '2012-12-21 05:20:17.123'", c1.valueObjectToString(Instant.ofEpochMilli(oriTimestamp)
																.atZone(ZoneOffset.ofHours(8))
																.toLocalDateTime(), DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("timestamp '2012-12-21 05:20:17.123'", c1.valueObjectToString(new java.util.Date(oriTimestamp), DBSQLInjectionUtil.DBTYPE_ORACLE));
		
	}
	
	/**
	 * 这里测试 valueObjectToString 方法。非列表参数。要测试常用的数据库类型，以及常用的Java数据类型。
	 * <p>函数的作用是 把不同类型的value值，转换成一个能显示出来的字符串。</p>
	 * <p>测试涵盖（null+字符串+8个基本数据类型+8个基本引用类型+BigDecimal+日期类型+时间类型）</p>
	 */
	@Test
	void testValueObjectToString1_sqlserver2016() throws Exception {
		
		// 定义一个 条件参数变量
		CondiParam c1 = new CondiParam();
		
		// null 值
		assertEquals("null", c1.valueObjectToString((Object)null, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertTrue(c1.valueObjectToString("aaaa", null).contains("error"));
		assertTrue(c1.valueObjectToString("aaaa", "notdb").contains("error"));
		
		// 字符值（无注入的字符串，有注入的字符串）。注意：字符串值需要在前后增加 单引号。
		assertEquals("''", c1.valueObjectToString("", DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("'null'", c1.valueObjectToString("null", DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("' null '", c1.valueObjectToString(" null ", DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("'a'", c1.valueObjectToString("a", DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("' b '", c1.valueObjectToString(" b ", DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("' b kkk ff '", c1.valueObjectToString(" b kkk ff ", DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		// 有注入的字符串
		assertEquals("'\\ \" % '' _ = ; abcdef ` ...'", 
			c1.valueObjectToString("\\ \" % ' _ = ; abcdef ` ...", DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// 8 种基本数据类型（byte、short、int、long、float、double、char、boolean）
		assertEquals("1", c1.valueObjectToString((byte)1, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2", c1.valueObjectToString((short)2, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("123456789", c1.valueObjectToString(123456789, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("1234567890123", c1.valueObjectToString(1234567890123L, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2.0", c1.valueObjectToString(2.0f, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("3.14159265358979", c1.valueObjectToString(3.14159265358979, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("'a'", c1.valueObjectToString('a', DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("1", c1.valueObjectToString(true, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER)); // 在 sqlserver 中 BOOLEAN 底层是 BIT，TRUE 就是 1
		assertEquals("0", c1.valueObjectToString(false, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// 8 种基本数据类型的引用对象（Byte、Short、Integer、Long、Float、Double、Character、Boolean）
		assertEquals("1", c1.valueObjectToString(new Byte("1"), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2", c1.valueObjectToString(new Short("2"), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("123456789", c1.valueObjectToString(new Integer("123456789"), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("1234567890123", c1.valueObjectToString(new Long(1234567890123L), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2.0", c1.valueObjectToString(new Float(2.0f), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("3.14159265358979", c1.valueObjectToString(new Double(3.14159265358979), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("'a'", c1.valueObjectToString(new Character('a'), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("1", c1.valueObjectToString(new Boolean(true), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER)); // 在 sqlserver 中 BOOLEAN 底层是 BIT，TRUE 就是 1
		assertEquals("0", c1.valueObjectToString(new Boolean(false), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// 大数字 BigDecimal 类型(小数部分保留 10 位，然后四舍五入)
		assertEquals("-1", c1.valueObjectToString(new BigDecimal("-1"), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("0", c1.valueObjectToString(new BigDecimal("0"), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("1", c1.valueObjectToString(new BigDecimal("1"), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("1234567890123456", c1.valueObjectToString(new BigDecimal("1234567890123456"), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("1.2345678901", c1.valueObjectToString(new BigDecimal("1.234567890123456"), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("-1.2345678901", c1.valueObjectToString(new BigDecimal("-1.234567890123456"), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// 日期(java.sql.Date, java.util.Date, LocalDate)
		long oriDate = LocalDate.parse("2012-12-21").atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("'2012-12-21 00:00:00.0'", c1.valueObjectToString(new java.sql.Date(oriDate), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("'2012-12-21 00:00:00.0'", c1.valueObjectToString(new java.util.Date(oriDate), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("'2012-12-21'", c1.valueObjectToString(Instant.ofEpochMilli(oriDate)
																.atZone(ZoneOffset.ofHours(8))
																.toLocalDate(), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// 时间(java.sql.Timestamp, LocalDateTime, java.util.Date)
		long oriTimestamp = LocalDateTime.parse("2012-12-21T05:20:17.123").atZone(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("'2012-12-21 05:20:17.123'", c1.valueObjectToString(new Timestamp(oriTimestamp), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("'2012-12-21 05:20:17.123'", c1.valueObjectToString(Instant.ofEpochMilli(oriTimestamp)
																.atZone(ZoneOffset.ofHours(8))
																.toLocalDateTime(), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("'2012-12-21 05:20:17.123'", c1.valueObjectToString(new java.util.Date(oriTimestamp), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
	}
	
	/**
	 * 这里测试 valueObjectToString 方法。List 参数。要测试常用的数据库类型，以及常用的Java数据类型。
	 * <p>函数的作用是 把不同类型的value值，转换成一个能显示出来的字符串。</p>
	 * <p>测试涵盖（null+字符串+8个基本数据类型+8个基本引用类型+BigDecimal+日期类型+时间类型）</p>
	 * <p>这个方法 主要用于 IN 操作符的转换处理</p>
	 */
	@Test
	void testValueObjectToString_List() throws Exception {
		
		CondiParam c2 = new CondiParam();
		
		// null 参数
		assertEquals("null", c2.valueObjectToString((List<?>)null, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertTrue(c2.valueObjectToString(Arrays.asList("12", "24"), null).contains("error"));
		assertTrue(c2.valueObjectToString(Arrays.asList("12", "24"), "notdb").contains("error"));
		
		// 字符值（无注入的字符串，有注入的字符串）。注意：字符串值需要在前后增加 单引号。
		//        对于列表中的 null 会去掉，再拼接结果。
		//        对于拼接结果，'' 这种长度为0的参数，会自动过滤掉
		List<String> testList = Arrays.asList("\\", "\"", null, "%", "'", "_", null, "", " ", "=", ";", "abcdef", "`", "...");
		assertEquals("'\\\\', '\\\"', '\\%', '\\'', '\\_', ' ', '\\=', '\\;', 'abcdef', '`', '...'", c2.valueObjectToString(testList, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("'\\', '\"', '%', '''', '_', ' ', '=', ';', 'abcdef', '`', '...'", c2.valueObjectToString(testList, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("'\\', '\"', '%', '''', '_', ' ', '=', ';', 'abcdef', '`', '...'", c2.valueObjectToString(testList, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("'\\', '\"', '%', '''', '_', ' ', '=', ';', 'abcdef', '`', '...'", c2.valueObjectToString(testList, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// 8 种基本数据类型（byte、short、int、long、float、double、char、boolean）
		//      对于列表中的 null 会去掉，再拼接结果。
		//      对于拼接结果，'' 这种长度为0的参数，会自动过滤掉
		List<Object> testList2 = Arrays.asList((byte)1, (short)2, 123456789, 1234567890123L, null, null, 2.0f, 3.14159265358979, 'a', true, false);
		assertEquals("1, 2, 123456789, 1234567890123, 2.0, 3.14159265358979, 'a', 1, 0", c2.valueObjectToString(testList2, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("1, 2, 123456789, 1234567890123, 2.0, 3.14159265358979, 'a', 1, 0", c2.valueObjectToString(testList2, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("1, 2, 123456789, 1234567890123, 2.0, 3.14159265358979, 'a', 1, 0", c2.valueObjectToString(testList2, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("1, 2, 123456789, 1234567890123, 2.0, 3.14159265358979, 'a', 1, 0", c2.valueObjectToString(testList2, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// 8 种基本数据类型的引用对象（Byte、Short、Integer、Long、Float、Double、Character、Boolean）
		//      对于列表中的 null 会去掉，再拼接结果。
		//      对于拼接结果，'' 这种长度为0的参数，会自动过滤掉
		List<Object> testList3 = Arrays.asList(new Byte("1"), new Short("2"), new Integer("123456789"), new Long(1234567890123L), 
			null, null, new Float(2.0f), new Double(3.14159265358979), new Character('a'), new Boolean(true), new Boolean(false));
		assertEquals("1, 2, 123456789, 1234567890123, 2.0, 3.14159265358979, 'a', 1, 0", c2.valueObjectToString(testList3, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("1, 2, 123456789, 1234567890123, 2.0, 3.14159265358979, 'a', 1, 0", c2.valueObjectToString(testList3, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("1, 2, 123456789, 1234567890123, 2.0, 3.14159265358979, 'a', 1, 0", c2.valueObjectToString(testList3, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("1, 2, 123456789, 1234567890123, 2.0, 3.14159265358979, 'a', 1, 0", c2.valueObjectToString(testList3, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// 大数字 BigDecimal 类型(小数部分保留 10 位，然后四舍五入)
		//      对于列表中的 null 会去掉，再拼接结果。
		//      对于拼接结果，'' 这种长度为0的参数，会自动过滤掉
		List<Object> testList4 = Arrays.asList(new BigDecimal("-1"), null, null, new BigDecimal("0"), new BigDecimal("1"),
			new BigDecimal("1234567890123456"), new BigDecimal("1.234567890123456"), new BigDecimal("-1.234567890123456"));
		assertEquals("-1, 0, 1, 1234567890123456, 1.2345678901, -1.2345678901", c2.valueObjectToString(testList4, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("-1, 0, 1, 1234567890123456, 1.2345678901, -1.2345678901", c2.valueObjectToString(testList4, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("-1, 0, 1, 1234567890123456, 1.2345678901, -1.2345678901", c2.valueObjectToString(testList4, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("-1, 0, 1, 1234567890123456, 1.2345678901, -1.2345678901", c2.valueObjectToString(testList4, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// 日期(java.sql.Date, java.util.Date, LocalDate)
		//      对于列表中的 null 会去掉，再拼接结果。
		//      对于拼接结果，'' 这种长度为0的参数，会自动过滤掉
		long oriDate = LocalDate.parse("2012-12-21").atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		List<Object> testList5 = Arrays.asList(null, null, 
			new java.sql.Date(oriDate), new java.util.Date(oriDate), Instant.ofEpochMilli(oriDate).atZone(ZoneOffset.ofHours(8)).toLocalDate());
		assertEquals("'2012-12-21 00:00:00.0', '2012-12-21 00:00:00.0', '2012-12-21'", c2.valueObjectToString(testList5, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("'2012-12-21 00:00:00.0', '2012-12-21 00:00:00.0', '2012-12-21'", c2.valueObjectToString(testList5, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("timestamp '2012-12-21 00:00:00.0', timestamp '2012-12-21 00:00:00.0', date '2012-12-21'", c2.valueObjectToString(testList5, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("'2012-12-21 00:00:00.0', '2012-12-21 00:00:00.0', '2012-12-21'", c2.valueObjectToString(testList5, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// 时间(java.sql.Timestamp, LocalDateTime, java.util.Date)
		//      对于列表中的 null 会去掉，再拼接结果。
		//      对于拼接结果，'' 这种长度为0的参数，会自动过滤掉
		long oriTimestamp = LocalDateTime.parse("2012-12-21T05:20:17.123").atZone(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		List<Object> testList6 = Arrays.asList(null, null, 
			new Timestamp(oriTimestamp), Instant.ofEpochMilli(oriTimestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime(), 
			new java.util.Date(oriTimestamp));
		assertEquals("'2012-12-21 05:20:17.123', '2012-12-21T05:20:17.123', '2012-12-21 05:20:17.123'", c2.valueObjectToString(testList6, DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("'2012-12-21 05:20:17.123', '2012-12-21T05:20:17.123', '2012-12-21 05:20:17.123'", c2.valueObjectToString(testList6, DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("timestamp '2012-12-21 05:20:17.123', timestamp '2012-12-21 05:20:17.123', timestamp '2012-12-21 05:20:17.123'", c2.valueObjectToString(testList6, DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("'2012-12-21 05:20:17.123', '2012-12-21 05:20:17.123', '2012-12-21 05:20:17.123'", c2.valueObjectToString(testList6, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
	}
	
	/**
	 * 这里测试 toSqlString 方法。普通操作符。要注意 SQL 注入 问题。
	 */
	@Test
	void testToSqlString_mysql8() throws Exception {
		
		// ================= 异常值，测试 ( String paramName, Object value, String optString, String alias, Class<?> className )
		// 
		CondiParam cErr1 = new CondiParam(null,     "bbb", CondiParam.OPT_EQ, "2k", Student.class); //paramName
		CondiParam cErr2 = new CondiParam("",       "bbb", CondiParam.OPT_EQ, "2k", Student.class);
		CondiParam cErr3 = new CondiParam(" ",      "bbb", CondiParam.OPT_EQ, "2k", Student.class);
		CondiParam cErr4 = new CondiParam(" stuNo  ", "bbb", CondiParam.OPT_EQ, "2k", Student.class);
		CondiParam cErr5 = new CondiParam("stuNo",  null, CondiParam.OPT_EQ, "2k", Student.class);    //value 
		CondiParam cErr6 = new CondiParam("stuNo",  "bbb", null, "2k", Student.class);                //optString
		CondiParam cErr7 = new CondiParam("stuNo",  "bbb", "", "2k", Student.class);
		CondiParam cErr8 = new CondiParam("stuNo",  "bbb", " ", "2k", Student.class);
		CondiParam cErr9 = new CondiParam("stuNo",  "bbb", " notopt ", "2k", Student.class);
		CondiParam cErr10 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, null, Student.class);  //alias
		CondiParam cErr11 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, "", Student.class);
		CondiParam cErr12 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " ", Student.class);
		CondiParam cErr13 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " 2k ", Student.class);
		CondiParam cErr14 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " 2k ", null);         //className
		CondiParam cErr15 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " 2k ", this.getClass());
		// 异常对象的 toSqlString 结果
		assertEquals("2k. = 'bbb'", cErr1.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2k. = 'bbb'", cErr2.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2k. = 'bbb'", cErr3.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2k.stu_no = 'bbb'", cErr4.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2k.stu_no = 'null'", cErr5.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2k.stu_no  'bbb'", cErr6.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2k.stu_no  'bbb'", cErr7.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2k.stu_no  'bbb'", cErr8.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2k.stu_no  'bbb'", cErr9.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("stu_no = 'bbb'", cErr10.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("stu_no = 'bbb'", cErr11.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("stu_no = 'bbb'", cErr12.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2k.stu_no = 'bbb'", cErr13.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2k.stuNo = 'bbb'", cErr14.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("2k.stuNo = 'bbb'", cErr15.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		// toString 异常的话，产生的结果
		assertTrue(cErr15.toSqlString(null).contains("error"));
		assertTrue(cErr15.toSqlString("aaaa").contains("error"));
		
		// ================= 首先，测试一遍 OPT 运算符 是否能正常拼接
		CondiParam c1 = new CondiParam("aaa", "bbb", CondiParam.OPT_EQ);
		CondiParam c2 = new CondiParam("aaa", "bbb", CondiParam.OPT_GT);
		CondiParam c3 = new CondiParam("aaa", "bbb", CondiParam.OPT_GTE);
		CondiParam c4 = new CondiParam("aaa", "bbb", CondiParam.OPT_LT);
		CondiParam c5 = new CondiParam("aaa", "bbb", CondiParam.OPT_LTE);
		CondiParam c6 = new CondiParam("aaa", "bbb", CondiParam.OPT_NOT_EQ);
		CondiParam c7 = new CondiParam("aaa", "bbb", CondiParam.OPT_LIKE);
		CondiParam c8 = new CondiParam("aaa", "bbb", CondiParam.OPT_LIKE_PRE);
		CondiParam c9 = new CondiParam("aaa", "bbb", CondiParam.OPT_LIKE_SUF);
		CondiParam c10 = new CondiParam("aaa", "bbb", CondiParam.OPT_IS_NULL);
		CondiParam c11 = new CondiParam("aaa", "bbb", CondiParam.OPT_IS_NOT_NULL);
		CondiParam c12 = new CondiParam("aaa", "bbb", CondiParam.OPT_REGEXP);
		CondiParam c13 = new CondiParam("aaa", "bbb", CondiParam.OPT_IN);
		CondiParam c14 = new CondiParam("aaa", Arrays.asList(1, "2", 3.0, 4L, LocalDate.parse("2012-12-21")), CondiParam.OPT_IN);
		// 
		assertEquals("aaa = 'bbb'", c1.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa > 'bbb'", c2.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa >= 'bbb'", c3.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa < 'bbb'", c4.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa <= 'bbb'", c5.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa <> 'bbb'", c6.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa like '%bbb%'", c7.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa like 'bbb%'", c8.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa like '%bbb'", c9.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa is null", c10.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa is not null", c11.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("regexp_like(aaa, 'bbb')", c12.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa in ('bbb')", c13.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa in (1, '2', 3.0, 4, '2012-12-21')", c14.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		
		// ================= 然后，测试 各类型的值 能否正常 拼接。
		//                   字符处理
		//                   8 种基本数据类型（byte、short、int、long、float、double、char、boolean）
		//                   8 种基本数据类型的引用对象（Byte、Short、Integer、Long、Float、Double、Char、Boolean）
		//                   大数字 BigDecimal 类型(小数部分保留 10 位，然后四舍五入)
		//                   日期(java.sql.Date, java.util.Date, LocalDate)
		//                   时间(java.sql.Timestamp, LocalDateTime, java.util.Date)
		CondiParam cT1 = new CondiParam("aaa", null, CondiParam.OPT_EQ);
		assertEquals("aaa = 'null'", cT1.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		// 这里，只是普通的相等操作。
		assertEquals("aaa = ''", cT1.setValue("").toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = ' '", cT1.setValue(" ").toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 'null'", cT1.setValue("null").toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = ' my ok . '", cT1.setValue(" my ok . ").toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 1", cT1.setValue((byte)1).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 2", cT1.setValue((short)2).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 123456789", cT1.setValue(123456789).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 1234567890123", cT1.setValue(1234567890123L).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 2.0", cT1.setValue(2.0f).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 3.14159265358979", cT1.setValue(3.14159265358979).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 'a'", cT1.setValue('a').toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 1", cT1.setValue(true).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 0", cT1.setValue(false).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 1", cT1.setValue(new Byte("1")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 2", cT1.setValue(new Short("2")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 123456789", cT1.setValue(new Integer("123456789")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 1234567890123", cT1.setValue(new Long(1234567890123L)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 2.0", cT1.setValue(new Float(2.0f)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 3.14159265358979", cT1.setValue(new Double(3.14159265358979)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 'a'", cT1.setValue(new Character('a')).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 1", cT1.setValue(new Boolean(true)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 0", cT1.setValue(new Boolean(false)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = -1", cT1.setValue(new BigDecimal("-1")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 0", cT1.setValue(new BigDecimal("0")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 1", cT1.setValue(new BigDecimal("1")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 1234567890123456", cT1.setValue(new BigDecimal("1234567890123456")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = 1.2345678901", cT1.setValue(new BigDecimal("1.234567890123456")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = -1.2345678901", cT1.setValue(new BigDecimal("-1.234567890123456")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		long oriDate = LocalDate.parse("2012-12-21").atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("aaa = '2012-12-21 00:00:00.0'", cT1.setValue(new java.sql.Date(oriDate)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = '2012-12-21 00:00:00.0'", cT1.setValue(new java.util.Date(oriDate)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = '2012-12-21'", cT1.setValue(Instant.ofEpochMilli(oriDate)
											.atZone(ZoneOffset.ofHours(8))
											.toLocalDate()).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		long oriTimestamp = LocalDateTime.parse("2012-12-21T05:20:17.123").atZone(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("aaa = '2012-12-21 05:20:17.123'", cT1.setValue(new Timestamp(oriTimestamp)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = '2012-12-21 05:20:17.123'", cT1.setValue(new java.util.Date(oriTimestamp)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("aaa = '2012-12-21T05:20:17.123'", cT1.setValue(Instant.ofEpochMilli(oriTimestamp)
											.atZone(ZoneOffset.ofHours(8))
											.toLocalDateTime()).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		
		// ================= 最后，测试 实体class 的属性翻译
		CondiParam cT2 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t", null);
		CondiParam cT3 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t2", Student.class);
		CondiParam cT4 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t3", VStudent.class);
		CondiParam cT5 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t4", VOStudent.class);
		//
		assertEquals("t.stuNo = 'S0001'", cT2.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("t2.stu_no = 'S0001'", cT3.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("t3.stu_no = 'S0001'", cT4.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("t4.stu_no = 'S0001'", cT5.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		
		// ================= 简单的 SQL 注入测试
		CondiParam cS1 = new CondiParam("stuNo1", "\\ \" % ' _ = ; abcdef ` ...", CondiParam.OPT_EQ, "t", Student.class);
		CondiParam cS2 = new CondiParam("stuNo2", "%h%", CondiParam.OPT_EQ, "t", Student.class);
		CondiParam cS3 = new CondiParam("stuNo3", "%' or 1=1 ; -- '%", CondiParam.OPT_EQ, "t", Student.class);
		// 
		assertEquals("t.stuNo1 = '\\\\ \\\" \\% \\' \\_ \\= \\; abcdef ` ...'", cS1.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("t.stuNo2 = '\\%h\\%'", cS2.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		assertEquals("t.stuNo3 = '\\%\\' or 1\\=1 \\; -- \\'\\%'", cS3.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
	}
	
	/**
	 * 这里测试 toSqlString 方法。普通操作符。要注意 SQL 注入 问题。
	 */
	@Test
	void testToSqlString_mysql8_ansi() throws Exception {
		
		// ================= 异常值，测试 ( String paramName, Object value, String optString, String alias, Class<?> className )
		// 
		CondiParam cErr1 = new CondiParam(null,     "bbb", CondiParam.OPT_EQ, "2k", Student.class); //paramName
		CondiParam cErr2 = new CondiParam("",       "bbb", CondiParam.OPT_EQ, "2k", Student.class);
		CondiParam cErr3 = new CondiParam(" ",      "bbb", CondiParam.OPT_EQ, "2k", Student.class);
		CondiParam cErr4 = new CondiParam(" stuNo  ", "bbb", CondiParam.OPT_EQ, "2k", Student.class);
		CondiParam cErr5 = new CondiParam("stuNo",  null, CondiParam.OPT_EQ, "2k", Student.class);    //value 
		CondiParam cErr6 = new CondiParam("stuNo",  "bbb", null, "2k", Student.class);                //optString
		CondiParam cErr7 = new CondiParam("stuNo",  "bbb", "", "2k", Student.class);
		CondiParam cErr8 = new CondiParam("stuNo",  "bbb", " ", "2k", Student.class);
		CondiParam cErr9 = new CondiParam("stuNo",  "bbb", " notopt ", "2k", Student.class);
		CondiParam cErr10 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, null, Student.class);  //alias
		CondiParam cErr11 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, "", Student.class);
		CondiParam cErr12 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " ", Student.class);
		CondiParam cErr13 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " 2k ", Student.class);
		CondiParam cErr14 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " 2k ", null);         //className
		CondiParam cErr15 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " 2k ", this.getClass());
		// 异常对象的 toSqlString 结果
		assertEquals("2k. = 'bbb'", cErr1.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2k. = 'bbb'", cErr2.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2k. = 'bbb'", cErr3.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2k.stu_no = 'bbb'", cErr4.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2k.stu_no = 'null'", cErr5.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2k.stu_no  'bbb'", cErr6.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2k.stu_no  'bbb'", cErr7.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2k.stu_no  'bbb'", cErr8.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2k.stu_no  'bbb'", cErr9.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("stu_no = 'bbb'", cErr10.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("stu_no = 'bbb'", cErr11.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("stu_no = 'bbb'", cErr12.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2k.stu_no = 'bbb'", cErr13.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2k.stuNo = 'bbb'", cErr14.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("2k.stuNo = 'bbb'", cErr15.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		// toString 异常的话，产生的结果
		assertTrue(cErr15.toSqlString(null).contains("error"));
		assertTrue(cErr15.toSqlString("aaaa").contains("error"));
		
		// ================= 首先，测试一遍 OPT 运算符 是否能正常拼接
		CondiParam c1 = new CondiParam("aaa", "bbb", CondiParam.OPT_EQ);
		CondiParam c2 = new CondiParam("aaa", "bbb", CondiParam.OPT_GT);
		CondiParam c3 = new CondiParam("aaa", "bbb", CondiParam.OPT_GTE);
		CondiParam c4 = new CondiParam("aaa", "bbb", CondiParam.OPT_LT);
		CondiParam c5 = new CondiParam("aaa", "bbb", CondiParam.OPT_LTE);
		CondiParam c6 = new CondiParam("aaa", "bbb", CondiParam.OPT_NOT_EQ);
		CondiParam c7 = new CondiParam("aaa", "bbb", CondiParam.OPT_LIKE);
		CondiParam c8 = new CondiParam("aaa", "bbb", CondiParam.OPT_LIKE_PRE);
		CondiParam c9 = new CondiParam("aaa", "bbb", CondiParam.OPT_LIKE_SUF);
		CondiParam c10 = new CondiParam("aaa", "bbb", CondiParam.OPT_IS_NULL);
		CondiParam c11 = new CondiParam("aaa", "bbb", CondiParam.OPT_IS_NOT_NULL);
		CondiParam c12 = new CondiParam("aaa", "bbb", CondiParam.OPT_REGEXP);
		CondiParam c13 = new CondiParam("aaa", "bbb", CondiParam.OPT_IN);
		CondiParam c14 = new CondiParam("aaa", Arrays.asList(1, "2", 3.0, 4L, LocalDate.parse("2012-12-21")), CondiParam.OPT_IN);
		// 
		assertEquals("aaa = 'bbb'", c1.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa > 'bbb'", c2.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa >= 'bbb'", c3.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa < 'bbb'", c4.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa <= 'bbb'", c5.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa <> 'bbb'", c6.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa like '%bbb%'", c7.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa like 'bbb%'", c8.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa like '%bbb'", c9.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa is null", c10.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa is not null", c11.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("regexp_like(aaa, 'bbb')", c12.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa in ('bbb')", c13.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa in (1, '2', 3.0, 4, '2012-12-21')", c14.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		
		// ================= 然后，测试 各类型的值 能否正常 拼接。
		//                   字符处理
		//                   8 种基本数据类型（byte、short、int、long、float、double、char、boolean）
		//                   8 种基本数据类型的引用对象（Byte、Short、Integer、Long、Float、Double、Char、Boolean）
		//                   大数字 BigDecimal 类型(小数部分保留 10 位，然后四舍五入)
		//                   日期(java.sql.Date, java.util.Date, LocalDate)
		//                   时间(java.sql.Timestamp, LocalDateTime, java.util.Date)
		CondiParam cT1 = new CondiParam("aaa", null, CondiParam.OPT_EQ);
		assertEquals("aaa = 'null'", cT1.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		// 这里，只是普通的相等操作。
		assertEquals("aaa = ''", cT1.setValue("").toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = ' '", cT1.setValue(" ").toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 'null'", cT1.setValue("null").toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = ' my ok . '", cT1.setValue(" my ok . ").toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 1", cT1.setValue((byte)1).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 2", cT1.setValue((short)2).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 123456789", cT1.setValue(123456789).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 1234567890123", cT1.setValue(1234567890123L).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 2.0", cT1.setValue(2.0f).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 3.14159265358979", cT1.setValue(3.14159265358979).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 'a'", cT1.setValue('a').toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 1", cT1.setValue(true).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 0", cT1.setValue(false).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 1", cT1.setValue(new Byte("1")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 2", cT1.setValue(new Short("2")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 123456789", cT1.setValue(new Integer("123456789")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 1234567890123", cT1.setValue(new Long(1234567890123L)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 2.0", cT1.setValue(new Float(2.0f)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 3.14159265358979", cT1.setValue(new Double(3.14159265358979)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 'a'", cT1.setValue(new Character('a')).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 1", cT1.setValue(new Boolean(true)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 0", cT1.setValue(new Boolean(false)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = -1", cT1.setValue(new BigDecimal("-1")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 0", cT1.setValue(new BigDecimal("0")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 1", cT1.setValue(new BigDecimal("1")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 1234567890123456", cT1.setValue(new BigDecimal("1234567890123456")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = 1.2345678901", cT1.setValue(new BigDecimal("1.234567890123456")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = -1.2345678901", cT1.setValue(new BigDecimal("-1.234567890123456")).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		long oriDate = LocalDate.parse("2012-12-21").atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("aaa = '2012-12-21 00:00:00.0'", cT1.setValue(new java.sql.Date(oriDate)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = '2012-12-21 00:00:00.0'", cT1.setValue(new java.util.Date(oriDate)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = '2012-12-21'", cT1.setValue(Instant.ofEpochMilli(oriDate)
											.atZone(ZoneOffset.ofHours(8))
											.toLocalDate()).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		long oriTimestamp = LocalDateTime.parse("2012-12-21T05:20:17.123").atZone(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("aaa = '2012-12-21 05:20:17.123'", cT1.setValue(new Timestamp(oriTimestamp)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = '2012-12-21 05:20:17.123'", cT1.setValue(new java.util.Date(oriTimestamp)).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("aaa = '2012-12-21T05:20:17.123'", cT1.setValue(Instant.ofEpochMilli(oriTimestamp)
											.atZone(ZoneOffset.ofHours(8))
											.toLocalDateTime()).toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		
		// ================= 最后，测试 实体class 的属性翻译
		CondiParam cT2 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t", null);
		CondiParam cT3 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t2", Student.class);
		CondiParam cT4 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t3", VStudent.class);
		CondiParam cT5 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t4", VOStudent.class);
		//
		assertEquals("t.stuNo = 'S0001'", cT2.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("t2.stu_no = 'S0001'", cT3.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("t3.stu_no = 'S0001'", cT4.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("t4.stu_no = 'S0001'", cT5.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		
		// ================= 简单的 SQL 注入测试
		CondiParam cS1 = new CondiParam("stuNo1", "\\ \" % ' _ = ; abcdef ` ...", CondiParam.OPT_EQ, "t", Student.class);
		CondiParam cS2 = new CondiParam("stuNo2", "%h%", CondiParam.OPT_EQ, "t", Student.class);
		CondiParam cS3 = new CondiParam("stuNo3", "%' or 1=1 ; -- '%", CondiParam.OPT_EQ, "t", Student.class);
		// 
		assertEquals("t.stuNo1 = '\\ \" % '' _ = ; abcdef ` ...'", cS1.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("t.stuNo2 = '%h%'", cS2.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
		assertEquals("t.stuNo3 = '%'' or 1=1 ; -- ''%'", cS3.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI));
	}
	
	/**
	 * 这里测试 toSqlString 方法。普通操作符。要注意 SQL 注入 问题。
	 */
	@Test
	void testToSqlString_oracle11() throws Exception {
		
		// ================= 异常值，测试 ( String paramName, Object value, String optString, String alias, Class<?> className )
		// 
		CondiParam cErr1 = new CondiParam(null,     "bbb", CondiParam.OPT_EQ, "2k", Student.class); //paramName
		CondiParam cErr2 = new CondiParam("",       "bbb", CondiParam.OPT_EQ, "2k", Student.class);
		CondiParam cErr3 = new CondiParam(" ",      "bbb", CondiParam.OPT_EQ, "2k", Student.class);
		CondiParam cErr4 = new CondiParam(" stuNo  ", "bbb", CondiParam.OPT_EQ, "2k", Student.class);
		CondiParam cErr5 = new CondiParam("stuNo",  null, CondiParam.OPT_EQ, "2k", Student.class);    //value 
		CondiParam cErr6 = new CondiParam("stuNo",  "bbb", null, "2k", Student.class);                //optString
		CondiParam cErr7 = new CondiParam("stuNo",  "bbb", "", "2k", Student.class);
		CondiParam cErr8 = new CondiParam("stuNo",  "bbb", " ", "2k", Student.class);
		CondiParam cErr9 = new CondiParam("stuNo",  "bbb", " notopt ", "2k", Student.class);
		CondiParam cErr10 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, null, Student.class);  //alias
		CondiParam cErr11 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, "", Student.class);
		CondiParam cErr12 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " ", Student.class);
		CondiParam cErr13 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " 2k ", Student.class);
		CondiParam cErr14 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " 2k ", null);         //className
		CondiParam cErr15 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " 2k ", this.getClass());
		// 异常对象的 toSqlString 结果
		assertEquals("2k. = 'bbb'", cErr1.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2k. = 'bbb'", cErr2.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2k. = 'bbb'", cErr3.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2k.stu_no = 'bbb'", cErr4.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2k.stu_no = 'null'", cErr5.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2k.stu_no  'bbb'", cErr6.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2k.stu_no  'bbb'", cErr7.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2k.stu_no  'bbb'", cErr8.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2k.stu_no  'bbb'", cErr9.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("stu_no = 'bbb'", cErr10.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("stu_no = 'bbb'", cErr11.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("stu_no = 'bbb'", cErr12.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2k.stu_no = 'bbb'", cErr13.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2k.stuNo = 'bbb'", cErr14.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("2k.stuNo = 'bbb'", cErr15.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		// toString 异常的话，产生的结果
		assertTrue(cErr15.toSqlString(null).contains("error"));
		assertTrue(cErr15.toSqlString("aaaa").contains("error"));
		
		// ================= 首先，测试一遍 OPT 运算符 是否能正常拼接
		CondiParam c1 = new CondiParam("aaa", "bbb", CondiParam.OPT_EQ);
		CondiParam c2 = new CondiParam("aaa", "bbb", CondiParam.OPT_GT);
		CondiParam c3 = new CondiParam("aaa", "bbb", CondiParam.OPT_GTE);
		CondiParam c4 = new CondiParam("aaa", "bbb", CondiParam.OPT_LT);
		CondiParam c5 = new CondiParam("aaa", "bbb", CondiParam.OPT_LTE);
		CondiParam c6 = new CondiParam("aaa", "bbb", CondiParam.OPT_NOT_EQ);
		CondiParam c7 = new CondiParam("aaa", "bbb", CondiParam.OPT_LIKE);
		CondiParam c8 = new CondiParam("aaa", "bbb", CondiParam.OPT_LIKE_PRE);
		CondiParam c9 = new CondiParam("aaa", "bbb", CondiParam.OPT_LIKE_SUF);
		CondiParam c10 = new CondiParam("aaa", "bbb", CondiParam.OPT_IS_NULL);
		CondiParam c11 = new CondiParam("aaa", "bbb", CondiParam.OPT_IS_NOT_NULL);
		CondiParam c12 = new CondiParam("aaa", "bbb", CondiParam.OPT_REGEXP);
		CondiParam c13 = new CondiParam("aaa", "bbb", CondiParam.OPT_IN);
		CondiParam c14 = new CondiParam("aaa", Arrays.asList(1, "2", 3.0, 4L, LocalDate.parse("2012-12-21")), CondiParam.OPT_IN);
		// 
		assertEquals("aaa = 'bbb'", c1.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa > 'bbb'", c2.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa >= 'bbb'", c3.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa < 'bbb'", c4.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa <= 'bbb'", c5.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa <> 'bbb'", c6.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa like '%bbb%'", c7.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa like 'bbb%'", c8.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa like '%bbb'", c9.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa is null", c10.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa is not null", c11.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("regexp_like(aaa, 'bbb')", c12.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa in ('bbb')", c13.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa in (1, '2', 3.0, 4, date '2012-12-21')", c14.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		
		// ================= 然后，测试 各类型的值 能否正常 拼接。
		//                   字符处理
		//                   8 种基本数据类型（byte、short、int、long、float、double、char、boolean）
		//                   8 种基本数据类型的引用对象（Byte、Short、Integer、Long、Float、Double、Char、Boolean）
		//                   大数字 BigDecimal 类型(小数部分保留 10 位，然后四舍五入)
		//                   日期(java.sql.Date, java.util.Date, LocalDate)
		//                   时间(java.sql.Timestamp, LocalDateTime, java.util.Date)
		CondiParam cT1 = new CondiParam("aaa", null, CondiParam.OPT_EQ);
		assertEquals("aaa = 'null'", cT1.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		// 这里，只是普通的相等操作。
		assertEquals("aaa = ''", cT1.setValue("").toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = ' '", cT1.setValue(" ").toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 'null'", cT1.setValue("null").toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = ' my ok . '", cT1.setValue(" my ok . ").toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 1", cT1.setValue((byte)1).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 2", cT1.setValue((short)2).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 123456789", cT1.setValue(123456789).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 1234567890123", cT1.setValue(1234567890123L).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 2.0", cT1.setValue(2.0f).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 3.14159265358979", cT1.setValue(3.14159265358979).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 'a'", cT1.setValue('a').toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 1", cT1.setValue(true).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 0", cT1.setValue(false).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 1", cT1.setValue(new Byte("1")).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 2", cT1.setValue(new Short("2")).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 123456789", cT1.setValue(new Integer("123456789")).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 1234567890123", cT1.setValue(new Long(1234567890123L)).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 2.0", cT1.setValue(new Float(2.0f)).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 3.14159265358979", cT1.setValue(new Double(3.14159265358979)).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 'a'", cT1.setValue(new Character('a')).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 1", cT1.setValue(new Boolean(true)).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 0", cT1.setValue(new Boolean(false)).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = -1", cT1.setValue(new BigDecimal("-1")).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 0", cT1.setValue(new BigDecimal("0")).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 1", cT1.setValue(new BigDecimal("1")).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 1234567890123456", cT1.setValue(new BigDecimal("1234567890123456")).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = 1.2345678901", cT1.setValue(new BigDecimal("1.234567890123456")).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = -1.2345678901", cT1.setValue(new BigDecimal("-1.234567890123456")).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		long oriDate = LocalDate.parse("2012-12-21").atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("aaa = timestamp '2012-12-21 00:00:00.0'", cT1.setValue(new java.sql.Date(oriDate)).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = timestamp '2012-12-21 00:00:00.0'", cT1.setValue(new java.util.Date(oriDate)).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = date '2012-12-21'", cT1.setValue(Instant.ofEpochMilli(oriDate)
											.atZone(ZoneOffset.ofHours(8))
											.toLocalDate()).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		long oriTimestamp = LocalDateTime.parse("2012-12-21T05:20:17.123").atZone(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("aaa = timestamp '2012-12-21 05:20:17.123'", cT1.setValue(new Timestamp(oriTimestamp)).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = timestamp '2012-12-21 05:20:17.123'", cT1.setValue(new java.util.Date(oriTimestamp)).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("aaa = timestamp '2012-12-21 05:20:17.123'", cT1.setValue(Instant.ofEpochMilli(oriTimestamp)
											.atZone(ZoneOffset.ofHours(8))
											.toLocalDateTime()).toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		
		// ================= 最后，测试 实体class 的属性翻译
		CondiParam cT2 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t", null);
		CondiParam cT3 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t2", Student.class);
		CondiParam cT4 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t3", VStudent.class);
		CondiParam cT5 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t4", VOStudent.class);
		//
		assertEquals("t.stuNo = 'S0001'", cT2.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("t2.stu_no = 'S0001'", cT3.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("t3.stu_no = 'S0001'", cT4.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("t4.stu_no = 'S0001'", cT5.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		
		// ================= 简单的 SQL 注入测试
		CondiParam cS1 = new CondiParam("stuNo1", "\\ \" % ' _ = ; abcdef ` ...", CondiParam.OPT_EQ, "t", Student.class);
		CondiParam cS2 = new CondiParam("stuNo2", "%h%", CondiParam.OPT_EQ, "t", Student.class);
		CondiParam cS3 = new CondiParam("stuNo3", "%' or 1=1 ; -- '%", CondiParam.OPT_EQ, "t", Student.class);
		// 
		assertEquals("t.stuNo1 = '\\ \" % '' _ = ; abcdef ` ...'", cS1.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("t.stuNo2 = '%h%'", cS2.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		assertEquals("t.stuNo3 = '%'' or 1=1 ; -- ''%'", cS3.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		
	}
	
	/**
	 * 这里测试 toSqlString 方法。普通操作符。要注意 SQL 注入 问题。
	 */
	@Test
	void testToSqlString_sqlserver2016() throws Exception {
		
		// ================= 异常值，测试 ( String paramName, Object value, String optString, String alias, Class<?> className )
		// 
		CondiParam cErr1 = new CondiParam(null,     "bbb", CondiParam.OPT_EQ, "2k", Student.class); //paramName
		CondiParam cErr2 = new CondiParam("",       "bbb", CondiParam.OPT_EQ, "2k", Student.class);
		CondiParam cErr3 = new CondiParam(" ",      "bbb", CondiParam.OPT_EQ, "2k", Student.class);
		CondiParam cErr4 = new CondiParam(" stuNo  ", "bbb", CondiParam.OPT_EQ, "2k", Student.class);
		CondiParam cErr5 = new CondiParam("stuNo",  null, CondiParam.OPT_EQ, "2k", Student.class);    //value 
		CondiParam cErr6 = new CondiParam("stuNo",  "bbb", null, "2k", Student.class);                //optString
		CondiParam cErr7 = new CondiParam("stuNo",  "bbb", "", "2k", Student.class);
		CondiParam cErr8 = new CondiParam("stuNo",  "bbb", " ", "2k", Student.class);
		CondiParam cErr9 = new CondiParam("stuNo",  "bbb", " notopt ", "2k", Student.class);
		CondiParam cErr10 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, null, Student.class);  //alias
		CondiParam cErr11 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, "", Student.class);
		CondiParam cErr12 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " ", Student.class);
		CondiParam cErr13 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " 2k ", Student.class);
		CondiParam cErr14 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " 2k ", null);         //className
		CondiParam cErr15 = new CondiParam("stuNo",  "bbb", CondiParam.OPT_EQ, " 2k ", this.getClass());
		// 异常对象的 toSqlString 结果
		assertEquals("2k. = 'bbb'", cErr1.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2k. = 'bbb'", cErr2.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2k. = 'bbb'", cErr3.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2k.stu_no = 'bbb'", cErr4.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2k.stu_no = 'null'", cErr5.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2k.stu_no  'bbb'", cErr6.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2k.stu_no  'bbb'", cErr7.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2k.stu_no  'bbb'", cErr8.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2k.stu_no  'bbb'", cErr9.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("stu_no = 'bbb'", cErr10.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("stu_no = 'bbb'", cErr11.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("stu_no = 'bbb'", cErr12.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2k.stu_no = 'bbb'", cErr13.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2k.stuNo = 'bbb'", cErr14.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("2k.stuNo = 'bbb'", cErr15.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		// toString 异常的话，产生的结果
		assertTrue(cErr15.toSqlString(null).contains("error"));
		assertTrue(cErr15.toSqlString("aaaa").contains("error"));
		
		// ================= 首先，测试一遍 OPT 运算符 是否能正常拼接
		CondiParam c1 = new CondiParam("aaa", "bbb", CondiParam.OPT_EQ);
		CondiParam c2 = new CondiParam("aaa", "bbb", CondiParam.OPT_GT);
		CondiParam c3 = new CondiParam("aaa", "bbb", CondiParam.OPT_GTE);
		CondiParam c4 = new CondiParam("aaa", "bbb", CondiParam.OPT_LT);
		CondiParam c5 = new CondiParam("aaa", "bbb", CondiParam.OPT_LTE);
		CondiParam c6 = new CondiParam("aaa", "bbb", CondiParam.OPT_NOT_EQ);
		CondiParam c7 = new CondiParam("aaa", "bbb", CondiParam.OPT_LIKE);
		CondiParam c8 = new CondiParam("aaa", "bbb", CondiParam.OPT_LIKE_PRE);
		CondiParam c9 = new CondiParam("aaa", "bbb", CondiParam.OPT_LIKE_SUF);
		CondiParam c10 = new CondiParam("aaa", "bbb", CondiParam.OPT_IS_NULL);
		CondiParam c11 = new CondiParam("aaa", "bbb", CondiParam.OPT_IS_NOT_NULL);
		CondiParam c12 = new CondiParam("aaa", "bbb", CondiParam.OPT_REGEXP);
		CondiParam c13 = new CondiParam("aaa", "bbb", CondiParam.OPT_IN);
		CondiParam c14 = new CondiParam("aaa", Arrays.asList(1, "2", 3.0, 4L, LocalDate.parse("2012-12-21")), CondiParam.OPT_IN);
		// 
		assertEquals("aaa = 'bbb'", c1.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa > 'bbb'", c2.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa >= 'bbb'", c3.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa < 'bbb'", c4.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa <= 'bbb'", c5.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa <> 'bbb'", c6.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa like '%bbb%'", c7.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa like 'bbb%'", c8.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa like '%bbb'", c9.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa is null", c10.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa is not null", c11.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("regexp_like(aaa, 'bbb')", c12.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa in ('bbb')", c13.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa in (1, '2', 3.0, 4, '2012-12-21')", c14.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// ================= 然后，测试 各类型的值 能否正常 拼接。
		//                   字符处理
		//                   8 种基本数据类型（byte、short、int、long、float、double、char、boolean）
		//                   8 种基本数据类型的引用对象（Byte、Short、Integer、Long、Float、Double、Char、Boolean）
		//                   大数字 BigDecimal 类型(小数部分保留 10 位，然后四舍五入)
		//                   日期(java.sql.Date, java.util.Date, LocalDate)
		//                   时间(java.sql.Timestamp, LocalDateTime, java.util.Date)
		CondiParam cT1 = new CondiParam("aaa", null, CondiParam.OPT_EQ);
		assertEquals("aaa = 'null'", cT1.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		// 这里，只是普通的相等操作。
		assertEquals("aaa = ''", cT1.setValue("").toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = ' '", cT1.setValue(" ").toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 'null'", cT1.setValue("null").toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = ' my ok . '", cT1.setValue(" my ok . ").toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 1", cT1.setValue((byte)1).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 2", cT1.setValue((short)2).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 123456789", cT1.setValue(123456789).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 1234567890123", cT1.setValue(1234567890123L).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 2.0", cT1.setValue(2.0f).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 3.14159265358979", cT1.setValue(3.14159265358979).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 'a'", cT1.setValue('a').toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 1", cT1.setValue(true).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 0", cT1.setValue(false).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 1", cT1.setValue(new Byte("1")).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 2", cT1.setValue(new Short("2")).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 123456789", cT1.setValue(new Integer("123456789")).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 1234567890123", cT1.setValue(new Long(1234567890123L)).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 2.0", cT1.setValue(new Float(2.0f)).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 3.14159265358979", cT1.setValue(new Double(3.14159265358979)).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 'a'", cT1.setValue(new Character('a')).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 1", cT1.setValue(new Boolean(true)).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 0", cT1.setValue(new Boolean(false)).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = -1", cT1.setValue(new BigDecimal("-1")).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 0", cT1.setValue(new BigDecimal("0")).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 1", cT1.setValue(new BigDecimal("1")).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 1234567890123456", cT1.setValue(new BigDecimal("1234567890123456")).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = 1.2345678901", cT1.setValue(new BigDecimal("1.234567890123456")).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = -1.2345678901", cT1.setValue(new BigDecimal("-1.234567890123456")).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		long oriDate = LocalDate.parse("2012-12-21").atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("aaa = '2012-12-21 00:00:00.0'", cT1.setValue(new java.sql.Date(oriDate)).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = '2012-12-21 00:00:00.0'", cT1.setValue(new java.util.Date(oriDate)).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = '2012-12-21'", cT1.setValue(Instant.ofEpochMilli(oriDate)
											.atZone(ZoneOffset.ofHours(8))
											.toLocalDate()).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		long oriTimestamp = LocalDateTime.parse("2012-12-21T05:20:17.123").atZone(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		assertEquals("aaa = '2012-12-21 05:20:17.123'", cT1.setValue(new Timestamp(oriTimestamp)).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = '2012-12-21 05:20:17.123'", cT1.setValue(new java.util.Date(oriTimestamp)).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("aaa = '2012-12-21 05:20:17.123'", cT1.setValue(Instant.ofEpochMilli(oriTimestamp)
											.atZone(ZoneOffset.ofHours(8))
											.toLocalDateTime()).toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// ================= 最后，测试 实体class 的属性翻译
		CondiParam cT2 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t", null);
		CondiParam cT3 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t2", Student.class);
		CondiParam cT4 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t3", VStudent.class);
		CondiParam cT5 = new CondiParam("stuNo", "S0001", CondiParam.OPT_EQ, "t4", VOStudent.class);
		//
		assertEquals("t.stuNo = 'S0001'", cT2.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("t2.stu_no = 'S0001'", cT3.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("t3.stu_no = 'S0001'", cT4.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("t4.stu_no = 'S0001'", cT5.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
		// ================= 简单的 SQL 注入测试
		CondiParam cS1 = new CondiParam("stuNo1", "\\ \" % ' _ = ; abcdef ` ...", CondiParam.OPT_EQ, "t", Student.class);
		CondiParam cS2 = new CondiParam("stuNo2", "%h%", CondiParam.OPT_EQ, "t", Student.class);
		CondiParam cS3 = new CondiParam("stuNo3", "%' or 1=1 ; -- '%", CondiParam.OPT_EQ, "t", Student.class);
		// 
		assertEquals("t.stuNo1 = '\\ \" % '' _ = ; abcdef ` ...'", cS1.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("t.stuNo2 = '%h%'", cS2.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		assertEquals("t.stuNo3 = '%'' or 1=1 ; -- ''%'", cS3.toSqlString(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER));
		
	}
	
	/**
	 * 这里测试的是 toString 方法。主要用于显示打印 CondiParam 对象内部的属性。
	 * 这里是关于本对象的一个属性信息输出。它主要打印内部属性的值。
	 */
	@Test
	void testToString() throws Exception {
		
		// 把 5个 构造函数都测试一遍
		CondiParam c1 = new CondiParam();
		CondiParam c2 = new CondiParam("stu_no", "S0001");
		CondiParam c3 = new CondiParam("stu_name", "王%", CondiParam.OPT_LIKE_PRE);
		CondiParam c4 = new CondiParam("birth_date", LocalDate.parse("1972-10-20"), CondiParam.OPT_GTE, "a1");
		CondiParam c5 = new CondiParam("stuNo", "S00002", CondiParam.OPT_EQ, "a2", Student.class);
		
		// 这里的输出，主要用于打印输出。观察信息是否有异常
		Stdout.pl(c1);
		Stdout.pl(c2);
		Stdout.pl(c3);
		Stdout.pl(c4);
		Stdout.pl(c5);
		
		// 定义一个正则，用于判断格式是否正确。(匹配时，忽略大小写)
		String regexp = "condiparam\\{\\(private\\).+(,[\\s]*\\(private\\).+)*\\}";
		
		// 判断
		assertTrue(PatternUtil.matches(regexp, c1.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, c2.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, c3.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, c4.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, c5.toString(), Pattern.CASE_INSENSITIVE));
		
	}
	
}
