/******************************************************************************************************

This file "TestEntityQuery1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.oracle11.enti;

import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.query.CondiParam;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.models.query.OrderParam;
import net.mickarea.tools.models.query.OrderParamList;
import net.mickarea.tools.models.query.PageInfo;
import net.mickarea.tools.testcodes.dbtest.oracle11.beans.TestOrclDataType;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 实体对象查询功能测试，第一阶段
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年7月1日-2024年7月2日
 */
public class TestEntityQuery1 {
	
	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_oracle");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_ORACLE);
		
		//分页参数
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageSize(8);
		
		//条件参数
		CondiParamList condiList = new CondiParamList(TestOrclDataType.class);
		condiList.addParam(new CondiParam("appDate", null, CondiParam.OPT_IS_NOT_NULL));
		condiList.addParam(new CondiParam("vClob", "FI", CondiParam.OPT_LIKE));
		
		//排序参数
		OrderParamList orderList = new OrderParamList(TestOrclDataType.class);
		orderList.addParam(new OrderParam("cname", OrderParam.OPT_ASC));
		orderList.addParam(new OrderParam("appDate", OrderParam.OPT_DESC));
		
		//执行查询
		StringBuffer execMsg = new StringBuffer();
		List<TestOrclDataType> resultList = db.queryEntity(TestOrclDataType.class, condiList, orderList, pageInfo, execMsg);
		if(StrUtil.isEmptyString(execMsg.toString())) {
			Stdout.pl("执行查询成功。");
			if(ListUtil.isEmptyList(resultList)) {
				Stdout.pl("执行的查询，没有返回结果");
			}else {
				Stdout.pl(pageInfo);
				Stdout.pl("结果的遍历如下：");
				resultList.stream().forEach(Stdout::pl);
			}
		}else {
			Stdout.pl("执行查询失败。"+execMsg.toString());
		}
		
		//回收资源
		mana.destroyDataPools();
	}

}
