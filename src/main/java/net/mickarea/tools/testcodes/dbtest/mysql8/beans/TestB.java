/******************************************************************************************************

This file "TestB.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.beans;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import net.mickarea.tools.annotation.MyAutoIncrement;
import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * >> 一个普通的 java bean 类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月2日-2024年4月5日
 */
@MyTableOrView(name="test_b")
public class TestB {
	
	@MyAutoIncrement
	@MyColumn(name="id")
	private BigInteger id;
	
	@MyIdGroup
	@MyColumn(name="test_a_id")
	private Integer testAId; //对于 test_b 表，a 表的id是唯一的
	
	@MyColumn(name="score")
	private Long score;
	
	@MyColumn(name="descrption")
	private String descrption;
	
	@MyColumn(name="exam_score")
	private BigDecimal examScore;
	
	@MyColumn(name="final")
	private BigDecimal finals;
	
	@MyColumn(name="update_time")
	private LocalDateTime updateTime;
	
	/**
	 * >> 构造函数
	 */
	public TestB() {
		// TODO Auto-generated constructor stub
	}
	public TestB(BigInteger id, Integer testAId, Long score, String descrption, BigDecimal examScore, BigDecimal finals) {
		this.id = id;
		this.testAId = testAId;
		this.score = score;
		this.descrption = descrption;
		this.examScore = examScore;
		this.finals = finals;
	}
	
	// getter and setter
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public Integer getTestAId() {
		return testAId;
	}
	public void setTestAId(Integer testAId) {
		this.testAId = testAId;
	}
	public Long getScore() {
		return score;
	}
	public void setScore(Long score) {
		this.score = score;
	}
	public String getDescrption() {
		return descrption;
	}
	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}
	public BigDecimal getExamScore() {
		return examScore;
	}
	public void setExamScore(BigDecimal examScore) {
		this.examScore = examScore;
	}
	public BigDecimal getFinals() {
		return finals;
	}
	public void setFinals(BigDecimal finals) {
		this.finals = finals;
	}
	/**
	 * @return the updateTime
	 */
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
	
	@Override
	public String toString() {
		return Stdout.fplToAnyWhere("TestB{id:%s, testAId:%s, score:%s, descrption:%s, examScore:%s, finals:%s, updateTime:%s}", 
                this.id, this.testAId, this.score, this.descrption, this.examScore, this.finals, this.updateTime
               );
	}
}
