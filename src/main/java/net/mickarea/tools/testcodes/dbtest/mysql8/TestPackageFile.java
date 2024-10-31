/******************************************************************************************************

This file "TestPackageFile.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.Stdout;

/**
 * >> 测试数据库连接池管理对象
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月12日-2023年5月16日
 */
public class TestPackageFile {

	public static void main(String[] args) throws Exception {
		
		//获取数据库连接池管理对象
		DatabasePoolManager dbMana = DatabasePoolManager.getInstance();
		//根据对应的连接池名称，获取连接池对象
		DataSource dbSource = dbMana.getDataPoolByName("jar_primary");
		//打印一下信息
		Stdout.fpl("测试：%s", dbSource);
		//关闭连接池
		dbMana.destroyDataPools();
		
	}

}
