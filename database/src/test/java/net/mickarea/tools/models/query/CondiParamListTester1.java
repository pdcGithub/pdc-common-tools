/******************************************************************************************************

This file "CondiParamListTester1.java" is part of project "pdc-common-database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import net.mickarea.tools.beans.tables.Student;
import net.mickarea.tools.beans.views.VStudent;
import net.mickarea.tools.beans.virtualentities.VOStudent;
import net.mickarea.tools.utils.PatternUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 这是 net.mickarea.tools.models.query.CondiParamList 类的单元测试
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年11月11日
 */
public class CondiParamListTester1 {

	/**
	 * 这里是关于 构造函数的测试 CondiParamList()
	 */
	@SuppressWarnings("deprecation")
	@Test
	void testConstructor1() {
		
		// 测试构造函数时，将会顺带测试 getter 和 setter 
		
		CondiParamList cdList1 = new CondiParamList();
		
		// 检测容器是否初始化完成
		assertEquals(0, cdList1.getSize());
		
		// 比对初始状态
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList1.getDbType()); // 默认情况下，为 MYSQL
		assertEquals(null, cdList1.getEntityClassName());
		assertEquals(null, cdList1.getSqlTemplate());
		
		// 比对 设置值后的状态。如果值不符合要求，它不会设置进去
		
