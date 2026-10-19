/******************************************************************************************************

This file "DatabasePoolManagerTester1.java" is part of project "database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.mickarea.tools.db.checkers.DBConnectionChecker;
import net.mickarea.tools.db.checkers.MSSQLServerConnectionChecker;
import net.mickarea.tools.db.checkers.MySQL8ConnectionChecker;
import net.mickarea.tools.db.checkers.Oracle11ConnectionChecker;

/**
 * 这里是数据库连接池管理对象 net.mickarea.tools.models.DatabasePoolManager 的单元测试
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月19日
 */
public class DatabasePoolManagerTester1 {

	/**
	 * 在执行数据库测试前，先验证下数据库是否可以连接。如果数据库没有配置，则不需要测试这块了
	 * @throws Exception 任何可能的异常
	 */
	@BeforeAll
	public static void beforeAll() throws Exception {
		
		// mysql 8 连通性测试
		DBConnectionChecker mysql8Checker = new MySQL8ConnectionChecker();
		Assumptions.assumeTrue(mysql8Checker.check(), "MySQL 8 数据库测试不满足条件，无法连接MySQL8");
		
		// oracle 11 连通性测试
		DBConnectionChecker oracle11Checker = new Oracle11ConnectionChecker();
		Assumptions.assumeTrue(oracle11Checker.check(), "Oracle 11 数据库测试不满足条件，无法连接MySQL8");
		
		// ms sqlserver 2008+ 连通性测试
		DBConnectionChecker sqlserverChecker = new MSSQLServerConnectionChecker();
		Assumptions.assumeTrue(sqlserverChecker.check(), "MS SQL Server 2008+ 数据库测试不满足条件，无法连接 MS SQL Server 2008+");
		
	}
	
	@Test
	void test1() {
		
	}
	
}
