/******************************************************************************************************

This file "CondiParamList.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
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
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * &gt;&gt;&nbsp;一个关于查询条件的列表（如果不指定数据库类型，默认为 MySQL）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月26日-2024年7月1日
 */
public class CondiParamList {
	
	private String sqlTemplate; //条件语句模板
	private String dbType = DBSQLInjectionUtil.DBTYPE_MYSQL; //数据库类型
	private final List<CondiParam> params; //查询条件对象列表
	
	/**
	 * 查询条件对应的实体类名
	 */
	private Class<?> entityClassName;
	
	/**
	 * &gt;&gt;&nbsp;构造函数
	 */
	public CondiParamList() {
		this.sqlTemplate="";
		this.params = new ArrayList<CondiParam>();
	}
	public CondiParamList(Class<?> entityClassName) {
		this.sqlTemplate="";
		this.params = new ArrayList<CondiParam>();
		this.entityClassName = entityClassName;
	}
	public CondiParamList(String sqlTemplate) {
		this.sqlTemplate=sqlTemplate;
		this.params = new ArrayList<CondiParam>();
	}
	public CondiParamList(String sqlTemplate, Class<?> entityClassName) {
		this.sqlTemplate=sqlTemplate;
		this.params = new ArrayList<CondiParam>();
		this.entityClassName = entityClassName;
	}
	public CondiParamList(String dbType, String sqlTemplate) {
		this.sqlTemplate=sqlTemplate;
		this.params = new ArrayList<CondiParam>();
		this.dbType = dbType;
	}
	public CondiParamList(String dbType, String sqlTemplate, Class<?> entityClassName) {
		this.sqlTemplate=sqlTemplate;
		this.params = new ArrayList<CondiParam>();
		this.dbType = dbType;
		this.entityClassName = entityClassName;
	}
	
	/**
	 * &gt;&gt;&nbsp;获取当前条件列表对象的 数据库类型 信息。参考 DBSQLInjectionUtil 类的 DBTYPE 常量
	 * @return dbType 对象
	 */
	public String getDbType() {
		return this.dbType;
	}
	
	/**
	 * <p>重新设置防 SQL注入处理。这个要根据不同数据库的情况设置。</p>
	 * <p>其实，CondiParamList 默认有数据库类型，是 MySQL 的。使用其他数据库时，才重新设置</p>
	 * @param dbType 数据库类型
	 * @return CondiParamList 对象
	 */
	public CondiParamList setCodec(String dbType) {
		this.dbType = dbType;
		return this;
	}
	
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
	 * &gt;&gt;&nbsp;添加查询条件到列表中（如果条件对象为null，则不加入列表；如果查询条件名称为空，则不加入列表）
	 * @param condi 查询条件对象
	 * @return 返回一个当前对象
	 */
	public CondiParamList addParam(CondiParam condi) {
		// 参数名 和 参数 操作符 一定不能为空。
		if(condi!=null && !StrUtil.isEmptyString(condi.getParamName()) 
				       && !StrUtil.isEmptyString(condi.getOptString())) {
			//对于 is null 和 is not null 可以让参数值为 null
			if(condi.getValue()!=null || condi.getOptString().equals(CondiParam.OPT_IS_NULL) 
					                  || condi.getOptString().equals(CondiParam.OPT_IS_NOT_NULL)){
				this.params.add(condi);
				//对于查询条件列表 已经设置 实体类名，但是参数没有 实体类名，则自动设置
				if(condi.getClassName()==null && this.entityClassName!=null) {
					condi.setClassName(this.entityClassName);
				}
			}
		}
		return this;
	}
	
	/**
	 * &gt;&gt;&nbsp;移除特定位置的查询条件
	 * @param i 要移除的查询条件的序号，从0开始。
	 */
	public void removeParam(int i) {
		this.params.remove(i);
	}
	
	/**
	 * &gt;&gt;&nbsp;移除特定名称的查询条件
	 * @param paramName 查询条件名
	 */
	public void removeParam(String paramName) {
		if(!StrUtil.isEmptyString(paramName)) {
			this.params.removeIf(con->con.getParamName().equals(paramName));
		}
	}
	
