package com.cineevent.reviewservice.exceptions;

public class UserDoesNotHavePermissionException extends RuntimeException {

	private static final long serialVersionUID = 174708790454537194L;

	public UserDoesNotHavePermissionException(String message) {
        super(message);
    }
	
}
