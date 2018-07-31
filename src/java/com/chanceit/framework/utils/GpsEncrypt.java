/**
 * Copyright (c) 2011-2012 RoadRover Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Mar 20, 2012
 * Id: GpsEncrypt.java,v 1.0 Mar 20, 2012 5:23:41 PM Administrator
 */
package com.chanceit.framework.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GpsEncrypt
 * @author Administrator
 * @date Mar 20, 2012 5:23:41 PM
 * @Description GPS坐标的加密算法
 */
public class GpsEncrypt {
	
	private static int casm_t1;
	private static int casm_t2;
	private static double casm_rr;
	private static double casm_x1;
	private static double casm_y1;
	private static double casm_x2;
	private static double casm_y2;
	private static double casm_f;
	
	/**
	 * @author yehao
	 * @date Mar 21, 2012
	 * @param wg_lng WGS84经度（单位：1/1024秒）
	 * @param wg_lat WGS84纬度（单位：1/1024秒）
	 * @param wg_heit 当前位置的高程（单位：米）
	 * @param wg_week GPS接受时间的周数
	 * @param wg_time GPS接收时间（单位：1/1000秒）
	 * @return 加密之后的GPS值，容器只有两个值，第一个为经度，第二个为纬度
	 * @Description GPS加密
	 */
	public static List<Integer> EncryptGps(int wg_lng , int wg_lat , int wg_heit , int wg_week , int wg_time){
		List<Integer> list = new ArrayList<Integer>();
		double x_add ;
		double y_add ;
		double h_add ;
		double x_1;
		double y_1;
		double casm_v ;
		double t1_t2;
		double x1_x2;
		double y1_y2;
		
		if( wg_heit > 5000) {
			return null;
		}
		x_1 = wg_lng / 3686400.0;
		y_1 = wg_lat / 3686400.0;
		if(!checkGPS(x_1, y_1)){
			return null;
		}
		initCasm(wg_time, wg_lng, wg_lat);
		casm_t2 = wg_time ;
		t1_t2 = (casm_t2 - casm_t1) / 1000.0;
		if (t1_t2 <= 0) {
			casm_t1 = casm_t2 ;
			casm_f=casm_f+1 ;
			casm_x1 = casm_x2 ;
			casm_f=casm_f+1 ;
			casm_y1 = casm_y2 ;
			casm_f=casm_f+1 ;
		} else {
			if ( t1_t2 > 120 ) {
				if (casm_f == 3) {
					casm_f=0;
					casm_x2 = wg_lng;
					casm_y2 = wg_lat;
					x1_x2 = casm_x2 - casm_x1;
					y1_y2 = casm_y2 - casm_y1;
					casm_v = Math.sqrt (x1_x2 * x1_x2 + y1_y2 * y1_y2 ) /t1_t2;
					casm_v = 0.0;
					if (casm_v  > 3185) {
						return null;
					}
				}
				casm_t1= casm_t2 ;
				casm_f=casm_f+1;
				casm_x1 = casm_x2;
				casm_f=casm_f+1;
				casm_y1 = casm_y2;
				casm_f=casm_f+1;
			}
		}
		x_add = Transform_yj5(x_1 - 105, y_1 - 35) ;
		y_add = Transform_yjy5(x_1 - 105, y_1 - 35) ;
		h_add = wg_heit;
		
		x_add = x_add + h_add * 0.001 + yj_sin2(wg_time*0.0174532925199433)+random_yj();
		y_add = y_add + h_add * 0.001 + yj_sin2(wg_time*0.0174532925199433)+random_yj();
		int china_lng = (int)((x_1 + Transform_jy5(y_1, x_add)) * 3686400);
		int china_lat = (int)((y_1 + Transform_jyj5(y_1, y_add)) * 3686400);
		list.add(china_lng);
		list.add(china_lat);
		return list;
	}
	
	/**
	 * @author yehao
	 * @date Mar 21, 2012
	 * @param lng 经度
	 * @param lat 纬度
	 * @return true:有效 ,false:无效
	 * @Description 检查经纬度是否有效
	 */
	private static boolean checkGPS(double lng , double lat){
		if (lng < 72.004) { 
			return false;
		}
		if (lng > 137.8347) { 
			return false;
		}
		if (lat < 0.8293) {
			return false;
		}
		if (lat > 55.8271 ){
			return false;
		}
		return true;
	}
	
	/**
	 * @author yehao
	 * @date Mar 21, 2012
	 * @param w_time GPS接收时间（单位：1/1000秒）
	 * @param w_lng WGS84经度（单位：1/1024秒）
	 * @param w_lat WGS84纬度（单位：1/1024秒）
	 * @Description 初始化方法
	 */
	public static void initCasm(int w_time , int w_lng ,int w_lat){
		int tt ;
		casm_t1 = w_time;
		casm_t2 = w_time;
		tt = (int)(w_time / 0.357); 
		casm_rr = w_time - tt*0.357;
		if (w_time == 0){
			casm_rr = 0.3;
		}
		casm_x1 = w_lng;
		casm_y1 = w_lat;
		casm_x2 = w_lng;
		casm_y2 = w_lat;
		casm_f = 3;
	}
	
