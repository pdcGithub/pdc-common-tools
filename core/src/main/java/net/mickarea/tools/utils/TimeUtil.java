/******************************************************************************************************

This file "TimeUtil.java" is part of project "niceday" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

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
 * 时间相关处理的工具类。
 * <p>关于秒的换算关系，参考如下：
 * 1秒(s) = 1000 毫秒(ms) = 1,000,000 微秒(μs) = 1,000,000,000 纳秒(ns) = 1,000,000,000,000 皮秒(ps)
 * </p>
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2022年12月6日-2026年1月27日
 */
public final class TimeUtil {
	
	/**
	 * 私有构造方法，防止创建对象
	 */
	private TimeUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * （这已经淘汰，它不是线程安全的）固定的时间格式化，4位年份-2位月份-2位日期 24小时:2位分钟:2位秒 这样的格式
	 */
	@Deprecated
	public static final SimpleDateFormat DEFAULT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 固定的时间格式化，4位年份-2位月份-2位日期 24小时:2位分钟:2位秒 这样的格式
	 */
	public static final DateTimeFormatter FMT_DEFAULT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * （这已经淘汰，它不是线程安全的）固定的时间格式化，4位年份-2位月份-2位日期 24小时:2位分钟:2位秒.3位毫秒 这样的格式
	 */
	@Deprecated
	public static final SimpleDateFormat DEFAULT_WITH_MILISECOND = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	/**
	 * 固定的时间格式化，4位年份-2位月份-2位日期 24小时:2位分钟:2位秒.3位毫秒 这样的格式
	 */
	public static final DateTimeFormatter FMT_DEFAULT_MILISECOND = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	
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
     * 获取操作系统中，默认的时区信息，并返回一个对象
     * @return 如果获取成功，返回 ZoneId。否则，返回 null。
     */
    public static ZoneId getDefaultZoneId() {
    	ZoneId zId = null;
    	try {
    		zId = ZoneId.systemDefault();
    	}catch(Exception e) {
    		Stdout.pl(e);
    	}
    	return zId;
    }
    
