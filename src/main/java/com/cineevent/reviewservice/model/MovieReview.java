package com.cineevent.reviewservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "MovieReviews")
public class MovieReview {
	
	 @Id
	 private String id;
	 
	 private String movieId;
	 
	 private int userId;
	 
	 private int starRating;
	 
	 private String reviewDescription;
	 
	 private long reviewDate;

}
