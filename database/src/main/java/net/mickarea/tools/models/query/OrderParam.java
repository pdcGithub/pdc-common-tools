/******************************************************************************************************

This file "OrderParam.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBStrUtil;

/**
 * &gt;&gt;&nbsp;排序对象，用于构造排序字段sql语句
 * <p>排序对象一般是针对单个字段处理的。创建 OrderParam 对象后，放入 OrderParamList 中等待查询调用</p>
 * <p>在使用上，paramName 属性是指查询的数据库字段名；</p>
 * <p>如果按照实体类的属性名查询，需要指定 className 属性，即实体类的class值</p>
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月2日-2023年10月23日
 */
public class OrderParam {
	
	/**
	 * &gt;&gt;&nbsp;字段排序方式，升序（默认排序方式）
	 */
	public static String OPT_ASC = "asc";
	/**
	 * &gt;&gt;&nbsp;字段排序方式，降序
	 */
	public static String OPT_DESC = "desc";
	
	private String alias = null;
	private String paramName = null;
	private String orderOpt = OPT_ASC; //排序信息默认升序
	private Class<?> className = null;
	
	/**
	 * &gt;&gt;&nbsp;无参构造函数，所有值需要通过 setter 方法手动设定
	 */
	public OrderParam() {
	}
	/**
	 * &gt;&gt;&nbsp;有参构造函数（设置排序字段名）
	 * @param paramName 要排序的数据库字段名
	 */
	public OrderParam(String paramName) {
		this.paramName = paramName;
	}
	/**
	 * &gt;&gt;&nbsp;有参构造函数（设置排序字段名，排序方式）
	 * @param paramName 要排序的数据库字段名
	 * @param orderOpt 排序方式，OrderParam.OPT_ASC 升序，OrderParam.OPT_DESC 降序
	 */
	public OrderParam(String paramName, String orderOpt) {
		this.paramName = paramName;
		this.orderOpt = orderOpt;
	}
	/**
	 * &gt;&gt;&nbsp;有参构造函数（设置排序字段名，排序方式，字段所属表的别名）
	 * @param paramName 要排序的数据库字段名
	 * @param orderOpt 排序方式，OrderParam.OPT_ASC 升序，OrderParam.OPT_DESC 降序
	 * @param alias 如果查询的表有别名，则设置。否则，设置为 null。比如：<code>select a.id, a.name from user a where a.id=1;</code> 中，
	 * a 就是 id 字段的表别名
	 */
	public OrderParam(String paramName, String orderOpt, String alias) {
		this.paramName = paramName;
		this.orderOpt = orderOpt;
		this.alias = alias;
	}
	/**
	 * &gt;&gt;&nbsp;有参构造函数（设置排序字段名，排序方式，字段所属表的别名，实体类的类名）
	 * @param paramName 要排序的数据库字段名
	 * @param orderOpt 排序方式，OrderParam.OPT_ASC 升序，OrderParam.OPT_DESC 降序
	 * @param alias 如果查询的表有别名，则设置。否则，设置为 null。比如：<code>select a.id, a.name from user a where a.id=1;</code> 中，
	 * a 就是 id 字段的表别名
	 * @param className 查询字段所属实体类的类名。当 paramName 值为实体类的属性名，则需要设置类名，以供框架内容转化。
	 */
	public OrderParam(String paramName, String orderOpt, String alias, Class<?> className) {
		this.paramName = paramName;
		this.orderOpt = orderOpt;
		this.alias = alias;
		this.className = className;
	}
	
	//setter and getter
	public String getAlias() {
		return alias;
	}
	public OrderParam setAlias(String alias) {
		this.alias = alias;
		return this;
	}
	public String getParamName() {
		return paramName;
	}
	public OrderParam setParamName(String paramName) {
		this.paramName = paramName;
		return this;
	}
	public String getOrderOpt() {
		return orderOpt;
	}
	public OrderParam setOrderOpt(String orderOpt) {
		this.orderOpt = orderOpt;
		return this;
	}
	public Class<?> getClassName() {
		return className;
	}
	public OrderParam setClassName(Class<?> className) {
		this.className = className;
		return this;
	}
	
	/**
	 * &gt;&gt;&nbsp;构造排序信息并返回
	 * @return 排序列的sql语句段落
	 */
	public String toSqlString() {
		StringBuffer result = new StringBuffer();
		//只有当参数名不为空的时候才构造
		if(!StrUtil.isEmptyString(this.paramName)) {
			//构造别名
			if(!StrUtil.isEmptyString(this.alias)) {
				result.append(this.alias+".");
			}
			//构造字段内容
			if(this.className!=null) {
				result.append(DBStrUtil.parsePropertyNameToColumnName(className, this.paramName));
			}else {
				result.append(this.paramName);
			}
			//构造排序参数 asc 或者 desc ；如果为空，默认 asc
			if(!StrUtil.isEmptyString(this.orderOpt)) {
				result.append(" "+this.orderOpt);
			}else {
				result.append(" "+OPT_ASC);
			}
		}
		return result.toString();
	}
	
	@Override
	public String toString() {
		return Stdout.fplToAnyWhere("排序对象{alias:%s, paramName:%s, orderOpt:%s, className:%s}", 
				this.alias, this.paramName, this.orderOpt, this.className);
	}
	
	/*
	public static void main(String[] args) {
		
		Stdout.pl(new OrderParam());
		Stdout.pl(new OrderParam().toSqlString());
		Stdout.pl(new OrderParam("testAId"));
		Stdout.pl(new OrderParam("testAId").toSqlString());
		Stdout.pl(new OrderParam("testAId", OPT_DESC));
		Stdout.pl(new OrderParam("testAId", OPT_DESC).toSqlString());
		Stdout.pl(new OrderParam("testAId", OPT_DESC, "b"));
		Stdout.pl(new OrderParam("testAId", OPT_DESC, "b").toSqlString());
		Stdout.pl(new OrderParam("testAId", OPT_DESC, "b", TestB.class));
		Stdout.pl(new OrderParam("testAId", OPT_DESC, "b", TestB.class).toSqlString());
		
		Stdout.pl(new OrderParam().setParamName("examScore").setClassName(TestB.class).toSqlString());
	}
	*/
	
}
