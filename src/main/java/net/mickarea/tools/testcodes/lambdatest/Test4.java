/******************************************************************************************************

This file "Test4.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.lambdatest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.mickarea.tools.utils.Stdout;

/**
 * >> Stream API 测试
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月17日
 */
public class Test4 {
	
	public static void main(String[] args) throws Exception {
		
		//第一个例子
		String fileName = "D:\\mysvn-repo\\personal\\trunk\\excelRptMaker\\src\\com\\mickspace\\exl\\util\\DomsExcelReadUtil.java";
		String prefix = "imp";
		Optional<String> firstMatchdLine = Files.lines(Paths.get(fileName))
				                                .filter(line->line.startsWith(prefix))
				                                .findFirst();
		Stdout.pl("第一个匹配的值为："+firstMatchdLine.orElse("can not find anything."));
		
		//第二个例子
		List<Person> persons = Person.getTestData();
		List<String> names = persons.stream()
				                    .filter(person-> person.getAge()>15)
				                    .map(person-> person.getName().toUpperCase())
				                    .collect(Collectors.toList());
		Stdout.pl(persons);
		Stdout.pl(names);
		
		//第三个例子
		int sum = persons.stream()
				         .filter(person->person.getGender().equalsIgnoreCase(Person.MALE))
				         .mapToInt(Person::getAge)
				         .sum();
		int average = (int)persons.stream()
				                  .filter(person->person.getGender().equalsIgnoreCase(Person.MALE))
				                  .mapToInt(Person::getAge)
				                  .average()
				                  .getAsDouble();
		int max = persons.stream()
				         .filter(person->person.getGender().equalsIgnoreCase(Person.MALE))
				         .mapToInt(Person::getAge)
				         .max()
				         .getAsInt();
		Stdout.fpl("男性的总年龄为：%s，平均年龄：%s，最大年龄：%s", sum, average, max);
		
		//第四个例子
		int sum2 = persons.stream()
				          .filter(person->person.getGender().equalsIgnoreCase(Person.MALE))
				          .mapToInt(Person::getAge)
				          .reduce((total, age)->total+age)
				          .getAsInt();
		long males = persons.stream()
				            .filter(person->person.getGender().equalsIgnoreCase(Person.MALE))
				            .count();
		long average2 = persons.stream()
				             .filter(person->person.getGender().equalsIgnoreCase(Person.MALE))
				             .mapToInt(Person::getAge)
				             .reduce((total, age)->total+age)
				             .getAsInt() / males;
		int max2 = persons.stream()
				          .filter(person->person.getGender().equalsIgnoreCase(Person.MALE))
				          .mapToInt(Person::getAge)
				          .reduce(0, (currentMax, age)->age>currentMax?age:currentMax);
		Stdout.fpl("男性总年龄为：%s, 男性人数：%s, 男性平均年龄：%s, 男性最大年龄为：%s", sum2, males, average2, max2);
	}

}
