/******************************************************************************************************

This file "TestCallProcedure3.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.proc;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.Stdout;

/**
 * 用于测试 直接 jdbc 调用 sqlserver 存储过程（存储过程不带返回值）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月29日
 */
public class TestCallProcedure3 {
	
	public static void main(String[] args) {
		
		//String odbcUrl = "jdbc:oracle:thin:@192.168.1.201:1521:ORCL";
		String odbcClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		//String username = "scott";
		//String password = "tiger";
		
		boolean isJDBCDriverOK = false;
		
		//注册jdbc类
		try {
			Class.forName(odbcClassName);
			isJDBCDriverOK = true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		//如果注册成功，则处理
		if(isJDBCDriverOK) {
			
			//创建管理类
			DatabasePoolManager mana = DatabasePoolManager.getInstance();
			//获取数据库连接池对象
			DataSource ds = mana.getDataPoolByName("jar_sqlserver");
			//创建数据库操作对象
			//DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_ORACLE);
			
			//获取Connection
			Connection conn = null;
			//获取Statement
			CallableStatement stmt = null;
			try {
				//创建连接
				//conn = DriverManager.getConnection(odbcUrl, username, password);
				conn = ds.getConnection();
				//创建可调用的statement
				stmt = conn.prepareCall(" exec dbo.p_insert_test_2 ");
				//
				boolean execRe = stmt.execute();
				
				conn.commit();
				
				Stdout.pl("执行结果1："+execRe);
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				if(stmt!=null) {
					try{stmt.close();}catch(Exception e1) {}
					stmt=null;
				}
				if(conn!=null) {
					try{conn.close();}catch(Exception e2) {}
					conn=null;
				}
				if(mana!=null) {
					mana.destroyDataPools();
				}
				Stdout.pl("资源回收结束");
			}
		}
		
		//
	}
	
}
