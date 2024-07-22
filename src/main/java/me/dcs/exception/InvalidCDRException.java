package me.dcs.exception;

import java.io.Serial;

public class InvalidCDRException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidCDRException(String message) {
        super(message);
    }
}
