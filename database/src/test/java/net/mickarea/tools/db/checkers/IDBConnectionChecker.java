/******************************************************************************************************

This file "IDBConnectionChecker.java" is part of project "database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.db.checkers;

/**
 * 这是一个数据库连接检查器的接口。它主要用于保证连接检查器的接口统一。
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月19日
 */
public interface IDBConnectionChecker {

	/**
	 * 这是检查方法，用于执行数据库连接检查。
	 * @return 如果数据库连接成功，则返回一个 true；否则，返回 false。
	 */
	public boolean check();
}
