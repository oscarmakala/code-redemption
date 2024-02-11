package com.divforce.cr.voucherservice.domain;

import com.divforce.cr.audit.AuditRecordService;
import com.divforce.cr.audit.repository.AuditActionType;
import com.divforce.cr.audit.repository.AuditOperationType;
import com.divforce.cr.common.UnsupportedStateTransitionException;
import com.divforce.cr.encrypt.util.AttributeEncryptor;
import com.divforce.cr.voucherservice.exception.NoSuchCampaignException;
import com.divforce.cr.voucherservice.exception.VoucherNotUploadedException;
import com.divforce.cr.voucherservice.api.UploadVoucherResponse;
import com.divforce.cr.voucherservice.web.UploadedVoucher;
import com.univocity.parsers.common.RowProcessorErrorHandler;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

/**
 * @author Oscar Makala
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class CampaignService {
    private final VoucherService voucherService;
    private final CampaignRepository campaignRepository;
    private final AttributeEncryptor attributeEncryptor;
    private final AuditRecordService auditRecordService;

    @Transactional
    public Campaign create(String name, String description, String passPhrase, Integer maximumRedemptionsPerUser) {
        var campaign = new Campaign();
        campaign.setName(name);
        campaign.setDescription(description);
        campaign.setMaxRedemptionsPerUser(maximumRedemptionsPerUser);

        //add padding for 16 bit
        String paddedString = StringUtils.rightPad(passPhrase, 16);
        campaign.setPassPhrase(attributeEncryptor.convertToDatabaseColumn(paddedString));
        this.campaignRepository.save(campaign);
        return campaign;
    }


    @Transactional(readOnly = true)
    public Optional<Campaign> findById(Long campaignId) {
        return this.campaignRepository.findById(campaignId);
    }

    public void delete(Long id) {
        var campaign = this.campaignRepository.findById(id).orElseThrow(() -> new NoSuchCampaignException(id));
        this.campaignRepository.delete(campaign);
    }

    public Campaign activate(Long campaignId) {
        var campaign = this.campaignRepository.findById(campaignId).orElseThrow(() -> new NoSuchCampaignException(campaignId));
        campaign.activate();
        return this.campaignRepository.save(campaign);
    }

    public Campaign deActivate(Long campaignId) {
        var campaign = this.campaignRepository.findById(campaignId).orElseThrow(() -> new NoSuchCampaignException(campaignId));
        campaign.deActivate();
        return this.campaignRepository.save(campaign);
    }

    public Campaign approve(Long campaignId, CampaignStatus status, Principal principal) {
        var campaign = this.campaignRepository.findById(campaignId).orElseThrow(() -> new NoSuchCampaignException(campaignId));
        if (!campaign.hasVouchers()) {
            throw new VoucherNotUploadedException(campaignId);
        }
        if (status.equals(CampaignStatus.ACTIVE)) {
            campaign.approve(principal.getName());
        } else if (status.equals(CampaignStatus.PAUSED)) {
            campaign.pause();
        } else if (status.equals(CampaignStatus.DEACTIVATED)) {
            campaign.deActivate();
        } else {
            throw new UnsupportedStateTransitionException(status);
        }


        final Map<String, Object> mapAuditData = new HashMap<>(2);
        mapAuditData.put("pauseApprovedBy", principal.getName());
        mapAuditData.put("campaign", campaign.toString());

        this.auditRecordService.populateAndSaveAuditRecordUsingMapData(AuditOperationType.CAMPAIGN, AuditActionType.UPDATE, mapAuditData);

        return this.campaignRepository.save(campaign);
    }

    public Campaign reject(Long campaignId, Principal principal) {
        var campaign = this.campaignRepository.findById(campaignId).orElseThrow(() -> new NoSuchCampaignException(campaignId));
        campaign.reject(principal.getName());
        return this.campaignRepository.save(campaign);
    }


//    public Campaign pause(Long campaignId) {
//        var campaign = this.campaignRepository.findById(campaignId).orElseThrow(() -> new NoSuchCampaignException(campaignId));
//        campaign.pause(principal.getName());
//        return this.campaignRepository.save(campaign);
//    }



    public UploadVoucherResponse importVouchers(Long campaignId, MultipartFile file) {
        List<String> errors = new ArrayList<>();
        Campaign campaign = this.findById(campaignId).orElseThrow(() -> new NoSuchCampaignException(campaignId));
        List<UploadedVoucher> vouchers;

        if (!campaign.getStatus().equals(CampaignStatus.DRAFT)) {
            throw new InvalidFileException(file.getName());
        }
        try {
            vouchers = parseCsv(file, errors);
        } catch (IOException e) {
            throw new InvalidFileException(file.getName());
        }

        if (errors.isEmpty() && !vouchers.isEmpty()) {
            this.voucherService.batchCreate(vouchers, campaign);
        }

        return new UploadVoucherResponse(errors.size(), errors);
    }

    private List<UploadedVoucher> parseCsv(MultipartFile file, List<String> errors) throws IOException {
        // The settings object provides many configuration options
        CsvParserSettings parserSettings = new CsvParserSettings();
        // A RowListProcessor stores each parsed row in a List.
        BeanListProcessor<UploadedVoucher> rowProcessor = new BeanListProcessor<>(UploadedVoucher.class);
        // You can configure the parser to use a RowProcessor to process the values of each parsed row.
        parserSettings.setProcessor(new ConcurrentRowProcessor(rowProcessor));
        parserSettings.setProcessorErrorHandler((RowProcessorErrorHandler) (error, inputRow, parsingContext) -> {
            errors.add("Error details: column '" + error.getColumnName() + "' (index " + error.getColumnIndex() + ") has value '" + inputRow[error.getColumnIndex()] + "'");
        });
        // Let's consider the first parsed row as the headers of each column in the file.
        parserSettings.setHeaderExtractionEnabled(true);
        // creates a parser instance with the given settings
        CsvParser parser = new CsvParser(parserSettings);
        parser.parse(file.getInputStream());
        return rowProcessor.getBeans();
    }

    @Transactional(readOnly = true)
    public Page<Campaign> findBy(CampaignStatus[] status, Pageable pageable) {
        return this.campaignRepository.findByCampaignStatus(status, null, null, pageable);
    }


}
