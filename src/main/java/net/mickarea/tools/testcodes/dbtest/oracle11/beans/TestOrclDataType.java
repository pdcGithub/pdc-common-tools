/******************************************************************************************************

This file is automatically generated.
This file "TestOrclDataType.java" is part of project <"just write what you want"> , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.oracle11.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;

import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * 一个普通的 java bean 类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月21日-2024年7月2日
 */
@MyTableOrView(name="TEST_ORCL_DATA_TYPE")
public class TestOrclDataType {

    @MyIdGroup
    @MyColumn(name="seq_no", displayName="序列号", extProperty="")
    private String seqNo;

    @MyColumn(name="cname", displayName="测试名字", extProperty="")
    private String cname;

    @MyColumn(name="app_date", displayName="日期", extProperty="")
    private java.util.Date appDate;

    @MyColumn(name="v_year", displayName="测试年份", extProperty="")
    private LocalDate vYear;

    @MyColumn(name="v_month", displayName="测试月份", extProperty="")
    private LocalDateTime vMonth;

    @MyColumn(name="v_day", displayName="测试日期", extProperty="")
    private LocalDateTime vDay;

    @MyColumn(name="app_datetime_1", displayName="时间戳测试1", extProperty="")
    private LocalDateTime appDatetime1;

    @MyColumn(name="app_datetime_2", displayName="时间戳测试2", extProperty="")
    private LocalDateTime appDatetime2;

    @MyColumn(name="content", displayName="测试内容1", extProperty="")
    private String content;

    @MyColumn(name="v_row2", displayName="rowid信息", extProperty="")
    private String vRow2;

    @MyColumn(name="v_clob", displayName="字符大字段", extProperty="")
    private String vClob;

    @MyColumn(name="v_nclob", displayName="字符大字段2", extProperty="")
    private String vNclob;

    @MyColumn(name="v_blob", displayName="字节型大字段", extProperty="")
    private byte[] vBlob;

    @MyColumn(name="v_nvarchar2", displayName="测试字符信息2", extProperty="")
    private String vNvarchar2;

    @MyColumn(name="v_char", displayName="测试字符信息3", extProperty="")
    private String vChar;

    @MyColumn(name="v_raw", displayName="raw类型", extProperty="")
    private byte[] vRaw;

    @MyColumn(name="v_float", displayName="float类型", extProperty="")
    private Double vFloat;

    @MyColumn(name="v_int", displayName="int类型", extProperty="")
    private java.math.BigDecimal vInt;

    @MyColumn(name="v_long", displayName="long类型", extProperty="")
    private String vLong;

    @MyColumn(name="v_num_1", displayName="number类型", extProperty="")
    private java.math.BigDecimal vNum1;

    @MyColumn(name="v_num_2", displayName="number2类型", extProperty="")
    private java.math.BigDecimal vNum2;

    @MyColumn(name="my_float", displayName="", extProperty="")
    private Double myFloat;

    @MyColumn(name="my_float_2", displayName="", extProperty="")
    private Double myFloat2;

    /**
     * 构造函数
     */
    public TestOrclDataType() {
        // TODO Auto-generated constructor stub
    }
    public TestOrclDataType(String seqNo, String cname, java.util.Date appDate, LocalDate vYear, LocalDateTime vMonth, LocalDateTime vDay, LocalDateTime appDatetime1, LocalDateTime appDatetime2, String content, String vRow2, String vClob, String vNclob, byte[] vBlob, String vNvarchar2, String vChar, byte[] vRaw, Double vFloat, java.math.BigDecimal vInt, String vLong, java.math.BigDecimal vNum1, java.math.BigDecimal vNum2, Double myFloat, Double myFloat2) {
        this.seqNo=seqNo;
        this.cname=cname;
        this.appDate=appDate;
        this.vYear=vYear;
        this.vMonth=vMonth;
        this.vDay=vDay;
        this.appDatetime1=appDatetime1;
        this.appDatetime2=appDatetime2;
        this.content=content;
        this.vRow2=vRow2;
        this.vClob=vClob;
        this.vNclob=vNclob;
        this.vBlob=vBlob;
        this.vNvarchar2=vNvarchar2;
        this.vChar=vChar;
        this.vRaw=vRaw;
        this.vFloat=vFloat;
        this.vInt=vInt;
        this.vLong=vLong;
        this.vNum1=vNum1;
        this.vNum2=vNum2;
        this.myFloat=myFloat;
        this.myFloat2=myFloat2;
    }

