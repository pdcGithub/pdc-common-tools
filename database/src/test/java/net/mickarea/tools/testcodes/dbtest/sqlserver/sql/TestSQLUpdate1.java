/******************************************************************************************************

This file "TestSQLUpdate1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
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
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 数据库信息更新操作，以sql形式，测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月28日
 */
public class TestSQLUpdate1 {
	
	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		String sql = "update dbo.my_data_types set col_4 = ? where col_1 = ?";
		List<Object> params = new ArrayList<Object>();
		params.add("你好");
		params.add("60374");
		
		//执行update
		String msg = db.updateSQL(sql, params);
		
		if(StrUtil.isEmptyString(msg)) {
			Stdout.pl("执行成功");
		}else {
			Stdout.pl("执行失败");
			Stdout.pl(msg);
		}
		//
		mana.destroyDataPools();
	}

}