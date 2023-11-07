package com.cineevent.reviewservice.transformer;

import java.util.Date;

import org.apache.logging.log4j.util.Strings;

import com.cineevent.reviewservice.dto.request.ReviewRequestDTO;
import com.cineevent.reviewservice.dto.response.EventReviewResponseDTO;
import com.cineevent.reviewservice.dto.response.UserAuthResponseDTO;
import com.cineevent.reviewservice.model.EventReview;
import com.cineevent.reviewservice.security.ThreadLocalAuthStore;

public final class EventReviewDTOTransformer {
	
	private EventReviewDTOTransformer() {
		 throw new IllegalStateException("Tranformer class");
	}
	
	public static EventReview transformToReview(ReviewRequestDTO reviewRequestDTO, String eventId) {
		EventReview review = new EventReview();
		UserAuthResponseDTO userAuthResponseDTO = ThreadLocalAuthStore.getAuthDetails();
		int userId = 0;
		if(userAuthResponseDTO != null) {
			userId = userAuthResponseDTO.getUserId();
		}
		review.setUserId(userId);
		review.setEventId(eventId);
		review.setReviewDescription(reviewRequestDTO.getReviewDescription());
		review.setStarRating(Integer.parseInt(reviewRequestDTO.getStarRating()));
		review.setReviewDate(System.currentTimeMillis());
		
		return review;
	}

	public static EventReviewResponseDTO transformToReviewResponseDTO(EventReview review) {
		EventReviewResponseDTO eventResponseDTO = new EventReviewResponseDTO();
		
		eventResponseDTO.setReviewId(review.getId());
		eventResponseDTO.setReviewDate(new Date(review.getReviewDate()));
		eventResponseDTO.setStarRating(review.getStarRating());
		eventResponseDTO.setReviewDescription(review.getReviewDescription());
		eventResponseDTO.setEventId(review.getEventId());
		eventResponseDTO.setUserId(review.getUserId());
		
		return eventResponseDTO;
	}

	public static void updateReviewFromDB(EventReview reviewFromDB, ReviewRequestDTO reviewRequestDTO) {
		
		boolean isUpdated = false;
		
		if (Strings.isNotBlank(reviewRequestDTO.getStarRating())) {
			reviewFromDB.setStarRating(Integer.parseInt(reviewRequestDTO.getStarRating()));
			isUpdated = true;
		}
		
		if (Strings.isNotBlank(reviewRequestDTO.getReviewDescription())) {
			reviewFromDB.setReviewDescription(reviewRequestDTO.getReviewDescription());
			isUpdated = true;
		}
		
		if(isUpdated) {
			reviewFromDB.setReviewDate(System.currentTimeMillis());
		}
	}

}
