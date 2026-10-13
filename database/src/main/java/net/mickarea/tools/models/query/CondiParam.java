/******************************************************************************************************

This file "CondiParam.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import java.util.List;
import java.util.regex.Pattern;

import net.mickarea.tools.models.query.abstracts.impls.MySQLValueToString;
import net.mickarea.tools.models.query.abstracts.impls.OracleValueToString;
import net.mickarea.tools.models.query.abstracts.impls.SqlServerValueToString;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;
import net.mickarea.tools.utils.database.DBStrUtil;

/**
 * &gt;&gt;&nbsp;查询对象，用于构造查询字段sql语句
 * <p>查询对象一般是针对单个字段处理的。创建 CondiParam 对象后，放入 CondiParamList 中等待查询调用</p>
 * <p>在使用上，paramName 属性是指查询的数据库字段名；</p>
 * <p>如果按照实体类的属性名查询，需要指定 className 属性，即实体类的class值</p>
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月21日-2025年1月20日
 */
public class CondiParam {

	//这些是查询条件的操作符
	/**
	 * &gt;&gt;&nbsp;条件操作符，“等于”，如果参数不指定，则默认为这个
	 */
	public static final String OPT_EQ = "=";
	/**
	 * &gt;&gt;&nbsp;条件操作符，“大于”
	 */
	public static final String OPT_GT = ">";
	/**
	 * &gt;&gt;&nbsp;条件操作符，“大于等于”
	 */
	public static final String OPT_GTE = ">=";
	/**
	 * &gt;&gt;&nbsp;条件操作符，“小于”
	 */
	public static final String OPT_LT = "<";
	/**
	 * &gt;&gt;&nbsp;条件操作符，“小于等于”
	 */
	public static final String OPT_LTE = "<=";
	/**
	 * &gt;&gt;&nbsp;条件操作符，“不等于”
	 */
	public static final String OPT_NOT_EQ = "<>";
	/**
	 * &gt;&gt;&nbsp;条件操作符，“前后模糊匹配，%a%”
	 */
	public static final String OPT_LIKE = "like";
	/**
	 * &gt;&gt;&nbsp;条件操作符，“前模糊匹配，a%”
	 */
	public static final String OPT_LIKE_PRE = "like_pre";
	/**
	 * &gt;&gt;&nbsp;条件操作符，“后模糊匹配，%a”
	 */
	public static final String OPT_LIKE_SUF = "like_suf";
	/**
	 * &gt;&gt;&nbsp;条件操作符，“为 NULL”
	 */
	public static final String OPT_IS_NULL = "is null";
	/**
	 * &gt;&gt;&nbsp;条件操作符，“不为 NULL”
	 */
	public static final String OPT_IS_NOT_NULL = "is not null";
	/**
	 * &gt;&gt;&nbsp;条件操作符，“正则匹配”
	 */
	public static final String OPT_REGEXP = "regexp";
	/**
	 * &gt;&gt;&nbsp;条件操作符，“包含匹配 in”
	 */
	public static final String OPT_IN = "in";
	
	private String alias;                 //别名
	private String paramName;             //字段或者属性名
	private String optString = OPT_EQ;    //操作符
	private Object value;                 //值
	private Class<?> className = null;    //这个属性对应的数据库映射对象类名
	
	private String sqlTemplateParamName;  //对应 CondiParamList 类中， sqlTemplate 里面的别名
	
