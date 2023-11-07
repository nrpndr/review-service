package com.cineevent.reviewservice.dto.request;

public class InValidAccessTokenException extends RuntimeException {

	private static final long serialVersionUID = 1636844910414930397L;

	public InValidAccessTokenException(String message) {
        super(message);
    }
}
