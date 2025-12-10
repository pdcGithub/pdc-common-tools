/******************************************************************************************************

This file "TestEntityQuery2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.enti;

import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.query.CondiParam;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.models.query.OrderParam;
import net.mickarea.tools.models.query.OrderParamList;
import net.mickarea.tools.models.query.PageInfo;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.ViewAB;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 实体对象查询功能测试，第二阶段
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月14日-2024年4月3日
 */
public class TestEntityQuery2 {
	
	public static void main(String[] args) {
		
		//获取数据库链接处理
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		DataSource dbSource = mana.getDataPoolByName("jar_primary");
		DBOperator db = new DBOperator(dbSource);
		
		PageInfo page = new PageInfo();
		StringBuffer execMsg = new StringBuffer();
		
		//List<ViewAB> dataList = db.queryEntity(ViewAB.class, execMsg);
		
		CondiParamList condiList = new CondiParamList();
		condiList.addParam(new CondiParam("tel", "15", CondiParam.OPT_LIKE_PRE));
		condiList.addParam(new CondiParam("id", 2, CondiParam.OPT_GT));
		condiList.addParam(new CondiParam("finals", 100, CondiParam.OPT_LT).setClassName(ViewAB.class)); //如果属性跟字段不一致，需要设置映射类
		//List<ViewAB> dataList = db.queryEntity(ViewAB.class, condiList, execMsg);
		
		OrderParamList orderList = new OrderParamList();
		orderList.addParam(new OrderParam("id"));
		orderList.addParam(new OrderParam("name", OrderParam.OPT_DESC));
		orderList.addParam(new OrderParam("finals").setClassName(ViewAB.class)); //如果属性跟字段不一致，需要设置映射类
		List<ViewAB> dataList = db.queryEntity(ViewAB.class, condiList, orderList, execMsg);
		
		Stdout.fpl("执行结果：%s", StrUtil.isEmptyString(execMsg.toString())?"成功":"失败" );
		Stdout.pl(execMsg);
		Stdout.fpl("分页数据：%s", page);
		
		//数据展示
		Stdout.pl("{{");
		if(dataList!=null) {
			for(Object v: dataList) {
				Stdout.pl(v);
			}
		}
		Stdout.pl("}}");
		
	}

}
