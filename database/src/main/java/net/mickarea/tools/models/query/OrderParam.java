/******************************************************************************************************

This file "OrderParam.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import java.util.Arrays;
import java.util.List;

import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.annotation.MyVirtualEntity;
import net.mickarea.tools.utils.AnnotationUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBStrUtil;

/**
 * 排序对象，用于构造排序字段sql语句
 * <p>排序对象一般是针对单个字段处理的。创建 OrderParam 对象后，放入 OrderParamList 中等待查询调用</p>
 * <p>在使用上，paramName 属性是指查询的数据库字段名；</p>
 * <p>如果按照实体类的属性名查询，需要指定 className 属性，即实体类的class值</p>
 * @author Michael Pang (Dongcan Pang)
 * @since 2023年6月2日
 */
public class OrderParam {
	
	/**
	 * 字段排序方式，升序（默认排序方式）
	 */
	public static String OPT_ASC = "asc";
	
	/**
	 * 字段排序方式，降序
	 */
	public static String OPT_DESC = "desc";
	
	/**
	 * 这是字段排序方式的列表。主要用于判断 排序方式字符串 是否合规。
	 */
	public static final List<String> OPT_LIST = Arrays.asList(OPT_ASC, OPT_DESC);
	
	/**
	 * 这是 排序字段的表别名。比如：select k.user_id from users k order by k.user_id desc ; 中的 k
	 */
	private String alias = null;
	
	/**
	 * 这是 排序字段的名称。如果是实体属性名，则需要指定 className 来进行翻译。
	 */
	private String paramName = null;
	
	/**
	 * 这是 排序操作符。默认为 OPT_ASC 升序。
	 */
	private String orderOpt = OPT_ASC;
	
	/**
	 * 这是 排序字段 对应的实体类信息。当需要从实体属性翻译为字段名，则需要这个信息。
	 */
	private Class<?> className = null;
	
	// ============================================== setter and getter
	
	/**
	 * 获取 排序字段的表别名
	 * @return 排序字段的表别名
	 */
	public String getAlias() {
		return this.alias;
	}
	
	/**
	 * 设置 排序字段的表别名。注意 null 和 空白字符串 都不执行设置处理。
	 * @param alias 排序字段的表别名。设置时，会把所有空白字符去掉。
	 * @return 本对象的引用
	 */
	public OrderParam setAlias(String alias) {
		// 先去掉所有空白字符 或者 null
		String tmp = StrUtil.removeAllBlankStrings(alias); 
		// 如果有值，则设置 
		if(!StrUtil.isEmptyString(tmp)) this.alias = tmp;
		// 返回 本对象的引用
		return this;
	}
	
	/**
	 * 获取 排序字段的名称。如果是实体属性名，则需要指定 className 来进行翻译。
	 * @return 排序字段的名称
	 */
	public String getParamName() {
		return this.paramName;
	}
	
	/**
	 * 设置 排序字段的名称。如果是实体属性名，则需要指定 className 来进行翻译。
	 * 注意 null 和 空白字符串 都不执行设置处理。
	 * @param paramName 排序字段的名称。设置时，会把所有空白字符去掉。
	 * @return 本对象的引用
	 */
	public OrderParam setParamName(String paramName) {
		// 先去掉所有空白字符 或者 null
		String tmp = StrUtil.removeAllBlankStrings(paramName); 
		// 如果有值，则设置 
		if(!StrUtil.isEmptyString(tmp)) this.paramName = tmp;
		// 返回 本对象的引用
		return this;
	}
	
	/**
	 * 获取 排序操作符。默认为 OPT_ASC 升序。
	 * @return 排序操作符。默认为 OPT_ASC 升序。
	 */
	public String getOrderOpt() {
		return this.orderOpt;
	}
	
	/**
	 * 设置 排序操作符。对于排序操作符，只能是 OPT_ASC, OPT_DESC 这2个常量中的一个。
	 * @param orderOpt 如果操作符符合要求，则设置。否则，不执行设置处理。
	 * @return 本对象的引用
	 */
	public OrderParam setOrderOpt(String orderOpt) {
		// 如果符合要求，则设置。
		if(OPT_LIST.contains(orderOpt)) this.orderOpt = orderOpt;
		// 返回 本对象的引用
		return this;
	}
	
	/**
	 * 获取 排序字段 对应的实体类信息。当需要从实体属性翻译为字段名，则需要这个信息。
	 * @return 排序字段 对应的实体类信息。
	 */
	public Class<?> getClassName() {
		return this.className;
	}
	
	/**
	 * 设置 排序字段 对应的实体类信息。当需要从实体属性翻译为字段名，则需要这个信息。
	 * @param className 排序字段 对应的实体类信息。它必须是有 实体注解（MyTableOrView 或者 MyVirtualEntity）。否则，将不执行设置处理。
	 * @return 本对象的引用
	 */
	public OrderParam setClassName(Class<?> className) {
		// 首先不能为空，然后必须有实体类注解
		if(className!=null 
			&& AnnotationUtil.isAnnotationPresentByOne(
				className, Arrays.asList(MyTableOrView.class, MyVirtualEntity.class))) this.className = className;
		// 返回 本对象的引用
		return this;
	}
	
	// ============================================== 构造函数
	
