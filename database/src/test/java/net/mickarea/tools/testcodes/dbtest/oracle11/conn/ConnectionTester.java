/******************************************************************************************************

This file "ConnectionTester.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.oracle11.conn;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.Stdout;

/**
 * 测试多数据库配置下，数据库连接池是否稳定运行
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月19日
 */
public class ConnectionTester {

	public static void main(String[] args) {
		
		//数据库连接池管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		
		Stdout.pl(mana);
		
		//关闭连接池
		mana.destroyDataPools();

	}

}
