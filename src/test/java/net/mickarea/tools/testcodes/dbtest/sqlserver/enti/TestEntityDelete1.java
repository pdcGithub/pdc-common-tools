/******************************************************************************************************

This file "TestEntityDelete1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.enti;

import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.MyDataTypes;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 关于删除的实体操作测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月31日
 */
public class TestEntityDelete1 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		StringBuffer sb = new StringBuffer();
		List<MyDataTypes> objectList = db.queryEntity(MyDataTypes.class, sb); //把数据一次性查出来，看看删除数据要多久。。。
		
		String msg = db.deleteEntity(objectList);
		if(!StrUtil.isEmptyString(msg)) {
			Stdout.pl("执行失败，"+msg);
		}else {
			Stdout.pl("执行成功");
		}
		
		//回收资源
		mana.destroyDataPools();
	}
	
}
