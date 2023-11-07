package com.cineevent.reviewservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "EventReviews")
public class EventReview {
	
	 @Id
	 private String id;
	 
	 private String eventId;
	 
	 private int userId;
	 
	 private int starRating;
	 
	 private String reviewDescription;
	 
	 private long reviewDate;

}
