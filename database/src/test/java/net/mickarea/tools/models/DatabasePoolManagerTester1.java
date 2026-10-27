/******************************************************************************************************

This file "DatabasePoolManagerTester1.java" is part of project "database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import net.mickarea.tools.db.checkers.IDBConnectionChecker;
import net.mickarea.tools.db.checkers.impl.MSSQLServerConnectionChecker;
import net.mickarea.tools.db.checkers.impl.MySQL8ConnectionChecker;
import net.mickarea.tools.db.checkers.impl.Oracle11ConnectionChecker;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.test.ConcurrencyTestUtil;

/**
 * 这里是数据库连接池管理对象 net.mickarea.tools.models.DatabasePoolManager 的单元测试。
 * <p>这里有个要注意的地方：连接池配置，可以在本地路径，也可以在Jar包里面。所以要打包一个Jar，测试内部的properties有没有检测到</p>
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月19日
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabasePoolManagerTester1 {

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
	
	// ========================= 对于 DatabasePoolManager 这个类它会运行于多线程下，所以出了普通测试外，还要对它进行线程安全测试
	// ========================= 对外开放的就 3个方法 getInstance, getDataPoolByName, destroyDataPools。
	
	/**
	 * 并发下的功能测试。
	 * 因为 DatabasePoolManager 是一个单例处理的对象。所以，测试时必须先测试 getInstance ；否则其它函数先创建对象，就测试不了了。
	 */
	@Test
	@Order(value = 1)
	void getInstance() throws Exception {
		
		// 首先定义测试条件。100个线程，整体执行时间 10秒内。
		int threadNum = 100;
		long timeout = 10;
		TimeUnit unit = TimeUnit.SECONDS;
		
		// 定义2个同步列表，装载执行结果
		// 这个记录正常的结果
		List<DatabasePoolManager> reList = Collections.synchronizedList(new ArrayList<DatabasePoolManager>());
		// 这个记录 异常对象
		List<Exception> errorList = Collections.synchronizedList(new ArrayList<Exception>());
		
		// 开始测试
		ConcurrencyTestUtil.test(threadNum, timeout, unit, ()->{
			try {
				// 这里只记录 getInstance 出来的对象
				reList.add(DatabasePoolManager.getInstance());
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 执行 出问题了
					errorList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// ====== 统计结果 
		assertEquals(0, errorList.size());      // 异常数 0
		assertEquals(threadNum, reList.size()); // 总数 和线程数 吻合
		//
		assertEquals(1, reList.stream().distinct().count()); // 去重后，对象数 1 个
		assertNotEquals(null, reList.stream().distinct().collect(Collectors.toList()).get(0)); // 去重后，唯一的对象值 不是 null
	}
	
	/**
	 * 普通测试。看看参数处理是否正常。连接池是否按配置成功创建。
	 */
	@Test
	@Order(value = 2)
	void getDataPoolByName_1() throws Exception {
		
		assertNull(DatabasePoolManager.getInstance().getDataPoolByName(null)); // 4 个 空的连接池名字
		assertNull(DatabasePoolManager.getInstance().getDataPoolByName(""));
		assertNull(DatabasePoolManager.getInstance().getDataPoolByName("    "));
		assertNull(DatabasePoolManager.getInstance().getDataPoolByName("  ddd  "));
		assertNotNull(DatabasePoolManager.getInstance().getDataPoolByName("unittest_mysql8"));  // 3 个真实的名字
		assertNotNull(DatabasePoolManager.getInstance().getDataPoolByName("unittest_oracle11"));
		assertNotNull(DatabasePoolManager.getInstance().getDataPoolByName("unittest_sqlserver2016"));
		assertNull(DatabasePoolManager.getInstance().getDataPoolByName("unittest_mysql8_2")); // 这个配置文件 读取不到
		assertNull(DatabasePoolManager.getInstance().getDataPoolByName("unittest_name5"));    // 这个配置文件 是空的
		
		// 
		DatabasePoolManager poolMana = DatabasePoolManager.getInstance();
		
		// 查看3个真实的配置，是否创建成功
		// 3 个配置，应该是 3个不同的 连接池对象
		assertNotEquals(poolMana.getDataPoolByName("unittest_mysql8"), poolMana.getDataPoolByName("unittest_oracle11"));
		assertNotEquals(poolMana.getDataPoolByName("unittest_mysql8"), poolMana.getDataPoolByName("unittest_sqlserver2016"));
		assertNotEquals(poolMana.getDataPoolByName("unittest_oracle11"), poolMana.getDataPoolByName("unittest_sqlserver2016"));
	}
	
	/**
	 * 并发下的功能测试。
	 * 在 DatabasePoolManager 初始化时，会根据文件规则，搜索 conf/database-config/ 这个路径下的 db 开头的 properties 文件。
	 * 然后，会按照合规配置的数量，生成对应的数据库连接池。
	 * 当前
	 */
	@Test
	@Order(value = 3)
	void getDataPoolByName_2() throws Exception {
		
		// 首先定义测试条件。100个线程，整体执行时间 10秒内。
		int threadNum = 100;
		long timeout = 10;
		TimeUnit unit = TimeUnit.SECONDS;
		// 这是配置数量
		int configCount = 5; 
		
		/* 记录每一个线程的执行结果 */
		Map<Integer, DataSource> reMap = Collections.synchronizedMap(new HashMap<Integer, DataSource>());
		// 这个记录 异常对象
		List<Exception> errorList = Collections.synchronizedList(new ArrayList<Exception>());
		
		// 并发测试
		ConcurrencyTestUtil.test(threadNum, timeout, unit, ()->{
			// 
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				int sn = Integer.parseInt(num);
				
				// 根据线程序号，计算 对应的配置文件序号
				int choose = sn % configCount ;
				
				// 根据序号来提取连接池对象
				DataSource d = null;
				switch(choose) {
				case 0: // 配置5
					d = DatabasePoolManager.getInstance().getDataPoolByName("unittest_name5");
					break;
				case 1: // 配置1
					d = DatabasePoolManager.getInstance().getDataPoolByName("unittest_mysql8");
					break;
				case 2: // 配置2
					d = DatabasePoolManager.getInstance().getDataPoolByName("unittest_oracle11");
					break;
				case 3: // 配置3
					d = DatabasePoolManager.getInstance().getDataPoolByName("unittest_sqlserver2016");
					break;
				case 4: // 配置4
					d = DatabasePoolManager.getInstance().getDataPoolByName("unittest_mysql8_2");
					break;
				default:
					// 这里不应该进入的
					Stdout.fpl("======= thead %s , choose %s, run into a dead way.", num, choose);
					break;
				}
				
				// 记录执行结果，线程号，对象
				reMap.put(sn, d);
				
			} catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				} else {
					// 记录其它异常，这里有异常说明 执行 出问题了
					errorList.add(e);
				}
			}
		});
		
		// ========== 汇总统计
		// 异常数量 应该为 0
		assertEquals(0, errorList.size());
		// 线程数 应该是 乱序的 1 到 threadNum 共 threadNum 个数字
		assertEquals(threadNum, reMap.size());
		// 线程号信息应该完整，全部都是数字
		assertEquals(0, reMap.entrySet().stream().filter(entry->entry.getKey()==null).count());
		assertEquals(threadNum, reMap.entrySet().stream().mapToInt(entry->entry.getKey()).distinct().count());
		// 比对每个线程的结果
		for(Map.Entry<Integer, DataSource> entry: reMap.entrySet()) {
			int sn = entry.getKey().intValue();
			DataSource re = entry.getValue();
			// 计算 choose
			int choose = sn % configCount;
			switch(choose) {
			case 0:
				assertEquals(null, re);
				break;
			case 1:
				assertEquals(DatabasePoolManager.getInstance().getDataPoolByName("unittest_mysql8"), re);
				break;
			case 2:
				assertEquals(DatabasePoolManager.getInstance().getDataPoolByName("unittest_oracle11"), re);
				break;
			case 3:
				assertEquals(DatabasePoolManager.getInstance().getDataPoolByName("unittest_sqlserver2016"), re);
				break;
			case 4:
				assertEquals(null, re);
				break;
			default:
				assertTrue(false); // 这里不应该进来的
				break;
			}
		}
		
	}
	
	/**
	 * 普通的功能测试。
	 * 这里是连接池管理类的清空处理。它负责关闭数据库连接池，释放资源。
	 */
	@Test
	@Order(value = 4)
	void destroyDataPools_1() throws Exception {
		
		// 这里测试下，销毁后还能不能获取到 连接池对象
		
		// 获取管理对象
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		
		// 当前的3个连接池
		assertNotNull(mana.getDataPoolByName("unittest_mysql8"));
		assertNotNull(mana.getDataPoolByName("unittest_oracle11"));
		assertNotNull(mana.getDataPoolByName("unittest_sqlserver2016"));
		
		// 销毁操作
		mana.destroyDataPools();
		
		// 连接池对象应该为 null
		assertNull(mana.getDataPoolByName("unittest_mysql8"));
		assertNull(mana.getDataPoolByName("unittest_oracle11"));
		assertNull(mana.getDataPoolByName("unittest_sqlserver2016"));
		
		// 管理对象本身 无需释放。
		// 保留对象的引用，可以避免 get 方法出现空指针异常。
		assertNotNull(mana);
	}
	
	/**
	 * 并发下的功能测试。
	 * 这里是连接池管理类的清空处理。它负责关闭数据库连接池，释放资源。
	 */
	@Test
	@Order(value = 5)
	void destroyDataPools_2() throws Exception {
		
		// 这里在并发下测试 destroyDataPools 处理
		
		// 首先定义测试条件。100个线程，整体执行时间 10秒内。
		int threadNum = 100;
		long timeout = 10;
		TimeUnit unit = TimeUnit.SECONDS;
		
		// 定义一个同步列表 errorList 用于装载异常
		List<Exception> errorList = Collections.synchronizedList(new ArrayList<Exception>());
		
		// 获取管理对象
		DatabasePoolManager manaOld = DatabasePoolManager.getInstance();
		
		// 当前的3个连接池
		assertNotNull(manaOld.getDataPoolByName("unittest_mysql8"));
		assertNotNull(manaOld.getDataPoolByName("unittest_oracle11"));
		assertNotNull(manaOld.getDataPoolByName("unittest_sqlserver2016"));
		
		// 并发测试
		ConcurrencyTestUtil.test(threadNum, timeout, unit, ()->{
			try {
				// 并发执行 多次 
				manaOld.destroyDataPools();
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				} else {
					// 记录其它异常，这里有异常说明 执行 出问题了
					errorList.add(e);
				}
			}
		});
		
		// 统计
		assertEquals(0, errorList.size());
		
		// 连接池对象应该为 null
		assertNull(manaOld.getDataPoolByName("unittest_mysql8"));
		assertNull(manaOld.getDataPoolByName("unittest_oracle11"));
		assertNull(manaOld.getDataPoolByName("unittest_sqlserver2016"));
		
		// 保留对象的引用，可以避免 get 方法出现空指针异常。
		assertNotNull(manaOld);
		
		// 最后再检查一下 mana 对象 和 重新初始化的对象
		DatabasePoolManager manaNew = DatabasePoolManager.getInstance();
		// 引用地址应该不一样
		assertNotEquals(manaOld, manaNew);
		// 重新初始化的连接
		assertNotNull(manaNew.getDataPoolByName("unittest_mysql8"));
		assertNotNull(manaNew.getDataPoolByName("unittest_oracle11"));
		assertNotNull(manaNew.getDataPoolByName("unittest_sqlserver2016"));
		// 原来的链接
		assertNull(manaOld.getDataPoolByName("unittest_mysql8"));
		assertNull(manaOld.getDataPoolByName("unittest_oracle11"));
		assertNull(manaOld.getDataPoolByName("unittest_sqlserver2016"));
		
		// 再销毁一次
		manaNew.destroyDataPools();
		assertNotNull(manaNew);
		assertNull(manaNew.getDataPoolByName("unittest_mysql8"));
		assertNull(manaNew.getDataPoolByName("unittest_oracle11"));
		assertNull(manaNew.getDataPoolByName("unittest_sqlserver2016"));
		
	}
	
	/**
	 * 这里是测试外部会否改变 管理类内部的信息
	 */
	@Test
	@Order(value = 6)
	void lastInstanceTest() {
		
		// 定义一个 DatabasePoolManager 变量
		// 把引用复制给2个变量
		DatabasePoolManager tmp1 = DatabasePoolManager.getInstance();
		DatabasePoolManager tmp2 = DatabasePoolManager.getInstance();
		
		assertNotNull(tmp1);
		assertNotNull(tmp2);
		assertEquals(tmp1, tmp2);
		
		// 外部修改变量为 null
		tmp1 = null;
		assertNotEquals(tmp1, tmp2);
		assertNull(tmp1);
		assertNotNull(tmp2);
		
		// 和 instance 比对(外部只会修改变量的引用，而不是 单例内部的引用)
		assertNotEquals(tmp1, DatabasePoolManager.getInstance());
		assertEquals(tmp2, DatabasePoolManager.getInstance());
		
		// 销毁
		DatabasePoolManager.getInstance().destroyDataPools();
	}
	
	@Test
	@Order(value = 7)
	void lastInstanceCount() {
		
		// 当前测试环境 应该有 6 个 可用的 db 开头的 数据配置文件 
		// 然后 unittest_name5 是空文件
		// 再然后 db_unittest_jar3.properties 的连接池，增加了 test 前缀，会忽略掉。
		
		/*
		 * 注意：对于数据库连接池的初始化，INFO 日志中会描述 扫描到多少个 配置文件，然后 生成了多少个连接池。
		 */
		
		// 剩下应该只有 4 个合理的连接池
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		
		// 正常的配置，生成的连接池实例
		assertNotNull(mana.getDataPoolByName("unittest_mysql8"));
		assertNotNull(mana.getDataPoolByName("unittest_oracle11"));
		assertNotNull(mana.getDataPoolByName("unittest_sqlserver2016"));
		assertNotNull(mana.getDataPoolByName("unittest_jar1_mysql8"));
		
		// 空配置 忽略
		assertNull(mana.getDataPoolByName("unittest_name5"));
		
		// test 前缀的配置 忽略
		assertNull(mana.getDataPoolByName("test_unittest_jar3_mysql8"));
	}
	
}
