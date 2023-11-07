package com.cineevent.reviewservice.dto.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class ReviewResponseDTO {

	private String reviewId;
	
	private int starRating;

	private String reviewDescription;

	private Date reviewDate;
	
	private int userId;
}
