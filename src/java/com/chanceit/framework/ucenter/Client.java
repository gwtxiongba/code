package com.chanceit.framework.ucenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ================================================ Discuz! Ucenter API for JAVA
 * ================================================ UC Client ͨ�õ��ø�����ʵ����UC
 * Server֮���ͨ�š� ����ʵ����UC Serverͨ�ŵ����нӿں���
 * 
 * ������Ϣ��http://code.google.com/p/discuz-ucenter-api-for-java/ ���ߣ���ƽ
 * (no_ten@163.com) ����ʱ�䣺2009-2-20
 */
public class Client extends PHPFunctions {

	public static boolean IN_UC = true;
	public static String UC_IP = "127.0.0.1";
	public static String UC_API = "http://localhost/uc";
	public static String UC_CONNECT = "";
	public static String UC_KEY = "123456";
	public static String UC_APPID = "3";
	public static String UC_CLIENT_VERSION = "1.0";
	public static String UC_CLIENT_RELEASE = "20090212";
	public static String UC_ROOT = ""; // note �û����Ŀͻ��˵ĸ�Ŀ¼ UC_CLIENTROOT
	public static String UC_DATADIR = UC_ROOT + "./data/"; // note �û����ĵ����ݻ���Ŀ¼
	public static String UC_DATAURL = "UC_API" + "/data"; // note �û����ĵ����� URL
	public static String UC_API_FUNC = UC_CONNECT.equals("mysql") ? "uc_api_mysql"
			: "uc_api_post";
	public static String[] uc_controls = {};

