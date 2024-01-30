package dev.crashteam.hermes.mapper;

import dev.crashteam.hermes.model.domain.Contact;
import dev.crashteam.hermes.model.domain.CrmDomain;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class CrmMapper {

    public static CrmDomain mapLeadToCrm(LeadRequest source, int crmExternalId) {
        LeadRequest.Contact contact = source.getContact();
        String firstName = contact.getName();
        long phone = Long.parseLong(contact.getPhone());

        CrmDomain crm = new CrmDomain();
        crm.setCrmExternalId(String.valueOf(crmExternalId));
        crm.setFirstName(firstName);
        crm.setPhone(phone);

        return crm;
    }

    public static Contact mapCrmToContactResponse(CrmDomain crm) {
        Contact contact = new Contact();
        contact.setPhone(crm.getPhone());
        contact.setEmail(crm.getEmail());
        if (crm.getInn() != null) {
            contact.setInn(crm.getInn());
        } else {
            log.info("INN not found");
        }
        contact.setVerification(crm.isVerification());
        return contact;
    }

    public static Contact mapCrmToContact(CrmDomain crm) {
        Contact contact = new Contact();
        contact.setEmail(crm.getEmail());
        contact.setPhone(crm.getPhone());
        if (crm.getInn() != null) {
            contact.setInn(crm.getInn());
        }
        return contact;
    }

    public static CrmDomain mapContactToCrm(Contact contact) {
        CrmDomain crm = new CrmDomain();
        crm.setEmail(contact.getEmail());
        crm.setPhone(contact.getPhone());
        if (contact.getInn() != null) {
            crm.setInn(contact.getInn());
        }
        return crm;
    }

}
