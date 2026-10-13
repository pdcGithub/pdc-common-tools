/******************************************************************************************************

This file "MyIdGroup.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
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
 * &gt;&gt;&nbsp;这个注解用于标记表或者视图的唯一字段组（可以是一个字段，也可以是多个字段）
 * <p>比如：id 是 test_a 表的唯一索引字段，则标记；另外，可以标记多个字段，因为唯一索引不一定是只有一个字段</p>
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月1日
 */
public @interface MyIdGroup {

}
