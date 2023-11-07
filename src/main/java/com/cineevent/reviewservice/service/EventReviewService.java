package com.cineevent.reviewservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cineevent.reviewservice.dto.request.ReviewRequestDTO;
import com.cineevent.reviewservice.dto.response.EventReviewResponseDTO;
import com.cineevent.reviewservice.dto.response.UserAuthResponseDTO;
import com.cineevent.reviewservice.exceptions.InValidUserInputException;
import com.cineevent.reviewservice.exceptions.ReviewDoesNotExistException;
import com.cineevent.reviewservice.exceptions.UserDoesNotHavePermissionException;
import com.cineevent.reviewservice.model.EventReview;
import com.cineevent.reviewservice.repository.EventReviewRepository;
import com.cineevent.reviewservice.security.ThreadLocalAuthStore;
import com.cineevent.reviewservice.transformer.EventReviewDTOTransformer;

import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EventReviewService {

	@Autowired
	private EventReviewRepository eventReviewRepository;
	
	public EventReviewResponseDTO createReview(ReviewRequestDTO reviewRequestDTO, String eventId) {
	   validateInput(reviewRequestDTO, eventId);
	   EventReview review = EventReviewDTOTransformer.transformToReview(reviewRequestDTO, eventId);
	   eventReviewRepository.save(review);
	   
	   return EventReviewDTOTransformer.transformToReviewResponseDTO(review);
	}

	private void validateInput(ReviewRequestDTO reviewRequestDTO, String eventId) {
		
		validateEventId(eventId);
		
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
	
	private void validateEventId(String eventId) {
		if(StringUtils.isBlank(eventId)) {
			throw new InValidUserInputException("eventId should not be null");
		}
	}

	public List<EventReviewResponseDTO> getAllReviews(String eventId) {
		validateEventId(eventId);
		List<EventReviewResponseDTO> eventReviewResponseDTOs = new ArrayList<>();
		List<EventReview> reviews = eventReviewRepository.findByEventId(eventId, Sort.by(Sort.Direction.DESC, "reviewDate"));
		if(!CollectionUtils.isEmpty(reviews)) {
			for(EventReview review : reviews) {
				eventReviewResponseDTOs.add(EventReviewDTOTransformer.transformToReviewResponseDTO(review));
			}
		}
		return eventReviewResponseDTOs;
	}

	public EventReviewResponseDTO getReview(String eventId, String reviewId) {
		validateEventId(eventId);
		validateReviewId(reviewId);
		EventReview review = eventReviewRepository.findById(reviewId).orElse(null);
		if(review != null && review.getEventId().equals(eventId)) {
			return EventReviewDTOTransformer.transformToReviewResponseDTO(review);
		}else {
			throw constructReviewDoesNotExistException(eventId, reviewId);
		}
	}

	private ReviewDoesNotExistException constructReviewDoesNotExistException(String eventId, String reviewId) {
		String msg = String.format("There is no review with id %s for event with id %s", reviewId, eventId);
		return new ReviewDoesNotExistException(msg);
	}
	
	private void validateReviewId(String reviewId) {
		if(StringUtils.isBlank(reviewId)) {
			throw new InValidUserInputException("reviewId should not be null");
		}
	}

	public void deleteReview(String eventId, String reviewId) {
		EventReview review = eventReviewRepository.findById(reviewId).orElse(null);
		
		if(review != null && review.getEventId().equals(eventId)) {
			boolean isAllowedToModifyOrDelete = isAllowedToModifyOrDelete(review.getUserId());
			if(isAllowedToModifyOrDelete) {
				eventReviewRepository.deleteById(reviewId);
			} else {
				throw new UserDoesNotHavePermissionException("Not Authorized to delete");
			}
		}else {
			throw constructReviewDoesNotExistException(eventId, reviewId);
		}
		log.info("Review with id {} for event id {} has been deleted.", reviewId, eventId);
	}

	public EventReviewResponseDTO updateReview(String eventId, String reviewId, ReviewRequestDTO reviewRequestDTO) {
		validateEventId(eventId);
		validateReviewId(reviewId);
		validateInputForUpdate(reviewRequestDTO);
		
		EventReview reviewFromDB = eventReviewRepository.findById(reviewId).orElse(null);
		if(reviewFromDB == null || reviewFromDB.getEventId().equals(eventId)) {
			throw constructReviewDoesNotExistException(eventId, reviewId);
		}
		
		boolean isAllowedToModifyOrDelete = isAllowedToModifyOrDelete(reviewFromDB.getUserId());
		
		if(isAllowedToModifyOrDelete) {
			EventReviewDTOTransformer.updateReviewFromDB(reviewFromDB, reviewRequestDTO);
			eventReviewRepository.save(reviewFromDB);
			return EventReviewDTOTransformer.transformToReviewResponseDTO(reviewFromDB);
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
		List<EventReview> eventReviews = eventReviewRepository.findByUserId(userId);
		if(!CollectionUtils.isEmpty(eventReviews)) {
			log.info("deleteReviewByUserId:: Number of reviews by userid {} are {}", userId, eventReviews.size());
			eventReviewRepository.deleteByUserId(userId);
		} else {
			log.info("deleteReviewByUserId:: No reviews by userid {}, no deletion required", userId);
		}
	}
}
