/******************************************************************************************************

This file "AnnotationUtilTester1.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import net.mickarea.tools.utils.testannos.JustForTest1;
import net.mickarea.tools.utils.testannos.JustForTest2;
import net.mickarea.tools.utils.testannos.JustForTest3;

/**
 * 这里是 AnnotationUtil 工具类的一个单元测试
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年1月28日
 */
public class AnnotationUtilTester1 {

	/* 这里定义几个内部类，用于测试，他们的注解是随便写的 */
	
	@JustForTest1
	@JustForTest2
	@JustForTest3
	class WithAnno {
		
		@JustForTest1
		@JustForTest2
		@JustForTest3
		public String name;
		
		@JustForTest1
		@JustForTest2
		@JustForTest3
		public boolean testFunc(String input) {
			return false;
		}
	}
	
	class WithoutAnno {
		
		public int age;
		
		public String testMethod(String input) {
			return "tom";
		}
	}
	
	/* 定义一些单元测试用到的内部变量， yes 是有注解的，no 是没有注解的 */
	Class<?> clsYes, clsNo;
	Field fYes, fNo;
	Method mYes, mNo;
	
	/**
	 * 这里，每一个单元测函数都会执行一次
	 * @throws Exception
	 */
	@BeforeEach
	void beforeRun() throws Exception {
		
		/* 有注解的类、属性、函数 */
		this.clsYes = WithAnno.class;
		this.fYes = WithAnno.class.getDeclaredField("name");
		this.mYes = WithAnno.class.getDeclaredMethod("testFunc", String.class);
		
		/* 没有注解的类、属性、函数 */
		this.clsNo = WithoutAnno.class;
		this.fNo = WithoutAnno.class.getDeclaredField("age");
		this.mNo = WithoutAnno.class.getDeclaredMethod("testMethod", String.class);
	}
	
