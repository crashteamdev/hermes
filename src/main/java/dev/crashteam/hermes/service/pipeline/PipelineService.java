package dev.crashteam.hermes.service.pipeline;

import dev.crashteam.hermes.model.dto.pipeline.PipelineStagesResponse;
import dev.crashteam.hermes.service.crm.CrmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PipelineService {

    private final CrmService crmService;

    public PipelineStagesResponse getPipelineStagesResponse(int pipelineId) {
        return crmService.getPipelineStagesResponse(pipelineId);
    }

}
