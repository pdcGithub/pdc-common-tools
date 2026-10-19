/******************************************************************************************************

This file "MySQL8ConnectionChecker.java" is part of project "database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.db.checkers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import net.mickarea.tools.utils.PropertiesUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 这是 Oracle11 的数据库连接测试类。它主要用于在单元测试开始前，检测数据库是否已经配置完成。
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月19日
 */
public class Oracle11ConnectionChecker implements DBConnectionChecker {
	
	// 设置配置文件的资源路径
	String baseName = "dbunittest-conf.database-status";
	
	// 配置参数前缀
	String preffix = "oracle11";
	
	// 执行的 sql 语句
	String testSql = "select 1 from dual ";
	
	// ============= 定义 数据库服务连接用的几个参数（服务网络地址，端口，数据库用户名，数据库用户密码）
	String host ; 
	String port ;
	String username ;
	String password ;

	/**
	 * 空参构造函数。这个类主要用于在单元测试开始前，检测数据库是否已经配置完成。
	 * 虽然，它是空参
	 */
	public Oracle11ConnectionChecker() {
		// 获取配置信息
		this.host = PropertiesUtil.getMessage(baseName, preffix+".host");
		this.port = PropertiesUtil.getMessage(baseName, preffix+".port");
		this.username = PropertiesUtil.getMessage(baseName, preffix+".username");
		this.password = PropertiesUtil.getMessage(baseName, preffix+".password");
	}

	@Override
	public boolean check() {
		
		// 定义一个返回结果
		boolean result = false;
		
		// ========= 首先检查 配置参数 是否配置好
		if(StrUtil.isEmptyString(this.host)) {
			Stdout.mylogger.debug("The parameter 'host' is empty, please check.");
			return result;
		}
		if(StrUtil.isEmptyString(this.port)) {
			Stdout.mylogger.debug("The parameter 'port' is empty, please check.");
			return result;
		}
		if(StrUtil.isEmptyString(this.username)) {
			Stdout.mylogger.debug("The parameter 'username' is empty, please check.");
			return result;
		}
		if(StrUtil.isEmptyString(this.password)) {
			Stdout.mylogger.debug("The parameter 'password' is empty, please check.");
			return result;
		}
		
		// 定义 jdbc 驱动类的名字
		String jdbcClassName = "oracle.jdbc.driver.OracleDriver";
		
		// 定义 jdbc url 信息
		String jdbcUrl = Stdout.fplToAnyWhere("jdbc:oracle:thin:@%s:%s:ORCL", this.host, this.port);
		
		// 开始创建一个 JDBC 数据库连接
		Connection jdbcConnection = null;
		Statement jdbcStatement = null;
		ResultSet jdbcResultSet = null;
		try {
			// 加载驱动类
			Class.forName(jdbcClassName);
			// 创建连接
			jdbcConnection = DriverManager.getConnection(jdbcUrl, this.username, this.password);
			// 创建语句对象
			jdbcStatement = jdbcConnection.createStatement();
			// 查询结果集
			jdbcResultSet = jdbcStatement.executeQuery(this.testSql);
			// 获得结果，并更新状态
			// 只要有记录，就说明 测试用的 select 语句执行成功了。
			result = jdbcResultSet.next();
			
		} catch(Exception e) {
			Stdout.mylogger.debug("An exception occurred while connecting to the database.");
			Stdout.pl(e);
			
		} finally {
			// 资源回收
			if(jdbcResultSet!=null) { try { jdbcResultSet.close(); } catch (Exception e) {} jdbcResultSet = null;}
			if(jdbcStatement!=null) { try { jdbcStatement.close(); } catch (Exception e) {} jdbcStatement = null;}
			if(jdbcConnection!=null) { try { jdbcConnection.close(); } catch (Exception e) {} jdbcConnection = null;}
			Stdout.mylogger.debug("Database resource recovery processing completed.");
			
		}
		
		// 返回
		return result;
		
	}
	
}
