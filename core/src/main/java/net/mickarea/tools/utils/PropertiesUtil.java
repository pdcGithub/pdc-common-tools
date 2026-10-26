/******************************************************************************************************

This file "PropertiesUtil.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.mickarea.tools.filter.FileNameFilter;

/**
 * 这个是一个 properties 配置文件读取的工具类。在实现上依赖 ResourceBundle 类。它对于每一个配置文件，在第一次读取时，都会自动缓存。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年10月29日-2025年10月30日
 */
public final class PropertiesUtil {

	private PropertiesUtil() {
		// 私有构造函数，防止外部创建
	}
	
	/**
	 * 这里是从资源池中，找到对应的资源配置，然后返回对应 键信息 的一个值。（读取不到会打印异常日志，但是不抛异常，返回空字符串）
	 * @param baseName 配置文件基础路径。举例：如果层次结构是 resources/i18n/messages_en.properties，则 baseName 是 i18n.messages。
	 * @param keyName keyName 配置文件中的一个键信息
	 * @param loc 所在的地域信息，比如 en, zh_CN 等等
	 * @return 如果 配置名 和 键 当中有一个为空，则返回空字符串。如果 信息正常，但是找不到对应信息，会打印异常，并返回空字符串。
	 */
	public static final String getMessage(String baseName, String keyName, Locale loc) {
		
		// 参数校验
		if(StrUtil.isEmptyString(baseName) || StrUtil.isEmptyString(keyName)) return "";
		
		// 开始调用
		try {
			if(loc == null) {
				// 普通读取
				return ResourceBundle.getBundle(baseName).getString(keyName);
			}else {
				// 按地区读取
				return ResourceBundle.getBundle(baseName, loc).getString(keyName);
			}
		}catch(Exception e) {
			Stdout.fpl("something wrong when getting message. name: %s, key: %s, loc: %s", baseName, keyName, loc);
			Stdout.pl(e);
			// 如果抛异常，则返回空字符串。
			return "";
		}
	}
	
	/**
	 * 这里是从资源池中，找到对应的资源配置，然后返回对应 键信息 的一个值。（读取不到会打印异常日志，但是不抛异常，返回空字符串）
	 * @param baseName 配置文件基础路径。举例：如果层次结构是 resources/i18n/messages_en.properties，则 baseName 是 i18n.messages。
	 * @param keyName keyName 配置文件中的一个键信息
	 * @return 如果 配置名 和 键 当中有一个为空，则返回空字符串。如果 信息正常，但是找不到对应信息，会打印异常，并返回空字符串。
	 */
	public static final String getMessage(String baseName, String keyName) {
		// 调用上一个实现，这里不用重复写
		return getMessage(baseName, keyName, null);
	}
	
