/******************************************************************************************************

The file is generated automatically.
This Class File "VDataTypes.java" is part of project <"just write what you want"> , which is belong to Michael Pang (It's Me).
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
@MyTableOrView(name="V_DATA_TYPES")
public class VDataTypes {

    @MyColumn(name="col_1", displayName="", extProperty="")
    private Long col1;

    @MyColumn(name="col_2", displayName="", extProperty="")
    private byte[] col2;

    @MyColumn(name="col_3", displayName="", extProperty="")
    private Boolean col3;

    @MyColumn(name="col_4", displayName="", extProperty="")
    private String col4;

    @MyColumn(name="col_5", displayName="", extProperty="")
    private java.sql.Date col5;

    @MyColumn(name="col_6", displayName="", extProperty="")
    private java.sql.Timestamp col6;

    @MyColumn(name="col_7", displayName="", extProperty="")
    private java.sql.Timestamp col7;

    @MyColumn(name="col_8", displayName="", extProperty="")
    private microsoft.sql.DateTimeOffset col8;

    @MyColumn(name="col_9", displayName="", extProperty="")
    private java.math.BigDecimal col9;

    @MyColumn(name="col_10", displayName="", extProperty="")
    private Double col10;

    @MyColumn(name="col_11", displayName="", extProperty="")
    private byte[] col11;

    @MyColumn(name="col_12", displayName="", extProperty="")
    private byte[] col12;

    @MyColumn(name="col_13", displayName="", extProperty="")
    private byte[] col13;

    @MyColumn(name="col_14", displayName="", extProperty="")
    private byte[] col14;

    @MyColumn(name="col_15", displayName="", extProperty="")
    private Integer col15;

    @MyColumn(name="col_16", displayName="", extProperty="")
    private java.math.BigDecimal col16;

    @MyColumn(name="col_17", displayName="", extProperty="")
    private String col17;

    @MyColumn(name="col_18", displayName="", extProperty="")
    private String col18;

    @MyColumn(name="col_19", displayName="", extProperty="")
    private java.math.BigDecimal col19;

    @MyColumn(name="col_20", displayName="", extProperty="")
    private String col20;

    @MyColumn(name="col_21", displayName="", extProperty="")
    private String col21;

    @MyColumn(name="col_22", displayName="", extProperty="")
    private Float col22;

    @MyColumn(name="col_23", displayName="", extProperty="")
    private java.sql.Timestamp col23;

    @MyColumn(name="col_24", displayName="", extProperty="")
    private Short col24;

    @MyColumn(name="col_25", displayName="", extProperty="")
    private java.math.BigDecimal col25;

    @MyColumn(name="col_26", displayName="", extProperty="")
    private Object col26;

    @MyColumn(name="col_27", displayName="", extProperty="")
    private String col27;

    @MyColumn(name="col_28", displayName="", extProperty="")
    private java.sql.Time col28;

    @MyColumn(name="col_29", displayName="", extProperty="")
    private byte[] col29;

    @MyColumn(name="col_30", displayName="", extProperty="")
    private Short col30;

    @MyColumn(name="col_31", displayName="", extProperty="")
    private String col31;

    @MyColumn(name="col_32", displayName="", extProperty="")
    private byte[] col32;

    @MyColumn(name="col_33", displayName="", extProperty="")
    private byte[] col33;

    @MyColumn(name="col_34", displayName="", extProperty="")
    private String col34;

    @MyColumn(name="col_35", displayName="", extProperty="")
    private String col35;

    @MyColumn(name="col_36", displayName="", extProperty="")
    private String col36;

    /**
     * >> 构造函数
     */
    public VDataTypes() {
        // TODO Auto-generated constructor stub
    }
    public VDataTypes(Long col1, byte[] col2, Boolean col3, String col4, java.sql.Date col5, java.sql.Timestamp col6, java.sql.Timestamp col7, microsoft.sql.DateTimeOffset col8, java.math.BigDecimal col9, Double col10, byte[] col11, byte[] col12, byte[] col13, byte[] col14, Integer col15, java.math.BigDecimal col16, String col17, String col18, java.math.BigDecimal col19, String col20, String col21, Float col22, java.sql.Timestamp col23, Short col24, java.math.BigDecimal col25, Object col26, String col27, java.sql.Time col28, byte[] col29, Short col30, String col31, byte[] col32, byte[] col33, String col34, String col35, String col36) {
        this.col1=col1;
        this.col2=col2;
        this.col3=col3;
        this.col4=col4;
        this.col5=col5;
        this.col6=col6;
        this.col7=col7;
        this.col8=col8;
        this.col9=col9;
        this.col10=col10;
        this.col11=col11;
        this.col12=col12;
        this.col13=col13;
        this.col14=col14;
        this.col15=col15;
        this.col16=col16;
        this.col17=col17;
        this.col18=col18;
        this.col19=col19;
        this.col20=col20;
        this.col21=col21;
        this.col22=col22;
        this.col23=col23;
        this.col24=col24;
        this.col25=col25;
        this.col26=col26;
        this.col27=col27;
        this.col28=col28;
        this.col29=col29;
        this.col30=col30;
        this.col31=col31;
        this.col32=col32;
        this.col33=col33;
        this.col34=col34;
        this.col35=col35;
        this.col36=col36;
    }

    // getter and setter
    public Long getCol1() {
        return col1;
    }
    public void setCol1(Long col1) {
        this.col1=col1;
    }
    public byte[] getCol2() {
        return col2;
    }
    public void setCol2(byte[] col2) {
        this.col2=col2;
    }
    public Boolean getCol3() {
        return col3;
    }
    public void setCol3(Boolean col3) {
        this.col3=col3;
    }
    public String getCol4() {
        return col4;
    }
    public void setCol4(String col4) {
        this.col4=col4;
    }
    public java.sql.Date getCol5() {
        return col5;
    }
    public void setCol5(java.sql.Date col5) {
        this.col5=col5;
    }
    public java.sql.Timestamp getCol6() {
        return col6;
    }
    public void setCol6(java.sql.Timestamp col6) {
        this.col6=col6;
    }
    public java.sql.Timestamp getCol7() {
        return col7;
    }
    public void setCol7(java.sql.Timestamp col7) {
        this.col7=col7;
    }
    public microsoft.sql.DateTimeOffset getCol8() {
        return col8;
    }
    public void setCol8(microsoft.sql.DateTimeOffset col8) {
        this.col8=col8;
    }
    public java.math.BigDecimal getCol9() {
        return col9;
    }
    public void setCol9(java.math.BigDecimal col9) {
        this.col9=col9;
    }
    public Double getCol10() {
        return col10;
    }
    public void setCol10(Double col10) {
        this.col10=col10;
    }
    public byte[] getCol11() {
        return col11;
    }
    public void setCol11(byte[] col11) {
        this.col11=col11;
    }
    public byte[] getCol12() {
        return col12;
    }
    public void setCol12(byte[] col12) {
        this.col12=col12;
    }
    public byte[] getCol13() {
        return col13;
    }
    public void setCol13(byte[] col13) {
        this.col13=col13;
    }
    public byte[] getCol14() {
        return col14;
    }
    public void setCol14(byte[] col14) {
        this.col14=col14;
    }
    public Integer getCol15() {
        return col15;
    }
    public void setCol15(Integer col15) {
        this.col15=col15;
    }
    public java.math.BigDecimal getCol16() {
        return col16;
    }
    public void setCol16(java.math.BigDecimal col16) {
        this.col16=col16;
    }
    public String getCol17() {
        return col17;
    }
    public void setCol17(String col17) {
        this.col17=col17;
    }
    public String getCol18() {
        return col18;
    }
    public void setCol18(String col18) {
        this.col18=col18;
    }
    public java.math.BigDecimal getCol19() {
        return col19;
    }
    public void setCol19(java.math.BigDecimal col19) {
        this.col19=col19;
    }
    public String getCol20() {
        return col20;
    }
    public void setCol20(String col20) {
        this.col20=col20;
    }
    public String getCol21() {
        return col21;
    }
    public void setCol21(String col21) {
        this.col21=col21;
    }
    public Float getCol22() {
        return col22;
    }
    public void setCol22(Float col22) {
        this.col22=col22;
    }
    public java.sql.Timestamp getCol23() {
        return col23;
    }
    public void setCol23(java.sql.Timestamp col23) {
        this.col23=col23;
    }
    public Short getCol24() {
        return col24;
    }
    public void setCol24(Short col24) {
        this.col24=col24;
    }
    public java.math.BigDecimal getCol25() {
        return col25;
    }
    public void setCol25(java.math.BigDecimal col25) {
        this.col25=col25;
    }
    public Object getCol26() {
        return col26;
    }
    public void setCol26(Object col26) {
        this.col26=col26;
    }
    public String getCol27() {
        return col27;
    }
    public void setCol27(String col27) {
        this.col27=col27;
    }
    public java.sql.Time getCol28() {
        return col28;
    }
    public void setCol28(java.sql.Time col28) {
        this.col28=col28;
    }
    public byte[] getCol29() {
        return col29;
    }
    public void setCol29(byte[] col29) {
        this.col29=col29;
    }
    public Short getCol30() {
        return col30;
    }
    public void setCol30(Short col30) {
        this.col30=col30;
    }
    public String getCol31() {
        return col31;
    }
    public void setCol31(String col31) {
        this.col31=col31;
    }
    public byte[] getCol32() {
        return col32;
    }
    public void setCol32(byte[] col32) {
        this.col32=col32;
    }
    public byte[] getCol33() {
        return col33;
    }
    public void setCol33(byte[] col33) {
        this.col33=col33;
    }
    public String getCol34() {
        return col34;
    }
    public void setCol34(String col34) {
        this.col34=col34;
    }
    public String getCol35() {
        return col35;
    }
    public void setCol35(String col35) {
        this.col35=col35;
    }
    public String getCol36() {
        return col36;
    }
    public void setCol36(String col36) {
        this.col36=col36;
    }

    @Override
    public String toString() {
        String s = "VDataTypes{col1:%s, col2:%s, col3:%s, col4:%s, col5:%s, col6:%s, col7:%s, col8:%s, col9:%s, col10:%s, col11:%s, col12:%s, col13:%s, col14:%s, col15:%s, col16:%s, col17:%s, col18:%s, col19:%s, col20:%s, col21:%s, col22:%s, col23:%s, col24:%s, col25:%s, col26:%s, col27:%s, col28:%s, col29:%s, col30:%s, col31:%s, col32:%s, col33:%s, col34:%s, col35:%s, col36:%s}";
        return Stdout.fplToAnyWhere(s, this.col1, this.col2, this.col3, this.col4, this.col5, this.col6, this.col7, this.col8, this.col9, this.col10, this.col11, this.col12, this.col13, this.col14, this.col15, this.col16, this.col17, this.col18, this.col19, this.col20, this.col21, this.col22, this.col23, this.col24, this.col25, this.col26, this.col27, this.col28, this.col29, this.col30, this.col31, this.col32, this.col33, this.col34, this.col35, this.col36);
    }
}
