/******************************************************************************************************

This file "TimeUtilTester2.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/**
 * 这是时间处理工具 TimeUtil 类的一个测试类。这里进行的是数值正确性测试。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年1月20日-2026年1月27日
 */
public class TimeUtilTester2 {

	/**
	 * 测试 getDefaultZoneId()
	 */
	@Test
	void test1_getDefaultZoneId() {
		// 首先，它不应该是 null 值
		assertNotNull(TimeUtil.getDefaultZoneId());
		// 然后，它应该是当前时区
		assertEquals(TimeUtil.getDefaultZoneId(), ZoneId.of(TimeZone.getDefault().getID()));
	}
	
	/**
	 * 测试 getDefaultValue()
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test2_getDefaultValue() throws Exception {
		// 首先，它不应该是 null 值
		assertNotNull(TimeUtil.getDefaultValue());
		// 然后，它应该是，当前的一个有效的时间字符串
		String nowTime = TimeUtil.getDefaultValue();
		// 格式检查
		assertTrue(Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}", nowTime));
		// 数值检查
		String value1 = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String value2 = TimeUtil.getDefaultValue();
		// 由于第 59 秒 再+1 就要 分钟数 +1 了。所以如果是 59 秒时执行，就等待3秒钟，保证分钟不 +1
		if(LocalDateTime.now().getSecond()>=58) {
			Thread.sleep(3*1000);
		}
		// 由于即时执行，可能有1秒的误差，所以去掉秒值后比较（它们的结果，去掉秒值后，应该是一致的）
		String strVal1 = value1.substring(0, value1.lastIndexOf(":"));
		String strVal2 = value2.substring(0, value2.lastIndexOf(":"));
		Stdout.mylogger.debug("strVal1={}, strVal2={}", strVal1, strVal2);
		assertEquals(strVal1, strVal2);
	}
	
	/**
	 * 测试 getDefaultValue(Date date)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test3_getDefaultValue() throws Exception {
		// 首先，如果参数异常，应该 返回 null，否则应该返回一个非null值
		assertNull(TimeUtil.getDefaultValue(null));
		assertNotNull(TimeUtil.getDefaultValue(new Date()));
		assertTrue(TimeUtil.getDefaultValue(new Date()) instanceof String);
		// 首先，确定一个 Date 对象
		Date date1 = new Date();
		LocalDateTime dateTime1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		// 然后比较，转换后的字符串是否一致
		String fromDate = TimeUtil.getDefaultValue(date1);
		String fromLocalDateTime = dateTime1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		// 格式检查
		assertTrue(Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}", fromDate));
		assertTrue(Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}", fromLocalDateTime));
		// 一致性检查
		assertEquals(fromDate, fromLocalDateTime);
	}
	
	/**
	 * 测试 getDefaultValue(long timeMillis)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test4_getDefaultValue() throws Exception {
		
		// 首先，用 UTC 时间测试一下。它是1970年1月1日0时0分0秒。1970-01-01 00:00:00
		LocalDateTime utcTime = LocalDateTime.parse("1970-01-01T00:00:00.0");
		// 验证 utcTime 是否正确
		assertEquals("1970-01-01T00:00", utcTime.toString());
		
		// 获取当前时区的 offset 是多少。如果是北京时间，则是 +8 小时，如果是其他时区，可能为负数
		int offset = LocalDateTime.now().atZone(ZoneId.systemDefault()).getOffset().getTotalSeconds() / (60*60);
		
		// 格式化对象
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		// 然后，我们开始计算。utc 时间加上时区差，我们这里的时间是多少。
		String strVal1 = utcTime.plusHours(offset).minusDays(1).format(fmt);
		String strVal2 = utcTime.plusHours(offset).format(fmt);
		String strVal3 = utcTime.plusHours(offset).plusDays(1).format(fmt);
		
		// 格式检查
		assertTrue(Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}", strVal1));
		assertTrue(Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}", strVal2));
		assertTrue(Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}", strVal3));
		
		// 这里进行值判断。(根据时区差，判断utc前一天，utc当天，utc后一天)
		assertEquals(strVal1, TimeUtil.getDefaultValue(-(1*24*60*60*1000)));
		assertEquals(strVal2, TimeUtil.getDefaultValue(0));
		assertEquals(strVal3, TimeUtil.getDefaultValue(1*24*60*60*1000));
	}
	
	/**
	 * 测试 getDefaultValueWithMiliseconds()
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test5_getDefaultValueWithMiliseconds() throws Exception {
		// 首先，它不应该是 null 值
		assertNotNull(TimeUtil.getDefaultValueWithMiliseconds());
		// 然后，它应该是，当前的一个有效的时间字符串
		String nowTime = TimeUtil.getDefaultValueWithMiliseconds();
		// 格式检查
		assertTrue(Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}", nowTime));
		// 格式化对象
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		// 数值检查
		String value1 = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(fmt);
		String value2 = TimeUtil.getDefaultValueWithMiliseconds();
		// 由于第 59 秒 再+1 就要 分钟数 +1 了。所以如果是 59 秒时执行，就等待3秒钟，保证分钟不 +1
		if(LocalDateTime.now().getSecond()>=58) {
			Thread.sleep(3*1000);
		}
		// 由于即时执行，可能有1秒的误差，所以去掉秒值后比较（它们的结果，去掉秒值后，应该是一致的）
		String strVal1 = value1.substring(0, value1.lastIndexOf(":"));
		String strVal2 = value2.substring(0, value2.lastIndexOf(":"));
		Stdout.mylogger.debug("strVal1={}, strVal2={}", strVal1, strVal2);
		assertEquals(strVal1, strVal2);
	}
	
	/**
	 * 测试 getDefaultValueWithMiliseconds(Date date)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test6_getDefaultValueWithMiliseconds() throws Exception {
		// 首先，如果参数异常，应该 返回 null，否则应该返回一个非null值
		assertNull(TimeUtil.getDefaultValueWithMiliseconds(null));
		assertNotNull(TimeUtil.getDefaultValueWithMiliseconds(new Date()));
		assertTrue(TimeUtil.getDefaultValueWithMiliseconds(new Date()) instanceof String);
		// 首先，确定一个 Date 对象
		Date date1 = new Date();
		LocalDateTime dateTime1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		// 然后比较，转换后的字符串是否一致
		String fromDate = TimeUtil.getDefaultValueWithMiliseconds(date1);
		String fromLocalDateTime = dateTime1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
		// 格式检查
		assertTrue(Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}", fromDate));
		assertTrue(Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}", fromLocalDateTime));
		// 一致性检查
		assertEquals(fromDate, fromLocalDateTime);
	}
	
	/**
	 * 测试 getDefaultValueWithMiliseconds(long timeMillis)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test7_getDefaultValueWithMiliseconds() throws Exception {
		// 首先，用 UTC 时间测试一下。它是1970年1月1日0时0分0秒。1970-01-01 00:00:00.000
		LocalDateTime utcTime = LocalDateTime.parse("1970-01-01T00:00:00.0");
		// 验证 utcTime 是否正确
		assertEquals("1970-01-01T00:00", utcTime.toString());
		
		// 获取当前时区的 offset 是多少。如果是北京时间，则是 +8 小时，如果是其他时区，可能为负数
		int offset = LocalDateTime.now().atZone(ZoneId.systemDefault()).getOffset().getTotalSeconds() / (60*60);
		
		// 格式化对象
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		
		// 然后，我们开始计算。utc 时间加上时区差，我们这里的时间是多少。
		String strVal1 = utcTime.plusHours(offset).minusDays(1).format(fmt);
		String strVal2 = utcTime.plusHours(offset).format(fmt);
		String strVal3 = utcTime.plusHours(offset).plusDays(1).format(fmt);
		
		// 格式检查
		assertTrue(Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}", strVal1));
		assertTrue(Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}", strVal2));
		assertTrue(Pattern.matches("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}", strVal3));
		
		// 这里进行值判断。(根据时区差，判断utc前一天，utc当天，utc后一天)
		assertEquals(strVal1, TimeUtil.getDefaultValueWithMiliseconds(-(1*24*60*60*1000)));
		assertEquals(strVal2, TimeUtil.getDefaultValueWithMiliseconds(0));
		assertEquals(strVal3, TimeUtil.getDefaultValueWithMiliseconds(1*24*60*60*1000));
	}
	
	/**
	 * 测试 getCustomValue(Date date, String formatStr)
	 * @throws Exception 一些非预期的异常。
	 */
	@SuppressWarnings("deprecation")
	@Test
	void test8_getCustomValue() throws Exception {
		
		// 首先，取一个时间
		Date date1 = new Date();
		
		// 检测参数异常（2个参数，只要有一个参数异常就是 null）
		assertNull(TimeUtil.getCustomValue(null, "yyyy-MM-dd HH:mm:ss.SSS"));
		// 因为 TimeUtil.getCustomValue(Date, null) 这个写法，编译器无法识别具体方法，所以这里用反射处理
		Method m = TimeUtil.class.getDeclaredMethod("getCustomValue", java.util.Date.class, String.class);
		assertNull(m.invoke(null, date1, null));
		Stdout.mylogger.debug(new SimpleDateFormat("@@@").format(date1));
		assertNull(TimeUtil.getCustomValue(date1, ""));
		assertNull(TimeUtil.getCustomValue(date1, "   "));
		
		// 设置一个时间：2026年1月27日5点10分30秒
		LocalDateTime targetTime = LocalDateTime.of(2026, 1, 27, 5, 10, 30);
		long timeMili = targetTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		Date targetDate = new Date(timeMili);
		Stdout.mylogger.debug("targetTime={}, targetDate={}", targetTime, targetDate);
		
		// 转格式
		String targetDateStr = TimeUtil.getCustomValue(targetDate, "yyyy-MM-dd HH:mm:ss");
		
		// 开始校验
		assertEquals("2026-01-27 05:10:30", targetDateStr);
	}
	
