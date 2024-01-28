package dev.crashteam.hermes.service.lead;

import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.service.contact.ContactService;
import dev.crashteam.hermes.service.crm.CrmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final CrmService crmService;
    private final ContactService contactService;

    @Override
    public void createLead(LeadRequest leadRequest) {
        Integer crmExternalId = contactService.createContact(List.of(leadRequest.getContact()));
        crmService.saveLead(leadRequest, crmExternalId);
        crmService.createLead(leadRequest);
    }

}
