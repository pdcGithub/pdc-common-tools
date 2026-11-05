/******************************************************************************************************

The file is generated automatically.
This Class File "VStudent.java" is part of project <"pdc-common-database"> , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.beans.views;

import java.time.LocalDate;
import java.time.LocalDateTime;

import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.StrUtil;

/**
 * 这是一个关于数据库视图 V_STUDENT 的实体类。它会映射所有的视图内字段。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年11月5日
 */
@MyTableOrView(name="V_STUDENT")
public class VStudent {

	@MyIdGroup
    @MyColumn(name="stu_no", displayName="学生编码", extProperty="")
    private String stuNo;

    @MyColumn(name="stu_name", displayName="学生名称", extProperty="")
    private String stuName;

    @MyColumn(name="birth_date", displayName="出生日期", extProperty="")
    private LocalDate birthDate;

    @MyColumn(name="age", displayName="", extProperty="")
    private Long age;

    @MyColumn(name="enroll_date", displayName="入学日期", extProperty="")
    private LocalDate enrollDate;

    @MyColumn(name="grade", displayName="", extProperty="")
    private Long grade;

    @MyColumn(name="gender", displayName="", extProperty="")
    private String gender;

    @MyColumn(name="address", displayName="家庭住址", extProperty="")
    private String address;

    @MyColumn(name="tel", displayName="联系电话", extProperty="")
    private String tel;

    @MyColumn(name="create_user", displayName="创建人", extProperty="")
    private String createUser;

    @MyColumn(name="create_time", displayName="创建时间", extProperty="")
    private LocalDateTime createTime;

    /**
     * 构造函数
     */
    public VStudent() {
        // TODO Auto-generated constructor stub
    }
    public VStudent(String stuNo, String stuName, LocalDate birthDate, Long age, LocalDate enrollDate, Long grade, String gender, String address, String tel, String createUser, LocalDateTime createTime) {
        this.stuNo=stuNo;
        this.stuName=stuName;
        this.birthDate=birthDate;
        this.age=age;
        this.enrollDate=enrollDate;
        this.grade=grade;
        this.gender=gender;
        this.address=address;
        this.tel=tel;
        this.createUser=createUser;
        this.createTime=createTime;
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
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate=birthDate;
    }
    public Long getAge() {
        return age;
    }
    public void setAge(Long age) {
        this.age=age;
    }
    public LocalDate getEnrollDate() {
        return enrollDate;
    }
    public void setEnrollDate(LocalDate enrollDate) {
        this.enrollDate=enrollDate;
    }
    public Long getGrade() {
        return grade;
    }
    public void setGrade(Long grade) {
        this.grade=grade;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender=gender;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address=address;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel=tel;
    }
    public String getCreateUser() {
        return createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser=createUser;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime=createTime;
    }

    @Override
    public String toString() {
        return StrUtil.getJavaBeanFieldsInfo(this);
    }
}
