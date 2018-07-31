package com.chanceit.framework.mail;

import java.util.Properties;


public class SmtpMailSend {
	private static Properties cache = new Properties();
	static{
		try {
			cache.load(SmtpMailSend.class.getClassLoader().getResourceAsStream("smtpmail.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getValue(String key){
		return cache.getProperty(key);
	}
	public static boolean sendSMTPMail(String subject,String content,String toEmail,String type){
		 MailSenderInfo mailInfo = new MailSenderInfo();   
		 mailInfo.setMailServerHost(cache.getProperty("serverHost"));   
	      mailInfo.setMailServerPort(cache.getProperty("port"));   
	      mailInfo.setValidate(Boolean.parseBoolean(cache.getProperty("validate")));  
	      mailInfo.setUserName(cache.getProperty("userName"));   
	      mailInfo.setPassword(cache.getProperty("password"));//ƒ˙µƒ” œ‰√‹¬Î   
	      mailInfo.setFromAddress(cache.getProperty("sendMail"));  
	      mailInfo.setToAddress(toEmail);
	      mailInfo.setSubject(subject);
	      mailInfo.setContent(content);
	      SimpleMailSender sms = new SimpleMailSender();  
	      if("html".equals(type)){
	    	  return SimpleMailSender.sendHtmlMail(mailInfo);
	      }
	      return sms.sendTextMail(mailInfo);
	}
}
