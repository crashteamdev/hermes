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

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeadService {

    private final CrmService crmService;
    private final ContactService contactService;
    private final PipelineService pipelineService;
    private final OkoProperties okoProperties;

    public void createLead(LeadRequest leadRequest) {
        crmService.createLead(leadRequest);
    }

    public void createDemoLead(LeadRequest leadRequest) {
        log.info("Lead: [{}]", leadRequest);
        int pipelineId = okoProperties.getPipelineId().getDemo();
        fillStageId(leadRequest, pipelineId);

        LeadRequest.Contact contact = leadRequest.getContact();
        contactService.createContact(contact);
        createLead(leadRequest);
    }

    public void createFeedbackLead(LeadRequest leadRequest) {
        int pipelineId = okoProperties.getPipelineId().getFeedback();
        fillStageId(leadRequest, pipelineId);

        LeadRequest.Contact contact = leadRequest.getContact();
        UserFeedbackEntity userFeedback = new UserFeedbackEntity();
        userFeedback.setFirstName(contact.getName());
        userFeedback.setPhone(Long.valueOf(contact.getPhone()));
        userFeedback.setEmail(contact.getEmail());

        crmService.saveFeedback(userFeedback);
        createLead(leadRequest);
    }

    public void createServiceLead(LeadRequest leadRequest) {
        int pipelineId = okoProperties.getPipelineId().getService();
        fillStageId(leadRequest, pipelineId);

        createLead(leadRequest);
    }

    private void fillStageId(LeadRequest leadRequest, int pipelineId) {
        Optional<PipelineStagesResponse.Stage> pipelineStage
                = pipelineService.getPipelineStagesResponse(pipelineId).getData().stream().findFirst();

        if (pipelineStage.isPresent()) {
            int stageId = pipelineStage.get().getId();
            leadRequest.setStageId(stageId);
            leadRequest.setPipelineId(pipelineId);
            log.info("Received stage_id");
        } else {
            throw new PipelineStageNotFound("Stages for pipeline_id:[%s] not found".formatted(pipelineId));
        }
    }

}
