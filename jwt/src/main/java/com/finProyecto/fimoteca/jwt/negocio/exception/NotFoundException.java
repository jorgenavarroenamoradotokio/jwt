package com.finProyecto.fimoteca.jwt.negocio.exception;

public class NotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(String msg) {
		super(msg);
	}

	public NotFoundException(Throwable t) {
		super(t);
	}

	public NotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
}
