package com.cineevent.reviewservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cineevent.reviewservice.dto.response.ErrorResponseDTO;
import com.cineevent.reviewservice.exceptions.InValidUserInputException;
import com.cineevent.reviewservice.exceptions.ReviewDoesNotExistException;
import com.cineevent.reviewservice.exceptions.UserDoesNotHavePermissionException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({InValidUserInputException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponseDTO> handleAsBadRequest(RuntimeException ex) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ReviewDoesNotExistException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponseDTO> handleAsNotFound(RuntimeException ex) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({UserDoesNotHavePermissionException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ErrorResponseDTO> handleAsMissingPermssion(RuntimeException ex) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex.getMessage());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.FORBIDDEN);
	}
	
}
