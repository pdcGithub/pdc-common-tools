/******************************************************************************************************

This file "TestATempVO.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.beans.vo;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyVirtualEntity;
import net.mickarea.tools.utils.Stdout;

/**
 * test_a 这个表的临时sql查询结果对象
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月18日
 */
@MyVirtualEntity
public class TestATempVO {

	@MyColumn(name="date1")
	private Date date1;
	
	@MyColumn(name="date2")
	private Time date2;
	
	@MyColumn(name="date3")
	private LocalDateTime date3;
	
	@MyColumn(name="id")
	private Integer id;
	
	@MyColumn(name="name")
	private String name;
	
	@MyColumn(name="cont")
	private Boolean cont;
	
	//setter and getter
	public Date getDate1() {
		return date1;
	}
	public void setDate1(Date date1) {
		this.date1 = date1;
	}
	public Time getDate2() {
		return date2;
	}
	public void setDate2(Time date2) {
		this.date2 = date2;
	}
	public LocalDateTime getDate3() {
		return date3;
	}
	public void setDate3(LocalDateTime date3) {
		this.date3 = date3;
	}
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
	public Boolean getCont() {
		return cont;
	}
	public void setCont(Boolean cont) {
		this.cont = cont;
	}
	
	//构造函数
	public TestATempVO() {
	}
	public TestATempVO(Date date1, Time date2, LocalDateTime date3, Integer id, String name, Boolean cont) {
		this.date1 = date1;
		this.date2 = date2;
		this.date3 = date3;
		this.id = id;
		this.name = name;
		this.cont = cont;
	}
	
	@Override
	public String toString() {
		return Stdout.fplToAnyWhere("TestATempVO{date1:%s, date2:%s, date3:%s, id:%s, name:%s, cont:%s}", 
				                    this.date1, this.date2, this.date3, this.id, this.name, this.cont);
	}
	
	public static void main(String[] args) {
		Stdout.pl(new TestATempVO());
	}
	
}