	/**
	 * 这里进行 isAnnotationPresentBy 异常参数测试 它有3个参数 targetObj, annotaions, atLeast
	 */
	@Test
	void test1ParamsErrorOfIsAnnotationPresentBy() {
		// 异常参数，测试 targetObj
		assertEquals(false, AnnotationUtil.isAnnotationPresentBy(null, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class), 1));
		// 异常参数，测试 annotaions
		assertEquals(false, AnnotationUtil.isAnnotationPresentBy(this.clsYes, null, 1));
		assertEquals(false, AnnotationUtil.isAnnotationPresentBy(this.clsYes, Arrays.asList(), 1));
		// 异常参数，测试 atLeast 小于 1
		assertEquals(false, AnnotationUtil.isAnnotationPresentBy(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class), 0));
		assertEquals(false, AnnotationUtil.isAnnotationPresentBy(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class), -1));
		//               atLeast 大于非重复的注解数
		assertEquals(false, AnnotationUtil.isAnnotationPresentBy(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class), 4));
		assertEquals(false, AnnotationUtil.isAnnotationPresentBy(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class), 5));
	}
	
	/**
	 * 这里进行 isAnnotationPresentBy 正常参数测试 它有3个参数 targetObj, annotaions, atLeast
	 */
	@Test
	void test2ParamsOkOfIsAnnotationPresentBy() {
		// 测试 targetObj 
		assertEquals(true, AnnotationUtil.isAnnotationPresentBy(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class), 1));
		assertEquals(false, AnnotationUtil.isAnnotationPresentBy(this.clsNo, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class), 1));
		// 测试 annotaions
		assertEquals(true, AnnotationUtil.isAnnotationPresentBy(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class), 1));
		assertEquals(true, AnnotationUtil.isAnnotationPresentBy(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class), 1));
		assertEquals(true, AnnotationUtil.isAnnotationPresentBy(this.clsYes, Arrays.asList(JustForTest1.class), 1));
		assertEquals(true, AnnotationUtil.isAnnotationPresentBy(this.clsYes, Arrays.asList(Deprecated.class, SuppressWarnings.class, JustForTest3.class), 1));
		assertEquals(false, AnnotationUtil.isAnnotationPresentBy(this.clsNo, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class), 1));
		assertEquals(false, AnnotationUtil.isAnnotationPresentBy(this.clsNo, Arrays.asList(JustForTest1.class, JustForTest2.class), 1));
		assertEquals(false, AnnotationUtil.isAnnotationPresentBy(this.clsNo, Arrays.asList(JustForTest1.class), 1));
		assertEquals(false, AnnotationUtil.isAnnotationPresentBy(this.clsNo, Arrays.asList(Deprecated.class, SuppressWarnings.class, JustForTest3.class), 1));
		// 测试 atLeast
		assertEquals(true, 
				AnnotationUtil.isAnnotationPresentBy(this.clsYes, 
						Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class, Deprecated.class, SuppressWarnings.class), 1));
		assertEquals(true, 
				AnnotationUtil.isAnnotationPresentBy(this.clsYes, 
						Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class, Deprecated.class, SuppressWarnings.class), 2));
		assertEquals(true, 
				AnnotationUtil.isAnnotationPresentBy(this.clsYes, 
						Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class, Deprecated.class, SuppressWarnings.class), 3));
		assertEquals(false, 
				AnnotationUtil.isAnnotationPresentBy(this.clsYes, 
						Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class, Deprecated.class, SuppressWarnings.class), 4));
		assertEquals(false, 
				AnnotationUtil.isAnnotationPresentBy(this.clsYes, 
						Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class, Deprecated.class, SuppressWarnings.class), 5));
		assertEquals(false, 
				AnnotationUtil.isAnnotationPresentBy(this.clsNo, 
						Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class, Deprecated.class, SuppressWarnings.class), 1));
		assertEquals(false, 
				AnnotationUtil.isAnnotationPresentBy(this.clsNo, 
						Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class, Deprecated.class, SuppressWarnings.class), 2));
		assertEquals(false, 
				AnnotationUtil.isAnnotationPresentBy(this.clsNo, 
						Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class, Deprecated.class, SuppressWarnings.class), 3));
		assertEquals(false, 
				AnnotationUtil.isAnnotationPresentBy(this.clsNo, 
						Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class, Deprecated.class, SuppressWarnings.class), 4));
		assertEquals(false, 
				AnnotationUtil.isAnnotationPresentBy(this.clsNo, 
						Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class, Deprecated.class, SuppressWarnings.class), 5));
		// 测试 重复的注解信息，是否会合并
		assertEquals(true, 
				AnnotationUtil.isAnnotationPresentBy(this.clsYes, 
						Arrays.asList(JustForTest1.class, JustForTest1.class, JustForTest1.class, JustForTest1.class, JustForTest1.class), 1));
		assertEquals(false, 
				AnnotationUtil.isAnnotationPresentBy(this.clsYes, 
						Arrays.asList(JustForTest1.class, JustForTest1.class, JustForTest1.class, JustForTest1.class, JustForTest1.class), 2));
		assertEquals(false, 
				AnnotationUtil.isAnnotationPresentBy(this.clsYes, 
						Arrays.asList(JustForTest1.class, JustForTest1.class, JustForTest1.class, JustForTest1.class, JustForTest1.class), 3));
	}
	
	@Timeout(unit = TimeUnit.SECONDS, value = 2)
	@Test
	void testEfficiencyOfIsAnnotationPresentBy() {
		// 这里执行一百万次，大概2秒内吧。其实，1秒应该够了。
		int total = 1000000;
		for(int i=0;i<total;i++) {
			AnnotationUtil.isAnnotationPresentBy(this.clsYes, 
					Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class, Deprecated.class, SuppressWarnings.class), 3);
		}
	}
	
	/**
	 * 这里进行 isAnnotationPresentByOneOfThem 异常参数测试 它有2个参数 targetObj, annotaions
	 */
	@Test
	void test1ParamsErrorOfIsAnnotationPresentByOne() {
		// 异常参数，测试 targetObj
		assertEquals(false, AnnotationUtil.isAnnotationPresentByOne(null, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class)));
		// 异常参数，测试 annotaions
		assertEquals(false, AnnotationUtil.isAnnotationPresentByOne(this.clsYes, null));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByOne(this.clsYes, Arrays.asList()));
	}
	
	/**
	 * 这里进行 isAnnotationPresentByOneOfThem 正常参数测试 它有2个参数 targetObj, annotaions
	 */
	@Test
	void test2ParamsOkOfIsAnnotationPresentByOne() {
		// 测试 targetObj
		assertEquals(true, AnnotationUtil.isAnnotationPresentByOne(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class)));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByOne(this.clsNo, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class)));
		// 测试 annotaions
		assertEquals(true, AnnotationUtil.isAnnotationPresentByOne(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class)));
		assertEquals(true, AnnotationUtil.isAnnotationPresentByOne(this.clsYes, Arrays.asList(Deprecated.class, JustForTest1.class)));
		assertEquals(true, AnnotationUtil.isAnnotationPresentByOne(this.clsYes, Arrays.asList(JustForTest1.class)));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByOne(this.clsYes, Arrays.asList(Deprecated.class, SuppressWarnings.class)));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByOne(this.clsNo, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class)));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByOne(this.clsNo, Arrays.asList(Deprecated.class, JustForTest1.class)));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByOne(this.clsNo, Arrays.asList(JustForTest1.class)));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByOne(this.clsNo, Arrays.asList(Deprecated.class, SuppressWarnings.class)));
	}
	
	/**
	 * 这里进行 isAnnotationPresentByAllOfThem 异常参数测试 它有2个参数 targetObj, annotaions
	 */
	@Test
	void test1ParamsErrorOfIsAnnotationPresentByAll() {
		// 异常参数，测试 targetObj
		assertEquals(false, AnnotationUtil.isAnnotationPresentByAll(null, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class)));
		// 异常参数，测试 annotaions
		assertEquals(false, AnnotationUtil.isAnnotationPresentByAll(this.clsYes, null));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByAll(this.clsYes, Arrays.asList()));
	}
	
	/**
	 * 这里进行 isAnnotationPresentByAll 正常参数测试 它有2个参数 targetObj, annotaions
	 */
	@Test
	void test2ParamsOkOfIsAnnotationPresentByAll() {
		// 测试 targetObj
		assertEquals(true, AnnotationUtil.isAnnotationPresentByAll(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class)));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByAll(this.clsNo, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class)));
		// 测试 annotaions
		assertEquals(true, AnnotationUtil.isAnnotationPresentByAll(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class)));
		assertEquals(true, AnnotationUtil.isAnnotationPresentByAll(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class)));
		assertEquals(true, AnnotationUtil.isAnnotationPresentByAll(this.clsYes, Arrays.asList(JustForTest1.class)));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByAll(this.clsYes, Arrays.asList(JustForTest1.class, JustForTest2.class, Deprecated.class)));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByAll(this.clsNo, Arrays.asList(JustForTest1.class, JustForTest2.class, JustForTest3.class)));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByAll(this.clsNo, Arrays.asList(JustForTest1.class, JustForTest2.class)));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByAll(this.clsNo, Arrays.asList(JustForTest1.class)));
		assertEquals(false, AnnotationUtil.isAnnotationPresentByAll(this.clsNo, Arrays.asList(JustForTest1.class, JustForTest2.class, Deprecated.class)));
	}
	
	/**
	 * 这里进行 getClassesByAnnotaion 的测试
	 */
	@Test
	void getClassesByAnnotaion1() throws Exception {
		
		// 参数异常测试，应该返回null，并且没有抛出异常
		assertNull(AnnotationUtil.getClassesByAnnotaion(null, JustForTest1.class));
		assertNull(AnnotationUtil.getClassesByAnnotaion("", JustForTest1.class));
		assertNull(AnnotationUtil.getClassesByAnnotaion("     ", JustForTest1.class));
		assertNull(AnnotationUtil.getClassesByAnnotaion("net.mickarea.tools.utils", null));
		
		// 正常参数测试
		assertNotNull(AnnotationUtil.getClassesByAnnotaion("net.mickarea.tools.utils.test", JustForTest1.class));
		assertTrue(AnnotationUtil.getClassesByAnnotaion("net.mickarea.tools.utils.test", JustForTest1.class) instanceof Set);
		
		// 数量检测
		Set<Class<?>> clsSet = AnnotationUtil.getClassesByAnnotaion("net.mickarea.tools.utils.test", JustForTest1.class);
		assertEquals(0, clsSet.size());
		
		Set<Class<?>> clsSet2 = AnnotationUtil.getClassesByAnnotaion("net.mickarea.tools.utils", JustForTest1.class);
		assertEquals(1, clsSet2.size());
		
		// 结果检测
		Class<?> cls = clsSet2.iterator().next();
		assertEquals(cls, AnnotationUtilTester1.WithAnno.class);
	}

}
