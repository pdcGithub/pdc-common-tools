/******************************************************************************************************

This file "DBValidUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.database;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.mickarea.tools.annotation.MyIdGroup;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.annotation.MyVirtualEntity;
import net.mickarea.tools.models.SimpleDBData;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * &gt;&gt;&nbsp;数据库操作中的参数校验类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月14日-2023年10月21日
 */
public final class DBValidUtil {

	/**
	 * &gt;&gt;&nbsp;一个DDL的关键字数组 create、alter、drop、truncate、comment、grant、revoke
	 */
	private final static String[] ddlKeyWords = new String[]{"create", "alter", "drop", "truncate", "comment", "grant", "revoke"};
	
	/**
	 * &gt;&gt;&nbsp;一个DML的关键字数组 select、update、delete、insert、merge
	 */
	private final static String[] dmlKeyWords = new String[]{"select", "update", "delete", "insert", "merge"};
	
	/**
	 * &gt;&gt;&nbsp;构造函数，私有
	 */
	private DBValidUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * &gt;&gt;&nbsp;校验将要执行的Sql语句是否符合执行要求
	 * @param preSql 将要执行的Sql语句，参数的位置以 '?' 符号代替。<p>比如 <code>select * from test_a where id=? </code></p>
	 * @param params 对应替换符的sql语句参数值列表。参数值与替换符必须一一对应
	 * @param sqlType sql语句类型，参考：DBStaticUtil 类中的 SQL_TYPE_INSERT、SQL_TYPE_SELECT、SQL_TYPE_DELETE、SQL_TYPE_UPDATE
	 * @return 如果校验不通过会返回内容；如果校验通过返回空字符串。
	 */
	public static String validateParams(String preSql, List<?> params, String sqlType) {
		
		String sb = "";
		if(StrUtil.isEmptyString(preSql) || preSql.indexOf("?")<=-1) {
			sb = "参数校验不通过，sql语句为空，或者语句没有预处理参数 '?' ";
			return sb;
		}
		if(!validateSQL(preSql, sqlType)) {
			sb = "参数校验不通过，sql语句包含了非法sql操作（不能使用 drop, alter等等DDL操作语句；不能在本方法使用其他DML语句）";
			return sb;
		}
		if(ListUtil.isEmptyList(params)){
			sb = "参数校验不通过，参数列表为空";
		} else {
			if(DBStaticUtil.SQL_TYPE_INSERT.equalsIgnoreCase(sqlType)) { //insert 语句进行二维校验
				Object o = params.get(0);
				if(o instanceof List) {
					if(ListUtil.isEmptyList((List<?>)o) || StrUtil.countString(preSql, "[\\?]")!=((List<?>)o).size()) {
						sb = "参数校验不通过，参数为空，或者 参数的数量与 预处理符 '?' 数量不匹配";
					}
				}else {
					sb = "insert 语句的参数有误，insert 语句需要二维参数。比如 List<List<Object>> 这种类型";
				}
			}else { //其他做一维校验
				if(StrUtil.countString(preSql, "[\\?]")!=params.size()) {
					sb = "参数校验不通过，参数的数量与 预处理符 '?' 数量不匹配";
				}
			}
		}
		return sb;
	}
	
	/**
	 * &gt;&gt;&nbsp;校验实体对象是否合规。虚拟实体类必须有 MyVirtualEntity注解 或者 MyTableOrView注解
	 * @param virtualEntityClass 虚拟的实体对象类
	 * @return 如果校验不通过会返回内容；如果校验通过返回空字符串。
	 */
	public static String validateParams(Class<?> virtualEntityClass) {
		String sb = "";
		if(virtualEntityClass==null) {
			sb = "参数校验不通过，实体对象类 EntityClass 不能为 null";
			return sb;
		}
		if(!virtualEntityClass.isAnnotationPresent(MyVirtualEntity.class) && !virtualEntityClass.isAnnotationPresent(MyTableOrView.class)) {
			sb = "参数校验不通过，实体对象类 EntityClass 参数必须有 @MyVirtualEntity注解 或者 @MyTableOrView注解";
			return sb;
		}
		return sb;
	}
	
