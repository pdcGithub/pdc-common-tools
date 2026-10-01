/******************************************************************************************************

This file "TestAnnoScanMain1.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.maintests;

import java.io.File;
import java.io.FilenameFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import net.mickarea.tools.filter.FileNameFilter;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 这里是一个 main 函数测试程序。它用于验证一些功能是否可行，并非自动化的单元测试.
 * 要测试的内容：根据所属的 包路径 和 注解类，搜索在这个包下面的类中，有哪些类被这个注解标记，并返回这些类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年9月28日
 */
public class TestAnnoScanMain1 {

	public static void main(String[] args) {
		
		// 这里用逗号分隔几个包名，这样可以出现 file 协议 和 jar 协议。方便测试
		String packageName = "com.microsoft.sqlserver.jdbc, net.mickarea.tools.utils";
		
		// 加载器
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		// 把包名处理下，转换为一个待处理的 列表
		List<String> packagePaths = Arrays.asList(packageName.split(",")).stream()
			.map(name->{
				// 拆解后，去掉空白字符。然后，把 点号，替换为 / 。因为 loader 的 getResources 需要 path 不是 包名。
				return StrUtil.removeAllBlankStrings(name).replaceAll("\\.", "/");
			})
			.filter(path->{
				// 过滤掉空白的内容，保留非空内容
				return !StrUtil.isEmptyString(path);
			})
			.distinct() // 去重
			.collect(Collectors.toList()); // 最后收集为一个 list 
		
		// 打印一下要处理的 包路径
		Stdout.pl("将要处理的包路径如下："+ packagePaths);
		
		// 对包路径进行遍历
		packagePaths.forEach(path->{
			try {
				//
				Stdout.pl("开始处理 "+path);
				
				// 得到一个枚举对象
				Enumeration<URL> urls = loader.getResources(path);
				while(urls.hasMoreElements()) {
					// 把 URL 链接传递下去
					// 如果 class 是 jar 包内的 会执行 jar 搜索处理
					// 如果 class 是 本地文件路径 会执行 file 搜索处理
					TestAnnoScanMain1.getClassListByJarOrFile(urls.nextElement(), path);
				}
				
			} catch (Exception e) {
				// 打印异常信息
				Stdout.pl(e);
			}
		});

	}
	
	public static final List<String> getClassListByJarOrFile(URL resourceURL, String searchingPath) throws Exception {
		
		// 
		List<String> result = new ArrayList<String>();
		
		// 这里根据不同的协议，处理方式不同。
		// file 是文本协议用 File 对象处理。
		// jar 是压缩包协议 用 JarUrlConnection 对象处理
		if("jar".equalsIgnoreCase(resourceURL.getProtocol())) {
			
			Stdout.pl("============================ jar ==========================");
			
			// 先把 URL 转换为 JarFile 对象
			JarFile file = ((JarURLConnection)resourceURL.openConnection()).getJarFile();
			
			// 把 jar 包内部的信息转为 List 对象。
			// 对于 jar 它会 完整解包
			// 所以，只处理 searchingPath 及其 下级目录，然后文件只处理 .class。并且 class 文件只处理 不带 $ 符号的。
			// 因为，带 $ 符号的是 类内部的子类
			List<JarEntry> jarEntryies = ListUtil.makeEnumerationObjectToListObject(file.entries());
			
			// 通过流处理，过滤出需要搜索的文件夹
			// 前提，jarEntryies 不是空列表。如果 file.entries() 没有内容，则 List 是 null
			if(!ListUtil.isEmptyList(jarEntryies)) {
				// 先找出需要遍历的文件夹
				// 以为 jar 是解包搜索，所以遍历一次就行了
				List<String> jarTargetList = jarEntryies.parallelStream() // 这是并行流，加速处理
													.filter(jarEntry->{
														// 过滤出 以 searchingPath 开头的class 文件
														// class 不能带 $ 符号
														return jarEntry.getName().startsWith(searchingPath) 
																&& jarEntry.getName().endsWith(".class")
																&& !jarEntry.getName().contains("$")
																&& !jarEntry.isDirectory();
													})
													.map(jarEntry->{
														// 转换 jarEntry 对象 为 路径字符串
														return jarEntry.getName();
													})
													.distinct() // 去重
													.sorted() // 排序
													.collect(Collectors.toList()); // 最后，收集为字符串列表
													
				// 打印一下文件夹信息
				jarTargetList.forEach(Stdout::pl);
			}
			
		} else if("file".equalsIgnoreCase(resourceURL.getProtocol())) {
			
			Stdout.pl("============================ file ==========================");
			
			File file = new File(resourceURL.toURI());
			
			// 递归搜索，并返回
			FilenameFilter searchFilter = new FileNameFilter("[\\s\\S]+\\.class", true, true);
			List<String> absPaths = TestAnnoScanMain1.searchLocalFiles(file, true, searchFilter);
			
			// 打印看看
			absPaths.stream().distinct().sorted().forEach(path->{
				Stdout.pl(path);
			});
			
		} else {
			Stdout.fpl("遇到无法处理的文件，url=%s, 协议=%s", resourceURL, resourceURL.getProtocol());
			
		}
		
		// 返回
		return result ;
	}
	
	/**
	 * 根据本地路径，一层层递归搜索，全部的文件绝对路径信息
	 * @param directory 文件夹目录对象
	 * @param recursive 是否递归搜索子文件夹。true 则递归搜索，false 则只搜索当前传入的文件夹
	 * @param fileSubffix 要搜索的文件后缀
	 * @return 一个文件信息列表。如果传入的参数不是文件夹，或者文件夹没有文件，则返回空列表。空列表指的是：null 或者 长度为0
	 */
	public static final List<String> searchLocalFiles(File directory, boolean recursive, FilenameFilter filter) {
		
		// 定义一个返回结果
		List<String> result = null;
		
		// 如果 文件夹 对象信息为空，则不处理。
		// 如果 不是文件夹也不处理
		if(directory==null || !directory.isDirectory()) return result;
		
		// 先初始化
		result = new ArrayList<String>();
		
		// 首先搜索 传入的目录（这里如果有设置过滤器，则过滤；没有则不过滤）
		File[] currFiles = filter==null ? directory.listFiles() : directory.listFiles(filter);
		for(int i=0;i<currFiles.length;i++) {
			
			// 如果是文件，插入列表，并跳过后续处理
			if(currFiles[i].isFile()) {
				// 放入结果列表
				result.add(currFiles[i].getAbsolutePath());
				// 结束本次循环
				continue;
			}
			
			// 如果是文件夹，并且配置了递归搜索，则递归搜索
			// 否则，不递归搜索
			if(recursive && currFiles[i].isDirectory()) {
				// 递归得到一个文件路径列表
				List<String> recursiveFiles = TestAnnoScanMain1.searchLocalFiles(currFiles[i], recursive, filter);
				// 如果有内容，则放入结果列表，否则不放入
				if(!ListUtil.isEmptyList(recursiveFiles)) result.addAll(recursiveFiles);
				// 结束本次循环
				continue;
			}
			
			// 其它不用处理
		}
		
		// 返回
		return result;
	}
	
}
