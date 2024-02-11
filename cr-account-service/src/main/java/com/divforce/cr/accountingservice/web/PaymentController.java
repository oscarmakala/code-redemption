package com.divforce.cr.accountingservice.web;

import com.divforce.cr.accountingservice.domain.Payment;
import com.divforce.cr.accountingservice.domain.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Oscar Makala
 */
@Log4j2
@RestController
@RequestMapping(path = "/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final Assembler paymentAssembler = new Assembler();


    @GetMapping
    public PagedModel<PaymentDto> getPayments(Pageable pageable,
                                              @RequestParam(required = false) String mobile,
                                              @RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              PagedResourcesAssembler<Payment> assembler) {

        final Instant fromDateAsInstant = paresStringToInstant(fromDate);
        final Instant toDateAsInstant = paresStringToInstant(toDate);

        if (fromDate != null && toDate != null && fromDate.compareTo(toDate) > 0) {
            throw new InvalidDateRangeException("The fromDate cannot be after the toDate.");
        }

        Page<Payment> page = this.paymentService.findByMobileAndDate(pageable, mobile, fromDateAsInstant, toDateAsInstant);
        return assembler.toModel(page, this.paymentAssembler);
    }

    private Instant paresStringToInstant(String textDate) {
        if (textDate == null) {
            return null;
        }

        LocalDateTime localDateTime = LocalDateTime.parse(textDate, DateTimeFormatter.ISO_DATE_TIME);
        return localDateTime.toInstant(ZoneOffset.UTC);
    }


    static class Assembler extends RepresentationModelAssemblerSupport<Payment, PaymentDto> {
        public Assembler() {
            super(PaymentController.class, PaymentDto.class);

        }

        @Override
        public PaymentDto toModel(Payment entity) {
            try {
                return createModelWithId(entity.getTransactionId(), entity);
            } catch (IllegalStateException e) {
                log.warn("Failed to create PaymentDto. " + e.getMessage());
            }
            return null;
        }

        @Override
        protected PaymentDto instantiateModel(Payment entity) {
            return new PaymentDto(entity);
        }
    }

}