	/**
	 * &gt;&gt;&nbsp;校验存储过程或者函数的名称是否为空
	 * @param funcOrProcName 数据库存储过程 或者 函数名称
	 * @param type 类型：proc 、 func ，参考：DBStaticUtil 中 OBJ_TYPE_FUNC 和 OBJ_TYPE_PROC
	 * @return 如果校验不通过，返回错误信息；否则，返回空
	 */
	public static String validateParams(String funcOrProcName, String type) {
		String sb = "";
		if(StrUtil.isEmptyString(funcOrProcName)) {
			String pre = "";
			if(DBStaticUtil.OBJ_TYPE_FUNC.equalsIgnoreCase(type)) {
				pre = "函数";
			}else if(DBStaticUtil.OBJ_TYPE_PROC.equalsIgnoreCase(type)) {
				pre = "存储过程";
			}else {
				pre = "不明对象";
			}
			sb = "调用"+pre+"错误，"+pre+"名不能为空。";
		}
		return sb;
	}
	
	/**
	 * &gt;&gt;&nbsp;校验连接对象是否有效
	 * @param conn 数据库连接对象
	 * @return 如果校验不通过会返回内容；如果校验通过返回空字符串。
	 */
	public static String validateParams(Connection conn) {
		String sb = "";
		if(conn==null) {
			sb = "参数校验不通过，数据库连接对象为空，请检查";
		}
		return sb;
	}
	
	/**
	 * &gt;&gt;&nbsp;校验 SimpleDBData 对象是否为空
	 * @param sdb SimpleDBData 对象
	 * @return 如果校验不通过会返回内容；如果校验通过返回空字符串。
	 */
	public static String validateParams(SimpleDBData sdb) {
		return sdb==null?"SimpleDBData 对象不能为空":"";
	}
	
	/**
	 * &gt;&gt;&nbsp;校验实体对象列表是否有效
	 * @param entityList 实体对象列表
	 * @return 如果校验不通过会返回内容；如果校验通过返回空字符串。
	 */
	public static String validateEntityList(List<?> entityList) {
		String sb = "";
		//列表简单检查
		if(ListUtil.isEmptyList(entityList)) {
			sb = "参数校验不通过，实体列表为空";
			return sb;
		}
		//对列表的值一个个校验
		Object firstObject = entityList.get(0);
		for(int i=0;i<entityList.size();i++) {
			Object o = entityList.get(i);
			if(!o.getClass().isAnnotationPresent(MyTableOrView.class)){
				sb = "实体列表中(第"+(i+1)+"个对象)，存在对象的类型不是实体类型，没有 @MyTableOrView 注解修饰";
				break;
			}
			if(StrUtil.isEmptyString(o.getClass().getDeclaredAnnotation(MyTableOrView.class).name())) {
				sb = "实体列表中(第"+(i+1)+"个对象)，存在实体映射类注解 @MyTableOrView 中name值为空，请检查。";
				break;
			}
			if(firstObject.getClass()!=o.getClass()) {
				sb = "实体列表中，并非所有的对象类型都一致。请检查(第"+(i+1)+"个对象)";
				break;
			}
			Field[] fields = o.getClass().getDeclaredFields();
			if(fields!=null && fields.length>0) {
				//这里将开始校验对象是否有设置唯一字段组
				int idCount = 0;
				for(Field f: fields) {
					if(f.isAnnotationPresent(MyIdGroup.class)) {
						idCount += 1;
					}
				}
				if(idCount==0) {
					sb = "当前实体映射类没有唯一字段组信息，请检查 @MyIdGroup 注解是否正确设置";
					break;
				}
				//这里将开始校验唯一字段组的值是否为空（因为 insert、update、delete 都依赖唯一字段组）
				for(Field f: fields) {
					if(f.isAnnotationPresent(MyIdGroup.class)) {
						try {
							String getterName = "get"+StrUtil.makeFirstCharUpperCase(f.getName());
							//Stdout.pl(getterName);
							Method m = o.getClass().getMethod(getterName);
							//Stdout.pl(m);
							Object fValue= m.invoke(o);
							//Stdout.pl("fValue:"+fValue);
							if(fValue instanceof String) {
								sb = StrUtil.isEmptyString((String)fValue)?"第"+(i+1)+"个对象，唯一字段组的属性("+f.getName()+")值为空，请检查":"";
								if(!StrUtil.isEmptyString(sb)) {
									break;
								}
							}else {
								sb = fValue==null?"第"+(i+1)+"个对象，唯一字段组的属性("+f.getName()+")值为null，请检查":"";
								if(!StrUtil.isEmptyString(sb)) {
									break;
								}
							}
						} catch (Exception e) {
							sb = "第"+(i+1)+"个对象，获取唯一字段组的值出错，请检查代码("+e.getMessage()+")";
							Stdout.pl(e);
							break;
						}
					}
				}
				//由于这里是双重循环，所以对于 第二重循环的代码，需要在这里再 break 一次才能达到要求。
				if(!StrUtil.isEmptyString(sb)) {
					break;
				}
			}else {
				sb = "当前实体映射类没有可用属性，请检查";
				break;
			}
		}
		return sb;
	}
	
