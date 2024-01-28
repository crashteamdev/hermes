package dev.crashteam.hermes.service.feign;

import dev.crashteam.hermes.model.dto.contact.ContactRequest;
import dev.crashteam.hermes.model.dto.contact.ContactResp;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.model.dto.lead.LeadResponse;
import dev.crashteam.hermes.model.dto.pipeline.PipelineStagesResponse;
import dev.crashteam.hermes.model.dto.pipeline.PipelinesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(value = "OkoCrmClient", url = "${app.integration.oko-crm.url}")
public interface OkoCrmClient {

    @PostMapping("/contacts")
    ContactResp createContact(@RequestHeader Map<String, String> header, @RequestBody ContactRequest contact);

    @PostMapping("/leads")
    LeadResponse createLead(@RequestHeader Map<String, String> header, @RequestBody LeadRequest lead);

    @GetMapping("/pipelines")
    PipelinesResponse getPipelines(@RequestHeader Map<String, String> header);

    @GetMapping("/pipelines/stages/{pipelineId}")
    PipelineStagesResponse getPipelineStages(@RequestHeader Map<String, String> header, @PathVariable int pipelineId);

}
