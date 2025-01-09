/******************************************************************************************************

This file "TimeUtil.java" is part of project "niceday" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Date;

/**
 * 时间相关处理的工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2022年12月6日-2025年1月9日
 */
public final class TimeUtil {
	
	/**
	 * 私有构造方法，防止创建对象
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
	 * 一个固定日期格式的转换对象。它所能处理的字符串格式如下：2020-1-1、2021-12-31、2021/1/1、2021/12/31
	 */
	public static final DateTimeFormatter FMT_DATE_NORMAL ;
    static {
    	FMT_DATE_NORMAL = new DateTimeFormatterBuilder()
    			   .optionalStart()
				   .append(DateTimeFormatter.ofPattern("yyyy-M-d"))
				   .optionalEnd()
				   .optionalStart()
				   .append(DateTimeFormatter.ofPattern("yyyy/M/d"))
				   .optionalEnd()
				   .toFormatter();
    }
    
    /**
     * 一个固定日期格式的转换对象。它所能处理的字符串格式如下：2021、202101、20210131
     */
    public static final DateTimeFormatter FMT_DATE_WTSPLIT ;
    static {
    	FMT_DATE_WTSPLIT = new DateTimeFormatterBuilder()
    			 .appendPattern("[yyyy][yyyyMM][yyyyMMdd]")
				 .optionalStart()
                 .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                 .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                 .optionalEnd()
                 .toFormatter();
    }
    
