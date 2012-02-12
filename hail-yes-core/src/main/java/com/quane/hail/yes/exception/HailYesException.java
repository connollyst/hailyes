package com.quane.hail.yes.exception;

public class HailYesException extends Exception {

	private static final long serialVersionUID = 42L;

	public HailYesException() {
		super();
	}

	public HailYesException(String message) {
		super(message);
	}

	public HailYesException(Throwable error) {
		super(error);
	}

	public HailYesException(String message, Throwable error) {
		super(message, error);
	}
}
