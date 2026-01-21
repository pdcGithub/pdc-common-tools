/******************************************************************************************************

This file "CodeLocationConverterConfig.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.logback.converters;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import net.mickarea.tools.utils.Stdout;

/**
 * logback 的日志输出参数转换器。用于获取调用日志打印的代码，具体位置信息。它的信息格式如下, 类名::方法名::行号。
 * 有一点要注意，它的处理可能会消耗一部分的执行时间。建议在调试时使用。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年1月21日
 */
public class CodeLocationConverterConfig extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		
		String re = "code loc err";
		
		// 获取当前线程的调用栈
		try {
			StackTraceElement[] stackElems = Thread.currentThread().getStackTrace();
			
			if(stackElems==null || stackElems.length<=0) return re;
			// 获取栈内，最后一个元素。因为，代码栈是先进后出的，所以具体的调用点肯定在最后一个元素
			int targetNum = stackElems.length-1;
			re = Stdout.fplToAnyWhere("%s::%s::%s", 
					stackElems[targetNum].getClassName(),    // 类名
					stackElems[targetNum].getMethodName(),   // 方法名
					stackElems[targetNum].getLineNumber());  // 行号
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return re;
	}

}
