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
import com.cineevent.reviewservice.dto.response.EventReviewResponseDTO;
import com.cineevent.reviewservice.service.EventReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/events")
public class EventsReviewsController {

	@Autowired
	private EventReviewService reviewService;
	
	@GetMapping("/{eventId}/reviews")
	public List<EventReviewResponseDTO> getAllReviews(@PathVariable("eventId") String eventId) {
		return reviewService.getAllReviews(eventId);
	}

	@GetMapping("/{eventId}/reviews/{reviewId}")
	public EventReviewResponseDTO getReview(@PathVariable("eventId") String eventId,
			@PathVariable("reviewId") String reviewId) {
		return reviewService.getReview(eventId, reviewId);
	}

	@Operation(security = { @SecurityRequirement (name = "bearer-key") })
	@PostMapping("/{eventId}/reviews")
	@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ADMIN')")
	public ResponseEntity<EventReviewResponseDTO> createReview(@PathVariable("eventId") String eventId,
			@RequestBody ReviewRequestDTO reviewRequestDTO) {
		return new ResponseEntity<>(reviewService.createReview(reviewRequestDTO, eventId), null, HttpStatus.CREATED);
	}

	@Operation(security = { @SecurityRequirement (name = "bearer-key") })
	@PatchMapping("/{eventId}/reviews/{reviewId}")
	@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ADMIN')")
	public EventReviewResponseDTO updateReview(@PathVariable("eventId") String eventId,
			@PathVariable("reviewId") String reviewId, @RequestBody ReviewRequestDTO reviewRequestDTO) {
		return reviewService.updateReview(eventId, reviewId, reviewRequestDTO);
	}

	@Operation(security = { @SecurityRequirement (name = "bearer-key") })
	@DeleteMapping("/{eventId}/reviews/{reviewId}")
	@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ADMIN')")
	public ResponseEntity<?> deleteReview(@PathVariable("eventId") String eventId,
			@PathVariable("reviewId") String reviewId) {
		reviewService.deleteReview(eventId, reviewId);
		return ResponseEntity.noContent().build();
	}

}
