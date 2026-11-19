/******************************************************************************************************

This file "ProcParam.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import java.sql.JDBCType;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 这是 存储过程执行用的参数对象类
 * @author Michael Pang (Dongcan Pang)
 * @since 2023年6月10日
 */
public class ProcParam {
	
	/**
	 * 这是 存储过程参数对象 的传送类型 常量：输入。
	 */
	public static final String TYPE_IN = "in";
	
	/**
	 * 这是 存储过程参数对象 的传送类型 常量：输出。
	 */
	public static final String TYPE_OUT = "out";
	
	/**
	 * 这是 存储过程参数对象 的传送类型 常量：输入并且输出。
	 */
	public static final String TYPE_INOUT = "inout";
	
	/**
	 * 这是 存储过程参数对象 的传送类型列表 常量。主要用户判断 传送类型是否填写正确。
	 */
	public static final List<String> TYPE_LIST = Arrays.asList(TYPE_IN, TYPE_OUT, TYPE_INOUT);
	
	/**
	 * 这是 存储过程参数对象 的传送类型。有多个可能性，包含在 TYPE_LIST 中。（默认是 IN ，表示输入参数）
	 */
	private String paramType = TYPE_IN;
	
	/**
	 * 这是 存储过程参数对象 的 参数名。这个名称的设置，只是为了便于参数对象查找。建议与存储过程的定义保持一致。
	 */
	private String paramName;
	
	/**
	 * 这是 存储过程参数对象 的 参数值。建议与存储过程的定义保持一致。
	 * 如果是输出的参数值，还需要配合 outParamSqlType 才能获取到值。
	 */
	private Object paramValue;
	
	/**
	 * 这是 存储过程参数对象 的 输出参数的数据类型（默认是字符串 varchar）。
	 * 当参数的传送类型是 输出、或者 输入并输出 时，用于接收数据库返回的数据。如果这里的设置 和 数据库定义不一样，则无法接收返回的数据库结果。
	 */
	private int outParamSqlType = Types.VARCHAR;
	
	// ======================================= getter and setter
	
	/**
	 * 获取 存储过程参数对象 的传送类型
	 * @return 存储过程参数对象 的传送类型
	 */
	public String getParamType() {
		return this.paramType;
	}
	
	/**
	 * 设置 存储过程参数对象 的传送类型。对于传送类型，有一个合规性校验。需要在 TYPE_LIST 中的类型信息，才能设置。
	 * @param paramType 存储过程参数对象 的传送类型。
	 * @return 本对象的引用
	 */
	public ProcParam setParamType(String paramType) {
		// 先去掉所有空白字符 或者 null
		String tmp = StrUtil.removeAllBlankStrings(paramType); 
		// 如果有值，且符合规范，则设置
		if(!StrUtil.isEmptyString(tmp) && TYPE_LIST.contains(tmp)) this.paramType = tmp;
		// 返回 本对象的引用
		return this;
	}
	
	/**
	 * 获取 存储过程参数对象 的 参数名
	 * @return 存储过程参数对象 的 参数名
	 */
	public String getParamName() {
		return this.paramName;
	}
	
	/**
	 * 设置 存储过程参数对象 的 参数名。这个名称的设置，只是为了便于参数对象查找。建议，建议与存储过程的定义保持一致。
	 * @param paramName 存储过程参数对象 的 参数名。对于参数名，首先不能为空；然后，不能为 null 字符。
	 * @return 本对象的引用
	 */
	public ProcParam setParamName(String paramName) {
		// 先去掉所有空白字符 或者 null
		String tmp = StrUtil.removeAllBlankStrings(paramName); 
		// 如果有值，且符合规范，则设置 
		if(!StrUtil.isEmptyString(tmp)) this.paramName = tmp;
		// 返回 本对象的引用
		return this;
	}
	
