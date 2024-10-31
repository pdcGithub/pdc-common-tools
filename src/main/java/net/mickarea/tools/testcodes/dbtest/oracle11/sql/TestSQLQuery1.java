/******************************************************************************************************

This file "TestSQLQuery1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.oracle11.sql;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.testcodes.dbtest.oracle11.beans.vo.TestOrclDataTypeVO;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * >> 数据库信息查询操作，以sql形式，测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月20日-2024年6月21日
 */
public class TestSQLQuery1 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_oracle");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_ORACLE);
		
		//执行查询
		List<Object> params = Arrays.asList(6);
		StringBuffer execMsg = new StringBuffer();
		List<TestOrclDataTypeVO> result = db.querySQL(TestOrclDataTypeVO.class, 
				                                      "select * from test_orcl_data_type where seq_no=?", 
				                                      params, 
				                                      execMsg);
		//
		if(StrUtil.isEmptyString(execMsg.toString())) {
			Stdout.pl("执行成功");
			Stdout.pl("执行结果："+result);
			Stdout.pl("BLOB 数据："+result.get(0).getVBlob());
		}else {
			Stdout.pl("执行失败");
			Stdout.pl(execMsg.toString());
		}
		//
		Stdout.pl("测试日志记录。。。");
		//
		mana.destroyDataPools();
	}
	
}
