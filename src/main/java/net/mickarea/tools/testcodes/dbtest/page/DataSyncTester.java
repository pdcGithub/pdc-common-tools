/******************************************************************************************************

This file "DataSyncTester.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.page;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.db.DBTrans;
import net.mickarea.tools.models.db.DBTransAction;
import net.mickarea.tools.models.query.PageInfo;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.BiAccessLog;
import net.mickarea.tools.testcodes.dbtest.oracle11.beans.MyBiAccessLog;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;
import net.mickarea.tools.utils.database.DBStaticUtil;

/**
 * 一个简易的数据同步测试，从 mysql 将数据 同步到 oracle 
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年7月3日
 */
public class DataSyncTester {
	
	public static void main(String[] args) {
		
		//创建管理类
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		
		//获取数据库连接池对象（mysql）
		DataSource mysqlDS = mana.getDataPoolByName("jar_primary");
		//创建数据库操作对象（mysql）
		DBOperator mysqlDB = new DBOperator(mysqlDS);

		//获取数据库连接池对象（oracle）
		DataSource oracleDS = mana.getDataPoolByName("jar_oracle");
		//创建数据库操作对象（oracle）
		DBOperator oracleDB = new DBOperator(oracleDS, DBSQLInjectionUtil.DBTYPE_ORACLE);
		
		//加载 mysql 数据库中的访问日志
		StringBuffer execMsg = new StringBuffer();
		List<BiAccessLog> mysqlLogs = mysqlDB.queryEntity(BiAccessLog.class, null, null, new PageInfo(1, 20000), execMsg);
		if(StrUtil.isEmptyString(execMsg.toString())) {
			//查询成功，如果有内容，则继续
			if(!ListUtil.isEmptyList(mysqlLogs)) {
				//将记录插入 oracle 数据库
				DBTrans myTrans = new DBTransAction(oracleDB) {
					@Override
					public void doTrans(Connection conn) throws Exception {
						//这里开始 事务 同步处理
						//清空 oracle 的 my_bi_access_log 表
						DBStaticUtil.deleteSQL(conn, "delete from my_bi_access_log where 1=?", Arrays.asList(1));
						//将 BiAccessLog 对象，转换为 MyBiAccessLog 对象
						List<MyBiAccessLog> oracleAccessLogs = mysqlLogs
								.stream()
								.collect(Collector.of(
										//提供一个装载的对象
										ArrayList<MyBiAccessLog>::new, 
										(list, item)->{
											//业务处理（将对象转换）
											list.add(item.translateToMyBiAccessLog());
										}, 
										(left, right)->{
											//资源合并
											left.addAll(right);
											return left;
										}, 
										Collector.Characteristics.IDENTITY_FINISH)
								);
						//将 MyBiAccessLog 对象，写入 oracle 数据库
						DBStaticUtil.insertEntity(conn, oracleAccessLogs);
						//执行结束，事务由 DBTransAction 提交，如果抛异常，则由 DBTransAction 回滚 。。。
					}
				};
				//执行
				myTrans.run();
				//事务执行结束
				Stdout.pl("事务执行结束...");
			}else {
				Stdout.pl("没有什么记录可以转义到 oracle 中。请检查");
			}
		}else {
			Stdout.pl("执行 mysql 查询失败，"+execMsg.toString());
		}
		
		//将数据库连接池 关闭
		mana.destroyDataPools();
	}

}
