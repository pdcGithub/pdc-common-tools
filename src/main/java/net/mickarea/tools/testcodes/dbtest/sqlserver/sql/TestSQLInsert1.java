/******************************************************************************************************

This file "TestSQLInsert1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.sql;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import microsoft.sql.DateTimeOffset;
import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.RandomUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.TimeUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * >> 数据库信息插入操作，以sql形式，测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月28日
 */
public class TestSQLInsert1 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		String preSql = "insert into dbo.my_data_types(col_1, col_2, col_3, col_4, col_5, col_6,       \r\n"
				      + "                              col_7, col_8, col_9, col_10, col_11, col_12,    \r\n"
				      + "							   col_13, col_14, col_15, col_16, col_17, col_18, \r\n"
				      + "							   col_19, col_20, col_21, col_22, col_23, col_24, \r\n"
				      + "							   col_25, col_26, col_27, col_28, col_29, col_30, \r\n"
				      + "							   col_31, col_32, col_33, col_34, col_35, col_36) \r\n"
				      + "						values(?, ?, ?, ?, ?, ?, \r\n"
				      + "							   ?, ?, ?, ?, ?, ?, \r\n"
				      + "							   ?, ?, ?, ?, ?, ?, \r\n"
				      + "							   ?, ?, ?, ?, ?, ?, \r\n"
				      + "							   ?, ?, ?, ?, DEFAULT, ?, \r\n"
				      + "							   ?, ?, ?, ?, ?, ?  \r\n"
				      + "							  )";
		
		//二维参数列表
		List<Object> params = new ArrayList<Object>();
		//当前时间
		LocalDateTime now = LocalDateTime.now();
		
		//定义插入的参数列表
		List<Object> line1 = new ArrayList<Object>();
		
		//col_1 bigint
		line1.add(Integer.parseInt(RandomUtil.genNumberStringWitoutException(5)));
		//col_2 binary(50)
		line1.add("abcdefgh".getBytes());
		//col_3 bit
		line1.add(new Boolean(true));
		//col_4 char(10)
		line1.add(RandomUtil.genLetterStringWitoutException(10));
		//col_5 date
		line1.add(new Date(TimeUtil.getTimestampByLocalDateTime(now).getTime()));
		//col_6 datetime
		line1.add(TimeUtil.getTimestampByLocalDateTime(now));
		//col_7 datetime2(7)
		line1.add(TimeUtil.getTimestampByLocalDateTime(now));
		//col_8 datetimeoffset(7)
		line1.add(DateTimeOffset.valueOf(TimeUtil.getTimestampByLocalDateTime(now), Calendar.getInstance()));
		//col_9 decimal(18,0)
		line1.add(new BigDecimal("12.123"));
		//col_10 float
		line1.add(456.789123456);
		//col_11 geography
		line1.add(null);
		//col_12 geometry
		line1.add(null);
		//col_13 hierarchyid
		line1.add("xiusodflskdjf".getBytes());
		//col_14 image
		line1.add("x90sdf90dxufd".getBytes());
		//col_15 int
		line1.add(1000);
		//col_16 money
		line1.add(new BigDecimal("250.12"));
		//col_17 nchar(10)
		line1.add(RandomUtil.genLetterStringWitoutException(10));
		//col_18 ntext
		line1.add(RandomUtil.genLetterStringWitoutException(20));
		//col_19 numeric(18, 0)
		line1.add(new BigDecimal("125555"));
		//col_20 nvarchar(50)
		line1.add(RandomUtil.genLetterStringWitoutException(5));
		//col_21 nvarchar(MAX)
		line1.add(RandomUtil.genLetterStringWitoutException(5));
		//col_22 real
		line1.add(234.12f);
		//col_23 smalldatetime
		line1.add(TimeUtil.getTimestampByLocalDateTime(now));
		//col_24 smallint
		line1.add(new Short("8"));
		//col_25 smallmoney
		line1.add(new BigDecimal("124"));
		//col_26 sql_variant
		line1.add("a:b");
		//col_27 text
		line1.add(RandomUtil.genLetterStringWitoutException(5));
		//col_28 time(7)
		line1.add(new Time(TimeUtil.getTimestampByLocalDateTime(now).getTime()));
		//col_29 timestamp
		//line1.add("DEFAULT");
		//col_30 tinyint
		line1.add(new Short("10"));
		//col_31 uniqueidentifier
		line1.add(null);
		//col_32 varbinary(50)
		line1.add("sdfdsf".getBytes());
		//col_33 varbinary(MAX)
		line1.add("32e32feewffds".getBytes());
		//col_34 varchar(50)
		line1.add(RandomUtil.genLetterStringWitoutException(5));
		//col_35 varchar(MAX)
		line1.add(RandomUtil.genLetterStringWitoutException(5));
		//col_36 xml
		line1.add("<name>Tom</name>");
		
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
