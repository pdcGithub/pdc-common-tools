/******************************************************************************************************

This file "DBStaticUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.database;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mickarea.tools.annotation.MyAutoIncrement;
import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyColumnIgnore;
import net.mickarea.tools.annotation.MyColumnReadOnly;
import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.models.query.CondiParamList;
import net.mickarea.tools.models.query.OrderParamList;
import net.mickarea.tools.models.query.PageInfo;
import net.mickarea.tools.models.query.ProcParam;
import net.mickarea.tools.models.query.ProcParamList;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.impl.query.MysqlQuery;
import net.mickarea.tools.utils.database.impl.query.OracleQuery;
import net.mickarea.tools.utils.database.impl.query.SqlserverQuery;

/**
 * 数据库操作的静态工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月14日-2024年12月17日
 */
public final class DBStaticUtil {

	/**
	 * sql执行的状态参数
	 */
	public static String OK = "ok";
	/**
	 * sql执行的状态参数
	 */
	public static String ERR = "error";
	/**
	 * sql语句类型
	 */
	public static String SQL_TYPE_INSERT = "insert";
	/**
	 * sql语句类型
	 */
	public static String SQL_TYPE_SELECT = "select";
	/**
	 * sql语句类型
	 */
	public static String SQL_TYPE_DELETE = "delete";
	/**
	 * sql语句类型
	 */
	public static String SQL_TYPE_UPDATE = "update";
	/**
	 * 数据库对象类型，函数
	 */
	public static String OBJ_TYPE_FUNC = "func";
	/**
	 * 数据库对象类型，存储过程
	 */
	public static String OBJ_TYPE_PROC = "proc";
	
	/**
	 * 构造函数，私有
	 */
	private DBStaticUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 执行数据库映射类的实体数据查询（默认是分页查询，查询的分页默认值，可查看 PageInfo 类）
	 * @param <T> 与传入的 cls 相同的类型
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param cls 数据库表或者视图的实体映射类
	 * @param dbType 数据库类型（由于不同的数据库，查询处理可能不同，所以要传递一个数据库类型）
	 * @return 查询后返回的数据对象列表（如果查询失败或者无数据返回，则为null或者空列表对象）
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static <T> List<T> queryEntity(String dbType, Connection conn, Class<T> cls) throws Exception {
		return queryEntity(dbType, conn, cls, null, null, null);
	}
	
