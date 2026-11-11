/******************************************************************************************************

This file "CondiParam.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.annotation.MyVirtualEntity;
import net.mickarea.tools.models.query.abstracts.impls.MySQLValueToString;
import net.mickarea.tools.models.query.abstracts.impls.OracleValueToString;
import net.mickarea.tools.models.query.abstracts.impls.SqlServerValueToString;
import net.mickarea.tools.utils.AnnotationUtil;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;
import net.mickarea.tools.utils.database.DBStrUtil;

/**
 * 查询条件对象，用于构造查询字段sql语句
 * <p>查询条件对象一般是针对单个字段处理的。创建 CondiParam 对象后，放入 CondiParamList 中等待查询调用</p>
 * <p>在使用上，paramName 属性是指查询的数据库字段名；</p>
 * <p>如果按照实体类的属性名查询，需要指定 className 属性，即实体类的class值</p>
 * @author Michael Pang (Dongcan Pang)
 * @since 2023年5月21日
 */
public class CondiParam {
	
	/**
	 * 条件操作符，“等于”，如果参数不指定，则默认为这个
	 */
	public static final String OPT_EQ = "=";
	
	/**
	 * 条件操作符，“大于”
	 */
	public static final String OPT_GT = ">";
	
	/**
	 * 条件操作符，“大于等于”
	 */
	public static final String OPT_GTE = ">=";
	
	/**
	 * 条件操作符，“小于”
	 */
	public static final String OPT_LT = "<";
	
	/**
	 * 条件操作符，“小于等于”
	 */
	public static final String OPT_LTE = "<=";
	
	/**
	 * 条件操作符，“不等于”
	 */
	public static final String OPT_NOT_EQ = "<>";
	
	/**
	 * 条件操作符，“前后模糊匹配，%a%”
	 */
	public static final String OPT_LIKE = "like";
	
	/**
	 * 条件操作符，“前模糊匹配，a%”
	 */
	public static final String OPT_LIKE_PRE = "like_pre";
	
	/**
	 * 条件操作符，“后模糊匹配，%a”
	 */
	public static final String OPT_LIKE_SUF = "like_suf";
	
	/**
	 * 条件操作符，“为 NULL”
	 */
	public static final String OPT_IS_NULL = "is null";
	
	/**
	 * 条件操作符，“不为 NULL”
	 */
	public static final String OPT_IS_NOT_NULL = "is not null";
	
	/**
	 * 条件操作符，“正则匹配”。
	 * <p>要注意，不是所有的数据库都兼容 正则匹配。MySQL8 和 Oracle 11 可以用 REGEXP_LIKE 函数。但是 SQL Server 要 2025 才支持</p>
	 */
	public static final String OPT_REGEXP = "regexp";
	
	/**
	 * 条件操作符，“包含匹配 in”
	 */
	public static final String OPT_IN = "in";
	
	/**
	 * 这是条件操作符的列表。主要用于判断 操作符 是否为这个列表中的一个。
	 */
	private static final List<String> OPT_LIST = Arrays.asList(OPT_EQ, OPT_GT, OPT_GTE, OPT_LT, OPT_LTE, OPT_NOT_EQ,
																OPT_LIKE, OPT_LIKE_PRE, OPT_LIKE_SUF, OPT_IS_NULL, OPT_IS_NOT_NULL,
																OPT_REGEXP, OPT_IN);
	
	/**
	 * 这是 构建 SQL 语句的查询条件时，字段对应的 表别名。如果这是一个表或者视图的查询，是没有配置别名的需要。
	 */
	private String alias;
	
	/**
	 * 这是 构建 SQL 语句的查询条件时，字段名。
	 * 另外，如果填写了 实体Java Bean 的属性名，可以通过指定本类的 className 属性来自动映射 实体Java Bean。然后，自动匹配对应的字段名。
	 */
	private String paramName;
	
	/**
	 * 这是 构建 SQL 语句的查询条件时，对应字段的条件操作符。默认是 OPT_EQ ，即 相等。
	 */
	private String optString = OPT_EQ;
	
	/**
	 * 这是 构建 SQL 语句的查询条件时，字段对应的条件值。
	 */
	private Object value;
	
