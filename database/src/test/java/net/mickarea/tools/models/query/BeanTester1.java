/******************************************************************************************************

This file "BeanTester1.java" is part of project "pdc-common-database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.mickarea.tools.beans.tables.Student;
import net.mickarea.tools.beans.views.VStudent;
import net.mickarea.tools.beans.virtualentities.VOStudent;
import net.mickarea.tools.db.checkers.IDBConnectionChecker;
import net.mickarea.tools.db.checkers.impl.MSSQLServerConnectionChecker;
import net.mickarea.tools.db.checkers.impl.MySQL8ConnectionChecker;
import net.mickarea.tools.db.checkers.impl.Oracle11ConnectionChecker;
import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 这里是 数据库实体类 bean 的测试处理。主要是测试 同一个 Bean 能否在不同的 数据库正确识别。
 * 这里主要是查询一下，看对象能否自动装载。
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年11月5日
 */
public class BeanTester1 {

	/**
	 * 在执行数据库测试前，先验证下数据库是否可以连接。如果数据库没有配置，则不需要测试这块了
	 * @throws Exception 任何可能的异常
	 */
	@BeforeAll
	public static void beforeAll() throws Exception {
		
		// mysql 8 连通性测试
		IDBConnectionChecker mysql8Checker = new MySQL8ConnectionChecker();
		assertTrue(mysql8Checker.check(), "MySQL 8 数据库测试不满足条件，无法连接数据库");
		Stdout.mylogger.debug("MySQL 8 数据库连接测试，通过！");
		
		// oracle 11 连通性测试
		IDBConnectionChecker oracle11Checker = new Oracle11ConnectionChecker();
		assertTrue(oracle11Checker.check(), "Oracle 11 数据库测试不满足条件，无法连接数据库");
		Stdout.mylogger.debug("Oracle 11 数据库连接测试，通过！");
		
		// ms sqlserver 2008+ 连通性测试
		IDBConnectionChecker sqlserverChecker = new MSSQLServerConnectionChecker();
		assertTrue(sqlserverChecker.check(), "MS SQL Server 2008+ 数据库测试不满足条件，无法连接数据库");
		Stdout.mylogger.debug("MS SQL Server 2016 数据库连接测试，通过！");
		
	}
	
	/**
	 * 每次测试前，确保连接池存在。
	 * @throws Exception 一些可能的异常。
	 */
	@BeforeEach
	void beforeEach() throws Exception {
		DatabasePoolManager.getInstance();
	}
	
	// ================================= 在开始测试前，需要在 对应数据库 构建 student 表, v_student 视图。 
	
	/**
	 * 这是关于 MySQL 8 数据库的测试
	 * @throws Exception 一些可能的异常。
	 */
	@Test
	void testMySQL8() throws Exception {
		
		// 创建一个数据库操作对象
		DBOperator db = new DBOperator(
			DatabasePoolManager.getInstance().getDataPoolByName("unittest_mysql8"), DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI);
		
		// 虚拟实体的sql语句
		String VOSql = "select stu_no, stu_name, \r\n"
					 + "       timestampdiff(YEAR, birth_date, curdate()) as age, \r\n"
					 + "       timestampdiff(YEAR, enroll_date, curdate()) as grade, \r\n"
					 + "       substr(address, 1, 2) country\r\n"
					 + "  from student where 1=?";
		
		// 执行结果
		StringBuffer execMsg1 = new StringBuffer();
		StringBuffer execMsg2 = new StringBuffer();
		StringBuffer execMsg3 = new StringBuffer();
		
		// 查询这个表，应该有5个结果。
		List<Student> studentList = db.queryEntity(Student.class, execMsg1);
		List<VStudent> vStudentList = db.queryEntity(VStudent.class, execMsg2);
		List<VOStudent> vEntityList = db.querySQL(VOStudent.class, VOSql, Arrays.asList(1), execMsg3);
		
		// 执行应该没有报错。
		assertTrue(StrUtil.isEmptyString(execMsg1.toString()));
		assertTrue(StrUtil.isEmptyString(execMsg2.toString()));
		assertTrue(StrUtil.isEmptyString(execMsg3.toString()));
		
		// 结果校验
		assertEquals(5, studentList.size());
		assertEquals(5, vStudentList.size());
		assertEquals(5, vEntityList.size());
		
		// 排序后验证结果 第一条数据。升序
		studentList.sort(Comparator.comparing(Student::getStuNo));
		// 
		Student s1 = studentList.get(0);
		// 
		assertEquals("S0000001", s1.getStuNo());
		assertEquals("张三", s1.getStuName());
		assertEquals(LocalDate.parse("1976-10-01"), s1.getBirthDate());
		assertEquals(LocalDate.parse("1983-07-01"), s1.getEnrollDate());
		assertEquals("M", s1.getGender());
		assertEquals("英国白金汉宫", s1.getAddress());
		assertEquals("001-002-003-004", s1.getTel());
		assertEquals("ADMIN00001", s1.getCreateUser());
		assertTrue(LocalDateTime.parse("2026-11-01T00:00:00").isBefore(s1.getCreateTime())); // 插入时是系统时间
		
		// 排序后验证结果 第一条数据。升序
		vStudentList.sort(Comparator.comparing(VStudent::getStuNo));
		//
		VStudent s2 = vStudentList.get(0);
		//
		assertEquals("S0000001", s2.getStuNo());
		assertEquals("张三", s2.getStuName());
		assertEquals(LocalDate.parse("1976-10-01"), s2.getBirthDate());
		assertEquals(50L, s2.getAge());
		assertEquals(LocalDate.parse("1983-07-01"), s2.getEnrollDate());
		assertEquals(43L, s2.getGrade());
		assertEquals("男", s2.getGender());
		assertEquals("英国白金汉宫", s2.getAddress());
		assertEquals("001-002-003-004", s2.getTel());
		assertEquals("ADMIN00001", s2.getCreateUser());
		assertTrue(LocalDateTime.parse("2026-11-01T00:00:00").isBefore(s2.getCreateTime())); // 插入时是系统时间
		
		// 排序后验证结果 第一条数据。升序
		vEntityList.sort(Comparator.comparing(VOStudent::getStuNo));
		// 
		VOStudent s3 = vEntityList.get(0);
		//
		assertEquals("S0000001", s3.getStuNo());
		assertEquals("张三", s3.getStuName());
		assertEquals(50L, s3.getAge());
		assertEquals(43L, s3.getGrade());
		assertEquals("英国", s3.getCountry());
	}
	
