/******************************************************************************************************

This file "DBInjectionUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.database;

import java.util.regex.Pattern;

import net.mickarea.tools.utils.StrUtil;

/**
 * 数据库Sql语句防注入处理工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月24日-2025年1月20日
 */
public final class DBSQLInjectionUtil {
	
	/**
	 * 数据库类型：DB2
	 */
	public static final String DBTYPE_DB2 = "db2";
	/**
	 * 数据库类型：MySQL
	 */
	public static final String DBTYPE_MYSQL = "mysql";
	/**
	 * 数据库类型：MySQL，但是 处理方式与 DBTYPE_MYSQL 不同，它只处理单引号。
	 */
	public static final String DBTYPE_MYSQL_ANSI = "mysql_ansi";
	/**
	 * 数据库类型：Oracle
	 */
	public static final String DBTYPE_ORACLE = "oracle";
	/**
	 * 数据库类型：MS SQL Server
	 */
	public static final String DBTYPE_MS_SQLSERVER = "sqlserver";
	/**
	 * 数据库类型：默认
	 */
	public static final String DBTYPE_DEFAULT = "default";

	/**
	 * SQL 语句中的条件参数转换，将带特殊符号的SQL语句参数字符串，转换为合规的SQL语句参数字符串
	 * @param dbType 数据库类型字符串(mysql, oracle, sqlserver, db2 等等)
	 * @param sqlParam 待处理的 SQL 语句 条件参数字符串
	 * @return 已处理的SQL语句字符串。如果数据库类型不支持，返回 error: dbType not supported。如果参数为空，则返回空字符串
	 */
	public static String sqlParamEncode(String dbType, String sqlParam) {
		String result = "";
		if(!StrUtil.isEmptyString(dbType) && !StrUtil.isEmptyString(sqlParam)) {
			switch(dbType.toLowerCase()) {
			case DBTYPE_MYSQL:
				result = mySqlParamEncode(sqlParam);
				break;
			case DBTYPE_DB2:
				result = db2SqlParamEncode(sqlParam);
				break;
			default:
				result = defaultSqlParamEncode(sqlParam);
				break;
			}
		}
		return result;
	}
	
	/**
	 * 默认的防注入处理，只处理单引号
	 * @param sqlParam sql语句参数字符串
	 * @return 替换后的字符串
	 */
	private static String defaultSqlParamEncode(String sqlParam) {
		return sqlParam.replaceAll("\'", "\'\'");
	}
	
	/**
	 * 关于 DB2 数据库的防注入处理
	 * @param sqlParam sql语句参数字符串
	 * @return 替换后的字符串
	 */
	private static String db2SqlParamEncode(String sqlParam) {
		return sqlParam.replaceAll("\'", "\'\'").replaceAll(";", ".");
	}
	
	/**
	 * 关于 MySQL 数据库的防注入处理
	 * @param sqlParam sql语句参数字符串
	 * @return 替换后的字符串
	 */
	private static String mySqlParamEncode(String sqlParam) {
		String result = sqlParam;
		//先替换空白字符，将其变为一个单空格
		if(Pattern.matches(".*\\s+.*", result)) {
			result = result.replaceAll("\\s+", " ");
		}
		//如果有反斜杠（因为后面可能替换后有反斜杠，无法判断是拼接的还是原始的。所以要提前判断）
		if(result.indexOf("\\")>=0) {
			result = result.replaceAll("\\\\", "\\\\\\\\");
		}
		//如果有双引号，则替换
		if(result.indexOf("\"")>=0) {
			result = result.replaceAll("\\\"", "\\\\\"");
		}
		//如果有百分号
		if(result.indexOf("%")>=0) {
			result = result.replaceAll("\\%", "\\\\%");
		}
		//如果有单引号
		if(result.indexOf("'")>=0) {
			result = result.replaceAll("\\'", "\\\\'");
		}
		//如果有下划线
		if(result.indexOf("_")>=0) {
			result = result.replaceAll("\\_", "\\\\_");
		}
		//如果有等号
		if(result.indexOf("=")>=0) {
			result = result.replaceAll("\\=", "\\\\=");
		}
		//如果有分号
		if(result.indexOf(";")>=0) {
			result = result.replaceAll("\\;", "\\\\;");
		}
		return result;
	}
	
	/**
	 * 测试函数
	 * @param args 参数
	 */
	/*
	public static void main(String[] args) {
		
		String param = " % _ ' \" \\ ' or 1=1 ; drop table test_a ; select 1 from dual where 1=1 or 1=' ";
		String sql1 = "select * from test_a where id='"+param+"'";
		
		String param2 = ESAPI.encoder().encodeForSQL(new MySQLCodec(MySQLCodec.Mode.STANDARD), param);
		String sql3 = "select * from test_a where id='"+param2+"'";
		
		Stdout.pl(sql3);
		Stdout.pl(sql1);
		
		Stdout.pl("select * from test_a where id='"+sqlParmEncode("mysql",param)+"'");
		Stdout.pl("select * from test_a where id='"+sqlParmEncode("oracle",param)+"'");
		Stdout.pl("select * from test_a where id='"+sqlParmEncode("db2",param)+"'");
	}
	*/
}