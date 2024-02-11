package com.divforce.cr.voucherservice.domain;

import com.divforce.cr.encrypt.util.AttributeEncryptor;
import com.divforce.cr.common.SerialNumberGenerator;
import com.divforce.cr.encrypt.util.CampaignCryptoException;
import com.divforce.cr.voucherservice.api.events.VoucherDomainEvent;
import com.divforce.cr.voucherservice.exception.NoSuchVoucherException;
import com.divforce.cr.voucherservice.exception.VoucherAlreadyExistsException;
import com.divforce.cr.voucherservice.web.UploadedVoucher;
import com.divforce.cr.voucherservice.api.VoucherExpiryAndDate;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Oscar Makala
 * <p>
 * https://githubmemory.com/repo/eventuate-tram/eventuate-tram-core/issues/118
 * Also, code invoked by message handlers shouldn't use Spring's @Transactional
 * since that interferes with Eventuate Tram's transaction management. The stack trace suggests that @Transactional is being used.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherDomainEventPublisher domainEventPublisher;
    private final AttributeEncryptor attributeEncryptor;


    public void validateVoucher(String serialNumber, String orderId) {
        log.info("validateOrderForVoucher({},{})", serialNumber, orderId);
        voucherRepository.findById(serialNumber).orElseThrow(() -> new NoSuchVoucherException(serialNumber)).validateVoucher();
    }


    public void reserveVoucher(String serialNumber, String orderId) {
        log.info("reserveVoucher({},{})", serialNumber, orderId);
        var voucher = voucherRepository.findById(serialNumber).orElseThrow(() -> new NoSuchVoucherException(serialNumber));
        List<VoucherDomainEvent> events = voucher.reserve();
        domainEventPublisher.publish(voucher, events);
    }


    public void rejectReservation(String serialNumber) {
        log.info("rejectReservation({})", serialNumber);
        var voucher = voucherRepository.findById(serialNumber).orElseThrow(() -> new NoSuchVoucherException(serialNumber));
        List<VoucherDomainEvent> events = voucher.rejectReservation();
        domainEventPublisher.publish(voucher, events);
    }


    public void fulFillReservation(String serialNumber) {
        log.info("fulFillReservation({})", serialNumber);
        var voucher = voucherRepository.findById(serialNumber).orElseThrow(() -> new NoSuchVoucherException(serialNumber));
        List<VoucherDomainEvent> events = voucher.used();
        domainEventPublisher.publish(voucher, events);
    }

    public Optional<Voucher> findById(String serialNumber) {
        return this.voucherRepository.findById(SerialNumberGenerator.generate(serialNumber));
    }


    public Page<Voucher> findAllByCampaignIdAndCode(Long campaignId, String code, Pageable pageable) {
        Page<Voucher> result;
        if (StringUtils.isNotEmpty(code)) {
            //decode the text.
            String serial = SerialNumberGenerator.generate(code);
            result = this.voucherRepository.findBySerialNumber(serial, pageable);
        } else if (campaignId != null) {
            result = this.voucherRepository.findByCampaign_Id(campaignId, pageable);
        } else {
            result = this.voucherRepository.findAll(pageable);
        }
        return result;
    }



    public void batchCreate(List<UploadedVoucher> vouchers, Campaign campaign) {
        vouchers.forEach(uv -> {
            String serialNumber = SerialNumberGenerator.generate(uv.getCode());
            this.voucherRepository.findById(serialNumber).ifPresent(voucher -> {
                throw new VoucherAlreadyExistsException(uv.getCode());
            });

            AttributeEncryptor codeEncoder = createEncryptor(campaign.getPassPhrase());
            String encryptedCode = codeEncoder.convertToDatabaseColumn(uv.getCode());
            ResultWithDomainEvents<Voucher, VoucherDomainEvent> voucherAndEvents = Voucher.create(encryptedCode, uv.getAmount(), uv.getCurrency(), uv.getExpireAt(), campaign, serialNumber);
            Voucher voucher = voucherAndEvents.result;
            voucherRepository.save(voucher);
            domainEventPublisher.publish(voucher, voucherAndEvents.events);
        });

    }

    private AttributeEncryptor createEncryptor(String passPhrase) {
        try {
            return new AttributeEncryptor(attributeEncryptor.convertToEntityAttribute(passPhrase));
        } catch (Exception e) {
            throw new CampaignCryptoException("");
        }
    }

    public VoucherExpiryAndDate decryptVoucher(Long campaignId, String code, String password) {
        return this.voucherRepository.findByCampaignIdAndCode(campaignId, code).map(voucher -> {
            String paddedString = StringUtils.rightPad(password, 16);
            AttributeEncryptor encryptor;
            try {
                encryptor = new AttributeEncryptor(paddedString);
                String decryptedCode = encryptor.convertToEntityAttribute(voucher.getCode());
                Date expiry = voucher.getExpireAt();
                return new VoucherExpiryAndDate(decryptedCode, expiry);
            } catch (Exception e) {
                throw new CampaignCryptoException(password);
            }
        }).orElseThrow(() -> new NoSuchVoucherException(code));
    }
}
