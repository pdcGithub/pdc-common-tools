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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 文件读写操作工具类（默认的文件读写字符集为 UTF-8）
 * @author Michael Pang (Dongcan Pang)
 * @since 2023年9月27日-2026年10月08日
 */
public final class FileUtil {
	
	/**
	 * 默认的字符集，UTF-8
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 * 构造函数私有化，防止创建对象
	 */
	private FileUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 将字符串内容写入到指定路径的文件
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
	 * 将字符串内容写入到指定路径的文件（可配置是否以追加形式写入）
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
	 * 将字符串内容写入到指定路径的文件
	 * @param text 待写入内容
	 * @param dir 指定的文件夹
	 * @param fileName 指定的文件名
	 * @return 如果程序执行成功，则返回true；否则，返回false。（如果执行过程中有什么异常信息，会以标准方式输出）
	 */
	public static boolean saveToLocalpath(String text, String dir, String fileName) {
		return saveToLocalpath(text, dir, fileName, false, DEFAULT_CHARSET);
	}
	
	/**
	 * 将字符串内容写入到指定路径的文件
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
	 * 从指定的文件路径中，读取对应的文件内容（以指定的字符集），并以string返回。
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
	 * 从指定的文件路径中，读取对应的文件内容（以UTF-8字符集），并以string返回。
	 * @param dir 文件所在的文件夹路径
	 * @param filename 要读取的文件名
	 * @param keepLineSeparator 是否保留换行符。true 保留，false 不保留。如果不保留，文字将以一行内容返回。
	 * @return 文件内容<p>如果，执行报错，则为 null 值；执行成功，为长度大于等于0的字符串。</p>
	 */
	public static String loadStringFromFile(String dir, String filename, boolean keepLineSeparator) {
		return loadStringFromFile(dir, filename, keepLineSeparator, DEFAULT_CHARSET);
	}
	
	/**
	 * 从指定的文件路径中，读取对应的文件内容（以指定的字符集，不保留换行符），并以string返回。
	 * @param dir 文件所在的文件夹路径
	 * @param filename 要读取的文件名
	 * @param charset 文件的字符集。主要用于处理非英文字符文件。比如：文件内容的字符集为 GBK ，则该参数需要设置为 GBK，否则返回内容可能乱码。
	 * @return 文件内容<p>如果，执行报错，则为 null 值；执行成功，为长度大于等于0的字符串。</p>
	 */
	public static String loadStringFromFile(String dir, String filename, String charset) {
		return loadStringFromFile(dir, filename, false, charset);
	}
	
	/**
	 * 从指定的文件路径中，读取对应的文件内容（以UTF-8字符集，不保留换行符），并以string返回。
	 * @param dir 文件所在的文件夹路径
	 * @param filename 要读取的文件名
	 * @return <p>该函数不保留换行符，文件内容将以一行的形式返回</p>
	 * <p>如果，执行报错，则为 null 值；执行成功，为长度大于等于0的字符串。</p>
	 */
	public static String loadStringFromFile(String dir, String filename) {
		return loadStringFromFile(dir, filename, false, DEFAULT_CHARSET);
	}
	
	/**
	 * 查找出当前操作系统用户的桌面目录地址(绝对路径)。如果没有桌面目录，则返回当前操作系统用户的主目录
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
	
	/**
	 * 将给定地址的文件，以字节数组的方式返回。
	 * @param filePath 文件地址
	 * @return 字节数组形式的文件对象。如果文件异常（比如：文件不存在、无法读取、流处理报错等），则返回 null
	 */
	public static byte[] loadByteArrayFromFile(String filePath) {
		//
		byte[] re = null;
		//
		if(filePath==null) {
			Stdout.pl("读取文件异常，文件路径为 null：");
			return re;
		}
		//创建文件对象，判断文件是否存在
		File targetFile = new File(filePath);
		if(!targetFile.exists()) {
			Stdout.fpl("读取文件异常，文件[%s]不存在：", filePath);
			return re;
		}
		//判断文件是否可读
		if(!targetFile.canRead()) {
			Stdout.fpl("读取文件异常，文件[%s]无法读取：", filePath);
			return re;
		}
		// 开始处理
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		try {
			//将文件识别为 文件流
			fis = new FileInputStream(filePath);
			//初始化 输出用的字节流
			baos = new ByteArrayOutputStream();
			//设置缓冲区
			int buffSize = 1024*8;
			int len = 0;
			byte[] buffer = new byte[buffSize];
			//开始读写
			while((len=fis.read(buffer))!=-1) {
				baos.write(buffer, 0 , len);
			}
			//结果转化
			re = baos.toByteArray();
		} catch (Exception e) {
			Stdout.fpl("读取文件[%s]为 ByteBuffer 时发生异常。", filePath);
			Stdout.pl(e);
		} finally {
			if(baos!=null) {
				try {baos.close(); } catch (Exception e1) { }
				baos=null;
			}
			if(fis!=null) {
				try {fis.close(); } catch (Exception e2) { }
				fis=null;
			}
		}
		
		//
		return re;
	}
	