		CondiParamList cdList2 = new CondiParamList();
		cdList2.setCodec("");
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList2.getDbType()); // （默认情况下 dbtype 为 mysql）
		cdList2.setCodec(" ");
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList2.getDbType());
		cdList2.setCodec("   ");
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList2.getDbType());
		cdList2.setCodec(" oracle ");
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList2.getDbType());
		cdList2.setCodec("oracle");
		assertEquals(DBSQLInjectionUtil.DBTYPE_ORACLE, cdList2.getDbType());
		cdList2.setCodec(DBSQLInjectionUtil.DBTYPE_DB2);
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList2.getDbType());
		cdList2.setCodec(DBSQLInjectionUtil.DBTYPE_DEFAULT);
		assertEquals(DBSQLInjectionUtil.DBTYPE_DEFAULT, cdList2.getDbType());
		cdList2.setCodec(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		assertEquals(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER, cdList2.getDbType());
		cdList2.setCodec(DBSQLInjectionUtil.DBTYPE_MYSQL);
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList2.getDbType());
		cdList2.setCodec(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI);
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI, cdList2.getDbType());
		cdList2.setCodec(DBSQLInjectionUtil.DBTYPE_ORACLE);
		assertEquals(DBSQLInjectionUtil.DBTYPE_ORACLE, cdList2.getDbType());
		
		CondiParamList cdList3 = new CondiParamList();
		cdList3.setDbType("");
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList3.getDbType()); // （默认情况下 dbtype 为 mysql）
		cdList3.setDbType(" ");
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList3.getDbType());
		cdList3.setDbType("   ");
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList3.getDbType());
		cdList3.setDbType(" oracle ");
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList3.getDbType());
		cdList3.setDbType("oracle");
		assertEquals(DBSQLInjectionUtil.DBTYPE_ORACLE, cdList3.getDbType());
		cdList3.setDbType(DBSQLInjectionUtil.DBTYPE_DB2);
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList3.getDbType());
		cdList3.setDbType(DBSQLInjectionUtil.DBTYPE_DEFAULT);
		assertEquals(DBSQLInjectionUtil.DBTYPE_DEFAULT, cdList3.getDbType());
		cdList3.setDbType(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		assertEquals(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER, cdList3.getDbType());
		cdList3.setDbType(DBSQLInjectionUtil.DBTYPE_MYSQL);
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList3.getDbType());
		cdList3.setDbType(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI);
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI, cdList3.getDbType());
		cdList3.setDbType(DBSQLInjectionUtil.DBTYPE_ORACLE);
		assertEquals(DBSQLInjectionUtil.DBTYPE_ORACLE, cdList3.getDbType());
		
		CondiParamList cdList4 = new CondiParamList();  
		cdList4.setEntityClassName(null);                   // 默认情况下 entityClassName 为 null
		assertEquals(null, cdList4.getEntityClassName());
		cdList4.setEntityClassName(this.getClass());
		assertEquals(null, cdList4.getEntityClassName()); 
		cdList4.setEntityClassName(Student.class);
		assertEquals(Student.class, cdList4.getEntityClassName()); 
		cdList4.setEntityClassName(VStudent.class);
		assertEquals(VStudent.class, cdList4.getEntityClassName()); 
		cdList4.setEntityClassName(VOStudent.class);
		assertEquals(VOStudent.class, cdList4.getEntityClassName()); 
		
		CondiParamList cdList5 = new CondiParamList();
		cdList5.setSqlTemplate(null);
		assertEquals(null, cdList5.getSqlTemplate());      // 默认情况下 SqlTemplate 为 null
		cdList5.setSqlTemplate("");
		assertEquals(null, cdList5.getSqlTemplate());
		cdList5.setSqlTemplate(" ");
		assertEquals(null, cdList5.getSqlTemplate());
		cdList5.setSqlTemplate("     ");
		assertEquals(null, cdList5.getSqlTemplate());
		cdList5.setSqlTemplate(" aaaa ");
		assertEquals(" aaaa ", cdList5.getSqlTemplate());
		cdList5.setSqlTemplate(" :a or :b");
		assertEquals(" :a or :b", cdList5.getSqlTemplate());
		
	}
	
	/**
	 * 这里是关于 构造函数的测试 CondiParamList(Class<?> entityClassName)
	 */
	@Test
	void testConstructor2() {
		
		// 测试构造函数时，将会顺带测试 getter 和 setter 
		
		CondiParamList cdList1 = new CondiParamList((Class<?>)null);
		CondiParamList cdList2 = new CondiParamList((Class<?>)this.getClass()); // 对于非实体类，不会设置进去
		CondiParamList cdList3 = new CondiParamList((Class<?>)Student.class);
		CondiParamList cdList4 = new CondiParamList((Class<?>)VStudent.class);
		CondiParamList cdList5 = new CondiParamList((Class<?>)VOStudent.class);
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList1.getDbType());
		assertEquals(null, cdList1.getEntityClassName());
		assertEquals(null, cdList1.getSqlTemplate());
		assertEquals(0, cdList1.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList2.getDbType());
		assertEquals(null, cdList2.getEntityClassName());
		assertEquals(null, cdList2.getSqlTemplate());
		assertEquals(0, cdList2.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList3.getDbType());
		assertEquals(Student.class, cdList3.getEntityClassName());
		assertEquals(null, cdList3.getSqlTemplate());
		assertEquals(0, cdList3.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList4.getDbType());
		assertEquals(VStudent.class, cdList4.getEntityClassName());
		assertEquals(null, cdList4.getSqlTemplate());
		assertEquals(0, cdList4.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList5.getDbType());
		assertEquals(VOStudent.class, cdList5.getEntityClassName());
		assertEquals(null, cdList5.getSqlTemplate());
		assertEquals(0, cdList5.getSize()); // 检测容器初始化是否完成
		
	}
	
	/**
	 * 这里是关于 构造函数的测试 CondiParamList(String sqlTemplate)
	 */
	@Test
	void testConstructor3() {
		
		// 测试构造函数时，将会顺带测试 getter 和 setter 
		
		CondiParamList cdList1 = new CondiParamList((String)null);
		CondiParamList cdList2 = new CondiParamList("");
		CondiParamList cdList3 = new CondiParamList(" ");
		CondiParamList cdList4 = new CondiParamList("     ");  // 对于null 和 空白字符串，不会设置进去。
		CondiParamList cdList5 = new CondiParamList("aaa");
		CondiParamList cdList6 = new CondiParamList(" :a or :b ");
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList1.getDbType());
		assertEquals(null, cdList1.getEntityClassName());
		assertEquals(null, cdList1.getSqlTemplate());
		assertEquals(0, cdList1.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList2.getDbType());
		assertEquals(null, cdList2.getEntityClassName());
		assertEquals(null, cdList2.getSqlTemplate());
		assertEquals(0, cdList2.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList3.getDbType());
		assertEquals(null, cdList3.getEntityClassName());
		assertEquals(null, cdList3.getSqlTemplate());
		assertEquals(0, cdList3.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList4.getDbType());
		assertEquals(null, cdList4.getEntityClassName());
		assertEquals(null, cdList4.getSqlTemplate());
		assertEquals(0, cdList4.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList5.getDbType());
		assertEquals(null, cdList5.getEntityClassName());
		assertEquals("aaa", cdList5.getSqlTemplate());
		assertEquals(0, cdList5.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList6.getDbType());
		assertEquals(null, cdList6.getEntityClassName());
		assertEquals(" :a or :b ", cdList6.getSqlTemplate());
		assertEquals(0, cdList6.getSize()); // 检测容器初始化是否完成
		
	}
	
	/**
	 * 这里是关于 构造函数的测试 CondiParamList(String sqlTemplate, Class<?> entityClassName)
	 */
	@Test
	void testConstructor4() {
		
		// 测试构造函数时，将会顺带测试 getter 和 setter 
		
		CondiParamList cdList1 = new CondiParamList((String)null, Student.class); // 异常值，string 遍历
		CondiParamList cdList2 = new CondiParamList("", Student.class);
		CondiParamList cdList3 = new CondiParamList(" ", Student.class);
		CondiParamList cdList4 = new CondiParamList("    ", Student.class);
		CondiParamList cdList5 = new CondiParamList(" aaa ", (Class<?>)null);      // 异常值，class 遍历
		CondiParamList cdList6 = new CondiParamList(" aaa ", this.getClass());
		CondiParamList cdList7 = new CondiParamList(" :a or :c ", Student.class);  // 正常值
		CondiParamList cdList8 = new CondiParamList(" :a or :c ", VStudent.class); 
		CondiParamList cdList9 = new CondiParamList(" :a or :c ", VOStudent.class);
		
		// 因为这里构造函数初始化2个值，所以2个都要校验
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList1.getDbType());
		assertEquals(Student.class, cdList1.getEntityClassName());
		assertEquals(null, cdList1.getSqlTemplate());
		assertEquals(0, cdList1.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList2.getDbType());
		assertEquals(Student.class, cdList2.getEntityClassName());
		assertEquals(null, cdList2.getSqlTemplate());
		assertEquals(0, cdList2.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList3.getDbType());
		assertEquals(Student.class, cdList3.getEntityClassName());
		assertEquals(null, cdList3.getSqlTemplate());
		assertEquals(0, cdList3.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList4.getDbType());
		assertEquals(Student.class, cdList4.getEntityClassName());
		assertEquals(null, cdList4.getSqlTemplate());
		assertEquals(0, cdList4.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList5.getDbType());
		assertEquals(null, cdList5.getEntityClassName());
		assertEquals(" aaa ", cdList5.getSqlTemplate());
		assertEquals(0, cdList5.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList6.getDbType());
		assertEquals(null, cdList6.getEntityClassName());
		assertEquals(" aaa ", cdList6.getSqlTemplate());
		assertEquals(0, cdList6.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList7.getDbType());
		assertEquals(Student.class, cdList7.getEntityClassName());
		assertEquals(" :a or :c ", cdList7.getSqlTemplate());
		assertEquals(0, cdList7.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList8.getDbType());
		assertEquals(VStudent.class, cdList8.getEntityClassName());
		assertEquals(" :a or :c ", cdList8.getSqlTemplate());
		assertEquals(0, cdList8.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList9.getDbType());
		assertEquals(VOStudent.class, cdList9.getEntityClassName());
		assertEquals(" :a or :c ", cdList9.getSqlTemplate());
		assertEquals(0, cdList9.getSize()); // 检测容器初始化是否完成
		
	}
	
	/**
	 * 这里是关于 构造函数的测试 CondiParamList(String dbType, String sqlTemplate)
	 */
	@Test
	void testConstructor5() {
		
		// 测试构造函数时，将会顺带测试 getter 和 setter 
		
		CondiParamList cdList1 = new CondiParamList((String)null, "aaaa"); // 异常值，string 遍历
		CondiParamList cdList2 = new CondiParamList("", "aaaa");
		CondiParamList cdList3 = new CondiParamList(" ", "aaaa");
		CondiParamList cdList4 = new CondiParamList("    ", "aaaa");
		CondiParamList cdList5 = new CondiParamList("type", "aaaa");
		CondiParamList cdList6 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, (String)null); // 异常值，string 遍历
		CondiParamList cdList7 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, "");
		CondiParamList cdList8 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, " ");
		CondiParamList cdList9 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, "    ");
		CondiParamList cdList10 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_ORACLE, " :a or :c "); // 正常值
		
		// 因为这里构造函数初始化2个值，所以2个都要校验
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList1.getDbType());
		assertEquals(null, cdList1.getEntityClassName());
		assertEquals("aaaa", cdList1.getSqlTemplate());
		assertEquals(0, cdList1.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList2.getDbType());
		assertEquals(null, cdList2.getEntityClassName());
		assertEquals("aaaa", cdList2.getSqlTemplate());
		assertEquals(0, cdList2.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList3.getDbType());
		assertEquals(null, cdList3.getEntityClassName());
		assertEquals("aaaa", cdList3.getSqlTemplate());
		assertEquals(0, cdList3.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList4.getDbType());
		assertEquals(null, cdList4.getEntityClassName());
		assertEquals("aaaa", cdList4.getSqlTemplate());
		assertEquals(0, cdList4.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList5.getDbType());
		assertEquals(null, cdList5.getEntityClassName());
		assertEquals("aaaa", cdList5.getSqlTemplate());
		assertEquals(0, cdList5.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList6.getDbType());
		assertEquals(null, cdList6.getEntityClassName());
		assertEquals(null, cdList6.getSqlTemplate());
		assertEquals(0, cdList6.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList7.getDbType());
		assertEquals(null, cdList7.getEntityClassName());
		assertEquals(null, cdList7.getSqlTemplate());
		assertEquals(0, cdList7.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList8.getDbType());
		assertEquals(null, cdList8.getEntityClassName());
		assertEquals(null, cdList8.getSqlTemplate());
		assertEquals(0, cdList8.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList9.getDbType());
		assertEquals(null, cdList9.getEntityClassName());
		assertEquals(null, cdList9.getSqlTemplate());
		assertEquals(0, cdList9.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_ORACLE, cdList10.getDbType());
		assertEquals(null, cdList10.getEntityClassName());
		assertEquals(" :a or :c ", cdList10.getSqlTemplate());
		assertEquals(0, cdList10.getSize()); // 检测容器初始化是否完成
		
	}
	
	/**
	 * 这里是关于 构造函数的测试 CondiParamList(String dbType, String sqlTemplate, Class<?> entityClassName)
	 */
	@Test
	void testConstructor6() {
		
		// 测试构造函数时，将会顺带测试 getter 和 setter 
		
		CondiParamList cdList1 = new CondiParamList((String)null, "aaaa", Student.class); // 异常值，string 遍历
		CondiParamList cdList2 = new CondiParamList("", "aaaa", Student.class);
		CondiParamList cdList3 = new CondiParamList(" ", "aaaa", Student.class);
		CondiParamList cdList4 = new CondiParamList("   ", "aaaa", Student.class);
		CondiParamList cdList5 = new CondiParamList(" type ", "aaaa", Student.class);
		CondiParamList cdList6 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, null, Student.class); // 异常值，string 遍历
		CondiParamList cdList7 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, "", Student.class);
		CondiParamList cdList8 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, " ", Student.class);
		CondiParamList cdList9 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, "    ", Student.class);
		CondiParamList cdList10 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, "aaaa", null);       // 异常值，class 遍历
		CondiParamList cdList11 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, "aaaa", this.getClass());
		CondiParamList cdList12 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, " :a or :c ", Student.class); // 正常值
		CondiParamList cdList13 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, " :a or :c ", VStudent.class);
		CondiParamList cdList14 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, " :a or :c ", VOStudent.class);
		
		// 因为这里构造函数初始化3个值，所以3个都要校验
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList1.getDbType());
		assertEquals(Student.class, cdList1.getEntityClassName());
		assertEquals("aaaa", cdList1.getSqlTemplate());
		assertEquals(0, cdList1.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList2.getDbType());
		assertEquals(Student.class, cdList2.getEntityClassName());
		assertEquals("aaaa", cdList2.getSqlTemplate());
		assertEquals(0, cdList2.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList3.getDbType());
		assertEquals(Student.class, cdList3.getEntityClassName());
		assertEquals("aaaa", cdList3.getSqlTemplate());
		assertEquals(0, cdList3.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList4.getDbType());
		assertEquals(Student.class, cdList4.getEntityClassName());
		assertEquals("aaaa", cdList4.getSqlTemplate());
		assertEquals(0, cdList4.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_MYSQL, cdList5.getDbType());
		assertEquals(Student.class, cdList5.getEntityClassName());
		assertEquals("aaaa", cdList5.getSqlTemplate());
		assertEquals(0, cdList5.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList6.getDbType());
		assertEquals(Student.class, cdList6.getEntityClassName());
		assertEquals(null, cdList6.getSqlTemplate());
		assertEquals(0, cdList6.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList7.getDbType());
		assertEquals(Student.class, cdList7.getEntityClassName());
		assertEquals(null, cdList7.getSqlTemplate());
		assertEquals(0, cdList7.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList8.getDbType());
		assertEquals(Student.class, cdList8.getEntityClassName());
		assertEquals(null, cdList8.getSqlTemplate());
		assertEquals(0, cdList8.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList9.getDbType());
		assertEquals(Student.class, cdList9.getEntityClassName());
		assertEquals(null, cdList9.getSqlTemplate());
		assertEquals(0, cdList9.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList10.getDbType());
		assertEquals(null, cdList10.getEntityClassName());
		assertEquals("aaaa", cdList10.getSqlTemplate());
		assertEquals(0, cdList10.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList11.getDbType());
		assertEquals(null, cdList11.getEntityClassName());
		assertEquals("aaaa", cdList11.getSqlTemplate());
		assertEquals(0, cdList11.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList12.getDbType());
		assertEquals(Student.class, cdList12.getEntityClassName());
		assertEquals(" :a or :c ", cdList12.getSqlTemplate());
		assertEquals(0, cdList12.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList13.getDbType());
		assertEquals(VStudent.class, cdList13.getEntityClassName());
		assertEquals(" :a or :c ", cdList13.getSqlTemplate());
		assertEquals(0, cdList13.getSize()); // 检测容器初始化是否完成
		
		assertEquals(DBSQLInjectionUtil.DBTYPE_DB2, cdList14.getDbType());
		assertEquals(VOStudent.class, cdList14.getEntityClassName());
		assertEquals(" :a or :c ", cdList14.getSqlTemplate());
		assertEquals(0, cdList14.getSize()); // 检测容器初始化是否完成
		
	}
	
	/**
	 * 这里是关于 addParam(CondiParam condi) 函数的测试
	 */
	@Test
	void testAdd() {
		
		// =========== 首先测试的是 单个条件对像的添加
		//             注意：只有完整的条件对象，才会加入容器。校验的内容有：参数名，条件操作符，参数值。
		
		// 这里 添加异常对象。（因为条件信息不完整，它们不应该加入 list 中）
		CondiParamList list1 = new CondiParamList();
		list1.addParam(null);
		assertEquals(0, list1.getSize());
		list1.addParam(new CondiParam());
		assertEquals(0, list1.getSize());
		// paramName , value ,optString
		list1.addParam(new CondiParam(null, "value", CondiParam.OPT_EQ));
		assertEquals(0, list1.getSize());
		list1.addParam(new CondiParam("", "value", CondiParam.OPT_EQ));
		assertEquals(0, list1.getSize());
		list1.addParam(new CondiParam(" ", "value", CondiParam.OPT_EQ));
		assertEquals(0, list1.getSize());
		list1.addParam(new CondiParam("    ", "value", CondiParam.OPT_EQ));
		assertEquals(0, list1.getSize());
		list1.addParam(new CondiParam("paramName", null, CondiParam.OPT_EQ));
		assertEquals(0, list1.getSize());
		list1.addParam(new CondiParam("paramName", null, null));
		assertEquals(0, list1.getSize());
		list1.addParam(new CondiParam("paramName", null, ""));
		assertEquals(0, list1.getSize());
		list1.addParam(new CondiParam("paramName", null, " "));
		assertEquals(0, list1.getSize());
		list1.addParam(new CondiParam("paramName", null, "   "));
		assertEquals(0, list1.getSize());
		
		// 这里添加正常对象。
		list1.addParam(new CondiParam("aaa", "111", CondiParam.OPT_EQ));
		assertEquals(1, list1.getSize());
		list1.addParam(new CondiParam("aaa", "111", CondiParam.OPT_GT));
		assertEquals(2, list1.getSize());
		list1.addParam(new CondiParam("aaa", "111", CondiParam.OPT_GTE));
		assertEquals(3, list1.getSize());
		list1.addParam(new CondiParam("aaa", Arrays.asList("111"), CondiParam.OPT_IN));
		assertEquals(4, list1.getSize());
		list1.addParam(new CondiParam("aaa", null, CondiParam.OPT_IS_NOT_NULL)); // not null 和 null 不需要value值
		assertEquals(5, list1.getSize());
		list1.addParam(new CondiParam("aaa", null, CondiParam.OPT_IS_NULL));
		assertEquals(6, list1.getSize());
		list1.addParam(new CondiParam("aaa", "111", CondiParam.OPT_LIKE));
		assertEquals(7, list1.getSize());
		list1.addParam(new CondiParam("aaa", "111", CondiParam.OPT_LIKE_PRE));
		assertEquals(8, list1.getSize());
		list1.addParam(new CondiParam("aaa", "111", CondiParam.OPT_LIKE_SUF));
		assertEquals(9, list1.getSize());
		list1.addParam(new CondiParam("aaa", "111", CondiParam.OPT_LT));
		assertEquals(10, list1.getSize());
		list1.addParam(new CondiParam("aaa", "111", CondiParam.OPT_LTE));
		assertEquals(11, list1.getSize());
		list1.addParam(new CondiParam("aaa", "111", CondiParam.OPT_NOT_EQ));
		assertEquals(12, list1.getSize());
		list1.addParam(new CondiParam("aaa", "111", CondiParam.OPT_REGEXP));
		assertEquals(13, list1.getSize());
		
	}
	
	/**
	 * 这里是关于 removeParam(int i)、removeParam(String paramName)、removeParam(CondiParam condi)、removeAll() 函数的测试
	 */
	@Test
	void testRemove() {
		
		// ===================================== 首先，验证 对于不存在的 内容，删除是否会报错
		CondiParamList l0 = new CondiParamList();
		assertEquals(0, l0.getSize());
		l0.removeParam(-1);
		assertEquals(0, l0.getSize());
		l0.removeParam(0);
		assertEquals(0, l0.getSize());
		l0.removeParam(1);
		assertEquals(0, l0.getSize());
		l0.removeParam(1000);
		assertEquals(0, l0.getSize());
		// 
		l0.removeParam("");
		assertEquals(0, l0.getSize());
		l0.removeParam("  ");
		assertEquals(0, l0.getSize());
		l0.removeParam("aaaaa");
		assertEquals(0, l0.getSize());
		// 
		l0.removeParam(new CondiParam());
		assertEquals(0, l0.getSize());
		l0.removeParam(new CondiParam("aaaaa", "11111"));
		assertEquals(0, l0.getSize());
		//
		l0.removeAll();
		assertEquals(0, l0.getSize());
		
		// =========================================== 然后，一个一个删除，看看大小有没有变化。 
		
		CondiParamList l2 = new CondiParamList();
		l2.addParam(new CondiParam("stuNo", "S00001"));
		l2.addParam(new CondiParam("gender", "F"));
		l2.addParam(new CondiParam("address", "中国"));
		l2.addParam(new CondiParam("birthDate", LocalDate.parse("2012-12-21"), CondiParam.OPT_GTE));
		assertEquals(4, l2.getSize());
		
		// removeParam(int i)
		l2.removeParam(3);
		assertEquals(3, l2.getSize());
		l2.removeParam(2);
		assertEquals(2, l2.getSize());
		l2.removeParam(1);
		assertEquals(1, l2.getSize());
		l2.removeParam(0);
		assertEquals(0, l2.getSize());
		
		l2.addParam(new CondiParam("stuNo", "S00001"));
		l2.addParam(new CondiParam("gender", "F"));
		l2.addParam(new CondiParam("address", "中国"));
		l2.addParam(new CondiParam("birthDate", LocalDate.parse("2012-12-21"), CondiParam.OPT_GTE));
		assertEquals(4, l2.getSize());
		
		// removeParam(String paramName)
		l2.removeParam("stuNo");
		assertEquals(3, l2.getSize());
		l2.removeParam("gender");
		assertEquals(2, l2.getSize());
		l2.removeParam("address");
		assertEquals(1, l2.getSize());
		l2.removeParam("birthDate");
		assertEquals(0, l2.getSize());
		
		CondiParam c1 = new CondiParam("stuNo", "S00001");
		CondiParam c2 = new CondiParam("gender", "F");
		CondiParam c3 = new CondiParam("address", "中国");
		CondiParam c4 = new CondiParam("birthDate", LocalDate.parse("2012-12-21"), CondiParam.OPT_GTE);

		l2.addParam(c1);
		l2.addParam(c2);
		l2.addParam(c3);
		l2.addParam(c4);
		assertEquals(4, l2.getSize());
		
		// removeParam(CondiParam condi) 关于对象比对，有2个：引用一致，或者 CondiParam中的信息一致。
		l2.removeParam(c1);
		assertEquals(3, l2.getSize());
		l2.removeParam(c2);
		assertEquals(2, l2.getSize());
		l2.removeParam(c3);
		assertEquals(1, l2.getSize());
		l2.removeParam(c4);
		assertEquals(0, l2.getSize());
		
		l2.addParam(c1);
		l2.addParam(c2);
		l2.addParam(c3);
		l2.addParam(c4);
		assertEquals(4, l2.getSize());
		
		l2.removeParam(new CondiParam("stuNo", "S00001"));
		assertEquals(3, l2.getSize());
		l2.removeParam(new CondiParam("gender", "F"));
		assertEquals(2, l2.getSize());
		l2.removeParam(new CondiParam("address", "中国"));
		assertEquals(1, l2.getSize());
		l2.removeParam(new CondiParam("birthDate", LocalDate.parse("2012-12-21"), CondiParam.OPT_GTE));
		assertEquals(0, l2.getSize());
		
		l2.addParam(new CondiParam("stuNo", "S00001"));
		l2.addParam(new CondiParam("gender", "F"));
		l2.addParam(new CondiParam("address", "中国"));
		l2.addParam(new CondiParam("birthDate", LocalDate.parse("2012-12-21"), CondiParam.OPT_GTE));
		assertEquals(4, l2.getSize());
		
		// removeAll()
		l2.removeAll();
		assertEquals(0, l2.getSize());
		
	}
	
	/**
	 * 这里是测试 toSqlString 方法。它主要负责把查询条件容器，转换为一个 查询条件 sql 语句。
	 */
	@Test
	void testToSqlString() {
		
		// 对于 SQL 语言转换，要测试几个方面：模板语句拼接、实体类翻译、数据库类型对应的sql注入处理。
		//                                  另外，因为条件语句的具体实现，实际上是 CondiParam 处理的。所以这里不做过多的测试。
		
		// 开始前，有个东西要注意：dbType 默认是 MYSQL，所以要注意 mysql 对应处理。
		
		// ==================== 首先是模板语句拼接 测试。主要是 模板替换符的测试 以及 模板存在与不存在的测试。
		
		CondiParam c1 = new CondiParam("stuNo", "S00001", CondiParam.OPT_EQ);
		CondiParam c2 = new CondiParam("gender", "F", CondiParam.OPT_EQ);
		CondiParam c3 = new CondiParam("address", "中国", CondiParam.OPT_LIKE);
		CondiParam c4 = new CondiParam("birthDate", LocalDate.parse("2012-12-21"), CondiParam.OPT_GTE);
		
		// 模板不存在, 也不配置 模板替换符名称
		CondiParamList list1 = new CondiParamList();
		list1.addParam(c1);
		list1.addParam(c2);
		list1.addParam(c3);
		list1.addParam(c4);
		assertEquals("stuNo = 'S00001' and gender = 'F' and address like '%中国%' and birthDate >= '2012-12-21'", 
			list1.toSqlString());
		
		// 配置了模板，但是 配置的替换符名称不匹配（默认的话，CondiParam 会把 paramName 作为替换符）
		CondiParamList list2 = new CondiParamList(" (:stuNo1 or :gender2 or :address3 or :birthDate4) ");
		list2.addParam(c1);
		list2.addParam(c2);
		list2.addParam(c3);
		list2.addParam(c4);
		assertEquals(" (:stuNo1 or :gender2 or :address3 or :birthDate4) ", list2.toSqlString());
		
		// 配置了模板，配置的替换符名称匹配
		CondiParamList list3 = new CondiParamList(" (:stuNo1 or (:gender2 or :address3) or :birthDate4) ");
		list3.addParam(c1.setSqlTemplateParamName("stuNo1"));
		list3.addParam(c2.setSqlTemplateParamName("gender2"));
		list3.addParam(c3.setSqlTemplateParamName("address3"));
		list3.addParam(c4.setSqlTemplateParamName("birthDate4"));
		assertEquals(" (stuNo = 'S00001' or (gender = 'F' or address like '%中国%') or birthDate >= '2012-12-21') ", 
			list3.toSqlString());
		
		// ==================== 然后是实体类翻译 测试。这里主要是测试 当条件对象 设置了实体信息， 和 没有设置实体信息 2个情况的差异。
		
		CondiParam c11 = new CondiParam("stuNo", "S00001", CondiParam.OPT_EQ, null, Student.class); // 只有整个设置实体类信息
		CondiParam c12 = new CondiParam("gender", "F", CondiParam.OPT_EQ);
		CondiParam c13 = new CondiParam("address", "中国", CondiParam.OPT_LIKE);
		CondiParam c14 = new CondiParam("birthDate", LocalDate.parse("2012-12-21"), CondiParam.OPT_GTE);
		
		// 这里不配置 list 容器的实体类。那么应该只有 c11 可以翻译出字段信息。
		CondiParamList list11 = new CondiParamList();
		list11.addParam(c11);
		list11.addParam(c12);
		list11.addParam(c13);
		list11.addParam(c14);
		assertEquals("stu_no = 'S00001' and gender = 'F' and address like '%中国%' and birthDate >= '2012-12-21'", 
			list11.toSqlString());
		
		// 这里配置 list 容器的实体类。应该全都可以翻译成功
		CondiParamList list21 = new CondiParamList(Student.class);
		list21.addParam(c11);
		list21.addParam(c12);
		list21.addParam(c13);
		list21.addParam(c14);
		assertEquals("stu_no = 'S00001' and gender = 'F' and address like '%中国%' and birth_date >= '2012-12-21'", 
			list21.toSqlString());
		
		// ==================== 最后是数据库类型对应的sql注入处理 测试。
		
		// 设置2个查询条件。（sql注入只是 字符串的处理）
		CondiParam sCondi1 = new CondiParam("stu_no", "%' or 1=1 ; -- '%", CondiParam.OPT_EQ);
		CondiParam sCondi2 = new CondiParam("address", "' or 1=1 ; -- '", CondiParam.OPT_LIKE);
		
		// 对于不同的数据库类型，它们的处理是不一样的。
		
		// 默认的方式 mysql
		CondiParamList sList1 = new CondiParamList();
		sList1.addParam(sCondi1);
		sList1.addParam(sCondi2);
		assertEquals("stu_no = '\\%\\' or 1\\=1 \\; -- \\'\\%' and address like '%\\' or 1\\=1 \\; -- \\'%'", sList1.toSqlString());
		
		// mysql
		CondiParamList sList2 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_MYSQL, "");
		sList2.addParam(sCondi1);
		sList2.addParam(sCondi2);
		assertEquals("stu_no = '\\%\\' or 1\\=1 \\; -- \\'\\%' and address like '%\\' or 1\\=1 \\; -- \\'%'", sList2.toSqlString());
		
		// mysql_ansi
		CondiParamList sList3 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI, "");
		sList3.addParam(sCondi1);
		sList3.addParam(sCondi2);
		assertEquals("stu_no = '%'' or 1=1 ; -- ''%' and address like '%'' or 1=1 ; -- ''%'", sList3.toSqlString());
		
		// oracle
		CondiParamList sList4 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_ORACLE, "");
		sList4.addParam(sCondi1);
		sList4.addParam(sCondi2);
		assertEquals("stu_no = '%'' or 1=1 ; -- ''%' and address like '%'' or 1=1 ; -- ''%'", sList4.toSqlString());
		
		// sql server 
		CondiParamList sList5 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER, "");
		sList5.addParam(sCondi1);
		sList5.addParam(sCondi2);
		assertEquals("stu_no = '%'' or 1=1 ; -- ''%' and address like '%'' or 1=1 ; -- ''%'", sList5.toSqlString());
		
	}
	
	/**
	 * 这里测试的是 toString 方法。它内部会输出 private 私有属性信息。
	 */
	@Test
	void testToString() {
		
		CondiParamList l1 = new CondiParamList();
		CondiParamList l2 = new CondiParamList(Student.class);
		CondiParamList l3 = new CondiParamList(":stuNo or :gender or :address or :birthDate");
		CondiParamList l4 = new CondiParamList(":stuNo or :gender or :address or :birthDate", Student.class);
		CondiParamList l5 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_DB2, ":stuNo or :gender or :address or :birthDate");
		CondiParamList l6 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_ORACLE, ":stuNo or :gender or :address or :birthDate", Student.class);
		l6.addParam(new CondiParam("stuNo", "S00001"));
		l6.addParam(new CondiParam("gender", "F"));
		l6.addParam(new CondiParam("address", "中国"));
		l6.addParam(new CondiParam("birthDate", LocalDate.parse("2012-12-21"), CondiParam.OPT_GTE));
		
		// 这里的输出，主要用于打印输出。观察信息是否有异常
		Stdout.pl(l1);
		Stdout.pl(l2);
		Stdout.pl(l3);
		Stdout.pl(l4);
		Stdout.pl(l5);
		Stdout.pl(l6);
		
		// 定义一个正则，用于判断格式是否正确。(匹配时，忽略大小写)
		String regexp = "condiparamlist\\{\\(private\\).+(,[\\s]*\\(private\\).+)*\\}";
		
		// 判断
		assertTrue(PatternUtil.matches(regexp, l1.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, l2.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, l3.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, l4.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, l5.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, l6.toString(), Pattern.CASE_INSENSITIVE));
		
	}
	
	/**
	 * 这是用于测试 正则信息提取用的一个测试函数。
	 */
	@Test
	void testRegexpString() {
		
		// 这里有3种 替换符 头部、尾部、中间。[:aaa_1, :22222, :bbb_2, :ddd_4, :ccc_3]
		String s1 = ":aaa_1 bbbb:22222  fff   :bbb_2   :ddd_4  fff  :ccc_3";
		
		// 在头部增加一些 字符，看看会不会错误识别。[:xx_0, :aaa_1, :22222, :bbb_2, :aaa_1, :ccc_3]
		String s2 = "ttt:xx_0  :aaa_1   bbbb:22222 fff  :bbb_2   :aaa_1  fff  :ccc_3";
		
		// [:aaa_1, :22222, :bbb_2, :ddd_4, :ccc_3]
		String s3 = "(:aaa_1 bbbb:22222  fff   :bbb_2   :ddd_4  fff  :ccc_3)";
		
		// [:aaa_1, :22222, :bbb_2, :ddd_4, :ccc_3]
		String s4 = " (:aaa_1 bbbb:22222  fff   :bbb_2   :ddd_4  fff  :ccc_3) ";
		
		// [:aaa_1, :22222, :bbb_2, :ddd_4, :ccc_3]
		String s5 = " ( :aaa_1 bbbb:22222  fff   (:bbb_2   :ddd_4)  fff  :ccc_3 ) ";
		
		// 提取正则（冒号左侧，不能非空白。即必须顶格 或者 有空白字符。冒号右侧不能有空白字符）
		String regexp = ":[a-zA-Z0-9\\_]+"; 
		
		// 看看能否提取到
		Stdout.pl("========= s1 ");
		Stdout.pl(PatternUtil.findMatchString(regexp, s1));
		
		// 看看能否提取到
		Stdout.pl("========= s2 ");
		Stdout.pl(PatternUtil.findMatchString(regexp, s2));
		
		Stdout.pl("========= s3 ");
		Stdout.pl(PatternUtil.findMatchString(regexp, s3));
		
		Stdout.pl("========= s4 ");
		Stdout.pl(PatternUtil.findMatchString(regexp, s4));
		
		Stdout.pl("========= s5 ");
		Stdout.pl(PatternUtil.findMatchString(regexp, s5));
		
	}
	
}
