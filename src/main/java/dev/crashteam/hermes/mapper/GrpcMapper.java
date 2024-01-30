package dev.crashteam.hermes.mapper;

import dev.crashteam.crm.CreateLeadRequest;
import dev.crashteam.crm.GetUserContactInfoResponse;
import dev.crashteam.crm.UpdateUserContactInfoRequest;
import dev.crashteam.crm.UpdateUserContactInfoState;
import dev.crashteam.crm.UserContact;
import dev.crashteam.hermes.model.domain.Contact;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class GrpcMapper {

    public static LeadRequest map(CreateLeadRequest request) {
        LeadRequest.Contact contact
                = new LeadRequest.Contact(request.getUserIdentity().getFirstName(), String.valueOf(request.getUserPhoneNumber().getPhoneNumber()));
        return new LeadRequest("Демо", contact);
    }

    public static GetUserContactInfoResponse mapToUserContact(Contact contact) {
        return GetUserContactInfoResponse.newBuilder()
                .setSuccessResponse(GetUserContactInfoResponse.SuccessResponse.newBuilder()
                        .setUserContact(UserContact.newBuilder()
                                .setEmail(contact.getEmail() != null ? contact.getEmail() : "")
                                .setPhone(contact.getPhone())
                                .setInn(contact.getInn() != null ? contact.getInn().toString() : "")
                                .build())
                        .setUserContactInfoState(
                                contact.isVerification() ? UpdateUserContactInfoState.UPDATE_USER_CONTACT_INFO_STATE_VERIFIED
                                        : UpdateUserContactInfoState.UPDATE_USER_CONTACT_INFO_STATE_NOT_VERIFIED
                        )
                        .build())
                .build();
    }

    public static Contact map(UpdateUserContactInfoRequest.InitialUpdateContactInfoPayload contactInfoPayload) {
        Contact contact = new Contact();
        contact.setEmail(contactInfoPayload.getEmail());
        contact.setPhone(contactInfoPayload.getPhoneNumber());
        if (!contactInfoPayload.getInn().isBlank()) {
            contact.setInn(Long.parseLong(contactInfoPayload.getInn()));
        }
        return contact;
    }

    public static UserContact map(Contact contact) {
        return UserContact.newBuilder()
                .setEmail(contact.getEmail() != null ? contact.getEmail() : "")
                .setPhone(contact.getPhone())
                .setInn(contact.getInn() != null ? contact.getInn().toString() : "")
                .build();
    }

}
