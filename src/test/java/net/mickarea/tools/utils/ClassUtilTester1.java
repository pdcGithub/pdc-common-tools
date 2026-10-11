/******************************************************************************************************

This file "ClassUtilTester1.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.RepeatedTest;

/**
 * 这是关于 ClassUtil 的单元测试类
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月11日
 */
public class ClassUtilTester1 {

	/**
	 * 按照 URL 对象，搜索 Java 类名称字符串
	 */
	@RepeatedTest(failureThreshold = 1, value = 3)
	void searchClassNamesFromURL_test() throws Exception {
		
		// 一个加载器
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		// 一个合规的 URL 对象
		URL url1 = loader.getResource("net/mickarea");
		// 测试url 不为 null
		assertNotEquals(null, url1);
		
		// ======== 首先，测试异常参数 URL 对象, resourcePath 字符串
		List<String> errResult1 = ClassUtil.searchClassNamesFromURL(null, "net/mickarea");
		List<String> errResult2 = ClassUtil.searchClassNamesFromURL(new URL("http://www.baidu.com"), "net/mickarea"); // http 协议不会处理
		List<String> errResult3 = ClassUtil.searchClassNamesFromURL(url1, null);
		List<String> errResult4 = ClassUtil.searchClassNamesFromURL(url1, "");
		List<String> errResult5 = ClassUtil.searchClassNamesFromURL(url1, "   ");
		List<String> errResult6 = ClassUtil.searchClassNamesFromURL(url1, ".");
		List<String> errResult7 = ClassUtil.searchClassNamesFromURL(url1, "/");
		// 对于异常参数的结果，应该都是 空列表
		assertTrue(ListUtil.isEmptyList(errResult1));
		assertTrue(ListUtil.isEmptyList(errResult2));
		assertTrue(ListUtil.isEmptyList(errResult3));
		assertTrue(ListUtil.isEmptyList(errResult4));
		assertTrue(ListUtil.isEmptyList(errResult5));
		assertTrue(ListUtil.isEmptyList(errResult6));
		assertTrue(ListUtil.isEmptyList(errResult7));
		
		// ======== 然后，测试正常参数（这里要测试 2个类型的 URL， jar协议 和 file协议）
		URL jarUrl = loader.getResource("ch/qos/logback/core/filter");
		URL jarUrl2 = loader.getResource("ch/qos/logback/core/filter/"); // 多了个 / 后缀
		URL fileUrl = loader.getResource("net/mickarea/tools/utils/testcls");
		URL fileUrl2 = loader.getResource("net/mickarea/tools/utils/testcls/"); // 多了个 / 后缀
		//
		List<String> okResult1 = ClassUtil.searchClassNamesFromURL(jarUrl, "ch/qos/logback/core/filter");
		List<String> okResult2 = ClassUtil.searchClassNamesFromURL(jarUrl2, "ch/qos/logback/core/filter/");
		List<String> okResult3 = ClassUtil.searchClassNamesFromURL(fileUrl, "net/mickarea/tools/utils/testcls");
		List<String> okResult4 = ClassUtil.searchClassNamesFromURL(fileUrl2, "net/mickarea/tools/utils/testcls/");
		//
		assertFalse(ListUtil.isEmptyList(okResult1)); // 这里应该都是可以搜索到的
		assertFalse(ListUtil.isEmptyList(okResult2));
		assertFalse(ListUtil.isEmptyList(okResult3));
		assertFalse(ListUtil.isEmptyList(okResult4));
		assertEquals(3, okResult1.size());
		assertEquals(3, okResult2.size());
		assertEquals(4, okResult3.size()); // 对于 testcls 路径，有3个主类，1个内部类，所以是 4个
		assertEquals(4, okResult4.size());
		assertEquals(1, okResult3.stream().filter(name->name.contains("$")).count()); //校验，是否找到1个 内部类
		assertEquals(1, okResult4.stream().filter(name->name.contains("$")).count()); //校验，是否找到1个 内部类
	}
	
