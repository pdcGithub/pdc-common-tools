/******************************************************************************************************

This file "TestSQLInsert2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBStaticUtil;
import net.mickarea.tools.utils.RandomUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * 数据库信息插入操作，以sql形式，测试2
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月21日-2024年4月3日
 */
public class TestSQLInsert2 {

	public static void main(String[] args) {
		
		//生成数据库操作对象
		DBOperator db = new DBOperator(DatabasePoolManager.getInstance().getDataPoolByName("jar_primary"));
		
		//插入数据的sql语句
		String sql = "insert into test_insert(strings, ctime) values(?, ?)";
		
		LocalDateTime nowTime = LocalDateTime.now();
		List<Object> params = new ArrayList<Object>();
		
		//第一行数据
		List<Object> line1 = new ArrayList<Object>();
		line1.add(RandomUtil.genLetterStringWitoutException(20));
		line1.add(nowTime);
		params.add(line1);
		
		//第二行数据
		List<Object> line2 = new ArrayList<Object>();
		line2.add(RandomUtil.genLetterStringWitoutException(20));
		line2.add(nowTime);
		params.add(line2);
		
		//第三行数据
		List<Object> line3 = new ArrayList<Object>();
		params.add(line3);
		
		//第四行数据 （测试是否回滚）
		//List<Object> line4 = new ArrayList<Object>();
		//line4.add(nowTime);
		//params.add(line4);
		
		Connection conn = null;
		try {
			conn = db.getDb().getConnection();
			DBStaticUtil.insertSQL(conn, sql, params);
			conn.commit();
			Stdout.pl("执行完成");
		} catch (Exception e) {
			//获取报错信息
			Stdout.pl(e);
			//数据回滚
			if(conn!=null) {
				try {conn.rollback();Stdout.pl("数据回滚");} catch (SQLException e1) {Stdout.pl(e1);}
			}
		} finally {
			//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
			if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
		}
		
		Stdout.pl(params);
		
	}
	
}
