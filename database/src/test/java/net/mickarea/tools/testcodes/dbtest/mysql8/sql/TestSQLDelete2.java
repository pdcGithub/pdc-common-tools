/******************************************************************************************************

This file "TestSQLDelete2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBStaticUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * 数据库信息删除操作，以sql形式，测试2
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月20日-2024年4月3日
 */
public class TestSQLDelete2 {

	public static void main(String[] args) {
		
		//生成数据库操作对象
		DBOperator db = new DBOperator(DatabasePoolManager.getInstance().getDataPoolByName("jar_primary"));

		String deleteSql1 = "delete from test_insert where id=? ";
		List<Object> params1 = new ArrayList<Object>();
		params1.add(9);
		
		String deleteSql2 = "delete from test_insert where id=? and xx:xx "; // 保留一个语法错误，看看是否会回滚。
		List<Object> params2 = new ArrayList<Object>();
		params2.add(10);
		
		//获取连接对象
		Connection conn = null;
		try {
			conn = db.getDb().getConnection();
			
			DBStaticUtil.deleteSQL(conn, deleteSql1, params1); //这个不会报错
			
			DBStaticUtil.deleteSQL(conn, deleteSql2, params2); //这个会报错
			
			//测试不提交，是否会更改。因为配置上设置了 不自动提交
			//conn.commit();
			
			conn.commit();
			
			Stdout.pl("执行完成");
			
		} catch (Exception e) {
			//获取报错信息
			Stdout.pl(e);
			//数据回滚
			if(conn!=null) {
				try {
					conn.rollback();
					Stdout.pl("数据库操作回滚");
				} catch (SQLException e1) {
					Stdout.pl(e1);
					Stdout.pl("数据库操作回滚失败");
				}
			}
		} finally {
			//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
			if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
		}
		
		Stdout.pl(params1);
		Stdout.pl(params2);
		
	}
	
}