	/**
	 * 这是 构建 SQL 语句的查询条件时，条件字段 对应的 实体Java Bean类。它主要是负责把实体属性，翻译为对应字段名。
	 * 如果 paramName 填写了字段名，则 className 是可以不配置的。
	 */
	private Class<?> className = null;
	
	/**
	 * 这是 CondiParamList 使用模板处理时，本条件对应的 模板占位符 名称。
	 * 如果没有使用模板查询条件，则不需要配置这个属性。
	 */
	private String sqlTemplateParamName;
	
	/**
	 * 查询条件对象，用于构建一个查询条件对应的 SQL 语句。这是 无参构造函数，所有值需要通过 setter 方法手动设定
	 */
	public CondiParam() {
	}
	
	/**
	 * 查询条件对象，用于构建一个查询条件对应的 SQL 语句。这是 有参构造函数（设置查询条件数据库字段名，字段的值）。
	 * 这里的条件操作符号，为默认值，OPT_EQ，即 相等。
	 * @param paramName 查询条件的字段名
	 * @param value 查询条件的字段值
	 */
	public CondiParam(String paramName, Object value) {
		this.setParamName(paramName);
		this.setValue(value);
		// 默认的话，模板参数替换符，设置为与条件参数一致。
		this.setSqlTemplateParamName(paramName);
	}
	
	/**
	 * 查询条件对象，用于构建一个查询条件对应的 SQL 语句。这是 有参构造函数（设置查询条件数据库字段名，字段的值，条件操作符）
	 * @param paramName 查询条件的字段名
	 * @param value 查询条件的字段值
	 * @param optString 查询条件的操作符：可写 CondiParam 中的 OPT_EQ， OPT_GT 等等常量。
	 */
	public CondiParam(String paramName, Object value, String optString) {
		this.setParamName(paramName);
		this.setValue(value);
		this.setOptString(optString);
		// 默认的话，模板参数替换符，设置为与条件参数一致。
		this.setSqlTemplateParamName(paramName);
	}
	
	/**
	 * 查询条件对象，用于构建一个查询条件对应的 SQL 语句。有参构造函数（设置查询条件数据库字段名，字段的值，条件操作符，表别名）
	 * @param paramName 查询条件的字段名
	 * @param value 查询条件的字段值
	 * @param optString 查询条件的操作符：可写 CondiParam 中的 OPT_EQ， OPT_GT 等等
	 * @param alias 查询条件的字段所属表的别名。
	 * 如果查询的表有别名，则设置。否则，设置为 null。
	 * 比如：<p>select a.id, a.name from user a where a.id=1;</p> 中，a 就是 id 字段的表别名
	 */
	public CondiParam(String paramName, Object value, String optString, String alias) {
		this.setAlias(alias);
		this.setParamName(paramName);
		this.setValue(value);
		this.setOptString(optString);
		// 默认的话，模板参数替换符，设置为与条件参数一致。
		this.setSqlTemplateParamName(paramName);
	}
	
	/**
	 * 查询条件对象，用于构建一个查询条件对应的 SQL 语句。有参构造函数（设置查询条件数据库字段名，字段的值，条件操作符，表别名，实体类类名）
	 * @param paramName 查询条件的字段名
	 * @param value 查询条件的字段值
	 * @param optString 查询条件的条件操作符：可写 CondiParam 中的 OPT_EQ， OPT_GT 等等
	 * @param alias 查询条件的字段所属表的别名。
	 * 如果查询的表有别名，则设置。否则，设置为 null。
	 * 比如：<p>select a.id, a.name from user a where a.id=1;</p> 中，a 就是 id 字段的表别名
	 * @param className 查询条件的字段所属实体类的类名。当 paramName 值为实体类的属性名，则需要设置类名，让框架自动转化属性名为字段名。
	 */
	public CondiParam(String paramName, Object value, String optString, String alias, Class<?> className) {
		this.setAlias(alias);
		this.setParamName(paramName);
		this.setValue(value);
		this.setOptString(optString);
		// 默认的话，模板参数替换符，设置为与条件参数一致。
		this.setSqlTemplateParamName(paramName);
		this.setClassName(className);
	}
	
	// ================ getter and setter
	
