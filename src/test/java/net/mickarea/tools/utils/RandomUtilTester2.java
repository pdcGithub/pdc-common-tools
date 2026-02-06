/******************************************************************************************************

This file "RandomUtilTester2.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.RepeatedTest;

/**
 * 这是关于 RandomUtil 的单元测试。这里主要是简单的参数测试。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年2月6日
 */
public class RandomUtilTester2 {

	@RepeatedTest(value = 15, failureThreshold = 1)
	void test_param_genNumberString() throws Exception {
		// 首先是会抛异常的情况
		assertThrows(Exception.class, ()->{RandomUtil.genNumberString(-1);});
		assertThrows(Exception.class, ()->{RandomUtil.genNumberString(0);});
		// 然后是不抛异常的情况
		assertDoesNotThrow(()->{
			assertTrue(Pattern.matches("[0-9]", RandomUtil.genNumberString(1)));
			assertTrue(Pattern.matches("[0-9]{5}", RandomUtil.genNumberString(5)));
			assertTrue(Pattern.matches("[0-9]{10}", RandomUtil.genNumberString(10)));
			assertTrue(Pattern.matches("[0-9]{20}", RandomUtil.genNumberString(20)));
		});
	}
	
	@RepeatedTest(value = 15, failureThreshold = 1)
	void test_param_genNumberStringWithoutException() throws Exception {
		// 这里是不抛异常的，所以直接比对结果就行了。
		assertEquals(RandomUtil.ERROR_CODE, RandomUtil.genNumberStringWithoutException(-1));
		assertEquals(RandomUtil.ERROR_CODE, RandomUtil.genNumberStringWithoutException(0));
		// 
		assertTrue(Pattern.matches("[0-9]", RandomUtil.genNumberStringWithoutException(1)));
		assertTrue(Pattern.matches("[0-9]{5}", RandomUtil.genNumberStringWithoutException(5)));
		assertTrue(Pattern.matches("[0-9]{10}", RandomUtil.genNumberStringWithoutException(10)));
		assertTrue(Pattern.matches("[0-9]{20}", RandomUtil.genNumberStringWithoutException(20)));
	}
	
	@RepeatedTest(value = 15, failureThreshold = 1)
	void test_param_genLetterString() throws Exception {
		// 首先是会抛异常的情况
		assertThrows(Exception.class, ()->{RandomUtil.genLetterString(-1);});
		assertThrows(Exception.class, ()->{RandomUtil.genLetterString(0);});
		// 然后是不抛异常的情况
		assertDoesNotThrow(()->{
			assertTrue(Pattern.matches("[A-Z]", RandomUtil.genLetterString(1)));
			assertTrue(Pattern.matches("[A-Z]{5}", RandomUtil.genLetterString(5)));
			assertTrue(Pattern.matches("[A-Z]{10}", RandomUtil.genLetterString(10)));
			assertTrue(Pattern.matches("[A-Z]{20}", RandomUtil.genLetterString(20)));
		});
	}
	
	@RepeatedTest(value = 15, failureThreshold = 1)
	void test_param_genLetterStringWithoutException() throws Exception {
		// 这里是不抛异常的，所以直接比对结果就行了。
		assertEquals(RandomUtil.ERROR_CODE, RandomUtil.genLetterStringWithoutException(-1));
		assertEquals(RandomUtil.ERROR_CODE, RandomUtil.genLetterStringWithoutException(0));
		// 
		assertTrue(Pattern.matches("[A-Z]", RandomUtil.genLetterStringWithoutException(1)));
		assertTrue(Pattern.matches("[A-Z]{5}", RandomUtil.genLetterStringWithoutException(5)));
		assertTrue(Pattern.matches("[A-Z]{10}", RandomUtil.genLetterStringWithoutException(10)));
		assertTrue(Pattern.matches("[A-Z]{20}", RandomUtil.genLetterStringWithoutException(20)));
	}
	
	@RepeatedTest(value = 15, failureThreshold = 1)
	void test_param_genNumAndLetterMixedString() throws Exception {
		// 首先是会抛异常的情况
		assertThrows(Exception.class, ()->{RandomUtil.genNumAndLetterMixedString(-1);});
		assertThrows(Exception.class, ()->{RandomUtil.genNumAndLetterMixedString(0);});
		// 然后是不抛异常的情况
		assertDoesNotThrow(()->{
			assertTrue(Pattern.matches("[A-Z0-9]", RandomUtil.genNumAndLetterMixedString(1)));
			assertTrue(Pattern.matches("[A-Z0-9]{5}", RandomUtil.genNumAndLetterMixedString(5)));
			assertTrue(Pattern.matches("[A-Z0-9]{10}", RandomUtil.genNumAndLetterMixedString(10)));
			assertTrue(Pattern.matches("[A-Z0-9]{20}", RandomUtil.genNumAndLetterMixedString(20)));
		});
	}
	
	@RepeatedTest(value = 15, failureThreshold = 1)
	void test_param_genNumAndLetterMixedStringWithoutException() throws Exception {
		// 这里是不抛异常的，所以直接比对结果就行了。
		assertEquals(RandomUtil.ERROR_CODE, RandomUtil.genNumAndLetterMixedStringWithoutException(-1));
		assertEquals(RandomUtil.ERROR_CODE, RandomUtil.genNumAndLetterMixedStringWithoutException(0));
		// 
		assertTrue(Pattern.matches("[A-Z0-9]", RandomUtil.genNumAndLetterMixedStringWithoutException(1)));
		assertTrue(Pattern.matches("[A-Z0-9]{5}", RandomUtil.genNumAndLetterMixedStringWithoutException(5)));
		assertTrue(Pattern.matches("[A-Z0-9]{10}", RandomUtil.genNumAndLetterMixedStringWithoutException(10)));
		assertTrue(Pattern.matches("[A-Z0-9]{20}", RandomUtil.genNumAndLetterMixedStringWithoutException(20)));
	}
}
