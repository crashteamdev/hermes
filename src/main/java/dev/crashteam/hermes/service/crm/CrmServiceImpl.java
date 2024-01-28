package dev.crashteam.hermes.service.crm;

import dev.crashteam.hermes.mapper.CrmMapper;
import dev.crashteam.hermes.model.domain.Contact;
import dev.crashteam.hermes.model.domain.Crm;
import dev.crashteam.hermes.model.dto.contact.ContactRequest;
import dev.crashteam.hermes.model.dto.contact.ContactResponse;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.model.dto.lead.LeadResponse;
import dev.crashteam.hermes.repository.CrmRepository;
import dev.crashteam.hermes.service.feign.OkoCrmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final CrmMapper crmMapper;

    private static final Map<String, String> HEADERS = Map.of(
            "Accept", MediaType.APPLICATION_JSON_VALUE,
            "Authorization", "Bearer 0510599d43fb005883494861667b8c8c:ea6cb0d691bf40cc82f976fe8d761efe"
    );

    @Override
    public Integer createContact(String firstName, long phone) {
        return okoCrmClient.createContact(HEADERS, new ContactRequest(firstName, phone)).getId();
    }

    @Override
    public Integer createContact(List<LeadRequest.Contact> contact) {
        LeadRequest.Contact contactRequest = contact.stream().findFirst().get();
        return okoCrmClient.createContact(HEADERS, new ContactRequest(contactRequest.getName(), Long.parseLong(contactRequest.getPhone()))).getId();
    }

    @Override
    public LeadResponse createLead(LeadRequest leadRequest) {
        return okoCrmClient.createLead(HEADERS, leadRequest);
    }

    @Transactional
    @Override
    public void saveLead(LeadRequest leadRequest, int crmExternalId) {
        Crm save = crmRepository.save(crmMapper.mapLeadToCrm(leadRequest, crmExternalId));
        log.info("Новая запись в БД:[{}]", save);
    }

    @Transactional
    @Override
    public ContactResponse getContact(String userId) {
        Optional<Crm> byUserId = crmRepository.findByUserId(userId);
        if (byUserId.isPresent()) {
            return crmMapper.mapCrmToContactResponse(byUserId.get());
        } else {
            log.info("Пользователь с id:{} не найден", userId);
            return null;
        }
    }

    @Transactional
    @Override
    public Contact updateContact(String userId, Contact contact) {
        Crm crmToSave = crmRepository.findByUserId(userId).get();
        crmToSave.setEmail(contact.getEmail());
        crmToSave.setPhone(contact.getPhone());
        if (contact.getInn() != null) {
            crmToSave.setInn(contact.getInn());
        }
        Crm crm = crmRepository.save(crmToSave);
        log.info("Обновлена запись в БД [{}]", crm);
        return crmMapper.mapCrmToContact(crm);
    }

}
