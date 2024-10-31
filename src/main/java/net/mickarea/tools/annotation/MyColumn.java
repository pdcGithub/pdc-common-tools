/******************************************************************************************************

This file "Column.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
/**
 * &gt;&gt;&nbsp;一个注解，用于标记java类中，属性映射到哪个数据库表或者视图的哪个字段
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月23日-2023年8月5日
 */
public @interface MyColumn {

	/**
	 * &gt;&gt;&nbsp;这个方法用于标记此属性属于哪个数据库字段
	 * @return 数据库字段
	 */
	String name() default "";
	
	/**
	 * &gt;&gt;&nbsp;这个方法用于标记此属性的UI展示名称
	 * @return UI展示名称
	 */
	String displayName() default "";
	
	/**
	 * &gt;&gt;&nbsp;这个方法用于标记此属性的扩展内容。比如：数值的翻译处理关键字
	 * @return 
	 */
	String extProperty() default "";
}
