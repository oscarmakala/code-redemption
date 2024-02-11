package com.divforce.cr.cqrs.voucherhistory.web;

import com.divforce.cr.cqrs.voucherhistory.api.VoucherHistory;
import com.divforce.cr.cqrs.voucherhistory.domain.VoucherHistoryAggregate;
import com.divforce.cr.cqrs.voucherhistory.domain.VoucherHistoryService;
import com.divforce.cr.encrypt.util.AttributeEncryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Oscar Makala
 */
@Log4j2
@RestController
@RequestMapping(path = "/voucher-history")
@RequiredArgsConstructor
public class VoucherHistoryController {
    private final VoucherHistoryService voucherHistoryService;
    private final Assembler voucherHistoryAssembler = new Assembler();

    @GetMapping(path = "{campaignId}")
    public PagedModel<VoucherHistoryDto> getOrders(Pageable pageable,
                                                   @PathVariable("campaignId") Long campaignId,
                                                   @RequestParam(required = false) String code,
                                                   PagedResourcesAssembler<VoucherHistory> assembler) {
        Page<VoucherHistory> page = this.voucherHistoryService.findByCampaignIdAndCode(campaignId, code, pageable);
        return assembler.toModel(page, this.voucherHistoryAssembler);
    }

    @GetMapping(path = "/aggregate-by-state")
    public List<VoucherHistoryAggregate> aggregateVouchers() {
        return this.voucherHistoryService.aggregateVouchersByState();
    }

    static class Assembler extends RepresentationModelAssemblerSupport<VoucherHistory, VoucherHistoryDto> {


        public Assembler() {
            super(VoucherHistoryController.class, VoucherHistoryDto.class);

        }

        @Override
        public VoucherHistoryDto toModel(VoucherHistory entity) {
            try {
                return createModelWithId(entity.getId(), entity);
            } catch (IllegalStateException e) {
                log.warn("Failed to create CampaignResource. " + e.getMessage());
            }
            return null;
        }

        @Override
        protected VoucherHistoryDto instantiateModel(VoucherHistory entity) {
            return new VoucherHistoryDto(entity);
        }
    }
}
