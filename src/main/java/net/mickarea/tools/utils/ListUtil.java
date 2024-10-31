/******************************************************************************************************

This file "ListUtil.java" is part of project "niceday" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * &gt;&gt;&nbsp;列表工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2022年11月11日-2023年10月21日
 */
public final class ListUtil {
	
	/**
	 * 私有构造函数
	 */
	private ListUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 把一个枚举对象转化为一个字符串列表
	 * @param input 枚举对象
	 * @return 字符串列表
	 */
	public static List<String> makeEnumerationObjectToStringList(@SuppressWarnings("rawtypes") Enumeration input){
		List<String> output = null;
		if(input!=null) {
			output = new ArrayList<String>();
			for(;input.hasMoreElements();) {
				output.add((String)input.nextElement());
			}
		}
		return output;
	}
	
	/**
	 * &gt;&gt;&nbsp;把一个枚举对象转化为一个列表对象（由于某些对象直接转String会报错，所以只转换载体，不转换类型）
	 * @param input 枚举对象
	 * @return 列表
	 */
	public static <T> List<T> makeEnumerationObjectToListObject(Enumeration<T> input) {
		List<T> output = null;
		if(input!=null) {
			output = new ArrayList<T>();
			for(;input.hasMoreElements();) {
				output.add(input.nextElement());
			}
		}
		return output;
	}
	
	/**
	 * &gt;&gt;&nbsp;判断一个 List 类型的数据是否为空
	 * @param list 待判断的 List 类型对象
	 * @return 如果为空，则 true ； 否则 false;
	 */
	public static boolean isEmptyList(List<?> list) {
		return (list==null||list.size()<=0)?true:false;
	}
	
	public static void main(String[] args) {
		
		Stdout.pl(ListUtil.isEmptyList(null));
		
		Stdout.pl(ListUtil.isEmptyList(new ArrayList<Object>()));
		
		List<Object> params = new ArrayList<Object>();
		params.add("123");
		Stdout.pl(ListUtil.isEmptyList(params));
		
	}
	
}
