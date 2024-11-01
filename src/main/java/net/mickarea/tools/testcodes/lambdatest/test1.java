/******************************************************************************************************

This file "test1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.lambdatest;

import java.util.Arrays;

import net.mickarea.tools.utils.Stdout;

/**
 * lambda 表达式 测试
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月13日
 */
public class test1 {
	
	public static void main(String[] args) {
		
		String[] names = {"tom", "jack", "lucy", "marry", "wac", "tim", "jo"};
		
		Stdout.plArray(names);
		
		/* 原本的匿名内部类写法
		Arrays.sort(names, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.length()-o2.length();
			}
		});
		*/
		
		/* 初始的lambda
		Comparator<String> byLength = (String x, String y)->x.length()-y.length();
		Arrays.sort(names, byLength);
		*/
		
		/* 中间的lambda
		Arrays.sort(names, (String x, String y)->x.length()-y.length());
		*/
		
		//最终的lambda
		Arrays.sort(names, (x,y)->x.length()-y.length());
		
		Stdout.plArray(names);
		
		//在forEach中使用 lambda
		Arrays.asList(names).forEach((name)->{
			name = name.toUpperCase();
			name = "name:"+name;
			System.out.println(name);
		});
		
		//如果不处理直接输出
		Arrays.asList(names).forEach(Stdout::pl);
		
	}

}
