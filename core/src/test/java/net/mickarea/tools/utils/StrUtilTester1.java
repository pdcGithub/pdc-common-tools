/******************************************************************************************************

This file "StrUtilTester1.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 这是一个关于 StrUtil 的单元测类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年12月10日
 */
public class StrUtilTester1 {
	
	@BeforeEach
	void inits() {
		// 这是每次都执行的初始化处理
	}
	
	@AfterEach
	void finals() {
		// 这是每次都执行的最终处理
	}
	
	@Test
	void makeHumpStringIsOk() {
		
		// 这里执行 makeHumpString 函数，一些不会报错的测试
		
		assertDoesNotThrow(()->{
			// 返回空字符串的处理
			assertEquals("", StrUtil.makeHumpString(null, null, null, false));
			assertEquals("", StrUtil.makeHumpString(null, "", null, false));
			assertEquals("", StrUtil.makeHumpString(null, "a", null, false));
			assertEquals("", StrUtil.makeHumpString("", null, null, false));
			assertEquals("", StrUtil.makeHumpString("", "", null, false));
			assertEquals("", StrUtil.makeHumpString("", "a", null, false));
			assertEquals("", StrUtil.makeHumpString("a", null, null, false));
			assertEquals("", StrUtil.makeHumpString("a", "", null, false));
			assertEquals("", StrUtil.makeHumpString("a", "a", null, false));
			
			// 返回驼峰写法的处理
			assertEquals("WwwMickareaNet", StrUtil.makeHumpString("www.mickarea.net", "\\.", null, false));
			assertEquals("WwwMickareaNet", StrUtil.makeHumpString("/www/mickarea/net/", "/", null, false));
			assertEquals("WwwMickareaNet", StrUtil.makeHumpString("www_mickarea_net", "\\_", null, false));
			
			// 返回驼峰写法的处理 测试拼接符号的处理
			assertEquals("WWW...MICKAREA...NET", StrUtil.makeHumpString("WWW_MICKAREA_NET", "\\_", "...", false));
			assertEquals("WWW/MICKAREA/NET", StrUtil.makeHumpString("WWW_MICKAREA_NET", "\\_", "/", false));
			assertEquals("WWWMICKAREANET", StrUtil.makeHumpString("WWW_MICKAREA_NET", "\\_", "", false));
			assertEquals("WWW MICKAREA NET", StrUtil.makeHumpString("WWW_MICKAREA_NET", "\\_", " ", false));
			
			// 返回驼峰写法的处理 测试大小写预处理（如果为true，则在转换首字符前，会做一个全部的转小写处理）
			assertEquals("WWWMICKAREANET", StrUtil.makeHumpString("WWW_MICKAREA_NET", "\\_", null, false));
			assertEquals("WwwMickareaNet", StrUtil.makeHumpString("WWW_MICKAREA_NET", "\\_", null, true));
		});
		
		// 这里执行 另外一个 makeHumpString 函数，一些不会报错的测试 (这里 joinString 为空字符串，toLowerCase 为 true)
		
		assertDoesNotThrow(()->{
			// 返回空字符串的处理
			assertEquals("", StrUtil.makeHumpString(null, null));
			assertEquals("", StrUtil.makeHumpString(null, ""));
			assertEquals("", StrUtil.makeHumpString(null, "a"));
			assertEquals("", StrUtil.makeHumpString("", null));
			assertEquals("", StrUtil.makeHumpString("", ""));
			assertEquals("", StrUtil.makeHumpString("", "a"));
			assertEquals("", StrUtil.makeHumpString("a", null));
			assertEquals("", StrUtil.makeHumpString("a", ""));
			assertEquals("", StrUtil.makeHumpString("a", "a"));
			
			// 返回驼峰写法的处理
			assertEquals("WwwMickareaNet", StrUtil.makeHumpString("WWW_MICKAREA_NET", "\\_"));
			assertEquals("TestsMynameIamold", StrUtil.makeHumpString("/tests/myName/IAMold", "/"));
		});
	}
	
	/**
	 * 这是 StrUtil.injectionTranslateForSQL(String sqlParam) 的单元测试
	 */
	@SuppressWarnings("deprecation")
	@Test
	void injectionTranslateForSQL_1() {
		
		// ========= 这里对比 injectionTranslateForSQL_2 ，它固定了 dbType 为 mysql 
		
		// ======== 首先是错误参数的测试
		assertEquals("", StrUtil.injectionTranslateForSQL(null));
		assertEquals("", StrUtil.injectionTranslateForSQL(""));
		assertEquals(" ", StrUtil.injectionTranslateForSQL(" "));
		assertEquals(" ", StrUtil.injectionTranslateForSQL("        "));
		
		// 待转换的字符串
		String oriStr = "\\ \" % ' _ = ; abcdef ` ...";
		
		// ========= 然后是正常的参数处理
		assertEquals("\\\\ \\\" \\% \\' \\_ \\= \\; abcdef ` ...", StrUtil.injectionTranslateForSQL(oriStr)); // mysql 全部加斜杠
	}
	
	/**
	 * 这是 StrUtil.injectionTranslateForSQL(String dbType, String sqlParam) 的单元测试
	 */
	@SuppressWarnings("deprecation")
	@Test
	void injectionTranslateForSQL_2() {
		
		// ======== 首先是错误参数的测试（不管 dbType 如何，默认都是 只处理 单引号）
		assertEquals("'';", StrUtil.injectionTranslateForSQL(null, "';"));
		assertEquals("'';", StrUtil.injectionTranslateForSQL("", "';"));
		assertEquals("'';", StrUtil.injectionTranslateForSQL(" ", "';"));
		assertEquals("'';", StrUtil.injectionTranslateForSQL("aaaaa", "';"));
		// 然后是 sqlParam 
		assertEquals("", StrUtil.injectionTranslateForSQL("mysql_ansi", null));
		assertEquals("", StrUtil.injectionTranslateForSQL("mysql_ansi", ""));
		assertEquals(" ", StrUtil.injectionTranslateForSQL("mysql_ansi", " "));        // 转换只是替换，不会修改
		assertEquals(" ", StrUtil.injectionTranslateForSQL("mysql_ansi", "        ")); // 多个空格会处理为一个。转换只是替换，不会修改
		
		// 待转换的字符串
		String oriStr = "\\ \" % ' _ = ; abcdef ` ...";
		
		// ========= 然后是正常的参数处理
		assertEquals("\\\\ \\\" \\% \\' \\_ \\= \\; abcdef ` ...", StrUtil.injectionTranslateForSQL("mysql", oriStr)); // mysql 全部加斜杠
		assertEquals("\\ \" % '' _ = . abcdef ` ...", StrUtil.injectionTranslateForSQL("db2", oriStr)); // db2 单引号转2个单引号，分号转 点号。
		assertEquals("\\ \" % '' _ = ; abcdef ` ...", StrUtil.injectionTranslateForSQL("mysql_ansi", oriStr)); // 默认处理 单引号 单引号转2个单引号
		assertEquals("\\ \" % '' _ = ; abcdef ` ...", StrUtil.injectionTranslateForSQL("oracle", oriStr));     // 默认处理 单引号 单引号转2个单引号
		assertEquals("\\ \" % '' _ = ; abcdef ` ...", StrUtil.injectionTranslateForSQL("default", oriStr));    // 默认处理 单引号 单引号转2个单引号
	}
	
}
