/******************************************************************************************************

This file "DBTransActionTester1.java" is part of project "pdc-common-database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import net.mickarea.tools.db.checkers.IDBConnectionChecker;
import net.mickarea.tools.db.checkers.impl.MSSQLServerConnectionChecker;
import net.mickarea.tools.db.checkers.impl.MySQL8ConnectionChecker;
import net.mickarea.tools.db.checkers.impl.Oracle11ConnectionChecker;
import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBStaticUtil;

/**
 * 这是关于 net.mickarea.tools.models.db.DBTransAction 的单元测试类。这个类是抽象类，测试方式比较特殊。
 * <p>因为这里涉及到数据库表的查询和处理，不要并行执行单元测试，应该一个接着一个</p>
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月28日
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DBTransActionTester1 {

	/**
	 * 在所有的测试开始前，需要先验证 测试数据库 是否已经开机
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
	 * 这里是每次单元测试函数运行的时候，都首先执行的处理。
	 */
	@BeforeEach
	void beforeEach() throws Exception {
		
		// 先保证连接管理器有对象
		// 这里面如果没有，会重新初始化
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		
		// 这里应该检查 autoCommit 有没有设置 为 false。如果 autoCommit 为 true ，则每个语句都自动提交，没法测试 事务处理。
		
		// 从每个连接池提取 1 个 连接对象 用于检测。
		DataSource dsMySql1 = mana.getDataPoolByName("unittest_mysql8");
		DataSource dsMySql2 = mana.getDataPoolByName("unittest_jar1_mysql8");
		DataSource dsOracle = mana.getDataPoolByName("unittest_mysql8");
		DataSource dsSqlServer = mana.getDataPoolByName("unittest_mysql8");
		
		// 创建一个容器，用于存放测试对象
		Map<String, DataSource> dsMap = new HashMap<String, DataSource>();
		dsMap.put("dsMySql1", dsMySql1);
		dsMap.put("dsMySql2", dsMySql2);
		dsMap.put("dsOracle", dsOracle);
		dsMap.put("dsSqlServer", dsSqlServer);
		
		// 开始检测
		assertEquals(0, dsMap.entrySet().stream()
										.filter(entry->entry.getValue()==null && entry.getValue() instanceof DataSource)
										.count());
		assertEquals(0, dsMap.entrySet().stream()
										.filter(entry->{ // 过滤出所有的 自动提交的连接池
											boolean autoCommit = true;
											Connection conn = null;
											try {
												conn = entry.getValue().getConnection();
												autoCommit = conn.getAutoCommit(); // 正常来说，应该是 false 。因为配置文件指定的。
											}catch(Exception e) {
												Stdout.mylogger.debug("测试连接池时发生异常，"+e);
											}finally {
												//这里使用了连接池，所以 connection 对象不需要置 null，close方法会把连接归还连接池
												if(conn!=null) {
													try { conn.close(); } catch (Exception e) { Stdout.pl(e); }
												}
											}
											return autoCommit; 
										})
										.count());
	}
	
	/**
	 * 这里测试的是 抽象类 初始化的情况。无参初始化
	 */
	@Test
	@Order(value = 1)
	void testInit1() throws Exception {
		
		// 无参 初始化
		DBTransAction trans1 = new DBTransAction() {
			@Override
			public void doTrans(Connection conn) throws Exception {
				Stdout.pl("1+1=2");
			}
		};
		
		// ============== 首先，不初始化的情况下执行一次。（这里必然是报错的，报错的话会返回一些错误信息）
		assertFalse(StrUtil.isEmptyString(trans1.run()));
		// 这里目视检查返回的信息
		Stdout.mylogger.debug(trans1.run());
		
		// ============== 然后，测试 setter 和 getter
		
		// 一开始应该全是 null
		assertNull(trans1.getOpt());
		assertNull(trans1.getSource());
		
		// 定义 2个 连接池 mysql8 数据库 和 oracle11
		DataSource source1 = DatabasePoolManager.getInstance().getDataPoolByName("unittest_mysql8");
		DataSource source2 = DatabasePoolManager.getInstance().getDataPoolByName("unittest_oracle11");
		// 这里反过来 初始化 2个 操作类，方便测试 opt 和 datasource 的差异。
		DBOperator opt1 = new DBOperator(source2);
		DBOperator opt2 = new DBOperator(source1);
		
		// 验证 测试对象
		assertNotNull(opt1);
		assertNotNull(opt2);
		assertNotEquals(opt1, opt2);
		assertNotNull(source1);
		assertNotNull(source2);
		assertNotEquals(source1, source2);
		
		// 设置 opt 后对比（观察 opt 设置值后会不会被修改）
		trans1.setOpt(opt1);
		assertNotNull(trans1.getOpt());
		assertEquals(opt1, trans1.getOpt());
		trans1.setOpt(opt2); // 有值的情况下，不会二次赋值
		assertNotNull(trans1.getOpt());
		assertEquals(opt1, trans1.getOpt());
		assertNotEquals(opt2, trans1.getOpt());
		
		// 设置 source 后对比（观察 source 设置值后，会不会被修改）
		trans1.setSource(source1);
		assertNotNull(trans1.getSource());
		assertEquals(source1, trans1.getSource());
		trans1.setSource(source2);
		assertNotNull(trans1.getSource());
		assertEquals(source1, trans1.getSource());
		assertNotEquals(source2, trans1.getSource());
		
		// ============== 然后，在赋值的情况下执行一次。
		assertTrue(StrUtil.isEmptyString(trans1.run()));
		
		// ============== 最后，测试链接是否正确 source1 是 mysql8 , 但是 opt1 内部的source 是 oracle11 (正常的话，优先使用 source 而不是 opt)
		
		// 无参初始化
		DBTransAction trans2Mixed = new DBTransAction() {
			@Override
			public void doTrans(Connection conn) throws Exception {
				// 创建 stmt 对象
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
					stmt.execute("select 1 from i_am_oracle"); 
				}catch(Exception e) {
					// 把捕捉的异常抛出
					throw e;
				}finally {
					// 关闭 stmt 对象
					if(stmt!=null) { try { stmt.close(); } catch (Exception e1) { Stdout.pl(e1); } stmt = null;} 
				}
			}
		};
		trans2Mixed.setOpt(opt1); // 如果正常， 它会优先执行 source1 的链接，即 MySQL 8。然后报错 表 不存在
		trans2Mixed.setSource(source1);
		String re1 = trans2Mixed.run();
		assertTrue(!StrUtil.isEmptyString(re1)); // 预期是会报错的，返回异常信息。
		assertTrue(re1.toLowerCase().contains("i_am_oracle")); // 预期信息中，报错信息应该有 i_am_oracle 不存在的提示。
		
		// 无参初始化
		DBTransAction trans3Mixed = new DBTransAction() {
			@Override
			public void doTrans(Connection conn) throws Exception {
				// 创建 stmt 对象
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
					stmt.execute("select 1 from i_am_oracle"); 
				}catch(Exception e) {
					// 把捕捉的异常抛出
					throw e;
				}finally {
					// 关闭 stmt 对象
					if(stmt!=null) { try { stmt.close(); } catch (Exception e1) { Stdout.pl(e1); } stmt = null;} 
				}
			}
		};
		trans3Mixed.setOpt(opt1); // 只初始化 opt ，则会执行 oracle 的数据库连接
		String re2 = trans3Mixed.run();
		assertTrue(StrUtil.isEmptyString(re2)); // 执行成功，则不会返回消息
		
	}
	
	/**
	 * 这里测试的是 抽象类 初始化的情况。有参初始化 DBOperator
	 */
	@Test
	@Order(value = 2)
	void testInit2() throws Exception {
		
		// 定义 2个 连接池 mysql8 数据库 和 oracle11
		DataSource source1 = DatabasePoolManager.getInstance().getDataPoolByName("unittest_mysql8");
		DataSource source2 = DatabasePoolManager.getInstance().getDataPoolByName("unittest_oracle11");
		// 这里反过来 初始化 2个 操作类，方便测试 opt 和 datasource 的差异。
		DBOperator opt1 = new DBOperator(source2);
		DBOperator opt2 = new DBOperator(source1);
		
		// ======== 带参初始化 DBOperator
		DBTransAction trans2 = new DBTransAction(opt1) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				Stdout.pl("2+2=4");
			} 
		};
		
		// ============== 首先，执行一次，确保初始化是成功的
		assertTrue(StrUtil.isEmptyString(trans2.run()));
		
		// ============== 然后，测试 setter 和 getter 
		
		// 1、测试 opt 设置 和 值比对
		assertNotNull(trans2.getOpt());
		assertNull(trans2.getSource());
		assertEquals(opt1, trans2.getOpt());
		assertNotEquals(source1, trans2.getOpt());
		trans2.setOpt(null); // 尝试改掉已经赋值的 opt1，应该不成功
		assertNotNull(trans2.getOpt());
		assertEquals(opt1, trans2.getOpt());
		
		// 2、测试 source 设置 和 值比对
		assertNull(trans2.getSource());
		trans2.setSource(source1);
		assertNotNull(trans2.getSource());
		assertEquals(source1, trans2.getSource());
		trans2.setSource(null); // 尝试改掉已经赋值的 source1，应该不成功
		assertNotNull(trans2.getSource());
		assertEquals(source1, trans2.getSource());
		
		// ============== 最后，测试链接是否正确 （ 无参单独设置 setter 的 run 处理，在 testInit1 已经测试过了。这里只测试 带参的 ）
		//                      因为 testInit1 已经测试了 opt 和 source 都设置的情况。
		
		DBTransAction transOnlyOpt = new DBTransAction(opt1) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				// 创建 stmt 对象
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
					stmt.execute("select 1 from i_am_oracle"); 
				}catch(Exception e) {
					// 把捕捉的异常抛出
					throw e;
				}finally {
					// 关闭 stmt 对象
					if(stmt!=null) { try { stmt.close(); } catch (Exception e1) { Stdout.pl(e1); } stmt = null;} 
				}
			}
		};
		
		// 对于默认情况下，只有 opt1 则执行的是 Oracle 的连接。理论上是不会有异常返回的
		assertTrue(StrUtil.isEmptyString(transOnlyOpt.run()));
		
		// .......
		
		DBTransAction transOnlyOpt2 = new DBTransAction(opt2) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				// 创建 stmt 对象
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
					stmt.execute("select 1 from i_am_mysql"); 
				}catch(Exception e) {
					// 把捕捉的异常抛出
					throw e;
				}finally {
					// 关闭 stmt 对象
					if(stmt!=null) { try { stmt.close(); } catch (Exception e1) { Stdout.pl(e1); } stmt = null;} 
				}
			}
		};
		
		// 对于默认情况下，只有 opt2 则执行的是 mysql 的连接。理论上是不会有异常返回的
		assertTrue(StrUtil.isEmptyString(transOnlyOpt2.run()));
		
	}
	
	/**
	 * 这里测试的是 抽象类 初始化的情况。有参初始化 DataSource
	 */
	@Test
	@Order(value = 3)
	void testInit3() throws Exception {
		
		// 定义 2个 连接池 mysql8 数据库 和 oracle11
		DataSource source1 = DatabasePoolManager.getInstance().getDataPoolByName("unittest_mysql8");
		DataSource source2 = DatabasePoolManager.getInstance().getDataPoolByName("unittest_oracle11");
		// 这里反过来 初始化 2个 操作类，方便测试 opt 和 datasource 的差异。
		DBOperator opt1 = new DBOperator(source2);
		
		// ======== 带参初始化 DBOperator
		DBTransAction trans3 = new DBTransAction(source1) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				Stdout.pl("3+3=6");
			}
		};
		
		// ============== 首先，执行一次，确保初始化是成功的
		assertTrue(StrUtil.isEmptyString(trans3.run()));
		
		// ============== 然后，测试 setter 和 getter 
		
		// 1、测试 source 设置 和 值比对
		assertNotNull(trans3.getSource());
		assertNull(trans3.getOpt());
		assertEquals(source1, trans3.getSource());
		trans3.setSource(null); // 尝试改掉 source
		assertNotNull(trans3.getSource());
		assertEquals(source1, trans3.getSource());
		
		// 2、测试 opt 设置 和 值比对
		assertNull(trans3.getOpt());
		trans3.setOpt(opt1);
		assertNotNull(trans3.getOpt());
		assertEquals(opt1, trans3.getOpt());
		trans3.setOpt(null); // 尝试改掉 opt
		assertNotNull(trans3.getOpt());
		assertEquals(opt1, trans3.getOpt());
		
		// ============== 最后，测试链接是否正确 （ 无参单独设置 setter 的 run 处理，在 testInit1 已经测试过了。这里只测试 带参的 ）
		//                      因为 testInit1 已经测试了 opt 和 source 都设置的情况。
		
		DBTransAction trans3Source1 = new DBTransAction(source1) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				// 创建 stmt 对象
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
					stmt.execute("select 1 from i_am_mysql"); 
				}catch(Exception e) {
					// 把捕捉的异常抛出
					throw e;
				}finally {
					// 关闭 stmt 对象
					if(stmt!=null) { try { stmt.close(); } catch (Exception e1) { Stdout.pl(e1); } stmt = null;} 
				}
			}
		};
		
		// 对于默认情况下，只有 source1 则执行的是 mysql 的连接。理论上是不会有异常返回的
		assertTrue(StrUtil.isEmptyString(trans3Source1.run()));
		
		// .......
		
		DBTransAction trans3Source2 = new DBTransAction(source2) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				// 创建 stmt 对象
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
					stmt.execute("select 1 from i_am_oracle"); 
				}catch(Exception e) {
					// 把捕捉的异常抛出
					throw e;
				}finally {
					// 关闭 stmt 对象
					if(stmt!=null) { try { stmt.close(); } catch (Exception e1) { Stdout.pl(e1); } stmt = null;} 
				}
			}
		};
		
		// 对于默认情况下，只有 source2 则执行的是 oracle 的连接。理论上是不会有异常返回的
		assertTrue(StrUtil.isEmptyString(trans3Source2.run()));
		
	}
	
	/**
	 * 这里测试的是 MySQL 的事务完整性。
	 */
	@Test
	@Order(value = 4)
	void testRun_mysql8_rollback() throws Exception {
		
		// 首先，定义一个数据源
		DataSource source1 = DatabasePoolManager.getInstance().getDataPoolByName("unittest_mysql8");
		
		// 定义一个操作对象
		DBOperator opt1 = new DBOperator(source1);
		
		// 验证数据库中 i_am_mysql ，有一条原始记录， test_id=1, test_name=first
		SimpleDBData sdb = opt1.querySQL("select test_id, test_name from i_am_mysql where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb.getResponseInfo()));
		assertNotNull(sdb.getData());
		assertEquals(1, sdb.getData().length);
		assertEquals("1", sdb.getData()[0][0].toString());
		assertEquals("first", sdb.getData()[0][1]);
		
		// ============ 在事务中写入2个记录。然后，在事务外查询结果。
		
		DBTrans tran1 = new DBTransAction(source1) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				
				// 插入2个数据
				List<Object> newDatas = new ArrayList<Object>();
				newDatas.add(Arrays.asList(new BigDecimal("2"), "test_data2"));
				newDatas.add(Arrays.asList(new BigDecimal("3"), "test_data3"));
				DBStaticUtil.insertSQL(conn, "insert into i_am_mysql(test_id, test_name) values (?, ?)", newDatas);
				
				// 正常来说，事务提交前，只有这个链接可以查询到 3个结果
				SimpleDBData sdbInner = DBStaticUtil.querySQL(conn, "select test_id, test_name from i_am_mysql where 1=?", Arrays.asList(1));
				assertEquals(DBStaticUtil.OK, sdbInner.getResponseStatus());
				assertTrue(StrUtil.isEmptyString(sdbInner.getResponseInfo()));
				assertNotNull(sdbInner.getData());
				assertEquals(3, sdbInner.getData().length);
				
				// 然后抛异常任何异常，触发回滚。
				throw new Exception("just for rollback.");
			}
		};
		
		// 执行 事务
		String runLog = tran1.run();
		assertFalse(StrUtil.isEmptyString(runLog));
		assertTrue(runLog.contains("just for rollback."));
		
		// 最后查询数据库，确认只有一数据，事务回滚了。
		SimpleDBData sdb2 = opt1.querySQL("select test_id, test_name from i_am_mysql where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb2.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb2.getResponseInfo()));
		assertNotNull(sdb2.getData());
		assertEquals(1, sdb2.getData().length);
		assertEquals("1", sdb2.getData()[0][0].toString());
		assertEquals("first", sdb2.getData()[0][1]);
	}
	
	/**
	 * 这里测试的是 MySQL 的事务完整性。
	 */
	@Test
	@Order(value = 5)
	void testRun_mysql8_commit() throws Exception {
		
		// 首先，定义一个数据源
		DataSource source1 = DatabasePoolManager.getInstance().getDataPoolByName("unittest_mysql8");
		
		// 定义一个操作对象
		DBOperator opt1 = new DBOperator(source1);
		
		// 验证数据库中 i_am_mysql ，有一条原始记录， test_id=1, test_name=first
		SimpleDBData sdb = opt1.querySQL("select test_id, test_name from i_am_mysql where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb.getResponseInfo()));
		assertNotNull(sdb.getData());
		assertEquals(1, sdb.getData().length);
		assertEquals("1", sdb.getData()[0][0].toString());
		assertEquals("first", sdb.getData()[0][1]);
		
		// ============ 在事务中写入2个记录。然后，在事务外查询结果。
		
		DBTrans tran1 = new DBTransAction(source1) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				
				// 插入2个数据
				List<Object> newDatas = new ArrayList<Object>();
				newDatas.add(Arrays.asList(new BigDecimal("2"), "test_data2"));
				newDatas.add(Arrays.asList(new BigDecimal("3"), "test_data3"));
				DBStaticUtil.insertSQL(conn, "insert into i_am_mysql(test_id, test_name) values (?, ?)", newDatas);
				
				// 正常来说，事务提交前，只有这个链接可以查询到 3个结果
				SimpleDBData sdbInner = DBStaticUtil.querySQL(conn, "select test_id, test_name from i_am_mysql where 1=?", Arrays.asList(1));
				assertEquals(DBStaticUtil.OK, sdbInner.getResponseStatus());
				assertTrue(StrUtil.isEmptyString(sdbInner.getResponseInfo()));
				assertNotNull(sdbInner.getData());
				assertEquals(3, sdbInner.getData().length);
				
				// 这里不触发异常的话，就会提交
			}
		};
		
		// 执行 事务 （提交了，就代表没有异常）
		String runLog = tran1.run();
		assertTrue(StrUtil.isEmptyString(runLog));
		
		// 最后查询数据库，确认 1+2 条数据，事务提交了。
		SimpleDBData sdb2 = opt1.querySQL("select test_id, test_name from i_am_mysql where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb2.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb2.getResponseInfo()));
		assertNotNull(sdb2.getData());
		assertEquals(3, sdb2.getData().length); // 3 条数据
		
		// 删除测试数据，只保留 序号为 1 的记录
		String execMsg = opt1.deleteSQL("delete from i_am_mysql where test_id > ? ", Arrays.asList(new BigDecimal("1")));
		assertTrue(StrUtil.isEmptyString(execMsg));
		
		// （最后的检测）验证数据库中 i_am_mysql ，有一条原始记录， test_id=1, test_name=first
		SimpleDBData sdb3 = opt1.querySQL("select test_id, test_name from i_am_mysql where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb3.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb3.getResponseInfo()));
		assertNotNull(sdb3.getData());
		assertEquals(1, sdb3.getData().length);
		assertEquals("1", sdb3.getData()[0][0].toString());
		assertEquals("first", sdb3.getData()[0][1]);
	}
	
	/**
	 * 这里测试的是 Oracle 的事务完整性。
	 */
	@Test
	@Order(value = 6)
	void testRun_oracle11g_rollback() throws Exception {
		
		// 首先，定义一个数据源
		DataSource source2 = DatabasePoolManager.getInstance().getDataPoolByName("unittest_oracle11");
		
		// 定义一个操作对象
		DBOperator opt2 = new DBOperator(source2);
		
		// 验证数据库中 i_am_oracle ，有一条原始记录， test_id=1, test_name=first
		SimpleDBData sdb = opt2.querySQL("select test_id, test_name from i_am_oracle where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb.getResponseInfo()));
		assertNotNull(sdb.getData());
		assertEquals(1, sdb.getData().length);
		assertEquals("1", sdb.getData()[0][0].toString());
		assertEquals("first", sdb.getData()[0][1]);
		
		// ============ 在事务中写入2个记录。然后，在事务外查询结果。
		
		DBTrans tran2 = new DBTransAction(source2) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				
				// 插入2个数据
				List<Object> newDatas = new ArrayList<Object>();
				newDatas.add(Arrays.asList(new BigDecimal("2"), "test_data2"));
				newDatas.add(Arrays.asList(new BigDecimal("3"), "test_data3"));
				DBStaticUtil.insertSQL(conn, "insert into i_am_oracle(test_id, test_name) values (?, ?)", newDatas);
				
				// 正常来说，事务提交前，只有这个链接可以查询到 3个结果
				SimpleDBData sdbInner = DBStaticUtil.querySQL(conn, "select test_id, test_name from i_am_oracle where 1=?", Arrays.asList(1));
				assertEquals(DBStaticUtil.OK, sdbInner.getResponseStatus());
				assertTrue(StrUtil.isEmptyString(sdbInner.getResponseInfo()));
				assertNotNull(sdbInner.getData());
				assertEquals(3, sdbInner.getData().length);
				
				// 然后抛异常任何异常，触发回滚。
				throw new Exception("just for rollback.");
			}
		};
		
		// 执行 事务
		String runLog = tran2.run();
		assertFalse(StrUtil.isEmptyString(runLog));
		assertTrue(runLog.contains("just for rollback."));
		
		// 最后查询数据库，确认只有一数据，事务回滚了。
		SimpleDBData sdb2 = opt2.querySQL("select test_id, test_name from i_am_oracle where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb2.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb2.getResponseInfo()));
		assertNotNull(sdb2.getData());
		assertEquals(1, sdb2.getData().length);
		assertEquals("1", sdb2.getData()[0][0].toString());
		assertEquals("first", sdb2.getData()[0][1]);
		
	}
	
	/**
	 * 这里测试的是 Oracle 的事务完整性。
	 */
	@Test
	@Order(value = 7)
	void testRun_oracle11g_commit() throws Exception {
		
		// 首先，定义一个数据源
		DataSource source2 = DatabasePoolManager.getInstance().getDataPoolByName("unittest_oracle11");
		
		// 定义一个操作对象
		DBOperator opt2 = new DBOperator(source2);
		
		// 验证数据库中 i_am_oracle ，有一条原始记录， test_id=1, test_name=first
		SimpleDBData sdb = opt2.querySQL("select test_id, test_name from i_am_oracle where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb.getResponseInfo()));
		assertNotNull(sdb.getData());
		assertEquals(1, sdb.getData().length);
		assertEquals("1", sdb.getData()[0][0].toString());
		assertEquals("first", sdb.getData()[0][1]);
		
		// ============ 在事务中写入2个记录。然后，在事务外查询结果。
		
		DBTrans tran2 = new DBTransAction(source2) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				
				// 插入2个数据
				List<Object> newDatas = new ArrayList<Object>();
				newDatas.add(Arrays.asList(new BigDecimal("2"), "test_data2"));
				newDatas.add(Arrays.asList(new BigDecimal("3"), "test_data3"));
				DBStaticUtil.insertSQL(conn, "insert into i_am_oracle(test_id, test_name) values (?, ?)", newDatas);
				
				// 正常来说，事务提交前，只有这个链接可以查询到 3个结果
				SimpleDBData sdbInner = DBStaticUtil.querySQL(conn, "select test_id, test_name from i_am_oracle where 1=?", Arrays.asList(1));
				assertEquals(DBStaticUtil.OK, sdbInner.getResponseStatus());
				assertTrue(StrUtil.isEmptyString(sdbInner.getResponseInfo()));
				assertNotNull(sdbInner.getData());
				assertEquals(3, sdbInner.getData().length);
				
				// 这里不触发异常的话，就会提交
			}
		};
		
		// 执行 事务 （提交了，就代表没有异常）
		String runLog = tran2.run();
		assertTrue(StrUtil.isEmptyString(runLog));
		
		// 最后查询数据库，确认 1+2 条数据，事务提交了。
		SimpleDBData sdb2 = opt2.querySQL("select test_id, test_name from i_am_oracle where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb2.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb2.getResponseInfo()));
		assertNotNull(sdb2.getData());
		assertEquals(3, sdb2.getData().length); // 3 条数据
		
		// 删除测试数据，只保留 序号为 1 的记录
		String execMsg = opt2.deleteSQL("delete from i_am_oracle where test_id > ? ", Arrays.asList(new BigDecimal("1")));
		assertTrue(StrUtil.isEmptyString(execMsg));
		
		// （最后的检测）验证数据库中 i_am_oracle ，有一条原始记录， test_id=1, test_name=first
		SimpleDBData sdb3 = opt2.querySQL("select test_id, test_name from i_am_oracle where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb3.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb3.getResponseInfo()));
		assertNotNull(sdb3.getData());
		assertEquals(1, sdb3.getData().length);
		assertEquals("1", sdb3.getData()[0][0].toString());
		assertEquals("first", sdb3.getData()[0][1]);
		
	}
	
	/**
	 * 这里测试的是 SQL Server 的事务完整性。
	 */
	@Test
	@Order(value = 8)
	void testRun_sqlserver2016_rollback() throws Exception {
		
		// 首先，定义一个数据源
		DataSource source3 = DatabasePoolManager.getInstance().getDataPoolByName("unittest_sqlserver2016");
		
		// 定义一个操作对象
		DBOperator opt3 = new DBOperator(source3);
		
		// 验证数据库中 i_am_sqlserver ，有一条原始记录， test_id=1, test_name=first
		SimpleDBData sdb = opt3.querySQL("select test_id, test_name from i_am_sqlserver where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb.getResponseInfo()));
		assertNotNull(sdb.getData());
		assertEquals(1, sdb.getData().length);
		assertEquals("1", sdb.getData()[0][0].toString());
		assertEquals("first", sdb.getData()[0][1]);
		
		// ============ 在事务中写入2个记录。然后，在事务外查询结果。
		
		DBTrans tran3 = new DBTransAction(source3) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				
				// 插入2个数据
				List<Object> newDatas = new ArrayList<Object>();
				newDatas.add(Arrays.asList(new BigDecimal("2"), "test_data2"));
				newDatas.add(Arrays.asList(new BigDecimal("3"), "test_data3"));
				DBStaticUtil.insertSQL(conn, "insert into i_am_sqlserver(test_id, test_name) values (?, ?)", newDatas);
				
				// 正常来说，事务提交前，只有这个链接可以查询到 3个结果
				SimpleDBData sdbInner = DBStaticUtil.querySQL(conn, "select test_id, test_name from i_am_sqlserver where 1=?", Arrays.asList(1));
				assertEquals(DBStaticUtil.OK, sdbInner.getResponseStatus());
				assertTrue(StrUtil.isEmptyString(sdbInner.getResponseInfo()));
				assertNotNull(sdbInner.getData());
				assertEquals(3, sdbInner.getData().length);
				
				// 然后抛异常任何异常，触发回滚。
				throw new Exception("just for rollback.");
			}
		};
		
		// 执行 事务
		String runLog = tran3.run();
		assertFalse(StrUtil.isEmptyString(runLog));
		assertTrue(runLog.contains("just for rollback."));
		
		// 最后查询数据库，确认只有一数据，事务回滚了。
		SimpleDBData sdb2 = opt3.querySQL("select test_id, test_name from i_am_sqlserver where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb2.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb2.getResponseInfo()));
		assertNotNull(sdb2.getData());
		assertEquals(1, sdb2.getData().length);
		assertEquals("1", sdb2.getData()[0][0].toString());
		assertEquals("first", sdb2.getData()[0][1]);
		
	}
	
	/**
	 * 这里测试的是 SQL Server 的事务完整性。
	 */
	@Test
	@Order(value = 9)
	void testRun_sqlserver2016_commit() throws Exception {
		
		// 首先，定义一个数据源
		DataSource source3 = DatabasePoolManager.getInstance().getDataPoolByName("unittest_sqlserver2016");
		
		// 定义一个操作对象
		DBOperator opt3 = new DBOperator(source3);
		
		// 验证数据库中 i_am_sqlserver ，有一条原始记录， test_id=1, test_name=first
		SimpleDBData sdb = opt3.querySQL("select test_id, test_name from i_am_sqlserver where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb.getResponseInfo()));
		assertNotNull(sdb.getData());
		assertEquals(1, sdb.getData().length);
		assertEquals("1", sdb.getData()[0][0].toString());
		assertEquals("first", sdb.getData()[0][1]);
		
		// ============ 在事务中写入2个记录。然后，在事务外查询结果。
		
		DBTrans tran3 = new DBTransAction(source3) {
			@Override
			public void doTrans(Connection conn) throws Exception {
				
				// 插入2个数据
				List<Object> newDatas = new ArrayList<Object>();
				newDatas.add(Arrays.asList(new BigDecimal("2"), "test_data2"));
				newDatas.add(Arrays.asList(new BigDecimal("3"), "test_data3"));
				DBStaticUtil.insertSQL(conn, "insert into i_am_sqlserver(test_id, test_name) values (?, ?)", newDatas);
				
				// 正常来说，事务提交前，只有这个链接可以查询到 3个结果
				SimpleDBData sdbInner = DBStaticUtil.querySQL(conn, "select test_id, test_name from i_am_sqlserver where 1=?", Arrays.asList(1));
				assertEquals(DBStaticUtil.OK, sdbInner.getResponseStatus());
				assertTrue(StrUtil.isEmptyString(sdbInner.getResponseInfo()));
				assertNotNull(sdbInner.getData());
				assertEquals(3, sdbInner.getData().length);
				
				// 这里不触发异常的话，就会提交
			}
		};
		
		// 执行 事务 （提交了，就代表没有异常）
		String runLog = tran3.run();
		assertTrue(StrUtil.isEmptyString(runLog));
		
		// 最后查询数据库，确认 1+2 条数据，事务提交了。
		SimpleDBData sdb2 = opt3.querySQL("select test_id, test_name from i_am_sqlserver where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb2.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb2.getResponseInfo()));
		assertNotNull(sdb2.getData());
		assertEquals(3, sdb2.getData().length); // 3 条数据
		
		// 删除测试数据，只保留 序号为 1 的记录
		String execMsg = opt3.deleteSQL("delete from i_am_sqlserver where test_id > ? ", Arrays.asList(new BigDecimal("1")));
		assertTrue(StrUtil.isEmptyString(execMsg));
		
		// （最后的检测）验证数据库中 i_am_sqlserver ，有一条原始记录， test_id=1, test_name=first
		SimpleDBData sdb3 = opt3.querySQL("select test_id, test_name from i_am_sqlserver where 1=?", Arrays.asList(1));
		assertEquals(DBStaticUtil.OK, sdb3.getResponseStatus());
		assertTrue(StrUtil.isEmptyString(sdb3.getResponseInfo()));
		assertNotNull(sdb3.getData());
		assertEquals(1, sdb3.getData().length);
		assertEquals("1", sdb3.getData()[0][0].toString());
		assertEquals("first", sdb3.getData()[0][1]);
		
	}
	
}
