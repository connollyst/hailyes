package com.quane.hail.yes.exception;

public class MissingUserException extends HailYesException {

	private static final long serialVersionUID = 42L;

	public MissingUserException() {
		super();
	}

	public MissingUserException(String message) {
		super(message);
	}

	public MissingUserException(Throwable error) {
		super(error);
	}

	public MissingUserException(String message, Throwable error) {
		super(message, error);
	}

}
