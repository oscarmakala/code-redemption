package com.divforce.cr.orderservice.domain;

import com.divforce.cr.common.RejectionReason;
import com.divforce.cr.common.SerialNumberGenerator;
import com.divforce.cr.orderservice.events.OrderDetails;
import com.divforce.cr.orderservice.events.OrderDomainEvent;
import com.divforce.cr.orderservice.events.OrderState;
import com.divforce.cr.orderservice.sagas.createorder.CreateOrderSaga;
import com.divforce.cr.orderservice.sagas.createorder.CreateOrderSagaState;
import com.divforce.cr.voucherservice.api.events.VoucherCreatedEvent;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;

/**
 * @author Oscar Makala
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final VoucherRepository voucherRepository;
    private final OrderDomainEventPublisher orderAggregateEventPublisher;
    private final SagaInstanceFactory sagaInstanceFactory;
    private final CreateOrderSaga createOrderSaga;


    @Transactional
    public Order createOrder(String transactionId,
                             String requestType,
                             String voucherCode,
                             String mobileNumber) {
        String serialNumber = SerialNumberGenerator.generate(voucherCode);
        log.debug("generated serial number {}", serialNumber);
        var voucher = voucherRepository.findById(serialNumber)
                .orElseThrow(() -> new NoSuchVoucherException(voucherCode));

        if (voucher.getState().equals(VoucherState.USED) || voucher.getState().equals(VoucherState.PENDING)) {
            throw new VoucherAlreadyUseException(voucherCode);
        }
        ResultWithDomainEvents<Order, OrderDomainEvent> orderAndEvents = Order.createOrder(
                transactionId,
                requestType,
                serialNumber,
                mobileNumber,
                voucher.getAmount()
        );
        var order = orderAndEvents.result;
        this.orderRepository.save(order);
        this.orderAggregateEventPublisher.publish(order, orderAndEvents.events);

        Instant start = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant end = start.plus(24, ChronoUnit.HOURS);


        Integer count = this.orderRepository.countByMobileNumberAndStateAndCreatedDateBetween(
                mobileNumber,
                OrderState.PROCESSED,
                start,
                end
        );
        var orderDetails = new OrderDetails(
                transactionId,
                serialNumber,
                mobileNumber,
                voucher.getAmount(),
                voucher.getCode(),
                voucher.getKeyPhrase(),
                count
        );

        var data = new CreateOrderSagaState(order.getOrderId(), orderDetails);
        sagaInstanceFactory.create(createOrderSaga, data);
        return order;
    }


    public void submitOrder(String orderId) {
        updateOrder(orderId, Order::orderSubmitted);
    }

    public void rejectOrder(String orderId, RejectionReason rejectionReason) {
        updateOrder(orderId, order -> order.orderRejected(rejectionReason));
    }

    private Order updateOrder(String orderId, Function<Order, List<OrderDomainEvent>> updater) {
        return orderRepository.findById(orderId).map(order -> {
            orderAggregateEventPublisher.publish(order, updater.apply(order));
            return order;
        }).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public void updateVoucher(String serialNumber, VoucherState state) {
        this.voucherRepository
                .findById(serialNumber)
                .map(voucher -> voucher.updateState(state))
                .orElseThrow(RuntimeException::new);
    }


    public void createVoucher(VoucherCreatedEvent voucherDetails) {
        log.debug("createVoucher({})", voucherDetails);
        Voucher voucher = new Voucher();
        voucher.setAmount(voucherDetails.getAmount());
        voucher.setSerialNumber(voucherDetails.getSerialNumber());
        voucher.setState(VoucherState.AVAILABLE);
        voucher.setExpireAt(voucherDetails.getExpireAt());
        voucher.setKeyPhrase(voucherDetails.getKeyPhrase());
        voucher.setCode(voucherDetails.getCode());
        this.voucherRepository.save(voucher);
    }
}
