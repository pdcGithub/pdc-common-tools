/******************************************************************************************************

This file "TestPools1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8;

import java.util.regex.Pattern;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.models.query.PageInfo;
import net.mickarea.tools.models.query.ProcParam;
import net.mickarea.tools.models.query.ProcParamList;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBStaticUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * 测试连接池方式访问数据库，程序是否正常
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月11日-2024年4月3日
 */
public class TestPools1 {
	
	public static void main(String[] args) throws Exception {
		
		Stdout.pl("开始创建连接池...");
		
		//获取数据库连接池管理对象
		DatabasePoolManager dbMana = DatabasePoolManager.getInstance();
		DBOperator dbOpt = new DBOperator(dbMana.getDataPoolByName("jar_primary"));
		
		//分页对象设置
		PageInfo p = new PageInfo();
		
		ProcParamList params = new ProcParamList();
		params.addParam(new ProcParam(ProcParam.TYPE_IN, "table_name_str","test_b"));
		params.addParam(new ProcParam(ProcParam.TYPE_IN, "where_str", "score > 50"));
		params.addParam(new ProcParam(ProcParam.TYPE_IN, "order_by_str", ""));
		params.addParam(new ProcParam(ProcParam.TYPE_IN, "page_size", p.getPageSize()));
		params.addParam(new ProcParam(ProcParam.TYPE_IN, "page_num", p.getPageNum()));
		params.addParam(new ProcParam(ProcParam.TYPE_OUT,"total_count", ""));
		params.addParam(new ProcParam(ProcParam.TYPE_OUT,"result_str", ""));
		params.addParam(new ProcParam(ProcParam.TYPE_OUT,"result_sql", ""));
		params.addParam(new ProcParam(ProcParam.TYPE_OUT,"count_sql", ""));
		
		//执行存储过程
		SimpleDBData sdb = dbOpt.executeProcedure("page_procedure", params);
		//打印参数信息
		Stdout.pl(params);
		
		String runningInfo = params.getParam("result_str").getParamValue().toString();
		if(sdb.getResponseStatus().equals(DBStaticUtil.OK)) {
			if(Pattern.matches(".*失败.*", runningInfo)) {
				Stdout.pl("存储过程内部异常："+runningInfo);
			}else {
				//分页信息设置
				p.setTotalCount(Integer.valueOf(params.getParam("total_count").getParamValue().toString()));
				p.setTotalPage( (p.getTotalCount()/p.getPageSize()) + ((p.getTotalCount()%p.getPageSize())>0?1:0) );
				//打印分页信息
				Stdout.pl(p);
				//打印数据对象
				Stdout.pl(sdb);
			}
		} else {
			Stdout.pl("调用发生错误："+sdb.getResponseInfo());
		}
		
		Stdout.pl("开始关闭连接池...");
		
		dbMana.destroyDataPools();
		
	}

}
