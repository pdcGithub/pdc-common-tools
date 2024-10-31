/******************************************************************************************************

The file is generated automatically.
This Class File "TestInsert.java" is part of project <"just write what you want"> , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.beans;

import net.mickarea.tools.annotation.MyAutoIncrement;
import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * 一个普通的 java bean 类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月25日
 */
@MyTableOrView(name="TEST_INSERT")
public class TestInsert {

    @MyAutoIncrement
    @MyIdGroup
    @MyColumn(name="id", displayName="", extProperty="")
    private Integer id;

    @MyColumn(name="name", displayName="", extProperty="")
    private String name;

    @MyColumn(name="age", displayName="", extProperty="")
    private Integer age;

    @MyColumn(name="address", displayName="", extProperty="")
    private String address;

    @MyColumn(name="email", displayName="", extProperty="")
    private String email;

    @MyColumn(name="tel", displayName="", extProperty="")
    private String tel;

    /**
     * >> 构造函数
     */
    public TestInsert() {
        // TODO Auto-generated constructor stub
    }
    public TestInsert(Integer id, String name, Integer age, String address, String email, String tel) {
        this.id=id;
        this.name=name;
        this.age=age;
        this.address=address;
        this.email=email;
        this.tel=tel;
    }

    // getter and setter
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id=id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age=age;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address=address;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email=email;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel=tel;
    }

    @Override
    public String toString() {
        String s = "TestInsert{id:%s, name:%s, age:%s, address:%s, email:%s, tel:%s}";
        return Stdout.fplToAnyWhere(s, this.id, this.name, this.age, this.address, this.email, this.tel);
    }
}
