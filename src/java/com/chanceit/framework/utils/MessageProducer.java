package com.chanceit.framework.utils;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Transactional
@Component("messageProducer")
public class MessageProducer {
    
    @Autowired
    private RabbitTemplate  rabbitTemplate;
     
    public void sendDataToCrQueue(String type,Object obj) {
    	rabbitTemplate.convertAndSend(type, obj);
    }   
    
    public static void main(String[] args) {
    	long current = System.currentTimeMillis()/1000;
    	System.out.println(current);
	}

	public RabbitTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

}
