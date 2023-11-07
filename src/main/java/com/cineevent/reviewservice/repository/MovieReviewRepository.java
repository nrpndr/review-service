package com.cineevent.reviewservice.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cineevent.reviewservice.model.MovieReview;

@Repository
public interface MovieReviewRepository extends MongoRepository<MovieReview, String>{
	
	List<MovieReview> findByMovieId(String movieId, Sort sort);
	
	List<MovieReview> findByIdAndMovieId(String reviewId, String movieId);
	
	List<MovieReview> findByUserId(int userId);
	
	void deleteByUserId(int userId);

}
