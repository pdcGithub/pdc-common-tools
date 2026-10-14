/******************************************************************************************************

This file "DataPaginationTester.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.dbtest.page;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.regex.Pattern;

import net.mickarea.tools.models.query.CondiParam;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.models.query.OrderParam;
import net.mickarea.tools.models.query.OrderParamList;
import net.mickarea.tools.models.query.PageInfo;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 数据分页处理测试，主要测试使用完全的 java 代码查询语句而非 存储过程
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月28日-2024年6月30日
 */
public class DataPaginationTester {
	
	public static void main(String[] args) {
		
		/**
		 * 数据库类型
		 */
		String databaseType = "oracle";
		
		/**
		 * 要查询的表
		 */
		String tableName = "test_orcl_data_type";
		
		/**
		 * 查询条件
		 */
		CondiParamList condiList = new CondiParamList();
		if(databaseType.equalsIgnoreCase("oracle")) {
			//由于默认的是MYSQL，所以这里要改为 oracle 的处理
			condiList.setCodec(DBSQLInjectionUtil.DBTYPE_ORACLE);
		}
		condiList.addParam(new CondiParam("cname", "名称'", CondiParam.OPT_LIKE));
		//Timestamp t = Timestamp.valueOf(LocalDate.of(2024, 6, 21).atTime(LocalTime.MIDNIGHT));
		condiList.addParam(new CondiParam("app_date", LocalDate.now()));
		condiList.addParam(new CondiParam("v_day", LocalTime.now()));
		condiList.addParam(new CondiParam("app_datetime_1", LocalDateTime.now()));
		condiList.addParam(new CondiParam("app_datetime_2", new Timestamp(System.currentTimeMillis())));
		condiList.addParam(new CondiParam("app_datetime_3", new java.util.Date()));
		condiList.addParam(new CondiParam("seq_no", Arrays.asList(15,18,19), CondiParam.OPT_IN));
		condiList.addParam(new CondiParam("seq_no", Arrays.asList(new BigDecimal(1),new BigDecimal(2.5),new BigDecimal(3.1515926)), CondiParam.OPT_IN));
		
		/**
		 * 排序条件
		 */
		OrderParamList orderList = new OrderParamList(); 
		orderList.addParam(new OrderParam("cname", OrderParam.OPT_ASC));
		orderList.addParam(new OrderParam("seq_no", OrderParam.OPT_DESC));
		
		/**
		 * 分页参数
		 */
		PageInfo pageInfo = new PageInfo();
		
		//输出看看
		Stdout.pl("数据库类型:"+databaseType);
		Stdout.pl("查询的表:"+tableName);
		Stdout.pl("查询条件:"+condiList.toSqlString());
		Stdout.pl("排序条件:"+orderList.toSqlString());
		Stdout.pl("分页条件:"+pageInfo.toString());
		
		//开始拼接
		StringBuffer resultSql = new StringBuffer();
		resultSql.append("select distinct my2k.* from ");
		//判断传入的是表名，还是sql语句；如果是sql语句，则需要用括号括起来
		if(Pattern.matches("\\s*[sS][eE][lL][eE][cC][tT]\\s+.*", tableName)) {
			resultSql.append("(");
			resultSql.append(tableName);
			resultSql.append(")");
		}else {
			resultSql.append(tableName);
		}
		//拼接别名
		resultSql.append(" my2k ");
		//拼接条件
		resultSql.append(" where 1=1 ");
		if(condiList.toSqlString().length()>0) {
			resultSql.append(" and ( "+condiList.toSqlString()+" ) ");
		}
		//拼接排序
		if(orderList.toSqlString().length()>0) {
			resultSql.append(" order by "+orderList.toSqlString()+" ");
		}else {
			resultSql.append(" order by 1 ");
		}
		
		//根据生成的语句，分页处理（oracle 和 mysql 处理方式不一样）
		if( !StrUtil.isEmptyString(resultSql.toString()) ) {
			
			//设置 分页信息
			pageInfo.setPageSize(10);
			pageInfo.setPageNum(3);
			
			//count 处理
			String countSql = resultSql.toString().replaceFirst("\\s+[mM][yY][2][kK]\\.\\*\\s+", " count(1) ").replaceFirst("\\s+[oO][rR][dD][eE][rR]\\s+[bB][yY].+", " ");
			Stdout.pl(countSql);
			
			if(databaseType.equalsIgnoreCase(DBSQLInjectionUtil.DBTYPE_ORACLE)) {
				//计算分页的头尾行号（oracle 行号 1 开始）
				int start = 1+(pageInfo.getPageNum()-1)*pageInfo.getPageSize();
				int end = start+pageInfo.getPageSize()-1;
				//对于 oracle 数据库的分页处理
				String myPagingSql = String.format("select * from (\r\n" + 
						"    select rownum as rno, tk.* from (\r\n" + 
						"           %s \r\n" + 
						"    ) tk where rownum <= ? \r\n" + 
						") where rno >= ? ", resultSql.toString());
				//输出
				Stdout.pl(myPagingSql);
				Stdout.fpl(" rownum <= %s, rownum >= %s", end, start);
			}else if(databaseType.equalsIgnoreCase(DBSQLInjectionUtil.DBTYPE_MYSQL)) {
				//计算分页的头尾行号（mysql行号，0 开始）
				int start = (pageInfo.getPageNum()-1) * pageInfo.getPageSize();
				int pageSize = pageInfo.getPageSize();
				//对于 mysql 数据库的分页处理
				resultSql.append(" limit ?, ? ");
				//输出
				Stdout.pl(resultSql.toString());
				Stdout.fpl(" limit %s, %s", start, pageSize);
			}else {
				Stdout.pl("数据库类型不支持");
			}
			
		}else {
			Stdout.pl("==== sql 语句没有内容");
		}
		
	}

}
