/******************************************************************************************************

This file "DBSQLInjectionUtilTester1.java" is part of project "database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * 这里是 net.mickarea.tools.utils.database.DBSQLInjectionUtil 类的单元测试
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月15日
 */
public class DBSQLInjectionUtilTester1 {
	
	@Test
	void sqlParamEncode() throws Exception {
		
		// ======== 首先是错误参数的测试（不管 dbType 如何，默认都是 只处理 单引号）
		assertEquals("'';", DBSQLInjectionUtil.sqlParamEncode(null, "';"));
		assertEquals("'';", DBSQLInjectionUtil.sqlParamEncode("", "';"));
		assertEquals("'';", DBSQLInjectionUtil.sqlParamEncode(" ", "';"));
		assertEquals("'';", DBSQLInjectionUtil.sqlParamEncode("aaaaa", "';"));
		// 然后是 sqlParam 
		assertEquals("", DBSQLInjectionUtil.sqlParamEncode(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI, null));
		assertEquals("", DBSQLInjectionUtil.sqlParamEncode(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI, ""));
		assertEquals(" ", DBSQLInjectionUtil.sqlParamEncode(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI, " "));        // 转换只是替换，不会修改
		assertEquals(" ", DBSQLInjectionUtil.sqlParamEncode(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI, "        ")); // 多个空格会处理为一个。转换只是替换，不会修改
		
		// 待转换的字符串
		String oriStr = "\\ \" % ' _ = ; abcdef ` ...";
		
		// ========= 然后是正常的参数处理
		assertEquals("\\\\ \\\" \\% \\' \\_ \\= \\; abcdef ` ...", DBSQLInjectionUtil.sqlParamEncode(DBSQLInjectionUtil.DBTYPE_MYSQL, oriStr)); // mysql 全部加斜杠
		assertEquals("\\ \" % '' _ = . abcdef ` ...", DBSQLInjectionUtil.sqlParamEncode(DBSQLInjectionUtil.DBTYPE_DB2, oriStr)); // db2 单引号转2个单引号，分号转 点号。
		assertEquals("\\ \" % '' _ = ; abcdef ` ...", DBSQLInjectionUtil.sqlParamEncode(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI, oriStr)); // 默认处理 单引号 单引号转2个单引号
		assertEquals("\\ \" % '' _ = ; abcdef ` ...", DBSQLInjectionUtil.sqlParamEncode(DBSQLInjectionUtil.DBTYPE_ORACLE, oriStr));     // 默认处理 单引号 单引号转2个单引号
		assertEquals("\\ \" % '' _ = ; abcdef ` ...", DBSQLInjectionUtil.sqlParamEncode(DBSQLInjectionUtil.DBTYPE_DEFAULT, oriStr));    // 默认处理 单引号 单引号转2个单引号
		
	}
	
}
