package dev.crashteam.hermes.service.crm;

import dev.crashteam.hermes.mapper.CrmMapper;
import dev.crashteam.hermes.model.domain.Contact;
import dev.crashteam.hermes.model.domain.CrmUser;
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
        CrmUser save = crmRepository.save(CrmMapper.mapLeadToCrm(leadRequest, crmExternalId));
        log.info("New lead in the DB:[{}]", save);
        return okoCrmClient.createLead(headers, leadRequest);
    }

    @Transactional
    @Override
    public Contact getContact(String userId) {
        Optional<CrmUser> byUserId = crmRepository.findByUserId(userId);
        if (byUserId.isPresent()) {
            return CrmMapper.mapCrmToContactResponse(byUserId.get());
        } else {
            log.info("Contact with user_id:{} not found", userId);
            return null;
        }
    }

    @Transactional
    @Override
    public Contact updateContact(String userId, Contact contact) {
        CrmUser crmToSave = crmRepository.findByUserId(userId).get();
        crmToSave.setEmail(contact.getEmail());
        crmToSave.setPhone(contact.getPhone());
        if (contact.getInn() != null) {
            crmToSave.setInn(contact.getInn());
        }
        CrmUser crm = crmRepository.save(crmToSave);
        log.info("Update contact in the DB [{}]", crm);
        return CrmMapper.mapCrmToContact(crm);
    }

    @Transactional
    @Override
    public void verifyContact(String userId) {
        CrmUser crm = crmRepository.findByUserId(userId).get();
        crm.setVerification(true);
        crmRepository.save(crm);
        log.info("Contact with user_id [Id: {}] verified", userId);
    }

    @Transactional
    @Override
    public void saveApproveCode(String approveCode) {
        CrmUser crmUser = new CrmUser();
        crmUser.setApproveCode(approveCode);
        crmRepository.save(crmUser);
    }

}
