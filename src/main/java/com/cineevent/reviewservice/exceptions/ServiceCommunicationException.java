package com.cineevent.reviewservice.exceptions;

public class ServiceCommunicationException extends RuntimeException{

	private static final long serialVersionUID = -5168455483047261172L;

	public ServiceCommunicationException(String message) {
        super(message);
    }
}
