package com.divforce.cr.accountingservice;

import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author Oscar Makala
 */
@SpringBootApplication
public class AccountServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceMain.class, args);
    }
}
