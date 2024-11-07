/******************************************************************************************************

This file "TestEntityUpdate2.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
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
import net.mickarea.tools.models.query.PageInfo;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestA;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.RandomUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;

/**
 * 关于更新的实体操作测试2
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月8日-2024年7月2日
 */
public class TestEntityUpdate2 {

	public static void main(String[] args) {
		
		//生成数据库操作对象 
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_primary");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds);
		
		StringBuffer queryMsg = new StringBuffer();
		CondiParamList condiList = new CondiParamList();
		condiList.addParam(new CondiParam("id", 70, CondiParam.OPT_GTE)); // id >= 16 and id < 25
		condiList.addParam(new CondiParam("id", 78, CondiParam.OPT_LT));
		PageInfo page = new PageInfo();
		page.setPageSize(10000); //分页参数可以设置大些
		
		//查询指定的内容出来
		List<TestA> testAList = db.queryEntity(TestA.class, condiList, null, page, queryMsg); 
		
		if(StrUtil.isEmptyString(queryMsg.toString()) && !ListUtil.isEmptyList(testAList)) {
			
			//遍历并且设置一个新值
			for(TestA a: testAList) {
				a.setName(RandomUtil.genLetterStringWithoutException(12));
				a.setAddress("阳江市");
			}
			
			//更新
			Stdout.pl("更新开始");
			String updateMsg = db.updateEntity(testAList, "name, address");
			Stdout.pl("执行情况："+updateMsg);
			
		}else {
			Stdout.pl("执行情况："+queryMsg);
			Stdout.pl(testAList);
		}
		
		//回收资源
		mana.destroyDataPools();
	}
	
}
