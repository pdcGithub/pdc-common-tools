/******************************************************************************************************

This file "TestEntityQuery1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.sqlserver.enti;

import java.math.BigDecimal;
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
 * 实体对象查询功能测试，第一阶段
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月31日
 */
public class TestEntityQuery1 {
	
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
		
		//条件参数（因为 col_26 是 sql_variant 数据库类型，使用了函数转换，所以，不能直接通过属性名映射）
		CondiParamList condiList = new CondiParamList();
		condiList.addParam(new CondiParam("col_1", new Long("1730356294040"), CondiParam.OPT_LTE));
		condiList.addParam(new CondiParam("col_6", LocalDate.now().atStartOfDay(), CondiParam.OPT_GTE));
		condiList.addParam(new CondiParam("col_16", new BigDecimal("456.123")));
		// col_26 是 sql_variant 数据库类型，不能直接 like ，需要转换
		condiList.addParam(new CondiParam("cast(col_26 as varchar(max))", "吧", CondiParam.OPT_LIKE));
		
		//排序参数（以对象属性进行映射）
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
