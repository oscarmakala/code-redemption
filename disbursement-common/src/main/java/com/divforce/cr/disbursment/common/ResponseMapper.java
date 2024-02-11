package com.divforce.cr.disbursment.common;

import org.springframework.lang.Nullable;

/**
 * @author Oscar Makala
 */
@FunctionalInterface
public interface ResponseMapper<T> {
    @Nullable
    T mapResponse(String response) throws Exception;
}
