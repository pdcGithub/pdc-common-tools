/******************************************************************************************************

This file "MysqlQuery.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
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
 * 这里是关于 MySQL 的查询处理实现类。由于数据库较多，写在一个方法会很难维护，所以拆分开，一个数据库一个类。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月24日
 */
public class MysqlQuery {

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
		
		//开始拼接
		StringBuffer resultSql = new StringBuffer();
		resultSql.append("select distinct my2k.* from ");
		resultSql.append(tableName);
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
		//查询语句（结果查询 以及 记录总数查询）
		String querySql = resultSql.toString();
		String countSql = querySql.replaceFirst("\\s+[mM][yY][2][kK]\\.\\*\\s+", " count(1) ").replaceFirst("\\s+[oO][rR][dD][eE][rR]\\s+[bB][yY].+", " ");
		countSql += " and 1=? ";
		
		//由于不同数据库，分页方式不同，所以这里分开处理
		String myPagingSql = null;
		List<Object> queryParams = new ArrayList<Object>();
		List<Object> countParams = new ArrayList<Object>();
		countParams.add(1);
		
		//构建分页查询语句
		//计算分页的头尾行号（mysql行号，0 开始）
		int start = (pageInfo.getPageNum()-1) * pageInfo.getPageSize();
		int pageSize = pageInfo.getPageSize();
		//对于 mysql 数据库的分页处理
		myPagingSql = String.format(" %s limit ?, ? ", querySql);
		queryParams.add(start);
		queryParams.add(pageSize);
		
		//执行查询
		result = DBStaticUtil.querySQL(conn, cls, myPagingSql, queryParams);
		
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
