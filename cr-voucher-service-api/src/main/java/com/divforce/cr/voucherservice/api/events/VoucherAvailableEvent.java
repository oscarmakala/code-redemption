package com.divforce.cr.voucherservice.api.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherAvailableEvent implements VoucherDomainEvent {
    private String serialNumber;
}
