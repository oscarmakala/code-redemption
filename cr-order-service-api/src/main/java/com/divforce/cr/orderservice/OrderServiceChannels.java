package com.divforce.cr.orderservice;

/**
 * @author Oscar Makala
 */
public final class OrderServiceChannels {
    private OrderServiceChannels() {
        super();
    }

    public static final String COMMAND_CHANNEL = "orderService";
    public static final String ORDER_EVENT_CHANNEL = "com.divforce.cr.orderservice.domain.Order";
}
