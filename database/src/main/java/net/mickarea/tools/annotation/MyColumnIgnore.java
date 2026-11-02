/******************************************************************************************************

This file "MyColumnIgnore.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
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
 * &gt;&gt;&nbsp;这个注解用于忽略某些类的属性，因为有些类的属性可能不属于数据库，数据库语句并没有这内容
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月23日
 */
public @interface MyColumnIgnore {

}
