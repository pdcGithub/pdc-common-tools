/******************************************************************************************************

This file "DBTransAction.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.db;

import java.sql.Connection;
import java.sql.SQLException;

import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.database.DBOperator;

/**
 * &gt;&gt;&nbsp;数据库事务相关的操作抽象类。这个类作为父类，具体的数据库业务处理类，应该继承这个类。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月13日-2023年7月16日
 */
public abstract class DBTransAction implements DBTrans {
	
	private DBOperator opt = null;
	
	/**
	 * &gt;&gt;&nbsp;设置 DBOperator 对象
	 * @param opt
	 */
	public void setOpt(DBOperator opt) {
		this.opt = opt;
	}
	
	/**
	 * &gt;&gt;&nbsp;获取 DBOperator 对象
	 * @return DBOperator 对象
	 */
	public DBOperator getOpt() {
		return opt;
	}
	
	/**
	 * 构造函数
	 */
	public DBTransAction() {
	}
	/**
	 * 构造函数
	 * @param operator 以 DBOperator 类型对象为入参。
	 */
	public DBTransAction(DBOperator operator) {
		this.opt = operator;
	}
	
	/**
	 * &gt;&gt;&nbsp;这个函数作为本抽象类的一个执行入口。
	 * @return 如果执行出错，则返回信息；否则，返回空字符串。
	 */
	public String run() {
		StringBuffer msg = new StringBuffer();
		Connection conn = null;
		try {
			conn = opt.getDb().getConnection();
			this.doTrans(conn);
			conn.commit(); //事务提交
		} catch (Exception e) {
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				msg.append(String.format("发生数据库异常：SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			}else {
				msg.append("发生其它异常："+e.getMessage()); // 其它异常
			}
			if(conn!=null) {
				try {conn.rollback(); Stdout.pl("数据库操作回滚成功。"); } catch (SQLException e1) { msg.append("数据库操作回滚出错："+e1.getMessage());
					Stdout.pl(e1);
				}
			}
			//输出异常的栈信息
			Stdout.pl(e);
		}finally {
			//这里使用了连接池，所以 connection 对象不需要置null，close方法会把连接归还连接池
			if(conn!=null) { try { conn.close(); } catch (Exception e) {}}
		}
		return msg.toString();
	}
	
	/**
	 * &gt;&gt;&nbsp;这个函数留给具体的实现类来处理。主要用于保持数据库操作的事务一致性（执行有异常直接抛出即可）
	 * <p>事务的提交操作，由 父类 DBTransAction 来处理，子类不需要处理 commit 操作</p>
	 * @param conn 数据库操作类产生的 数据库会话对象，由父类提供，不需要传值。
	 * @throws Exception 一些执行时的异常
	 */
	public abstract void doTrans(Connection conn) throws Exception ;
	
}
