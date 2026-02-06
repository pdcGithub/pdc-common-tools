/******************************************************************************************************

This file "RandomUtilTester1.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.RepeatedTest;

import net.mickarea.tools.utils.test.ConcurrencyTestUtil;

/**
 * 这是关于 RandomUtil 的单元测试。这里主要是并发测试。简单的参数测试，放在另外一个 tester 类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年2月5日-2026年2月6日
 */
public class RandomUtilTester1 {

	private static final AtomicLong GLOBAL_SEED_COUNTER = new AtomicLong();
	
	/**
	 * 测试 随机种子 是否可能产生 重复记录
	 */
	@RepeatedTest(value = 10, failureThreshold = 1)
	void test_seed() throws Exception {
		// 定义一个线程总数
		int threadCount = 100;
		// 定义一个存储结果的列表
		List<Long> reList = Collections.synchronizedList(new ArrayList<Long>());
		// 开始并发测试
		ConcurrencyTestUtil.test(threadCount, 10, TimeUnit.SECONDS, ()->{
			long time = System.currentTimeMillis();
			long seed = System.nanoTime() ^ (System.identityHashCode(Thread.currentThread()) << 32) ^ GLOBAL_SEED_COUNTER.getAndIncrement();
			
			reList.add(seed);
			Stdout.mylogger.debug("time={}, seed={}", time, seed);
		});
		// 开始过滤
		Map<Long, Integer> newMap = reList.stream()
										.collect(Collectors.toMap(t->t, t->1, (oldVal, newVal)->oldVal+1))
										.entrySet().stream()
										.filter(entry->entry.getValue().intValue()>1)
										.collect(Collectors.toMap(entry->entry.getKey(), entry->entry.getValue()));
		if(newMap.size()>0) {
			Stdout.mylogger.debug("found some error values, {}", newMap);
		}
		assertEquals(0, newMap.size());
	}
	
	/**
	 * 测试 genNumberString 方法。 这里是使用 并发测试，因为只有大量并发访问时，才能测试出问题。
	 */
	@RepeatedTest(value = 10, failureThreshold = 1)
	void test_genNumberString() throws Exception {
		
		// ================================ 首先，定义2个用于装载结果和异常的map
		// 键名为线程号，值为生成的随机数结果
		Map<String, String> reMap = Collections.synchronizedMap(new HashMap<String, String>());
		// 键名为线程号，值为执行时产生的异常
		Map<String, Exception> errMap = Collections.synchronizedMap(new HashMap<String, Exception>());
		
		// 定义并发的线程数
		int threadCount = 100;
		
		// 开始，并发测试
		ConcurrencyTestUtil.test(threadCount, 10, TimeUnit.SECONDS, ()->{
			// 线程号 
			String threadName = Thread.currentThread().getName();
			String num = threadName.substring(threadName.lastIndexOf("-")+1);
			try {
				reMap.put(num, RandomUtil.genNumberString(16));
			}catch(Exception e) {
				// 如果异常是线程中断异常，则标记
				if(e instanceof InterruptedException) Thread.currentThread().interrupt();
				// 放入异常 map
				errMap.put(num, e);
			}
		});
		
		// ================================ 然后，比对执行结果
		if(errMap.size()!=0) {
			// 打印异常信息
			errMap.entrySet().stream().forEach(entry->{
				Stdout.mylogger.debug("在线程 {} 发现异常 {}", entry.getKey(), entry.getValue());
			});
		}
		assertEquals(0, errMap.size());
		// 重复的随机值，统计结果 map
		Map<String, Integer> duplicateMap = reMap.entrySet().stream()
								// 根据 reMap 映射为一个以结果为key的统计信息 map
								.collect(Collectors.toMap(entry->entry.getValue(), entry->1, (oldVal, newVal)->oldVal+1))
								// 因为collect后是map，所以启动 第二个 流
								.entrySet().stream()
								// 过滤出大于1的记录
								.filter(entry->entry.getValue().intValue()>1)
								// 重新收集为 map
								.collect(Collectors.toMap(entry->entry.getKey(), entry->entry.getValue()));
		if(duplicateMap.size()!=0) {
			// 打印重复的ID信息
			reMap.entrySet().stream().forEach(entry->{
				if(duplicateMap.containsKey(entry.getValue())) {
					Stdout.mylogger.debug("在线程 {} 发现重复值 {}", entry.getKey(), entry.getValue());
				}
			});
		}
		assertEquals(0, duplicateMap.size());
	}
	
