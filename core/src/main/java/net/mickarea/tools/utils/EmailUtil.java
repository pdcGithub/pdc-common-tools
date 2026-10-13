/******************************************************************************************************

This file "EmailUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import net.mickarea.tools.utils.security.RSAUtil;

/**
 * 关于电子邮件的收发处理
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年9月27日-2024年12月2日
 */
public final class EmailUtil {
	
	/**
	 * 构造函数，私有
	 */
	private EmailUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 一个邮件配置信息对象，用于构造电子邮件的服务配置信息
	 * 这个是一个静态的变量，只会在第一次使用时赋值，所以没有线程安全问题。
	 */
	private static final Map<String, String> EMAIL_CONFIG = emailConfigInit();
	
	/**
	 * 默认的发送方名称。
	 */
	public static final String DEFAULT_FROM_NAME = "mickarea.net 官方服务";
	
	/**
	 * 默认的主题信息
	 */
	public static final String DEFAULT_SUBJECT = "无主题";
	
	/**
	 * 默认的字符集
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 * 邮件配置信息对象，初始化
	 * @return 配置信息 map
	 */
	private static Map<String, String> emailConfigInit(){
		
		Map<String, String> map = new HashMap<String, String>();
		
		//测试配置文件读写
		ResourceBundle bundle = null;
		try {
			//这里使用包内路径，因为不管是相对路径，还是绝对路径，jar 作为模块导入其它web项目中，都识别不了。
			bundle = ResourceBundle.getBundle("conf.email-config.emailserver");
			System.out.println("解析配置文件成功，邮件配置读取完毕。");
		} catch (Exception e) {
			System.out.println("解析配置文件异常，请确认当前程序包中 /conf/email-config/emailserver.properties 是否存在 ???");
			System.out.println(e.getMessage());
		}
		
		//这里开始读取配置信息
		if(bundle!=null) {
			//获取配置文件中的所有配置键
			List<String> keys = ListUtil.makeEnumerationObjectToStringList(bundle.getKeys());
			if(keys!=null) {
				for(String key: keys) {
					try {
						String value = StrUtil.isEmptyString(bundle.getString(key))?"":bundle.getString(key).trim().toLowerCase();
						map.put(key, value);
					} catch (Exception e) {
						//这里一个键值对错误，不影响整体的赋值。
						continue;
					}
				}
			}
			//获取服务账号和密码
			if(map.containsKey("filedir") && !StrUtil.isEmptyString(map.get("filedir"))) {
				String fileDir = map.get("filedir");
				File dir = new File(fileDir);
				if(dir.exists() && dir.isDirectory()) {
					//私钥信息
					String privateKeyStr = null;
					try{
						privateKeyStr=FileUtil.loadStringFromFile(fileDir, "pdc-rsa.private");
					}catch(Exception e1){
					}
					//服务账户
					String userCipherStr = null;
					try{
						userCipherStr=FileUtil.loadStringFromFile(fileDir, "pdc-rsa.username");
					}catch(Exception e2){
					}
					//服务密码
					String passwdCipherStr = null;
					try {
						passwdCipherStr=FileUtil.loadStringFromFile(fileDir, "pdc-rsa.password");
					}catch(Exception e3) {
					}
					//如果所有信息都齐全，则开始解码
					if(privateKeyStr!=null && userCipherStr!=null && passwdCipherStr!=null) {
						//账户
						String username = null;
						try {
							username = RSAUtil.decode(userCipherStr, privateKeyStr);
						}catch(Exception e1) {
							Stdout.pl("电邮服务账户名，解码出错，请检查!!");
						}
						//密码
						String password = null;
						try {
							password = RSAUtil.decode(passwdCipherStr, privateKeyStr);
						}catch(Exception e2) {
							Stdout.pl("电邮服务密码，解码出错，请检查!!");
						}
						//加入变量信息
						if(username!=null && password!=null) {
							map.put("serverUserName", username);
							map.put("serverPassword", password);
						}
					} else {
						Stdout.pl("加密信息获取异常，请检查。"+dir.getAbsolutePath());
					}
				}else {
					Stdout.pl("本地文件夹, filedir="+fileDir+", 不存在 或者 不是文件夹");
				}
			}else {
				Stdout.pl("邮件服务配置信息异常，filedir 信息缺失，"+map.get("filedir"));
			}
		}
		
		return map;
	}
	
	/**
	 * 邮件一对一发送方法
	 * @param subject 邮件主题信息
	 * @param htmlString 要发送的内容，html 格式
	 * @param toEmailAddr 目标邮件地址
	 * @param charsetName 邮件内容字符集 。比如 GBK, GB18030, UTF-8 之类的
	 * @return 如果发送成功，返回 true ；如果发送失败，返回 false 。
	 */
	public static boolean sendEmail(String subject, String htmlString, String toEmailAddr, String charsetName) {
		return sendEmail(DEFAULT_FROM_NAME, subject, htmlString, toEmailAddr, charsetName);
	}
	
