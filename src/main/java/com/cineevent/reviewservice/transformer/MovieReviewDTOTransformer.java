package com.cineevent.reviewservice.transformer;

import java.util.Date;

import org.apache.logging.log4j.util.Strings;

import com.cineevent.reviewservice.dto.request.ReviewRequestDTO;
import com.cineevent.reviewservice.dto.response.MovieReviewResponseDTO;
import com.cineevent.reviewservice.dto.response.UserAuthResponseDTO;
import com.cineevent.reviewservice.model.MovieReview;
import com.cineevent.reviewservice.security.ThreadLocalAuthStore;

public final class MovieReviewDTOTransformer {
	
	private MovieReviewDTOTransformer() {
		 throw new IllegalStateException("Tranformer class");
	}
	
	public static MovieReview transformToReview(ReviewRequestDTO reviewRequestDTO, String movieId) {
		MovieReview review = new MovieReview();
		
		UserAuthResponseDTO userAuthResponseDTO = ThreadLocalAuthStore.getAuthDetails();
		int userId = 0;
		if(userAuthResponseDTO != null) {
			userId = userAuthResponseDTO.getUserId();
		}
		review.setUserId(userId);
		review.setMovieId(movieId);
		review.setReviewDescription(reviewRequestDTO.getReviewDescription());
		review.setStarRating(Integer.parseInt(reviewRequestDTO.getStarRating()));
		review.setReviewDate(System.currentTimeMillis());
		
		return review;
	}

	public static MovieReviewResponseDTO transformToReviewResponseDTO(MovieReview review) {
		MovieReviewResponseDTO reviewResponseDTO = new MovieReviewResponseDTO();
		
		reviewResponseDTO.setReviewId(review.getId());
		reviewResponseDTO.setReviewDate(new Date(review.getReviewDate()));
		reviewResponseDTO.setStarRating(review.getStarRating());
		reviewResponseDTO.setReviewDescription(review.getReviewDescription());
		reviewResponseDTO.setMovieId(review.getMovieId());
		reviewResponseDTO.setUserId(review.getUserId());
		
		return reviewResponseDTO;
	}

	public static void updateReviewFromDB(MovieReview reviewFromDB, ReviewRequestDTO reviewRequestDTO) {
		
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
