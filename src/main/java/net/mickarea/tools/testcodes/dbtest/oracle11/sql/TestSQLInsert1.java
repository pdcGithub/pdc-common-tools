/******************************************************************************************************

This file "TestSQLInsert1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.oracle11.sql;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.RandomUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 数据库信息插入操作，以sql形式，测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月21日
 */
public class TestSQLInsert1 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_oracle");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_ORACLE);
		
		String preSql = "insert into test_orcl_data_type("
				+ "SEQ_NO,  CNAME,          APP_DATE,          V_YEAR,       V_MONTH,  "
				+ "V_DAY,   APP_DATETIME_1, APP_DATETIME_2,    CONTENT,      V_ROW2,   "
				+ "V_CLOB,  V_NCLOB,        V_BLOB,            V_NVARCHAR2,  V_CHAR,   "
				+ "V_RAW,   V_FLOAT,        V_INT,             V_LONG,       V_NUM_1,  "
				+ "V_NUM_2, MY_FLOAT,       MY_FLOAT_2) "
				+ "values("
				+ "seq_test_1_no.nextval, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, "
				+ "?, ?, ? "
				+ ")";
		
		List<Object> params = new ArrayList<Object>();
		
		//当前时间
		LocalDateTime now = LocalDateTime.now();
		
		//待插入的数据
		List<Object> line1 = new ArrayList<Object>();
		line1.add("测试名称");
		line1.add(Timestamp.valueOf(now));
		line1.add(Timestamp.valueOf(now.withNano(0).withSecond(0).withMinute(0).withHour(0).withDayOfYear(1)));
		line1.add(Timestamp.valueOf(now.withNano(0).withSecond(0).withMinute(0).withHour(0).withDayOfMonth(1)));
		
		line1.add(Timestamp.valueOf(now.withNano(0).withSecond(0).withMinute(0).withHour(0)));
		line1.add(Timestamp.valueOf(now));
		line1.add(Timestamp.valueOf(now));
		line1.add("测试内容呢？");
		line1.add(new oracle.sql.ROWID(RandomUtil.genNumAndLetterMixedStringWitoutException(10).getBytes()));
		
		line1.add("CLOB:测试内容");
		line1.add("NCLOB:测试内容2");
		line1.add("BLOB:测试内容3".getBytes());
		line1.add("NVARCHAR2:nvarchar测试111");
		line1.add("V_CHAR:char测试");
		
		line1.add("V_RAW:测试".getBytes());
		line1.add(2.5);
		line1.add(54321);
		line1.add(12364789);
		line1.add(55555.25);
		
		line1.add(2.568);
		line1.add(3.15);
		line1.add(3.152458);
		
		//放入二维参数列表
		params.add(line1);
		
		//执行
		String execMsg = db.insertSQL(preSql, params);
		if(StrUtil.isEmptyString(execMsg)) {
			Stdout.pl("数据插入，执行成功！");
		}else {
			Stdout.pl("数据插入，执行失败！");
			Stdout.pl(execMsg);
		}
		//
		mana.destroyDataPools();
	}
	
}