	/**
	 * &gt;&gt;&nbsp;检查sql语句是否包含DDL关键字 以及违规的 DML关键字。
	 * <p>因为正常操作是不允许使用 create、drop 等DDL语句；以及在执行DML时，不允许混用其他DML语句。</p>
	 * @param preSql 一个待执行的sql语句
	 * @param type SQL语句类型，比如 select 、 insert 、 delete 、 update 四大类。参考：DBOperator 的 SQL_TYPE 常量
	 * @return 如果包含违规DML关键字 或者 包含 DDL 关键字，则false；否则校验通过，返回true
	 */
	public static boolean validateSQL(String preSql, String type) {
		return validateSQLDDL(preSql) && validateSQLDML(preSql, type);
	}
	
	/**
	 * &gt;&gt;&nbsp;检查sql语句是否包含DML关键字。比如包含(select、update、delete、insert、merge)这些，原则上是不允许包含的。
	 * <p>如果是select语句，则不能包含 update、delete、insert、merge 这些。如此类推</p>
	 * @param preSql 一个待执行的sql语句
	 * @param type SQL语句类型，比如 select 、 insert 、 delete 、 update 四大类。参考：DBOperator 的 SQL_TYPE 常量
	 * @return 如果包含违规DML关键字，则false；否则校验通过，返回true
	 */
	public static boolean validateSQLDML(String preSql, String type) {
		boolean result = true;
		//不为空才处理
		if(!StrUtil.isEmptyString(preSql) && !StrUtil.isEmptyString(type)) {
			String tmpSql = preSql.toLowerCase();
			List<String> tmpKeywords = new ArrayList<String>();
			//遍历出要过滤的关键字；
			for(int i=0;i<dmlKeyWords.length;i++) {
				if(type.equalsIgnoreCase(dmlKeyWords[i])) {
					continue;
				}else {
					tmpKeywords.add(dmlKeyWords[i]);
				}
			}
			//然后关键字过滤
			if(!ListUtil.isEmptyList(tmpKeywords)) {
				for(String s: tmpKeywords) {
					if(Pattern.matches("(.*[\\s;])?"+s+"\\s.*", tmpSql)) {
						result = false;
						break;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;检查sql语句是否包含DDL关键字。比如包含(create、alter、drop、truncate、comment、grant、revoke)这些，原则上是不允许包含的。
	 * @param preSql 一个待执行的sql语句
	 * @return 如果有包含 DDL 关键字，则为false ； 否则校验通过，返回true
	 */
	public static boolean validateSQLDDL(String preSql) {
		boolean result = true;
		//不为空才处理
		if(!StrUtil.isEmptyString(preSql)) {
			String tmpSql = preSql.toLowerCase();
			for(String s: ddlKeyWords) {
				if(Pattern.matches("(.*[\\s;])?"+s+"\\s.*", tmpSql)) {
					result = false;
					break;
				}
			}
		}
		return result;
	}
	
}
