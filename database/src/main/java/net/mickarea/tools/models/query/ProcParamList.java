/******************************************************************************************************

This file "ProcParamList.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;

/**
 * 存储过程 执行的参数 列表容器。对于存储过程的参数，顺序是关键。否则会传送错误参数，或者接收错误参数。
 * <p>有一点要注意，这个容器类不是线程安全的。所以，不要定义为全局对象，然后并发调用。每一次调用，都应该重新 new 出来。</p>
 * @author Michael Pang (Dongcan Pang)
 * @since 2023年6月10日
 */
public class ProcParamList {

	/**
	 * 存储过程 执行的参数 列表容器
	 */
	private List<ProcParam> params;
	
	/**
	 * 无参的构造函数。对于这个对象，它本身就是一个 list 容器，没有附加的属性。
	 */
	public ProcParamList() {
		this.params = new ArrayList<ProcParam>();
	}
	
	/**
	 * 添加 存储过程参数对象。添加顺序必须和存储过程的定义，保持一致。
	 * <p>然后，添加时内部会 校验参数对象是否完整。对于一个存储过程参数，它可以没有名字，但是必须有传输方向 和 参数值。</p>
	 * <p>而且，当参数是输出类型的参数（out, inout），它必须有 对应的 outParamSqlType 信息。</p>
	 * @param p 存储过程参数对象
	 * @return 本对象的引用
	 */
	public ProcParamList addParam(ProcParam p) {
		
		// 参数对象不能为null
		if(p==null) return this;
		
		// 参数对象的传输方向必须合规
		if(!ProcParam.TYPE_LIST.contains(p.getParamType())) return this;
		
		// 对于输出参数，需要校验 outParamSqlType
		if(Arrays.asList(ProcParam.TYPE_OUT, ProcParam.TYPE_INOUT).contains(p.getParamType())) {
			// 看看是否合规
			try {
				JDBCType.valueOf(p.getOutParamSqlType());
			} catch (Exception e) {
				Stdout.mylogger.debug(Stdout.fplToAnyWhere("wrong type : %s, %s", p.getOutParamSqlType(), p.toString()));
				// 返回
				return this;
			}
		}
		
		// 全部通过，则添加到容器中
		this.params.add(p);
		
		// 返回本对象的引用
		return this;
	}
	
	/**
	 * 根据 指定数字顺序，获取 存储过程参数对象
	 * @param i 从 1 开始的数字
	 * @return 参数对象。如果序号超出了容器长度，则返回 null
	 */
	public ProcParam getParam(int i) {
		// 不在范围内，则返回 null，避免报错
		return (1<=i && i<=this.params.size()) ? this.params.get(i-1) : null;
	}
	
	/**
	 * 根据 指定参数名称，获取 存储过程参数对象
	 * @param name 存储过程的参数名
	 * @return 参数对象。如果参数名为空，或者匹配不到参数，则返回 null。
	 */
	public ProcParam getParam(String name) {
		
		// 定义一个返回值
		ProcParam p = null;
		
		// 去掉空白字符再看
		String tmpName = StrUtil.removeAllBlankStrings(name);
		
		// 桉顺序遍历搜索，同名参数
		if(!StrUtil.isEmptyString(tmpName)) p = this.params.stream()
														// 先过滤出同名参数（lp 中的参数名不一定有值，所以要反过来 equals）
														.filter(lp->tmpName.equals(lp.getParamName()))
														// 如果有多个，则返回第一个
														.findFirst()
														// 如果没有，则返回 null
														.orElse(null);
		
		// 返回结果
		return p;
	}
	
	/**
	 * 获取 当前存储过程参数容器的 参数数量
	 * @return 返回 当前存储过程参数容器的 参数数量
	 */
	public int getParamLength() {
		return this.params.size();
	}
	
	/**
	 * 删除 所有条件参数，清空当前存储过程参数容器
	 */
	public void removeAll() {
		this.params.clear();
	}
	
	/**
	 * 打印 当前对象的私有属性信息
	 */
	@Override
	public String toString() {
		return StrUtil.getJavaBeanFieldsInfo(this);
	}
	
}
