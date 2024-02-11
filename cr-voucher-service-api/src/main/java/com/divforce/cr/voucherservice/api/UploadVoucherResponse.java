package com.divforce.cr.voucherservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Oscar Makala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadVoucherResponse {
    private Integer invalidRowCount;
    private List<String> errors;
}
