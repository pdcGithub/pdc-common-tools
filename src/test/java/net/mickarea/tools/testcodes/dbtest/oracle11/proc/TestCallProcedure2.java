/******************************************************************************************************

This file "TestCallProcedure2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.oracle11.proc;

import java.sql.Types;

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
 * 测试存储过程调用，普通存储过程（有参存储过程）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月25日-2024年6月28日
 */
public class TestCallProcedure2 {

	public static void main(String[] args) throws Exception {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_oracle");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_ORACLE);
		
		//参数定义
		ProcParamList procList = new ProcParamList();
		procList.addParam(new ProcParam(ProcParam.TYPE_IN, "table_name", "EMP"));
		procList.addParam(new ProcParam(ProcParam.TYPE_OUT, "row_count", 0).setOutParamSqlType(Types.INTEGER));
		//执行
		SimpleDBData sdb = db.executeProcedure("p_proc_test_2", procList);
		
		if(sdb.getResponseStatus().equals(DBStaticUtil.OK)) {
			Stdout.fpl("执行成功，数据库表 %s 的记录数为 %s", 
					procList.getParam("table_name").getParamValue(),
					procList.getParam("row_count").getParamValue());
		}else {
			Stdout.pl("执行失败，"+sdb.getResponseInfo());
		}
		
		//释放数据库连接池
		mana.destroyDataPools();
		
	}
	
}
