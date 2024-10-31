/******************************************************************************************************

This file "TestCallProcedure4.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.proc;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.models.query.ProcParam;
import net.mickarea.tools.models.query.ProcParamList;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;
import net.mickarea.tools.utils.database.DBStaticUtil;

/**
 * 测试存储过程调用，存储过程 返回一个结果集（结果集已转换为sdb）；
 * 由于 sql server 可以直接在 存储过程通过 select 返回结果集。所以，不需要像oracle一样，使用返回游标变量
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月30日
 */
public class TestCallProcedure4 {

	public static void main(String[] args) throws Exception {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		//参数定义
		ProcParamList procList = new ProcParamList();
		procList.addParam(new ProcParam(ProcParam.TYPE_IN, "table_name", "test_insert"));
		//执行
		SimpleDBData sdb = db.executeProcedure("p_get_top10_rs", procList);
		
		if(sdb.getResponseStatus().equals(DBStaticUtil.OK)) {
			Stdout.pl("执行成功，结果如下：");
			Stdout.pl(sdb);
		}else {
			Stdout.pl("执行失败，"+sdb.getResponseInfo());
		}
		
		//释放数据库连接池
		mana.destroyDataPools();
		
	}
	
}
