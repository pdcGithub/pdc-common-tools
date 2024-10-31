/******************************************************************************************************

This file "TmpDataGramAnalyzer.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import net.mickarea.tools.utils.ArrayUtil;
import net.mickarea.tools.utils.PatternUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * &gt;&gt;&nbsp;这是一个 Servlet Input Stream 请求报文解析器。适合于报文只包含一个文件字节流的情况
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月26日-2023年4月27日
 */
public class TmpDataGramAnalyzer {
	
	/**
	 * &gt;&gt;&nbsp;要解析的临时文件名
	 */
	private File tmpFile;
	
	/**
	 * &gt;&gt;&nbsp;解析文件后，将要传送到的目标文件夹
	 */
	private String targetDir;
	
	/**
	 * &gt;&gt;&nbsp;文件的真实文件名，以 UTF-8 方式储存
	 */
	private String RealFileName;
	
	/**
	 * &gt;&gt;&nbsp;临时文件读取流
	 */
	private FileInputStream in;
	
	/**
	 * &gt;&gt;&nbsp;目标文件写入流
	 */
	private FileOutputStream out;
	
	/**
	 * &gt;&gt;&nbsp;构造函数
	 * @param tmpFilePath 临时文件全路径
	 * @param targetDir 目标文件夹
	 */
	public TmpDataGramAnalyzer(String tmpFilePath, String targetDir) {
		this.tmpFile = new File(tmpFilePath);
		this.targetDir = targetDir;
	}
	
	/**
	 * &gt;&gt;&nbsp;分析临时文件，提取真正的上传内容到目标文件夹
	 * @return 执行结果信息，如果是成功，则为 ok开头字符串，如果是 失败则为 error 开头字符串
	 */
	public String analyze() {
		String re = "ok";
		try {
			//开始文件解析
			long pageSize = 1024;
			long length = this.tmpFile.length();
			long pageCount = length/pageSize + ((length%pageSize)!=0?1:0);
			Stdout.fpl("临时文件大小 %s 字节，分页大小 %s 字节，总页数：%s 页", length, pageSize, pageCount);
			
			if(pageCount<=0) {
				throw new Exception("临时报文信息文件为空，请检查："+this.tmpFile.getAbsolutePath());
			}
			
			//首先读取第一页，获取上传文件的具体信息
			this.in = new FileInputStream(this.tmpFile);
			byte[] buffer = new byte[(int)pageSize];
			int firstPageLength = this.in.read(buffer);
			
			//获取报文信息，以字符信息来匹配
			String firstPageContent = new String(buffer, 0, firstPageLength, Charset.forName("ISO-8859-1"));
			String header = PatternUtil.getUploadFileHeader(firstPageContent);
			if(header.length()<=0) {
				throw new Exception("临时报文头部信息提取失败，无法继续执行。");
			}
			
			//提取文件真实名字，后缀名，以及可能的目标文件名
			this.RealFileName = new String(PatternUtil.getUploadFileRealName(header).getBytes("ISO-8859-1"), "UTF-8");
			String suffixName = PatternUtil.getFileSuffixName(this.RealFileName);
			String targetName = this.tmpFile.getName().split("\\.")[0]+(suffixName.length()>0?"."+suffixName:"");
			
			//如果真的有上传文件，就开始正式读取和写入
			if(this.RealFileName.length()>0) {
				this.out = new FileOutputStream(this.targetDir+File.separator+targetName);
				//开始处理数据
				for(int page=1;page<=pageCount;page++) {
					if(page==1 && pageCount!=2) {
						//第一页数据写入（如果数据只有2页，则不写入第一页内容，执行1、2两页内容合并，然后解析）
						if(pageCount==1) {
							//对于只有一页数据，这里需要判断尾部内容，然后再写入
							String tail = PatternUtil.getUploadFileTail(firstPageContent);
							this.out.write(buffer, header.length(), firstPageLength-tail.length()-header.length());
						} else {
							this.out.write(buffer, header.length(), firstPageLength-header.length());
						}
					} else if(page==pageCount-1) {
						//倒数第2页，与最后一页合并，解析尾部内容，并写入
						if(pageCount>2) {
							//加个条件，如果是只有2页，则 buffer 不直接读取数据，因为倒数第2页，就是第1页
							this.in.read(buffer);
						}else {
							//如果是只有2页，第一页是包含了头部信息，要去除，否则会多写一些内容
							buffer = ArrayUtil.subBytes(buffer, header.length(), buffer.length);
						}
						byte[] lastPageBuffer = new byte[(int)pageSize];
						int lastPageLength = this.in.read(lastPageBuffer);
						byte[] allBuffer = ArrayUtil.mergeBytes(buffer, ArrayUtil.subBytes(lastPageBuffer, lastPageLength));
						String all = new String(allBuffer, 0, allBuffer.length, Charset.forName("ISO-8859-1"));
						String tail = PatternUtil.getUploadFileTail(all);
						this.out.write(allBuffer, 0, allBuffer.length-tail.length());
					} else if(page!=pageCount) {
						//中间页数的数据是满缓存区的，可以直接写入
						this.in.read(buffer);
						this.out.write(buffer, 0, buffer.length);
					}
				}
			}
		} catch (Exception e) {
			re = "error: "+e.getMessage();
			Stdout.pl(e);
		} finally {
			//释放资源，主要是 I/O 流对象
			if(this.out!=null) {
				try {
					this.out.close();
					Stdout.pl("out 资源释放成功");
				}catch(Exception e1) {
					this.out = null;
					Stdout.pl("out 资源释放失败");
				}
			}
			if(this.in!=null) {
				try {
					this.in.close();
					Stdout.pl("in 资源释放成功");
				} catch (IOException e2) {
					this.in = null;
					Stdout.pl("in 资源释放失败");
				}
			}
		}
		return re;
		
	}
	
	//getters
	public File getTmpFile() {
		return tmpFile;
	}
	public String getTargetDir() {
		return targetDir;
	}
	public String getRealFileName() {
		return RealFileName;
	}

}
