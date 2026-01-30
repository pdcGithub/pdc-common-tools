/******************************************************************************************************

This file "PropertiesUtil.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.util.Locale;
import java.util.ResourceBundle;

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
}
