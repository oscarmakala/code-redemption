package com.divforce.cr.notificationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * @author Oscar Makala
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
@Access(AccessType.FIELD)
public class Notification {
    @Id
    private String id;
    private String mobile;
    private String message;
    private Instant createdAt;


    public static Notification create(String orderId, String mobile, String text) {
        return new Notification(orderId, mobile, text, Instant.now());
    }
}
