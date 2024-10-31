/******************************************************************************************************

This file "AnsjSEGUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.nlp;

import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import net.mickarea.tools.utils.StrUtil;

/**
 * &gt;&gt;&nbsp;一个中文分词器工具类，使用的是 NLPchina / ansj_seg 分词工具.
 * <p>功能统计</p>
 * <p>BaseAnalysis	用户自定义词典X	数字识别X	人名识别X	机构名识别X	新词发现X</p>
 * <p>ToAnalysis	用户自定义词典√	数字识别√	人名识别√	机构名识别X	新词发现X</p>
 * <p>DicAnalysis	用户自定义词典√	数字识别√	人名识别√	机构名识别X	新词发现X</p>
 * <p>IndexAnalysis	用户自定义词典√	数字识别√	人名识别√	机构名识别X	新词发现X</p>
 * <p>NlpAnalysis	用户自定义词典√	数字识别√	人名识别√	机构名识别√	新词发现√</p>
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年12月23日-2023年12月24日
 */
public final class AnsjSEGUtil {

	/**
	 * &gt;&gt;&nbsp;默认分隔符
	 */
	public final static String DEFAULT_SPLITER = ",";
	
	/**
	 * &gt;&gt;&nbsp;私有构造函数，防止静态类被创建对象
	 */
	private AnsjSEGUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * &gt;&gt;&nbsp;使用 BaseAnalysis 最小颗粒度的分词器，进行分词操作
	 * <p>基本就是保证了最基本的分词.词语颗粒度最非常小的..所涉及到的词大约是10万左右.</p>
	 * <p>基本分词速度非常快.在macAir上.能到每秒300w字每秒.同时准确率也很高.但是对于新词他的功能十分有限.</p>
	 * @param words 待分词的字符串
	 * @param splitter 分隔符
	 * @return 一个已分割的字符串。如果 words 参数为null 或者 空，返回空字符串。
	 */
	public static String splitByBaseAnalysis(String words, String splitter) {
		String result = "";
		String tmp=StrUtil.removeAllBlankStrings(words);
		if(tmp.length()>0 && splitter!=null) {
			result = BaseAnalysis.parse(tmp).toStringWithOutNature(splitter);
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;使用 BaseAnalysis 最小颗粒度的分词器，进行分词操作（分割符使用 英文逗号","）
	 * <p>基本就是保证了最基本的分词.词语颗粒度最非常小的..所涉及到的词大约是10万左右.</p>
	 * <p>基本分词速度非常快.在macAir上.能到每秒300w字每秒.同时准确率也很高.但是对于新词他的功能十分有限.</p>
	 * @param words 待分词的字符串
	 * @return 一个已分割的字符串。如果 words 参数为null 或者 空，返回空字符串。
	 */
	public static String splitByBaseAnalysis(String words) {
		return splitByBaseAnalysis(words, DEFAULT_SPLITER);
	}
	
	/**
	 * &gt;&gt;&nbsp;使用 ToAnalysis 精准分词器，进行分词操作
	 * <p>它在易用性,稳定性.准确性.以及分词效率上.都取得了一个不错的平衡.</p>
	 * <p>如果你初次尝试Ansj如果你想开箱即用.那么就用这个分词方式是不会错的.</p>
	 * @param words 待分词的字符串
	 * @param splitter 分隔符
	 * @return 一个已分割的字符串。如果 words 参数为null 或者 空，返回空字符串。
	 */
	public static String splitByToAnalysis(String words, String splitter) {
		String result = "";
		String tmp=StrUtil.removeAllBlankStrings(words);
		if(tmp.length()>0 && splitter!=null) {
			result = ToAnalysis.parse(tmp).toStringWithOutNature(splitter);
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;使用 ToAnalysis 精准分词器，进行分词操作（分割符使用 英文逗号","）
	 * <p>它在易用性,稳定性.准确性.以及分词效率上.都取得了一个不错的平衡.</p>
	 * <p>如果你初次尝试Ansj如果你想开箱即用.那么就用这个分词方式是不会错的.</p>
	 * @param words 待分词的字符串
	 * @return 一个已分割的字符串。如果 words 参数为null 或者 空，返回空字符串。
	 */
	public static String splitByToAnalysis(String words) {
		return splitByToAnalysis(words, DEFAULT_SPLITER);
	}
	
	/**
	 * &gt;&gt;&nbsp;使用 DicAnalysis 用户自定义词典优先策略的分词器，进行分词操作
	 * <p>用户自定义词典优先策略的分词,如果你的用户自定义词典足够好,</p>
	 * <p>或者你的需求对用户自定义词典的要求比较高,那么强烈建议你使用DicAnalysis的分词方式.</p>
	 * <p>可以说在很多方面Dic优于ToAnalysis的结果</p>
	 * @param words 待分词的字符串
	 * @param splitter 分隔符
	 * @return 一个已分割的字符串。如果 words 参数为null 或者 空，返回空字符串。
	 */
	public static String splitByDicAnalysis(String words, String splitter) {
		String result = "";
		String tmp=StrUtil.removeAllBlankStrings(words);
		if(tmp.length()>0 && splitter!=null) {
			result = DicAnalysis.parse(tmp).toStringWithOutNature(splitter);
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;使用 DicAnalysis 用户自定义词典优先策略的分词器，进行分词操作（分割符使用 英文逗号","）
	 * <p>用户自定义词典优先策略的分词,如果你的用户自定义词典足够好,</p>
	 * <p>或者你的需求对用户自定义词典的要求比较高,那么强烈建议你使用DicAnalysis的分词方式.</p>
	 * <p>可以说在很多方面Dic优于ToAnalysis的结果</p>
	 * @param words 待分词的字符串
	 * @return 一个已分割的字符串。如果 words 参数为null 或者 空，返回空字符串。
	 */
	public static String splitByDicAnalysis(String words) {
		return splitByDicAnalysis(words, DEFAULT_SPLITER);
	}
	
	/**
	 * &gt;&gt;&nbsp;使用 IndexAnalysis 面向索引的分词器，进行分词操作
	 * <p>面向索引的分词。顾名思义就是适合在lucene等文本检索中用到的分词。 主要考虑以下两点</p>
	 * <p>召回率 * 召回率是对分词结果尽可能的涵盖。比如对“上海虹桥机场南路” 召回结果是[上海/ns, 上海虹桥机场/nt, 虹桥/ns, 虹桥机场/nz, 机场/n, 南路/nr]</p>
	 * <p>准确率 * 其实这和召回本身是具有一定矛盾性的Ansj的强大之处是很巧妙的避开了这两个的冲突 。</p>
	 * @param words 待分词的字符串
	 * @param splitter 分隔符
	 * @return 一个已分割的字符串。如果 words 参数为null 或者 空，返回空字符串。
	 */
	public static String splitByIndexAnalysis(String words, String splitter) {
		String result = "";
		String tmp=StrUtil.removeAllBlankStrings(words);
		if(tmp.length()>0 && splitter!=null) {
			result = IndexAnalysis.parse(tmp).toStringWithOutNature(splitter);
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;使用 IndexAnalysis 面向索引的分词器，进行分词操作（分割符使用 英文逗号","）
	 * <p>面向索引的分词。顾名思义就是适合在lucene等文本检索中用到的分词。 主要考虑以下两点</p>
	 * <p>召回率 * 召回率是对分词结果尽可能的涵盖。比如对“上海虹桥机场南路” 召回结果是[上海/ns, 上海虹桥机场/nt, 虹桥/ns, 虹桥机场/nz, 机场/n, 南路/nr]</p>
	 * <p>准确率 * 其实这和召回本身是具有一定矛盾性的Ansj的强大之处是很巧妙的避开了这两个的冲突 。</p>
	 * @param words 待分词的字符串
	 * @return 一个已分割的字符串。如果 words 参数为null 或者 空，返回空字符串。
	 */
	public static String splitByIndexAnalysis(String words) {
		return splitByIndexAnalysis(words, DEFAULT_SPLITER);
	}
	
	/**
	 * &gt;&gt;&nbsp;使用 NlpAnalysis 带有新词发现功能的分词器，进行分词操作
	 * <p>nlp分词是总能给你惊喜的一种分词方式.</p>
	 * <p>它可以识别出未登录词.但是它也有它的缺点.速度比较慢.稳定性差.ps:我这里说的慢仅仅是和自己的其他方式比较.应该是40w字每秒的速度吧.</p>
	 * <p>个人觉得nlp的适用方式.1.语法实体名抽取.未登录词整理.主要是对文本进行发现分析等工作</p>
	 * @param words 待分词的字符串
	 * @param splitter 分隔符
	 * @return 一个已分割的字符串。如果 words 参数为null 或者 空，返回空字符串。
	 */
	public static String splitByNlpAnalysis(String words, String splitter) {
		String result = "";
		String tmp=StrUtil.removeAllBlankStrings(words);
		if(tmp.length()>0 && splitter!=null) {
			result = NlpAnalysis.parse(tmp).toStringWithOutNature(splitter);
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;使用 NlpAnalysis 带有新词发现功能的分词器，进行分词操作（分割符使用 英文逗号","）
	 * <p>nlp分词是总能给你惊喜的一种分词方式.</p>
	 * <p>它可以识别出未登录词.但是它也有它的缺点.速度比较慢.稳定性差.ps:我这里说的慢仅仅是和自己的其他方式比较.应该是40w字每秒的速度吧.</p>
	 * <p>个人觉得nlp的适用方式.1.语法实体名抽取.未登录词整理.主要是对文本进行发现分析等工作</p>
	 * @param words 待分词的字符串
	 * @return 一个已分割的字符串。如果 words 参数为null 或者 空，返回空字符串。
	 */
	public static String splitByNlpAnalysis(String words) {
		return splitByNlpAnalysis(words, DEFAULT_SPLITER);
	}
	
	//测试用
	/*
	public static void main(String[] args) {
		
		String title = "月光下的火山石是我的博客";
		String paperName = "#技术分享#MySQL8在RHEL（红帽操作系统）6.5上的安装（二）";
		
		Stdout.pl(splitByNlpAnalysis(title));
		Stdout.pl(splitByNlpAnalysis(paperName));
	}
	*/
}
