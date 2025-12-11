/******************************************************************************************************

This file "StrToDataConvertor.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * <p>
 * <strong>这是一个字符串转换器。它会将原始字符串转换为一个对应的数据。至于具体转换为什么数据，看调用的是哪个函数。</strong>
 * (注意：这个类一般不会直接调用。通过反射机制，动态调用更好一些。或者使用默认提供的 StrUtil.autoConvert 方法)
 * </p>
 * Java 语言提供了8种基本类型。6种数字类型（四个整数型，两个浮点型），1种字符类型，还有1种布尔型。相对应的它们都有自己的8个引用类型。
 * 它们分别为:
 * <pre>byte, short, int, long, float, double, boolean, char</pre>
 * <pre>Byte, Short, Integer, Long, Float, Double, Boolean, Character</pre>
 * <p>此外，这里还提供了一些常用的类型转换。比如：LocalDate, LocalDateTime, String, Object, 枚举对象</p>
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年11月5日-2025年12月11日
 */
public final class StrToDataConvertor {
	
	private StrToDataConvertor() {
		// 把构造函数设置为私有，这样就不会被 new 创建了
	}
	
	// ============================ 首先是对 8 种基本的引用类型转换（这里之所以要抛出异常，那是为了告诉调用者，这里出错了。）
	
	/**
	 * 把字符串参数，转换为 Byte 类型对象
	 * @param inValue 待转换字符串
	 * @return Byte 类型对象
	 * @throws NumberFormatException 如果转换时出错，则抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static Byte toJavaLangByte(String inValue) throws NumberFormatException, Exception {
		return Byte.valueOf(inValue);
	}
	
	/**
	 * 把字符串参数，转换为 Short 类型对象
	 * @param inValue 待转换字符串
	 * @return Short 类型对象
	 * @throws NumberFormatException 如果转换时出错，则抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static Short toJavaLangShort(String inValue) throws NumberFormatException, Exception {
		return Short.valueOf(inValue);
	}
	
	/**
	 * 把字符串参数，转换为 Integer 类型对象
	 * @param inValue 待转换字符串
	 * @return Integer 类型对象
	 * @throws NumberFormatException 如果转换时出错，则抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static Integer toJavaLangInteger(String inValue) throws NumberFormatException, Exception {
		return Integer.valueOf(inValue);
	}
	
	/**
	 * 把字符串参数，转换为 Long 类型对象
	 * @param inValue 待转换字符串
	 * @return Long 类型对象
	 * @throws NumberFormatException 如果转换时出错，则抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static Long toJavaLangLong(String inValue) throws NumberFormatException, Exception {
		return Long.valueOf(inValue);
	}
	
	/**
	 * 把字符串参数，转换为 Float 类型对象
	 * @param inValue 待转换字符串
	 * @return Float 类型对象
	 * @throws NumberFormatException 如果转换时出错，则抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static Float toJavaLangFloat(String inValue) throws NumberFormatException, Exception {
		Float f = null;
		try {
			f = Float.valueOf(inValue);
		}catch(NullPointerException npe) {
			// 因为如果 inValue 是 null 值，则会抛出 NullPointerException。为了统一管理，转换为 NumberFormatException
			throw new NumberFormatException("the inValue is null");
		}
		// 如果超出最大值，需要抛出异常，别收起来
		if("Infinity".equalsIgnoreCase(f.toString())) 
			throw new NumberFormatException(Stdout.fplToAnyWhere("The value=%s has exceeded the maximum limit", inValue));
		
		return f;
	}
	
	/**
	 * 把字符串参数，转换为 Double 类型对象
	 * @param inValue 待转换字符串
	 * @return Double 类型对象
	 * @throws NumberFormatException 如果转换时出错，则抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static Double toJavaLangDouble(String inValue) throws NumberFormatException, Exception {
		Double d = null;
		try {
			d = Double.valueOf(inValue);
		}catch(NullPointerException npe) {
			// 因为如果 inValue 是 null 值，则会抛出 NullPointerException。为了统一管理，转换为 NumberFormatException
			throw new NumberFormatException("the inValue is null");
		}
		// 如果超出最大值，需要抛出异常，别收起来
		if("Infinity".equalsIgnoreCase(d.toString())) 
			throw new NumberFormatException(Stdout.fplToAnyWhere("The value=%s has exceeded the maximum limit", inValue));
		return d;
	}
	
	/**
	 * 把字符串参数，转换为 Boolean 类型对象
	 * @param inValue 待转换字符串
	 * @return Boolean 类型对象
	 * @throws IllegalArgumentException 参数异常，无法转换时，抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static Boolean toJavaLangBoolean(String inValue) throws IllegalArgumentException, Exception {
		// 首先排除 null 和 空字符串
		if(StrUtil.isEmptyString(inValue)) throw new IllegalArgumentException("the inValue is empty or null, can not convert to boolean");
		// 然后排除无法转换的字符串
		String regexp = "(?i)true|false"; // 只接受 true 或者 false 字符串，忽略大小写
		if(!Pattern.matches(regexp, inValue)) throw new IllegalArgumentException(
				Stdout.fplToAnyWhere("the value=%s can not convert to boolean value.", inValue));
		// 最后返回
		return Boolean.valueOf(inValue);
	}
	
	/**
	 * 把字符串参数，转换为 Character 类型对象
	 * @param inValue 待转换字符串
	 * @return Character 类型对象
	 * @throws Exception 参数异常，无法转换时，抛出此异常
	 */
	public static Character toJavaLangCharacter(String inValue) throws Exception {
		// 这里是字符类型，要注意处理
		// 取字符串的第一个字符
		char re = (inValue==null || inValue.length()<=0) ? '\0' : inValue.charAt(0);
		// 返回
		return Character.valueOf(re);
	}
	
