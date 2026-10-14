/******************************************************************************************************

This file "TestEntityInsert1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.oracle11.enti;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.testcodes.dbtest.oracle11.beans.TestOrclDataType;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 关于插入的实体操作测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年7月2日
 */
public class TestEntityInsert1 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_oracle");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_ORACLE);
		
		List<TestOrclDataType> dataList = new ArrayList<TestOrclDataType>();
		
		//定义一行数据
		TestOrclDataType row1 = new TestOrclDataType();
		row1.setSeqNo("222");
		row1.setAppDate(new Date());
		row1.setVYear(LocalDate.now());
		row1.setAppDatetime1(LocalDateTime.now());
		row1.setVClob("这是一个内容呢？");
		row1.setMyFloat2(1.234567);
		dataList.add(row1);
		
		//定义二行数据
		TestOrclDataType row2 = new TestOrclDataType();
		row2.setSeqNo("223");
		row2.setAppDate(new Date());
		row2.setVYear(LocalDate.now());
		row2.setAppDatetime1(LocalDateTime.now());
		row2.setVClob("这是一个内容呢？");
		row2.setMyFloat2(2.345678);
		dataList.add(row2);
		
		//执行插入
		String msg = db.insertEntity(dataList);
		if(StrUtil.isEmptyString(msg)) {
			Stdout.pl("插入成功");
		}else {
			Stdout.pl("插入失败");
		}
		
		//查询表记录
		StringBuffer execMsg = new StringBuffer();
		List<TestOrclDataType> results = db.queryEntity(TestOrclDataType.class, execMsg);
		if(StrUtil.isEmptyString(execMsg.toString())) {
			Stdout.pl("查询数据成功");
			results.forEach(Stdout::pl);
		}else {
			Stdout.pl("查询数据失败，"+execMsg);
		}
		
		//回收资源
		mana.destroyDataPools();
	}
	
}
