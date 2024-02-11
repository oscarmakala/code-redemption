package com.divforce.cr.common;

/**
 * @author Oscar Makala
 */
public class UnsupportedStateTransitionException extends RuntimeException {
    public UnsupportedStateTransitionException(Enum state) {
        super("current state: " + state);
    }
}
