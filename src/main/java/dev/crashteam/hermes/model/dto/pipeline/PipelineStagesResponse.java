package dev.crashteam.hermes.model.dto.pipeline;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PipelineStagesResponse {

    private List<Stage> data;

    @Data
    public static class Stage {

        private int id;
        @JsonProperty("pipeline_id")
        private int pipelineId;
        private String name;
        private String color;

    }

}
