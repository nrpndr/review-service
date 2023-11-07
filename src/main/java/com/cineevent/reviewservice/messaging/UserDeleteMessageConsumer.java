package com.cineevent.reviewservice.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cineevent.reviewservice.dto.request.UserMQMessage;
import com.cineevent.reviewservice.service.EventReviewService;
import com.cineevent.reviewservice.service.MovieReviewService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserDeleteMessageConsumer {
	
	@Autowired
	private MovieReviewService movieReviewService;
	
	@Autowired
	private EventReviewService eventReviewService;
	
	private Gson gson = new Gson();
	
    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(String message){
        log.info("Message arrived! Message: " + message);
        UserMQMessage userMQMessage = null;
        try {
        	userMQMessage = gson.fromJson(message, UserMQMessage.class);
        } catch(JsonSyntaxException ex) {
        	log.error("Error converting message to UserMQMessage. Ignoring the message {}, errorMessage={}", message, ex.getMessage());
        }
        if(userMQMessage != null) {
	        movieReviewService.deleteReviewByUserId(userMQMessage.getUserId());
	        eventReviewService.deleteReviewByUserId(userMQMessage.getUserId());
        }
    }

}