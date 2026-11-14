/******************************************************************************************************

This file "CondiParamList.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.annotation.MyVirtualEntity;
import net.mickarea.tools.utils.AnnotationUtil;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

/**
 * 一个关于查询条件对象的列表容器（如果不指定数据库类型，默认为 MySQL）
 * <p>有一点要注意，这个容器类不是线程安全的。所以，不要定义为全局对象，然后并发调用。每一次调用，都应该重新 new 出来。</p>
 * @author Michael Pang (Dongcan Pang)
 * @since 2023年5月26日
 */
public class CondiParamList {
	
	/**
	 * 查询条件对象列表。作为一个内部容器，这个对象不对外开放。（不允许有 getter 和 setter 方法，只能由构造函数初始化）
	 */
	private List<CondiParam> params;
	
	/**
	 * 数据库类型。参考 DBSQLInjectionUtil 类 的 DBTYPE 常量
	 */
	private String dbType = DBSQLInjectionUtil.DBTYPE_MYSQL;
	
	/**
	 * 查询条件对应的实体类名，主要用于实体的字段名称翻译。在 CondiParamList 和 CondiParam 中都可以设置 实体类名。
	 * <p>在 CondiParamList 进行 sql 语句处理时，会检查 CondiParam 对象有没有设置 实体类。</p>
	 * <p>如果 CondiParamList 设置了，但是 CondiParam 中没有，则会覆盖 CondiParam 的设置，把 CondiParamList 的实体类写入 CondiParam 中。</p>
	 */
	private Class<?> entityClassName;
	
	/**
	 * 条件语句模板。每一个查询条件替换符以英文":"号为前缀 下面举例：
	 * <p>" :tel and :gender and (:id or :address1 or :address2) "</p>
	 */
	private String sqlTemplate;
	
	// ====================================================== getter and setter 
	
	/**
	 * <p>注意：这个方法已经弃用。因为命名不够规范。请使用 setDbType 方法代替。</p>
	 * <p>重新设置 dbType。这个要根据不同数据库的情况设置。其实，CondiParamList 默认有数据库类型，是 MySQL 的。使用其他数据库时，才重新设置</p>
	 * @param dbType 数据库类型。参考 DBSQLInjectionUtil 类 的 DBTYPE 常量。
	 * @return CondiParamList 对象
	 */
	@Deprecated
	public CondiParamList setCodec(String dbType) {
		// 如果类型信息合规，则设置
		if(DBSQLInjectionUtil.DB_TYPE_LIST.contains(dbType)) this.dbType = dbType;
		// 返回本对象的引用
		return this;
	}
	
	/**
	 * 获取 当前条件列表容器对象的 数据库类型 信息。参考 DBSQLInjectionUtil 类的 DBTYPE 常量
	 * @return dbType 字符串信息。默认是有值，但是可能被 setter 方法修改。
	 */
	public String getDbType() {
		return this.dbType;
	}
	
	/**
	 * 设置 当前条件列表容器对象的 数据库类型 信息。如果 dbType 不符合要求，不会执行 setter 处理。
	 * <p>其实，CondiParamList 默认有数据库类型，是 MySQL 的。</p>
	 * @param dbType 参考 DBSQLInjectionUtil 类的 DBTYPE 常量
	 * @return 当前条件列表容器对象的引用
	 */
	public CondiParamList setDbType(String dbType) {
		// 如果类型信息合规，则设置
		if(DBSQLInjectionUtil.DB_TYPE_LIST.contains(dbType)) this.dbType = dbType;
		// 返回本对象的引用
		return this;
	}
	
	/**
	 * 获取 查询条件对应的实体类名，主要用于实体的字段名称翻译。
	 * @return 查询条件对应的实体类名。
	 */
	public Class<?> getEntityClassName() {
		return this.entityClassName;
	}
	
	/**
	 * 设置 查询条件对应的实体类名，主要用于实体的字段名称翻译。在 CondiParamList 和 CondiParam 中都可以设置 实体类名。
	 * <p>在 CondiParamList 进行 sql 语句处理时，会检查 CondiParam 对象有没有设置 实体类。</p>
	 * <p>如果 CondiParamList 设置了，但是 CondiParam 中没有，则会覆盖 CondiParam 的设置，把 CondiParamList 的实体类写入 CondiParam 中。</p>
	 * @param entityClassName 查询条件对应的实体类名。如果是实体类，则执行设置。否则，不执行。
	 * @return 当前条件列表容器对象的引用
	 */
	public CondiParamList setEntityClassName(Class<?> entityClassName) {
		// 如果是实体类，则执行设置。否则，不执行。
		if(entityClassName!=null 
			&& AnnotationUtil.isAnnotationPresentByOne(entityClassName, Arrays.asList(MyTableOrView.class, MyVirtualEntity.class))) {
			// 设置
			this.entityClassName = entityClassName;
		}
		// 返回本对象的引用
		return this;
	}
	
