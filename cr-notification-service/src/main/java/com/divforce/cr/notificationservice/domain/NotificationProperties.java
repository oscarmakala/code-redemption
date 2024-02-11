package com.divforce.cr.notificationservice.domain;

import lombok.Data;

import java.util.Map;

/**
 * @author Oscar Makala
 */
@Data
public class NotificationProperties {
    private String endpoint;
    private Map<String, String> headers;
    private Map<String, LocaleMessage> messages;
    private String template;

    public String messageFrom(String key, String lang) {
        if (lang.equalsIgnoreCase("en")) {
            return messages.get(key).getEn();
        }
        return messages.get(key).getSw();

    }
}
