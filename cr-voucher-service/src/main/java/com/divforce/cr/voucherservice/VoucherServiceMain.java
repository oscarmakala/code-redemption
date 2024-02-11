package com.divforce.cr.voucherservice;

import com.divforce.cr.audit.AppAuditingConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


/**
 * @author Oscar Makala
 */
@SpringBootApplication
@Import({
        OptimisticLockingDecoratorConfiguration.class,
        AppAuditingConfiguration.class
})
public class VoucherServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(VoucherServiceMain.class, args);
    }


}
