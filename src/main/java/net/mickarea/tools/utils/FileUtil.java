/******************************************************************************************************

This file "FileUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

/**
 * &gt;&gt;&nbsp;文件读写操作工具类（默认的文件读写字符集为 UTF-8）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年9月27日-2023年10月15日
 */
public final class FileUtil {
	
	/**
	 * &gt;&gt;&nbsp;默认的字符集，UTF-8
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 * 构造函数私有化，防止创建对象
	 */
	private FileUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * &gt;&gt;&nbsp;将字符串内容写入到指定路径的文件
	 * @param text 待写入内容
	 * @param dir 指定的文件夹
	 * @param fileName 指定的文件名
	 * @param isAppend 是否为追加模式。如果为true，内容将以追加形式写入
	 * @param charset 写入文件时的指定字符集
	 * @return 如果程序执行成功，则返回true；否则，返回false。（如果执行过程中有什么异常信息，会以标准方式输出）
	 */
	public static boolean saveToLocalpath(String text, String dir, String fileName, boolean isAppend, String charset) {
		
		boolean result = false;
		
		//判断传入的参数是否有效
		if(StrUtil.isEmptyString(text) || StrUtil.isEmptyString(dir) || StrUtil.isEmptyString(fileName) || StrUtil.isEmptyString(charset)) {
			Stdout.pl("传入的参数有误，参数(text="+text+"), (dir="+dir+"), (fileName="+fileName+"), (charset="+charset+")");
			//直接返回，避免还继续执行。
			return result;
		}
		
		//文件夹处理，如果文件夹不存在，则创建
		File fileDir = new File(dir);
		try {
			if(!fileDir.exists()) {
				fileDir.mkdirs();
			}
		}catch(Exception e) {
			Stdout.pl("文件夹处理异常："+e.getMessage()+"，请检查文件夹(dir="+fileDir.getAbsolutePath()+")");
			Stdout.pl(e);
			//直接返回，避免还继续执行。
			return result;
		}
		
		//目标文件处理，如果文件不存在，则创建
		File fileTarget = new File(dir+File.separator+fileName);
		try {
			if(!fileTarget.exists()) {
				fileTarget.createNewFile();
			}
		}catch(Exception e) {
			Stdout.pl("文件处理异常："+e.getMessage()+"，请检查文件(file="+fileTarget.getAbsolutePath()+")");
			Stdout.pl(e);
			//直接返回，避免还继续执行。
			return result;
		}
		
		//判断文件是否可写
		boolean canWrite = true;
		try {
			canWrite = fileTarget.canWrite();
		}catch(Exception e) {
			canWrite = false;
			Stdout.pl("判断文件(file="+fileTarget.getAbsolutePath()+")是否可写出错，信息："+e.getMessage());
			Stdout.pl(e);
			//直接返回，避免还继续执行。
			return result;
		}
		
		//开始目标文件写入
		if(canWrite) {
			BufferedWriter bw = null;
			OutputStreamWriter outw = null;
			FileOutputStream fout = null;
			try {
				fout = new FileOutputStream(fileTarget, isAppend);
				outw = new OutputStreamWriter(fout, charset);//这里指定了字符集
				bw = new BufferedWriter(outw);
				//写信息
				bw.write(text);
				bw.flush();
				//设置完成标志
				result = true;
				
			} catch (Exception e) {
				Stdout.pl("文件写入失败(file="+fileTarget.getAbsolutePath()+"), 信息："+e.getMessage());
				Stdout.pl(e);
				result = false;
			} finally {
				//文件写入工具，资源回收
				if(bw!=null) {
					try {
						bw.close();
					} catch (Exception e1) {
					}
					bw = null;
				}
				if(outw!=null) {
					try {
						outw.close();
					} catch (Exception e2) {
					}
					outw = null;
				}
				if(fout!=null) {
					try {
						fout.close();
					} catch (Exception e3) {
					}
					fout = null;
				}
			}
		} else {
			Stdout.pl("文件无法写入(file="+fileTarget.getAbsolutePath()+")，请检查。");
			result = false;
		}
		
		//最终返回结果
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;将字符串内容写入到指定路径的文件（可配置是否以追加形式写入）
	 * @param text 待写入内容
	 * @param dir 指定的文件夹
	 * @param fileName 指定的文件名
	 * @param isAppend 是否为追加模式。如果为true，内容将以追加形式写入
	 * @return 如果程序执行成功，则返回true；否则，返回false。（如果执行过程中有什么异常信息，会以标准方式输出）
	 */
	public static boolean saveToLocalpath(String text, String dir, String fileName, boolean isAppend) {
		return saveToLocalpath(text, dir, fileName, isAppend, DEFAULT_CHARSET);
	}
	
	/**
	 * &gt;&gt;&nbsp;将字符串内容写入到指定路径的文件
	 * @param text 待写入内容
	 * @param dir 指定的文件夹
	 * @param fileName 指定的文件名
	 * @return 如果程序执行成功，则返回true；否则，返回false。（如果执行过程中有什么异常信息，会以标准方式输出）
	 */
	public static boolean saveToLocalpath(String text, String dir, String fileName) {
		return saveToLocalpath(text, dir, fileName, false, DEFAULT_CHARSET);
	}
	
	/**
	 * &gt;&gt;&nbsp;将字符串内容写入到指定路径的文件
	 * @param text 待写入内容
	 * @param dir 指定的文件夹
	 * @param fileName 指定的文件名
	 * @param charset 写入文件时的指定字符集
	 * @return 如果程序执行成功，则返回true；否则，返回false。（如果执行过程中有什么异常信息，会以标准方式输出）
	 */
	public static boolean saveToLocalpath(String text, String dir, String fileName, String charset) {
		return saveToLocalpath(text, dir, fileName, false, charset);
	}
	
	/**
	 * &gt;&gt;&nbsp;从指定的文件路径中，读取对应的文件内容（以指定的字符集），并以string返回。
	 * @param dir 文件所在的文件夹路径
	 * @param filename 要读取的文件名
	 * @param keepLineSeparator 是否保留换行符。true 保留，false 不保留。如果不保留，文字将以一行内容返回。
	 * @param charset 文件的字符集。主要用于处理非英文字符文件。比如：文件内容的字符集为 GBK ，则该参数需要设置为 GBK，否则返回内容可能乱码。
	 * @return 文件内容<p>如果，执行报错，则为 null 值；执行成功，为长度大于等于0的字符串。</p>
	 */
	public static String loadStringFromFile(String dir, String filename, boolean keepLineSeparator,String charset) {
		String re = "";
		//判断入参是否合法
		if(StrUtil.isEmptyString(dir) || StrUtil.isEmptyString(filename)) {
			Stdout.pl("传入的参数异常：(dir="+dir+"), (filename="+filename+")");
			re = null;
			return re;
		}
		//拼接文件路径，创建文件对象
		String path = dir + File.separator + filename;
		File file = new File(path);
		//判断文件是否可读写
		boolean canRead = false;
		try {
			canRead = file.exists() && file.isFile() && file.canRead();
		}catch(Exception e) {
			Stdout.pl("文件处理异常："+e.getMessage()+"，请检查文件(file="+file.getAbsolutePath()+")");
			Stdout.pl(e);
			//直接返回，避免还继续执行。
			re = null;
			return re;
		}
		
		if(canRead) {
			//开始解析
			BufferedReader reader = null;
			InputStreamReader isr = null;
			FileInputStream fis = null;
			try {
				//以一个流来读取字符信息
				fis = new FileInputStream(file);
				isr = new InputStreamReader(fis, charset);
				reader = new BufferedReader(isr);
				
				//由于读取的时候，遇到回车会停止；所以每次读取完毕缓存到一个地方
				StringBuffer buffer = new StringBuffer();
				String tmp = null;
				while((tmp = reader.readLine())!=null) {
					buffer.append(tmp);
					if(keepLineSeparator) {
						buffer.append(SystemUtil.getLineSeparator());
					}
				}
				//将读取的内容一次性输出
				re = buffer.toString();
				re = re==null?"":re;
				
			}catch(Exception e) {
				re = null;
				Stdout.pl(e);
				Stdout.pl("文件："+file.getAbsolutePath()+", 读取解析异常:"+e.getMessage());
			}finally {
				if(reader!=null) {
					try {
						reader.close();
					} catch (Exception e1) {
					}
					reader = null;
				}
				if(isr!=null) {
					try {
						isr.close();
					} catch (Exception e2) {
					}
					isr = null;
				}
				if(fis!=null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
					fis = null;
				}
			}
		} else {
			Stdout.pl("文件(file="+file.getAbsolutePath()+")状态异常，无法读取（可能文件不存在或者文件不允许读取，请检查）");
			re = null;
		}
		
		return re ;
	}
	
	/**
	 * &gt;&gt;&nbsp;从指定的文件路径中，读取对应的文件内容（以UTF-8字符集），并以string返回。
	 * @param dir 文件所在的文件夹路径
	 * @param filename 要读取的文件名
	 * @param keepLineSeparator 是否保留换行符。true 保留，false 不保留。如果不保留，文字将以一行内容返回。
	 * @return 文件内容<p>如果，执行报错，则为 null 值；执行成功，为长度大于等于0的字符串。</p>
	 */
	public static String loadStringFromFile(String dir, String filename, boolean keepLineSeparator) {
		return loadStringFromFile(dir, filename, keepLineSeparator, DEFAULT_CHARSET);
	}
	
	/**
	 * &gt;&gt;&nbsp;从指定的文件路径中，读取对应的文件内容（以指定的字符集，不保留换行符），并以string返回。
	 * @param dir 文件所在的文件夹路径
	 * @param filename 要读取的文件名
	 * @param charset 文件的字符集。主要用于处理非英文字符文件。比如：文件内容的字符集为 GBK ，则该参数需要设置为 GBK，否则返回内容可能乱码。
	 * @return 文件内容<p>如果，执行报错，则为 null 值；执行成功，为长度大于等于0的字符串。</p>
	 */
	public static String loadStringFromFile(String dir, String filename, String charset) {
		return loadStringFromFile(dir, filename, false, charset);
	}
	
	/**
	 * &gt;&gt;&nbsp;从指定的文件路径中，读取对应的文件内容（以UTF-8字符集，不保留换行符），并以string返回。
	 * @param dir 文件所在的文件夹路径
	 * @param filename 要读取的文件名
	 * @return <p>该函数不保留换行符，文件内容将以一行的形式返回</p>
	 * <p>如果，执行报错，则为 null 值；执行成功，为长度大于等于0的字符串。</p>
	 */
	public static String loadStringFromFile(String dir, String filename) {
		return loadStringFromFile(dir, filename, false, DEFAULT_CHARSET);
	}
	
	/**
	 * &gt;&gt;&nbsp;查找出当前操作系统用户的桌面目录地址(绝对路径)。如果没有桌面目录，则返回当前操作系统用户的主目录
	 * @return 桌面目录地址(绝对路径)
	 */
	public static String findUserDesktopDir() {
		//一开始赋予默认值（用户的主目录）
		String result = SystemUtil.getUserHome();
		File homeDir = null ;
		try{
			homeDir = new File(result);
		}catch(Exception e) {
			Stdout.pl("用户主目录获取异常，无法创建File对象(file="+result+")");
		}
		//查找桌面目录
		if(homeDir!=null && homeDir.exists() && homeDir.isDirectory()) {
			String[] desktopNameArray = homeDir.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					boolean re = false;
					//找出桌面目录
					if(!StrUtil.isEmptyString(name) && Pattern.matches("^desktop$", name.toLowerCase())) {
						re = true;
					}
					return re;
				}
			});
			//如果找到了桌面目录，则重新赋值
			if(desktopNameArray!=null && desktopNameArray.length>0) {
				result = result + File.separator + desktopNameArray[0];
			}
		}
		//返回结果
		return StrUtil.isEmptyString(result)?"":result;
	}
	
	//测试函数
	public static void main(String[] args) {
		
		//获取桌面路径
		String desktop = FileUtil.findUserDesktopDir();
		//要写入的内容
		//String str = "這是一個神奇的世界，到處充滿了危機。天吶！！！";
		String str = "這是一個神奇的世界，到處充滿了危機。天吶！！！        this is a test string. please dont't delete it.";
		
		Stdout.fpl("待测字符串字符长度(%s), 字节长度(%s)", str.length(), str.getBytes().length);
		
		//读写测试
		if(!saveToLocalpath(str, desktop, "test_1.txt", "BIG5")) {
			Stdout.pl("test_1.txt 写入失败");
		}else {
			String s1 = loadStringFromFile(desktop, "test_1.txt", "BIG5");
			if(s1!=null) {
				Stdout.pl(s1);
			}else {
				Stdout.pl("文件读取异常,test_1.txt");
			}
		}
		//只读测试（保留换行符）
		String s2 = loadStringFromFile(desktop, "test_2.txt", true, "BIG5");
		if(s2!=null) {
			Stdout.pl(s2);
		}else {
			Stdout.pl("文件读取异常,test_2.txt");
		}
		//只读测试（一行返回）
		String s3 = loadStringFromFile(desktop, "test_2.txt", "BIG5");
		if(s3!=null) {
			Stdout.pl(s3);
		}else {
			Stdout.pl("文件读取异常,test_2.txt");
		}
		
		//测试默认处理
		if(!saveToLocalpath("我是一个神奇的人类，but they were shit.", desktop, "test_3.txt")) {
			Stdout.pl("test_3.txt 写入失败");
		}else {
			String s4 = loadStringFromFile(desktop, "test_3.txt");
			if(s4!=null) {
				Stdout.pl(s4);
			}else {
				Stdout.pl("文件读取异常,test_3.txt");
			}
		}
		
	}
	
}
