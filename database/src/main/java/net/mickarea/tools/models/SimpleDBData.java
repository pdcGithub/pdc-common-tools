/******************************************************************************************************

This file "SimpleDBData.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models;

import java.util.List;

import net.mickarea.tools.utils.database.DBStaticUtil;

/**
 * 这是一个简单的数据库查询结果对象。它会按字段顺序，储存整个查询结果的返回信息。比如：字段名、数据类型等等。
 * 这个类一般用于自动装配。当然，手动设置也是可以的。
 * @author Michael Pang (Dongcan Pang)
 * @since 2022年12月13日
 */
public class SimpleDBData {

	/**
	 * 查询结果的 列名 列表
	 */
	private List<String> columnName;
	/**
	 * 查询结果的 列名对应的Java类名 列表
	 */
	private List<String> columnClassName;
	/**
	 * 查询结果的 列明对应的数据库类型名 列表
	 */
	private List<String> columnTypeName;
	/**
	 * 查询结果的 数据结果。这是一个二维数组。
	 */
	private Object[][] data;
	/**
	 * 查询结果的 响应状态：一般都是 {@link DBStaticUtil#OK}
	 */
	private String responseStatus = DBStaticUtil.OK;
	/**
	 * 查询结果的 响应状态相关的内容信息
	 */
	private String responseInfo = null;
	/**
	 * 获取 查询结果的 列名 列表
	 * @return 查询结果的 列名 列表
	 */
	public List<String> getColumnName() {
		return columnName;
	}
	/**
	 * 设置 查询结果的 列名 列表
	 * @param columnName 查询结果的 列名 列表
	 */
	public void setColumnName(List<String> columnName) {
		this.columnName = columnName;
	}
	/**
	 * 获取 查询结果的 列名对应的Java类名 列表
	 * @return 查询结果的 列名对应的Java类名 列表
	 */
	public List<String> getColumnClassName() {
		return columnClassName;
	}
	/**
	 * 设置 查询结果的 列名对应的Java类名 列表
	 * @param columnClassName 查询结果的 列名对应的Java类名 列表
	 */
	public void setColumnClassName(List<String> columnClassName) {
		this.columnClassName = columnClassName;
	}
	/**
	 * 获取 查询结果的 数据结果。这是一个二维数组。
	 * @return 查询结果的 数据结果。这是一个二维数组。
	 */
	public Object[][] getData() {
		return data;
	}
	/**
	 * 设置 查询结果的 数据结果。这是一个二维数组。
	 * @param data 查询结果的 数据结果。这是一个二维数组。
	 */
	public void setData(Object[][] data) {
		this.data = data;
	}
	/**
	 * 获取 查询结果的 响应状态。一般都是 {@link DBStaticUtil#OK} 或者 {@link DBStaticUtil#ERR}
	 * @return 查询结果的 响应状态
	 */
	public String getResponseStatus() {
		return responseStatus;
	}
	/**
	 * 设置 查询结果的 响应状态。一般都是 {@link DBStaticUtil#OK} 或者 {@link DBStaticUtil#ERR}
	 * @param responseStatus 查询结果的 响应状态
	 */
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	/**
	 * 获取 查询结果的 响应状态相关的内容信息
	 * @return 查询结果的 响应状态相关的内容信息
	 */
	public String getResponseInfo() {
		return responseInfo;
	}
	/**
	 * 设置 查询结果的 响应状态相关的内容信息
	 * @param responseInfo 查询结果的 响应状态相关的内容信息
	 */
	public void setResponseInfo(String responseInfo) {
		this.responseInfo = responseInfo;
	}
	/**
	 * 获取 查询结果的 列明对应的数据库类型名 列表
	 * @return 查询结果的 列明对应的数据库类型名 列表
	 */
	public List<String> getColumnTypeName() {
		return columnTypeName;
	}
	/**
	 * 设置 查询结果的 列明对应的数据库类型名 列表
	 * @param columnTypeName 查询结果的 列明对应的数据库类型名 列表
	 */
	public void setColumnTypeName(List<String> columnTypeName) {
		this.columnTypeName = columnTypeName;
	}
	
	/**
	 * 这是一个空参数的构造函数。它会构建一个建议的数据库查询结果对象。这个类一般用于自动装配。当然，手动设置也是可以的。
	 */
	public SimpleDBData() {
	}
	
	/**
	 * 关于 SimpleDBData 对象的销毁操作。因为这个对象包含较多内容，如果不处理可能内存会占用较多。这里手工销毁对象
	 */
	public void destroy() {
		//清空字段名列表
		if(this.columnName!=null) {
			this.columnName.clear();
			this.columnName=null;
		}
		//清空类型名列表
		if(this.columnClassName!=null) {
			this.columnClassName.clear();
			this.columnClassName=null;
		}
		//清空类型名列表
		if(this.columnTypeName!=null) {
			this.columnTypeName.clear();
			this.columnTypeName=null;
		}
		//清空数据
		if(this.data!=null) {
			this.data=null;
		}
		//清空状态
		if(this.responseStatus!=null) {
			this.responseStatus=null;
		}
		//清空响应信息
		if(this.responseInfo!=null) {
			this.responseInfo=null;
		}
	}
	
	/**
	 * 重写了对象的输出方法。用于打印结构化的查询结果信息。
	 * @return 结构化的查询结果信息
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Database information object printing started >>>>>>>>>>>>>>>>>>>>>\n");
		sb.append("The Retrieved status signal is: "+this.responseStatus+", status information: "+this.responseInfo+"\n");
		sb.append("Retrieved column name:\n");
		sb.append(this.columnName==null?"<Object is null>\n":(this.columnName.toString())+"\n");
		sb.append("Retrieved column data java class:\n");
		sb.append(this.columnClassName==null?"<Object is null>\n":(this.columnClassName.toString())+"\n");
		sb.append("Retrieved column database field type:\n");
		sb.append(this.columnTypeName==null?"<Object is null>\n":(this.columnTypeName.toString())+"\n");
		sb.append("Retrieved data:\n");
		if(this.data!=null) {
			 for(int i=0;i<this.data.length;i++) {
				 for(int j=0;j<this.data[i].length;j++) {
					 sb.append(this.data[i][j]);
					 if(j<this.data[i].length-1) {
						 sb.append(", ");
					 }
				 }
				 sb.append("\n");
			 }
		} else {
			sb.append("<Object is null>\n");
		}
		sb.append("Database information object printing finished <<<<<<<<<<<<<<<<<<<<<");
		return sb.toString();
	}
	
}
