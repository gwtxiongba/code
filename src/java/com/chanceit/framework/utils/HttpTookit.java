package com.chanceit.framework.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpTookit
 * @author Administrator
 * @date Apr 18, 2012 11:54:58 AM
 * @Description 模拟HTTP请求，将百度地图的坐标转化成GPS的坐标
 */
public final class HttpTookit {
	
	private static Logger log = LoggerFactory.getLogger(HttpTookit.class);

	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * @param url 请求的URL地址
	 * @param queryString 请求的查询参数,可以为null
	 * @param charset 字符集
	 * @param pretty 是否美化
	 * @return 返回请求响应的HTML
	 */
	public static String doGet(String url, String queryString, String charset,
			boolean pretty) {
		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		client.getParams().setParameter("http.protocol.single-cookie-header",true);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		try {
			if (StringUtils.isNotBlank(queryString))// 对get请求参数做了http请求默认编码，好像没有任何问题，汉字编码后，就成为%式样的字符串
				method.setQueryString(URIUtil.encodeQuery(queryString));
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(),charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty)
						response.append(line).append(System.getProperty("line.separator"));
					else
						response.append(line);
				}
				reader.close();
			}
		} catch (URIException e) {
			log.error("执行HTTP Get请求时，编码查询字符串“" + queryString + "”发生异常！", e);
		} catch (IOException e) {
			log.error("执行HTTP Get请求" + url + "时，发生异常！", e);
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}

	/**
	 * 执行一个HTTP POST请求，返回请求响应的HTML
	 * @param url 请求的URL地址
	 * @param params 请求的查询参数,可以为null
	 * @param charset 字符集
	 * @param pretty 是否美化
	 * @return 返回请求响应的HTML
	 */
	public static String doPost(String url, Map<String, String> params,
			String charset, boolean pretty) {
		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		HttpMethod method = new PostMethod(url);
		// 设置Http Post数据
		if (params != null) {
			HttpMethodParams p = new HttpMethodParams();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				p.setParameter(entry.getKey(), entry.getValue());
			}
			method.setParams(p);
		}
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(),charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty)
						response.append(line).append(
								System.getProperty("line.separator"));
					else
						response.append(line);
				}
				reader.close();
			}
		} catch (IOException e) {
			log.error("执行HTTP Post请求" + url + "时，发生异常！", e);
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}
   
   public static String requestHttp(double x,double y){
		String baiduCoordinate = doGet(
				"http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x=" + x
						+ "&y=" + y + "&callback=", null, "GBK", true);
	   return baiduCoordinate;
   }
   
   public static List<Double> parsePoint(String coordinate) {
	   List<Double> list = new ArrayList<Double>();
	   try {
		JSONObject jsonObj = new JSONObject(coordinate);
		String pointX = jsonObj.getString("x");
		String pointY = jsonObj.getString("y");
		try {
			double	pointx = Double.parseDouble(unEncrypt(pointX));
			double	pointy = Double.parseDouble(unEncrypt(pointY));
			list.add(pointx);
			list.add(pointy);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	} catch (JSONException e) {
		e.printStackTrace();
	}
	   return null;
   }
	
 /*	public static void main(String[] args) {
	    Long start=System.currentTimeMillis();
		double x = 114.403304;
		double y = 30.455224;
		String baiduCoordinateString=requestHttp(x,y);
		List<Double> list = parsePoint(baiduCoordinateString);
		double cov_x = list.get(0);
		double cov_y = list.get(1);
		double new_cov_x=2*x-cov_x;//第一次计算的gps坐标
		double new_cov_y=2*y-cov_y;//第一次计算的gps坐标
		List<Double> list_tmp=null;
		for(int i=0;i<4;i++){
			baiduCoordinateString=requestHttp(new_cov_x,new_cov_y);
			list_tmp = parsePoint(baiduCoordinateString);
	        System.out.println("list_tmp:"+list_tmp.get(0)+","+list_tmp.get(1));
			new_cov_x=x-(list_tmp.get(0)-new_cov_x);
			new_cov_y=y-(list_tmp.get(1)-new_cov_y);
			list_tmp.clear();
			System.out.println(list_tmp.size());
		}
		Long end=System.currentTimeMillis();
		System.out.println("转换所需时间："+(end-start));
		System.out.println(new_cov_x+","+new_cov_y);
	}*/

	public static String unEncrypt(String str) throws IOException {
		if (str == null || str.equals("")) {
			return "";
		}
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = decoder.decodeBuffer(str);
		return new String(b);
	}
	
	
	public List<Integer> parse_Coor(double xx,double yy){
		List<Integer> list_int = new ArrayList<Integer>();
		double x = xx;
		double y = yy;
		String baiduCoordinateString=requestHttp(x,y);
		List<Double> list = parsePoint(baiduCoordinateString);
		if(list == null) return null;//如果坐标不存在，说明获得百度地图转点访问失败
		double cov_x = list.get(0);
		double cov_y = list.get(1);
		double new_cov_x=2*x-cov_x;//第一次计算的gps坐标
		double new_cov_y=2*y-cov_y;//第一次计算的gps坐标
		List<Double> list_tmp=null;
		for(int i=0;i<4;i++){
			baiduCoordinateString=requestHttp(new_cov_x,new_cov_y);
			list_tmp = parsePoint(baiduCoordinateString);
			new_cov_x=x-(list_tmp.get(0)-new_cov_x);
			new_cov_y=y-(list_tmp.get(1)-new_cov_y);
			list_tmp.clear();
		}
		list_int.add((int)(new_cov_x*3600*24));
		list_int.add((int)(new_cov_y*3600*24));
		return list_int;
	}
}