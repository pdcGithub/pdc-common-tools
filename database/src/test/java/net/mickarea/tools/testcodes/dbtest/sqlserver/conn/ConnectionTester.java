/******************************************************************************************************

This file "ConnectionTester.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.conn;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 测试多数据库配置下，数据库连接池是否稳定运行
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月25日
 */
public class ConnectionTester {

	public static void main(String[] args) {
		
		//数据库连接池管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		
		Stdout.pl(mana);
		
		//连接 sqlserver 
		DataSource dsSqlServer = mana.getDataPoolByName("jar_sqlserver");
		
		DBOperator db = new DBOperator(dsSqlServer, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		List<Object> params = new ArrayList<Object>();
		params.add(1);
		
		SimpleDBData sdb = db.querySQL("select top 10 * from test_insert where 1=?", params);
		Stdout.pl(sdb);
		
		//关闭连接池
		mana.destroyDataPools();

	}

}
