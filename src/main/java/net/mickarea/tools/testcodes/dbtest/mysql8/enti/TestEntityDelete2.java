/******************************************************************************************************

This file "TestEntityDelete2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.enti;

import java.util.List;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestB;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * >> 关于删除的实体操作测试2
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月6日-2024年4月3日
 */
public class TestEntityDelete2 {

	public static void main(String[] args) {
		
		//生成数据库操作对象
		DBOperator db = new DBOperator(DatabasePoolManager.getInstance().getDataPoolByName("jar_primary"));
		
		StringBuffer sb = new StringBuffer();
		List<TestB> objectList = db.queryEntity(TestB.class, sb); //把数据一次性查出来，看看删除数据要多久。。。
		
		String msg = db.deleteEntity(objectList);
		if(!StrUtil.isEmptyString(msg)) {
			Stdout.pl("执行失败，"+msg);
		}else {
			Stdout.pl("执行成功");
		}
		
	}
	
}
