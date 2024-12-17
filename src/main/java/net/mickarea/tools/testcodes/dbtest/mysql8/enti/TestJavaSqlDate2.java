package net.mickarea.tools.testcodes.dbtest.mysql8.enti;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.models.query.CondiParam;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.testcodes.dbtest.mysql8.beans.AnalyBusyHours;
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
public class TestJavaSqlDate2 {

	public static void main(String[] args) {
		//资源创建
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		DataSource mysqlDS = mana.getDataPoolByName("jar_primary");
		DBOperator mysqlDB = new DBOperator(mysqlDS, DBSQLInjectionUtil.DBTYPE_MYSQL);
		
		//添加一个记录
		AnalyBusyHours h1 = new AnalyBusyHours(LocalDate.now(), 1,2,3,4,5,6);
		
		//开始测试
		String insertResult = mysqlDB.insertEntity(Arrays.asList(h1));
		if(StrUtil.isEmptyString(insertResult)) {
			//
			CondiParamList condiParams = new CondiParamList(AnalyBusyHours.class);
			condiParams.addParam(new CondiParam("dataDate", LocalDate.now()));
			//插入 成功，再查询出来
			StringBuffer sb = new StringBuffer();
			List<AnalyBusyHours> dataList = mysqlDB.queryEntity(AnalyBusyHours.class, condiParams, sb);
			dataList.forEach(Stdout::pl);
		}else {
			Stdout.pl("执行实体【AnalyBusyHours】插入出错，"+insertResult);
		}
		
		//资源回收
		mana.destroyDataPools();
	}

}
