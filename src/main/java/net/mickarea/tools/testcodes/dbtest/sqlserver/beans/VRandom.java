/******************************************************************************************************

The file is generated automatically.
This Class File "VRandom.java" is part of project <"just write what you want"> , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.beans;

import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * 一个普通的 java bean 类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月28日
 */
@MyTableOrView(name="V_RANDOM")
public class VRandom {

    @MyColumn(name="rvalue", displayName="", extProperty="")
    private Double rvalue;

    /**
     * >> 构造函数
     */
    public VRandom() {
        // TODO Auto-generated constructor stub
    }
    public VRandom(Double rvalue) {
        this.rvalue=rvalue;
    }

    // getter and setter
    public Double getRvalue() {
        return rvalue;
    }
    public void setRvalue(Double rvalue) {
        this.rvalue=rvalue;
    }

    @Override
    public String toString() {
        String s = "VRandom{rvalue:%s}";
        return Stdout.fplToAnyWhere(s, this.rvalue);
    }
}