    /**
     * 这是一个我自己定义的 时分秒 信息转换器。用于兼容 1位 时分秒的情况：比如：8时2分3秒 这种情况
     */
    public static final DateTimeFormatter FMT_MY_TIME ;
    static {
    	FMT_MY_TIME = new DateTimeFormatterBuilder()
    			.appendValue(HOUR_OF_DAY, 1, 2, SignStyle.NEVER)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 1, 2, SignStyle.NEVER)
                .optionalStart()
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 1, 2, SignStyle.NEVER)
                .optionalStart()
                .appendFraction(NANO_OF_SECOND, 0, 9, true)
                .toFormatter();
    }
    
    /**
     * 一个固定时间时间格式的转换对象。它能处理的字符串格式为：年、月、日 拼接 时、分、秒。中间的间隔， 可以为' '空格，也可以为'T'字符，也可以没有
     */
    public static final DateTimeFormatter FMT_DATETIME_NORMAL ;
    static {
    	FMT_DATETIME_NORMAL = new DateTimeFormatterBuilder()
    			.append(FMT_DATE_NORMAL)
    			.optionalStart()
    			.appendLiteral('T')
    			.optionalEnd()
    			.optionalStart()
    			.appendLiteral(' ')
				.optionalEnd()
				.append(FMT_MY_TIME)
				.toFormatter();
    }
	
	/**
	 * 按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒)，格式化当前时间
	 * @return 格式化后的时间字符串
	 */
	public static String getDefaultValue() {
		return TimeUtil.DEFAULT.format(new Date());
	}
	
	/**
	 * 按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒)，格式化传入的时间对象
	 * @param date 传入的时间对象
	 * @return 格式化后的时间字符串
	 */
	public static String getDefaultValue(Date date) {
		return TimeUtil.DEFAULT.format(date);
	}
	
	/**
	 * 按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒)，格式化传入的长整形毫秒时间对象
	 * @param timeMillis 传入的长整形毫秒时间对象
	 * @return 格式化后的时间字符串
	 */
	public static String getDefaultValue(long timeMillis) {
		return TimeUtil.DEFAULT.format(new Date(timeMillis));
	}
	
	/**
	 * 按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒.3位毫秒)，格式化当前时间
	 * @return 格式化后的时间字符串
	 */
	public static String getDefaultValueWithMiliseconds() {
		return TimeUtil.DEFAULT_WITH_MILISECOND.format(new Date());
	}
	
	/**
	 * 按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒.3位毫秒)，格式化传入的时间对象
	 * @param date 传入的时间对象
	 * @return 格式化后的时间字符串
	 */
	public static String getDefaultValueWithMiliseconds(Date date) {
		return TimeUtil.DEFAULT_WITH_MILISECOND.format(date);
	}
	
	/**
	 * 按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒.3位毫秒)，格式化传入的长整形毫秒时间对象
	 * @param timeMillis 需要转换的毫秒时间参数
	 * @return 格式化后的时间字符串
	 */
	public static String getDefaultValueWithMiliseconds(long timeMillis) {
		return TimeUtil.DEFAULT_WITH_MILISECOND.format(new Date(timeMillis));
	}
	
	/**
	 * 按照传入的字符串格式，格式化时间对象，并返回。
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
	 * 按照传入的字符串格式，格式化长整形毫秒时间对象，并返回。
	 * @param timeMillis 待格式化的长整形毫秒时间对象
	 * @param formatStr 格式化字符串
	 * @return 格式化后的时间字符串
	 */
	public static String getCustomValue(long timeMillis, String formatStr) {
		return getCustomValue(new Date(timeMillis), formatStr);
	}
	
	/**
	 * 一个简单的自定义时间格式转换函数。将 LocalDateTime 类型的时间对象，转换为字符串。
	 * @param time 待转换时间对象
	 * @param formatStr 格式化字符串, 比如：yyyy-MM-dd HH:mm:ss.SSS <strong>如果需要展示纳秒，写 .SSSSSS</strong>
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
	 * 将 LocalDateTime 对象转为 Timestamp 对象
	 * @param time LocalDateTime 类型的时间对象
	 * @param zone 地域对象
	 * @return Timestamp 对象
	 */
	public static Timestamp getTimestampByLocalDateTime(LocalDateTime time, ZoneId zone) {
		return new Timestamp(time.atZone(zone).toInstant().toEpochMilli());
	}
	
	/**
	 * 将 LocalDateTime 对象转为 Timestamp 对象，使用系统默认的地域对象信息
	 * @param time LocalDateTime 类型的时间对象
	 * @return Timestamp 对象
	 */
	public static Timestamp getTimestampByLocalDateTime(LocalDateTime time) {
		return Timestamp.valueOf(time);
	}
	
	/**
	 * 将指定格式的字符串，转换为 LocalDate 对象。如果转换失败，会抛出异常。
	 * @param dateString 指定格式的日期字符串
	 * @param formatter 转换器对象，TimeUtil 工具类，有提供默认的 FMT 对象
	 * @return LocalDate 对象
	 * @throws DateTimeParseException 如果转换失败，会抛出异常。
	 */
	public static LocalDate parseLocaDate(String dateString, DateTimeFormatter formatter) throws DateTimeParseException {
		return LocalDate.parse(dateString, formatter);
	}
	
	/**
	 * 将指定格式的字符串，转换为 LocalDateTime 对象。如果转换失败，会抛出异常。
	 * @param dateTimeString 指定格式的日期时间字符串
	 * @param formatter 转换器对象，TimeUtil 工具类，有提供默认的 FMT 对象
	 * @return LocalDateTime 对象
	 * @throws DateTimeParseException 如果转换失败，会抛出异常。
	 */
	public static LocalDateTime parseLocalDateTime(String dateTimeString, DateTimeFormatter formatter) throws DateTimeParseException {
		return LocalDateTime.parse(dateTimeString, formatter);
	}
	
	/*
	public static void main(String[] args) {
		//无分隔符
		Stdout.pl("===========  日期转换，无分隔符  =============");
		Stdout.pl(TimeUtil.parseLocaDate("2020", FMT_DATE_WTSPLIT));
		Stdout.pl(TimeUtil.parseLocaDate("202011", FMT_DATE_WTSPLIT));
		Stdout.pl(TimeUtil.parseLocaDate("20201205", FMT_DATE_WTSPLIT));
		//有分隔符
		Stdout.pl("===========  日期转换，有分隔符  =============");
		Stdout.pl(TimeUtil.parseLocaDate("2020-1-1", FMT_DATE_NORMAL));
		Stdout.pl(TimeUtil.parseLocaDate("2020-8-1", FMT_DATE_NORMAL));
		Stdout.pl(TimeUtil.parseLocaDate("2020-8-21", FMT_DATE_NORMAL));
		Stdout.pl(TimeUtil.parseLocaDate("2020/1/1", FMT_DATE_NORMAL));
		Stdout.pl(TimeUtil.parseLocaDate("2020/8/1", FMT_DATE_NORMAL));
		Stdout.pl(TimeUtil.parseLocaDate("2020/8/21", FMT_DATE_NORMAL));
		//时间转换
		Stdout.pl("===========  时间转换，1  =============");
		Stdout.pl(TimeUtil.parseLocalDateTime("2020-1-1 02:03:04", FMT_DATETIME_NORMAL));
		Stdout.pl(TimeUtil.parseLocalDateTime("2020-12-13 02:03", FMT_DATETIME_NORMAL));
		Stdout.pl(TimeUtil.parseLocalDateTime("2020-12-13 02:03:04", FMT_DATETIME_NORMAL));
		Stdout.pl(TimeUtil.parseLocalDateTime("2020-1-1T02:03:04", FMT_DATETIME_NORMAL));
		Stdout.pl(TimeUtil.parseLocalDateTime("2020-12-13T02:03", FMT_DATETIME_NORMAL));
		Stdout.pl(TimeUtil.parseLocalDateTime("2020-12-13T02:03:04", FMT_DATETIME_NORMAL));
		//
		Stdout.pl("===========  时间转换，2 时分秒为一个数字的情况 =============");
		Stdout.pl(TimeUtil.parseLocalDateTime("2020/1/1 2:3:4", FMT_DATETIME_NORMAL));
		Stdout.pl(TimeUtil.parseLocalDateTime("2020/12/13 2:3", FMT_DATETIME_NORMAL));
		Stdout.pl(TimeUtil.parseLocalDateTime("2020/12/13 2:3:4", FMT_DATETIME_NORMAL));
		Stdout.pl(TimeUtil.parseLocalDateTime("2020/1/1T2:3:4", FMT_DATETIME_NORMAL));
		Stdout.pl(TimeUtil.parseLocalDateTime("2020/12/13T2:3", FMT_DATETIME_NORMAL));
		Stdout.pl(TimeUtil.parseLocalDateTime("2020/12/13T2:3:4", FMT_DATETIME_NORMAL));
	}
	*/
	
}
