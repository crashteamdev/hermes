package dev.crashteam.hermes.model.dto.lead;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeadResponse {

    @JsonProperty("lead_id")
    private int leadId;
    @JsonProperty("contact_id")
    private int contactId;

}