	/**
	 * 获取 查询条件的 字段名 或者 实体类的属性名
	 * @return 查询条件的 字段名 或者 实体类的属性名
	 */
	public String getParamName() {
		return paramName;
	}
	
	/**
	 * 设置 查询条件的 字段名 或者 实体类的属性名（设置值的时候，会去掉所有的空白字符）
	 * @param paramName 查询条件的 字段名 或者 实体类的属性名。如果传入的参数值是 null，则设置 空字符 ""。
	 * @return 返回本对象的引用。主要用于链式写法。
	 */
	public CondiParam setParamName(String paramName) {
		this.paramName = StrUtil.removeAllBlankStrings(paramName);
		return this;
	}
	
	/**
	 * 获取 查询条件的 操作字符串。
	 * @return 查询条件的 操作字符串。
	 */
	public String getOptString() {
		return optString;
	}
	
	/**
	 * 设置 查询条件的 操作字符串。可写 CondiParam 中的 OPT_EQ， OPT_GT 等等（设置值的时候，会去掉前后的空白字符）操作字符串必须是在 CondiParam 的 OPT 常量中的一个。
	 * @param optString 查询条件的 操作字符串。如果传入的参数值 不是 OPT常量 或者 为null，则设置 空字符 ""。
	 * @return 返回本对象的引用。主要用于链式写法。
	 */
	public CondiParam setOptString(String optString) {
		
		// 先处理一次空字符
		this.optString = StrUtil.isEmptyString(optString) ? "" : optString.trim();
		
		// 如果 optString 非空，但不在 OPT 常量中，则 设置为 空字符。
		if(this.optString.length()>0 && !OPT_LIST.contains(this.optString)) this.optString = "";
		
		// 返回，本对象的引用
		return this;
	}
	
	/**
	 * 获取 查询条件的 值。
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * 设置 查询条件的 值。
	 * @param value 查询条件的 值。传入什么值，就设置什么值。
	 * @return 返回本对象的引用。主要用于链式写法。
	 */
	public CondiParam setValue(Object value) {
		this.value = value;
		return this;
	}
	
	/**
	 * 获取 查询条件的 表或者视图的别名
	 * @return 查询条件的 表或者视图的别名
	 */
	public String getAlias() {
		return alias;
	}
	
	/**
	 * 设置 查询条件的 表或者视图的别名（设置值的时候，会去掉所有的空白字符）。
	 * @param alias 查询条件的 表或者视图的别名。如果传入的参数值是 null，则设置 空字符 ""。
	 * @return 返回本对象的引用。主要用于链式写法。
	 */
	public CondiParam setAlias(String alias) {
		this.alias = StrUtil.removeAllBlankStrings(alias);
		return this;
	}
	
	/**
	 * 获取 查询条件的 模板占位符 名称
	 * @return 查询条件的 模板占位符 名称
	 */
	public String getSqlTemplateParamName() {
		return sqlTemplateParamName;
	}
	
	/**
	 * 设置 查询条件的 模板占位符 名称（设置值的时候，会去掉所有的空白字符）
	 * @param sqlTemplateParamName 查询条件的 模板占位符 名称。如果传入的参数值是 null，则设置 空字符 ""。
	 * @return 返回本对象的引用。主要用于链式写法。
	 */
	public CondiParam setSqlTemplateParamName(String sqlTemplateParamName) {
		this.sqlTemplateParamName = StrUtil.removeAllBlankStrings(sqlTemplateParamName);
		return this;
	}
	
	/**
	 * 获取 查询条件的 字段所属的实体类。
	 * @return 查询条件的 字段所属的实体类。
	 */
	public Object getClassName() {
		return this.className;
	}
	
	/**
	 * 设置 查询条件的 字段所属的实体类。
	 * @param className 查询条件的 字段所属的实体类。
	 * 如果 className 是 实体类（表、视图、虚拟实体），则传入什么值，就设置什么值。如果不是，则 设置 null。
	 * @return 返回本对象的引用。主要用于链式写法。
	 */
	public CondiParam setClassName(Class<?> className) {
		// 如果 className 是带 MyTableOrView 或者 MyVirtualEntity 注解的类，则赋值。
		if(AnnotationUtil.isAnnotationPresentByOne(className, Arrays.asList(MyTableOrView.class, MyVirtualEntity.class))) {
			this.className = className;
		} else {
			// 否则，设置为 null
			this.className = null;
		}
		// 返回本对象的引用
		return this;
	}
	
