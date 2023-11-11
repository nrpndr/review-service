package com.cineevent.reviewservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cineevent.reviewservice.dto.request.ReviewRequestDTO;
import com.cineevent.reviewservice.dto.response.MovieReviewResponseDTO;
import com.cineevent.reviewservice.service.MovieReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/movies")
public class MoviesReviewsController {

	@Autowired
	private MovieReviewService reviewService;
	
	@GetMapping("/{movieId}/reviews")
	public List<MovieReviewResponseDTO> getAllReviews(@PathVariable("movieId") String movieId) {
		return reviewService.getAllReviews(movieId);
	}

	@GetMapping("/{movieId}/reviews/{reviewId}")
	public MovieReviewResponseDTO getReview(@PathVariable("movieId") String movieId,
			@PathVariable("reviewId") String reviewId) {
		return reviewService.getReview(movieId, reviewId);
	}

	@Operation(security = { @SecurityRequirement (name = "bearer-key") })
	@PostMapping("/{movieId}/reviews")
	@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
	public ResponseEntity<MovieReviewResponseDTO> createReview(@PathVariable("movieId") String movieId,
			@RequestBody ReviewRequestDTO reviewRequestDTO) {
		return new ResponseEntity<>(reviewService.createReview(reviewRequestDTO, movieId), null, HttpStatus.CREATED);
	}

	@Operation(security = { @SecurityRequirement (name = "bearer-key") })
	@PatchMapping("/{movieId}/reviews/{reviewId}")
	@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
	public MovieReviewResponseDTO updateReview(@PathVariable("movieId") String movieId,
			@PathVariable("reviewId") String reviewId, @RequestBody ReviewRequestDTO reviewRequestDTO) {
		return reviewService.updateReview(movieId, reviewId, reviewRequestDTO);
	}

	@Operation(security = { @SecurityRequirement (name = "bearer-key") })
	@DeleteMapping("/{movieId}/reviews/{reviewId}")
	@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
	public ResponseEntity<?> deleteReview(@PathVariable("movieId") String movieId,
			@PathVariable("reviewId") String reviewId) {
		reviewService.deleteReview(movieId, reviewId);
		return ResponseEntity.noContent().build();
	}

}