	/**
	 * 获取 存储过程参数对象 的 参数值。
	 * @return 存储过程参数对象 的 参数值。
	 */
	public Object getParamValue() {
		return this.paramValue;
	}
	
	/**
	 * 设置 存储过程参数对象 的 参数值。（对于值，一般都是 Object 类型，没有参数限制）
	 * @param paramValue 存储过程参数对象 的 参数值。
	 * @return 本对象的引用
	 */
	public ProcParam setParamValue(Object paramValue) {
		// 直接赋值
		this.paramValue = paramValue;
		// 返回 本对象的引用
		return this;
	}
	
	/**
	 * 获取 存储过程参数对象 的 输出参数的数据类型（默认是字符串 varchar）
	 * @return 存储过程参数对象 的 输出参数的数据类型
	 */
	public int getOutParamSqlType() {
		return this.outParamSqlType;
	}
	
	/**
	 * 设置 存储过程参数对象 的 输出参数的数据类型（默认是字符串 varchar）
	 * @param outParamSqlType 输出参数的数据类型。它的值是 java.sql.Types 中定义的一个整数。
	 * @return 本对象的引用
	 */
	public ProcParam setOutParamSqlType(int outParamSqlType) {
		
		// 这里需要对 type 的整数值进行一个校验，符合才赋值。
		// 在 java 8 有一个基于 java.sql.Types 的 新的枚举类 JDBCType。可用于测试 int值是否匹配。
		
		boolean intTypeOk = false;
		try {
			// 枚举测试
			JDBCType.valueOf(outParamSqlType);
			// 不报错就表示通过
			intTypeOk = true;
			
		} catch (Exception e) {
			Stdout.mylogger.debug(Stdout.fplToAnyWhere("wrong type : %s", outParamSqlType));
		}
		
		// 赋值。如果枚举测试通过的话。
		if(intTypeOk) this.outParamSqlType = outParamSqlType;
		
		// 返回 本对象的引用
		return this;
	}
	
	// =========================== 
	
	/**
	 * 这是无参构造函数，用于构建 存储过程参数对象。值的设置交给 setter 方法
	 */
	public ProcParam() {
		// 使用 其它构造函数，统一初始化
		this(null, null, null);
	}
	
	/**
	 * 这是有参的构造函数，用于构建 存储过程参数对象。
	 * @param pType 存储过程参数对象 的传送类型。有多个可能性，包含在 TYPE_LIST 中。
	 * @param pValue 存储过程参数对象 的 参数值。如果是输出的参数值，还需要配合 outParamSqlType 才能获取到值。
	 */
	public ProcParam(String pType, Object pValue) {
		// 使用 其它构造函数，统一初始化
		this(pType, null, pValue);
	}
	
	/**
	 * 这是有参的构造函数，用于构建 存储过程参数对象。
	 * @param pType 存储过程参数对象 的传送类型。有多个可能性，包含在 TYPE_LIST 中。
	 * @param pName 存储过程参数对象 的 参数名。这个名称的设置，只是为了便于参数对象查找。建议与存储过程的定义保持一致。
	 * @param pValue 存储过程参数对象 的 参数值。如果是输出的参数值，还需要配合 outParamSqlType 才能获取到值。
	 */
	public ProcParam(String pType, String pName, Object pValue) {
		// 统一使用 setter 方法处理
		this.setParamName(pName);
		this.setParamType(pType);
		this.setParamValue(pValue);
	}
	
	// ==================================
	
	/**
	 * 打印本对象的私有属性信息。
	 */
	@Override
	public String toString() {
		
		// 使用工具类打印 这个对象的内部信息
		String fieldsInfo = StrUtil.getJavaBeanFieldsInfo(this);
		
		// 因为内部还有静态属性，所以先过滤掉。
		return Stdout.fplToAnyWhere("%s{%s", 
			this.getClass().getSimpleName(), fieldsInfo.substring(fieldsInfo.indexOf("(private)")));
		
	}

}