	/**
	 * 按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒)，格式化当前时间。
	 * 时区信息会按照当前系统的默认时区转换（比如：北京时间是 UTC+8:00）
	 * @return 格式化后的时间字符串。参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	public static String getDefaultValue() {
		return getDefaultValue(new Date());
	}
	
	/**
	 * 按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒)，格式化传入的时间对象。
	 * 时区信息会按照当前系统的默认时区转换（比如：北京时间是 UTC+8:00）
	 * @param date 传入的时间对象。
	 * @return 格式化后的时间字符串。参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	public static String getDefaultValue(Date date) {
		// 如果传入的 date 是null值，他就返回一个 null 值。
		if(date==null) return null;
		try {
			// 否则进行默认的转换
			return date.toInstant().atZone(getDefaultZoneId()).format(TimeUtil.FMT_DEFAULT);
		} catch (Exception e) {
			Stdout.pl(e);
			return null;
		}
	}
	
	/**
	 * 按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒)，格式化传入的长整形毫秒时间对象。
	 * 时区信息会按照当前系统的默认时区转换（比如：北京时间是 UTC+8:00）
	 * @param timeMillis 传入的长整形毫秒时间对象
	 * @return 格式化后的时间字符串。参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	public static String getDefaultValue(long timeMillis) {
		return getDefaultValue(new Date(timeMillis));
	}
	
	/**
	 * 按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒.3位毫秒)，格式化当前时间。
	 * 时区信息会按照当前系统的默认时区转换（比如：北京时间是 UTC+8:00）
	 * @return 格式化后的时间字符串。参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	public static String getDefaultValueWithMiliseconds() {
		return getDefaultValueWithMiliseconds(new Date());
	}
	
	/**
	 * 按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒.3位毫秒)，格式化传入的时间对象。
	 * 时区信息会按照当前系统的默认时区转换（比如：北京时间是 UTC+8:00）
	 * @param date 传入的时间对象
	 * @return 格式化后的时间字符串。参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	public static String getDefaultValueWithMiliseconds(Date date) {
		// 如果传入的 date 是null值，他就返回一个 null 值。
		if(date==null) return null;
		// 否则进行默认的转换
		try {
			return date.toInstant().atZone(getDefaultZoneId()).format(FMT_DEFAULT_MILISECOND);
		} catch (Exception e) {
			Stdout.pl(e);
			return null;
		}
	}
	
	/**
	 * 按照默认格式的时间字符串(4位年份-2位月份-2位日期 24小时:2位分钟:2位秒.3位毫秒)，格式化传入的长整形毫秒时间对象。
	 * 时区信息会按照当前系统的默认时区转换（比如：北京时间是 UTC+8:00）
	 * @param timeMillis 需要转换的毫秒时间参数
	 * @return 格式化后的时间字符串。参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	public static String getDefaultValueWithMiliseconds(long timeMillis) {
		return getDefaultValueWithMiliseconds(new Date(timeMillis));
	}
	
	/**
	 * （这个方法已经淘汰，建议使用 DateTimeFormatter 来格式化处理）按照传入的字符串格式，格式化时间对象，并返回。
	 * @param date 待格式化的时间对象
	 * @param formatStr 格式化字符串。它的编写语法参考 SimpleDateFormat
	 * @return 格式化后的时间字符串。参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	@Deprecated
	public static String getCustomValue(Date date, String formatStr) {
		String result = null;
		try {
			//先创建格式化字符串对象，如果日期格式填错，创建会报错
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			//date 为 null 也可能报错
			result = sdf.format(date);
			// 如果 result 不是 null，但是和 formatStr 一样，那就是格式字符串有问题。
			if(formatStr!=null && formatStr.equals(result)) throw new Exception("the parameter formatStr is wrong.");
		}catch(Exception e) {
			Stdout.fpl("日期格式化函数(%s)，执行出错(%s)，传入的参数(date=%s)，(formatStr=%s)", 
					   "TimeUtil.getCustomValue", e.getMessage(), date, formatStr);
			Stdout.pl(e);
			// 因为上面可能已经赋值过，所以重置一次。
			result = null;
		}
		return result;
	}
	
	/**
	 * 按照传入的字符串格式对象，对时间对象进行格式化，并返回字符串结果。
	 * @param date 待处理的时间对象
	 * @param myFmt 用于表示格式化处理的对象。
	 * 它可以通过 DateTimeFormatterBuilder 创建，也可以通过 DateTimeFormatter.ofPattern 以字符串方式创建
	 * @return 如果参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	public static String getCustomValue(Date date, DateTimeFormatter myFmt) {
		// 定义一个返回结果
		String result = null;
		
		// 参数校验
		if(date==null || myFmt==null) {
			Stdout.mylogger.debug("the input parameter date={} or myFmt={} is invalid.", date, myFmt);
			return result;
		}
		
		// 开始转换
		try {
			result = date.toInstant().atZone(getDefaultZoneId()).format(myFmt);
		} catch (Exception e) {
			// 在转换过程中异常，需要记录
			Stdout.pl(e);
		}
		
		// 返回
		return result;
	}
	
	/**
	 * （这个方法已经淘汰，建议使用 DateTimeFormatter 来格式化处理）按照传入的字符串格式，格式化长整形毫秒时间对象，并返回。
	 * @param timeMillis 待格式化的长整形毫秒时间对象
	 * @param formatStr 格式化字符串。它的编写语法参考 SimpleDateFormat
	 * @return 格式化后的时间字符串。参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	@Deprecated
	public static String getCustomValue(long timeMillis, String formatStr) {
		return getCustomValue(new Date(timeMillis), formatStr);
	}
	
	/**
	 * 按照传入的字符串格式对象，对long类型的时间戳信息进行格式化，并返回字符串结果。
	 * @param timeMillis 待处理的时间戳信息
	 * @param myFmt 用于表示格式化处理的对象。
	 * 它可以通过 DateTimeFormatterBuilder 创建，也可以通过 DateTimeFormatter.ofPattern 以字符串方式创建
	 * @return 如果参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	public static String getCustomValue(long timeMillis, DateTimeFormatter myFmt) {
		return getCustomValue(new Date(timeMillis), myFmt);
	}
	
	/**
	 * 一个简单的自定义时间格式转换函数。将 LocalDateTime 类型的时间对象，转换为字符串。
	 * @param time 待转换时间对象
	 * @param formatStr 格式化字符串。它使用的是 DateTimeFormatter 的语法。比如：yyyy-MM-dd HH:mm:ss.SSS <strong>如果需要展示微秒，写 .SSSSSS</strong>
	 * @param defaultStr 默认值。如果格式化失败，则按照默认值返回
	 * @return 转换后的字符串。参数校验通过，且执行成功，返回有效的值，否则返回 defaultStr 参数对应的值。
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
	 * 将 LocalDateTime 对象转为 Timestamp 对象。由于LocalDateTime是没有时区的，所以需要指定时区，然后再换算为时间戳才准确。
	 * @param time LocalDateTime 类型的时间对象
	 * @param zone 地域对象。关于 time 所在地区的地域信息。如果你是中国北京时间，应该是 ZoneId.of("UTC+8")
	 * @return 如果参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	public static Timestamp getTimestampByLocalDateTime(LocalDateTime time, ZoneId zone) {
		
		// 定义一个返回结果
		Timestamp result = null;
		
		// 参数校验
		if(time==null || zone==null) {
			Stdout.mylogger.debug("the input parameter time={} or zone={} is invalid.", time, zone);
			return result;
		}
		
		// 转换
		try {
			// 先将 time 绑定时区，再提取 Instant 对象，然后提取毫秒信息。
			result = new Timestamp(time.atZone(zone).toInstant().toEpochMilli());
		}catch(Exception e) {
			// 在转换过程中异常，需要记录
			Stdout.pl(e);
		}
		
		// 返回
		return result;
	}
	
	/**
	 * 将 LocalDateTime 对象转为 Timestamp 对象，使用系统默认的地域对象信息
	 * @param time LocalDateTime 类型的时间对象
	 * @return 如果参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	public static Timestamp getTimestampByLocalDateTime(LocalDateTime time) {
		try {
			return getTimestampByLocalDateTime(time, getDefaultZoneId());
		} catch (Exception e) {
			Stdout.pl(e);
			return null;
		}
	}
	
	/**
	 * 将指定格式的字符串，转换为 LocalDate 对象。如果转换失败，会抛出异常。
	 * @param dateString 指定格式的日期字符串
	 * @param formatter 用于表示格式化处理的对象。
	 * 它可以通过 DateTimeFormatterBuilder 创建，也可以通过 DateTimeFormatter.ofPattern 以字符串方式创建
	 * @return LocalDate 对象
	 * @throws DateTimeParseException 如果转换失败，会抛出异常。
	 */
	public static LocalDate parseLocalDate(String dateString, DateTimeFormatter formatter) throws DateTimeParseException {
		try {
			return LocalDate.parse(dateString, formatter);
		} catch(NullPointerException npe) {
			// 因为我们需要的是 转换异常，而不是 NullPointerException ，所以转换异常的类型
			String exMsg = npe.getMessage();
			if("formatter".equalsIgnoreCase(exMsg)) {
				throw new DateTimeParseException("formatter is null", "", 0);
			}else if("text".equalsIgnoreCase(exMsg)){
				throw new DateTimeParseException("dateString is null", "", 0);
			}else {
				throw new DateTimeParseException("unkown param is null", "", 0);
			}
		} catch (DateTimeParseException dtpe) {
			throw dtpe;
		} catch (Exception e) {
			throw new DateTimeParseException("unkown exception", "", 0);
		}
	}
	