	/**
	 * 获取 条件语句模板。
	 * @return 条件语句模板字符串。
	 */
	public String getSqlTemplate() {
		return this.sqlTemplate;
	}

	/**
	 * 设置 条件语句模板。这个不是必须设置的。默认的情况下条件会以 and 操作符拼接。但是，如果查询条件有 or判断 或者 其它需要，则需设置条件语句模板。
	 * <p>每一个查询条件替换符以英文":"号为前缀 下面举例：</p>
	 * <p>" :tel and :gender and (:id or :address1 or :address2) "</p>
	 * @param sqlTemplate 条件语句模板字符串。如果不为 null 则设置。
	 */
	public CondiParamList setSqlTemplate(String sqlTemplate) {
		// 如果不为 null 以及 空字符串 则设置。
		if(sqlTemplate!=null && sqlTemplate.trim().length()>0) this.sqlTemplate = sqlTemplate;
		// 返回本对象的引用
		return this;
	}
	
	// ====================================================== 构造函数

	/**
	 * 无参构造函数，用于创建 查询条件对象的列表容器（如果不指定数据库类型，默认为 MySQL）。
	 */
	public CondiParamList() {
		// 调用另外一个构造函数，进行初始化
		this(null, null, null);
	}
	
	/**
	 * 普通构造函数，用于创建 查询条件对象的列表容器（如果不指定数据库类型，默认为 MySQL）。对于这几个参数，都有对应的校验。如果不符合要求，则不会执行 setter 处理
	 * @param entityClassName 查询条件对应的实体类名，主要用于实体的字段名称翻译。
	 */
	public CondiParamList(Class<?> entityClassName) {
		// 调用另外一个构造函数，进行初始化
		this(null, null, entityClassName);
	}
	
	/**
	 * 普通构造函数，用于创建 查询条件对象的列表容器（如果不指定数据库类型，默认为 MySQL）。对于这几个参数，都有对应的校验。如果不符合要求，则不会执行 setter 处理
	 * @param sqlTemplate 条件语句模板。每一个查询条件替换符以英文":"号为前缀 下面举例：
	 * " :tel and :gender and (:id or :address1 or :address2) "
	 */
	public CondiParamList(String sqlTemplate) {
		// 调用另外一个构造函数，进行初始化
		this(null, sqlTemplate, null);
	}
	
	/**
	 * 普通构造函数，用于创建 查询条件对象的列表容器（如果不指定数据库类型，默认为 MySQL）。对于这几个参数，都有对应的校验。如果不符合要求，则不会执行 setter 处理
	 * @param sqlTemplate 条件语句模板。每一个查询条件替换符以英文":"号为前缀 下面举例：
	 * " :tel and :gender and (:id or :address1 or :address2) "
	 * @param entityClassName 查询条件对应的实体类名，主要用于实体的字段名称翻译。
	 */
	public CondiParamList(String sqlTemplate, Class<?> entityClassName) {
		// 调用另外一个构造函数，进行初始化
		this(null, sqlTemplate, entityClassName);
	}
	
	/**
	 * 普通构造函数，用于创建 查询条件对象的列表容器（如果不指定数据库类型，默认为 MySQL）。对于这几个参数，都有对应的校验。如果不符合要求，则不会执行 setter 处理
	 * @param dbType 数据库类型。参考 DBSQLInjectionUtil 类 的 DBTYPE 常量
	 * @param sqlTemplate 条件语句模板。每一个查询条件替换符以英文":"号为前缀 下面举例：
	 * " :tel and :gender and (:id or :address1 or :address2) "
	 */
	public CondiParamList(String dbType, String sqlTemplate) {
		// 调用另外一个构造函数，进行初始化
		this(dbType, sqlTemplate, null);
	}
	
