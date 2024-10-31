/******************************************************************************************************

This file "SystemUtil.java" is part of project "niceday" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

/**
 * 系统参数获取工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2022年11月11日-2023年10月21日
 */
public final class SystemUtil {
	
	/**
	 * 私有构造函数
	 */
	private SystemUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/** 一个系统参数的获取方法，当前键值为：awt.toolkit */
	public static String getAwtToolkit() {
		return System.getProperty("awt.toolkit");
	}

	/** 一个系统参数的获取方法，当前键值为：file.encoding */
	public static String getFileEncoding() {
		return System.getProperty("file.encoding");
	}

	/** 一个系统参数的获取方法，当前键值为：file.encoding.pkg */
	public static String getFileEncodingPkg() {
		return System.getProperty("file.encoding.pkg");
	}

	/** 一个系统参数的获取方法，当前键值为：file.separator */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	/** 一个系统参数的获取方法，当前键值为：java.awt.graphicsenv */
	public static String getJavaAwtGraphicsenv() {
		return System.getProperty("java.awt.graphicsenv");
	}

	/** 一个系统参数的获取方法，当前键值为：java.awt.printerjob */
	public static String getJavaAwtPrinterjob() {
		return System.getProperty("java.awt.printerjob");
	}

	/** 一个系统参数的获取方法，当前键值为：java.class.path */
	public static String getJavaClassPath() {
		return System.getProperty("java.class.path");
	}

	/** 一个系统参数的获取方法，当前键值为：java.class.version */
	public static String getJavaClassVersion() {
		return System.getProperty("java.class.version");
	}

	/** 一个系统参数的获取方法，当前键值为：java.endorsed.dirs */
	public static String getJavaEndorsedDirs() {
		return System.getProperty("java.endorsed.dirs");
	}

	/** 一个系统参数的获取方法，当前键值为：java.ext.dirs */
	public static String getJavaExtDirs() {
		return System.getProperty("java.ext.dirs");
	}

	/** 一个系统参数的获取方法，当前键值为：java.home */
	public static String getJavaHome() {
		return System.getProperty("java.home");
	}

	/** 一个系统参数的获取方法，当前键值为：java.io.tmpdir */
	public static String getJavaIoTmpdir() {
		return System.getProperty("java.io.tmpdir");
	}

	/** 一个系统参数的获取方法，当前键值为：java.library.path */
	public static String getJavaLibraryPath() {
		return System.getProperty("java.library.path");
	}

	/** 一个系统参数的获取方法，当前键值为：java.runtime.name */
	public static String getJavaRuntimeName() {
		return System.getProperty("java.runtime.name");
	}

	/** 一个系统参数的获取方法，当前键值为：java.runtime.version */
	public static String getJavaRuntimeVersion() {
		return System.getProperty("java.runtime.version");
	}

	/** 一个系统参数的获取方法，当前键值为：java.specification.name */
	public static String getJavaSpecificationName() {
		return System.getProperty("java.specification.name");
	}

	/** 一个系统参数的获取方法，当前键值为：java.specification.vendor */
	public static String getJavaSpecificationVendor() {
		return System.getProperty("java.specification.vendor");
	}

	/** 一个系统参数的获取方法，当前键值为：java.specification.version */
	public static String getJavaSpecificationVersion() {
		return System.getProperty("java.specification.version");
	}

	/** 一个系统参数的获取方法，当前键值为：java.vendor */
	public static String getJavaVendor() {
		return System.getProperty("java.vendor");
	}

	/** 一个系统参数的获取方法，当前键值为：java.vendor.url */
	public static String getJavaVendorUrl() {
		return System.getProperty("java.vendor.url");
	}

	/** 一个系统参数的获取方法，当前键值为：java.vendor.url.bug */
	public static String getJavaVendorUrlBug() {
		return System.getProperty("java.vendor.url.bug");
	}

	/** 一个系统参数的获取方法，当前键值为：java.version */
	public static String getJavaVersion() {
		return System.getProperty("java.version");
	}

	/** 一个系统参数的获取方法，当前键值为：java.vm.info */
	public static String getJavaVmInfo() {
		return System.getProperty("java.vm.info");
	}

	/** 一个系统参数的获取方法，当前键值为：java.vm.name */
	public static String getJavaVmName() {
		return System.getProperty("java.vm.name");
	}

	/** 一个系统参数的获取方法，当前键值为：java.vm.specification.name */
	public static String getJavaVmSpecificationName() {
		return System.getProperty("java.vm.specification.name");
	}

	/** 一个系统参数的获取方法，当前键值为：java.vm.specification.vendor */
	public static String getJavaVmSpecificationVendor() {
		return System.getProperty("java.vm.specification.vendor");
	}

