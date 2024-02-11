package com.divforce.cr.orderservice.domain;

import com.divforce.cr.common.RejectionReason;
import com.divforce.cr.common.UnsupportedStateTransitionException;
import com.divforce.cr.orderservice.events.*;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.divforce.cr.orderservice.events.OrderState.PROCESSED;
import static com.divforce.cr.orderservice.events.OrderState.REJECTED;
import static java.util.Collections.singletonList;

/**
 * @author Oscar Makala
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "orders", indexes = @Index(name = "user_metric_idx", columnList = "mobile_number,state,created_date"))
@Access(AccessType.FIELD)
public class Order {

    @Id
    private String orderId;
    private String requestType;

    private String serialNumber;
    @Column(name = "mobile_number")
    private String mobileNumber;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private OrderState state;
    @Enumerated(EnumType.STRING)
    private RejectionReason rejectionReason;
    private BigDecimal amount;
    @Version
    private Long version;
    @Column(name = "created_date")
    private Instant createdDate;

    public static ResultWithDomainEvents<Order, OrderDomainEvent> createOrder(String orderId, String requestType, String serialNumber, String mobileNumber, BigDecimal amount) {
        var order = new Order(orderId, requestType, serialNumber, mobileNumber, amount);
        List<OrderDomainEvent> events = Collections.singletonList(
                new OrderCreatedEvent(
                        orderId,
                        serialNumber,
                        mobileNumber,
                        amount
                )
        );
        return new ResultWithDomainEvents<>(order, events);
    }

    public Order(String orderId, String requestType, String serialNumber, String mobileNumber, BigDecimal amount) {
        this.orderId = orderId;
        this.requestType = requestType;
        this.serialNumber = serialNumber;
        this.mobileNumber = mobileNumber;
        this.amount = amount;
        this.state = OrderState.ACCEPTED;
        this.createdDate = Instant.now();
    }

    public List<OrderDomainEvent> orderSubmitted() {
        if (state == OrderState.ACCEPTED) {
            this.state = PROCESSED;
            return singletonList(new OrderProcessedEvent(getOrderId(), getMobileNumber(), getAmount()));
        }
        throw new UnsupportedStateTransitionException(state);
    }

    public List<OrderDomainEvent> orderRejected(RejectionReason rejectionReason) {
        if (state == OrderState.ACCEPTED) {
            this.state = REJECTED;
            this.rejectionReason = rejectionReason;
            return singletonList(new OrderRejectedEvent(getOrderId(), getMobileNumber(), getRejectionReason()));
        }
        throw new UnsupportedStateTransitionException(state);
    }
}
