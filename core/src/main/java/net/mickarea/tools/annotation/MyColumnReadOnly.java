/******************************************************************************************************

This file "MyColumnReadOnly.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
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
 * <p>这个注解用于标记哪些列是只读的。因为，有些数据库列不能通过insert、update语句 设置值，数据库默认会设置这些值。</p>
 * <p>比如：sql server 的 timestamp类型的列，直接设置值会报错，只能由数据库自己处理。</p>
 * <p>这个注解要区别于：MyAutoIncrement 和 MyColumnIgnore</p>
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月28日
 */
public @interface MyColumnReadOnly {

}
