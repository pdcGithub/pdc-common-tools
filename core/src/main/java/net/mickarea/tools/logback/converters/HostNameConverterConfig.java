/******************************************************************************************************

This file "HostNameConverterConfig.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.logback.converters;

import java.net.Inet4Address;
import java.net.InetAddress;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * logback 的日志输出参数转换器（获取服务主机名称信息）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月21日
 */
public class HostNameConverterConfig extends ClassicConverter {

	/* (non-Javadoc)
	 * @see ch.qos.logback.core.pattern.Converter#convert(java.lang.Object)
	 */
	@Override
	public String convert(ILoggingEvent event) {
		String hostInfo = null;
		try {
			InetAddress addr = Inet4Address.getLocalHost();
			hostInfo = addr.getHostName();
		} catch (Exception e) {
			hostInfo = "解析host信息出错";
		}
		return hostInfo;
	}

}
