/******************************************************************************************************

This file "DatabasePoolManagerTester2.java" is part of project "pdc-common-database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import net.mickarea.tools.db.checkers.IDBConnectionChecker;
import net.mickarea.tools.db.checkers.impl.MSSQLServerConnectionChecker;
import net.mickarea.tools.db.checkers.impl.MySQL8ConnectionChecker;
import net.mickarea.tools.db.checkers.impl.Oracle11ConnectionChecker;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.test.ConcurrencyTestUtil;

/**
 * 这里是数据库连接池管理对象 net.mickarea.tools.models.DatabasePoolManager 的单元测试。
 * <p>注意：普通的单元测试以及在 DatabasePoolManagerTester1 中完成了。这里测试的是连接池对象的并发安全性</p>
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月30日
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabasePoolManagerTester2 {

	/**
	 * 在执行数据库测试前，先验证下数据库是否可以连接。如果数据库没有配置，则不需要测试这块了
	 * @throws Exception 任何可能的异常
	 */
	@BeforeAll
	public static void beforeAll() throws Exception {
		
		// mysql 8 连通性测试
		IDBConnectionChecker mysql8Checker = new MySQL8ConnectionChecker();
		assertTrue(mysql8Checker.check(), "MySQL 8 数据库测试不满足条件，无法连接数据库");
		Stdout.mylogger.debug("MySQL 8 数据库连接测试，通过！");
		
		// oracle 11 连通性测试
		IDBConnectionChecker oracle11Checker = new Oracle11ConnectionChecker();
		assertTrue(oracle11Checker.check(), "Oracle 11 数据库测试不满足条件，无法连接数据库");
		Stdout.mylogger.debug("Oracle 11 数据库连接测试，通过！");
		
		// ms sqlserver 2008+ 连通性测试
		IDBConnectionChecker sqlserverChecker = new MSSQLServerConnectionChecker();
		assertTrue(sqlserverChecker.check(), "MS SQL Server 2008+ 数据库测试不满足条件，无法连接数据库");
		Stdout.mylogger.debug("MS SQL Server 2016 数据库连接测试，通过！");
		
	}
	
	/**
	 * 每次跑单元测试函数前，都先确认数据库连接池以及初始化了。
	 */
	@BeforeEach
	void beforeEach() throws Exception {
		DatabasePoolManager.getInstance();
	}
	
	/**
	 * 并发测试，看看连接池是否正常 这里是 MySQL8 数据库。初始化应该最少有 10 个连接对象。
	 * 因为连接池是和其它程序共用的，所以测试时不要直接等于。只要在 上限 和 下限 之内，就可以了。
	 */
	@Test
	@Order(value = 1)
	void testConnectionsMySQL8() throws Exception {
		
		// 获取一个 mysql8 的数据库连接池对象
		final DataSource ds = DatabasePoolManager.getInstance().getDataPoolByName("unittest_mysql8");
		
		// 定义并发测试相关的参数
		int threadNum = 10;
		long time = 100;
		TimeUnit unit = TimeUnit.SECONDS;
		
		// 定义2个装载结果的同步列表 
		List<Connection> reList = Collections.synchronizedList(new ArrayList<Connection>());
		// 
		List<Exception> errList = Collections.synchronizedList(new ArrayList<Exception>());
		
		// 开始并发测试
		ConcurrencyTestUtil.test(threadNum, time, unit, ()->{
			try {
				// 这里只 get 不 close 。那么每次应该是 不同的 connection 。
				reList.add(ds.getConnection());
				
			} catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				} else {
					// 记录其它异常，这里有异常说明 执行 出问题了
					errList.add(e);
				}
			}
		});
		
		// ======= 按照配置文件的定义，连接池初始 10 个链接，最大可以增长到 50 个。
		
		if(errList.size()>0) {
			errList.forEach(Stdout::pl);
		}
		
		// 开始结果验证
		assertEquals(0, errList.size());
		
		assertEquals(0, reList.stream().filter(conn->conn==null).count()); // 按连接池的管理，自动维护，不应该有 null
		assertTrue(10 <= reList.stream().distinct().count());  // 并发下，有些连接可能复用，但是不会少于 10 个连接
		assertTrue(50 >= reList.stream().distinct().count());  // 并发下，按配置最多应该有 50 个。
		
		// 归还前，打印以下内部情况
		HikariPoolMXBean mxBean = ((HikariDataSource)ds).getHikariPoolMXBean();
		Stdout.mylogger.debug(Stdout.fplToAnyWhere("connection pool %s, total=%s", ds, mxBean.getTotalConnections()));
		
		// 归还链接
		reList.forEach(conn->{
			try {
				Stdout.mylogger.debug(Stdout.fplToAnyWhere("连接对象:%s", conn));
				if(conn!=null) conn.close();
				Stdout.mylogger.debug("归还连接池的连接 成功 ... ");
			}catch(Exception e) {
				Stdout.mylogger.debug("归还连接池的连接时，发生异常："+e);
			}
		});
		
	}
	
	/**
	 * 并发测试，看看连接池是否正常 这里是 Oracle11 数据库。初始化应该最少有 10 个连接对象。
	 * 因为连接池是和其它程序共用的，所以测试时不要直接等于。只要在 上限 和 下限 之内，就可以了。
	 */
	@Test
	@Order(value = 2)
	void testConnectionsOracle11() throws Exception {
		
		// 获取一个 mysql8 的数据库连接池对象
		final DataSource ds = DatabasePoolManager.getInstance().getDataPoolByName("unittest_oracle11");
		
		// 定义并发测试相关的参数
		int threadNum = 10;
		long time = 100;
		TimeUnit unit = TimeUnit.SECONDS;
		
		// 定义2个装载结果的同步列表 
		List<Connection> reList = Collections.synchronizedList(new ArrayList<Connection>());
		// 
		List<Exception> errList = Collections.synchronizedList(new ArrayList<Exception>());
		
		// 开始并发测试
		ConcurrencyTestUtil.test(threadNum, time, unit, ()->{
			try {
				// 这里只 get 不 close 。那么每次应该是 不同的 connection 。
				reList.add(ds.getConnection());
				
			} catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				} else {
					// 记录其它异常，这里有异常说明 执行 出问题了
					errList.add(e);
				}
			}
		});
		
		// ======= 按照配置文件的定义，连接池初始 10 个链接，最大可以增长到 50 个。
		
		if(errList.size()>0) {
			errList.forEach(Stdout::pl);
		}
		
		// 开始结果验证
		assertEquals(0, errList.size());
		
		assertEquals(0, reList.stream().filter(conn->conn==null).count()); // 按连接池的管理，自动维护，不应该有 null
		assertTrue(10 <= reList.stream().distinct().count());  // 并发下，有些连接可能复用，但是不会少于 10 个连接
		assertTrue(50 >= reList.stream().distinct().count());  // 并发下，按配置最多应该有 50 个。
		
		// 归还前，打印以下内部情况
		HikariPoolMXBean mxBean = ((HikariDataSource)ds).getHikariPoolMXBean();
		Stdout.mylogger.debug(Stdout.fplToAnyWhere("connection pool %s, total=%s", ds, mxBean.getTotalConnections()));
		
		// 归还链接
		reList.forEach(conn->{
			try {
				Stdout.mylogger.debug(Stdout.fplToAnyWhere("连接对象:%s", conn));
				if(conn!=null) conn.close();
				Stdout.mylogger.debug("归还连接池的连接 成功 ... ");
			}catch(Exception e) {
				Stdout.mylogger.debug("归还连接池的连接时，发生异常："+e);
			}
		});
		
	}
	
	/**
	 * 并发测试，看看连接池是否正常 这里是 SQL Server 2016 数据库。初始化应该最少有 10 个连接对象。
	 * 因为连接池是和其它程序共用的，所以测试时不要直接等于。只要在 上限 和 下限 之内，就可以了。
	 */
	@Test
	@Order(value = 3)
	void testConnectionsSQLServer2016() throws Exception {
		
		// 获取一个 mysql8 的数据库连接池对象
		final DataSource ds = DatabasePoolManager.getInstance().getDataPoolByName("unittest_sqlserver2016");
		
		// 定义并发测试相关的参数
		int threadNum = 10;
		long time = 100;
		TimeUnit unit = TimeUnit.SECONDS;
		
		// 定义2个装载结果的同步列表 
		List<Connection> reList = Collections.synchronizedList(new ArrayList<Connection>());
		// 
		List<Exception> errList = Collections.synchronizedList(new ArrayList<Exception>());
		
		// 开始并发测试
		ConcurrencyTestUtil.test(threadNum, time, unit, ()->{
			try {
				// 这里只 get 不 close 。那么每次应该是 不同的 connection 。
				reList.add(ds.getConnection());
				
			} catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				} else {
					// 记录其它异常，这里有异常说明 执行 出问题了
					errList.add(e);
				}
			}
		});
		
		// ======= 按照配置文件的定义，连接池初始 10 个链接，最大可以增长到 50 个。
		
		if(errList.size()>0) {
			errList.forEach(Stdout::pl);
		}
		
		// 开始结果验证
		assertEquals(0, errList.size());
		
		assertEquals(0, reList.stream().filter(conn->conn==null).count()); // 按连接池的管理，自动维护，不应该有 null
		assertTrue(10 <= reList.stream().distinct().count());  // 并发下，有些连接可能复用，但是不会少于 10 个连接
		assertTrue(50 >= reList.stream().distinct().count());  // 并发下，按配置最多应该有 50 个。
		
		// 归还前，打印以下内部情况
		HikariPoolMXBean mxBean = ((HikariDataSource)ds).getHikariPoolMXBean();
		Stdout.mylogger.debug(Stdout.fplToAnyWhere("connection pool %s, total=%s", ds, mxBean.getTotalConnections()));
		
		// 归还链接
		reList.forEach(conn->{
			try {
				Stdout.mylogger.debug(Stdout.fplToAnyWhere("连接对象:%s", conn));
				if(conn!=null) conn.close();
				Stdout.mylogger.debug("归还连接池的连接 成功 ... ");
			}catch(Exception e) {
				Stdout.mylogger.debug("归还连接池的连接时，发生异常："+e);
			}
		});
		
	}
	
}
