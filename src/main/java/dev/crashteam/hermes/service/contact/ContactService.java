package dev.crashteam.hermes.service.contact;

import dev.crashteam.hermes.model.domain.UserContactEntity;
import dev.crashteam.hermes.model.dto.contact.ContactRequest;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.service.crm.CrmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    private final CrmService crmService;

    public Integer createContact(LeadRequest.Contact contact) {
        ContactRequest contactRequest = new ContactRequest(contact.getName(),
                Long.parseLong(contact.getPhone()), contact.getEmail());
        return crmService.createContact(contactRequest);
    }

    public UserContactEntity getContact(String userId) {
        return crmService.getContact(userId);
    }

    public UserContactEntity updateContact(String userId, UserContactEntity userContact) {
        return crmService.updateContact(userId, userContact);
    }

    public void verifyContact(String userId) {
        crmService.verifyContact(userId);
    }

}
