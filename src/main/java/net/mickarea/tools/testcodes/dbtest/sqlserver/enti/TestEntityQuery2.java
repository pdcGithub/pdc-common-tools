/******************************************************************************************************

This file "TestEntityQuery1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.enti;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.query.CondiParam;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.models.query.OrderParam;
import net.mickarea.tools.models.query.OrderParamList;
import net.mickarea.tools.models.query.PageInfo;
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.MyDataTypes;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 实体对象查询功能测试，第一阶段（使用sql语句模板查询）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月31日
 */
public class TestEntityQuery2 {
	
	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_sqlserver");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		
		//分页参数
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageSize(3);
		
		//条件参数
		String sqlTemplate = " (:date1 and :date2) or :content ";
		CondiParamList condiList = new CondiParamList(sqlTemplate, MyDataTypes.class);
		condiList.addParam(new CondiParam("col7", 
							LocalDate.now(), 
							CondiParam.OPT_GTE).setSqlTemplateParamName("date1"));
		condiList.addParam(new CondiParam("col7", 
							LocalDate.now().plusDays(1), 
							CondiParam.OPT_LT).setSqlTemplateParamName("date2"));
		condiList.addParam(new CondiParam("col35", 
							"字段", 
							CondiParam.OPT_LIKE).setSqlTemplateParamName("content"));
		
		//排序参数
		OrderParamList orderList = new OrderParamList(MyDataTypes.class);
		orderList.addParam(new OrderParam("col1", OrderParam.OPT_ASC));
		orderList.addParam(new OrderParam("col4", OrderParam.OPT_DESC));
		
		//执行查询
		StringBuffer execMsg = new StringBuffer();
		List<MyDataTypes> resultList = db.queryEntity(MyDataTypes.class, condiList, orderList, pageInfo, execMsg);
		if(StrUtil.isEmptyString(execMsg.toString())) {
			Stdout.pl("执行查询成功。");
			if(ListUtil.isEmptyList(resultList)) {
				Stdout.pl("执行的查询，没有返回结果");
			}else {
				Stdout.pl(pageInfo);
				Stdout.pl("结果的遍历如下：");
				resultList.forEach(Stdout::pl);
			}
		}else {
			Stdout.pl("执行查询失败。"+execMsg.toString());
		}
		
		//回收资源
		mana.destroyDataPools();
	}

}
