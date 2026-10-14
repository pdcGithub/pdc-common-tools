/******************************************************************************************************

This file "TestDataTypes.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.utils.Stdout;

/**
 * &gt;&gt;&nbsp;数据类型测试
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年4月3日
 */
public class TestDataTypes {
	
	public static void main(String[] args) {
		
		//获取数据库连接池管理对象
		DatabasePoolManager dbMana = DatabasePoolManager.getInstance();
		//创建datasource
		DataSource ds = dbMana.getDataPoolByName("jar_primary");
		
		try {
			Connection conn = ds.getConnection();
			PreparedStatement preStmt = conn.prepareStatement("select * from bi_access_log b where b.access_time>='2024-3-10' and b.access_id='ACC20240310153312QV2K1212QE679'");
			boolean hasResultSet = preStmt.execute();
			if(hasResultSet) {
				ResultSet rs = preStmt.getResultSet();
				while(rs.next()) {
					for(int i=1;i<=rs.getMetaData().getColumnCount();i++) {
						Object o = rs.getObject(i);
						if(o instanceof LocalDateTime) {
							o = rs.getTimestamp(i);
						}
						System.out.println(String.format("data=%s, className=%s", o, o==null?"NULL":o.getClass()));
					}
				}
			}else {
				Stdout.pl("没有返回结果");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(dbMana!=null) {
				//关闭连接池
				dbMana.destroyDataPools();
			}
		}
		
	}

}