	// ============================ 首先是对 8 种基本类型转换（这里之所以要抛出异常，那是为了告诉调用者，这里出错了。）
	
	/**
	 * 把字符串参数，转换为 byte 类型数据
	 * @param inValue 待转换字符串
	 * @return byte 类型数据
	 * @throws NumberFormatException 如果转换时出错，则抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static byte toByte(String inValue) throws NumberFormatException, Exception {
		return toJavaLangByte(inValue).byteValue();
	}
	
	/**
	 * 把字符串参数，转换为 short 类型数据
	 * @param inValue 待转换字符串
	 * @return short 类型数据
	 * @throws NumberFormatException 如果转换时出错，则抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static short toShort(String inValue) throws NumberFormatException, Exception {
		return toJavaLangShort(inValue).shortValue();
	}
	
	/**
	 * 把字符串参数，转换为 int 类型数据
	 * @param inValue 待转换字符串
	 * @return int 类型数据
	 * @throws NumberFormatException 如果转换时出错，则抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static int toInt(String inValue) throws NumberFormatException, Exception {
		return toJavaLangInteger(inValue).intValue();
	}
	
	/**
	 * 把字符串参数，转换为 long 类型数据
	 * @param inValue 待转换字符串
	 * @return long 类型数据
	 * @throws NumberFormatException 如果转换时出错，则抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static long toLong(String inValue) throws NumberFormatException, Exception {
		return toJavaLangLong(inValue).longValue();
	}
	
	/**
	 * 把字符串参数，转换为 float 类型数据
	 * @param inValue 待转换字符串
	 * @return float 类型数据
	 * @throws NumberFormatException 如果转换时出错，则抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static float toFloat(String inValue) throws NumberFormatException, Exception {
		return toJavaLangFloat(inValue).floatValue();
	}
	
	/**
	 * 把字符串参数，转换为 double 类型数据
	 * @param inValue 待转换字符串
	 * @return double 类型数据
	 * @throws NumberFormatException 如果转换时出错，则抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static double toDouble(String inValue) throws NumberFormatException, Exception {
		return toJavaLangDouble(inValue).doubleValue();
	}
	
	/**
	 * 把字符串参数，转换为 boolean 类型数据
	 * @param inValue 待转换字符串
	 * @return boolean 类型数据
	 * @throws IllegalArgumentException 参数异常，无法转换时，抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static boolean toBoolean(String inValue) throws Exception {
		return toJavaLangBoolean(inValue).booleanValue();
	}
	
	/**
	 * 把字符串参数，转换为 char 类型数据
	 * @param inValue 待转换字符串
	 * @return char 类型数据
	 * @throws Exception 参数异常，无法转换时，抛出此异常
	 */
	public static char toChar(String inValue) throws Exception {
		return toJavaLangCharacter(inValue).charValue();
	}
	
	// ============================ 对一些常用的数据类型进行转换，比如：时间，日期 等等
	
