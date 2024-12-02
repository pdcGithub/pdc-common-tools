/******************************************************************************************************

This file "StrUtil.java" is part of project "niceday" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * &gt;&gt;&nbsp;个人的一个字符工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.1
 * @since 2022年6月26日-2024年12月2日
 */
public final class StrUtil {
	
	/**
	 * 私有构造函数
	 */
	private StrUtil() {
		// TODO Auto-generated constructor stub
	}
	
	//java 关键字；构造属性时不能和关键字同名
	private static final String[] javaKeyWords = {"abstract", "assert",     "boolean", "break",     "byte",      "case",       "catch",    "char",    "class", "continue", 
			                                      "default",  "do",         "double",  "else",      "enum",      "extends",    "final",    "finally", "float",  "for", 
			                                      "if",       "implements", "import",  "int",       "interface", "instanceof", "long",     "native",  "new",    "package", 
			                                      "private",  "protected",  "public",  "return",    "short",     "static",     "strictfp", "super",   "switch", "synchronized",
			                                      "this",     "throw",      "throws",  "transient", "try",       "void",       "volatile", "while",   "true",   "false",
			                                      "null",     "goto",       "const"};
	
	/**
	 * 使一个英文字符串的首字母变为大写的形式
	 * @param input 输入字符串
	 * @return 处理后的字符串
	 */
	public static String makeFirstCharUpperCase(String input) {
		String output = "";
		if(!isEmptyString(input)) {
			output = input.substring(0, 1).toUpperCase()+input.substring(1);
		}
		return output;
	}
	
