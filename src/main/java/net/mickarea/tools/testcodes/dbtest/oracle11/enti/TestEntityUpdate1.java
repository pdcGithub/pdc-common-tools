/******************************************************************************************************

This file "TestEntityUpdate1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.oracle11.enti;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.query.CondiParam;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.testcodes.dbtest.oracle11.beans.TestOrclDataType;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.RandomUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * >> 关于更新的实体操作测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年7月2日
 */
public class TestEntityUpdate1 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_oracle");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_ORACLE);
		
		//先把要更新的内容查询出来
		CondiParamList condiList = new CondiParamList("( :date1 or :date2 )", TestOrclDataType.class);
		condiList.addParam(new CondiParam("appDate", null, CondiParam.OPT_IS_NOT_NULL).setSqlTemplateParamName("date1"));
		condiList.addParam(new CondiParam("appDatetime1", null, CondiParam.OPT_IS_NULL).setSqlTemplateParamName("date2"));
		
		StringBuffer execMsg = new StringBuffer();
		List<TestOrclDataType> targetList = db.queryEntity(TestOrclDataType.class, condiList, execMsg);
		if(StrUtil.isEmptyString(execMsg.toString())) {
			Stdout.pl("执行查询成功，信息如下：");
			if(!ListUtil.isEmptyList(targetList)) {
				Stdout.pl(targetList);
				//开始执行更新
				targetList.stream().forEach((data)->{
					data.setAppDate(new Date());
					data.setAppDatetime1(LocalDateTime.now());
					data.setVYear(LocalDate.now());
					data.setVClob(RandomUtil.genLetterStringWitoutException(20));
					data.setMyFloat(1123.4567879);
				});
				//写入数据库
				String msg = db.updateEntity(targetList, "appDate, appDatetime1,  vYear, vClob, myFloat");
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
