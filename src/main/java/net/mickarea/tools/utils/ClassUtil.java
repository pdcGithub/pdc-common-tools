/******************************************************************************************************

This file "ClassUtil.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.mickarea.tools.filter.FileNameFilter;

/**
 * 这里是一个关于 Java 类相关操作的工具类。
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月11日
 */
public final class ClassUtil {
	
	private ClassUtil() {
		// 这里把构造函数私有化，不让外部 new 处理
	}

	/**
	 * 根据传入的 Java 包名，来搜索路径下的Java类的名称。这个函数依赖 searchClassNamesFromPackage ，但是执行逻辑和它不一样。
	 * 这里的入参数是不定参数，可以传入多个或者0个包名信息。另外，这里会执行去重处理，但不进行排序
	 * 注意：这里返回的是全部类名，包括带$符号的内部类。
	 * @param packageNames Java 包名。比如：com 或者 com.sun 等等。不能为空字符串 或者 "."
	 * @return 具体包下的Java类名称字符串列表（去重+排序）。如果参数异常，或者搜索异常，返回空列表（null 或者 长度为 0）
	 */
	public static final List<String> searchClassNamesFromPackages(String... packageNames) {
		
		// 定义一个 返回的结果 
		List<String> result = null;
		
		// 首先检测入参情况。如果异常则返回 null
		if(packageNames==null || packageNames.length<=0) return result;
		
		// 初始化结果
		result = new ArrayList<String>();
		
		// 调用 searchClassNamesFromPackage 
		for(int i=0;i<packageNames.length;i++) {
			// 如果包名信息为空，则需要跳过
			if(StrUtil.isEmptyString(packageNames[i])) continue;
			// 单个搜索（要先去掉空白字符，才能保证能匹配包名）
			List<String> tmpList = searchClassNamesFromPackage(StrUtil.removeAllBlankStrings(packageNames[i]));
			// 如果有结果则合并
			if(!ListUtil.isEmptyList(tmpList)) result.addAll(tmpList);
		}
		
		// 这里进行去重，但是不排序。
		result = result.parallelStream().distinct().collect(Collectors.toList());
		
		// 返回结果
		return result;
	}
	
	/**
	 * 根据传入的 Java 包名，来搜索路径下的Java类的名称。因为外部调用时，大部分情况还需要合并处理；所以，这里不对结果进行排序和去重。
	 * 注意：这里返回的是全部类名，包括带$符号的内部类。
	 * @param packageName Java 包名。比如：com 或者 com.sun 等等。不能为空字符串 或者 "."。包路径有正则校验
	 * @return 具体包下的Java类名称字符串列表。如果参数异常，或者搜索异常，返回空列表（null 或者 长度为 0）
	 */
	public static final List<String> searchClassNamesFromPackage(String packageName) {
		
		// 定义一个 返回的结果 
		List<String> result = null;
		
		// 入参检测（不能为空，而且需要符合 java 包的命名规则）
		if(StrUtil.isEmptyString(packageName) 
				|| !PatternUtil.matches("[a-z0-9\\_\\-]+(\\.[a-z0-9\\_\\-]+)*", packageName, Pattern.CASE_INSENSITIVE)) return result;
		
		// 加载器
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if(loader==null) {
			// 如果加载器为异常，那就不需要执行了。因为，我们是依靠加载器来搜索的
			Stdout.mylogger.error(Stdout.fplToAnyWhere("The ClassLoader object is null. The search has been terminated."));
			// 终止执行
			return result;
		}
		
		// 把包名，转换为资源路径，然后转换为 URL
		String searchPath = packageName.replaceAll("\\.", "/");
		
		// 搜索（根据资源路径，得到一个 URL。然后开始搜索）
		// 一般来说，jar 是不会重复的，但是 file 协议可能重复。像代码中 main 的 classpath 和 test 的 classpath 是2个不同目录。
		// 如果直接用 getResource 返回一个路径，它可能漏掉了其它 classpath
		Enumeration<URL> enumUrls = null;
		try {
			enumUrls =loader.getResources(searchPath); // 按照搜索规则，可能有多个路径，需要遍历一下。
		} catch (Exception e) {
			Stdout.mylogger.error(Stdout.fplToAnyWhere("Failed to load resource, the path is as follows: %s", searchPath));
			Stdout.pl(e);
		}
		
		// 如果没有资源，则不用处理了。
		if(enumUrls==null) return result;
		
		// 创建空的list
		result = new ArrayList<String>();
		
		// 遍历处理
		while(enumUrls.hasMoreElements()) {
			// 每一个 url 都遍历一次
			List<String> tmp = searchClassNamesFromURL(enumUrls.nextElement(), searchPath);
			// 如果有结果，则合并进去
			if(!ListUtil.isEmptyList(tmp)) result.addAll(tmp);
		}
		
		// 返回
		return result;
	}
	
