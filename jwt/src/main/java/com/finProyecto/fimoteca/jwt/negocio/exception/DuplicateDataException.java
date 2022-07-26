package com.finProyecto.fimoteca.jwt.negocio.exception;

public class DuplicateDataException extends Exception {

	private static final long serialVersionUID = 1L;

	public DuplicateDataException() {
		super();
	}

	public DuplicateDataException(String msg) {
		super(msg);
	}

	public DuplicateDataException(Throwable t) {
		super(t);
	}

	public DuplicateDataException(String msg, Throwable t) {
		super(msg, t);
	}

}
