/******************************************************************************************************

This file "DBConnectionChecker.java" is part of project "database" , which is belong to Michael Pang (It's Me).
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
 * 这是一个数据库检查器的抽象实现。它把整个 检查的处理 封装好了。但是，保留一些差异性的处理给子类。
 * <p>这个类 整体的配置都在 src/test/resources/dbunittest-conf/database-status.properties </p>
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月20日
 */
public abstract class DBConnChecker implements IDBConnectionChecker {

	/** 
	 * 设置配置文件的资源路径
	 */
	private static String CONFIG_NAME = "dbunittest-conf.database-status";
	
	/**
	 * 数据库服务器的网络地址
	 */
	private String host ; 
	/**
	 * 数据库服务器的监听端口
	 */
	private String port ;
	/**
	 * 数据库服务器的测试账号信息
	 */
	private String username ;
	/**
	 * 数据库服务器的测试账号的密码信息
	 */
	private String password ;
	/**
	 * 数据库服务器的测试用数据库名（在Mysql和SQLServer是数据库名，在Oracle是数据库实例名）
	 */
	private String dbName ;
	
	// ===================================================== 这里是几个特殊信息的获取方法，由子类实现
	
	/**
	 * 获取配置文件中，关于特定数据库的配置信息前缀。比如：mysql8.host 中的 mysql8
	 * @return 数据库的配置信息前缀
	 */
	public abstract String getConfigPreffix();
	
	/**
	 * 获取对应数据库的链接信息模板。它需要按顺序保留 host, port, dbname 三个占位符。
	 * 比如："jdbc:mysql://%s:%s/%s"
	 * @return 数据库的链接信息模板。
	 */
	public abstract String getConnUrlTemplate();
	
	/**
	 * 获取对应数据库的测试sql语句。比如：select 1
	 * @return 数据库的测试sql语句。
	 */
	public abstract String getTestSql();
	
	/**
	 * 获取对应数据库的JDBC驱动类名称。比如：com.mysql.cj.jdbc.Driver
	 * @return 数据库的JDBC驱动类名称
	 */
	public abstract String getJDBCClassName();
	
	// =====================================================
	
	/**
	 * 这是默认的构造函数。它负责初始化 数据库连接时，需要的参数
	 */
	public DBConnChecker() {
		// 获取配置信息
		this.host = PropertiesUtil.getMessage(CONFIG_NAME, this.getConfigPreffix()+"."+"host");
		this.port = PropertiesUtil.getMessage(CONFIG_NAME, this.getConfigPreffix()+"."+"port");
		this.username = PropertiesUtil.getMessage(CONFIG_NAME, this.getConfigPreffix()+"."+"username");
		this.password = PropertiesUtil.getMessage(CONFIG_NAME, this.getConfigPreffix()+"."+"password");
		this.dbName = PropertiesUtil.getMessage(CONFIG_NAME, this.getConfigPreffix()+"."+"dbname");
	}
	
	@Override
	public boolean check() {
		
		// 定义一个返回结果
		boolean result = false;
		
		// 首先检查 配置参数 是否配置好
		if(StrUtil.isEmptyString(this.host) || StrUtil.isEmptyString(this.port) || StrUtil.isEmptyString(this.username)
			|| StrUtil.isEmptyString(this.password) || StrUtil.isEmptyString(this.dbName)) {
			Stdout.mylogger.debug("Some parameters are abnormal, please check.");
			Stdout.mylogger.debug(
				Stdout.fplToAnyWhere("The parameters are as follows: host=%s, port=%s, username=%s, password=%s, dbName=%s"),
				this.host, this.port, this.username, this.password, this.dbName
			);
			return result;
		}
		
		// 定义 jdbc 驱动类的名字
		String jdbcClassName = this.getJDBCClassName();
		
		// 定义 jdbc url 信息
		String jdbcUrl = Stdout.fplToAnyWhere(this.getConnUrlTemplate(), this.host, this.port, this.dbName);
		
		// 定义 测试用的 sql 语句
		String jdbcTestSql = this.getTestSql();
		
		// 开始创建一个 JDBC 数据库连接
		Connection jdbcConnection = null;
		Statement jdbcStatement = null;
		ResultSet jdbcResultSet = null;
		try {
			// 加载驱动类
			Class.forName(jdbcClassName);
			jdbcConnection = DriverManager.getConnection(jdbcUrl, this.username, this.password);
			// 创建语句对象
			jdbcStatement = jdbcConnection.createStatement();
			// 查询结果集
			jdbcResultSet = jdbcStatement.executeQuery(jdbcTestSql);
			// 获得结果，并更新状态
			// 只要有记录，就说明 测试用的 select 语句执行成功了。
			result = jdbcResultSet.next();
			
		} catch(Exception e) {
			Stdout.mylogger.debug("An exception occurred while connecting to the database.");
			Stdout.mylogger.debug("The following are several key parameters used in the test:");
			Stdout.mylogger.debug(Stdout.fplToAnyWhere("JDBC Class=%s", jdbcClassName));
			Stdout.mylogger.debug(Stdout.fplToAnyWhere("JDBC UrL=%s", jdbcUrl));
			Stdout.mylogger.debug(Stdout.fplToAnyWhere("JDBC Test Sql=%s", jdbcTestSql));
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