	/**
	 * 这里是根据传入的 URL 和 资源路径，来搜索路径下的Java类的名称。因为外部调用时，大部分情况还需要合并处理；所以，这里不对结果进行排序和去重。
	 * 注意：这里返回的是全部类名，包括带$符号的内部类。
	 * @param resourceURL 这是通过 ClassLoader 的 getResource 或者 getResources 方法搜索得到的资源 URL 对象。
	 * <p>资源 URL 它是类似 jar:xxxx 或者 file:xxxx 这样的一个链接信息，并非文件夹路径。但是可以转换为绝对路径</p>
	 * @param inResourcePath 这是在进行 getResource 或者 getResources 搜索时，使用的资源路径字符串。（注意，这个路径字符串，有正则校验）
	 * <p>资源搜索路径 resource path 它实际上是类所在的包名字符串，把"."替换为"/"后的一个路径字符串。虽然，getResource 方法支持 空字符串，但是这里不支持</p>
	 * @return 具体包下的Java类名称字符串列表。如果参数异常，或者搜索异常，返回空列表（null 或者 长度为 0）
	 */
	public static final List<String> searchClassNamesFromURL(URL resourceURL, String inResourcePath) {
		
		// 定义一个返回的结果
		List<String> result = null;
		
		// resourcePath 处理下空字符，只保留非空字符
		String resourcePath = StrUtil.removeAllBlankStrings(inResourcePath);
		
		// 这里对于参数做一个简单的判断。如果参数异常，则直接返回 null
		// 对于 resourcePath 它必须是路径字符串。即 net 或者 net/mickarea 这样。用正则判断是否匹配
		if(resourceURL==null 
				|| !PatternUtil.matches("[a-z0-9\\_\\-]+(\\/[a-z0-9\\_\\-]+)*/?", resourcePath, Pattern.CASE_INSENSITIVE)) return result;
		
		// 如果最后有一个 / 结束，则去掉这个
		if(resourcePath.endsWith("/")) resourcePath = resourcePath.substring(0, resourcePath.lastIndexOf("/"));
		
		// 提取 URL 的文件协议：file 或者 jar 。统一转小写，方便测试
		String protocal = resourceURL.getProtocol()==null ? "" : resourceURL.getProtocol().toLowerCase();
		
		// 根据协议，进行不同的处理
		switch (protocal) {
		case "file": // file 是文本协议用 File 对象处理。
			
			// 先把 URL 转换为一个 File 对象。注意，不一定能成功
			File file = null;
			try {
				file = new File(resourceURL.toURI());
			} catch (Exception e) {
				Stdout.mylogger.error(Stdout.fplToAnyWhere("An exception occurred while converting a URL object to a File object. URL=%s", resourceURL));
			}
			
			// file 协议，它处理的是本地文件的绝对路径。所以，先按照路径分隔符，构建一个绝对路径前缀，用于匹配。（路径分隔符，不同操作系统不一样）
			String preffix = resourcePath.replaceAll("\\/", Matcher.quoteReplacement(File.separator))+"\\";
			
			// 递归搜索，并返回（文件名过滤器，它处理的是文件名，不是整个绝对路径）
			// 如果异常，返回的是 空列表
			List<String> absPathStrs = FileUtil.searchFilesFromLocalDir(file, true, new FileNameFilter(".+\\.class", true, true));
			
			// 这里处理的是 class 文件的绝对路径（如果是空列表，则不处理）
			if(!ListUtil.isEmptyList(absPathStrs)) result = absPathStrs.parallelStream() // 开启并行流
															.map(absPath->{
																// 先计算 前缀第一次出现的位置, ".class" 最后一次出现的位置
																int startPos = absPath.indexOf(preffix);
																int endPos = absPath.lastIndexOf(".");
																// 截取 从 前缀 到 .class 之前的内容
																String re = (startPos>=0 && endPos>startPos) ? absPath.substring(startPos, endPos) : "";
																// 把文件路径分隔符，替换为 “.”
																if(!StrUtil.isEmptyString(re)) re = re.replaceAll(Pattern.quote(File.separator), ".");
																// 返回
																return re;
															})
															// 过滤空字符串
															.filter(className->!StrUtil.isEmptyString(className)) 
															// 因为使用了并行流，所以必须自己收集，然后赋值。直接赋值，会有偶发性异常。
															.collect(Collectors.toList()); 
			
			break;
			
		case "jar": // jar 是压缩包协议 用 JarUrlConnection 对象处理
			
			// 先把 URL 转换为 JarFile 对象(这里不一定能成功)
			JarFile jarFile = null;
			try {
				jarFile = ((JarURLConnection)resourceURL.openConnection()).getJarFile();
			} catch (Exception e) {
				Stdout.mylogger.error(Stdout.fplToAnyWhere("An exception occurred while converting a URL object to a JarFile object. URL=%s", resourceURL));
			}
			
			// JarFile 如果是 null，并不影响搜索函数，它会返回一个 null
			// 另外，Jar 处理时会返回全部路径。所以增加 resourcePath 前缀匹配
			// 根据正则匹配的情况 可能得到 com/microsoft/sqlserver/jdbc/AppDTVImpl$SetValueOp.class 这样的字符串。
			List<String> jarFiles = FileUtil.searchFilesFromJar(jarFile, true, Pattern.quote(resourcePath)+".+\\.class");
			
			// 不为空列表，才开启流处理。
			if(!ListUtil.isEmptyList(jarFiles)) result = jarFiles.parallelStream() // 这里是并行流
														.filter(pathStr->!StrUtil.isEmptyString(pathStr)) // 过滤：不为空字符串
														.map(pathStr->{
															// 把 .class 后缀去掉，并把 / 替换为 “.” 符号 
															// 因为这里是 .class 后缀的文件名列表，所以 lastIndexOf 一定有值。
															return pathStr.substring(0, pathStr.lastIndexOf(".")).replaceAll("\\/", ".");
														})
														// 因为使用了并行流，所以必须自己收集，然后赋值。直接赋值，会有偶发性异常。
														.collect(Collectors.toList());
			break;
			
		default:
			// 对于其它协议，暂时无法处理，所以仅在日志里面提示
			Stdout.fpl("Encountered a URL that cannot be processed, please check. URL=%s", resourceURL);
			break;
			
		}
		
		// 返回
		return result ;
	}
	
}