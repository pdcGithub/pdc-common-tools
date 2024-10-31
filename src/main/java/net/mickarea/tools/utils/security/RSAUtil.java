/******************************************************************************************************

This file "RSAUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * &gt;&gt;&nbsp;RSA 算法的加密工具包
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年9月27日-2024年08月12日
 */
public final class RSAUtil {
	
	/**
	 * 私有构造函数
	 */
	private RSAUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 密钥长度
	 */
	public static int KEY_LENGTH = 2048;
	
	/**
	 * &gt;&gt;&nbsp;生成密钥对信息
	 * @return 一个简单的密钥对对象，包含公钥 和 私钥
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static KeyPair genKeys() throws Exception {
		//密钥对生成器：生成类型为 RSA 算法所需密钥对
		KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		//随机种子处理
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(System.nanoTime());
		//生成器初始化
		gen.initialize(KEY_LENGTH, random);
		//生成
		KeyPair keyPair = gen.genKeyPair();
		//返回结果
		return keyPair;
	}
	
	/**
	 * &gt;&gt;&nbsp;获取密钥对中的公钥信息字符串（以base64编码方式输出）
	 * @param keypair 密钥对
	 * @return 公钥信息字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String getPublicKey(KeyPair keypair) throws Exception {
		return byteToBase64(keypair.getPublic().getEncoded());
	}
	
	/**
	 * &gt;&gt;&nbsp;获取密钥对中的私钥信息字符串（以base64编码方式输出）
	 * @param keypair 密钥对
	 * @return 私钥信息字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String getPrivateKey(KeyPair keypair) throws Exception {
		return byteToBase64(keypair.getPrivate().getEncoded());
	}
	
	/**
	 * &gt;&gt;&nbsp;将公钥信息字符串（base64编码），转换为公钥信息对象
	 * @param publicBase64String 公钥信息字符串
	 * @return 公钥信息对象
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static PublicKey stringToPublicKey(String publicBase64String) throws Exception {
		byte[] keyBytes = base64Tobyte(publicBase64String);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
	}
	
	/**
	 * &gt;&gt;&nbsp;将私钥信息字符串（base64编码），转换为私钥信息对象
	 * @param privateBase64String 私钥信息字符串
	 * @return 私钥信息对象
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static PrivateKey stringToPrivateKey(String privateBase64String) throws Exception {
		byte[] keyBytes = base64Tobyte(privateBase64String);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}
	
	/**
	 * &gt;&gt;&nbsp;将字节数组，转换为base64编码的字符串输出。
	 * @param input 字节数组
	 * @return base64编码的字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String byteToBase64(byte[] input) throws Exception {
		return Base64.getEncoder().encodeToString(input);
	}
	
	/**
	 * &gt;&gt;&nbsp;将base64编码的字符串，转换为字节数组输出。
	 * @param base64 base64编码的字符串
	 * @return 字节数组
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static byte[] base64Tobyte(String base64) throws Exception {
		return Base64.getDecoder().decode(base64);
	}
	
	/**
	 * &gt;&gt;&nbsp;使用公钥，对传入的字节数组信息进行加密，返回一个加密后的字节数组
	 * @param input 待加密的字节数组
	 * @param publicKey 公钥信息对象
	 * @return 加密后的字节数组
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static byte[] encodeByteArray(byte[] input, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(input);
	}
	
	/**
	 * &gt;&gt;&nbsp;使用私钥，对传入的字节数组信息进行解密，返回一个解密后的字节数组
	 * @param input 待解密的字节数组
	 * @param privateKey 私钥信息对象
	 * @return 解密后的字节数组
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static byte[] decodeByteArray(byte[] input, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(input);
	}
	
	/**
	 * &gt;&gt;&nbsp;使用公钥对象，对传入的字符串进行加密
	 * @param plaintext 待加密字符串
	 * @param publicKey 公钥信息对象
	 * @return 加密后的字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String encode(String plaintext, PublicKey publicKey) throws Exception {
		byte[] encodeBytes = encodeByteArray(plaintext.getBytes(), publicKey);
		return byteToBase64(encodeBytes);
	}
	
	/**
	 * &gt;&gt;&nbsp;使用公钥字符串信息，对传入的字符串进行加密
	 * @param plaintext 待加密字符串
	 * @param publicBase64String 公钥字符串信息（base64编码）
	 * @return 加密后的字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String encode(String plaintext, String publicBase64String) throws Exception {
		return encode(plaintext, stringToPublicKey(publicBase64String));
	}
	
	/**
	 * &gt;&gt;&nbsp;使用私钥对象，对传入的字符串进行解密
	 * @param ciphertext 待解密字符串
	 * @param privateKey 私钥信息对象
	 * @return 解密后的字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String decode(String ciphertext, PrivateKey privateKey) throws Exception {
		byte[] decodeBytes = decodeByteArray(base64Tobyte(ciphertext), privateKey);
		return new String(decodeBytes);
	}
	
	/**
	 * &gt;&gt;&nbsp;使用私钥字符串信息，对传入的字符串进行解密
	 * @param ciphertext 待解密字符串
	 * @param privateBase64String 私钥字符串信息（base64编码）
	 * @return 解密后的字符串
	 * @throws Exception 执行过程可能发生异常导致报错
	 */
	public static String decode(String ciphertext, String privateBase64String) throws Exception {
		return decode(ciphertext, stringToPrivateKey(privateBase64String));
	}
	
	/**
	 * &gt;&gt;&nbsp;测试函数
	 */
	/*
	public static void main(String[] args) throws Exception {
		
		Stdout.pl("构造一对密钥=========================");
		long start = System.currentTimeMillis();
		KeyPair keyPairs = RSAUtil.genKeys();
		Stdout.pl("密钥生成耗时:"+(System.currentTimeMillis()-start)+"毫秒");
		
		String publickeyStr = RSAUtil.getPublicKey(keyPairs);
		String privatekeyStr = RSAUtil.getPrivateKey(keyPairs);
		String input = "今天天气真好，I'm Iron Man. 122939404934029348";
		
		long  start1 = System.currentTimeMillis();
		String output = RSAUtil.encode(input, publickeyStr);
		Stdout.pl("加密耗时:"+(System.currentTimeMillis()-start1)+"毫秒");
		
		long  start2 = System.currentTimeMillis();
		String output2 = RSAUtil.decode(output, privatekeyStr);
		Stdout.pl("解密耗时:"+(System.currentTimeMillis()-start2)+"毫秒");
		
		Stdout.pl("公钥（长度:"+publickeyStr.length()+"）:"+publickeyStr);
		Stdout.pl("私钥（长度:"+privatekeyStr.length()+"）:"+privatekeyStr);
		Stdout.pl("待加密字符串:("+input+")");
		Stdout.pl("加密后字符串（长度:"+output.length()+"）:("+output+")");
		Stdout.pl("解密后字符串:("+output2+")");
		Stdout.pl("====================================");
	}
	*/
}