	/**
	 * 这是关于 Oracle 11 数据库的测试
	 * @throws Exception 一些可能的异常。
	 */
	@Test
	void testOracle11() throws Exception {
		
		// 创建一个数据库操作对象
		DBOperator db = new DBOperator(
			DatabasePoolManager.getInstance().getDataPoolByName("unittest_oracle11"), DBSQLInjectionUtil.DBTYPE_ORACLE);
		
		// 虚拟实体的sql语句
		String VOSql = "select stu_no, stu_name, \r\n"
				+ "       trunc(months_between(sysdate, birth_date)/12) as age, \r\n"
				+ "       trunc(months_between(sysdate, enroll_date)/12) as grade, \r\n"
				+ "       substr(address, 1, 2) country\r\n"
				+ "  from student where 1=?";
		
		// 执行结果
		StringBuffer execMsg1 = new StringBuffer();
		StringBuffer execMsg2 = new StringBuffer();
		StringBuffer execMsg3 = new StringBuffer();
		
		// 查询这个表，应该有5个结果。
		List<Student> studentList = db.queryEntity(Student.class, execMsg1);
		List<VStudent> vStudentList = db.queryEntity(VStudent.class, execMsg2);
		List<VOStudent> vEntityList = db.querySQL(VOStudent.class, VOSql, Arrays.asList(1), execMsg3);
		
		// 执行应该没有报错。
		assertTrue(StrUtil.isEmptyString(execMsg1.toString()));
		assertTrue(StrUtil.isEmptyString(execMsg2.toString()));
		assertTrue(StrUtil.isEmptyString(execMsg3.toString()));
		
		// 结果校验
		assertEquals(5, studentList.size());
		assertEquals(5, vStudentList.size());
		assertEquals(5, vEntityList.size());
		
		// 排序后验证结果 第一条数据。升序
		studentList.sort(Comparator.comparing(Student::getStuNo));
		// 
		Student s1 = studentList.get(0);
		// 
		assertEquals("S0000001", s1.getStuNo());
		assertEquals("张三", s1.getStuName());
		assertEquals(LocalDate.parse("1976-10-01"), s1.getBirthDate());
		assertEquals(LocalDate.parse("1983-07-01"), s1.getEnrollDate());
		assertEquals("M", s1.getGender());
		assertEquals("英国白金汉宫", s1.getAddress());
		assertEquals("001-002-003-004", s1.getTel());
		assertEquals("ADMIN00001", s1.getCreateUser());
		assertTrue(LocalDateTime.parse("2026-11-01T00:00:00").isBefore(s1.getCreateTime())); // 插入时是系统时间
		
		// 排序后验证结果 第一条数据。升序
		vStudentList.sort(Comparator.comparing(VStudent::getStuNo));
		//
		VStudent s2 = vStudentList.get(0);
		//
		assertEquals("S0000001", s2.getStuNo());
		assertEquals("张三", s2.getStuName());
		assertEquals(LocalDate.parse("1976-10-01"), s2.getBirthDate());
		assertEquals(50L, s2.getAge());
		assertEquals(LocalDate.parse("1983-07-01"), s2.getEnrollDate());
		assertEquals(43L, s2.getGrade());
		assertEquals("男", s2.getGender());
		assertEquals("英国白金汉宫", s2.getAddress());
		assertEquals("001-002-003-004", s2.getTel());
		assertEquals("ADMIN00001", s2.getCreateUser());
		assertTrue(LocalDateTime.parse("2026-11-01T00:00:00").isBefore(s2.getCreateTime())); // 插入时是系统时间
		
		// 排序后验证结果 第一条数据。升序
		vEntityList.sort(Comparator.comparing(VOStudent::getStuNo));
		// 
		VOStudent s3 = vEntityList.get(0);
		//
		assertEquals("S0000001", s3.getStuNo());
		assertEquals("张三", s3.getStuName());
		assertEquals(50L, s3.getAge());
		assertEquals(43L, s3.getGrade());
		assertEquals("英国", s3.getCountry());
	}
	
