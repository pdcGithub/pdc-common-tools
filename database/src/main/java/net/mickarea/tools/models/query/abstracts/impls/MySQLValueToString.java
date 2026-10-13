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
 * 将 java 对象转换为 MySQL 数据库可以识别的 sql 语句字符串
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月30日-2025年1月20日
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
	 * @param dbType 数据库类型信息
	 */
	public MySQLValueToString(String dbType) {
		super(dbType);
	}
	
	/* (non-Javadoc)
	 * @see net.mickarea.tools.models.query.abstracts.ADBValueToString#valueToString(java.lang.Object)
	 */
	@Override
	public String valueToString(Object oriValue) {
		
		//预先设置一个值
		String tmpValue = "null";
		
		if(oriValue!=null) {
			
			//数字类型
			if(oriValue instanceof Number) {
				
				if(oriValue instanceof BigDecimal) {
					if(oriValue.toString().contains(".")) {
						//BigDecimal 类型，并且为小数，则保留10位小数，然后四舍五入
						tmpValue = ((BigDecimal)oriValue).setScale(10, BigDecimal.ROUND_HALF_UP).toString();
					}else {
						//BigDecimal 类型，并且为整数
						tmpValue = ((BigDecimal)oriValue).longValue()+"";
					}
				}else {
					//其它数字，直接输出即可
					tmpValue = oriValue.toString();
				}
				
			} else if( oriValue instanceof java.util.Date) {
				Timestamp t = new Timestamp(((java.util.Date) oriValue).getTime());
				tmpValue = super.valueToString(t);
				
			} else {
				// 其它 非数字类型，非特殊时间类型
				tmpValue = super.valueToString(oriValue);
			}
			
		}
		
		return tmpValue;
	}
	
}
