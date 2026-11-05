/******************************************************************************************************

The file is generated automatically.
This Class File "Sql1793866028907VO.java" is part of project <"just write what you want"> , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.beans.virtualentities;

import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyVirtualEntity;
import net.mickarea.tools.utils.StrUtil;

/**
 * 这是一个 关于 STUDENT 表的 SQL 语句，所映射的虚拟实体。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年11月5日
 */
@MyVirtualEntity
public class VOStudent {

	@MyIdGroup
    @MyColumn(name="stu_no", displayName="", extProperty="")
    private String stuNo;

    @MyColumn(name="stu_name", displayName="", extProperty="")
    private String stuName;

    @MyColumn(name="age", displayName="", extProperty="")
    private Long age;

    @MyColumn(name="grade", displayName="", extProperty="")
    private Long grade;

    @MyColumn(name="country", displayName="", extProperty="")
    private String country;

    /**
     * 构造函数
     */
    public VOStudent() {
        // TODO Auto-generated constructor stub
    }
    public VOStudent(String stuNo, String stuName, Long age, Long grade, String country) {
        this.stuNo=stuNo;
        this.stuName=stuName;
        this.age=age;
        this.grade=grade;
        this.country=country;
    }

    // getter and setter
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
    public Long getAge() {
        return age;
    }
    public void setAge(Long age) {
        this.age=age;
    }
    public Long getGrade() {
        return grade;
    }
    public void setGrade(Long grade) {
        this.grade=grade;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country=country;
    }

    @Override
    public String toString() {
        return StrUtil.getJavaBeanFieldsInfo(this);
    }
}
