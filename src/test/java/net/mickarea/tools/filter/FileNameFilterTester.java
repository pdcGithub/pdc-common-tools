/******************************************************************************************************

This file "FileNameFilterTester.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.SystemUtil;
import net.mickarea.tools.utils.test.ConcurrencyTestUtil;

/**
 * 这里是 net.mickarea.tools.filter.FileNameFilter 这个文件名过滤器的单元测试。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年9月30日
 */
public class FileNameFilterTester {

	/**
	 * 定义一个测试文件存放的文件夹
	 */
	public static String TEST_DIR = SystemUtil.getUserHome();
	
	/**
	 * 这些是测试文件的前缀
	 */
	public static String FILE_PREFFIX = "PdcUnitTest";
	
	/**
	 * 这些是测试文件的后缀
	 */
	public static String FILE_SUBFFIX = "txt";
	
	/**
	 * 首先定义4个测试用的文件名
	 */
	public static List<String> NAMES = Arrays.asList("1abcdefgh", "2ABCDEFGH", "3abcdeFGH", "4ABCDefgh");
	
	/**
	 * 这里是单元测试的初始化处理。它负责在单元测试方法执行前，先初始化环境。
	 * @throws Exception 
	 */
	@BeforeAll
	public static void beforeAll() throws Exception {
		
		// 循环构建测试文件
		for(String name : NAMES) {
			// 定义完整路径
			String fileAbsPath = TEST_DIR + File.separator + (FILE_PREFFIX + "_" + name + "." + FILE_SUBFFIX);
			Path filePath = Paths.get(fileAbsPath); 
			// 如果文件不存在则创建，否则不创建
			if(Files.notExists(filePath)) {
				Files.createFile(filePath);
				Stdout.pl("创建测试用的文件 "+fileAbsPath);
			}else {
				Stdout.pl("跳过测试用的文件 "+fileAbsPath);
			}
		}
		
		Stdout.pl("测试用的文件创建结束，开始测试 ...");
	}
	
	/**
	 * 这里是单元测试的终结处理。她负责在所有测试结束后，清理测试环境
	 */
	@AfterAll
	public static void afterAll() throws Exception {
		
		// 循环清除测试文件
		for(String name : NAMES) {
			// 定义完整路径
			String fileAbsPath = TEST_DIR + File.separator + (FILE_PREFFIX + "_" + name + "." + FILE_SUBFFIX);
			Path filePath = Paths.get(fileAbsPath); 
			// 如果存在，则删除
			Files.deleteIfExists(filePath);
			Stdout.pl("删除测试用的文件 "+fileAbsPath);
		}
		
		Stdout.pl("测试用的文件清理结束，结束测试 ...");
		
	}
	
	/**
	 * 这里我们测试，能不能准确获取对应的文件。
	 */
	@RepeatedTest(failureThreshold = 1, value = 5)
	void filterCase() throws Exception {
		
		// 正则
		String regexpStr = "\\S+\\_\\d[a-h]{8}\\.txt";
		
		// 先确定要检索的文件夹
		File testDir = new File(TEST_DIR);
		
		// ================ 先测试区分大小写
		String[] pathArr = testDir.list(new FileNameFilter(regexpStr, false));
		
		// 正常来说，应该只能匹配到 1abcdefgh 这个文件
		assertEquals(1, pathArr.length);
		assertEquals(FILE_PREFFIX+"_"+NAMES.get(0)+"."+FILE_SUBFFIX, pathArr[0]);
		
		// ================ 再测试不区分大小写
		String[] pathArr2 = testDir.list(new FileNameFilter(regexpStr, true));
		
		// 正常来说，应该 能匹配到 4 个文件
		assertEquals(4, pathArr2.length);
		assertEquals(FILE_PREFFIX+"_"+NAMES.get(0)+"."+FILE_SUBFFIX, pathArr2[0]);
		assertEquals(FILE_PREFFIX+"_"+NAMES.get(1)+"."+FILE_SUBFFIX, pathArr2[1]);
		assertEquals(FILE_PREFFIX+"_"+NAMES.get(2)+"."+FILE_SUBFFIX, pathArr2[2]);
		assertEquals(FILE_PREFFIX+"_"+NAMES.get(3)+"."+FILE_SUBFFIX, pathArr2[3]);
		
		// ================ 再测试默认参数，它相当于 不区分大小写
		String[] pathArr3 = testDir.list(new FileNameFilter(regexpStr));
		
		// 正常来说，应该 能匹配到 4 个文件
		assertEquals(4, pathArr3.length);
		assertEquals(FILE_PREFFIX+"_"+NAMES.get(0)+"."+FILE_SUBFFIX, pathArr3[0]);
		assertEquals(FILE_PREFFIX+"_"+NAMES.get(1)+"."+FILE_SUBFFIX, pathArr3[1]);
		assertEquals(FILE_PREFFIX+"_"+NAMES.get(2)+"."+FILE_SUBFFIX, pathArr3[2]);
		assertEquals(FILE_PREFFIX+"_"+NAMES.get(3)+"."+FILE_SUBFFIX, pathArr3[3]);
		
		// ================ 再测试 无参。正常来说，没有正常初始化的过滤对象，执行时会报错，然后返回 false。相当于 一个文件都匹配不到
		String[] pathArr4 = testDir.list(new FileNameFilter());
		// 正常来说，应该 一个文件都匹配不到
		assertEquals(0, pathArr4.length);
		
	}
	
