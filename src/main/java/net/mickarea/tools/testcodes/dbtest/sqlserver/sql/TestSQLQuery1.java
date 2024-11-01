/******************************************************************************************************

This file "TestSQLQuery1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
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
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.MyDataTypes;
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.vo.TestInsertVO;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 数据库信息查询操作，以sql形式，测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月28日
 */
public class TestSQLQuery1 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		//执行查询
		List<Object> params = new ArrayList<Object>(); //查询 id 为 3
		params.add(3);
		StringBuffer execMsg = new StringBuffer();
		List<TestInsertVO> result = db.querySQL(TestInsertVO.class, 
				                                      "select id, name, email, tel from dbo.test_insert where id=?", 
				                                      params, 
				                                      execMsg);
		//
		if(StrUtil.isEmptyString(execMsg.toString())) {
			Stdout.pl("执行成功");
			Stdout.pl("执行结果："+result);
		}else {
			Stdout.pl("执行失败");
			Stdout.pl(execMsg.toString());
		}
		//
		Stdout.pl("#################################################");
		//各数据都有的一个测试表
		String preSql = "select * from dbo.my_data_types where col_1 > ?";
		params.clear(); //清空查询参数
		params.add(35791);
		execMsg.setLength(0); //清空消息
		//再查询一下另外一个
		List<MyDataTypes> datas = db.querySQL(MyDataTypes.class, preSql, params, execMsg);
		String resultMsg = execMsg.toString();
		if(StrUtil.isEmptyString(resultMsg)) {
			Stdout.pl("执行成功");
			Stdout.pl("执行结果，如下("+datas.size()+"行记录)");
			Stdout.pl(datas);
		}else{
			Stdout.pl("执行失败");
			Stdout.pl(resultMsg);
		}
		//
		mana.destroyDataPools();
	}
	
}
