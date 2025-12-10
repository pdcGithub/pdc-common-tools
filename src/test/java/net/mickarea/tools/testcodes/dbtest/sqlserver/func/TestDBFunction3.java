/******************************************************************************************************

This file "TestDBFunction3.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.func;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * 函数调用第一阶段测试（无参的函数，测试 sql server 自定义 的函数）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月29日
 */
public class TestDBFunction3 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		//执行函数（无参函数，参数传入null）
		SimpleDBData sdb = db.executeFunction("dbo.f_get_random_float", null);
		Stdout.pl(sdb);
		
		//执行函数（无参函数，参数传入 空列表）
		sdb.destroy();
		List<Object> params = new ArrayList<Object>();
		sdb = db.executeFunction("dbo.f_get_random_float", params);
		Stdout.pl(sdb);
		
		//执行函数（无参函数，参数传入 空列表）；测试类型转换
		sdb.destroy();
		List<Object> params2 = new ArrayList<Object>();
		Double myTime = db.executeFunction(Double.class, "dbo.f_get_random_float", params2);
		Stdout.pl();
		Stdout.pl("类型转换，浮点值为:"+myTime);
		Stdout.pl();
		
		//释放资源
		mana.destroyDataPools();

	}

}
