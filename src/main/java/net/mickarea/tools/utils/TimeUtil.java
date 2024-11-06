/******************************************************************************************************

This file "TimeUtil.java" is part of project "niceday" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * &gt;&gt;&nbsp;时间相关处理的工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2022年12月6日-2023年11月8日
 */
public final class TimeUtil {
	
	/**
	 * &gt;&gt;&nbsp;私有构造方法，防止创建对象
	 */
	private TimeUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 固定的时间格式化，4位年份-2位月份-2位日期 24小时:2位分钟:2位秒 这样的格式
	 */
	public static final SimpleDateFormat DEFAULT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 固定的时间格式化，4位年份-2位月份-2位日期 24小时:2位分钟:2位秒.3位毫秒 这样的格式
	 */
	public static final SimpleDateFormat DEFAULT_WITH_MILISECOND = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	/**
	 * &gt;&gt;&nbsp;按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒)，格式化当前时间
	 * @return 格式化后的时间字符串
	 */
	public static String getDefaultValue() {
		return TimeUtil.DEFAULT.format(new Date());
	}
	
	/**
	 * &gt;&gt;&nbsp;按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒)，格式化传入的时间对象
	 * @param date 传入的时间对象
	 * @return 格式化后的时间字符串
	 */
	public static String getDefaultValue(Date date) {
		return TimeUtil.DEFAULT.format(date);
	}
	
	/**
	 * &gt;&gt;&nbsp;按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒)，格式化传入的长整形毫秒时间对象
	 * @param timeMillis 传入的长整形毫秒时间对象
	 * @return 格式化后的时间字符串
	 */
	public static String getDefaultValue(long timeMillis) {
		return TimeUtil.DEFAULT.format(new Date(timeMillis));
	}
	
	/**
	 * &gt;&gt;&nbsp;按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒.3位毫秒)，格式化当前时间
	 * @return 格式化后的时间字符串
	 */
	public static String getDefaultValueWithMiliseconds() {
		return TimeUtil.DEFAULT_WITH_MILISECOND.format(new Date());
	}
	
	/**
	 * &gt;&gt;&nbsp;按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒.3位毫秒)，格式化传入的时间对象
	 * @param date 传入的时间对象
	 * @return 格式化后的时间字符串
	 */
	public static String getDefaultValueWithMiliseconds(Date date) {
		return TimeUtil.DEFAULT_WITH_MILISECOND.format(date);
	}
	
	/**
	 * &gt;&gt;&nbsp;按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒.3位毫秒)，格式化传入的长整形毫秒时间对象
	 * @param timeMillis 需要转换的毫秒时间参数
	 * @return 格式化后的时间字符串
	 */
	public static String getDefaultValueWithMiliseconds(long timeMillis) {
		return TimeUtil.DEFAULT_WITH_MILISECOND.format(new Date(timeMillis));
	}
	
	/**
	 * &gt;&gt;&nbsp;按照传入的字符串格式，格式化时间对象，并返回。
	 * @param date 待格式化的时间对象
	 * @param formatStr 格式化字符串
	 * @return 格式化后的时间字符串
	 */
	public static String getCustomValue(Date date, String formatStr) {
		String result = null;
		try {
			//先创建格式化字符串对象，如果日期格式填错，创建会报错
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			//date 为 null 也可能报错
			result = sdf.format(date);
		}catch(Exception e) {
			Stdout.fpl("日期格式化函数(%s)，执行出错(%s)，传入的参数(date=%s)，(formatStr=%s)", 
					   "TimeUtil.getCustomValue", e.getMessage(), date, formatStr);
			Stdout.pl(e);
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;按照传入的字符串格式，格式化长整形毫秒时间对象，并返回。
	 * @param timeMillis 待格式化的长整形毫秒时间对象
	 * @param formatStr 格式化字符串
	 * @return 格式化后的时间字符串
	 */
	public static String getCustomValue(long timeMillis, String formatStr) {
		return getCustomValue(new Date(timeMillis), formatStr);
	}
	
	/**
	 * &gt;&gt;&nbsp;一个简单的自定义时间格式转换函数。将 LocalDateTime 类型的时间对象，转换为字符串。
	 * @param time 待转换时间对象
	 * @param formatStr 格式化字符串, 比如：yyyy-MM-dd HH:mm:ss.SSS
	 * @param defaultStr 默认值。如果格式化失败，则按照默认值返回
	 * @return 转换后的字符串
	 */
	public static String getCustomValue(LocalDateTime time, String formatStr, String defaultStr) {
		String result = defaultStr;
		try {
			if(time!=null && !StrUtil.isEmptyString(formatStr)) {
				result = time.format(DateTimeFormatter.ofPattern(formatStr));
			}
		}catch(IllegalArgumentException e1) {
			Stdout.pl("时间格式转换失败，格式化字符串非法(formatStr="+formatStr+")，"+e1.getMessage());
			Stdout.pl(e1);
		}catch(DateTimeException e2) {
			Stdout.pl("时间格式转换失败，时间对象异常(time="+time+")，"+e2.getMessage());
			Stdout.pl(e2);
		}catch(Exception e3) {
			Stdout.pl("时间格式转换失败，其它异常，"+e3.getMessage());
			Stdout.pl(e3);
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;将 LocalDateTime 对象转为 Timestamp 对象
	 * @param time LocalDateTime 类型的时间对象
	 * @param zone 地域对象
	 * @return Timestamp 对象
	 */
	public static Timestamp getTimestampByLocalDateTime(LocalDateTime time, ZoneId zone) {
		return new Timestamp(time.atZone(zone).toInstant().toEpochMilli());
	}
	
	/**
	 * &gt;&gt;&nbsp;将 LocalDateTime 对象转为 Timestamp 对象，使用系统默认的地域对象信息
	 * @param time LocalDateTime 类型的时间对象
	 * @return Timestamp 对象
	 */
	public static Timestamp getTimestampByLocalDateTime(LocalDateTime time) {
		return new Timestamp(time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
	}
	/*
	public static void main(String[] args) {
		
		Stdout.pl(TimeUtil.getCustomValue(null, null, null));
		Stdout.pl(TimeUtil.getCustomValue(null, null, "默认值1"));
		Stdout.pl(TimeUtil.getCustomValue(LocalDateTime.now(), null, "默认值2"));
		Stdout.pl(TimeUtil.getCustomValue(LocalDateTime.now(), "", "默认值3"));
		Stdout.pl(TimeUtil.getCustomValue(LocalDateTime.now(), "aaa", "默认值3"));
		
		Stdout.pl(TimeUtil.getCustomValue(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss.SSS", "默认值3"));
	}
	*/
}
