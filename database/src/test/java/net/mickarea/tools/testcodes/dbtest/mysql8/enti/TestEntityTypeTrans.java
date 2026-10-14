package net.mickarea.tools.testcodes.dbtest.mysql8.enti;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestInsert;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.TestInsertNew;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

public class TestEntityTypeTrans {

	public static void main(String[] args) {
		
		//获取数据库链接处理
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		DataSource mysqlDataSource = mana.getDataPoolByName("jar_primary");
		DBOperator mysqlDB = new DBOperator(mysqlDataSource, DBSQLInjectionUtil.DBTYPE_MYSQL);
		
		//打印数据
		// [java.lang.Integer, java.lang.String, java.time.LocalDateTime]
		SimpleDBData sdb = mysqlDB.querySQL("select * from test_insert where 1=?", Arrays.asList(1));
		Stdout.pl(sdb);
		Stdout.pl("================================  第 1 次  =========================================");
		//将数据映射为对象，然后输出
		StringBuffer sb = new StringBuffer();
		List<TestInsert> data1 = mysqlDB.querySQL(
				TestInsert.class, "select * from test_insert where 1=?", Arrays.asList(1), sb);
		if(StrUtil.isEmptyString(sb.toString())){
			Stdout.fpl("打印数据[数量为%s]", ListUtil.isEmptyList(data1)?0:data1.size());
			data1.forEach(Stdout::pl);
		}else {
			Stdout.pl("查询执行出错："+sb.toString());
		}
		Stdout.pl("================================  第 2 次  =========================================");
		//将数据映射为对象，然后输出
		StringBuffer sb2 = new StringBuffer();
		List<TestInsertNew> data2 = mysqlDB.querySQL(
				TestInsertNew.class, "select * from test_insert where 1=?", Arrays.asList(1), sb2);
		if(StrUtil.isEmptyString(sb2.toString())){
			Stdout.fpl("第2次打印数据[数量为%s]", ListUtil.isEmptyList(data2)?0:data2.size());
			data2.forEach(Stdout::pl);
		}else {
			Stdout.pl("查询执行出错："+sb2.toString());
		}
		Stdout.pl("================================  第 3 次  =========================================");
		//将数据映射为对象，然后输出
		StringBuffer sb3 = new StringBuffer();
		List<TestInsertNew> data3 = mysqlDB.queryEntity(TestInsertNew.class, sb3);
		if(StrUtil.isEmptyString(sb3.toString())){
			Stdout.fpl("第3次打印数据[数量为%s]", ListUtil.isEmptyList(data3)?0:data3.size());
			data3.forEach(Stdout::pl);
		}else {
			Stdout.pl("查询执行出错："+sb3.toString());
		}
		Stdout.pl("================================  第 4 次  =========================================");
		//将数据映射为对象，然后输出
		StringBuffer sb4 = new StringBuffer();
		List<TestInsert> data4 = mysqlDB.queryEntity(TestInsert.class, sb4);
		if(StrUtil.isEmptyString(sb4.toString())){
			Stdout.fpl("第4次打印数据[数量为%s]", ListUtil.isEmptyList(data4)?0:data4.size());
			data4.forEach(Stdout::pl);
		}else {
			Stdout.pl("查询执行出错："+sb4.toString());
		}
		Stdout.pl("================================  结束    =========================================");
		//释放资源
		mana.destroyDataPools();
	}

}
