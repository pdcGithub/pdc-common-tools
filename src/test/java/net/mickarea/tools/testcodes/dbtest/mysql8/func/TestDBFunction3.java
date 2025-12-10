/******************************************************************************************************

This file "TestDBFunction1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.func;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.TimeUtil;
import net.mickarea.tools.utils.database.DBOperator;

/**
 * 函数调用测试，测试 纳秒数的获取是否完整。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年11月29日
 */
public class TestDBFunction3 {

	public static void main(String[] args) {
		
		//获取数据库链接处理
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		DataSource dbSource = mana.getDataPoolByName("jar_primary");
		DBOperator db = new DBOperator(dbSource);
		
		//执行函数
		LocalDateTime nowTime = db.executeFunction(LocalDateTime.class, "current_timestamp", Arrays.asList(6));
		// 时间：  2024-11-29T15:14:21.980033
		Stdout.pl("时间："+nowTime); 
		// 格式化：2024-11-29 15:14:21.980033
		Stdout.pl("格式化："+TimeUtil.getCustomValue(nowTime, "yyyy-MM-dd HH:mm:ss.SSSSSS", "无内容"));
		
		//释放资源
		mana.destroyDataPools();

	}

}
