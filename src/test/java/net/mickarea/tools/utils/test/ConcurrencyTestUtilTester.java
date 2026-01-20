/******************************************************************************************************

This file "ConcurrencyTestUtilTester.java" is part of project "myservice" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import net.mickarea.tools.utils.Stdout;

/**
 * 这是 ConcurrencyTestUtil 类的一个单元测试类。主要是测试 test 方法
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年1月9日-2026年1月20日
 */
public class ConcurrencyTestUtilTester {

	@Test
	void testRunnable1() {
		
		// 参数错误测试 threadNum 
		assertThrows(IllegalArgumentException.class, ()->{
			ConcurrencyTestUtil.test(-1, 10, TimeUnit.SECONDS, ()->{});
		});
		assertThrows(IllegalArgumentException.class, ()->{
			ConcurrencyTestUtil.test(0, 10, TimeUnit.SECONDS, ()->{});
		});
		// 参数错误测试 timeout 
		assertThrows(IllegalArgumentException.class, ()->{
			ConcurrencyTestUtil.test(1, -1, TimeUnit.SECONDS, ()->{});
		});
		assertThrows(IllegalArgumentException.class, ()->{
			ConcurrencyTestUtil.test(1, 0, TimeUnit.SECONDS, ()->{});
		});
		// 参数错误测试 timeoutUnit
		assertThrows(IllegalArgumentException.class, ()->{
			ConcurrencyTestUtil.test(1, 1, null, ()->{});
		});
		// 参数错误测试 runnableTask 
		assertThrows(IllegalArgumentException.class, ()->{
			ConcurrencyTestUtil.test(1, 1, TimeUnit.SECONDS, null);
		});
	}
	
	@Test
	void testRunnable2() {
		
		// 正确参数测试
		assertDoesNotThrow(()->{
			ConcurrencyTestUtil.test(1, 1, TimeUnit.SECONDS, ()->{
				Stdout.mylogger.debug("testing ...1");
			});
			ConcurrencyTestUtil.test(5, 1, TimeUnit.SECONDS, ()->{
				Stdout.mylogger.debug("testing ...2");
			});
		});
		
		// 正常的参数下，中断异常检测
		assertThrows(InterruptedException.class, ()->{
			// 定义一个容器来存储结果
			List<Exception> errList = Collections.synchronizedList(new ArrayList<Exception>());
			// 开始处理
			ConcurrencyTestUtil.test(5, 1, TimeUnit.MILLISECONDS, ()->{
				// 这里，由于等待任务完成时，时限小于运行时间，在 await 的地方，会直接强制关闭任务池，全部中断。
				try {
					Thread.sleep(100); // 这里模拟需要运行 100 毫秒。但是，等待阈值是 1 毫秒。线程池会直接中断。
				} catch (Exception e) {
					errList.add(e);
				}
			});
			// 结果检测
			boolean hasInterruptedException = errList.stream()
													.filter(e->e instanceof InterruptedException)
													.distinct()
													.collect(Collectors.toList()).size()>0;
			// 如果有这个异常，则抛出一个给测试程序
			if(hasInterruptedException) throw errList.get(0);
		});
	}
	
	/**
	 * 这里做一个线程匹配测试。在并发启动时，它应该是有对应个数的线程的。重复执行3次
	 * @throws Exception 
	 */
	@RepeatedTest(failureThreshold = 1, value = 3)
	void testRunnable3() throws Exception {
		// 用一个同步的列表，装载线程序号
		List<Integer> reList = Collections.synchronizedList(new ArrayList<Integer>());
		// 开始并发测试
		ConcurrencyTestUtil.test(50, 10, TimeUnit.SECONDS, ()->{
			// 线程号 
			String threadName = Thread.currentThread().getName();
			String num = threadName.substring(threadName.lastIndexOf("-")+1);
			// 放入同步列表
			reList.add(Integer.parseInt(num));
		});
		// 判断是否有50个线程在跑
		assertEquals(50, reList.stream().distinct().count());
		// 然后把结果按顺序输出
		Stdout.mylogger.debug("reList={}",reList.stream().distinct().sorted().collect(Collectors.toList()));
	}
	
	/**
	 * 这里做一个线程安全测试，如果他真的是并发的，那么 map 中的数据一定不会跟 总线程数保持一致。可以多测试几遍
	 * @throws Exception
	 */
	@RepeatedTest(failureThreshold = 3, value=5)
	void testRunnable4() throws Exception {
		// 定义一个非同步的map
		Map<String, Integer> normalMap = new HashMap<String, Integer>();
		// 定义一个同步的map
		Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<String, Integer>());
		
		// 开始并发测试
		ConcurrencyTestUtil.test(500, 10, TimeUnit.SECONDS, ()->{
			// 线程号 
			String threadName = Thread.currentThread().getName();
			String num = threadName.substring(threadName.lastIndexOf("-")+1);
			// 根据 map 的情况判断，是否放入
			if(!normalMap.containsKey(num)) normalMap.put(num, Integer.parseInt(num));
			if(!syncMap.containsKey(num)) syncMap.put(num, Integer.parseInt(num));
		});
		
		// 结果统计 ，非同步的map，应该是小于 500 的
		assertNotEquals(500, normalMap.keySet().stream().distinct().count());
		// 结果统计，同步的map，结果一定是500的
		assertEquals(500, syncMap.keySet().stream().distinct().count());
		//
		Stdout.mylogger.debug("normal:{}, sync:{}", 
				normalMap.keySet().stream().distinct().count(), 
				syncMap.keySet().stream().distinct().count());
	}
	
}
