package com.divforce.cr.orderservice.web;

import com.divforce.cr.orderservice.domain.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;




/**
 * @author Oscar Makala
 */
@Log4j2
@RestController
@RequestMapping(path = "/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @Value("${notification.voucher_request_submitted}")
    private String successMessage;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        log.info("createRequest({})", request);
        var order = orderService.createOrder(
                request.getTransactionId(),
                request.getRequestType(),
                request.getCode(),
                request.getMsisdn()
        );
        CreateOrderResponse response = new CreateOrderResponse(
                "200",
                successMessage,
                order.getAmount().toString()
        );
        log.info("createResponse({})", response);
        return response;
    }
}
