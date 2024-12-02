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
 * 一个正则工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月24日-2024年12月2日
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
	 * 提取上传的文件头报文
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
	 * 提取文件的真实名称
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
	 * 提取文件名中的后缀
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
	 * 提取上传的文件末尾
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
	 * 校验传入的邮件地址是否符合邮件地址规范
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
	 * 利用正则表达式，从目标字符串提取对应的内容
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
	 * 从html文本中，查找纯文本内容。
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
	
	/**
	 * 生成一个正则字符串，用于html标签文本整体替换。如果传入的是一个空字符串 或者 null，则返回 空字符串。
	 * @param tagName html 标签名，大小写都可以，因为内部会自动转换
	 * @return 一个正则字符串
	 */
	public static String genRegStrForHtmlTagReplace(String tagName) {
		String re = "";
		// 不为空才处理
		if(!StrUtil.isEmptyString(tagName)) {
			// 转小写字母
			String lowerCaseStr = tagName.toLowerCase();
			// 开始 生成一个名字字符串 
			StringBuffer tagNameSb = new StringBuffer();
			for(char c: lowerCaseStr.toCharArray()) {
				tagNameSb.append("[");
				if( c >= 97 && c <= 122 ) { // 小写字母 a-z 的范围
					tagNameSb.append((char)(c-32)); // 添加 大写字母 内容
				}
				tagNameSb.append(c);
				tagNameSb.append("]");
			}
			String tmpName = tagNameSb.toString();
			//
			re = "[\\s]*<("+tmpName+"\\s+.*?|"+tmpName+")>[\\s\\S]*?</("+tmpName+")>[\\s]*";
		}
		return re;
	}
	
	//测试函数
	/*
	public static void main(String[] args) {
		String formStr = "[\\s]*<([Ff][Oo][Rr][Mm]\\s+.*?|[Ff][Oo][Rr][Mm])>[\\s\\S]*?</([Ff][Oo][Rr][Mm])>[\\s]*";
		String newStr = genRegStrForHtmlTagReplace("Form2");
		Stdout.pl(formStr);
		Stdout.pl(newStr);
		Stdout.pl(formStr.equals(newStr));
		String scriptStr = "[\\s]*<([Ss][Cc][Rr][Ii][Pp][Tt]\\s+.*?|[Ss][Cc][Rr][Ii][Pp][Tt])>[\\s\\S]*?</([Ss][Cc][Rr][Ii][Pp][Tt])>[\\s]*";
		String newStr2 = genRegStrForHtmlTagReplace("scRipt");
		Stdout.pl(scriptStr);
		Stdout.pl(newStr2);
		Stdout.pl(scriptStr.equals(newStr2));
		Stdout.pl(genRegStrForHtmlTagReplace("H2"));
		Stdout.pl(genRegStrForHtmlTagReplace("objectZip"));
	}
	*/
}
