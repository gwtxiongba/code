package com.chanceit.framework.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.chanceit.http.pojo.Point;
 
@Component("converter")
public class Converter
{
	Converter me ;
	final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	double casm_rr = 0;
	double casm_t1 = 0;
	double casm_t2 = 0;
	double casm_x1 = 0;
	double casm_y1 = 0;
	double casm_x2 = 0;
	double casm_y2 = 0;
	double casm_f = 0;

	public Converter() {
		this.me = this;
		this.casm_rr = 0;
		this.casm_t1 = 0;
		this.casm_t2 = 0;
		this.casm_x1 = 0;
		this.casm_y1 = 0;
		this.casm_x2 = 0;
		this.casm_y2 = 0;
		this.casm_f = 0;
	}

	protected double yj_sin2(double x) {
		double tt;
		double ss;
		double ff;
		double s2;
		int cc;
		ff = 0;
		if (x < 0) {
			x = -x;
			ff = 1;
		}

		cc = (int) (x / 6.28318530717959);

		tt = x - cc * 6.28318530717959;
		if (tt > 3.1415926535897932) {
			tt = tt - 3.1415926535897932;
			if (ff == 1) {
				ff = 0;
			} else if (ff == 0) {
				ff = 1;
			}
		}
		x = tt;
		ss = x;
		s2 = x;
		tt = tt * tt;
		s2 = s2 * tt;
		ss = ss - s2 * 0.166666666666667;
		s2 = s2 * tt;
		ss = ss + s2 * 8.33333333333333E-03;
		s2 = s2 * tt;
		ss = ss - s2 * 1.98412698412698E-04;
		s2 = s2 * tt;
		ss = ss + s2 * 2.75573192239859E-06;
		s2 = s2 * tt;
		ss = ss - s2 * 2.50521083854417E-08;
		if (ff == 1) {
			ss = -ss;
		}
		return ss;
	}

	protected double Transform_yj5(double x, double y) {
		double tt;
		tt = 300 + 1 * x + 2 * y + 0.1 * x * x + 0.1 * x * y + 0.1
				* Math.sqrt(Math.sqrt(x * x));
		tt = tt
				+ (20 * me.yj_sin2(18.849555921538764 * x) + 20 * me
						.yj_sin2(6.283185307179588 * x)) * 0.6667;
		tt = tt
				+ (20 * me.yj_sin2(3.141592653589794 * x) + 40 * me
						.yj_sin2(1.047197551196598 * x)) * 0.6667;
		tt = tt
				+ (150 * me.yj_sin2(0.2617993877991495 * x) + 300 * me
						.yj_sin2(0.1047197551196598 * x)) * 0.6667;
		return tt;
	}

	protected double Transform_yjy5(double x, double y) {
		double tt;
		tt = -100 + 2 * x + 3 * y + 0.2 * y * y + 0.1 * x * y + 0.2
				* Math.sqrt(Math.sqrt(x * x));
		tt = tt
				+ (20 * me.yj_sin2(18.849555921538764 * x) + 20 * me
						.yj_sin2(6.283185307179588 * x)) * 0.6667;
		tt = tt
				+ (20 * me.yj_sin2(3.141592653589794 * y) + 40 * me
						.yj_sin2(1.047197551196598 * y)) * 0.6667;
		tt = tt
				+ (160 * me.yj_sin2(0.2617993877991495 * y) + 320 * me
						.yj_sin2(0.1047197551196598 * y)) * 0.6667;
		return tt;
	}

	protected double Transform_jy5(double x, double xx) {
		double n;
		double a;
		double e;
		a = 6378245;
		e = 0.00669342;
		n = Math.sqrt(1 - e * me.yj_sin2(x * 0.0174532925199433)
				* me.yj_sin2(x * 0.0174532925199433));
		n = (xx * 180) / (a / n * Math.cos(x * 0.0174532925199433) * 3.1415926);
		return n;
	}

	protected double Transform_jyj5(double x, double yy) {
		double m;
		double a;
		double e;
		double mm;
		a = 6378245;
		e = 0.00669342;
		mm = 1 - e * me.yj_sin2(x * 0.0174532925199433)
				* me.yj_sin2(x * 0.0174532925199433);
		m = (a * (1 - e)) / (mm * Math.sqrt(mm));
		return (yy * 180) / (m * 3.1415926);
	}

	protected int r_yj() {
		int casm_a = 314159269;
		int casm_c = 453806245;
		return 0;
	}

