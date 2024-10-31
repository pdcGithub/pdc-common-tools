/******************************************************************************************************

This file "DBStrUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.database;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.regex.Pattern;

import net.mickarea.tools.annotation.MyColumn;
import net.mickarea.tools.annotation.MyColumnIgnore;
import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.annotation.MyVirtualEntity;
import net.mickarea.tools.annotation.scanner.MyAnnotationScanner;
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.TestCol2;
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.TestCol3;
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.TestCol4;
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.TestInsert;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * &gt;&gt;&nbsp;<p>一个与数据库相关的字符串工具类</p>
 * <p>对于这个工具类，目前只能处理一般的命名方式比如字母、数字这些。如果有些特殊的命名，比如名字以下划线开头或者其它特殊字符，这个类是处理不了的</p>
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年5月22日-2024年10月25日
 */
public final class DBStrUtil {
	
	/**
	 * 执行异常的一个返回结果
	 */
	public static final String EXEC_ERROR = "exec_error";

	/**
	 * 私有构造函数
	 */
	private DBStrUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入的表名或者视图名，生成一个符合驼峰写法的类名
	 * @param tabOrViewName 数据库中的表名或者视图名
	 * @return 一个java类名（驼峰写法）
	 */
	public static String genNameFromTableOrView(String tabOrViewName) {
		String result = "";
		//传入的表名和视图名，不能为空；不能带有"点"符号
		if(!StrUtil.isEmptyString(tabOrViewName) && !Pattern.matches(".*[\\.].*", tabOrViewName)) {
			String input = tabOrViewName.toLowerCase().replaceAll("[\\s]+", "");
			result = StrUtil.makeHumpString(input, "_");
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入的表名或者视图名，以及包名，获取对应的数据库映射类名
	 * @param tabOrViewName 数据库表名 或者 数据库视图名
	 * @param pkgName 存放数据库映射类的包路径
	 * @return 数据库映射类的类名
	 */
	public static String genNameFromTableOrView(String tabOrViewName, String pkgName) {
		String result = "";
		if(!StrUtil.isEmptyString(tabOrViewName) && !StrUtil.isEmptyString(pkgName)) {
			//使用扫描器获取特定包下的映射类信息
			Set<Class<?>> clsSet = MyAnnotationScanner.getClassesByPackageNameAndAnnotaionName(pkgName, MyTableOrView.class);
			//如果有内容就继续解析
			if(clsSet!=null && clsSet.size()>0) {
				for(Class<?> cls: clsSet) {
					//如果表名或者视图名匹配，则返回
					String tmpTab = cls.getAnnotation(MyTableOrView.class).name();
					if(tabOrViewName.toLowerCase().equals(tmpTab)) {
						result = cls.getTypeName();
						break;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入的字段名，生成一个符合驼峰写法的属性名
	 * @param columnName 字段名
	 * @return 属性名
	 */
	public static String genNameFromColumn(String columnName) {
		String result = "";
		//传入的字段名，不能为空；不能带有"点"符号
		if(!StrUtil.isEmptyString(columnName) && !Pattern.matches(".*[\\.].*", columnName)) {
			String input = columnName.toLowerCase().replaceAll("[\\s]+", "");
			result = StrUtil.makeFirstCharLowerCase(StrUtil.makeHumpString(input, "_"));
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入的字段名，数据库映射类，找到映射的属性名
	 * @param columnName 字段名
	 * @param clsName 数据库映射类
	 * @return 属性名
	 */
	public static String genNameFromColumn(String columnName, Class<?> clsName) {
		String result = "";
		//传入的字段名不能为空，而且映射类不为null，映射类必须要有注解
		if(!StrUtil.isEmptyString(columnName) && clsName!=null
				                              && (clsName.isAnnotationPresent(MyTableOrView.class)
				                            		   ||clsName.isAnnotationPresent(MyVirtualEntity.class)
				                                 )) {
			Field[] fields = clsName.getDeclaredFields();
			if(fields!=null) {
				for(Field f: fields) {
					//首先属性必须被MyColumn注解，然后不能被MyColumnIgnore注解，然后注解的字段名与入参相同
					if(f.isAnnotationPresent(MyColumn.class) && !f.isAnnotationPresent(MyColumnIgnore.class)
							                                 && columnName.toLowerCase().equals(f.getAnnotation(MyColumn.class).name())) {
						result = f.getName();
						break; //找到对应属性，跳出
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入的字段名，生成一个符合驼峰写法的get方法名
	 * @param columnName 字段名
	 * @return get方法名
	 */
	public static String genGetterNameFromColumn(String columnName) {
		String result = "";
		//传入的字段名，不能为空；不能带有"点"符号
		if(!StrUtil.isEmptyString(columnName) && !Pattern.matches(".*[\\.].*", columnName)) {
			String input = columnName.toLowerCase().replaceAll("[\\s]+", "");
			result = "get"+StrUtil.makeHumpString(input, "_");
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入的字段名，获取对应数据库映射类中 该字段属性的 get方法名
	 * @param columnName 字段名
	 * @param clsName 映射类
	 * @return 对应属性的 get 方法名
	 */
	public static String genGetterNameFromColumn(String columnName, Class<?> clsName) {
		String result = "";
		if(!StrUtil.isEmptyString(columnName) && clsName!=null) {
			//先获取该字段对应的属性名
			String propertyName = genNameFromColumn(columnName, clsName);
			if(!StrUtil.isEmptyString(propertyName)) {
				//拼接get前缀
				result = "get"+StrUtil.makeFirstCharUpperCase(propertyName);
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入的字段名，生成一个符合驼峰写法的set方法名
	 * @param columnName 字段名
	 * @return set方法名
	 */
	public static String genSetterNameFromColumn(String columnName) {
		String result = "";
		//传入的字段名，不能为空；不能带有"点"符号
		if(!StrUtil.isEmptyString(columnName) && !Pattern.matches(".*[\\.].*", columnName)) {
			String input = columnName.toLowerCase().replaceAll("[\\s]+", "");
			result = "set"+StrUtil.makeHumpString(input, "_");
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入的字段名，获取对应数据库映射类中 该字段属性的 set 方法名
	 * @param columnName 字段名
	 * @param clsName 数据库映射类
	 * @return 对应属性的 set 方法名
	 */
	public static String genSetterNameFromColumn(String columnName, Class<?> clsName) {
		String result = "";
		if(!StrUtil.isEmptyString(columnName) && clsName!=null) {
			//先获取该字段对应的属性名
			String propertyName = genNameFromColumn(columnName, clsName);
			if(!StrUtil.isEmptyString(propertyName)) {
				//拼接get前缀
				result = "set"+StrUtil.makeFirstCharUpperCase(propertyName);
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入数据库映射的对象类，获取它对应的表名或者视图名。（需要在对象类添加注解MyTableOrView，才能获取到信息）
	 * @param cls 数据库映射对象类
	 * @return 表名或者视图名
	 */
	public static String parseClassNameToTableOrViewName(Class<?> cls) {
		String result = "";
		if(cls.isAnnotationPresent(MyTableOrView.class)) {
			result = cls.getAnnotation(MyTableOrView.class).name();
		}
		return result ;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入数据库映射的对象类名称，获取它对应的表名或者视图名。（需要在对象类添加注解MyTableOrView，才能获取到信息）
	 * @param clsName 数据库映射对象类 的名称，比如：net.test.pool.TestA 这样
	 * @return 表名或者视图名
	 */
	public static String parseClassNameToTableOrViewName(String clsName) {
		String result = "";
		if(!StrUtil.isEmptyString(clsName)) {
			try {
				Class<?> cls = Class.forName(clsName);
				result = parseClassNameToTableOrViewName(cls);
			} catch (Exception e) {
				Stdout.pl(e);
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入数据库映射的对象类 和 属性名称，获取它对应的数据库字段名。（需要在对象类添加注解 MyColumn，才能获取到信息）
	 * @param cls 数据库映射的对象类
	 * @param propertyName 属性名称
	 * @return 数据库字段名
	 */
	public static String parsePropertyNameToColumnName(Class<?> cls, String propertyName) {
		String result = "";
		if(!StrUtil.isEmptyString(propertyName)) {
			try {
				Field f = cls.getDeclaredField(propertyName);
				//如果被注解MyColumn修饰，而且没有被MyColumnIgnore注解修饰，即可提取字段名
				if(f.isAnnotationPresent(MyColumn.class) && !f.isAnnotationPresent(MyColumnIgnore.class)) {
					result = f.getAnnotation(MyColumn.class).name();
				}
			} catch (Exception e) {
				Stdout.pl(e);
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入数据库映射的对象类名称 和 属性名称，获取它对应的数据库字段名。（需要在对象类添加注解 MyColumn，才能获取到信息）
	 * @param clsName 数据库映射的对象类名称
	 * @param propertyName 属性名称
	 * @return 数据库字段名
	 */
	public static String parsePropertyNameToColumnName(String clsName, String propertyName) {
		String result = "";
		if(!StrUtil.isEmptyString(clsName) && !StrUtil.isEmptyString(propertyName)) {
			try {
				Class<?> cls = Class.forName(clsName);
				result = parsePropertyNameToColumnName(cls, propertyName);
			} catch (Exception e) {
				Stdout.pl(e);
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据 数据库映射类的 getter方法名，获取它的数据库字段名
	 * @param cls 数据库映射类
	 * @param getterName getter方法名称
	 * @return  数据库字段名称
	 */
	public static String parseGetterNameToColumnName(Class<?> cls, String getterName) {
		String result = "";
		if(!StrUtil.isEmptyString(getterName)) {
			String propertyName = StrUtil.makeFirstCharLowerCase(getterName.substring(3));
			result = parsePropertyNameToColumnName(cls, propertyName);
		}
		return result ;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据 数据库映射类的 getter方法名，获取它的数据库字段名
	 * @param clsName 数据库映射的对象类名称
	 * @param getterName getter方法名称
	 * @return 
	 */
	public static String parseGetterNameToColumnName(String clsName, String getterName) {
		String result = "";
		if(!StrUtil.isEmptyString(clsName) && !StrUtil.isEmptyString(getterName)) {
			String propertyName = StrUtil.makeFirstCharLowerCase(getterName.substring(3));
			result = parsePropertyNameToColumnName(clsName, propertyName);
		}
		return result ;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据 数据库映射类的 setter方法名，获取它的数据库字段名
	 * @param cls 数据库映射类
	 * @param setterName setter方法名称
	 * @return  数据库字段名称
	 */
	public static String parseSetterNameToColumnName(Class<?> cls, String setterName) {
		return parseGetterNameToColumnName(cls, setterName);
	}
	
	/**
	 * &gt;&gt;&nbsp;根据 数据库映射类的 setter方法名，获取它的数据库字段名
	 * @param clsName 数据库映射的对象类名称
	 * @param setterName setter方法名称
	 * @return  数据库字段名称
	 */
	public static String parseSetterNameToColumnName(String clsName, String setterName) {
		return parseGetterNameToColumnName(clsName, setterName);
	}
	
	/**
	 * 根据 数据库表 映射的 Java Bean 类，获取第一个有效列的列名
	 * @param cls 数据库映射对象类
	 * @return 该类映射表对应的第一个字段名；如果出问题，会返回 一个 'exec_error' 字符串
	 */
	public static String getFirstColumnFromBeanClass(Class<?> cls) {
		
		//定义默认结果
		String result = EXEC_ERROR;
		
		//首先验证参数，是否为 映射类（不是映射类，则直接返回）
		if(cls==null || !cls.isAnnotationPresent(MyTableOrView.class)) {
			Stdout.pl("传入的类不是数据库映射类，cls 为 "+(cls==null?null:cls.getName()));
			return result;
		}
		
		//获取类内的属性信息，判断 类内是否定义了 属性
		Field[] fields = cls.getDeclaredFields();
		if(fields==null || fields.length<=0) {
			Stdout.pl("该数据库映射类（"+cls.getName()+"），没有定义可用属性。");
			return result;
		}
		
		//循环便利，获取类内 第一个 有 列注解的 属性 上面的 列名。
		for(Field f: fields) {
			if(f.isAnnotationPresent(MyColumn.class) && !f.isAnnotationPresent(MyColumnIgnore.class)) {
				result = f.getAnnotation(MyColumn.class).name();
				break;
			}
		}
		
		//如果传入的是 实体映射类，但是 它没有对属性进行 列映射。则需要提示
		if(EXEC_ERROR.equalsIgnoreCase(result)) {
			Stdout.pl("该数据库映射类（"+cls.getName()+"），内部没有一个属性对数据库字段进行映射。");
			return result;
		}
		
		//如果对应映射的列信息为空，则返回报错信息。
		if(StrUtil.isEmptyString(result)) {
			Stdout.pl("该数据库映射类（"+cls.getName()+"），第一个映射属性没有填写 映射的列名。");
			return EXEC_ERROR;
		}
		
		//返回
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;测试
	 */
	public static void main(String[] args) {
		// 测试非实体映射类
		Stdout.pl(DBStrUtil.getFirstColumnFromBeanClass(Object.class));
		Stdout.pl("---------------------------------------------------");
		// 测试实体映射类
		Stdout.pl(DBStrUtil.getFirstColumnFromBeanClass(TestInsert.class));
		Stdout.pl("---------------------------------------------------");
		// 测试实体，不设置映射字段
		Stdout.pl(DBStrUtil.getFirstColumnFromBeanClass(TestCol2.class));
		Stdout.pl("---------------------------------------------------");
		// 测试实体，映射字段设置为空
		Stdout.pl(DBStrUtil.getFirstColumnFromBeanClass(TestCol3.class));
		Stdout.pl("---------------------------------------------------");
		// 测试实体，没有属性
		Stdout.pl(DBStrUtil.getFirstColumnFromBeanClass(TestCol4.class));
		Stdout.pl("---------------------------------------------------");
	}
	
}
