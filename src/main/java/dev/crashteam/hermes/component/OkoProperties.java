package dev.crashteam.hermes.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.integration.oko-crm")
public class OkoProperties {

    private String url;
    private String token;
    private PipelineId pipelineId;

    @Data
    @ConfigurationProperties(prefix = "app.integration.oko-crm.pipeline-id")
    public static class PipelineId {

        private Integer demo;
        private Integer feedback;
        private Integer service;

    }

}
