/******************************************************************************************************

This file "TestEntityQuery1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
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
 * 实体对象查询功能测试，第一阶段
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月13日-2024年4月3日
 */
public class TestEntityQuery1 {
	
	public static void main(String[] args) {
		
		//获取数据库链接处理
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		DataSource dbSource = mana.getDataPoolByName("jar_primary");
		DBOperator db = new DBOperator(dbSource);
		
		//分页参数
		PageInfo page = new PageInfo();
		
		//执行信息，如果报错之类的会有内容返回
		StringBuffer execMsg = new StringBuffer();
		
		//条件参数
		//CondiParamList condiList = new CondiParamList();
		//condiList.addParam(new CondiParam().setClassName(ViewAB.class).setParamName("score").setOptString(CondiParam.OPT_IS_NULL));
		//condiList.addParam(new CondiParam().setParamName("update_time").setOptString(CondiParam.OPT_IS_NOT_NULL));
		//condiList.addParam(new CondiParam().setClassName(ViewAB.class).setParamName("gender").setValue("男"));
		
		//条件参数
		CondiParamList condiList = new CondiParamList("(:score or :updateTime) and :gender");
		condiList.addParam(new CondiParam("score", "", CondiParam.OPT_IS_NULL).setClassName(ViewAB.class));
		condiList.addParam(new CondiParam("updateTime", "", CondiParam.OPT_IS_NOT_NULL).setClassName(ViewAB.class));
		condiList.addParam(new CondiParam("gender", "' or 1=1; drop table test_a; select 1 '").setClassName(ViewAB.class));
		
		//排序参数
		OrderParamList orderList = new OrderParamList();
		orderList.addParam(new OrderParam().setParamName("exam_score"));
		orderList.addParam(new OrderParam().setClassName(ViewAB.class).setParamName("finals"));
		orderList.addParam(new OrderParam().setClassName(ViewAB.class).setParamName("id").setOrderOpt(OrderParam.OPT_DESC));
		
		//执行查询
		List<ViewAB> dataList = db.queryEntity(ViewAB.class, condiList, orderList, page, execMsg);
		
		Stdout.fpl("执行结果：%s", StrUtil.isEmptyString(execMsg.toString())?"成功":"失败" );
		Stdout.fpl("分页数据：%s", page);
		
		//数据展示
		Stdout.pl("{{");
		if(dataList!=null) {
			for(ViewAB v: dataList) {
				Stdout.pl(v);
			}
		}
		Stdout.pl("}}");
		
	}

}
