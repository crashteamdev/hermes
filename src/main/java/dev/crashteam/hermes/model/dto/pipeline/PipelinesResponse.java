package dev.crashteam.hermes.model.dto.pipeline;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PipelinesResponse {

    private List<Pipeline> data;

    @Data
    public static class Pipeline {

        private int id;
        private String name;


    }

}
