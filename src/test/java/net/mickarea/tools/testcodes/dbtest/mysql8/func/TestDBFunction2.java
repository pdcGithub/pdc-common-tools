/******************************************************************************************************

This file "TestDBFunction2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.func;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.Stdout;

/**
 * 函数调用第二阶段测试
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月12日-2024年4月3日
 */
public class TestDBFunction2 {

	public static void main(String[] args) {
		
		//获取数据库链接处理
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		DataSource dbSource = mana.getDataPoolByName("jar_primary");
		DBOperator db = new DBOperator(dbSource);
		
		//执行函数
		LocalDateTime time = db.executeFunction(LocalDateTime.class, "current_timestamp", null);
		Stdout.pl(time);
		
		List<Object> params = new ArrayList<Object>();
		time = db.executeFunction(LocalDateTime.class, "current_timestamp", params);
		Stdout.pl(time);
		
		params.clear();
		params.add(new Integer(2));
		Integer result = db.executeFunction(Integer.class, "test_func", params);
		Stdout.pl(result);
		
		params.clear();
		params.add("as;flksdj;kfjlskfj");
		params.add("k");
		Boolean result2 = db.executeFunction(Boolean.class, "contain_str", params);
		Stdout.pl(result2);
		Stdout.pl(params);
		
		params.clear();
		params.add("as;flksdj;kfjlskfj");
		params.add("qqqq");
		Boolean result3 = db.executeFunction(Boolean.class, "contain_str", params);
		Stdout.pl(result3);
		Stdout.pl(params);
		
		params.clear();
		params = null;
		Boolean result4 = db.executeFunction(Boolean.class, "contain_str", null);
		Stdout.pl(result4);
		Stdout.pl(params);
		
		//释放资源
		mana.destroyDataPools();

	}

}