	/**
	 * 普通构造函数，用于创建 查询条件对象的列表容器（如果不指定数据库类型，默认为 MySQL）。对于这几个参数，都有对应的校验。如果不符合要求，则不会执行 setter 处理
	 * @param dbType 数据库类型。参考 DBSQLInjectionUtil 类 的 DBTYPE 常量
	 * @param sqlTemplate 条件语句模板。每一个查询条件替换符以英文":"号为前缀 下面举例：
	 * " :tel and :gender and (:id or :address1 or :address2) "
	 * @param entityClassName 查询条件对应的实体类名，主要用于实体的字段名称翻译。
	 */
	public CondiParamList(String dbType, String sqlTemplate, Class<?> entityClassName) {
		// 初始化 params 容器
		this.params = new ArrayList<CondiParam>();
		// 设置 其它3个值。须符合设置要求，才会执行设置。
		this.setDbType(dbType);
		this.setEntityClassName(entityClassName);
		this.setSqlTemplate(sqlTemplate);
	}
	
	// ====================================================== 对于 List 容器的操作
	
	/**
	 * 添加 查询条件对象 到 列表容器 中。
	 * <p>注意：只有完整的条件对象，才会加入容器。校验的内容有：参数名，条件操作符，参数值。</p>
	 * @param condi 查询条件对象
	 * @return 返回一个当前对象的引用
	 */
	public CondiParamList addParam(CondiParam condi) {
		
		// 首先，校验 CondiParam 本身
		if(condi==null) return this;
		
		// 然后，校验 参数名不能为空。
		if(StrUtil.isEmptyString(condi.getParamName())) return this;
		
		// 然后，校验 条件操作符 是否合规
		if(!CondiParam.OPT_LIST.contains(condi.getOptString())) return this;
		
		// 然后，除了 OPT_IS_NULL 或者 OPT_IS_NOT_NULL 这2个操作 外，都要校验 条件的 value 值 不能为 null。
		if(!condi.getOptString().equals(CondiParam.OPT_IS_NULL)
			&& !condi.getOptString().equals(CondiParam.OPT_IS_NOT_NULL)
			&& condi.getValue()==null) return this;
		
		// 把参数 放入容器（对于查询条件的实体类 entityClassName 的信息，这里不处理，交给 toSqlString 函数处理）
		this.params.add(condi);
		
		// 返回本对象的引用
		return this;
	}
	
	/**
	 * 移除 列表容器中 特定位置的查询条件对象。（注意：这个序号如果超出了 list 的长度，则不执行）
	 * @param i 要移除的查询条件的序号，从0开始。
	 */
	public void removeParam(int i) {
		// 为了避免抛异常，序号需要在长度范围内。
		if(0<=i && i<this.params.size()) this.params.remove(i);
	}
	
	/**
	 * 移除 列表容器中 特定名称的查询条件对象。（注意：这个名称不能为空字符串，然后需要和查询条件对象的参数名一致）
	 * @param paramName 查询条件名。这个名字需要和 CondiParam 对象的 paramName 一致。
	 */
	public void removeParam(String paramName) {
		// 不为空的话，去掉前后空白字符，再比对
		if(!StrUtil.isEmptyString(paramName)) this.params.removeIf(con->paramName.trim().equals(con.getParamName()));
	}
	
	/**
	 * 移除 列表容器中 特定的查询条件对象。（注意：比对有2个方面：对象的引用 和 对象值。如果，对象的引用一致，则直接删除；否则，需要比对对象的值。
	 * <p>条件对象，要比对 条件参数名、条件操作符、条件参数值</p>
	 * @param condi 查询条件对象。这个对象不能为null，否则不执行。
	 */
	public void removeParam(CondiParam condi) {
		// 入参不为 null ，则执行
		if(condi!=null) this.params.removeIf(lCon->{
			
			// ========================= 如果引用一致，则删除
			if(lCon==condi) return true;
			
			// ========================= 否则，进行属性值比对。值相等才删除
			// 获取 3个值 paraName , optString, value 的比对结果
			//           基于对另外一个类的参数处理不信任，还是要考虑 null 值。因为 null 值 直接带出 方法名，会抛 空指针异常。
			boolean paramNameEq = lCon.getParamName()==null ? null==condi.getParamName() : lCon.getParamName().equals(condi.getParamName());
			boolean optEq = lCon.getOptString()==null ? null==condi.getOptString() : lCon.getOptString().equals(condi.getOptString());
			boolean valueEq = lCon.getValue()==null ? null==condi.getValue() : lCon.getValue().equals(condi.getValue());
			// 
			return paramNameEq && optEq && valueEq;
			
		});
	}
	
	/**
	 * 移除 列表容器中 所有条件参数，清空列表
	 */
	public void removeAll() {
		this.params.clear();
	}
	
	/**
	 * 获取 列表容器中 元素的数量
	 * @return 返回查询参数的数量
	 */
	public int getSize() {
		return this.params.size();
	}
	
