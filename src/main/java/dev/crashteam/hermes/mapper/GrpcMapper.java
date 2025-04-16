package dev.crashteam.hermes.mapper;

import dev.crashteam.crm.*;
import dev.crashteam.hermes.model.UtmTag;
import dev.crashteam.hermes.model.domain.UserContactEntity;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class GrpcMapper {

    public static LeadRequest mapDemoLead(CreateLeadRequest.CreateDemoLead request, CreateLeadRequest.UtmTag grpcUtmTag) {
        String firstName = request.getUserIdentity().getFirstName();
        String phone = "+" + request.getUserPhoneNumber().getPhoneNumber();
        String userEmail = request.getUserEmail();

        String telegramUsername = request.getTelegramUsername();
        String leadName = "Демо | %s | %s".formatted(userEmail, telegramUsername);

        LeadRequest.Contact contact = new LeadRequest.Contact(firstName, phone, userEmail);

        if (grpcUtmTag != null) {
            UtmTag utmTag = CrmMapper.mapUtmToCrm(grpcUtmTag);
            return new LeadRequest(leadName, contact, utmTag);
        }

        return new LeadRequest(leadName, contact);
    }

    public static LeadRequest mapDemoLead(RequestDemoAccess request) {
        String firstName = request.getUserIdentity().getFirstName();
        String phone = "+" + request.getUserPhoneNumber().getPhoneNumber();
        String userEmail = request.getUserEmail();

        String telegramUsername = request.getTelegramUsername();
        String leadName = "[TG-BOT] Демо | %s | %s".formatted(userEmail, telegramUsername);

        LeadRequest.Contact contact = new LeadRequest.Contact(firstName, phone, userEmail);

        if (request.hasUtmTag()) {
            UtmTag utmTag = CrmMapper.mapUtmToCrm(request.getUtmTag());
            return new LeadRequest(leadName, contact, utmTag);
        }

        return new LeadRequest(leadName, contact);
    }

    public static LeadRequest mapServiceLead(CreateLeadRequest.CreateServiceLead request, CreateLeadRequest.UtmTag grpcUtmTag) {
        String firstName = request.getUserIdentity().getFirstName();
        String phone = "+" + request.getUserPhoneNumber().getPhoneNumber();
        String userEmail = request.getUserEmail();

        String telegramUsername = request.getTelegramUsername();
        String serviceName = "Сервис | '%s' | %s | %s".formatted(request.getServiceName(), userEmail, telegramUsername);

        LeadRequest.Contact contact = new LeadRequest.Contact(firstName, phone, userEmail);

        if (grpcUtmTag != null) {
            UtmTag utmTag = CrmMapper.mapUtmToCrm(grpcUtmTag);
            return new LeadRequest(serviceName, contact, utmTag);
        }

        return new LeadRequest(serviceName, contact);
    }

    public static LeadRequest mapFeedbackLead(CreateLeadRequest.CreateFeedbackLead request, CreateLeadRequest.UtmTag grpcUtmTag) {
        String firstName = request.getUserIdentity().getFirstName();
        String phone = "+" + request.getUserPhoneNumber().getPhoneNumber();
        String userEmail = request.getUserEmail();

        String telegramUsername = request.getTelegramUsername();
        String leadName = "Обратная связь | %s | %s".formatted(userEmail, telegramUsername);

        LeadRequest.Contact contact = new LeadRequest.Contact(firstName, phone, userEmail);

        if (grpcUtmTag != null) {
            UtmTag utmTag = CrmMapper.mapUtmToCrm(grpcUtmTag);
            return new LeadRequest(leadName, contact, utmTag);
        }

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
