/******************************************************************************************************

This file "TmpFileAnalyzer.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.filetest;

import net.mickarea.tools.models.TmpDataGramAnalyzer;
import net.mickarea.tools.utils.Stdout;

/**
 * 解析request报文临时文件
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月25日-2023年4月27日
 */
public class TmpFileAnalyzer {

	public static void main(String[] args) throws Exception {
		
		String tmpFileName = "D:\\test_tmpfile\\J08X7LP4QQQI1MGE4V.tmp";
		String targetDir = "C:\\Users\\pangd\\Desktop";
		
		//报文解析工作，由这个类来接管
		TmpDataGramAnalyzer a = new TmpDataGramAnalyzer(tmpFileName, targetDir);
		//开始分析
		String re = a.analyze();
		//打印执行结果
		Stdout.fpl("执行结果：<%s>，文件名：%s", re, a.getRealFileName());
		
	}
	
}
