/******************************************************************************************************

This file "ViewAB.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.beans;

import java.time.LocalDateTime;

import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyColumnIgnore;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * >> view_a_b 视图 的实体类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月13日-2023年7月1日
 */
@MyTableOrView(name="view_a_b")
public class ViewAB {
	
	@MyIdGroup
	@MyColumn(name="id")
	private Integer id;
	
	//测试下字段忽略功能
	@MyColumnIgnore
	@MyColumn(name="name")
	private String name;
	
	@MyColumn(name="gender")
	private String gender;
	
	//测试下字段忽略功能
	private String address;
	
	@MyColumn(name="tel")
	private String tel;
	
	@MyColumn(name="score")
	private Integer score;
	
	@MyColumn(name="descrption")
	private String descrption;
	
	@MyColumn(name="exam_score")
	private Float examScore;
	
	@MyColumn(name="final")
	private Double finals;
	
	@MyColumn(name="update_time")
	private LocalDateTime updateTime;

	/**
	 * 构造函数
	 */
	public ViewAB() {
	}
	public ViewAB(Integer id, String name, String gender, String address, String tel, Integer score, String descrption,
			Float examScore, Double finals, LocalDateTime updateTime) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.address = address;
		this.tel = tel;
		this.score = score;
		this.descrption = descrption;
		this.examScore = examScore;
		this.finals = finals;
		this.updateTime = updateTime;
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
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getDescrption() {
		return descrption;
	}
	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}
	public Float getExamScore() {
		return examScore;
	}
	public void setExamScore(Float examScore) {
		this.examScore = examScore;
	}
	public Double getFinals() {
		return finals;
	}
	public void setFinals(Double finals) {
		this.finals = finals;
	}
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
	
	@Override
	public String toString() {
		return Stdout.fplToAnyWhere("%s{id:%s, name:%s, gender:%s, address:%s, tel:%s, score:%s, descrption:%s, examScore:%s, finals:%s, updateTime:%s}",
				ViewAB.class.getSimpleName(),
				this.id, this.name, this.gender, this.address, this.tel, this.score, this.descrption, this.examScore, this.finals, this.updateTime);
	}
	
	/**
	 * >> 测试
	 */
	public static void main(String[] args) {
		Stdout.pl(new ViewAB());
	}
	
}
