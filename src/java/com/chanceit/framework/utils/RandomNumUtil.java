/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jul 2, 2013
 * Id: RandomNumUtil.java,v 1.0 Jul 2, 2013 4:25:50 PM Administrator
 */
package com.chanceit.framework.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.Captcha;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.word.RandomWordFactory;

/**
 * @ClassName RandomNumUtil
 * @author Administrator
 * @date Jul 2, 2013 4:25:50 PM
 * @Description ��֤�����ɹ�����
 */
public class RandomNumUtil {    
	private ByteArrayInputStream image;//ͼ��    
	private String str;//��֤��    
	  
	private RandomNumUtil(){    
		init();//��ʼ������    
	}    
	/*   
	* ȡ��RandomNumUtilʵ��   
	*/    
	public static RandomNumUtil Instance(){    
		return new RandomNumUtil();    
	}    
	/*   
	* ȡ����֤��ͼƬ   
	*/    
	public ByteArrayInputStream getImage(){    
		return this.image;    
	}    
	/*   
	* ȡ��ͼƬ����֤��   
	*/    
	public String getString(){    
		return this.str;    
	}    
	  
	private void init() {    
		createPhote();
	}
	public static int getRandomPassword(){
		 Random random=new Random();
		while(true){
			int x=random.nextInt(999999);
			if(x>100000){
				return x;
			}
		}
	}
	private void createPhote(){
		BufferedImage bfimage = null;
		
		ConfigurableCaptchaService service = new ConfigurableCaptchaService();
		service.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
//		service.setHeight(25);
//		service.setWidth(70);
		
		CurvesRippleFilterFactory curvesRippleFilterFactory = new CurvesRippleFilterFactory(service.getColorFactory());//���ù����ߵķ�ʽ
		service.setFilterFactory(curvesRippleFilterFactory);
		
		RandomWordFactory wordFactory = new RandomWordFactory(); //��������ĸ���
		wordFactory.setCharacters("123456789");
		wordFactory.setMinLength(4);
		wordFactory.setMaxLength(4);
		service.setWordFactory(wordFactory);
		
		RandomFontFactory fontFactory = new RandomFontFactory();//��������Ĵ�С
//		fontFactory.setMinSize(20);
//		fontFactory.setMaxSize(20);
		service.setFontFactory(fontFactory);
		
		Captcha captcha = service.getCaptcha();
		bfimage = captcha.getImage();
		this.str = captcha.getChallenge();
		
		ByteArrayInputStream input=null;    
		ByteArrayOutputStream output = new ByteArrayOutputStream();    
		try{    
			ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);    
			ImageIO.write(bfimage, "png", imageOut);    
			imageOut.close();    
			input = new ByteArrayInputStream(output.toByteArray());    
			}catch(Exception e){    
				e.printStackTrace();
//			logger.("��֤��ͼƬ�������ִ���"+e.toString());    
		}    
	  
		this.image=input;/* ��ֵͼ�� */    
	}
	
	private void createImage(){
		// ���ڴ��д���ͼ��    
		int width=60, height=20;    
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);    
		// ��ȡͼ��������    
		Graphics g = image.getGraphics();    
		// ���������    
		Random random = new Random();    
		// �趨����ɫ    
		g.setColor(getRandColor(200,250));    
		g.fillRect(0, 0, width, height);    
		// �趨����    
		g.setFont(new Font("Times New Roman",Font.PLAIN,18));    
		// �������155�������ߣ�ʹͼ���е���֤�벻�ױ���������̽�⵽    
		g.setColor(getRandColor(160,200));    
		for (int i=0;i<155;i++){    
			int x = random.nextInt(width);    
			int y = random.nextInt(height);    
			int xl = random.nextInt(12);    
			int yl = random.nextInt(12);    
			g.drawLine(x,y,x+xl,y+yl);    
		}    
		// ȡ�����������֤��(6λ����)    
		String sRand="";    
		for (int i=0;i<4;i++){    
			String rand=String.valueOf(random.nextInt(10));    
			sRand+=rand;    
			// ����֤����ʾ��ͼ����    
			g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));    
			// ���ú�����������ɫ��ͬ����������Ϊ����̫�ӽ�������ֻ��ֱ������    
			g.drawString(rand,13*i+6,16);    
		}   
		//��ֵ��֤��   
		this.str=sRand;    
	  
		//ͼ����Ч    
		g.dispose();    
		ByteArrayInputStream input=null;    
		ByteArrayOutputStream output = new ByteArrayOutputStream();    
		try{    
			ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);    
			ImageIO.write(image, "JPEG", imageOut);    
			imageOut.close();    
			input = new ByteArrayInputStream(output.toByteArray());    
			}catch(Exception e){    
			System.out.println("��֤��ͼƬ�������ִ���"+e.toString());    
		}    
	  
		this.image=input;/* ��ֵͼ�� */    
	}
	
	/*   
	* ������Χ��������ɫ   
	*/    
	private Color getRandColor(int fc,int bc){    
	Random random = new Random();    
	if(fc>255) fc=255;    
	if(bc>255) bc=255;    
	int r=fc+random.nextInt(bc-fc);    
	int g=fc+random.nextInt(bc-fc);    
	int b=fc+random.nextInt(bc-fc);    
	return new Color(r,g,b);    
	}   
	
	public static void main(String[] args){
		System.out.println(RandomNumUtil.Instance().getRandomPassword());
	}
}