	protected double random_yj() {
		double t;
		double casm_a = 314159269;
		double casm_c = 453806245;
		me.casm_rr = casm_a * me.casm_rr + casm_c;
		t = (int) (me.casm_rr / 2);
		me.casm_rr = me.casm_rr - t * 2;
		me.casm_rr = me.casm_rr / 2;
		return (me.casm_rr);
	}

	protected void IniCasm(double w_time, double w_lng, double w_lat) {
		double tt;
		me.casm_t1 = w_time;
		me.casm_t2 = w_time;
		tt = (int) (w_time / 0.357);
		me.casm_rr = w_time - tt * 0.357;
		if (w_time == 0)
			me.casm_rr = 0.3;
		me.casm_x1 = w_lng;
		me.casm_y1 = w_lat;
		me.casm_x2 = w_lng;
		me.casm_y2 = w_lat;
		me.casm_f = 3;
	}

	protected Point wgtochina_lb(int wg_flag, int wg_lng, int wg_lat,
			int wg_heit, int wg_week, int wg_time) {
		double x_add;
		double y_add;
		double h_add;
		double x_l;
		double y_l;
		double casm_v;
		double t1_t2;
		double x1_x2;
		double y1_y2;
		Point point = new Point();
		;
		if (wg_heit > 5000) {
			return point;
		}
		x_l = wg_lng;
		x_l = x_l / 3686400.0;
		y_l = wg_lat;
		y_l = y_l / 3686400.0;
		if (x_l < 72.004) {
			return point;
		}
		if (x_l > 137.8347) {
			return point;
		}
		if (y_l < 0.8293) {
			return point;
		}
		if (y_l > 55.8271) {
			return point;
		}
		if (wg_flag == 0) {
			me.IniCasm(wg_time, wg_lng, wg_lat);
			point = new Point();
			point.setLatitude(wg_lng);
			point.setLongitude(wg_lat);
			return point;
		}
		me.casm_t2 = wg_time;
		t1_t2 = (me.casm_t2 - me.casm_t1) / 1000.0;
		if (t1_t2 <= 0) {
			me.casm_t1 = me.casm_t2;
			me.casm_f = me.casm_f + 1;
			me.casm_x1 = me.casm_x2;
			me.casm_f = me.casm_f + 1;
			me.casm_y1 = me.casm_y2;
			me.casm_f = me.casm_f + 1;
		} else {
			if (t1_t2 > 120) {
				if (me.casm_f == 3) {
					me.casm_f = 0;
					me.casm_x2 = wg_lng;
					me.casm_y2 = wg_lat;
					x1_x2 = me.casm_x2 - me.casm_x1;
					y1_y2 = me.casm_y2 - me.casm_y1;
					casm_v = Math.sqrt(x1_x2 * x1_x2 + y1_y2 * y1_y2) / t1_t2;
					if (casm_v > 3185) {
						return (point);
					}
				}
				me.casm_t1 = me.casm_t2;
				me.casm_f = me.casm_f + 1;
				me.casm_x1 = me.casm_x2;
				me.casm_f = me.casm_f + 1;
				me.casm_y1 = me.casm_y2;
				me.casm_f = me.casm_f + 1;
			}
		}
		x_add = me.Transform_yj5(x_l - 105, y_l - 35);
		y_add = me.Transform_yjy5(x_l - 105, y_l - 35);
		h_add = wg_heit;
		x_add = x_add + h_add * 0.001
				+ me.yj_sin2(wg_time * 0.0174532925199433) + me.random_yj();
		y_add = y_add + h_add * 0.001
				+ me.yj_sin2(wg_time * 0.0174532925199433) + me.random_yj();
		point = new Point();
		point.setX(((x_l + me.Transform_jy5(y_l, x_add)) * 3686400));
		point.setY(((y_l + me.Transform_jyj5(y_l, y_add)) * 3686400));
		return point;
	}

	protected boolean isValid(long validdays) {
		// long standand = 1253525356;
		long h = 3600;
		Date currentTime = new Date();
		if (currentTime.getTime() / 1000 - 1253525356 >= validdays * 24 * h) {
			return false;
		} else {
			return true;
		}
	}

