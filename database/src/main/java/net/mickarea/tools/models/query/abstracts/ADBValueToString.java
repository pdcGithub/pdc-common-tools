/******************************************************************************************************

This file "AValueToString.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query.abstracts;

import net.mickarea.tools.utils.StrUtil;

/**
 * 定义一个，关于对象转字符串的处理 抽象类（在java对象转换为 sql 语句内容时用）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月30日
 */
public abstract class ADBValueToString implements IValueToString {

	/**
	 * 目标的数据库类型，参考 DBSQLInjectionUtil 的字符串常量
	 */
	private String targetDbType = null;
	
	/**
	 * 获取 目标的数据库类型，参考 DBSQLInjectionUtil 的字符串常量
	 * @return 一个数据类型 的字符串
	 */
	public String getTargetDbType() {
		return targetDbType;
	}
	
	/**
	 * 设置  目标的数据库类型，参考 DBSQLInjectionUtil 的字符串常量
	 * @param targetDbType 一个数据类型 的字符串
	 */
	public void setTargetDbType(String targetDbType) {
		this.targetDbType = targetDbType;
	}

	/**
	 * 构造函数，构建一个 关于对象转字符串的处理 抽象类
	 */
	public ADBValueToString() {
		
	}

	/**
	 * 构造函数，构建一个 关于对象转字符串的处理 抽象类
	 * @param targetDbType 一个数据类型 的字符串，参考 DBSQLInjectionUtil 的字符串常量
	 */
	public ADBValueToString(String targetDbType) {
		super();
		this.targetDbType = targetDbType;
	}
	
	/**
	 * 把接收到的对象 转换为 字符串内容（由于是数据库可以直接读取的字符串。所以，这里添加了防注入处理）
	 * @param oriValue 原始对象
	 * @return 字符串结果
	 */
	@Override
	public String valueToString(Object oriValue) {
		String result = "null";
		if(oriValue!=null) {
			result = oriValue.toString().length()>0?("'"+StrUtil.injectionTranslateForSQL(this.targetDbType, oriValue.toString())+"'"):"";
		}
		return result ;
	}
}
