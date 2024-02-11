package com.divforce.cr.cqrs.voucherhistory;

import io.eventuate.tram.spring.consumer.common.TramConsumerCommonConfiguration;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author Oscar Makala
 */
@SpringBootApplication
@Import({EventuateTramKafkaMessageConsumerConfiguration.class, TramConsumerCommonConfiguration.class})
public class VoucherHistoryMain {
    public static void main(String[] args) {
        SpringApplication.run(VoucherHistoryMain.class, args);
    }
}
