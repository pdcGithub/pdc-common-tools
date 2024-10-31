/******************************************************************************************************

This file "ProcParam.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import java.sql.Types;

import net.mickarea.tools.utils.Stdout;

/**
 * &gt;&gt;&nbsp;存储过程执行的参数类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月10日-2024年6月28日
 */
public class ProcParam {
	
	public static final String TYPE_IN = "in";
	public static final String TYPE_OUT = "out";
	public static final String TYPE_INOUT = "inout";
	
	private String paramType; //参数类型：输入、输出、输入并且输出
	private String paramName; //参数名：与存储过程保持一致
	private Object paramValue;//参数值
	
	/**
	 * 存储过程，输出参数的数据类型（默认是字符串）
	 */
	private int outParamSqlType = Types.VARCHAR;
	
	/**
	 * &gt;&gt;&nbsp;构造
	 */
	public ProcParam() {
	}
	public ProcParam(String type, Object value) {
		this.paramType=type;
		this.paramValue=value;
	}
	public ProcParam(String type, String name, Object value) {
		this.paramType = type;
		this.paramName = name;
		this.paramValue= value;
	}
	
	// getter and setter
	public String getParamType() {
		return paramType;
	}
	public ProcParam setParamType(String paramType) {
		this.paramType = paramType;
		return this;
	}
	public String getParamName() {
		return paramName;
	}
	public ProcParam setParamName(String paramName) {
		this.paramName = paramName;
		return this;
	}
	public Object getParamValue() {
		return paramValue;
	}
	public ProcParam setParamValue(Object paramValue) {
		this.paramValue = paramValue;
		return this;
	}
	public int getOutParamSqlType() {
		return outParamSqlType;
	}
	public ProcParam setOutParamSqlType(int outParamSqlType) {
		this.outParamSqlType = outParamSqlType;
		return this;
	}
	
	@Override
	public String toString() {
		return Stdout.fplToAnyWhere("参数类型：%s，参数名：%s，参数值：%s, %s", 
				this.paramType, 
				this.paramName, 
				this.paramValue,
				(TYPE_OUT.equalsIgnoreCase(this.getParamType())?"输出参数映射："+this.getOutParamSqlType():"")
		);
	}

	/**
	 * &gt;&gt;&nbsp;测试
	 * @param args
	 */
	/*
	public static void main(String[] args) {
		
		Stdout.pl(new ProcParam());
		Stdout.pl(new ProcParam(ProcParam.TYPE_IN, new Integer(123)));
		Stdout.pl(new ProcParam(ProcParam.TYPE_OUT, "测试xxxx"));
		Stdout.pl(new ProcParam(ProcParam.TYPE_OUT, "yyy", "value"));
		
		Stdout.pl(new ProcParam().setParamType(TYPE_INOUT).setParamName(null).setParamValue(new Float(123.256)));
		Stdout.pl(new ProcParam().setParamType(TYPE_INOUT).setParamName("table_name").setParamValue(new Float(123.256)));
	}
	*/
}
