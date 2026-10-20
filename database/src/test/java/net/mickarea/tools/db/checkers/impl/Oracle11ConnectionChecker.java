/******************************************************************************************************

This file "MySQL8ConnectionChecker.java" is part of project "database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.db.checkers.impl;

import net.mickarea.tools.db.checkers.DBConnChecker;

/**
 * 这是 Oracle11 的数据库连接测试类。它主要用于在单元测试开始前，检测数据库是否已经配置完成。
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月19日
 */
public class Oracle11ConnectionChecker extends DBConnChecker {

	@Override
	public String getConfigPreffix() {
		return "oracle11";
	}

	@Override
	public String getConnUrlTemplate() {
		return "jdbc:oracle:thin:@%s:%s:%s";
	}

	@Override
	public String getTestSql() {
		return "select 1 from dual ";
	}

	@Override
	public String getJDBCClassName() {
		return "oracle.jdbc.driver.OracleDriver";
	}
	
}
