package dev.crashteam.hermes.service.contact;

import dev.crashteam.hermes.model.domain.Contact;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;

import java.util.List;

public interface ContactService {

    Integer createContact(String firstName, long phone);

    Integer createContact(List<LeadRequest.Contact> contact);

    Contact getContact(String userId);

    Contact updateContact(String userId, Contact contact);

    void verifyContact(String userId);

}
