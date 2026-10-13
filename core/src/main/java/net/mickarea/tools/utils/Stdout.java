/******************************************************************************************************

This file "Stdout.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * &gt;&gt;&nbsp;一个标准的信息输出工具类（在2024年6月，改用logback来处理日志）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年3月19日-2024年6月28日
 */
public final class Stdout {
	
	/**
	 * 私有构造函数
	 */
	private Stdout() {
		// TODO Auto-generated constructor stub
	}
	
	//定义一个日志处理类
	public static final Logger mylogger = LoggerFactory.getLogger("common");
	
	/**
	 * &gt;&gt;&nbsp;标准输出，带换行
	 */
	public static void pl() {
		mylogger.info("");
	}
	
	/**
	 * &gt;&gt;&nbsp;标准输出，带换行
	 * @param pl 要输出的对象
	 */
	public static void pl(Object pl) {
		mylogger.info(pl==null?null:pl.toString());
	}
	
	/**
	 * &gt;&gt;&nbsp;打印报错的异常信息详情
	 * @param e 异常对象
	 */
	public static void pl(Exception e) {
		mylogger.error("当前异常信息为："+e, e);
	}
	
	/**
	 * &gt;&gt;&nbsp;格式化输出，带换行
	 * @param formatStr 格式化字符串
	 * @param objects 输出对象参数
	 */
	public static void fpl(String formatStr, Object...objects ) {
		if(formatStr==null) {
			pl(new Exception("fpl 函数的格式化字符串为 null，请检查..."));
		}else {
			mylogger.info(fplToAnyWhere(formatStr, objects));
		}
	}
	
	/**
	 * &gt;&gt;&nbsp;获取格式化后的文本内容
	 * @param formatStr 格式化字符串
	 * @param objects 输出对象参数
	 * @return 返回一个字符串
	 */
	public static String fplToAnyWhere(String formatStr, Object...objects ) {
		if(formatStr==null) {
			pl(new Exception("fplToAnyWhere 函数的格式化字符串为 null，请检查..."));
			return null;
		}else {
			return String.format(formatStr, objects);
		}
	}
	
	/**
	 * 打印整形数组
	 * @param arr 要处理的数组
	 */
	public static void plArray(int[] arr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("数组：[");
		if(arr!=null) {
			for(int i=0;i<arr.length;i++) {
				buffer.append(arr[i]);
				if(i<arr.length-1) {
					buffer.append(", ");
				}
			}
		}else {
			buffer.append("error：数组为 null ");
		}
		buffer.append("]");
		//打印日志信息
		mylogger.info(buffer.toString());
		//清空 buffer 信息
		buffer.setLength(0);
		buffer = null;
	}
	
	/**
	 * &gt;&gt;&nbsp;打印字节数组
	 * @param arr 一个字节数组
	 */
	public static void plArray(byte[] arr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("数组：[");
		if(arr!=null) {
			for(int i=0;i<arr.length;i++) {
				buffer.append(arr[i]);
				if(i<arr.length-1) {
					buffer.append(", ");
				}
			}
		}else {
			buffer.append("error：数组为 null ");
		}
		buffer.append("]");
		//打印日志信息
		mylogger.info(buffer.toString());
		//清空 buffer 信息
		buffer.setLength(0);
		buffer = null;
	}
	
	/**
	 * 打印字符数组
	 * @param arr 要处理的数组
	 */
	public static void plArray(char[] arr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("数组：[");
		if(arr!=null) {
			for(int i=0;i<arr.length;i++) {
				buffer.append(arr[i]);
				if(i<arr.length-1) {
					buffer.append(", ");
				}
			}
		}else {
			buffer.append("error：数组为 null ");
		}
		buffer.append("]");
		//打印日志信息
		mylogger.info(buffer.toString());
		//清空 buffer 信息
		buffer.setLength(0);
		buffer = null;
	}
	