	/**
	 * 把字符串参数，转换为 LocalDate 类型对象
	 * @param inValue 待转换字符串
	 * @param fmtStr 格式化处理字符串。如果不提供，默认是：TimeUtil.FMT_DATE_WTSPLIT 或者 TimeUtil.FMT_DATE_NORMAL
	 * @return LocalDate 类型对象
	 * @throws DateTimeParseException 日期字符串转换为数据出错，抛出此异常。如果格式化字符串有问题，也会抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static LocalDate toJavaTimeLocalDate(String inValue, String fmtStr) throws DateTimeParseException, Exception {
		
		// 先做参数校验
		if(StrUtil.isEmptyString(inValue)) throw new DateTimeParseException("parameter inValue is empty or null.", ""+inValue, 0);
		
		// 先初始化一次转换器
		DateTimeFormatter fmter = null;
		try {
			fmter = StrUtil.isEmptyString(fmtStr)?null:DateTimeFormatter.ofPattern(fmtStr);
		} catch (IllegalArgumentException e) {
			throw new DateTimeParseException(Stdout.fplToAnyWhere("parameter fmtStr=%s is invalid.", fmtStr), ""+fmtStr, 0);
		}
		
		// 这里根据值的情况，再选择一次转换器
		// 如果是纯数字，可能为 2025、202501、20250102 这几种形式，否则可能为其它形式
		if(fmter==null) fmter=inValue.trim().matches("\\d+")?TimeUtil.FMT_DATE_WTSPLIT:TimeUtil.FMT_DATE_NORMAL;
		
		// 最后转换，并返回
		return LocalDate.parse(inValue.trim(), fmter);
	}
	
	/**
	 * 把字符串参数，转换为 LocalDate 类型对象。这是一个简化的方法，它使用了默认的格式化字符串。
	 * @param inValue 待转换字符串
	 * @return LocalDate 类型对象
	 * @throws DateTimeParseException 日期字符串转换为数据出错，抛出此异常。如果格式化字符串有问题，也会抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static LocalDate toJavaTimeLocalDate(String inValue) throws DateTimeParseException, Exception {
		return toJavaTimeLocalDate(inValue, null);
	}
	
	/**
	 * 把字符串参数，转换为 LocalDateTime 类型对象
	 * @param inValue 待转换字符串
	 * @param fmtStr 格式化处理字符串。如果不提供，默认是：TimeUtil.FMT_DATETIME_NORMAL
	 * @return LocalDateTime 类型对象
	 * @throws DateTimeParseException 日期字符串转换为数据出错，抛出此异常。如果格式化字符串有问题，也会抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static LocalDateTime toJavaTimeLocalDateTime(String inValue, String fmtStr) throws DateTimeParseException, Exception {
		
		// 先做参数校验
		if(StrUtil.isEmptyString(inValue)) throw new DateTimeParseException("parameter inValue is empty or null.", ""+inValue, 0);
		
		// 先初始化一次转换器（如果没有传入，则用默认的）
		DateTimeFormatter fmter;
		try {
			fmter = StrUtil.isEmptyString(fmtStr)?TimeUtil.FMT_DATETIME_NORMAL:DateTimeFormatter.ofPattern(fmtStr);
		} catch (IllegalArgumentException e) {
			throw new DateTimeParseException(Stdout.fplToAnyWhere("parameter fmtStr=%s is invalid.", fmtStr), ""+fmtStr, 0);
		}
		
		// 最后转换，并返回
		return LocalDateTime.parse(inValue, fmter);
	}
	
	/**
	 * 把字符串参数，转换为 LocalDateTime 类型对象。这是一个简化的方法，它使用了默认的格式化字符串。
	 * @param inValue 待转换字符串
	 * @return LocalDateTime 类型对象
	 * @throws DateTimeParseException 日期字符串转换为数据出错，抛出此异常。如果格式化字符串有问题，也会抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static LocalDateTime toJavaTimeLocalDateTime(String inValue) throws DateTimeParseException, Exception {
		return toJavaTimeLocalDateTime(inValue, null);
	}
	
	/**
	 * 把字符串参数，转换为 String 类型对象。
	 * @param inValue 待转换字符串
	 * @return 返回一个去掉前后空白字符的字符串结果。如果 inValue 为空，返回空字符串。
	 */
	public static String toJavaLangString(String inValue) {
		return !StrUtil.isEmptyString(inValue)?inValue.trim():"";
	}
	
	/**
	 * 把字符串参数，转换为 Object 类型对象。
	 * @param inValue 待转换字符串
	 * @return 返回一个 Object 类型对象
	 */
	public static Object toJavaLangObject(String inValue) {
		return inValue;
	}
	
	/**
	 * 把字符串参数，转换为 对应的枚举类型对象。枚举类内部的值，需要为大写
	 * @param <T> 要转换为的枚举类型
	 * @param inValue 待转换字符串。如果为空，返回结果为null
	 * @param enumClass 要转换为的枚举类型
	 * @return 返回一个 枚举类型对象
	 * @throws IllegalArgumentException 参数异常，无法转换时，抛出此异常
	 * @throws Exception 一些可能的其它异常
	 */
	public static <T extends Enum<T>> T toEnumObject(String inValue, Class<T> enumClass) throws IllegalArgumentException, Exception {
		
		// 空入参，则返回 null
		if(StrUtil.isEmptyString(inValue)) return null;
		
		// 如果不是枚举类型，抛出异常
		if(!enumClass.isEnum()) {
			String temp = "the parameter enumType=%s is not a enum class.";
			throw new IllegalArgumentException(Stdout.fplToAnyWhere(temp, enumClass));
		}
		
		// 正式处理
		T result = null;
		try {
			// 这里会把字符串统一设置为大写
			result = Enum.valueOf(enumClass, inValue.trim().toUpperCase());
		} catch (Exception e) {
			String temp = "there was something wrong when converting the inValue=%s. enumClass=%s. msg=%s.";
			throw new IllegalArgumentException(Stdout.fplToAnyWhere(temp, inValue, enumClass, e));
		}
		
		// 返回结果
		return result;
	}
}
