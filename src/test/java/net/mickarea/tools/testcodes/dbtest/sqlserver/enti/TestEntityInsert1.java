/******************************************************************************************************

This file "TestEntityInsert1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.enti;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.query.OrderParam;
import net.mickarea.tools.models.query.OrderParamList;
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.TestInsert;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 关于插入的实体操作测试1（一些普通的数据表，即无特殊注解，无特殊列的表）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月31日
 */
public class TestEntityInsert1 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		List<TestInsert> dataList = new ArrayList<TestInsert>();
		
		//定义一行数据
		TestInsert row1 = new TestInsert(0, "测试", 100, "中国", "mickarea.net@gmail.com", "12345678901");
		
		dataList.add(row1);
		
		//执行插入
		String msg = db.insertEntity(dataList);
		if(StrUtil.isEmptyString(msg)) {
			Stdout.pl("插入成功");
		}else {
			Stdout.pl("插入失败");
		}
		
		//设置排序
		OrderParamList orderList = new OrderParamList(TestInsert.class);
		orderList.addParam(new OrderParam("id", OrderParam.OPT_DESC));
		
		//设置消息容器
		StringBuffer execMsg = new StringBuffer();
		
		//查询表记录
		List<TestInsert> results = db.queryEntity(TestInsert.class, null, orderList, execMsg);
		if(StrUtil.isEmptyString(execMsg.toString())) {
			Stdout.pl("查询数据成功");
			results.forEach(Stdout::pl);
		}else {
			Stdout.pl("查询数据失败，"+execMsg);
		}
		
		//回收资源
		mana.destroyDataPools();
	}
	
}
