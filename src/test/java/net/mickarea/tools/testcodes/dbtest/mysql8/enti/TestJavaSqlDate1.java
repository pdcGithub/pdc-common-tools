package net.mickarea.tools.testcodes.dbtest.mysql8.enti;

import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.AnalyBusyHours;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 关于 java.sql.Date 这个类型的测试(增、删、改、查)
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年12月17日
 */
public class TestJavaSqlDate1 {

	public static void main(String[] args) {
		//资源创建
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		DataSource mysqlDS = mana.getDataPoolByName("jar_primary");
		DBOperator mysqlDB = new DBOperator(mysqlDS, DBSQLInjectionUtil.DBTYPE_MYSQL);
		
		//开始测试
		StringBuffer sb = new StringBuffer();
		List<AnalyBusyHours> dataList = mysqlDB.queryEntity(AnalyBusyHours.class, sb);
		String msg = sb.toString();
		if(StrUtil.isEmptyString(msg)) {
			if(!ListUtil.isEmptyList(dataList)) {
				Stdout.pl("【AnalyBusyHours】数据如下：");
				dataList.forEach(Stdout::pl);
			}else {
				Stdout.pl("查询不到数据。");
			}
		}else {
			Stdout.pl("执行实体【AnalyBusyHours】查询出错，"+msg);
		}
		
		//资源回收
		mana.destroyDataPools();
	}

}
