package com.divforce.cr.voucherservice.web;

import com.divforce.cr.voucherservice.domain.Campaign;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

/**
 * @author Oscar Makala
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class CampaignResource extends RepresentationModel<CampaignResource> {
    private String createdBy;
    private Instant createdOn;
    private Long id;
    private String status;
    private String name;
    private String description;

    public CampaignResource(Campaign entity) {
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.status = entity.getStatus().toString();
        this.id = entity.getId();
        this.createdBy = entity.getCreatedBy();
        this.createdOn = entity.getCreatedOn();
    }
}
