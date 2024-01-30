package dev.crashteam.hermes.service.lead;

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

    @Override
    public void createLead(LeadRequest leadRequest) {
        int pipelineId = pipelineService.getPipelines().getData().stream().findFirst().get().getId();
        leadRequest.setPipelineId(pipelineId);

        Optional<PipelineStagesResponse.Stage> pipelineStage
                = pipelineService.getPipelineStagesResponse(pipelineId).getData().stream().findFirst();
        int stageId;
        if (pipelineStage.isPresent()) {
            stageId = pipelineStage.get().getId();
            leadRequest.setStageId(stageId);
        } else {
            log.error("stage_id not found");
        }

        Integer crmExternalId = contactService.createContact(List.of(leadRequest.getContact()));
        crmService.createLead(leadRequest, crmExternalId);
    }

}