	/**
	 * 邮件一对一发送方法（邮件内容字符集 默认 GBK）。此函数已经不建议使用，因为邮件发送最好指定字符编码
	 * @param subject 邮件主题信息
	 * @param htmlString 要发送的内容，html 格式
	 * @param toEmailAddr 目标邮件地址
	 * @return 如果发送成功，返回 true ；如果发送失败，返回 false 。
	 */
	@Deprecated
	public static boolean sendEmail(String subject, String htmlString, String toEmailAddr) {
		return sendEmail(subject, htmlString, toEmailAddr, "GBK");
	}
	
	/**
	 * 邮件一对一发送方法
	 * @param fromName 发送方的名称。不是邮件地址，是一个简单的名称
	 * @param subject 邮件主题信息
	 * @param htmlString 要发送的内容，html 格式
	 * @param toEmailAddr 目标邮件地址
	 * @param charsetName 邮件内容字符集 。比如 GBK, GB18030, UTF-8 之类的
	 * @return 如果发送成功，返回 true ；如果发送失败，返回 false 。
	 */
	public static boolean sendEmail(String fromName, String subject, String htmlString, String toEmailAddr, String charsetName) {
		// 定义一个返回执行结果的变量
		boolean result = false;
		// 获取服务器的账号和密码
		String serverUserName = EmailUtil.EMAIL_CONFIG.get("serverUserName");
		String serverPassword = EmailUtil.EMAIL_CONFIG.get("serverPassword");
		// 
		if(StrUtil.isEmptyString(serverUserName) || StrUtil.isEmptyString(serverPassword)) {
			Stdout.pl("邮件服务的账号或者密码缺失，请检查配置信息");
		}else {
			if(!StrUtil.isEmptyString(htmlString) && !StrUtil.isEmptyString(toEmailAddr)) {
				//如果传入的参数有效才执行
				Properties props = new Properties();
				EmailUtil.EMAIL_CONFIG.forEach((key,value)->{
					// mail. 开头的参数才加入到 props
					if(Pattern.matches("^mail\\..+", key)) {
						props.put(key, value);
					}
				});
				//Stdout.pl(props);
				// 处理服务器的账户验证
				Session session = Session.getInstance(props, new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(serverUserName, serverPassword);
					}
				});
				//处理邮件发送方的名称，如果转码错误，则直接显示 发送方邮箱地址
				String senderName = null;
				try {
					senderName = MimeUtility.encodeWord(fromName) + "<" + serverUserName + ">" ;
				}catch(Exception e) {
					senderName = serverUserName;
				}
				//邮件信息对象
				Message message = new MimeMessage(session);
				try {
					message.setFrom(new InternetAddress(senderName));
					message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddr));
					message.setSubject(StrUtil.isEmptyString(subject)?DEFAULT_SUBJECT:subject);
					// 校验字符集是否正确
					if(!StrUtil.isCharsetSupported(charsetName)) {
						throw new Exception("字符集 "+charsetName+"，不被支持，请检查输入是否正确");
					}
					//
					message.setContent(htmlString, "text/html;charset="+charsetName);
					message.setSentDate(new Date());
					message.saveChanges();
				}catch(Exception e) {
					message = null;
					Stdout.pl("构造邮件 Message 对象出错");
					Stdout.pl(e);
				}
				//发送邮件
				try {
					Transport.send(message);
					//如果发送成功，返回 true 值。
					result = true;
					//
					Stdout.pl("发送邮件到对方邮箱("+toEmailAddr+")成功");
					//
				}catch(SendFailedException e1) { 
					Stdout.pl("发送邮件到对方邮箱("+toEmailAddr+")失败1, "+e1.getMessage());
					Stdout.pl(e1);
				}catch (MessagingException e2) {
					Stdout.pl("发送邮件到对方邮箱("+toEmailAddr+")失败2, "+e2.getMessage());
					Stdout.pl(e2);
				}catch(Exception e3) {
					Stdout.pl("发送邮件到对方邮箱("+toEmailAddr+")失败3, "+e3.getMessage());
					Stdout.pl(e3);
				}
			}else {
				Stdout.pl("传入的参数无效, htmlString("+htmlString+"), toEmailAddr("+toEmailAddr+")");
			}
		}
		return result;
	}
	
	/**
	 * 群发邮件的处理方法
	 * @param fromName 发送方的名称。不是邮件地址，是一个简单的名称
	 * @param subject 邮件主题信息
	 * @param htmlString 要发送的内容，html 格式
	 * @param toEmailAddrs 待发送的邮件地址列表
	 * @param charsetName 邮件内容字符集 。比如 GBK, GB18030, UTF-8 之类的
	 * @return 如果发送成功，返回 true ；如果发送失败，返回 false 。
	 */
	public static boolean sendMassEmail(String fromName, String subject, String htmlString, List<String> toEmailAddrs, String charsetName) {
		// 定义一个返回执行结果的变量
		boolean result = false;
		// 获取服务器的账号和密码
		String serverUserName = EmailUtil.EMAIL_CONFIG.get("serverUserName");
		String serverPassword = EmailUtil.EMAIL_CONFIG.get("serverPassword");
		// 
		if(StrUtil.isEmptyString(serverUserName) || StrUtil.isEmptyString(serverPassword)) {
			Stdout.pl("邮件服务的账号或者密码缺失，请检查配置信息");
		}else {
			if(!StrUtil.isEmptyString(htmlString) && !ListUtil.isEmptyList(toEmailAddrs)) {
				//如果传入的参数有效才执行
				Properties props = new Properties();
				EmailUtil.EMAIL_CONFIG.forEach((key,value)->{
					// mail. 开头的参数才加入到 props
					if(Pattern.matches("^mail\\..+", key)) {
						props.put(key, value);
					}
				});
				//Stdout.pl(props);
				// 处理服务器的账户验证
				Session session = Session.getInstance(props, new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(serverUserName, serverPassword);
					}
				});
				//处理邮件发送方的名称，如果转码错误，则直接显示 发送方邮箱地址
				String senderName = null;
				try {
					senderName = MimeUtility.encodeWord(fromName) + "<" + serverUserName + ">" ;
				}catch(Exception e) {
					senderName = serverUserName;
				}
				//邮件信息对象
				Message message = new MimeMessage(session);
				try {
					message.setFrom(new InternetAddress(senderName));
					//
					// message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddr));
					// 群发处理
					InternetAddress[] addrs = new InternetAddress[toEmailAddrs.size()];
					for(int i=0;i<addrs.length;i++) {
						addrs[i] = new InternetAddress(toEmailAddrs.get(i));
					}
					message.setRecipients(Message.RecipientType.TO, addrs);
					//
					message.setSubject(StrUtil.isEmptyString(subject)?DEFAULT_SUBJECT:subject);
					// 校验字符集是否正确
					if(!StrUtil.isCharsetSupported(charsetName)) {
						throw new Exception("字符集 "+charsetName+"，不被支持，请检查输入是否正确");
					}
					//
					message.setContent(htmlString, "text/html;charset="+charsetName);
					message.setSentDate(new Date());
					message.saveChanges();
				}catch(Exception e) {
					message = null;
					Stdout.pl("构造邮件 Message 对象出错");
					Stdout.pl(e);
				}
				//发送邮件
				try {
					Transport.send(message);
					//如果发送成功，返回 true 值。
					result = true;
					//
					Stdout.pl("发送邮件到对方邮箱成功");
					Stdout.pl(toEmailAddrs);
					//
				}catch(SendFailedException e1) { 
					Stdout.pl("发送邮件到对方邮箱失败1, "+e1.getMessage());
					Stdout.pl(toEmailAddrs);
					Stdout.pl(e1);
				}catch (MessagingException e2) {
					Stdout.pl("发送邮件到对方邮箱失败2, "+e2.getMessage());
					Stdout.pl(toEmailAddrs);
					Stdout.pl(e2);
				}catch(Exception e3) {
					Stdout.pl("发送邮件到对方邮箱失败3, "+e3.getMessage());
					Stdout.pl(toEmailAddrs);
					Stdout.pl(e3);
				}
			}else {
				Stdout.pl("传入的参数无效, htmlString("+htmlString+")");
				Stdout.pl("传入的参数无效");
				Stdout.pl(toEmailAddrs);
			}
		}
		return result;
	}
	
	//测试函数
	/*
	public static void main(String[] args) {
		// 方法1
		//Stdout.pl("方法1："+sendEmail("邮件发送测试 方法1", "<h2>请勿回复 方法1</h2>", "pangdongcan@live.com"));
		// 方法2
		//Stdout.pl("方法2："+sendEmail("邮件发送测试 方法2", "<h2>请勿回复 方法2</h2>", "pangdongcan@live.com", "UTF-8"));
		// 方法3
		//Stdout.pl("方法3："
		//+sendEmail("Micheal Pang 测试", "邮件发送测试 方法3", "<h2>请勿回复 方法3</h2>", "pangdongcan@live.com", "UTF-8"));
		//
		//List<String> addrs = Arrays.asList("pangdongcan@live.com", "mickarea.net@gmail.com");
		// 方法4
		//Stdout.pl("方法4："
		//+sendMassEmail("Pang Dong Can 测试", "邮件发送测试（群发） 方法4", "<h2>请勿回复 方法4</h2>", addrs, "UTF-8"));
		//
		//Stdout.pl("方法 5："+sendEmail(null, null, "<h2>测试5</h2>", "pangdongcan@live.com", "UTF-8"));
		//
		//List<String> addrs = Arrays.asList("xxx@live.com", "yyy@gmail.com");
		//Stdout.pl("方法6："
		//+sendMassEmail(null, null, "<h2>测试6</h2>", addrs, "UTF-8"));
	}
	*/
	
}
