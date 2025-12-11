/******************************************************************************************************

This file "StrUtilTester2.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 这是一个关于 StrUtil 的单元测类 （主要测试 autoConvert 方法的处理）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年12月10日-2025年12月11日
 */
public class StrUtilTester2 {

	/**
	 * 定义一个颜色的枚举类
	 * @author Michael Pang (Dongcan Pang)
	 * @version 1.0
	 * @since 2025年12月10日
	 */
	enum Color {
	    RED, GREEN, BLUE, WHITE;
	}
	
	@BeforeEach
	void inits() {
		// 这是每次都执行的初始化处理
	}
	
	@AfterEach
	void finals() {
		// 这是每次都执行的最终处理
	}
	
	// 主要的测试有 byte, short, int, long, float, double, boolean, char 基础类型 以及它们的引用类型。
	// 再要测试的有 LocalDate, LocalDateTime, String, Object 这些对象。
	// 最后要测试的 一些无法映射的类，比如：StrUtilTester2
	// 当然，还有一些 超过最大值的处理
	
	@Test
	void toByte1() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("123", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(null, byte.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("", byte.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("\r\t", byte.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", byte.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("1", byte.class, null);
			Object result2 = StrUtil.autoConvert("   2    ", byte.class, null);
			assertEquals(new Byte("1").byteValue(), result1);
			assertEquals(new Byte("2").byteValue(), result2);
		});
	}
	
	@Test
	void toByte2() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("123", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(null, Byte.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("", Byte.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("\r\t", Byte.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", Byte.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("1", Byte.class, null);
			Object result2 = StrUtil.autoConvert("   2    ", Byte.class, null);
			assertEquals(new Byte("1"), result1);
			assertEquals(new Byte("2"), result2);
		});
	}
	
	@Test
	void toShort1() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("123", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(null, short.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("", short.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("\r\t", short.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", short.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("1", short.class, null);
			Object result2 = StrUtil.autoConvert("   2    ", short.class, null);
			assertEquals(new Short("1").shortValue(), result1);
			assertEquals(new Short("2").shortValue(), result2);
		});
	}
	
	@Test
	void toShort2() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("123", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(null, Short.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("", Short.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("\r\t", Short.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", Short.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("1", Short.class, null);
			Object result2 = StrUtil.autoConvert("   2    ", Short.class, null);
			assertEquals(new Short("1"), result1);
			assertEquals(new Short("2"), result2);
		});
	}
	
	@Test
	void toInt() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("123", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(null, int.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("", int.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("\r\t", int.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", int.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("1", int.class, null);
			Object result2 = StrUtil.autoConvert("   2    ", int.class, null);
			assertEquals(1, result1);
			assertEquals(2, result2);
		});
	}
	
	@Test
	void toInteger() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("123", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(null, Integer.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("", Integer.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("\r\t", Integer.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", Integer.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("1", Integer.class, null);
			Object result2 = StrUtil.autoConvert("   2    ", Integer.class, null);
			assertEquals(new Integer(1), result1);
			assertEquals(new Integer(2), result2);
		});
	}
	
	@Test
	void toLong1() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("123456", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(null, long.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("", long.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("\r\t", long.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", long.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("1", long.class, null);
			Object result2 = StrUtil.autoConvert("   2    ", long.class, null);
			assertEquals(1L, result1);
			assertEquals(2L, result2);
		});
	}
	
	@Test
	void toLong2() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("123456", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(null, Long.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("", Long.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("\r\t", Long.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", Long.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("1", Long.class, null);
			Object result2 = StrUtil.autoConvert("   2    ", Long.class, null);
			assertEquals(new Long(1), result1);
			assertEquals(new Long(2), result2);
		});
	}
	
	@Test
	void toFloat1() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("1.2345", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(null, float.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("", float.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("\r\t", float.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", float.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("1.2345", float.class, null);
			Object result2 = StrUtil.autoConvert("   5.4321    ", float.class, null);
			assertEquals(1.2345f, result1);
			assertEquals(5.4321f, result2);
		});
	}
	
	@Test
	void toFloat2() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("1.2345", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(null, Float.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("", Float.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("\r\t", Float.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", Float.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("1.2345", Float.class, null);
			Object result2 = StrUtil.autoConvert("   5.4321    ", Float.class, null);
			assertEquals(new Float("1.2345"), result1);
			assertEquals(new Float("5.4321"), result2);
		});
	}
	
	@Test
	void toDouble1() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("1.2345", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(null, double.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("", double.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("\r\t", double.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", double.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("1.2345", double.class, null);
			Object result2 = StrUtil.autoConvert("   5.4321    ", double.class, null);
			assertEquals(1.2345, result1);
			assertEquals(5.4321, result2);
		});
	}
	
	@Test
	void toDouble2() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("1.2345", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(null, Double.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("", Double.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("\r\t", Double.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 NumberFormatException
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", Double.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("1.2345", Double.class, null);
			Object result2 = StrUtil.autoConvert("   5.4321    ", Double.class, null);
			assertEquals(new Double("1.2345"), result1);
			assertEquals(new Double("5.4321"), result2);
		});
	}
	
	@Test
	void toBoolean1() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("true", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert(null, boolean.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("", boolean.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("\r\t", boolean.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", boolean.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("TRUE", boolean.class, null);
			Object result2 = StrUtil.autoConvert("   TRue    ", boolean.class, null);
			Object result3 = StrUtil.autoConvert("False", boolean.class, null);
			Object result4 = StrUtil.autoConvert("   FALSE    ", boolean.class, null);
			assertEquals(true, result1);
			assertEquals(true, result2);
			assertEquals(false, result3);
			assertEquals(false, result4);
		});
	}
	
	@Test
	void toBoolean2() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("true", null, "");
		});
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert(null, Boolean.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("", Boolean.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("\r\t", Boolean.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", Boolean.class, null);
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			Object result1 = StrUtil.autoConvert("TRUE", Boolean.class, null);
			Object result2 = StrUtil.autoConvert("   TRue    ", Boolean.class, null);
			Object result3 = StrUtil.autoConvert("False", Boolean.class, null);
			Object result4 = StrUtil.autoConvert("   FALSE    ", Boolean.class, null);
			assertEquals(new Boolean("true"), result1);
			assertEquals(new Boolean("true"), result2);
			assertEquals(new Boolean("false"), result3);
			assertEquals(new Boolean("false"), result4);
		});
	}
	
	@Test
	void toChar1() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("a", null, "");
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			// 如果是 null ，返回 '\0'
			assertEquals('\0', StrUtil.autoConvert(null, char.class, null));
			// 如果是 空 ，返回 '\0'
			assertEquals('\0', StrUtil.autoConvert("", char.class, null));
			// 如果是 空白字符应该直接返回第一个
			assertEquals('\r', StrUtil.autoConvert("\r\t", char.class, null));
			// 如果是 有值的字符串，应该直接返回第一个
			assertEquals('F', StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", char.class, null));
		});
	}
	
	@Test
	void toChar2() {
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("a", null, "");
		});
		// 这里最后，测试正确的返回
		assertDoesNotThrow(()->{
			// 如果是 null ，返回 '\0'
			assertEquals(new Character('\0'), StrUtil.autoConvert(null, Character.class, null));
			// 如果是 空 ，返回 '\0'
			assertEquals(new Character('\0'), StrUtil.autoConvert("", Character.class, null));
			// 如果是 空白字符应该直接返回第一个
			assertEquals(new Character('\r'), StrUtil.autoConvert("\r\t", Character.class, null));
			// 如果是 有值的字符串，应该直接返回第一个
			assertEquals(new Character('F'), StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", Character.class, null));
		});
	}
	
	@Test
	void toLocalDate() {
		
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("2025", null, "");
		});
		
		// ===============================  接下来是一些转换失败的测试 =============== 自定义格式化字符串 为默认
		
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 DateTimeParseException
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert(null, LocalDate.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 DateTimeParseException
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert("", LocalDate.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 DateTimeParseException
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert("\r\t", LocalDate.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 DateTimeParseException
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", LocalDate.class, null);
		});
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert("2025-5", LocalDate.class, null); // 默认格式化，处理不了类。年月日必须齐全
			StrUtil.autoConvert("2025-05", LocalDate.class, null); // 默认格式化，处理不了类。年月日必须齐全
			StrUtil.autoConvert("2025/5", LocalDate.class, null); // 默认格式化，处理不了类。年月日必须齐全
			StrUtil.autoConvert("2025/05", LocalDate.class, null); // 默认格式化，处理不了类。年月日必须齐全
		});
		
		// ===============================  接下来是一些转换失败的测试 =============== 自定义格式化字符串 为异常
		
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert("20250101", LocalDate.class, "yyyy-M-d"); // 这里格式不符合，应该抛异常
		});
		
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert("2025-1-1", LocalDate.class, "m-d"); // 这里格式不符合，应该抛异常
		});
		
		// ===============================  接下来是一些转换成功的测试
		
		// 这里最后，测试正确的返回
		// 默认来说，它所能处理的字符串格式如下：2020-1-1、2021-12-31、2021/1/1、2021/12/31；2021、202101、20210131
		assertDoesNotThrow(()->{
			// 首先是默认的日期转换 （这里不带分隔符）
			assertEquals(
				LocalDate.parse("2025-1-1", DateTimeFormatter.ofPattern("yyyy-M-d")), StrUtil.autoConvert("2025", LocalDate.class, null));
			assertEquals(
				LocalDate.parse("2025-5-1", DateTimeFormatter.ofPattern("yyyy-M-d")), StrUtil.autoConvert("202505", LocalDate.class, null));
			assertEquals(
				LocalDate.parse("2025-5-20", DateTimeFormatter.ofPattern("yyyy-M-d")), StrUtil.autoConvert("20250520", LocalDate.class, null));
			// 这里带分割符，但是不带 0 开头
			assertEquals(
				LocalDate.parse("2025-5-1", DateTimeFormatter.ofPattern("yyyy-M-d")), StrUtil.autoConvert("2025-5-1", LocalDate.class, null));
			assertEquals(
				LocalDate.parse("2025-5-20", DateTimeFormatter.ofPattern("yyyy-M-d")), StrUtil.autoConvert("2025-5-20", LocalDate.class, null));
			assertEquals(
				LocalDate.parse("2025-5-1", DateTimeFormatter.ofPattern("yyyy-M-d")), StrUtil.autoConvert("2025/5/1", LocalDate.class, null));
			assertEquals(
				LocalDate.parse("2025-5-20", DateTimeFormatter.ofPattern("yyyy-M-d")), StrUtil.autoConvert("2025/5/20", LocalDate.class, null));
			// 这里带分割符，但是 带 0 开头
			assertEquals(
				LocalDate.parse("2025-5-1", DateTimeFormatter.ofPattern("yyyy-M-d")), StrUtil.autoConvert("2025-05-01", LocalDate.class, null));
			assertEquals(
				LocalDate.parse("2025-5-20", DateTimeFormatter.ofPattern("yyyy-M-d")), StrUtil.autoConvert("2025-05-20", LocalDate.class, null));
			assertEquals(
				LocalDate.parse("2025-5-1", DateTimeFormatter.ofPattern("yyyy-M-d")), StrUtil.autoConvert("2025/05/01", LocalDate.class, null));
			assertEquals(
				LocalDate.parse("2025-5-20", DateTimeFormatter.ofPattern("yyyy-M-d")), StrUtil.autoConvert("2025/05/20", LocalDate.class, null));
			
			// 最后是一些默认不提供的格式化处理
			assertEquals(
				LocalDate.parse("2025-12-31", DateTimeFormatter.ofPattern("yyyy-M-d")), 
				StrUtil.autoConvert("2025年12月31日", LocalDate.class, "yyyy年M月d日"));
			assertEquals(
					LocalDate.parse("2025-1-5", DateTimeFormatter.ofPattern("yyyy-M-d")), 
					StrUtil.autoConvert("2025年1月5日", LocalDate.class, "yyyy年M月d日"));
			assertEquals(
					LocalDate.parse("2025-1-5", DateTimeFormatter.ofPattern("yyyy-M-d")), 
					StrUtil.autoConvert("2025年01月05日", LocalDate.class, "yyyy年M月d日"));
		});
	}
	
	@Test
	void toLocalDateTime() {
		
		// 它能处理的字符串格式为：年、月、日 拼接 时、分、秒。中间的间隔， 可以为' '空格，也可以为'T'字符，也可以没有
		
		// 如果要转换的目标类型 targetType 是 null，则无法转换，抛出 IllegalArgumentException
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("2025-1-12 12:30:45.222000", null, "");
		});
		
