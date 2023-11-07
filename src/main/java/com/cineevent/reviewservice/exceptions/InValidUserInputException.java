package com.cineevent.reviewservice.exceptions;

public class InValidUserInputException extends RuntimeException{

	private static final long serialVersionUID = -6984757846339663263L;

	public InValidUserInputException(String message) {
        super(message);
    }
}