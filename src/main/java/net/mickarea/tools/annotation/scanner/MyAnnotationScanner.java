/******************************************************************************************************

This file "MyAnnotationScanner.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.annotation.scanner;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.reflections.Reflections;

import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * &gt;&gt;&nbsp;一个自定义的注解扫描器，用于扫描已经被注解的对象
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月5日-2023年6月6日
 */
public class MyAnnotationScanner {

	/**
	 * &gt;&gt;&nbsp;根据所属的 包路径 和 注解名，搜索对应的类
	 * @param pkg 包路径
	 * @param annotation 注解名
	 * @return 一个类的集合
	 */
	public static Set<Class<?>> getClassesByPackageNameAndAnnotaionName(String pkg, Class<? extends Annotation> annotation) {
		Set<Class<?>> result = null;
		Reflections flect = new Reflections(pkg);
		result = flect.getTypesAnnotatedWith(annotation);
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;测试函数
	 * @param args 参数
	 */
	public static void main(String[] args) {
		
		Set<Class<?>> clses = MyAnnotationScanner.getClassesByPackageNameAndAnnotaionName("net.mickarea.tools.dbtest.beans", MyTableOrView.class);
		if(clses!=null) {
			for(Class<?> cls:clses) {
				Stdout.fpl("%s, %s, %s, %s", cls.getName(),cls.getCanonicalName(),cls.getSimpleName(),cls.getTypeName());
			}
		}else {
			Stdout.pl("获取不到内容");
		}
		
	}
	
}
