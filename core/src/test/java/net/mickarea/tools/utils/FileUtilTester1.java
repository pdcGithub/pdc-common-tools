/******************************************************************************************************

This file "FileUtilTester1.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.mickarea.tools.filter.FileNameFilter;

/**
 * 这是 net.mickarea.tools.utils.FileUtil 的单元测试。
 * 因为这个类是很久以前开发的。目前是逐步补全单元测试代码，并非全部代码都有单元测试对应。
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月8日
 */
public class FileUtilTester1 {

	/**
	 * 定义一个测试文件存放的文件夹（这个文件夹，不能和其它测试重复，否则会造成IO异常）
	 */
	public static String TEST_DIR = SystemUtil.getUserHome() + File.separator + "FileUtilTester1_tests";
	
	/**
	 * 这里定义5个测试用的文件名。
	 * 其中 第1和2是小写文件后缀；第3和4是大写的为文件后缀；
	 * 第5个 是在二级目录 test2 下面。用于测试递归处理。
	 */
	public static List<String> TEST_FILES = Arrays.asList(
		"0lskdjflsdjfsdjkf.txt", /* 这个 0 号文件 是用于测试 过滤器的，他命名规则不符合测试的正则 */
		"1abcdefgh.txt", "2ABCDEFGH.txt", "3abcdeFGH.TXT", "4ABCDefgh.TxT", 
		"test2"+File.separator+"5ABCDefgh.txt"
	);
	
	/**
	 * 这里负责初始化测试用的资源
	 * @throws Exception 一些可能出现的异常。当 JUnit 检测到异常，会终止这个单元测试。
	 */
	@BeforeAll
	public static void beforeAll() throws Exception {
		
		// 创建 测试文件 以及 文件夹
		for(int i=0;i<TEST_FILES.size();i++) {
			// 构建一个完整的路径
			File tmpFile = new File(TEST_DIR + File.separator + TEST_FILES.get(i));
			if(tmpFile.exists()) {
				// 如果已经存在，则不创建
				Stdout.mylogger.debug(Stdout.fplToAnyWhere("已存在 文件 %s", tmpFile.getAbsolutePath()));
			}else {
				// 否则，需要创建上级文件夹，以及 本文件。
				File parentDir = tmpFile.getParentFile();
				// 这里创建 上级文件夹
				if(!parentDir.exists()) parentDir.mkdirs();
				// 这里创建 本文件
				tmpFile.createNewFile();
				// 
				Stdout.mylogger.debug(Stdout.fplToAnyWhere("创建  文件 %s", tmpFile.getAbsolutePath()));
			}
		}
		
	}
	
	/**
	 * 这里负责销毁一些测试用的资源
	 * @throws Exception 一些可能出现的异常。当 JUnit 检测到异常，会终止这个单元测试。
	 */
	@AfterAll
	public static void afterAll() throws Exception {
		
		// 创建一个列表，用于记录要删除的东西
		List<Path> allPaths = new ArrayList<Path>();
		TEST_FILES.forEach(name->{
			// 记录文件
			allPaths.add(Paths.get(TEST_DIR + File.separator + name));
		});
		// 记录文件夹
		allPaths.add(Paths.get(TEST_DIR + File.separator + "test2"));
		allPaths.add(Paths.get(TEST_DIR));
		
		// 这里负责删除文件以及文件夹
		for(int i=0;i<allPaths.size();i++) {
			// 存在的话，则删除
			Files.deleteIfExists(allPaths.get(i));
		}
		
		// 检查删除情况
		allPaths.forEach(absPath->{
			Stdout.mylogger.debug(
				Stdout.fplToAnyWhere(
					"%s 文件 %s", Files.exists(absPath)?"未删除":"已删除", absPath.toAbsolutePath())
			);
		});
		
	}
	