		// ===============================  接下来是一些转换失败的测试 =============== 自定义格式化字符串 为默认
		
		// 如果传入的待转换字符串 oriValue 是 null 值，则无法转换，抛出 DateTimeParseException
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert(null, LocalDateTime.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 "" 值，则无法转换，抛出 DateTimeParseException
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert("", LocalDateTime.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 长度大于1的空白字符串 值，则无法转换，抛出 DateTimeParseException
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert("\r\t", LocalDateTime.class, null);
		});
		// 如果传入的待转换字符串 oriValue 是 无法转换的值，则抛出 DateTimeParseException
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert("FFFFFFFFFFFFFFFFFFF", LocalDateTime.class, null);
		});
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert("2025", LocalDateTime.class, null); // 默认格式化，这里是时间处理，不是日期，所以处理不了报错
		});
		
		// ===============================  接下来是一些转换失败的测试 =============== 自定义格式化字符串 为异常
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert("20250101", LocalDateTime.class, "yyyy-MM-dd"); // 这里格式不符合，应该抛异常
		});
		assertThrows(DateTimeParseException.class, ()->{
			StrUtil.autoConvert("20250101 12:45:30", LocalDateTime.class, "yyyy-MM-dd HH:mm:ss"); // 这里格式不符合，应该抛异常
		});
		
		// ===============================  接下来是一些转换成功的测试
		
		// 这里最后，测试正确的返回
		// 默认来说，它所能处理的字符串格式如下：年月日时分秒。中间可以带 T 也可以用 空格 代替。但是时分秒信息必须要有
		assertDoesNotThrow(()->{
			
			// 定义几个值来比对
			LocalDateTime time1 = LocalDateTime.of(2025, 1, 12, 12, 30, 45, 222*1000*1000);
			LocalDateTime time2 = LocalDateTime.of(2025, 1, 12, 12, 30, 45, 0);
			LocalDateTime time3 = LocalDateTime.of(2025, 1, 12, 12, 30, 0, 0);

			assertEquals(time1, StrUtil.autoConvert("2025-01-12 12:30:45.222", LocalDateTime.class, null));
			assertEquals(time1, StrUtil.autoConvert("2025-01-12T12:30:45.222", LocalDateTime.class, null));
			assertEquals(time2, StrUtil.autoConvert("2025-01-12 12:30:45", LocalDateTime.class, null));
			assertEquals(time3, StrUtil.autoConvert("2025-01-12 12:30", LocalDateTime.class, null));
			
			assertEquals(time1, StrUtil.autoConvert("2025/01/12 12:30:45.222", LocalDateTime.class, null));
			assertEquals(time1, StrUtil.autoConvert("2025/01/12T12:30:45.222", LocalDateTime.class, null));
			assertEquals(time2, StrUtil.autoConvert("2025/01/12 12:30:45", LocalDateTime.class, null));
			assertEquals(time3, StrUtil.autoConvert("2025/01/12 12:30", LocalDateTime.class, null));
			
			assertEquals(time1, StrUtil.autoConvert("2025-1-12 12:30:45.222", LocalDateTime.class, null));
			assertEquals(time1, StrUtil.autoConvert("2025-1-12T12:30:45.222", LocalDateTime.class, null));
			assertEquals(time2, StrUtil.autoConvert("2025-1-12 12:30:45", LocalDateTime.class, null));
			assertEquals(time3, StrUtil.autoConvert("2025-1-12 12:30", LocalDateTime.class, null));
			
			assertEquals(time1, StrUtil.autoConvert("2025/1/12 12:30:45.222", LocalDateTime.class, null));
			assertEquals(time1, StrUtil.autoConvert("2025/1/12T12:30:45.222", LocalDateTime.class, null));
			assertEquals(time2, StrUtil.autoConvert("2025/1/12 12:30:45", LocalDateTime.class, null));
			assertEquals(time3, StrUtil.autoConvert("2025/1/12 12:30", LocalDateTime.class, null));
			
			assertEquals(time2, StrUtil.autoConvert("2025年1月12日12时30分45秒", LocalDateTime.class, "yyyy年M月d日HH时mm分ss秒"));
		});
	}
	
	@Test
	void toStr() {
		
		// 这里是转换为字符串。因为反射的时候，它也是一个合法的值，所以干脆写写吧。其实，就是返回自身。不过如果是空或者null，返回一个 “”
		
		assertDoesNotThrow(()->{
			//
			assertEquals("", StrUtil.autoConvert(null, String.class, null));
			assertEquals("", StrUtil.autoConvert("", String.class, null));
			assertEquals("sss", StrUtil.autoConvert("\r\t   sss   \t \r \t", String.class, null));
			assertEquals("AAA", StrUtil.autoConvert("AAA", String.class, null));
		});
	}
	
	@Test
	void toObj() {
		
		// 这里是转换为 通用对象 Object。因为反射的时候，它也是一个合法的值，所以干脆写写吧。其实，就是返回自身。
		
		assertDoesNotThrow(()->{
			//
			assertEquals(null, StrUtil.autoConvert(null, Object.class, null)); // 这里还是有点区别的，这里可以返回 null
			assertEquals("", StrUtil.autoConvert("", Object.class, null));
			assertEquals("sss", StrUtil.autoConvert("\r\t   sss   \t \r \t", Object.class, null));
			assertEquals("AAA", StrUtil.autoConvert("AAA", Object.class, null));
			assertEquals("123", StrUtil.autoConvert("123", Object.class, null));
		});
	}
	
	@Test
	void toUnknowObj() {
		
		// 这里主要检测反射机制有没有起作用。如果有些类型没有映射，则应该抛出异常
		assertThrows(NoSuchMethodException.class, ()->{
			StrUtil.autoConvert("slslssls", StrUtilTester2.class, null);
		});
	}
	
	@Test
	void toMaxValue() {
		
		// 这里是 数据最大值的测试，超出最大值，应该抛异常
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("99999999999999999999999999999999", byte.class, null);
		});
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("99999999999999999999999999999999", Byte.class, null);
		});
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("99999999999999999999999999999999", short.class, null);
		});
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("99999999999999999999999999999999", Short.class, null);
		});
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("99999999999999999999999999999999", int.class, null);
		});
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("99999999999999999999999999999999", Integer.class, null);
		});
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("99999999999999999999999999999999", long.class, null);
		});
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert("99999999999999999999999999999999", Long.class, null);
		});
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(Double.MAX_VALUE+"", float.class, null);
		});
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert(Double.MAX_VALUE+"", Float.class, null);
		});
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert((Double.MAX_VALUE+Double.MAX_VALUE)+"", double.class, null);
		});
		assertThrows(NumberFormatException.class, ()->{
			StrUtil.autoConvert((Double.MAX_VALUE+Double.MAX_VALUE)+"", Double.class, null);
		});
	}
	
	@Test
	void toEnumObject() {
		
		// 这里应该不会抛出异常
		assertDoesNotThrow(()->{
			// 这里是 枚举值的测试
			assertEquals(Color.RED, StrUtil.autoConvert("red", Color.class, null));
			assertEquals(Color.GREEN, StrUtil.autoConvert("green", Color.class, null));
			assertEquals(Color.BLUE, StrUtil.autoConvert("blue", Color.class, null));
			assertEquals(Color.WHITE, StrUtil.autoConvert("white", Color.class, null));
			
			// 如果待转换字符串为 null 或者 空字符串，则返回 null
			assertEquals(null, StrUtil.autoConvert(null, Color.class, null));
			assertEquals(null, StrUtil.autoConvert("", Color.class, null));
		});
		
		// 这里应该抛出异常（转换类型不对，不是枚举类。这样自动匹配时，是找不到对应的处理方法的）
		assertThrows(NoSuchMethodException.class, ()->{
			StrUtil.autoConvert("red", StrUtilTester2.class, null);
		});
		
		// 这里应该抛出异常（转换数据不对，不是这个枚举类的内部值）
		assertThrows(IllegalArgumentException.class, ()->{
			StrUtil.autoConvert("aaaa", Color.class, null);
		});
	}
	
	@Test
	void valueTest() {
		
		assertDoesNotThrow(()->{
			// 这里对数值进行一些测试
			assertTrue(1==StrUtil.autoConvert("1", int.class, null));
		});
	}
}
