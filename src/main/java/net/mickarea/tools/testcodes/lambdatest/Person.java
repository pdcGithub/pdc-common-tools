/******************************************************************************************************

This file "Person.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.lambdatest;

import java.util.ArrayList;
import java.util.List;

/**
 * >> 一个用于测试的类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月17日
 */
public class Person {
	public static final String MALE = "M";
	public static final String FEMALE = "F";
	String name;
	String gender;
	int age;
	/**
	 * >> 构造函数
	 */
	public Person() {
	}
	public Person(String name, String gender, int age) {
		this.name = name;
		this.gender = gender;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return getName()+"-"+getGender()+"-"+getAge();
	}
	//获取测试数据的函数
	public static List<Person> getTestData(){
		Person p1 = new Person("Tom", Person.MALE, 12);
		Person p2 = new Person("Jack", Person.MALE, 24);
		Person p3 = new Person("Lucy", Person.FEMALE, 32);
		Person p4 = new Person("Marry", Person.FEMALE, 14);
		Person p5 = new Person("Joe", Person.MALE, 12);
		Person p6 = new Person("Kate", Person.FEMALE, 34);
		Person p7 = new Person("Jetty", Person.MALE, 36);
		Person p8 = new Person("John", Person.MALE, 45);
		List<Person> list = new ArrayList<Person>();
		list.add(p1);
		list.add(p2);
		list.add(p3);
		list.add(p4);
		list.add(p5);
		list.add(p6);
		list.add(p7);
		list.add(p8);
		return list;
	}
}
