package com.divforce.cr.orderservice.domain;

import com.divforce.cr.orderservice.events.OrderDomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Oscar Makala
 */

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "order_service_vouchers")
@Access(AccessType.FIELD)
public class Voucher {
    @Id
    private String serialNumber;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private VoucherState state;
    private Date expireAt;
    private String keyPhrase;
    @Column(length = 100)
    private String code;
    private LocalDate updatedAt;

    public List<OrderDomainEvent> updateState(VoucherState state) {
        this.state = state;
        this.updatedAt = LocalDate.now();
        return Collections.emptyList();
    }

}
