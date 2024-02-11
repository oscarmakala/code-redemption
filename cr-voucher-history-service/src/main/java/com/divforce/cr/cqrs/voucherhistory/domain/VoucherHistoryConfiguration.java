package com.divforce.cr.cqrs.voucherhistory.domain;

import com.divforce.cr.encrypt.util.AttributeEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Oscar Makala
 */
@Configuration
public class VoucherHistoryConfiguration {
    @Bean
    AttributeEncryptor attributeEncryptor() throws Exception {
        return new AttributeEncryptor();
    }
}
