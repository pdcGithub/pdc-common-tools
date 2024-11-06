/******************************************************************************************************

This file "PatternUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * &gt;&gt;&nbsp;一个正则工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月24日-2023年11月13日
 */
public final class PatternUtil {
	
	/**
	 * 构造函数私有化，防止创建对象
	 */
	private PatternUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 报文分界线识别正则
	 */
	public static final String DATAGRAM_BOUNDARY = "(\\-){2,}\\w+";
	
	/**
	 * 报文最后的分界线识别正则
	 */
	public static final String DATAGRAM_BOUNDARY_LAST = "(\\-){2,}\\w+(\\-){2}";
	
	/**
	 * 上传的文件头部过滤正则
	 */
	public static final String UPLOAD_FILE_HEADER = "(\\-){2,}.+\r\n.+(filename=.*)\r\n.+(\r\n){2}";
	
	/**
	 * 上传的文件尾部过滤正则
	 */
	public static final String UPLOAD_FILE_TAIL = "\r\n(\\-){4,}[a-zA-Z0-9]+(\\-){2}\r\n";
	
	/**
	 * 上传的文件名称过滤正则
	 */
	public static final String UPLOAD_FILE_NAME = "filename=\"(.*)\"";
	
	/**
	 * &gt;&gt;&nbsp;提取上传的文件头报文
	 * @param str 源头字符串
	 * @return 目标字符串
	 */
	public static String getUploadFileHeader(String str) {
		String result = "";
		if(str!=null) {
			Matcher m = Pattern.compile(UPLOAD_FILE_HEADER).matcher(str);
			if(m.find()) {
				result = m.group();
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;提取文件的真实名称
	 * @param str 带有名称的报文头部
	 * @return 文件的真实名称
	 */
	public static String getUploadFileRealName(String str) {
		String result = "";
		if(str!=null) {
			Matcher m = Pattern.compile(UPLOAD_FILE_NAME).matcher(str);
			if(m.find()) {
				result = m.group();
				String[] s = result.split("\"");
				if(s.length>=2) {
					result = s[s.length-1];
				}
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;提取文件名中的后缀
	 * @param str 一个文件名
	 * @return 后缀字符串
	 */
	public static String getFileSuffixName(String str) {
		String result = "";
		if(str!=null) {
			String[] sfx = str.split("\\.");
			if(sfx.length>=1) {
				result = sfx[sfx.length-1];
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;提取上传的文件末尾
	 * @param str 报文
	 * @return 末尾字符串
	 */
	public static String getUploadFileTail(String str) {
		String result = "";
		if(str!=null) {
			Matcher m = Pattern.compile(UPLOAD_FILE_TAIL).matcher(str);
			if(m.find()) {
				result = m.group();
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;校验传入的邮件地址是否符合邮件地址规范
	 * @param address 将要校验的邮件地址信息
	 * @return 如果符合，则返回true；否则，返回false
	 */
	public static boolean isEmailAddress(String address) {
		boolean result = false;
		if(!StrUtil.isEmptyString(address) && Pattern.matches("[a-z0-9\\.\\_\\-]+@[a-z0-9\\.\\_\\-]+", address.toLowerCase())) {
			result = true;
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;利用正则表达式，从目标字符串提取对应的内容
	 * @param patternStr 提取字符串用的正则表达式
	 * @param oriStr 被提取的目标字符串
	 * @return 提取出来的结果列表；如果正则表达式报错，则返回null; 如果正常执行，则返回一个 List 对象
	 */
	public static List<String> findMatchString(String patternStr, String oriStr) {
		List<String> result = null;
		Pattern p = null;
		if(patternStr!=null && patternStr.trim().length()>0 && !StrUtil.isEmptyString(oriStr)) {
			try{
				p = Pattern.compile(patternStr);
			}catch(Exception e) {
				Stdout.pl("正则“"+patternStr+"”异常，"+e.getMessage());
				Stdout.pl(e);
			}
			if(p!=null) {
				result = new ArrayList<String>();
				Matcher m = p.matcher(oriStr);
				while(m.find()) {
					result.add(m.group());
				}
			}
		}else {
			Stdout.pl("传入的 patternStr(="+patternStr+") 参数异常；或者 传入的 oriStr(="+oriStr+") 参数异常");
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;从html文本中，查找纯文本内容。
	 * <p>比如：&lt;p style='text-align:center;'&gt;测试&lt;/p&gt;内容，返回值为：测试</p>
	 * @param htmlContent 待处理的html文本
	 * @return 纯文本内容
	 */
	public static String findPureTextFromHtmlContent(String htmlContent) {
		String result = "";
		if(!StrUtil.isEmptyString(htmlContent)) {
			result = htmlContent.replaceAll("<[^>]+>", "");
		}
		return result;
	}
	
	//测试函数
	/*
	public static void main(String[] args) {
		Stdout.pl(isEmailAddress("674256821@qq.com"));
		Stdout.pl(isEmailAddress("sdlfis.d6f87d.s5-sdfd-s5_asdfs@163.com"));
		Stdout.pl(isEmailAddress("net.mick_area@gmail.com"));
		Stdout.pl(isEmailAddress("mick123.com.123.com@...sd5fsd4f5dsf"));
		Stdout.pl(isEmailAddress("mick123.com.123.com@dfsfdf@...sd5fsd4f5dsf"));
	}
	*/
}
