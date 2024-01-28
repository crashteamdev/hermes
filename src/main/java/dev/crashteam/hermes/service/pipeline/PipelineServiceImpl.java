package dev.crashteam.hermes.service.pipeline;

import dev.crashteam.hermes.model.dto.pipeline.PipelineStagesResponse;
import dev.crashteam.hermes.model.dto.pipeline.PipelinesResponse;
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

    private final OkoCrmClient okoCrmClient;

    public static final Map<String, String> HEADERS = Map.of(
            "Accept", MediaType.APPLICATION_JSON_VALUE,
            "Authorization", "Bearer 0510599d43fb005883494861667b8c8c:ea6cb0d691bf40cc82f976fe8d761efe"
    );

    @Override
    public PipelinesResponse getPipelines() {
        return okoCrmClient.getPipelines(HEADERS);
    }

    @Override
    public PipelineStagesResponse getPipelineStagesResponse(int pipelineId) {
        return okoCrmClient.getPipelineStages(HEADERS, pipelineId);
    }

}
