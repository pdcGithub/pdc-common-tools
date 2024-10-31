/******************************************************************************************************

This file "TestEntityInsert1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.enti;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestA;
import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.query.PageInfo;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBStaticUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * >> 关于插入的实体操作测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月7日-2024年4月3日
 */
public class TestEntityInsert1 {
	
	public static void main(String[] args) {
		
		//生成数据库操作对象
		DBOperator db = new DBOperator(DatabasePoolManager.getInstance().getDataPoolByName("jar_primary"));
		
		TestA a1 = new TestA(1, "测试名称3", "男", "地址1", "1542545465");
		TestA a2 = new TestA(2, "测试名称4", "男", "地址2", "4654321324");
		TestA a3 = new TestA(3, "测试名称5", "男", "地址1", "4475787877");
		List<Object> objectList = new ArrayList<Object>();
		objectList.add(a1);
		objectList.add(a2);
		objectList.add(a3);
		
		Connection conn = null;
		try {
			conn = db.getDb().getConnection();
			DBStaticUtil.insertEntity(conn, objectList);
			conn.commit();
			Stdout.pl("执行成功，事务提交。");
		} catch (Exception e) {
			Stdout.pl(e); //获取报错信息
			//数据回滚
			if(conn!=null) {
				try {
					conn.rollback(); Stdout.pl("数据库操作回滚");
				} catch (SQLException e1) {
					Stdout.pl(e1); Stdout.pl("数据库操作回滚失败");
				}
			}
		} finally {
			//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
			if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
		}
		
		StringBuffer sb = new StringBuffer();
		List<TestA> list = db.queryEntity(TestA.class, null, null, new PageInfo(1, 1000), sb);
		if(StrUtil.isEmptyString(sb.toString())) {
			Stdout.pl("{{");
			for(TestA a : list) {
				Stdout.pl(a);
			}
			Stdout.pl("}}");
		}else {
			Stdout.pl("执行查询出错，"+sb.toString());
		}
	}

}
