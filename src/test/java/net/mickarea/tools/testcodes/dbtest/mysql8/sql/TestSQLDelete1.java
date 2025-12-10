/******************************************************************************************************

This file "TestSQLDelete1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.Stdout;

/**
 * 数据库信息删除操作，以sql形式，测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月20日-2024年4月3日
 */
public class TestSQLDelete1 {
	
	public static void main(String[] args) {
		
		//生成数据库操作对象
		DBOperator db = new DBOperator(DatabasePoolManager.getInstance().getDataPoolByName("jar_primary"));

		String deleteSql = "delete from test_insert where id=? and (ctime >= now() or ctime >= ?) ";
		List<Object> params = new ArrayList<Object>();
		params.add(7);
		params.add(LocalDateTime.of(2023, 6, 1, 0, 0));
		
		String errorMsg = db.deleteSQL(deleteSql, params);
		Stdout.pl(errorMsg);
		
		Stdout.pl(params);
		
	}

}
