/******************************************************************************************************

This file "TestEntityUpdate1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.enti;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.query.CondiParam;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.MyDataTypes;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.TimeUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 关于更新的实体操作测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月31日-2024年11月28日
 */
public class TestEntityUpdate1 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		//定义当前时间
		LocalDate nowDate = LocalDate.now();
		
		//先把要更新的内容查询出来
		Timestamp nowT = TimeUtil.getTimestampByLocalDateTime(nowDate.atStartOfDay());
		Timestamp nextT = TimeUtil.getTimestampByLocalDateTime(nowDate.plusDays(1).atStartOfDay());
		
		CondiParamList condiList = new CondiParamList("( :date1 and :date2 )", MyDataTypes.class);
		condiList.addParam(new CondiParam("col6", nowT, CondiParam.OPT_GTE).setSqlTemplateParamName("date1"));
		condiList.addParam(new CondiParam("col6", nextT, CondiParam.OPT_LT).setSqlTemplateParamName("date2"));
		
		StringBuffer execMsg = new StringBuffer();
		List<MyDataTypes> targetList = db.queryEntity(MyDataTypes.class, condiList, execMsg);
		if(StrUtil.isEmptyString(execMsg.toString())) {
			Stdout.pl("执行查询成功（记录数有："+(targetList==null?0:targetList.size())+"），信息如下：");
			if(!ListUtil.isEmptyList(targetList)) {
				//打印找到的数据
				Stdout.pl();
				targetList.forEach(Stdout::pl);
				Stdout.pl();
				//开始执行更新
				targetList.forEach((data)->{
					data.setCol9(new BigDecimal("3333"));
					data.setCol32("测试一下吧".getBytes());
				});
				
				//写入数据库
				
				//这里测试，单独更新 几个数据库字段
				String msg = db.updateEntity(targetList, "col9, col32");
				
				//这里测试的是 整行全部更新
				//String msg = db.updateEntity(targetList, null); 
				
				if(StrUtil.isEmptyString(msg)) {
					Stdout.pl("写入数据成功。");
				}else {
					Stdout.pl("写入数据失败,"+msg);
				}
			}else {
				Stdout.pl("查询到的结果为：0");
			}
		}else {
			Stdout.pl("执行查询失败，信息如下："+execMsg.toString());
		}
	
		//回收资源
		mana.destroyDataPools();
		
	}
	
}
