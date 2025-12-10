/******************************************************************************************************

This file "MySQLTest1.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.injectiontest;

import net.mickarea.tools.models.query.CondiParam;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestInsert;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 这里测试新增加的数据库处理类型 DBTYPE_MYSQL_ANSI
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年1月20日
 */
public class MySQLTest1 {

	public static void main(String[] args) {
		
		//设置查询条件
		String sqlTemplate = "(:s1 or :s2 or :s3 or :s4)";
		CondiParamList condiParams = new CondiParamList(DBSQLInjectionUtil.DBTYPE_MYSQL, sqlTemplate, TestInsert.class);
		
		CondiParam s1 = new CondiParam("strings", "NEW.ICP_URL").setSqlTemplateParamName("s1");
		CondiParam s2 = new CondiParam("strings", "' or 12=12 ; delete test_a; selec 1, '").setSqlTemplateParamName("s2");
		CondiParam s3 = new CondiParam("strings", "null").setSqlTemplateParamName("s3");
		CondiParam s4 = new CondiParam("strings", null, CondiParam.OPT_IS_NULL).setSqlTemplateParamName("s4");
		
		// 对于 value 为 null 的参数，添加时会跳过。如果确实需要null，则 OPT 设置为 OPT_IS_NULL
		condiParams.addParam(s1);
		condiParams.addParam(s2);
		condiParams.addParam(s3);
		condiParams.addParam(s4);
		
		//用 DBTYPE_MYSQL 打印一次
		Stdout.pl(condiParams.toSqlString());
		
		//修改为 DBTYPE_MYSQL_ANSI 再打印一次。
		condiParams.setCodec(DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI);
		Stdout.pl(condiParams.toSqlString());
	}

}
