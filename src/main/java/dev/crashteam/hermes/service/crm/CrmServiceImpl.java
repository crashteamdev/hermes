package dev.crashteam.hermes.service.crm;

import dev.crashteam.hermes.exception.ContactNotFoundException;
import dev.crashteam.hermes.mapper.CrmMapper;
import dev.crashteam.hermes.model.domain.CrmUserEntity;
import dev.crashteam.hermes.model.domain.UserContactEntity;
import dev.crashteam.hermes.model.domain.UserFeedbackEntity;
import dev.crashteam.hermes.model.dto.contact.ContactRequest;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.model.dto.lead.LeadResponse;
import dev.crashteam.hermes.model.dto.pipeline.PipelineStagesResponse;
import dev.crashteam.hermes.model.dto.pipeline.PipelinesResponse;
import dev.crashteam.hermes.repository.CrmRepository;
import dev.crashteam.hermes.repository.UserFeedbackRepository;
import dev.crashteam.hermes.service.feign.OkoCrmClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CrmServiceImpl implements CrmService {

    public CrmServiceImpl(@Value("${app.integration.oko-crm.token}") String token, CrmRepository crmRepository, OkoCrmClient okoCrmClient, UserFeedbackRepository userFeedbackRepository) {
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

    @Override
    public PipelinesResponse getPipelines() {
        return okoCrmClient.getPipelines(headers);
    }

    @Override
    public PipelineStagesResponse getPipelineStagesResponse(int pipelineId) {
        return okoCrmClient.getPipelineStages(headers, pipelineId);
    }

    @Override
    public Integer createContact(String firstName, long phone, String email) {
        return okoCrmClient.createContact(headers, new ContactRequest(firstName, phone, email)).getId();
    }

    @Override
    public Integer createContact(List<LeadRequest.Contact> contacts) {
        LeadRequest.Contact contact = contacts.stream().findFirst().get();
        ContactRequest contactRequest = new ContactRequest(contact.getName(),
                Long.parseLong(contact.getPhone()), contact.getEmail());
        return okoCrmClient.createContact(headers, contactRequest).getId();
    }

    @Transactional
    @Override
    public LeadResponse createLead(LeadRequest leadRequest) {
        return okoCrmClient.createLead(headers, leadRequest);
    }

    @Transactional
    @Override
    public void saveFeedback(UserFeedbackEntity userFeedback) {
        userFeedbackRepository.save(userFeedback);
    }

    @Transactional
    @Override
    public UserContactEntity getContact(String userId) {
        CrmUserEntity crmUserEntity = findByUserId(userId);
        return CrmMapper.mapCrmToContactResponse(crmUserEntity);
    }

    @Transactional
    @Override
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
    @Override
    public void verifyContact(String userId) {
        CrmUserEntity crm = findByUserId(userId);
        crm.setVerification(true);
        crmRepository.save(crm);
        log.info("Contact with user_id [Id: {}] verified", userId);
    }

    @Transactional
    @Override
    public void saveApproveCode(String approveCode) {
        CrmUserEntity crmUser = new CrmUserEntity();
        crmUser.setApproveCode(approveCode);
        crmRepository.save(crmUser);
        log.info("saved approve code");
    }

    @Transactional
    @Override
    public CrmUserEntity findByUserId(String userId) {
        Optional<CrmUserEntity> crmToSaveOptional = crmRepository.findByUserId(userId);
        if (crmToSaveOptional.isPresent()) {
            return crmToSaveOptional.get();
        } else {
            throw new ContactNotFoundException("Contact with user_id:%s not found".formatted(userId));
        }
    }

}
