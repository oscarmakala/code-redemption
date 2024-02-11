package com.divforce.cr.voucherservice.api.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignConfiguration {
    private Integer maxRedemptionsPerUser = 0;
}
