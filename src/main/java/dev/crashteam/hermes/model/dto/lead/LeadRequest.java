package dev.crashteam.hermes.model.dto.lead;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class LeadRequest {

    public LeadRequest(String name, Contact contact) {
        this.name = name;
        this.contact = contact;
    }

    @JsonProperty("pipeline_id")
    private int pipelineId;
    @JsonProperty("stages_id")
    private int stageId;
    private String name;
    private Contact contact;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Contact {

        private String name;
        private String phone;

    }
}