	/** 一个系统参数的获取方法，当前键值为：java.vm.specification.version */
	public static String getJavaVmSpecificationVersion() {
		return System.getProperty("java.vm.specification.version");
	}

	/** 一个系统参数的获取方法，当前键值为：java.vm.vendor */
	public static String getJavaVmVendor() {
		return System.getProperty("java.vm.vendor");
	}

	/** 一个系统参数的获取方法，当前键值为：java.vm.version */
	public static String getJavaVmVersion() {
		return System.getProperty("java.vm.version");
	}

	/** 一个系统参数的获取方法，当前键值为：line.separator */
	public static String getLineSeparator() {
		return System.getProperty("line.separator");
	}

	/** 一个系统参数的获取方法，当前键值为：os.arch */
	public static String getOsArch() {
		return System.getProperty("os.arch");
	}

	/** 一个系统参数的获取方法，当前键值为：os.name */
	public static String getOsName() {
		return System.getProperty("os.name");
	}

	/** 一个系统参数的获取方法，当前键值为：os.version */
	public static String getOsVersion() {
		return System.getProperty("os.version");
	}

	/** 一个系统参数的获取方法，当前键值为：path.separator */
	public static String getPathSeparator() {
		return System.getProperty("path.separator");
	}

	/** 一个系统参数的获取方法，当前键值为：sun.arch.data.model */
	public static String getSunArchDataModel() {
		return System.getProperty("sun.arch.data.model");
	}

	/** 一个系统参数的获取方法，当前键值为：sun.boot.class.path */
	public static String getSunBootClassPath() {
		return System.getProperty("sun.boot.class.path");
	}

	/** 一个系统参数的获取方法，当前键值为：sun.boot.library.path */
	public static String getSunBootLibraryPath() {
		return System.getProperty("sun.boot.library.path");
	}

	/** 一个系统参数的获取方法，当前键值为：sun.cpu.endian */
	public static String getSunCpuEndian() {
		return System.getProperty("sun.cpu.endian");
	}

	/** 一个系统参数的获取方法，当前键值为：sun.cpu.isalist */
	public static String getSunCpuIsalist() {
		return System.getProperty("sun.cpu.isalist");
	}

	/** 一个系统参数的获取方法，当前键值为：sun.desktop */
	public static String getSunDesktop() {
		return System.getProperty("sun.desktop");
	}

	/** 一个系统参数的获取方法，当前键值为：sun.io.unicode.encoding */
	public static String getSunIoUnicodeEncoding() {
		return System.getProperty("sun.io.unicode.encoding");
	}

	/** 一个系统参数的获取方法，当前键值为：sun.java.command */
	public static String getSunJavaCommand() {
		return System.getProperty("sun.java.command");
	}

	/** 一个系统参数的获取方法，当前键值为：sun.java.launcher */
	public static String getSunJavaLauncher() {
		return System.getProperty("sun.java.launcher");
	}

	/** 一个系统参数的获取方法，当前键值为：sun.jnu.encoding */
	public static String getSunJnuEncoding() {
		return System.getProperty("sun.jnu.encoding");
	}

	/** 一个系统参数的获取方法，当前键值为：sun.management.compiler */
	public static String getSunManagementCompiler() {
		return System.getProperty("sun.management.compiler");
	}

	/** 一个系统参数的获取方法，当前键值为：sun.os.patch.level */
	public static String getSunOsPatchLevel() {
		return System.getProperty("sun.os.patch.level");
	}

	/** 一个系统参数的获取方法，当前键值为：user.country */
	public static String getUserCountry() {
		return System.getProperty("user.country");
	}

	/** 一个系统参数的获取方法，当前键值为：user.dir */
	public static String getUserDir() {
		return System.getProperty("user.dir");
	}

	/** 一个系统参数的获取方法，当前键值为：user.home */
	public static String getUserHome() {
		return System.getProperty("user.home");
	}

	/** 一个系统参数的获取方法，当前键值为：user.language */
	public static String getUserLanguage() {
		return System.getProperty("user.language");
	}

	/** 一个系统参数的获取方法，当前键值为：user.name */
	public static String getUserName() {
		return System.getProperty("user.name");
	}

	/** 一个系统参数的获取方法，当前键值为：user.script */
	public static String getUserScript() {
		return System.getProperty("user.script");
	}

	/** 一个系统参数的获取方法，当前键值为：user.timezone */
	public static String getUserTimezone() {
		return System.getProperty("user.timezone");
	}

	/** 一个系统参数的获取方法，当前键值为：user.variant */
	public static String getUserVariant() {
		return System.getProperty("user.variant");
	}

}
