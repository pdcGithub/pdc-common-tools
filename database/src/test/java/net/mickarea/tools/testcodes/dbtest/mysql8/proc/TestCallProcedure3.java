/******************************************************************************************************

This file "TestCallProcedure3.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.proc;

import java.sql.Types;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.models.query.ProcParam;
import net.mickarea.tools.models.query.ProcParamList;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;
import net.mickarea.tools.utils.database.DBStaticUtil;

/**
 * 测试关于调整 数据库工具类后，是否对原 MySQL 存储过程有影响
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月28日
 */
public class TestCallProcedure3 {

	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_primary");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds, DBSQLInjectionUtil.DBTYPE_MYSQL);
		
		//以存储过程动态查询信息
		ProcParamList params = new ProcParamList();
		params.addParam(new ProcParam(ProcParam.TYPE_IN, "table_name_str", "bi_access_log"));
		params.addParam(new ProcParam(ProcParam.TYPE_IN, "where_str", "login_user_id = 'USR20231023172429187MY44977HLA'"));
		params.addParam(new ProcParam(ProcParam.TYPE_IN, "order_by_str", "access_time desc"));
		params.addParam(new ProcParam(ProcParam.TYPE_IN, "page_size", 10));
		params.addParam(new ProcParam(ProcParam.TYPE_IN, "page_num", 19));
		params.addParam(new ProcParam(ProcParam.TYPE_OUT,"total_count", 0).setOutParamSqlType(Types.INTEGER));
		params.addParam(new ProcParam(ProcParam.TYPE_OUT,"result_str", ""));
		params.addParam(new ProcParam(ProcParam.TYPE_OUT,"result_sql", ""));
		params.addParam(new ProcParam(ProcParam.TYPE_OUT,"count_sql", ""));
		
		//开始查询
		SimpleDBData sdb = db.executeProcedure("page_procedure", params);
		
		//参数获取
		Integer totalCount = (Integer)params.getParam("total_count").getParamValue();
		String result_str = (String)params.getParam("result_str").getParamValue();
		String result_sql = (String)params.getParam("result_sql").getParamValue();
		String count_sql = (String)params.getParam("count_sql").getParamValue();
		
		if(sdb.getResponseStatus().equals(DBStaticUtil.OK)) {
			Stdout.pl("存储过程执行成功:: ");
			if(!Pattern.matches(".*失败.*", result_str)) {
				Stdout.pl(sdb);
			}else {
				Stdout.pl("存储过程执行分页查询异常：" + result_str);
			}
		}else {
			Stdout.pl("存储过程执行失败::");
		}
		
		//输出数据
		Stdout.pl("totalCount: "+totalCount);
		Stdout.pl("result_str: "+result_str);
		Stdout.pl("result_sql: "+result_sql);
		Stdout.pl("count_sql: "+count_sql);
		
		//释放数据库连接池
		mana.destroyDataPools();
		
	}

}
