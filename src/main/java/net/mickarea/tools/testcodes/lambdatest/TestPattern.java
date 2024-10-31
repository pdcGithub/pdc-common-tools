/******************************************************************************************************

This file "TestPattern.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.lambdatest;

import net.mickarea.tools.utils.Stdout;

/**
 * >> 正则测试类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月18日
 */
public class TestPattern {
	
	public static void main(String[] args) {
		
		String name = "中国北京市东城区王府井大街255号";
		
		/*int countryIndex = name.indexOf("国");
		String country = countryIndex<0?"":name.substring(0, countryIndex+1);
		name = name.substring(countryIndex+1);
		Stdout.pl(country);
		
		int provinceIndex = name.indexOf("省");
		String province = provinceIndex<0?"":name.substring(0, provinceIndex+1);
		name = name.substring(provinceIndex+1);
		Stdout.pl(province);
		
		int cityIndex = name.indexOf("市");
		String city = cityIndex<0?"":name.substring(0, cityIndex+1);
		name = name.substring(cityIndex+1);
		Stdout.pl(city);
		
		int countyIndex = name.indexOf("区")<0?name.indexOf("县"):name.indexOf("区");
		String county = countyIndex<0?"":name.substring(0, countyIndex+1);
		name = name.substring(countyIndex+1);
		Stdout.pl(county);
		
		String others = name;
		Stdout.pl(others);*/
		
		//======================================
		int start = 0;
		int countryIndex = name.indexOf("国", start);
		String country = countryIndex<0?"":name.substring(start, countryIndex+1);
		start = countryIndex<0?start:countryIndex+1;
		Stdout.pl(country);
		
		int provinceIndex = name.indexOf("省", start);
		String province = provinceIndex<0?"":name.substring(start, provinceIndex+1);
		start = provinceIndex<0?start:provinceIndex+1;
		Stdout.pl(province);
		
		int cityIndex = name.indexOf("市", start);
		String city = cityIndex<0?"":name.substring(start, cityIndex+1);
		start = cityIndex<0?start:cityIndex+1;
		Stdout.pl(city);
		
		int countyIndex = name.indexOf("区", start)<0?name.indexOf("县", start):name.indexOf("区", start);
		String county = countyIndex<0?"":name.substring(start, countyIndex+1);
		start = countyIndex<0?start:countyIndex+1;
		Stdout.pl(county);
		
		Stdout.pl(name);
		
	}

}
