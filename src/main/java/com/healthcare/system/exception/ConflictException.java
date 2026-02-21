package com.healthcare.system.exception;

/**
 * Thrown when a request conflicts with current state (e.g., overlapping schedules).
 */
public class ConflictException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConflictException(String message) {
		super(message);
	}
}
