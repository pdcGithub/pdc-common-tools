/******************************************************************************************************

The file is generated automatically.
This Class File "TestInsert.java" is part of project <"just write what you want"> , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.beans;

import net.mickarea.tools.annotation.MyAutoIncrement;
import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * 一个普通的 java bean 类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年12月3日
 */
@MyTableOrView(name="TEST_INSERT")
public class TestInsert {

    @MyAutoIncrement
    @MyIdGroup
    @MyColumn(name="id", displayName="", extProperty="")
    private Integer id;

    @MyColumn(name="strings", displayName="", extProperty="")
    private String strings;

    @MyColumn(name="ctime", displayName="，精确到毫秒", extProperty="")
    private java.time.LocalDateTime ctime;

    /**
     * 构造函数
     */
    public TestInsert() {
        // TODO Auto-generated constructor stub
    }
    public TestInsert(Integer id, String strings, java.time.LocalDateTime ctime) {
        this.id=id;
        this.strings=strings;
        this.ctime=ctime;
    }

    // getter and setter
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id=id;
    }
    public String getStrings() {
        return strings;
    }
    public void setStrings(String strings) {
        this.strings=strings;
    }
    public java.time.LocalDateTime getCtime() {
        return ctime;
    }
    public void setCtime(java.time.LocalDateTime ctime) {
        this.ctime=ctime;
    }

    @Override
    public String toString() {
        String s = "TestInsert{id:%s, strings:%s, ctime:%s}";
        return Stdout.fplToAnyWhere(s, this.id, this.strings, this.ctime);
    }
}
