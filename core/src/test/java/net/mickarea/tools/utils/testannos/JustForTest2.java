/******************************************************************************************************

This file "JustForTest2.java" is part of project "myservice" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.testannos;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({TYPE, FIELD, METHOD})
/**
 * 这是用于测试的一个注解，没有其它用途
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年1月6日
 */
public @interface JustForTest2 {

}
