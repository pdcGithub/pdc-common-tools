/******************************************************************************************************

This file "TestDBTrans1.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.mysql8;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.db.DBTrans;
import net.mickarea.tools.models.db.DBTransAction;
import net.mickarea.tools.models.query.PageInfo;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestA;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestB;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.RandomUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBStaticUtil;

/**
 * >> 数据库事务同步测试
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月13日-2024年7月2日
 */
public class TestDBTrans1 {

	public static void main(String[] args) {
		
		//生成数据库操作对象 
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		//获取数据库连接池对象
		DataSource ds = mana.getDataPoolByName("jar_primary");
		//创建数据库操作对象
		DBOperator db = new DBOperator(ds);
		
		//构造事务处理对象
		DBTrans trans1 = new TestDBTransDemo1(db);
		
		//查看事务处理结果
		String msg = trans1.run();
		if(StrUtil.isEmptyString(msg)) {
			Stdout.pl("事务执行成功");
		}else {
			Stdout.pl("事务执行失败，信息："+msg);
		}
		
		//资源回收
		mana.destroyDataPools();
	}
	
}

/**
 * >> 实现抽象类 DBTransAction 中的 doTrans 方法，以保证事务同步
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月13日
 */
class TestDBTransDemo1 extends DBTransAction {

	/**
	 * 构造函数（将DBOperator对象传入，发送参数到 父类）
	 */
	public TestDBTransDemo1(DBOperator operator) {
		super(operator);
	}
	
	/* (non-Javadoc)
	 * @see net.mickarea.tools.models.db.DBTransAction#doTrans(net.mickarea.tools.utils.DBOperator, java.sql.Connection)
	 */
	@Override
	public void doTrans(Connection conn) throws Exception {
		
		//查询 test_a 表所有数据
		PageInfo pageInfo = new PageInfo(1, 10000);
		List<TestA> aList = DBStaticUtil.queryEntity(this.getOpt().getDbType(), conn, TestA.class, null, null, pageInfo); //查询 test_a 表中的数据，用于构造 test_b 表中的数据
		
		if(!ListUtil.isEmptyList(aList)) {
			
			//根据test_a 表的信息，构造 test_b 表的数据
			List<TestB> bList = new ArrayList<TestB>();
			for(TestA a: aList) {
				TestB b = new TestB(new BigInteger("0"), a.getId(), new Long(RandomUtil.genNumberStringWitoutException(2)), null, null, null);
				bList.add(b);
			}
			DBStaticUtil.insertEntity(conn, bList); //把构造好的数据插入 test_b 表
			
			//由于上面插入还没commit，所以要同一个conn才能查询到插入的结果
			
			List<TestB> newBList = DBStaticUtil.queryEntity(this.getOpt().getDbType(), conn, TestB.class, null, null, pageInfo);
			if(!ListUtil.isEmptyList(newBList)) {
				
				//读取刚插入的数据，并修改数值，进行更新
				for(TestB b: newBList) {
					b.setExamScore(new BigDecimal(RandomUtil.genNumberStringWitoutException(2)+""));
					b.setFinals(new BigDecimal("100"));
					b.setUpdateTime(LocalDateTime.now());
				}
				DBStaticUtil.updateEntity(conn, newBList, "examScore, finals, updateTime"); //对刚才插入的 数据，执行部分更新
				Stdout.pl("更新 examScore完毕");
				
			}else {
				Stdout.pl("msg2查询的列表信息为空，无数据返回");
			}
			
		}else if(ListUtil.isEmptyList(aList)) {
			Stdout.pl("msg1查询的列表信息为空，无数据返回");
		}
		
	}
	
}
