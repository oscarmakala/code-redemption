package com.divforce.cr.disbursment.common;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Oscar Makala
 */
@CommonsLog
public class DisbursementTemplate {
    private final AbstractDisbursementSource source;
    private final SpelExpressionParser parser;
    private final RestTemplate restTemplate;
    private final ResponseMapper<DisburseDto> mapper;

    public DisbursementTemplate(AbstractDisbursementSource source, RestTemplate restTemplate, ResponseMapper<DisburseDto> mapper) {
        this.source = source;
        this.parser = new SpelExpressionParser();
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public DisburseDto execute(String mobile,
                               String transactionId,
                               BigDecimal amount) throws Exception {
        return makeRequest(createPayload(mobile, transactionId, amount));
    }

    private DisburseDto makeRequest(String payload) throws Exception {
        log.info("makeRequest:req(" + payload + ")");
        var headers = new HttpHeaders();
        //todo move this to the headers section
        headers.setContentType(MediaType.APPLICATION_JSON);
        enrichHeaders(headers);
        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        String response = this.restTemplate.postForObject(source.getEndpoint(), request, String.class);
        log.info("makeRequest:resp(" + response + ")");
        return mapper.mapResponse(response);
    }

    private void enrichHeaders(HttpHeaders headers) {
        log.debug("Source:" + source.toString());
        if (source.getHeaders() == null || source.getHeaders().isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : source.getHeaders().entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
            log.debug("header " + entry.getKey() + " " + entry.getValue());
        }
    }

    private String createPayload(String mobile, String transactionId, BigDecimal amount) {
        var paymentContext = new StandardEvaluationContext(new PaymentDetails(transactionId, mobile, amount));
        return parser
                .parseExpression(source.getTemplate(), new TemplateParserContext())
                .getValue(paymentContext, String.class);
    }
}