	/**
	 * 这里进行的是 searchFilesFromLocalDir 测试
	 * @throws Exception 任何从测试代码抛出的异常
	 */
	@Test
	void test_searchLocal() throws Exception {
		
		// 
		// 这个测试，它需要在 beforeAll 里面创建测试用的文件
		// 
		
		// 文件名检索用的正则表达式
		String regexpStr = "\\d[a-h]{8}\\.txt";
		
		// 首先对于参数，进行一个 异常参数 可能性遍历 ========================
		// 第一个参数
		List<String> result1 = FileUtil.searchFilesFromLocalDir(null, true, new FileNameFilter(regexpStr));
		List<String> result2 = FileUtil.searchFilesFromLocalDir(new File(""), true, new FileNameFilter(regexpStr));
		List<String> result3 = FileUtil.searchFilesFromLocalDir(new File("."), true, new FileNameFilter(regexpStr));
		List<String> result4 = FileUtil.searchFilesFromLocalDir(new File("unknown"), true, new FileNameFilter(regexpStr));
		List<String> result5 = FileUtil.searchFilesFromLocalDir(new File(TEST_DIR), true, new FileNameFilter(regexpStr));
		assertTrue(ListUtil.isEmptyList(result1)); // 空
		assertTrue(ListUtil.isEmptyList(result2)); // 空
		assertTrue(ListUtil.isEmptyList(result3)); // 空
		assertTrue(ListUtil.isEmptyList(result4)); // 空
		assertEquals(5, result5.stream().distinct().count()); // 第5个是测试文件夹，递归遍历，忽略文件名大小写。所以，5个文件都可以搜索到
		// 第二个参数
		List<String> result6 = FileUtil.searchFilesFromLocalDir(new File(TEST_DIR), false, new FileNameFilter(regexpStr));
		List<String> result7 = FileUtil.searchFilesFromLocalDir(new File(TEST_DIR), true, new FileNameFilter(regexpStr));
		assertEquals(4, result6.stream().distinct().count()); // 不递归搜搜，结果是 4个文件
		assertEquals(5, result7.stream().distinct().count()); // 递归搜索，结果是 5 个文件
		// 第三个参数
		List<String> result8 = FileUtil.searchFilesFromLocalDir(new File(TEST_DIR), false, null);
		List<String> result9 = FileUtil.searchFilesFromLocalDir(new File(TEST_DIR), true, null);
		List<String> result10 = FileUtil.searchFilesFromLocalDir(new File(TEST_DIR), true, new FileNameFilter(regexpStr));
		assertEquals(5, result8.stream().distinct().count()); // 文件名过滤 null，不递归，则返回文件夹下文件
		assertEquals(6, result9.stream().distinct().count()); // 文件名过滤 null，递归搜索，则返回所有文件
		assertEquals(5, result10.stream().distinct().count()); // 文件名过滤有配置，递归搜索，则返回规则内所有
		
		// 然后对功能进行测试，看看数据是否正常 =====================
		
		// 搜索不到文件
		List<String> okResult1 = FileUtil.searchFilesFromLocalDir(new File(TEST_DIR), true, new FileNameFilter("\\d+"));
		assertTrue(ListUtil.isEmptyList(okResult1)); // 空
		
		// 搜索 0 号文件
		List<String> okResult2 = FileUtil.searchFilesFromLocalDir(new File(TEST_DIR), true, new FileNameFilter("0[a-z]+\\.txt"));
		assertEquals(1, okResult2.stream().distinct().count());
		assertEquals(TEST_DIR+File.separator+TEST_FILES.get(0), okResult2.get(0));
		
		// 搜索 1-4 号文件
		List<String> okResult3 = FileUtil.searchFilesFromLocalDir(new File(TEST_DIR), false, new FileNameFilter(regexpStr));
		assertEquals(4, okResult3.stream().distinct().count());
		
		// 搜索 1-5 号文件
		List<String> okResult4 = FileUtil.searchFilesFromLocalDir(new File(TEST_DIR), true, new FileNameFilter(regexpStr));
		assertEquals(5, okResult4.stream().distinct().count());
	}
	