	/**
	 * 测试 genNumberString 方法。 这里比对的是结果是否符合构造要求
	 */
	@RepeatedTest(value = 10, failureThreshold = 1)
	void test_genNumberString_value() throws Exception {
		// 定义2个列表，用于装载结果
		List<String> reList = Collections.synchronizedList(new ArrayList<String>());
		List<Exception> errList = Collections.synchronizedList(new ArrayList<Exception>());
		// 定义并发的线程数
		int threadCount = 100;
		// 并发生成
		ConcurrencyTestUtil.test(threadCount, 10, TimeUnit.SECONDS, ()->{
			try {
				reList.add(RandomUtil.genNumberString(50));
			}catch(Exception e) {
				errList.add(e);
			}
		});
		// 结果校验
		assertEquals(0, errList.size());
		assertEquals(threadCount, reList.size());
		long matchCount = reList.stream()
								.filter(t->Pattern.matches("\\d{50}", t))
								.count();
		if(matchCount!=threadCount) {
			// 如果有结果不符合要求，这里打印出来
			reList.stream().filter(t->!Pattern.matches("\\d{50}", t))
			.forEach(t->{
				Stdout.mylogger.debug("有个不符合的值 {}", t);
			});
		}
		assertEquals(threadCount, matchCount);
	}
	
	/**
	 * 测试 genLetterString 方法。 这里是使用 并发测试，因为只有大量并发访问时，才能测试出问题。
	 */
	@RepeatedTest(value = 10, failureThreshold = 1)
	void test_genLetterString() throws Exception {

		// ================================ 首先，定义2个用于装载结果和异常的map
		// 键名为线程号，值为生成的随机数结果
		Map<String, String> reMap = Collections.synchronizedMap(new HashMap<String, String>());
		// 键名为线程号，值为执行时产生的异常
		Map<String, Exception> errMap = Collections.synchronizedMap(new HashMap<String, Exception>());
		
		// 定义并发的线程数
		int threadCount = 100;
		
		// 开始，并发测试
		ConcurrencyTestUtil.test(threadCount, 10, TimeUnit.SECONDS, ()->{
			// 线程号 
			String threadName = Thread.currentThread().getName();
			String num = threadName.substring(threadName.lastIndexOf("-")+1);
			try {
				reMap.put(num, RandomUtil.genLetterString(25));
			}catch(Exception e) {
				// 如果异常是线程中断异常，则标记
				if(e instanceof InterruptedException) Thread.currentThread().interrupt();
				// 放入异常 map
				errMap.put(num, e);
			}
		});
		
		// ================================ 然后，比对执行结果
		if(errMap.size()!=0) {
			// 打印异常信息
			errMap.entrySet().stream().forEach(entry->{
				Stdout.mylogger.debug("在线程 {} 发现异常 {}", entry.getKey(), entry.getValue());
			});
		}
		assertEquals(0, errMap.size());
		// 重复的随机值，统计结果 map
		Map<String, Integer> duplicateMap = reMap.entrySet().stream()
								// 根据 reMap 映射为一个以结果为key的统计信息 map
								.collect(Collectors.toMap(entry->entry.getValue(), entry->1, (oldVal, newVal)->oldVal+1))
								// 因为collect后是map，所以启动 第二个 流
								.entrySet().stream()
								// 过滤出大于1的记录
								.filter(entry->entry.getValue().intValue()>1)
								// 重新收集为 map
								.collect(Collectors.toMap(entry->entry.getKey(), entry->entry.getValue()));
		if(duplicateMap.size()!=0) {
			// 打印重复的ID信息
			reMap.entrySet().stream().forEach(entry->{
				if(duplicateMap.containsKey(entry.getValue())) {
					Stdout.mylogger.debug("在线程 {} 发现重复值 {}", entry.getKey(), entry.getValue());
				}
			});
		}
		assertEquals(0, duplicateMap.size());
	}
	