	/**
	 * &gt;&gt;&nbsp;打印字符数组，根据参数可控制是否直接打印空白字符（比如\n , \t 之类的）。
	 * @param arr 一个字符数组
	 * @param printBlankChar 当为true时，直接输出空白字符；当为false时，以' '代替空白字符输出
	 */
	public static void plArray(char[] arr, boolean printBlankChar) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("数组：[");
		if(arr!=null) {
			for(int i=0;i<arr.length;i++) {
				if(Pattern.matches("[\\s]", String.valueOf(arr[i])) && !printBlankChar) {
					buffer.append(' ');
				}else {
					buffer.append(arr[i]);
				}
				if(i<arr.length-1) {
					buffer.append(", ");
				}
			}
		}else {
			buffer.append("error：数组为 null ");
		}
		buffer.append("]");
		//打印日志信息
		mylogger.info(buffer.toString());
		//清空 buffer 信息
		buffer.setLength(0);
		buffer = null;
	}
	
	/**
	 * 打印字符串数组
	 * @param arr 要处理的数组
	 */
	public static void plArray(String[] arr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("数组：[");
		if(arr!=null) {
			for(int i=0;i<arr.length;i++) {
				buffer.append(arr[i]);
				if(i<arr.length-1) {
					buffer.append(", ");
				}
			}
		}else {
			buffer.append("error：数组为 null ");
		}
		buffer.append("]");
		//打印日志信息
		mylogger.info(buffer.toString());
		//清空 buffer 信息
		buffer.setLength(0);
		buffer = null;
	}
	
	public static void plArray(Object[] oarr) {
		StringBuffer buffer = new StringBuffer();
		String head = "数组：[";
		if(oarr!=null && oarr.length>0 && oarr[0] instanceof StackTraceElement) {
			head = "异常栈信息数组内容如下：[";
		}
		buffer.append(head);
		if(oarr!=null) {
			for(int i=0;i<oarr.length;i++) {
				buffer.append(oarr[i]);
				if(i<oarr.length-1) {
					buffer.append(", ");
				}
			}
		}else {
			buffer.append("error：数组为 null ");
		}
		buffer.append("]");
		//打印日志信息
		mylogger.info(buffer.toString());
		//清空 buffer 信息
		buffer.setLength(0);
		buffer = null;
	}
	
	/**
	 * 记录要执行的 sql 语句 以及 其参数 信息（一般这个信息是不显示的，如果要显示，将 logback 的配置级别，改为 DEBUG）
	 * @param preSql sql 语句
	 * @param params3 参数
	 */
	public static void plExecutedSqlInfo(String preSql, Object params3) {
		String sqlInfo = "将要执行的 sql 是: "+ preSql;
		String paramsInfo = "将要执行的 sql 对应的参数是: "+(params3==null?null:params3.toString());
		mylogger.debug(sqlInfo+"\n"+paramsInfo);
	}
	
	/*
	public static void main(String[] args) {
		Stdout.pl("========= 测试 pl().");
		Stdout.pl();
		Stdout.pl("========= 测试 pl(object).");
		Stdout.pl(null);
		Stdout.pl("测试字符串");
		Stdout.pl(Arrays.asList("al", "a", "b"));
		Stdout.pl(new HashMap<String, String>(){{
			put("name", "tom");
			put("age", "15");
		}});
		Stdout.pl("========= 测试 pl(Exception).");
		Stdout.pl(new Exception("测试异常..."));
		Stdout.pl("========= 测试 fpl(String, Object...).");
		Stdout.fpl(null, null);
		Stdout.fpl("测试。。。这里没有格式化字符", null);
		Stdout.fpl("%s %s \t %s", "这是", 1+2+1, "测试...");
		Stdout.pl("========= 测试 fplToAnyWhere(String, Object...).");
		Stdout.pl(Stdout.fplToAnyWhere(null, null));
		Stdout.fpl(Stdout.fplToAnyWhere("测试。。。这里没有格式化字符222", null));
		Stdout.pl(Stdout.fplToAnyWhere("%s %s \t %s", "这是", 1+2+1, "测试 222 fplToAnyWhere..."));
		Stdout.pl("========= 测试 plArray(int[])");
		int[] a = null;
		Stdout.plArray(a);
		a = new int[] {1,2,3,4};
		Stdout.plArray(a);
		Stdout.pl("========= 测试 plArray(byte[])");
		byte[] a2 = null;
		Stdout.plArray(a2);
		a2 = new byte[] {0x21, 0x2, 0x3, 0x4};
		Stdout.plArray(a2);
		Stdout.pl("========= 测试 plArray(char[])");
		char[] a3 = null;
		Stdout.plArray(a3);
		a3 = new char[] {'a', 'b', 'c', 'd', '\n', '\t'};
		Stdout.plArray(a3);
		Stdout.pl("========= 测试 plArray(char[], boolean)");
		char[] a4 = null;
		Stdout.plArray(a4, false);
		a4 = new char[] {'a', 'b', 'c', 'd', '\n', '\t'};
		Stdout.plArray(a4, false);
		Stdout.plArray(a4, true);
		Stdout.pl("========= 测试 plArray(String[])");
		String[] a5 = null;
		Stdout.plArray(a5);
		a5 = new String[] {"tom", "jack", "link"};
		Stdout.plArray(a5);
		Stdout.pl("========= 测试 plArray(object[])");
		Object[] a6 = null;
		Stdout.plArray(a6);
		a6 = new Object[] {"tom", 1, 0x23, 'a'};
		Stdout.plArray(a6);
		//=======================================
		Stdout.pl("========= 测试 plExecutedSqlInfo");
		Stdout.plExecutedSqlInfo(null, null);
		String preSql = "select * from test_orcl_data_type where seq_no=?";
		List<Object> params = Arrays.asList("1" , 2);
		Stdout.plExecutedSqlInfo(preSql, params);
	}
	*/
	
}
