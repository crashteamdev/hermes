package dev.crashteam.hermes.mapper;

import dev.crashteam.crm.CreateLeadRequest;
import dev.crashteam.crm.GetUserContactInfoResponse;
import dev.crashteam.crm.UpdateUserContactInfoRequest;
import dev.crashteam.crm.UpdateUserContactInfoState;
import dev.crashteam.crm.UserContact;
import dev.crashteam.hermes.model.domain.UserContactEntity;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class GrpcMapper {

    public static LeadRequest mapDemoLead(CreateLeadRequest.CreateDemoLead request) {
        String firstName = request.getUserIdentity().getFirstName();
        String phone = "+" + request.getUserPhoneNumber().getPhoneNumber();
        String userEmail = request.getUserEmail();
        String telegramUsername = request.getTelegramUsername();
        String leadName = "Демо | %s | %s".formatted(userEmail, telegramUsername);
        LeadRequest.Contact contact = new LeadRequest.Contact(firstName, phone, userEmail);
        return new LeadRequest(leadName, contact);
    }

    public static LeadRequest mapServiceLead(CreateLeadRequest.CreateServiceLead request) {
        String firstName = request.getUserIdentity().getFirstName();
        String phone = "+" + request.getUserPhoneNumber().getPhoneNumber();
        String userEmail = request.getUserEmail();
        String telegramUsername = request.getTelegramUsername();
        String serviceName = "Сервис | '%s' | %s | %s".formatted(request.getServiceName(), userEmail, telegramUsername);
        LeadRequest.Contact contact = new LeadRequest.Contact(firstName, phone, userEmail);
        return new LeadRequest(serviceName, contact);
    }

    public static LeadRequest mapFeedbackLead(CreateLeadRequest.CreateFeedbackLead request) {
        String firstName = request.getUserIdentity().getFirstName();
        String phone = "+" + request.getUserPhoneNumber().getPhoneNumber();
        String userEmail = request.getUserEmail();
        String telegramUsername = request.getTelegramUsername();
        String leadName = "Обратная связь | %s | %s".formatted(userEmail, telegramUsername);
        LeadRequest.Contact contact = new LeadRequest.Contact(firstName, phone, userEmail);
        return new LeadRequest(leadName, contact);
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

    public static UserContact map(UserContactEntity userContact) {
        return UserContact.newBuilder()
                .setEmail(userContact.getEmail() != null ? userContact.getEmail() : "")
                .setPhone(userContact.getPhone())
                .setInn(userContact.getInn() != null ? userContact.getInn().toString() : "")
                .build();
    }

}
