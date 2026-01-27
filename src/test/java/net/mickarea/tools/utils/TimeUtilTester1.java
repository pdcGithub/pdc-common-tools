/******************************************************************************************************

This file "TimeUtilTester1.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import net.mickarea.tools.utils.test.ConcurrencyTestUtil;

/**
 * 这是时间处理工具 TimeUtil 类的一个测试类。这里进行的是线程并发测试。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年1月20日-2026年1月22日
 */
public class TimeUtilTester1 {
	
	// 定义一些测试用的时间对象
	
	private String date1Str ;
	private String date2Str ;
	
	private LocalDate date1 ;
	private LocalDate date2 ;
	
	private Date oldDate1 ;
	private Date oldDate2 ;
	
	private LocalDateTime time1;
	private LocalDateTime time2;
	
	private long time1Long;
	private long time2Long;
	
	private Timestamp time1Stamp ;
	private Timestamp time2Stamp ;
	
	/**
	 * 每一轮测试，都要执行的处理
	 */
	@BeforeEach
	void beforeEach() {
		// 初始化字符串
		this.date1Str = "2025-01-01";
		this.date2Str = "2012-12-21";
		// 初始化时间
		this.date1 = LocalDate.parse(this.date1Str);
		this.date2 = LocalDate.parse(this.date2Str);
		// 将 LocalDate 转 java.util.Date
		this.oldDate1 = Date.from(this.date1.atStartOfDay(ZoneId.systemDefault()).toInstant());
		this.oldDate2 = Date.from(this.date2.atStartOfDay(ZoneId.systemDefault()).toInstant());
		// 从原有日期，提取0点时间
		this.time1 = date1.atStartOfDay();
		this.time2 = date2.atStartOfDay();
		// 将 时间1 和 时间2 提取为 long 时间戳
		this.time1Long = this.time1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		this.time2Long = this.time2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		// 将 time1 和 time2 转换为 Timestamp 类型
		this.time1Stamp = Timestamp.valueOf(this.time1);
		this.time2Stamp = Timestamp.valueOf(this.time2);
		// 这里打印所用到的数据，用于测试对照
		Stdout.mylogger.debug("date1={}, date2={}, \noldDate1={}, oldDate2={},\n"
				+ "time1={}, time2={}, \ntime1Long={}, time2Long={}, \n"
				+ "time1Stamp={}, time2Stamp={}", 
				this.date1, this.date2, this.oldDate1, this.oldDate2, 
				this.time1, this.time2, this.time1Long, this.time2Long,
				this.time1Stamp, this.time2Stamp);
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，getDefaultZoneId() 方法得出的结果是否正常。我们这里主要测试，它返回的结果是否一致
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest0GetDefaultZoneId1() throws Exception {
		int threadNum = 100;
		//
		List<ZoneId> reList = Collections.synchronizedList(new ArrayList<ZoneId>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			try {
				Thread.sleep(6);
				reList.add(TimeUtil.getDefaultZoneId());
				Thread.sleep(6);
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getDefaultValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 然后执行结果应该没有 null
		assertEquals(0, reList.stream().distinct().filter(t->t==null).count());
		// 然后应该都是一个值
		assertEquals(1, reList.stream().distinct().count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，getDefaultValue(long) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest1GetDefaultValue1() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				String re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.getDefaultValue(this.time1Long);
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.getDefaultValue(this.time2Long);
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getDefaultValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		assertEquals(threadNum, reList.stream().filter(result->!StrUtil.isEmptyString((String)result.result)).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = "2025-01-01 00:00:00".equals(result.result);
			if(result.targetNum==2) re = "2012-12-21 00:00:00".equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，getDefaultValue(Date) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest1GetDefaultValue2() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				String re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.getDefaultValue(this.oldDate1);
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.getDefaultValue(this.oldDate2);
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getDefaultValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		assertEquals(threadNum, reList.stream().filter(result->!StrUtil.isEmptyString((String)result.result)).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = "2025-01-01 00:00:00".equals(result.result);
			if(result.targetNum==2) re = "2012-12-21 00:00:00".equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，getDefaultValueWithMiliseconds(long) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest2GetDefaultValueWithMiliseconds1() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				String re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.getDefaultValueWithMiliseconds(this.time1Long);
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.getDefaultValueWithMiliseconds(this.time2Long);
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getDefaultValueWithMiliseconds 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		assertEquals(threadNum, reList.stream().filter(result->!StrUtil.isEmptyString((String)result.result)).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = "2025-01-01 00:00:00.000".equals(result.result);
			if(result.targetNum==2) re = "2012-12-21 00:00:00.000".equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，getDefaultValueWithMiliseconds(Date) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest2GetDefaultValueWithMiliseconds2() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				String re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.getDefaultValueWithMiliseconds(this.oldDate1);
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.getDefaultValueWithMiliseconds(this.oldDate2);
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getDefaultValueWithMiliseconds 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		assertEquals(threadNum, reList.stream().filter(result->!StrUtil.isEmptyString((String)result.result)).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = "2025-01-01 00:00:00.000".equals(result.result);
			if(result.targetNum==2) re = "2012-12-21 00:00:00.000".equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，getCustomValue(Date date, String formatStr) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@SuppressWarnings("deprecation")
	@RepeatedTest(value = 3)
	void concurrTest3GetCustomValue1() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				String re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.getCustomValue(this.oldDate1, "yyyy-MM-dd");
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.getCustomValue(this.oldDate2, "yyyy-MM-dd");
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getCustomValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		assertEquals(threadNum, reList.stream().filter(result->!StrUtil.isEmptyString((String)result.result)).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = "2025-01-01".equals(result.result);
			if(result.targetNum==2) re = "2012-12-21".equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，getCustomValue(long timeMillis, String formatStr) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@SuppressWarnings("deprecation")
	@RepeatedTest(value = 3)
	void concurrTest3GetCustomValue2() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				String re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.getCustomValue(this.time1Long, "yyyy-MM-dd");
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.getCustomValue(this.time2Long, "yyyy-MM-dd");
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getCustomValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		assertEquals(threadNum, reList.stream().filter(result->!StrUtil.isEmptyString((String)result.result)).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = "2025-01-01".equals(result.result);
			if(result.targetNum==2) re = "2012-12-21".equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，getCustomValue(LocalDateTime time, String formatStr, String defaultStr) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest3GetCustomValue3() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				String re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.getCustomValue(this.time1, "yyyy-MM-dd", "无1");
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.getCustomValue(this.time2, "yyyy-MM-dd", "无2");
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getCustomValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		assertEquals(threadNum, reList.stream().filter(result->!StrUtil.isEmptyString((String)result.result)).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = "2025-01-01".equals(result.result);
			if(result.targetNum==2) re = "2012-12-21".equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，getCustomValue(Date date, DateTimeFormatter myFmt) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest3GetCustomValue4() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				String re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.getCustomValue(this.oldDate1, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.getCustomValue(this.oldDate2, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getCustomValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		assertEquals(threadNum, reList.stream().filter(result->!StrUtil.isEmptyString((String)result.result)).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = "2025-01-01".equals(result.result);
			if(result.targetNum==2) re = "2012-12-21".equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，getCustomValue(long timeMillis, DateTimeFormatter myFmt) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest3GetCustomValue5() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				String re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.getCustomValue(this.time1Long, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.getCustomValue(this.time2Long, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getCustomValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		assertEquals(threadNum, reList.stream().filter(result->!StrUtil.isEmptyString((String)result.result)).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = "2025-01-01".equals(result.result);
			if(result.targetNum==2) re = "2012-12-21".equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，getTimestampByLocalDateTime(LocalDateTime time, ZoneId zone) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest4GetTimestampByLocalDateTime1() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				String re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.getTimestampByLocalDateTime(this.time1, ZoneId.systemDefault()).toString();
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.getTimestampByLocalDateTime(this.time2, ZoneId.systemDefault()).toString();
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getCustomValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		assertEquals(threadNum, reList.stream().filter(result->!StrUtil.isEmptyString((String)result.result)).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = "2025-01-01 00:00:00.0".equals(result.result);
			if(result.targetNum==2) re = "2012-12-21 00:00:00.0".equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，getTimestampByLocalDateTime(LocalDateTime time) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest4GetTimestampByLocalDateTime2() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				String re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.getTimestampByLocalDateTime(this.time1).toString();
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.getTimestampByLocalDateTime(this.time2).toString();
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getCustomValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		assertEquals(threadNum, reList.stream().filter(result->!StrUtil.isEmptyString((String)result.result)).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = "2025-01-01 00:00:00.0".equals(result.result);
			if(result.targetNum==2) re = "2012-12-21 00:00:00.0".equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，parseLocaDate(String dateString, DateTimeFormatter formatter) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest5ParseLocaDate1() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				Object re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.parseLocalDate(this.date1Str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.parseLocalDate(this.date2Str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getCustomValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		// 这里的结果是 LocalDate 不是 String
		assertEquals(threadNum, reList.stream().filter(result->result.result!=null).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = this.date1.equals(result.result);
			if(result.targetNum==2) re = this.date2.equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，parseLocalDateWithoutExpt(String dateString, DateTimeFormatter formatter) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest5ParseLocalDateWithoutExpt1() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				Object re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.parseLocalDateWithoutExpt(this.date1Str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.parseLocalDateWithoutExpt(this.date2Str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getCustomValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		// 这里的结果是 LocalDate 不是 String
		assertEquals(threadNum, reList.stream().filter(result->result.result!=null).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = this.date1.equals(result.result);
			if(result.targetNum==2) re = this.date2.equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，parseLocalDateTime(String dateTimeString, DateTimeFormatter formatter) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest6ParseLocalDateTime1() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				Object re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.parseLocalDateTime(this.date1Str+" 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.parseLocalDateTime(this.date2Str+" 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getCustomValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		// 这里的结果是 LocalDateTime 不是 String
		assertEquals(threadNum, reList.stream().filter(result->result.result!=null).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = this.time1.equals(result.result);
			if(result.targetNum==2) re = this.time2.equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
	
	/**
	 * 这里是一个并发线程测试。看看并发情况下，parseLocalDateTimeWithoutExpt(String dateTimeString, DateTimeFormatter formatter) 方法得出的结果是否正常。
	 * 这里使用了 RepeatedTest ，多跑几次，看看他是否会出错
	 */
	@RepeatedTest(value = 3)
	void concurrTest6ParseLocalDateTimeWithoutExpt1() throws Exception {
		// 定义2个同步的列表对象，记录操作结果
		int threadNum = 100;
		// 这个记录运行结果
		List<TestResult> reList = Collections.synchronizedList(new ArrayList<TestResult>());
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		//
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			// 这里根据线程号，交替格式化这2个值，看结果是否正确。
			try {
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int targetNum = Integer.parseInt(num)%2+1; // 在 1 和 2 之间随机
				//
				Object re = null;
				// 如果是 1号签，则执行 时间 1 的格式化
				if(1==targetNum) re = TimeUtil.parseLocalDateTimeWithoutExpt(this.date1Str+" 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				// 如果是 2号签，则执行 时间 2 的格式化
				if(2==targetNum) re = TimeUtil.parseLocalDateTimeWithoutExpt(this.date2Str+" 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				// 记录结果
				reList.add(new TestResult(num, targetNum, re));
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录其它异常，这里有异常说明 TimeUtil.getCustomValue 转换出问题了
					excepList.add(e);
				}
				// 打印异常信息
				Stdout.mylogger.error("出现异常, "+e);
			}
		});
		
		// 结果比对 ================== 理论上来说，应该没有异常，然后信息是匹配的
		assertEquals(0, excepList.size());
		// 校验 单个信息是否正常
		assertEquals(threadNum, reList.stream().filter(result->result.threadNum!=null && Pattern.matches("\\d+", result.threadNum)).count());	
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum==1 || result.targetNum==2).count());
		// 这里的结果是 LocalDateTime 不是 String
		assertEquals(threadNum, reList.stream().filter(result->result.result!=null).count());
		// 检测 抽签的结果，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->result.targetNum == Integer.parseInt(result.threadNum)%2+1).count());
		// 查看 获取的结果字符串，是否匹配
		assertEquals(threadNum, reList.stream().filter(result->{
			boolean re = true;
			if(result.targetNum==1) re = this.time1.equals(result.result);
			if(result.targetNum==2) re = this.time2.equals(result.result);
			if(re==false) {
				Stdout.mylogger.debug("value is not match: {}", result);
			}
			return re;
		}).count());
	}
}

/**
 * 这个是用于记录线程执行结果的一个对象。为了方便测试处理，所以直接把成员属性设置为public
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年1月20日
 */
class TestResult {
	/**
	 * 线程号字符串
	 */
	public String threadNum ;
	/**
	 * 抽签的签号。这里一般就是 1 和 2
	 */
	public int targetNum;
	/**
	 * 执行后的字符串结果
	 */
	public Object result ;
	
	/**
	 * 这个是用于记录线程执行结果的一个对象。这是构造函数。
	 * @param threadNum 线程号字符串
	 * @param targetNum 线程抽签结果
	 * @param result 执行结果
	 */
	public TestResult(String threadNum, int targetNum, Object result) {
		super();
		this.threadNum = threadNum;
		this.targetNum = targetNum;
		this.result = result;
	}
	
	@Override
	public String toString() {
		return StrUtil.getJavaBeanFieldsInfo(this);
	}
}