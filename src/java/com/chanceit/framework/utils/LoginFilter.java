/**
 * Copyright (c) 2011-2012 RoadRover Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Nov 15, 2011
 * Id: LoginFilter.java,v 1.0 Nov 15, 2011 3:25:47 PM Administrator
 */
package com.chanceit.framework.utils;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.chanceit.framework.enums.EnumHTTP;
import com.chanceit.http.service.IAccountService;

/**
 * @ClassName LoginFilter
 * @author Administrator
 * @date Nov 15, 2013 3:25:47 PM
 * @Description 登录拦截器
 */
public class LoginFilter implements Filter {
	
	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;

	/**
	 * @author Administrator
	 * @date Nov 15, 2011
	 * @Description 摧毁方法
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {

	}

	/**
	 * @author Administrator
	 * @date Nov 15, 2011
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws IOException
	 * @throws ServletException
	 * @Description 拦截器
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		String uri = req.getRequestURI();
		System.out.println("uri:"+uri);
		String suffix = uri.substring(uri.lastIndexOf(".") + 1);
		System.out.println("suffix:"+suffix);
		Object account = session.getAttribute("account");
		String page = uri.substring(uri.lastIndexOf("/") + 1);
		if (checkSuffix(suffix) || isWebService(uri) || checkPage(page) || account!=null) {
			chain.doFilter(request, response);
		} else{
				resp.sendRedirect("index.jsp");
				chain.doFilter(request, response);
		}
	}
	
//	public void doFilter(ServletRequest request, ServletResponse response,
//			FilterChain chain) throws IOException, ServletException {
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse resp = (HttpServletResponse) response;
//		String uri = req.getRequestURI();
////		 System.out.println(uri);
//		String suffix = uri.substring(uri.lastIndexOf("/") + 1);
//		if (!"".equals(suffix)) {//no suffix
//			chain.doFilter(request, response);
//		} else {
//			resp.sendRedirect("index.action");
//		}
//
//	}
	/*public void doFilter(ServletRequest arg0, ServletResponse arg1,  
            FilterChain filterChain) throws IOException, ServletException {  
        HttpServletRequest request = (HttpServletRequest) arg0;  
        HttpServletResponse response = (HttpServletResponse) arg1;  
		HttpSession session = request.getSession();
        Account account = (Account)session.getAttribute("account");      
        
		String uri = request.getRequestURI();
		// System.out.println(uri);
		String suffix = uri.substring(uri.lastIndexOf(".") + 1);
		String page = uri.substring(uri.lastIndexOf("/") + 1);
		if (checkSuffix(suffix) || isWebService(uri) || checkPage(page)) { //过滤掉不需要验证登录的请求
			filterChain.doFilter(request, response);
		} else {
	        if(account==null){
	            Cookie[] cookies = request.getCookies();  
	            String[] cooks = null;  
	            String accountName = null;  
	            String password = null;  
	            if (cookies != null) {  
	                for (Cookie coo : cookies) {  
	                    String aa = coo.getValue();  
	                    cooks = aa.split("==");  
	                    if (cooks.length == 2) {  
	                    	accountName = cooks[0];  
	                        password = cooks[1];
	                        break;
	                    }  
	                } 
	                if(StringUtils.isNotEmpty(accountName) && StringUtils.isNotEmpty(accountName)){
	                	try {
							account = accountService.getByName(accountName);
							if(account != null && account.getAccountPwd().equals(Coder.encryptMD5(password))){
								account.setVisitIp(request.getRemoteAddr());
								account.setVisitTime(new Date());
								accountService.save(account);
								session.setAttribute("account", account);
								session.setAttribute("levelId", account.getLevel().getLevelId());
								session.setAttribute("levelName", account.getLevel().getLevelName());
								session.setAttribute("limitNum", account.getLevel().getLimitNum());
								filterChain.doFilter(request,response );  
							}else{
								response.sendRedirect("login.html"); 
							}
						} catch (Exception e) {
							//e.printStackTrace();
							response.sendRedirect("login.html"); 
						}
	                }else{
	                	filterChain.doFilter(request,response );  
	                }
	                
	            }else{
	            	response.sendRedirect("login.html"); 
	            }
	        }else{
	        	filterChain.doFilter(request,response );  
	        }
		}
    }  
	*/
	/**
	 * @author Administrator
	 * @date Dec 7, 2011
	 * @param url 请求地址
	 * @return 如果请求的是WebService地址，则返回true，否则返回false
	 * @Description 判断该请求地址是否是内部WebService地址
	 */
	private boolean isWebService(String url) {
		String result = StringUtil.regexMatchOne(".*/(.*?)/.*", url, 1);
		return result.equals("services");
	}

	/**
	 * @author Administrator
	 * @date Nov 24, 2011
	 * @param suffix
	 * @return
	 * @Description 自动过滤css,jpg,js,gif格式链接请求
	 */
	private boolean checkSuffix(String suffix) {
		boolean flag = false;
		if ("js".equalsIgnoreCase(suffix))
			flag = true;
		else if ("css".equalsIgnoreCase(suffix))
			flag = true;
		else if ("jpg".equalsIgnoreCase(suffix))
			flag = true;
		else if ("gif".equalsIgnoreCase(suffix))
			flag = true;
		else if ("png".equalsIgnoreCase(suffix))
			flag = true;
		else if ("html".equalsIgnoreCase(suffix))
			flag = true;
		// if("action".equalsIgnoreCase(suffix)) flag = true;
		return flag;
	}

	/**
	 * @author Administrator
	 * @date Nov 24, 2011
	 * @param page
	 * @return
	 * @Description 自动过滤以下页面
	 */
	private boolean checkPage(String page) {
		boolean flag = false;
		if(page.contains(EnumHTTP.ACCOUNT_LOGIN) || page.contains(EnumHTTP.ACCOUNT_SAVE)
				|| page.contains(EnumHTTP.ACCOUNT_CHECK)){
			flag = true;
		}
		if(page.contains(EnumHTTP.ACCOUNT_LOGOUT)){
			flag = true;
		}
		return flag;
	}

	/**
	 * @author Administrator
	 * @date Nov 15, 2011
	 * @param arg0
	 * @throws ServletException
	 * @Description TODO(这里用一句话描述这个方法的作用)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
