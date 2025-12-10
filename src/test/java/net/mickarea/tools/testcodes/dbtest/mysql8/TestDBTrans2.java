/******************************************************************************************************

This file "TestDBTrans2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestA;
import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.db.DBTransAction;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBStaticUtil;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 数据库事务同步测试2
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月16日-2024年4月3日
 */
public class TestDBTrans2 {

	public static void main(String[] args) {
		
		//先初始化一个操作对象
		DBOperator opt = new DBOperator(DatabasePoolManager.getInstance().getDataPoolByName("jar_primary"));
		
		//执行数据库操作，需要事务同步使用 DBStaticUtil静态类，不需要同步的处理 使用 DBOperator类
		String msg = new DBTransAction(opt) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				
				DBOperator db = this.getOpt(); //操作类（由构造函数传入，可以直接获取）
				
				//查询A表的部分数据
				StringBuffer execMsg = new StringBuffer();
				List<TestA> listA = db.queryEntity(TestA.class, execMsg);
				
				if(StrUtil.isEmptyString(execMsg.toString())) {
					
					if(!ListUtil.isEmptyList(listA)) {
						
						List<Object> params = new ArrayList<Object>();
						params.add("测试测试");
						params.add(43);
						
						//把序号43以后的 姓名 数据更新掉
						DBStaticUtil.updateSQL(conn, "update test_a set name=? where id>=?", params); 
						
						//把序号43以后的 地址 数据更新掉（这里有违规操作 drop ，正常是报错，回滚）
						DBStaticUtil.updateSQL(conn, "update test_a set address=? where id>=?; drop table test_a;", params);
						
						Stdout.pl("全部操作执行完毕");
						
					}
					
				}else {
					Stdout.pl("执行查询 test_a 表操作有误");
				}
				
			}
		}.run(); // new 的同时，连带调用
		
		Stdout.pl("执行信息："+msg);
		
	}
	
}
