/******************************************************************************************************

This file "TestAnnoScanMain1.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.maintests;

import java.util.List;

import net.mickarea.tools.utils.ClassUtil;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * 这里是一个 main 函数测试程序。它用于验证一些功能是否可行，并非自动化的单元测试.
 * 要测试的内容：根据所属的 包路径 和 注解类，搜索在这个包下面的类中，有哪些类被这个注解标记，并返回这些类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年9月28日
 */
public class TestAnnoScanMain1 {

	public static void main(String[] args) throws Exception {
		
		// 这样可以出现 file 协议 和 jar 协议。方便测试
		List<String> clsNames = ClassUtil.searchClassNamesFromPackages("com.microsoft.sqlserver.jdbc", "net.mickarea.tools.utils");
		// 输出
		if(!ListUtil.isEmptyList(clsNames)) clsNames.stream().sorted().forEach(Stdout::pl);
		
		List<String> clsNames2 = ClassUtil.searchClassNamesFromPackages("org");
		// 输出
		if(!ListUtil.isEmptyList(clsNames2)) clsNames2.stream().sorted().forEach(Stdout::pl);
		
		List<String> clsNames3 = ClassUtil.searchClassNamesFromPackages("a.b.c");
		Stdout.pl(clsNames3);
	}
	
}
