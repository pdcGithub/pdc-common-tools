/******************************************************************************************************

This file "TestDataTypes2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.Stdout;

/**
 * &gt;&gt;&nbsp;数据类型测试
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年4月3日
 */
public class TestDataTypes2 {

	public static void main(String[] args) {
		
		//获取数据库连接池管理对象
		DatabasePoolManager dbMana = DatabasePoolManager.getInstance();
		//创建datasource
		DataSource ds = dbMana.getDataPoolByName("jar_primary");
		
		try {
			Connection conn = ds.getConnection();
			PreparedStatement preStmt = conn.prepareStatement("insert into bi_access_log(access_id, access_time, access_timestamp) values(?,?,?)");
			
			Stdout.pl("设置参数");
			preStmt.setObject(1, "X0002");
			preStmt.setObject(2, new Timestamp(System.currentTimeMillis()));
			preStmt.setObject(3, new Timestamp(System.currentTimeMillis()));
			preStmt.addBatch();
			
			preStmt.setObject(1, "X0003");
			preStmt.setObject(2, new Timestamp(System.currentTimeMillis()));
			preStmt.setObject(3, new Timestamp(System.currentTimeMillis()));
			preStmt.addBatch();
			
			//执行插入处理
			Stdout.pl(preStmt.executeBatch());
			
			conn.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(dbMana!=null) {
				//关闭连接池
				dbMana.destroyDataPools();
			}
		}
		
	}

}
