/******************************************************************************************************

This file "StrUtil.java" is part of project "niceday" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 个人的一个字符工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.1
 * @since 2022年6月26日-2025年12月11日
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
	 * 根据输入的英文字符串，返回一个驼峰表示的字符串。（如果 input 和 split 都不为空，才处理。否则，返回空字符串）
	 * @param input 输入的待处理字符串
	 * @param splitString 字符串中的分割符号正则表达式。它与 split 函数的参数一致。
	 * @param joinString 字符串拼接时，使用的一个拼接字符。比如 net.mickarea 中的 点号 “.”。默认是空字符串。
	 * @param toLowerCase 是否需要预处理。如果为 true，对所有字符统一转小写。
	 * @return 一个驼峰表示的字符串
	 */
	public static String makeHumpString(String input, String splitString, String joinString, boolean toLowerCase) {
		// 定义一个返回结果
		String output = "";
		// 定义一个拼接字符串。默认是空字符串（这里为了保证分隔符号的通用，不做trim处理。因为有时候，就是需要空格之类的分隔符）
		String tmpJoinStr = joinString==null ? "":joinString;
		// 如果 input 和 split 都不为空，才处理
		if(!isEmptyString(input) && !isEmptyString(splitString)) {
			// 这里使用了 JDK8 开始有的 流式处理
			output = Arrays.stream(input.split(splitString))
					.map(str->{
						// 对切割后的字符串数组，内部单个字符串进行 转换处理（如果需要先转为小写，则转换，否则不转换）
						String tmp = toLowerCase ? str.toLowerCase() : str;
						// 首字母转为大写
						return makeFirstCharUpperCase(tmp.trim());
					})
					.filter(path->!StrUtil.isEmptyString(path)) // 去掉List中的空字符串
					.collect(Collectors.joining(tmpJoinStr));  // 将List转为 字符串。
		}
		// 返回结果
		return output ;
	}
	
	/**
	 * 根据输入的英文字符串，返回一个驼峰表示的字符串。（如果 input 和 split 都不为空，才处理。否则，返回空字符串）
	 * @param input 输入的待处理字符串
	 * @param splitString 字符串中的分割符号正则表达式。它与 split 函数的参数一致。
	 * @return 一个驼峰表示的字符串（joinString 为空字符串，toLowerCase 为 true）
	 */
	public static String makeHumpString(String input, String splitString) {
		// 这里修改为调用通用的 makeHumpString
		return makeHumpString(input, splitString, "", true);
	}
	
	/**
	 * 判断字符串参数是否为空，当字符内容为null也判断为空。
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
	 * 网页参数注入转换，用于防范网页攻击
	 * @param param 待过滤字符
	 * @return 转换完毕的字符
	 */
	@Deprecated
	public static String injectionTranslate(String param) {
		//return ESAPI.encoder().encodeForHTML(param);
		return injectionTranslateForHtml(param);
	}
	
	/**
	 * Sql语句参数注入转换，用于防范sql注入。（默认为：MYSQL）
	 * @param sqlParam sql语句的参数
	 * @return 转换完毕的字符
	 */
	public static String injectionTranslateForSQL(String sqlParam) {
		return DBSQLInjectionUtil.sqlParamEncode(DBSQLInjectionUtil.DBTYPE_MYSQL, sqlParam);
	}
	
	/**
	 * Sql语句参数注入转换，用于防范sql注入。
	 * @param dbType 要转义的数据库类型。参考：DBSQLInjectionUtil 类中的 <strong>DBTYPE</strong>常量
	 * @param sqlParam sql语句的参数
	 * @return 转换完毕的字符
	 */
	public static String injectionTranslateForSQL(String dbType, String sqlParam) {
		return DBSQLInjectionUtil.sqlParamEncode(dbType, sqlParam);
	}
	
	/**
	 * 对于html的内容进行转义。否则，网页显示会有影响
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
	 * 对于html的内容进行转义。否则，网页显示会有影响
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
	 * 在原始字符串中，查找有多少个 匹配的字符串，返回匹配数量
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
	 * 判断输入的字符串是否与java关键字同名
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
	 * 判断输入的字符集的名称是否符合规范
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
	 * 删除待处理字符串中的所有空白字符
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
	
	/**
	 * 获取 Java bean 类的属性信息字符串。通常是一个全部属性的拼接完整的内容。
	 * @param targetObj 待处理对象
	 * @return 属性信息字符串。如果参数为null，可能返回 null。
	 */
	public static final String getJavaBeanFieldsInfo(Object targetObj) {
		
		//如果传来的是null对象，那就直接返回 null
		if(targetObj==null) {
			return null;
		}
		
		//定义返回的结果对象
		StringBuffer sb = new StringBuffer();
		
		//获取该对象的所有属性
		Field[] fields = targetObj.getClass().getDeclaredFields();
		
		//(拼接类名)
		sb.append(targetObj.getClass().getSimpleName());
		sb.append("{");
		//拼接属性名称 和 数值
		if(fields!=null && fields.length>0) {
			for(int i=0;i<fields.length;i++) {
				Field tmpField = fields[i];
				String fieldName = tmpField.getName();
				String fieldValue = null;
				try {
					//设置 可访问性，为 true，强制访问（有些属性是私有的无法直接访问，除非设置为可访问）
					if(!tmpField.isAccessible()) {
						tmpField.setAccessible(true);
					}
					//获取字符串形式的内容
					fieldValue = tmpField.get(targetObj)==null?null:tmpField.get(targetObj).toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 添加 修饰符信息
				sb.append(getModifierInfo(tmpField));
				//拼接属性名和属性值
				sb.append(fieldName);
				sb.append("=");
				sb.append(fieldValue);
				if(i<fields.length-1) {
					sb.append(", ");
				}
			}
		}
		sb.append("}");
		
		//返回结果
		return sb.toString();
	}
	
	/**
	 * 根据传来的 Java 类的属性对象，解析它所定义的关键字是什么
	 * @param f Java 类的属性对象
	 * @return 修饰的关键字信息
	 */
	public static final String getModifierInfo(Field f) {
		//定义返回的结果对象
		StringBuffer sb = new StringBuffer();
		//
		if(f!=null) {
			sb.append("(");
			int modifiers = f.getModifiers();
			if(Modifier.isPublic(modifiers)) {
				sb.append("public ");
			}
			if(Modifier.isPrivate(modifiers)) {
				sb.append("private ");
			}
			if(Modifier.isProtected(modifiers)) {
				sb.append("protected ");
			}
			if(Modifier.isStatic(modifiers)) {
				sb.append("static ");
			}
			if(Modifier.isFinal(modifiers)) {
				sb.append("final ");
			}
			//这里把前内容输出，然后把最后一个空格去掉
			String tmp = sb.toString().trim();
			sb.setLength(0);
			sb.append(tmp);//重新设置
			sb.append(")");//最后的拼接
		}else {
			//如果传来的属性对象是null，则直接写 null
			sb.append("(null)");
		}
		//返回结果
		return sb.toString();
	}
	
	/**
	 * 这是这个转换器默认提供的数据转换方法。它可以把字符串，按照需要自动转换为目标类型数据。使用方法，请参考 StrUtilTester2 里面的单元测试
	 * @param <T> 对应类型的泛型。
	 * @param oriValue 待转换的字符串。
	 * @param targetType 要转换为的目标类型。比如：String.class, int.class, Integer.class 等等
	 * @param defaultFmtStr 这是日期和时间的转换字符串，可以不提供。设置为 null 或者 空字符。
	 * @return 一个目标对象
	 * @throws IllegalArgumentException 方法的参数校验不通过，则抛出此异常
	 * @throws NoSuchMethodException 没有匹配的方法时，抛出此异常。需要检查待处理类型是否有映射到位。
	 * @throws NumberFormatException 数字转换出错时，抛出的异常
	 * @throws DateTimeParseException 时间转换出错时，抛出的异常
	 * @throws Exception 其它异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T autoConvert(String oriValue, Class<T> targetType, String defaultFmtStr)
			throws IllegalArgumentException, NoSuchMethodException, NumberFormatException, DateTimeParseException, Exception {
		
		// 首先校验 targetType。因为如果目标类型是 null，还转换什么
		if(targetType==null) throw new IllegalArgumentException("the parameter targetType is null.");
		
		// 如果 target 不是字符串，则把 oriValue 的前后空白字符去掉再处理
		String newVal = oriValue;
		if(!targetType.equals(String.class) 
				&& !targetType.equals(char.class) && !targetType.equals(Character.class) && oriValue!=null) newVal = newVal.trim();
		
		// 提取有效的值 和 获取类名
		String methodName = "to" + makeHumpString(targetType.getTypeName(), "\\.", "", false);
		
		// 对于枚举值，方法名有点差异
		if(targetType.isEnum()) methodName = "toEnumObject";
		
		// 获取反射的方法
		Method method = null;
		Object result = null; // 这里是反射处理，都是用 Object 作为一个中间媒介的
		
		try {
			// 如果是时间处理，需要映射为另外一个方法（它带有一个格式转换字符串）
			if(targetType.equals(LocalDate.class) || targetType.equals(LocalDateTime.class)) {
				method = StrToDataConvertor.class.getMethod(methodName, String.class, String.class);
				// 开始执行反射处理
				result = method.invoke(null, newVal, defaultFmtStr);
			}else if(targetType.isEnum()){
				// 关于枚举类型
				// 这里方法要映射2个参数，一个是字符值，一个是枚举类
				method = StrToDataConvertor.class.getMethod(methodName, String.class, targetType.getClass());
				// 开始执行反射处理
				result = method.invoke(null, newVal, targetType);
			} else {
				method = StrToDataConvertor.class.getMethod(methodName, String.class);
				// 开始执行反射处理
				result = method.invoke(null, newVal);
			}
		}catch(InvocationTargetException itex) {
			// 因为反射的调用时发生的异常，会被封装在 InvocationTargetException ，所以需要解包
			// 解包 处理 (解包后，数字、时间等等的转换异常才可以抛出到外部，然后捕捉)
			throw (Exception)itex.getTargetException();
		}catch(Exception e) {
			// 普通异常直接抛出
			throw e;
		}
		
		// 返回结果
		return (T) result;
	}
}
