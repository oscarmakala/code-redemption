package com.divforce.cr.accountingservice;

import com.divforce.cr.accountingservice.api.web.PaymentDetails;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oscar Makala
 */
public class TestSpel {
    public static void main(String[] args) {
        SpelExpressionParser parser = new SpelExpressionParser();
        String template = "{ \"serviceCode\": \"CASHIN\",\"bearerCode\": \"USSD\",\"transactionAmount\": \"#{amount}\",\"currency\": \"101\",\"externalReferenceId\": \"#{transactionId}\",\"source\": \"CODE_REDEMPTION\",\"remarks\": \"CashIn\",\"transactionMode\": \"transactionMode\",\"initiator\": \"transactor\",\"language\": \"en\",\"transactor\": { \"idType\": \"mobileNumber\",\"idValue\": \"7701536457\",\"productId\": \"12\",\"mpin\": \"1357\",\"tpin\": \"1357\" },\"receiver\": { \"idType\": \"mobileNumber\",\"idValue\": \"#{mobile}\",\"productId\": \"12\" } }";
        var paymentDetails = new PaymentDetails("PP180115.0650.A00009", "785670105", BigDecimal.valueOf(500.0));
        var paymentContext = new StandardEvaluationContext(paymentDetails);
        var expression = parser.parseExpression(template, new TemplateParserContext());
        String request = expression.getValue(paymentContext, String.class);


        System.out.println(request);

        Map<String, Object> response = new HashMap<>();
        response.put("test", 2);


    }


}
