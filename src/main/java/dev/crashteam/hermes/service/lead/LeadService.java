package dev.crashteam.hermes.service.lead;

import dev.crashteam.hermes.model.dto.lead.LeadRequest;

public interface LeadService {

    void createDemoLead(LeadRequest leadRequest);

    void createFeedbackLead(LeadRequest leadRequest);

    void createServiceLead(LeadRequest leadRequest);
}
