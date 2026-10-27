/******************************************************************************************************

This file "DatabasePoolManager.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.PatternUtil;
import net.mickarea.tools.utils.PropertiesUtil;
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
	 * 构造函数（主要负责 dataPools 数据库连接池创建）
	 */
	private DatabasePoolManager() {
		
		//数据库连接池的配置文件所在位置。properties 搜索使用了资源路径，不能 / 开头。
		String propertiesFolder = DBCONFIG_FOLDER.substring(1);
		
		// 数据库配置文件的匹配规则，仅名字，不含文件后缀
		String propertiesNameRegexp = DBCONFIG_REGEXP.replaceFirst(Pattern.quote("\\.properties"), "");
		
		//数据库连接池载体，可装载多个连接池，以实现多数据库访问。键值对为==》 连接池名称：连接池对象
		this.dataPools = new ConcurrentHashMap<String, HikariDataSource>();
		
		//获取配置文件的名称
		List<String> fileNames = PropertiesUtil.searchPropertiesFiles(propertiesFolder, propertiesNameRegexp);
		
		// 如果搜索不到配置文件，则停止；否则，继续。
		if(ListUtil.isEmptyList(fileNames)) return ;
		
		// 当前识别到有以下配置信息（这里日志需要INFO级别。因为这是使用者必须知道的信息）
		Stdout.fpl("The following database configuration files were detected. There are a total of %s", fileNames.size());
		fileNames.forEach(Stdout::pl);
		
		// 这里的配置路径规则，有点不一样。HikariConfig 它会先判断是否为 File 
		// 如果是 File 则转 FileInputStream。
		// 如果不是，则执行 this.getClass().getResourceAsStream 加载 资源。这里需要以 / 开头。
		
		// 将配置文件信息过滤，然后生成 配置对象，并初始化连接池
		fileNames.stream()
				// 转 HikariConfig 对象（内部有 try 所以外部应该不会抛异常）
				.map(name->new HikariConfig(name.startsWith("/") ? name : "/"+name))
				// 这里需要过滤 异常配置 和 不需要的配置
				.filter(hConfig->{
					// 定义一个结果
					boolean result = false;
					// 首先 连接池名不能以 test 开头。然后，不能缺少配置信息。最后，不能和已经配置的连接池重名。
					if(StrUtil.isEmptyString(hConfig.getPoolName()) || hConfig.getPoolName().startsWith(DBCONFIG_IGNORE_PREFIX)) return result;
					if(StrUtil.isEmptyString(hConfig.getJdbcUrl()) 
						|| StrUtil.isEmptyString(hConfig.getUsername())
						|| StrUtil.isEmptyString(hConfig.getPassword())
						|| StrUtil.isEmptyString(hConfig.getDriverClassName())) return result;
					if(this.dataPools.containsKey(hConfig.getPoolName())) return result;
					// 全部检测通过，赋值 true
					result = true;
					// 返回
					return result;
				})
				// 遍历正常的配置，进行连接池初始化。不一定成功，所以需要捕捉异常。
				.forEach(hConfig->{
					try {
						this.dataPools.put(hConfig.getPoolName(), new HikariDataSource(hConfig));
					} catch(Exception e) {
						Stdout.mylogger.debug(Stdout.fplToAnyWhere("An exception occurred while creating the database connection pool. poolName=%s", hConfig.getPoolName()));
						Stdout.pl(e);
					}
				});
		
		// 清点成功初始化的连接池信息
		Stdout.fpl("The current connection pool situation is as follows. num=%s", this.dataPools.size());
		this.dataPools.entrySet().stream().forEach(Stdout::pl);
		
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
