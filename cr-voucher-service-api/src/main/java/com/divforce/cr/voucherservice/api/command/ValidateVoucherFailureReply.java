package com.divforce.cr.voucherservice.api.command;

import com.divforce.cr.common.RejectionReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateVoucherFailureReply {
    private RejectionReason rejectionReason;
}
