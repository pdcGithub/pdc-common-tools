/******************************************************************************************************

This file "RandomUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.util.Random;

/**
 * &gt;&gt;&nbsp;一个产生随机信息的工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月6日-2023年10月21日
 */
public final class RandomUtil {
	
	/**
	 * 私有构造函数
	 */
	private RandomUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 单次可生成的最大长度
	 */
	public static final int MAX_LENGTH = 10;
	
	/**
	 * 单次可生成的最小长度
	 */
	public static final int MIN_LENGTH = 1;
	
	/**
	 * 一个错误代码，如果使用了不抛异常的函数，在遇到程序异常的时候就会 返回这个值。
	 */
	public static final String ERROR_CODE = "ERROR00001";
	
	/**
	 * &gt;&gt;&nbsp;生成一个10位(或以下)长度的随机10进制数字字符串
	 * @param length 要生成的字符串长度
	 * @param random 随机对象，如果为 null，则函数内自动生成。
	 * @return 一个数字字符串
	 * @throws Exception 各种异常
	 */
	private static String createFewNums(int length, Random random) throws Exception {
		String result = "";
		if(random==null) {
			random = new Random(System.nanoTime());
		}
		if(length>MAX_LENGTH || length<MIN_LENGTH) {
			throw new IllegalArgumentException("传入的length参数不符合要求, length:"+length);
		}
		String tmp = random.nextDouble()+"";
		tmp = tmp.substring(2);
		result = tmp.substring(tmp.length()-length, tmp.length());
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;生成一个指定长度的数字字符串（这个函数生成的数字字符串可能为0开头的）
	 * @param length 数字的长度
	 * @return 一个数字
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static String genNumberString(int length) throws Exception {
		
		if(length<MIN_LENGTH) {
			throw new IllegalArgumentException("传入的length参数不符合要求，小于最小值"+MIN_LENGTH+", length:"+length);
		}
		
		StringBuffer result = new StringBuffer();
		Random random = new Random(System.nanoTime());
		
		int a = length / MAX_LENGTH; //商
		int b = length % MAX_LENGTH; //余数
		
		//每个商为一段整齐的数字字符串。
		for(int i=0;i<a;i++) {
			result.append(createFewNums(MAX_LENGTH, random));
		}
		//余数则根据数字生成余数长度的内容
		if(b>0) {
			result.append(createFewNums(b, random));
		}
		
		return result.toString();
	}
	
	/**
	 * &gt;&gt;&nbsp;<p>生成一个指定长度的数字字符串（这个函数生成的数字字符串可能为0开头的）</p>
	 * <p>这个函数不报异常，如果执行出错就返回 ERROR00001</p>
	 * @param length 要生成的随机字符串的长度
	 * @return 一个指定长度的随机字符串
	 */
	public static String genNumberStringWithoutException(int length) {
		String result = ERROR_CODE;
		try {
			result = genNumberString(length);
		}catch(Exception e) {
			Stdout.pl(e);
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;生成一个指定长度的英文字母字符串
	 * @param length 字符串的长度
	 * @return 字母字符串
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static String genLetterString(int length) throws Exception {
		String result = "";
		//校验输入参数
		if(length<MIN_LENGTH) {
			throw new IllegalArgumentException("指定的字符长度,length 值小于最小值 "+MIN_LENGTH);
		}
		//随机对象
		Random random = new Random(System.nanoTime());
		//循环生成内容
		char[] tmp = new char[length];
		for(int i=0;i<length;i++) {
			tmp[i] = (char)(65+random.nextFloat()*26); //大写字母的范围是 65-90
		}
		//返回结果
		result = new String(tmp);
		
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;<p>生成一个指定长度的英文字母字符串</p>
	 * <p>这个函数不报异常，如果执行出错就返回 ERROR00001</p>
	 * @param length 字符串的长度
	 * @return 字母字符串 
	 */
	public static String genLetterStringWithoutException(int length) {
		String result = ERROR_CODE;
		try {
			result = genLetterString(length);
		}catch(Exception e) {
			Stdout.pl(e);
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;生成一个英文字母和数字混合的字符串
	 * @param length 字符串的长度
	 * @return 一个字符串
	 * @throws Exception 如果执行出错，就会抛出异常。
	 */
	public static String genNumAndLetterMixedString(int length) throws Exception {
		String result = "";
		//校验输入参数
		if(length<MIN_LENGTH) {
			throw new IllegalArgumentException("指定的字符长度,length 值小于最小值 "+MIN_LENGTH);
		}
		//随机对象
		Random random = new Random(System.nanoTime());
		//循环生成内容
		char[] tmp = new char[length];
		for(int i=0;i<length;i++) {
			tmp[i] = (char)(65+random.nextFloat()*26); //大写字母的范围是 65-90
			//这里增加一个随机判断，如果 随机值 大于0.5 则将内容替换为一个数字 字符。
			if(random.nextFloat()>0.5f) {
				tmp[i] = (char)(48+random.nextFloat()*10); //数字字符的范围是 48-57
			}
		}
		//返回结果
		result = new String(tmp);
		
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;<p>生成一个英文字母和数字混合的字符串</p>
	 * <p>这个函数不报异常，如果执行出错就返回 ERROR00001</p>
	 * @param length 字符串长度
	 * @return 随机字符串
	 */
	public static String genNumAndLetterMixedStringWithoutException(int length) {
		String result = ERROR_CODE;
		try {
			result = genNumAndLetterMixedString(length);
		}catch(Exception e) {
			Stdout.pl(e);
		}
		return result;
	}

	/*
	public static void main(String[] args) throws Exception {
		
		String o = "0123456789"; //48-57
		Stdout.plArray(o.getBytes());
		
		String a = "abcdefghijklmnopqrstuvwxyz"; //97-122
		Stdout.plArray(a.getBytes());
		
		String b = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //65-90
		Stdout.plArray(b.getBytes());
		
		Stdout.pl(genNumberString(35));
		
		Stdout.pl(genLetterString(30));
		
		Stdout.pl(genNumAndLetterMixedString(25));
		
		Stdout.pl(genLetterStringWitoutException(0));
		Stdout.pl(genNumberStringWitoutException(0));
		Stdout.pl(genNumAndLetterMixedStringWitoutException(0));
		
		Stdout.pl(genLetterStringWitoutException(3));
		Stdout.pl(genNumberStringWitoutException(4));
		Stdout.pl(genNumAndLetterMixedStringWitoutException(5));
		
		Stdout.pl(genLetterStringWitoutException(-1));
		Stdout.pl(genNumberStringWitoutException(-1));
		Stdout.pl(genNumAndLetterMixedStringWitoutException(-1));
		
	}
	*/
}