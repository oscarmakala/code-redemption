package com.divforce.cr.azampay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oscar Makala
 * <p>
 * <p>
 * {
 * "txnStatus": "TS",
 * "serviceRequestId": "0a3ed604-455f-4d26-a6da-44e7dec48b8d",
 * "mfsTenantId": "mfsPrimaryTenant",
 * "language": "sw",
 * "serviceFlow": "CASHIN",
 * "message": "Cash In transaction of TSH 500.00 has been successfully completed between the sender: 683000001 and receiver: 713986696. Txn ID: CI210531.1439.A00294",
 * "transactionId": "CI210531.1439.A00294",
 * "remarks": "Codedisburse",
 * "originalServiceRequestId": "0a3ed604-455f-4d26-a6da-44e7dec48b8d",
 * "status": "SUCCEEDED"
 * }
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AzamResponse {
    private String txnStatus;
    private String serviceRequestId;
    private String mfsTenantId;
    private String language;
    private String serviceFlow;
    private String transactionId;
    private String remarks;
    private String originalServiceRequestId;
    private String status;
    private String message;
    private List<AzamError> errors = new ArrayList<>();

    public AzamError error() {
        if (errors == null || errors.isEmpty()) {
            return new AzamError();
        }
        return errors.get(0);
    }


}
