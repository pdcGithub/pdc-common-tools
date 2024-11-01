/******************************************************************************************************

This file "TestSQLDelete1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.sql;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 数据库信息删除操作，以sql形式，测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月28日
 */
public class TestSQLDelete1 {
	
	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);

		String deleteSql = "delete from dbo.my_data_types where col_1 = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(35314);
		
		//删除一条数据
		String errorMsg = db.deleteSQL(deleteSql, params);
		
		if(StrUtil.isEmptyString(errorMsg)) {
			Stdout.pl("执行成功");
		}else {
			Stdout.pl("执行失败");
			Stdout.pl(errorMsg);
		}
		//
		mana.destroyDataPools();
	}

}
