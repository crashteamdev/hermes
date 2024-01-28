package dev.crashteam.hermes.service.crm;

import dev.crashteam.hermes.model.domain.Contact;
import dev.crashteam.hermes.model.dto.contact.ContactResponse;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.model.dto.lead.LeadResponse;

import java.util.List;

public interface CrmService {

    Integer createContact(String firstName, long phone);

    Integer createContact(List<LeadRequest.Contact> contact);

    LeadResponse createLead(LeadRequest leadRequest);

    void saveLead(LeadRequest leadRequest, int crmExternalId);

    ContactResponse getContact(String userId);

    Contact updateContact(String userId, Contact contact);

}
