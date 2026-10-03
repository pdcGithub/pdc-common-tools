/******************************************************************************************************

This file "PatternUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一个正则工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月24日
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
	@Deprecated
	public static final String DATAGRAM_BOUNDARY = "(\\-){2,}\\w+";
	
	/**
	 * 报文最后的分界线识别正则
	 */
	@Deprecated
	public static final String DATAGRAM_BOUNDARY_LAST = "(\\-){2,}\\w+(\\-){2}";
	
	/**
	 * 上传的文件头部过滤正则
	 */
	@Deprecated
	public static final String UPLOAD_FILE_HEADER = "(\\-){2,}.+\r\n.+(filename=.*)\r\n.+(\r\n){2}";
	
	/**
	 * 上传的文件尾部过滤正则
	 */
	@Deprecated
	public static final String UPLOAD_FILE_TAIL = "\r\n(\\-){4,}[a-zA-Z0-9]+(\\-){2}\r\n";
	
	/**
	 * 上传的文件名称过滤正则
	 */
	@Deprecated
	public static final String UPLOAD_FILE_NAME = "filename=\"(.*)\"";
	
	/** 
	 * 正则表达式的标准可选配置。这些配置信息，用于 Pattern.compile 方法上的 flags 参数。
	 */
	public static final List<Integer> STANDARD_FLAGS = Arrays.asList(
		Pattern.CASE_INSENSITIVE, Pattern.MULTILINE, Pattern.DOTALL,
		Pattern.UNICODE_CASE, Pattern.CANON_EQ, Pattern.UNIX_LINES, 
		Pattern.LITERAL, Pattern.UNICODE_CHARACTER_CLASS, Pattern.COMMENTS
	);
	
	/**
	 * 提取上传的文件头报文
	 * @param str 源头字符串
	 * @return 目标字符串
	 */
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	
	/**
	 * 这是一个 Pattern.matches 的替代方法。标准库中，这个方法对于正则校验本身，没有相关的配置处理。
	 * 但是，Pattern.compile 方法是有配置参数的。所以，这里我们实现一个带配置的 matches 方法。
	 * @param regexp 这是 Java 版本的正则表达式字符串。
	 * @param input 这是要校验的字符信息
	 * @param configs 可选配置。它是正则表达式的编译配置。这段内容将以掩码的方式处理。多个config进行 或运算。
     * 具体的常量，可以参考 
     * {@link Pattern#CASE_INSENSITIVE}, 
     * {@link Pattern#MULTILINE}, 
     * {@link Pattern#DOTALL},
     * {@link Pattern#UNICODE_CASE}, 
     * {@link Pattern#CANON_EQ}, 
     * {@link Pattern#UNIX_LINES},
     * {@link Pattern#LITERAL}, 
     * {@link Pattern#UNICODE_CHARACTER_CLASS} 
     * 和 {@link Pattern#COMMENTS}
	 * @return 如果正则表达式 和 字符内容 匹配，则返回 true 。如果参数异常，或者 不匹配，则返回 false。
	 */
	public static boolean matches(String regexp, CharSequence input, int... optionalConfigs) {
		
		// 定义一个结果
		boolean result = false;
		
		// 如果 正则表达式是 空，或者 待判断字符串为 空，直接返回 false
		if(StrUtil.isEmptyString(regexp) || input==null || input.length()<=0 ) return result;
		
		// 如果 configs 有参入，但是不在 Pattern 配置中，则去除。
		// 如果 configs 有重复，也移除
		// 最后会得到一个 int 数组
		List<Integer> flags = new ArrayList<Integer>();
		if(optionalConfigs!=null) {
			for(int i=0; i<optionalConfigs.length;i++) {
				if(STANDARD_FLAGS.contains(optionalConfigs[i])) flags.add(optionalConfigs[i]);
			}
		}
		// 将结果去重，并做一个掩码运算（或运算）得出一个 flag 信息。
		int flag = -1;
		if(flags.size()>0) flag = flags.stream()
										.distinct()
										.mapToInt(Integer::intValue)
										.reduce(0, (a, b)-> a | b); // 因为是 或运算，初始值用 0
		
		// 这里开始进行正则匹配
		try {
			// 把字符串，编译为正则对象
			Pattern p = flag==-1 ? Pattern.compile(regexp) : Pattern.compile(regexp, flag);
			// 设置 要进行匹配的内容
			Matcher m = p.matcher(input);
			// 检测是否匹配
			result = m.matches();
			
		} catch(Exception e) {
			// 记录错误日志，写入到 debug 中
			Stdout.mylogger.debug(
				Stdout.fplToAnyWhere(
					"An exception occurred while matching or compiling a regular expression exp=%s, input=%s, flags=%s", 
					regexp, input, flags)
			);
		}
		
		return result ;
	}
	
}
