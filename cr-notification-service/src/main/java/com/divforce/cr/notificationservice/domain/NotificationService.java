package com.divforce.cr.notificationservice.domain;

import com.divforce.cr.common.RejectionReason;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author Oscar Makala
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SmsService smsService;
    private final NotificationRepository notificationRepository;
    private final NotificationProperties notificationProperties;


    public void send(String orderId, String mobile, RejectionReason rejectionReason, Object[] parameters) {
        log.info("send({},{})", mobile, rejectionReason);
        if (rejectionReason == null) {
            return;
        }
        try {
            String pattern = notificationProperties.messageFrom(rejectionReason.toString().toLowerCase(), "sw");
            MessageFormat formatter = new MessageFormat(pattern, Locale.US);
            String text = formatter.format(parameters);
            this.smsService.send(mobile, text, notificationProperties.getEndpoint(), notificationProperties.getHeaders(), notificationProperties.getTemplate());
            this.notificationRepository.save(Notification.create(orderId, mobile, text));
        } catch (Exception e) {
            log.error(e);
        }
    }


}
