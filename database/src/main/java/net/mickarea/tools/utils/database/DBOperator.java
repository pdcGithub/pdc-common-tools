/******************************************************************************************************

This file "DBOperator.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.models.query.OrderParamList;
import net.mickarea.tools.models.query.PageInfo;
import net.mickarea.tools.models.query.ProcParamList;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 一个数据库操作工具类，包括简单的增、删、改、查操作 和 存储过程、函数 的调用操作 
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月19日-2024年10月29日
 */
public class DBOperator {
	
	/**
	 * 数据源对象（一般为一个连接池对象）
	 */
	private final DataSource db ;
	/**
	 * 一个针对特定数据库的类型，用于防sql注入。参考 DBSQLInjectionUtil 类的 DBTYPE 常量
	 */
	private final String dbType;
	
	/**
	 * 获取当前数据库操作类的数据源
	 * @return the db 数据源对象（一般为一个连接池对象）
	 */
	public DataSource getDb() {
		return db;
	}
	
	/**
	 * 获取数据库类型。参考 DBSQLInjectionUtil 类的 DBTYPE 常量
	 * @return the dbType
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * &gt;&gt;&nbsp;获取对应数据库的虚拟表。比如 Oracle 的dual , 以及 DB2 的 SYSIBM.SYSDUMMY1 
	 * @return 表名 或者 空字符串
	 */
	public String getVirtualTable() {
		String result = "";
		switch(this.dbType) {
		case DBSQLInjectionUtil.DBTYPE_ORACLE:
			result = "DUAL";
			break;
		case DBSQLInjectionUtil.DBTYPE_DB2:
			result = "SYSIBM.SYSDUMMY1";
			break;
		}
		return result;
	}
	
	/**
	 * 构造函数
	 * <p>主要用于确认该数据库操作，是针对哪个数据库连接池的</p>
	 * <p>默认情况下，对应 MySQL 数据库</p>
	 * @param dbSource 数据源对象，一般为连接池对象
	 */
	public DBOperator(DataSource dbSource) {
		this.db = dbSource;
		this.dbType = DBSQLInjectionUtil.DBTYPE_MYSQL;
	}
	
	/**
	 * 构造函数
	 * <p>主要用于确认该数据库操作，是针对哪个数据库连接池的</p>
	 * @param dbSource 数据源对象，一般为连接池对象
	 * @param dbType 数据库类型，参考 DBSQLInjectionUtil 类的 DBTYPE 常量
	 */
	public DBOperator(DataSource dbSource, String dbType) {
		this.db = dbSource;
		this.dbType = dbType;
	}
	
	//***************************************************************************************************
	
	/**
	 * &gt;&gt;&nbsp;执行数据库映射类的实体数据查询（默认是分页查询，查询的分页默认值，可查看 PageInfo 类）
	 * @param <T> 与传入的 cls 相同的类型
	 * @param cls 数据库表或者视图的实体映射类
	 * @param execMsg 执行的结果信息。如果执行成功返回 空 ，否则返回执行异常的结果信息
	 * @return 查询后返回的数据对象列表（如果查询失败或者无数据返回，则为null或者空列表对象）
	 */
	public <T> List<T> queryEntity(Class<T> cls, StringBuffer execMsg) {
		return this.queryEntity(cls, null, null, new PageInfo(), execMsg);
	}
	
	/**
	 * &gt;&gt;&nbsp;执行数据库映射类的实体数据查询（默认是分页查询，查询的分页默认值，可查看 PageInfo 类）
	 * @param <T> 与传入的 cls 相同的类型
	 * @param cls 数据库表或者视图的实体映射类
	 * @param condiList 查询条件列表对象
	 * @param execMsg 执行的结果信息。如果执行成功返回 空 ，否则返回执行异常的结果信息
	 * @return 查询后返回的数据对象列表（如果查询失败或者无数据返回，则为null或者空列表对象）
	 */
	public <T> List<T> queryEntity(Class<T> cls, CondiParamList condiList, StringBuffer execMsg) {
		return this.queryEntity(cls, condiList, null, new PageInfo(), execMsg);
	}
	
