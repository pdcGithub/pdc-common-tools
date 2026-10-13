/******************************************************************************************************

This file "MyAutoIncrement.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
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
 * &gt;&gt;&nbsp;一个注解，用于标记java类中，哪个属性对应的字段是自增长的。自增长的字段，在构造insert sql语句时，会忽略掉。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月2日
 */
public @interface MyAutoIncrement {

}
