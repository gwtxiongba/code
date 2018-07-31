package com.chanceit.framework.mail;

public class TestSend {
	

	public static void main(String[] args){
		   //这个类主要是设置邮件  
	      MailSenderInfo mailInfo = new MailSenderInfo();   
	      mailInfo.setMailServerHost("smtp.163.com");   
	      mailInfo.setMailServerPort("25");   
	      mailInfo.setValidate(true);   
	      mailInfo.setUserName("tmniquan@163.com");   
	      mailInfo.setPassword("nq31549363");//您的邮箱密码   
	      mailInfo.setFromAddress("tmniquan@163.com");   
	      mailInfo.setToAddress("njniquan@163.com");   
	      mailInfo.setSubject("设置邮箱标题");   
	    /*  mailInfo.setContent("<input type='text' id='boxtrackpwd' /><br/>" +
	      		"<input type='button' value='提交' id='boxtrackbtn'><script>" +
	      		"document.getElementById('boxtrackbtn').onclick(" +
	      		"if(!document.getElementById('boxtrackpwd').value)alert('请输入新密码');else{" +
	      		"alert('')" +
	      		"}" +
	      		");" +
	      		"</script>");*/
	      mailInfo.setContent("<form action='' method=''><input  name='boxtrackedpwd'/>" +
	      		"<br/><input value='提交' id='boxtrackbtn'></form><script>" +
	      		"document.getElementById('boxtrackbtn').onclick(" +
	      		"if(document.ElementById('boxtrackpwd').value)alert" +
	      		")" + 
	      		"</script>");
	      //这个类主要来发送邮件  
	      SimpleMailSender sms = new SimpleMailSender();  
	       //   sms.sendTextMail(mailInfo);//发送文体格式   
	         SimpleMailSender.sendHtmlMail(mailInfo);//发送html格式 
	}
}