	/**
	 * 这里进行的是 searchFilesFromJar 测试
	 * @throws Exception 任何从测试代码抛出的异常
	 */
	@Test
	void test_searchJar() throws Exception {
		
		// 注意：这里进行测试的是 Jar 文件内部的文件搜索
		
		// 确定一个搜索用的 Jar 包路径（为了保证过程不被干扰，这里使用 logback 库的路径）
		// 在这个包里面，有3个类
		String packagePath = "ch.qos.logback.core.filter";
		
		// 把包路径，转换为资源路径
		String resourcePath = packagePath.replaceAll("\\.", "/");
		// 获取当前的资源加载器
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		// 获取这个包路径的 URL 资源信息
		URL jarUrl = loader.getResource(resourcePath);
		// 把 URL 转换为 JarFile 对象
		JarFile jarFile = ((JarURLConnection)jarUrl.openConnection()).getJarFile();
		
		// 定义一个正则表达式
		// 这里要注意 jar 搜索的结果是类似这样的 ch/qos/logback/core/filter/xxxxx/sdlkfjj.class
		// 这个头部一定要拼接进去
		String regexp = Pattern.quote(resourcePath+"/") + "[a-z\\_\\-\\d\\/\\s]+\\.class";
		
		// 首先对于参数，进行一个 异常参数 可能性遍历 ======================== JarFile , boolean, String
		
		// 第一个 参数
		List<String> result1 = FileUtil.searchFilesFromJar(null, true, regexp);
		assertThrows(FileNotFoundException.class, ()->{
			// 这里的 JarFile 创建会失败
			List<String> result2 = FileUtil.searchFilesFromJar(new JarFile(""), true, regexp);
			assertTrue(ListUtil.isEmptyList(result2));
		});
		assertThrows(FileNotFoundException.class, ()->{
			// 这里的 JarFile 创建会失败
			List<String> result3 = FileUtil.searchFilesFromJar(new JarFile("."), true, regexp);
			assertTrue(ListUtil.isEmptyList(result3));
		});
		assertTrue(ListUtil.isEmptyList(result1));
		// 第二个 参数
		List<String> result4 = FileUtil.searchFilesFromJar(jarFile, false, regexp);
		List<String> result5 = FileUtil.searchFilesFromJar(jarFile, true, regexp);
		assertEquals(0, result4.stream().distinct().count()); // 如果不忽略大小写，这个正则什么都匹配不到
		assertEquals(3, result5.stream().distinct().count()); // 忽略大小写，然后匹配到3个
		// 第三个 参数
		List<String> result6 = FileUtil.searchFilesFromJar(jarFile, true, null);
		List<String> result7 = FileUtil.searchFilesFromJar(jarFile, true, "");
		List<String> result8 = FileUtil.searchFilesFromJar(jarFile, true, "  ");
		assertTrue(ListUtil.isEmptyList(result6)); // 没有正则 或者 正则为空，则不检索
		assertTrue(ListUtil.isEmptyList(result7));
		assertTrue(ListUtil.isEmptyList(result8));
		
		// 然后对功能进行测试，看看数据是否正常 =====================
		
		// 换一条正则再看看
		String regexp2 = "([\\w\\d]+\\/)*[a-z\\d]+\\.class";
		
		// 看看区分大小写情况下，是否能匹配到
		
		List<String> newResult1 = FileUtil.searchFilesFromJar(jarFile, false, regexp2);
		assertEquals(0, newResult1.size()); // 在区分大小写的情况下，按照 java 命名规则，这个正则是匹配不到的才对
		
		List<String> newResult2 = FileUtil.searchFilesFromJar(jarFile, true, regexp2);
		assertTrue(newResult2.size()>0);    // 不区分大小写，才可能匹配到结果
		
		assertTrue(newResult2.size()>=newResult1.size()); // 以结果来说，忽略大小写的数量 很肯定大于 不忽略的
	}
	
}
