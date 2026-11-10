/******************************************************************************************************

This file "MySQLValueToString.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query.abstracts.impls;

import java.math.BigDecimal;
import java.sql.Timestamp;

import net.mickarea.tools.models.query.abstracts.ADBValueToString;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 将 Java 对象转换为 MySQL 数据库可以识别的 sql 语句字符串
 * @author Michael Pang (Dongcan Pang)
 * @since 2024年6月30日
 */
public class MySQLValueToString extends ADBValueToString {

	/**
	 * 构造函数，这个转换对象是 关于 MySQL 数据库的
	 */
	public MySQLValueToString() {
		super(DBSQLInjectionUtil.DBTYPE_MYSQL);
	}
	
	/**
	 * 构造函数，这个转换对象是 关于 MySQL 数据库的。但是，它的数据类型字符串是传递过来的
	 * @param dbType 数据库类型信息。参考 DBSQLInjectionUtil 类的 DBTYPE
	 */
	public MySQLValueToString(String dbType) {
		super(dbType);
	}
	
	/**
	 * 一般来说，只需要处理 toString 的字符串结果 无法被 数据库识别的类型。
	 * 因为，父类中 默认是对 toString 进行处理的。
	 */
	@Override
	public String valueToString(Object oriValue) {
		
		//预先设置一个值
		String tmpValue = "null";
		
		// 如果 传入的值 是 null， 则直接返回 "null" 字符串。
		if(oriValue==null) return tmpValue;
		
		// 如果是数字类型
		if(oriValue instanceof Number) {
			if(oriValue instanceof BigDecimal) {
				if(oriValue.toString().contains(".")) {
					//BigDecimal 类型，并且为小数，则保留10位小数，然后四舍五入
					tmpValue = ((BigDecimal)oriValue).setScale(10, BigDecimal.ROUND_HALF_UP).toString();
				} else {
					//BigDecimal 类型，并且为整数
					tmpValue = ((BigDecimal)oriValue).longValue()+"";
				}
			} else {
				//其它数字，直接输出即可
				tmpValue = oriValue.toString();
			}
			// 返回
			return tmpValue;
		}
		
		// 如果是 java.util.Date 
		if( oriValue instanceof java.util.Date) {
			Timestamp t = new Timestamp(((java.util.Date) oriValue).getTime());
			tmpValue = super.valueToString(t);
			// 返回
			return tmpValue;
		}
		
		// 如果是 布尔值
		if( oriValue instanceof Boolean) {
			// 对于 布尔型，mysql 是 1 为 true ，0 为 false
			tmpValue = ((boolean)oriValue) ? "1" : "0" ;
			// 返回
			return tmpValue;
		}
		
		// 其它默认类型处理
		tmpValue = super.valueToString(oriValue);
		
		// 返回
		return tmpValue;
	}
	
}