	/**
	 * &gt;&gt;&nbsp;移除特定的查询条件对象
	 * @param condi 查询条件对象
	 */
	public void removeParam(CondiParam condi) {
		if(condi!=null) {
			this.params.removeIf( con -> con.getParamName().equals(condi.getParamName()) 
					                     && con.getOptString().equals(condi.getOptString())
					                     && con.getValue().equals(condi.getValue())
					            );
		}
	}
	
	/**
	 * &gt;&gt;&nbsp;删除所有条件参数，清空列表
	 */
	public void removeAll() {
		this.params.clear();
	}
	
	/**
	 * &gt;&gt;&nbsp;获取查询条件的列表长度
	 * @return 返回查询参数的数量
	 */
	public int getSize() {
		return this.params.size();
	}
	
	/**
	 * &gt;&gt;&nbsp;关于查询条件sql语句模板信息的替换（按照模板信息构造sql语句，替换符一般带有":"前缀）
	 * @param sqlTemplate 
	 * @param params
	 */
	private String replaceTempInfo(String sqlTemplate, List<CondiParam> params) {
		String tempSql = sqlTemplate;
		for(int i=0;i<params.size();i++) {
			CondiParam condi = params.get(i);
			while(true) {
				int start = tempSql.indexOf(":"+condi.getSqlTemplateParamName());
				if(start<=-1) {
					break;
				} else {
					int length = (":"+condi.getSqlTemplateParamName()).length();
					tempSql = tempSql.substring(0, start) + condi.toSqlString(this.dbType) + tempSql.substring(start+length);
				}
			}
		}
		return tempSql;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据当前条件，生成一个查询条件的sql语句。
	 * @return 一段经过处理的sql语句
	 */
	public String toSqlString() {
		StringBuffer sb = new StringBuffer();
		if(!StrUtil.isEmptyString(this.sqlTemplate)) {
			//如果模板信息不为空，则按照模板信息构造sql语句，替换符一般带有":"前缀
			sb.append(this.replaceTempInfo(this.sqlTemplate, this.params));
		}else {
			//如果sql模板信息为空，则将所有条件按照 and 关系输出
			for(int i=0;i<this.params.size();i++) {
				sb.append(this.params.get(i).toSqlString(this.dbType));
				if(i<this.params.size()-1) {
					sb.append(" and ");
				}
			}
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		this.params.forEach(condi->{
			sb.append(Stdout.fplToAnyWhere("%s\n", condi));
		});
		return sb.toString();
	}
	
	/**
	 * &gt;&gt;&nbsp;测试
	 * @param args
	 */
	/*
	public static void main(String[] args) throws Exception {
		
		//以MySQL的方式
		CondiParamList params = new CondiParamList(":tel and :gender and (:id or :address1 or :address2)");
		params.addParam(new CondiParam("tel", "' or 1=1 ; drop table test_b; select '15", CondiParam.OPT_LIKE_PRE));
		params.addParam(new CondiParam("gender", "男"));
		params.addParam(new CondiParam("id", ""));
		params.addParam(new CondiParam("address", "阳江", CondiParam.OPT_LIKE_SUF).setSqlTemplateParamName("address1"));
		params.addParam(new CondiParam("address", "广州", CondiParam.OPT_LIKE).setSqlTemplateParamName("address2"));
		Stdout.pl(params.toSqlString());
		
		//以oracle的方式
		CondiParamList params2 = new CondiParamList(DBSQLInjectionUtil.DBTYPE_ORACLE, "");
		params2.addParam(new CondiParam("tel", "' or 1=1 ; drop table test_b; select '15", CondiParam.OPT_LIKE));
		params2.addParam(new CondiParam("gender", "男"));
		params2.addParam(new CondiParam("id", ""));
		params2.addParam(new CondiParam("address", "阳江", CondiParam.OPT_LIKE));
		params2.addParam(new CondiParam("address", "广州", CondiParam.OPT_LIKE));
		Stdout.pl(params2.toSqlString());
		
	}
	*/
	
}