	// ====================================================== 一些业务上的处理方法
	
	/**
	 * 关于查询条件sql语句模板信息的替换（按照模板信息构造sql语句，替换符一般带有":"前缀）
	 * @param sqlTemplate 条件语句模板。每一个查询条件替换符以英文":"号为前缀 下面举例：
	 * <p>" :tel and :gender and (:id or :address1 or :address2) "</p>
	 * @param params 查询条件对象列表。作为一个内部容器，这个对象不对外开放。
	 */
	private String replaceTempInfo(String sqlTemplate, List<CondiParam> params) {
		
		// 定义一个返回的结果
		String result = "";
		
		// 首先，sqlTemplate 不能为 null，params 也不能为 null
		if(sqlTemplate==null || params==null) return result;
		
		// 如果 sqlTemplate 不为 null，则以它作为预设的结果
		result = sqlTemplate;
		
		// 然后 如果 sqlTemplate 没有模板信息，则原样返回。模板替换符的前缀是 英文":"号
		//      如果 params 为空，也原样返回。
		if(sqlTemplate.indexOf(":")<0 || params.size()<=0) return result;
		
		// 定义提取正则（冒号右侧不能有空白字符）
		String regexp = ":[a-zA-Z0-9\\_]+";
		
		// 然后，提取出需要处理的 替换符信息。（这里不一定有信息，因为即使有 : 前缀，也不一定符合替换符的要求）
		Matcher matcher = Pattern.compile(regexp).matcher(sqlTemplate);
		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
			// 如果匹配到了，顺便替换掉。
			String oldStr = matcher.group();
			// 预想中的 参数名 
			String tempName = oldStr.substring(1);
			// 对应的信息对象（可能搜索不到，也可能搜索到多个，取第一个）
			CondiParam targetCondi = this.params.stream()
												.filter(c->tempName.equals(c.getSqlTemplateParamName()))
												.findFirst()
												.orElse(null);
			// 新的值（从 CondiParam 的 sql 方法，生成一个 语句字符串。）
			String newStr = targetCondi == null ? oldStr : targetCondi.toSqlString(this.dbType);
			// 替换值（要用 quote 方法，保留原始字符）
			matcher.appendReplacement(sb, Matcher.quoteReplacement(newStr));
		}
		// 把最后一次匹配之后的所有原文追加进去
		matcher.appendTail(sb);
		
		// 输出结果
		result = sb.toString();
		
		// 返回结果
		return result;
	}
	
	/**
	 * 根据当前列表容器中 所有条件对象，生成一个完整的查询条件的sql语句。
	 * <p>如果有设置 sql 模板字符串，则一定按模板批量替换。替换时，是按照替换符的顺序，不是按照参数顺序。如果没有则按照条件顺序，一个个 以 ' and ' 字符拼接</p>
	 * @return 一段经过处理的sql语句字符串。
	 */
	public String toSqlString() {
		
		// 首先定义一个 返回结果
		String result = null;
		
		// 这里处理 实体类 翻译的问题（如果条件对象没有设置，则以容器对象的覆盖）
		this.params.forEach(c->{
			if(c.getClassName()==null && this.getEntityClassName()!=null) c.setClassName(this.getEntityClassName());
		});
		
		// 根据 模板信息的设置情况 来处理
		if(!StrUtil.isEmptyString(this.getSqlTemplate())) {
			
			// 如果模板信息不为空，则按照模板信息构造sql语句，替换符一般带有":"前缀
			result = this.replaceTempInfo(this.getSqlTemplate(), this.params);
			
		} else {
			
			//如果sql模板信息为空，则将所有条件按照 and 关系输出
			// 这里以流的方式处理
			result = this.params.stream()
								// 把 CondiParam 对象转为 sql 语句
								.map(c -> c==null ? "" : c.toSqlString(this.getDbType()))
								// 过滤掉 null 和 纯空白的字符串
								.filter(s->s!=null && s.trim().length()>=0)
								// 每个sql条件之间，拼接一个 " and " 字符串
								.collect(Collectors.joining(" and "));
		}
		
		// 如果上面处理后，还是 null 值。则设置为 "" 。因为这是sql 语句，不能返回 null 值。
		if(result==null) result = "";
		
		// 返回结果。
		return result;
		
	}
	
	/**
	 * 输出本对象的内部属性信息
	 */
	@Override
	public String toString() {
		return StrUtil.getJavaBeanFieldsInfo(this);
	}
	
}