	static {
		InputStream in = Client.class.getClassLoader().getResourceAsStream(
				"ucenter.properties");
		Properties properties = new Properties();
		try {
			properties.load(in);
			UC_API = properties.getProperty("UC_API");
			UC_IP = properties.getProperty("UC_IP");
			UC_KEY = properties.getProperty("UC_KEY");
			UC_APPID = properties.getProperty("UC_APPID");
			UC_CONNECT = properties.getProperty("UC_CONNECT");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String uc_serialize(String $arr, int $htmlon) {
		// return xml_serialize($arr, $htmlon);
		return $arr;
	}

	protected String uc_unserialize(String $s) {
		// include_once UC_ROOT.'./lib/xml.class.php';
		// return xml_unserialize($s);
		return $s;
	}

	protected String uc_addslashes(String $string, int $force, boolean $strip) {
		// !defined('MAGIC_QUOTES_GPC') && define('MAGIC_QUOTES_GPC',
		// get_magic_quotes_gpc());
		// if(!MAGIC_QUOTES_GPC || $force) {
		// if(is_array($string)) {
		// foreach($string as $key => $val) {
		// $string[$key] = uc_addslashes($val, $force, $strip);
		// }
		// } else {
		// $string = addslashes($strip ? stripslashes($string) : $string);
		// }
		// }
		return $string;
	}

	protected String daddslashes(String $string, int $force) {
		return uc_addslashes($string, $force, false);
	}

	protected String uc_stripslashes(String $string) {
		// !defined('MAGIC_QUOTES_GPC') && define('MAGIC_QUOTES_GPC',
		// get_magic_quotes_gpc());
		// if(MAGIC_QUOTES_GPC) {
		// return stripslashes($string);
		// } else {
		return $string;
		// }
	}

	@Override
	@SuppressWarnings("unchecked")
	public String uc_api_post(String $module, String $action,
			Map<String, Object> $arg) {
		StringBuffer $s = new StringBuffer();
		String $sep = "";
		// foreach($arg as $k => $v) {
		for (String $k : $arg.keySet()) {
			// $k = ($k);
			Object $v = $arg.get($k);
			$k = urlencode($k);
            
			
			if ($v.getClass().isAssignableFrom(Map.class) || $v.getClass().isAssignableFrom(HashMap.class)) {
				String $s2 = "";
				String $sep2 = "";
				// foreach($v as $k2 => $v2) {
				for (String $k2 : ((Map<String, Object>) $v).keySet()) {
					Object $v2 = ((Map<String, Object>) $v).get($k2);
					$k2 = urlencode($k2);
					$s2 += $sep2 + "" + $k + "[" + $k2 + "]="
							+ urlencode(uc_stripslashes(String.valueOf($v2)));
					$sep2 = "&";
				}
				$s.append($sep).append($s2);
			} else {
				$s.append($sep).append($k).append("=").append(
						urlencode(uc_stripslashes(String.valueOf($v))));
			}
			$sep = "&";
		}
		

		String $postdata = uc_api_requestdata($module, $action, $s.toString(),
				"");
		return uc_fopen2(UC_API + "/index.php", 500000, $postdata, "", true,
				UC_IP, 20, true);
	}

	/**
	 * ���췢�͸��û����ĵ���������
	 * 
	 * @param string
	 *            $module �����ģ��
	 * @param string
	 *            $action ����Ķ���
	 * @param string
	 *            $arg ����������ܵķ�ʽ���ͣ�
	 * @param string
	 *            $extra ���Ӳ���������ʱ�����ܣ�
	 * @return string
	 */
	protected String uc_api_requestdata(String $module, String $action,
			String $arg, String $extra) {
		String $input = uc_api_input($arg);
		String $post = "m=" + $module + "&a=" + $action + "&inajax=2&release="
				+ UC_CLIENT_RELEASE + "&input=" + $input + "&appid=" + UC_APPID
				+ $extra;
		return $post;
	}

	protected String uc_api_url(String $module, String $action, String $arg,
			String $extra) {
		String $url = UC_API + "/index.php?"
				+ uc_api_requestdata($module, $action, $arg, $extra);
		return $url;
	}

	public String uc_api_input(String $data) {
		// String $s = $data;
		// String $s =
		// urlencode(uc_authcode($data+"&agent="+md5($_SERVER["HTTP_USER_AGENT"])+"&time="+time(),
		// "ENCODE", UC_KEY));
		// String $s =
		// urlencode(uc_authcode($data+"&agent="+md5("Java/1.5.0_01")+"&time="+time(),
		// "ENCODE", UC_KEY));
		String $s = urlencode(uc_authcode($data + "&agent=" + md5("")
				+ "&time=" + time(), "ENCODE", UC_KEY));
		return $s;
	}

	/**
	 * MYSQL ��ʽȡָ����ģ��Ͷ���������
	 * 
	 * @param string
	 *            $model �����ģ��
	 * @param string
	 *            $action ����Ķ���
	 * @param string
	 *            $args ����������ܵķ�ʽ���ͣ�
	 * @return mix
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String uc_api_mysql(String $model, String $action, Map $args) {
		// global $uc_controls;
		// if(empty($uc_controls[$model])) {
		// include_once UC_ROOT.'./lib/db.class.php';
		// include_once UC_ROOT.'./model/base.php';
		// include_once UC_ROOT."./control/$model.php";
		// eval("\$uc_controls['$model'] = new {$model}control();");
		// }
		if ($action.charAt(0) != '_') {
			// $args = uc_addslashes($args, 1, true);
			// $action = "on"+$action;
			// $uc_controls[$model]->input = $args;
			// return $uc_controls[$model]->$action($args);
			return null;
		} else {
			return "";
		}
	}

	/**
	 * �ַ��������Լ����ܺ���
	 * 
	 * @param string
	 *            $string ԭ�Ļ�������
	 * @param string
	 *            $operation ����(ENCODE | DECODE), Ĭ��Ϊ DECODE
	 * @param string
	 *            $key ��Կ
	 * @param int
	 *            $expiry ������Ч��, ����ʱ����Ч�� ��λ �룬0 Ϊ������Ч
	 * @return string ������ ԭ�Ļ��� ���� base64_encode ����������
	 * 
	 * @example
	 * 
	 * $a = authcode('abc', 'ENCODE', 'key'); $b = authcode($a, 'DECODE',
	 * 'key'); // $b(abc)
	 * 
	 * $a = authcode('abc', 'ENCODE', 'key', 3600); $b = authcode('abc',
	 * 'DECODE', 'key'); // ��һ��Сʱ�ڣ�$b(abc)������ $b Ϊ��
	 */
	public String uc_authcode(String $string, String $operation) {
		return uc_authcode($string, $operation, null);
	}

	public String uc_authcode(String $string, String $operation, String $key) {
		return uc_authcode($string, $operation, $key, 0);
	}

	public String uc_authcode(String $string, String $operation, String $key,
			int $expiry) {

		int $ckey_length = 4; // note �����Կ���� ȡֵ 0-32;
		// note ���������Կ���������������κι��ɣ�������ԭ�ĺ���Կ��ȫ��ͬ�����ܽ��Ҳ��ÿ�β�ͬ�������ƽ��Ѷȡ�
		// note ȡֵԽ�����ı䶯����Խ�����ı仯 = 16 �� $ckey_length �η�
		// note ����ֵΪ 0 ʱ���򲻲��������Կ

		$key = md5($key != null ? $key : UC_KEY);
		String $keya = md5(substr($key, 0, 16));
		String $keyb = md5(substr($key, 16, 16));
		String $keyc = $ckey_length > 0 ? ($operation.equals("DECODE") ? substr(
				$string, 0, $ckey_length)
				: substr(md5(microtime()), -$ckey_length))
				: "";

		String $cryptkey = $keya + md5($keya + $keyc);
		int $key_length = $cryptkey.length();

		$string = $operation.equals("DECODE") ? base64_decode(substr($string,
				$ckey_length)) : sprintf("%010d", $expiry > 0 ? $expiry
				+ time() : 0)
				+ substr(md5($string + $keyb), 0, 16) + $string;
		int $string_length = $string.length();

		StringBuffer $result1 = new StringBuffer();

		int[] $box = new int[256];
		for (int i = 0; i < 256; i++) {
			$box[i] = i;
		}

		int[] $rndkey = new int[256];
		for (int $i = 0; $i <= 255; $i++) {
			$rndkey[$i] = $cryptkey.charAt($i % $key_length);
		}

		int $j = 0;
		for (int $i = 0; $i < 256; $i++) {
			$j = ($j + $box[$i] + $rndkey[$i]) % 256;
			int $tmp = $box[$i];
			$box[$i] = $box[$j];
			$box[$j] = $tmp;
		}

		$j = 0;
		int $a = 0;
		for (int $i = 0; $i < $string_length; $i++) {
			$a = ($a + 1) % 256;
			$j = ($j + $box[$a]) % 256;
			int $tmp = $box[$a];
			$box[$a] = $box[$j];
			$box[$j] = $tmp;

			$result1
					.append((char) ($string.charAt($i) ^ ($box[($box[$a] + $box[$j]) % 256])));

		}

		if ($operation.equals("DECODE")) {
			String $result = $result1.substring(0, $result1.length());
			if ((Integer.parseInt(substr($result.toString(), 0, 10)) == 0 || Long
					.parseLong(substr($result.toString(), 0, 10))
					- time() > 0)/*
									 * && substr($result.toString(), 10,
									 * 16).equals(
									 * substr(md5(substr($result.toString(),
									 * 26)+ $keyb), 0, 16))
									 */) {
				return substr($result.toString(), 26);
			} else {
				return "";
			}
		} else {
			return $keyc
					+ base64_encode($result1.toString()).replaceAll("=", "");
		}
	}

	/**
	 * Զ�̴�URL
	 * 
	 * @param string
	 *            $url �򿪵�url�� �� http://www.baidu.com/123.htm
	 * @param int
	 *            $limit ȡ���ص����ݵĳ���
	 * @param string
	 *            $post Ҫ���͵� POST ���ݣ���uid=1&password=1234
	 * @param string
	 *            $cookie Ҫģ��� COOKIE ���ݣ���uid=123&auth=a2323sd2323
	 * @param bool
	 *            $bysocket TRUE/FALSE �Ƿ�ͨ��SOCKET��
	 * @param string
	 *            $ip IP��ַ
	 * @param int
	 *            $timeout ���ӳ�ʱʱ��
	 * @param bool
	 *            $block �Ƿ�Ϊ����ģʽ defaul valuet:true
	 * @return ȡ�����ַ���
	 */
	protected String uc_fopen2(String $url, int $limit, String $post,
			String $cookie, boolean $bysocket, String $ip, int $timeout,
			boolean $block) {
		// long $__times__ = isset($_GET["__times__"]) ?
		// intval($_GET["__times__"]) + 1 : 1;
		// if($__times__ > 2) {
		// return "";
		// }
		$url += $url.indexOf("?") > 0 ? "&" : "?" + "__times__=1";
		return uc_fopen($url, $limit, $post, $cookie, $bysocket, $ip, $timeout,
				$block);
	}

	protected String uc_fopen(String $url, int $limit, String $post,
			String $cookie, boolean $bysocket, String $ip, int $timeout,
			boolean $block) {
		String $return = "";

		URL $matches;
		String $host = "";
		String $path = "";
		int $port = 80;
		try {
			$matches = new URL($url);
			$host = $matches.getHost();
			$path = $matches.getPath() != null ? $matches.getPath()
					+ ($matches.getQuery() != null ? "?" + $matches.getQuery()
							: "") : "/";
			if ($matches.getPort() > 0)
				$port = $matches.getPort();
		} catch (MalformedURLException e1) {
		}

		StringBuffer $out = new StringBuffer();
		if ($post != null && $post.length() > 0) {
			$out.append("POST ").append($path).append(" HTTP/1.0\r\n");
			$out.append("Accept: */*\r\n");
			$out.append("Accept-Language: zh-cn\r\n");
			$out.append("Content-Type: application/x-www-form-urlencoded\r\n");
			$out.append("User-Agent: \r\n");
			$out.append("Host: ").append($host).append("\r\n");
			$out.append("Content-Length: ").append($post.length()).append(
					"\r\n");
			$out.append("Connection: Close\r\n");
			$out.append("Cache-Control: no-cache\r\n");
			$out.append("Cookie: \r\n\r\n");
			$out.append($post);
		} else {
			$out.append("GET $path HTTP/1.0\r\n");
			$out.append("Accept: */*\r\n");
			// $out .= "Referer: $boardurl\r\n";
			$out.append("Accept-Language: zh-cn\r\n");
			$out.append("User-Agent: Java/1.5.0_01\r\n");
			$out.append("Host: $host\r\n");
			$out.append("Connection: Close\r\n");
			$out.append("Cookie: $cookie\r\n\r\n");
		}

		try {
			Socket $fp = new Socket($ip != null && $ip.length() > 10 ? $ip
					: $host, $port);
			if (!$fp.isConnected()) {
				return "";// note $errstr : $errno \r\n
			} else {

				OutputStream os = $fp.getOutputStream();
				os.write($out.toString().getBytes());

				InputStream ins = $fp.getInputStream();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(ins, "utf-8"));
				while (true) {
					String $header = reader.readLine();
					if ($header == null || $header.equals("")
							|| $header == "\r\n" || $header == "\n") {
						break;
					}
				}

				while (true) {
					String $data = reader.readLine();
					if ($data == null || $data.equals("")) {
						break;
					} else {
						$return += $data;
					}
				}

				$fp.close();
			}
		} catch (IOException e) {

		}
		return $return;
	}

	public String uc_app_ls() {
		String $return = call_user_func(UC_API_FUNC, "app", "ls", null);
		return UC_CONNECT.equals("mysql") ? $return : uc_unserialize($return);
	}

	/**
	 * �û�ע��
	 * 
	 * @param string
	 *            $username �û���
	 * @param string
	 *            $password ����
	 * @param string
	 *            $phone ��ϵ�绰
	 * @param int
	 *            $questionid ��ȫ����
	 * @param string
	 *            $answer ��ȫ���ʴ�
	 * @return int -1 : �û������Ϸ� -2 : ����������ע��Ĵ��� -3 : �û����Ѿ����� -4 : email ��ʽ���� -5 :
	 *         email ������ע�� -6 : �� email �Ѿ���ע�� >1 : ��ʾ�ɹ�����ֵΪ UID
	 */
	public String uc_user_register(String $username, String $password,
			String $phone) {
		return uc_user_register($username, $password, $phone, "", "");
	}

	public String uc_user_register(String $username, String $password,
			String $phone, String $questionid, String $answer) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("username", $username);
		args.put("password", $password);
		args.put("phone", $phone);
		args.put("questionid", $questionid);
		args.put("answer", $answer);
		return call_user_func(UC_API_FUNC, "user", "register", args);
	}

