package dev.crashteam.hermes.mapper;

import dev.crashteam.crm.CreateLeadRequest;
import dev.crashteam.crm.GetUserContactInfoResponse;
import dev.crashteam.crm.UpdateUserContactInfoRequest;
import dev.crashteam.crm.UpdateUserContactInfoState;
import dev.crashteam.hermes.model.domain.UserContactEntity;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class GrpcMapper {

    public static LeadRequest map(CreateLeadRequest.CreateDemoLead request) {
        String firstName = request.getUserIdentity().getFirstName();
        String phone = String.valueOf(request.getUserPhoneNumber().getPhoneNumber());
        String userEmail = request.getUserEmail();
        LeadRequest.Contact contact = new LeadRequest.Contact(firstName, phone, userEmail);
        return new LeadRequest(contact);
    }

    public static LeadRequest map(CreateLeadRequest.CreateServiceLead request) {
        String serviceName = request.getServiceName();
        String firstName = request.getUserIdentity().getFirstName();
        String phone = String.valueOf(request.getUserPhoneNumber().getPhoneNumber());
        String userEmail = request.getUserEmail();
        LeadRequest.Contact contact = new LeadRequest.Contact(firstName, phone, userEmail);
        return new LeadRequest(serviceName, contact);
    }

    public static LeadRequest map(CreateLeadRequest.CreateFeedbackLead request) {
        String firstName = request.getUserIdentity().getFirstName();
        String phone = String.valueOf(request.getUserPhoneNumber().getPhoneNumber());
        String userEmail = request.getUserEmail();
        LeadRequest.Contact contact = new LeadRequest.Contact(firstName, phone, userEmail);
        return new LeadRequest(contact);
    }

    public static GetUserContactInfoResponse mapToUserContact(UserContactEntity userContact) {
        return GetUserContactInfoResponse.newBuilder()
                .setSuccessResponse(GetUserContactInfoResponse.SuccessResponse.newBuilder()
                        .setUserContact(dev.crashteam.crm.UserContact.newBuilder()
                                .setEmail(userContact.getEmail() != null ? userContact.getEmail() : "")
                                .setPhone(userContact.getPhone())
                                .setInn(userContact.getInn() != null ? userContact.getInn().toString() : "")
                                .build())
                        .setUserContactInfoState(
                                userContact.isVerification() ? UpdateUserContactInfoState.UPDATE_USER_CONTACT_INFO_STATE_VERIFIED
                                        : UpdateUserContactInfoState.UPDATE_USER_CONTACT_INFO_STATE_NOT_VERIFIED
                        )
                        .build())
                .build();
    }

    public static UserContactEntity map(UpdateUserContactInfoRequest.InitialUpdateContactInfoPayload contactInfoPayload) {
        UserContactEntity userContact = new UserContactEntity();
        userContact.setEmail(contactInfoPayload.getEmail());
        userContact.setPhone(contactInfoPayload.getPhoneNumber());
        if (!contactInfoPayload.getInn().isBlank()) {
            userContact.setInn(Long.parseLong(contactInfoPayload.getInn()));
        }
        return userContact;
    }

    public static dev.crashteam.crm.UserContact map(UserContactEntity userContact) {
        return dev.crashteam.crm.UserContact.newBuilder()
                .setEmail(userContact.getEmail() != null ? userContact.getEmail() : "")
                .setPhone(userContact.getPhone())
                .setInn(userContact.getInn() != null ? userContact.getInn().toString() : "")
                .build();
    }
}
