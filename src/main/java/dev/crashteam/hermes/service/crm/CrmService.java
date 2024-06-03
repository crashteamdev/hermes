package dev.crashteam.hermes.service.crm;

import dev.crashteam.hermes.exception.ContactNotFoundException;
import dev.crashteam.hermes.exception.integration.crm.CreateLeadException;
import dev.crashteam.hermes.mapper.CrmMapper;
import dev.crashteam.hermes.model.domain.CrmUserEntity;
import dev.crashteam.hermes.model.domain.UserContactEntity;
import dev.crashteam.hermes.model.domain.UserFeedbackEntity;
import dev.crashteam.hermes.model.dto.contact.ContactRequest;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.model.dto.pipeline.PipelineStagesResponse;
import dev.crashteam.hermes.repository.CrmRepository;
import dev.crashteam.hermes.repository.UserFeedbackRepository;
import dev.crashteam.hermes.service.feign.OkoCrmClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Service
public class CrmService {

    public CrmService(@Value("${app.integration.oko-crm.token}") String token, CrmRepository crmRepository, OkoCrmClient okoCrmClient, UserFeedbackRepository userFeedbackRepository) {
        headers = Map.of(
                "Accept", MediaType.APPLICATION_JSON_VALUE,
                "Authorization", "Bearer " + token
        );
        this.crmRepository = crmRepository;
        this.okoCrmClient = okoCrmClient;
        this.userFeedbackRepository = userFeedbackRepository;
    }

    private final CrmRepository crmRepository;
    private final OkoCrmClient okoCrmClient;
    private final UserFeedbackRepository userFeedbackRepository;
    private final Map<String, String> headers;

    public PipelineStagesResponse getPipelineStagesResponse(int pipelineId) {
        return okoCrmClient.getPipelineStages(headers, pipelineId);
    }

    public Integer createContact(ContactRequest contactRequest) {
        try {
            log.info("[Request] Create contact: [{}]", contactRequest);
            Integer contactId = okoCrmClient.createContact(headers, contactRequest).getId();
            log.info("[Response] Create contact: [{}]", contactId);
            return contactId;
        } catch (Exception e) {
            Properties properties = new Properties();
            try {
                properties.load(new StringReader("Create Contact ERROR: [%s]".formatted(e.getMessage())));
            } catch (IOException ex) {
                log.warn(e.getMessage());
            }
            log.warn(properties.getProperty("Create"));
//            throw new CreateContactException("Create Contact ERROR: [%s]".formatted(e.getMessage()));
        }

        return -1;
    }

    public Integer createLead(LeadRequest leadRequest) {
        try {
            log.info("[Request] Create lead: [{}]", leadRequest);
            int leadId = okoCrmClient.createLead(headers, leadRequest).getId();
            log.info("[Response] Create lead: [{}]", leadId);
            return leadId;
        } catch (Exception e) {
            throw new CreateLeadException("Create Lead ERROR: [%s]".formatted(e.getMessage()));
        }
    }

    @Transactional
    public void saveFeedback(UserFeedbackEntity userFeedback) {
        userFeedbackRepository.save(userFeedback);
    }

    @Transactional
    public UserContactEntity getContact(String userId) {
        CrmUserEntity crmUserEntity = findByUserId(userId);
        return CrmMapper.mapCrmToContactResponse(crmUserEntity);
    }

    @Transactional
    public UserContactEntity updateContact(String userId, UserContactEntity userContact) {
        CrmUserEntity crmToSave = findByUserId(userId);
        crmToSave.setEmail(userContact.getEmail());
        crmToSave.setPhone(userContact.getPhone());
        crmToSave.setInn(userContact.getInn());

        CrmUserEntity crm = crmRepository.save(crmToSave);

        log.info("Update contact [{}]", crm);
        return CrmMapper.mapCrmToContact(crm);
    }

    @Transactional
    public void verifyContact(String userId) {
        CrmUserEntity crm = findByUserId(userId);
        crm.setVerification(true);
        crmRepository.save(crm);
        log.info("Contact with user_id [Id: {}] verified", userId);
    }

    @Transactional
    public void saveApproveCode(String approveCode) {
        CrmUserEntity crmUser = new CrmUserEntity();
        crmUser.setApproveCode(approveCode);
        crmRepository.save(crmUser);
        log.info("saved approve code");
    }

    @Transactional
    public CrmUserEntity findByUserId(String userId) {
        Optional<CrmUserEntity> crmToSaveOptional = crmRepository.findByUserId(userId);
        if (crmToSaveOptional.isPresent()) {
            return crmToSaveOptional.get();
        } else {
            throw new ContactNotFoundException("Contact with user_id:%s not found".formatted(userId));
        }
    }

}
