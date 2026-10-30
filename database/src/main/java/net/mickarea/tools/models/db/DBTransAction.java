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

import javax.sql.DataSource;

import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.database.DBOperator;

/**
 * 数据库事务相关的操作抽象类。这个类作为父类，具体的数据库业务处理类，应该继承这个类。
 * 作为一个数据库操作类，它需要的是数据库链接，而不是什么操作对象。所以 DBOperator 类的作用只是提供一个数据库链接而已。
 * <p>注意：这个抽象类有2个内置对象：DBOperator 和 DataSource。当两个都赋值的情况下，优先从 DataSource 获取数据库链接。</p>
 * @author Michael Pang (Dongcan Pang)
 * @since 2023年7月13日
 */
public abstract class DBTransAction implements DBTrans {
	
	/**
	 * 数据库操作对象。它用于提供数据库链接。
	 */
	private DBOperator opt = null;
	
	/**
	 * 数据库连接源对象，相当于数据库连接池。它用于提供数据库链接。
	 */
	private DataSource source = null;
	
	/**
	 * 设置 DBOperator 数据库操作对象。它用于提供数据库链接。
	 * <p>注意：为了避免已经设置好的内容被修改，如果内置对象不是null，则拒绝新的对象赋值。</p>
	 * @param opt 数据库操作对象
	 */
	public void setOpt(DBOperator opt) {
		if(this.opt==null) this.opt = opt;
	}
	
	/**
	 * 获取 DBOperator 数据库操作对象。它用于提供数据库链接。
	 * @return DBOperator 对象
	 */
	public DBOperator getOpt() {
		return opt;
	}
	
	/**
	 * 获取 DataSource 数据库连接源对象，相当于数据库连接池。它用于提供数据库链接。
	 * @return DataSource 数据库连接源对象
	 */
	public DataSource getSource() {
		return source;
	}

	/**
	 * 设置 DataSource 数据库连接源对象，相当于数据库连接池。它用于提供数据库链接。
	 * <p>注意：为了避免已经设置好的内容被修改，如果内置对象不是null，则拒绝新的对象赋值。</p>
	 * @param source DataSource 数据库连接源对象
	 */
	public void setSource(DataSource source) {
		if(this.source==null) this.source = source;
	}

	/**
	 * 这是 无参 构造函数。
	 * 这里需要用 setter 方法，手动设置 DBOperator 或者 DataSource。当两个都赋值的情况下，优先从 DataSource 获取数据库链接。
	 */
	public DBTransAction() {
	}
	
	/**
	 * 这是带参的构造函数。这里以 DBOperator 作为参数传入。
	 * 但是，当 DBOperator 和 DataSource。当两个都赋值的情况下，优先从 DataSource 获取数据库链接。
	 * @param operator 数据库操作对象
	 */
	public DBTransAction(DBOperator operator) {
		this.opt = operator;
	}
	
	/**
	 * 这是带参的构造函数。这里以 DataSource 作为参数传入。
	 * 但是，当 DBOperator 和 DataSource。当两个都赋值的情况下，优先从 DataSource 获取数据库链接。
	 * @param source 数据库连接源对象，相当于数据库连接池。
	 */
	public DBTransAction(DataSource source) {
		this.source = source;
	}

	/**
	 * 这个函数作为本抽象类的一个执行入口。对于 Exception 这里有 try ... catch 处理。
	 * 并且，内部会将异常信息打印到日志 和 返回给调用者。
	 * @return 如果执行出错，则返回信息；否则，返回空字符串。
	 */
	public String run() {
		
		// 首先，定义一个 信息存储容器。当发生异常，往里面写内容。
		StringBuffer msg = new StringBuffer();
		
		// 然后，确定一个待用的 数据库连接对象
		Connection conn = null;
		
		// 开始执行具体的操作。这由子类决定
		try {
			// 先初始化链接对象（source 对象优先，然后是 opt 对象）
			// 这里受初始化的影响，可能抛异常。
			conn = this.source != null ? this.source.getConnection() : this.opt.getDb().getConnection();
			
			// 如果 数据库连接有问题，则抛异常
			if(conn==null) throw new NullPointerException(
				Stdout.fplToAnyWhere("The obtained database connection object is null. opt=%s, source=%s", this.opt, this.source));
			
			// 开始执行数据处理。如果有异常这里会抛出
			this.doTrans(conn);
			
			//事务提交 
			conn.commit();
			
		} catch (Exception e) {
			
			//打印异常的栈信息
			Stdout.pl(e);
			
			// 上面已经打印异常信息了。这里构造一个异常信息给函数返回即可。
			if(e instanceof SQLException) {
				// SQL 数据库返回的异常
				SQLException ex = (SQLException)e;
				msg.append(
					Stdout.fplToAnyWhere(
						"An sql exception occurred, information:%s, state:%s, code:%s", 
						ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			} else {
				// 一般的异常
				msg.append(Stdout.fplToAnyWhere("An exception occurred, info:%s", e.getMessage())); 
			}
			
			// 数据库回滚操作
			if(conn!=null) {
				try {
					conn.rollback(); 
					Stdout.mylogger.debug("The rollback of database operations was successful."); // 打印到 debug 级别
				} catch (Exception e1) {
					Stdout.mylogger.debug("The rollback of database operations was failed."); // 打印到 debug 级别
					Stdout.pl(e1);
				}
			}
			
		} finally {
			//这里使用了连接池，所以 connection 对象不需要置 null，close方法会把连接归还连接池
			if(conn!=null) {
				try {
					// 归还链接到资源池
					conn.close(); 
				} catch (Exception e) {
					// 这里打印下吧
					Stdout.pl(e);
				}
			}
		}
		
		// 这里返回执行过程的异常信息。如果没有异常，则没有内容，是空字符串。
		return msg.toString();
		
	}
	
	/**
	 * 这个函数留给具体的实现类来处理。主要用于保持数据库操作的事务一致性（执行有异常直接抛出即可）
	 * <p>事务的提交操作，由 父类 DBTransAction 来处理，子类不需要处理 commit 操作</p>
	 * @param conn 数据库操作类产生的 数据库会话对象，由父类提供，不需要传值。
	 * @throws Exception 一些执行时的异常
	 */
	public abstract void doTrans(Connection conn) throws Exception ;
	
}
