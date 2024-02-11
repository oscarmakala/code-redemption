package com.divforce.cr.voucherservice.web;

import com.divforce.cr.voucherservice.api.VoucherExpiryAndDate;
import com.divforce.cr.voucherservice.exception.NoSuchVoucherException;
import com.divforce.cr.voucherservice.domain.Voucher;
import com.divforce.cr.voucherservice.domain.VoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Oscar Makala
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/vouchers")
@ExposesResourceFor(VoucherResource.class)
public class VoucherController {
    private final VoucherService voucherService;
    private final Assembler voucherAssembler = new Assembler();


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<VoucherResource> list(Pageable pageable,
                                            @RequestParam(required = false) Long campaignId,
                                            @RequestParam(required = false) String code,
                                            PagedResourcesAssembler<Voucher> assembler) {
        Page<Voucher> voucherRecords = this.voucherService.findAllByCampaignIdAndCode(campaignId, code, pageable);
        return assembler.toModel(voucherRecords, this.voucherAssembler);
    }


    @GetMapping(path = "/decrypt")
    @ResponseStatus(HttpStatus.OK)
    public VoucherExpiryAndDate decrypt(
            @RequestParam("code") String code,
            @RequestParam("campaignId") String campaignId,
            @RequestParam("password") String password
    ) {
        return this.voucherService.decryptVoucher(Long.valueOf(campaignId), code, password);
    }

    @GetMapping(path = "/{code}")
    @ResponseStatus(HttpStatus.OK)
    public VoucherResource get(@PathVariable("code") String code) {
        return voucherService.findById(code)
                .map(voucherAssembler::toModel)
                .orElseThrow(() -> new NoSuchVoucherException(code));
    }

    static class Assembler extends RepresentationModelAssemblerSupport<Voucher, VoucherResource> {
        public Assembler() {
            super(VoucherController.class, VoucherResource.class);

        }

        @Override
        public VoucherResource toModel(Voucher entity) {
            try {
                return createModelWithId(entity.getCode(), entity);
            } catch (IllegalStateException e) {
                log.warn("Failed to create CampaignResource. " + e.getMessage());
            }
            return null;
        }

        @Override
        protected VoucherResource instantiateModel(Voucher entity) {
            return new VoucherResource(entity);
        }
    }
}
