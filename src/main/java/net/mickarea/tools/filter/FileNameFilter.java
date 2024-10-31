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
import java.util.regex.Pattern;

/**
 * &gt;&gt;&nbsp;一个文件名过滤器（以文件名得小写字母匹配）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月15日
 */
public class FileNameFilter implements FilenameFilter {
	
	private String filenameRegexp;
	
	/**
	 * &gt;&gt;&nbsp;构造函数
	 * @param regexp 文件名匹配正则表达式
	 */
	public FileNameFilter(String regexp) {
		this.filenameRegexp = regexp;
	}
	
	@Override
	public boolean accept(File dir, String name) {
		return Pattern.matches(filenameRegexp, name.toLowerCase());
	}
	
}
