package com.divforce.cr.accountingservice.web;


/**
 * Thrown by controller classes to indicate invalid date ranges.
 *
 * @author Oscar Makala
 */
public class InvalidDateRangeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidDateRangeException(String message) {
        super(message);
    }
}
