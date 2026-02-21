package com.healthcare.system.exception;

/**
 * Thrown when a client submits an invalid request or parameters.
 */
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String message) {
		super(message);
	}
}