	/**
	 * 将给定地址的文件，以 ByteBuffer 的方式返回。
	 * @param filePath 文件地址
	 * @return ByteBuffer 对象。如果文件异常（比如：文件不存在、无法读取、流处理报错等），则返回 null
	 */
	public static ByteBuffer loadByteBufferFromFile(String filePath) {
		ByteBuffer re = null;
		byte[] byteArray = loadByteArrayFromFile(filePath);
		if(byteArray!=null) {
			//初始化一个同样大小的缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(byteArray.length);
			// 放入缓冲区
			buffer.put(byteArray);
			// 充值缓冲区
			buffer.flip();
			//
			re = buffer;
		}
		return re;
	}
	
	/**
	 * 根据本地路径，一层层递归搜索，整个路径下的所有文件的绝对路径信息。（这里只筛选文件，不做排序处理）
	 * <p>对于 windows 系统，它返回的文件路径可能是这样的：C:\Users\pdc\Downloads\test.txt</p>
	 * <p>对于 linux 系统，它返回的文件路径可能是这样的：/home/pdc/Downloads/test.txt</p>
	 * @param directory 文件夹目录对象
	 * @param recursive 是否递归搜索子文件夹。true 则递归搜索，false 则只搜索当前传入的文件夹
	 * @param filter 要搜索的文件名过滤器。建议使用 本框架自带的 FileNameFilter 类，作为实现。
	 * @return 一个文件信息列表。如果传入的参数不是文件夹，或者文件夹没有文件，则返回空列表。空列表指的是：null 或者 长度为0
	 */
	public static final List<String> searchFilesFromLocalDir(File directory, boolean recursive, FilenameFilter filter) {
		
		// 定义一个返回结果
		List<String> result = null;
		
		// 如果 文件夹 对象信息为空，则不处理。
		// 如果 不是文件夹也不处理
		if(directory==null || !directory.isDirectory()) return result;
		
		// 先初始化
		result = new ArrayList<String>();
		
		// 首先搜索 传入的目录（这里如果有设置过滤器，则过滤；没有则不过滤）
		File[] currFiles = filter==null ? directory.listFiles() : directory.listFiles(filter);
		for(int i=0;i<currFiles.length;i++) {
			
			// 如果是文件，插入列表，并跳过后续处理
			if(currFiles[i].isFile()) {
				// 放入结果列表
				result.add(currFiles[i].getAbsolutePath());
				// 结束本次循环
				continue;
			}
			
			// 如果是文件夹，并且配置了递归搜索，则递归搜索
			// 否则，不递归搜索
			if(recursive && currFiles[i].isDirectory()) {
				// 递归得到一个文件路径列表
				List<String> recursiveFiles = FileUtil.searchFilesFromLocalDir(currFiles[i], recursive, filter);
				// 如果有内容，则放入结果列表，否则不放入
				if(!ListUtil.isEmptyList(recursiveFiles)) result.addAll(recursiveFiles);
				// 结束本次循环
				continue;
			}
			
			// 其它不用处理
		}
		
		// 返回
		return result;
	}
	
	/**
	 * 根据正则规则，检索Jar文件内部，名字符合要求的文件。（这里只筛选文件，不做排序处理）
	 * Jar 文件实际上是一个压缩包。它的文件检索，会先解包全部内容，后搜索。所以，它不需要递归处理。
	 * <p>对于 Jar 的搜索结果，假设搜索 class文件，结果可能是这样的字符串：com/microsoft/sqlserver/jdbc/SessionStateValue.class</p>
	 * @param file 这是 Jar 文件对象。它实际上是一个 zip 压缩文件。
	 * @param ignoreCase 文件名字符串匹配时，是否忽略字母大小写。
	 * @param regexpStr 检索文件名用的正则表达式字符串
	 * @return 一个文件信息列表。如果传入的参数异常，则返回空列表。空列表指的是：null 或者 长度为0
	 */
	public static final List<String> searchFilesFromJar(JarFile file, boolean ignoreCase, String regexpStr) {
		
		// 首先定义一个返回结果
		List<String> result = null;
		
		// 如果参数不对，则直接返回 null
		if(file==null || StrUtil.isEmptyString(regexpStr)) return result;
		
		// 把 jar 包内部的信息转为 List 对象。
		// 对于 jar 它会 完整解包
		List<JarEntry> jarEntryies = ListUtil.makeEnumerationObjectToListObject(file.entries());
		
		// 通过流处理，过滤出需要搜索的文件夹
		// 前提，jarEntryies 不是空列表。如果 file.entries() 没有内容，则 List 是 null
		if(!ListUtil.isEmptyList(jarEntryies)) {
			// 因为 jar 是解包搜索，所以遍历一次就行了
			result = jarEntryies.parallelStream() // 这是并行流，加速处理
								.filter(jarEntry->{
									// 首先做正则校验
									boolean regexpResult = ignoreCase ? PatternUtil.matches(regexpStr, jarEntry.getName(), Pattern.CASE_INSENSITIVE) : PatternUtil.matches(regexpStr, jarEntry.getName());
									// 返回筛选结果
									// 由于 jar 是解包搜索，所以不需要递归，也就不需要文件夹内容
									return regexpResult && !jarEntry.isDirectory();
								})
								.map(jarEntry->{
									// 转换 jarEntry 对象 为 路径字符串
									return jarEntry.getName();
								}) // （因为排序、去重，可能影响性能，所以外部使用时，再自己处理）
								.collect(Collectors.toList()); // 最后，收集为字符串列表
		}
		
		// 返回
		return result;
	}
	
}
