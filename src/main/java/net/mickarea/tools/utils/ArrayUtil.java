/******************************************************************************************************

This file "ArrayUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * &gt;&gt;&nbsp;一个数组工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月26日-2023年10月21日
 */
public final class ArrayUtil {
	
	/**
	 * &gt;&gt;&nbsp;私有构造函数
	 */
	private ArrayUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * &gt;&gt;&nbsp;将两个byte数组合并为一个
     * @param data1  要合并的数组1
     * @param data2  要合并的数组2
     * @return 合并后的新数组
     */
	public static byte[] mergeBytes(byte[] data1, byte[] data2) {
		byte[] result = null;
		if(data1!=null && data2!=null) {
			result = new byte[data1.length + data2.length];
	        System.arraycopy(data1, 0, result, 0, data1.length);
	        System.arraycopy(data2, 0, result, data1.length, data2.length);
		}
        return result;
    }
	
	/**
	 * &gt;&gt;&nbsp;按指定长度，截取字节数组
	 * @param data1 要截取的原数据
	 * @param length 截取长度
	 * @return 截取后的数组
	 */
	public static byte[] subBytes(byte[] data1, int length) {
		byte[] result = null;
		if(data1!=null && length>0 && length<=data1.length) {
			result = new byte[length];
			System.arraycopy(data1, 0, result, 0, length);
		}
        return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;按照指定的起始位置截取数组， 闭开区间
	 * @param data1 字节数组
	 * @param start 开始位置
	 * @param end   结束位置 +1
	 * @return 
	 */
	public static byte[] subBytes(byte[] data1, int start, int end) {
		byte[] result = null;
		if(data1!=null && start>=0 && end>=start) {
			result = new byte[end-start];
			for(int i=0;i<result.length;i++) {
				result[i] = data1[start+i];
			}
		}
        return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;将对应类型的数组转换为对应类型的列表
	 * @param arrays 待转化的数组
	 * @return 转化的结果列表；如果成功则返回一个有内容的列表；否则，返回null
	 */
	public static <T> List<T> translateArrayToList(T[] arrays){
		List<T> re = null;
		if(arrays!=null && arrays.length>0) {
			re = new ArrayList<T>();
			for(int i=0;i<arrays.length;i++) {
				re.add(arrays[i]);
			}
		}
		return re;
	}
	
	public static void main(String[] args) {
		
		byte[] a = {10, 20, 30, 40, 50, 60};
		byte[] b = {4,  5,  6,  7,  8,  9, 11};
		
		Stdout.plArray(ArrayUtil.mergeBytes(a, b));
		
		Stdout.plArray(ArrayUtil.subBytes(a, 2));
		
		Stdout.plArray(ArrayUtil.subBytes(a, 0, a.length));
		
	}

}