	public Point getEncryPoint(double x, double y) {
		Point point = new Point();
		double x1, tempx;
		double y1, tempy;
		x1 = x * 3686400.0;
		y1 = y * 3686400.0;
		double gpsWeek = 0;
		double gpsWeekTime = 0;
		double gpsHeight = 0;

		point = me.wgtochina_lb(1, (int) (x1), (int) (y1), (int) (gpsHeight),
				(int) (gpsWeek), (int) (gpsWeekTime));
		tempx = point.getX();
		tempy = point.getY();
		tempx = tempx / 3686400.0;
		tempy = tempy / 3686400.0;
		point = new Point();
		point.setX(tempx);
		point.setY(tempy);
		return point;
	}

	protected String getEncryCoord(String coord, boolean flag) {
		if (flag) {

			double x = Double.parseDouble(coord.split(",")[0]);
			double y = Double.parseDouble(coord.split(",")[1]);
			Point point = new Point();
			double x1, tempx;
			double y1, tempy;
			x1 = x * 3686400.0;
			y1 = y * 3686400.0;
			int gpsWeek = 0;
			int gpsWeekTime = 0;
			int gpsHeight = 0;
			point = me.wgtochina_lb(1, (int) (x1), (int) (y1),
					(gpsHeight), (gpsWeek), (gpsWeekTime));
			tempx = point.getX();
			tempy = point.getY();
			tempx = tempx / 3686400.0;
			tempy = tempy / 3686400.0;
			return tempx + "," + tempy;
		} else {
			return "";
		}
	}

	// Point

	Point bd_encrypt(double gg_lat, double gg_lon) {
		// , double &bd_lat, double &bd_lon
		// Point pt = this.getEncryPoint(gg_lat,gg_lon);

		double x = gg_lon, y = gg_lat;

		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);

		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);

		double bd_lon = z * Math.cos(theta) + 0.0065;

		double bd_lat = z * Math.sin(theta) + 0.006;
		Point pt = new Point();
		pt.setLatitude(bd_lat);
		pt.setLongitude(bd_lon);
		return pt;
	}

	Point bd_decrypt(double bd_lat, double bd_lon)

	{

		double x = bd_lon - 0.0065, y = bd_lat - 0.006;

		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);

		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);

		double gg_lon = z * Math.cos(theta);

		double gg_lat = z * Math.sin(theta);
		Point pt = new Point();
		pt.setX(gg_lon);
		pt.setY(gg_lat);
		return pt;
	}

	/**
	 * GPS ���� ת�ٶ�����
	 * 
	 * @param gg_lat
	 * @param gg_lon
	 * @return
	 */
	public Point wgs2bd(double gg_lat, double gg_lon) {
		// wgs2gcj
		Point pt = this.getEncryPoint(gg_lon, gg_lat);
		// gcj2baidu
		return bd_encrypt(pt.y, pt.x);
	}
public Point bd2wgs(double gg_lat, double gg_lon){
	  double gg_lat_o = gg_lat;
	  double gg_lon_o = gg_lon;
	  
		for(int i = 0; i < 5; i++) {
			Point pt = wgs2bd(gg_lat_o,gg_lon_o);
			gg_lon_o = gg_lon - (pt.getLongitude() - gg_lon_o);
			gg_lat_o = gg_lat - (pt.getLatitude() - gg_lat_o);
		}
		Point pt = new Point();
		pt.setLatitude(gg_lat_o);
		pt.setLongitude(gg_lon_o);
		return pt;
}
	
	public static void main(String argv[]) throws Exception {
		Converter c = new Converter();
		Point p = c.getEncryPoint(102.702825064591, 25.0457617282833);
		System.out.println("������ x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(102.485140720179, 24.9226075470813);
		System.out.println("������ x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(102.795792248383, 24.8909889571743);
		System.out.println("�ʹ��� x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(102.702825064591, 25.0457617282833);
		System.out.println("���� x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(103.180145041594, 26.0851335734867);
		System.out.println("������ x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(102.484333184238, 25.238607620033);
		System.out.println("������ x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(102.740115855018, 25.0118811075002);
		System.out.println("�ٶ��� x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(102.595106736332, 24.6679260310969);
		System.out.println("������ x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(102.468291511824, 25.5529734769783);
		System.out.println("»Ȱ�� x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(103.273299939618, 24.7568405347061);
		System.out.println("ʯ���� x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(103.041508523306, 25.3310846069293);
		System.out.println("������ x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(103.151790850516, 25.3343405555925);
		System.out.println("Ѱ���� x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(102.65842000874, 25.0390786264324);
		System.out.println("��ɽ�� x:" + p.x + " y:" + p.y);
		p = c.getEncryPoint(103.146517127597, 24.9222445338786);
		System.out.println("������ x:" + p.x + " y:" + p.y);
	}

	
}