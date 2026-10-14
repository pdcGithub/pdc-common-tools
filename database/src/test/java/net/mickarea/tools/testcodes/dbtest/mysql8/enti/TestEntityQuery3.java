/******************************************************************************************************

This file "TestEntityQuery3.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.enti;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.vo.TestATempVO;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 实体对象查询功能测试，第三阶段
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月18日-2024年4月3日
 */
public class TestEntityQuery3 {
	
	public static void main(String[] args) {
		
		//获取数据库链接处理
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		DataSource dbSource = mana.getDataPoolByName("jar_primary");
		DBOperator db = new DBOperator(dbSource);
		
		String sql = "select current_date() date1, current_time() date2, current_timestamp() date3, "
				          + "id, name, contain_str(?, ?) cont from test_a a where a.name like concat(?, '%') ";
		List<Object> params = new ArrayList<Object>();
		params.add("a");
		params.add("a");
		params.add("小");
		
		StringBuffer execMsg = new StringBuffer();
		List<TestATempVO> dataList = db.querySQL(TestATempVO.class, sql, params, execMsg);
		if(StrUtil.isEmptyString(execMsg.toString())) {
			//输出结果
			if(dataList!=null && dataList.size()>0) {
				Stdout.pl("{{");
				for(TestATempVO v: dataList) {
					Stdout.pl(v);
				}
				Stdout.pl("}}");
			} else {
				Stdout.pl("查询执行成功，但没有返回数据");
			}
		} else {
			Stdout.pl(execMsg.toString());
		}
	}

}
