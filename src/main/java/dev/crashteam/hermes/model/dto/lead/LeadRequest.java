package dev.crashteam.hermes.model.dto.lead;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.crashteam.hermes.model.UtmTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class LeadRequest {

    public LeadRequest(Contact contact) {
        this.contact = contact;
    }

    public LeadRequest(String name, Contact contact) {
        this.name = name;
        this.contact = contact;
    }

    public LeadRequest(String name, Contact contact, UtmTag utmTag) {
        this.name = name;
        this.contact = contact;

        this.utmSource = utmTag.getSource();
        this.utmMedium = utmTag.getMedium();
        this.utmCampaign = utmTag.getCampaign();
        this.utmContent = utmTag.getContent();
        this.utmTerm = utmTag.getTerm();
    }

    public LeadRequest(int pipelineId, String name, Contact contact) {
        this.pipelineId = pipelineId;
        this.name = name;
        this.contact = contact;
    }

    @JsonProperty("pipeline_id")
    private int pipelineId;
    @JsonProperty("stages_id")
    private int stageId;
    private String name;
    private Contact contact;

    @JsonProperty("utm_source")
    private String utmSource;
    @JsonProperty("utm_medium")
    private String utmMedium;
    @JsonProperty("utm_campaign")
    private String utmCampaign;
    @JsonProperty("utm_content")
    private String utmContent;
    @JsonProperty("utm_term")
    private String utmTerm;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Contact {

        private String name;
        private String phone;
        private String email;

    }
}
