/******************************************************************************************************

This file "ImageUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;

/**
 * &gt;&gt;&nbsp;一个关于图片处理的工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月5日-2024年1月23日
 */
public final class ImageUtil {

	/**
	 * &gt;&gt;&nbsp;私有构造函数
	 */
	private ImageUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * &gt;&gt;&nbsp;默认字体，黑体--加粗--20号
	 */
	public static Font DEFAULT_FONT = new Font("SimHei", Font.BOLD, 20);
	
	/**
	 * &gt;&gt;&nbsp;默认画笔颜色
	 */
	public static Color DEFAULT_COLOR = Color.BLACK;
	/**
	 * &gt;&gt;&nbsp;默认背景颜色
	 */
	public static Color DEFAULT_BG_COLOR = new Color(220,220,220);
	
	/**
	 * &gt;&gt;&nbsp;默认图片大小
	 */
	public static int DEFAULT_IMG_WIDTH = 100;
	public static int DEFAULT_IMG_HEIGHT = 40;
	
	/**
	 * &gt;&gt;&nbsp;默认绘制开始位置
	 */
	public static int DEFAULT_X = 25;
	public static int DEFAULT_Y = 25;
	
	/**
	 * &gt;&gt;&nbsp;根据传来的字符，生成一张包含该字符的图片
	 * @param words 用于显示的字符，可以是中文或者其它文字。
	 * @param imgWidth 生成的图片宽度，以像素为单位
	 * @param imgHeight 生成的图片高度，以像素为单位
	 * @param startX 绘制的文字的起始坐标x
	 * @param startY 绘制的文字的起始坐标y
	 * @param bgColor 绘制的图片的背景色
	 * @param fontColor 用于显示的文字颜色
	 * @param fontStyle 用于显示的文字字体类型
	 * @return 一个图片对象
	 * @throws Exception 在绘制时有一定可能性发生异常
	 */
	public static BufferedImage createStringImage(String words, int imgWidth, int imgHeight, int startX, int startY, Color bgColor, Color fontColor, Font fontStyle) throws Exception {
		BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		//设置文字抗锯齿
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//设置图形抗锯齿
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//先填充一个背景出来
		g2d.setColor(bgColor);
		g2d.fillRect(0, 0, imgWidth, imgHeight);
		
		g2d.setColor(fontColor);
		g2d.setFont(fontStyle);
		//当有文字的时候才绘制
		if(!StrUtil.isEmptyString(words)) {
			g2d.drawString(words, startX, startY);
		}
		return bufferedImage;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传来的字符，生成一张包含该字符的图片（这个是默认方法，将得到一个默认的图片对象）
	 * @param words 传来的字符串
	 * @return 图片对象
	 * @throws Exception 在绘制时有一定可能性发生异常
	 */
	public static BufferedImage createStringImage(String words) throws Exception {
		return createStringImage(words, DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT, DEFAULT_X, DEFAULT_Y, DEFAULT_BG_COLOR, DEFAULT_COLOR, DEFAULT_FONT);
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传来的字符，生成一张包含该字符的图片
	 * @param words 用于显示的字符，可以是中文或者其它文字。
	 * @param imgWidth 生成的图片宽度，以像素为单位
	 * @param imgHeight 生成的图片高度，以像素为单位
	 * @param startX 绘制的文字的起始坐标x
	 * @param startY 绘制的文字的起始坐标y
	 * @return 一个图片对象
	 * @throws Exception 在绘制时有一定可能性发生异常
	 */
	public static BufferedImage createStringImage(String words, int imgWidth, int imgHeight, int startX, int startY) throws Exception {
		return createStringImage(words, imgWidth, imgHeight, startX, startY, DEFAULT_BG_COLOR, DEFAULT_COLOR, DEFAULT_FONT);
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传来的字符，生成一张包含该字符的图片
	 * @param words 用于显示的字符，可以是中文或者其它文字。
	 * @param imgWidth 生成的图片宽度，以像素为单位
	 * @param imgHeight 生成的图片高度，以像素为单位
	 * @param startX 绘制的文字的起始坐标x
	 * @param startY 绘制的文字的起始坐标y
	 * @param bgColor 设置绘制的图片背景色
	 * @return 一个图片对象
	 * @throws Exception 在绘制时有一定可能性发生异常
	 */
	public static BufferedImage createStringImage(String words, int imgWidth, int imgHeight, int startX, int startY, Color bgColor) throws Exception {
		return createStringImage(words, imgWidth, imgHeight, startX, startY, bgColor, DEFAULT_COLOR, DEFAULT_FONT);
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传来的字符，生成一张包含该字符的图片（此函数有背景图参数需要传递，根据图片高度和宽度，可拉伸）
	 * @param words 用于显示的字符，可以是中文或者其它文字。
	 * @param imgWidth 生成的图片宽度，以像素为单位
	 * @param imgHeight 生成的图片高度，以像素为单位
	 * @param startX 绘制的文字的起始坐标x
	 * @param startY 绘制的文字的起始坐标y
	 * @param bgImg 背景图片
	 * @param fontColor 用于显示的文字颜色
	 * @param fontStyle 用于显示的文字字体类型
	 * @return 一个图片对象
	 * @throws Exception 在绘制时有一定可能性发生异常
	 */
	public static BufferedImage createStringImage(String words, int imgWidth, int imgHeight, int startX, int startY, BufferedImage bgImg, Color fontColor, Font fontStyle) throws Exception {
		BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		//设置文字抗锯齿
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//设置图形抗锯齿
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//把背景图片添加进来（如果图片不为空值的话）
		if(bgImg!=null) {
			g2d.drawImage(bgImg, 0, 0, imgWidth, imgHeight, null);
		}
		
		g2d.setColor(fontColor);
		g2d.setFont(fontStyle);
		//当有文字的时候才绘制
		if(!StrUtil.isEmptyString(words)) {
			g2d.drawString(words, startX, startY);
		}
		return bufferedImage;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传来的字符，生成一张包含该字符的图片（此函数有背景图参数需要传递，按照背景图的大小绘制）
	 * @param words 用于显示的字符，可以是中文或者其它文字。
	 * @param startX 绘制的文字的起始坐标x
	 * @param startY 绘制的文字的起始坐标y
	 * @param bgImg 背景图片
	 * @param fontColor 用于显示的文字颜色
	 * @param fontStyle 用于显示的文字字体类型
	 * @return 一个图片对象
	 * @throws Exception 在绘制时有一定可能性发生异常
	 */
	public static BufferedImage createStringImage(String words, int startX, int startY, BufferedImage bgImg, Color fontColor, Font fontStyle) throws Exception {
		
		//根据背景图片的大小绘制
		int bgWidth = bgImg.getWidth();
		int bgHeight = bgImg.getHeight();
		
		return createStringImage(words, bgWidth, bgHeight, startX, startY, bgImg, fontColor, fontStyle);
	}
	
	/**
	 * &gt;&gt;&nbsp;根据设置的最大宽度和最大高度，对图片对象进行缩放。
	 * @param oriImg 待处理的图像对象
	 * @param maxWidth 缩放后的最大宽度
	 * @param maxHeight 缩放后的最大高度
	 * @return BufferedImage 类型的图像对象
	 * @throws Exception 如果执行出错，返回异常
	 */
	public static BufferedImage getScaledImage(BufferedImage oriImg, int maxWidth, int maxHeight) throws Exception {
		BufferedImage resultImg = null;
		//先判断参数有效性
		if(oriImg==null || maxWidth<=0 || maxHeight <=0) {
			throw new IllegalArgumentException(
					Stdout.fplToAnyWhere("方法传入的参数异常，(oriImg=%s, maxWidth=%s, maxHeight=%s)", oriImg, maxWidth, maxHeight));
		}
		//获取图片的宽度和高度
		int oriWidth = oriImg.getWidth();
		int oriHeight = oriImg.getHeight();
		//新的 宽度 和 高度
		int newWidth = 0;
		int newHeight = 0;
		//根据最大宽度 和 最大高度 判断是否需要缩放
		if(oriWidth<=maxWidth && oriHeight<=maxHeight) {
			//不缩放，直接返回原对象
			resultImg =  oriImg;
		}else {
			//根据最大宽度缩放，计算出可能的高度为多少
			newWidth = maxWidth;
			newHeight = oriHeight*newWidth/oriWidth;
			//如果计算结果大于最大高度，则重新按高度缩放
			if(newHeight>maxHeight) {
				newHeight = maxHeight;
				newWidth = newHeight*oriWidth/oriHeight;
			}
			//根据计算出的高度和宽度，缩放图片
			Image newImg = oriImg.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			//创建一个新的 BufferedImage 对象用于返回结果
			resultImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
			//构建2D图形处理对象，将缩放的图像绘制到新的 BufferedImage 对象
			Graphics g = resultImg.getGraphics();
			g.drawImage(newImg, 0, 0, null);
		}
		//返回结果
		return resultImg;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据设置的源文件路径、最大宽度、最大高度，对图片对象进行缩放。并将缩放结果保存到新路径。
	 * @param oriPath 源图片路径
	 * @param tarPath 生成图片路径
	 * @param maxWidth 最大宽度
	 * @param maxHeight 最大高度
	 * @throws Exception 如果执行出错，返回异常
	 */
	public static void getScaledImage(String oriPath, String tarPath, int maxWidth, int maxHeight) throws Exception {
		//先判断参数有效性
		if(StrUtil.isEmptyString(oriPath) || StrUtil.isEmptyString(tarPath) || maxWidth<=0 || maxHeight <=0) {
			throw new IllegalArgumentException(
					Stdout.fplToAnyWhere("方法传入的参数异常，(oriPath=%s, tarPath=%s, maxWidth=%s, maxHeight=%s)", 
							oriPath, tarPath, maxWidth, maxHeight));
		}
		//开始处理
		File oriFile = new File(oriPath);
		if(oriFile.exists() && oriFile.isFile()) {
			int lastIndex = oriFile.getName().lastIndexOf(".");
			if(lastIndex==-1) {
				throw new Exception(Stdout.fplToAnyWhere("源图片文件缺少后缀名，fileName=%s", oriFile.getName()));
			}
			String suffix = oriFile.getName().substring(lastIndex+1);
			if(StrUtil.isEmptyString(suffix)) {
				throw new Exception(Stdout.fplToAnyWhere("源图片文件后缀名异常，suffix=%s", suffix));
			}
			BufferedImage oriImg = ImageIO.read(oriFile);
			BufferedImage tarImg = ImageUtil.getScaledImage(oriImg, maxWidth, maxHeight);
			//保存到目标路径
			boolean result = ImageIO.write(tarImg, suffix, new File(tarPath));
			if(!result) {
				throw new Exception("图像保存失败，请检查。");
			}
		}else {
			throw new FileNotFoundException(Stdout.fplToAnyWhere("图片路径不正确，无法找到有效文件，路径：%s",oriPath));
		}
	}
	
	/**
	 * &gt;&gt;&nbsp;测试函数
	 * @param args
	 * @throws Exception 
	 */
	/*
	public static void main(String[] args) throws Exception {
		
		//绘制一个有背景颜色的图片
		//BufferedImage myImg = createStringImage("你好,HELLO");
		//ImageIO.write(myImg, "PNG", new File(FileUtil.findUserDesktopDir()+File.separator+"test.png"));
		
		//绘制一个有背景图片的图片
		//BufferedImage bgImg = ImageIO.read(new File(FileUtil.findUserDesktopDir()+File.separator+"bg.png"));
		//BufferedImage myImg2 = createStringImage("你好呀，哈哈哈哈哈哈哈", 40, 40, myImg, DEFAULT_COLOR, DEFAULT_FONT);
		//ImageIO.write(myImg2, "PNG", new File(FileUtil.findUserDesktopDir()+File.separator+"test2.png"));
		
		//绘制一个有背景图片的图片（图片按照需要缩放）
		//BufferedImage myImg3 = createStringImage("你好呀，哈哈哈哈哈哈哈", 300, 300, 40, 40, myImg, DEFAULT_COLOR, DEFAULT_FONT);
		//ImageIO.write(myImg3, "PNG", new File(FileUtil.findUserDesktopDir()+File.separator+"test3.png"));
		
		String ori = "C:\\Users\\CaptCHN-PC\\Pictures\\Screenshots\\DSC00355-1.JPG";
		String tar = "C:\\Users\\CaptCHN-PC\\Pictures\\Screenshots\\DSC00355-1-M.JPG";
		
		ImageUtil.getScaledImage(ori, tar, 600, 600);
		
	}
	*/
	
}
