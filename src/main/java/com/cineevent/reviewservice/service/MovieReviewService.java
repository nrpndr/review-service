package com.cineevent.reviewservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cineevent.reviewservice.dto.request.ReviewRequestDTO;
import com.cineevent.reviewservice.dto.response.MovieReviewResponseDTO;
import com.cineevent.reviewservice.dto.response.UserAuthResponseDTO;
import com.cineevent.reviewservice.exceptions.InValidUserInputException;
import com.cineevent.reviewservice.exceptions.ReviewDoesNotExistException;
import com.cineevent.reviewservice.exceptions.UserDoesNotHavePermissionException;
import com.cineevent.reviewservice.model.MovieReview;
import com.cineevent.reviewservice.repository.MovieReviewRepository;
import com.cineevent.reviewservice.security.ThreadLocalAuthStore;
import com.cineevent.reviewservice.transformer.MovieReviewDTOTransformer;

import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MovieReviewService {

	@Autowired
	private MovieReviewRepository movieReviewRepository;
	
	public MovieReviewResponseDTO createReview(ReviewRequestDTO reviewRequestDTO, String movieId) {
	   validateInput(reviewRequestDTO, movieId);
	   MovieReview review = MovieReviewDTOTransformer.transformToReview(reviewRequestDTO, movieId);
	   movieReviewRepository.save(review);
	   
	   return MovieReviewDTOTransformer.transformToReviewResponseDTO(review);
	}

	private void validateInput(ReviewRequestDTO reviewRequestDTO, String movieId) {
		
		validateMovieId(movieId);
		
		if (reviewRequestDTO == null) {
			throw new InValidUserInputException("ReviewRequest Input cannot be null");
		} else if (reviewRequestDTO.getStarRating() == null) {
			throw new InValidUserInputException("StarRating is missing in input");
		}
		   
		String starRating = reviewRequestDTO.getStarRating();

		int starRatingInt = 0;
		try {
			starRatingInt = Integer.parseInt(starRating);
			if (starRatingInt < 0 || starRatingInt > 5) {
				throw new InValidUserInputException("StarRating value should be between 1 to 5");
			}
		} catch (NumberFormatException e) {
			throw new InValidUserInputException("StarRating should be a valid integer");
		}
	}
	
	private void validateMovieId(String movieId) {
		if(StringUtils.isBlank(movieId)) {
			throw new InValidUserInputException("movieId should not be null");
		}
	}

	public List<MovieReviewResponseDTO> getAllReviews(String movieId) {
		validateMovieId(movieId);
		List<MovieReviewResponseDTO> movieReviewResponseDTOs = new ArrayList<>();
		List<MovieReview> reviews = movieReviewRepository.findByMovieId(movieId, Sort.by(Sort.Direction.DESC, "reviewDate"));
		if(!CollectionUtils.isEmpty(reviews)) {
			for(MovieReview review : reviews) {
				movieReviewResponseDTOs.add(MovieReviewDTOTransformer.transformToReviewResponseDTO(review));
			}
		}
		return movieReviewResponseDTOs;
	}

	public MovieReviewResponseDTO getReview(String movieId, String reviewId) {
		validateMovieId(movieId);
		validateReviewId(reviewId);
		MovieReview review = movieReviewRepository.findById(reviewId).orElse(null);
		if(review != null && review.getMovieId().equals(movieId)) {
			return MovieReviewDTOTransformer.transformToReviewResponseDTO(review);
		}else {
			throw constructReviewDoesNotExistException(movieId, reviewId);
		}
	}

	private ReviewDoesNotExistException constructReviewDoesNotExistException(String movieId, String reviewId) {
		String msg = String.format("There is no review with id %s for movie with id %s", reviewId, movieId);
		return new ReviewDoesNotExistException(msg);
	}
	
	private void validateReviewId(String reviewId) {
		if(StringUtils.isBlank(reviewId)) {
			throw new InValidUserInputException("reviewId should not be null");
		}
	}

	public void deleteReview(String movieId, String reviewId) {
		MovieReview review = movieReviewRepository.findById(reviewId).orElse(null);
		
		if(review != null && review.getMovieId().equals(movieId)) {
			boolean isAllowedToModifyOrDelete = isAllowedToModifyOrDelete(review.getUserId());
			if(isAllowedToModifyOrDelete) {
				movieReviewRepository.deleteById(reviewId);
			} else {
				throw new UserDoesNotHavePermissionException("Not Authorized to delete");
			}
		}else {
			throw constructReviewDoesNotExistException(movieId, reviewId);
		}
		log.info("Review with id {} for movie id {} has been deleted.", reviewId, movieId);
	}

	public MovieReviewResponseDTO updateReview(String movieId, String reviewId, ReviewRequestDTO reviewRequestDTO) {
		validateMovieId(movieId);
		validateReviewId(reviewId);
		validateInputForUpdate(reviewRequestDTO);
		
		MovieReview reviewFromDB = movieReviewRepository.findById(reviewId).orElse(null);
		if(reviewFromDB == null || reviewFromDB.getMovieId().equals(movieId)) {
			throw constructReviewDoesNotExistException(movieId, reviewId);
		}
		
		boolean isAllowedToModifyOrDelete = isAllowedToModifyOrDelete(reviewFromDB.getUserId());
		
		if(isAllowedToModifyOrDelete) {
			MovieReviewDTOTransformer.updateReviewFromDB(reviewFromDB, reviewRequestDTO);
			movieReviewRepository.save(reviewFromDB);
			return MovieReviewDTOTransformer.transformToReviewResponseDTO(reviewFromDB);
		} else {
			throw new UserDoesNotHavePermissionException("Not Authorized to update");
		}
	}
	
	private boolean isAllowedToModifyOrDelete(int userIdForReviewInDB) {
		UserAuthResponseDTO userAuthResponseDTO =  ThreadLocalAuthStore.getAuthDetails();
		int userId = userAuthResponseDTO.getUserId();
		String userRole = userAuthResponseDTO.getUserRole();
		
		return userIdForReviewInDB == userId || userRole.equalsIgnoreCase("ADMIN");
	}
	
	private void validateInputForUpdate(ReviewRequestDTO reviewRequestDTO) {
		
		if (reviewRequestDTO == null) {
			throw new InValidUserInputException("ReviewRequest Input cannot be null");
		}
		   
		String starRating = reviewRequestDTO.getStarRating();

		int starRatingInt = 0;
		try {
			starRatingInt = Integer.parseInt(starRating);
			if (starRatingInt < 0 || starRatingInt > 5) {
				throw new InValidUserInputException("StarRating value should be between 1 to 5");
			}
		} catch (NumberFormatException e) {
			throw new InValidUserInputException("StarRating should be a valid integer");
		}
	}

	public void deleteReviewByUserId(int userId) {
		log.info("deleteReviewByUserId:: User Id {}", userId);
		List<MovieReview> movieReviews = movieReviewRepository.findByUserId(userId);
		if(!CollectionUtils.isEmpty(movieReviews)) {
			log.info("deleteReviewByUserId:: Number of reviews by userid {} are {}", userId, movieReviews.size());
			movieReviewRepository.deleteByUserId(userId);
		} else {
			log.info("deleteReviewByUserId:: No reviews by userid {}, no deletion required", userId);
		}
	}
}
