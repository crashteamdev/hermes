package dev.crashteam.hermes.service.lead;

import dev.crashteam.hermes.model.dto.lead.LeadRequest;

public interface LeadService {

    void createLead(LeadRequest leadRequest);

}
