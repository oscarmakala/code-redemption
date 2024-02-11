package com.divforce.cr.orderservice.sagas.createorder;

import com.divforce.cr.accountingservice.api.events.PaymentFailure;
import com.divforce.cr.orderservice.sagaparticipants.AccountServiceProxy;
import com.divforce.cr.orderservice.sagaparticipants.OrderServiceProxy;
import com.divforce.cr.orderservice.sagaparticipants.VoucherServiceProxy;
import com.divforce.cr.voucherservice.api.command.ValidateVoucherFailureReply;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;

/**
 * @author Oscar Makala
 */
public class CreateOrderSaga implements SimpleSaga<CreateOrderSagaState> {
    private final SagaDefinition<CreateOrderSagaState> sagaDefinition;

    public CreateOrderSaga(OrderServiceProxy orderService, VoucherServiceProxy voucherService, AccountServiceProxy accountService) {

        // @formatter:off
        this.sagaDefinition = step()
                    .withCompensation(orderService.reject, CreateOrderSagaState::makeRejectOrderCommand)
                .step()
                    .invokeParticipant(voucherService.validate, CreateOrderSagaState::makeValidateVoucherCommand)
                    .onReply(ValidateVoucherFailureReply.class,CreateOrderSagaState::handleValidateVoucherFailureReply)
                .step()
                    .invokeParticipant(voucherService.reserve, CreateOrderSagaState::makeReserveVoucherCommand)
                    .withCompensation(voucherService.reject, CreateOrderSagaState::makeRejectReservationCommand)
                .step()
                    .invokeParticipant(accountService.create, CreateOrderSagaState::makeCreatePaymentCommand)
                    .withCompensation(accountService.reject, CreateOrderSagaState::makeRejectPaymentCommand)
                .step()
                    .invokeParticipant(accountService.disburse, CreateOrderSagaState::makePaymentCommand)
                    .onReply(PaymentFailure.class,CreateOrderSagaState::handleDisbursementReply)
                .step()
                    .invokeParticipant(voucherService.fulfill, CreateOrderSagaState::makeUsedVoucherCommand)
                .step()
                    .invokeParticipant(orderService.submit, CreateOrderSagaState::makeSubmitOrderCommand)
                .build();
        // @formatter:on
    }


    @Override
    public SagaDefinition<CreateOrderSagaState> getSagaDefinition() {
        return sagaDefinition;
    }
}
