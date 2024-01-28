package dev.crashteam.hermes.mapper;

import dev.crashteam.crm.CreateLeadRequest;
import dev.crashteam.crm.GetUserContactInfoResponse;
import dev.crashteam.crm.UpdateUserContactInfoRequest;
import dev.crashteam.crm.UserContact;
import dev.crashteam.hermes.model.domain.Contact;
import dev.crashteam.hermes.model.dto.contact.ContactResponse;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.model.dto.pipeline.PipelineStagesResponse;
import dev.crashteam.hermes.service.pipeline.PipelineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GrpcMapper {

    private final PipelineService pipelineService;

    public LeadRequest map(CreateLeadRequest request) {
        int pipelineId = pipelineService.getPipelines().getData().stream().findFirst().get().getId();
        Optional<PipelineStagesResponse.Stage> pipelineStage
                = pipelineService.getPipelineStagesResponse(pipelineId).getData().stream().findFirst();
        int stageId = Integer.MIN_VALUE;
        if (pipelineStage.isPresent()) {
            stageId = pipelineStage.get().getId();
        } else {
            log.error("stage_id not found");
        }

        LeadRequest.Contact contact
                = new LeadRequest.Contact(request.getUserIdentity().getFirstName(), String.valueOf(request.getUserPhoneNumber().getPhoneNumber()));

        return new LeadRequest(pipelineId, stageId, "Демо", contact);
    }

    public GetUserContactInfoResponse map(ContactResponse contact) {
        return GetUserContactInfoResponse.newBuilder()
                .setSuccessResponse(GetUserContactInfoResponse.SuccessResponse.newBuilder()
                        .setUserContact(UserContact.newBuilder()
                                .setEmail(contact.getEmail() != null ? contact.getEmail() : "")
                                .setPhone(contact.getPhone())
                                .setInn(contact.getInn() != null ? contact.getInn() : "")
                                .build())
                        .build())
                .build();
    }

    public Contact map(UpdateUserContactInfoRequest.InitialUpdateContactInfoPayload contactInfoPayload) {
        Contact contact = new Contact();
        contact.setEmail(contactInfoPayload.getEmail());
        contact.setPhone(contactInfoPayload.getPhoneNumber());
        if (!contactInfoPayload.getInn().isBlank()) {
            contact.setInn(Long.parseLong(contactInfoPayload.getInn()));
        }
        return contact;
    }

    public UserContact map(Contact contact) {
        return UserContact.newBuilder()
                .setEmail(contact.getEmail() != null ? contact.getEmail() : "")
                .setPhone(contact.getPhone())
                .setInn(contact.getInn() != null ? contact.getInn().toString() : "")
                .build();
    }

}
