/******************************************************************************************************

This file "TestEntityInsert2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.enti;

import java.util.ArrayList;
import java.util.List;

import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestA;
import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * >> 关于插入的实体操作测试2
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月7日-2024年4月3日
 */
public class TestEntityInsert2 {

	public static void main(String[] args) {
		
		//生成数据库操作对象
		DBOperator db = new DBOperator(DatabasePoolManager.getInstance().getDataPoolByName("jar_primary"));
		
		TestA a1 = new TestA(1, "测试名称113", "男", "地址4", "1542545465");
		TestA a2 = new TestA(2, "测试名称114", "男", "地址4", "4654321324");
		TestA a3 = new TestA(3, "测试名称115", "男", "地址4", "4475787877");
		List<Object> objectList = new ArrayList<Object>();
		objectList.add(a1);
		objectList.add(a2);
		objectList.add(a3);
		
		String msg = db.insertEntity(objectList);
		if(StrUtil.isEmptyString(msg)) {
			Stdout.pl("插入成功");
		}else {
			Stdout.pl("插入失败");
		}
		
		List<Object> params = new ArrayList<Object>();
		params.add("男");
		SimpleDBData sdb = db.querySQL("select * from test_a where gender=?", params);
		Stdout.pl(sdb);
		Stdout.pl(params);
	}
	
}