	public static double Transform_yj5(double x , double y) {
		double tt = 0.0;
		tt = 300 + 1 * x + 2 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.sqrt(x*x));
		tt = tt + (20 *yj_sin2(18.849555921538764 * x ) + 20 * yj_sin2(6.283185307179588 * x))*0.6667;
		tt = tt + (20 * yj_sin2(3.141592653589794 * x ) + 40 * yj_sin2(1.047197551196598 * x))*0.6667;
		tt = tt + (150 * yj_sin2(0.2617993877991495 * x) + 300 * yj_sin2(0.1047197551196598 * x))*0.6667;
		return tt;
	}
	
	public static double yj_sin2(double x ){
		double tt ;
		double ss;
		int ff ;
		double s2;
		int cc;
		ff=0;
		if (x<0)
		{
			x=-x;
			ff=1;
		}
		cc=(int)(x/6.28318530717959); 
		tt=x-cc*6.28318530717959;
		if (tt>3.1415926535897932)
		{
			tt=tt-3.1415926535897932;
			if (ff==1)
				ff=0;
			else if (ff==0)
				ff=1;
		}
		x=tt;
		ss=x;
		s2=x;
		tt=tt*tt;
		s2=s2*tt;
		ss=ss-s2* 0.166666666666667;
		s2=s2*tt;
		ss=ss+s2* 8.33333333333333E-03;
		s2=s2*tt;
		ss=ss-s2* 1.98412698412698E-04;
		s2=s2*tt;
		ss=ss+s2* 2.75573192239859E-06;
		s2=s2*tt;
		ss=ss-s2* 2.50521083854417E-08;
		if (ff==1)
			ss=-ss;
		return ss;
	}
	
	public static double Transform_yjy5(double x , double y){
		double tt ;
		tt = -100 +  2 * x + 3 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.sqrt(x*x));
		tt = tt + (20 * yj_sin2(18.849555921538764 * x) + 20 * yj_sin2(6.283185307179588 * x))*0.6667;
		tt = tt + (20 * yj_sin2(3.141592653589794 * y)+ 40 * yj_sin2(1.047197551196598 * y))*0.6667;
		tt = tt + (160 * yj_sin2(0.2617993877991495 * y) + 320 * yj_sin2(0.1047197551196598 * y))*0.6667;
		return tt;
	}
	
	public static double random_yj(){
		int t;
		int casm_a ;
		int casm_c ;
		casm_a = 314159269;
		casm_c = 453806245;
		casm_rr = casm_a * casm_rr + casm_c ;
		t = (int)(casm_rr /2) ;  
		casm_rr = casm_rr - t * 2;
		casm_rr = casm_rr / 2 ;
		return (casm_rr);
	}
	
	public static double Transform_jy5(double x ,double xx){
		double n ;
		double a ;
		double e ;
		a = 6378245;
		e = 0.00669342;
		n = Math.sqrt (1 - e * yj_sin2(x * 0.0174532925199433) * yj_sin2(x * 0.0174532925199433) );
		n = (xx * 180) /(a / n * Math.cos(x * 0.0174532925199433) * 3.1415926) ;
		return n;
	}
	
	public static double Transform_jyj5(double x , double yy){
		double m ;
		double a ;
		double e ;
		double mm;
		a = 6378245;
		e = 0.00669342;
		mm= 1 - e * yj_sin2(x * 0.0174532925199433) * yj_sin2(x * 0.0174532925199433) ;
		m = (a * (1 - e)) / (mm * Math.sqrt(mm));
		return (yy * 180) / (m * 3.1415926);
	}
	
	public static void main(String[] args) {
		//10052837,3450745,10053372,3450862
		File mfile = new File("D:/5.txt");
		BufferedReader reader;
		String str = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(mfile)));
			PrintWriter writer = new PrintWriter(new FileOutputStream("D:/6.txt"));
			while ((str = reader.readLine())!= null){
//				System.out.println(str);
				int ax = Integer.parseInt(str.split(",")[0]);
				int ay = Integer.parseInt(str.split(",")[1]);
				int anewX = Integer.parseInt(str.split(",")[2]);
				int anewY = Integer.parseInt(str.split(",")[3]);
				int x = (int)((ax/24.0)*1024);
				int y = (int)((ay/24.0)*1024);
				List<Integer> list = GpsEncrypt.EncryptGps(x, y, 0, 0, 0);
				//list = encrypt.EncryptGps(false, x, y, 0, 0, 0);
				int newX = (int)((list.get(0)/1024.0)*24);
				int newY = (int)((list.get(1)/1024.0)*24);
				writer.write(str+"\t"+newX+","+newY+"\t"+(anewX-newX)+","+(anewY-newY)+"\r\t\n");
				writer.flush();
//				System.out.println(newX+","+newY);
//				System.out.println((anewX-newX)+","+(anewY-newY));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
