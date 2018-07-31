package com.chanceit.framework.utils;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageProducers {
    
    @Autowired
    private RabbitTemplate  amqpTemplate;
     
    public void sendDataToCrQueue(Object obj) {
        amqpTemplate.convertAndSend("line", obj);
    }   
    
    public static void main(String[] args) {
    	long current = System.currentTimeMillis()/1000;
    	System.out.println(current);
	}

	public RabbitTemplate getAmqpTemplate() {
		return amqpTemplate;
	}

	public void setAmqpTemplate(RabbitTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}
}
