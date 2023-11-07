package com.cineevent.reviewservice.exceptions;

public class ReviewDoesNotExistException extends RuntimeException{

	private static final long serialVersionUID = -255986935176609859L;

	public ReviewDoesNotExistException(String message) {
        super(message);
    }
}
