package br.com.gs3tecnologia.usermanager.exception;

import java.io.Serial;

public class BusinessException extends Exception {

	@Serial
    private static final long serialVersionUID = 8399414906322030923L;

	public BusinessException(String message) {
        super(message);
    }
}
