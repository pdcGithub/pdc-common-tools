/******************************************************************************************************

This file "TestSQLInsert1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.RandomUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * 数据库信息插入操作，以sql形式，测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月21日-2024年4月3日
 */
public class TestSQLInsert1 {

	public static void main(String[] args) {
		
		//生成数据库操作对象
		DBOperator db = new DBOperator(DatabasePoolManager.getInstance().getDataPoolByName("jar_primary"));
		
		String sql = "insert into test_insert(strings, ctime) values(?, ?)";
		
		List<Object> params = new ArrayList<Object>();
		
		//第一行数据
		List<Object> line1 = new ArrayList<Object>();
		line1.add(RandomUtil.genLetterStringWithoutException(20));
		line1.add(LocalDateTime.now());
		params.add(line1);
		
		//第二行数据
		List<Object> line2 = new ArrayList<Object>();
		line2.add(RandomUtil.genLetterStringWithoutException(20));
		line2.add(LocalDateTime.now());
		params.add(line2);
		
		//第三行数据
		//List<Object> line3 = new ArrayList<Object>();
		//params.add(line3);
		
		//第四行数据
		//List<Object> line4 = new ArrayList<Object>();
		//line4.add(LocalDateTime.now());
		//params.add(line4);
		
		String msg = db.insertSQL(sql, params);
		Stdout.pl(msg);
		
		Stdout.pl(params);
		
	}
	
}
