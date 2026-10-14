/******************************************************************************************************

This file "TestDBFunction2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
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
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 函数调用第二阶段测试（有参的函数，测试 sql server 自定义 的函数）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月29日
 */
public class TestDBFunction2 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		//有参数，第一个是随机字符串的内容范围，第二个是 随机字符串的长度
		List<Object> params = new ArrayList<Object>();
		params.add("abcdefgh中间是文字nopqrstuvwxyz");
		params.add(10);
		SimpleDBData sdb = db.executeFunction("dbo.f_get_random_str", params);
		Stdout.pl(sdb);
		
		//有参数，第一个是随机字符串的内容范围，第二个是 随机字符串的长度（结果进行类型转换）
		String result = db.executeFunction(String.class, "dbo.f_get_random_str", params);
		Stdout.pl();
		Stdout.pl("测试结果如下："+result);
		Stdout.pl();
		
		//释放资源
		mana.destroyDataPools();
	}

}