	/**
	 * 将指定格式的字符串，转换为 LocalDate 对象。注意：这里不会抛出异常，如果有问题，则返回一个null。
	 * @param dateString 指定格式的日期字符串
	 * @param formatter 用于表示格式化处理的对象。
	 * 它可以通过 DateTimeFormatterBuilder 创建，也可以通过 DateTimeFormatter.ofPattern 以字符串方式创建
	 * @return LocalDate 对象。参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	public static LocalDate parseLocalDateWithoutExpt(String dateString, DateTimeFormatter formatter) {
		// 定义一个返回结果
		LocalDate result = null;
		// 执行
		try {
			result = parseLocalDate(dateString, formatter);
		}catch(Exception e) {
			// 打印异常信息
			Stdout.pl(e);
		}
		// 返回结果
		return result;
	}
	
	/**
	 * 将指定格式的字符串，转换为 LocalDateTime 对象。如果转换失败，会抛出异常。
	 * @param dateTimeString 指定格式的日期时间字符串
	 * @param formatter 用于表示格式化处理的对象。
	 * 它可以通过 DateTimeFormatterBuilder 创建，也可以通过 DateTimeFormatter.ofPattern 以字符串方式创建
	 * @return LocalDateTime 对象
	 * @throws DateTimeParseException 如果转换失败，会抛出异常。
	 */
	public static LocalDateTime parseLocalDateTime(String dateTimeString, DateTimeFormatter formatter) throws DateTimeParseException {
		try {
			return LocalDateTime.parse(dateTimeString, formatter);
		} catch(NullPointerException npe) {
			// 因为我们需要的是 转换异常，而不是 NullPointerException ，所以转换异常的类型
			String exMsg = npe.getMessage();
			if("formatter".equalsIgnoreCase(exMsg)) {
				throw new DateTimeParseException("formatter is null", "", 0);
			}else if("text".equalsIgnoreCase(exMsg)){
				throw new DateTimeParseException("dateTimeString is null", "", 0);
			}else {
				throw new DateTimeParseException("unkown param is null", "", 0);
			}
		} catch (DateTimeParseException dtpe) {
			throw dtpe;
		} catch (Exception e) {
			throw new DateTimeParseException("unkown exception", "", 0);
		}
	}
	
	/**
	 * 将指定格式的字符串，转换为 LocalDateTime 对象。注意：这里不会抛出异常，如果有问题，则返回一个null。
	 * @param dateTimeString 指定格式的日期时间字符串
	 * @param formatter 用于表示格式化处理的对象。
	 * 它可以通过 DateTimeFormatterBuilder 创建，也可以通过 DateTimeFormatter.ofPattern 以字符串方式创建
	 * @return LocalDateTime 对象。参数校验通过，且执行成功，返回有效的值，否则返回 null。
	 */
	public static LocalDateTime parseLocalDateTimeWithoutExpt(String dateTimeString, DateTimeFormatter formatter) {
		// 定义一个返回结果
		LocalDateTime result = null;
		// 执行
		try {
			result = parseLocalDateTime(dateTimeString, formatter);
		}catch(Exception e) {
			// 打印异常信息
			Stdout.pl(e);
		}
		// 返回结果
		return result;
	}
	
}
