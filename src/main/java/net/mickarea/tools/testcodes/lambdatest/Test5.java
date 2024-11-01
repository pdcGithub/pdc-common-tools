/******************************************************************************************************

This file "Test5.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.lambdatest;

import java.util.Optional;

import net.mickarea.tools.utils.RandomUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * flatmap 测试 
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月18日-2023年4月19日
 */
public class Test5 {
	
	public static void main(String[] args) {
		
		Stdout.pl(findCompany().orElse(new OptionalCompany()));
		Stdout.pl(findCompany2());
		
		//居于 Optional 的处理
		String city = findCompany().flatMap(OptionalCompany::getAddress)
		                           .flatMap(OptionalAddress::getCity)
		                           .orElse("N.A.City");
		String province =  findCompany().flatMap(OptionalCompany::getAddress)
				                        .flatMap(OptionalAddress::getProvince)
				                        .orElse("N.A.Province");
		Stdout.fpl("省份：%s, 城市：%s", province, city);
		
		//居于普通对象，然后使用 map 处理
		String city2 = Optional.ofNullable(findCompany2())
				               .map(Company::getAddress)
				               .map(Address::getCity)
				               .orElse("N.A.City");
		Stdout.pl(city2);
		
	}
	
	private static Optional<OptionalCompany> findCompany() {
		String num2 = "ERROR001";
		try {
			num2 = RandomUtil.genNumAndLetterMixedString(18);
		} catch (Exception e) {
			Stdout.pl(e);
		}
		OptionalCompany c = new OptionalCompany(num2, "一个神秘的Optional公司", "010-456465724", "中国北京市东城区王府井大街255号");
		return Optional.ofNullable(c);
	}
	
	private static Company findCompany2() {
		String num2 = "ERROR002";
		try {
			num2 = RandomUtil.genNumAndLetterMixedString(18);
		} catch (Exception e) {
			Stdout.pl(e);
		}
		Company c = new Company(num2, "第二个神秘的公司", "020-12545557", "中国广东省广州市越秀区盘福路1号");
		return c;
	}
}
