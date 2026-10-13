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
 * 将 java 对象转换为 Oracle 数据库可以识别的 sql 语句字符串
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月30日
 */
public class OracleValueToString extends ADBValueToString {

	/**
	 * 构造函数，这个转换对象是 关于 Oracle 数据库的
	 */
	public OracleValueToString() {
		super(DBSQLInjectionUtil.DBTYPE_ORACLE);
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
				
			} else if(oriValue instanceof LocalDateTime) {
				tmpValue = " timestamp '" + oriValue.toString().replaceFirst("T", " ") + "'";
				
			} else if(oriValue instanceof LocalDate) {
				tmpValue = " date '" + oriValue.toString() + "'";
				
			} else if(oriValue instanceof LocalTime){
				tmpValue = " date '" + oriValue.toString() + "'";
				
			} else if(oriValue instanceof Timestamp){
				tmpValue = " timestamp '" + oriValue.toString() + "'";
				
			} else if(oriValue instanceof java.util.Date) {
				Timestamp t = new Timestamp(((java.util.Date) oriValue).getTime());
				tmpValue = " timestamp '" + t.toString() + "'";
				
			} else {
				// 其它 非数字类型，非时间类型
				tmpValue = super.valueToString(oriValue);
			}
			
		}
		
		return tmpValue;
	}
}
