/******************************************************************************************************

This file "MyVirtualEntity.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
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
 * &gt;&gt;&nbsp;<p>一个注解，用于标记一种虚拟的数据库到java类的映射。</p>
 * <p>比如：当需要把一些临时性的sql语句查询结果，映射到一个对象时，就可以用这个注解</p>
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月15日
 */
public @interface MyVirtualEntity {

}
