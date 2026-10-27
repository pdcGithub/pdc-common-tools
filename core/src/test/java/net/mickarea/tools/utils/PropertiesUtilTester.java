/******************************************************************************************************

This file "PropertiesUtilTester.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.mickarea.tools.utils.test.ConcurrencyTestUtil;

/**
 * 这里是 PropertiesUtil 工具类的测试类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年10月30日
 */
public class PropertiesUtilTester {

	@BeforeEach
	void setTheEnv() throws Exception {
		// 每次测试前，都设置一下地区。这样可以保证默认是 英文
		Locale.setDefault(Locale.ENGLISH);
	}
	
	@Test
	void getMessageOKTest() throws Exception {
		// getMessage 的正常返回测试
		assertEquals(PropertiesUtil.getMessage("tests.mytest", "myname"), "Tom");
		assertEquals(PropertiesUtil.getMessage("i18n.messages", "test.greeting"), "Hello");
	}
	
	@Test
	void getMessageERRORTest() throws Exception {
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
	void getMessageWithLocaleOKTest() throws Exception {
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
	void getMessageWithLocaleERRORTest() throws Exception {
		// getMessage 的异常返回测试（附带区域选择）
		assertEquals(PropertiesUtil.getMessage(null, null, null),"");
		assertEquals(PropertiesUtil.getMessage("", null, null),"");
		assertEquals(PropertiesUtil.getMessage(null, "", null),"");
		assertEquals(PropertiesUtil.getMessage("", "", null),"");
		//
		assertEquals(PropertiesUtil.getMessage("tests.mytest", null, null), "");
		assertEquals(PropertiesUtil.getMessage(null, "myname", null), "");
	}
	
	/**
	 * 这里负责测试 searchPropertiesFiles 方法是否正常搜索到预想的结果
	 * @throws Exception 可能的异常
	 */
	@Test
	void searchPropertiesFiles() throws Exception {
		
		// =============================== 先测试异常的参数
		
		// 对于文件夹（文件夹最少要有一个英文字母或者数字，否则会从 classpath 根路径搜索。 ）
		// 
		// 要注意：folder 不含数字或者字母时，进行 classes 根路径搜索 。这不会包含 jar 包中的配置，除非你指定了 文件夹路径。比如：conf/
		//
		List<String> errRe1 = PropertiesUtil.searchPropertiesFiles(null, ".+");
		List<String> errRe2 = PropertiesUtil.searchPropertiesFiles("", ".+");
		List<String> errRe3 = PropertiesUtil.searchPropertiesFiles(" ", ".+");
		List<String> errRe4 = PropertiesUtil.searchPropertiesFiles("   ", ".+");
		List<String> errRe5 = PropertiesUtil.searchPropertiesFiles(".", ".+");
		List<String> errRe6 = PropertiesUtil.searchPropertiesFiles("..", ".+");
		List<String> errRe7 = PropertiesUtil.searchPropertiesFiles("/", ".+");
		List<String> errRe8 = PropertiesUtil.searchPropertiesFiles("./", ".+");
		List<String> errRe9 = PropertiesUtil.searchPropertiesFiles("../", ".+");
		List<String> errRe10 = PropertiesUtil.searchPropertiesFiles("../", ".+");
		List<String> errRe11 = PropertiesUtil.searchPropertiesFiles("?.", ".+");
		List<String> errRe12 = PropertiesUtil.searchPropertiesFiles("+.", ".+");
		// 
		List<String> errRe13 = PropertiesUtil.searchPropertiesFiles("", null); // 这里测试 异常的正则。为 null 的话就是 全部 properties
		List<String> errRe14 = PropertiesUtil.searchPropertiesFiles("", "");   // 这里文件名为空，再程序中有后缀，这样就匹配不到结果了
		List<String> errRe15 = PropertiesUtil.searchPropertiesFiles("", " ");  // 后面这几个，一样的，匹配不到结果的
		List<String> errRe16 = PropertiesUtil.searchPropertiesFiles("", "   ");
		List<String> errRe17 = PropertiesUtil.searchPropertiesFiles("", "aaaa");
		// 
		assertTrue(5 <= errRe1.size()); // 根据目前配置，搜索根路径，最少有5个。resources 5 （test jar 里面，需要指定文件夹才能搜索）
		assertEquals(errRe1.size(), errRe2.size());   // 其余的，应该都是一样的
		assertEquals(errRe1.size(), errRe3.size());
		assertEquals(errRe1.size(), errRe4.size());
		assertEquals(errRe1.size(), errRe5.size());
		assertEquals(errRe1.size(), errRe6.size());
		assertEquals(errRe1.size(), errRe7.size());
		assertEquals(errRe1.size(), errRe8.size());
		assertEquals(errRe1.size(), errRe9.size());
		assertEquals(errRe1.size(), errRe10.size());
		assertEquals(errRe1.size(), errRe11.size());
		assertEquals(errRe1.size(), errRe12.size());
		// 
		assertEquals(errRe1.size(), errRe13.size());
		assertEquals(0, errRe14.size());
		assertEquals(0, errRe15.size());
		assertEquals(0, errRe16.size());
		assertEquals(0, errRe17.size());
		
		// =============================== 测试正常的参数，看看能不能获得预想结果
		
		List<String> okRe1 = PropertiesUtil.searchPropertiesFiles("conf",  "demo\\..+");
		List<String> okRe2 = PropertiesUtil.searchPropertiesFiles("conf/", "demo\\..+");
		assertEquals(1, okRe1.size());
		assertEquals(okRe1.size(), okRe2.size());
		assertEquals("conf/email-config/demo.emailserver.properties", okRe1.get(0));
		assertEquals(okRe1.get(0), okRe2.get(0));
		
		// 搜索 db 配置文件。包含测试和正式 ，包含 jartest 的包里面的
		List<String> okRe3 = PropertiesUtil.searchPropertiesFiles("conf",  "db.+");
		assertEquals(2, okRe3.size());
		List<String> okRe4 = PropertiesUtil.searchPropertiesFiles("conf",  "unittest.+");
		assertEquals(1, okRe4.size());
		List<String> okRe31 = PropertiesUtil.searchPropertiesFiles("conf/",  "db.+");
		assertEquals(2, okRe31.size());
		List<String> okRe41 = PropertiesUtil.searchPropertiesFiles("conf/",  "unittest.+");
		assertEquals(1, okRe41.size());
		List<String> okRe311 = PropertiesUtil.searchPropertiesFiles("conf////",  "db.+");
		assertEquals(2, okRe311.size());
		List<String> okRe411 = PropertiesUtil.searchPropertiesFiles("conf////",  "unittest.+");
		assertEquals(1, okRe411.size());
		
		// 再测试一下
		List<String> okRe5 = PropertiesUtil.searchPropertiesFiles("i18n/", "message.+");
		assertEquals(2, okRe5.size());
		assertEquals("i18n/messages_en.properties", okRe5.stream().sorted().collect(Collectors.toList()).get(0));
		
		// 这里验证 jar 路径资源搜索
		List<String> okRe6 = PropertiesUtil.searchPropertiesFiles("conf/", null); //resouces 1 个 + jar 3
		assertEquals(4, okRe6.size());
		List<String> okRe7 = PropertiesUtil.searchPropertiesFiles("conf/", ".+jar3"); // 只搜索 jar 中的一个。
		assertEquals(1, okRe7.size());
		assertEquals("conf/database-config/testfolder1/db_unittest_jar3.properties", okRe7.get(0));
	}
	
	/**
	 * 这里测试 searchPropertiesFiles 方法在多线程并发下，能否保证数据的一致性。
	 */
	@Test
	void searchPropertiesFiles_concurr() throws Exception {
		
		// 定义线程 和 测试信息
		int threadNum = 100;
		int timeout = 10;
		TimeUnit timeoutUnit = TimeUnit.SECONDS;
		
		// 定义装载结果的 同步 map。在多线程下，普通的容器会产生线程问题，必须用 同步容器。
		Map<Integer, String> reMap = Collections.synchronizedMap(new HashMap<Integer, String>());
		
		// 定义一个装载 异常信息的 list
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		
		// 开始测试
		ConcurrencyTestUtil.test(threadNum, timeout, timeoutUnit, ()->{
			try {
				// 在测试时，我们根据线程号，进行随机 搜索。单数搜索 resources下的 i18n/, 双数搜索 jar 下的 conf/database-config/testfolder1/
				
				// 线程号 
				String threadName = Thread.currentThread().getName();
				String num = threadName.substring(threadName.lastIndexOf("-")+1);
				// 对应的数据序号
				int tNum = Integer.parseInt(num);
				int targetNum = tNum % 2; // 在 0 和 1 之间随机
				// 
				String re = "";
				if(targetNum==0) {
					List<String> lRe = PropertiesUtil.searchPropertiesFiles("conf/database-config/testfolder1/", ".+jar3");
					if(lRe.size()>=1) re = lRe.get(0);
				}
				if(targetNum==1) {
					List<String> lRe2 = PropertiesUtil.searchPropertiesFiles("i18n/", "messages\\_en");
					if(lRe2.size()>=1) re = lRe2.get(0);
				}
				
				reMap.put(tNum, re); // 记录每个线程的结果
				
			}catch(Exception e) {
				if(e instanceof InterruptedException) {
					// 线程中断处理
					Thread.currentThread().interrupt();
				}else {
					// 记录异常
					excepList.add(e);
				}
			}
		});
		
		// 打印看看
		// reMap.entrySet().stream().forEach(Stdout::pl);
		
		// ======= 结果比对
		assertEquals(0, excepList.size()); // 异常为0
		assertEquals(threadNum, reMap.size());
		assertEquals(threadNum, reMap.entrySet().stream().map(entry->entry.getKey()).distinct().count());
		assertEquals(2, reMap.entrySet().stream().map(entry->entry.getValue()).distinct().count());
		// 最后比对每个线程的结果
		assertEquals(threadNum, reMap.entrySet().stream()
									.map(entry->{
										int key = entry.getKey().intValue();
										String value = entry.getValue();
										boolean re = false;
										if(key%2==0) re = "conf/database-config/testfolder1/db_unittest_jar3.properties".equals(value);
										if(key%2==1) re = "i18n/messages_en.properties".equals(value);
										return re;
									})
									// 正常来说，每个线程的结果都是符合预期，然后 有 threadnum 个 true 。
									.filter(t->t==true)
									.count());
	}
	
}