	/**
	 * 测试 genLetterString 方法。 这里比对的是结果是否符合构造要求
	 */
	@RepeatedTest(value = 10, failureThreshold = 1)
	void test_genLetterString_value() throws Exception {
		// 定义2个列表，用于装载结果
		List<String> reList = Collections.synchronizedList(new ArrayList<String>());
		List<Exception> errList = Collections.synchronizedList(new ArrayList<Exception>());
		// 定义并发的线程数
		int threadCount = 100;
		// 并发生成
		ConcurrencyTestUtil.test(threadCount, 10, TimeUnit.SECONDS, ()->{
			try {
				reList.add(RandomUtil.genLetterString(60));
			}catch(Exception e) {
				errList.add(e);
			}
		});
		// 结果校验
		assertEquals(0, errList.size());
		assertEquals(threadCount, reList.size());
		long matchCount = reList.stream()
								.filter(t->Pattern.matches("[A-Z]{60}", t))
								.count();
		if(matchCount!=threadCount) {
			// 如果有结果不符合要求，这里打印出来
			reList.stream().filter(t->!Pattern.matches("[A-Z]{60}", t))
			.forEach(t->{
				Stdout.mylogger.debug("有个不符合的值 {}", t);
			});
		}
		assertEquals(threadCount, matchCount);
	}
	
	/**
	 * 测试 genNumAndLetterMixedString 方法。 这里是使用 并发测试，因为只有大量并发访问时，才能测试出问题。
	 */
	@RepeatedTest(value = 10, failureThreshold = 1)
	void test_genNumAndLetterMixedString() throws Exception {
		
		// ================================ 首先，定义2个用于装载结果和异常的map
		// 键名为线程号，值为生成的随机数结果
		Map<String, String> reMap = Collections.synchronizedMap(new HashMap<String, String>());
		// 键名为线程号，值为执行时产生的异常
		Map<String, Exception> errMap = Collections.synchronizedMap(new HashMap<String, Exception>());
		
		// 定义并发的线程数
		int threadCount = 100;
		
		// 开始，并发测试
		ConcurrencyTestUtil.test(threadCount, 10, TimeUnit.SECONDS, ()->{
			// 线程号 
			String threadName = Thread.currentThread().getName();
			String num = threadName.substring(threadName.lastIndexOf("-")+1);
			try {
				reMap.put(num, RandomUtil.genNumAndLetterMixedString(20));
			}catch(Exception e) {
				// 如果异常是线程中断异常，则标记
				if(e instanceof InterruptedException) Thread.currentThread().interrupt();
				// 放入异常 map
				errMap.put(num, e);
			}
		});
		
		// ================================ 然后，比对执行结果
		if(errMap.size()!=0) {
			// 打印异常信息
			errMap.entrySet().stream().forEach(entry->{
				Stdout.mylogger.debug("在线程 {} 发现异常 {}", entry.getKey(), entry.getValue());
			});
		}
		assertEquals(0, errMap.size());
		// 重复的随机值，统计结果 map
		Map<String, Integer> duplicateMap = reMap.entrySet().stream()
								// 根据 reMap 映射为一个以结果为key的统计信息 map
								.collect(Collectors.toMap(entry->entry.getValue(), entry->1, (oldVal, newVal)->oldVal+1))
								// 因为collect后是map，所以启动 第二个 流
								.entrySet().stream()
								// 过滤出大于1的记录
								.filter(entry->entry.getValue().intValue()>1)
								// 重新收集为 map
								.collect(Collectors.toMap(entry->entry.getKey(), entry->entry.getValue()));
		if(duplicateMap.size()!=0) {
			// 打印重复的ID信息
			reMap.entrySet().stream().forEach(entry->{
				if(duplicateMap.containsKey(entry.getValue())) {
					Stdout.mylogger.debug("在线程 {} 发现重复值 {}", entry.getKey(), entry.getValue());
				}
			});
		}
		assertEquals(0, duplicateMap.size());
	}
	
