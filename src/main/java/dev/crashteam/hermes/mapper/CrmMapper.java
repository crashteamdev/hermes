package dev.crashteam.hermes.mapper;

import dev.crashteam.hermes.model.domain.Contact;
import dev.crashteam.hermes.model.domain.Crm;
import dev.crashteam.hermes.model.dto.contact.ContactResponse;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CrmMapper {

    public Crm mapLeadToCrm(LeadRequest source, int crmExternalId) {
        LeadRequest.Contact contact = source.getContact();
        String firstName = contact.getName();
        long phone = Long.parseLong(contact.getPhone());

        Crm crm = new Crm();
        crm.setCrmExternalId(String.valueOf(crmExternalId));
        crm.setFirstName(firstName);
        crm.setPhone(phone);

        return crm;
    }

    public ContactResponse mapCrmToContactResponse(Crm crm) {
        ContactResponse contact = new ContactResponse(crm.getEmail(), crm.getPhone());
        if (crm.getInn() != null) {
            contact.setInn(crm.getInn().toString());
        } else {
            contact.setInn("");
            log.info("ИНН не найден.");
        }
        return contact;
    }

    public Contact mapCrmToContact(Crm crm) {
        Contact contact = new Contact();
        contact.setEmail(crm.getEmail());
        contact.setPhone(crm.getPhone());
        if (crm.getInn() != null) {
            contact.setInn(crm.getInn());
        }
        return contact;
    }

    public Crm mapContactToCrm(Contact contact) {
        Crm crm = new Crm();
        crm.setEmail(contact.getEmail());
        crm.setPhone(contact.getPhone());
        if (contact.getInn() != null) {
            crm.setInn(contact.getInn());
        }
        return crm;
    }

}
