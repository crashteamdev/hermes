package dev.crashteam.hermes.service.lead;

import dev.crashteam.hermes.component.OkoProperties;
import dev.crashteam.hermes.exception.pipeline.PipelineStageNotFound;
import dev.crashteam.hermes.model.domain.UserFeedbackEntity;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.model.dto.pipeline.PipelineStagesResponse;
import dev.crashteam.hermes.service.contact.ContactService;
import dev.crashteam.hermes.service.crm.CrmService;
import dev.crashteam.hermes.service.pipeline.PipelineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final CrmService crmService;
    private final ContactService contactService;
    private final PipelineService pipelineService;
    private final OkoProperties okoProperties;

    @Override
    public void createDemoLead(LeadRequest leadRequest) {
        int pipelineId = okoProperties.getPipelineId().getDemo();
        get(leadRequest, pipelineId);
        leadRequest.setName("Демо %s".formatted(leadRequest.getContact().getEmail()));

        LeadRequest.Contact contact = leadRequest.getContact();
        Integer crmExternalId = contactService.createContact(List.of(contact));
        crmService.createLead(leadRequest);
    }

    @Override
    public void createFeedbackLead(LeadRequest leadRequest) {
        int pipelineId = okoProperties.getPipelineId().getFeedback();
        get(leadRequest, pipelineId);
        leadRequest.setName("Обратная связь %s".formatted(leadRequest.getContact().getEmail()));

        LeadRequest.Contact contact = leadRequest.getContact();
        UserFeedbackEntity userFeedback = new UserFeedbackEntity();
        userFeedback.setFirstName(contact.getName());
        userFeedback.setPhone(Long.valueOf(contact.getPhone()));
        userFeedback.setEmail(contact.getEmail());

        crmService.saveFeedback(userFeedback);
        crmService.createLead(leadRequest);
    }

    @Override
    public void createServiceLead(LeadRequest leadRequest) {
        int pipelineId = okoProperties.getPipelineId().getService();
        get(leadRequest, pipelineId);
        leadRequest.setName(leadRequest.getName());

        crmService.createLead(leadRequest);
    }

    private void get(LeadRequest leadRequest, int pipelineId) {
        Optional<PipelineStagesResponse.Stage> pipelineStage
                = pipelineService.getPipelineStagesResponse(pipelineId).getData().stream().findFirst();
        int stageId;
        if (pipelineStage.isPresent()) {
            stageId = pipelineStage.get().getId();
            leadRequest.setStageId(stageId);
            leadRequest.setPipelineId(pipelineId);
            log.info("Received stage_id");
        } else {
            throw new PipelineStageNotFound("Stages for pipeline_id:[%s] not found".formatted(pipelineId));
        }
    }

}