	/**
	 * 按照 单个 package 字符串，搜索 Java 类名称字符串
	 */
	@RepeatedTest(failureThreshold = 1, value = 3)
	void searchClassNamesFromPackage_test() throws Exception {
		
		// ======== 首先，测试异常参数 packageName 字符串
		List<String> errResult1 = ClassUtil.searchClassNamesFromPackage(null);
		List<String> errResult2 = ClassUtil.searchClassNamesFromPackage("");
		List<String> errResult3 = ClassUtil.searchClassNamesFromPackage("   ");
		List<String> errResult4 = ClassUtil.searchClassNamesFromPackage(".");
		List<String> errResult5 = ClassUtil.searchClassNamesFromPackage("net...mickarea");
		List<String> errResult6 = ClassUtil.searchClassNamesFromPackage("net.mickarea.");
		// 对于异常参数的结果，应该都是 空列表
		assertTrue(ListUtil.isEmptyList(errResult1));
		assertTrue(ListUtil.isEmptyList(errResult2));
		assertTrue(ListUtil.isEmptyList(errResult3));
		assertTrue(ListUtil.isEmptyList(errResult4));
		assertTrue(ListUtil.isEmptyList(errResult5));
		assertTrue(ListUtil.isEmptyList(errResult6));
		
		// ======== 然后，测试正常参数（这里要测试 2个类型的包路径：jar 内部的包 和 本地的文件夹）
		List<String> okResult1 = ClassUtil.searchClassNamesFromPackage("ch.qos.logback.core.filter");
		List<String> okResult2 = ClassUtil.searchClassNamesFromPackage("net.mickarea.tools.utils.testcls");
		//
		assertEquals(3, okResult1.size());
		assertEquals(4, okResult2.size());
		//
		assertEquals(1, okResult2.stream().filter(name->name.contains("$")).count()); //校验，是否找到1个 内部类
	}
	
	/**
	 * 按照 多个 package 字符串，搜索 Java 类名称字符串
	 */
	@RepeatedTest(failureThreshold = 1, value = 3)
	void searchClassNamesFromPackages_test() throws Exception {
		
		// ======== 首先，测试异常参数 packageNames 不定参数。类型是 字符串 
		String[] testStrArr = null;
		List<String> errResult0 = ClassUtil.searchClassNamesFromPackages();
		List<String> errResult1 = ClassUtil.searchClassNamesFromPackages(testStrArr);
		List<String> errResult2 = ClassUtil.searchClassNamesFromPackages(new String[]{});
		List<String> errResult3 = ClassUtil.searchClassNamesFromPackages("");
		List<String> errResult4 = ClassUtil.searchClassNamesFromPackages("   ");
		List<String> errResult5 = ClassUtil.searchClassNamesFromPackages(".");
		// ======== 因为是不定参数，所以要测试 多个 异常参数情况
		List<String> errResult6 = ClassUtil.searchClassNamesFromPackages(null, null);
		List<String> errResult7 = ClassUtil.searchClassNamesFromPackages(null, "");
		List<String> errResult8 = ClassUtil.searchClassNamesFromPackages(null, " ");
		List<String> errResult9 = ClassUtil.searchClassNamesFromPackages(null, ".");
		List<String> errResult10 = ClassUtil.searchClassNamesFromPackages("", ".");
		List<String> errResult11 = ClassUtil.searchClassNamesFromPackages(" ", ".");
		List<String> errResult12 = ClassUtil.searchClassNamesFromPackages(".", ".");
		// 
		assertTrue(ListUtil.isEmptyList(errResult0));
		assertTrue(ListUtil.isEmptyList(errResult1));
		assertTrue(ListUtil.isEmptyList(errResult2));
		assertTrue(ListUtil.isEmptyList(errResult3));
		assertTrue(ListUtil.isEmptyList(errResult4));
		assertTrue(ListUtil.isEmptyList(errResult5));
		assertTrue(ListUtil.isEmptyList(errResult6));
		assertTrue(ListUtil.isEmptyList(errResult7));
		assertTrue(ListUtil.isEmptyList(errResult8));
		assertTrue(ListUtil.isEmptyList(errResult9));
		assertTrue(ListUtil.isEmptyList(errResult10));
		assertTrue(ListUtil.isEmptyList(errResult11));
		assertTrue(ListUtil.isEmptyList(errResult12));
		
		// ======== 然后，测试正常参数（这里要测试 2个类型的包路径：jar 内部的包 和 本地的文件夹）
		List<String> okResult1 = ClassUtil.searchClassNamesFromPackages("ch.qos.logback.core.filter", "net.mickarea.tools.utils.testcls");
		List<String> okResult2 = ClassUtil.searchClassNamesFromPackages("ch.qos.logback.core.filter", null, "", " ", ".");
		List<String> okResult3 = ClassUtil.searchClassNamesFromPackages("net.mickarea.tools.utils.testcls", null, "", " ", ".");
		List<String> okResult4 = ClassUtil.searchClassNamesFromPackages(null, "", " ", ".", "ch.qos.logback.core.filter");
		List<String> okResult5 = ClassUtil.searchClassNamesFromPackages(null, "", " ", ".", "net.mickarea.tools.utils.testcls");
		//
		assertEquals(7, okResult1.size());
		assertEquals(3, okResult2.size());
		assertEquals(4, okResult3.size());
		assertEquals(3, okResult4.size());
		assertEquals(4, okResult5.size());
		//
		assertEquals(1, okResult1.stream().filter(name->name.contains("$")).count()); //校验，是否找到1个 内部类
		assertEquals(1, okResult3.stream().filter(name->name.contains("$")).count()); //校验，是否找到1个 内部类
		assertEquals(1, okResult5.stream().filter(name->name.contains("$")).count()); //校验，是否找到1个 内部类
	}
	
}
