package dev.crashteam.hermes.mapper;

import dev.crashteam.crm.CreateLeadRequest;
import dev.crashteam.hermes.model.UtmTag;
import dev.crashteam.hermes.model.domain.CrmUserEntity;
import dev.crashteam.hermes.model.domain.UserContactEntity;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class CrmMapper {

    public static CrmUserEntity mapLeadToCrm(LeadRequest source, int crmExternalId) {
        LeadRequest.Contact contact = source.getContact();
        String firstName = contact.getName();
        long phone = Long.parseLong(contact.getPhone());

        CrmUserEntity crm = new CrmUserEntity();
        crm.setCrmExternalId(String.valueOf(crmExternalId));
        crm.setFirstName(firstName);
        crm.setPhone(phone);

        return crm;
    }

    public static UserContactEntity mapCrmToContactResponse(CrmUserEntity crm) {
        UserContactEntity userContact = new UserContactEntity();
        userContact.setPhone(crm.getPhone());
        userContact.setEmail(crm.getEmail());
        userContact.setInn(crm.getInn());
        userContact.setApproveCode(crm.getApproveCode());
        userContact.setVerification(crm.isVerification());
        return userContact;
    }

    public static UserContactEntity mapCrmToContact(CrmUserEntity crm) {
        UserContactEntity userContact = new UserContactEntity();
        userContact.setEmail(crm.getEmail());
        userContact.setPhone(crm.getPhone());
        if (crm.getInn() != null) {
            userContact.setInn(crm.getInn());
        }
        return userContact;
    }

    public static CrmUserEntity mapContactToCrm(UserContactEntity userContact) {
        CrmUserEntity crm = new CrmUserEntity();
        crm.setEmail(userContact.getEmail());
        crm.setPhone(userContact.getPhone());
        if (userContact.getInn() != null) {
            crm.setInn(userContact.getInn());
        }
        return crm;
    }

    public static UtmTag mapUtmToCrm(CreateLeadRequest.UtmTag grpcUtmTag) {
        UtmTag utmTag = new UtmTag();
        utmTag.setCampaign(grpcUtmTag.getUtmCampaign());
        utmTag.setTerm(grpcUtmTag.getUtmTerm());
        utmTag.setContent(grpcUtmTag.getUtmContent());
        utmTag.setSource(grpcUtmTag.getUtmSource());
        utmTag.setMedium(grpcUtmTag.getUtmMedium());
        return utmTag;
    }

}