	/**
	 * 无参构造函数，创建一个空的排序条件对象。它所有值需要通过 setter 方法手动设定
	 */
	public OrderParam() {
		// 这里没有容器处理，直接空着就行了。
	}
	
	/**
	 * 有参构造函数。创建一个排序条件对象。它一般是针对单个字段处理的。创建 OrderParam 对象后，放入 OrderParamList 中等待查询调用。
	 * @param paramName 排序字段的名称。如果是实体属性名，则需要指定 className 来进行翻译。
	 */
	public OrderParam(String paramName) {
		// 调用其它构造函数，进行初始化
		this(paramName, null, null, null);
	}
	
	/**
	 * 有参构造函数。创建一个排序条件对象。它一般是针对单个字段处理的。创建 OrderParam 对象后，放入 OrderParamList 中等待查询调用。
	 * @param paramName 排序字段的名称。如果是实体属性名，则需要指定 className 来进行翻译。
	 * @param orderOpt 排序操作符。默认为 OPT_ASC 升序。
	 */
	public OrderParam(String paramName, String orderOpt) {
		// 调用其它构造函数，进行初始化
		this(paramName, orderOpt, null, null);
	}
	
	/**
	 * 有参构造函数。创建一个排序条件对象。它一般是针对单个字段处理的。创建 OrderParam 对象后，放入 OrderParamList 中等待查询调用。
	 * @param paramName 排序字段的名称。如果是实体属性名，则需要指定 className 来进行翻译。
	 * @param orderOpt 排序操作符。默认为 OPT_ASC 升序。
	 * @param alias 排序字段的表别名。比如：select k.user_id from users k order by k.user_id desc ; 中的 k
	 */
	public OrderParam(String paramName, String orderOpt, String alias) {
		// 调用其它构造函数，进行初始化
		this(paramName, orderOpt, alias, null);
	}
	
	/**
	 * 有参构造函数。创建一个排序条件对象。它一般是针对单个字段处理的。创建 OrderParam 对象后，放入 OrderParamList 中等待查询调用。
	 * @param paramName 排序字段的名称。如果是实体属性名，则需要指定 className 来进行翻译。
	 * @param orderOpt 排序操作符。默认为 OPT_ASC 升序。
	 * @param alias 排序字段的表别名。比如：select k.user_id from users k order by k.user_id desc ; 中的 k
	 * @param className 排序字段 对应的实体类信息。当需要从实体属性翻译为字段名，则需要这个信息。
	 */
	public OrderParam(String paramName, String orderOpt, String alias, Class<?> className) {
		// 调用 setter 方法，用于保证处理方式是一致的。
		this.setParamName(paramName);
		this.setOrderOpt(orderOpt);
		this.setAlias(alias);
		this.setClassName(className);
	}
	
	// ============================================= 业务函数
	
	/**
	 * 根据 当前的对象内置参数信息，构造排序信息并返回。比如：" k.user_id desc " 
	 * @return 排序列的sql语句段落
	 */
	public String toSqlString() {
		
		// 首先，我们明确单个排序字段 对应的信息有 2个 部分: 字段名、排序操作符
		// 所以，我们只需要拼接出 这2个东西就行了。
		
		// 定义 字段名
		String tmpColName = StrUtil.isEmptyString(this.getParamName()) ? "" : this.getParamName();
		
		// 定义 排序操作符
		String tmpColOpt = StrUtil.isEmptyString(this.getOrderOpt()) ? "" : this.getOrderOpt();
		
		// 字段的表别名
		String tmpAlias = StrUtil.isEmptyString(this.getAlias()) ? "" : this.getAlias();
		
		// 如果 有写实体类信息，而且属性字符串有设置，则翻译一下。
		if(this.getClassName()!=null && tmpColName.length()>0) {
			// 如果匹配不到，可能返回空白字符串。
			String tmp = DBStrUtil.parsePropertyNameToColumnName(this.getClassName(), tmpColName);
			// 翻译成功，才赋值
			if(!StrUtil.isEmptyString(tmp)) tmpColName = tmp;
		}
		
		// 如果字段名处理好了，则处理 表别名。如果没有字段名，则不需要拼接表的别名。
		if(!StrUtil.isEmptyString(tmpAlias) && tmpColName.length()>0) tmpColName = Stdout.fplToAnyWhere("%s.%s", tmpAlias, tmpColName);
		
		// 最后返回结果（如果有排序操作信息，则拼接；没有则不需要拼接）
		return StrUtil.isEmptyString(tmpColOpt) ? tmpColName : Stdout.fplToAnyWhere("%s %s", tmpColName, tmpColOpt.toLowerCase());
		
	}
	
	// ====================================== 信息输出
	
	/**
	 * 这是一个对象的私有属性输出函数。它只输出私有属性，用于信息比对。
	 */
	@Override
	public String toString() {
		
		// 使用工具类打印 这个对象的内部信息
		String fieldsInfo = StrUtil.getJavaBeanFieldsInfo(this);
		
		// 因为内部还有静态属性，所以先过滤掉。
		return Stdout.fplToAnyWhere("%s{%s", 
			this.getClass().getSimpleName(), fieldsInfo.substring(fieldsInfo.indexOf("(private)")));
		
	}
	
}