	/**
	 * 这是关于 SQL Server 2016 数据库的测试
	 * @throws Exception 一些可能的异常。
	 */
	@Test
	void testSQLServer2016() throws Exception {
		// 创建一个数据库操作对象
		DBOperator db = new DBOperator(
			DatabasePoolManager.getInstance().getDataPoolByName("unittest_sqlserver2016"), DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		// 虚拟实体的sql语句
		String VOSql = "select stu_no, stu_name, \r\n"
				+ "       datediff(YEAR, birth_date, GETDATE()) as age, \r\n"
				+ "       datediff(YEAR, enroll_date, GETDATE()) as grade, \r\n"
				+ "       substring(address, 1, 2) country\r\n"
				+ "  from student where 1=?";
		
		// 执行结果
		StringBuffer execMsg1 = new StringBuffer();
		StringBuffer execMsg2 = new StringBuffer();
		StringBuffer execMsg3 = new StringBuffer();
		
		// 查询这个表，应该有5个结果。
		List<Student> studentList = db.queryEntity(Student.class, execMsg1);
		List<VStudent> vStudentList = db.queryEntity(VStudent.class, execMsg2);
		List<VOStudent> vEntityList = db.querySQL(VOStudent.class, VOSql, Arrays.asList(1), execMsg3);
		
		// 执行应该没有报错。
		assertTrue(StrUtil.isEmptyString(execMsg1.toString()));
		assertTrue(StrUtil.isEmptyString(execMsg2.toString()));
		assertTrue(StrUtil.isEmptyString(execMsg3.toString()));
		
		// 结果校验
		assertEquals(5, studentList.size());
		assertEquals(5, vStudentList.size());
		assertEquals(5, vEntityList.size());
		
		// 排序后验证结果 第一条数据。升序
		studentList.sort(Comparator.comparing(Student::getStuNo));
		// 
		Student s1 = studentList.get(0);
		// 
		assertEquals("S0000001", s1.getStuNo());
		assertEquals("张三", s1.getStuName());
		assertEquals(LocalDate.parse("1976-10-01"), s1.getBirthDate());
		assertEquals(LocalDate.parse("1983-07-01"), s1.getEnrollDate());
		assertEquals("M", s1.getGender());
		assertEquals("英国白金汉宫", s1.getAddress());
		assertEquals("001-002-003-004", s1.getTel());
		assertEquals("ADMIN00001", s1.getCreateUser());
		assertTrue(LocalDateTime.parse("2026-11-01T00:00:00").isBefore(s1.getCreateTime())); // 插入时是系统时间
		
		// 排序后验证结果 第一条数据。升序
		vStudentList.sort(Comparator.comparing(VStudent::getStuNo));
		//
		VStudent s2 = vStudentList.get(0);
		//
		assertEquals("S0000001", s2.getStuNo());
		assertEquals("张三", s2.getStuName());
		assertEquals(LocalDate.parse("1976-10-01"), s2.getBirthDate());
		assertEquals(50L, s2.getAge());
		assertEquals(LocalDate.parse("1983-07-01"), s2.getEnrollDate());
		assertEquals(43L, s2.getGrade());
		assertEquals("男", s2.getGender());
		assertEquals("英国白金汉宫", s2.getAddress());
		assertEquals("001-002-003-004", s2.getTel());
		assertEquals("ADMIN00001", s2.getCreateUser());
		assertTrue(LocalDateTime.parse("2026-11-01T00:00:00").isBefore(s2.getCreateTime())); // 插入时是系统时间
		
		// 排序后验证结果 第一条数据。升序
		vEntityList.sort(Comparator.comparing(VOStudent::getStuNo));
		// 
		VOStudent s3 = vEntityList.get(0);
		//
		assertEquals("S0000001", s3.getStuNo());
		assertEquals("张三", s3.getStuName());
		assertEquals(50L, s3.getAge());
		assertEquals(43L, s3.getGrade());
		assertEquals("英国", s3.getCountry());
	}
	
}
