/******************************************************************************************************

This file "IValueToString.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query.abstracts;

/**
 * 定义一个，关于对象转字符串的处理接口
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月30日
 */
public interface IValueToString {
	
	/**
	 * 把接收到的对象 转换为 字符串内容
	 * @param oriValue 原始对象
	 * @return 字符串结果
	 */
	public String valueToString(Object oriValue);
	
}
