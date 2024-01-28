package dev.crashteam.hermes.service.contact;

import dev.crashteam.hermes.model.domain.Contact;
import dev.crashteam.hermes.model.dto.contact.ContactResponse;
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
    public Integer createContact(String firstName, long phone) {
        return crmService.createContact(firstName, phone);
    }

    @Override
    public Integer createContact(List<LeadRequest.Contact> contact) {
        return crmService.createContact(contact);
    }

    @Override
    public ContactResponse getContact(String userId) {
        return crmService.getContact(userId);
    }

    @Override
    public Contact updateContact(String userId, Contact contact) {
        return crmService.updateContact(userId, contact);
    }

}
