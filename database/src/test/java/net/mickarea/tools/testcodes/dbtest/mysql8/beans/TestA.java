/******************************************************************************************************

This file "TestA.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.beans;

import net.mickarea.tools.annotation.MyAutoIncrement;
import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyColumnIgnore;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * 一个普通的 java bean 类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月6日-2023年7月1日
 */
@MyTableOrView(name="test_a")
public class TestA {

	@MyAutoIncrement
	@MyIdGroup
	@MyColumn(name="id")
	private Integer id;
	
	@MyColumn(name="name")
	private String name;
	
	@MyColumn(name="gender")
	private String gender;
	
	@MyColumn(name="address")
	private String address;
	
	@MyColumn(name="tel")
	private String tel;
	
	@MyColumnIgnore
	@MyColumn
	private String testColumn;
	
	/**
	 * 构造函数
	 */
	public TestA() {
	}
	public TestA(Integer id, String name, String gender, String address, String tel) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.address = address;
		this.tel = tel;
	}
	
	// getter and setter
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getTestColumn() {
		return testColumn;
	}
	public void setTestColumn(String testColumn) {
		this.testColumn = testColumn;
	}
	
	/**
	 * 对象信息打印
	 */
	@Override
	public String toString() {
		return Stdout.fplToAnyWhere("TestA{id:%s, name:%s, gender:%s, address:%s, tel:%s, testColumn:%s}", 
				                    this.id, this.name, this.gender, this.address, this.tel, this.testColumn
				                   );
	}
	
}
