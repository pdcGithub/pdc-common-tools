/******************************************************************************************************

This file "ThreadTest.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.threads;

import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.TimeUtil;

/**
 * >> 多线程测试类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月2日
 */
public class ThreadTest extends Thread {
	public ThreadTest(String name) {
		this.setName(name);
	}
	@Override
	public void run() {
		for(int i=0;i<5;i++) {
			Stdout.pl("::: "+TimeUtil.getDefaultValueWithMiliseconds()+" ::: "+this.getName()+" running...");
		}
	}
}
