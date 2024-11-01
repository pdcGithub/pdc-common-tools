/******************************************************************************************************

This file "Test42.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.lambdatest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.mickarea.tools.utils.Stdout;

/**
 * Stream API 测试 2
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月17日
 */
public class Test42 {
	
	public static void main(String[] args) {
		
		List<Person> persons = Person.getTestData();
		Stdout.pl(persons);
		
		//第五个例子，关于 collect 方法
		List<Person> males = persons.stream()
				                    .filter(person->person.getGender().equals(Person.MALE))
				                    .collect(
				                    		()->new ArrayList<Person>(), 
				                    		(maleLt,person)->maleLt.add(person), 
				                    		(maleLt1, maleLt2)->maleLt1.addAll(maleLt2)
				                    );
		Stdout.pl(males);
		
		//再简化为
		List<Person> males2 = persons.stream()
				                    .filter(person->person.getGender().equals(Person.MALE))
				                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		Stdout.pl(males2);
		
		//第六个例子 依据性别分组
		Map<String, List<Person>> males3 = persons.stream()
				                                  .collect(Collectors.groupingBy(Person::getGender));
		Stdout.pl(males3);
		
		//第七个例子 依据性别分组，再取得对应人的名字列表
		Map<String, List<String>> maleNames = persons.stream()
				                                     .collect(
				                                    		 Collectors.groupingBy(Person::getGender, 
				                                    				               Collectors.mapping(Person::getName, Collectors.toList())
				                                    	     )
				                                     );
		Stdout.pl(maleNames);
		
		//第八个例子，分组后分别取得 男女的年龄并相加
		Map<String, Integer> malesAge = persons.stream()
				                               .collect(
				                            		   Collectors.groupingBy(Person::getGender, 
				                            				                 Collectors.reducing(0, Person::getAge, Integer::sum))
				                               );
		Stdout.pl(malesAge);
	}

}
