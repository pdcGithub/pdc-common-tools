/******************************************************************************************************

This file "Test3.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.lambdatest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.mickarea.tools.utils.Stdout;

/**
 * Optional API 测试
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月16日
 */
public class Test3 {

	public static void main(String[] args) {
		
		Stdout.pl(getNickName("ok"));
		
		Stdout.pl(getNickName("ok").orElse("no such name."));
		
		Stdout.pl(getNickName("tom").orElse("no such name."));
		
	}

	private static Optional<String> getNickName(String name) {
		Map<String, String> nicknames = new HashMap<String ,String>();
		nicknames.put("tom", "one");
		nicknames.put("jack", "two");
		nicknames.put("jetty", "three");
		return Optional.ofNullable(nicknames.get(name));
	}
	
}
