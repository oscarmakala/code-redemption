package com.divforce.cr.voucherservice.web;

import com.divforce.cr.voucherservice.api.CreateCampaignRequest;
import com.divforce.cr.voucherservice.api.UploadVoucherResponse;
import com.divforce.cr.voucherservice.domain.Campaign;
import com.divforce.cr.voucherservice.domain.CampaignService;
import com.divforce.cr.voucherservice.domain.CampaignStatus;
import com.divforce.cr.voucherservice.exception.NoSuchCampaignException;
import jakarta.validation.Valid;
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
import org.springframework.web.multipart.MultipartFile;


import java.security.Principal;

/**
 * @author Oscar Makala
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/campaigns")
@ExposesResourceFor(CampaignResource.class)
public class CampaignController {
    private final CampaignService campaignService;

    private final Assembler assembler = new Assembler();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CampaignResource create(@Valid @RequestBody CreateCampaignRequest request) {
        Campaign campaign = campaignService.create(
                request.getName(),
                request.getDescription(),
                request.getPassPhrase(),
                request.getMaxRedemptionsPerUser()
        );
        return this.assembler.toModel(campaign);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<CampaignResource> list(Pageable pageable,
                                             @RequestParam(required = false) CampaignStatus[] status,
                                             PagedResourcesAssembler<Campaign> assembler) {
        Page<Campaign> campaignRecords = this.campaignService.findBy(status, pageable);
        return assembler.toModel(campaignRecords, this.assembler);
    }


    @GetMapping(path = "/{campaignId}")
    @ResponseStatus(HttpStatus.OK)
    public CampaignResource get(@PathVariable Long campaignId) {
        return campaignService.findById(campaignId).map(assembler::toModel).orElseThrow(() -> new NoSuchCampaignException(campaignId));
    }

    @GetMapping(path = "/{campaignId}/activation")
    @ResponseStatus(HttpStatus.OK)
    public CampaignResource activations(@PathVariable Long campaignId, Principal principal) {
        return assembler.toModel(campaignService.activate(campaignId));
    }

    @GetMapping(path = "/{campaignId}/approve")
    @ResponseStatus(HttpStatus.OK)
    public CampaignResource approve(@PathVariable Long campaignId,
                                    @RequestParam("status") CampaignStatus status,
                                    Principal principal) {
        return assembler.toModel(campaignService.approve(campaignId, status, principal));
    }

    @GetMapping(path = "/{campaignId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public CampaignResource reject(@PathVariable Long campaignId, Principal principal) {
        return assembler.toModel(campaignService.reject(campaignId, principal));
    }


    @GetMapping(path = "/{campaignId}/deactivation")
    @ResponseStatus(HttpStatus.OK)
    public CampaignResource deActivations(@PathVariable Long campaignId) {
        return assembler.toModel(campaignService.deActivate(campaignId));
    }

//    @GetMapping(path = "/{campaignId}/pause")
//    @ResponseStatus(HttpStatus.OK)
//    public CampaignResource pause(@PathVariable Long campaignId) {
//        return assembler.toModel(campaignService.pause(campaignId));
//    }


    @PutMapping(path = "/{campaignId}/vouchers")
    @ResponseStatus(HttpStatus.OK)
    public UploadVoucherResponse importVouchers(@PathVariable("campaignId") Long campaignId, MultipartFile file) {
        return this.campaignService.importVouchers(campaignId, file);
    }


    @DeleteMapping(path = "/{campaignId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("campaignId") Long id) {
        this.campaignService.delete(id);
    }

    static class Assembler extends RepresentationModelAssemblerSupport<Campaign, CampaignResource> {

        public Assembler() {
            super(CampaignController.class, CampaignResource.class);
        }

        @Override
        public CampaignResource toModel(Campaign campaign) {
            try {
                return createModelWithId(campaign.getId(), campaign);
            } catch (IllegalStateException e) {
                log.warn("Failed to create CampaignResource. " + e.getMessage());
            }
            return null;
        }

        @Override
        protected CampaignResource instantiateModel(Campaign entity) {
            return new CampaignResource(entity);
        }
    }

}
