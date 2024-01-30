package dev.crashteam.hermes.service.contact;

import dev.crashteam.hermes.model.domain.UserContactEntity;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;

import java.util.List;

public interface ContactService {

    Integer createContact(String firstName, long phone);

    Integer createContact(List<LeadRequest.Contact> contact);

    UserContactEntity getContact(String userId);

    UserContactEntity updateContact(String userId, UserContactEntity userContact);

    void verifyContact(String userId);

}