	/**
	 * 测试 getCustomValue(Date date, DateTimeFormatter myFmt)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test9_getCustomValue() throws Exception {
		
		// 首先，取一个时间
		Date date1 = new Date();
		
		// 检测参数异常（2个参数，只要有一个参数异常就是 null）
		assertNull(TimeUtil.getCustomValue(null, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		// 因为 TimeUtil.getCustomValue(Date, null) 这个写法，编译器无法识别具体方法，所以这里用反射处理
		Method m = TimeUtil.class.getDeclaredMethod("getCustomValue", java.util.Date.class, DateTimeFormatter.class);
		assertNull(m.invoke(null, date1, null));
		
		// 设置一个时间：2026年1月27日5点10分30秒
		LocalDateTime targetTime = LocalDateTime.of(2026, 1, 27, 5, 10, 30);
		long timeMili = targetTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		Date targetDate = new Date(timeMili);
		Stdout.mylogger.debug("targetTime={}, targetDate={}", targetTime, targetDate);
		
		// 转格式
		String targetDateStr = TimeUtil.getCustomValue(targetDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		// 开始校验
		assertEquals("2026-01-27 05:10:30", targetDateStr);
	}
	
	/**
	 * 测试 getCustomValue(long timeMillis, String formatStr)
	 * @throws Exception 一些非预期的异常。
	 */
	@SuppressWarnings("deprecation")
	@Test
	void test10_getCustomValue() throws Exception {
		
		// 检测参数异常（2个参数，只要有一个参数异常就是 null。因为第一个是 long，所以只需要测试第二个参数 String formatStr）
		Method m = TimeUtil.class.getDeclaredMethod("getCustomValue", long.class, String.class);
		assertNull(m.invoke(null, 1L, null));
		// 继续检测，如果格式化字符串是 空字符的话，结果也应该是 null
		assertNull(TimeUtil.getCustomValue(1L, ""));
		assertNull(TimeUtil.getCustomValue(1L, "    "));
		
		// 设置一个时间：2026年1月27日5点10分30秒
		LocalDateTime targetTime = LocalDateTime.of(2026, 1, 27, 5, 10, 30);
		long timeMili = targetTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		Date targetDate = new Date(timeMili);
		Stdout.mylogger.debug("targetTime={}, targetDate={}", targetTime, targetDate);
		
		// 开始数值测试
		String targetDateStr = TimeUtil.getCustomValue(timeMili, "yyyy-MM-dd HH:mm:ss");
		assertEquals("2026-01-27 05:10:30", targetDateStr);
	}
	