	/**
	 * 执行数据库映射类的实体数据查询（默认是分页查询，查询的分页默认值，可查看 PageInfo 类）
	 * @param <T> 与传入的 cls 相同的类型
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param cls 数据库表或者视图的实体映射类
	 * @param dbType 数据库类型（由于不同的数据库，查询处理可能不同，所以要传递一个数据库类型）
	 * @param condiList 查询条件列表对象
	 * @return 查询后返回的数据对象列表（如果查询失败或者无数据返回，则为null或者空列表对象）
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static <T> List<T> queryEntity(String dbType, Connection conn, Class<T> cls, CondiParamList condiList) throws Exception {
		return queryEntity(dbType, conn, cls, condiList, null, null);
	}
	
	/**
	 * 执行数据库映射类的实体数据查询（默认是分页查询，查询的分页默认值，可查看 PageInfo 类）
	 * @param <T> 与传入的 cls 相同的类型
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param cls 数据库表或者视图的实体映射类
	 * @param dbType 数据库类型（由于不同的数据库，查询处理可能不同，所以要传递一个数据库类型）
	 * @param condiList 查询条件列表对象
	 * @param orderList 排序条件列表对象
	 * @return 查询后返回的数据对象列表（如果查询失败或者无数据返回，则为null或者空列表对象）
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static <T> List<T> queryEntity(String dbType, Connection conn, Class<T> cls, CondiParamList condiList, OrderParamList orderList) throws Exception {
		return queryEntity(dbType, conn, cls, condiList, orderList, null);
	}
	
	/**
	 * 执行数据库映射类的实体数据查询（默认是分页查询，查询的分页默认值，可查看 PageInfo 类）
	 * @param <T> 与传入的 cls 相同的类型
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param cls 数据库表或者视图的实体映射类
	 * @param dbType 数据库类型（由于不同的数据库，查询处理可能不同，所以要传递一个数据库类型）
	 * @param condiList 查询条件列表对象
	 * @param orderList 排序条件列表对象
	 * @param pageInfo 分页条件对象
	 * @return 查询后返回的数据对象列表（如果查询失败或者无数据返回，则为null或者空列表对象）
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static <T> List<T> queryEntity(String dbType, Connection conn, Class<T> cls, CondiParamList condiList, OrderParamList orderList, PageInfo pageInfo) throws Exception {
		
		List<T> result = null;
		
		//参数校验
		if(StrUtil.isEmptyString(dbType)) {
			throw new Exception("数据库类型为空，无法执行查询。");
		}
		
		//关于条件参数的数据库类型处理（如果内部dbType 和 外部 dbType 不一致，则使用外部的）
		if(condiList!=null && !dbType.equalsIgnoreCase(condiList.getDbType())) {
			condiList.setCodec(dbType);
		}
		
		//根据数据库类型，来处理
		switch(dbType) {
		case DBSQLInjectionUtil.DBTYPE_MYSQL:
			result = MysqlQuery.queryEntity(conn, cls, condiList, orderList, pageInfo);
			break;
		case DBSQLInjectionUtil.DBTYPE_ORACLE:
			result = OracleQuery.queryEntity(conn, cls, condiList, orderList, pageInfo);
			break;
		case DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER:
			result = SqlserverQuery.queryEntity(conn, cls, condiList, orderList, pageInfo);
			break;
		default:
			throw new Exception("暂时未支持你所使用的数据库，数据库类型为：["+dbType+"]");
		}
		
		//返回结果
		return result;
	}
	
	/**
	 * 数据库映射的实体对象的更新操作
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param objectList 数据库映射的实体对象列表
	 * @param updatePropertyNames 更新操作，所涉及的属性名，以逗号分隔
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static void updateEntity(Connection conn, List<?> objectList, String updatePropertyNames) throws Exception {
		executeEntity(conn, objectList, SQL_TYPE_UPDATE, updatePropertyNames);
	}
	
	/**
	 * 数据库映射的实体对象的删除操作
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param objectList 数据库映射的实体对象列表
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static void deleteEntity(Connection conn, List<?> objectList) throws Exception {
		executeEntity(conn, objectList, SQL_TYPE_DELETE, "");
	}
	
	/**
	 * 数据库映射的实体对象的插入操作
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param objectList 数据库映射的实体对象列表
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static void insertEntity(Connection conn, List<?> objectList) throws Exception {
		executeEntity(conn, objectList, SQL_TYPE_INSERT, "");
	}
	
	/**
	 * 执行查询语句，并将查询结果写入 SimpleDBData 这个容器中（这个函数在事务同步中使用）
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param preSql 用预处理格式的sql语句，参数位置用 ? 代替
	 * @param params 参数不单单对应查询条件，也对应查询内容。
	 * @return 返回一个虚拟的实体对象列表，如果没有结果可能为空，可能为 null
	 * @throws Exception 如果执行出错，就会抛出异常
	 */
	public static SimpleDBData querySQL(Connection conn, String preSql, List<Object> params) throws Exception {
		//一个容器，用于装载数据以及执行情况
		SimpleDBData sdb = new SimpleDBData();
		//参数校验
		String validString = DBValidUtil.validateParams(conn);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		validString = DBValidUtil.validateParams(preSql, params, SQL_TYPE_SELECT);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		//开始处理
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			//参数预处理
			stmt = conn.prepareStatement(preSql);
			for(int i=0;i<params.size();i++) {
				stmt.setObject(i+1, params.get(i));
			}
			
			//调用之前打印一下 sql 和 参数
			//打印将要执行的查询语句（需要将 Logback 级别调整为 DEBUG 才会显示）
			Stdout.plExecutedSqlInfo(preSql, params);
			
			//调用SQL查询语句
			boolean hasResultSet = stmt.execute();
			if(hasResultSet) {
				rs = stmt.getResultSet();
				if(rs!=null) {
					loadSimpleDBData(sdb, rs);//将数据转换到输出对象
				}
			} else {
				sdb.setResponseStatus(ERR);
				sdb.setResponseInfo("执行的语句没有返回值。");
			}
		} catch(Exception e) {
			throw e;
		} finally {
			if(rs!=null) { try { rs.close(); } catch (Exception e) {} rs = null; }
			if(stmt!=null) { try { stmt.close(); } catch (Exception e) {} stmt = null;}
		}
		return sdb;
	}
	
	/**
	 * 这里用于处理一些临时性的非实体查询（这个函数在事务同步中使用）
	 * @param <T> 与传入的 cls 相同的类型
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param virtualEntityClass 虚拟的实体对象，必须有 MyVirtualEntity 注解
	 * @param preSql 用预处理格式的sql语句，参数位置用 ? 代替
	 * @param params 参数不单单对应查询条件，也对应查询内容。
	 * @return 返回一个虚拟的实体对象列表，如果没有结果可能为空，可能为 null
	 * @throws Exception 如果执行出错，就会抛出异常
	 */
	public static <T> List<T> querySQL(Connection conn, Class<T> virtualEntityClass, String preSql, List<Object> params) throws Exception {
		List<T> tList = null;
		//参数校验
		String validString = DBValidUtil.validateParams(conn);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		validString = DBValidUtil.validateParams(virtualEntityClass);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		validString = DBValidUtil.validateParams(preSql, params, SQL_TYPE_SELECT);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		//执行
		SimpleDBData sdb = querySQL(conn, preSql, params);
		if(OK.equals(sdb.getResponseStatus())) {
			//数据转换
			if(sdb.getData()!=null) {
				tList = loadEntityListBySimpleDBData(virtualEntityClass, sdb);
			}
		}else {
			throw new Exception(sdb.getResponseInfo());
		}
		//销毁SimpleDBData 对象
		sdb.destroy();
		//返回结果
		return tList;
	}
	
	/**
	 * 以 SQL 语句的方式更新数据。这个方法是用于在多个操作下，保持事务一致性的。
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param preSql 数据库 update 语句。语句必须带参数，参数以 '?' 替换符匹配
	 * @param params 数据库 update 语句中的 参数（包括 set 以及 where 条件）
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static void updateSQL(Connection conn, String preSql, List<Object> params) throws Exception {
		executeSQL(conn, preSql, params, SQL_TYPE_UPDATE);
	}
	
	/**
	 * 以 SQL 语句的方式删除数据。这个方法是用于在多个操作下，保持事务一致性的。
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param preSql 数据库 delete 语句。语句必须带参数，参数以 '?' 替换符匹配
	 * @param params 数据库 delete 语句中的 where 条件参数
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static void deleteSQL(Connection conn, String preSql, List<Object> params) throws Exception {
		executeSQL(conn, preSql, params, SQL_TYPE_DELETE);
	}
	
	/**
	 * 以 SQL 语句的方式插入数据。这个方法是用于在多个操作下，保持事务一致性的。
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param preSql 数据库 insert 语句。语句必须带参数，参数以 '?' 替换符匹配
	 * @param params 要插入的数据内容。以二维列表( List&lt;List&lt;Object&gt;&gt; )的形式传入。
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static void insertSQL(Connection conn, String preSql, List<? super Object> params) throws Exception {
		executeSQL(conn, preSql, params, SQL_TYPE_INSERT);
	}
	
	/**
	 * 将查询到的 SimpleDBData 对象，转换为 对应的 数据库表或者视图的映射实体对象列表
	 * @param <T> 与传入的 cls 相同的类型
	 * @param cls 数据库表或者视图映射实体对象
	 * @param sdb 一个数据结果集的简易对象
	 * @return 实体对象列表，可能为 null 或者 一个 大小为 0 的列表
	 * @throws Exception 在数据转换 或者 设置时，反射处理可能抛出异常信息
	 */
	public static <T> List<T> loadEntityListBySimpleDBData(Class<T> cls, SimpleDBData sdb) throws Exception {
		List<T> tList = null;
		//首先映射类不能为null, 然后sdb必须有数据
		if(cls!=null && sdb!=null &&sdb.getData()!=null) {
			//有数据，则可以生成列表对象
			tList = new ArrayList<T>();
			for(int i=0;i<sdb.getData().length;i++) {
				try {
					//先构建对象
					T t = cls.newInstance();
					//根据sdb中的字段信息，填充
					for(int j=0;j<sdb.getColumnName().size();j++) {
						//根据映射类获取 setter 方法
						String setterName = DBStrUtil.genSetterNameFromColumn(sdb.getColumnName().get(j), cls);
						if(StrUtil.isEmptyString(setterName)) {
							//如果数据有这个字段，但是实体没有映射，则跳过
							continue;
						}
						//根据映射类获取 该字段对应的属性名
						String fieldName = DBStrUtil.genNameFromColumn(sdb.getColumnName().get(j), cls);
						
						// 映射对象的属性 类型
						Class<?> targetType = cls.getDeclaredField(fieldName).getType();
						
						//方法获取需要匹配，如果数据类型不匹配，需要修改映射类
						//2024-4-4 原本是从sdb读取jdbc自动映射的java类型。
						//        但是考虑到可能存在修改映射类属性类型的需要， 
						//        这里修改为根据映射类的属性所指定的类型，来设置数据类型。
						Method m = cls.getMethod(setterName, targetType);
						
						// 对象类型转换（数字、时间）
						Object targetObj = sdb.getData()[i][j];
						
						//不为 null 才执行 类型转换
						if(targetObj!=null) {
							
							if(targetObj instanceof Number) {
								// 如果 映射类的属性 为 数字类型，则转换
								// 在获取 sdb 时，数字对象已经转为 BigDecimal 了，参考 translateDictInfoType 方法）
								targetObj = translateNumberObj(targetObj, targetType);
								
							}else if(targetObj instanceof Timestamp
									|| targetObj instanceof java.sql.Date) {
								// 如果 数据库 为 java.sql.Timestamp ，但是映射类是 LocalDateTime 之类要转换
								// 如果 数据库 为 java.sql.Date，但是映射类是 LocalDate 之类的要转换
								targetObj = translateTimeObj(targetObj, targetType);
								
							}else if(targetObj instanceof LocalDateTime
									&& targetType.getName().equalsIgnoreCase("java.sql.Timestamp")) {
								//如果数据库返回的是 LocalDateTime 但是 目标类型是 java.sql.Timestamp
								//这种情况比较少有，不过还是转换一下 （因为 jdk 1.8 之后 推荐用 java.time.* 下面的时间类型。
								// 一般不会用到这代码，除非有特殊需要。目前就 MySQL 8+ 会返回 LocalDateTime ）
								targetObj = Timestamp.valueOf((LocalDateTime)targetObj);
							}
						}
						
						// 调用 setter 方法，设置 数值
						try {
							//setter 方法执行
							m.invoke(t, targetObj);
						}catch(Exception e2) {
							Stdout.pl("============= 数据类型转换出错 ==============");
							String s = String.format("%s, %s, 自动设置数据异常，属性：%s，属性类型：%s，方法：%s，sdb数据类型：%s",
									e2.getMessage(), 
									cls, 
									fieldName, 
									targetType, 
									setterName, 
									sdb.getData()[i][j].getClass());
							Stdout.pl(new Exception(s));
							Stdout.pl("============================================");
							s = null;
							//如果反射处理出错，则跳过
							continue;
						}
					}
					//如果上面执行成功，则设置对象到结果列表
					tList.add(t);
				} catch (Exception e) {
					//报错就全部清空，避免数据异常。
					tList.clear();
					//输出完整的报错信息
					Stdout.pl(e);
					throw new Exception("构造映射对象<"+cls.getTypeName()+">失败");
				}
			}
		}
		return tList;
	}
	
	/**
	 * 将 查找到的 结果集 封装到 SimpleDBData 对象中去
	 * @param sdb 一个 SimpleDBData 对象。作为容器使用
	 * @param rs 数据库操作执行后，返回的结果集
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static void loadSimpleDBData(SimpleDBData sdb, ResultSet rs) throws Exception {
		
		if(sdb!=null && rs!=null) {
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int count = metaData.getColumnCount();
			
			//创建列名列表
			List<String> columns = new ArrayList<String>(count);
			List<String> columnsClass = new ArrayList<String>(count);
			List<String> columnsType = new ArrayList<String>(count);
			for(int i=1;i<=count;i++) {
				// metaData.getColumnName(i) 改为获取 display name 否则获取不到 别名
				columns.add(metaData.getColumnLabel(i));
				//如果数据库是 binary 或者 blog 之类的，类型信息会表述为 [B ，即 byte[] 数组
				columnsClass.add(metaData.getColumnClassName(i).equalsIgnoreCase("[B")?"byte[]":metaData.getColumnClassName(i));
				//
				columnsType.add(metaData.getColumnTypeName(i));
			}
			sdb.setColumnName(columns);
			sdb.setColumnClassName(columnsClass);
			sdb.setColumnTypeName(columnsType);
			
			//创建数据二维列表
			List<List<Object>> dataList = new ArrayList<List<Object>>();
			while(rs.next()) {
				List<Object> tmpDatas = new ArrayList<Object>();
				for(int i=1;i<=count;i++) {
					//数据需要获取Object类型才行，否则后面无法处理
					tmpDatas.add(translateDictInfoType(rs, i));
				}
				dataList.add(tmpDatas);
			}
			
			//将二维列表转换为二维矩阵
			if(dataList.size()>0) {
				int rowCount = dataList.size();
				int columnCount = count;
				Object[][] obj = new Object[rowCount][columnCount];
				for(int i=0;i<rowCount;i++) {
					for(int j=0;j<columnCount;j++) {
						obj[i][j] = dataList.get(i).get(j);
					}
				}
				sdb.setData(obj);
				//清空临时变量
				dataList.clear();
			}
		}
	}
	
	/**
	 * 这个方法用于执行 update、delete、insert 三种类型 的操作
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param preSql 数据库 update 语句。语句必须带参数，参数以 '?' 替换符匹配
	 * @param params 数据库 update 语句中的 参数（包括 set 以及 where 条件）
	 * @param type SQL语句类型，比如  insert 、 delete 、 update 三大类。参考：DBOperator 的 SQL_TYPE 常量
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static void executeSQL(Connection conn, String preSql, List<? super Object> params, String type) throws Exception {
		//参数校验
		String validString = DBValidUtil.validateParams(conn);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		validString = DBValidUtil.validateParams(preSql, params, type);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		
		//在调用之前，打印一下信息
		Stdout.plExecutedSqlInfo(preSql, params);
		
		//开始执行
		PreparedStatement stmt =null;
		try {
			stmt = conn.prepareStatement(preSql);
			//处理参数（要区分参数是那种操作，因为insert操作的params是一个二维列表）
			if(SQL_TYPE_INSERT.equalsIgnoreCase(type)) {
				int firstLen = ((List<?>)params.get(0)).size();
				for(int i=0;i<params.size();i++) {
					@SuppressWarnings("unchecked")
					List<? super Object> tmpList = (List<? super Object>) params.get(i);
					if(ListUtil.isEmptyList(tmpList)) {//对于空的参数列表，跳过
						continue; 
					}else if(firstLen!=tmpList.size()) {//行与行 之间的参数数量不一致时报错。因为 addBatch 对于缺失的数据会自动补充，造成数据错乱
						throw new Exception("参数错误：行与行 之间的参数数量不一致");
					}
					for(int j=0;j<tmpList.size();j++) {
						//使用了预处理，则不需要 esapi 的处理了。
						stmt.setObject(j+1, translateBeforExecuteIUD(tmpList.get(j)));
					}
					stmt.addBatch(); //收集参数
				}
				stmt.executeBatch(); // insert 使用 executeBatch 批量执行
			}else if(SQL_TYPE_UPDATE.equalsIgnoreCase(type) || SQL_TYPE_DELETE.equalsIgnoreCase(type)) {
				for(int i=0;i<params.size();i++) {
					//使用了预处理，则不需要 esapi 的处理了。
					stmt.setObject(i+1, translateBeforExecuteIUD(params.get(i)));
				}
				stmt.executeUpdate(); // update 和 delete 使用 executeUpdate 方法执行语句
			}else {
				throw new Exception("传入的sql类型参数有误，只能为 SQL_TYPE_INSERT、SQL_TYPE_UPDATE、SQL_TYPE_DELETE 其中之一 ");
			}
		} catch(Exception e) {
			throw e; //将捕捉的异常抛出去
		} finally {
			if(stmt!=null) { try { stmt.close(); } catch (Exception e) {} stmt = null;} //这里只需要关闭 PreparedStatement
		}
	}
	
	/**
	 * 这个方法用于执行 update、delete、insert 三种类型 实体的操作
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param objectList 将要执行操作的实体对象列表
	 * @param type 实体操作类型，比如  insert 、 delete 、 update 三大类。参考：DBOperator 的 SQL_TYPE 常量
	 * @param updatePropertyNames 更新操作，所涉及的属性名，以逗号分隔
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static void executeEntity(Connection conn, List<?> objectList, String type, String updatePropertyNames) throws Exception {
		//参数校验
		String errMsg = DBValidUtil.validateParams(conn);
		if(!StrUtil.isEmptyString(errMsg)) {
			throw new Exception(errMsg);
		}
		errMsg = DBValidUtil.validateEntityList(objectList);
		if(!StrUtil.isEmptyString(errMsg)) {
			throw new Exception(errMsg);
		}
		//开始执行
		StringBuffer sb = new StringBuffer();
		if(type.equalsIgnoreCase(SQL_TYPE_UPDATE)) {
			List<List<Object>> params = genUpdateSqlAndParamsByEntityList(objectList, sb, updatePropertyNames);
			for(List<Object> objList: params) {//根据参数列表循环执行（如果效率不高，后面可以改批处理）
				updateSQL(conn, sb.toString(), objList);
			}
		}else if(type.equalsIgnoreCase(SQL_TYPE_DELETE)) {
			//开始执行
			List<List<Object>> params = genDeleteSqlAndParamsByEntityList(objectList, sb);
			for(List<Object> objList: params) {//根据参数列表循环执行（如果效率不高，后面可以改批处理）
				deleteSQL(conn, sb.toString(), objList);
			}
		}else if(type.equalsIgnoreCase(SQL_TYPE_INSERT)) {
			List<Object> params = genInsertSqlAndParamsByEntityList(objectList, sb);
			//调用sql插入操作
			insertSQL(conn, sb.toString(), params);
		}else {
			throw new Exception("传入的sql类型参数有误，只能为 SQL_TYPE_INSERT、SQL_TYPE_UPDATE、SQL_TYPE_DELETE 其中之一 ");
		}
	}
	
	/**
	 * 执行数据库的函数
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param functionName 数据库函数名
	 * @param params 数据库函数的执行参数列表
	 * @param virtualTable 对应数据库的虚表。比如：Oracle 的 dual 。
	 * @return SimpleDBData 一个数据载体，用于存放sql返回的结果。
	 * @throws Exception 如果执行出错，抛出异常
	 */
	public static SimpleDBData executeFunction(Connection conn,  String functionName, List<Object> params, String virtualTable) throws Exception {
		//参数校验
		String validString = DBValidUtil.validateParams(conn);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		validString = DBValidUtil.validateParams(functionName, OBJ_TYPE_FUNC);
		if(StrUtil.isEmptyString(functionName)) {
			throw new Exception(validString);
		}
		
		SimpleDBData sdb = new SimpleDBData();
		
		String preSql = genFunctionSql(functionName, params, virtualTable); //构造调用函数的预处理语句
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(preSql);
			//设置预处理参数
			if(!ListUtil.isEmptyList(params)) {
				for(int i=0;i<params.size();i++) {
					stmt.setObject(i+1, params.get(i));
				}
			}
			
			//在调用之前，打印一下信息
			Stdout.plExecutedSqlInfo(preSql, params);
			
			//调用函数
			boolean hasResultSet = stmt.execute();
			if(hasResultSet) {
				rs = stmt.getResultSet();
				if(rs!=null) {
					loadSimpleDBData(sdb, rs);//将数据转换到输出对象
				}
			} else {
				sdb.setResponseStatus(DBStaticUtil.ERR);
				sdb.setResponseInfo("执行的函数没有返回值。");
			}
		} catch(Exception e) {
			throw e; //将捕捉的异常抛出去
		} finally {
			if(rs!=null) { try { rs.close(); } catch (Exception e) {} rs = null; }
			if(stmt!=null) { try { stmt.close(); } catch (Exception e) {} stmt = null;}
		}
		
		return sdb;
	}
	
	/**
	 * 执行数据库的函数，并将执行结果以 Java 对象的形式返回。
	 * @param <T> 一个类型与 targetClass 指定的类型一样的对象
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param targetClass 返回的数据所对应的数据类型，比如：String, Boolean, Integer 等等
	 * @param functionName 数据库函数名
	 * @param params 数据库函数的执行参数列表
	 * @param virtualTable 对应数据库的虚表。比如：Oracle 的 dual 。
	 * @return 返回一个指定数据类型的对象，可能为 null
	 * @throws Exception 如果执行出错，抛出异常
	 */
	public static <T> T executeFunction(Connection conn, Class<T> targetClass, String functionName, List<Object> params, String virtualTable) throws Exception {
		T t = null;
		//参数校验
		String validString = DBValidUtil.validateParams(conn);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		SimpleDBData sdb = executeFunction(conn, functionName, params, virtualTable);
		if(sdb.getResponseStatus().equalsIgnoreCase(OK) && sdb.getData()!=null) {
			//必须是有数据才处理（如果是数字，时间，需要转换一次）
			Object targetObj = sdb.getData()[0][0];
			//
			if(targetObj!=null && targetObj instanceof Number) {
				// 对象类型转换（数字）
				t = targetClass.cast(translateNumberObj(targetObj, targetClass));
			}else if(targetObj!=null && targetObj instanceof Timestamp) {
				// 对象类型转换（时间）
				t = targetClass.cast(translateTimeObj(targetObj, targetClass));
			}else {
				//其它
				t = targetClass.cast(targetObj);
			}
		} else {
			throw new Exception(Stdout.fplToAnyWhere("函数< %s >执行失败，异常信息：%s", functionName, sdb.getResponseInfo()));
		}
		sdb.destroy();
		return t;
	}
	
	/**
	 * 执行数据库的存储过程
	 * @param dbType 数据库类型。参考 DBSQLInjectionUtil 的 DBTYPE
	 * @param conn 数据库连接对象，由外部传入，用于保持事务一致性。（在方法内，这个连接不回收，由外部调用程序回收）
	 * @param procedureName 存储过程的名称
	 * @param params 存储过程的参数
	 * @return SimpleDBData 用于存放执行结果的对象；如果存储过程有数据返回，可以用 SimpleDBData 类 来接收。
	 * @throws Exception 如果执行出错，抛出异常
	 */
	public static SimpleDBData executeProcedure(String dbType, Connection conn, String procedureName, ProcParamList params) throws Exception {
		
		SimpleDBData sdb = new SimpleDBData();
		
		//参数校验
		String validString = DBValidUtil.validateParams(conn);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		validString = DBValidUtil.validateParams(procedureName, OBJ_TYPE_PROC);
		if(!StrUtil.isEmptyString(validString)) {
			throw new Exception(validString);
		}
		
		//构造调用语句(由于 sql server 数据库 调用 存储过程比较特殊，所以要传入 数据库类型)
		String preSql = genProcedureSql(dbType, procedureName, params);
		
		CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			//预处理
			stmt = conn.prepareCall(preSql);
			//设置参数
			if(params!=null && params.getParamLength()>0) {
				for(int i=1;i<=params.getParamLength();i++) {
					//根据参数的类型，分开处理  输入参数 和 输出参数
					if(params.getParam(i).getParamType().equalsIgnoreCase(ProcParam.TYPE_OUT)) {
						//如果是输出参数，则需要注册输出类型
						stmt.registerOutParameter(i, params.getParam(i).getOutParamSqlType());
					}else {
						//如果是输入参数，则直接设置
						stmt.setObject(i, params.getParam(i).getParamValue());
					}
				}
			}
			
			//调用之前打印一下 sql 和 参数
			Stdout.plExecutedSqlInfo(preSql, params);
			
			//调用存储过程
			boolean hasResultSet = stmt.execute();
			if(hasResultSet) {
				rs = stmt.getResultSet();
				if(rs!=null) {
					//将数据转换到输出对象
					loadSimpleDBData(sdb, rs);
				}
			} else {
				sdb.setResponseStatus(DBStaticUtil.OK);
			}
			//存储过程中的输出参数设置
			if(params!=null && params.getParamLength()>0) {
				for(int i=1;i<=params.getParamLength();i++) {
					ProcParam tmpParam = params.getParam(i);
					
					//只有输出参数需要获取返回的值
					if(tmpParam.getParamType().equals(ProcParam.TYPE_OUT)) {
						
						//（如果是oracle的输出参数，并且类型为游标，则需要转换为 sdb；）
						if(tmpParam.getOutParamSqlType() == oracle.jdbc.OracleTypes.CURSOR) {
							SimpleDBData sdb001 = new SimpleDBData();
							loadSimpleDBData(sdb001, (ResultSet)stmt.getObject(i));
							tmpParam.setParamValue(sdb001);
						}else {
							tmpParam.setParamValue(stmt.getObject(i));
						}
						
					}
					
				}
			}
		} catch(Exception e) {
			throw e;
		} finally {
			if(rs!=null) { try { rs.close(); } catch (Exception e) {} rs = null; }
			if(stmt!=null) { try { stmt.close(); } catch (Exception e) {} stmt = null;}
		}
		
		return sdb;
	}
	
	/**
	 * 构造delete操作的sql语句 以及 返回一个参数列表
	 * @param entityList 需要删除的实体对象列表
	 * @param sqlBuffer 用于返回sql语句的载体
	 * @return 关于这个delete sql 语句所需要的参数列表
	 */
	private static List<List<Object>> genDeleteSqlAndParamsByEntityList(List<?> entityList, StringBuffer sqlBuffer){
		
		List<List<Object>> params = new ArrayList<List<Object>>();
		
		if(!ListUtil.isEmptyList(entityList)) {
			
			StringBuffer deleteSql = new StringBuffer();
			
			boolean genSql = true; //这个是sql拼接标识；第一次循环时构造sql，后面不再重复构造sql
			
			for(int i=0;i<entityList.size();i++) {//根据传入的实体对象列表，循环构造sql语句和sql参数列表
				Object tmp = entityList.get(i);
				String tableName = DBStrUtil.parseClassNameToTableOrViewName(tmp.getClass());
				if(genSql) {
					deleteSql.append("delete from "+tableName +" where ");
				}
				Field[] fs = tmp.getClass().getDeclaredFields();
				List<Object> tmpParams = new ArrayList<Object>();
				
				for(Field f: fs) {//这里根据实体对象内部属性，构造sql语句和sql参数
					if(f.isAnnotationPresent(MyIdGroup.class) && f.isAnnotationPresent(MyColumn.class) 
							                                  && !f.isAnnotationPresent(MyColumnIgnore.class)) {
						String col_name = f.getDeclaredAnnotation(MyColumn.class).name();
						if(genSql) {
							deleteSql.append(" and "+col_name+"=? ");
						}
						try {
							Method m = tmp.getClass().getMethod("get"+StrUtil.makeFirstCharUpperCase(f.getName()));
							Object fValue= m.invoke(tmp);
							tmpParams.add(fValue);
						} catch (Exception e) {
							tmpParams.add(null);
							Stdout.pl(e);
						}
					}
				}
				params.add(tmpParams);
				genSql = false;
			}
			//构造的sql语句有 where and 这样的结构，所以需要去除，以 where 代替
			sqlBuffer.append(deleteSql.toString().toLowerCase().replaceFirst("(where)[\\s]+(and)[\\s]+", "where "));
		}
		return params;
	}
	
	/**
	 * 构造update操作的sql语句 以及 返回一个参数列表
	 * @param entityList 需要更新的实体对象列表
	 * @param sqlBuffer 用于返回sql语句的载体
	 * @param updatePropertyNames 更新操作，所涉及的属性名，以逗号分隔
	 * @return 关于这个update sql 语句所需要的参数列表
	 */
	private static List<List<Object>> genUpdateSqlAndParamsByEntityList(List<?> entityList, StringBuffer sqlBuffer, String updatePropertyNames){
		List<List<Object>> params = new ArrayList<List<Object>>();
		if(!ListUtil.isEmptyList(entityList)) {
			
			//如果 updatePropertyNames 不为空，则需要根据传递过来的属性进行更新，而非全部字段更新
			List<String> propertyNames = new ArrayList<String>();
			if(!StrUtil.isEmptyString(updatePropertyNames)) {
				String[] names = updatePropertyNames.replaceAll("\\s+", "").split(",");
				for(String name: names) {
					propertyNames.add(name);
				}
			}
			
			StringBuffer updateSql = new StringBuffer(); //用于装载 最后完整的 update 语句
			StringBuffer setSql = new StringBuffer(); //用于装载 set 语句
			StringBuffer whereSql = new StringBuffer(); //用于装载 where 语句
			
			boolean genSql = true; //这个是sql拼接标识；第一次循环时构造sql，后面不再重复构造sql
			
			for(int i=0;i<entityList.size();i++) {//根据传入的实体对象列表，循环构造sql语句和sql参数列表
				Object tmp = entityList.get(i);
				String tableName = DBStrUtil.parseClassNameToTableOrViewName(tmp.getClass());
				if(genSql) {
					updateSql.append("update "+tableName +" set ");
				}
				
				Field[] fs = tmp.getClass().getDeclaredFields();
				List<Object> tmpParamsFroSet = new ArrayList<Object>();
				List<Object> tmpParamsFroWhere = new ArrayList<Object>();
				
				//第一次遍历属性，构造set语句和参数
				for(Field f: fs) {
					if(f.isAnnotationPresent(MyColumn.class) 
							&& !f.isAnnotationPresent(MyIdGroup.class)
							&& !f.isAnnotationPresent(MyAutoIncrement.class)
							&& !f.isAnnotationPresent(MyColumnIgnore.class)
							&& !f.isAnnotationPresent(MyColumnReadOnly.class)) {
						//只能 更新（update） 一些普通的列；排除 唯一索引（主键），排除自增列，排除 非数据库列，排除 只读列
						
						//如果有指定要更新的字段，则只需要获取这些字段的值，其它值不需要处理
						if(!ListUtil.isEmptyList(propertyNames) && !propertyNames.contains(f.getName())) {
							continue;
						}
						
						String colName = f.getDeclaredAnnotation(MyColumn.class).name();
						if(genSql) {
							setSql.append(colName+"=?, ");
						}
						try {
							Method m = tmp.getClass().getMethod("get"+StrUtil.makeFirstCharUpperCase(f.getName()));
							Object fValue= m.invoke(tmp);
							tmpParamsFroSet.add(fValue);
						} catch (Exception e) {
							tmpParamsFroSet.add(null);
							Stdout.pl(e);
						}
					}
				}
				//第二次遍历属性，构造where语句和参数
				for(Field f:fs) {
					if(f.isAnnotationPresent(MyIdGroup.class) && f.isAnnotationPresent(MyColumn.class) 
                            && !f.isAnnotationPresent(MyColumnIgnore.class)) {
						String colName = f.getDeclaredAnnotation(MyColumn.class).name();
						if(genSql) {
							whereSql.append(" and "+colName+"=? ");
						}
						try {
							Method m = tmp.getClass().getMethod("get"+StrUtil.makeFirstCharUpperCase(f.getName()));
							Object fValue= m.invoke(tmp);
							tmpParamsFroWhere.add(fValue);
						} catch (Exception e) {
							tmpParamsFroWhere.add(null);
							Stdout.pl(e);
						}
					}
				}
				//组合参数列表
				tmpParamsFroSet.addAll(tmpParamsFroWhere);//由于预处理的标记符号 ? 是按顺序出现的，所以参数也要按顺序
				params.add(tmpParamsFroSet);
				genSql = false;
			}
			//构造的sql语句有 where and 这样的结构，所以需要去除，以 where 代替
			updateSql.append(setSql).append(" where ").append(whereSql);
			//构造的sql语句有 where and 这样的结构，所以需要去除，以 where 代替
			sqlBuffer.append(updateSql.toString().toLowerCase().replaceFirst("(\\,)[\\s]*(where)[\\s]+(and)[\\s]+", " where "));
		}
		
		return params;
	}
	
	/**
	 * 构造insert操作的sql语句 以及 返回一个参数列表
	 * @param entityList 需要插入的实体对象列表
	 * @param sqlBuffer 用于返回sql语句的载体
	 * @return 关于这个insert sql 语句所需要的参数列表
	 */
	private static List<Object> genInsertSqlAndParamsByEntityList(List<?> entityList, StringBuffer sqlBuffer){
		
		List<Object> params = new ArrayList<Object>();
		
		if(!ListUtil.isEmptyList(entityList)) {
			
			StringBuffer insertSql = new StringBuffer();
			StringBuffer colNameSql = new StringBuffer();
			StringBuffer colPreSql = new StringBuffer();
			
			boolean genSql = true; //这个是sql拼接标识；第一次循环时构造sql，后面不再重复构造sql
			
			for(int i=0;i<entityList.size();i++) {//根据传入的实体对象列表，循环构造sql语句和sql参数列表
				Object tmp = entityList.get(i);
				String tableName = DBStrUtil.parseClassNameToTableOrViewName(tmp.getClass());
				if(genSql) {
					insertSql.append("insert into "+tableName +"(");
				}
				Field[] fs = tmp.getClass().getDeclaredFields();
				List<Object> tmpParams = new ArrayList<Object>();
				
				//这里根据实体对象内部属性，构造sql语句和sql参数
				for(Field f: fs) {
					if(f.isAnnotationPresent(MyAutoIncrement.class)
							|| f.isAnnotationPresent(MyColumnReadOnly.class)) {
						//如果是自增字段，则跳过；只读字段也跳过
						continue;
					}
					if(f.isAnnotationPresent(MyColumn.class) && !f.isAnnotationPresent(MyColumnIgnore.class)) {
						String col_name = f.getDeclaredAnnotation(MyColumn.class).name();
						if(genSql) {
							colNameSql.append(col_name+",");
							colPreSql.append("?,");
						}
						try {
							Method m = tmp.getClass().getMethod("get"+StrUtil.makeFirstCharUpperCase(f.getName()));
							Object fValue= m.invoke(tmp);
							tmpParams.add(fValue);
						} catch (Exception e) {
							tmpParams.add(null);
							Stdout.pl(e);
						}
					}
				}
				params.add(tmpParams);
				genSql = false;
			}
			//构造sql
			insertSql.append(colNameSql.toString());
			insertSql.append(")");
			insertSql.append(" values(");
			insertSql.append(colPreSql.toString());
			insertSql.append(")");
			sqlBuffer.append(insertSql.toString().toLowerCase().replaceAll("(\\,\\))", ")"));
		}
		return params;
	}
	
	/**
	 * 
	 * 根据传入的函数名称和函数参数列表，构造一个函数的调用语句
	 * @param funcName 函数名
	 * @param params 参数列表；可以为null值，或者空列表
	 * @param virtualTable 对应数据库的虚表。比如：Oracle 的 dual 。
	 * @return 一个函数的调用语句
	 */
	private static String genFunctionSql(String funcName, List<Object> params, String virtualTable) {
		StringBuffer sb = new StringBuffer();
		if(!StrUtil.isEmptyString(funcName)) {
			sb.append("select ");
			sb.append(funcName);
			sb.append("(");
			if(!ListUtil.isEmptyList(params)) {
				for(int i=0;i<params.size();i++) {
					sb.append("?");
					if(i<params.size()-1) {
						sb.append(",");
					}
				}
			}
			sb.append(")");
			if(!StrUtil.isEmptyString(virtualTable)) {
				sb.append(" from "+virtualTable);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 根据传入的存储过程名称和存储过程参数列表，构造一个存储过程的调用语句
	 * @param dbType 数据库类型。参考 DBSQLInjectionUtil 的 DBTYPE
	 * @param procName 存储过程的名称
	 * @param params 存储过程的参数
	 * @return 存储过程的调用语句。比如：<code>call proc(?, ?)</code>；
	 * 如果是 sql server 则是 <code>exec ?, ? </code>
	 */
	private static String genProcedureSql(String dbType, String procName, ProcParamList params) {
		//定义返回的结果 变量
		StringBuffer sb = new StringBuffer();
		
		//由于 sql server 和 oracle 、 mysql 语法不一样，所以要针对 sql server 单独写一个实现
		switch(dbType) {
		case DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER:
			//拼接执行关键字
			sb.append("exec ");
			//拼接存储过程名
			sb.append(procName);
			//判断有无参数，如果有则循环（因为 sql server 不使用 小括号，所以要留一个空格）
			sb.append(" ");
			if(params!=null && params.getParamLength()>0) {
				for(int i=0;i<params.getParamLength();i++) {
					sb.append("?");
					if(i<params.getParamLength()-1) {
						sb.append(",");
					}
				}
			}
			break;
		default:
			//拼接执行关键字
			sb.append("call ");
			//拼接存储过程名
			sb.append(procName);
			//判断有无参数，如果有则循环
			sb.append("(");
			if(params!=null && params.getParamLength()>0) {
				for(int i=0;i<params.getParamLength();i++) {
					sb.append("?");
					if(i<params.getParamLength()-1) {
						sb.append(",");
					}
				}
			}
			sb.append(")");
			break;
		}
		
		//返回构造的语句
		return sb.toString();
	}
	
	/**
	 * <p>如果传入的对象 oriObj 是 BigDecimal 类型，并且 targetType 是 Number 类型，则按照指定的 targetType 进行类型转换</p>
	 * <p>在 loadSimpleDBData 处理中，已经将 Number 类型的数据转换为 BigDecimal 了，所以这里只需要根据 Java Bean 指定的类型转换</p>
	 * @param oriObj 待处理的源对象
	 * @param targetType 转换后的目标数据类型
	 * @return 如果符合转换条件，则返回转换后的数据对象；如果不符合转换条件，则返回源对象，不进行转换。
	 */
	private static Object translateNumberObj(Object oriObj, Class<?> targetType) {
		//结果对象
		Object result = oriObj;
		//转换的前提条件，oriObj是 BigDecimal 类型，targetType 是 Number 类型的子类，比如：Integer 之类的
		if(oriObj!=null && targetType!=null && oriObj instanceof BigDecimal) {
			//开始转换，主要类型有 Short, Integer, Long, BigInteger, Float, Double
			String targetTypeName = targetType.getName();
			switch(targetTypeName) {
			case "java.lang.Short":
				result = new Short(((BigDecimal)oriObj).shortValue());
				break;
			case "java.lang.Integer":
				result = new Integer(((BigDecimal)oriObj).intValue());
				break;
			case "java.lang.Long":
				result = new Long(((BigDecimal)oriObj).longValue());
				break;
			case "java.math.BigInteger":
				result = ((BigDecimal)oriObj).toBigInteger();
				break;
			case "java.lang.Float":
				result = new Float(((BigDecimal)oriObj).floatValue());
				break;
			case "java.lang.Double":
				result = new Double(((BigDecimal)oriObj).doubleValue());
				break;
			default:
				break;
			}
		}
		return result;
	}
	
	/**
	 * <p>如果传入的对象 oriObj 是 java.sql.Timestamp 类型，并且 targetType 是  Date、LocalDateTime 类型，则按照指定的 targetType 进行类型转换</p>
	 * <p>在 loadSimpleDBData 处理中，已经将 Number 类型的数据转换为 BigDecimal 了，所以这里只需要根据 Java Bean 指定的类型转换</p>
	 * @param oriObj 待处理的源对象
	 * @param targetType 转换后的目标数据类型
	 * @return 如果符合转换条件，则返回转换后的数据对象；如果不符合转换条件，则返回源对象，不进行转换。
	 */
	private static Object translateTimeObj(Object oriObj, Class<?> targetType) {
		//结果对象
		Object result = oriObj;
		
		//转换的前提条件，oriObj是 Timestamp 类型，targetType 是 时间 类型，比如：Date 之类的
		if(oriObj!=null && targetType!=null && oriObj instanceof Timestamp) {
			//开始转换
			String targetTypeName = targetType.getName();
			switch(targetTypeName) {
			case "java.util.Date":
				result = new Date(((Timestamp)oriObj).getTime());
				break;
			case "java.time.LocalDate":
				result = ((Timestamp)oriObj).toLocalDateTime().toLocalDate();
				break;
			case "java.time.LocalDateTime":
				result = ((Timestamp)oriObj).toLocalDateTime();
				break;
			default:
				break;
			}
		}
		
		// 关于 java.sql.Date 转 LocalDate 和 java.util.Date 的处理
		if(oriObj!=null && targetType!=null && oriObj instanceof java.sql.Date) {
			String targetTypeName = targetType.getName();
			switch(targetTypeName) {
			case "java.util.Date":
				// java.sql.Date 是 java.util.Date 的子类，直接转类型即可
				result = (java.util.Date)oriObj;
				break;
			case "java.time.LocalDate":
				// 对于 java.sql.Date 的输出，就是一个日期字符串，比如：2024-12-17
				// 因此转字符串，然后将字符串转换为 LocalDate 即可
				result = LocalDate.parse(oriObj.toString());
				break;
			default:
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * 根据从 JDBC 获取的原始数据对象，做一些类型转换，方便后续处理
	 * @param rs 结果集对象
	 * @param colIndex 列号 从 1 开始
	 * @return 转换后的对象
	 * @throws Exception 如果执行异常，直接抛出错误。
	 */
	private static Object translateDictInfoType(ResultSet rs, int colIndex) throws Exception {
		
		//获取原始的数据对象
		Object result = rs.getObject(colIndex);
		if(result==null) {
			//如果为空，则直接返回。
			return result;
		}
		
		//2024-4-5    如果对象是数字类型，比如 BigInteger, Integer , Long , Double 之类的，则需要转换为 BigDecimal
		//          这么处理主要是为了方便 java bean 在修改属性类型时，转换用
		if(result instanceof Number) {
			result = rs.getBigDecimal(colIndex);
		}
		//对于oracle的TIMESTAMP类型，一律转为 java.sql.Timestamp
		if(result instanceof oracle.sql.TIMESTAMP) {
			result = rs.getTimestamp(colIndex);
		}
		//对于 oracle 的 ROWID 类型，一律转换为 String 类型
		if(result instanceof oracle.sql.ROWID) {
			result = ((oracle.sql.ROWID)result).stringValue();
		}
		//对于 oracle 的 OracleClob 类型，一律转换为 String 类型
		if(result instanceof oracle.jdbc.OracleClob) {
			oracle.jdbc.OracleClob tempClob = (oracle.jdbc.OracleClob)result;
			if(tempClob!=null) {
				result = tempClob.getSubString(1, (int)tempClob.length());
			}
		}
		//对于 oracle 的 OracleNClob 类型，一律转换为 String 类型
		if(result instanceof oracle.jdbc.OracleNClob) {
			oracle.jdbc.OracleNClob tempNClob = (oracle.jdbc.OracleNClob)result;
			if(tempNClob!=null) {
				result = tempNClob.getSubString(1, (int)tempNClob.length());
			}
		}
		//对于 oracle 的 OracleBlob 类型，一律转换为 byte[] 类型
		if(result instanceof oracle.jdbc.OracleBlob) {
			oracle.jdbc.OracleBlob blob = (oracle.jdbc.OracleBlob)result;
			if(blob!=null) {
				result = blob.getBytes(1, (int)blob.length());
			}
		}
		
		//处理完毕，放入临时变量
		return result;
	}
	
	/**
	 * 在执行 insert 、 update 、delete 方法前，对一些数据类型进行转换
	 * @param oriObject 待转换的对象
	 * @return 转换后的对象
	 */
	private static Object translateBeforExecuteIUD(Object oriObject) {
		//这里 对于 不能被 数据库 sql 语句直接识别的 数据类型，需要转换一下
		Object myTmp = oriObject;
		if(myTmp instanceof LocalDateTime) {
			myTmp = Timestamp.valueOf((LocalDateTime)myTmp);
		}
		if(myTmp instanceof LocalDate) {
			myTmp = Timestamp.valueOf(((LocalDate)myTmp).atStartOfDay());
		}
		if(myTmp instanceof java.util.Date) {
			myTmp = new Timestamp(((java.util.Date)myTmp).getTime());
		}
		return myTmp;
	}
	
}
