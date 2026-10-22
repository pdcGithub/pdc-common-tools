/******************************************************************************************************

This file "DatabasePoolManager.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.mickarea.tools.filter.FileNameFilter;
import net.mickarea.tools.utils.PatternUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 一个数据库连接池管理类，通常用于管理多个数据连接池的创建、销毁等工作。
 * @author Michael Pang (Dongcan Pang)
 * @since 2023年5月15日
 */
public class DatabasePoolManager {

	/**
	 * 配置文件的文件名过滤正则
	 */
	public static final String DBCONFIG_REGEXP = "db.*\\.properties";
	/**
	 * 配置文件中连接池名称前缀，用于忽略该连接池
	 */
	public static final String DBCONFIG_IGNORE_PREFIX = "test";
	/**
	 * 配置文件存放位置，一般位于程序包内的路径
	 */
	public static final String DBCONFIG_FOLDER = "/conf/database-config/";
	
	/**
	 * 这是用于存储“连接池”对象的一个并发哈希对象。
	 */
	private ConcurrentHashMap<String, HikariDataSource> dataPools;
	
	/**
	 * 把 管理对象 定义为私有，只能通过 getInstance 方法获取。
	 */
	private static DatabasePoolManager mana;
	/**
	 * 这里用于确保管理类只有一个实例，否则会出现用 new 创建多个管理类的混乱局面。
	 * 注意：这里增加了一个处理。当执行 destroyDataPools 销毁后，通过 getInstance 可以重新初始化 
	 * @return 管理类的唯一实例
	 */
	public static synchronized DatabasePoolManager getInstance() {
		if(mana==null) {
			mana = new DatabasePoolManager();
		}
		// 增加一个处理，当执行 destroyDataPools 销毁后，通过 getInstance 可以重新初始化 
		if(mana.dataPools==null) {
			Stdout.mylogger.debug("The database connection pool manager is starting to reinitialize.");
			// 把旧的引用置空
			mana = null;
			// 重新初始化
			mana = new DatabasePoolManager();
		}
		return mana;
	}
	
	/**
	 * 获取当前的数据库连接池配置文件（以文件夹遍历的方式）
	 * @return 装载有数据库配置的文件名列表
	 */
	private List<String> getDatabaseConfigFileNames(String folderName) {
		
		List<String> result = new ArrayList<String>();
		
		if(!StrUtil.isEmptyString(folderName)) {
			
			//目标包内文件夹
			URL url = Thread.currentThread().getContextClassLoader().getResource(folderName.substring(1));
			Stdout.fpl("将要解析的URL：%s", url);
			
			if(url==null) {
				Stdout.fpl("你传入的文件夹名称有误(%s)，无法生成URL", folderName);
				return result;
			}
			
			//由于打jar包后，和不打包协议不一样，需要分开处理
			if("file".equalsIgnoreCase(url.getProtocol().toLowerCase())) {
				Stdout.fpl("使用普通方式解析:file，文件夹：%s", folderName);
				File configDir = new File(url.getFile());
				File[] fs = configDir.listFiles(new FileNameFilter(DBCONFIG_REGEXP));
				Stdout.plArray(fs);
				if(fs!=null && fs.length>0) {
					for(File f: fs) {
						result.add(f.getName());
					}
				}
			} else if("jar".equalsIgnoreCase(url.getProtocol().toLowerCase())) {
				Stdout.fpl("使用jar方式解析:jar，文件夹：%s", folderName);
				try {
					JarURLConnection jarConn = (JarURLConnection)url.openConnection();
					Enumeration<JarEntry> entrys = jarConn.getJarFile().entries();
					for(;entrys.hasMoreElements();) {
						String name = entrys.nextElement().getName();
						if(Pattern.matches(folderName.substring(1)+DBCONFIG_REGEXP, name)) {
							result.add(name.replaceFirst(folderName.substring(1), ""));
						}
					}
				} catch(Exception e) {
					//如果发生异常，把已经装载的信息清空，保持信息一致。
					result.clear();
					Stdout.fpl("解析jar包时发生错误，路径：%s, 异常：%s", url, e.getMessage());
					Stdout.pl(e);
				}
			} else {
				Stdout.fpl("当前文件协议为：%s，不受支持，请使用 file 或者 jar 协议。", url.getProtocol());
			}
		}else {
			Stdout.pl("传入的文件夹名称为空，不对文件进行解析。");
		}
		
		return result;
	}
	
