package com.divforce.cr.voucherservice.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;



/**
 * @author Oscar Makala
 */
@Data
public class CreateCampaignRequest {
    @NotBlank(message = "Campaign name is mandatory")
    private String name;
    private String description;
    @NotBlank(message = "passPhrase is mandatory")
    private String passPhrase;
    private Integer maxRedemptionsPerUser = 0;
}
