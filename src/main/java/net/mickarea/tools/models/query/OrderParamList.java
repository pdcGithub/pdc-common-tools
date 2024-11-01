/******************************************************************************************************

This file "OrderParamList.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import java.util.ArrayList;
import java.util.List;

import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * &gt;&gt;&nbsp;排序对象列表，用于构造排序字段sql语句
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月2日-2024年7月2日
 */
public class OrderParamList {

	/**
	 * &gt;&gt;&nbsp;排序参数列表
	 */
	private final List<OrderParam> params;
	
	/**
	 * 排序字段对应的实体类名
	 */
	private Class<?> entityClassName;
	
	/**
	 * @return the entityClassName
	 */
	public Class<?> getEntityClassName() {
		return entityClassName;
	}
	/**
	 * @param entityClassName the entityClassName to set
	 */
	public void setEntityClassName(Class<?> entityClassName) {
		this.entityClassName = entityClassName;
	}
	
	/**
	 * 构造函数
	 */
	public OrderParamList() {
		this.params = new ArrayList<OrderParam>();
	}
	public OrderParamList(Class<?> entityClassName) {
		this.params = new ArrayList<OrderParam>();
		this.entityClassName = entityClassName;
	}
	
	/**
	 * &gt;&gt;&nbsp;添加排序信息
	 * @param order 排序对象
	 * @return 返回一个排序参数列表对象
	 */
	public OrderParamList addParam(OrderParam order) {
		if(order!=null && !StrUtil.isEmptyString(order.getParamName())) {
			this.params.add(order);
			if(order.getClassName()==null && this.entityClassName!=null) {
				order.setClassName(this.entityClassName);
			}
		}
		return this;
	}
	
	/**
	 * &gt;&gt;&nbsp;删除一个排序参数
	 * @param i 要参数序号
	 */
	public void removeParam(int i) {
		this.params.remove(i);
	}
	
	/**
	 * &gt;&gt;&nbsp;移除特定名称的排序参数
	 * @param paramName 排序参数的名称
	 */
	public void removeParam(String paramName) {
		if(!StrUtil.isEmptyString(paramName)) {
			this.params.removeIf(order->order.getParamName().equals(paramName));
		}
	}
	
	/**
	 * &gt;&gt;&nbsp;移除特定排序参数
	 * @param order 排序参数对象
	 */
	public void removeParam(OrderParam order) {
		if(order!=null && !StrUtil.isEmptyString(order.getParamName())) {
			this.removeParam(order.getParamName());
		}
	}
	
	/**
	 * &gt;&gt;&nbsp;删除所有条件参数，清空列表
	 */
	public void removeAll() {
		this.params.clear();
	}
	
	/**
	 * &gt;&gt;&nbsp;生成排序字段sql字符串，比如： <code>a.id asc, b.obj_id desc</code> 这样
	 * @return 排序字段sql字符串
	 */
	public String toSqlString() {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<this.params.size();i++) {
			sb.append(this.params.get(i).toSqlString());
			if(i<this.params.size()-1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		this.params.forEach(o->{
			sb.append(Stdout.fplToAnyWhere("%s\n", o));
		});
		return sb.toString();
	}
	
	/**
	 * &gt;&gt;&nbsp;测试函数
	 * @param args 
	 */
	/*
	public static void main(String[] args) {
		
		OrderParam p1 = new OrderParam();
		OrderParam p2 = new OrderParam("id");
		OrderParam p3 = new OrderParam("name", OrderParam.OPT_DESC);
		OrderParam p4 = new OrderParam().setAlias("a").setParamName("age").setOrderOpt(OrderParam.OPT_DESC);
		OrderParam p5 = new OrderParam("name");
		
		OrderParamList list = new OrderParamList();
		list.addParam(p1);
		list.addParam(p2);
		list.addParam(p3);
		list.addParam(p4);
		list.addParam(p5);
		
		Stdout.pl(list);
		
		Stdout.pl(list.toSqlString());
		
		list.removeParam(0);
		list.removeParam("age");
		
		Stdout.pl(list.toSqlString());
		
		list.removeParam(p5);
		
		Stdout.pl(list.toSqlString());
		
	}
	*/
	
}
