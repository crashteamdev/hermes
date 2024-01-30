package dev.crashteam.hermes.service.crm;

import dev.crashteam.hermes.mapper.CrmMapper;
import dev.crashteam.hermes.model.domain.UserContactEntity;
import dev.crashteam.hermes.model.domain.CrmUserEntity;
import dev.crashteam.hermes.model.dto.contact.ContactRequest;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.model.dto.lead.LeadResponse;
import dev.crashteam.hermes.repository.CrmRepository;
import dev.crashteam.hermes.service.feign.OkoCrmClient;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CrmServiceImpl implements CrmService {

    private final CrmRepository crmRepository;
    private final OkoCrmClient okoCrmClient;

    @Value("${app.integration.oko-crm.token}")
    private String token;

    private final Map<String, String> headers = Map.of(
            "Accept", MediaType.APPLICATION_JSON_VALUE,
            "Authorization", "Bearer " + token
    );

    @Override
    public Integer createContact(String firstName, long phone) {
        return okoCrmClient.createContact(headers, new ContactRequest(firstName, phone)).getId();
    }

    @Override
    public Integer createContact(List<LeadRequest.Contact> contact) {
        LeadRequest.Contact contactRequest = contact.stream().findFirst().get();
        return okoCrmClient.createContact(headers, new ContactRequest(contactRequest.getName(), Long.parseLong(contactRequest.getPhone()))).getId();
    }

    @Transactional
    @Override
    public LeadResponse createLead(LeadRequest leadRequest, int crmExternalId) {
        CrmUserEntity save = crmRepository.save(CrmMapper.mapLeadToCrm(leadRequest, crmExternalId));
        log.info("New lead in the DB:[{}]", save);
        return okoCrmClient.createLead(headers, leadRequest);
    }

    @Transactional
    @Override
    public UserContactEntity getContact(String userId) {
        Optional<CrmUserEntity> byUserId = crmRepository.findByUserId(userId);
        if (byUserId.isPresent()) {
            return CrmMapper.mapCrmToContactResponse(byUserId.get());
        } else {
            log.info("Contact with user_id:{} not found", userId);
            return null;
        }
    }

    @Transactional
    @Override
    public UserContactEntity updateContact(String userId, UserContactEntity userContact) {
        CrmUserEntity crmToSave = crmRepository.findByUserId(userId).get();
        crmToSave.setEmail(userContact.getEmail());
        crmToSave.setPhone(userContact.getPhone());
        if (userContact.getInn() != null) {
            crmToSave.setInn(userContact.getInn());
        }
        CrmUserEntity crm = crmRepository.save(crmToSave);
        log.info("Update contact in the DB [{}]", crm);
        return CrmMapper.mapCrmToContact(crm);
    }

    @Transactional
    @Override
    public void verifyContact(String userId) {
        CrmUserEntity crm = crmRepository.findByUserId(userId).get();
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
    }

}
