/******************************************************************************************************

This file "AnnotationUtil.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

/**
 * 这是一个关于注解使用的工具类，它关注于注解的相关处理
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年1月28日
 */
public final class AnnotationUtil {

	/**
	 * 私有构造函数
	 */
	private AnnotationUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 判断，当前这个传入的可注解对象，是否被指定不重复注解中的至少 atLeast 个所标记。这个方法对于入参异常只会返回 false，因为抛异常代码处理太麻烦。
	 * @param targetObj 这是一个待检测的对象。它应该是一个可注解对象。比如：类(Class)、方法(Method)、属性(Field) 等等。
	 * @param annotaions 这是一个注解类列表。它里面的元素都是注解类。请不要重复填写，因为会先将列表元素去重，再检测。如果只填入多个重复的注解，相当于只填一个。
	 * @param atLeast 至少被标记的个数。它应该大于等于 1
	 * @return <p>1、待检测对象为 null，返回 false；注解类列表为空或者 null，返回 false；至少标记个数 atLeast 小于1 或者大于注解类数量，返回 false</p>
	 * <p>2、待检测对象至少被 atLeast 个指定的不重复注解标记，返回true</p>
	 */
	public static final boolean isAnnotationPresentBy(AnnotatedElement targetObj, List<Class<? extends Annotation>> annotaions, int atLeast) {
		
		// 先定义一个返回值
		boolean re = false;
		
		// 入参检测，如果参数异常，直接返回 false；
		if(targetObj==null || ListUtil.isEmptyList(annotaions)) return re;
		// 将列表去重，然后才判断列表长度
		List<Class<? extends Annotation>> tmpList = annotaions.stream().distinct().collect(Collectors.toList());
		// atLeast 小于1 或者大于注解类数量，返回 false
		if(atLeast<1 || atLeast>tmpList.size()) return re;
		
		// 开始遍历整个列表（一般来说，这里的注解列表长度，只有几个。所以，性能优化空间不大）
		re  = tmpList.stream()
				.map(cls->targetObj.isAnnotationPresent(cls)) // 判断待检测对象是否有指定的注解
				.filter(val->val==true)                       // 过滤，只要为 true 的值
				.collect(Collectors.toList())                 // 收集流数据为一个列表
				.size() >= atLeast;                           // 被注解数量，是否大于等于 atLeast
		
		// 返回 结果
		return re;
	}
	
	/**
	 * 判断，当前这个传入的可注解对象，是否被指定不重复注解中的至少 1 个所标记。这个方法对于入参异常只会返回 false，因为抛异常代码处理太麻烦。
	 * @param targetObj 这是一个待检测的对象。它应该是一个可注解对象。比如：类(Class)、方法(Method)、属性(Field) 等等。
	 * @param annotaions 这是一个注解类列表。它里面的元素都是注解类。请不要重复填写，因为会先将列表元素去重，再检测。如果只填入多个重复的注解，相当于只填一个。
	 * @return <p>1、待检测对象为 null，返回 false；注解类列表为空或者 null，返回 false；</p>
	 * <p>2、待检测对象至少被 1 个指定的不重复注解标记，返回true</p>
	 */
	public static final boolean isAnnotationPresentByOne(AnnotatedElement targetObj, List<Class<? extends Annotation>> annotaions) {
		
		// 是否至少被1个注解类所标记
		return isAnnotationPresentBy(targetObj, annotaions, 1);
	}
	
	/**
	 * 判断，当前这个传入的可注解对象，是否被指定不重复注解中的 全部 所标记。这个方法对于入参异常只会返回 false，因为抛异常代码处理太麻烦。
	 * @param targetObj 这是一个待检测的对象。它应该是一个可注解对象。比如：类(Class)、方法(Method)、属性(Field) 等等。
	 * @param annotaions 这是一个注解类列表。它里面的元素都是注解类。请不要重复填写，因为会先将列表元素去重，再检测。如果只填入多个重复的注解，相当于只填一个。
	 * @return <p>1、待检测对象为 null，返回 false；注解类列表为空或者 null，返回 false；</p>
	 * <p>2、待检测对象被 全部 指定的不重复注解标记，返回true</p>
	 */
	public static final boolean isAnnotationPresentByAll(AnnotatedElement targetObj, List<Class<? extends Annotation>> annotaions) {
		
		// 先定义一个返回值
		boolean re = false;
		
		// 注解列表，不能为空
		if(ListUtil.isEmptyList(annotaions)) return re;
		// 这里增加一些判断，因为不能直接传入，我们需要获取到底有多少个不重复的注解
		// 将列表去重，然后才判断列表长度
		List<Class<? extends Annotation>> tmpList = annotaions.stream().distinct().collect(Collectors.toList());
		
		// 这里才调用通用方法(长度，是根据列表的非重复注解类数量来确定)
		re = isAnnotationPresentBy(targetObj, annotaions, tmpList.size());
		
		// 返回 结果
		return re;
	}
	
	/**
	 * 根据所属的 包路径 和 注解类，搜索在这个包下面的类中，有哪些类被这个注解标记，并返回这些类
	 * @param pkg 包路径字符串
	 * @param annotation 注解类
	 * @return 如果参数正常，执行也正常，则返回一个类的集合（大小可能为0）。否则，返回 null。
	 */
	public static final Set<Class<?>> getClassesByAnnotaion(String pkg, Class<? extends Annotation> annotation) {
		
		// 定义返回结果
		Set<Class<?>> result = null;
		
		// 参数校验
		if(StrUtil.isEmptyString(pkg) || annotation==null) {
			Stdout.mylogger.debug("参数校验异常，请检查接收的参数。pkg={}, annotation={}", pkg, annotation);
			return result;
		}
		
		// 开始执行
		Reflections flect = new Reflections(pkg);
		result = flect.getTypesAnnotatedWith(annotation);
		
		// 返回结果
		return result;
	}
}
