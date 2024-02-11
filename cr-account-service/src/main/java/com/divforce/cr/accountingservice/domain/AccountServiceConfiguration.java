package com.divforce.cr.accountingservice.domain;

import com.divforce.cr.azampay.AzamError;
import com.divforce.cr.azampay.AzamResponse;
import com.divforce.cr.disbursment.common.AbstractDisbursementSource;
import com.divforce.cr.disbursment.common.DisburseDto;
import com.divforce.cr.disbursment.common.DisbursementTemplate;
import com.divforce.cr.disbursment.common.ResponseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

/**
 * @author Oscar Makala
 */
@Log4j2
@Configuration
public class AccountServiceConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {


        var sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, new TrustAllStrategy())
                .build();

        var csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", csf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();


        return builder
                .setConnectTimeout(Duration.ofMillis(10000))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "vendors.disbursement")
    public AbstractDisbursementSource source() {
        return new AbstractDisbursementSource();
    }

    @Bean
    public ResponseMapper<DisburseDto> azamPayMapper(ObjectMapper objectMapper) {
        return response -> {
            AzamResponse azamResponse = objectMapper.readValue(response, AzamResponse.class);
            AzamError azamError = azamResponse.error();
            return new DisburseDto(
                    azamResponse.getStatus(),
                    azamResponse.getTransactionId(),
                    azamResponse.getServiceRequestId(),
                    azamError.getCode(),
                    azamError.getMessage(),
                    azamResponse.getMessage()
            );
        };
    }

    @Bean
    public DisbursementTemplate disbursementTemplate(RestTemplate restTemplate,
                                                     AbstractDisbursementSource source,
                                                     ResponseMapper<DisburseDto> mapper) {
        return new DisbursementTemplate(source, restTemplate, mapper);
    }


}
