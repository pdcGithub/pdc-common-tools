/******************************************************************************************************

This file "TestEntityUpdate1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8.enti;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestA;
import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBStaticUtil;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.RandomUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 关于更新的实体操作测试1
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月8日-2024年7月2日
 */
public class TestEntityUpdate1 {

	public static void main(String[] args) {
		
		//生成数据库操作对象 
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_primary");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds);
		
		StringBuffer sb = new StringBuffer();
		List<TestA> objectList = db.queryEntity(TestA.class, sb); //把数据第一页查出来，看看更新数据要多久。。。
		
		if(StrUtil.isEmptyString(sb.toString())) {
			
			if(!ListUtil.isEmptyList(objectList)) {

				for(TestA a: objectList) {
					String randomName = null;
					try {
						randomName = RandomUtil.genLetterString(30);
					} catch (Exception e) {
						randomName = "ERROR";
					}
					a.setName(randomName); //修改名字
				}
				
				Connection conn = null;
				try {
					conn = db.getDb().getConnection();
					DBStaticUtil.updateEntity(conn, objectList, "name"); //执行更新
					conn.commit();
					Stdout.pl("执行成功，事务提交。");
					
					List<TestA> queryResults = db.queryEntity(TestA.class, sb); //再次查询
					Stdout.pl(sb.toString());
					Stdout.pl("{{");
					if(!ListUtil.isEmptyList(queryResults)) {
						for(TestA a:queryResults) {
							Stdout.pl(a);
						}
					}
					Stdout.pl("}}");
					
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
				Stdout.pl("执行查询后，结果为空，无数据可处理");
			}
		} else {
			Stdout.pl("执行查询出错，"+sb.toString());
		}
		
		//回收资源
		mana.destroyDataPools();
	}
	
}
