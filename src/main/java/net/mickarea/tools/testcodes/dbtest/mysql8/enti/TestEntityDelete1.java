/******************************************************************************************************

This file "TestEntityDelete1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.enti;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestB;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBStaticUtil;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 关于删除的实体操作测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月1日-2024年4月3日
 */
public class TestEntityDelete1 {
	
	public static void main(String[] args) {
		
		//生成数据库操作对象
		DBOperator db = new DBOperator(DatabasePoolManager.getInstance().getDataPoolByName("jar_primary"));
		
		StringBuffer sb = new StringBuffer();
		List<TestB> objectList = db.queryEntity(TestB.class, sb); //把数据一次性查出来，看看删除数据要多久。。。
		
		if(StrUtil.isEmptyString(sb.toString())) {
			if(!ListUtil.isEmptyList(objectList)) {
				Connection conn = null;
				try {
					conn = db.getDb().getConnection();
					DBStaticUtil.deleteEntity(conn, objectList);
					conn.commit();
					Stdout.pl("执行成功，事务提交。");
				} catch (Exception e) {
					Stdout.pl(e); //获取报错信息
					//数据回滚
					if(conn!=null) {
						try {
							conn.rollback(); Stdout.pl("数据库操作回滚");
						} catch (SQLException e1) {
							Stdout.pl(e1); Stdout.pl("数据库操作回滚失败");
						}
					}
				} finally {
					//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
					if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
				}
			}else {
				Stdout.pl("执行查询后，结果为空，无数据可删除");
			}
		} else {
			Stdout.pl("执行查询出错，"+sb.toString());
		}
		
	}

}
