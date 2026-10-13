/******************************************************************************************************

This file "FileNameFilter.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.filter;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mickarea.tools.utils.Stdout;

/**
 * 一个文件名过滤器。它一般是用在 File 对象的 listFiles 方法上，进行文件名过滤.
 * 在使用的时候，主要控制2个内容：文件名正则表达式 和 匹配时是否区分大小写。
 * 正则表达式需要参数传入，而默认状态下，是忽略大小写，然后进行匹配的。并且，匹配时包含文件夹，方便递归搜索
 * @author Michael Pang (Dongcan Pang)
 * @since 2023年5月15日
 */
public class FileNameFilter implements FilenameFilter {
	
	/**
	 * 这是搜索文件名时，使用的正则表达式字符串。
	 */
	private String filenameRegexp;
	
	/**
	 * 这是匹配文件名时，是否区分大小写。默认是区分的。
	 */
	private boolean ignoreCase;
	
	/**
	 * 在执行 list 的时候，是否包含文件夹。如果是递归搜索，则需要；不是递归搜索，则不需要。
	 * 默认是支持递归搜索，即包含文件夹
	 */
	private boolean includeDirs ;
	
	/**
	 * 从对象中，提取 搜索文件名时，使用的正则表达式字符串
	 * @return 正则表达式字符串
	 */
	public String getFilenameRegexp() {
		return filenameRegexp;
	}

	/**
	 * 向对象中，设置 搜索文件名时，使用的正则表达式字符串
	 * @param filenameRegexp 正则表达式字符串
	 */
	public void setFilenameRegexp(String filenameRegexp) {
		this.filenameRegexp = filenameRegexp;
	}

	/**
	 * 从对象中，提取 是否区分大小写的状态值
	 * @return 是否区分大小写的状态值
	 */
	public boolean getIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * 向对象中，设置 是否区分大小写的状态值
	 * @param ignoreCase 是否区分大小写的状态值
	 */
	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}
	
	/**
	 * 从对象中，提取 是否包含文件夹的状态值
	 * @return 是否包含文件夹的状态值
	 */
	public boolean getIncludeDirs() {
		return includeDirs;
	}

	/**
	 * 向对象中，设置 是否包含文件夹的状态值
	 * @param includeDirs 是否包含文件夹的状态值
	 */
	public void setIncludeDirs(boolean includeDirs) {
		this.includeDirs = includeDirs;
	}

	/**
	 * 这是一个空参，空处理的构造函数。使用时，请务必调用 setter 方法把属性补全
	 * ignoreCase 的值 默认是 true。
	 * includeDirs 的值 默认是 true。
	 */
	public FileNameFilter() {
		this.ignoreCase = true;
		this.includeDirs = true;
	}

	/**
	 * 构造函数。它构建一个文件名过滤器。它一般是用在 File 对象的 listFiles 方法上，进行文件名过滤.
	 * 这个构造函数中，ignoreCase 的值 默认是 true。includeDirs 的值 默认是 true。
	 * @param filenameRegexp 用于匹配文件名信息的正则表达式字符串
	 */
	public FileNameFilter(String filenameRegexp) {
		this.filenameRegexp = filenameRegexp;
		this.ignoreCase = true;
		this.includeDirs = true;
	}
	
	/**
	 * 构造函数。它构建一个文件名过滤器。它一般是用在 File 对象的 listFiles 方法上，进行文件名过滤.
	 * @param filenameRegexp 用于匹配文件名信息的正则表达式字符串
	 * @param ignoreCase 匹配时，是否忽略字符大小写。
	 * @param includeDirs 匹配时，是否包含文件夹（如果搜索时需要递归，则设置为true；否则，设置 false）
	 */
	public FileNameFilter(String filenameRegexp, boolean ignoreCase, boolean includeDirs) {
		this.filenameRegexp = filenameRegexp;
		this.ignoreCase = ignoreCase;
		this.includeDirs = includeDirs;
	}

	/**
	 * 这是文件名过滤接口中，唯一需要自己实现的方法。它用于执行过滤判断
	 */
	@Override
	public boolean accept(File dir, String name) {
		
		// 定一下结果。默认是不适配。只有通过正则校验，才可能返回 true 。
		boolean result = false;
		
		// 这里打印一下 内部的信息
		// Stdout.mylogger.debug(Stdout.fplToAnyWhere("dir=%s, name=%s", dir, name));
		
		// 如果 需要包含文件夹 并且 这路径是 文件夹
		// 则返回 true。
		// 在判断时，要整合 dir 和 name ，因为 name 只是 文件或者文件夹的名字。要加上 dir 才是完整路径。这样判断才不会出错
		if(this.includeDirs && Files.isDirectory(Paths.get(dir + File.separator + name))) {
			result = true;
			return result;
		}
		
		// 因为无法预期 name 参数，filenameRegexp 参数，ignoreCase 参数。
		// 反正报错就记录，并设置 false。
		try {
			// 构建一个正则对象
			Pattern regexp = this.ignoreCase ? Pattern.compile(this.filenameRegexp, Pattern.CASE_INSENSITIVE) : Pattern.compile(this.filenameRegexp);
			// matcher 设置要匹配的字符串
			Matcher matcher = regexp.matcher(name);
			// 获得结果
			result = matcher.matches();
			
		} catch (Exception e) {
			// 记录错误日志，写入到 debug 中
			Stdout.mylogger.debug(
				Stdout.fplToAnyWhere("Filename filter encountered an exception. dir=%s, filename=%s, regexp=%s, ignoreCase=%s", 
					dir, name, this.filenameRegexp, this.ignoreCase
				)
			);
			
		}
		
		// 返回结果
		return result;
	}
	
}
