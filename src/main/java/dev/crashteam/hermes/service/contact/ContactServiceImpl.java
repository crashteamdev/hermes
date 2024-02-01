package dev.crashteam.hermes.service.contact;

import dev.crashteam.hermes.model.domain.UserContactEntity;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.service.crm.CrmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final CrmService crmService;

    @Override
    public Integer createContact(String firstName, long phone, String email) {
        return crmService.createContact(firstName, phone, email);
    }

    @Override
    public Integer createContact(List<LeadRequest.Contact> contact) {
        return crmService.createContact(contact);
    }

    @Override
    public UserContactEntity getContact(String userId) {
        return crmService.getContact(userId);
    }

    @Override
    public UserContactEntity updateContact(String userId, UserContactEntity userContact) {
        return crmService.updateContact(userId, userContact);
    }

    @Override
    public void verifyContact(String userId) {
        crmService.verifyContact(userId);
    }

}
