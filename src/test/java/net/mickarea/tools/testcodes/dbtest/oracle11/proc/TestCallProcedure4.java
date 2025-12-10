/******************************************************************************************************

This file "TestCallProcedure4.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.oracle11.proc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.Stdout;

/**
 * 用于测试 直接 jdbc 调用 oracle 存储过程（存储过程 返回一个游标结果集）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月28日
 */
public class TestCallProcedure4 {
	
public static void main(String[] args) {
		
		String odbcClassName = "oracle.jdbc.driver.OracleDriver";
		
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
			DataSource ds = mana.getDataPoolByName("jar_oracle");
			
			//获取Connection
			Connection conn = null;
			//获取Statement
			CallableStatement stmt = null;
			//
			ResultSet rs = null;
			try {
				//创建连接
				//conn = DriverManager.getConnection(odbcUrl, username, password);
				conn = ds.getConnection();
				//创建可调用的statement
				stmt = conn.prepareCall(" call p_proc_test_3(?) ");
				stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
				//
				boolean execRe = stmt.execute();
				
				Stdout.pl("执行结果1："+execRe);
				Stdout.pl("返回结果2："+stmt.getObject(1));
				
				rs = (ResultSet)stmt.getObject(1);
				while(rs.next()) {
					Stdout.pl(rs.getMetaData().getColumnLabel(1)+"="+rs.getBigDecimal(1));
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				if(rs!=null) {
					try{rs.close();}catch(Exception e0) {}
					rs=null;
				}
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
		
	}

}