	/**
	 * &gt;&gt;&nbsp;无参构造函数，所有值需要通过 setter 方法手动设定
	 */
	public CondiParam() {
	}
	/**
	 * &gt;&gt;&nbsp;有参构造函数（设置查询条件数据库字段名，字段的值）
	 * @param paramName 字段名
	 * @param value 字段的值
	 */
	public CondiParam(String paramName, Object value) {
		this.paramName = paramName;
		this.value = value;
		this.sqlTemplateParamName = paramName;
	}
	/**
	 * &gt;&gt;&nbsp;有参构造函数（设置查询条件数据库字段名，字段的值，条件操作符）
	 * @param paramName 字段名
	 * @param value 字段的值
	 * @param optString 条件操作符：可写 OPT_EQ， OPT_GT 等等
	 */
	public CondiParam(String paramName, Object value, String optString) {
		this.paramName = paramName;
		this.value = value;
		this.optString = optString;
		this.sqlTemplateParamName = paramName;
	}
	/**
	 * &gt;&gt;&nbsp;有参构造函数（设置查询条件数据库字段名，字段的值，条件操作符，表别名）
	 * @param paramName 字段名
	 * @param value 字段的值
	 * @param optString 条件操作符：可写 OPT_EQ， OPT_GT 等等
	 * @param alias 字段所属表的别名。如果查询的表有别名，则设置。否则，设置为 null。比如：<code>select a.id, a.name from user a where a.id=1;</code> 中，
	 * a 就是 id 字段的表别名
	 */
	public CondiParam(String paramName, Object value, String optString, String alias) {
		this.alias = alias;
		this.paramName = paramName;
		this.value = value;
		this.optString = optString;
		this.sqlTemplateParamName = paramName;
	}
	/**
	 * &gt;&gt;&nbsp;有参构造函数（设置查询条件数据库字段名，字段的值，条件操作符，表别名，实体类类名）
	 * @param paramName 字段名
	 * @param value 字段的值
	 * @param optString 条件操作符：可写 OPT_EQ， OPT_GT 等等
	 * @param alias 字段所属表的别名。如果查询的表有别名，则设置。否则，设置为 null。比如：<code>select a.id, a.name from user a where a.id=1;</code> 中，
	 * a 就是 id 字段的表别名
	 * @param className 查询字段所属实体类的类名。当 paramName 值为实体类的属性名，则需要设置类名，以供框架内容转化。
	 */
	public CondiParam(String paramName, Object value, String optString, String alias, Class<?> className) {
		this.alias = alias;
		this.paramName = paramName;
		this.value = value;
		this.optString = optString;
		this.sqlTemplateParamName = paramName;
		this.className = className;
	}
	
	//getter and setter
	public String getParamName() {
		return paramName;
	}
	public CondiParam setParamName(String paramName) {
		this.paramName = paramName;
		return this;
	}
	public String getOptString() {
		return optString;
	}
	public CondiParam setOptString(String optString) {
		this.optString = optString;
		return this;
	}
	public Object getValue() {
		return value;
	}
	public CondiParam setValue(Object value) {
		this.value = value;
		return this;
	}
	public String getAlias() {
		return alias;
	}
	public CondiParam setAlias(String alias) {
		this.alias = alias;
		return this;
	}
	public String getSqlTemplateParamName() {
		return sqlTemplateParamName;
	}
	public CondiParam setSqlTemplateParamName(String sqlTemplateParamName) {
		this.sqlTemplateParamName = sqlTemplateParamName;
		return this;
	}
	public Object getClassName() {
		return this.className;
	}
	public CondiParam setClassName(Class<?> className) {
		this.className = className;
		return this;
	}
	