	/**
	 * 构造函数（主要负责 dataPools 数据库连接池创建）
	 */
	private DatabasePoolManager() {
		
		//数据库连接池的配置文件所在位置
		String folder = DBCONFIG_FOLDER;
		
		//数据库连接池载体，可装载多个连接池，以实现多数据库访问。键值对为==》 连接池名称：连接池对象
		this.dataPools = new ConcurrentHashMap<String, HikariDataSource>();
		
		//获取配置文件的名称
		List<String> fileNames = this.getDatabaseConfigFileNames(folder);
		
		Stdout.pl("获取到的配置文件名为："+fileNames);
		
		fileNames.forEach(name->{
			//如果筛选后，有合适的配置文件，则构建数据库连接池
			HikariConfig dbConf = new HikariConfig(folder+name);
			Stdout.pl("获取到的 HikariConfig 为："+(folder+name));
			//如果连接池的名称开头为test，则不创建
			//如果连接池的名称为空，则不创建
			if(!dbConf.getPoolName().toLowerCase().startsWith(DBCONFIG_IGNORE_PREFIX) && !StrUtil.isEmptyString(dbConf.getPoolName())) {
				try {
					if(this.dataPools.containsKey(dbConf.getPoolName())) {
						//如果管理池中已经有这个名字的连接池对象了，则说明可能配文件中的poolName 可能重名了。要提示，并跳过
						Stdout.fpl("请注意::连接池名称(%s) <<已经存在>>，可能配置出现重复值，请检查...", dbConf.getPoolName());
					}else {
						this.dataPools.put(dbConf.getPoolName(), new HikariDataSource(dbConf));
					}
				}catch(Exception e) {
					Stdout.fpl("创建连接池(%s)出错，%s", dbConf.getPoolName(), e.getMessage());
					Stdout.fpl("< 如果要屏蔽该数据库连接池的配置文件(%s)，请在poolName属性加上前缀test，如：poolName=test_second >", dbConf.getPoolName());
					Stdout.fpl("< 也可以通过将poolName信息清空来屏蔽该数据库的配置文件，如：poolName= >");
					Stdout.pl(e);
				}
			}
		});
		
		//最后查看有多少个连接池被创建了。
		Stdout.fpl("连接池已全部创建，创建成功数量为：%s；创建的连接池为：%s", this.dataPools.size(), this.dataPools);
	}
	
	/**
	 * 根据配置中定义的数据库连接池信息，返回一个数据库连接池对象。这里容器用的并发Map，不用加同步了。
	 * @param poolName 连接池名称，在配置文件中的 poolName 属性。
	 * @return 返回一个数据库连接池对象。如果匹配不到 poolName 则返回 null。
	 */
	public DataSource getDataPoolByName(String poolName){
		
		// 定义一个 返回结果
		DataSource source = null;
		
		// 如果 dataPools 是空的 或者 是 null，则之际返回 null
		if(this.dataPools==null || this.dataPools.isEmpty()) return source;
		
		// 如果配置名字为空，则不需要处理
		if(poolName==null || PatternUtil.matches("\\s+", poolName)) return source;
		
		// 搜索并赋值
		source = (DataSource)this.dataPools.get(poolName);
		
		// 返回结果
		return source;
	}
	
	/**
	 * <p>释放资源，关闭所管理的连接池。内部因为 dataPools 处理多，所以增加了 同步锁</p>
	 * <p>注意：它应该在，所有程序停止时才调用。比如：程序完全退出前，调用它释放资源。</p>
	 * <p>由于连接池有异常捕捉，这里就不需要异常捕捉处理了。它的关闭操作，由新建的线程完成</p>
	 */
	public synchronized void destroyDataPools() {
		
		// 如果资源池本来就是 null 则不需要处理。
		if(this.dataPools==null || this.dataPools.isEmpty()) {
			Stdout.fpl("The database connection pool Map has already been cleared, no further action is needed.");
			// 直接结束
			return ;
		}
		
		// 开始处理
		Stdout.fpl("The operation to clear the database connection pool begins now.");
		
		// 首先释放连接池（使用并行流 同时释放多个数据库链接）
		this.dataPools.entrySet().parallelStream().forEach(entry->{
			// 释放连接池
			entry.getValue().close();
		});
		
		// 确认状态
		this.dataPools.entrySet().stream().forEach(entry->{
			// 打印资源处理情况
			if(entry.getValue().isClosed()) {
				Stdout.fpl("Database connection pool %s closed successfully", entry.getKey());
			}else {
				Stdout.fpl("Failed to close database connection pool %s. ", entry.getKey());
			}
		});
		
		// 清空连接池存放的容器 Map
		this.dataPools.clear();
		this.dataPools = null;
		
		// mana 对象是 static 的属于类，保留引用可以防止 get 方法出现空指针异常。
	}
	
}
