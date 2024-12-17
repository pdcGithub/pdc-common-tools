/******************************************************************************************************

The file is generated automatically.
This Class File "AnalyBusyHours.java" is part of project <"just write what you want"> , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.beans;

import java.time.LocalDate;

import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * 一个普通的 java bean 类（测试 java.sql.Date 映射为 LocalDate）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年12月17日
 */
@MyTableOrView(name="ANALY_BUSY_HOURS")
public class AnalyBusyHours {

    @MyIdGroup
    @MyColumn(name="data_date", displayName="数据日期", extProperty="")
    private LocalDate dataDate;

    @MyColumn(name="col00", displayName="00-04时间段", extProperty="")
    private Integer col00;

    @MyColumn(name="col04", displayName="04-08时间段", extProperty="")
    private Integer col04;

    @MyColumn(name="col08", displayName="08-12时间段", extProperty="")
    private Integer col08;

    @MyColumn(name="col12", displayName="12-16时间段", extProperty="")
    private Integer col12;

    @MyColumn(name="col16", displayName="16-20时间段", extProperty="")
    private Integer col16;

    @MyColumn(name="col20", displayName="20-24时间段", extProperty="")
    private Integer col20;

    /**
     * 构造函数
     */
    public AnalyBusyHours() {
        // TODO Auto-generated constructor stub
    }
    public AnalyBusyHours(LocalDate dataDate, Integer col00, Integer col04, Integer col08, Integer col12, Integer col16, Integer col20) {
        this.dataDate=dataDate;
        this.col00=col00;
        this.col04=col04;
        this.col08=col08;
        this.col12=col12;
        this.col16=col16;
        this.col20=col20;
    }

    // getter and setter
    public LocalDate getDataDate() {
        return dataDate;
    }
    public void setDataDate(LocalDate dataDate) {
        this.dataDate=dataDate;
    }
    public Integer getCol00() {
        return col00;
    }
    public void setCol00(Integer col00) {
        this.col00=col00;
    }
    public Integer getCol04() {
        return col04;
    }
    public void setCol04(Integer col04) {
        this.col04=col04;
    }
    public Integer getCol08() {
        return col08;
    }
    public void setCol08(Integer col08) {
        this.col08=col08;
    }
    public Integer getCol12() {
        return col12;
    }
    public void setCol12(Integer col12) {
        this.col12=col12;
    }
    public Integer getCol16() {
        return col16;
    }
    public void setCol16(Integer col16) {
        this.col16=col16;
    }
    public Integer getCol20() {
        return col20;
    }
    public void setCol20(Integer col20) {
        this.col20=col20;
    }

    @Override
    public String toString() {
        String s = "AnalyBusyHours{dataDate:%s, col00:%s, col04:%s, col08:%s, col12:%s, col16:%s, col20:%s}";
        return Stdout.fplToAnyWhere(s, this.dataDate, this.col00, this.col04, this.col08, this.col12, this.col16, this.col20);
    }
}
