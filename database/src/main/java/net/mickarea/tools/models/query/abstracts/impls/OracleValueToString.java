/******************************************************************************************************

This file "OracleValueToString.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query.abstracts.impls;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import net.mickarea.tools.models.query.abstracts.ADBValueToString;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 将 Java 对象转换为 Oracle 数据库可以识别的 sql 语句字符串
 * @author Michael Pang (Dongcan Pang)
 * @since 2024年6月30日
 */
public class OracleValueToString extends ADBValueToString {

	/**
	 * 构造函数，这个转换对象是 关于 Oracle 数据库的
	 */
	public OracleValueToString() {
		super(DBSQLInjectionUtil.DBTYPE_ORACLE);
	}
	
	/**
	 * 一般来说，只需要处理 toString 的字符串结果 无法被 数据库识别的类型。
	 * 因为，父类中 默认是对 toString 进行处理的。
	 */
	@Override
	public String valueToString(Object oriValue) {
		
		//预先设置一个值
		String tmpValue = "null";
		
		// 如果 oriValue 是 null 则直接返回 
		if(oriValue==null) return tmpValue;
		
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
			
			// 返回
			return tmpValue;
		}
		
		// 时间类型 LocalDateTime
		if(oriValue instanceof LocalDateTime) {
			tmpValue = "timestamp '" + oriValue.toString().replaceFirst("T", " ") + "'";
			// 返回 
			return tmpValue;
		}
		
		// 时间类型 LocalDate 
		if(oriValue instanceof LocalDate) {
			tmpValue = "date '" + oriValue.toString() + "'";
			// 返回 
			return tmpValue;
		}
		
		// 时间类型 LocalTime
		if(oriValue instanceof LocalTime){
			tmpValue = "date '" + oriValue.toString() + "'";
			// 返回 
			return tmpValue;
		}
		
		// 时间类型 Timestamp 
		if(oriValue instanceof Timestamp){
			tmpValue = "timestamp '" + oriValue.toString() + "'";
			// 返回 
			return tmpValue;
		}
		
		// 时间类型 java.util.Date 
		if(oriValue instanceof java.util.Date) {
			Timestamp t = new Timestamp(((java.util.Date) oriValue).getTime());
			tmpValue = "timestamp '" + t.toString() + "'";
			// 返回 
			return tmpValue;
		}
		
		// 如果是 布尔值
		if(oriValue instanceof Boolean) {
			// 对于 布尔型，oracle 是 1 为 true ，0 为 false
			tmpValue = ((boolean)oriValue) ? "1" : "0" ;
			// 返回
			return tmpValue;
		}
		
		// 其它 类型
		tmpValue = super.valueToString(oriValue);
		
		// 返回
		return tmpValue;
	}
	
}
