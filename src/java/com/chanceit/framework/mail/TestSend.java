package com.chanceit.framework.mail;

public class TestSend {
	

	public static void main(String[] args){
		   //�������Ҫ�������ʼ�  
	      MailSenderInfo mailInfo = new MailSenderInfo();   
	      mailInfo.setMailServerHost("smtp.163.com");   
	      mailInfo.setMailServerPort("25");   
	      mailInfo.setValidate(true);   
	      mailInfo.setUserName("tmniquan@163.com");   
	      mailInfo.setPassword("nq31549363");//������������   
	      mailInfo.setFromAddress("tmniquan@163.com");   
	      mailInfo.setToAddress("njniquan@163.com");   
	      mailInfo.setSubject("�����������");   
	    /*  mailInfo.setContent("<input type='text' id='boxtrackpwd' /><br/>" +
	      		"<input type='button' value='�ύ' id='boxtrackbtn'><script>" +
	      		"document.getElementById('boxtrackbtn').onclick(" +
	      		"if(!document.getElementById('boxtrackpwd').value)alert('������������');else{" +
	      		"alert('')" +
	      		"}" +
	      		");" +
	      		"</script>");*/
	      mailInfo.setContent("<form action='' method=''><input  name='boxtrackedpwd'/>" +
	      		"<br/><input value='�ύ' id='boxtrackbtn'></form><script>" +
	      		"document.getElementById('boxtrackbtn').onclick(" +
	      		"if(document.ElementById('boxtrackpwd').value)alert" +
	      		")" + 
	      		"</script>");
	      //�������Ҫ�������ʼ�  
	      SimpleMailSender sms = new SimpleMailSender();  
	       //   sms.sendTextMail(mailInfo);//���������ʽ   
	         SimpleMailSender.sendHtmlMail(mailInfo);//����html��ʽ 
	}
}
