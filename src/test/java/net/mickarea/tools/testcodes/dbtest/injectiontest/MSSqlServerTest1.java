/******************************************************************************************************

This file "MSSqlServerTest1.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.injectiontest;

import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.query.CondiParam;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.TestInsert;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * sql 注入测试（关于 Microsoft SQL Server 的注入测试，测试1）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月31日
 */
public class MSSqlServerTest1 {
	
	public static void main(String[] args) {
		
		//初始化数据库连接池管理类（根据配置文件，可以同时管理多个连接池）
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		
		//获取sql server 数据库的连接池
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		
		//构建数据库操作类
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		//构造查询条件
		CondiParamList condiParams = new CondiParamList();
		// 正常条件
		//condiParams.addParam(new CondiParam("name", "测试", CondiParam.OPT_EQ));
		// 注入条件 1
		//condiParams.addParam(new CondiParam("name", "' and 1=1 --测试", CondiParam.OPT_EQ));
		// 注入条件 2
		//condiParams.addParam(new CondiParam("name", "' or 1=1 '测试", CondiParam.OPT_EQ));
		// 注入条件 3
		condiParams.addParam(new CondiParam("name", "测试' or 1=1 or '", CondiParam.OPT_EQ));
		
		//开始查询
		StringBuffer execMsg = new StringBuffer();
		List<TestInsert> results = db.queryEntity(TestInsert.class, condiParams, execMsg);
		
		//校验执行操作是否成功
		String resultMsg = execMsg.toString();
		if(!StrUtil.isEmptyString(resultMsg)) {
			Stdout.pl("执行查询出错："+resultMsg);
			System.exit(-1);
		}
		
		//校验执行是否有记录返回
		if(ListUtil.isEmptyList(results)) {
			Stdout.pl("执行的查询没有记录返回 ！！！");
		}else {
			//打印结果
			Stdout.pl();
			results.forEach(Stdout::pl);
			Stdout.pl();
		}
		
		//销毁连接池
		mana.destroyDataPools();
		
	}

}
