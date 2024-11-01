/******************************************************************************************************

This file "SqlserverQuery.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.database.impl.query;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.models.query.OrderParamList;
import net.mickarea.tools.models.query.PageInfo;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBStaticUtil;
import net.mickarea.tools.utils.database.DBStrUtil;
import net.mickarea.tools.utils.database.DBValidUtil;

/**
 * 这里是关于 MS SQL Server 的查询处理实现类。由于数据库较多，写在一个方法会很难维护，所以拆分开，一个数据库一个类。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月24日-2024年10月31日
 */
public class SqlserverQuery {
	
	/**
	 * &gt;&gt;&nbsp;执行数据库映射类的实体数据查询（默认是分页查询，查询的分页默认值，可查看 PageInfo 类）
	 * @param <T> 与传入的 cls 类型相同
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param cls 数据库表或者视图的实体映射类
	 * @param condiList 查询条件列表对象
	 * @param orderList 排序条件列表对象
	 * @param pageInfo 分页条件对象
	 * @return 查询后返回的数据对象列表（如果查询失败或者无数据返回，则为null或者空列表对象）
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static final <T> List<T> queryEntity(Connection conn, Class<T> cls, CondiParamList condiList, OrderParamList orderList, PageInfo pageInfo) throws Exception {
		
		List<T> result = null;
		
		//参数校验
		String validString = DBValidUtil.validateParams(conn);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		validString = DBValidUtil.validateParams(cls);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		
		//处理排序、条件、分页参数、
		condiList = condiList==null?new CondiParamList():condiList;
		orderList = orderList==null?new OrderParamList():orderList;
		pageInfo = pageInfo==null?new PageInfo():pageInfo;
		
		//处理存储过程的参数（表名）
		String tableName = DBStrUtil.parseClassNameToTableOrViewName(cls);
		
		//获取该表的第一个字段
		String firstColName = DBStrUtil.getFirstColumnFromBeanClass(cls);
		if(DBStrUtil.EXEC_ERROR.equalsIgnoreCase(firstColName)) {
			throw new Exception("获取映射类 "+cls.getName()+" 所属表的第一个数据列名称异常。");
		}
		
		// 构造 2 条 sql 语句，一条用于 查询分页数据，一条用于 统计数据总数
		
		//计算分页的头尾行号（sqlserver ROW_NUMBER 行号 从 1 开始）
		int start = 1+(pageInfo.getPageNum()-1)*pageInfo.getPageSize();
		int end = start+pageInfo.getPageSize()-1;
		
		//分页查询语句的模板（top 后面不能用 ? 号参数）
		String querySqlTemplate = "select tk.* from ( \r\n"
				                + "    select top "+end+" ROW_NUMBER() over(order by %s) rownum, * from %s where %s \r\n"
				                + ") tk where tk.rownum>= ? order by %s";
		
		//统计查询语句的模板
		String countSqlTemplate = "select count(1) from %s where %s";
		
		//查询的 where 条件
		String whereInfo = condiList.toSqlString();
		if(StrUtil.isEmptyString(whereInfo)) {
			whereInfo = "1=1";
		}
		
		//查询的 order by 信息（因为 sql server 的 over 语句，order by 只能写字段名，所以要拼字段）
		String orderbyInfo = orderList.toSqlString();
		if(StrUtil.isEmptyString(orderbyInfo)) {
			orderbyInfo = firstColName + " asc ";
		}
		
		//构建 分页查询 sql
		String querySql = String.format(querySqlTemplate, orderbyInfo, tableName, whereInfo, orderbyInfo);
		//构建 统计查询 sql；统计语句如果有条件的情况下，需要拼接一个 1=?
		String countWhereSql = whereInfo.equals("1=1")?"1=?":(whereInfo+" and 1=?");
		String countSql = String.format(countSqlTemplate, tableName, countWhereSql);
		
		//处理 sql 的预处理条件
		List<Object> queryParams = new ArrayList<Object>();
		List<Object> countParams = new ArrayList<Object>();
		//
		queryParams.add(start);
		//
		countParams.add(1);
		
		//执行查询
		result = DBStaticUtil.querySQL(conn, cls, querySql, queryParams);
		
		//统计记录总数
		SimpleDBData sdb = DBStaticUtil.querySQL(conn, countSql, countParams);
		if(DBStaticUtil.OK.equalsIgnoreCase(sdb.getResponseStatus())) {
			//获取分页记录总数
			int totalCount = ((BigDecimal)sdb.getData()[0][0]).intValue();
			//计算分页数
			int tocalPage = totalCount / pageInfo.getPageSize() + (totalCount % pageInfo.getPageSize()==0?0:1);
			//设置分页信息
			pageInfo.setTotalCount(totalCount);
			pageInfo.setTotalPage(tocalPage);
		}else {
			throw new Exception("查询分页记录总数异常，请检查..."+sdb.getResponseInfo());
		}
		
		//返回结果
		return result;
	}

}