	/**
	 * 根据当前的查询条件信息，拼接出一个 条件 sql 语句
	 * @param cls 查询映射的对象类（一个实体类）
	 * @param dbType 数据库类型。参考 DBSQLInjectionUtil 类的 DBTYPE 常量
	 * @return 一个拼接好的查询条件
	 */
	private String toSqlString(Class<?> cls, String dbType) {
		
		String result = "";
		
		//处理字段或者属性名，最后都会转换成数据库字段
		String tmpParamName = this.paramName;
		if(cls!=null) {
			tmpParamName = DBStrUtil.parsePropertyNameToColumnName(cls, tmpParamName);//如果有匹配的对象类信息，则执行对象翻译
		}
		if(!StrUtil.isEmptyString(this.alias)) {
			tmpParamName = this.alias+"."+tmpParamName;//如果有前缀，则加上
		}
		
		//处理查询条件的值
		String tmpValue = "";
		if(OPT_IN.equalsIgnoreCase(this.optString) && this.value instanceof List) {
			tmpValue = this.valueObjectToString((List<?>)this.value, dbType);
		}else {
			tmpValue = this.valueObjectToString(this.value, dbType);
		}
		if(StrUtil.isEmptyString(tmpValue)) {
			tmpValue = "''";
		}
		
		//根据关系操作符构造
		switch(this.optString) {
			case OPT_LIKE_PRE:
				result = Stdout.fplToAnyWhere("%s %s '%s%%'", tmpParamName, this.optString.replaceFirst("_pre", ""), tmpValue.substring(1, tmpValue.length()-1));
				break;
			case OPT_LIKE_SUF:
				result = Stdout.fplToAnyWhere("%s %s '%%%s'", tmpParamName, this.optString.replaceFirst("_suf", ""), tmpValue.substring(1, tmpValue.length()-1));
				break;
			case OPT_LIKE:
				result = Stdout.fplToAnyWhere("%s %s '%%%s%%'", tmpParamName, this.optString, tmpValue.substring(1, tmpValue.length()-1));
				break;
			case OPT_IS_NULL:
			case OPT_IS_NOT_NULL:
				result = Stdout.fplToAnyWhere("%s %s", tmpParamName, this.optString);
				break;
			case OPT_IN:
				result = Stdout.fplToAnyWhere("%s %s (%s)", tmpParamName, this.optString, tmpValue);
				break;
			case OPT_REGEXP:
			default:
				result = Stdout.fplToAnyWhere("%s %s %s", tmpParamName, this.optString, tmpValue);
				break;
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据当前的查询条件信息，拼接出一个 条件 sql 语句
	 * @param dbType 数据库类型。参考 DBSQLInjectionUtil 类的 DBTYPE 常量
	 * @return 一个拼接好的查询条件
	 */
	public String toSqlString(String dbType) {
		return this.toSqlString(this.className, dbType);
	}
	
	@Override
	public String toString() {
		return Stdout.fplToAnyWhere("查询条件信息如下：sqlTemplateParamName:%s，alias为：%s，paramName为：%s，optString为：%s，value为：%s", 
				this.sqlTemplateParamName,
				this.alias,
				this.paramName,
				this.optString,
				this.value);
	}
	
	/**
	 * &gt;&gt;&nbsp;把不同类型的value值，转换成一个能显示出来的字符串
	 * @param value 条件对象的值
	 * @param dbType 数据库类型。参考 DBSQLInjectionUtil 类的 DBTYPE 常量
	 * @return 一个字符串
	 */
	public String valueObjectToString(Object value, String dbType) {
		String result = "dbType("+dbType+") error, while mapping to string.";
		switch(dbType) {
		case DBSQLInjectionUtil.DBTYPE_MYSQL:
			result = new MySQLValueToString().valueToString(value);
			break;
		case DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI:
			result = new MySQLValueToString(dbType).valueToString(value);
			break;
		case DBSQLInjectionUtil.DBTYPE_ORACLE:
			result = new OracleValueToString().valueToString(value);
			break;
		case DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER:
			result = new SqlServerValueToString().valueToString(value);
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;把不同类型的value值，转换成一个能显示出来的字符串 （这个函数主要用于处理 List 类型的数据）
	 * @param valueList 一个条件值的列表
	 * @param dbType 数据库类型。参考 DBSQLInjectionUtil 类的 DBTYPE 常量
	 * @return 一个条件值的字符串
	 */
	public String valueObjectToString(List<?> valueList, String dbType) {
		String tmpValue = "";
		String tmp = "";
		if(valueList!=null) {
			for(int i=0;i<valueList.size();i++) {
				if(valueList.get(i)==null) {
					//如果列表中有空值，跳过
					continue;
				}else {
					//对于单个值的翻译，使用已经存在的翻译函数
					tmp = this.valueObjectToString(valueList.get(i), dbType);
					//如果翻译出来不是空内容，则可以拼接
					if(!StrUtil.isEmptyString(tmp)) {
						tmpValue += tmp;
						if(i<valueList.size()-1) {
							tmpValue += ",";
						}
					}
				}
			}
			//因为如果有null值，被跳过了，则可能出现多个逗号相连的情况，要处理多余的逗号
			if(Pattern.matches("[\\,]{2,}", tmpValue)) {
				tmpValue.replaceAll("[\\,]{2,}", ",");
			}
			//最后处理下，如果有逗号结尾，则去掉
			if(tmpValue.endsWith(",")) {
				tmpValue = tmpValue.substring(0, tmpValue.length()-1);
			}
		}
		return tmpValue;
	}
	
	/**
	 * &gt;&gt;&nbsp;测试函数
	 */
	/*
	public static void main(String[] args) {
		
		CondiParam condParam = new CondiParam("tel",
				                              Arrays.asList("' or 12=12 ; delete test_a; selec 1, '", 14, "aaaa", null, "", null, "", new BigDecimal(123.256398)), 
				                              CondiParam.OPT_IN);
		condParam.setAlias("usb");
		Stdout.pl(condParam.toSqlString(DBSQLInjectionUtil.DBTYPE_MYSQL));
		Stdout.pl(condParam.toSqlString(TestA.class, DBSQLInjectionUtil.DBTYPE_MYSQL));
		
		condParam.setAlias("my2k");
		Stdout.pl(condParam.toSqlString(DBSQLInjectionUtil.DBTYPE_ORACLE));
		Stdout.pl(condParam.toSqlString(TestA.class, DBSQLInjectionUtil.DBTYPE_ORACLE));
		
		Stdout.pl(new CondiParam().setParamName("examScore")
				                  .setValue("abck;dkdlkjfdjk")
				                  .setClassName(TestB.class)
				                  .toSqlString(DBSQLInjectionUtil.DBTYPE_DB2));
	}*/
	
}
