<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Curl
 *
 * @author Administrator
 */
class Curl {
    private $_cookie;
    private $_ch;
    private $_header;
    
    public function __construct() {
        $this->_cookie = dirname(__FILE__)."/cookie_".md5(basename(__FILE__)).".txt"; 
        $this->_ch = curl_init();
        $this->_header = 'content-type: application/x-www-form-urlencoded;charset=UTF-8';
        
        if(!file_exists($this->_cookie)) {//检查cookie是否存在
            
        }
    }
    
    public function setCharset($charset) {
        $this->_header = 'content-type: application/x-www-form-urlencoded;charset='.$charset;
    }
    
    public function __destruct() {
        if($this->_ch != null ) {
            curl_close($this->_ch);
        }
    }
    
    public function close() {
        curl_close($this->_ch);
    }
    
    public function get($url,$params = array()) {
        curl_setopt($this->_ch,CURLOPT_HTTPHEADER,array($this->_header));
        curl_setopt($this->_ch, CURLOPT_URL, $url.http_build_query($params));
        curl_setopt($this->_ch, CURLOPT_HTTPGET, 1);
        curl_setopt($this->_ch, CURLOPT_HEADER, 0);
        curl_setopt($this->_ch, CURLOPT_AUTOREFERER, 1);
        curl_setopt($this->_ch, CURLOPT_USERAGENT, $_SERVER['HTTP_USER_AGENT']); 
        //curl_setopt($this->_ch, CURLOPT_COOKIESESSION, 1); //生效cookie就没有session_id了
        curl_setopt($this->_ch, CURLOPT_COOKIEFILE, $this->_cookie);
        curl_setopt($this->_ch, CURLOPT_COOKIEJAR, $this->_cookie);
        curl_setopt($this->_ch, CURLOPT_TIMEOUT, 30);
        curl_setopt($this->_ch, CURLOPT_RETURNTRANSFER, 1);
        
        $respTxt = curl_exec($this->_ch);
        return $respTxt;
    }
    
    /*
    public function get($url,$params = array()) {
        $ch = curl_init($url.  http_build_query($params));
        curl_setopt($ch, CURLOPT_HTTPGET, 1);
        curl_setopt($ch, CURLOPT_HEADER, 0);
        curl_setopt($ch, CURLOPT_AUTOREFERER, 1);
        curl_setopt($ch, CURLOPT_USERAGENT, $_SERVER['HTTP_USER_AGENT']); 
        curl_setopt($ch, CURLOPT_COOKIESESSION, 1);
        curl_setopt($ch, CURLOPT_COOKIEFILE, $this->_cookie);
        curl_setopt($ch, CURLOPT_COOKIEJAR, $this->_cookie);
        curl_setopt($ch, CURLOPT_TIMEOUT, 30);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        $respTxt = curl_exec($ch);
        curl_close($ch);
        return $respTxt;
    }*/
    
    
    public function post($url,$params = array()) {
        
    }
    
    public function login($url,$params) {
        $respTxt = $this->get($url, $params);
        $jsonData = $respTxt ? json_decode($respTxt) : null;
        
        if($jsonData) {
            foreach($jsonData->srcs as $src) {
                if(strpos($src, 'app_login.jsp')) {
                   $this->get($src);
                   break;
                }
            }
        }
        return $respTxt;
    }
}

?>