	/**
	 * 根据当前 CondiParam 对象 的查询条件信息，拼接出一个 条件 sql 语句。
	 * @param cls 条件字段 对应的 实体Java Bean类。注意：这里有注解校验。必须是带 MyTableOrView 或者 MyVirtualEntity 注解的类才行。
	 * @param dbType 数据库类型字符串。参考 DBSQLInjectionUtil 类的 DBTYPE 常量
	 * @return 一个拼接好的查询条件 sql 语句。
	 */
	private String toSqlString(Class<?> cls, String dbType) {
		
		// 定义一个返回结果
		String result = "";
		
		// 处理字段或者属性名，最后都会转换成数据库字段
		String tmpParamName = this.getParamName();
		
		//如果有匹配的实体类信息，则执行对象翻译。（实体类必须是带有 指定注解的类）
		if(cls!=null 
				&& AnnotationUtil.isAnnotationPresentByOne(cls, Arrays.asList(MyTableOrView.class, MyVirtualEntity.class))) {
			// 把属性翻译为 字段名
			tmpParamName = DBStrUtil.parsePropertyNameToColumnName(cls, tmpParamName);
		}
		
		// 如果 Annotation 注解找不到，那就还是原来的值
		if(tmpParamName.trim().length()<=0) tmpParamName = this.getParamName();
		
		// 如果有表或者视图的别名，则加上；否则，不处理
		if(!StrUtil.isEmptyString(this.getAlias())) {
			tmpParamName = this.getAlias() + "." + tmpParamName; // 如果有前缀，则加上
		}
		
		// 处理 查询条件的值
		String tmpValue = "";
		
		// 注意：这里不是预处理。这里是 直接生成一个 SQL 语句的条件参数值。比如：col_1 = 100 或者 col_2 like '%name%' 这样的东西。
		
		// 构建 这个查询条件 对应的字符串形式的值信息。
		// 因为 in 操作，有 1000 个参数的限制，要注意生成的字符串处理。
		if(OPT_IN.equals(this.optString) && this.value instanceof List) {
			tmpValue = this.valueObjectToString((List<?>)this.value, dbType);
		} else {
			tmpValue = this.valueObjectToString(this.value, dbType);
		}
		
		
		if(tmpValue==null || tmpValue.trim().length()<=0) {
			// 如果值信息是 空白字符串，则返回 ''；如果是 "null" 则返回 'null'
			tmpValue = "''";
		} else if(tmpValue.trim().equalsIgnoreCase("null")) {
			// 如果 null 字符串，则加上前后 单引号。
			tmpValue = "'null'";
		}
		
		//根据关系操作符构造
		switch(this.optString) {
			case OPT_LIKE_PRE:
				result = Stdout.fplToAnyWhere("%s %s '%s%%'", tmpParamName, this.optString.replaceFirst("_pre", ""), tmpValue.substring(1, tmpValue.length()-1));
				break;
			case OPT_LIKE_SUF:
				result = Stdout.fplToAnyWhere("%s %s '%%%s'", tmpParamName, this.optString.replaceFirst("_suf", ""), tmpValue.substring(1, tmpValue.length()-1));
				break;
			case OPT_LIKE:
				result = Stdout.fplToAnyWhere("%s %s '%%%s%%'", tmpParamName, this.optString, tmpValue.substring(1, tmpValue.length()-1));
				break;
			case OPT_IS_NULL:
			case OPT_IS_NOT_NULL:
				result = Stdout.fplToAnyWhere("%s %s", tmpParamName, this.optString);
				break;
			case OPT_IN:
				result = Stdout.fplToAnyWhere("%s %s (%s)", tmpParamName, this.optString, tmpValue);
				break;
			case OPT_REGEXP:
				// 对于正则，它应该使用 REGEXP_LIKE 函数
				result = Stdout.fplToAnyWhere("regexp_like(%s, %s)", tmpParamName, tmpValue);
				break;
			default:
				// 默认处理
				result = Stdout.fplToAnyWhere("%s %s %s", tmpParamName, this.optString, tmpValue);
				break;
		}
		
		// 返回结果
		return result;
	}
	