	/**
	 * �û���½���
	 * 
	 * @param string
	 *            $username �û���/uid
	 * @param string
	 *            $password ����
	 * @param int
	 *            $isuid �Ƿ�Ϊuid
	 * @param int
	 *            $checkques �Ƿ�ʹ�ü�鰲ȫ�ʴ�
	 * @param int
	 *            $questionid ��ȫ����
	 * @param string
	 *            $answer ��ȫ���ʴ�
	 * @return array (uid/status, username, password, email) �����һ�� 1 : �ɹ� -1 :
	 *         �û�������,���߱�ɾ�� -2 : �����
	 */
	public String uc_user_login(String $username, String $password) {
		return uc_user_login($username, $password, 0, 0);
	}

	public String uc_user_login(String $username, String $password, int $isuid,
			int $checkques) {
		return uc_user_login($username, $password, $isuid, $checkques, "", "");
	}

	public String uc_user_login(String $username, String $password, int $isuid,
			int $checkques, String $questionid, String $answer) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("username", $username);
		args.put("password", $password);
		args.put("isuid", $isuid);
		args.put("checkques", $checkques);
		args.put("questionid", $questionid);
		args.put("answer", $answer);
		String $return = call_user_func(UC_API_FUNC, "user", "login", args);
		return UC_CONNECT.equals("mysql") ? $return : uc_unserialize($return);
	}

	/**
	 * ����ͬ����¼����
	 * 
	 * @param int
	 *            $uid �û�ID
	 * @return string HTML����
	 */
	public String uc_user_synlogin(int $uid) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("uid", $uid);
		String $return = uc_api_post("user", "synlogin", args);
		return $return;
	}

	/**
	 * ����ͬ���ǳ�����
	 * 
	 * @return string HTML����
	 */
	public String uc_user_synlogout() {
		String $return = uc_api_post("user", "synlogout",
				new HashMap<String, Object>());
		return $return;
	}

	/**
	 * ȡ���û�����
	 * 
	 * @param string
	 *            $username �û���
	 * @param int
	 *            $isuid �Ƿ�ΪUID
	 * @return array (uid, username, email)
	 */
	public String uc_get_user(String $username, int $isuid) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("username", $username);
		args.put("isuid", $isuid);
		String $return = call_user_func(UC_API_FUNC, "user", "get_user", args);
		return UC_CONNECT.equals("mysql") ? $return : uc_unserialize($return);
	}

	/**
	 * �༭�û�
	 * 
	 * @param string
	 *            $username �û���
	 * @param string
	 *            $oldpw ������
	 * @param string
	 *            $newpw ������
	 * @param string
	 *            $email Email
	 * @param int
	 *            $ignoreoldpw �Ƿ���Ծ�����, ���Ծ�����, �򲻽��о�����У��.
	 * @param int
	 *            $questionid ��ȫ����
	 * @param string
	 *            $answer ��ȫ���ʴ�
	 * @return int 1 : �޸ĳɹ� 0 : û���κ��޸� -1 : �����벻��ȷ -4 : email ��ʽ���� -5 : email
	 *         ������ע�� -6 : �� email �Ѿ���ע�� -7 : û�����κ��޸� -8 : �ܱ������û���û��Ȩ���޸�
	 */
	public String uc_user_edit(String $username, String $oldpw, String $newpw,
			String $email, int $ignoreoldpw, String $questionid, String $answer) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("username", $username);
		args.put("oldpw", $oldpw);
		args.put("newpw", $newpw);
		args.put("email", $email);
		args.put("ignoreoldpw", $ignoreoldpw);
		args.put("questionid", $questionid);
		args.put("answer", $answer);
		return call_user_func(UC_API_FUNC, "user", "edit", args);
	}

	/**
	 * ɾ���û�
	 * 
	 * @param string/array
	 *            $uid �û��� UID
	 * @return int >0 : �ɹ� 0 : ʧ��
	 */
	public String uc_user_delete(String $uid) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("uid", $uid);
		return call_user_func(UC_API_FUNC, "user", "delete", args);
	}

	/**
	 * ɾ���û�ͷ��
	 * 
	 * @param string/array
	 *            $uid �û��� UID
	 */
	public String uc_user_deleteavatar(String $uid) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("uid", $uid);
		return uc_api_post("user", "deleteavatar", args);
	}

	/**
	 * �ж��û����Ƿ����
	 * 
	 * @param string/array
	 *            username �û��� UID
	 * 
	 * �û����жϳ���	-1 
	 * �û������Ϸ�	-2
	 * �û�������		-3 
	 * �û���������	+1
	 */
	public String uc_user_checkname(String username) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("username", username);
		return call_user_func(UC_API_FUNC, "user", "check_username", args);
	}
	
	/**
	 * 
	 * @author Administrator
	 * @date Mar 8, 2013
	 * @param $uid
	 * @param $friendid
	 * @param $comment ��������
	 * @return
	 * @Description ��Ӻ���
	 */
	public String uc_friendrelation_add(int $uid, int $friendid,String $comment) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("uid", $uid);
		args.put("friendid", $friendid);
		args.put("friendname", $comment);
		return call_user_func(UC_API_FUNC, "friendrelation", "add", args); 
    } 
    
	/**
	 * 
	 * @author Administrator
	 * @date Mar 8, 2013
	 * @param $uid
	 * @param $friendids ����id��
	 * @return  ����ɾ��������
	 * @Description  ����ɾ������
	 */
	public String uc_friendrelation_delete(int $uid, String $friendids) { 
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("uid", $uid);
		
		Map<String, Integer> uids = new HashMap<String, Integer>();
		
		//List<Integer> argids=  new ArrayList<Integer>();
		
		if($friendids.indexOf(",")>0){
			String[] ids=$friendids.split(",");
			for(int i=0;i<ids.length;i++){
				//argids.add(Integer.parseInt(ids[i]));
				uids.put(i+"", Integer.parseInt(ids[i]));
				
			}
		}else{
			//argids.add(Integer.parseInt($friendids));
			uids.put(0+"", Integer.parseInt($friendids));
		}
		
		//Integer[] deleteids=new Integer[argids.size()];
		
	//	argids.toArray(deleteids);
		
		args.put("friendids", uids);
		return call_user_func(UC_API_FUNC, "friendrelation", "delete", args); 
	} 
	
	/**
	 * 
	 * @author Administrator
	 * @date Mar 8, 2013
	 * @param $uid
	 * @param $status Ĭ�ϴ���1
	 * @return
	 * @Description ��ȡ��������
	 */

	public String uc_friendrelation_totalnum(int $uid, int $status) { 
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("uid", $uid);
		args.put("status", $status);
		return call_user_func(UC_API_FUNC, "friendrelation", "totalnum",args); 
	} 
    
	/**
	 * 
	 * @author Administrator
	 * @date Mar 8, 2013
	 * @param $uid
	 * @param $page �ڼ�ҳ
	 * @param $pagesize ÿҳ�Ĵ�С
	 * @param $totalnum
	 * @param $status  Ĭ��Ϊ1
	 * @return
	 * @Description ��ȡ�����б�
	 */
	public String uc_friendrelation_ls(int $uid,int $page,int $pagesize , int $totalnum ,int $status ) { 
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("uid", $uid);
		args.put("page", $page);
		args.put("pagesize", $pagesize);
		args.put("totalnum", $totalnum);
		args.put("status", $status);
		
		String $return = call_user_func(UC_API_FUNC, "friendrelation", "ls", args); 
		return UC_CONNECT == "mysql" ? $return : uc_unserialize($return); 
		}
	
	
	/**
	 * 
	 * @author Administrator
	 * @date Mar 8, 2013
	 * @param $uid
	 * @param $keyword ��ѯ�ؼ���
	 * @return
	 * @Description ģ����ѯ����
	 */
	public String uc_friendrelation_search(int $uid,String $keyword) { 
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("uid", $uid);
		args.put("keyword", $keyword);
		String $return = call_user_func(UC_API_FUNC, "friendrelation", "search", args); 
		return UC_CONNECT == "mysql" ? $return : uc_unserialize($return); 
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		System.out.println(client.uc_user_register("43555", "451120", "15927451120"));
	}

}
