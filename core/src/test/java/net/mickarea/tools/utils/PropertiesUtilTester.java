/******************************************************************************************************

This file "PropertiesUtilTester.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 这里是 PropertiesUtil 工具类的测试类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年10月30日
 */
public class PropertiesUtilTester {

	@BeforeEach
	void setTheEnv() {
		// 每次测试前，都设置一下地区。这样可以保证默认是 英文
		Locale.setDefault(Locale.ENGLISH);
	}
	
	@Test
	void getMessageOKTest() {
		// getMessage 的正常返回测试
		assertEquals(PropertiesUtil.getMessage("tests.mytest", "myname"), "Tom");
		assertEquals(PropertiesUtil.getMessage("i18n.messages", "test.greeting"), "Hello");
	}
	
	@Test
	void getMessageERRORTest() {
		// getMessage 的异常返回测试
		assertEquals(PropertiesUtil.getMessage(null, null), "");
		assertEquals(PropertiesUtil.getMessage("", null), "");
		assertEquals(PropertiesUtil.getMessage(null, ""), "");
		assertEquals(PropertiesUtil.getMessage("", ""), "");
		// 
		assertEquals(PropertiesUtil.getMessage("tests.mytest", null), "");
		assertEquals(PropertiesUtil.getMessage(null, "myname"), "");
		// 
		assertEquals(PropertiesUtil.getMessage("tests.mytest", "xxxxxx"), "");
		assertEquals(PropertiesUtil.getMessage("yyyyy", "myname"), "");
	}
	
	@Test
	void getMessageWithLocaleOKTest() {
		// getMessage 的正常返回测试（附带区域选择）
		assertEquals(PropertiesUtil.getMessage("i18n.messages", "test.greeting", new Locale("en")), "Hello");
		// 这个没有对应配置，读取了 en 的配置
		assertEquals(PropertiesUtil.getMessage("i18n.messages", "test.greeting", new Locale("zh")), "Hello");
		// 这个有配置
		assertEquals(PropertiesUtil.getMessage("i18n.messages", "test.greeting", new Locale("zh", "CN")), "你好");
		// 这个配置文件，没有地区限制
		assertEquals(PropertiesUtil.getMessage("tests.mytest", "myname", new Locale("zh")), "Tom");
		//（这个是不存在的地区，但是已经设置了默认为英文地区，所以读取了 en 的配置）
		assertEquals(PropertiesUtil.getMessage("i18n.messages", "test.greeting", Locale.GERMANY), "Hello");
	}
	
	@Test
	void getMessageWithLocaleERRORTest() {
		// getMessage 的异常返回测试（附带区域选择）
		assertEquals(PropertiesUtil.getMessage(null, null, null),"");
		assertEquals(PropertiesUtil.getMessage("", null, null),"");
		assertEquals(PropertiesUtil.getMessage(null, "", null),"");
		assertEquals(PropertiesUtil.getMessage("", "", null),"");
		//
		assertEquals(PropertiesUtil.getMessage("tests.mytest", null, null), "");
		assertEquals(PropertiesUtil.getMessage(null, "myname", null), "");
	}
	
}
