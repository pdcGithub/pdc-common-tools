/******************************************************************************************************

This file "PatternUtilTester.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/**
 * 这里是关于 PatternUtil 工具类的单元测试。
 * 因为这个类很多方法已经淘汰，所以先对一些新实现的方法进行测试
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月3日
 */
public class PatternUtilTester {

	/**
	 * 这里是 PatternUtil.matches 方法的异常参数测试
	 */
	@Test
	void matchesTest1() throws Exception {
		//
		// 三个参数 String regexp, CharSequence int, int... configs。第3个是不定参数，相当于 int[] 数组
		//
		assertEquals(false, PatternUtil.matches(null, "ss", Pattern.CASE_INSENSITIVE));
		assertEquals(false, PatternUtil.matches("", "ss", Pattern.CASE_INSENSITIVE));
		assertEquals(false, PatternUtil.matches(" ", "ss", Pattern.CASE_INSENSITIVE));
		assertEquals(true,  PatternUtil.matches("ss", "ss", Pattern.CASE_INSENSITIVE));
		//
		assertEquals(false, PatternUtil.matches("[a-z]+", null, Pattern.CASE_INSENSITIVE));
		assertEquals(false, PatternUtil.matches("[a-z]+", "", Pattern.CASE_INSENSITIVE));
		assertEquals(false, PatternUtil.matches("[a-z]+", " ", Pattern.CASE_INSENSITIVE));
		assertEquals(true,  PatternUtil.matches("[a-z]+", "aaa", Pattern.CASE_INSENSITIVE));
		//
		assertEquals(true,  PatternUtil.matches("[a-z]+", "aaa"));
		assertEquals(true,  PatternUtil.matches("[a-z]+", "aaa", null)); // 这里 第三个参数 null，不会加入 compile
		assertEquals(true,  PatternUtil.matches("[a-z]+", "aaa", new int[0])); //长度为0，相当于没有传入
		assertEquals(false, PatternUtil.matches("[a-z]+", "AAA", 444));
		assertEquals(false, PatternUtil.matches("[a-z]+", "AAA", 444, 555)); // 不在 flags 范围内，相当于没有传入
		assertEquals(true,  PatternUtil.matches("[a-z]+", "AAA", Pattern.CASE_INSENSITIVE));
		assertEquals(true,  PatternUtil.matches("[a-z]+", "AAA", Pattern.CASE_INSENSITIVE, Pattern.MULTILINE));
	}
	
	/**
	 * 这里是 PatternUtil.matches 方法的普通测试（这里主要是对 正则表达式 和 字符串 的正确性判断）
	 */
	@Test
	void matchesTest2() throws Exception {
		//
		assertEquals(true,   PatternUtil.matches("[a-z]+", "abcd"));
		assertEquals(false,  PatternUtil.matches("[a-z]+", "abcd2"));
		//
		assertEquals(false,   PatternUtil.matches("[a-z]+", "ABCD")); // 这正则表达式，匹配不了 大写
		assertEquals(true,    PatternUtil.matches("[a-z]+", "ABCD", Pattern.CASE_INSENSITIVE)); // 加参数后可以
		// 
		// 这里增加测试，多个 flags 整合
		assertEquals(false, PatternUtil.matches("a.*d", "ABCD"));
		assertEquals(true, PatternUtil.matches("a.*d", "ABCD", Pattern.CASE_INSENSITIVE));
		// 
		// 默认情况下, 正则表达式中点(.)不会匹配换行符
		assertEquals(false, PatternUtil.matches("a.*d", "AB\nCD", Pattern.CASE_INSENSITIVE));
		// dotall 允许用 dot 符号，匹配所有
		assertEquals(true, PatternUtil.matches("a.*d", "AB\nCD", Pattern.CASE_INSENSITIVE, Pattern.DOTALL));
	}
	
}