	/**
	 * &gt;&gt;&nbsp;执行数据库映射类的实体数据查询（默认是分页查询，查询的分页默认值，可查看 PageInfo 类）
	 * @param <T> 与传入的 cls 相同的类型
	 * @param cls 数据库表或者视图的实体映射类
	 * @param condiList 查询条件列表对象
	 * @param orderList 排序条件列表对象
	 * @param execMsg 执行的结果信息。如果执行成功返回 空 ，否则返回执行异常的结果信息
	 * @return 查询后返回的数据对象列表（如果查询失败或者无数据返回，则为null或者空列表对象）
	 */
	public <T> List<T> queryEntity(Class<T> cls, CondiParamList condiList, OrderParamList orderList, StringBuffer execMsg) {
		return this.queryEntity(cls, condiList, orderList, new PageInfo(), execMsg);
	}
	
	/**
	 * &gt;&gt;&nbsp;执行数据库映射类的实体数据查询（默认是分页查询，查询的分页默认值，可查看 PageInfo 类）
	 * @param <T> 与传入的 cls 相同的类型
	 * @param cls 数据库表或者视图的实体映射类
	 * @param condiList 查询条件列表对象
	 * @param orderList 排序条件列表对象
	 * @param pageInfo 分页条件对象
	 * @param execMsg 执行的结果信息。如果执行成功返回 空 ，否则返回执行异常的结果信息
	 * @return 查询后返回的数据对象列表（如果查询失败或者无数据返回，则为null或者空列表对象）
	 */
	public <T> List<T> queryEntity(Class<T> cls, CondiParamList condiList, OrderParamList orderList, PageInfo pageInfo, StringBuffer execMsg) {
		List<T> result = null;
		Connection conn = null;
		try {
			conn = this.db.getConnection();
			result = DBStaticUtil.queryEntity(this.dbType, conn, cls, condiList, orderList, pageInfo);
		} catch (Exception e) {
			Stdout.pl(e); //增加错误信息打印
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				execMsg.append(String.format("发生数据库异常：SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			} else {
				execMsg.append("发生其它异常："+e.getMessage()); // 其它异常
			}
		} finally {
			//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
			if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
		}
		return result;
	}
	
	//*****************************************************************************************************
	/**
	 * &gt;&gt;&nbsp;数据库映射的实体对象的更新操作
	 * @param objectList 数据库映射的实体对象列表
	 * @param updatePropertyNames 更新操作，所涉及的属性名，以逗号分隔
	 * @return 异常信息。如果执行有异常会返回异常信息,否则为空
	 */
	public String updateEntity(List<?> objectList, String updatePropertyNames) {
		return this.executeEntity(objectList, DBStaticUtil.SQL_TYPE_UPDATE, updatePropertyNames);
	}
	
	/**
	 * &gt;&gt;&nbsp;数据库映射的实体对象的删除操作
	 * @param objectList 数据库映射的实体对象列表
	 * @return 异常信息。如果执行有异常会返回异常信息,否则为空
	 */
	public String deleteEntity(List<?> objectList) {
		return this.executeEntity(objectList, DBStaticUtil.SQL_TYPE_DELETE, "");
	}
	
	/**
	 * &gt;&gt;&nbsp;数据库映射的实体对象的插入操作
	 * @param objectList 数据库映射的实体对象列表
	 * @return 异常信息。如果执行有异常会返回异常信息,否则为空
	 */
	public String insertEntity(List<?> objectList) {
		return this.executeEntity(objectList, DBStaticUtil.SQL_TYPE_INSERT, "");
	}
	
	/**
	 * &gt;&gt;&nbsp;执行数据库的存储过程
	 * @param procedureName 存储过程的名称
	 * @param params 存储过程的参数
	 * @return 用于存放执行结果的对象；如果存储过程有数据返回，可以用 SimpleDBData 类 来接收。
	 */
	public SimpleDBData executeProcedure(String procedureName, ProcParamList params) {
		Connection conn = null;
		SimpleDBData result = null;
		try {
			conn = this.db.getConnection();
			//增加一个参数：dbType ，根据不同数据库类型，进行不同处理。
			result = DBStaticUtil.executeProcedure(this.dbType, conn, procedureName, params);
			//增加一个 commit （有些数据库，需要手动再提交一次，比如：sql server）
			conn.commit();
		} catch (Exception e) {
			Stdout.pl(e); //增加错误信息打印
			if(result==null) {
				result = new SimpleDBData();
			}
			result.setResponseStatus(DBStaticUtil.ERR);
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				result.setResponseInfo(String.format("发生数据库异常：SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			} else {
				result.setResponseInfo("发生其它异常："+e.getMessage()); // 其它异常
			}
		} finally {
			//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
			if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;执行数据库的函数，执行后如果有返回值，将值封装到 SimpleDBData 对象
	 * @param functionName 函数名
	 * @param params 函数的执行参数
	 * @return 一个简单的数据库查询结果对象 SimpleDBData 类。
	 */
	public SimpleDBData executeFunction(String functionName, List<Object> params) {
		Connection conn = null;
		//获取对应数据库的虚表
		String virtualTable = this.getVirtualTable();
		SimpleDBData result = null;
		try {
			conn = this.db.getConnection();
			result = DBStaticUtil.executeFunction(conn, functionName, params, virtualTable);
		} catch (Exception e) {
			Stdout.pl(e); //增加错误信息打印
			if(result==null) {
				result = new SimpleDBData();
			}
			result.setResponseStatus(DBStaticUtil.ERR);
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				result.setResponseInfo(String.format("发生数据库异常：SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			} else {
				result.setResponseInfo("发生其它异常："+e.getMessage()); // 其它异常
			}
		} finally {
			//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
			if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;执行数据库的函数，执行后如果有返回值，则按照 targetClass 参数转换为对应的对象类型
	 * @param <T> 一个类型与 targetClass 指定的类型一样的对象
	 * @param targetClass 返回的数据所对应的数据类型，比如：String, Boolean, Integer 等等
	 * @param functionName 函数名
	 * @param params 函数的执行参数
	 * @return 返回一个指定数据类型的对象，可能为 null
	 */
	public <T> T executeFunction(Class<T> targetClass, String functionName, List<Object> params) {
		Connection conn = null;
		//获取对应数据库的虚表
		String virtualTable = this.getVirtualTable();
		T t = null;
		try {
			conn = this.db.getConnection();
			t = DBStaticUtil.executeFunction(conn, targetClass, functionName, params, virtualTable);
		} catch (Exception e) {
			Stdout.pl(e); //增加错误信息打印
			Stdout.pl(DBStaticUtil.ERR);
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				Stdout.pl(String.format("发生数据库异常：SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			} else {
				Stdout.pl("发生其它异常："+e.getMessage()); // 其它异常
			}
		} finally {
			//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
			if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
		}
		return t;
	}
	
	/**
	 * &gt;&gt;&nbsp;执行查询语句，并将查询结果写入 SimpleDBData 这个容器中
	 * @param preSql 查询的 SQL 语句，必须带参数，参数用预处理字符'?'标记<p>比如<code> select function(?) f1, id, name from test_a where id=?;</code></p>
	 * @param params 参数不单单对应查询条件，也对应查询内容。
	 * @return 一个简单的数据库查询结果对象
	 */
	public SimpleDBData querySQL(String preSql, List<Object> params) {
		Connection conn = null;
		SimpleDBData sdb = null;
		try {
			conn = this.db.getConnection();
			sdb = DBStaticUtil.querySQL(conn, preSql, params);
		}catch(Exception e) {
			Stdout.pl(e); //增加错误信息打印
			if(sdb==null) {
				sdb = new SimpleDBData();
			}
			sdb.setResponseStatus(DBStaticUtil.ERR);
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				sdb.setResponseInfo(String.format("发生数据库异常：SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			}else {
				sdb.setResponseInfo("发生其它异常："+e.getMessage()); // 其它异常
			}
		}finally {
			//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
			if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
		}
		return sdb;
	}
	
	/**
	 * &gt;&gt;&nbsp;这里用于处理一些临时性的非实体查询
	 * @param <T> 与 传入的 virtualEntityClass 同类型
	 * @param virtualEntityClass 虚拟的实体对象，必须有 MyVirtualEntity 注解
	 * @param preSql 用预处理格式的sql语句，参数位置用 ? 代替
	 * @param params 参数不单单对应查询条件，也对应查询内容。
	 * @param execMsg 执行的结果信息。如果执行成功返回 空 ，否则返回执行异常的结果信息
	 * @return 返回一个虚拟的实体对象列表，如果没有结果可能为空，可能为 null
	 */
	public <T> List<T> querySQL(Class<T> virtualEntityClass, String preSql, List<Object> params, StringBuffer execMsg) {
		Connection conn = null;
		List<T> result = null;
		try {
			conn = this.db.getConnection();
			result = DBStaticUtil.querySQL(conn, virtualEntityClass, preSql, params);
		}catch(Exception e) {
			Stdout.pl(e); //增加错误信息打印
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				execMsg.append(String.format("发生数据库异常：SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			}else {
				execMsg.append("发生其它异常："+e.getMessage()); // 其它异常
			}
		}finally {
			//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
			if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;以 SQL 语句的方式更新数据。如果执行有异常会返回异常信息 （执行完成后，直接提交结果）
	 * @param preSql 数据库 update 语句。语句必须带参数，参数以 '?' 替换符匹配
	 * @param params 数据库 update 语句中的 参数（包括 set 以及 where 条件）
	 * @return execMsg 异常信息。如果执行有异常会返回异常信息,否则为空
	 */
	public String updateSQL(String preSql, List<Object> params) {
		return this.executeSQL(preSql, params, DBStaticUtil.SQL_TYPE_UPDATE);
	}
	
	/**
	 * &gt;&gt;&nbsp;以 SQL 语句的方式删除数据。如果执行有异常会返回异常信息 （执行完成后，直接提交结果）
	 * @param preSql 数据库 delete 语句。语句必须带参数，参数以 '?' 替换符匹配
	 * @param params 数据库 delete 语句中的 where 条件参数
	 * @return execMsg 异常信息。如果执行有异常会返回异常信息,否则为空
	 */
	public String deleteSQL(String preSql, List<Object> params) {
		return this.executeSQL(preSql, params, DBStaticUtil.SQL_TYPE_DELETE);
	}
	
	/**
	 * &gt;&gt;&nbsp;以 SQL 语句的方式插入数据。如果执行有异常会返回异常信息 （执行完成后，直接提交结果）
	 * @param preSql 数据库 insert 语句。语句必须带参数，参数以 '?' 替换符匹配
	 * @param params 要插入的数据内容。以二维列表( List&lt;List&lt;Object&gt;&gt;&nbsp;)的形式传入。
	 * @return execMsg 异常信息。如果执行有异常会返回异常信息,否则为空
	 */
	public String insertSQL(String preSql, List<? super Object> params) {
		return this.executeSQL(preSql, params, DBStaticUtil.SQL_TYPE_INSERT);
	}
	
	/**
	 * &gt;&gt;&nbsp;这个方法用于执行 update、delete、insert 三种类型 的操作
	 * @param preSQL 数据库 update、delete、insert 语句。语句必须带参数，参数以 '?' 替换符匹配
	 * @param params 数据库 update、delete、insert 语句中的 参数（包括 set 以及 where 条件）
	 * @param type SQL语句类型，比如  insert 、 delete 、 update 三大类。参考：DBOperator 的 SQL_TYPE 常量
	 * @return 异常信息。如果执行有异常会返回异常信息,否则为空
	 */
	private String executeSQL(String preSQL, List<? super Object> params, String type) {
		StringBuffer execMsg = new StringBuffer();
		//参数校验
		String validString = DBValidUtil.validateParams(preSQL, params, type);
		if(!StrUtil.isEmptyString(validString)) {
			return validString;
		}
		//开始执行
		Connection conn = null;
		try {
			conn = this.db.getConnection();
			DBStaticUtil.executeSQL(conn, preSQL, params, type);
			conn.commit();
			Stdout.pl("数据库操作成功，提交成功。");
		}catch(Exception e) {
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				execMsg.append(String.format("发生数据库异常：SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			}else {
				execMsg.append("发生其它异常："+e.getMessage()); // 其它异常
			}
			Stdout.pl(e); //增加错误信息打印
			if(conn!=null) {
				try {conn.rollback(); Stdout.pl("数据库操作回滚成功。"); } catch (SQLException e1) { execMsg.append("数据库操作回滚出错："+e1.getMessage());
					Stdout.pl(e1);
				}
			}
		}finally {
			//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
			if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
		}
		return execMsg.toString();
	}
	
	/**
	 * &gt;&gt;&nbsp;这个方法用于执行 update、delete、insert 三种类型 实体的操作
	 * @param objectList 将要执行操作的实体对象列表
	 * @param type 实体操作类型，比如  insert 、 delete 、 update 三大类。参考：DBOperator 的 SQL_TYPE 常量
	 * @param updatePropertyNames 更新操作，所涉及的属性名，以逗号分隔
	 * @return 异常信息。如果执行有异常会返回异常信息,否则为空
	 */
	private String executeEntity(List<?> objectList, String type, String updatePropertyNames) {
		StringBuffer execMsg = new StringBuffer();
		//参数校验
		String validString = DBValidUtil.validateEntityList(objectList);
		if(!StrUtil.isEmptyString(validString)) {
			return validString;
		}
		//开始执行
		Connection conn = null;
		try {
			conn = this.db.getConnection();
			DBStaticUtil.executeEntity(conn, objectList, type, updatePropertyNames);
			conn.commit();
			Stdout.pl("数据库操作成功，提交成功。");
		}catch(Exception e) {
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				execMsg.append(String.format("发生数据库异常：SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			}else {
				execMsg.append("发生其它异常："+e.getMessage()); // 其它异常
			}
			Stdout.pl(e); //增加错误信息打印
			if(conn!=null) {
				try {conn.rollback(); Stdout.pl("数据库操作回滚成功。"); } catch (SQLException e1) { execMsg.append("数据库操作回滚出错："+e1.getMessage());
					Stdout.pl(e1);
				}
			}
		}finally {
			//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
			if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
		}
		return execMsg.toString();
	}
	
	//*****************************************************************************************************
	/*
	public static void main(String[] args) {
		
		//获取数据库链接处理
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		DataSource dbSource = mana.getDataPoolByName("jar_primary");
		DBOperator db = new DBOperator(dbSource);
		
		//执行函数
		Stdout.pl("测试 1 ==============================================");
		SimpleDBData sdb = db.executeFunction("current_timestamp2", null);
		Stdout.pl(sdb.getResponseStatus().equals(DBStaticUtil.OK)?sdb.getData()[0][0]:sdb.getResponseInfo());
		Stdout.pl("测试 2 ==============================================");
		LocalDateTime result = db.executeFunction(LocalDateTime.class, "current_timestamp", null);
		Stdout.pl(result);
		
		//设置执行参数
		Stdout.pl("测试 3 ==============================================");
		sdb.destroy();
		ProcParamList paramList = new ProcParamList();
		paramList.addParam(new ProcParam(ProcParam.TYPE_IN, "in_param", "测试"));
		paramList.addParam(new ProcParam(ProcParam.TYPE_OUT, "out_param", ""));
		//执行存储过程
		sdb = db.executeProcedure("test_procedure", paramList);
		if(sdb.getResponseStatus().equals(DBStaticUtil.OK)) {
			Stdout.pl("执行成功，返回信息："+paramList.getParam("out_param").getParamValue());
		}else {
			Stdout.pl("执行失败，"+sdb.getResponseInfo());
		}
		//Stdout.pl(sdb);
		
		Stdout.pl("测试 4 ==============================================");
		paramList.removeAll();
		paramList.addParam(new ProcParam(ProcParam.TYPE_IN, "table_name_str", "test_a"));
		paramList.addParam(new ProcParam(ProcParam.TYPE_IN, "where_str", ""));
		paramList.addParam(new ProcParam(ProcParam.TYPE_IN, "order_by_str", ""));
		paramList.addParam(new ProcParam(ProcParam.TYPE_IN, "page_size", 5));
		paramList.addParam(new ProcParam(ProcParam.TYPE_IN, "page_num", 1));
		paramList.addParam(new ProcParam(ProcParam.TYPE_OUT, "total_count", 0));
		paramList.addParam(new ProcParam(ProcParam.TYPE_OUT, "result_str", ""));
		paramList.addParam(new ProcParam(ProcParam.TYPE_OUT, "result_sql", ""));
		paramList.addParam(new ProcParam(ProcParam.TYPE_OUT, "count_sql", ""));
		//执行存储过程
		sdb = db.executeProcedure("page_procedure", paramList);
		if(sdb.getResponseStatus().equals(DBStaticUtil.OK)) {
			Stdout.pl("总页数："+paramList.getParam("total_count").getParamValue());
			Stdout.pl("分页大小："+paramList.getParam("page_size").getParamValue());
			Stdout.pl("当前页："+paramList.getParam("page_num").getParamValue());
			Stdout.pl("执行情况描述："+paramList.getParam("result_str").getParamValue());
			Stdout.pl("执行的sql ："+paramList.getParam("result_sql").getParamValue());
			Stdout.pl("执行的统计总数的sql："+paramList.getParam("count_sql").getParamValue());
		}else {
			Stdout.pl("执行失败，"+sdb.getResponseInfo());
		}
		Stdout.pl(sdb);
		
		//释放资源
		mana.destroyDataPools();
	}
	*/
}
