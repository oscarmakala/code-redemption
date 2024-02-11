package com.divforce.cr.orderservice.domain;

import com.divforce.cr.orderservice.events.OrderState;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;

/**
 * @author Oscar Makala
 */

public interface OrderRepository extends CrudRepository<Order, String> {

    Integer countByMobileNumberAndStateAndCreatedDateBetween(
            String mobile,
            OrderState state,
            Instant start,
            Instant end);

}
