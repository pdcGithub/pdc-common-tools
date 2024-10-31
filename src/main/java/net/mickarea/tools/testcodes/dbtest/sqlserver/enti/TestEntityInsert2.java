/******************************************************************************************************

This file "TestEntityInsert2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.enti;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import microsoft.sql.DateTimeOffset;
import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.query.OrderParam;
import net.mickarea.tools.models.query.OrderParamList;
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.MyDataTypes;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.TimeUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 关于插入的实体操作测试2（测试特殊的数据表，比如有特殊字段，特殊数据类型）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月31日
 */
public class TestEntityInsert2 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		List<MyDataTypes> dataList = new ArrayList<MyDataTypes>();
		
		//定义当前时间
		LocalDateTime now = LocalDateTime.now();
		Timestamp nowTimestamp = TimeUtil.getTimestampByLocalDateTime(now);
		java.sql.Date sqlDate = new java.sql.Date(nowTimestamp.getTime());
		java.sql.Time sqlTime = new java.sql.Time(nowTimestamp.getTime());
		DateTimeOffset dateTimeOffset = DateTimeOffset.valueOf(nowTimestamp, Calendar.getInstance());
		
		//定义一行数据
		MyDataTypes row1 = new MyDataTypes();
		row1.setCol1(System.currentTimeMillis());
		row1.setCol2("你好".getBytes());
		row1.setCol3(false);
		row1.setCol4("测试字段4");
		row1.setCol5(sqlDate);
		row1.setCol6(nowTimestamp);
		row1.setCol7(nowTimestamp);
		row1.setCol8(dateTimeOffset);
		row1.setCol9(new BigDecimal("123456"));
		row1.setCol10(123.4567879);
		//row1.setCol11("测试11".getBytes()); (11 和 12 是地理空间信息，无SRID 无法测试)
		//row1.setCol12("测试12".getBytes());
		row1.setCol13("测试13".getBytes());
		row1.setCol14("测试14".getBytes());
		row1.setCol15(12345678);
		row1.setCol16(new BigDecimal("456.123"));
		row1.setCol17("测试字段17");
		row1.setCol18("测试字段18");
		row1.setCol19(new BigDecimal("333333333"));
		row1.setCol20("测试字段20");
		row1.setCol21("测试字段21");
		row1.setCol22(1.222365f);
		row1.setCol23(nowTimestamp);
		row1.setCol24(new Short(Short.MAX_VALUE));
		row1.setCol25(new BigDecimal("12.255"));
		row1.setCol26("测试一下吧：阿西吧");
		row1.setCol27("水水水水");
		row1.setCol28(sqlTime);
		row1.setCol29("测试字段29".getBytes());
		row1.setCol30(new Short("255")); // 数据库 tinyint 类型 最大为 255
		// row1.setCol31("测试字段31"); uniqueidentifier 无法测试
		row1.setCol32("测试字段32".getBytes());
		row1.setCol33("测试字段33".getBytes());
		row1.setCol34("测试字段34");
		row1.setCol35("测试字段35");
		row1.setCol36("<nation>中国</nation>");
		
		//放入列表
		dataList.add(row1);
		
		//执行插入
		String msg = db.insertEntity(dataList);
		if(StrUtil.isEmptyString(msg)) {
			Stdout.pl("插入成功");
		}else {
			Stdout.pl("插入失败");
		}
		
		//设置排序
		OrderParamList orderList = new OrderParamList(MyDataTypes.class);
		orderList.addParam(new OrderParam("col1"));
		orderList.addParam(new OrderParam("col4"));
		
		//设置消息容器
		StringBuffer execMsg = new StringBuffer();
		
		//查询表记录
		List<MyDataTypes> results = db.queryEntity(MyDataTypes.class, null, orderList, execMsg);
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
