/******************************************************************************************************

This file "SimpleDBDataTester1.java" is part of project "pdc-common-database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.database.DBStaticUtil;

/**
 * 这里是关于 net.mickarea.tools.models.SimpleDBData 类的单元测试
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年10月27日
 */
public class SimpleDBDataTester1 {

	/**
	 * 因为这是一个 java bean ，所以测试 new 、get 、set、destroy、toString 就行了
	 * @throws Exception
	 */
	@Test
	void test1() throws Exception {
		
		// 因为 Arrays.asList 生成的列表无法 执行 修改操作。所以需要自己创建几个
		List<String> colNameList = new ArrayList<String>(Arrays.asList("col1", "col2"));
		List<String> colClsNameList = new ArrayList<String>(Arrays.asList("java.lang.String", "java.lang.String"));
		List<String> colTypeNameList = new ArrayList<String>(Arrays.asList("varchar", "varchar"));
		
		// 先创建
		SimpleDBData sdb = new SimpleDBData();
		
		// 然后提取 1
		assertNull(sdb.getColumnName());
		assertNull(sdb.getColumnClassName());
		assertNull(sdb.getColumnTypeName());
		assertNull(sdb.getData());
		assertEquals(DBStaticUtil.OK, sdb.getResponseStatus()); // 默认是有值的
		assertNull(sdb.getResponseInfo());
		
		// 然后设置 1
		sdb.setColumnName(colNameList);
		sdb.setColumnClassName(colClsNameList);
		sdb.setColumnTypeName(colTypeNameList);
		String[][] data1 = {{"a", "b"}, {"c", "d"}};
		sdb.setData(data1);
		sdb.setResponseStatus(DBStaticUtil.ERR);
		sdb.setResponseInfo("测试...");
		
		// 然后提取 2
		assertEquals(colNameList, sdb.getColumnName());
		assertEquals(colClsNameList, sdb.getColumnClassName());
		assertEquals(colTypeNameList, sdb.getColumnTypeName());
		assertEquals(data1, sdb.getData());
		assertEquals(DBStaticUtil.ERR, sdb.getResponseStatus());
		assertEquals("测试...", sdb.getResponseInfo());
		
		// 然后打印
		Stdout.mylogger.debug(sdb.toString());
		
		// 然后销毁
		sdb.destroy();
		
		// 然后再打印
		assertNull(sdb.getColumnName());
		assertNull(sdb.getColumnClassName());
		assertNull(sdb.getColumnTypeName());
		assertNull(sdb.getData());
		assertNull(sdb.getResponseStatus()); // 销毁后，这里应该是 null
		assertNull(sdb.getResponseInfo());
	}
	
}