	/**
	 * 这里我们测试，能不能准确获取对应的文件。（这里是多线程情况下的测试）
	 */
	@RepeatedTest(failureThreshold = 1, value = 5)
	void filterCaseConcurr() throws Exception {
	
		// 正则
		String regexpStr = "\\S+\\_\\d[a-h]{8}\\.txt";
		
		// 先确定要检索的文件夹
		File testDir = new File(TEST_DIR);
		
		// 并发线程数
		int threadNum = 100;
		
		// 执行结果
		List<String[]> reList = Collections.synchronizedList(new ArrayList<String[]>());
		
		// 这个记录 异常对象
		List<Exception> excepList = Collections.synchronizedList(new ArrayList<Exception>());
		
		// =============================================================== 测试 1
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			try {
				
				Thread.sleep(6);
				
				// ================ 先测试区分大小写
				reList.add(testDir.list(new FileNameFilter(regexpStr, false)));
				
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
		
		// 不应该出现异常
		assertEquals(0, excepList.size());
		
		// 然后执行结果应该全是 1abcdefgh 这个文件 
		long actCount = reList.stream().map(strArr->{
			return strArr.length==1 && (FILE_PREFFIX+"_"+NAMES.get(0)+"."+FILE_SUBFFIX).equals(strArr[0]);
		}).filter(boo->boo==true).count();
		//
		assertEquals(threadNum, actCount);
		
		// 清理结果列表。再测试一下 不区分大小写 ==========================
		reList.clear();
		excepList.clear();
		
		// =============================================================== 测试 2
		ConcurrencyTestUtil.test(threadNum, 10, TimeUnit.SECONDS, ()->{
			try {
				
				Thread.sleep(6);
				
				// ================ 这里测试的是 不区分大小写 ignoreCase = true
				reList.add(testDir.list(new FileNameFilter(regexpStr, true)));
				
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
		
		// 不应该出现异常
		assertEquals(0, excepList.size());
		
		// 然后执行结果应该全是 全部 4个文件 
		actCount = reList.stream().map(strArr->{
			return strArr.length==4 
					&& (FILE_PREFFIX+"_"+NAMES.get(0)+"."+FILE_SUBFFIX).equals(strArr[0]) 
					&& (FILE_PREFFIX+"_"+NAMES.get(1)+"."+FILE_SUBFFIX).equals(strArr[1]) 
					&& (FILE_PREFFIX+"_"+NAMES.get(2)+"."+FILE_SUBFFIX).equals(strArr[2]) 
					&& (FILE_PREFFIX+"_"+NAMES.get(3)+"."+FILE_SUBFFIX).equals(strArr[3]) ;
		}).filter(boo->boo==true).count();
		// 
		assertEquals(threadNum, actCount);
		
	}
	
}
