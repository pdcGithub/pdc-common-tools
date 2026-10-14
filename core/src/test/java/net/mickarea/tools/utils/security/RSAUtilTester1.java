/******************************************************************************************************

This file "RSAUtilTester1.java" is part of project "pdc-common-tool" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.KeyPair;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import net.mickarea.tools.utils.Stdout;

/**
 * 这里是 RSAUtil 的单元测试类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年12月11日-2025年12月11日
 */
public class RSAUtilTester1 {

	@Test
	@Timeout(value = 10, unit = TimeUnit.SECONDS)
	void encodeAndDecode() {
		
		// 这里使用 RSAUtil 工具类 进行一次模拟的 加密和解密处理
		// 另外这里还会计时，超过 10 秒就异常
		
		assertDoesNotThrow(()->{
			
			KeyPair keyPairs = RSAUtil.genKeys();
			
			String publickeyStr = RSAUtil.getPublicKey(keyPairs);
			String privatekeyStr = RSAUtil.getPrivateKey(keyPairs);
			String input = "今天天气真好，I'm Iron Man. 122939404934029348";
			
			String encodeStr = RSAUtil.encode(input, publickeyStr);
			
			String decodeStr = RSAUtil.decode(encodeStr, privatekeyStr);
			
			Stdout.mylogger.debug("公钥（长度:"+publickeyStr.length()+"）:"+publickeyStr);
			Stdout.mylogger.debug("私钥（长度:"+privatekeyStr.length()+"）:"+privatekeyStr);
			Stdout.mylogger.debug("待加密字符串:("+input+")");
			Stdout.mylogger.debug("加密后字符串（长度:"+encodeStr.length()+"）:("+encodeStr+")");
			Stdout.mylogger.debug("解密后字符串:("+decodeStr+")");
			
			// 比对，解密的结果 是否与 加密前一致
			assertEquals(input, decodeStr);
		});
	}
	
	@Test
	@Timeout(value = 10, unit = TimeUnit.SECONDS)
	void encodeAndDecode2() {
		
		// 这里使用 RSAUtil 工具类 进行一次模拟的 加密和解密处理
		// 另外这里还会计时，超过 10 秒就异常
		
		assertDoesNotThrow(()->{
			
			// 这里把密钥长度改为：4096
			KeyPair keyPairs = RSAUtil.genKeys(4096, "RSA");
			
			String publickeyStr = RSAUtil.getPublicKey(keyPairs);
			String privatekeyStr = RSAUtil.getPrivateKey(keyPairs);
			String input = "今天天气真好，I'm Iron Man. 122939404934029348";
			
			String encodeStr = RSAUtil.encode(input, publickeyStr);
			
			String decodeStr = RSAUtil.decode(encodeStr, privatekeyStr);
			
			Stdout.mylogger.debug("公钥（长度:"+publickeyStr.length()+"）:"+publickeyStr);
			Stdout.mylogger.debug("私钥（长度:"+privatekeyStr.length()+"）:"+privatekeyStr);
			Stdout.mylogger.debug("待加密字符串:("+input+")");
			Stdout.mylogger.debug("加密后字符串（长度:"+encodeStr.length()+"）:("+encodeStr+")");
			Stdout.mylogger.debug("解密后字符串:("+decodeStr+")");
			
			// 比对，解密的结果 是否与 加密前一致
			assertEquals(input, decodeStr);
		});
	}
}
