/******************************************************************************************************

The file is generated automatically.
This Class File "Test2.java" is part of project <"just write what you want"> , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.beans;

import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * 一个普通的 java bean 类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月25日
 */
@MyTableOrView(name="TEST_2")
public class TestCol2 {

    private Integer id;

    private String name;

    private Integer age;

    private String address;

    /**
     * 构造函数
     */
    public TestCol2() {
        // TODO Auto-generated constructor stub
    }
    public TestCol2(Integer id, String name, Integer age, String address) {
        this.id=id;
        this.name=name;
        this.age=age;
        this.address=address;
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

    @Override
    public String toString() {
        String s = "Test2{id:%s, name:%s, age:%s, address:%s}";
        return Stdout.fplToAnyWhere(s, this.id, this.name, this.age, this.address);
    }
}
