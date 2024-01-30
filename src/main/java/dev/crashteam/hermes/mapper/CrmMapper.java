package dev.crashteam.hermes.mapper;

import dev.crashteam.hermes.model.domain.Contact;
import dev.crashteam.hermes.model.domain.CrmUser;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class CrmMapper {

    public static CrmUser mapLeadToCrm(LeadRequest source, int crmExternalId) {
        LeadRequest.Contact contact = source.getContact();
        String firstName = contact.getName();
        long phone = Long.parseLong(contact.getPhone());

        CrmUser crm = new CrmUser();
        crm.setCrmExternalId(String.valueOf(crmExternalId));
        crm.setFirstName(firstName);
        crm.setPhone(phone);

        return crm;
    }

    public static Contact mapCrmToContactResponse(CrmUser crm) {
        Contact contact = new Contact();
        contact.setPhone(crm.getPhone());
        contact.setEmail(crm.getEmail());
        contact.setInn(crm.getInn());
        contact.setApproveCode(crm.getApproveCode());
        contact.setVerification(crm.isVerification());
        return contact;
    }

    public static Contact mapCrmToContact(CrmUser crm) {
        Contact contact = new Contact();
        contact.setEmail(crm.getEmail());
        contact.setPhone(crm.getPhone());
        if (crm.getInn() != null) {
            contact.setInn(crm.getInn());
        }
        return contact;
    }

    public static CrmUser mapContactToCrm(Contact contact) {
        CrmUser crm = new CrmUser();
        crm.setEmail(contact.getEmail());
        crm.setPhone(contact.getPhone());
        if (contact.getInn() != null) {
            crm.setInn(contact.getInn());
        }
        return crm;
    }

}
