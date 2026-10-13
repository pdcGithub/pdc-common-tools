/******************************************************************************************************

This file "ProcParamList.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import java.util.ArrayList;
import java.util.List;

import net.mickarea.tools.utils.StrUtil;

/**
 * &gt;&gt;&nbsp;存储过程执行的参数列表
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年6月10日
 */
public class ProcParamList {

	private final List<ProcParam> params;
	
	/**
	 * &gt;&gt;&nbsp;构造函数
	 */
	public ProcParamList() {
		this.params = new ArrayList<ProcParam>();
	}
	
	/**
	 * &gt;&gt;&nbsp;添加存储过程参数对象
	 * @param p 存储过程参数对象
	 * @return 参数列表对象
	 */
	public ProcParamList addParam(ProcParam p) {
		this.params.add(p);
		return this;
	}
	
	/**
	 * &gt;&gt;&nbsp;获取指定数字的参数对象
	 * @param i 从 1 开始的数字
	 * @return 参数对象
	 */
	public ProcParam getParam(int i) {
		return this.params.get(i-1);
	}
	
	/**
	 * &gt;&gt;&nbsp;根据参数名，获取指定的参数对象
	 * @param name 存储过程的参数名
	 * @return 参数对象
	 */
	public ProcParam getParam(String name) {
		ProcParam p = null;
		if(!StrUtil.isEmptyString(name)) {
			for(ProcParam c: this.params) {
				if(name.equals(c.getParamName())) {
					p = c;
					break;
				}
			}
		}
		return p;
	}
	
	/**
	 * &gt;&gt;&nbsp;获取参数数量
	 * @return 返回当前列表的参数数量
	 */
	public int getParamLength() {
		return this.params.size();
	}
	
	/**
	 * &gt;&gt;&nbsp;删除所有条件参数，清空列表
	 */
	public void removeAll() {
		this.params.clear();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(ProcParam p : this.params) {
			sb.append(p.toString()+"\n");
		}
		return sb.toString();
	}
	
}
