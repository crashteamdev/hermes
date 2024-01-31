package dev.crashteam.hermes.service.crm;

import dev.crashteam.hermes.model.domain.CrmUserEntity;
import dev.crashteam.hermes.model.domain.UserContactEntity;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.model.dto.lead.LeadResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CrmService {

    Integer createContact(String firstName, long phone);

    Integer createContact(List<LeadRequest.Contact> contact);

    LeadResponse createLead(LeadRequest leadRequest, int crmExternalId);

    UserContactEntity getContact(String userId);

    UserContactEntity updateContact(String userId, UserContactEntity userContact);

    void verifyContact(String userId);

    void saveApproveCode(String approveCode);

    @Transactional
    CrmUserEntity findByUserId(String userId);
}
