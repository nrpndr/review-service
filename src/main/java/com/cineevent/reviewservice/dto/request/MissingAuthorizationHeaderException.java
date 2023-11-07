package com.cineevent.reviewservice.dto.request;

public class MissingAuthorizationHeaderException extends RuntimeException {

	private static final long serialVersionUID = 1636844910414930397L;

	public MissingAuthorizationHeaderException(String message) {
        super(message);
    }
}
