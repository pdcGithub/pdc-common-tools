/******************************************************************************************************

This file "TestCallProcedure1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.proc;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.models.query.ProcParam;
import net.mickarea.tools.models.query.ProcParamList;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBStaticUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * >> 测试存储过程调用，普通存储过程
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月20日-2024年4月3日
 */
public class TestCallProcedure1 {

	public static void main(String[] args) throws Exception {
		
		//获取数据库连接池管理对象
		DatabasePoolManager dbMana = DatabasePoolManager.getInstance();
		//根据对应的连接池名称，获取连接池对象
		DataSource dbSource = dbMana.getDataPoolByName("jar_primary");
		//构建数据库操作对象
		DBOperator dbOpt = new DBOperator(dbSource);
		
		//设置执行参数
		ProcParamList paramList = new ProcParamList();
		paramList.addParam(new ProcParam(ProcParam.TYPE_IN, "in_param", "测试"));
		paramList.addParam(new ProcParam(ProcParam.TYPE_OUT, "out_param", ""));
		
		//执行
		SimpleDBData sdb = dbOpt.executeProcedure("test_procedure", paramList);
		
		if(sdb.getResponseStatus().equals(DBStaticUtil.OK)) {
			Stdout.pl("执行成功，返回信息："+paramList.getParam("out_param").getParamValue());
		}else {
			Stdout.pl("执行失败，"+sdb.getResponseInfo());
		}
		
		//释放数据库连接池
		dbMana.destroyDataPools();
		
	}
	
}
