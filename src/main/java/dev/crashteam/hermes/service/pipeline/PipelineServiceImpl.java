package dev.crashteam.hermes.service.pipeline;

import dev.crashteam.hermes.model.dto.pipeline.PipelineStagesResponse;
import dev.crashteam.hermes.model.dto.pipeline.PipelinesResponse;
import dev.crashteam.hermes.service.crm.CrmService;
import dev.crashteam.hermes.service.feign.OkoCrmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PipelineServiceImpl implements PipelineService {

    private final CrmService crmService;

    @Override
    public PipelinesResponse getPipelines() {
        return crmService.getPipelines();
    }

    @Override
    public PipelineStagesResponse getPipelineStagesResponse(int pipelineId) {
        return crmService.getPipelineStagesResponse(pipelineId);
    }

}
