package com.chanceit.framework.ucenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ================================================
 * Discuz! Ucenter API for JAVA
 * ================================================
 * ��������ͬ��UC Server�����Ĳ���ָ��
 * ���Ը���ҵ����Ҫ�����Ӧ��ִ�д���
 * 
 * ���ߣ���ƽ
 * ����ʱ�䣺2009-2-20
 */
public class UC extends HttpServlet{

	private static final long serialVersionUID = -7377364931916922413L;
	
	public static boolean IN_DISCUZ= true;
	public static String UC_CLIENT_VERSION="1.5.0";	//note UCenter �汾��ʶ
	public static String UC_CLIENT_RELEASE="20081031";

	public static boolean API_DELETEUSER=true;		//note �û�ɾ�� API �ӿڿ���
	public static boolean API_RENAMEUSER=true;		//note �û����� API �ӿڿ���
	public static boolean API_GETTAG=true;		//note ��ȡ��ǩ API �ӿڿ���
	public static boolean API_SYNLOGIN=true;		//note ͬ����¼ API �ӿڿ���
	public static boolean API_SYNLOGOUT=true;		//note ͬ���ǳ� API �ӿڿ���
	public static boolean API_UPDATEPW=true;		//note �����û����� ����
	public static boolean API_UPDATEBADWORDS=true;	//note ���¹ؼ����б� ����
	public static boolean API_UPDATEHOSTS=true;		//note ���������������� ����
	public static boolean API_UPDATEAPPS=true;		//note ����Ӧ���б� ����
	public static boolean API_UPDATECLIENT=true;		//note ���¿ͻ��˻��� ����
	public static boolean API_UPDATECREDIT=true;		//note �����û����� ����
	public static boolean API_GETCREDITSETTINGS=true;	//note �� UCenter �ṩ�������� ����
	public static boolean API_GETCREDIT=true;		//note ��ȡ�û���ĳ����� ����
	public static boolean API_UPDATECREDITSETTINGS=true;	//note ����Ӧ�û������� ����

	public static String API_RETURN_SUCCEED   =    "1";
	public static String API_RETURN_FAILED    =   "-1";
	public static String API_RETURN_FORBIDDEN =   "-2";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = doAnswer(request, response);
		response.getWriter().print(result);
	}
	
	/**
	 * ִ�о����Action
	 * ���з����������Ĳ�������ͨ��$get����á�
	 * ע�⣺ request�����ǲ��ܵõ�����ֵ�ġ�
	 * 
	 * @param request
	 * @param response
	 * @return ����״̬��������
	 */
	private String doAnswer(HttpServletRequest request, HttpServletResponse response){
		//����
		String $code = request.getParameter("code");
		if($code==null) return API_RETURN_FAILED;
		
		Map<String,String> $get = new HashMap<String, String>();
		$code = new Client().uc_authcode($code, "DECODE");
		parse_str($code, $get);

		if($get.isEmpty()) {
			return "Invalid Request";
		}
		if(time() - tolong($get.get("time")) > 3600) {
			return "Authracation has expiried";
		}

		String $action = $get.get("action");
		if($action==null) return API_RETURN_FAILED;
		
		if($action.equals("test")) {

			return API_RETURN_SUCCEED;

		} else if($action.equals("deleteuser")) {


			return API_RETURN_SUCCEED;

		} else if($action.equals("renameuser")) {

			return API_RETURN_SUCCEED;

		} else if($action.equals("gettag")) {

			if(!API_GETTAG ) return API_RETURN_FORBIDDEN;
			
			//ͬ������
			
			return API_RETURN_SUCCEED;
			

		} else if($action.equals("synlogin")) {

			if(!API_SYNLOGIN ) return (API_RETURN_FORBIDDEN);

			if($get.get("regtype").equals("0")) return (API_RETURN_FORBIDDEN);

			//note ͬ����¼ API �ӿ�
			//obclean();
			response.addHeader("P3P","CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");

			int $cookietime = 2592000;
			
		
			Cookie user = new Cookie("personloginuser",$get.get("username"));
			user.setMaxAge($cookietime);
			user.setPath("/");
		
	
			Client uc = new Client();
			Cookie auth = new Cookie("personappauth",uc.uc_authcode($get.get("password") + "\t" + $get.get("uid"), "ENCODE"));
			auth.setMaxAge($cookietime);
			auth.setPath("/");
		
			response.addCookie(user);
			response.addCookie(auth);

		} else if($action.equals("synlogout")) {

			if(!API_SYNLOGOUT ) return (API_RETURN_FORBIDDEN);

			//note ͬ���ǳ� API �ӿ�
			//obclean();
			HttpSession  httpSession= request.getSession();
			httpSession.removeAttribute("username");
			httpSession.removeAttribute("nickName");
			httpSession.removeAttribute("uid");
			httpSession.removeAttribute("regtype");
			httpSession.invalidate();
			
			response.addHeader("P3P"," CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");

			//clearcookie();
			Cookie user = new Cookie("personloginuser",null);
			user.setMaxAge(0);
			user.setPath("/");
			
			Cookie auth = new Cookie("personappauth",null);
			auth.setMaxAge(0);
			auth.setPath("/");
			
			response.addCookie(user);
			response.addCookie(auth);

		} else if($action.equals("updateclient")) {

			if(!API_UPDATECLIENT ) return API_RETURN_FORBIDDEN;

			
			//ͬ������
			
			return API_RETURN_SUCCEED;

		} else if($action.equals("updatepw")) {

			if(!API_UPDATEPW) return API_RETURN_FORBIDDEN;
			
			//ͬ������
			
			return API_RETURN_SUCCEED;

		} else if($action.equals("updatebadwords")) {

			if(!API_UPDATEBADWORDS) return API_RETURN_FORBIDDEN;
			
			//ͬ������
			
			return API_RETURN_SUCCEED;

		} else if($action.equals("updatehosts")) {

			if(!API_UPDATEHOSTS ) return API_RETURN_FORBIDDEN;


			return API_RETURN_SUCCEED;

		} else if($action.equals("updateapps")) {

			if(!API_UPDATEAPPS ) return API_RETURN_FORBIDDEN;


			return API_RETURN_SUCCEED;

		} else if($action.equals("updatecredit")) {

			//if(!UPDATECREDIT ) return API_RETURN_FORBIDDEN;

			return API_RETURN_SUCCEED;

		} else if($action.equals("getcreditsettings")) {

			//if(!GETCREDITSETTINGS ) return API_RETURN_FORBIDDEN;

			return "";//����ֵ

		} else if($action.equals("updatecreditsettings")) {

			if(!API_UPDATECREDITSETTINGS) return API_RETURN_FORBIDDEN;

			
			//ͬ������
			
			return API_RETURN_SUCCEED;

		} else {

			return (API_RETURN_FORBIDDEN);

		}		
		return "";
	}

	private void parse_str(String str, Map<String,String> sets){
		if(str==null||str.length()<1) 
			return;
		String[] ps = str.split("&");
		for(int i=0;i<ps.length;i++){
			String[] items = ps[i].split("=");
			if(items.length==2){
				sets.put(items[0], items[1]);
			}else if(items.length ==1){
				sets.put(items[0], "");
			}
		}
	}
	
	protected long time(){
		return System.currentTimeMillis()/1000;
	}
	
    private static long tolong(Object s){
        if(s!=null){
            String ss = s.toString().trim();
            if(ss.length()==0){
                return 0L;
            }else{
                return Long.parseLong(ss);
            }
        }else{
            return 0L;
        }
    }

}
