/******************************************************************************************************

This file "OrderParamList.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.mickarea.tools.annotation.MyTableOrView;
import net.mickarea.tools.annotation.MyVirtualEntity;
import net.mickarea.tools.utils.AnnotationUtil;
import net.mickarea.tools.utils.StrUtil;

/**
 * 排序对象列表容器，用于构造完整的排序字段sql语句
 * <p>有一点要注意，这个容器类不是线程安全的。所以，不要定义为全局对象，然后并发调用。每一次调用，都应该重新 new 出来。</p>
 * @author Michael Pang (Dongcan Pang)
 * @since 2023年6月2日
 */
public class OrderParamList {

	/**
	 * 排序对象列表。作为一个内部容器，这个对象不对外开放。（不允许有 getter 和 setter 方法，只能由构造函数初始化）
	 */
	private List<OrderParam> params;
	
	/**
	 * 排序字段对应的实体类名，主要用于实体的字段名称翻译。在 OrderParamList 和 OrderParam 中都可以设置 实体类名。
	 * <p>在 OrderParamList 进行 sql 语句处理时，会检查 OrderParam 对象有没有设置 实体类。</p>
	 * <p>如果 OrderParamList 设置了，但是 OrderParam 中没有，则会覆盖 OrderParam 的设置，把 OrderParamList 的实体类写入 OrderParam 中。</p>
	 */
	private Class<?> entityClassName;
	
	/**
	 * 获取 排序字段对应的实体类名，主要用于实体的字段名称翻译。
	 * @return 排序字段对应的实体类名。
	 */
	public Class<?> getEntityClassName() {
		return this.entityClassName;
	}
	
	/**
	 * 设置 排序字段对应的实体类名，主要用于实体的字段名称翻译。
	 * @param entityClassName 排序字段对应的实体类名。注意，它需要带有 实体注解 才会设置进去。
	 */
	public OrderParamList setEntityClassName(Class<?> entityClassName) {
		// 对于实体类，需要校验 实体注解
		if(entityClassName!=null && 
			AnnotationUtil.isAnnotationPresentByOne(
				entityClassName, Arrays.asList(MyTableOrView.class, MyVirtualEntity.class))) this.entityClassName = entityClassName;
		// 返回一个本对象的引用
		return this;
	}
	
	/**
	 * 这里是无参的构造函数。属性交由 setter 和 getter 方法处理。
	 */
	public OrderParamList() {
		// 构造函数初始化，统一由一个函数处理。
		this(null);
	}
	
	/**
	 * 这里是有参构造函数。主要通过 设置实体类信息，来初始化本对象
	 * @param entityClassName 排序字段对应的实体类名。注意，它需要带有 实体注解 才会设置进去。
	 */
	public OrderParamList(Class<?> entityClassName) {
		// 首先 初始化 容器list
		this.params = new ArrayList<OrderParam>();
		// 然后 设置实体类信息。
		this.setEntityClassName(entityClassName);
	}
	
	/**
	 * 添加 排序字段信息 到 排序对象列表容器中。
	 * @param order 排序字段对象（只有当对象不为null，并且排序对象信息完整：排序字段信息、排序操作符，才放入容器）
	 * @return 返回 一个本对象的引用
	 */
	public OrderParamList addParam(OrderParam order) {
		
		// 如果为null 则不处理
		if(order==null) return this;
		
		// 如果信息不完整，则不处理
		if(StrUtil.isEmptyString(order.getParamName())
			|| StrUtil.isEmptyString(order.getOrderOpt())) return this;
		
		// 再检测，看看 排序操作符 是否符合要求。不在指定的范围内，不处理
		if(!OrderParam.OPT_LIST.contains(order.getOrderOpt())) return this;
		
		// 如果通过，则放入容器
		this.params.add(order);
		
		// 返回对象的引用
		return this;
	}
	
	/**
	 * 删除一个排序字段信息
	 * @param i 要参数序号。序号必须在 0 到 列表容器长度 范围内
	 */
	public void removeParam(int i) {
		// 如果参数正常，则删除
		if(0<=i && i<this.params.size()) this.params.remove(i);
	}
	
	/**
	 * 删除一个排序字段信息（参数名称不能为空字符串 。然后再遍历删除）
	 * @param paramName 排序字段对象的参数名称
	 */
	public void removeParam(String paramName) {
		// 首先，这个参数名称不能为空字符串 。然后再遍历删除
		if(!StrUtil.isEmptyString(paramName)) this.params.removeIf(order->paramName.trim().equals(order.getParamName()));
	}
	
	/**
	 * 删除一个排序字段信息（首先，比较对象的引用。如果引用不同，则比对 排序字段信息 和 排序操作符）
	 * @param order 排序字段对象
	 */
	public void removeParam(OrderParam order) {
		
		// 这里 按条件判断是否删除。引用相同 或者 排序字段信息+操作符符 相同。
		this.params.removeIf(listOrder->{
			
			// 比对 对象的引用
			if(order==listOrder) return true;
			
			// 对象引用不一致，则比对 参数名 和 排序操作符
			boolean nameEq = listOrder.getParamName() == null ? null==order.getParamName() : listOrder.getParamName().equals(order.getParamName());
			boolean optEq = listOrder.getOrderOpt() == null  ? null==order.getOrderOpt() : listOrder.getOrderOpt().equals(order.getOrderOpt());
			
			// 一致才删除
			return nameEq && optEq;
		});
		
	}
	
	/**
	 * 删除所有排序字段信息，清空列表容器
	 */
	public void removeAll() {
		this.params.clear();
	}
	
	/**
	 * 获取 排序字段列表容器中 元素的数量
	 * @return 返回排序字段的数量
	 */
	public int getSize() {
		return this.params.size();
	}
	
	/**
	 * 生成排序字段sql字符串，比如： " a.id asc, b.obj_id desc " 这样。
	 * <p>在 sql 字符串生成时，要注意 entityClassName 的处理。因为，有些排序字段可能需要翻译，但是没有设置。可以由容器的对象进行覆盖</p>
	 * @return 排序字段sql字符串。可能空，也可能有值。
	 */
	public String toSqlString() {
		
		// 定义一个返回结果
		String result = null;
		
		// 这里处理 实体类 翻译的问题（如果条件对象没有设置，则以容器对象的覆盖）
		this.params.forEach(listOrder->{
			if(listOrder!=null 
				&& listOrder.getClassName()==null 
				&& this.getEntityClassName()!=null) listOrder.setClassName(this.getEntityClassName());
		});
		
		// 因为 param 不一定有内容，所以整体的值，可能为空
		result = this.params.stream()
							// 把 OrderParam 对象，转为 字符串。
							.map(listOrder-> listOrder==null ? "" : listOrder.toSqlString())
							// 过滤不符合要求的内容
							.filter(s->!StrUtil.isEmptyString(s))
							// 拼接 分隔符
							.collect(Collectors.joining(", "));
		
		// 如果 null 则设置空字符串。
		if(result==null) result = "";
		
		// 返回
		return result;
	}
	
	/**
	 * 这是一个私有属性打印方法。主要输出私有属性信息，方便测试时查看对象是否异常。
	 */
	@Override
	public String toString() {
		return StrUtil.getJavaBeanFieldsInfo(this);
	}
	
}