	/**
	 * 测试 getCustomValue(long timeMillis, DateTimeFormatter myFmt)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test11_getCustomValue() throws Exception {
		
		// 检测参数异常（2个参数，只要有一个参数异常就是 null。因为第一个是 long，所以只需要测试第二个参数 DateTimeFormatter myFmt）
		Method m = TimeUtil.class.getDeclaredMethod("getCustomValue", long.class, DateTimeFormatter.class);
		assertNull(m.invoke(null, 1L, null));
		
		// 设置一个时间：2026年1月27日5点10分30秒
		LocalDateTime targetTime = LocalDateTime.of(2026, 1, 27, 5, 10, 30);
		long timeMili = targetTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		Date targetDate = new Date(timeMili);
		Stdout.mylogger.debug("targetTime={}, targetDate={}", targetTime, targetDate);
		
		// 开始数值测试
		String targetDateStr = TimeUtil.getCustomValue(timeMili, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		assertEquals("2026-01-27 05:10:30", targetDateStr);
	}
	
	/**
	 * 测试 getCustomValue(LocalDateTime time, String formatStr, String defaultStr)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test12_getCustomValue() throws Exception {
		
		// 首先是异常值测试。如果转换失败，返回的是 defaultStr 的值。
		assertEquals("无", TimeUtil.getCustomValue(null, "yyyy", "无"));
		assertEquals("无", TimeUtil.getCustomValue(LocalDateTime.now(), null, "无"));
		assertEquals("无", TimeUtil.getCustomValue(LocalDateTime.now(), "", "无"));
		assertEquals("无", TimeUtil.getCustomValue(LocalDateTime.now(), "   ", "无"));
		assertEquals("无", TimeUtil.getCustomValue(LocalDateTime.now(), "aaaaaaa", "无"));
		// 把默认值换成 null 试试
		assertNull(TimeUtil.getCustomValue(null, "yyyy", null));
		assertNull(TimeUtil.getCustomValue(LocalDateTime.now(), null, null));
		assertNull(TimeUtil.getCustomValue(LocalDateTime.now(), "", null));
		assertNull(TimeUtil.getCustomValue(LocalDateTime.now(), "   ", null));
		assertNull(TimeUtil.getCustomValue(LocalDateTime.now(), "aaaaaaa", null));
		
		// 设置一个时间：2026年1月27日5点10分30秒
		LocalDateTime targetTime = LocalDateTime.of(2026, 1, 27, 5, 10, 30);
		
		// 合格值的测试
		assertEquals("2026", TimeUtil.getCustomValue(targetTime, "yyyy", "无"));
		assertEquals("2026-01", TimeUtil.getCustomValue(targetTime, "yyyy-MM", "无"));
		assertEquals("2026-01-27", TimeUtil.getCustomValue(targetTime, "yyyy-MM-dd", "无"));
		assertEquals("2026-01-27 05:10:30", TimeUtil.getCustomValue(targetTime, "yyyy-MM-dd HH:mm:ss", "无"));
	}
	
	/**
	 * 测试 getTimestampByLocalDateTime(LocalDateTime time, ZoneId zone)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test13_getTimestampByLocalDateTime() throws Exception {
		
		// 首先是异常值测试。如果有一个参数异常，返回的是null
		assertNull(TimeUtil.getTimestampByLocalDateTime(null, ZoneId.systemDefault()));
		assertNull(TimeUtil.getTimestampByLocalDateTime(LocalDateTime.now(), null));
		
		// 获取当前时间的时间戳
		long timeMilli = System.currentTimeMillis();
		
		// 生成一个 Timestamp 
		Timestamp expected = new Timestamp(timeMilli);
		// 生成一个 LocalDateTime 
		LocalDateTime targetTime = new Date(timeMilli).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		// 执行函数，进行比对
		assertEquals(expected, TimeUtil.getTimestampByLocalDateTime(targetTime, ZoneId.systemDefault()));
	}
	
	/**
	 * 测试 getTimestampByLocalDateTime(LocalDateTime time)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test14_getTimestampByLocalDateTime() throws Exception {
		
		// 首先是异常值测试。如果有一个参数异常，返回的是null
		assertNull(TimeUtil.getTimestampByLocalDateTime(null));
		
		// 获取当前时间的时间戳
		long timeMilli = System.currentTimeMillis();
		
		// 生成一个 Timestamp 
		Timestamp expected = new Timestamp(timeMilli);
		// 生成一个 LocalDateTime 
		LocalDateTime targetTime = new Date(timeMilli).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		// 执行函数，进行比对
		assertEquals(expected, TimeUtil.getTimestampByLocalDateTime(targetTime));
	}
	
	/**
	 * 测试 parseLocalDate(String dateString, DateTimeFormatter formatter)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test15_parseLocalDate() throws Exception {
		// 首先是异常值测试。如果有一个参数异常，都会抛出异常 DateTimeParseException
		assertThrows(DateTimeParseException.class, ()->{TimeUtil.parseLocalDate(null, DateTimeFormatter.ofPattern("yyyy"));});
		assertThrows(DateTimeParseException.class, ()->{TimeUtil.parseLocalDate("", DateTimeFormatter.ofPattern("yyyy"));});
		assertThrows(DateTimeParseException.class, ()->{TimeUtil.parseLocalDate("     ", DateTimeFormatter.ofPattern("yyyy"));});
		assertThrows(DateTimeParseException.class, ()->{TimeUtil.parseLocalDate("aaaaa", DateTimeFormatter.ofPattern("yyyy"));});
		assertThrows(DateTimeParseException.class, ()->{TimeUtil.parseLocalDate("2025-01-01", null);});
		// 这种单年份的日期，它默认是转不了的。需要特殊的转换格式对象。会抛出 DateTimeParseException
		// 像这种单年份的，它缺少了月份和日期，普通的格式对象转不了的。
		assertThrows(DateTimeParseException.class, ()->{TimeUtil.parseLocalDate("2025", DateTimeFormatter.ofPattern("yyyy"));});
		
		// 下面开始转换 ===== 这里的函数都是可成功执行的，不应该有异常
		
		assertEquals(LocalDate.of(2020, 1, 1), TimeUtil.parseLocalDate("2020-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		
		//无分隔符
		Stdout.mylogger.debug("===========  日期转换，无分隔符  =============");
		assertEquals(LocalDate.of(2020, 1, 1), TimeUtil.parseLocalDate("2020", TimeUtil.FMT_DATE_WTSPLIT));
		assertEquals(LocalDate.of(2020, 11, 1), TimeUtil.parseLocalDate("202011", TimeUtil.FMT_DATE_WTSPLIT));
		assertEquals(LocalDate.of(2020, 12, 5), TimeUtil.parseLocalDate("20201205", TimeUtil.FMT_DATE_WTSPLIT));
		//有分隔符
		Stdout.mylogger.debug("===========  日期转换，有分隔符  =============");
		assertEquals(LocalDate.of(2020, 1, 1), TimeUtil.parseLocalDate("2020-1-1", TimeUtil.FMT_DATE_NORMAL));
		assertEquals(LocalDate.of(2020, 8, 1), TimeUtil.parseLocalDate("2020-8-1", TimeUtil.FMT_DATE_NORMAL));
		assertEquals(LocalDate.of(2020, 8, 21), TimeUtil.parseLocalDate("2020-8-21", TimeUtil.FMT_DATE_NORMAL));
		assertEquals(LocalDate.of(2020, 1, 1), TimeUtil.parseLocalDate("2020/1/1", TimeUtil.FMT_DATE_NORMAL));
		assertEquals(LocalDate.of(2020, 8, 1), TimeUtil.parseLocalDate("2020/8/1", TimeUtil.FMT_DATE_NORMAL));
		assertEquals(LocalDate.of(2020, 8, 21), TimeUtil.parseLocalDate("2020/8/21", TimeUtil.FMT_DATE_NORMAL));
	}
	
	/**
	 * 测试 parseLocalDateWithoutExpt(String dateString, DateTimeFormatter formatter)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test16_parseLocalDateWithoutExpt() throws Exception {
		// 首先是异常值测试。如果有一个参数异常，这里返回的是 null，因为这个函数，并不报错
		assertNull(TimeUtil.parseLocalDateWithoutExpt(null, DateTimeFormatter.ofPattern("yyyy")));
		assertNull(TimeUtil.parseLocalDateWithoutExpt("", DateTimeFormatter.ofPattern("yyyy")));
		assertNull(TimeUtil.parseLocalDateWithoutExpt("     ", DateTimeFormatter.ofPattern("yyyy")));
		assertNull(TimeUtil.parseLocalDateWithoutExpt("aaaaa", DateTimeFormatter.ofPattern("yyyy")));
		assertNull(TimeUtil.parseLocalDateWithoutExpt("2025-01-01", null));
		// 这种单年份的日期，它默认是转不了的。需要特殊的转换格式对象。这里返回的是 null，因为这个函数，并不报错
		// 像这种单年份的，它缺少了月份和日期，普通的格式对象转不了的。
		assertNull(TimeUtil.parseLocalDateWithoutExpt("2025", DateTimeFormatter.ofPattern("yyyy")));
		
		// 下面开始转换 ===== 这里的函数都是可成功执行的，不应该有异常
		
		assertEquals(LocalDate.of(2020, 1, 1), TimeUtil.parseLocalDateWithoutExpt("2020-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		
		//无分隔符
		Stdout.mylogger.debug("===========  日期转换，无分隔符  =============");
		assertEquals(LocalDate.of(2020, 1, 1), TimeUtil.parseLocalDateWithoutExpt("2020", TimeUtil.FMT_DATE_WTSPLIT));
		assertEquals(LocalDate.of(2020, 11, 1), TimeUtil.parseLocalDateWithoutExpt("202011", TimeUtil.FMT_DATE_WTSPLIT));
		assertEquals(LocalDate.of(2020, 12, 5), TimeUtil.parseLocalDateWithoutExpt("20201205", TimeUtil.FMT_DATE_WTSPLIT));
		//有分隔符
		Stdout.mylogger.debug("===========  日期转换，有分隔符  =============");
		assertEquals(LocalDate.of(2020, 1, 1), TimeUtil.parseLocalDateWithoutExpt("2020-1-1", TimeUtil.FMT_DATE_NORMAL));
		assertEquals(LocalDate.of(2020, 8, 1), TimeUtil.parseLocalDateWithoutExpt("2020-8-1", TimeUtil.FMT_DATE_NORMAL));
		assertEquals(LocalDate.of(2020, 8, 21), TimeUtil.parseLocalDateWithoutExpt("2020-8-21", TimeUtil.FMT_DATE_NORMAL));
		assertEquals(LocalDate.of(2020, 1, 1), TimeUtil.parseLocalDateWithoutExpt("2020/1/1", TimeUtil.FMT_DATE_NORMAL));
		assertEquals(LocalDate.of(2020, 8, 1), TimeUtil.parseLocalDateWithoutExpt("2020/8/1", TimeUtil.FMT_DATE_NORMAL));
		assertEquals(LocalDate.of(2020, 8, 21), TimeUtil.parseLocalDateWithoutExpt("2020/8/21", TimeUtil.FMT_DATE_NORMAL));
	}
	
	/**
	 * 测试 parseLocalDateTime(String dateTimeString, DateTimeFormatter formatter)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test17_parseLocalDateTime() throws Exception {
		
		// 测试用的格式描述对象
		DateTimeFormatter myFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		// 首先是异常值测试。如果有一个参数异常，都会抛出异常 DateTimeParseException
		assertThrows(DateTimeParseException.class, ()->{TimeUtil.parseLocalDateTime(null, myFmt);});
		assertThrows(DateTimeParseException.class, ()->{TimeUtil.parseLocalDateTime("", myFmt);});
		assertThrows(DateTimeParseException.class, ()->{TimeUtil.parseLocalDateTime("     ", myFmt);});
		assertThrows(DateTimeParseException.class, ()->{TimeUtil.parseLocalDateTime("aaaaa", myFmt);});
		assertThrows(DateTimeParseException.class, ()->{TimeUtil.parseLocalDateTime("2025-01-01 00:00:00", null);});
		// 它默认是转不了的。需要特殊的转换格式对象。会抛出 DateTimeParseException
		// 这里缺少了 小时，分钟，和秒，转不了的
		assertThrows(DateTimeParseException.class, ()->{TimeUtil.parseLocalDateTime("2025-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));});
		
		// 下面开始转换 ===== 这里的函数都是可成功执行的，不应该有异常
		assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0, 0), 
				TimeUtil.parseLocalDateTime("2020-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		
		//时间转换
		Stdout.pl("===========  时间转换，1  =============");
		assertEquals(LocalDateTime.of(2020, 1, 1, 2, 3, 4), TimeUtil.parseLocalDateTime("2020-1-1 02:03:04", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 0), TimeUtil.parseLocalDateTime("2020-12-13 02:03", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 4), TimeUtil.parseLocalDateTime("2020-12-13 02:03:04", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 1, 1, 2, 3, 4), TimeUtil.parseLocalDateTime("2020-1-1T02:03:04", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 0), TimeUtil.parseLocalDateTime("2020-12-13T02:03", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 4), TimeUtil.parseLocalDateTime("2020-12-13T02:03:04", TimeUtil.FMT_DATETIME_NORMAL));
		//
		Stdout.pl("===========  时间转换，2 时分秒为一个数字的情况 =============");
		assertEquals(LocalDateTime.of(2020, 1, 1, 2, 3, 4), TimeUtil.parseLocalDateTime("2020/1/1 2:3:4", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 0), TimeUtil.parseLocalDateTime("2020/12/13 2:3", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 4), TimeUtil.parseLocalDateTime("2020/12/13 2:3:4", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 1, 1, 2, 3, 4), TimeUtil.parseLocalDateTime("2020/1/1T2:3:4", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 0), TimeUtil.parseLocalDateTime("2020/12/13T2:3", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 4), TimeUtil.parseLocalDateTime("2020/12/13T2:3:4", TimeUtil.FMT_DATETIME_NORMAL));
	}
	
	/**
	 * 测试 parseLocalDateTimeWithoutExpt(String dateTimeString, DateTimeFormatter formatter)
	 * @throws Exception 一些非预期的异常。
	 */
	@Test
	void test18_parseLocalDateTimeWithoutExpt() throws Exception {

		// 测试用的格式描述对象
		DateTimeFormatter myFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		// 首先是异常值测试。如果有一个参数异常，它应该返回 null。这个函数是不抛出异常的。
		assertNull(TimeUtil.parseLocalDateTimeWithoutExpt(null, myFmt));
		assertNull(TimeUtil.parseLocalDateTimeWithoutExpt("", myFmt));
		assertNull(TimeUtil.parseLocalDateTimeWithoutExpt("     ", myFmt));
		assertNull(TimeUtil.parseLocalDateTimeWithoutExpt("aaaaa", myFmt));
		assertNull(TimeUtil.parseLocalDateTimeWithoutExpt("2025-01-01 00:00:00", null));
		// 它默认是转不了的。需要特殊的转换格式对象。应该返回 null。这个函数是不抛出异常的。
		// 这里缺少了 小时，分钟，和秒，转不了的
		assertNull(TimeUtil.parseLocalDateTimeWithoutExpt("2025-01-01", myFmt));
		
		// 下面开始转换 ===== 这里的函数都是可成功执行的，不应该有异常
		assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0, 0), 
				TimeUtil.parseLocalDateTimeWithoutExpt("2020-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		
		//时间转换
		Stdout.pl("===========  时间转换，1  =============");
		assertEquals(LocalDateTime.of(2020, 1, 1, 2, 3, 4), TimeUtil.parseLocalDateTimeWithoutExpt("2020-1-1 02:03:04", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 0), TimeUtil.parseLocalDateTimeWithoutExpt("2020-12-13 02:03", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 4), TimeUtil.parseLocalDateTimeWithoutExpt("2020-12-13 02:03:04", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 1, 1, 2, 3, 4), TimeUtil.parseLocalDateTimeWithoutExpt("2020-1-1T02:03:04", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 0), TimeUtil.parseLocalDateTimeWithoutExpt("2020-12-13T02:03", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 4), TimeUtil.parseLocalDateTimeWithoutExpt("2020-12-13T02:03:04", TimeUtil.FMT_DATETIME_NORMAL));
		//
		Stdout.pl("===========  时间转换，2 时分秒为一个数字的情况 =============");
		assertEquals(LocalDateTime.of(2020, 1, 1, 2, 3, 4), TimeUtil.parseLocalDateTimeWithoutExpt("2020/1/1 2:3:4", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 0), TimeUtil.parseLocalDateTimeWithoutExpt("2020/12/13 2:3", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 4), TimeUtil.parseLocalDateTimeWithoutExpt("2020/12/13 2:3:4", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 1, 1, 2, 3, 4), TimeUtil.parseLocalDateTimeWithoutExpt("2020/1/1T2:3:4", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 0), TimeUtil.parseLocalDateTimeWithoutExpt("2020/12/13T2:3", TimeUtil.FMT_DATETIME_NORMAL));
		assertEquals(LocalDateTime.of(2020, 12, 13, 2, 3, 4), TimeUtil.parseLocalDateTimeWithoutExpt("2020/12/13T2:3:4", TimeUtil.FMT_DATETIME_NORMAL));
	}
	
}