	/**
	 * 根据当前 CondiParam 对象 的查询条件信息，拼接出一个 条件 sql 语句。
	 * <p>注意：这里不是预处理。这里是 直接生成一个 SQL 语句的条件参数值。比如：col_1 = 100 或者 col_2 like '%name%' 这样的东西。</p>
	 * @param dbType 数据库类型。参考 DBSQLInjectionUtil 类的 DBTYPE 常量。如果不是指定类型，则返回 带 error 的异常信息。
	 * @return 一个拼接好的查询条件 sql 语句。
	 */
	public String toSqlString(String dbType) {
		return this.toSqlString(this.className, dbType);
	}
	
	/**
	 * 这里是关于本对象的一个属性信息输出。它主要打印内部属性的值。
	 * @return 内部属性的值拼接成的字符串。
	 */
	@Override
	public String toString() {
		
		// 使用工具类打印 这个对象的内部信息
		String fieldsInfo = StrUtil.getJavaBeanFieldsInfo(this);
		
		// 因为内部还有静态属性，所以先过滤掉。
		return fieldsInfo
				.replaceAll("\\(public static final\\)[a-zA-Z0-9\\_]+\\=.*?\\,", "")
				.replaceFirst("\\(private static final\\).+?\\]\\,", "")
				.replaceFirst("[\\s]+", "");
		
	}
	
	/**
	 * 把不同类型的value值，转换成一个能显示出来的字符串。
	 * <p>因为涉及到具体数据库的语法 和 防注入处理，所以这里要指定数据库的类型信息。</p>
	 * @param value 条件对象的值。如果传入的是 null，则返回 字符串 "null"。
	 * @param dbType 数据库类型。参考 DBSQLInjectionUtil 类的 DBTYPE 常量。如果不是指定类型，则返回 带 error 的异常信息。
	 * @return 一个字符串。如果参数异常，会返回一段带 error 的信息。
	 */
	public String valueObjectToString(Object value, String dbType) {
		
		// 定义一个默认的返回值
		String result = "dbType("+dbType+") error";
		
		// 如果数据库类型为 null 则 直接返回
		if(dbType==null) return result;
		
		// 经过处理后，设置一个返回值
		switch(dbType) {
		case DBSQLInjectionUtil.DBTYPE_MYSQL:
			result = new MySQLValueToString().valueToString(value);
			break;
		case DBSQLInjectionUtil.DBTYPE_MYSQL_ANSI:
			result = new MySQLValueToString(dbType).valueToString(value);
			break;
		case DBSQLInjectionUtil.DBTYPE_ORACLE:
			result = new OracleValueToString().valueToString(value);
			break;
		case DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER:
			result = new SqlServerValueToString().valueToString(value);
		}
		
		// 返回
		return result;
	}
	
	/**
	 * 把不同类型的value值，转换成一个能显示出来的字符串 （这个函数主要用于处理 List 类型的数据）
	 * @param valueList 一个条件值的列表。如果是空列表，则返回 字符串 "null"。
	 * @param dbType 数据库类型。参考 DBSQLInjectionUtil 类的 DBTYPE 常量。如果不是指定类型，则返回 带 error 的异常信息。
	 * @return 一个条件值的字符串
	 */
	public String valueObjectToString(List<?> valueList, String dbType) {
		
		// 定义一个默认的返回值
		String result = "dbType("+dbType+") error";
		
		// 校验入参 valueList
		if(ListUtil.isEmptyList(valueList)) return "null";
		// 校验入参 dbType 
		if(!DBSQLInjectionUtil.DB_TYPE_LIST.contains(dbType)) return result;
		
		// 开始处理（通过 list 的 流遍历）
		result = valueList.stream()
						// 过滤掉 null 值
						.filter(obj->obj!=null)
						// 把对象转换为 一个 sql 查询可识别的信息。
						.map(obj->this.valueObjectToString(obj, dbType))
						// 过滤掉一些无用的拼接内容
						.filter(str->!str.equals("''"))
						// 收集，并在结果之间，拼接一个 英文逗号。
						.collect(Collectors.joining(", "));
		
		// 返回
		return result;
	}
	
}