	/**
	 * 测试 genNumAndLetterMixedString 方法。 这里比对的是结果是否符合构造要求
	 */
	@RepeatedTest(value = 10, failureThreshold = 1)
	void test_genNumAndLetterMixedString_value() throws Exception {
		// 定义2个列表，用于装载结果
		List<String> reList = Collections.synchronizedList(new ArrayList<String>());
		List<Exception> errList = Collections.synchronizedList(new ArrayList<Exception>());
		// 定义并发的线程数
		int threadCount = 100;
		// 并发生成
		ConcurrencyTestUtil.test(threadCount, 10, TimeUnit.SECONDS, ()->{
			try {
				reList.add(RandomUtil.genNumAndLetterMixedString(40));
			}catch(Exception e) {
				errList.add(e);
			}
		});
		// 结果校验
		assertEquals(0, errList.size());
		assertEquals(threadCount, reList.size());
		long matchCount = reList.stream()
								.filter(t->Pattern.matches("[0-9A-Z]{40}", t))
								.count();
		if(matchCount!=threadCount) {
			// 如果有结果不符合要求，这里打印出来
			reList.stream().filter(t->!Pattern.matches("[0-9A-Z]{40}", t))
			.forEach(t->{
				Stdout.mylogger.debug("有个不符合的值 {}", t);
			});
		}
		assertEquals(threadCount, matchCount);
	}
	
	/**
	 * 测试 genNumberStringWithoutException 方法。 这些方法都是在原方法的基础上处理，一般不会有什么问题。
	 * 长度足够长的话，应该不会重复的。
	 */
	@RepeatedTest(value = 10, failureThreshold = 1)
	void test_genNumberStringWithoutException() throws Exception {
		// 定义1个列表，用于装载结果
		List<String> reList = Collections.synchronizedList(new ArrayList<String>());
		// 
		int threadCount = 100;
		// 
		ConcurrencyTestUtil.test(threadCount, 10, TimeUnit.SECONDS, ()->{
			// 这里不会报错的，放进去，然后比较结果就行了
			reList.add(RandomUtil.genNumberStringWithoutException(20));
		});
		// 结果比对
		assertEquals(threadCount, reList.stream().filter(t->!StrUtil.isEmptyString(t))
												.filter(t->Pattern.matches("\\d{20}", t))
												.distinct()
												.count());
	}
	
	/**
	 * 测试 genLetterStringWithoutException 方法。 这些方法都是在原方法的基础上处理，一般不会有什么问题。
	 * 长度足够长的话，应该不会重复的。
	 */
	@RepeatedTest(value = 10, failureThreshold = 1)
	void test_genLetterStringWithoutException() throws Exception {
		// 定义1个列表，用于装载结果
		List<String> reList = Collections.synchronizedList(new ArrayList<String>());
		// 
		int threadCount = 100;
		// 
		ConcurrencyTestUtil.test(threadCount, 10, TimeUnit.SECONDS, ()->{
			// 这里不会报错的，放进去，然后比较结果就行了
			reList.add(RandomUtil.genLetterStringWithoutException(30));
		});
		// 结果比对
		assertEquals(threadCount, reList.stream().filter(t->!StrUtil.isEmptyString(t))
												.filter(t->Pattern.matches("[A-Z]{30}", t))
												.distinct()
												.count());
	}
	
	/**
	 * 测试 genNumAndLetterMixedStringWithoutException 方法。 这些方法都是在原方法的基础上处理，一般不会有什么问题。
	 * 长度足够长的话，应该不会重复的。
	 */
	@RepeatedTest(value = 10, failureThreshold = 1)
	void test_genNumAndLetterMixedStringWithoutException() throws Exception {
		// 定义1个列表，用于装载结果
		List<String> reList = Collections.synchronizedList(new ArrayList<String>());
		// 
		int threadCount = 100;
		// 
		ConcurrencyTestUtil.test(threadCount, 10, TimeUnit.SECONDS, ()->{
			// 这里不会报错的，放进去，然后比较结果就行了
			reList.add(RandomUtil.genNumAndLetterMixedStringWithoutException(40));
		});
		// 结果比对
		assertEquals(threadCount, reList.stream().filter(t->!StrUtil.isEmptyString(t))
												.filter(t->Pattern.matches("[A-Z0-9]{40}", t))
												.distinct()
												.count());
	}
}
