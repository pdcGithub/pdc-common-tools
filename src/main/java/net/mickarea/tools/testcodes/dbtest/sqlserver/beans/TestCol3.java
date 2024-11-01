/******************************************************************************************************

The file is generated automatically.
This Class File "Test3.java" is part of project <"just write what you want"> , which is belong to Michael Pang (It's Me).
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
@MyTableOrView(name="TEST_3")
public class TestCol3 {

    @MyAutoIncrement
    @MyIdGroup
    @MyColumn(name="", displayName="", extProperty="")
    private Integer id;

    @MyColumn(name="test_name", displayName="", extProperty="")
    private String testName;

    /**
     * 构造函数
     */
    public TestCol3() {
        // TODO Auto-generated constructor stub
    }
    public TestCol3(Integer id, String testName) {
        this.id=id;
        this.testName=testName;
    }

    // getter and setter
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id=id;
    }
    public String getTestName() {
        return testName;
    }
    public void setTestName(String testName) {
        this.testName=testName;
    }

    @Override
    public String toString() {
        String s = "Test3{id:%s, testName:%s}";
        return Stdout.fplToAnyWhere(s, this.id, this.testName);
    }
}
