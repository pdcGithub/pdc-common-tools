/******************************************************************************************************

This file "Test2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.lambdatest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.mickarea.tools.utils.RandomUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * >> 测试 function 套件的四个基本用法
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月16日
 */
public class Test2 {
	
	public static void main(String[] args) throws Exception {
		
		//consumer 接受一个参数，无返回值
		Arrays.asList("Justin", "Monica", "Irene").forEach(Stdout::pl);
		
		//function 接受一个参数，有返回值
		Optional<String> nickOptional = getNickName("tom");
		Stdout.pl(nickOptional.map(String::toUpperCase));
		
		//predicate 接受一个参数，然后返回 boolean
		long countMp3 = Files.list(Paths.get("D:\\music"))
				             .filter(path->path.toString().endsWith("mp3"))
				             .count();
		Stdout.pl("mp3 数量:"+countMp3);
		
		//supplier 不接受参数，有返回值
		Integer[] coins = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
		randomZero(coins, ()->(int)(Math.random()*10));
		Stdout.plArray(coins);
		
		// 另外一个列子
		Stream<String> decimalNumberOfPI = Stream.generate(() -> {
			try {
				return RandomUtil.genLetterString(6);
			} catch (Exception e) {
				return "error";
			}
		});
		decimalNumberOfPI.map(n -> n.toUpperCase())
						 .filter(n-> n.endsWith("A"))
		                 .forEach(Stdout::pl);
		
	}
	
	private static Optional<String> getNickName(String name) {
		Map<String, String> nicknames = new HashMap<String ,String>();
		nicknames.put("tom", "one");
		nicknames.put("jack", "two");
		nicknames.put("jetty", "three");
		String nickName = nicknames.get(name);
		return nickName==null ? Optional.empty() : Optional.of(nickName);
	}
	
	private static void randomZero(Integer[] coins, Supplier<Integer> randomSupplier) {
		coins[randomSupplier.get()] = 0;
	}

}