	/**
	 * 这里可以根据传入的资源文件夹路径，搜索资源池中的 properties 文件，并且返回文件的资源路径。
	 * 这里的资源定位主要依赖 classpath 的指定。
	 * @param folder 要搜索的资源文件夹路径。资源路径以 classpath 为准。文件夹最少要有一个英文字母或者数字，否则会从 classpath 根路径搜索。
	 * 如果程序有 resources 配置，则 resources 下的 conf 文件夹为 conf/ 
	 * @param nameRegexp 这是用于匹配 properties 文件名的正则表达式。如果要匹配 demo1.properties 可以这么写："demo\\d+"。文件后缀，不需要写到正则。
	 * 如果要匹配所有的 properties 文件，传入null 。
	 * @return properties 文件的资源路径列表，比如：[conf/database-config/unittest7_jar2.properties]。注意，这里返回的结果已经 做了去重 和 非空处理
	 */
	public static List<String> searchPropertiesFiles(String folder, String nameRegexp){
		
		// 首先定义一个返回结果
		List<String> result = null;
		
		// 处理下 搜索路径。默认是空字符串。
		String searchPath = "";

		// 确定文件名正则表达式
		String searchNameRegexp = nameRegexp==null ? ".+" : nameRegexp;
		searchNameRegexp = searchNameRegexp + "\\.properties";
		
		// 如果传入的路径正常，至少有一个英文字母或者数字，则重新赋值
		if(folder!=null && PatternUtil.matches(".*[a-z0-9]+.*", folder, Pattern.CASE_INSENSITIVE)) {
			searchPath = folder.trim();
		}
		
		// 定义一个
		List<URL> urls = null;
		try {
			// 开始搜索
			Stdout.mylogger.debug(Stdout.fplToAnyWhere("Starting properties resource search now, searchPath=%s", searchPath));
			// 如果搜索时没有 / 后缀，则加上去，方便后续区分 路径
			if(searchPath.length()>0 && !searchPath.endsWith("/")) searchPath = searchPath + "/";
			// 为了避免有多个斜杠，做一个处理
			if(PatternUtil.matches(".*[/]{2,}.*", searchPath, Pattern.CASE_INSENSITIVE)) searchPath = searchPath.replaceAll("[/]{2,}", "/");
			// 资源结果会以 URL 的枚举集合出返回，并转为 列表对象
			urls = ListUtil.makeEnumerationObjectToListObject(Thread.currentThread().getContextClassLoader().getResources(searchPath));
			
		} catch (Exception e) {
			// 如果 资源获取出错，提示一下就行了。
			Stdout.mylogger.debug(Stdout.fplToAnyWhere("An exception occurred while loading resources, searchPath=%s", searchPath));
			Stdout.pl(e);
		}
		
		// 如果没有结果，则返回
		if(ListUtil.isEmptyList(urls)) return result;
		
		// 定义一个最终的搜索字符串 给 lambda 表达式
		final String finalSearchPath = searchPath;
		
		// 定义一个最终的文件名正则
		final String filenameRegexp = searchNameRegexp;
		
		// 有结果，则并发多个线程进行转化
		result = urls.parallelStream()
					// 只留下 jar 和 file 协议 的 URL
					.filter(url -> url!=null && ("jar".equalsIgnoreCase(url.getProtocol()) || "file".equalsIgnoreCase(url.getProtocol())))
					.map(url->{
						// 定义一个返回结果
						List<String> re = null;
						// 因为文件搜索处理，可能有异常抛出，所以需要 try ... catch 处理
						try {
							Stdout.mylogger.debug(Stdout.fplToAnyWhere("Search the following URL. url=%s", url));
							switch(url.getProtocol().toLowerCase()) {
							case "jar":
								// 把 url 转换为 jar file
								JarFile jarFile = ((JarURLConnection)url.openConnection()).getJarFile();
								// 对于 jar 形式的文件夹，直接匹配就行了（正则需要完整的匹配，从文件夹开始）
								re = FileUtil.searchFilesFromJar(jarFile, true, Pattern.quote(finalSearchPath)+"(.+/)?"+filenameRegexp);
								break;
							case "file":
								// 先确定文件夹的根路径是哪里
								String rootPath = url.getPath();
								// 如果是windows ，替换 / 为 本地文件系统的 分隔符
								if(File.separator.equals("\\")) rootPath = rootPath.replaceAll("/", Matcher.quoteReplacement(File.separator)).substring(1);
								// 如果文件夹后面没有 / 结尾，增加一个
								if(rootPath.length()>=1 && !rootPath.endsWith(File.separator)) rootPath = rootPath + File.separator;
								// 对于 file 协议的文件夹，需要递归搜索本地路径，并且匹配
								re = FileUtil.searchFilesFromLocalDir(new File(rootPath), true, new FileNameFilter(filenameRegexp));
								// 如果 re 不为空，则 把根路径信息去掉，并且替换 文件路径分隔符为 / 
								if(!ListUtil.isEmptyList(re)) {
									// 在使用前，需要把搜索文件夹 finalSearchpath 从 rootPath 去掉（由于windows会替换分隔符，这里也要再处理）
									String tmpPath = File.separator.equals("\\") 
											? finalSearchPath.replaceAll("/", Matcher.quoteReplacement(File.separator)) : finalSearchPath;
									// 截取 搜索路径前面的 classpath 根路径
									if(tmpPath.length()>0) rootPath = rootPath.substring(0, rootPath.lastIndexOf(tmpPath));
									for(int i=0; i<re.size();i++) {
										String fnPath = re.get(i);
										// 把本地的绝对路径中的根路径去掉
										fnPath = fnPath.replaceFirst(Pattern.quote(rootPath), "");
										// 如果是 windows 还需要替换 文件路径分隔符
										if(File.separator.equals("\\")) fnPath = fnPath.replaceAll(Pattern.quote(File.separator), "/");
										// 修改 list 里面的信息
										re.set(i, fnPath);
									}
								}
								break;
							default:
								// 这里不应该进入。进入这里说明代码条件判断有问题
								Stdout.mylogger.error(Stdout.fplToAnyWhere("An exception occurred while executing the switch. condition=%s", url.getProtocol()));
								break;
							}
						}catch(Exception e) {
							Stdout.mylogger.debug(Stdout.fplToAnyWhere("An exception occurred while performing URL traversal. url=%s", url));
							Stdout.pl(e);
						}
						
						// 返回 list 给 stream 流
						return re;
					})
					// 过滤掉 null 的结果，避免 空指针
					.filter(listRe->listRe!=null)
					// 把 list 结果 转换为 stream，让多个list 自动合并为一个 list
					.flatMap(List::stream)
					// 再过滤掉 空的字符串。
					.filter(str->!StrUtil.isEmptyString(str))
					// 去除重复的结果
					.distinct()
					// 收集结果
					.collect(Collectors.toList());
		
		// 返回
		return result;
	}
	
}
