/******************************************************************************************************

The file is generated automatically.
This Class File "Student.java" is part of project <"just write what you want"> , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.beans;

import java.time.LocalDateTime;

import net.mickarea.tools.annotation.MyAutoIncrement;
import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * 一个普通的 java bean 类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年11月25日
 */
@MyTableOrView(name="STUDENT")
public class Student {

    @MyAutoIncrement
    @MyColumn(name="seq", displayName="", extProperty="")
    private Integer seq;

    @MyIdGroup
    @MyColumn(name="stu_no", displayName="", extProperty="")
    private String stuNo;

    @MyColumn(name="stu_name", displayName="", extProperty="")
    private String stuName;

    @MyColumn(name="c_time", displayName="", extProperty="")
    private LocalDateTime cTime;

    @MyColumn(name="u_time", displayName="", extProperty="")
    private LocalDateTime uTime;

    /**
     * 构造函数
     */
    public Student() {
        // TODO Auto-generated constructor stub
    }
    public Student(Integer seq, String stuNo, String stuName, LocalDateTime cTime, LocalDateTime uTime) {
        this.seq=seq;
        this.stuNo=stuNo;
        this.stuName=stuName;
        this.cTime=cTime;
        this.uTime=uTime;
    }

    // getter and setter
    public Integer getSeq() {
        return seq;
    }
    public void setSeq(Integer seq) {
        this.seq=seq;
    }
    public String getStuNo() {
        return stuNo;
    }
    public void setStuNo(String stuNo) {
        this.stuNo=stuNo;
    }
    public String getStuName() {
        return stuName;
    }
    public void setStuName(String stuName) {
        this.stuName=stuName;
    }
    public LocalDateTime getCTime() {
        return cTime;
    }
    public void setCTime(LocalDateTime cTime) {
        this.cTime=cTime;
    }
    public LocalDateTime getUTime() {
        return uTime;
    }
    public void setUTime(LocalDateTime uTime) {
        this.uTime=uTime;
    }

    @Override
    public String toString() {
        String s = "Student{seq:%s, stuNo:%s, stuName:%s, cTime:%s, uTime:%s}";
        return Stdout.fplToAnyWhere(s, this.seq, this.stuNo, this.stuName, this.cTime, this.uTime);
    }
}