	/**
	 * 使一个英文字符串的首字母变为小写的形式
	 * @param input 输入字符串
	 * @return 处理后的字符串
	 */
	public static String makeFirstCharLowerCase(String input) {
		String output = "";
		if(!isEmptyString(input)) {
			output = input.substring(0, 1).toLowerCase()+input.substring(1);
		}
		return output;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据输入的英文字符串，返回一个驼峰表示的字符串
	 * @param input 输入的字符串
	 * @param splitString 字符串中的分割符号
	 * @return 一个驼峰表示的字符串
	 */
	public static String makeHumpString(String input, String splitString) {
		String output = "";
		if(!isEmptyString(input) && !isEmptyString(splitString)) {
			//统一转小写字母，然后再处理
			String[] s = input.toLowerCase().split(splitString);
			for(int i=0;i<s.length;i++) {
				s[i] = makeFirstCharUpperCase(s[i]);
			}
			output = String.join("", s);
		}
		return output ;
	}
	
	
	
	/**
	 * &gt;&gt;&nbsp;判断字符串参数是否为空，当字符内容为null也判断为空。
	 * @param param 待判断的字符串参数
	 * @return 如果param为null，'null'，空白字符，空等则为true。
	 */
	public static boolean isEmptyString(String param) {
		boolean result = false;
		if(param==null || param.trim().equalsIgnoreCase("null") || param.trim().length()==0 || Pattern.matches("[\\s]+", param.trim())) {
			result = true;
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;网页参数注入转换，用于防范网页攻击
	 * @param param 待过滤字符
	 * @return 转换完毕的字符
	 */
	@Deprecated
	public static String injectionTranslate(String param) {
		//return ESAPI.encoder().encodeForHTML(param);
		return injectionTranslateForHtml(param);
	}
	
	/**
	 * &gt;&gt;&nbsp;Sql语句参数注入转换，用于防范sql注入。（默认为：MYSQL）
	 * @param sqlParam sql语句的参数
	 * @return 转换完毕的字符
	 */
	public static String injectionTranslateForSQL(String sqlParam) {
		return DBSQLInjectionUtil.sqlParamEncode(DBSQLInjectionUtil.DBTYPE_MYSQL, sqlParam);
	}
	
	/**
	 * &gt;&gt;&nbsp;Sql语句参数注入转换，用于防范sql注入。
	 * @param dbType 要转义的数据库类型。参考：DBSQLInjectionUtil 类中的 <strong>DBTYPE</strong>常量
	 * @param sqlParam sql语句的参数
	 * @return 转换完毕的字符
	 */
	public static String injectionTranslateForSQL(String dbType, String sqlParam) {
		return DBSQLInjectionUtil.sqlParamEncode(dbType, sqlParam);
	}
	
	/**
	 * &gt;&gt;&nbsp;对于html的内容进行转义。否则，网页显示会有影响
	 * @param htmlParam 待转换的内容
	 * @return 转换后的内容
	 */
	public static String injectionTranslateForHtml(String htmlParam) {
		//return ESAPI.encoder().encodeForHTML(htmlParam);
		String re = "";
		if(!isEmptyString(htmlParam)) {
			re = htmlParam.trim().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		}
		return re;
	}
	
	/**
	 * &gt;&gt;&nbsp;对于html的内容进行转义。否则，网页显示会有影响
	 * <p>这里是关于富文本编辑器的内容转换问题。因为富文本无法直接转换 &lt; 和 &gt; 。所以，需要更加强大的防注入转换</p>
	 * @param htmlParam 待转换的内容
	 * @return 转换后的内容
	 */
	public static String injectionTranslateForHtmlEditor(String htmlParam) {
		String result = "";
		if(!StrUtil.isEmptyString(htmlParam)) {
			
			List<String> tagNameForDelete = Arrays.asList("script", "form", "iframe", "object", "embed");
			result = htmlParam;
			//替换 html 标签（完全删除）
			for(String tagName : tagNameForDelete) {
				result = result.replaceAll(PatternUtil.genRegStrForHtmlTagReplace(tagName), "");
			}
			//替换 on 属性
			result =  result.replaceAll("\\s+[oO][nN][a-zA-Z]+=", " forbidden=");
			//替换有 javascript 的 href 属性
			result = result.replaceAll("\\s+[Hh][Rr][Ee][Ff]=['\"]\\s*[Jj][Aa][Vv][Aa][Ss][Cc][Rr][Ii][Pp][Tt]:", " href=\"");
			// 存在一些字符串过滤 绕过的问题，比如：<a href="java&#x0073;cript:alert(6)">点击6下</a> 这是可执行的。要对 &# 内容替换 
			result = result.replaceAll("\\s+[Hh][Rr][Ee][Ff]=['\"][\\s]*.*?\\&\\#.*?", " href=\"");
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;在原始字符串中，查找有多少个 匹配的字符串，返回匹配数量
	 * @param ori 原始字符串
	 * @param matchStr  匹配的字符串，使用正则表达式
	 * @return 匹配数量
	 */
	public static int countString(String ori, String matchStr) {
		int result = 0 ;
		if(!isEmptyString(ori) && !isEmptyString(matchStr)) {
			Matcher m = Pattern.compile(matchStr).matcher(ori);
			while(m.find()) {
				result++;
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;判断输入的字符串是否与java关键字同名
	 * @param input 待判断的字符串
	 * @return 如果与java关键字同名，则返回true；否则，返回false
	 */
	public static boolean isJavaKeyWords(String input) {
		boolean result = false;
		for(String s: javaKeyWords) {
			if(s.equals(input)) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;判断输入的字符集的名称是否符合规范
	 * @param charsetName 字符集名称。比如：GBK, UTF-8, ISO-8859-1 等等
	 * @return 如果字符集能识别，则返回 true；否则 false 。
	 */
	public static boolean isCharsetSupported(String charsetName) {
		boolean result = false;
		if(!isEmptyString(charsetName)) {
			try {
				result = Charset.isSupported(charsetName);
			}catch(Exception e) {
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;删除待处理字符串中的所有空白字符
	 * @param words 待处理字符
	 * @return 如果words参数为null，返回空字符串""。否则，返回处理后的字符串
	 */
	public static String removeAllBlankStrings(String words) {
		String result = "";
		if(words!=null) {
			result = words.replaceAll("[\\s]+", "");
		}
		return result;
	}
	
	/*
	public static void main(String[] args) {
		String ori = FileUtil.loadStringFromFile("C:\\Users\\Michael\\Desktop", "injection_test.html", true);
		Stdout.pl(ori);
		Stdout.pl("============================================");
		String newStr = injectionTranslateForHtmlEditor(ori);
		Stdout.pl(newStr);
		Stdout.pl("===================== 写入到文件 ===================");
		boolean isOk = FileUtil.saveToLocalpath(newStr, "C:\\Users\\Michael\\Desktop", "ok_test.html", "UTF-8");
		Stdout.pl("文件写入 "+(isOk?"成功":"失败"));
	}
	*/
}
