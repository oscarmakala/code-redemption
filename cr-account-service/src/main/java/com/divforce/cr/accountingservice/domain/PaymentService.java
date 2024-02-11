package com.divforce.cr.accountingservice.domain;

import com.divforce.cr.accountingservice.api.events.PaymentDomainEvent;
import com.divforce.cr.disbursment.common.DisburseDto;
import com.divforce.cr.disbursment.common.DisbursementTemplate;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * @author Oscar Makala
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final AccountDomainEventPublisher domainEventPublisher;
    private final DisbursementTemplate disbursementTemplate;


    public void createPayment(String serialNumber,
                              String transactionId,
                              String mobile,
                              BigDecimal amount,
                              String code,
                              String passPhrase) {

        //check if payment entry exits
        ResultWithDomainEvents<Payment, PaymentDomainEvent> rwe = Payment.create(
                transactionId,
                serialNumber,
                mobile,
                amount,
                code,
                passPhrase
        );
        paymentRepository.save(rwe.result);
        domainEventPublisher.publish(rwe.result, rwe.events);
    }


    public void makePayment(String transactionId) {
        DisburseDto disburseDto;
        var payment = this.paymentRepository
                .findById(transactionId)
                .orElseThrow(() -> new PaymentNotFoundException(transactionId));
        try {
            disburseDto = disbursementTemplate.execute(
                    payment.getMobile(),
                    payment.getTransactionId(),
                    payment.getAmount()
            );
        } catch (Exception e) {
            log.error("createPayment", e);
            throw new PaymentFailedException(e.getMessage());
        }

        log.info("makePayment({})", disburseDto);
        String status = disburseDto.getStatus();
        String externalTxnOne = disburseDto.getExternalTxnOne();
        String externalTxnTwo = disburseDto.getExternalTxnTwo();
        String errorCode = disburseDto.getErrorCode();
        String errorMessage = disburseDto.getErrorMessage();
        if (!"SUCCEEDED".equalsIgnoreCase(status)) {
            //pass the error code
            throw new PaymentFailedException(status, externalTxnOne, externalTxnTwo, errorCode, errorMessage);
        }
        List<PaymentDomainEvent> events = payment.disburse(
                externalTxnOne,
                externalTxnTwo,
                status,
                disburseDto.getMessage()
        );
        domainEventPublisher.publish(payment, events);

    }


    public void rejectPayment(String transactionId,
                              String thirdPartyTxnId,
                              String externalDataOne,
                              String thirdPartyStatus,
                              String errorCode,
                              String errorMessage) {
        var payment = this.paymentRepository
                .findById(transactionId)
                .orElseThrow(() -> new PaymentNotFoundException(transactionId));
        List<PaymentDomainEvent> events = payment.reject(
                thirdPartyTxnId,
                externalDataOne,
                thirdPartyStatus,
                errorCode,
                errorMessage);
        domainEventPublisher.publish(payment, events);
    }


    @Transactional(readOnly = true)
    public Page<Payment> findByMobileAndDate(Pageable pageable, String mobile, Instant fromDateAsInstant, Instant toDateAsInstant) {
        return this.paymentRepository.findByMobileAndDate(
                mobile,
                fromDateAsInstant,
                toDateAsInstant,
                pageable);
    }
}
