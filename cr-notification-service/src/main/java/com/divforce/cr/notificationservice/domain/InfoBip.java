package com.divforce.cr.notificationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoBip {
    private String msisdn;
    private String text;
}
