/******************************************************************************************************

This file "DBTrans.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.db;

/**
 * &gt;&gt;&nbsp;数据库事务处理接口，用于规范事务处理的业务代码；接口只有一个run函数 作为 程序入口
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月12日
 */
public interface DBTrans {

	/**
	 * &gt;&gt;&nbsp;数据库事务处理的程序入口函数
	 * @return 如果执行出错，则返回字符串内容；否则，返回空字符串。
	 */
	public abstract String run();
	
}
