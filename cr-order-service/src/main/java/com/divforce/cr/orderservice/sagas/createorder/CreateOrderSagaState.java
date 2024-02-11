package com.divforce.cr.orderservice.sagas.createorder;

import com.divforce.cr.accountingservice.api.CreatePaymentCommand;
import com.divforce.cr.accountingservice.api.PaymentCommand;
import com.divforce.cr.accountingservice.api.RejectPaymentCommand;
import com.divforce.cr.accountingservice.api.events.PaymentFailure;
import com.divforce.cr.common.RejectionReason;
import com.divforce.cr.orderservice.events.OrderDetails;
import com.divforce.cr.orderservice.sagaparticipants.RejectOrderCommand;
import com.divforce.cr.orderservice.sagaparticipants.SubmitOrderCommand;
import com.divforce.cr.voucherservice.api.command.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @author Oscar Makala
 */
@Log4j2
@Data
@NoArgsConstructor
public class CreateOrderSagaState {
    private String orderId;
    private OrderDetails orderDetails;
    private String thirdPartyTransactionId;
    private String externalDataOne;
    private String thirdPartyStatus;
    private String errorMessage;
    private String errorCode;
    private RejectionReason rejectionReason;


    public CreateOrderSagaState(String orderId, OrderDetails orderDetails) {
        this.orderId = orderId;
        this.orderDetails = orderDetails;
    }

    public RejectOrderCommand makeRejectOrderCommand() {
        return new RejectOrderCommand(getOrderId(), getRejectionReason());
    }

    ValidateVoucherCommand makeValidateVoucherCommand() {
        var x = new ValidateVoucherCommand();
        OrderDetails order = getOrderDetails();
        x.setSerialNumber(order.getSerialNumber());
        x.setOrderId(getOrderId());
        x.setRedeemedVoucher(order.getRedeemedVouchers());
        return x;
    }

    public ReserveVoucherCommand makeReserveVoucherCommand() {
        return new ReserveVoucherCommand(getOrderDetails().getSerialNumber(), getOrderId());
    }

    public SubmitOrderCommand makeSubmitOrderCommand() {
        return new SubmitOrderCommand(getOrderId());
    }


    public PaymentCommand makePaymentCommand() {
        OrderDetails od = getOrderDetails();
        return new PaymentCommand(od.getOrderId());
    }


    public RejectReservationCommand makeRejectReservationCommand() {
        return new RejectReservationCommand(getOrderDetails().getSerialNumber());
    }

    public FulFillReservationCommand makeUsedVoucherCommand() {
        return new FulFillReservationCommand(getOrderDetails().getSerialNumber());
    }


    public CreatePaymentCommand makeCreatePaymentCommand() {
        OrderDetails od = getOrderDetails();
        return new CreatePaymentCommand(
                od.getSerialNumber(),
                od.getOrderId(),
                od.getMobileNumber(),
                od.getAmount(),
                od.getCode(),
                od.getKeyPhrase()
        );
    }

    public RejectPaymentCommand makeRejectPaymentCommand() {
        return new RejectPaymentCommand(
                getOrderDetails().getOrderId(),
                getThirdPartyTransactionId(),
                getExternalDataOne(),
                getThirdPartyStatus(),
                getErrorCode(),
                getErrorMessage()
        );
    }

    public void handleDisbursementReply(PaymentFailure reply) {
        log.info("handleDisbursementReply {}", reply);
        setThirdPartyTransactionId(reply.getThirdPartyTransactionId());
        setExternalDataOne(reply.getExternalDataOne());
        setThirdPartyStatus(reply.getThirdPartyStatus());
        setErrorCode(reply.getErrorCode());
        setErrorMessage(reply.getErrorMessage());
        setRejectionReason(RejectionReason.SYSTEM_FAILURE);
    }

    public void handleValidateVoucherFailureReply(ValidateVoucherFailureReply reply) {
        log.info("handleValidateVoucherReply {}", reply);
        setRejectionReason(reply.getRejectionReason());
    }


}
