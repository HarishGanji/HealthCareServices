package com.healthcare.system.exception;

/**
 * Thrown when a requested entity cannot be found.
 */
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
