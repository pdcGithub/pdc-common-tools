/******************************************************************************************************

This file "TableOrView.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
/**
 * &gt;&gt;&nbsp;一个注解，用于标记java类映射到哪个数据库表或者视图
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月23日
 */
public @interface MyTableOrView {

	String name() default "";
	
}
