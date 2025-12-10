/******************************************************************************************************

This file "TestDataTypes3.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.query.CondiParam;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestB;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.RandomUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;

/**
 * &gt;&gt;&nbsp;数据类型测试
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年4月4日-2024年4月5日
 */
public class TestDataTypes3 {
	
	public static void main(String[] args) {
		
		//获取数据库连接池管理对象
		DatabasePoolManager dbMana = DatabasePoolManager.getInstance();
		//创建datasource
		DataSource ds = dbMana.getDataPoolByName("jar_primary");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds);
		//开始插入数据
		List<TestB> insertParams = new ArrayList<TestB>();
		insertParams.add(new TestB(new BigInteger("0"), 78, 91L, "测试数据"+RandomUtil.genLetterStringWithoutException(4), 
				         new BigDecimal("98.2"), new BigDecimal("98.225")));
		String insertExecMsg = db.insertEntity(insertParams);
		if(StrUtil.isEmptyString(insertExecMsg)) {
			Stdout.pl("数据插入【成功】");
		}else {
			Stdout.pl("数据插入【失败】，异常信息："+insertExecMsg);
		}
		//展示数据
		CondiParamList condiParams = new CondiParamList();
		condiParams.addParam(new CondiParam("testAId", 78, CondiParam.OPT_EQ, null, TestB.class));
		StringBuffer execMsg = new StringBuffer();
		List<TestB> resultList = db.queryEntity(TestB.class, condiParams, execMsg);
		//
		if(StrUtil.isEmptyString(execMsg.toString())) {
			Stdout.pl("执行数据查询【成功】");
		}else {
			Stdout.pl("执行数据查询【失败】，异常信息："+execMsg.toString());
		}
		//
		if(ListUtil.isEmptyList(resultList)) {
			Stdout.pl("查询无数据返回");
		}else {
			Stdout.pl("查询获得数据返回如下：");
			Stdout.pl(resultList);
		}
		//资源回收
		dbMana.destroyDataPools();
	}

}
