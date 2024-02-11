package com.divforce.cr.notificationservice.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author Oscar Makala
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class InfoBipSmsService implements SmsService {
    private final RestTemplate restTemplate;
    private SpelExpressionParser parser;

    @PostConstruct
    public void init() {
        parser = new SpelExpressionParser();
    }

    @Override
    public void send(String mobile, String message, String endpoint, Map<String, String> headers, String template) {
        String msisdn = formatMsisdn(mobile);
        var infoBip = new InfoBip(msisdn, message);
        var infoBipContext = new StandardEvaluationContext(infoBip);
        var expression = parser.parseExpression(template, new TemplateParserContext());
        String payload = expression.getValue(infoBipContext, String.class);

        log.info("send {} {}", endpoint, payload);
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpHeaders.add(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(payload, httpHeaders);
        String responseJson = restTemplate.postForObject(endpoint, httpEntity, String.class);
        log.info("sendResponse({})", responseJson);
    }

    private String formatMsisdn(String msisdn) {
        return "255" + right(msisdn);
    }

    private String right(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() <= 9) {
            return str;
        } else {
            return str.substring(str.length() - 9);
        }
    }
}
