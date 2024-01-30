package dev.crashteam.hermes.service.pipeline;

import dev.crashteam.hermes.model.dto.pipeline.PipelineStagesResponse;
import dev.crashteam.hermes.model.dto.pipeline.PipelinesResponse;

public interface PipelineService {

    PipelinesResponse getPipelines();

    PipelineStagesResponse getPipelineStagesResponse(int pipelineId);

}
