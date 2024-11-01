/******************************************************************************************************

This file "TestSQLUpdate1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.sql;

import java.util.ArrayList;
import java.util.List;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 数据库信息更新操作，以sql形式，测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月22日-2024年4月3日
 */
public class TestSQLUpdate1 {
	
	public static void main(String[] args) {
		
		//生成数据库操作对象
		DBOperator db = new DBOperator(DatabasePoolManager.getInstance().getDataPoolByName("jar_primary"));
		
		String sql = "update test_b set exam_score=? where id=?";
		List<Object> params = new ArrayList<Object>();
		params.add(78.888);
		//params.add("AA"); //看看不同类型是否报错
		params.add(4);
		
		String msg = db.updateSQL(sql, params);
		
		if(StrUtil.isEmptyString(msg)) {
			Stdout.pl("执行成功");
		}else {
			Stdout.pl("执行失败");
			Stdout.pl(msg);
		}
		
	}

}