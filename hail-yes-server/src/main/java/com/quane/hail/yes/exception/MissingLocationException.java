package com.quane.hail.yes.exception;

public class MissingLocationException extends HailYesException {

	private static final long serialVersionUID = 42L;

	public MissingLocationException() {
		super();
	}

	public MissingLocationException(String message) {
		super(message);
	}

	public MissingLocationException(Throwable error) {
		super(error);
	}

	public MissingLocationException(String message, Throwable error) {
		super(message, error);
	}
}
