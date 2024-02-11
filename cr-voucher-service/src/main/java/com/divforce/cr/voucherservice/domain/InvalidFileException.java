package com.divforce.cr.voucherservice.domain;

/**
 * @author Oscar Makala
 */
public class InvalidFileException extends RuntimeException {
    public InvalidFileException(String message) {
        super(message);
    }
}
