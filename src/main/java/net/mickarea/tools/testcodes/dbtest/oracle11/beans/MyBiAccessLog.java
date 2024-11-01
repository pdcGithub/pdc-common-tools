/******************************************************************************************************

This file is automatically generated.
This file "MyBiAccessLog.java" is part of project <"pdc-common-tools"> , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.oracle11.beans;

import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.utils.Stdout;

/**
 * 一个普通的 java bean 类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年7月3日
 */
@MyTableOrView(name="MY_BI_ACCESS_LOG")
public class MyBiAccessLog {

    @MyIdGroup
    @MyColumn(name="access_id", displayName="访问记录ID，唯一主键", extProperty="")
    private String accessId;

    @MyColumn(name="access_ip", displayName="访问者IP地址信息", extProperty="")
    private String accessIp;

    @MyColumn(name="login_user_id", displayName="登录用户的ID", extProperty="")
    private String loginUserId;

    @MyColumn(name="access_url", displayName="访问的url", extProperty="")
    private String accessUrl;

    @MyColumn(name="access_type", displayName="访问类型（主站访问main，博客访问blog）", extProperty="")
    private String accessType;

    @MyColumn(name="access_obj_id", displayName="访问的博客对象id", extProperty="")
    private String accessObjId;

    @MyColumn(name="access_time", displayName="访问时间，精确到毫秒", extProperty="")
    private java.sql.Timestamp accessTime;

    @MyColumn(name="access_timestamp", displayName="访问时间，时间戳", extProperty="")
    private java.sql.Timestamp accessTimestamp;

    /**
     * 构造函数
     */
    public MyBiAccessLog() {
        // TODO Auto-generated constructor stub
    }
    public MyBiAccessLog(String accessId, String accessIp, String loginUserId, String accessUrl, String accessType, String accessObjId, java.sql.Timestamp accessTime, java.sql.Timestamp accessTimestamp) {
        this.accessId=accessId;
        this.accessIp=accessIp;
        this.loginUserId=loginUserId;
        this.accessUrl=accessUrl;
        this.accessType=accessType;
        this.accessObjId=accessObjId;
        this.accessTime=accessTime;
        this.accessTimestamp=accessTimestamp;
    }

    // getter and setter
    public String getAccessId() {
        return accessId;
    }
    public void setAccessId(String accessId) {
        this.accessId=accessId;
    }
    public String getAccessIp() {
        return accessIp;
    }
    public void setAccessIp(String accessIp) {
        this.accessIp=accessIp;
    }
    public String getLoginUserId() {
        return loginUserId;
    }
    public void setLoginUserId(String loginUserId) {
        this.loginUserId=loginUserId;
    }
    public String getAccessUrl() {
        return accessUrl;
    }
    public void setAccessUrl(String accessUrl) {
        this.accessUrl=accessUrl;
    }
    public String getAccessType() {
        return accessType;
    }
    public void setAccessType(String accessType) {
        this.accessType=accessType;
    }
    public String getAccessObjId() {
        return accessObjId;
    }
    public void setAccessObjId(String accessObjId) {
        this.accessObjId=accessObjId;
    }
    public java.sql.Timestamp getAccessTime() {
        return accessTime;
    }
    public void setAccessTime(java.sql.Timestamp accessTime) {
        this.accessTime=accessTime;
    }
    public java.sql.Timestamp getAccessTimestamp() {
        return accessTimestamp;
    }
    public void setAccessTimestamp(java.sql.Timestamp accessTimestamp) {
        this.accessTimestamp=accessTimestamp;
    }

    @Override
    public String toString() {
        String s = "MyBiAccessLog{accessId:%s, accessIp:%s, loginUserId:%s, accessUrl:%s, accessType:%s, accessObjId:%s, accessTime:%s, accessTimestamp:%s}";
        return Stdout.fplToAnyWhere(s, this.accessId, this.accessIp, this.loginUserId, this.accessUrl, this.accessType, this.accessObjId, this.accessTime, this.accessTimestamp);
    }
}
