/******************************************************************************************************

This file "ByteTester.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.filetest;

import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mickarea.tools.utils.PatternUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * 不知道要测试些什么
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月24日
 */
public class ByteTester {

	public static void main(String[] args) throws Exception {
		
		String filePath1 = "D:\\照片和视频\\照片\\DSC00010.JPG";
		String filePath2 = "D:\\test_tmpfile\\RB868RIT31TU2F06RN.tmp";

		FileInputStream f1 = new FileInputStream(filePath1);
		FileInputStream f2 = new FileInputStream(filePath2);
		
		byte[] buffer1 = new byte[1024];
		byte[] buffer2 = new byte[1024];
		
		f1.read(buffer1);
		f2.read(buffer2);
		
		f1.close();
		f2.close();
		Stdout.pl("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		String s1 = new String(buffer1, "ISO-8859-1"); 
		Stdout.pl(s1);
		Stdout.pl("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		String s2 = new String(buffer2, "ISO-8859-1");
		Stdout.pl(s2);
		Stdout.pl("############################################################");
		
		Pattern p1 = Pattern.compile(PatternUtil.UPLOAD_FILE_HEADER);
		Matcher m1 = p1.matcher(s2);
		if(m1.find()) {
			int header = m1.group().length();
			Stdout.pl("最终答案");
			Stdout.pl(s2.substring(header));
		}
		
		Pattern p2 = Pattern.compile(PatternUtil.UPLOAD_FILE_NAME);
		Matcher m2 = p2.matcher(s2);
		if(m2.find()) {
			String filename = m2.group().split("\"")[1];
			Stdout.pl(filename);
			String[] suffixArr = filename.split("\\.");
			Stdout.pl(suffixArr[suffixArr.length-1]);
		}
		
	}

}
