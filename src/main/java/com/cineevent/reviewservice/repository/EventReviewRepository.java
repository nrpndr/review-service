package com.cineevent.reviewservice.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cineevent.reviewservice.model.EventReview;

@Repository
public interface EventReviewRepository extends MongoRepository<EventReview, String>{

	List<EventReview> findByEventId(String eventId, Sort sort);

	List<EventReview> findByIdAndEventId(String reviewId, String eventId);
	
	List<EventReview> findByUserId(int userId);
	
	void deleteByUserId(int userId);
}
