/******************************************************************************************************

This file "TestCallProcedure2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.proc;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.models.query.ProcParamList;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBStaticUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * >> 测试存储过程调用，特殊的存储过程（带返回的结果集）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月20日-2024年4月3日
 */
public class TestCallProcedure2 {

	public static void main(String[] args) throws Exception {
		
		//获取数据库连接池管理对象
		DatabasePoolManager dbMana = DatabasePoolManager.getInstance();
		//根据对应的连接池名称，获取连接池对象
		DataSource dbSource = dbMana.getDataPoolByName("jar_primary");
		//构建数据库操作对象
		DBOperator dbOpt = new DBOperator(dbSource);
		
		//设置执行参数
		ProcParamList paramList = new ProcParamList();
		
		//执行 paramList 可以为 null；因为存储过程没有参数
		SimpleDBData sdb = dbOpt.executeProcedure("outofparam_procedure", paramList);
		
		if(sdb.getResponseStatus().equals(DBStaticUtil.OK)) {
			Stdout.pl("执行成功");
		}else {
			Stdout.pl("执行失败，"+sdb.getResponseInfo());
		}
		
		//释放数据库连接池
		dbMana.destroyDataPools();
		
	}
	
}
