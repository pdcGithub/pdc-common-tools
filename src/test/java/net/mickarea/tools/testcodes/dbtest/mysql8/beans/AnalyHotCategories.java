/******************************************************************************************************

The file is generated automatically.
This Class File "AnalyHotCategories.java" is part of project <"just write what you want"> , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.beans;

import java.util.Date;

import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * 一个普通的 java bean 类（测试 java.sql.Date 映射为 java.util.Date）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年12月17日
 */
@MyTableOrView(name="ANALY_HOT_CATEGORIES")
public class AnalyHotCategories {

	@MyIdGroup
    @MyColumn(name="data_date", displayName="数据日期", extProperty="")
    private Date dataDate;

	@MyIdGroup
    @MyColumn(name="tag_name", displayName="标签名", extProperty="")
    private String tagName;

    @MyColumn(name="tag_count", displayName="数量", extProperty="")
    private Integer tagCount;

    /**
     * 构造函数
     */
    public AnalyHotCategories() {
        // TODO Auto-generated constructor stub
    }
    public AnalyHotCategories(Date dataDate, String tagName, Integer tagCount) {
        this.dataDate=dataDate;
        this.tagName=tagName;
        this.tagCount=tagCount;
    }

    // getter and setter
    public Date getDataDate() {
        return dataDate;
    }
    public void setDataDate(Date dataDate) {
        this.dataDate=dataDate;
    }
    public String getTagName() {
        return tagName;
    }
    public void setTagName(String tagName) {
        this.tagName=tagName;
    }
    public Integer getTagCount() {
        return tagCount;
    }
    public void setTagCount(Integer tagCount) {
        this.tagCount=tagCount;
    }

    @Override
    public String toString() {
        String s = "AnalyHotCategories{dataDate:%s, tagName:%s, tagCount:%s}";
        return Stdout.fplToAnyWhere(s, this.dataDate, this.tagName, this.tagCount);
    }
}
