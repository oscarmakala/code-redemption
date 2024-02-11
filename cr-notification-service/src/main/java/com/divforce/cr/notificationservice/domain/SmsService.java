package com.divforce.cr.notificationservice.domain;

import java.util.Map;

/**
 * @author Oscar Makala
 */
public interface SmsService {
    void send(String mobile, String message, String endpoint, Map<String, String> headers, String template);
}