    // getter and setter
    public String getSeqNo() {
        return seqNo;
    }
    public void setSeqNo(String seqNo) {
        this.seqNo=seqNo;
    }
    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname=cname;
    }
    public java.util.Date getAppDate() {
        return appDate;
    }
    public void setAppDate(java.util.Date appDate) {
        this.appDate=appDate;
    }
    public LocalDate getVYear() {
        return vYear;
    }
    public void setVYear(LocalDate vYear) {
        this.vYear=vYear;
    }
    public LocalDateTime getVMonth() {
        return vMonth;
    }
    public void setVMonth(LocalDateTime vMonth) {
        this.vMonth=vMonth;
    }
    public LocalDateTime getVDay() {
        return vDay;
    }
    public void setVDay(LocalDateTime vDay) {
        this.vDay=vDay;
    }
    public LocalDateTime getAppDatetime1() {
        return appDatetime1;
    }
    public void setAppDatetime1(LocalDateTime appDatetime1) {
        this.appDatetime1=appDatetime1;
    }
    public LocalDateTime getAppDatetime2() {
        return appDatetime2;
    }
    public void setAppDatetime2(LocalDateTime appDatetime2) {
        this.appDatetime2=appDatetime2;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content=content;
    }
    public String getVRow2() {
        return vRow2;
    }
    public void setVRow2(String vRow2) {
        this.vRow2=vRow2;
    }
    public String getVClob() {
        return vClob;
    }
    public void setVClob(String vClob) {
        this.vClob=vClob;
    }
    public String getVNclob() {
        return vNclob;
    }
    public void setVNclob(String vNclob) {
        this.vNclob=vNclob;
    }
    public byte[] getVBlob() {
        return vBlob;
    }
    public void setVBlob(byte[] vBlob) {
        this.vBlob=vBlob;
    }
    public String getVNvarchar2() {
        return vNvarchar2;
    }
    public void setVNvarchar2(String vNvarchar2) {
        this.vNvarchar2=vNvarchar2;
    }
    public String getVChar() {
        return vChar;
    }
    public void setVChar(String vChar) {
        this.vChar=vChar;
    }
    public byte[] getVRaw() {
        return vRaw;
    }
    public void setVRaw(byte[] vRaw) {
        this.vRaw=vRaw;
    }
    public Double getVFloat() {
        return vFloat;
    }
    public void setVFloat(Double vFloat) {
        this.vFloat=vFloat;
    }
    public java.math.BigDecimal getVInt() {
        return vInt;
    }
    public void setVInt(java.math.BigDecimal vInt) {
        this.vInt=vInt;
    }
    public String getVLong() {
        return vLong;
    }
    public void setVLong(String vLong) {
        this.vLong=vLong;
    }
    public java.math.BigDecimal getVNum1() {
        return vNum1;
    }
    public void setVNum1(java.math.BigDecimal vNum1) {
        this.vNum1=vNum1;
    }
    public java.math.BigDecimal getVNum2() {
        return vNum2;
    }
    public void setVNum2(java.math.BigDecimal vNum2) {
        this.vNum2=vNum2;
    }
    public Double getMyFloat() {
        return myFloat;
    }
    public void setMyFloat(Double myFloat) {
        this.myFloat=myFloat;
    }
    public Double getMyFloat2() {
        return myFloat2;
    }
    public void setMyFloat2(Double myFloat2) {
        this.myFloat2=myFloat2;
    }

    @Override
    public String toString() {
        String s = "TestOrclDataType{seqNo:%s, cname:%s, appDate:%s, vYear:%s, vMonth:%s, vDay:%s, appDatetime1:%s, appDatetime2:%s, content:%s, vRow2:%s, vClob:%s, vNclob:%s, vBlob:%s, vNvarchar2:%s, vChar:%s, vRaw:%s, vFloat:%s, vInt:%s, vLong:%s, vNum1:%s, vNum2:%s, myFloat:%s, myFloat2:%s}";
        return Stdout.fplToAnyWhere(s, this.seqNo, this.cname, this.appDate, this.vYear, this.vMonth, this.vDay, this.appDatetime1, this.appDatetime2, this.content, this.vRow2, this.vClob, this.vNclob, this.vBlob, this.vNvarchar2, this.vChar, this.vRaw, this.vFloat, this.vInt, this.vLong, this.vNum1, this.vNum2, this.myFloat, this.myFloat2);
    }
}
